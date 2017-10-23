package org.slizaa.neo4j.hierarchicalgraph.ui.mapping.functions;

import java.util.Optional;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;

public class PropertyHasValue {

  public boolean propertyHasValue(HGNode node, String propertyKey, String propertyValue) {
    Optional<Neo4JBackedNodeSource> nodeSource = node.getNodeSource(Neo4JBackedNodeSource.class);
    return nodeSource.isPresent() && nodeSource.get().getProperties().containsKey(propertyKey) && nodeSource.get().getProperties().get(propertyKey).equals(propertyValue);
  }
}
