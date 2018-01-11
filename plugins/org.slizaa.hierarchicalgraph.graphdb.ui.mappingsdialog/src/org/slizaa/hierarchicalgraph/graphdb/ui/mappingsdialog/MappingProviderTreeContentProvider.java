package org.slizaa.hierarchicalgraph.graphdb.ui.mappingsdialog;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.slizaa.neo4j.hierarchicalgraph.mapping.service.IMappingProviderService;
import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
class MappingProviderTreeContentProvider implements ITreeContentProvider {

  /** - */
  final static String                         UNCATEGORIZED = MappingProviderTreeContentProvider.class.getName()
      + "###UNCATEGORIZED";

  /** - */
  private Map<String, List<IMappingProvider>> _mappingProviders;

  /**
   * <p>
   * Creates a new instance of type {@link MappingProviderTreeContentProvider}.
   * </p>
   *
   * @param mappingProviderService
   */
  public MappingProviderTreeContentProvider(IMappingProviderService mappingProviderService, String category) {

    //
    checkNotNull(mappingProviderService);
    checkNotNull(category);

    //
    this._mappingProviders = new HashMap<>();

    //
    for (IMappingProvider mappingProvider : mappingProviderService.getMappingProviders()) {

      // value
      String categoryValue = mappingProvider.getMetaInformation().getCategoryValue(category);

      //
      if (categoryValue == null) {
        categoryValue = UNCATEGORIZED;
      }

      //
      List<IMappingProvider> providerList = this._mappingProviders.computeIfAbsent(categoryValue,
          k -> new LinkedList<>());
      providerList.add(mappingProvider);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] getChildren(Object element) {
    return element instanceof String ? this._mappingProviders.get(element).toArray() : new Object[0];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getParent(Object element) {
    // TODO
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasChildren(Object element) {
    return getChildren(element).length > 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object[] getElements(Object element) {
    return this._mappingProviders.keySet().toArray();
  }

  @Override
  public void dispose() {
    // TODO
  }

  @Override
  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    // TODO
  }
}