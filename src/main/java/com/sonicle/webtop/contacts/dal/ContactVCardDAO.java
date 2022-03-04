/*
 * Copyright (C) 2018 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2018 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.dal;

import com.sonicle.webtop.contacts.bol.OContactVCard;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_VCARDS;
import com.sonicle.webtop.contacts.jooq.tables.records.ContactsVcardsRecord;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import org.jooq.DSLContext;

/**
 *
 * @author malbinola
 */
public class ContactVCardDAO extends BaseDAO {
	private final static ContactVCardDAO INSTANCE = new ContactVCardDAO();
	public static ContactVCardDAO getInstance() {
		return INSTANCE;
	}
	
	public String selectRawDataById(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_VCARDS.RAW_DATA
			)
			.from(CONTACTS_VCARDS)
			.where(
				CONTACTS_VCARDS.CONTACT_ID.equal(contactId)
			)
			.fetchOneInto(String.class);
	}
	
	public int insert(Connection con, int contactId, String rawData) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.insertInto(CONTACTS_VCARDS)
			.set(CONTACTS_VCARDS.CONTACT_ID, contactId)
			.set(CONTACTS_VCARDS.RAW_DATA, rawData)
			.execute();
	}
	
	public int update(Connection con, int contactId, String rawData) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS_VCARDS)
			.set(CONTACTS_VCARDS.RAW_DATA, rawData)
			.where(CONTACTS_VCARDS.CONTACT_ID.equal(contactId))
			.execute();
	}
	
	public int upsert(Connection con, int contactId, String rawData) throws DAOException {
		int ret = update(con, contactId, rawData);
		if (ret == 0) ret = insert(con, contactId, rawData);
		return ret;
	}
	
	public boolean hasVCardById(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.selectCount()
			.from(CONTACTS_VCARDS)
			.where(
				CONTACTS_VCARDS.CONTACT_ID.equal(contactId)
			)
			.fetchOne(0, Integer.class) == 1;
	}
	
	public OContactVCard selectById(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CONTACTS_VCARDS)
			.where(
				CONTACTS_VCARDS.CONTACT_ID.equal(contactId)
			)
			.fetchOneInto(OContactVCard.class);
	}
	
	public int insert(Connection con, OContactVCard item) throws DAOException {
		DSLContext dsl = getDSL(con);
		ContactsVcardsRecord record = dsl.newRecord(CONTACTS_VCARDS, item);
		return dsl
			.insertInto(CONTACTS_VCARDS)
			.set(record)
			.execute();
	}
	
	public int delete(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_VCARDS)
			.where(
				CONTACTS_VCARDS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	/*
	public int deleteByCategory(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_VCARDS)
			.where(
				CONTACTS_VCARDS.CONTACT_ID.in(
					DSL.select(
						CONTACTS.CONTACT_ID
					)
					.from(CONTACTS_)
					.where(
						CONTACTS.CATEGORY_ID.equal(categoryId)
					)
				)				
			)
			.execute();
	}
	*/
}
