package org.slizaa.neo4j.hierarchicalgraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slizaa.hierarchicalgraph.HierarchicalgraphFactoryFunctions.createNewNode;
import static org.slizaa.hierarchicalgraph.HierarchicalgraphFactoryFunctions.createNewRootNode;

import java.util.concurrent.ExecutionException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExtendedNeo4JBackedNodeSource_Test {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(
      TestDB.MAPSTRUCT);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /** - */
  private HGRootNode                        _rootNode;

  /** - */
  private HGNode                            _node;

  @Before
  public void init() throws Exception {

    //
    _rootNode = createNewRootNode(() -> {

      // create the
      Neo4JBackedRootNodeSource result = Neo4jHierarchicalgraphFactory.eINSTANCE.createNeo4JBackedRootNodeSource();

      // set the repository
      result.setRepository(_boltClientConnection.getBoltClient());

      // return the result
      return result;
    });

    //
    _node = createNewNode(_rootNode, _rootNode, () -> {

      // create the
      Neo4JBackedNodeSource nodeSource = Neo4jHierarchicalgraphFactory.eINSTANCE.createNeo4JBackedNodeSource();

      // set the repository
      nodeSource.setIdentifier(7722l);

      // return the result
      return nodeSource;
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Test
  public void testGetProperties() throws InterruptedException, ExecutionException {

    //
    EMap<String, String> properties = ((Neo4JBackedNodeSource) _node.getNodeSource()).getProperties();

    assertThat(properties).isNotNull();
    assertThat(properties).hasSize(9);
    assertThat(properties.get("synthetic")).isEqualTo("false");
    assertThat(properties.get("fqn")).isEqualTo("void closeTemplateSource(java.lang.Object)");
    assertThat(properties.get("accessLevel")).isEqualTo("PUBLIC");
    assertThat(properties.get("name")).isEqualTo("closeTemplateSource");
    // assertThat(properties.get("cyclomaticComplexity")).isEqualTo("1");
    // TODO
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testGetLabels() {

    //
    EList<String> labels = ((Neo4JBackedNodeSource) _node.getNodeSource()).getLabels();

    assertThat(labels).isNotNull();
    assertThat(labels).hasSize(1);
    assertThat(labels).contains("METHOD");
  }
}
