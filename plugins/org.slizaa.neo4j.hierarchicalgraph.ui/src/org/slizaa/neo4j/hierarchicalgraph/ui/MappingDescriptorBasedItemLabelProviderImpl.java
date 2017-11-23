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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.edit.provider.StyledString;
import org.eclipse.jface.resource.ImageRegistry;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.spi.INodeLabelProvider;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.DefaultLabelDefinition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinition;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.neo4j.hierarchicalgraph.ui.internal.OverlayImageRegistry;

public class MappingDescriptorBasedItemLabelProviderImpl implements INodeLabelProvider {

  /** - */
  private OverlayImageRegistry     _imageRegistry;

  /** - */
  private ILabelDefinitionProvider _labelDefinitionProvider;

  /**
   * <p>
   * Creates a new instance of type {@link MappingDescriptorBasedItemLabelProviderImpl}.
   * </p>
   *
   * @param labelDefinitionProvider
   * @param imageRegistry
   */
  public MappingDescriptorBasedItemLabelProviderImpl(ILabelDefinitionProvider labelDefinitionProvider,
      ImageRegistry imageRegistry) {
    _labelDefinitionProvider = checkNotNull(labelDefinitionProvider);
    _imageRegistry = new OverlayImageRegistry(checkNotNull(imageRegistry));
  }

  /**
   * <p>
   * Creates a new instance of type {@link MappingDescriptorBasedItemLabelProviderImpl}.
   * </p>
   *
   * @param labelDefinitionProvider
   */
  public MappingDescriptorBasedItemLabelProviderImpl(ILabelDefinitionProvider labelDefinitionProvider) {
    _labelDefinitionProvider = checkNotNull(labelDefinitionProvider);
    _imageRegistry = new OverlayImageRegistry();
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
    return getLabelDefinition(object).getText();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getImage(Object object) {

    ILabelDefinition labelDefinition = getLabelDefinition(object);

    //
    if (labelDefinition.hasBaseImage()) {
      return _imageRegistry.getOverlayImage(labelDefinition.getBaseImage(),
          new URL[] { labelDefinition.getOverlayTopLeft(), labelDefinition.getOverlayBottomRight(),
              labelDefinition.getOverlayBottomLeft(), labelDefinition.getOverlayBottomRight() });
    }

    //
    try {
      // TODO
      return _imageRegistry
          .getImage(new URL("platform:/plugin/org.slizaa.neo4j.hierarchicalgraph.ui/icons/HGNode.png"));
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param object
   * @return
   */
  private ILabelDefinition getLabelDefinition(Object object) {

    //
    if (object instanceof HGNode) {
      return _labelDefinitionProvider.getLabelDefinition((HGNode) object);
    }

    //
    return new DefaultLabelDefinition();
  }

}
