package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import org.slizaa.hierarchicalgraph.HGNode;

public interface ILabelProvider {

  /**
   * <p>
   * </p>
   */
  ILabelDefinition getLabelDefinition(HGNode node);
}
