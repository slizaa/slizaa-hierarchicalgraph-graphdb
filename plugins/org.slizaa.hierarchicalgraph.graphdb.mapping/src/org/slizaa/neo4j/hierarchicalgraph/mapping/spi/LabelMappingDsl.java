package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinition.OverlayPosition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.LabelMappingDsl.LabelDefinitionProcessor;

public class LabelMappingDsl {

  @FunctionalInterface
  public static interface LabelDefinitionProcessor {

    /**
     * <p>
     * </p>
     *
     * @param hgNode
     * @param labelDefinition
     */
    public void processLabelDefinition(HGNode hgNode, DefaultLabelDefinition labelDefinition);

    default LabelDefinitionProcessor and(LabelDefinitionProcessor next) {
      Objects.requireNonNull(next);
      return (n, l) -> {
        processLabelDefinition(n, l);
        next.processLabelDefinition(n, l);
      };
    }
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class LabelMappingConditionBuilder {

    /** - */
    private Function<HGNode, Boolean> _condition;

    /**
     * <p>
     * Creates a new instance of type {@link LabelMappingConditionBuilder}.
     * </p>
     *
     * @param condition
     */
    public LabelMappingConditionBuilder(Function<HGNode, Boolean> condition) {
      _condition = checkNotNull(condition);
    }

    /**
     * <p>
     * </p>
     *
     * @param processor
     * @return
     */
    public LabelDefinitionProcessor then(LabelDefinitionProcessor processor) {

      //
      checkNotNull(processor);

      //
      return (n, l) -> {
        if (_condition.apply(n)) {
          processor.processLabelDefinition(n, l);
        }
      };
    }
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   *
   */
  public class ExclusiveChoiceBuilder {

    /** - */
    private List<ExclusiveChoiceAlternative> _alternatives = new ArrayList<>();

    /**
     * <p>
     * </p>
     *
     * @param condition
     * @return
     */
    public ExclusiveChoiceAlternative when(Function<HGNode, Boolean> condition) {

      ExclusiveChoiceAlternative builder = new ExclusiveChoiceAlternative(this, condition);
      _alternatives.add(builder);
      return builder;
    }

    /**
     * <p>
     * </p>
     *
     * @param nothing
     * @return
     */
    public LabelDefinitionProcessor otherwise(LabelDefinitionProcessor processor) {

      //
      return (n, l) -> {

        //
        for (ExclusiveChoiceAlternative alternative : _alternatives) {
          if (alternative.getCondition().apply(n)) {
            alternative.getProcessor().processLabelDefinition(n, l);
            return;
          }
        }

        //
        processor.processLabelDefinition(n, l);
      };
    }
  }

  public static class ExclusiveChoiceAlternative {

    /** - */
    private ExclusiveChoiceBuilder    _parent;

    /** - */
    private Function<HGNode, Boolean> _condition;

    /** - */
    private LabelDefinitionProcessor  _processor;

    /**
     * <p>
     * Creates a new instance of type {@link LabelMappingExclusiveChoiceAlternative}.
     * </p>
     *
     * @param parent
     * @param condition
     */
    public ExclusiveChoiceAlternative(ExclusiveChoiceBuilder parent, Function<HGNode, Boolean> condition) {

      //
      _parent = checkNotNull(parent);
      _condition = checkNotNull(condition);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public Function<HGNode, Boolean> getCondition() {
      return _condition;
    }

    public LabelDefinitionProcessor getProcessor() {
      return _processor;
    }

    /**
     * <p>
     * </p>
     *
     * @param processor
     * @return
     */
    public ExclusiveChoiceBuilder then(LabelDefinitionProcessor processor) {
      _processor = checkNotNull(processor);
      return _parent;
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param imageUrl
   * @return
   */
  public LabelDefinitionProcessor setBaseImage(URL imageUrl) {
    return (node, labeldefinition) -> labeldefinition.setBaseImage(checkNotNull(imageUrl));
  }

  /**
   * <p>
   * </p>
   *
   * @param imageUrl
   * @param overlayPosition
   * @return
   */
  public LabelDefinitionProcessor setOverlayImage(URL imageUrl, OverlayPosition overlayPosition) {
    return (node, labeldefinition) -> labeldefinition.setOverlayImage(checkNotNull(imageUrl),
        checkNotNull(overlayPosition));
  }

  public LabelDefinitionProcessor setText(String textLabel) {
    return (node, labeldefinition) -> labeldefinition.setText(textLabel);
  }

  /**
   * <p>
   * </p>
   *
   * @param string
   * @return
   */
  public Function<HGNode, Boolean> nodeHasLabel(String string) {
    return (node) -> nodeSource(node).getLabels().contains(string);
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  public Function<HGNode, Boolean> nodeHasProperty(String propertyName) {
    checkNotNull(propertyName);
    return (node) -> nodeSource(node).getProperties().containsKey(propertyName);
  }

  /**
   * <p>
   * </p>
   *
   * @param path
   * @return
   */
  public URL fromClasspath(String path) {
    URL url = this.getClass().getClassLoader().getResource(checkNotNull(path));
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
  public Neo4JBackedNodeSource nodeSource(HGNode node) {
    return checkNotNull(node).getNodeSource(Neo4JBackedNodeSource.class).get();
  }

  /**
   * <p>
   * </p>
   *
   * @param containsLabel
   * @return
   */
  public LabelMappingConditionBuilder when(Function<HGNode, Boolean> containsLabel) {
    return new LabelMappingConditionBuilder(checkNotNull(containsLabel));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public LabelDefinitionProcessor doNothing() {
    return (n, l) -> {
    };
  }

  /**
   * <p>
   * </p>
   *
   * @param consumers
   * @return
   */
  public ExclusiveChoiceBuilder exclusiveChoice() {
    return new ExclusiveChoiceBuilder();
  }

  /**
   * <p>
   * </p>
   *
   * @param processors
   * @return
   */
  public LabelDefinitionProcessor executeAll(LabelDefinitionProcessor... processors) {
    checkNotNull(processors);

    //
    return (n, l) -> {
      for (LabelDefinitionProcessor labelDefinitionProcessor : processors) {
        labelDefinitionProcessor.processLabelDefinition(n, l);
      }
    };
  }
  
  /**
   * <p>
   * </p>
   *
   * @param textFunction
   * @return
   */
  protected LabelDefinitionProcessor setLabelText(Function<HGNode, String> textMapper) {
    return (n, l) -> l.setText(textMapper.apply(n));
  }

  /**
   * <p>
   * </p>
   *
   * @param key
   * @return
   */
  protected Function<HGNode, String> propertyValue(String key) {
    checkNotNull(key);
    return n -> nodeSource(n).getProperties().get(key);
  }

  /**
   * <p>
   * </p>
   *
   * @param key
   * @param formatter
   * @return
   */
  protected Function<HGNode, String> propertyValue(String key, Function<String, String> formatter) {
    checkNotNull(key);
    checkNotNull(formatter);
    return propertyValue(key).andThen(formatter);
  }
}
