package org.slizaa.hierarchicalgraph.graphdb.ui.hierarchicalgraphview;

import org.osgi.service.component.annotations.Component;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.spi.INodeLabelProvider;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IMappingParticipator;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

@Component
public class AdapterMappingParticipator implements IMappingParticipator {

  @Override
  public void postCreate(HGRootNode rootNode, IMappingProvider mappingDescriptor, Neo4jClient boltClient) {
    rootNode.registerExtension(INodeLabelProvider.class,
        new LabelDefinitionProvider2NoteLabelProviderAdapter(mappingDescriptor.getLabelDefinitionProvider()));
  }
}
