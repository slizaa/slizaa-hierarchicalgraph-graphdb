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

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.emf.edit.provider.StyledString;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.spi.INodeLabelProvider;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.ConditionalNodeVisualizationProperties;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.Expression;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.Function;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.NodeVisualizationDescriptor;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.NodeVisualizationProperties;
import org.slizaa.neo4j.hierarchicalgraph.mapping.dsl.mappingDsl.StringConstant;
import org.slizaa.neo4j.hierarchicalgraph.ui.internal.OverlayImageRegistry;
import org.slizaa.neo4j.hierarchicalgraph.ui.mapping.functions.FunctionServiceImpl;
import org.slizaa.neo4j.hierarchicalgraph.ui.mapping.functions.IFunctionService;

public class MappingDescriptorBasedItemLabelProviderImpl implements INodeLabelProvider {

  /** - */
  private OverlayImageRegistry     _imageRegistry;

  /** - */
  private ISlizaaMappingDescriptor _slizaaMappingDescriptor;

  /** - */
  private IFunctionService         _functionService;

  /**
   * <p>
   * Creates a new instance of type {@link MappingDescriptorBasedItemLabelProviderImpl}.
   * </p>
   *
   * @param slizaaMappingDescriptor
   */
  public MappingDescriptorBasedItemLabelProviderImpl(ISlizaaMappingDescriptor slizaaMappingDescriptor) {
    _slizaaMappingDescriptor = checkNotNull(slizaaMappingDescriptor);
    _imageRegistry = new OverlayImageRegistry();
    _functionService = new FunctionServiceImpl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getStyledText(Object object) {
    return new StyledString(getText(object));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getText(Object object) {

    //
    return getLabelDefinition(nodeSource(object)).text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getImage(Object object) {

    LabelDefinition labelDefinition = getLabelDefinition(nodeSource(object));

    //
    if (labelDefinition.hasBaseImage()) {
      return _imageRegistry.getOverlayImage(labelDefinition.baseImage, new String[] { labelDefinition.ovlTopLeft,
          labelDefinition.ovlTopRight, labelDefinition.ovlBottomLeft, labelDefinition.ovlBottomRight });
    }

    //
    return _imageRegistry.getImage("platform:/plugin/org.slizaa.neo4j.hierarchicalgraph.ui/icons/HGNode.png");
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private LabelDefinition getLabelDefinition(Neo4JBackedNodeSource nodeSource) {
    checkNotNull(nodeSource);

    // create default result
    LabelDefinition result = new LabelDefinition("Node " + nodeSource.getIdentifier());

    //
    if (_slizaaMappingDescriptor.getMappingDescriptor().getVisualisationDescriptor() != null) {

      // iterate over all descriptors
      for (NodeVisualizationDescriptor descriptor : _slizaaMappingDescriptor.getMappingDescriptor()
          .getVisualisationDescriptor().getNodeVisualizationDescriptors()) {

        // match check
        if (_functionService.evaluate(nodeSource.getNode(), descriptor.getCondition(), Boolean.TYPE)) {

          //
          evaluate(descriptor.getNodeVisualizationProperties(), result, nodeSource.getNode());

          // conditional
          for (ConditionalNodeVisualizationProperties conditional : descriptor
              .getConditionalNodeVisualizationProperties()) {

            if (_functionService.evaluate(nodeSource.getNode(), conditional.getFunction(), Boolean.TYPE)) {
              evaluate(conditional.getNodeVisualizationProperties(), result, nodeSource.getNode());
            }
          }
        }
      }
    }

    //
    return result;
  }

  /**
   * <p>
   * </p>
   *
   * @param properties
   * @param result
   * @param node
   */
  private void evaluate(NodeVisualizationProperties properties, LabelDefinition labelDefinition, HGNode node) {

    checkNotNull(properties);
    checkNotNull(labelDefinition);
    checkNotNull(node);

    // handle
    if (properties.getTextLabel() != null) {
      labelDefinition.setText(evaluateStringExpression(properties.getTextLabel(), node));
    }

    // handle
    if (properties.getBaseImage() != null) {
      labelDefinition.setBaseImage(
          _slizaaMappingDescriptor.resolveImage(evaluateStringExpression(properties.getBaseImage(), node)));
    }

    // handle
    if (properties.getOverlayImageBottomLeft() != null) {
      labelDefinition.setOvlBottomLeft(_slizaaMappingDescriptor
          .resolveImage(evaluateStringExpression(properties.getOverlayImageBottomLeft(), node)));
    }

    // handle
    if (properties.getOverlayImageBottomRight() != null) {
      labelDefinition.setOvlBottomRight(_slizaaMappingDescriptor
          .resolveImage(evaluateStringExpression(properties.getOverlayImageBottomRight(), node)));
    }

    // handle
    if (properties.getOverlayImageTopLeft() != null) {
      labelDefinition.setOvlTopLeft(
          _slizaaMappingDescriptor.resolveImage(evaluateStringExpression(properties.getOverlayImageTopLeft(), node)));
    }

    // handle
    if (properties.getOverlayImageTopRight() != null) {
      labelDefinition.setOvlTopRight(
          _slizaaMappingDescriptor.resolveImage(evaluateStringExpression(properties.getOverlayImageTopRight(), node)));
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param expression
   * @param node
   * @return
   */
  private String evaluateStringExpression(Expression expression, HGNode node) {

    //
    if (expression instanceof Function) {
      return _functionService.evaluate(node, (Function) expression, String.class);
    }
    //
    else if (expression instanceof StringConstant) {
      return ((StringConstant) expression).getValue();
    }

    return null;
  }

  /**
   * <p>
   * </p>
   *
   * @param object
   * @return
   */
  private Neo4JBackedNodeSource nodeSource(Object object) {
    return object instanceof HGNode ? (Neo4JBackedNodeSource) ((HGNode) object).getNodeSource() : null;
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class LabelDefinition {

    private String baseImage      = null;

    private String ovlTopRight    = null;

    private String ovlBottomRight = null;

    private String ovlTopLeft     = null;

    private String ovlBottomLeft  = null;

    private String text;

    public LabelDefinition(String text) {
      this.text = text;
    }

    public boolean hasBaseImage() {
      return this.baseImage != null;
    }

    public void setBaseImage(String baseImage) {
      this.baseImage = baseImage;
    }

    public void setOvlTopRight(String ovlTopRight) {
      this.ovlTopRight = ovlTopRight;
    }

    public void setOvlBottomRight(String ovlBottomRight) {
      this.ovlBottomRight = ovlBottomRight;
    }

    public void setOvlTopLeft(String ovlTopLeft) {
      this.ovlTopLeft = ovlTopLeft;
    }

    public void setOvlBottomLeft(String ovlBottomLeft) {
      this.ovlBottomLeft = ovlBottomLeft;
    }

    public void setText(String text) {
      this.text = text;
    }

    @Override
    public String toString() {
      return "LabelDefinition [text=" + text + ", baseImage=" + baseImage + ", ovlTopRight=" + ovlTopRight
          + ", ovlBottomRight=" + ovlBottomRight + ", ovlTopLeft=" + ovlTopLeft + ", ovlBottomLeft=" + ovlBottomLeft
          + "]";
    }

  }

}
