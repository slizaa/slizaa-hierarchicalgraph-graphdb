package org.slizaa.hierarchicalgraph.graphdb.ui.mappingsdialog.internal;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

public class MappingSelectorUtil {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static IMappingProvider openSelectMappingDescriptorDialog() {

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
