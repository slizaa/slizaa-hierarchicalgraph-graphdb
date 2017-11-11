package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.AbstractDependency;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IDependencyProvider;

public class DummyDependencyProvider implements IDependencyProvider {

  @Override
  public List<AbstractDependency> getDependencies(Neo4jClient neo4jClient, IProgressMonitor progressMonitor) {
    return null;
  }
}
