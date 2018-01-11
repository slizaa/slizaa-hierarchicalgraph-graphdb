package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.slizaa.hierarchicalgraph.spi.INodeComparator;
import org.slizaa.neo4j.hierarchicalgraph.mapping.internal.DefaultMappingProviderMetadata;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IMappingProvider {

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
        IDependencyProvider dependencyProvider, ILabelDefinitionProvider labelProvider,
        INodeComparator nodeComparator) {

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

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public interface IMappingProviderMetadata {

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getIdentifier();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getName();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getDescription();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String[] getCategories();

    /**
     * <p>
     * </p>
     *
     * @param category
     * @return
     */
    String getCategoryValue(String category);

    /**
     * <p>
     * </p>
     *
     * @param identifier
     *          has to be set
     * @param name
     *          has to be set
     * @param description
     *          may null
     * @param categories
     *          may null
     * @return
     */
    public static IMappingProviderMetadata createMetadata(String identifier, String name, String description,
        Map<String, String> categories) {

      return new DefaultMappingProviderMetadata(checkNotNull(identifier), checkNotNull(name), description, categories);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  IMappingProviderMetadata getMetaInformation();

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
