package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

public abstract class AbstractQueryBasedDependencyProvider implements IDependencyProvider {

  /** - */
  private Neo4jClient _boltClient;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractQueryBasedDependencyProvider}.
   * </p>
   *
   * @param boltClient
   */
  public AbstractQueryBasedDependencyProvider(Neo4jClient boltClient) {
    _boltClient = checkNotNull(boltClient);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<AbstractDependency> getDependencies(Neo4jClient neo4jClient, IProgressMonitor progressMonitor)
      throws InterruptedException, ExecutionException {

    //
    List<AbstractDependency> result = new ArrayList<>();

    //
    for (String query : simpleDependenciesQueries()) {

      //
      List<SimpleDependency> simpleDependencies = _boltClient.executeCypherQuery(query).get().list(r -> {

        //
        long idStart = r.get(0).asLong();
        long idTarget = r.get(1).asLong();
        long idRel = r.get(2).asLong();
        String type = r.get(3).asString();

        //
        return new SimpleDependency(idStart, idTarget, idRel, type);
      });

      result.addAll(simpleDependencies);
    }

    //
    return result;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected abstract String[] simpleDependenciesQueries();
}