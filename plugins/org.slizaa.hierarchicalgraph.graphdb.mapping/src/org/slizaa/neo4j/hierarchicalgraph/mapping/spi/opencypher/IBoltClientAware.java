/**
 *
 */
package org.slizaa.neo4j.hierarchicalgraph.mapping.spi.opencypher;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.neo4j.dbadapter.Neo4jClient;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public interface IBoltClientAware {

  void initialize(Neo4jClient boltClient, IProgressMonitor progressMonitor) throws Exception;
}
