package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.util.List;
import java.util.function.Function;

public final class ProxyDependency extends AbstractDependency {

  /** - */
  private SimpleDependency                              _aggregatedDependency;

  /** - */
  private Function<List<AbstractDependency>, SimpleDependency> _resolveFunction;

  /**
   * <p>
   * Creates a new instance of type {@link ${enclosing_type}}.
   * </p>
   */
  public ProxyDependency(SimpleDependency aggregatedDependency,
      Function<List<AbstractDependency>, SimpleDependency> resolveFunction) {
    // TODO
    _aggregatedDependency = aggregatedDependency;
    _resolveFunction = resolveFunction;
  }
  
  @Override
  public boolean isProxyDependency() {
    return true;
  }

  @Override
  public boolean isSimpleDependency() {
    return false;
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
  public Function<List<AbstractDependency>, SimpleDependency> getResolveFunction() {
    return _resolveFunction;
  }
}