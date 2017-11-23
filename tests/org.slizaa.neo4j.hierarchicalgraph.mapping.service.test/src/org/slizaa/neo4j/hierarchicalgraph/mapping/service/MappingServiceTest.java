package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.algorithms.AdjacencyMatrix;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.HierarchicalgraphMappingServiceImpl;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultMappingProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IDependencyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IHierarchyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

public class MappingServiceTest {

  @ClassRule
  public static PredefinedGraphDatabaseRule _predefinedGraphDatabase = new PredefinedGraphDatabaseRule(TestDB.MAPSTRUCT,
      5001);

  @ClassRule
  public static BoltClientConnectionRule    _boltClientConnection    = new BoltClientConnectionRule("localhost", 5001);

  /**
   * <p>
   * </p>
   */
  @Test
  public void testMappingService() {

    IHierarchicalGraphMappingService mappingService = new HierarchicalgraphMappingServiceImpl();

    HGRootNode rootNode = mappingService.convert(createMappingProvider(), _boltClientConnection.getBoltClient(), null);

    assertThat(rootNode.getChildren()).hasSize(2);

    assertThat(rootNode.getChildren().get(0).getChildren()).hasSize(5);
    assertThat(rootNode.getChildren().get(1).getChildren()).hasSize(36);

    int[][] matrix = AdjacencyMatrix.computeAdjacencyMatrix(rootNode.getChildren().get(1).getChildren());

    AdjacencyMatrix.printMatrix(matrix);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private IMappingProvider createMappingProvider() {

    IHierarchyProvider hierarchyProvider = new SimpleJTypeHierarchyProvider(_boltClientConnection.getBoltClient());
    IDependencyProvider dependencyProvider = new SimpleJTypeDependencyProvider(_boltClientConnection.getBoltClient());
    ILabelDefinitionProvider labelProvider = new DummyLabelProvider();

    return new DefaultMappingProvider(hierarchyProvider, dependencyProvider, labelProvider);
  }
}
