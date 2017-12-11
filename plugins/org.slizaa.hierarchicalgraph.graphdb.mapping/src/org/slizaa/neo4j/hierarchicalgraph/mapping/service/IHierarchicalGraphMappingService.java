package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.internal.HierarchicalgraphMappingServiceImpl;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IHierarchicalGraphMappingService {

  /**
   * <p>
   * </p>
   * 
   * @param mappingDescriptor
   * @param repository
   *
   * @return
   * @throws HierarchicalGraphMappingException
   */
  HGRootNode convert(IMappingProvider mappingProvider, Neo4jClient repository, IProgressMonitor progressMonitor)
      throws HierarchicalGraphMappingException;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static IHierarchicalGraphMappingService createHierarchicalgraphMappingService() {
    return new HierarchicalgraphMappingServiceImpl();
  }
}