package org.slizaa.hierarchicalgraph.graphdb.ui.mappingsdialog;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
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
  public MappingProviderTreeContentProvider(IMappingProviderService mappingProviderService,
      String category) {

    //
    checkNotNull(mappingProviderService);
    checkNotNull(category);

    //
    _mappingProviders = new HashMap<>();

    //
    for (IMappingProvider mappingProvider : mappingProviderService.getMappingProviders()) {

      // value
      String categoryValue = mappingProvider.getMetaInformation().getCategoryValue(category);

      //
      if (categoryValue == null) {
        categoryValue = UNCATEGORIZED;
      }

      //
      List<IMappingProvider> providerList = _mappingProviders.computeIfAbsent(categoryValue, k -> new LinkedList<>());
      providerList.add(mappingProvider);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getChildren(Object element) {
    return element instanceof String ? _mappingProviders.get(element).toArray() : new Object[0];
  }

  /**
   * {@inheritDoc}
   */
  public Object getParent(Object element) {
    // TODO
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasChildren(Object element) {
    return getChildren(element).length > 0;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getElements(Object element) {
    return _mappingProviders.keySet().toArray();
  }

  public void dispose() {
    // TODO
  }

  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    // TODO
  }
}