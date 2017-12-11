package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

public abstract class AbstractDependency {

  public abstract boolean isProxyDependency();

  public abstract boolean isSimpleDependency();
}
