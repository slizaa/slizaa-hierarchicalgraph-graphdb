package org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
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
  public IProxyDependencyResolverJob resolveProxyDependency(final HGProxyDependency dependency) {

    checkNotNull(dependency);

    //
    if (DEBUG) {
      System.out.println("--- CustomProxyDependencyResolver ---");
      System.out.println(String.format("Resolving ProxyDependency from '%s' to '%s'.",
          dependency.getFrom().getIdentifier(), dependency.getTo().getIdentifier()));
    }

    //
    return new ProxyDependencyResolverJob(dependency);
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class ProxyDependencyResolverJob implements IProxyDependencyResolverJob {

    /** - */
    private HGProxyDependency         _proxyDependency;

    /** - */
    private Future<List<IDependency>> _future;

    /**
     * <p>
     * Creates a new instance of type {@link ProxyDependencyResolverJob}.
     * </p>
     *
     * @param resolveFunction
     */
    public ProxyDependencyResolverJob(HGProxyDependency proxyDependency) {
      this._proxyDependency = checkNotNull(proxyDependency);

      //
      Neo4JBackedDependencySource dependencySource = (Neo4JBackedDependencySource) proxyDependency
          .getDependencySource();

      // TODO
      @SuppressWarnings("unchecked")
      Function<HGProxyDependency, Future<List<IDependency>>> resolveFunction = (Function<HGProxyDependency, Future<List<IDependency>>>) dependencySource
          .getUserObject();

      //
      this._future = resolveFunction.apply(proxyDependency);
    }

    @Override
    public void waitForCompletion() {
      try {
        this._future.get();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ExecutionException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
