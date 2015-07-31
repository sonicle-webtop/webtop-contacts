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

import com.sonicle.webtop.contacts.bol.OFolder;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_FOLDERS;
import static com.sonicle.webtop.contacts.jooq.Tables.FOLDERS;
import com.sonicle.webtop.contacts.jooq.tables.records.FoldersRecord;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import java.util.List;
import org.jooq.DSLContext;

/**
 *
 * @author malbinola
 */
public class FolderDAO extends BaseDAO {

	private final static FolderDAO INSTANCE = new FolderDAO();

	public static FolderDAO getInstance() {
		return INSTANCE;
	}

	public Long getSequence(Connection con) throws DAOException {
		DSLContext dsl = getDSL(con);
		Long nextID = dsl.nextval(SEQ_FOLDERS);
		return nextID;
	}
	
	public OFolder select(Connection con, Integer calendarId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(FOLDERS)
			.where(
					FOLDERS.FOLDER_ID.equal(calendarId)
			)
			.fetchOneInto(OFolder.class);
	}
	
	public List<OFolder> selectByDomain(Connection con, String domainId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
				.select()
				.from(FOLDERS)
				.where(
						FOLDERS.DOMAIN_ID.equal(domainId)
				)
				.orderBy(
						FOLDERS.BUILT_IN.desc(),
						FOLDERS.NAME.asc()
				)
				.fetchInto(OFolder.class);
	}
	
	public List<OFolder> selectByDomainUser(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
				.select()
				.from(FOLDERS)
				.where(
						FOLDERS.DOMAIN_ID.equal(domainId)
						.and(FOLDERS.USER_ID.equal(userId))
				)
				.orderBy(
						FOLDERS.BUILT_IN.desc(),
						FOLDERS.NAME.asc()
				)
				.fetchInto(OFolder.class);
	}
	
	public List<OFolder> selectByDomainUserIn(Connection con, String domainId, String userId, Integer[] folders) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
				.select()
				.from(FOLDERS)
				.where(
						FOLDERS.DOMAIN_ID.equal(domainId)
						.and(FOLDERS.USER_ID.equal(userId))
						.and(FOLDERS.FOLDER_ID.in(folders))
				)
				.orderBy(
						FOLDERS.BUILT_IN.desc(),
						FOLDERS.NAME.asc()
				)
				.fetchInto(OFolder.class);
	}
	
	public OFolder selectBuiltInByDomainUser(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(FOLDERS)
			.where(
					FOLDERS.DOMAIN_ID.equal(domainId)
					.and(FOLDERS.USER_ID.equal(userId))
					.and(FOLDERS.BUILT_IN.equal(true))
			)
			.fetchOneInto(OFolder.class);
	}

	public List<OFolder> selectNoBuiltInByDomainUser(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
				.select()
				.from(FOLDERS)
				.where(
						FOLDERS.DOMAIN_ID.equal(domainId)
						.and(FOLDERS.USER_ID.equal(userId))
						.and(FOLDERS.BUILT_IN.equal(false))
				)
				.orderBy(FOLDERS.NAME)
				.fetchInto(OFolder.class);
	}
	
	public int insert(Connection con, OFolder item) throws DAOException {
		DSLContext dsl = getDSL(con);
		FoldersRecord record = dsl.newRecord(FOLDERS, item);
		return dsl
			.insertInto(FOLDERS)
			.set(record)
			.execute();
	}
	
	public int update(Connection con, OFolder item) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(FOLDERS)
			.set(FOLDERS.NAME, item.getName())
			.set(FOLDERS.DESCRIPTION, item.getDescription())
			.set(FOLDERS.COLOR, item.getColor())
			.set(FOLDERS.SYNC, item.getSync())
			.set(FOLDERS.IS_DEFAULT, item.getIsDefault())
			.where(
				FOLDERS.FOLDER_ID.equal(item.getFolderId())
			)
			.execute();
	}
	
	public int update(Connection con, Integer folderId, FieldsMap fieldValues) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(FOLDERS)
			.set(fieldValues)
			.where(
				FOLDERS.FOLDER_ID.equal(folderId)
			)
			.execute();
	}
	
	public int resetIsDefaultByDomainUser(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(FOLDERS)
			.set(FOLDERS.IS_DEFAULT, false)
			.where(
				FOLDERS.DOMAIN_ID.equal(domainId)
				.and(FOLDERS.USER_ID.equal(userId))
			)
			.execute();
	}
	
	public int delete(Connection con, Integer folderId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
				.delete(FOLDERS)
				.where(FOLDERS.FOLDER_ID.equal(folderId))
				.execute();
	}
}
