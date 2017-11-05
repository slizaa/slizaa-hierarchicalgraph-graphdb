package org.slizaa.neo4j.hierarchicalgraph;

import org.eclipse.emf.common.util.EMap;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;
import org.slizaa.neo4j.hierarchicalgraph.fwk.TestModelFactory;

public class MappingTest {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /** - */
  private HGRootNode                        _rootNode;

  /**
   * {@inheritDoc}
   */
  @Before
  public void init() throws Exception {

    //
    _rootNode = TestModelFactory.createGraphFromDefaultMapping(_boltClientConnection.getBoltClient());
  }

  @Test
  public void testMapping() {

    System.out.println(_rootNode);
    _rootNode.getChildren().forEach(c1 -> {
      System.out.println(" - " + fqn(c1));
      c1.getChildren().forEach(c2 -> {
        System.out.println("   -- " + fqn(c2));
        c2.getChildren().forEach(c3 -> {
          System.out.println("     --- " + fqn(c3));
          c3.getChildren().forEach(c4 -> {
            System.out.println(" -" + fqn(c4));
            // c4.getChildren().forEach(c5 -> {
            // System.out.println(" -" + fqn(c5));
            // });
          });
        });
      });
    });
  }

  private static String fqn(HGNode hgNode) {
    EMap<String, String> properties = ((Neo4JBackedNodeSource) hgNode.getNodeSource()).getProperties();
    return properties.get("fqn");
  }
}
