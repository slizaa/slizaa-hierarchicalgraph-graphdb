/**
 *
 */
package org.slizaa.graphdb.testfwk;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultMappingProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultMappingProviderMetadata;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class SimpleJTypeMappingProvider extends DefaultMappingProvider {

  public SimpleJTypeMappingProvider(Neo4jClient boltClient) {

    //
    super(new DefaultMappingProviderMetadata("Test", "Test"),
        new SimpleJTypeHierarchyProvider(checkNotNull(boltClient)), new SimpleJTypeDependencyProvider(boltClient),
        new SimpleJTypeLabelProvider(), new SimpleJTypeNodeComparator());
  }

}
