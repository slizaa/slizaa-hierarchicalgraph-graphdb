package org.slizaa.neo4j.hierarchicalgraph.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.spi.INodeComparator;
import org.slizaa.hierarchicalgraph.spi.INodeLabelProvider;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IHierarchicalGraphMappingService;
import org.slizaa.workbench.model.SlizaaWorkbenchModel;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class LoadModelFromGraphDatabaseJob extends Job {

  /** - */
  private Neo4jClient                      _neo4jClient;

  /** - */
  private ISlizaaMappingDescriptor         _slizaaMappingDescriptor;

  /** - */
  private SlizaaWorkbenchModel             _workbenchModel;

  /** - */
  private IHierarchicalGraphMappingService _mappingService;

  /**
   * <p>
   * Creates a new instance of type {@link LoadModelFromGraphDatabaseJob}.
   * </p>
   *
   * @param neo4jClient
   * @param createHierarchicalGraphTreeAction
   *          TODO
   */
  public LoadModelFromGraphDatabaseJob(SlizaaWorkbenchModel workbenchModel,
      IHierarchicalGraphMappingService mappingService, Neo4jClient neo4jClient,
      ISlizaaMappingDescriptor slizaaMappingDescriptor) {
    super("Creating hierarchical graph");

    //
    _workbenchModel = checkNotNull(workbenchModel);
    _mappingService = checkNotNull(mappingService);

    //
    setUser(true);
    _neo4jClient = checkNotNull(neo4jClient);
    _slizaaMappingDescriptor = checkNotNull(slizaaMappingDescriptor);
  }

  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    //
    try {

      // convert the model
      HGRootNode rootNode = _mappingService.convert(_slizaaMappingDescriptor.getMappingDescriptor(), _neo4jClient,
          monitor);

      // set label provider
      rootNode.registerExtension(INodeLabelProvider.class,
          new MappingDescriptorBasedItemLabelProviderImpl(_slizaaMappingDescriptor));

      // set comparator
      // TODO
      rootNode.registerExtension(INodeComparator.class, new TEMPORARY_NodeComparator());

      _neo4jClient.setAssociatedHierarchicalGraph(rootNode);

      //
      _workbenchModel.setRootNode(rootNode);

    } catch (Exception e) {
      e.printStackTrace();
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          Throwable cause = e;
          while (cause.getCause() != null) {
            cause = cause.getCause();
          }
          MessageDialog.openError(Display.getDefault().getActiveShell(), "Error",
              "Cannot load model: " + cause.getMessage());
        }
      });
    }
    return Status.OK_STATUS;
  }
}