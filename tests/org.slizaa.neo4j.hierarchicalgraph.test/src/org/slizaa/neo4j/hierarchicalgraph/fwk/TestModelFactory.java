package org.slizaa.neo4j.hierarchicalgraph.fwk;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slizaa.hierarchicalgraph.HierarchicalgraphFactoryFunctions.createNewRootNode;
import static org.slizaa.neo4j.hierarchicalgraph.fwk.TestModelMappingFunctions.mapFirstLevelElements;
import static org.slizaa.neo4j.hierarchicalgraph.fwk.TestModelMappingFunctions.mapHierarchy;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.neo4j.driver.v1.StatementResult;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.IDependencySource;
import org.slizaa.hierarchicalgraph.INodeSource;
import org.slizaa.neo4j.dbadapter.DbAdapterFactory;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedDependencySource;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedRootNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.Neo4jHierarchicalgraphFactory;

public class TestModelFactory {

  /** - */
  private static final String ROOT_MODULES     = "Match (module:Module) Return id(module) as id";

  /** - */
  private static String       FLAT_DIRECTORIES = "Match (module:Module)-[:CONTAINS]->(d:Directory) WHERE d.isEmpty=false Return DISTINCT id(module), id(d)";

  /** - */
  private static final String FILES            = "Match (d:Directory)-[:CONTAINS]->(r:Resource) Return id(d), id(r)";

  /** - */
  private static final String TYPES            = "Match (r:Resource)-[:CONTAINS]->(t:Type) Return id(r), id(t)";

  // /** - */
  // private static final String DEPENDENCIES = "MATCH (t1:File:Type:Java)-[r:DEPENDS_ON]->(t2:File:Type:Java) RETURN
  // id(t1),id(t2),id(r),type(r)";

  /**
   * <p>
   * </p>
   *
   * @param uri
   * @return
   */
  public static Neo4jClient createNeo4JRemoteRepository(String uri) {

    // create the remote repository
    final Neo4jClient remoteRepository = DbAdapterFactory.eINSTANCE.createNeo4jClient();

    //
    remoteRepository.setUri(checkNotNull(uri));

    //
    return remoteRepository;
  }

  /**
   * <p>
   * </p>
   *
   * @throws InterruptedException
   * @throws ExecutionException
   * @throws IOException
   */
  public static HGRootNode createGraphFromDefaultMapping(Neo4jClient remoteRepository) throws Exception {

    // create the root element
    final HGRootNode rootElement = createNewRootNode(() -> {
      Neo4JBackedRootNodeSource rootNodeSource = Neo4jHierarchicalgraphFactory.eINSTANCE
          .createNeo4JBackedRootNodeSource();
      rootNodeSource.setIdentifier(-1l);
      rootNodeSource.setRepository(remoteRepository);
      return rootNodeSource;
    });

    // ***************************************************
    // create the node source creator function
    // ***************************************************
    Function<Long, INodeSource> createNodeSourceFunction = (id) -> {

      // create the node source
      Neo4JBackedNodeSource nodeSource = Neo4jHierarchicalgraphFactory.eINSTANCE.createNeo4JBackedNodeSource();
      nodeSource.setIdentifier(id);

      // return the result
      return nodeSource;
    };

    // ***************************************************
    // create the node source creator function
    // ***************************************************
    BiFunction<Long, String, IDependencySource> createDependencySourceFunction = (id, type) -> {

      // create the dependency source
      Neo4JBackedDependencySource dependencySource = Neo4jHierarchicalgraphFactory.eINSTANCE
          .createNeo4JBackedDependencySource();
      dependencySource.setIdentifier(id);
      dependencySource.setType("DEPENDS_ON");

      // return the result
      return dependencySource;
    };

    //
    Future<StatementResult> resultRoot = remoteRepository.executeCypherQuery(ROOT_MODULES);
    Future<StatementResult> resultDirectories = remoteRepository.executeCypherQuery(FLAT_DIRECTORIES);
    Future<StatementResult> resultFiles = remoteRepository.executeCypherQuery(FILES);
    Future<StatementResult> resultTypes = remoteRepository.executeCypherQuery(TYPES);

    // Future<StatementResult> dependencies = remoteRepository.executeCypherQuery(DEPENDENCIES);

    // map first level elements
    mapFirstLevelElements(resultRoot.get().list(r -> r.get(0).asLong()), rootElement, createNodeSourceFunction);

    // map hierarchy
    mapHierarchy(resultDirectories.get().list(r -> new Long[] { r.get(0).asLong(), r.get(1).asLong() }), rootElement,
        createNodeSourceFunction);
    mapHierarchy(resultFiles.get().list(r -> new Long[] { r.get(0).asLong(), r.get(1).asLong() }), rootElement,
        createNodeSourceFunction);
    mapHierarchy(resultTypes.get().list(r -> new Long[] { r.get(0).asLong(), r.get(1).asLong() }), rootElement,
        createNodeSourceFunction);

    // //
    // mapDependencies(Neo4jResultJsonConverter.extractDependencyDefinition(dependencies.get()), rootElement, true,
    // createDependencySourceFunction);

    //
    return rootElement;
  }
}
