package org.slizaa.neo4j.hierarchicalgraph.ui;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultLabelDefinition;

public class Dsl {

  public static Rule when(Function<HGNode, Boolean> containsLabel) {
    
    return null;
  }

  Dsl choice();

  void otherwise(BiConsumer<HGNode, DefaultLabelDefinition> labelProvider);
}
