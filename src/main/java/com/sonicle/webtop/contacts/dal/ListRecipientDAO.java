/* 
 * Copyright (C) 2014 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2014 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.dal;

import com.sonicle.commons.EnumUtils;
import com.sonicle.webtop.contacts.bol.OListRecipient;
import com.sonicle.webtop.contacts.bol.VListRecipient;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_LIST_RECIPIENTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CATEGORIES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS;
import static com.sonicle.webtop.contacts.jooq.Tables.LIST_RECIPIENTS;
import com.sonicle.webtop.contacts.jooq.tables.Contacts;
import com.sonicle.webtop.contacts.jooq.tables.records.ListRecipientsRecord;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactsListRecipient;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

/**
 *
 * @author malbinola
 */
public class ListRecipientDAO extends BaseDAO {
	private final static ListRecipientDAO INSTANCE = new ListRecipientDAO();
	public static ListRecipientDAO getInstance() {
		return INSTANCE;
	}

	public Long getSequence(Connection con) throws DAOException {
		DSLContext dsl = getDSL(con);
		Long nextID = dsl.nextval(SEQ_LIST_RECIPIENTS);
		return nextID;
	}
	
	public VListRecipient select(Connection con, Integer listRecipientId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
					LIST_RECIPIENTS.fields()
			)
			.select(
					CONTACTS.FIRSTNAME,
					CONTACTS.LASTNAME,
					CONTACTS.WORK_EMAIL,
					CONTACTS.HOME_EMAIL,
					CONTACTS.OTHER_EMAIL
			)
			.from(LIST_RECIPIENTS)
			.leftOuterJoin(CONTACTS).on(LIST_RECIPIENTS.RECIPIENT_CONTACT_ID.equal(CONTACTS.CONTACT_ID))
			.where(
				LIST_RECIPIENTS.LIST_RECIPIENT_ID.equal(listRecipientId)
			)
			.fetchOneInto(VListRecipient.class);
	}
	
	public List<VListRecipient> viewByContact(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
					LIST_RECIPIENTS.fields()
			)
			.select(
					CONTACTS.FIRSTNAME,
					CONTACTS.LASTNAME,
					CONTACTS.WORK_EMAIL,
					CONTACTS.HOME_EMAIL,
					CONTACTS.OTHER_EMAIL
			)
			.from(LIST_RECIPIENTS)
			.leftOuterJoin(CONTACTS).on(LIST_RECIPIENTS.RECIPIENT_CONTACT_ID.equal(CONTACTS.CONTACT_ID))
			.where(
				LIST_RECIPIENTS.CONTACT_ID.equal(contactId)
			)
			.orderBy(
				LIST_RECIPIENTS.RECIPIENT.asc()
			)
			.fetchInto(VListRecipient.class);
	}
	
	public List<VListRecipient> selectByProfileContact(Connection con, String domainId, String userId, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		Contacts contacts1=CONTACTS.as("CONTACTS1");
		return dsl
			.select(
					LIST_RECIPIENTS.fields()
			)
			.select(
					contacts1.FIRSTNAME,
					contacts1.LASTNAME,
					contacts1.WORK_EMAIL,
					contacts1.HOME_EMAIL,
					contacts1.OTHER_EMAIL
			)
			.from(LIST_RECIPIENTS)
			.join(CONTACTS).on(LIST_RECIPIENTS.CONTACT_ID.equal(CONTACTS.CONTACT_ID))
			.join(CATEGORIES).on(CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(contacts1).on(LIST_RECIPIENTS.RECIPIENT_CONTACT_ID.equal(contacts1.CONTACT_ID))
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
				.and(LIST_RECIPIENTS.CONTACT_ID.equal(contactId))
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				LIST_RECIPIENTS.RECIPIENT.asc()
			)
			.fetchInto(VListRecipient.class);
	}
	
	public int insert(Connection con, OListRecipient item) throws DAOException {
		DSLContext dsl = getDSL(con);
		ListRecipientsRecord record = dsl.newRecord(LIST_RECIPIENTS, item);
		return dsl
			.insertInto(LIST_RECIPIENTS)
			.set(record)
			.execute();
	}
	
	public int[] batchInsert(Connection con, int contactId, Collection<ContactsListRecipient> recipients) throws DAOException {
		DSLContext dsl = getDSL(con);
		BatchBindStep batch = dsl.batch(
			dsl.insertInto(LIST_RECIPIENTS, 
				LIST_RECIPIENTS.CONTACT_ID, 
				LIST_RECIPIENTS.RECIPIENT,
				LIST_RECIPIENTS.RECIPIENT_TYPE,
				LIST_RECIPIENTS.RECIPIENT_CONTACT_ID
			)
			.values((Integer)null, null, null, null)
		);
		for (ContactsListRecipient recipient : recipients) {
			batch.bind(
				contactId,
				recipient.getRecipient(),
				recipient.getRecipientType(),
				recipient.getRecipientContactId()
			);
		}
		return batch.execute();
	}
	
	public int update(Connection con, OListRecipient item) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(LIST_RECIPIENTS)
			.set(LIST_RECIPIENTS.RECIPIENT, item.getRecipient())
			.set(LIST_RECIPIENTS.RECIPIENT_TYPE, item.getRecipientType())
			.where(
				LIST_RECIPIENTS.LIST_RECIPIENT_ID.equal(item.getListRecipientId())
			)
			.execute();
	}
	
	public int delete(Connection con, int listRecipientId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(LIST_RECIPIENTS)
			.where(LIST_RECIPIENTS.LIST_RECIPIENT_ID.equal(listRecipientId))
			.execute();
	}
	
	public int deleteByContact(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(LIST_RECIPIENTS)
			.where(
				LIST_RECIPIENTS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	/*
	public int deleteByCategory(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(LIST_RECIPIENTS)
			.where(
				LIST_RECIPIENTS.CONTACT_ID.in(
					DSL.select(
						CONTACTS.CONTACT_ID
					)
					.from(CONTACTS)
					.where(
						CONTACTS.CATEGORY_ID.equal(categoryId)
					)
				)				
			)
			.execute();
	}
	*/
}
