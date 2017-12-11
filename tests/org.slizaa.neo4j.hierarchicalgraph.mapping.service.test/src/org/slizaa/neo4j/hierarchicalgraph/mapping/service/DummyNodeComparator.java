package org.slizaa.neo4j.hierarchicalgraph.mapping.service;

import org.slizaa.hierarchicalgraph.spi.INodeComparator;

public class DummyNodeComparator implements INodeComparator {

  @Override
  public int category(Object element) {
    return 0;
  }

  @Override
  public int compare(Object e1, Object e2) {
    return 0;
  }
}
