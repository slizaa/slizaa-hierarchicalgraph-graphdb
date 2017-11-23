package org.slizaa.neo4j.hierarchicalgraph.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultLabelDefinition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;

public class SimpleJTypeLabelProvider implements ILabelDefinitionProvider {

  @Override
  public ILabelDefinition getLabelDefinition(HGNode node) {

    DefaultLabelDefinition defaultLabelDefinition = new DefaultLabelDefinition();

    //
    URL url = this.getClass().getClassLoader().getResource("org/slizaa/neo4j/hierarchicalgraph/ui/icons/HGNode.png");

    checkNotNull(url);

    defaultLabelDefinition.setBaseImage(url);
    defaultLabelDefinition.setText(nodeSource(node).getProperties().get("fqn"));

    return defaultLabelDefinition;
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  private Neo4JBackedNodeSource nodeSource(HGNode node) {
    return node.getNodeSource(Neo4JBackedNodeSource.class).get();
  }
}
