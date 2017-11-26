package org.slizaa.neo4j.hierarchicalgraph.ui.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.Consumer;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultLabelDefinition;

public class LabelDefinitionBuilder {



  public LabelDefinitionBuilder onLabel(String label, Consumer<DefaultLabelDefinition> labelDefinition) {
    return this;
  }

  public LabelDefinitionBuilder choice() {
    // TODO Auto-generated method stub
    return null;
  }
  
  
}
