package org.slizaa.hierarchicalgraph.graphdb.ui.mappingsdialog;

import java.util.Map;

import org.slizaa.hierarchicalgraph.spi.INodeComparator;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultMappingProviderMetadata;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IDependencyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IHierarchyProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProviderMetadata;

public class DummyMappingProvider implements IMappingProvider {

  /** - */
  private IMappingProviderMetadata _metadata;

  /**
   * <p>
   * Creates a new instance of type {@link DummyMappingProvider}.
   * </p>
   *
   * @param identifier
   * @param name
   * @param description
   * @param categories
   */
  public DummyMappingProvider(String identifier, String name, String description, Map<String, String> categories) {
    _metadata = new DefaultMappingProviderMetadata(identifier, name, description, categories);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IMappingProviderMetadata getMetaInformation() {
    return _metadata;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHierarchyProvider getHierarchyProvider() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IDependencyProvider getDependencyProvider() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ILabelDefinitionProvider getLabelDefinitionProvider() {
    throw new UnsupportedOperationException();
  }

  @Override
  public INodeComparator getNodeComparator() {
    throw new UnsupportedOperationException();
  }
}
