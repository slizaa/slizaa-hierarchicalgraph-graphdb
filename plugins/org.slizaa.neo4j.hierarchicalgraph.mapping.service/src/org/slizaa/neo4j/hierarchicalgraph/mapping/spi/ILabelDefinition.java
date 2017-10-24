package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

public interface ILabelDefinition {

  String getBaseImage();

  String getOverlayTopRight();

  String getOverlayBottomRight();

  String getOverlayTopLeft();

  String getOverlayBottomLeft();

  String getText();
}
