package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

public interface IMappingProviderMetadata {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getIdentifier();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getName();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getDescription();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String[] getCategories();

  /**
   * <p>
   * </p>
   *
   * @param category
   * @return
   */
  String getCategoryValue(String category);
}
