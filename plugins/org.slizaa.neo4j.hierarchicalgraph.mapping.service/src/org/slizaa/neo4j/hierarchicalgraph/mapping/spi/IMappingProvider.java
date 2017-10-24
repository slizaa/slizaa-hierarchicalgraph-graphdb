package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

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
  ILabelProvider getLabelProvider();
}
