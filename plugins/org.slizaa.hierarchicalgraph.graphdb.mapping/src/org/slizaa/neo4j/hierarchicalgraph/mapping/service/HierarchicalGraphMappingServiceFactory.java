package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.HierarchicalgraphMappingServiceImpl;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class HierarchicalGraphMappingServiceFactory {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static IHierarchicalGraphMappingService newHierarchicalGraphMappingService() {
    return new HierarchicalgraphMappingServiceImpl();
  }
}
