package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.hierarchicalgraph.HGNode;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractLabelDefinitionProvider extends LabelMappingDsl implements ILabelDefinitionProvider {

  /** - */
  private LabelDefinitionProcessor _processor;

  /**
   * {@inheritDoc}
   */
  @Override
  public final ILabelDefinition getLabelDefinition(HGNode node) {

    //
    DefaultLabelDefinition defaultLabelDefinition = new DefaultLabelDefinition();

    //
    processor().processLabelDefinition(node, defaultLabelDefinition);

    //
    return defaultLabelDefinition;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected abstract LabelDefinitionProcessor createLabelDefinitionProcessor();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private LabelDefinitionProcessor processor() {

    //
    if (_processor == null) {
      _processor = createLabelDefinitionProcessor();
      checkNotNull(_processor);
    }

    //
    return _processor;
  }
}
