package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultMappingProviderMetadata implements IMappingProviderMetadata {

  /** - */
  private String              _identifier;

  /** - */
  private String              _name;

  /** - */
  private String              _description;

  /** - */
  private Map<String, String> _categories;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultMappingProviderMetadata}.
   * </p>
   *
   * @param identifier
   * @param name
   */
  public DefaultMappingProviderMetadata(String identifier, String name) {
    this(identifier, name, null);
  }

  /**
   * <p>
   * Creates a new instance of type {@link DefaultMappingProviderMetadata}.
   * </p>
   *
   * @param identifier
   * @param name
   * @param description
   */
  public DefaultMappingProviderMetadata(String identifier, String name, String description) {
    _identifier = checkNotNull(identifier);
    _name = checkNotNull(name);
    _description = description;
    _categories = new HashMap<>();
  }

  /**
   * <p>
   * Creates a new instance of type {@link DefaultMappingProviderMetadata}.
   * </p>
   *
   * @param identifier
   * @param name
   * @param description
   * @param categories
   */
  public DefaultMappingProviderMetadata(String identifier, String name, String description,
      Map<String, String> categories) {
    _identifier = checkNotNull(identifier);
    _name = checkNotNull(name);
    _description = description;
    _categories = categories != null ? new HashMap<>(categories) : new HashMap<>();
  }

  /**
   * <p>
   * </p>
   *
   * @param categoryValues
   */
  public void setCategoryValues(Map<String, String> categoryValues) {
    _categories.clear();
    _categories.putAll(categoryValues);
  }

  /**
   * <p>
   * </p>
   *
   * @param description
   */
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

  @Override
  public String[] getCategories() {
    return _categories.keySet().toArray(new String[0]);
  }

  @Override
  public String getCategoryValue(String category) {
    return _categories.get(checkNotNull(category));
  }
}
