package org.slizaa.neo4j.hierarchicalgraph.ui.mapping.functions;

import java.util.Optional;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;

public class HasLabel {

  public boolean hasLabel(HGNode node, String label) {
    Optional<Neo4JBackedNodeSource> nodeSource = node.getNodeSource(Neo4JBackedNodeSource.class);
    return nodeSource.isPresent() && nodeSource.get().getLabels().contains(label);
  }
}
