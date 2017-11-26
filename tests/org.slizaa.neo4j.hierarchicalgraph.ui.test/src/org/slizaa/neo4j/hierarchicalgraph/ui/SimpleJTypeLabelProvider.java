package org.slizaa.neo4j.hierarchicalgraph.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultLabelDefinition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class SimpleJTypeLabelProvider extends AbstractDslLabelDefinitonProvider implements ILabelDefinitionProvider {

  /** - */
  private ThreadLocal<HGNode>                 _currentHgNode;

  /** - */
  private ThreadLocal<DefaultLabelDefinition> _currentLabelDefinition;

  /**
   * {@inheritDoc}
   */
  @Override
  public ILabelDefinition getLabelDefinition(HGNode n) {

    _currentHgNode.set(n);

    //
    return Dsl.
        when(containsLabel("Schnurz")).
            then((node, label) -> label.setBaseImage(icon("icons/Schnurz.png"))).

        //
        choice().

            //
            when(containsLabel("Type")).
                then((node, label) -> label.setBaseImage(icon("icons/class_obj.png"))).

            //
            when(containsLabel("Schnurz")).
                then((node, label) -> label.setBaseImage(icon("icons/Schnurz.png"))).

            //
            otherwise((node, label) -> label.setBaseImage(icon("icons/Schnurz.png"))).
        
            build();

    // dsl().exclusiveChoice()
    // .when(n -> nodeSource(n).getLabels().contains("Type"), dl -> dl.setBaseImage(icon("icons/class_obj.png"))).
    //
    // // handle type
    // when(containsLabel("Type")).then(dl -> dl.setBaseImage(icon("icons/class_obj.png"))).
    //
    // // handle
    // when(containsLabel("Schnurz"), setBaseImage(icon("icons/class_obj.png"))).
    //
    // // handle bla
    // when(containsLabel("Blah"), setBaseImage(icon("icons/class_obj.png"))).
    //
    // // handle type
    // when(containsLabel("Type"), when(contains())).
    //
    // //
    // endExclusiveChoice().when();

    // //
    // DefaultLabelDefinition defaultLabelDefinition = new DefaultLabelDefinition();
    //
    // //
    // defaultLabelDefinition.setBaseImage());
    // defaultLabelDefinition.setText(property(node, "fqn"));

    //
    ILabelDefinition result = _currentLabelDefinition.get();

    //
    _currentHgNode.set(null);
    _currentLabelDefinition.set(null);

    //
    return result;
  }

  private Rule when(Function<HGNode, Boolean> containsLabel) {
    // TODO Auto-generated method stub
    return null;
  }

  private Dsl dsl() {

    // TODO Auto-generated method stub
    return null;
  }

  private Function<HGNode, Boolean> containsLabel(String string) {
    // TODO Auto-generated method stub
    return null;
  }

  // private Rule when(Function<HGNode, Boolean> condition) {
  //
  // return null;
  // }

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
