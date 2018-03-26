/**
 *
 */
package org.slizaa.hierarchicalgraph.graphdb.mapping.spi.opencypher;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
  private IBoltClientAware _boltClientAware;

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
  public ProxyDependency(long idStart, long idTarget, long idRel, String type, IBoltClientAware boltClientAware) {
    super(idStart, idTarget, idRel, type);

    //
    this._boltClientAware = boltClientAware;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Function<HGProxyDependency, Future<List<IDependency>>> getResolveFunction() {
    return (proxyDependency) -> CompletableFuture.completedFuture(Collections.emptyList());
  }
}
