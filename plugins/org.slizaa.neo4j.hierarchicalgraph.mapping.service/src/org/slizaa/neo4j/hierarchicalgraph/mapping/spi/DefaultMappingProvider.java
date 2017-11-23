package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultMappingProvider implements IMappingProvider {

  /** - */
  private IHierarchyProvider  _hierarchyProvider;

  private IDependencyProvider _dependencyProvider;

  private ILabelDefinitionProvider      _labelProvider;

  /**
   * <p>
   * Creates a new instance of type {@link ${enclosing_type}}.
   * </p>
   */
  public DefaultMappingProvider(IHierarchyProvider hierarchyProvider, IDependencyProvider dependencyProvider,
      ILabelDefinitionProvider labelProvider) {
    _hierarchyProvider = checkNotNull(hierarchyProvider);
    _dependencyProvider = checkNotNull(dependencyProvider);
    _labelProvider = checkNotNull(labelProvider);
  }

  public IHierarchyProvider getHierarchyProvider() {
    return _hierarchyProvider;
  }

  public IDependencyProvider getDependencyProvider() {
    return _dependencyProvider;
  }

  public ILabelDefinitionProvider getLabelProvider() {
    return _labelProvider;
  }
}
