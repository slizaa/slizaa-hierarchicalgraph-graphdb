package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.function.Supplier;

import org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.DefaultMappingProviderService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.DefaultMappingService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class MappingFactory {

  /**
   * <p>
   * </p>
   *
   * @param mappingProviderSupplier
   * @return
   */
  public static IMappingProviderService newMappingProviderService(
      Supplier<List<IMappingProvider>> mappingProviderSupplier) {

    //
    return new DefaultMappingProviderService(checkNotNull(mappingProviderSupplier));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static IMappingService newMappingService() {

    //
    return new DefaultMappingService();
  }
}
