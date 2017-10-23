package org.slizaa.neo4j.hierarchicalgraph.ui.mapping.functions;

import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.Function;

public interface IFunctionService {

  <T> T evaluate(HGNode node, Function function, Class<T> returnType);

}
