/**
 * This class is generated by jOOQ
 */
package com.sonicle.webtop.contacts.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.3"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContactsCustomValues extends org.jooq.impl.TableImpl<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord> {

	private static final long serialVersionUID = -404440852;

	/**
	 * The reference instance of <code>contacts.contacts_custom_values</code>
	 */
	public static final com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues CONTACTS_CUSTOM_VALUES = new com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord> getRecordType() {
		return com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord.class;
	}

	/**
	 * The column <code>contacts.contacts_custom_values.contact_id</code>.
	 */
	public final org.jooq.TableField<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, java.lang.Integer> CONTACT_ID = createField("contact_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>contacts.contacts_custom_values.custom_field_id</code>.
	 */
	public final org.jooq.TableField<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, java.lang.String> CUSTOM_FIELD_ID = createField("custom_field_id", org.jooq.impl.SQLDataType.VARCHAR.length(22).nullable(false), this, "");

	/**
	 * The column <code>contacts.contacts_custom_values.string_value</code>.
	 */
	public final org.jooq.TableField<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, java.lang.String> STRING_VALUE = createField("string_value", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>contacts.contacts_custom_values.number_value</code>.
	 */
	public final org.jooq.TableField<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, java.lang.Double> NUMBER_VALUE = createField("number_value", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>contacts.contacts_custom_values.boolean_value</code>.
	 */
	public final org.jooq.TableField<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, java.lang.Boolean> BOOLEAN_VALUE = createField("boolean_value", org.jooq.impl.SQLDataType.BOOLEAN, this, "");

	/**
	 * The column <code>contacts.contacts_custom_values.date_value</code>.
	 */
	public final org.jooq.TableField<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, org.joda.time.DateTime> DATE_VALUE = createField("date_value", org.jooq.impl.SQLDataType.TIMESTAMP, this, "", new com.sonicle.webtop.core.jooq.DateTimeConverter());

	/**
	 * The column <code>contacts.contacts_custom_values.text_value</code>.
	 */
	public final org.jooq.TableField<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, java.lang.String> TEXT_VALUE = createField("text_value", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * Create a <code>contacts.contacts_custom_values</code> table reference
	 */
	public ContactsCustomValues() {
		this("contacts_custom_values", null);
	}

	/**
	 * Create an aliased <code>contacts.contacts_custom_values</code> table reference
	 */
	public ContactsCustomValues(java.lang.String alias) {
		this(alias, com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues.CONTACTS_CUSTOM_VALUES);
	}

	private ContactsCustomValues(java.lang.String alias, org.jooq.Table<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord> aliased) {
		this(alias, aliased, null);
	}

	private ContactsCustomValues(java.lang.String alias, org.jooq.Table<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, com.sonicle.webtop.contacts.jooq.Contacts.CONTACTS, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord> getPrimaryKey() {
		return com.sonicle.webtop.contacts.jooq.Keys.CONTACTS_CUSTOM_VALUES_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord>>asList(com.sonicle.webtop.contacts.jooq.Keys.CONTACTS_CUSTOM_VALUES_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.sonicle.webtop.contacts.jooq.tables.records.ContactsCustomValuesRecord, ?>>asList(com.sonicle.webtop.contacts.jooq.Keys.CONTACTS_CUSTOM_VALUES__CONTACTS_CUSTOM_VALUES_CONTACT_ID_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues as(java.lang.String alias) {
		return new com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues(alias, this);
	}

	/**
	 * Rename this table
	 */
	public com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues rename(java.lang.String name) {
		return new com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues(name, null);
	}
}