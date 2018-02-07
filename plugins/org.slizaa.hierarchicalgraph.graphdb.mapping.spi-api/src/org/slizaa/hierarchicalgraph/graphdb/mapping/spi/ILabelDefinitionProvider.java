package org.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import java.net.URL;

import org.slizaa.hierarchicalgraph.HGNode;

public interface ILabelDefinitionProvider {

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static enum OverlayPosition {
    TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT;
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public interface ILabelDefinition {

    /**
     * <p>
     * </p>
     *
     * @return
     */
    boolean hasBaseImage();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    URL getBaseImage();

    /**
     * <p>
     * </p>
     *
     * @param overlayPosition
     * @return
     */
    boolean hasOverlayImage(OverlayPosition overlayPosition);

    /**
     * <p>
     * </p>
     *
     * @param overlayPosition
     * @return
     */
    URL getOverlayImage(OverlayPosition overlayPosition);

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getText();
  }

  /**
   * <p>
   * </p>
   */
  ILabelDefinition getLabelDefinition(HGNode node);
}
