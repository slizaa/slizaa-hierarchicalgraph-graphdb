package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.util.List;
import java.util.function.Function;

public final class ProxyDependency implements IDependency {

  /** - */
  private SimpleDependency                              _aggregatedDependency;

  /** - */
  private Function<List<IDependency>, SimpleDependency> _resolveFunction;

  /**
   * <p>
   * Creates a new instance of type {@link ${enclosing_type}}.
   * </p>
   */
  public ProxyDependency(SimpleDependency aggregatedDependency,
      Function<List<IDependency>, SimpleDependency> resolveFunction) {
    // TODO
    _aggregatedDependency = aggregatedDependency;
    _resolveFunction = resolveFunction;
  }

  /**
   * <p>
   * </p>
   */
  public SimpleDependency getAggregatedDependency() {
    return _aggregatedDependency;
  }

  /**
   * <p>
   * </p>
   */
  public Function<List<IDependency>, SimpleDependency> getResolveFunction() {
    return _resolveFunction;
  }
}