package org.slizaa.neo4j.hierarchicalgraph.fwk;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutionException;

import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

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
  public static long getDoGetMapperMethod(Neo4jClient boltClient)
      throws NoSuchRecordException, InterruptedException, ExecutionException {

    return checkNotNull(boltClient).executeCypherQuery(
        "Match (m:Method {fqn: 'java.lang.Object org.mapstruct.factory.Mappers.doGetMapper(java.lang.Class,java.lang.ClassLoader)'}) Return id(m)")
        .get().single().get(0).asLong();
  }
}
