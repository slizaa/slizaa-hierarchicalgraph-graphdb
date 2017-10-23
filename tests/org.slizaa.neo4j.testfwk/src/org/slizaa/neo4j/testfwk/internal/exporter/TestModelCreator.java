package org.slizaa.neo4j.testfwk.internal.exporter;

import static org.slizaa.neo4j.testfwk.internal.exporter.XmiUtils.save;
import static org.slizaa.neo4j.testfwk.internal.exporter.mapping.GraphFactoryFunctions.createDependencies;
import static org.slizaa.neo4j.testfwk.internal.exporter.mapping.GraphFactoryFunctions.createFirstLevelElements;
import static org.slizaa.neo4j.testfwk.internal.exporter.mapping.GraphFactoryFunctions.createHierarchy;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.slizaa.hierarchicalgraph.DefaultDependencySource;
import org.slizaa.hierarchicalgraph.DefaultNodeSource;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.HierarchicalgraphFactory;
import org.slizaa.hierarchicalgraph.IDependencySource;
import org.slizaa.hierarchicalgraph.INodeSource;
import org.slizaa.neo4j.dbadapter.DbAdapterFactory;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.testfwk.internal.exporter.mapping.Neo4jResultJsonConverter;

public class TestModelCreator {

  /** - */
  public static final String ROOT_MODULES     = "MATCH (module:Java:Artifact) WHERE (module:Directory OR module:Jar:Archive) RETURN id(module)";

  /** - */
  public static String       FLAT_DIRECTORIES = "MATCH (module:Java:Artifact)-[:CONTAINS]->(d:Directory)-[:CONTAINS]->(f:File) WHERE (module:Directory OR module:Jar:Archive)  AND NOT (f:Directory) RETURN DISTINCT id(module), id(d)";

  /** - */
  public static final String FILES            = "MATCH (d:Directory)-[:CONTAINS]->(f:File) WHERE NOT (f:Type) AND NOT (f:Directory) RETURN id(d), id(f)";

  /** - */
  public static final String TYPES            = "MATCH (d:Directory)-[:CONTAINS]->(t:Type) RETURN id(d), id(t)";

  /** - */
  public static final String DEPENDENCIES     = "MATCH (t1:File:Type:Java)-[r:DEPENDS_ON]->(t2:File:Type:Java) RETURN id(t1),id(t2),id(r),type(r)";

  /**
   * <p>
   * </p>
   *
   * @throws InterruptedException
   * @throws ExecutionException
   * @throws IOException
   */
  public static void createTestModel(String fileName, String uri, boolean createProxyDependencyModel) throws Exception {

    // create the remote repository
    final Neo4jClient neo4jClient = DbAdapterFactory.eINSTANCE.createNeo4jClient();
    neo4jClient.setUri(uri);
    neo4jClient.connect();

    // create the root element
    final HGRootNode rootNode = HierarchicalgraphFactory.eINSTANCE.createHGRootNode();
    DefaultNodeSource rootNodeSource = HierarchicalgraphFactory.eINSTANCE.createDefaultNodeSource();
    rootNodeSource.setIdentifier(-1l);
    rootNode.setNodeSource(rootNodeSource);

    // ***************************************************
    // create the node source creator function
    // ***************************************************
    Function<Long, INodeSource> createNodeSourceFunction = (id) -> {

      // create the node source
      DefaultNodeSource nodeSource = HierarchicalgraphFactory.eINSTANCE.createDefaultNodeSource();
      nodeSource.setIdentifier(id);

      // get the node
      Node node = neo4jClient.getNode(id);

      // add properties
      node.asMap().entrySet().forEach((e) -> {
        nodeSource.getProperties().put(e.getKey(), e.getValue().toString());
      });

      // add labels
      nodeSource.getProperties().put("labels", node.labels().toString());

      // return the result
      return nodeSource;
    };

    // ***************************************************
    // create the node source creator function
    // ***************************************************
    BiFunction<Long, String, IDependencySource> createDependencySourceFunction = (id, type) -> {

      // create the dependency source
      DefaultDependencySource dependencySource = HierarchicalgraphFactory.eINSTANCE.createDefaultDependencySource();
      dependencySource.setIdentifier(id);

      // // add properties
      // remoteRepository.getNodeProperties(id).entrySet().forEach((e) -> {
      // nodeSource.getProperties().put(e.getKey(), e.getValue().getAsString());
      // });

      // add type
      dependencySource.getProperties().put("type", type);

      // return the result
      return dependencySource;
    };

    //
    Future<StatementResult> resultRoot = neo4jClient.executeCypherQuery(ROOT_MODULES);
    Future<StatementResult> resultDirectories = neo4jClient.executeCypherQuery(FLAT_DIRECTORIES);
    Future<StatementResult> resultFiles = neo4jClient.executeCypherQuery(FILES);
    Future<StatementResult> resultTypes = neo4jClient.executeCypherQuery(TYPES);
    Future<StatementResult> dependencies = neo4jClient.executeCypherQuery(DEPENDENCIES);

    System.out.println("1");

    //
    createFirstLevelElements(Neo4jResultJsonConverter.extractRootNodes(resultRoot.get()).toArray(new Long[0]), rootNode,
        createNodeSourceFunction, null);

    System.out.println("2");

    //
    createHierarchy(Neo4jResultJsonConverter.extractHierarchy(resultDirectories.get()).toArray(new Long[0][0]),
        rootNode, createNodeSourceFunction, null);

    System.out.println("3");

    createHierarchy(Neo4jResultJsonConverter.extractHierarchy(resultFiles.get()).toArray(new Long[0][0]), rootNode,
        createNodeSourceFunction, null);

    System.out.println("4");

    createHierarchy(Neo4jResultJsonConverter.extractHierarchy(resultTypes.get()).toArray(new Long[0][0]), rootNode,
        createNodeSourceFunction, null);

    System.out.println("5");

    //
    createDependencies(Neo4jResultJsonConverter.extractNeo4jRelationships(dependencies.get()), rootNode,
        createDependencySourceFunction, createProxyDependencyModel, false, null);

    //
    System.out.println("DONE");
    save(fileName, rootNode);

    //
    neo4jClient.disconnect();
  }

  /**
   * <p>
   * </p>
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    TestModelCreator.createTestModel(new File(System.getProperty("user.dir"), "test.hggraph").getAbsolutePath(),
        "http://bolt:5001", true);
    System.out.println("DONE");
  }
}
