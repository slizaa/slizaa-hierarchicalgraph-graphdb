/**
 *
 */
package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

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
