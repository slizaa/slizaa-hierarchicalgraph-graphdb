package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.opencypher.AbstractQueryBasedDependencyProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SimpleJTypeDependencyProvider extends AbstractQueryBasedDependencyProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void initialize() {

    //
    addSimpleDependencyDefinitions(
        "Match (t1:Type)-[r:DEPENDS_ON]->(t2:Type) RETURN id(t1), id(t2), id(r), 'DEPENDS_ON'");
  }
}
