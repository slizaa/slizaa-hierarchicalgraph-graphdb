package org.slizaa.graphdb.testfwk;

import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.AbstractLabelDefinitionProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinition.OverlayPosition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class SimpleJTypeLabelProvider extends AbstractLabelDefinitionProvider implements ILabelDefinitionProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  protected LabelDefinitionProcessor createLabelDefinitionProcessor() {

    //@formatter:off
    return exclusiveChoice().
        
        // Module
        when(nodeHasLabel("Module")).then(handleModule()).
        
        // Package
        when(nodeHasLabel("Directory")).then(handleDirectory()).
        
        // Resource
        when(nodeHasLabel("Resource")).then(handleResource()).
        
        // Type
        when(nodeHasLabel("Type")).then(handleType()).
    
        // all other nodes
        otherwise(setBaseImage(fromClasspath("icons/jar_obj.png")).
              and(setLabelText(propertyValue("name"))));
    
    //@formatter:on
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected LabelDefinitionProcessor handleModule() {
    return setBaseImage(fromClasspath("icons/jar_obj.png")).and(setLabelText(propertyValue("name")));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected LabelDefinitionProcessor handleDirectory() {

    //@formatter:off
    return exclusiveChoice().
        
        // Packages
        when(nodeHasLabel("Package")).
          then(setBaseImage(fromClasspath("icons/package_obj.png")).
           and(setLabelText(propertyValue("fqn", str -> str.replace('/', '.'))))).
        
        // Directories
        otherwise(setBaseImage(fromClasspath("icons/fldr_obj.png")).
              and(setLabelText(propertyValue("fqn"))));
    //@formatter:on
  }

  private LabelDefinitionProcessor handleResource() {

    //@formatter:off
    return executeAll(
        
        exclusiveChoice().
          when(nodeHasLabel("ClassFile")).then(setBaseImage(fromClasspath("icons/classf_obj.png"))).
          otherwise(setBaseImage(fromClasspath("icons/file_obj.png"))),
        
        setLabelText(propertyValue("name"))
    );
    //@formatter:on    
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected LabelDefinitionProcessor handleType() {

    //@formatter:off
    return executeAll(
        
        setLabelText(propertyValue("name")),
        
        when(nodeHasProperty("final")).
          then(setOverlayImage(fromClasspath("icons/class_obj.png"), OverlayPosition.TOP_RIGHT)),
        
        when(nodeHasLabel("Class")).
          then(setBaseImage(fromClasspath("icons/class_obj.png"))),
          
        when(nodeHasLabel("Annotation")).
          then(setBaseImage(fromClasspath("icons/annotation_obj.png"))),   
          
        when(nodeHasLabel("Enum")).
          then(setBaseImage(fromClasspath("icons/enum_obj.png"))),
          
        when(nodeHasLabel("Interface")).
          then(setBaseImage(fromClasspath("icons/int_obj.png")))  
        );
    //@formatter:on
  }

}
