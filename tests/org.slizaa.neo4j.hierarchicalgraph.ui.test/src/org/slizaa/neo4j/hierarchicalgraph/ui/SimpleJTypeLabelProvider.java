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

    //
    DefaultLabelDefinition defaultLabelDefinition = new DefaultLabelDefinition();

    //
    defaultLabelDefinition.setBaseImage(icon("icons/class_obj.png"));
    defaultLabelDefinition.setText(property(node, "fqn"));

    //
    return defaultLabelDefinition;
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  private String property(HGNode node, String propertyName) {
    return nodeSource(node).getProperties().get(propertyName);
  }

  /**
   * <p>
   * </p>
   *
   * @param path
   * @return
   */
  private URL icon(String path) {
    URL url = this.getClass().getClassLoader().getResource("icons/class_obj.png");
    checkNotNull(url);
    return url;
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
