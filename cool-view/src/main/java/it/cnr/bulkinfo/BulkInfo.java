package it.cnr.bulkinfo;

import it.cnr.bulkinfo.BulkInfoImpl.FieldProperty;
import it.cnr.bulkinfo.BulkInfoImpl.FieldPropertySet;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BulkInfo {
	// type
	String TYPE_ID = "bulkInfo";

	// Properties
	// TODO deprecated?
	String ATTR_SHORT_DESCRIPTION_ID = "shortDescription";
	String ATTR_LONG_DESCRIPTION_ID = "longDescription";
	String ATTR_CMIS_TYPE_NAME_ID = "cmisTypeName";
	String ATTR_CMIS_QUERY_NAME_ID = "cmisQueryName";
	String ATTR_CMIS_EXTENDS_NAME_ID = "cmisExtendsName";

	String PROP_CMIS_IMPLEMENTS_ID = "cmisImplementsName";

	// FieldProperty
	// TODO deprecated?
	String PROP_FIELD_PROPERTY_ID = "fieldProperty";
	String PROP_FORM_FIELD_PROPERTY_ID = "formFieldProperty";
	String PROP_COLUMN_FIELD_PROPERTY_ID = "columnFieldProperty";
	String PROP_FIND_FIELD_PROPERTY_ID = "findFieldProperty";
	String PROP_PRINT_FORM_FIELD_PROPERTY_ID = "printFieldProperty";

	// TODO deprecated?
	String PROP_FORM_ID = "form";
	String PROP_COLUMNSET_ID = "columnSet";
	String PROP_FREESEARCHSET_ID = "freeSearchSet";
	String PROP_PRINT_FORM_ID = "printForm";

	// TODO deprecated?
	String getShortDescription();

	// TODO deprecated?
	String getLongDescription();

	String getCmisTypeName();

	String getCmisQueryName();

	void setCmisTypeName(String cmisTypeName);

	void setCmisQueryName(String cmisQueryName);

	void completeFieldProperty(FieldProperty fieldproperty);

	void addCmisExtensionElement(String key, Boolean value);

	String getCmisExtendsName();
	void setCmisExtendsName(String parent);

	Map<String, Boolean> getCmisImplementsName();
	
	List<String> getCmisImplementsNameList();

	Map<String, FieldProperty> getFieldProperties();

	void completeWithParent(BulkInfo parent, boolean aspect);

	Map<String, FieldPropertySet> getForms();

	Map<String, FieldPropertySet> getPrintForms();

	Map<String, FieldPropertySet> getColumnSets();

	Map<String, FieldPropertySet> getFreeSearchSets();

	Collection<FieldProperty> getForm(String name);

	Collection<FieldProperty> getColumnSet(String name);

	Collection<FieldProperty> getFreeSearchSet(String name);

	Collection<FieldProperty> getPrintForm(String name);

	List<FieldProperty> getFieldPropertyByProperty(String property);

	Map<String, Object> getFieldPropertiesByProperty();

	String getId();
}
