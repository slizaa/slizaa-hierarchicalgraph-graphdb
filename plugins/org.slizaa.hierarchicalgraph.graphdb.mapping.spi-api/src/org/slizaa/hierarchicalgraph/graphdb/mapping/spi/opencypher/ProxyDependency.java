/**
 *
 */
package org.slizaa.hierarchicalgraph.graphdb.mapping.spi.opencypher;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.slizaa.hierarchicalgraph.HGProxyDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.DefaultDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyProvider.IDependency;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyProvider.IProxyDependency;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProxyDependency extends DefaultDependency implements IProxyDependency {

  /** - */
  private Function<HGProxyDependency, Future<List<IDependency>>> _function;

  /**
   * <p>
   * Creates a new instance of type {@link ProxyDependency}.
   * </p>
   *
   * @param idStart
   * @param idTarget
   * @param idRel
   * @param type
   */
  public ProxyDependency(long idStart, long idTarget, long idRel, String type,
      Function<HGProxyDependency, Future<List<IDependency>>> function) {
    super(idStart, idTarget, idRel, type);

    //
    this._function = checkNotNull(function);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Function<HGProxyDependency, Future<List<IDependency>>> getResolveFunction() {
    return _function;
  }
}
