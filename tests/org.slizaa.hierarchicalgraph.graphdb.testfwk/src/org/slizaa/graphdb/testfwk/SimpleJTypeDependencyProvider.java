package org.slizaa.graphdb.testfwk;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.opencypher.AbstractQueryBasedDependencyProvider;

public class SimpleJTypeDependencyProvider extends AbstractQueryBasedDependencyProvider {

  @Override
  protected String[] simpleDependenciesQueries() {
    return new String[] { "Match (t1:Type)-[r:DEPENDS_ON]->(t2:Type) RETURN id(t1), id(t2), id(r), 'DEPENDS_ON'" };
  }
}
