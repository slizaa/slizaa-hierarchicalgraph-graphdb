package org.slizaa.neo4j.hierarchicalgraph.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.slizaa.hierarchicalgraph.HGNode;
import org.slizaa.hierarchicalgraph.HierarchicalgraphPackage;
import org.slizaa.neo4j.dbadapter.Neo4jClient;
import org.slizaa.neo4j.hierarchicalgraph.Neo4JBackedRootNodeSource;
import org.slizaa.neo4j.hierarchicalgraph.Neo4jHierarchicalgraphPackage;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExtendedNeo4JBackedNodeSourceTrait {

  /** - */
  private static final String       BATCH_UPDATE_QUERY = "MATCH (p) where id(p) in { ids } RETURN p";

  /** - */
  private Neo4JBackedNodeSourceImpl _nodeSource;

  /**
   * <p>
   * Creates a new instance of type {@link ExtendedNeo4JBackedNodeSourceTrait}.
   * </p>
   *
   * @param nodeSource
   */
  public ExtendedNeo4JBackedNodeSourceTrait(Neo4JBackedNodeSourceImpl nodeSource) {
    _nodeSource = nodeSource;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public EMap<String, String> getProperties() {

    // lazy load...
    if (_nodeSource.properties == null) {
      reloadNodeAndProperties();
    }

    // return the result
    return _nodeSource.properties;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public EList<String> getLabels() {

    // lazy load...
    if (_nodeSource.labels == null) {
      reloadNodeAndProperties();
    }

    // return the result
    return _nodeSource.labels;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public void reloadNodeAndProperties() {

    Node node = getNeo4jClient().getNode((long) _nodeSource.getIdentifier());

    // request properties
    setLabels(node);

    // request properties
    setProperties(node);
  }

  /**
   * <p>
   * </p>
   */
  public void loadPropertiesAndLabelsForChildren() {
    batchUpdate(_nodeSource.getNode().getChildren());
  }

  /**
   * <p>
   * </p>
   *
   * @param nodeProperties
   * @return
   */
  private EList<String> setLabels(Node node) {
    checkNotNull(node);

    // lazy init
    if (_nodeSource.labels == null) {
      _nodeSource.labels = new EDataTypeUniqueEList<String>(String.class, _nodeSource,
          Neo4jHierarchicalgraphPackage.NEO4_JBACKED_NODE_SOURCE__LABELS);
    } else {
      _nodeSource.labels.clear();
    }

    node.labels().forEach((e) -> _nodeSource.labels.add(e));

    // return the result
    return _nodeSource.labels;
  }

  public void onExpand() {
    loadPropertiesAndLabelsForChildren();
  }

  public void onCollapse() {
  }

  public void onSelect() {

  }

  /**
   * <p>
   * </p>
   *
   * @param jsonObject
   * @return
   */
  private EMap<String, String> setProperties(Node node) {
    checkNotNull(node);

    // lazy init
    if (_nodeSource.properties == null) {
      _nodeSource.properties = new EcoreEMap<String, String>(HierarchicalgraphPackage.Literals.STRING_TO_STRING_MAP,
          org.slizaa.hierarchicalgraph.impl.StringToStringMapImpl.class, _nodeSource,
          Neo4jHierarchicalgraphPackage.NEO4_JBACKED_NODE_SOURCE__PROPERTIES);
    } else {
      // clear the properties first
      _nodeSource.properties.clear();
    }

    // re-populate
    node.asMap().entrySet().forEach((e) -> {
      _nodeSource.properties.put(e.getKey(), e.getValue().toString());
    });

    // return the result
    return _nodeSource.properties;
  }

  /**
   * <p>
   * </p>
   *
   * @param hgNodes
   */
  private void batchUpdate(List<HGNode> hgNodes) {

    Map<Long, HGNode> nodes = new HashMap<>();
    hgNodes.forEach((n) -> nodes.put((Long) n.getIdentifier(), n));

    // query
    Map<String, Object> params = new HashMap<>();
    params.put("ids", nodes.keySet());
    Future<StatementResult> result = getNeo4jClient().executeCypherQuery(BATCH_UPDATE_QUERY, params);

    try {

      result.get().forEachRemaining(record -> {

        Node node = record.get(0).asNode();

        HGNode hgNode = nodes.get(new Long(node.id()));

        // set the labels and properties
        ((ExtendedNeo4JBackedNodeSourceImpl) hgNode.getNodeSource()).getTrait().setLabels(node);
        ((ExtendedNeo4JBackedNodeSourceImpl) hgNode.getNodeSource()).getTrait().setProperties(node);

      });

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ExecutionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Neo4jClient getNeo4jClient() {

    //
    Neo4JBackedRootNodeSource rootNodeSource = (Neo4JBackedRootNodeSource) _nodeSource.getNode().getRootNode()
        .getNodeSource();

    //
    Neo4jClient neo4jClient = rootNodeSource.getRepository();
    checkNotNull(neo4jClient, "No bolt client set.");
    return neo4jClient ;
  }

  public boolean isAutoExpand() {
    // TODO
    return true;
  }
}
