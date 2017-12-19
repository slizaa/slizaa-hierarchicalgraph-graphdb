package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.hierarchicalgraph.spi.INodeComparator;

public class DefaultMappingProvider implements IMappingProvider {

  /** - */
  private IMappingProviderMetadata _metaData;

  /** - */
  private IHierarchyProvider              _hierarchyProvider;

  private IDependencyProvider             _dependencyProvider;

  private ILabelDefinitionProvider        _labelProvider;

  private INodeComparator                 _nodeComparator;

  /**
   * <p>
   * Creates a new instance of type {@link ${enclosing_type}}.
   * </p>
   */
  public DefaultMappingProvider(IMappingProviderMetadata metaInformation, IHierarchyProvider hierarchyProvider,
      IDependencyProvider dependencyProvider, ILabelDefinitionProvider labelProvider, INodeComparator nodeComparator) {
    _metaData = checkNotNull(metaInformation);
    _hierarchyProvider = checkNotNull(hierarchyProvider);
    _dependencyProvider = checkNotNull(dependencyProvider);
    _labelProvider = checkNotNull(labelProvider);
    _nodeComparator = checkNotNull(nodeComparator);
  }

  public IMappingProviderMetadata getMetaInformation() {
    return _metaData;
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
