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

import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.VContact;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_CONTACTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CATEGORIES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS;
import com.sonicle.webtop.contacts.jooq.tables.records.ContactsRecord;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import static com.sonicle.webtop.core.jooq.core.Tables.CUSTOMERS;
import com.sonicle.webtop.core.sdk.UserProfile;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableField;

/**
 *
 * @author malbinola
 */
public class ContactDAO extends BaseDAO {
	private final static ContactDAO INSTANCE = new ContactDAO();
	public static ContactDAO getInstance() {
		return INSTANCE;
	}
	
	public Long getSequence(Connection con) throws DAOException {
		DSLContext dsl = getDSL(con);
		Long nextID = dsl.nextval(SEQ_CONTACTS);
		return nextID;
	}
	
	public List<VContact> viewOnBirthdayByDate(Connection con, LocalDate date) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		return dsl
			.select(
				CONTACTS.CONTACT_ID,
				CONTACTS.CATEGORY_ID,
				CONTACTS.FIRSTNAME,
				CONTACTS.LASTNAME
			)
			.select(
				CATEGORIES.DOMAIN_ID.as("category_domain_id"),
				CATEGORIES.USER_ID.as("category_user_id")
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS.BIRTHDAY.equal(date)
				.and(
					CONTACTS.REVISION_STATUS.equal(OContact.REV_STATUS_NEW)
					.or(CONTACTS.REVISION_STATUS.equal(OContact.REV_STATUS_MODIFIED))
				)
			)
			.orderBy(
				CONTACTS.LASTNAME.asc(),
				CONTACTS.FIRSTNAME.asc()
			)
			.fetchInto(VContact.class);
	}
	
	public List<VContact> viewOnAnniversaryByDate(Connection con, LocalDate date) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		return dsl
			.select(
				CONTACTS.CONTACT_ID,
				CONTACTS.CATEGORY_ID,
				CONTACTS.FIRSTNAME,
				CONTACTS.LASTNAME
			)
			.select(
				CATEGORIES.DOMAIN_ID.as("category_domain_id"),
				CATEGORIES.USER_ID.as("category_user_id")
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS.ANNIVERSARY.equal(date)
				.and(
					CONTACTS.REVISION_STATUS.equal(OContact.REV_STATUS_NEW)
					.or(CONTACTS.REVISION_STATUS.equal(OContact.REV_STATUS_MODIFIED))
				)
			)
			.orderBy(
				CONTACTS.LASTNAME.asc(),
				CONTACTS.FIRSTNAME.asc()
			)
			.fetchInto(VContact.class);
	}
	
	public List<VContact> viewByCategoryPattern(Connection con, int categoryId, String searchMode, String pattern) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		Condition searchCndt = null;
		if(searchMode.equals("lastname")) {
			//searchCndt = CONTACTS.LASTNAME.lower().like(query);
			searchCndt = CONTACTS.LASTNAME.lower().likeRegex(pattern);
			if(pattern.equals("^.*")) searchCndt = searchCndt.or(CONTACTS.LASTNAME.isNull());
		} else {
			searchCndt = CONTACTS.WORK_EMAIL.likeIgnoreCase(pattern)
				.or(CONTACTS.HOME_EMAIL.likeIgnoreCase(pattern))
				.or(CONTACTS.OTHER_EMAIL.likeIgnoreCase(pattern))
				.or(CONTACTS.COMPANY.likeIgnoreCase(pattern))
				.or(CONTACTS.SEARCHFIELD.likeIgnoreCase(pattern));
		}
		
		return dsl
			.select(
				CONTACTS.CONTACT_ID,
				CONTACTS.CATEGORY_ID,
				CONTACTS.IS_LIST,
				CONTACTS.SEARCHFIELD,
				CONTACTS.TITLE,
				CONTACTS.FIRSTNAME,
				CONTACTS.LASTNAME,
				CONTACTS.NICKNAME,
				CONTACTS.COMPANY,
				CONTACTS.FUNCTION,
				CONTACTS.WORK_ADDRESS,
				CONTACTS.WORK_CITY,
				CONTACTS.WORK_TELEPHONE,
				CONTACTS.WORK_MOBILE,
				CONTACTS.WORK_EMAIL,
				CONTACTS.HOME_TELEPHONE,
				CONTACTS.HOME_EMAIL,
				CONTACTS.BIRTHDAY
			)
			.select(
				CUSTOMERS.DESCRIPTION.as("company_as_customer"),
				CATEGORIES.DOMAIN_ID.as("category_domain_id"),
				CATEGORIES.USER_ID.as("category_user_id")
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(
					CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID)
			)
			.leftOuterJoin(CUSTOMERS).on(
					CONTACTS.COMPANY.equal(CUSTOMERS.CUSTOMER_ID)
			)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(
					CONTACTS.REVISION_STATUS.equal(OContact.REV_STATUS_NEW)
					.or(CONTACTS.REVISION_STATUS.equal(OContact.REV_STATUS_MODIFIED))
				)
				.and(
					searchCndt
				)
			)
			.orderBy(
				CONTACTS.LASTNAME.asc(),
				CONTACTS.FIRSTNAME.asc(),
				CONTACTS.COMPANY.asc()
			)
			.fetchInto(VContact.class);
	}
	
	private String patternizeWords(String text) {
		String[] tokens = StringUtils.split(text, " ");
		String s = "";
		for(String token : tokens) {
			s += "%" + token + "% ";
		}
		return StringUtils.trim(s);
	}
	
	public List<VContact> viewWorkRecipientsByOwnerQueryText(Connection con, UserProfile.Id ownerId, String queryText) throws DAOException {
		return viewRecipientsByFieldOwnerQueryText(con, CONTACTS.WORK_EMAIL, ownerId, queryText);
	}
	
	public List<VContact> viewHomeRecipientsByOwnerQueryText(Connection con, UserProfile.Id ownerId, String queryText) throws DAOException {
		return viewRecipientsByFieldOwnerQueryText(con, CONTACTS.HOME_EMAIL, ownerId, queryText);
	}
	
	public List<VContact> viewOtherRecipientsByOwnerQueryText(Connection con, UserProfile.Id ownerId, String queryText) throws DAOException {
		return viewRecipientsByFieldOwnerQueryText(con, CONTACTS.OTHER_EMAIL, ownerId, queryText);
	}
	
	private List<VContact> viewRecipientsByFieldOwnerQueryText(Connection con, TableField<ContactsRecord, String> emailField, UserProfile.Id ownerId, String queryText) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		String patt1 = null, patt2 = null, patt3 = null;
		if(StringUtils.contains(queryText, " ")) {
			patt1 = patternizeWords(queryText);
			patt2 = queryText;
		} else {
			patt1 = patternizeWords(queryText);
			patt2 = "%" + queryText;
		}
		patt3 = "%@" + queryText + "%";
		
		Condition searchCndt = null;
		searchCndt = CONTACTS.FIRSTNAME.likeIgnoreCase(patt1)
			.or(CONTACTS.LASTNAME.likeIgnoreCase(patt1)
			.or(emailField.likeIgnoreCase(patt1)
			.or(CONTACTS.COMPANY.likeIgnoreCase(patt2)
			.or(CUSTOMERS.DESCRIPTION.likeIgnoreCase(patt2)))));
		
		if(StringUtils.contains(queryText, "@")) {
			searchCndt = searchCndt.or(
				CONTACTS.WORK_EMAIL.likeIgnoreCase(patt3)
			);
		}
		
		return dsl
			.select(
				CONTACTS.IS_LIST,
				CONTACTS.FIRSTNAME,
				CONTACTS.LASTNAME,
				emailField
			)
			.select(
				CUSTOMERS.DESCRIPTION.as("company_as_customer")
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(
					CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID)
			)
			.leftOuterJoin(CUSTOMERS).on(
				CONTACTS.COMPANY.equal(CUSTOMERS.CUSTOMER_ID)
			)
			.where(
				CATEGORIES.DOMAIN_ID.equal(ownerId.getDomain())
					.and(CATEGORIES.USER_ID.equal(ownerId.getUser())
				)
				.and(
					CONTACTS.REVISION_STATUS.equal(OContact.REV_STATUS_NEW)
					.or(CONTACTS.REVISION_STATUS.equal(OContact.REV_STATUS_MODIFIED))
				)
				.and(
					emailField.isNotNull()
				)
				.and(
					searchCndt
				)
			)
			.orderBy(
				emailField.asc()
			)
			.fetchInto(VContact.class);
	}
	
	public OContact selectById(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CONTACTS)
			.where(CONTACTS.CONTACT_ID.equal(contactId))
			.fetchOneInto(OContact.class);
	}
	
	public int insert(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(OContact.REV_STATUS_NEW);
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence(0);
		ContactsRecord record = dsl.newRecord(CONTACTS, item);
		return dsl
			.insertInto(CONTACTS)
			.set(record)
			.execute();
	}
	
	public int batchInsert(Connection con, ArrayList<OContact> items, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		ArrayList<ContactsRecord> records = new ArrayList<>();
		for(OContact item : items) {
			item.setRevisionStatus(OContact.REV_STATUS_NEW);
			item.setRevisionTimestamp(revisionTimestamp);
			item.setRevisionSequence(0);
			records.add(dsl.newRecord(CONTACTS, item));
		}
		dsl.batchInsert(records).execute();
		return items.size();
	}
	
	public int update(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(OContact.REV_STATUS_MODIFIED);
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence(0);
		ContactsRecord record = dsl.newRecord(CONTACTS, item);
		return dsl
			.update(CONTACTS)
			.set(record)
			.where(
					CONTACTS.CONTACT_ID.equal(item.getContactId())
			)
			.execute();
	}
	
	public int updateList(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(OContact.REV_STATUS_MODIFIED);
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence(0);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.CATEGORY_ID, item.getCategoryId())
			.set(CONTACTS.REVISION_STATUS, item.getRevisionStatus())
			.set(CONTACTS.REVISION_TIMESTAMP, item.getRevisionTimestamp())
			.set(CONTACTS.SEARCHFIELD, item.getSearchfield())
			.set(CONTACTS.LASTNAME, item.getLastname())
			.where(
					CONTACTS.CONTACT_ID.equal(item.getContactId())
			)
			.execute();
	}
	
	public int updateCategory(Connection con, int contactId, int categoryId, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.CATEGORY_ID, categoryId)
			.set(CONTACTS.REVISION_STATUS, OContact.REV_STATUS_MODIFIED)
			.set(CONTACTS.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int updateRevision(Connection con, int contactId, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int updateRevisionStatus(Connection con, int contactId, String revisionStatus, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.REVISION_STATUS, revisionStatus)
			.set(CONTACTS.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int deleteByCategoryId(Connection con, int categoryId, boolean list) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS.IS_LIST.equal(list))
			)
			.execute();
	}
	
	public int logicDeleteById(Connection con, int contactId, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.REVISION_STATUS, OContact.REV_STATUS_DELETED)
			.set(CONTACTS.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS.CONTACT_ID.equal(contactId)
				.and(CONTACTS.REVISION_STATUS.notEqual(OContact.REV_STATUS_DELETED))
			)
			.execute();
	}
	
	public int logicDeleteByCategoryId(Connection con, int categoryId, boolean list, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.REVISION_STATUS, OContact.REV_STATUS_DELETED)
			.set(CONTACTS.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS.IS_LIST.equal(list))
				.and(CONTACTS.REVISION_STATUS.notEqual(OContact.REV_STATUS_DELETED))
			)
			.execute();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	public byte[] readPhoto(Connection con, int contactId) throws IOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			DSLContext dsl = getDSL(con);
			String sql = dsl
				.select(
					CONTACTS.PHOTO
				)
				.from(CONTACTS)
				.where(
						CONTACTS.CONTACT_ID.equal(contactId)
				)
				.getSQL();
			
			stmt = con.prepareStatement(sql);
			StatementUtils.setInt(stmt, 1, contactId);
			rs = stmt.executeQuery();
			if(!rs.next()) return null;
			
			LargeObjectManager lom = (con.unwrap(org.postgresql.PGConnection.class)).getLargeObjectAPI();
			LargeObject lo = lom.open(rs.getInt(1), LargeObjectManager.READ);
			byte buf[] = new byte[lo.size()];
			lo.read(buf, 0, lo.size());
			lo.close();
			return buf;
			
			//return (rs.next()) ? IOUtils.toByteArray(rs.getBinaryStream(1)) : null;
			
		} catch(SQLException ex) {
			throw new DAOException("Unable to read bytes", ex);
		} finally {
			RSUtils.closeQuietly(rs);
			StatementUtils.closeQuietly(stmt);
		}
	}
	*/
	
	/*
	public int writePhoto(Connection con, int contactId, byte[] bytes) throws IOException {
		PreparedStatement stmt = null;
		
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			String sql = "UPDATE contacts SET "
					+ "photo = ? "
					+ "WHERE (contact_id = ?)";
			
			stmt = con.prepareStatement(sql);
			StatementUtils.setBinaryStream(stmt, 1, bais, bytes.length);
			StatementUtils.setInt(stmt, 2, contactId);
			return stmt.executeUpdate();
			
		} catch(SQLException ex) {
			throw new DAOException("Unable to write bytes", ex);
		} finally {
			StatementUtils.closeQuietly(stmt);
		}
	}
	*/
}
