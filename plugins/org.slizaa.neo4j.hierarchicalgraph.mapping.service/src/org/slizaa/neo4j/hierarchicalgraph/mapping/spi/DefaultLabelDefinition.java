package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

public class DefaultLabelDefinition implements ILabelDefinition {

  private String _baseImage          = null;

  private String _overlayTopRight    = null;

  private String _overlayBottomRight = null;

  private String _overlayTopLeft     = null;

  private String _overlayBottomLeft  = null;

  private String _text;

  public String getBaseImage() {
    return _baseImage;
  }

  public String getOverlayTopRight() {
    return _overlayTopRight;
  }

  public String getOverlayBottomRight() {
    return _overlayBottomRight;
  }

  public String getOverlayTopLeft() {
    return _overlayTopLeft;
  }

  public String getOverlayBottomLeft() {
    return _overlayBottomLeft;
  }

  public String getText() {
    return _text;
  }

}
