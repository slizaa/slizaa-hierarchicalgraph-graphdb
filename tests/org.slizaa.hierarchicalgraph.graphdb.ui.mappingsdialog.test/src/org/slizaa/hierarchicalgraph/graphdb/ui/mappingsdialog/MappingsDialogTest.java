package org.slizaa.hierarchicalgraph.graphdb.ui.mappingsdialog;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slizaa.hierarchicalgraph.core.testfwk.ui.rules.ImageRegistryRule;
import org.slizaa.hierarchicalgraph.core.testfwk.ui.rules.SwtBotRule;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IMappingProviderService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class MappingsDialogTest {

  /** - */
  @ClassRule
  public static SwtBotRule        swtBotRule        = new SwtBotRule();

  @ClassRule
  public static ImageRegistryRule imageRegistryRule = new ImageRegistryRule(() -> swtBotRule.display());

  @Rule
  public MockitoRule              rule              = MockitoJUnit.rule();

  @Mock
  private IMappingProviderService _mappingProviderService;

  /**
   * <p>
   * </p>
   * @throws InterruptedException 
   */
  @Test
  public void testMappingsDialog() throws InterruptedException {

    //
    Shell shell = new Shell(Display.getDefault());
    
    //
    when(_mappingProviderService.getMappingProviders()).thenReturn(Arrays.asList(
        new DummyMappingProvider("test", "test", "description", Collections.singletonMap("location", "workspace")),
        new DummyMappingProvider("test", "test", "description", Collections.singletonMap("location", "provided"))));

    //
    MappingsProviderDialog dialog = new MappingsProviderDialog(shell, _mappingProviderService);
    dialog.setBlockOnOpen(false);
    dialog.open();

    //
    System.out.println("HURZ");
    
    SWTBot swtBot = new SWTBot();
    System.out.println(swtBot.button("OK").widget);
    System.out.println(swtBot.tree().widget);
    System.out.println(swtBot.tree().expandNode("workspace"));
    swtBot.button("OK").click();
    
  }
}
