package org.slizaa.neo4j.hierarchicalgraph;

import static org.slizaa.neo4j.hierarchicalgraph.fwk.TestModelFactory.createGraphFromDefaultMapping;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.HGAggregatedDependency;
import org.slizaa.hierarchicalgraph.HGCoreDependency;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HGProxyDependency;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.spi.IProxyDependencyResolver;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ResolveAggregatedDependenciesCallback_Test {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(
      TestDB.MAPSTRUCT);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);
  
  /** - */
  private HGRootNode               _rootNode;

  /** - */
  private IProxyDependencyResolver _aggregatedDependencyResolver;

  /**
   * {@inheritDoc}
   */
  @Before
  public void init() throws Exception {

    //
    _rootNode = createGraphFromDefaultMapping(_boltClientConnection.getBoltClient());

    //
    _aggregatedDependencyResolver = mock(IProxyDependencyResolver.class);

    //
    _rootNode.registerExtension(IProxyDependencyResolver.class, _aggregatedDependencyResolver);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testResolveProxyDependencies() {

    //
    HGNode pkg_omaiconversion = _rootNode.lookupNode(new Long(6295));
    HGNode pkg_omaimodelcommon = _rootNode.lookupNode(new Long(2397));

    //
    HGAggregatedDependency aggregatedDependency = pkg_omaiconversion.getOutgoingDependenciesTo(pkg_omaimodelcommon);
    assertThat(aggregatedDependency).isNotNull();
    assertThat(aggregatedDependency.getAggregatedWeight()).isEqualTo(59);
    assertThat(aggregatedDependency.getCoreDependencies().size()).isEqualTo(59);

    // resolve the dependency
    aggregatedDependency.resolveProxyDependencies();
    assertThat(aggregatedDependency.getCoreDependencies().size()).isEqualTo(59);

    //
    for (HGCoreDependency dependency : aggregatedDependency.getCoreDependencies()) {
      if (dependency instanceof HGProxyDependency) {
        verify(_aggregatedDependencyResolver).resolveProxyDependency((HGProxyDependency) dependency);
      }
    }

    //
    assertThat(aggregatedDependency.getAggregatedWeight()).isEqualTo(59);
    assertThat(aggregatedDependency.getCoreDependencies().size()).isEqualTo(59);
  }
}
