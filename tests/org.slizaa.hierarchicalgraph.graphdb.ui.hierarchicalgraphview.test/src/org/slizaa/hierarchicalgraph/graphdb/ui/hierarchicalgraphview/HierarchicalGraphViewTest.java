package org.slizaa.hierarchicalgraph.graphdb.ui.hierarchicalgraphview;

import org.eclipse.jface.resource.ImageRegistry;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.graphdb.testfwk.SimpleJTypeMappingProvider;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.core.testfwk.ui.AbstractSlizaaUiTest;
import org.slizaa.hierarchicalgraph.graphdb.ui.hierarchicalgraphview.HierarchicalGraphViewPart;
import org.slizaa.hierarchicalgraph.spi.INodeLabelProvider;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.MappingFactory;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IMappingService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

public class HierarchicalGraphViewTest extends AbstractSlizaaUiTest {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /**
   * <p>
   * </p>
   */
  @BeforeClass
  public static void createPart() {
    openShell(new HierarchicalGraphViewPart());
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testMappingService() {

    //
    IMappingService mappingService = MappingFactory
        .newMappingService();

    //
    IMappingProvider mappingProvider = new SimpleJTypeMappingProvider(_boltClientConnection.getBoltClient());

    HGRootNode rootNode = mappingService.convert(mappingProvider, _boltClientConnection.getBoltClient(), null);

    //
    rootNode.registerExtension(INodeLabelProvider.class, new LabelDefinitionProvider2NoteLabelProviderAdapter(
        mappingProvider.getLabelDefinitionProvider(), new ImageRegistry(display())));

    //
    rootNode.registerExtension(IMappingProvider.class, mappingProvider);

    //
    workbenchModel().setRootNode(rootNode);
  }
}