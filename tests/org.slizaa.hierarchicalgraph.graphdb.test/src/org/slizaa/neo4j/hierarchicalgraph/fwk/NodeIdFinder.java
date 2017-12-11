package org.slizaa.neo4j.hierarchicalgraph.fwk;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutionException;

import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class NodeIdFinder {

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getDoGetMapperMethod(Neo4jClient boltClient) {
    return requestId(boltClient,
        "Match (m:Method {fqn: 'java.lang.Object org.mapstruct.factory.Mappers.doGetMapper(java.lang.Class,java.lang.ClassLoader)'}) Return id(m)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getAssignmentClassFile(Neo4jClient boltClient) {
    return requestId(boltClient,
        "Match (r:Resource {fqn: 'org/mapstruct/ap/internal/model/common/Assignment.class'}) Return id(r)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getSetterWrapperForCollectionsAndMapsWithNullCheckType(Neo4jClient boltClient) {
    return requestId(boltClient,
        "Match (t:Type {fqn: 'org.mapstruct.ap.internal.model.assignment.SetterWrapperForCollectionsAndMapsWithNullCheck'}) Return id(t)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @param cypherQuery
   * @return
   */
  private static long requestId(Neo4jClient boltClient, String cypherQuery) {

    try {
      return checkNotNull(boltClient).executeCypherQuery(checkNotNull(cypherQuery)).get().single().get(0).asLong();
    } catch (NoSuchRecordException | InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
