/**
 */
package org.slizaa.neo4j.hierarchicalgraph.ui;

import org.eclipse.emf.ecore.EObject;

import org.slizaa.neo4j.hierarchicalgraph.mapping.spi.IMappingProvider;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ISlizaa Mapping Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.slizaa.neo4j.hierarchicalgraph.ui.ISlizaaMappingProvider#getMappingProvider <em>Mapping Provider</em>}</li>
 *   <li>{@link org.slizaa.neo4j.hierarchicalgraph.ui.ISlizaaMappingProvider#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.slizaa.neo4j.hierarchicalgraph.ui.ISlizaaMappingProvider#getName <em>Name</em>}</li>
 *   <li>{@link org.slizaa.neo4j.hierarchicalgraph.ui.ISlizaaMappingProvider#getDescription <em>Description</em>}</li>
 * </ul>
 *
 * @see org.slizaa.neo4j.hierarchicalgraph.ui.HierarchicalGraphUIPackage#getISlizaaMappingProvider()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ISlizaaMappingProvider extends EObject {
  /**
   * Returns the value of the '<em><b>Mapping Provider</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Mapping Provider</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mapping Provider</em>' attribute.
   * @see #setMappingProvider(IMappingProvider)
   * @see org.slizaa.neo4j.hierarchicalgraph.ui.HierarchicalGraphUIPackage#getISlizaaMappingProvider_MappingProvider()
   * @model dataType="org.slizaa.neo4j.hierarchicalgraph.ui.IMappingProvider"
   * @generated
   */
  IMappingProvider getMappingProvider();

  /**
   * Sets the value of the '{@link org.slizaa.neo4j.hierarchicalgraph.ui.ISlizaaMappingProvider#getMappingProvider <em>Mapping Provider</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Mapping Provider</em>' attribute.
   * @see #getMappingProvider()
   * @generated
   */
  void setMappingProvider(IMappingProvider value);

  /**
   * Returns the value of the '<em><b>Qualified Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Qualified Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Qualified Name</em>' attribute.
   * @see org.slizaa.neo4j.hierarchicalgraph.ui.HierarchicalGraphUIPackage#getISlizaaMappingProvider_QualifiedName()
   * @model changeable="false" volatile="true" derived="true"
   * @generated
   */
  String getQualifiedName();

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see org.slizaa.neo4j.hierarchicalgraph.ui.HierarchicalGraphUIPackage#getISlizaaMappingProvider_Name()
   * @model changeable="false" volatile="true" derived="true"
   * @generated
   */
  String getName();

  /**
   * Returns the value of the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Description</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Description</em>' attribute.
   * @see org.slizaa.neo4j.hierarchicalgraph.ui.HierarchicalGraphUIPackage#getISlizaaMappingProvider_Description()
   * @model changeable="false" volatile="true" derived="true"
   * @generated
   */
  String getDescription();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  String resolveImage(String relativeImagePath);

} // ISlizaaMappingProvider
