package org.slizaa.neo4j.hierarchicalgraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slizaa.neo4j.hierarchicalgraph.fwk.TestModelFactory.createGraphFromDefaultMapping;

import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.HGAggregatedDependency;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.spi.IProxyDependencyResolver;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.StatementResultUtil;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ResolveAggregatedDependenciesResolver_Test {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(
      TestDB.MAPSTRUCT);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /** - */
  private HGRootNode                        _rootNode;

  /** - */
  private IProxyDependencyResolver          _aggregatedDependencyResolver;

  /**
   * {@inheritDoc}
   */
  @Before
  public void init() throws Exception {

    //
    _rootNode = createGraphFromDefaultMapping(_boltClientConnection.getBoltClient());

    //
    _aggregatedDependencyResolver = null;

    //
    _rootNode.registerExtension(IProxyDependencyResolver.class, _aggregatedDependencyResolver);
  }

  /**
   * <p>
   * </p>
   * 
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Test
  public void testResolveProxyDependencies() throws InterruptedException, ExecutionException {

    _boltClientConnection.getBoltClient().executeCypherQuery("MATCH (d:DIRECTORY) RETURN d",
        statementResult -> StatementResultUtil.dumpStatement(statementResult)).get();

    //
    HGNode pkg_omaiconversion = _rootNode.lookupNode(new Long(6295));
    HGNode pkg_omaimodelcommon = _rootNode.lookupNode(new Long(2397));

    assertThat(pkg_omaiconversion).isNotNull();
    assertThat(pkg_omaimodelcommon).isNotNull();

    //
    HGAggregatedDependency hgDependency = pkg_omaiconversion.getOutgoingDependenciesTo(pkg_omaimodelcommon);
    assertThat(hgDependency).isNotNull();
    assertThat(hgDependency.getAggregatedWeight()).isEqualTo(59);
    assertThat(hgDependency.getCoreDependencies().size()).isEqualTo(59);

    // resolve the dependency
    hgDependency.resolveProxyDependencies();

    //
    assertThat(hgDependency.getAggregatedWeight()).isEqualTo(59);
    assertThat(hgDependency.getCoreDependencies().size()).isEqualTo(59);
  }
}
