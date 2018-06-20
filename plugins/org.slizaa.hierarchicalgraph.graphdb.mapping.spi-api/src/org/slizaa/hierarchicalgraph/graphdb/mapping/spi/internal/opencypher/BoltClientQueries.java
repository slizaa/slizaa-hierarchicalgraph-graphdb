package org.slizaa.hierarchicalgraph.graphdb.mapping.spi.internal.opencypher;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HGProxyDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.DefaultDependencyDefinition;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinition;
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
   * @IDependencyDefinitionDependencyDefinitionInterruptedException
   * @throws ExecutionException
   */
  public static List<IDependencyDefinition> resolveDependencyQuery(Neo4jClient boltClient, String query,
      Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> resolverFunction)
      throws InterruptedException, ExecutionException {

    //
    return checkNotNull(boltClient).executeCypherQuery(checkNotNull(query)).get().list(r -> {

      //
      if (resolverFunction != null) {
        return new ProxyDependencyDefinitionImpl(r.get(0).asLong(), r.get(1).asLong(), r.get(2).asLong(),
            r.get(3).asString(), resolverFunction);
      }
      //
      else {
        return new DefaultDependencyDefinition(r.get(0).asLong(), r.get(1).asLong(), r.get(2).asLong(),
            r.get(3).asString());
      }

    });
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @pIDependencyDefinitions
   * @return IDependencyDefinitionInterruptedException
   * @throws ExecutionException
   */
  public static List<IDependencyDefinition> resolveDependencyQueries(Neo4jClient boltClient, String[] queries,
      Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> resolverFunction)
      throws InterruptedException, ExecutionException {

    //
    checkNotNull(boltClient);

    //
    if (queries != null) {

      // create the result list
      List<IDependencyDefinition> result = new ArrayList<>();

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

  /**
   * <p>
   * </p>
   */
  public static List<Future<List<IDependencyDefinition>>> resolveProxyDependency(HGProxyDependency proxyDependency,
      ProxyDependencyQueriesHolder proxyDependenciesDefinition, Neo4jClient boltClient) {

    //
    checkNotNull(proxyDependency);
    checkNotNull(proxyDependenciesDefinition);
    checkNotNull(boltClient);

    //
    Set<Object> fromNodeIds = new HashSet<>();
    Set<Object> toNodeIds = new HashSet<>();

    //
    for (Iterator<?> iter = EcoreUtil.getAllContents(Collections.singleton(proxyDependency.getFrom())); iter
        .hasNext();) {
      Object containedElement = iter.next();
      if (containedElement instanceof HGNode) {
        fromNodeIds.add(((HGNode) containedElement).getIdentifier());
      }
    }

    for (Iterator<?> iter = EcoreUtil.getAllContents(Collections.singleton(proxyDependency.getTo())); iter.hasNext();) {
      Object containedElement = iter.next();
      if (containedElement instanceof HGNode) {
        toNodeIds.add(((HGNode) containedElement).getIdentifier());
      }
    }

    //
    Map<String, Object> params = new HashMap<>();
    params.put("from", fromNodeIds);
    params.put("to", toNodeIds);

    //
    String[] detailDependencyQueries = proxyDependenciesDefinition.detailDependencyQueries();

    //
    if (detailDependencyQueries != null && detailDependencyQueries.length > 0) {

      // create the result list
      List<Future<List<IDependencyDefinition>>> result = new ArrayList<>();

      // process all queries
      for (String cypherQuery : detailDependencyQueries) {

        //
        Future<List<IDependencyDefinition>> dependencyDefinitions = boltClient
            .executeCypherQueryAndTransformResult(cypherQuery, params, statementResult -> {
              return statementResult.list(r -> new DefaultDependencyDefinition(r.get(0).asLong(), r.get(1).asLong(),
                  r.get(2).asLong(), r.get(3).asString()));
            });

        //
        result.add(dependencyDefinitions);
      }

      // return the result
      return result;
    }

    //
    return Collections.emptyList();
  }
}
