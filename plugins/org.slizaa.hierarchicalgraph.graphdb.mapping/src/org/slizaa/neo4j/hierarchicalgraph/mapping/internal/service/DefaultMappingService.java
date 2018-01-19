package org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service.GraphFactoryFunctions.createDependencies;
import static org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service.GraphFactoryFunctions.createFirstLevelElements;
import static org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service.GraphFactoryFunctions.createHierarchy;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.osgi.service.component.annotations.Component;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.HierarchicalgraphFactory;
import org.slizaa.hierarchicalgraph.INodeSource;
import org.slizaa.hierarchicalgraph.impl.ExtendedHGRootNodeImpl;
import org.slizaa.hierarchicalgraph.spi.INodeComparator;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedRootNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.Neo4jHierarchicalgraphFactory;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IMappingService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.MappingException;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IDependencyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IHierarchyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.opencypher.IBoltClientAware;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
public class DefaultMappingService implements IMappingService {

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
  public HGRootNode convert(IMappingProvider mappingDescriptor, final Neo4jClient boltClient,
      IProgressMonitor progressMonitor) throws MappingException {

    checkNotNull(mappingDescriptor);
    checkNotNull(boltClient);

    try {
      // create the sub monitor
      SubMonitor subMonitor = progressMonitor != null ? SubMonitor.convert(progressMonitor, 100) : null;

      // create the root element
      final HGRootNode rootNode = HierarchicalgraphFactory.eINSTANCE.createHGRootNode();
      Neo4JBackedRootNodeSource rootNodeSource = Neo4jHierarchicalgraphFactory.eINSTANCE
          .createNeo4JBackedRootNodeSource();
      rootNodeSource.setIdentifier(-1l);
      rootNodeSource.setRepository(boltClient);
      rootNode.setNodeSource(rootNodeSource);

      // process root, hierarchy and dependency queries
      IHierarchyProvider hierarchyProvider = initializeBoltClientAwareMappingProviderComponent(
          mappingDescriptor.getHierarchyProvider(), boltClient, progressMonitor);

      if (hierarchyProvider != null) {

        //
        report(subMonitor, "Requesting root nodes...");
        List<Long> rootNodes = hierarchyProvider.getToplevelNodeIds();

        report(subMonitor, "Creating root nodes...");
        createFirstLevelElements(rootNodes.toArray(new Long[0]), rootNode, createNodeSourceFunction, progressMonitor);

        //
        report(subMonitor, "Requesting nodes...");
        List<Long[]> parentChildNodeIds = hierarchyProvider.getParentChildNodeIds();

        report(subMonitor, "Creating nodes...");
        createHierarchy(parentChildNodeIds, rootNode, createNodeSourceFunction, progressMonitor);

        // filter 'dangling' nodes
        removeDanglingNodes(rootNode);

        //
        IDependencyProvider dependencyProvider = initializeBoltClientAwareMappingProviderComponent(
            mappingDescriptor.getDependencyProvider(), boltClient, progressMonitor);

        if (dependencyProvider != null) {

          report(subMonitor, "Creating dependencies...");

          //
          createDependencies(dependencyProvider.getDependencies(), rootNode,
              (id, type) -> GraphFactoryFunctions.createDependencySource(id, type, null), false, progressMonitor);
        }
      }

      //
      // if (hierarchyProvider.getDependencyQueries() != null
      // && hierarchyProvider.getDependencyQueries().getSimpleDependencyQueries() != null) {
      //
      // //
      // hierarchyProvider.getDependencyQueries().getSimpleDependencyQueries().forEach(cypherQuery -> {
      // simpleDependencyQueries.add(remoteRepository.executeCypherQuery(CypherNormalizer.normalize(cypherQuery)));
      // });
      // }
      //
      // //
      // if (hierarchyProvider.getDependencyQueries() != null
      // && hierarchyProvider.getDependencyQueries().getAggregatedDependencyQueries() != null) {
      //
      // //
      // hierarchyProvider.getDependencyQueries().getAggregatedDependencyQueries()
      // .forEach(aggregatedDependencyQuery -> {
      // AggregatedDependencyQueryHolder holder = new AggregatedDependencyQueryHolder(aggregatedDependencyQuery,
      // remoteRepository
      // .executeCypherQuery(CypherNormalizer.normalize(aggregatedDependencyQuery.getAggregatedQuery())));
      // aggregatedDependencyQueries.add(holder);
      // });
      // }
      // }
      //
      // //
      // resolveRootQueries(rootNode, rootQueries,
      // subMonitor != null ? subMonitor.split(25).setWorkRemaining(rootQueries.size()) : null);
      //
      // //
      // resolveHierarchyQueries(rootNode, hierachyQueries,
      // subMonitor != null ? subMonitor.split(25).setWorkRemaining(hierachyQueries.size()) : null);
      //

      //
      // //
      // resolveSimpleDependencyQueries(rootNode, simpleDependencyQueries,
      // subMonitor != null ? subMonitor.split(25).setWorkRemaining(simpleDependencyQueries.size()) : null);
      //
      // //
      // resolveAggregatedDependencyQueries(rootNode, aggregatedDependencyQueries,
      // subMonitor != null ? subMonitor.split(25).setWorkRemaining(simpleDependencyQueries.size()) : null);
      //

      // register default extensions
      rootNode.registerExtension(Neo4jClient.class, boltClient);
      // rootNode.registerExtension(IProxyDependencyResolver.class, new CustomProxyDependencyResolver());
      rootNode.registerExtension(IMappingProvider.class, mappingDescriptor);
      rootNode.registerExtension(INodeComparator.class, mappingDescriptor.getNodeComparator());
      rootNode.registerExtension(ILabelDefinitionProvider.class, mappingDescriptor.getLabelDefinitionProvider());

      //
      // return addEditingDomain(rootNode);
      return rootNode;
    }
    //
    catch (Exception e) {
      throw new MappingException(e.getMessage(), e);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param service
   * @param boltClient
   * @param progressMonitor
   * @throws Exception
   */
  private <T> T initializeBoltClientAwareMappingProviderComponent(T component, final Neo4jClient boltClient,
      IProgressMonitor progressMonitor) throws Exception {

    if (component instanceof IBoltClientAware) {
      ((IBoltClientAware) component).initialize(boltClient, progressMonitor);
    }

    return component;
  }

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   */
  private void removeDanglingNodes(final HGRootNode rootNode) {

    //
    List<Object> nodeKeys2Remove = ((ExtendedHGRootNodeImpl) rootNode).getIdToNodeMap().entrySet().stream()
        .filter((n) -> {
          try {
            return !new Long(0).equals(n.getValue().getIdentifier()) && n.getValue().getRootNode() == null;
          } catch (Exception e) {
            return true;
          }
        }).map(n -> n.getKey()).collect(Collectors.toList());

    //
    nodeKeys2Remove.forEach(k -> ((ExtendedHGRootNodeImpl) rootNode).getIdToNodeMap().remove(k));
  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param rootNode
  // * @param dependencyQueries
  // * @param dependencyLoopMonitor
  // */
  // private void resolveSimpleDependencyQueries(final HGRootNode rootNode,
  // List<Future<StatementResult>> dependencyQueries, SubMonitor dependencyLoopMonitor) {
  //
  // //
  // dependencyQueries.forEach((dependencyQuery) -> {
  //
  // try {
  // SubMonitor iterationMonitor = dependencyLoopMonitor != null ? dependencyLoopMonitor.split(1) : null;
  //
  // // request dependencies
  // report(iterationMonitor, "Requesting dependencies...");
  //
  // List<GraphFactoryFunctions.Neo4jRelationship> dependencyDefinitions = dependencyQuery.get()
  // .list(record -> new Neo4jRelationship(record.get(0).asLong(), record.get(1).asLong(),
  // record.get(2).asLong(), record.get(3).asString()));
  //
  // // create dependencies
  // report(iterationMonitor, "Creating dependencies...");
  //
  // //
  // createDependencies(dependencyDefinitions, rootNode,
  // (id, type) -> GraphFactoryFunctions.createDependencySource(id, type, null), false, false, iterationMonitor);
  //
  // } catch (Exception e) {
  // throw new HierarchicalGraphMappingException(e);
  // }
  // });
  // }

  // // TODO
  // private void resolveAggregatedDependencyQueries(final HGRootNode rootNode,
  // List<AggregatedDependencyQueryHolder> dependencyQueries, SubMonitor dependencyLoopMonitor) {
  //
  // //
  // dependencyQueries.forEach((dependencyQuery) -> {
  //
  // try {
  // SubMonitor iterationMonitor = dependencyLoopMonitor != null ? dependencyLoopMonitor.split(1) : null;
  //
  // // request dependencies
  // report(iterationMonitor, "Requesting dependencies...");
  //
  // //
  // List<GraphFactoryFunctions.Neo4jRelationship> neo4jRelationships = dependencyQuery.getFuture().get()
  // .list(record -> new Neo4jRelationship(record.get(0).asLong(), record.get(1).asLong(),
  // record.get(2).asLong(), record.get(3).asString()));
  //
  // // create dependencies
  // report(iterationMonitor, "Creating dependencies...");
  // createDependencies(neo4jRelationships, rootNode,
  // (id, type) -> GraphFactoryFunctions.createDependencySource(id, type,
  // dependencyQueries.size() > 1 ? dependencyQuery.getAggregatedDependencyQuery() : null),
  // true, false, iterationMonitor);
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

  // /**
  // * <p>
  // * </p>
  // *
  // * @param rootNode
  // * @param hierachyQueries
  // * @param hierarchyLoopMonitor
  // */
  // private void resolveHierarchyQueries(final HGRootNode rootNode, List<Future<StatementResult>> hierachyQueries,
  // SubMonitor hierarchyLoopMonitor) {
  //
  // //
  // hierachyQueries.forEach((f) -> {
  //
  // try {
  // SubMonitor iterationMonitor = hierarchyLoopMonitor != null ? hierarchyLoopMonitor.split(1) : null;
  // report(iterationMonitor, "Requesting nodes...");
  //
  // Long[][] hierarchyElementIds = f.get()
  // .list(record -> new Long[] { record.get(0).asLong(), record.get(1).asLong() }).toArray(new Long[0][0]);
  //
  // report(iterationMonitor, "Creating nodes...");
  //
  // createHierarchy(hierarchyElementIds, rootNode, createNodeSourceFunction, iterationMonitor);
  // } catch (Exception e) {
  // throw new HierarchicalGraphMappingException(e);
  // }
  // });
  // }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param rootNode
  // * @param rootQueries
  // * @param rootLoopMonitor
  // */
  // private void resolveRootQueries(final HGRootNode rootNode, List<Future<StatementResult>> rootQueries,
  // SubMonitor rootLoopMonitor) {
  //
  // //
  // rootQueries.forEach((f) -> {
  //
  // try {
  // SubMonitor iterationMonitor = rootLoopMonitor != null ? rootLoopMonitor.split(1) : null;
  // report(iterationMonitor, "Requesting root nodes...");
  //
  // //
  // List<Long> rootNodes = f.get().list(record -> record.get(0).asLong());
  //
  // //
  // report(iterationMonitor, "Creating root nodes...");
  // createFirstLevelElements(rootNodes.toArray(new Long[0]), rootNode, createNodeSourceFunction, iterationMonitor);
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

  // /**
  // * <p>
  // * </p>
  // *
  // * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
  // */
  // private class AggregatedDependencyQueryHolder {
  //
  // /** - */
  // private Future<StatementResult> _future;
  //
  // /** - */
  // private AggregatedDependencyQuery _aggregatedDependencyQuery;
  //
  // /**
  // * <p>
  // * Creates a new instance of type {@link AggregatedDependencyQuery}.
  // * </p>
  // *
  // * @param aggregatedDependencyQuery
  // * @param future
  // */
  // public AggregatedDependencyQueryHolder(AggregatedDependencyQuery aggregatedDependencyQuery,
  // Future<StatementResult> future) {
  // this._future = checkNotNull(future);
  // this._aggregatedDependencyQuery = checkNotNull(aggregatedDependencyQuery);
  // }
  //
  // /**
  // * <p>
  // * </p>
  // *
  // * @return
  // */
  // public Future<StatementResult> getFuture() {
  // return _future;
  // }
  //
  // /**
  // * <p>
  // * </p>
  // *
  // * @return
  // */
  // public AggregatedDependencyQuery getAggregatedDependencyQuery() {
  // return _aggregatedDependencyQuery;
  // }
  // }
}
