package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.hierarchicalgraph.spi.INodeComparator;

public class DefaultMappingProvider implements IMappingProvider {

  /** - */
  private IHierarchyProvider       _hierarchyProvider;

  private IDependencyProvider      _dependencyProvider;

  private ILabelDefinitionProvider _labelProvider;

  private INodeComparator          _nodeComparator;

  /**
   * <p>
   * Creates a new instance of type {@link ${enclosing_type}}.
   * </p>
   */
  public DefaultMappingProvider(IHierarchyProvider hierarchyProvider, IDependencyProvider dependencyProvider,
      ILabelDefinitionProvider labelProvider, INodeComparator nodeComparator) {
    _hierarchyProvider = checkNotNull(hierarchyProvider);
    _dependencyProvider = checkNotNull(dependencyProvider);
    _labelProvider = checkNotNull(labelProvider);
    _nodeComparator = checkNotNull(nodeComparator);
  }

  public IHierarchyProvider getHierarchyProvider() {
    return _hierarchyProvider;
  }

  public IDependencyProvider getDependencyProvider() {
    return _dependencyProvider;
  }

  public ILabelDefinitionProvider getLabelDefinitionProvider() {
    return _labelProvider;
  }

  public INodeComparator getNodeComparator() {
    return _nodeComparator;
  }
}
