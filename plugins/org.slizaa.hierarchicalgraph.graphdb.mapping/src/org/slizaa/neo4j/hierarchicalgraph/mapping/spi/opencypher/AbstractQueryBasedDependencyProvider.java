package org.slizaa.neo4j.hierarchicalgraph.mapping.spi.opencypher;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.internal.spi.DefaultDependency;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IDependencyProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractQueryBasedDependencyProvider implements IDependencyProvider, IBoltClientAware {

  /** - */
  private List<IDependency> _dependencies;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(Neo4jClient boltClient, IProgressMonitor progressMonitor) throws Exception {

    checkNotNull(boltClient);

    this._dependencies = new ArrayList<>();

    //
    for (String query : simpleDependenciesQueries()) {

      //
      List<DefaultDependency> simpleDependencies = boltClient.executeCypherQuery(query).get().list(r -> {
        return new DefaultDependency(r.get(0).asLong(), r.get(1).asLong(), r.get(2).asLong(), r.get(3).asString());
      });

      //
      this._dependencies.addAll(simpleDependencies);
    }
  }

  @Override
  public List<IDependency> getDependencies() throws Exception {
    return this._dependencies;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected abstract String[] simpleDependenciesQueries();
}