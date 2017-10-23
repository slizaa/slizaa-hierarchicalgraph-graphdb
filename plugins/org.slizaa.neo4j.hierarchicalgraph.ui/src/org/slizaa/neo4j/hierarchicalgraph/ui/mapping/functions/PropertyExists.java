package org.slizaa.neo4j.hierarchicalgraph.ui.mapping.functions;

import java.util.Optional;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;

public class PropertyExists {

  public boolean propertyExists(HGNode node, String name) {
    Optional<Neo4JBackedNodeSource> nodeSource = node.getNodeSource(Neo4JBackedNodeSource.class);
    return nodeSource.isPresent() && nodeSource.get().getProperties().containsKey(name);
  }
}
