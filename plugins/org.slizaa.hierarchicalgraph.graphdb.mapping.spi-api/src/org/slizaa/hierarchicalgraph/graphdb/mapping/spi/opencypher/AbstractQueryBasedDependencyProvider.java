package org.slizaa.hierarchicalgraph.graphdb.mapping.spi.opencypher;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.hierarchicalgraph.HGProxyDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyProvider;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractQueryBasedDependencyProvider implements IDependencyProvider, IBoltClientAware {

  /** - */
  private List<String>                      _simpleDependenciesQueries;

  /** - */
  private List<ProxyDependenciesDefinition> _proxyDependenciesQueries;

  /** - */
  private List<IDependency>                 _dependencies;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractQueryBasedDependencyProvider}.
   * </p>
   */
  public AbstractQueryBasedDependencyProvider() {
    _simpleDependenciesQueries = new LinkedList<>();
    _proxyDependenciesQueries = new LinkedList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(final Neo4jClient boltClient, IProgressMonitor progressMonitor) throws Exception {

    checkNotNull(boltClient);

    initialize();

    this._dependencies = new ArrayList<>();

    // simple dependencies
    for (String query : _simpleDependenciesQueries) {
      this._dependencies.addAll(BoltClientQueries.resolveDependencyQuery(boltClient, query, null));
    }

    // proxy dependencies
    for (ProxyDependenciesDefinition dependenciesDefinition : _proxyDependenciesQueries) {

      // create the resolver function
      Function<HGProxyDependency, Future<List<IDependency>>> resolverFunction = (proxyDependency) -> {
        System.out.println("AJSHDJASHDJASHBDJHASJDBAJSHBD");
        return CompletableFuture.completedFuture(Collections.emptyList());
      };

      //
      for (String query : dependenciesDefinition.proxyDependencyQueries()) {
        this._dependencies.addAll(BoltClientQueries.resolveDependencyQuery(boltClient, query, resolverFunction));
      }
    }
  }

  /**
   * <p>
   * </p>
   */
  protected abstract void initialize();

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IDependency> getDependencies() throws Exception {
    return this._dependencies;
  }

  /**
   * <p>
   * </p>
   *
   * @param proxyDependencyQueries
   * @param detailDependencyQueries
   * @return
   */
  protected void addProxyDependencyDefinitions(String[] proxyDependencyQueries, String[] detailDependencyQueries) {

    //
    _proxyDependenciesQueries.add(
        new ProxyDependenciesDefinition(checkNotNull(proxyDependencyQueries), checkNotNull(detailDependencyQueries)));
  }

  /**
   * <p>
   * </p>
   *
   * @param proxyDependencyQuery
   * @param detailDependencyQueries
   */
  protected void addProxyDependencyDefinitions(String proxyDependencyQuery, String[] detailDependencyQueries) {

    //
    _proxyDependenciesQueries.add(new ProxyDependenciesDefinition(new String[] { checkNotNull(proxyDependencyQuery) },
        checkNotNull(detailDependencyQueries)));
  }

  /**
   * <p>
   * </p>
   *
   * @param simpleDependencyQueries
   * @param detailDependencyQueries
   */
  protected void addSimpleDependencyDefinitions(String simpleDependencyQuery) {

    //
    _simpleDependenciesQueries.add(simpleDependencyQuery);
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private static class ProxyDependenciesDefinition {

    /** - */
    private String[] _proxyDependencyQueries;

    /** - */
    private String[] _detailDependencyQueries;

    /**
     * <p>
     * Creates a new instance of type {@link ProxyDependenciesDefinition}.
     * </p>
     *
     * @param proxyDependencyQueries
     * @param detailDependencyQueries
     */
    public ProxyDependenciesDefinition(String[] proxyDependencyQueries, String[] detailDependencyQueries) {
      _proxyDependencyQueries = checkNotNull(proxyDependencyQueries);
      _detailDependencyQueries = checkNotNull(detailDependencyQueries);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public String[] proxyDependencyQueries() {
      return _proxyDependencyQueries;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public String[] detailDependencyQueries() {
      return _detailDependencyQueries;
    }
  }
}