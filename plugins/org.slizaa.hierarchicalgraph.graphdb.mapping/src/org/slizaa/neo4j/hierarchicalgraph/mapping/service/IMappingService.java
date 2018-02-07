package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.internal.service.DefaultMappingService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IMappingService {

  /**
   * <p>
   * </p>
   * 
   * @param mappingDescriptor
   * @param repository
   *
   * @return
   * @throws MappingException
   */
  HGRootNode convert(IMappingProvider mappingProvider, Neo4jClient repository, IProgressMonitor progressMonitor)
      throws MappingException;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static IMappingService createHierarchicalgraphMappingService() {
    return new DefaultMappingService();
  }
}
