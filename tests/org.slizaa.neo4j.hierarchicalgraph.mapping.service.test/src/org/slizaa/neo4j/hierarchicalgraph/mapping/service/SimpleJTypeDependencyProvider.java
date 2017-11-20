package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.AbstractQueryBasedDependencyProvider;

public class SimpleJTypeDependencyProvider extends AbstractQueryBasedDependencyProvider {

  /**
   * <p>
   * Creates a new instance of type {@link SimpleJTypeDependencyProvider}.
   * </p>
   *
   * @param boltClient
   */
  public SimpleJTypeDependencyProvider(Neo4jClient boltClient) {
    super(boltClient);
  }

  @Override
  protected String[] simpleDependenciesQueries() {
    return new String[] { "Match (t1:Type)-[r:DEPENDS_ON]->(t2:Type) RETURN id(t1), id(t2), id(r), 'DEPENDS_ON'" };
  }
}
