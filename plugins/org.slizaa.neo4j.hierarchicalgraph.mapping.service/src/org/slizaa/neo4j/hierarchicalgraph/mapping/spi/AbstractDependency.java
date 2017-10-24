package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

public abstract class AbstractDependency {

  abstract boolean isProxyDependency();

  abstract boolean isSimpleDependency();
}
