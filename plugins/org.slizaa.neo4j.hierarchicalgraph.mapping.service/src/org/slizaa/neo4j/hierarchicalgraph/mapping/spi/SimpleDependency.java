package org.slizaa.neo4j.hierarchicalgraph.mapping.spi;

public class SimpleDependency extends AbstractDependency {

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
  public SimpleDependency(long idStart, long idTarget, long idRel, String type) {
    _idStart = idStart;
    _idTarget = idTarget;
    _idRel = idRel;
    _type = type;
  }

  public long getIdStart() {
    return _idStart;
  }

  public long getIdTarget() {
    return _idTarget;
  }

  public long getIdRel() {
    return _idRel;
  }

  public String getType() {
    return _type;
  }
}