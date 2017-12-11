package org.slizaa.neo4j.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;
import org.slizaa.neo4j.hierarchicalgraph.fwk.NodeIdFinder;

import com.google.common.collect.Lists;

/**
 * <p>
 * https://github.com/lukas-krecan/JsonUnit
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@RunWith(value = Parameterized.class)
public class ExtendedNeo4JRemoteRepository_GetNodeLabels_Test {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /** - */
  private Function<Neo4jClient, Long>       _nodeIdProvider;

  /** - */
  private List<String>                      _expectedLabels;

  /**
   * <p>
   * Creates a new instance of type {@link ExtendedNeo4JRemoteRepository_GetNodeLabels_Test}.
   * </p>
   *
   * @param nodeIdProvider
   * @param expectedLabels
   */
  public ExtendedNeo4JRemoteRepository_GetNodeLabels_Test(Function<Neo4jClient, Long> nodeIdProvider,
      List<String> expectedLabels) {
    this._nodeIdProvider = checkNotNull(nodeIdProvider);
    this._expectedLabels = checkNotNull(expectedLabels);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void getNodeProperties() {

    Neo4jClient boltClient = _boltClientConnection.getBoltClient();

    List<String> labels = Lists.newArrayList(boltClient.getNode(_nodeIdProvider.apply(boltClient)).labels());
    assertThat(labels).containsExactlyElementsOf(_expectedLabels);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Parameters(name = "{index}: getNodeLabels({0}) = {1}")
  public static Collection<Object[]> data() {

    Function<Neo4jClient, Long> f1 = c -> NodeIdFinder.getAssignmentClassFile(c);
    Function<Neo4jClient, Long> f2 = c -> NodeIdFinder.getDoGetMapperMethod(c);
    Function<Neo4jClient, Long> f3 = c -> NodeIdFinder.getSetterWrapperForCollectionsAndMapsWithNullCheckType(c);

    return Arrays.asList(new Object[][] { { f1, Arrays.asList("Resource", "Binary", "ClassFile") },
        { f2, Arrays.asList("Method") }, { f3, Arrays.asList("Type", "Class") } });
  }
}
