/**
 * This class is generated by jOOQ
 */
package com.sonicle.webtop.contacts.jooq.tables.records;

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
public class ContactsTagsRecord extends org.jooq.impl.UpdatableRecordImpl<com.sonicle.webtop.contacts.jooq.tables.records.ContactsTagsRecord> implements org.jooq.Record2<java.lang.Integer, java.lang.String> {

	private static final long serialVersionUID = 1774021050;

	/**
	 * Setter for <code>contacts.contacts_tags.contact_id</code>.
	 */
	public void setContactId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>contacts.contacts_tags.contact_id</code>.
	 */
	public java.lang.Integer getContactId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>contacts.contacts_tags.tag_id</code>.
	 */
	public void setTagId(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>contacts.contacts_tags.tag_id</code>.
	 */
	public java.lang.String getTagId() {
		return (java.lang.String) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record2<java.lang.Integer, java.lang.String> key() {
		return (org.jooq.Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.String> fieldsRow() {
		return (org.jooq.Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.String> valuesRow() {
		return (org.jooq.Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return com.sonicle.webtop.contacts.jooq.tables.ContactsTags.CONTACTS_TAGS.CONTACT_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return com.sonicle.webtop.contacts.jooq.tables.ContactsTags.CONTACTS_TAGS.TAG_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getContactId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getTagId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContactsTagsRecord value1(java.lang.Integer value) {
		setContactId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContactsTagsRecord value2(java.lang.String value) {
		setTagId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContactsTagsRecord values(java.lang.Integer value1, java.lang.String value2) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContactsTagsRecord
	 */
	public ContactsTagsRecord() {
		super(com.sonicle.webtop.contacts.jooq.tables.ContactsTags.CONTACTS_TAGS);
	}

	/**
	 * Create a detached, initialised ContactsTagsRecord
	 */
	public ContactsTagsRecord(java.lang.Integer contactId, java.lang.String tagId) {
		super(com.sonicle.webtop.contacts.jooq.tables.ContactsTags.CONTACTS_TAGS);

		setValue(0, contactId);
		setValue(1, tagId);
	}
}
