package org.slizaa.neo4j.hierarchicalgraph.ui;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.slizaa.neo4j.hierarchicalgraph.ui.internal.mappingsdialog.MappingDescriptorUtil;
import org.slizaa.neo4j.hierarchicalgraph.ui.internal.mappingsdialog.SelectMappingDialog;

public class MappingSelectorUtil {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static ISlizaaMappingDescriptor openSelectMappingDescriptorDialog() {

    //
    SelectMappingDialog mappingDialog = new SelectMappingDialog(Display.getCurrent().getActiveShell(),
        MappingDescriptorUtil.getSlizaaMappingDescriptionContainer());

    // user pressed cancel
    if (mappingDialog.open() != Window.OK) {

      //
      return null;
    }
    //
    else {

      //
      return mappingDialog.getMappingDescriptor();
    }
  }
}
