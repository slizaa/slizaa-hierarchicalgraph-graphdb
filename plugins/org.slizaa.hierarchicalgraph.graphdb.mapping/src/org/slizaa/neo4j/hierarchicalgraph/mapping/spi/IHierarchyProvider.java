package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IHierarchyProvider {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<Long> getToplevelNodeIds() throws Exception;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<Long[]> getParentChildNodeIds() throws Exception;
}
