/*
 * webtop-contacts is a WebTop Service developed by Sonicle S.r.l.
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
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * "Powered by Sonicle WebTop" logo. If the display of the logo is not reasonably
 * feasible for technical reasons, the Appropriate Legal Notices must display
 * the words "Powered by Sonicle WebTop".
 */
package com.sonicle.webtop.contacts.dal;

import com.sonicle.webtop.contacts.bol.OListRecipient;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_LIST_RECIPIENTS;
import static com.sonicle.webtop.contacts.jooq.Tables.LIST_RECIPIENTS;
import com.sonicle.webtop.contacts.jooq.tables.records.ListRecipientsRecord;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import java.util.List;
import org.jooq.DSLContext;

/**
 *
 * @author malbinola
 */
public class ListRecipientsDAO extends BaseDAO {
	private final static ListRecipientsDAO INSTANCE = new ListRecipientsDAO();

	public static ListRecipientsDAO getInstance() {
		return INSTANCE;
	}

	public Long getSequence(Connection con) throws DAOException {
		DSLContext dsl = getDSL(con);
		Long nextID = dsl.nextval(SEQ_LIST_RECIPIENTS);
		return nextID;
	}
	
	public OListRecipient select(Connection con, Integer uid) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(LIST_RECIPIENTS)
			.where(
					LIST_RECIPIENTS.UID.equal(uid)
			)
			.fetchOneInto(OListRecipient.class);
	}
	
	public List<OListRecipient> selectByList(Connection con, Integer listId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
				.select()
				.from(LIST_RECIPIENTS)
				.where(
						LIST_RECIPIENTS.LIST_ID.equal(listId)
				)
				.orderBy(
						LIST_RECIPIENTS.DISPLAY_NAME.asc(),
						LIST_RECIPIENTS.EMAIL.asc()
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
			.set(LIST_RECIPIENTS.EMAIL, item.getEmail())
			.set(LIST_RECIPIENTS.DISPLAY_NAME, item.getDisplayName())
			.set(LIST_RECIPIENTS.RECIPIENT_TYPE, item.getRecipientType())
			.where(
				LIST_RECIPIENTS.UID.equal(item.getUid())
			)
			.execute();
	}
	
	public int delete(Connection con, Integer uid) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
				.delete(LIST_RECIPIENTS)
				.where(LIST_RECIPIENTS.UID.equal(uid))
				.execute();
	}
	
	public int deleteByList(Connection con, Integer listId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
				.delete(LIST_RECIPIENTS)
				.where(LIST_RECIPIENTS.LIST_ID.equal(listId))
				.execute();
	}
}
