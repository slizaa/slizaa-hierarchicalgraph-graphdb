package org.slizaa.hierarchicalgraph.graphdb.ui.mappingsdialog.actions;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.component.annotations.Component;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IHierarchicalGraphMappingService;
import org.slizaa.neo4j.hierarchicalgraph.ui.HierarchicalGraphViewPart;
import org.slizaa.neo4j.hierarchicalgraph.ui.LoadModelFromGraphDatabaseJob;
import org.slizaa.neo4j.hierarchicalgraph.ui.internal.mappingsdialog.MappingDescriptorUtil;
import org.slizaa.neo4j.hierarchicalgraph.ui.internal.mappingsdialog.SelectMappingDialog;
import org.slizaa.ui.tree.ISlizaaActionContribution;
import org.slizaa.workbench.model.SlizaaWorkbenchModel;

@Component
public class CreateHierarchicalGraphTreeAction implements ISlizaaActionContribution {

  @Inject SlizaaWorkbenchModel             _workbenchModel;

  @Inject IHierarchicalGraphMappingService _mappingService;

  @Inject
  private EPartService                     _partService;

  @Inject
  private MApplication                     _mApplication;

  @Override
  public String getParentGroupId() {
    return null;
  }

  @Override
  public int getRanking() {
    return 100;
  }

  @Override
  public boolean shouldShow(List<?> selection, Viewer viewer) {
    return selection.stream().allMatch(n -> n instanceof Neo4jClient);
  }

  @Override
  public boolean isEnabled(List<?> selection, Viewer viewer) {
    return selection.size() == 1 && selection.get(0) instanceof Neo4jClient
        && ((Neo4jClient) selection.get(0)).isConnected()
        && ((Neo4jClient) selection.get(0)).getAssociatedHierarchicalGraph() == null;
  }

  @Override
  public void execute(List<?> selection, Viewer viewer) {

    //
    Neo4jClient neo4jClient = (Neo4jClient) selection.get(0);

    SelectMappingDialog mappingDialog = new SelectMappingDialog(Display.getCurrent().getActiveShell(),
        MappingDescriptorUtil.getSlizaaMappingDescriptionContainer());

    // user pressed cancel
    if (mappingDialog.open() != Window.OK) {
      return;
    }

    //
    LoadModelFromGraphDatabaseJob myJob = new LoadModelFromGraphDatabaseJob(_workbenchModel, _mappingService,
        neo4jClient,
        mappingDialog.getMappingDescriptor());
    myJob.schedule();

    //
    Display.getDefault().asyncExec(new Runnable() {
      public void run() {
        MPart part = _partService.findPart(HierarchicalGraphViewPart.PART_ID);
        _partService.bringToTop(part);
      }
    });
  }

  @Override
  public String getLabel(List<?> selection) {
    return "New Graph... ";
  }

  @Override
  public String getImagePath(List<?> selection) {
    return null;
  }
}
