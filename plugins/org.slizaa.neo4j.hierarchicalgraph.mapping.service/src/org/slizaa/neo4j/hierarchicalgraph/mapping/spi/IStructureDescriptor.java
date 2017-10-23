package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IStructureDescriptor {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<Long> getToplevelNodeNodeIds();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<Long[]> getParentChildNodeIds();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<AbstractDependency> getDependencies();
}
