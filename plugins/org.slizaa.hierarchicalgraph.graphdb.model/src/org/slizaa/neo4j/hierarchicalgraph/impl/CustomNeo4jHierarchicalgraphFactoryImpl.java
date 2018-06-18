/**
 *
 */
package org.slizaa.neo4j.hierarchicalgraph.impl;

import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedDependencySource;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedRootNodeSource;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class CustomNeo4jHierarchicalgraphFactoryImpl extends Neo4jHierarchicalgraphFactoryImpl {

  @Override
  public Neo4JBackedNodeSource createNeo4JBackedNodeSource() {
    return new ExtendedNeo4JBackedNodeSourceImpl();
  }

  @Override
  public Neo4JBackedRootNodeSource createNeo4JBackedRootNodeSource() {
    return new ExtendedNeo4JBackedRootNodeSourceImpl();
  }

  @Override
  public Neo4JBackedDependencySource createNeo4JBackedDependencySource() {
    return new ExtendedNeo4JBackedDependencySourceImpl();
  }
}
