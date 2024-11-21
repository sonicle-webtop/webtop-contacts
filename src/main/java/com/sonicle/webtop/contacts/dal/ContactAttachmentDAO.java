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

import com.sonicle.webtop.contacts.bol.OContactAttachment;
import com.sonicle.webtop.contacts.bol.OContactAttachmentData;
import com.sonicle.webtop.contacts.bol.VContactAttachmentWithBytes;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_ATTACHMENTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_ATTACHMENTS_DATA;
import com.sonicle.webtop.contacts.jooq.tables.records.ContactsAttachmentsRecord;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import org.joda.time.DateTime;
import org.jooq.DSLContext;

/**
 *
 * @author malbinola
 */
public class ContactAttachmentDAO extends BaseDAO {
	private final static ContactAttachmentDAO INSTANCE = new ContactAttachmentDAO();
	public static ContactAttachmentDAO getInstance() {
		return INSTANCE;
	}
	
	public List<OContactAttachment> selectByContact(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CONTACTS_ATTACHMENTS)
			.where(
				CONTACTS_ATTACHMENTS.CONTACT_ID.equal(contactId)
			)
			.orderBy(
				CONTACTS_ATTACHMENTS.FILENAME.asc()
			)
			.fetchInto(OContactAttachment.class);
	}
	
	public List<VContactAttachmentWithBytes> selectByContactWithBytes(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
					CONTACTS_ATTACHMENTS.CONTACT_ATTACHMENT_ID,
					CONTACTS_ATTACHMENTS.CONTACT_ID,
					CONTACTS_ATTACHMENTS.FILENAME,
					CONTACTS_ATTACHMENTS.MEDIA_TYPE,
					CONTACTS_ATTACHMENTS.REVISION_SEQUENCE,
					CONTACTS_ATTACHMENTS.REVISION_TIMESTAMP,
					CONTACTS_ATTACHMENTS.SIZE,
					CONTACTS_ATTACHMENTS_DATA.BYTES
			)
			.from(CONTACTS_ATTACHMENTS)
			.join(CONTACTS_ATTACHMENTS_DATA).on(CONTACTS_ATTACHMENTS.CONTACT_ATTACHMENT_ID.equal(CONTACTS_ATTACHMENTS_DATA.CONTACT_ATTACHMENT_ID))
			.where(
				CONTACTS_ATTACHMENTS.CONTACT_ID.equal(contactId)
			)
			.orderBy(
				CONTACTS_ATTACHMENTS.FILENAME.asc()
			)
			.fetchInto(VContactAttachmentWithBytes.class);
	}
	
	public OContactAttachment selectByIdContact(Connection con, String attachmentId, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CONTACTS_ATTACHMENTS)
			.where(
				CONTACTS_ATTACHMENTS.CONTACT_ATTACHMENT_ID.equal(attachmentId)
				.and(CONTACTS_ATTACHMENTS.CONTACT_ID.equal(contactId))
			)
			.fetchOneInto(OContactAttachment.class);
	}
	
	public int insert(Connection con, OContactAttachment item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence((short)0);
		ContactsAttachmentsRecord record = dsl.newRecord(CONTACTS_ATTACHMENTS, item);
		return dsl
			.insertInto(CONTACTS_ATTACHMENTS)
			.set(record)
			.execute();
	}
	
	public int update(Connection con, OContactAttachment item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionTimestamp(revisionTimestamp);
		return dsl
			.update(CONTACTS_ATTACHMENTS)
			.set(CONTACTS_ATTACHMENTS.FILENAME, item.getFilename())
			.set(CONTACTS_ATTACHMENTS.SIZE, item.getSize())
			.set(CONTACTS_ATTACHMENTS.MEDIA_TYPE, item.getMediaType())
			.where(
				CONTACTS_ATTACHMENTS.CONTACT_ATTACHMENT_ID.equal(item.getContactAttachmentId())
			)
			.execute();
	}
	
	public int deleteByIdContact(Connection con, String attachmentId, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_ATTACHMENTS)
			.where(
				CONTACTS_ATTACHMENTS.CONTACT_ATTACHMENT_ID.equal(attachmentId)
				.and(CONTACTS_ATTACHMENTS.CONTACT_ID.equal(contactId))
			)
			.execute();
	}
	
	public int deleteByIdsContact(Connection con, Collection<String> attachmentIds, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_ATTACHMENTS)
			.where(
				CONTACTS_ATTACHMENTS.CONTACT_ATTACHMENT_ID.in(attachmentIds)
				.and(CONTACTS_ATTACHMENTS.CONTACT_ID.equal(contactId))
			)
			.execute();
	}
	
	public OContactAttachmentData selectBytesById(Connection con, String attachmentId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_ATTACHMENTS_DATA.BYTES
			)
			.from(CONTACTS_ATTACHMENTS_DATA)
			.where(CONTACTS_ATTACHMENTS_DATA.CONTACT_ATTACHMENT_ID.equal(attachmentId))
			.fetchOneInto(OContactAttachmentData.class);
	}
	
	public int insertBytes(Connection con, String attachmentId, byte[] bytes) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.insertInto(CONTACTS_ATTACHMENTS_DATA)
			.set(CONTACTS_ATTACHMENTS_DATA.CONTACT_ATTACHMENT_ID, attachmentId)
			.set(CONTACTS_ATTACHMENTS_DATA.BYTES, bytes)
			.execute();
	}
	
	public int deleteBytes(Connection con, String attachmentId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_ATTACHMENTS_DATA)
			.where(CONTACTS_ATTACHMENTS_DATA.CONTACT_ATTACHMENT_ID.equal(attachmentId))
			.execute();
	}
}
