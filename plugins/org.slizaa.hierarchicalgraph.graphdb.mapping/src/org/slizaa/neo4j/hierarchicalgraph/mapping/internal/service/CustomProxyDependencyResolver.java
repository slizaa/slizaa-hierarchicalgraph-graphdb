package org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.slizaa.hierarchicalgraph.HGProxyDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyProvider.IDependency;
import org.slizaa.hierarchicalgraph.spi.IProxyDependencyResolver;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedDependencySource;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CustomProxyDependencyResolver implements IProxyDependencyResolver {

  /** - */
  private static final boolean DEBUG = true;

  /**
   * {@inheritDoc}
   */
  @Override
  public IProxyDependencyResolverResult resolveProxyDependency(final HGProxyDependency dependency) {

    checkNotNull(dependency);

    //
    if (DEBUG) {
      System.out.println("--- CustomProxyDependencyResolver ---");
      System.out.println(String.format("Resolving ProxyDependency from '%s' to '%s'.",
          dependency.getFrom().getIdentifier(), dependency.getTo().getIdentifier()));
    }

    //
    Neo4JBackedDependencySource dependencySource = (Neo4JBackedDependencySource) dependency.getDependencySource();

    // TODO
    @SuppressWarnings("unchecked")
    Function<IDependency, Future<List<IDependency>>> resolveFunction = (Function<IDependency, Future<List<IDependency>>>) dependencySource
        .getUserObject();

    //
    return null;
  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
  // */
  // private class ProxyDependencyResolverResult implements IProxyDependencyResolverResult {
  //
  // /** - */
  // private Function<List<IDependency>, IDependency> _resolveFunction;
  //
  // /**
  // * <p>
  // * </p>
  // *
  // */
  // public void bla() {
  // _resolveFunction.apply(arg0)
  // }
  //
  //
  // @Override
  // public void waitForCompletion() {
  //
  // }
  // }
}
