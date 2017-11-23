package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import org.slizaa.hierarchicalgraph.HGNode;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractLabelDefinitionProvider implements ILabelDefinitionProvider {

  /**
   * <p>
   * </p>
   *
   * @param object
   * @return
   */
  protected <T> T nodeSource(HGNode node, Class<T> t) {

    //
    if (node instanceof HGNode) {

      //
      Object result = ((HGNode) node).getNodeSource();
      if (t.isAssignableFrom(result.getClass())) {
        return (T) result;
      }

      //
      return null;
    }

    //
    return null;
  }
}
