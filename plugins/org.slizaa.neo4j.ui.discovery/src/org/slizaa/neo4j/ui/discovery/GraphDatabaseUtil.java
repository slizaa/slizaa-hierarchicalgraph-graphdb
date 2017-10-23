package org.slizaa.neo4j.ui.discovery;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class GraphDatabaseUtil {

  /**
   * <p>
   * </p>
   *
   * @return
   * @throws IOException
   */
  public static String getGraphDatabaseHomeDirectory() {

    //
    Bundle graphDatabaseBundle = getGraphDatabaseBundle();

    //
    if (graphDatabaseBundle != null) {
      try {
        return FileLocator.getBundleFile(graphDatabaseBundle).getAbsolutePath();
      } catch (IOException e) {
        return null;
      }
    }

    //
    return null;
  }

  /**
   * <p>
   * </p>
   */
  public static boolean isGraphDatabaseInstalled() {
    return getGraphDatabaseBundle() != null;
  }

  /**
   * <p>
   * </p>
   */
  public static Bundle getGraphDatabaseBundle() {

    //
    BundleContext context = FrameworkUtil.getBundle(GraphDatabaseUtil.class).getBundleContext();
    for (Bundle bundle : context.getBundles()) {
      // TODO
      if (bundle.getHeaders().get(Constants.SLIZAA_EXTENSIONS_URL) != null) {
        return bundle;
      }
    }

    //
    return null;
  }
}
