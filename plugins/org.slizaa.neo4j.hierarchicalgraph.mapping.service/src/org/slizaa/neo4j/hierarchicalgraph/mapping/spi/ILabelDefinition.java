package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.net.URL;

public interface ILabelDefinition {

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static enum OverlayPosition {
    TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT;
  }

  boolean hasBaseImage();

  URL getBaseImage();

  boolean hasOverlayImage(OverlayPosition overlayPosition);

  URL getOverlayImage(OverlayPosition overlayPosition);

  String getText();
}
