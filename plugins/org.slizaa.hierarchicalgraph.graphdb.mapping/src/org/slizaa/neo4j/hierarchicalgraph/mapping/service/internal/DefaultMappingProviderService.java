package org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.function.Supplier;

import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IMappingProviderService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultMappingProviderService implements IMappingProviderService {

  /** - */
  private Supplier<List<IMappingProvider>> _mappingProviderSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultMappingProviderService}.
   * </p>
   *
   * @param mappingProviderSupplier
   */
  public DefaultMappingProviderService(Supplier<List<IMappingProvider>> mappingProviderSupplier) {

    //
    _mappingProviderSupplier = checkNotNull(mappingProviderSupplier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IMappingProvider> getMappingProviders() {
    return _mappingProviderSupplier.get();
  }
}
