package org.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import org.slizaa.hierarchicalgraph.HGNode;

public interface INodeSorter {

  /**
   * <p>
   * </p>
   */
  int compare(HGNode node1, HGNode node2);
}
