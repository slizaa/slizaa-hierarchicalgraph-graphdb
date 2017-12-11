package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import org.slizaa.hierarchicalgraph.spi.INodeComparator;

public interface IMappingProvider {

  /**
   * <p>
   * </p>
   */
  IHierarchyProvider getHierarchyProvider();

  /**
   * <p>
   * </p>
   */
  IDependencyProvider getDependencyProvider();

  /**
   * <p>
   * </p>
   */
  ILabelDefinitionProvider getLabelDefinitionProvider();
  
  /**
   * <p>
   * </p>
   *
   * @return
   */
  INodeComparator getNodeComparator();
}
