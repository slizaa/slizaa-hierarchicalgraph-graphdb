package org.slizaa.neo4j.hierarchicalgraph.ui;

import org.eclipse.jface.resource.ImageRegistry;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.spi.INodeLabelProvider;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.HierarchicalGraphMappingServiceFactory;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IHierarchicalGraphMappingService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultMappingProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IDependencyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IHierarchyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;
import org.slizaa.testfwk.ui.AbstractSlizaaUiTest;

public class HierarchicalGraphViewTest extends AbstractSlizaaUiTest {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /** - */
  private static HierarchicalGraphViewPart  _part;

  /**
   * <p>
   * </p>
   */
  @BeforeClass
  public static void createPart() {
    _part = openShell(new HierarchicalGraphViewPart());
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testMappingService() {

    IHierarchicalGraphMappingService mappingService = HierarchicalGraphMappingServiceFactory
        .newHierarchicalGraphMappingService();

    HGRootNode rootNode = mappingService.convert(createMappingProvider(), _boltClientConnection.getBoltClient(), null);
    rootNode.registerExtension(INodeLabelProvider.class,
        new MappingDescriptorBasedItemLabelProviderImpl(new SimpleJTypeLabelProvider(), new ImageRegistry(display())));

    workbenchModel().setRootNode(rootNode);

    displaySleep();
  }

  // /**
  // * <p>
  // * </p>
  // *
  // */
  // @Before
  // public void setup() {
  //
  // }
  //
  // @Override
  // protected void configureComposedAdapterFactory(ComposedAdapterFactory composedAdapterFactory) {
  // composedAdapterFactory.addAdapterFactory(new HierarchicalGraphUIItemProviderAdapterFactory());
  // }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private IMappingProvider createMappingProvider() {

    IHierarchyProvider hierarchyProvider = new SimpleJTypeHierarchyProvider(_boltClientConnection.getBoltClient());
    IDependencyProvider dependencyProvider = new SimpleJTypeDependencyProvider(_boltClientConnection.getBoltClient());
    ILabelDefinitionProvider labelProvider = new SimpleJTypeLabelProvider();

    return new DefaultMappingProvider(hierarchyProvider, dependencyProvider, labelProvider);
  }
}