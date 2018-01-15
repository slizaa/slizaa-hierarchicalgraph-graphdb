package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service.DefaultMappingService;

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
