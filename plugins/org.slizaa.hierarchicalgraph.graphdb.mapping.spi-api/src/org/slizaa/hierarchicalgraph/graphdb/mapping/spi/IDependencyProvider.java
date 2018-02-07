package org.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import java.util.List;
import java.util.function.Function;

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
  public interface IProxyDependency {

    /**
     * <p>
     * </p>
     */
    public Function<List<IDependency>, IDependency> getResolveFunction();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<IDependency> getDependencies() throws Exception;
}
