package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import java.util.List;

import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IMappingProviderService {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<IMappingProvider> getMappingProviders();
}
