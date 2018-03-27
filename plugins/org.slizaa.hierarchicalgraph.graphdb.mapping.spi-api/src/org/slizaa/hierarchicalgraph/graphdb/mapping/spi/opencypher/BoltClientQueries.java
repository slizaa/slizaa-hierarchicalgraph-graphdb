package org.slizaa.hierarchicalgraph.graphdb.mapping.spi.opencypher;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.slizaa.hierarchicalgraph.HGProxyDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.DefaultDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyProvider.IDependency;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BoltClientQueries {

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @param query
   * @return
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static List<IDependency> resolveDependencyQuery(Neo4jClient boltClient, String query,
      Function<HGProxyDependency, Future<List<IDependency>>> resolverFunction)
      throws InterruptedException, ExecutionException {

    //
    return checkNotNull(boltClient).executeCypherQuery(checkNotNull(query)).get().list(r -> {

      //
      if (resolverFunction != null) {
        return new ProxyDependency(r.get(0).asLong(), r.get(1).asLong(), r.get(2).asLong(), r.get(3).asString(),
            resolverFunction);
      }
      //
      else {
        return new DefaultDependency(r.get(0).asLong(), r.get(1).asLong(), r.get(2).asLong(), r.get(3).asString());
      }

    });
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @param queries
   * @return
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static List<IDependency> resolveDependencyQueries(Neo4jClient boltClient, String[] queries,
      Function<HGProxyDependency, Future<List<IDependency>>> resolverFunction)
      throws InterruptedException, ExecutionException {

    //
    checkNotNull(boltClient);

    //
    if (queries != null) {

      // create the result list
      List<IDependency> result = new ArrayList<>();

      // process all queries
      for (String query : queries) {
        result.addAll(resolveDependencyQuery(boltClient, query, resolverFunction));
      }

      // return the result
      return result;
    }

    //
    return Collections.emptyList();
  }
}
