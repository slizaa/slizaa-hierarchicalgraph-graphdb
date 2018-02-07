package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider.ILabelDefinition;

public class DummyLabelProvider implements ILabelDefinitionProvider {

  @Override
  public ILabelDefinition getLabelDefinition(HGNode node) {
    return null;
  }
}
