package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

public interface IDependencyProvider {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<AbstractDependency> getDependencies(Neo4jClient neo4jClient, IProgressMonitor progressMonitor);
}
