package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

public abstract class AbstractQueryBasedHierarchyProvider implements IHierarchyProvider {

  /** - */
  private Neo4jClient _boltClient;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractQueryBasedHierarchyProvider}.
   * </p>
   *
   * @param boltClient
   */
  public AbstractQueryBasedHierarchyProvider(Neo4jClient boltClient) {
    _boltClient = checkNotNull(boltClient);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Override
  public List<Long> getToplevelNodeIds(Neo4jClient neo4jClient, IProgressMonitor progressMonitor)
      throws InterruptedException, ExecutionException {

    //
    List<Long> result = new ArrayList<>();

    //
    for (String query : toplevelNodeIdQueries()) {

      List<Long> resultQuery = _boltClient.executeCypherQuery(query).get().list(r -> r.get(0).asLong());

      result.addAll(resultQuery);
    }

    //
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Long[]> getParentChildNodeIds(Neo4jClient neo4jClient, IProgressMonitor progressMonitor)
      throws InterruptedException, ExecutionException {

    //
    List<Long[]> result = new ArrayList<>();

    //
    for (String query : parentChildNodeIdsQueries()) {

      List<Long[]> resultQuery = _boltClient.executeCypherQuery(query).get()
          .list(r -> new Long[] { r.get(0).asLong(), r.get(1).asLong() });

      result.addAll(resultQuery);
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
  protected abstract String[] toplevelNodeIdQueries();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected abstract String[] parentChildNodeIdsQueries();
}
