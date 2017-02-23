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

import com.sonicle.webtop.contacts.bol.OListRecipient;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_LIST_RECIPIENTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS;
import static com.sonicle.webtop.contacts.jooq.Tables.LIST_RECIPIENTS;
import com.sonicle.webtop.contacts.jooq.tables.records.ListRecipientsRecord;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

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
	
	public OListRecipient select(Connection con, Integer listRecipientId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(LIST_RECIPIENTS)
			.where(
				LIST_RECIPIENTS.LIST_RECIPIENT_ID.equal(listRecipientId)
			)
			.fetchOneInto(OListRecipient.class);
	}
	
	public List<OListRecipient> selectByContact(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(LIST_RECIPIENTS)
			.where(
				LIST_RECIPIENTS.CONTACT_ID.equal(contactId)
			)
			.orderBy(
				LIST_RECIPIENTS.RECIPIENT.asc()
			)
			.fetchInto(OListRecipient.class);
	}
	
	public int insert(Connection con, OListRecipient item) throws DAOException {
		DSLContext dsl = getDSL(con);
		ListRecipientsRecord record = dsl.newRecord(LIST_RECIPIENTS, item);
		return dsl
			.insertInto(LIST_RECIPIENTS)
			.set(record)
			.execute();
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
	
	public int deleteByContactId(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(LIST_RECIPIENTS)
			.where(
				LIST_RECIPIENTS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int deleteByCategoryId(Connection con, int categoryId) throws DAOException {
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
}
