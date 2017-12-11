package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

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

  @Override
  public boolean hasOverlayImage(OverlayPosition overlayPosition) {
    switch (checkNotNull(overlayPosition)) {
    case TOP_RIGHT:
      return _overlayTopRight != null;
    case TOP_LEFT:
      return _overlayTopLeft != null;
    case BOTTOM_LEFT:
      return _overlayBottomLeft != null;
    case BOTTOM_RIGHT:
      return _overlayBottomRight != null;
    default:
      return false;
    }
  }

  @Override
  public URL getOverlayImage(OverlayPosition overlayPosition) {
    switch (checkNotNull(overlayPosition)) {
    case TOP_RIGHT:
      return _overlayTopRight;
    case TOP_LEFT:
      return _overlayTopLeft;
    case BOTTOM_LEFT:
      return _overlayBottomLeft;
    case BOTTOM_RIGHT:
      return _overlayBottomRight;
    default:
      throw new RuntimeException(String.format("Unknown overlay position '%s'.", overlayPosition));
    }
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

  public void setOverlayImage(URL image, OverlayPosition position) {
    switch (checkNotNull(position)) {
    case TOP_RIGHT:
      _overlayTopRight = image;
      break;
    case TOP_LEFT:
      _overlayTopLeft = image;
      break;
    case BOTTOM_LEFT:
      _overlayBottomLeft = image;
      break;
    case BOTTOM_RIGHT:
      _overlayBottomRight = image;
      break;
    default:
      throw new RuntimeException(String.format("Unknown overlay position '%s'.", position));
    }
  }

  public void setText(String text) {
    _text = text;
  }

}
