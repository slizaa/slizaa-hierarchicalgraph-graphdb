/*******************************************************************************
 * Copyright (c) Gerd W�therich 2012-2016.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *    Gerd W�therich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
package org.slizaa.neo4j.hierarchicalgraph.ui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HGRootNode;
import org.slizaa.hierarchicalgraph.selection.NodeSelection;
import org.slizaa.hierarchicalgraph.selection.SelectionFactory;
import org.slizaa.hierarchicalgraph.spi.INodeComparator;
import org.slizaa.ui.shared.AbstractSlizaaWorkbenchModelComponent;
import org.slizaa.ui.shared.NodeComparator2ViewerComparatorAdapter;
import org.slizaa.ui.shared.context.RootObject;
import org.slizaa.ui.tree.SlizaaTreeViewerFactory;
import org.slizaa.workbench.model.SlizaaWorkbenchModel;

public class HierarchicalGraphViewPart extends AbstractSlizaaWorkbenchModelComponent {

  /** - */
  public static final String PART_ID = HierarchicalGraphViewPart.class.getName();

  @Inject
  private ESelectionService  _selectionService;

  /** - */
  private TreeViewer         _treeViewer;

  /**
   * <p>
   * </p>
   *
   * @param parent
   */
  @PostConstruct
  public void createComposite(Composite parent) {

    //
    GridLayoutFactory.fillDefaults().applyTo(parent);

    //
    createTreeViewer(parent);
    if (getWorkbenchModel().getRootNode() != null) {
      setRootNodeInViewer(getWorkbenchModel().getRootNode());
    }
  }

  @Override
  protected void handleRootNodeChanged(HGRootNode oldValue, HGRootNode newValue) {

    //
    if (oldValue == newValue) {
      return;
    }

    setRootNodeInViewer(newValue);
  }

  private void setRootNodeInViewer(final HGRootNode newValue) {
    if (_treeViewer != null && !_treeViewer.getTree().isDisposed()) {
      if (newValue == null) {
        _treeViewer.setInput(null);
        _treeViewer.setComparator(null);
      } else {
        _treeViewer.setInput(new RootObject(newValue));
        if (newValue.hasExtension(INodeComparator.class)) {
          _treeViewer
              .setComparator(new NodeComparator2ViewerComparatorAdapter(newValue.getExtension(INodeComparator.class)));
        }
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param parent
   * @param project
   * @return
   */
  private TreeViewer createTreeViewer(Composite parent) {

    _treeViewer = SlizaaTreeViewerFactory.createTreeViewer(parent, SWT.NO_BACKGROUND | SWT.NONE | SWT.MULTI, 2, null);

    // TODO: MOVE SORTER TO Graph module!!!
    _treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

      @Override
      public void selectionChanged(SelectionChangedEvent event) {

        //
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();

        if (_selectionService != null) {
          _selectionService.setSelection(selection.size() == 1 ? selection.getFirstElement() : selection.toArray());
        }

        //
        List<HGNode> rep = new ArrayList<>();
        for (Object s : selection.toList()) {
          if (!(s instanceof HGNode) || s instanceof HGRootNode) {
            rep.clear();
            break;
          } else {
            rep.add((HGNode) s);
          }
        }

        //
        NodeSelection nodeSelection = SelectionFactory.eINSTANCE.createNodeSelection();
        nodeSelection.getNodes().addAll(rep);
        getWorkbenchModel().setNodeSelection(nodeSelection);
      }
    });

    _treeViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {

      @Override
      public void selectionChanged(SelectionChangedEvent event) {

        //
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();

        if (_selectionService != null) {
          _selectionService.setPostSelection(selection.size() == 1 ? selection.getFirstElement() : selection.toArray());
        }
      }
    });

    //
    return _treeViewer;
  }
}
