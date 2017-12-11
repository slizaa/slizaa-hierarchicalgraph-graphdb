package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultMappingProviderMetaData implements IMappingProviderMetaData {

  private String _identifier;

  private String _name;

  private String _description;

  public DefaultMappingProviderMetaData(String identifier, String name) {
    _identifier = checkNotNull(identifier);
    _name = checkNotNull(name);
  }

  public DefaultMappingProviderMetaData(String identifier, String name, String description) {
    _identifier = checkNotNull(identifier);
    _name = checkNotNull(name);
    _description = description;
  }

  public void setDescription(String description) {
    _description = description;
  }

  @Override
  public String getIdentifier() {
    return _identifier;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public String getDescription() {
    return _description;
  }
}
