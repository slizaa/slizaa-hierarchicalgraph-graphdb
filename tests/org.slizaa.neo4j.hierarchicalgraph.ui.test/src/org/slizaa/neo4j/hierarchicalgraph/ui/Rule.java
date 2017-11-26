package org.slizaa.neo4j.hierarchicalgraph.ui;

import java.util.function.BiConsumer;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultLabelDefinition;

public interface Rule {

   Dsl then(BiConsumer<HGNode, DefaultLabelDefinition> labelProvider);
}
