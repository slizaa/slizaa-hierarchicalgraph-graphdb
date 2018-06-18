package org.slizaa.hierarchicalgraph.graphdb.mapping.spi.opencypher;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IHierarchyDefinitionProvider;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

public abstract class AbstractQueryBasedHierarchyProvider implements IHierarchyDefinitionProvider, IBoltClientAware {

  /** - */
  private List<Long>   _toplevelNodeIds;

  /** - */
  private List<Long[]> _parentChildNodeIdsQueries;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(Neo4jClient boltClient, IProgressMonitor progressMonitor) throws Exception {

    checkNotNull(boltClient);

    //
    this._toplevelNodeIds = new ArrayList<>();
    for (String query : toplevelNodeIdQueries()) {
      this._toplevelNodeIds.addAll(boltClient.executeCypherQuery(query).get().list(r -> r.get(0).asLong()));
    }

    //
    this._parentChildNodeIdsQueries = new ArrayList<>();
    for (String query : parentChildNodeIdsQueries()) {
      this._parentChildNodeIdsQueries.addAll(
          boltClient.executeCypherQuery(query).get().list(r -> new Long[] { r.get(0).asLong(), r.get(1).asLong() }));
    }
  }

  @Override
  public List<Long> getToplevelNodeIds() throws Exception {
    return this._toplevelNodeIds;
  }

  @Override
  public List<Long[]> getParentChildNodeIds() throws Exception {
    return this._parentChildNodeIdsQueries;
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
