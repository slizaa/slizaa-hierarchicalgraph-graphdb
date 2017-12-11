package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IHierarchyProvider {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<Long> getToplevelNodeIds(Neo4jClient neo4jClient, IProgressMonitor progressMonitor) throws Exception;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<Long[]> getParentChildNodeIds(Neo4jClient neo4jClient, IProgressMonitor progressMonitor) throws Exception;
}
