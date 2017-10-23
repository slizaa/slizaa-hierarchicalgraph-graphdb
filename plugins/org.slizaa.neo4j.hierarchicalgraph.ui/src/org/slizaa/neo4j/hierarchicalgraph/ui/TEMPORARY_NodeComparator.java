package org.slizaa.neo4j.hierarchicalgraph.ui;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.spi.INodeComparator;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TEMPORARY_NodeComparator implements INodeComparator {

  /**
   * {@inheritDoc}
   */
  @Override
  public int category(Object element) {

    //
    if (element instanceof HGNode) {
      HGNode hgNode = (HGNode) element;
      Neo4JBackedNodeSource nodeSource = (Neo4JBackedNodeSource) hgNode.getNodeSource();

      if (nodeSource.getLabels().contains("FIELD")) {
        return 1;
      } else if (nodeSource.getLabels().contains("METHOD")) {
        return 2;
      } else if (nodeSource.getLabels().contains("DIRECTORY")) {
        return 1;
      } else if (nodeSource.getLabels().contains("PACKAGE")) {
        return 2;
      } else if (nodeSource.getLabels().contains("RESOURCE")) {
        return 3;
      } else if (nodeSource.getLabels().contains("TYPE")) {
        return 4;
      }
    }

    //
    return 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(Object e1, Object e2) {

    //
    if (!(e1 instanceof HGNode && e2 instanceof HGNode)) {
      return 0;
    }

    //
    Neo4JBackedNodeSource nodeSource1 = (Neo4JBackedNodeSource) ((HGNode) e1).getNodeSource();
    Neo4JBackedNodeSource nodeSource2 = (Neo4JBackedNodeSource) ((HGNode) e2).getNodeSource();

    //
    if ((nodeSource1.getLabels().contains("FIELD") && nodeSource2.getLabels().contains("FIELD"))
        || (nodeSource1.getLabels().contains("METHOD") && nodeSource2.getLabels().contains("METHOD"))
        || (nodeSource1.getLabels().contains("TYPE") && nodeSource2.getLabels().contains("TYPE"))) {

      return nodeSource1.getProperties().get("name").compareTo(nodeSource2.getProperties().get("name"));
    }
    //
    else if (((nodeSource1.getLabels().contains("DIRECTORY") && nodeSource2.getLabels().contains("DIRECTORY"))
        || (nodeSource1.getLabels().contains("RESOURCE") && nodeSource2.getLabels().contains("RESOURCE")))
        && nodeSource1.getProperties().containsKey("fqn") && nodeSource2.getProperties().containsKey("fqn")) {
      return nodeSource1.getProperties().get("fqn").compareTo(nodeSource2.getProperties().get("fqn"));
    }
    //
    else if (((nodeSource1.getLabels().contains("MODULE") && nodeSource2.getLabels().contains("MODULE")))
        && nodeSource1.getProperties().containsKey("name") && nodeSource2.getProperties().containsKey("name")) {
      return nodeSource1.getProperties().get("name").compareTo(nodeSource2.getProperties().get("name"));
    }

    //
    return -1;
  }
}
