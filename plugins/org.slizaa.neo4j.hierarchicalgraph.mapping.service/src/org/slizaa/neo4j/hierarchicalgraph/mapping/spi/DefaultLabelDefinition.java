package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import java.net.URL;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultLabelDefinition implements ILabelDefinition {

  /** - */
  private URL    _baseImage          = null;

  /** - */
  private URL    _overlayTopRight    = null;

  /** - */
  private URL    _overlayBottomRight = null;

  /** - */
  private URL    _overlayTopLeft     = null;

  /** - */
  private URL    _overlayBottomLeft  = null;

  /** - */
  private String _text;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBaseImage() {
    return _baseImage != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getBaseImage() {
    return _baseImage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getOverlayTopRight() {
    return _overlayTopRight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getOverlayBottomRight() {
    return _overlayBottomRight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getOverlayTopLeft() {
    return _overlayTopLeft;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getOverlayBottomLeft() {
    return _overlayBottomLeft;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getText() {
    return _text;
  }

  public void setBaseImage(URL baseImage) {
    _baseImage = baseImage;
  }

  public void setOverlayTopRight(URL overlayTopRight) {
    _overlayTopRight = overlayTopRight;
  }

  public void setOverlayBottomRight(URL overlayBottomRight) {
    _overlayBottomRight = overlayBottomRight;
  }

  public void setOverlayTopLeft(URL overlayTopLeft) {
    _overlayTopLeft = overlayTopLeft;
  }

  public void setOverlayBottomLeft(URL overlayBottomLeft) {
    _overlayBottomLeft = overlayBottomLeft;
  }

  public void setText(String text) {
    _text = text;
  }
  
  
}
