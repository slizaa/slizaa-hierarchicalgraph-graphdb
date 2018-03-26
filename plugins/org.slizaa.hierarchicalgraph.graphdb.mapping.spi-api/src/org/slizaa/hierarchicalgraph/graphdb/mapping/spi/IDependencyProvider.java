package org.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.slizaa.hierarchicalgraph.HGProxyDependency;

public interface IDependencyProvider {

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public interface IDependency {

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public long getIdStart();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public long getIdTarget();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public long getIdRel();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public String getType();
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public interface IProxyDependency extends IDependency {

    /**
     * <p>
     * </p>
     */
    public Function<HGProxyDependency, Future<List<IDependency>>> getResolveFunction();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<IDependency> getDependencies() throws Exception;
}
