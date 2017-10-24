package org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.GraphFactoryFunctions.createDependencies;
import static org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.GraphFactoryFunctions.createFirstLevelElements;
import static org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.GraphFactoryFunctions.createHierarchy;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.neo4j.driver.v1.StatementResult;
import org.osgi.service.component.annotations.Component;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.HierarchicalgraphFactory;
import org.slizaa.hierarchicalgraph.INodeSource;
import org.slizaa.hierarchicalgraph.impl.ExtendedHGRootNodeImpl;
import org.slizaa.hierarchicalgraph.spi.IProxyDependencyResolver;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedRootNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.Neo4jHierarchicalgraphFactory;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.HierarchicalGraphMappingException;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IHierarchicalGraphMappingService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.GraphFactoryFunctions.Neo4jRelationship;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
public class HierarchicalgraphMappingServiceImpl implements IHierarchicalGraphMappingService {

  /** create the node source creator function */
  static Function<Long, INodeSource> createNodeSourceFunction = (id) -> {

    // create the node source
    INodeSource nodeSource = Neo4jHierarchicalgraphFactory.eINSTANCE.createNeo4JBackedNodeSource();
    nodeSource.setIdentifier(id);

    // return the result
    return nodeSource;
  };

  /**
   * {@inheritDoc}
   */
  @Override
  public HGRootNode convert(IMappingProvider mappingDescriptor, final Neo4jClient remoteRepository,
      IProgressMonitor progressMonitor) throws HierarchicalGraphMappingException {

    checkNotNull(mappingDescriptor);
    checkNotNull(remoteRepository);

    // create the sub monitor
    SubMonitor subMonitor = progressMonitor != null ? SubMonitor.convert(progressMonitor, 100) : null;

    // create the root element
    final HGRootNode rootNode = HierarchicalgraphFactory.eINSTANCE.createHGRootNode();
    Neo4JBackedRootNodeSource rootNodeSource = Neo4jHierarchicalgraphFactory.eINSTANCE
        .createNeo4JBackedRootNodeSource();
    rootNodeSource.setIdentifier(-1l);
    rootNodeSource.setRepository(remoteRepository);
    rootNode.setNodeSource(rootNodeSource);

    // create the future lists
    List<Future<StatementResult>> rootQueries = new LinkedList<>();
    List<Future<StatementResult>> hierachyQueries = new LinkedList<>();
    List<Future<StatementResult>> simpleDependencyQueries = new LinkedList<>();
    List<AggregatedDependencyQueryHolder> aggregatedDependencyQueries = new LinkedList<>();

    // process root, hierarchy and dependency queries
    StructureDescriptor structureDescriptor = mappingDescriptor.getStructureDescriptor();
    if (structureDescriptor != null) {

      //
      if (structureDescriptor.getTopLevelNodeQueries() != null
          && structureDescriptor.getTopLevelNodeQueries().getQueries() != null) {

        //
        structureDescriptor.getTopLevelNodeQueries().getQueries().forEach(cypherQuery -> {
          rootQueries.add(remoteRepository.executeCypherQuery(CypherNormalizer.normalize(cypherQuery)));
        });
      }

      //
      if (structureDescriptor.getNodeHierarchyQueries() != null
          && structureDescriptor.getNodeHierarchyQueries().getQueries() != null) {

        //
        structureDescriptor.getNodeHierarchyQueries().getQueries().forEach(cypherQuery -> {
          hierachyQueries.add(remoteRepository.executeCypherQuery(CypherNormalizer.normalize(cypherQuery)));
        });
      }

      //
      if (structureDescriptor.getDependencyQueries() != null
          && structureDescriptor.getDependencyQueries().getSimpleDependencyQueries() != null) {

        //
        structureDescriptor.getDependencyQueries().getSimpleDependencyQueries().forEach(cypherQuery -> {
          simpleDependencyQueries.add(remoteRepository.executeCypherQuery(CypherNormalizer.normalize(cypherQuery)));
        });
      }

      //
      if (structureDescriptor.getDependencyQueries() != null
          && structureDescriptor.getDependencyQueries().getAggregatedDependencyQueries() != null) {

        //
        structureDescriptor.getDependencyQueries().getAggregatedDependencyQueries()
            .forEach(aggregatedDependencyQuery -> {
              AggregatedDependencyQueryHolder holder = new AggregatedDependencyQueryHolder(aggregatedDependencyQuery,
                  remoteRepository
                      .executeCypherQuery(CypherNormalizer.normalize(aggregatedDependencyQuery.getAggregatedQuery())));
              aggregatedDependencyQueries.add(holder);
            });
      }
    }

    //
    resolveRootQueries(rootNode, rootQueries,
        subMonitor != null ? subMonitor.split(25).setWorkRemaining(rootQueries.size()) : null);

    //
    resolveHierarchyQueries(rootNode, hierachyQueries,
        subMonitor != null ? subMonitor.split(25).setWorkRemaining(hierachyQueries.size()) : null);

    // filter 'dangling' nodes
    List<Object> nodeKeys2Remove = ((ExtendedHGRootNodeImpl) rootNode).getIdToNodeMap().entrySet().stream()
        .filter((n) -> {
          try {
            return !new Long(0).equals(n.getValue().getIdentifier()) && n.getValue().getRootNode() == null;
          } catch (Exception e) {
            return true;
          }
        }).map(n -> n.getKey()).collect(Collectors.toList());

    // TODO
    System.out.println("REMOVING: " + nodeKeys2Remove);
    nodeKeys2Remove.forEach(k -> ((ExtendedHGRootNodeImpl) rootNode).getIdToNodeMap().remove(k));

    //
    resolveSimpleDependencyQueries(rootNode, simpleDependencyQueries,
        subMonitor != null ? subMonitor.split(25).setWorkRemaining(simpleDependencyQueries.size()) : null);

    //
    resolveAggregatedDependencyQueries(rootNode, aggregatedDependencyQueries,
        subMonitor != null ? subMonitor.split(25).setWorkRemaining(simpleDependencyQueries.size()) : null);

    // register default extensions
    rootNode.registerExtension(Neo4jClient.class, remoteRepository);
    rootNode.registerExtension(IProxyDependencyResolver.class, new CustomProxyDependencyResolver());
    rootNode.registerExtension(MappingDescriptor.class, mappingDescriptor);

    //
    return addEditingDomain(rootNode);
  }

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   * @param dependencyQueries
   * @param dependencyLoopMonitor
   */
  private void resolveSimpleDependencyQueries(final HGRootNode rootNode,
      List<Future<StatementResult>> dependencyQueries, SubMonitor dependencyLoopMonitor) {

    //
    dependencyQueries.forEach((dependencyQuery) -> {

      try {
        SubMonitor iterationMonitor = dependencyLoopMonitor != null ? dependencyLoopMonitor.split(1) : null;

        // request dependencies
        report(iterationMonitor, "Requesting dependencies...");

        List<GraphFactoryFunctions.Neo4jRelationship> dependencyDefinitions = dependencyQuery.get()
            .list(record -> new Neo4jRelationship(record.get(0).asLong(), record.get(1).asLong(),
                record.get(2).asLong(), record.get(3).asString()));

        // create dependencies
        report(iterationMonitor, "Creating dependencies...");

        //
        createDependencies(dependencyDefinitions, rootNode,
            (id, type) -> GraphFactoryFunctions.createDependencySource(id, type, null), false, false, iterationMonitor);

      } catch (Exception e) {
        throw new HierarchicalGraphMappingException(e);
      }
    });
  }

