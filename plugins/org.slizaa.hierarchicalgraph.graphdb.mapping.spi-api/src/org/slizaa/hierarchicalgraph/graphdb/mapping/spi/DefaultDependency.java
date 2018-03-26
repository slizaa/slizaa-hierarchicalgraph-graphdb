package org.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyProvider.IDependency;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultDependency implements IDependency {

  /** - */
  public long   _idStart;

  /** - */
  public long   _idTarget;

  /** - */
  public long   _idRel;

  /** - */
  public String _type;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractDependency}.
   * </p>
   *
   * @param idStart
   * @param idTarget
   * @param idRel
   * @param type
   */
  public DefaultDependency(long idStart, long idTarget, long idRel, String type) {
    this._idStart = idStart;
    this._idTarget = idTarget;
    this._idRel = idRel;
    this._type = type;
  }

  @Override
  public long getIdStart() {
    return this._idStart;
  }

  @Override
  public long getIdTarget() {
    return this._idTarget;
  }

  @Override
  public long getIdRel() {
    return this._idRel;
  }

  @Override
  public String getType() {
    return this._type;
  }
}