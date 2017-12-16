package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;
import org.slizaa.graphdb.testfwk.SimpleJTypeMappingProvider;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.algorithms.AdjacencyMatrix;
import org.slizaa.neo4j.graphdb.testfwk.BoltClientConnectionRule;
import org.slizaa.neo4j.graphdb.testfwk.PredefinedGraphDatabaseRule;
import org.slizaa.neo4j.graphdb.testfwk.TestDB;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.HierarchicalgraphMappingServiceImpl;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
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

    HGRootNode rootNode = mappingService.convert(new SimpleJTypeMappingProvider(_boltClientConnection.getBoltClient()),
        _boltClientConnection.getBoltClient(), null);

    assertThat(rootNode.getChildren()).hasSize(2);

    assertThat(rootNode.getChildren().get(0).getChildren()).hasSize(5);
    assertThat(rootNode.getChildren().get(1).getChildren()).hasSize(36);

    int[][] matrix = AdjacencyMatrix.computeAdjacencyMatrix(rootNode.getChildren().get(1).getChildren());

    AdjacencyMatrix.printMatrix(matrix);
  }
}
