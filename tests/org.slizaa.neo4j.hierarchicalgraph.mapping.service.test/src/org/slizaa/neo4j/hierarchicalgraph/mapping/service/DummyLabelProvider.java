package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;

public class DummyLabelProvider implements ILabelDefinitionProvider {

  @Override
  public ILabelDefinition getLabelDefinition(HGNode node) {
    return null;
  }
}
