package org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service.GraphFactoryFunctions.createDependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.slizaa.hierarchicalgraph.HGCoreDependency;
import org.slizaa.hierarchicalgraph.HGProxyDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinition;
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
      System.out.println(String.format("Resolving ProxyDependencyDefinitionImpl from '%s' to '%s'.",
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
    private List<Future<List<IDependencyDefinition>>> _futures;

    /** - */
    private HGProxyDependency                         _proxyDependency;

    /**
     * <p>
     * Creates a new instance of type {@link ProxyDependencyResolverJob}.
     * </p>
     *
     * @param resolveFunction
     */
    public ProxyDependencyResolverJob(HGProxyDependency proxyDependency) {

      //
      this._proxyDependency = checkNotNull(proxyDependency);

      //
      Neo4JBackedDependencySource dependencySource = (Neo4JBackedDependencySource) proxyDependency
          .getDependencySource();

      // TODO List<Future<List<IDependencyDefinition>>>>
      @SuppressWarnings("unchecked")
      Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> resolveFunction = (Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>>) dependencySource
          .getUserObject();

      //
      this._futures = resolveFunction.apply(proxyDependency);
    }

    @Override
    public void waitForCompletion() {

      //
      List<IDependencyDefinition> resolvedDependencyDefinitions = new ArrayList<>();

      //
      for (Future<List<IDependencyDefinition>> future : this._futures) {
        try {
          resolvedDependencyDefinitions.addAll(future.get());
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }

      //
      List<HGCoreDependency> coreDependencies = createDependencies(resolvedDependencyDefinitions,
          this._proxyDependency.getRootNode(),
          (id, type) -> GraphFactoryFunctions.createDependencySource(id, type, null), false, null);

      //
      this._proxyDependency.getResolvedCoreDependencies().addAll(coreDependencies);

      //
      System.out.println("SPONKIE: " + coreDependencies);
    }
  }
}
