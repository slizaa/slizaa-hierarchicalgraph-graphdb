package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.AbstractQueryBasedHierarchyProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SimpleJTypeHierarchyProvider extends AbstractQueryBasedHierarchyProvider {

  /**
   * <p>
   * Creates a new instance of type {@link SimpleJTypeHierarchyProvider}.
   * </p>
   *
   * @param boltClient
   */
  public SimpleJTypeHierarchyProvider(Neo4jClient boltClient) {
    super(boltClient);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected String[] toplevelNodeIdQueries() {
    return new String[] { "Match (module:Module) Return id(module) as id" };
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected String[] parentChildNodeIdsQueries() {
    return new String[] {
        "Match (module:Module)-[:CONTAINS]->(d:Directory) WHERE d.isEmpty=false Return DISTINCT id(module), id(d)",
        "Match (d:Directory)-[:CONTAINS]->(r:Resource) Return id(d), id(r)",
        "Match (r:Resource)-[:CONTAINS]->(t:Type) Return id(r), id(t)" };
  }
}
