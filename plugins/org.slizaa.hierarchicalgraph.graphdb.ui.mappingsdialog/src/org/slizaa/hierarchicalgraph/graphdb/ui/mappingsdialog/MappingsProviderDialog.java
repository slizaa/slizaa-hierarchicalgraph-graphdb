package org.slizaa.hierarchicalgraph.graphdb.ui.mappingsdialog;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IMappingProviderService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MappingsProviderDialog extends TitleAreaDialog {

  /** - */
  private IMappingProviderService _mappingService;

  /**
   * <p>
   * Creates a new instance of type {@link MappingsProviderDialog}.
   * </p>
   *
   * @param parentShell
   * @param mappingProviderService
   */
  public MappingsProviderDialog(Shell parentShell, IMappingProviderService mappingProviderService) {
    super(parentShell);

    //
    _mappingService = checkNotNull(mappingProviderService);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create() {
    super.create();

    //
    setTitle("Select the graph mapping");
    setMessage("Select Mapping");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);

    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    GridLayout layout = new GridLayout(2, false);
    container.setLayout(layout);

    final TreeViewer tv = new TreeViewer(container, SWT.SINGLE | SWT.BORDER);
    tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
    tv.setContentProvider(new MappingProviderTreeContentProvider(_mappingService, "location"));
    tv.setLabelProvider(new MappingProviderLabelProvider());
    tv.setInput("root");

    return area;
  }

  @Override
  protected boolean isResizable() {
    return true;
  }

  @Override
  protected void okPressed() {
    super.okPressed();
  }
}