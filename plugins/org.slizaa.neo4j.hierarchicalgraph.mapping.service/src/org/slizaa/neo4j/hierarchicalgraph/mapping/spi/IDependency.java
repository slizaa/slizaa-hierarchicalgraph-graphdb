package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

public interface IDependency {

  boolean isProxyDependency();
  
  boolean isSimpleDependency();
}
