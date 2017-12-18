package org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal;

import java.util.ArrayList;
import java.util.List;

import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IHierarchicalGraphMappingProviderService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultHierarchicalGraphMappingProviderService implements IHierarchicalGraphMappingProviderService {

  /** - */
  private List<IMappingProvider> _mappingProvider;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultHierarchicalGraphMappingProviderService}.
   * </p>
   */
  public DefaultHierarchicalGraphMappingProviderService() {

    //
    _mappingProvider = new ArrayList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IMappingProvider> getMappingProviders() {
    return _mappingProvider;
  }
}
