/**
 *
 */
package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public interface IMappingParticipator {

  void postCreate(HGRootNode rootNode, IMappingProvider mappingDescriptor, Neo4jClient boltClient);
}