  // TODO
  private void resolveAggregatedDependencyQueries(final HGRootNode rootNode,
      List<AggregatedDependencyQueryHolder> dependencyQueries, SubMonitor dependencyLoopMonitor) {

    //
    dependencyQueries.forEach((dependencyQuery) -> {

      try {
        SubMonitor iterationMonitor = dependencyLoopMonitor != null ? dependencyLoopMonitor.split(1) : null;

        // request dependencies
        report(iterationMonitor, "Requesting dependencies...");

        //
        List<GraphFactoryFunctions.Neo4jRelationship> neo4jRelationships = dependencyQuery.getFuture().get()
            .list(record -> new Neo4jRelationship(record.get(0).asLong(), record.get(1).asLong(),
                record.get(2).asLong(), record.get(3).asString()));

        // create dependencies
        report(iterationMonitor, "Creating dependencies...");
        createDependencies(neo4jRelationships, rootNode,
            (id, type) -> GraphFactoryFunctions.createDependencySource(id, type,
                dependencyQueries.size() > 1 ? dependencyQuery.getAggregatedDependencyQuery() : null),
            true, false, iterationMonitor);

      } catch (Exception e) {
        throw new HierarchicalGraphMappingException(e);
      }
    });
  }

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   * @param dependencyQueries
   * @param dependencyLoopMonitor
   */
  // TODO
  // private void resolveDependencyQueries(final HGRootNode rootNode, List<DependencyQuery> dependencyQueries,
  // SubMonitor dependencyLoopMonitor) {
  //
  // //
  // dependencyQueries.forEach((dependencyQuery) -> {
  //
  // try {
  // SubMonitor iterationMonitor = dependencyLoopMonitor != null ? dependencyLoopMonitor.split(1) : null;
  //
  // // request dependencies
  // report(iterationMonitor, "Requesting dependencies...");
  // JsonArray jsonArray = dependencyQuery.getFuture().get().getAsJsonArray("data");
  //
  // // create dependencies
  // report(iterationMonitor, "Creating dependencies...");
  // createDependencies(jsonArray, rootNode,
  // (id, type) -> GraphFactoryFunctions.createDependencySource(id, type,
  // dependencyQueries.size() > 1 ? dependencyQuery.getDependencyMapping() : null),
  // dependencyQuery.getDependencyMapping().isProxyDependency(), false, iterationMonitor);
  //
  // } catch (Exception e) {
  // throw new HierarchicalGraphMappingException(e);
  // }
  // });
  // }

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   * @param hierachyQueries
   * @param hierarchyLoopMonitor
   */
  private void resolveHierarchyQueries(final HGRootNode rootNode, List<Future<StatementResult>> hierachyQueries,
      SubMonitor hierarchyLoopMonitor) {

    //
    hierachyQueries.forEach((f) -> {

      try {
        SubMonitor iterationMonitor = hierarchyLoopMonitor != null ? hierarchyLoopMonitor.split(1) : null;
        report(iterationMonitor, "Requesting nodes...");

        Long[][] hierarchyElementIds = f.get()
            .list(record -> new Long[] { record.get(0).asLong(), record.get(1).asLong() }).toArray(new Long[0][0]);

        report(iterationMonitor, "Creating nodes...");

        createHierarchy(hierarchyElementIds, rootNode, createNodeSourceFunction, iterationMonitor);
      } catch (Exception e) {
        throw new HierarchicalGraphMappingException(e);
      }
    });
  }

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   * @param rootQueries
   * @param rootLoopMonitor
   */
  private void resolveRootQueries(final HGRootNode rootNode, List<Future<StatementResult>> rootQueries,
      SubMonitor rootLoopMonitor) {

    //
    rootQueries.forEach((f) -> {

      try {
        SubMonitor iterationMonitor = rootLoopMonitor != null ? rootLoopMonitor.split(1) : null;
        report(iterationMonitor, "Requesting root nodes...");

        //
        List<Long> rootNodes = f.get().list(record -> record.get(0).asLong());

        //
        report(iterationMonitor, "Creating root nodes...");
        createFirstLevelElements(rootNodes.toArray(new Long[0]), rootNode, createNodeSourceFunction, iterationMonitor);
      } catch (Exception e) {
        throw new HierarchicalGraphMappingException(e);
      }
    });
  }

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   * @return
   */
  private HGRootNode addEditingDomain(HGRootNode rootNode) {

    //
    Resource resource = new ResourceSetImpl().createResource(URI.createURI("memory://localhost/hierarchicalgraph"));

    //
    BasicCommandStack basicCommandStack = new BasicCommandStack();
    AdapterFactoryEditingDomain adapterFactoryEditingDomain = new AdapterFactoryEditingDomain(
        new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE), basicCommandStack);
    resource.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(adapterFactoryEditingDomain));

    //
    resource.getContents().add(rootNode);

    //
    return rootNode;
  }

  /**
   * <p>
   * </p>
   *
   * @param iterationMonitor
   * @param taskName
   */
  private void report(SubMonitor iterationMonitor, String taskName) {
    if (iterationMonitor != null) {
      iterationMonitor.setTaskName(taskName);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class AggregatedDependencyQueryHolder {

    /** - */
    private Future<StatementResult>   _future;

    /** - */
    private AggregatedDependencyQuery _aggregatedDependencyQuery;

    /**
     * <p>
     * Creates a new instance of type {@link AggregatedDependencyQuery}.
     * </p>
     *
     * @param aggregatedDependencyQuery
     * @param future
     */
    public AggregatedDependencyQueryHolder(AggregatedDependencyQuery aggregatedDependencyQuery,
        Future<StatementResult> future) {
      this._future = checkNotNull(future);
      this._aggregatedDependencyQuery = checkNotNull(aggregatedDependencyQuery);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public Future<StatementResult> getFuture() {
      return _future;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public AggregatedDependencyQuery getAggregatedDependencyQuery() {
      return _aggregatedDependencyQuery;
    }
  }
}
