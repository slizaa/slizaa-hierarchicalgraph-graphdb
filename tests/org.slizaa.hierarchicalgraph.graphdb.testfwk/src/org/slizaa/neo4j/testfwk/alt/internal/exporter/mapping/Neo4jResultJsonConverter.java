package org.slizaa.neo4j.testfwk.alt.internal.exporter.mapping;

import java.util.List;

import org.neo4j.driver.v1.StatementResult;
import org.slizaa.neo4j.testfwk.alt.internal.exporter.mapping.GraphFactoryFunctions.Neo4jRelationship;

public class Neo4jResultJsonConverter {

  /**
   * <p>
   * </p>
   *
   * @param jsonObject
   * @return
   */
  public static List<Neo4jRelationship> extractNeo4jRelationships(StatementResult statementResult) {
    return statementResult.list(record -> new Neo4jRelationship(record.get(0).asLong(), record.get(1).asLong(),
        record.get(2).asLong(), record.get(3).asString()));
  }

  /**
   * <p>
   * </p>
   *
   * @param jsonObject
   */
  public static List<Long[]> extractHierarchy(StatementResult statementResult) {
    return statementResult.list(record -> new Long[] { record.get(0).asLong(), record.get(1).asLong() });
  }

  /**
   * <p>
   * </p>
   *
   * @param jsonObject
   * @return
   */
  public static List<Long> extractRootNodes(StatementResult statementResult) {
    return statementResult.list(record -> record.get(0).asLong());
  }
}
