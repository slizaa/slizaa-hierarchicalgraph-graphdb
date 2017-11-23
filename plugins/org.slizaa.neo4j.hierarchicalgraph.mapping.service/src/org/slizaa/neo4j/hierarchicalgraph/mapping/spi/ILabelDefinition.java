package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.net.URL;

public interface ILabelDefinition {

  boolean hasBaseImage();

  URL getBaseImage();

  URL getOverlayTopRight();

  URL getOverlayBottomRight();

  URL getOverlayTopLeft();

  URL getOverlayBottomLeft();

  String getText();
}
