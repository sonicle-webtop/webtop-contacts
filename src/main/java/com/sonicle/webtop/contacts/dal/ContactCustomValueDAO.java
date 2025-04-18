/*
 * Copyright (C) 2020 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2020 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.dal;

import com.sonicle.webtop.contacts.bol.OContactCustomValue;
import static com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues.CONTACTS_CUSTOM_VALUES;
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
public class ContactCustomValueDAO extends BaseDAO {
	private final static ContactCustomValueDAO INSTANCE = new ContactCustomValueDAO();
	public static ContactCustomValueDAO getInstance() {
		return INSTANCE;
	}
	
	public List<OContactCustomValue> selectByContact(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_CUSTOM_VALUES.CONTACT_ID,
				CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID,
				CONTACTS_CUSTOM_VALUES.STRING_VALUE,
				CONTACTS_CUSTOM_VALUES.NUMBER_VALUE,
				CONTACTS_CUSTOM_VALUES.BOOLEAN_VALUE,
				CONTACTS_CUSTOM_VALUES.DATE_VALUE,
				CONTACTS_CUSTOM_VALUES.TEXT_VALUE
			)
			.from(CONTACTS_CUSTOM_VALUES)
			.where(
				CONTACTS_CUSTOM_VALUES.CONTACT_ID.equal(contactId)
			)
			.orderBy(
				CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID.asc()
			)
			.fetchInto(OContactCustomValue.class);
	}
	
	public int[] batchInsert(Connection con, Collection<OContactCustomValue> values) throws DAOException {
		if (values.isEmpty()) return new int[0];
		DSLContext dsl = getDSL(con);
		BatchBindStep batch = dsl.batch(
			dsl.insertInto(CONTACTS_CUSTOM_VALUES, 
				CONTACTS_CUSTOM_VALUES.CONTACT_ID, 
				CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID, 
				CONTACTS_CUSTOM_VALUES.STRING_VALUE, 
				CONTACTS_CUSTOM_VALUES.NUMBER_VALUE,
				CONTACTS_CUSTOM_VALUES.BOOLEAN_VALUE,
				CONTACTS_CUSTOM_VALUES.DATE_VALUE,
				CONTACTS_CUSTOM_VALUES.TEXT_VALUE
			)
			.values((String)null, null, null, null, null, null, null)
		);
		for (OContactCustomValue value : values) {
			batch.bind(
				value.getContactId(),
				value.getCustomFieldId(),
				value.getStringValue(),
				value.getNumberValue(),
				value.getBooleanValue(),
				value.getDateValue(),
				value.getTextValue()
			);
		}
		return batch.execute();
	}
	
	public int deleteByContactFields(Connection con, String contactId, Collection<String> customFieldIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_CUSTOM_VALUES)
			.where(
				CONTACTS_CUSTOM_VALUES.CONTACT_ID.equal(contactId)
				.and(CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID.in(customFieldIds))
			)
			.execute();
	}
}
