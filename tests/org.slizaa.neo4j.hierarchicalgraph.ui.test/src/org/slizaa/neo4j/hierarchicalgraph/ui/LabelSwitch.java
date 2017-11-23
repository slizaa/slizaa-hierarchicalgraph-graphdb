package org.slizaa.neo4j.hierarchicalgraph.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.Consumer;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultLabelDefinition;

public class LabelSwitch {

  /** - */
  private HGNode _hgNode;

  /**
   * <p>
   * Creates a new instance of type {@link LabelSwitch}.
   * </p>
   *
   * @param hgNode
   */
  public LabelSwitch(HGNode hgNode) {
    _hgNode = checkNotNull(hgNode);
  }

  public LabelSwitch onLabel(String label, Consumer<DefaultLabelDefinition> labelDefinition) {
    return this;
  }
}
