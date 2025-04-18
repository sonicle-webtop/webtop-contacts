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

import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_TAGS;
import com.sonicle.webtop.contacts.jooq.tables.ContactsTags;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.*;

/**
 *
 * @author malbinola
 */
public class ContactTagDAO extends BaseDAO {
	private final static ContactTagDAO INSTANCE = new ContactTagDAO();
	public static ContactTagDAO getInstance() {
		return INSTANCE;
	}
	
	public Set<String> selectTagsByContact(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_TAGS.TAG_ID
			)
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(contactId)
			)
			.orderBy(
				CONTACTS_TAGS.TAG_ID.asc()
			)
			.fetchSet(CONTACTS_TAGS.TAG_ID);
	}
	
	public Map<String, List<String>> selectTagsByContact(Connection con, Collection<String> contactIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_TAGS.CONTACT_ID,
				CONTACTS_TAGS.TAG_ID
			)
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.in(contactIds)
			)
			.orderBy(
				CONTACTS_TAGS.TAG_ID.asc()
			)
			.fetchGroups(CONTACTS_TAGS.CONTACT_ID, CONTACTS_TAGS.TAG_ID);
	}
	
	public int insert(Connection con, String contactId, String tagId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.insertInto(CONTACTS_TAGS)
			.set(CONTACTS_TAGS.CONTACT_ID, contactId)
			.set(CONTACTS_TAGS.TAG_ID, tagId)
			.execute();
	}
	
	public int[] batchInsert(Connection con, String contactId, Collection<String> tagIds) throws DAOException {
		if (tagIds.isEmpty()) return new int[0];
		DSLContext dsl = getDSL(con);
		BatchBindStep batch = dsl.batch(
			dsl.insertInto(CONTACTS_TAGS, 
				CONTACTS_TAGS.CONTACT_ID, 
				CONTACTS_TAGS.TAG_ID
			)
			.values((String)null, (String)null)
		);
		for (String tagId : tagIds) {
			batch.bind(
				contactId,
				tagId
			);
		}
		return batch.execute();
	}
	
	public int insertByCategory(Connection con, int categoryId, String tagId) throws DAOException {
		DSLContext dsl = getDSL(con);
		ContactsTags cnttgs1 = CONTACTS_TAGS.as("cnttgs1");
		return dsl
			.insertInto(CONTACTS_TAGS)
			.select(
				select(
					CONTACTS_.CONTACT_ID,
					val(tagId, String.class).as("tag_id")
				)
				.from(CONTACTS_)
				.where(
					CONTACTS_.CATEGORY_ID.equal(categoryId)
					.and(CONTACTS_.CONTACT_ID.notIn(
						select(
							cnttgs1.CONTACT_ID
						)
						.from(cnttgs1)
						.where(
							cnttgs1.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
							.and(cnttgs1.TAG_ID.equal(tagId))
						)
					))
				)
			)
			.execute();
		/*
		INSERT INTO contacts.contacts_tags_copy
		("contact_id", "tag_id")
		(
		SELECT
		contacts.contact_id,
		unnest(string_to_array('zzzz|6eW6JRhgTsEjPWLQEV3pqr', '|')) as tag_id
		from contacts
		where contacts.category_id IN (26111)
		)
		*/
		/*
		return dsl
			.insertInto(CONTACTS_TAGS)
			.select(
				select(
					CONTACTS_.CONTACT_ID,
					field("un1.*", String.class)
				)
				.from(
					CONTACTS,
					unnest(tagIds.toArray(new String[tagIds.size()])).as("un1")
				)
				.where(
					CONTACTS_.CATEGORY_ID.equal(categoryId)
					.and(
						CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
						.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
					)
				)
			)
			//FIXME: use JOOQ > 3.10.8
			//.onConflictDoNothing()
			.execute();
		*/
	}
	
	public int insertByCategoriesContacts(Connection con, Collection<Integer> categoryIds, Collection<String> contactIds, String tagId) throws DAOException {
		DSLContext dsl = getDSL(con);
		ContactsTags cnttgs1 = CONTACTS_TAGS.as("cnttgs1");
		return dsl
			.insertInto(CONTACTS_TAGS)
			.select(
				select(
					CONTACTS_.CONTACT_ID,
					val(tagId, String.class).as("tag_id")
				)
				.from(CONTACTS_)
				.where(
					CONTACTS_.CATEGORY_ID.in(categoryIds)
					.and(CONTACTS_.CONTACT_ID.in(contactIds))
					.and(CONTACTS_.CONTACT_ID.notIn(
						select(
							cnttgs1.CONTACT_ID
						)
						.from(cnttgs1)
						.where(
							cnttgs1.CONTACT_ID.in(contactIds)
							.and(cnttgs1.TAG_ID.equal(tagId))
						)
					))
				)
			)
			.execute();
		
		
		/*
		return dsl
			.insertInto(CONTACTS_TAGS)
			.select(
				select(
					CONTACTS_.CONTACT_ID,
					field("un1.*", String.class)
				)
				.from(
					CONTACTS,
					unnest(tagIds.toArray(new String[tagIds.size()])).as("un1")
				)
				.where(
					CONTACTS_.CATEGORY_ID.in(categoryIds)
					.and(CONTACTS_.CONTACT_ID.in(contactIds))
					.and(
						CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
						.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
					)
				)
			)
			//FIXME: use JOOQ > 3.10.8
			//.onConflictDoNothing()
			.execute();
		*/
	}
	
	public int delete(Connection con, String contactId, String tagId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(contactId)
				.and(CONTACTS_TAGS.TAG_ID.equal(tagId))
			)
			.execute();
	}
	
	public int deleteByIdTags(Connection con, String contactId, Collection<String> tagIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(contactId)
				.and(CONTACTS_TAGS.TAG_ID.in(tagIds))
			)
			.execute();
	}
	
	public int deleteByContact(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int deleteByCategory(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.in(
					select(
						CONTACTS_.CONTACT_ID
					)
					.from(CONTACTS_)
					.where(
						CONTACTS_.CATEGORY_ID.equal(categoryId)
					)
				)
			)
			.execute();
	}
	
	public int deleteByCategoryTags(Connection con, int categoryId, Collection<String> tagIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.in(
					select(
						CONTACTS_.CONTACT_ID
					)
					.from(CONTACTS_)
					.where(
						CONTACTS_.CATEGORY_ID.equal(categoryId)
					)
				)
				.and(CONTACTS_TAGS.TAG_ID.in(tagIds))
			)
			.execute();
	}
	
	public int deleteByCategoriesContacts(Connection con, Collection<Integer> categoryIds, Collection<String> contactIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.in(
					select(
						CONTACTS_.CONTACT_ID
					)
					.from(CONTACTS_)
					.where(
						CONTACTS_.CONTACT_ID.in(contactIds)
						.and(CONTACTS_.CATEGORY_ID.in(categoryIds))
					)
				)
			)
			.execute();
	}
	
	public int deleteByCategoriesContactsTags(Connection con, Collection<Integer> categoryIds, Collection<String> contactIds, Collection<String> tagIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.in(
					select(
						CONTACTS_.CONTACT_ID
					)
					.from(CONTACTS_)
					.where(
						CONTACTS_.CONTACT_ID.in(contactIds)
						.and(CONTACTS_.CATEGORY_ID.in(categoryIds))
					)
				)
				.and(CONTACTS_TAGS.TAG_ID.in(tagIds))
			)
			.execute();
	}
}
