package org.slizaa.neo4j.hierarchicalgraph;

import org.junit.Before;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.neo4j.testfwk.AbstractRemoteRepositoryTest;

public class MappingTest extends AbstractRemoteRepositoryTest {

  /** - */
  private HGRootNode _rootNode;

  /**
   * {@inheritDoc}
   */
  @Before
  public void init() throws Exception {
    super.init();

    //
    _rootNode = createGraphFromDefaultMapping(getNeo4JRemoteRepository());
  }

  @Test
  public void testMapping() {

    System.out.println(_rootNode);
    _rootNode.getChildren().forEach(c1 -> {
      System.out.println(" - " + c1);
      c1.getChildren().forEach(c2 -> {
        System.out.println("   -- " + c2);
        c2.getChildren().forEach(c3 -> {
          System.out.println("     --- " + c3);
          c3.getChildren().forEach(c4 -> {
            System.out.println("         ~ " + c4);
            
          });
        });
      });
    });

    // //
    // HGNode pkg_omaiconversion = _rootNode.lookupNode(new Long(611));
    // HGNode pkg_omaimodelcommon = _rootNode.lookupNode(new Long(1634));
    //
    // //
    // HGAggregatedDependency aggregatedDependency = pkg_omaiconversion.getOutgoingDependenciesTo(pkg_omaimodelcommon);
    // assertThat(aggregatedDependency).isNotNull();
    // assertThat(aggregatedDependency.getAggregatedWeight()).isEqualTo(59);
    // assertThat(aggregatedDependency.getCoreDependencies().size()).isEqualTo(59);
    //
    // // resolve the dependency
    // aggregatedDependency.resolveProxyDependencies();
    // assertThat(aggregatedDependency.getCoreDependencies().size()).isEqualTo(59);
    //
    // //
    // for (HGCoreDependency dependency : aggregatedDependency.getCoreDependencies()) {
    // if (dependency instanceof HGProxyDependency) {
    // verify(_aggregatedDependencyResolver).resolveProxyDependency((HGProxyDependency) dependency);
    // }
    // }
    //
    // //
    // assertThat(aggregatedDependency.getAggregatedWeight()).isEqualTo(59);
    // assertThat(aggregatedDependency.getCoreDependencies().size()).isEqualTo(59);
  }
}
