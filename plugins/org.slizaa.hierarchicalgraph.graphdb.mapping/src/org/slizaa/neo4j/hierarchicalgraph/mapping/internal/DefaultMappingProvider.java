package org.slizaa.neo4j.hierarchicalgraph.mapping.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.hierarchicalgraph.spi.INodeComparator;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IDependencyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IHierarchyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultMappingProvider implements IMappingProvider {

  /** - */
  private IMappingProviderMetadata _metaData;

  /** - */
  private IHierarchyProvider       _hierarchyProvider;

  /** - */
  private IDependencyProvider      _dependencyProvider;

  /** - */
  private ILabelDefinitionProvider _labelProvider;

  /** - */
  private INodeComparator          _nodeComparator;

  /**
   * <p>
   * Creates a new instance of type {@link DelegatingMappingProvider}.
   * </p>
   *
   * @param metaInformation
   * @param hierarchyProvider
   * @param dependencyProvider
   * @param labelProvider
   * @param nodeComparator
   */
  public DefaultMappingProvider(IMappingProviderMetadata metaInformation, IHierarchyProvider hierarchyProvider,
      IDependencyProvider dependencyProvider, ILabelDefinitionProvider labelProvider, INodeComparator nodeComparator) {

    this._metaData = checkNotNull(metaInformation);
    this._hierarchyProvider = checkNotNull(hierarchyProvider);
    this._dependencyProvider = checkNotNull(dependencyProvider);
    this._labelProvider = checkNotNull(labelProvider);
    this._nodeComparator = checkNotNull(nodeComparator);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IMappingProviderMetadata getMetaInformation() {
    return this._metaData;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHierarchyProvider getHierarchyProvider() {
    return this._hierarchyProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IDependencyProvider getDependencyProvider() {
    return this._dependencyProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ILabelDefinitionProvider getLabelDefinitionProvider() {
    return this._labelProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public INodeComparator getNodeComparator() {
    return this._nodeComparator;
  }
}
