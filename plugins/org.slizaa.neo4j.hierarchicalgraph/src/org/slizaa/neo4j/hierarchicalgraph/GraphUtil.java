package org.slizaa.neo4j.hierarchicalgraph;

import org.eclipse.emf.common.util.EMap;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HGRootNode;

public class GraphUtil {

  public static void dump(HGRootNode rootNode) {

    System.out.println(rootNode);
    rootNode.getChildren().forEach(c1 -> {
      System.out.println(" - " + fqn(c1));
      c1.getChildren().forEach(c2 -> {
        System.out.println("   -- " + fqn(c2));
        c2.getChildren().forEach(c3 -> {
          System.out.println("     --- " + fqn(c3));
          c3.getChildren().forEach(c4 -> {
            System.out.println("       ----- " + fqn(c4));
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
