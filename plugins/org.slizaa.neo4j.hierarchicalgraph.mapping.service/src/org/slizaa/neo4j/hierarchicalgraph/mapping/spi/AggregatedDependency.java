package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.util.List;
import java.util.function.Function;

public class AggregatedDependency implements IDependency {

  
  
  public Function<List<>, R> getResolveFunction();
}