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
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.beans.SortInfo;
import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.OContactInfo;
import com.sonicle.webtop.contacts.bol.VContact;
import com.sonicle.webtop.contacts.bol.VContactObject;
import com.sonicle.webtop.contacts.bol.VContactObjectStat;
import com.sonicle.webtop.contacts.bol.VContactCompany;
import com.sonicle.webtop.contacts.bol.VContactHrefSync;
import com.sonicle.webtop.contacts.bol.VContactLookup;
import com.sonicle.webtop.contacts.bol.VContactObjectChanged;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_CONTACTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CATEGORIES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_PICTURES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_TAGS;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_VCARDS;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_ATTACHMENTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_CUSTOM_VALUES;
import static com.sonicle.webtop.contacts.jooq.Tables.HISTORY_CONTACTS;
import com.sonicle.webtop.contacts.jooq.tables.records.ContactsRecord;
import com.sonicle.webtop.contacts.model.ContactBase;
import com.sonicle.webtop.contacts.model.ContactType;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import static com.sonicle.webtop.core.jooq.core.Tables.MASTER_DATA;
import com.sonicle.webtop.core.model.RecipientFieldCategory;
import com.sonicle.webtop.core.model.RecipientFieldType;
import com.sonicle.webtop.core.sdk.WTException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.SelectLimitStep;
import org.jooq.SortField;
import org.jooq.impl.DSL;

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
				CONTACTS_.CONTACT_ID,
				CONTACTS_.CATEGORY_ID,
				CONTACTS_.DISPLAY_NAME,
				CONTACTS_.FIRSTNAME,
				CONTACTS_.LASTNAME
			)
			.select(
				CATEGORIES.DOMAIN_ID.as("category_domain_id"),
				CATEGORIES.USER_ID.as("category_user_id")
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS_.BIRTHDAY.equal(date)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				CONTACTS_.DISPLAY_NAME.asc()
			)
			.fetchInto(VContact.class);
	}
	
	public List<VContact> viewOnAnniversaryByDate(Connection con, LocalDate date) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.CATEGORY_ID,
				CONTACTS_.DISPLAY_NAME,
				CONTACTS_.FIRSTNAME,
				CONTACTS_.LASTNAME
			)
			.select(
				CATEGORIES.DOMAIN_ID.as("category_domain_id"),
				CATEGORIES.USER_ID.as("category_user_id")
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS_.ANNIVERSARY.equal(date)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				CONTACTS_.DISPLAY_NAME.asc()
			)
			.fetchInto(VContact.class);
	}
	
	/*

UPDATE contacts.contacts AS ccnts
SET
public_uid = md5(ccnts.public_uid || '.' || ccnts.contact_id) || '@' || cdoms.internet_name
FROM contacts.categories AS ccats, core.domains AS cdoms
WHERE (ccnts.category_id = ccats.category_id)
AND (ccats.domain_id = cdoms.domain_id)
AND (ccnts.href IS NULL)
//AND (ccats.provider = 'local')

UPDATE contacts.contacts AS ccnts
SET
href = '/carddav/addressbooks/' || ccats.user_id || '@' || cdoms.internet_name || '/contacts/' || ccnts.category_id || '/' || ccnts.public_uid
FROM contacts.categories AS ccats, core.domains AS cdoms
WHERE (ccnts.category_id = ccats.category_id)
AND (ccats.domain_id = cdoms.domain_id)
AND (ccnts.href IS NULL)
	//AND (ccats.provider = 'local')

	*/
	
	public VContactObject viewContactObjectById(Connection con, int categoryId, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		// New field: tags list
		Field<String> tags = DSL
			.select(DSL.groupConcat(CONTACTS_TAGS.TAG_ID, "|"))
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
			).asField("tags");
		
		return dsl
			.select(
				CONTACTS_.fields()
			)
			.select(
				MASTER_DATA.MASTER_DATA_ID.as("master_data_id"),
				MASTER_DATA.DESCRIPTION.as("master_data_description"),
				tags,
				DSL.nvl2(CONTACTS_PICTURES.CONTACT_ID, true, false).as("has_picture"),
				DSL.nvl2(CONTACTS_VCARDS.CONTACT_ID, true, false).as("has_vcard")
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.leftOuterJoin(CONTACTS_PICTURES).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_PICTURES.CONTACT_ID))
			.leftOuterJoin(CONTACTS_VCARDS).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_VCARDS.CONTACT_ID))
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
				.and(CONTACTS_.CATEGORY_ID.equal(categoryId))
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.fetchOneInto(VContactObject.class);
	}
	
	public VContactObject viewContactObjectById(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		// New field: tags list
		Field<String> tags = DSL
			.select(DSL.groupConcat(CONTACTS_TAGS.TAG_ID, "|"))
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
			).asField("tags");
		
		return dsl
			.select(
				CONTACTS_.fields()
			)
			.select(
				MASTER_DATA.MASTER_DATA_ID.as("master_data_id"),
				MASTER_DATA.DESCRIPTION.as("master_data_description"),
				tags,
				DSL.nvl2(CONTACTS_PICTURES.CONTACT_ID, true, false).as("has_picture"),
				DSL.nvl2(CONTACTS_VCARDS.CONTACT_ID, true, false).as("has_vcard")
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.leftOuterJoin(CONTACTS_PICTURES).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_PICTURES.CONTACT_ID))
			.leftOuterJoin(CONTACTS_VCARDS).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_VCARDS.CONTACT_ID))
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.fetchOneInto(VContactObject.class);
	}
	
	private Field[] getVContactObjectFields(boolean stat) {
		if (stat) {
			return new Field[]{
				CONTACTS_.CONTACT_ID,
				CONTACTS_.CATEGORY_ID,
				CONTACTS_.REVISION_STATUS,
				CONTACTS_.REVISION_TIMESTAMP,
				CONTACTS_.CREATION_TIMESTAMP,
				CONTACTS_.PUBLIC_UID,
				CONTACTS_.HREF
			};
		} else {
			return CONTACTS_.fields();
		}
	}
	
	public Map<String, List<VContactObject>> viewOnlineContactObjectsByCategory(Connection con, boolean stat, int categoryId) throws DAOException {
		return viewOnlineContactObjectsByCategoryHrefs(con, stat, categoryId, null);
	}
	
	public Map<String, List<VContactObject>> viewOnlineContactObjectsByCategoryHrefs(Connection con, boolean stat, int categoryId, Collection<String> hrefs) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		Condition inHrefsCndt = DSL.trueCondition();
		if (hrefs != null) {
			inHrefsCndt = CONTACTS_.HREF.in(hrefs);
		}
		
		// New field: tags list
		Field<String> tags = DSL
			.select(DSL.groupConcat(CONTACTS_TAGS.TAG_ID, "|"))
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
			).asField("tags");
		
		// New field: has picture
		Field<Boolean> hasPicture = DSL.nvl2(CONTACTS_PICTURES.CONTACT_ID, true, false).as("has_picture");
		
		// New field: has vcard
		Field<Boolean> hasVCard = DSL.nvl2(CONTACTS_VCARDS.CONTACT_ID, true, false).as("has_vcard");
		
		return dsl
			.select(
				getVContactObjectFields(stat)
			)
			.select(
				MASTER_DATA.MASTER_DATA_ID.as("master_data_id"),
				MASTER_DATA.DESCRIPTION.as("master_data_description"),
				tags,
				hasPicture,
				hasVCard
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.leftOuterJoin(CONTACTS_PICTURES).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_PICTURES.CONTACT_ID))
			.leftOuterJoin(CONTACTS_VCARDS).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_VCARDS.CONTACT_ID))
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(inHrefsCndt)
			)
			.orderBy(
				CONTACTS_.CONTACT_ID.asc()
			)
			.fetchGroups(CONTACTS_.HREF, VContactObject.class);
	}
	
	public void lazy_viewOnlineContactObjects(Connection con, boolean stat, int categoryId, VContactObject.Consumer consumer) throws DAOException, WTException {
		DSLContext dsl = getDSL(con);
		
		// New field: tags list
		Field<String> tags = DSL
			.select(DSL.groupConcat(CONTACTS_TAGS.TAG_ID, "|"))
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
			).asField("tags");
		
		// New field: has picture
		Field<Boolean> hasPicture = DSL.nvl2(CONTACTS_PICTURES.CONTACT_ID, true, false).as("has_picture");
		
		// New field: has attachments
		Field<Boolean> hasAttachments = DSL.field(DSL.exists(
			DSL.selectOne()
				.from(CONTACTS_ATTACHMENTS)
				.where(CONTACTS_ATTACHMENTS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID))
			)).as("has_attachments");
		
		// New field: has custom values
		Field<Boolean> hasCustomValues = DSL.field(DSL.exists(
			DSL.selectOne()
				.from(CONTACTS_CUSTOM_VALUES)
				.where(CONTACTS_CUSTOM_VALUES.CONTACT_ID.equal(CONTACTS_.CONTACT_ID))
			)).as("has_custom_values");
		
		// New field: has vcard
		Field<Boolean> hasVCard = DSL.nvl2(CONTACTS_VCARDS.CONTACT_ID, true, false).as("has_vcard");
		
		Cursor<Record> cursor = dsl
			.select(
				getVContactObjectFields(stat)
			)
			.select(
				MASTER_DATA.MASTER_DATA_ID.as("master_data_id"),
				MASTER_DATA.DESCRIPTION.as("master_data_description"),
				tags,
				hasPicture,
				hasAttachments,
				hasCustomValues,
				hasVCard
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.leftOuterJoin(CONTACTS_PICTURES).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_PICTURES.CONTACT_ID))
			.leftOuterJoin(CONTACTS_VCARDS).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_VCARDS.CONTACT_ID))
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				CONTACTS_.CONTACT_ID.asc()
			)
			.fetchLazy();

		try {
			for(;;) {
				VContactObject vco = cursor.fetchNextInto(VContactObject.class);
				if (vco == null) break;
				consumer.consume(vco, con);
			}
		} finally {
			cursor.close();
		}
	}
	
	public static Condition createContactsChangedNewOrModifiedCondition() {
		return HISTORY_CONTACTS.CHANGE_TYPE.equal(BaseDAO.CHANGE_TYPE_CREATION)
			.or(HISTORY_CONTACTS.CHANGE_TYPE.equal(BaseDAO.CHANGE_TYPE_UPDATE));
	}
	
	public static Condition createContactsChangedSinceUntilCondition(DateTime since, DateTime until) {
		return HISTORY_CONTACTS.CHANGE_TIMESTAMP.greaterThan(since)
			.and(HISTORY_CONTACTS.CHANGE_TIMESTAMP.lessThan(until));
	}
	
	public void lazy_viewChangedContactObjects(Connection con, Collection<Integer> categoryIds, Condition condition, boolean statFields, int limit, int offset, VContactObjectChanged.Consumer consumer) throws DAOException, WTException {
		DSLContext dsl = getDSL(con);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		
		// New field: tags list
		Field<String> tags = DSL
			.select(DSL.groupConcat(CONTACTS_TAGS.TAG_ID, "|"))
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
			).asField("tags");
		
		// New field: has picture
		Field<Boolean> hasPicture = DSL.nvl2(CONTACTS_PICTURES.CONTACT_ID, true, false).as("has_picture");
		
		// New field: has attachments
		Field<Boolean> hasAttachments = DSL.field(DSL.exists(
			DSL.selectOne()
				.from(CONTACTS_ATTACHMENTS)
				.where(CONTACTS_ATTACHMENTS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID))
			)).as("has_attachments");
		
		// New field: has custom values
		Field<Boolean> hasCustomValues = DSL.field(DSL.exists(
			DSL.selectOne()
				.from(CONTACTS_CUSTOM_VALUES)
				.where(CONTACTS_CUSTOM_VALUES.CONTACT_ID.equal(CONTACTS_.CONTACT_ID))
			)).as("has_custom_values");
		
		// New field: has vcard
		Field<Boolean> hasVCard = DSL.nvl2(CONTACTS_VCARDS.CONTACT_ID, true, false).as("has_vcard");
		
		Cursor<Record> cursor = dsl
			.select(
				HISTORY_CONTACTS.CHANGE_TIMESTAMP,
				HISTORY_CONTACTS.CHANGE_TYPE
			)
			.select(
				getVContactObjectFields(statFields)
			)
			.select(
				MASTER_DATA.MASTER_DATA_ID.as("master_data_id"),
				MASTER_DATA.DESCRIPTION.as("master_data_description"),
				tags,
				hasPicture,
				hasAttachments,
				hasCustomValues,
				hasVCard
			)
			.distinctOn(HISTORY_CONTACTS.CONTACT_ID)
			.from(HISTORY_CONTACTS)
			.leftOuterJoin(CONTACTS_).on(HISTORY_CONTACTS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID))
			.leftOuterJoin(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.leftOuterJoin(CONTACTS_PICTURES).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_PICTURES.CONTACT_ID))
			.leftOuterJoin(CONTACTS_VCARDS).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_VCARDS.CONTACT_ID))
			.where(
				HISTORY_CONTACTS.CATEGORY_ID.in(categoryIds)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(filterCndt)
			)
			.orderBy(
				HISTORY_CONTACTS.CONTACT_ID.asc(),
				HISTORY_CONTACTS.ID.desc()
			)
			.limit(limit)
			.offset(offset)
			.fetchLazy();
		
		try {
			for(;;) {
				VContactObjectChanged vco = cursor.fetchNextInto(VContactObjectChanged.class);
				if (vco == null) break;
				consumer.consume(vco, con);
			}
		} finally {
			cursor.close();
		}
	}
	
	public int countOnlineContactObjects(Connection con, Collection<Integer> categoryIds, Condition condition) throws DAOException, WTException {
		DSLContext dsl = getDSL(con);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		
		return dsl
			.selectCount()
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS_.CATEGORY_ID.in(categoryIds)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(filterCndt)
			)
			.fetchOne(0, Integer.class);
	}
	
	public void lazy_viewOnlineContactObjects(Connection con, Collection<Integer> categoryIds, Condition condition, Set<SortInfo> sortInfo, boolean statFields, int limit, int offset, VContactObject.Consumer consumer) throws DAOException, WTException {
		DSLContext dsl = getDSL(con);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		
		// New field: tags list
		Field<String> tags = DSL
			.select(DSL.groupConcat(CONTACTS_TAGS.TAG_ID, "|"))
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
			).asField("tags");
		
		// New field: has picture
		Field<Boolean> hasPicture = DSL.nvl2(CONTACTS_PICTURES.CONTACT_ID, true, false).as("has_picture");
		
		// New field: has attachments
		Field<Boolean> hasAttachments = DSL.field(DSL.exists(
			DSL.selectOne()
				.from(CONTACTS_ATTACHMENTS)
				.where(CONTACTS_ATTACHMENTS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID))
			)).as("has_attachments");
		
		// New field: has custom values
		Field<Boolean> hasCustomValues = DSL.field(DSL.exists(
			DSL.selectOne()
				.from(CONTACTS_CUSTOM_VALUES)
				.where(CONTACTS_CUSTOM_VALUES.CONTACT_ID.equal(CONTACTS_.CONTACT_ID))
			)).as("has_custom_values");
		
		// New field: has vcard
		Field<Boolean> hasVCard = DSL.nvl2(CONTACTS_VCARDS.CONTACT_ID, true, false).as("has_vcard");
		
		Cursor<Record> cursor = dsl
			.select(
				getVContactObjectFields(statFields)
			)
			.select(
				MASTER_DATA.MASTER_DATA_ID.as("master_data_id"),
				MASTER_DATA.DESCRIPTION.as("master_data_description"),
				tags,
				hasPicture,
				hasAttachments,
				hasCustomValues,
				hasVCard
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.leftOuterJoin(CONTACTS_PICTURES).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_PICTURES.CONTACT_ID))
			.leftOuterJoin(CONTACTS_VCARDS).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_VCARDS.CONTACT_ID))
			.where(
				CONTACTS_.CATEGORY_ID.in(categoryIds)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(filterCndt)
			)
			.orderBy(
				toContactsOrderByClause(sortInfo)
			)
			.limit(limit)
			.offset(offset)
			.fetchLazy();

		try {
			for(;;) {
				VContactObject vco = cursor.fetchNextInto(VContactObject.class);
				if (vco == null) break;
				consumer.consume(vco, con);
			}
		} finally {
			cursor.close();
		}
	}
	
	public List<VContactObjectStat> viewOnlineContactObjectStatsByCategory(Connection con, int categoryId, int limit) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.REVISION_STATUS,
				CONTACTS_.REVISION_TIMESTAMP,
				CONTACTS_.CREATION_TIMESTAMP,
				CONTACTS_.HREF
			)
			.from(CONTACTS_)
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				CONTACTS_.CONTACT_ID
			)
			.limit(limit)
			.fetchInto(VContactObjectStat.class);
	}
	
	public List<VContactObjectStat> viewContactObjectStatsByCategorySince(Connection con, int categoryId, DateTime since, int limit) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.REVISION_STATUS,
				CONTACTS_.REVISION_TIMESTAMP,
				CONTACTS_.CREATION_TIMESTAMP,
				CONTACTS_.HREF
			)
			.from(CONTACTS_)
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(CONTACTS_.REVISION_TIMESTAMP.greaterThan(since))
			)
			.orderBy(
				CONTACTS_.CREATION_TIMESTAMP
			)
			.limit(limit)
			.fetchInto(VContactObjectStat.class);
	}
	
	public boolean existByCategoryTypeCondition(Connection con, Collection<Integer> categoryIds, ContactType type, Condition condition) throws DAOException {
		DSLContext dsl = getDSL(con);
		Condition typeCndt = toContactTypeCondition(type);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		
		return dsl.fetchExists(
			dsl.selectOne()
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS_.CATEGORY_ID.in(categoryIds)
				.and(typeCndt)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(filterCndt)
			)
		);
	}
	
	public int countByCategoryTypeCondition(Connection con, Collection<Integer> categoryIds, ContactType type, Condition condition) throws DAOException {
		DSLContext dsl = getDSL(con);
		Condition typeCndt = toContactTypeCondition(type);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		
		return dsl
			.selectCount()
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS_.CATEGORY_ID.in(categoryIds)
				.and(typeCndt)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(filterCndt)
			)
			.fetchOne(0, Integer.class);
	}
	
	public List<VContactLookup> viewByCategoryTypeCondition(Connection con, Collection<OrderField> orderFields, Collection<Integer> categoryIds, ContactType type, Condition condition, int limit, int offset) throws DAOException {
		DSLContext dsl = getDSL(con);
		Condition typeCndt = toContactTypeCondition(type);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		ArrayList<SortField<?>> sortFlds = toSortClause(orderFields);
		
		// Define sort fields
		/*
		ArrayList<SortField<?>> sortFlds = new ArrayList<>();
		// TODO: maybe sort on joined field otherwise order could be inaccurate (company can be an ID)
		for (OrderField of : orderFields) {
			if (OrderField.FIRSTNAME.equals(of)) {
				sortFlds.add(CONTACTS_.FIRSTNAME.asc());
			} else if (OrderField.LASTNAME.equals(of)) {
				sortFlds.add(CONTACTS_.LASTNAME.asc());
			} else if (OrderField.COMPANY.equals(of)) {
				sortFlds.add(CONTACTS_.COMPANY.asc());
			}
		}
		*/
		
		Field<String> tags = DSL
			.select(DSL.groupConcat(CONTACTS_TAGS.TAG_ID, "|"))
			.from(CONTACTS_TAGS)
			.where(
				CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
			).asField("tags");
		
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.CATEGORY_ID,
				CONTACTS_.IS_LIST,
				CONTACTS_.DISPLAY_NAME,
				CONTACTS_.TITLE,
				CONTACTS_.FIRSTNAME,
				CONTACTS_.LASTNAME,
				CONTACTS_.DISPLAY_NAME,
				CONTACTS_.NICKNAME,
				CONTACTS_.COMPANY,
				CONTACTS_.COMPANY_MASTER_DATA_ID,
				CONTACTS_.FUNCTION,
				CONTACTS_.WORK_ADDRESS,
				CONTACTS_.WORK_CITY,
				CONTACTS_.WORK_STATE,
				CONTACTS_.WORK_COUNTRY,
				CONTACTS_.WORK_TELEPHONE,
				CONTACTS_.WORK_MOBILE,
				CONTACTS_.WORK_EMAIL,
				CONTACTS_.HOME_TELEPHONE,
				CONTACTS_.HOME_EMAIL
			)
			.select(
				tags,
				CATEGORIES.NAME.as("category_name"),
				CATEGORIES.DOMAIN_ID.as("category_domain_id"),
				CATEGORIES.USER_ID.as("category_user_id"),
				MASTER_DATA.MASTER_DATA_ID.as("master_data_id"),
				MASTER_DATA.DESCRIPTION.as("master_data_description")
			)
			.select(
				DSL.nvl2(CONTACTS_PICTURES.CONTACT_ID, true, false).as("has_picture")
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.leftOuterJoin(CONTACTS_PICTURES).on(CONTACTS_.CONTACT_ID.equal(CONTACTS_PICTURES.CONTACT_ID))
			.where(
				CONTACTS_.CATEGORY_ID.in(categoryIds)
				.and(typeCndt)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(filterCndt)
			)
			.orderBy(sortFlds)
			.limit(limit)
			.offset(offset)
			.fetchInto(VContactLookup.class);
	}
	
	public VContactCompany viewContactCompanyByContact(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.CATEGORY_ID,
				CONTACTS_.COMPANY,
				CONTACTS_.COMPANY_MASTER_DATA_ID
			)
			.select(
				MASTER_DATA.MASTER_DATA_ID.as("master_data_id"),
				MASTER_DATA.DESCRIPTION.as("master_data_description")
			)
			.from(CONTACTS_)
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.where(
				CONTACTS_.CONTACT_ID.in(contactId)
				.and(CONTACTS_.IS_LIST.equal(false))
			)
			.fetchOneInto(VContactCompany.class);
	}

	public List<VContact> viewRecipientsByFieldCategoryQuery(Connection con, RecipientFieldType fieldType, RecipientFieldCategory fieldCategory, Collection<Integer> categoryIds, String queryText) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		Field<?> targetField = getTableFieldBy(fieldType, fieldCategory);
		if (targetField == null) throw new DAOException("Unable to determine a targetField for passed Type and Category");
		Condition searchCndt = DSL.trueCondition();
		if (!StringUtils.isBlank(queryText)) {
			String patt1 = null, patt2 = null, patt3 = null;
			if(StringUtils.contains(queryText, " ")) {
				patt1 = LangUtils.patternizeWords(queryText);
				patt2 = queryText;
			} else {
				patt1 = LangUtils.patternizeWords(queryText);
				patt2 = "%" + queryText + "%";
			}
			
			searchCndt = CONTACTS_.DISPLAY_NAME.likeIgnoreCase(patt1)
				.or(CONTACTS_.FIRSTNAME.likeIgnoreCase(patt1)
				.or(CONTACTS_.LASTNAME.likeIgnoreCase(patt1)
				.or(targetField.likeIgnoreCase(patt1)
				.or(CONTACTS_.COMPANY.likeIgnoreCase(patt2)
				.or(MASTER_DATA.DESCRIPTION.likeIgnoreCase(patt2)
			)))));
			
			if (!fieldType.equals(RecipientFieldType.EMAIL)) {
				searchCndt = searchCndt.or(
					CONTACTS_.WORK_EMAIL.likeIgnoreCase(patt1)
				);
				searchCndt = searchCndt.or(
					CONTACTS_.HOME_EMAIL.likeIgnoreCase(patt1)
				);
				searchCndt = searchCndt.or(
					CONTACTS_.OTHER_EMAIL.likeIgnoreCase(patt1)
				);
			}
			
			if (StringUtils.contains(queryText, "@")) {
				patt3 = "%" + queryText + "%";
				searchCndt = searchCndt.or(
					CONTACTS_.WORK_EMAIL.likeIgnoreCase(patt3)
				);
			}			
		}
		
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.IS_LIST,
				CONTACTS_.DISPLAY_NAME,
				CONTACTS_.FIRSTNAME,
				CONTACTS_.LASTNAME,
				targetField
			)
			.select(
				MASTER_DATA.DESCRIPTION.as("company_as_master_data_id")
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS_.COMPANY_MASTER_DATA_ID.equal(MASTER_DATA.MASTER_DATA_ID))
			.where(
				CONTACTS_.CATEGORY_ID.in(categoryIds)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(
					targetField.isNotNull()
				)
				.and(
					searchCndt
				)
			)
			.orderBy(
				targetField.asc()
			)
			.fetchInto(VContact.class);
	}
	
	public Map<String, VContactHrefSync> viewHrefSyncDataByCategory(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.HREF,
				CONTACTS_.ETAG
			)
			.from(CONTACTS_)
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.fetchMap(CONTACTS_.HREF, VContactHrefSync.class);
	}
	
	@Deprecated
	public OContact selectById(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CONTACTS_)
			.where(CONTACTS_.CONTACT_ID.equal(contactId))
			.fetchOneInto(OContact.class);
	}
	
	public OContact selectOnlineById(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CONTACTS_)
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.fetchOneInto(OContact.class);
	}
	
	public OContactInfo selectOnlineContactInfoById(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.CATEGORY_ID,
				CONTACTS_.IS_LIST
			)
			.from(CONTACTS_)
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.fetchOneInto(OContactInfo.class);
	}
	
	public Map<String, OContactInfo> selectOnlineContactInfoByIds(Connection con, Collection<String> contactIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.CATEGORY_ID,
				CONTACTS_.IS_LIST
			)
			.from(CONTACTS_)
			.where(
				CONTACTS_.CONTACT_ID.in(contactIds)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.fetchMap(CONTACTS_.CONTACT_ID, OContactInfo.class);
	}
	
	public Integer selectCategoryId(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.CATEGORY_ID
			)
			.from(CONTACTS_)
			.where(CONTACTS_.CONTACT_ID.equal(contactId))
			.fetchOneInto(Integer.class);
	}
	
	public List<String> selectAliveIdsByCategoryHrefs(Connection con, int categoryId, String href) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.CONTACT_ID
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(CONTACTS_.HREF.equal(href))
			)
			.orderBy(
				CONTACTS_.CONTACT_ID.asc()
			)
			.fetchInto(String.class);
	}
	
	public Map<Integer, DateTime> selectMaxRevTimestampByCategoriesType(Connection con, Collection<Integer> categoryIds, boolean isList) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				HISTORY_CONTACTS.CATEGORY_ID,
				DSL.max(HISTORY_CONTACTS.CHANGE_TIMESTAMP)
			)
			.from(HISTORY_CONTACTS)
			.where(
				HISTORY_CONTACTS.CATEGORY_ID.in(categoryIds)
			)
			.groupBy(
				HISTORY_CONTACTS.CATEGORY_ID
			)
			.fetchMap(HISTORY_CONTACTS.CATEGORY_ID, DSL.max(HISTORY_CONTACTS.CHANGE_TIMESTAMP));
	}
	
	public Map<String, List<String>> selectHrefsByByCategory(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.HREF
			)
			.from(CONTACTS_)
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				CONTACTS_.CONTACT_ID.asc()
			)
			.fetchGroups(CONTACTS_.HREF, CONTACTS_.CONTACT_ID);
	}
	
	public Map<String, OContact> selectByCategoryHrefs(Connection con, int categoryId, Collection<String> hrefs) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.fields()
			)
			.from(CONTACTS_)
			.join(CATEGORIES).on(CONTACTS_.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS_.IS_LIST.equal(false))
				.and(
					CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW))
					.or(CONTACTS_.REVISION_STATUS.equal(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED)))
				)
				.and(CONTACTS_.HREF.in(hrefs))
			)
			.orderBy(
				CONTACTS_.CONTACT_ID.asc()
			)
			.fetchMap(CONTACTS_.CONTACT_ID, OContact.class);
	}
	
	public Map<String, Integer> selectCategoriesByIds(Connection con, Collection<String> contactIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.CATEGORY_ID
			)
			.from(CONTACTS_)
			.where(
				CONTACTS_.CONTACT_ID.in(contactIds)
			)
			.fetchMap(CONTACTS_.CONTACT_ID, CONTACTS_.CATEGORY_ID);
	}
	
	public Map<String, Integer> selectCategoriesByIdsType(Connection con, Collection<String> contactIds, boolean isList) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS_.CONTACT_ID,
				CONTACTS_.CATEGORY_ID
			)
			.from(CONTACTS_)
			.where(
				CONTACTS_.CONTACT_ID.in(contactIds)
				.and(CONTACTS_.IS_LIST.equal(isList))
			)
			.fetchMap(CONTACTS_.CONTACT_ID, CONTACTS_.CATEGORY_ID);
	}
	
	public int insert(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW));
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence(0);
		item.setCreationTimestamp(revisionTimestamp);
		ContactsRecord record = dsl.newRecord(CONTACTS_, item);
		return dsl
			.insertInto(CONTACTS_)
			.set(record)
			.execute();
	}
	
	public int batchInsert(Connection con, ArrayList<OContact> items, DateTime revisionTimestamp) throws DAOException {
		final String NEW = EnumUtils.toSerializedName(ContactBase.RevisionStatus.NEW);
		DSLContext dsl = getDSL(con);
		ArrayList<ContactsRecord> records = new ArrayList<>();
		for (OContact item : items) {
			item.setRevisionStatus(NEW);
			item.setRevisionTimestamp(revisionTimestamp);
			item.setRevisionSequence(0);
			item.setCreationTimestamp(revisionTimestamp);
			records.add(dsl.newRecord(CONTACTS_, item));
		}
		dsl.batchInsert(records).execute();
		return items.size();
	}
	
	public int update(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED));
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence(0);
		return dsl
			.update(CONTACTS_)
			.set(CONTACTS_.CATEGORY_ID, item.getCategoryId())
			.set(CONTACTS_.REVISION_STATUS, item.getRevisionStatus())
			.set(CONTACTS_.REVISION_TIMESTAMP, item.getRevisionTimestamp())
			.set(CONTACTS_.TITLE, item.getTitle())
			.set(CONTACTS_.FIRSTNAME, item.getFirstname())
			.set(CONTACTS_.LASTNAME, item.getLastname())
			.set(CONTACTS_.DISPLAY_NAME, item.getDisplayName())
			.set(CONTACTS_.NICKNAME, item.getNickname())
			.set(CONTACTS_.GENDER, item.getGender())
			.set(CONTACTS_.COMPANY, item.getCompany())
			.set(CONTACTS_.COMPANY_MASTER_DATA_ID, item.getCompanyMasterDataId())
			.set(CONTACTS_.FUNCTION, item.getFunction())
			.set(CONTACTS_.WORK_ADDRESS, item.getWorkAddress())
			.set(CONTACTS_.WORK_CITY, item.getWorkCity())
			.set(CONTACTS_.WORK_STATE, item.getWorkState())
			.set(CONTACTS_.WORK_POSTALCODE, item.getWorkPostalcode())
			.set(CONTACTS_.WORK_COUNTRY, item.getWorkCountry())
			.set(CONTACTS_.WORK_TELEPHONE, item.getWorkTelephone())
			.set(CONTACTS_.WORK_TELEPHONE2, item.getWorkTelephone2())
			.set(CONTACTS_.WORK_FAX, item.getWorkFax())
			.set(CONTACTS_.WORK_MOBILE, item.getWorkMobile())
			.set(CONTACTS_.WORK_PAGER, item.getWorkPager())
			.set(CONTACTS_.WORK_EMAIL, item.getWorkEmail())
			.set(CONTACTS_.WORK_IM, item.getWorkIm())
			.set(CONTACTS_.ASSISTANT, item.getAssistant())
			.set(CONTACTS_.ASSISTANT_TELEPHONE, item.getAssistantTelephone())
			.set(CONTACTS_.DEPARTMENT, item.getDepartment())
			.set(CONTACTS_.MANAGER, item.getManager())
			.set(CONTACTS_.HOME_ADDRESS, item.getHomeAddress())
			.set(CONTACTS_.HOME_CITY, item.getHomeCity())
			.set(CONTACTS_.HOME_STATE, item.getHomeState())
			.set(CONTACTS_.HOME_POSTALCODE, item.getHomePostalcode())
			.set(CONTACTS_.HOME_COUNTRY, item.getHomeCountry())
			.set(CONTACTS_.HOME_TELEPHONE, item.getHomeTelephone())
			.set(CONTACTS_.HOME_TELEPHONE2, item.getHomeTelephone2())
			.set(CONTACTS_.HOME_FAX, item.getHomeFax())
			.set(CONTACTS_.HOME_MOBILE, item.getHomeMobile())
			.set(CONTACTS_.HOME_PAGER, item.getHomePager())
			.set(CONTACTS_.HOME_EMAIL, item.getHomeEmail())
			.set(CONTACTS_.HOME_IM, item.getHomeIm())
			.set(CONTACTS_.PARTNER, item.getPartner())
			.set(CONTACTS_.BIRTHDAY, item.getBirthday())
			.set(CONTACTS_.ANNIVERSARY, item.getAnniversary())
			.set(CONTACTS_.OTHER_ADDRESS, item.getOtherAddress())
			.set(CONTACTS_.OTHER_CITY, item.getOtherCity())
			.set(CONTACTS_.OTHER_STATE, item.getOtherState())
			.set(CONTACTS_.OTHER_POSTALCODE, item.getOtherPostalcode())
			.set(CONTACTS_.OTHER_COUNTRY, item.getOtherCountry())
			.set(CONTACTS_.OTHER_EMAIL, item.getOtherEmail())
			.set(CONTACTS_.OTHER_IM, item.getOtherIm())
			.set(CONTACTS_.URL, item.getUrl())
			.set(CONTACTS_.NOTES, item.getNotes())
			.set(CONTACTS_.ETAG, item.getEtag())
			.where(
				CONTACTS_.CONTACT_ID.equal(item.getContactId())
			)
			.execute();
	}
	
	public int updateList(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED));
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence(0);
		return dsl
			.update(CONTACTS_)
			.set(CONTACTS_.CATEGORY_ID, item.getCategoryId())
			.set(CONTACTS_.REVISION_STATUS, item.getRevisionStatus())
			.set(CONTACTS_.REVISION_TIMESTAMP, item.getRevisionTimestamp())
			.set(CONTACTS_.DISPLAY_NAME, item.getDisplayName())
			.set(CONTACTS_.FIRSTNAME, item.getDisplayName())
			.set(CONTACTS_.LASTNAME, item.getDisplayName())
			.where(
					CONTACTS_.CONTACT_ID.equal(item.getContactId())
			)
			.execute();
	}
	
	public int updateCategory(Connection con, String contactId, int categoryId, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS_)
			.set(CONTACTS_.CATEGORY_ID, categoryId)
			.set(CONTACTS_.REVISION_STATUS, EnumUtils.toSerializedName(ContactBase.RevisionStatus.MODIFIED))
			.set(CONTACTS_.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int updateRevision(Connection con, String contactId, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS_)
			.set(CONTACTS_.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int updateRevisionStatus(Connection con, String contactId, ContactBase.RevisionStatus revisionStatus, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS_)
			.set(CONTACTS_.REVISION_STATUS, EnumUtils.toSerializedName(revisionStatus))
			.set(CONTACTS_.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int deleteById(Connection con, String contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_)
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int deleteByCategory(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS_)
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
			)
			.execute();
	}
	
	public int logicDeleteById(Connection con, String contactId, DateTime revisionTimestamp) throws DAOException {
		final String DELETED = EnumUtils.toSerializedName(ContactBase.RevisionStatus.DELETED);
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS_)
			.set(CONTACTS_.REVISION_STATUS, DELETED)
			.set(CONTACTS_.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS_.CONTACT_ID.equal(contactId)
				.and(CONTACTS_.REVISION_STATUS.notEqual(DELETED))
			)
			.execute();
	}
	
	public int logicDeleteByCategory(Connection con, int categoryId, DateTime revisionTimestamp) throws DAOException {
		final String DELETED = EnumUtils.toSerializedName(ContactBase.RevisionStatus.DELETED);
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS_)
			.set(CONTACTS_.REVISION_STATUS, DELETED)
			.set(CONTACTS_.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS_.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS_.REVISION_STATUS.notEqual(DELETED))
			)
			.execute();
	}
	
	public boolean hasTableFieldFor(RecipientFieldType fieldType, RecipientFieldCategory fieldCategory) {
		return getTableFieldBy(fieldType, fieldCategory) != null;
	}
	
	private Field<?> getTableFieldBy(RecipientFieldType fieldType, RecipientFieldCategory fieldCategory) {
		if (fieldType.equals(RecipientFieldType.TELEPHONE)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS_.WORK_TELEPHONE;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS_.HOME_TELEPHONE;
			}
		} else if (fieldType.equals(RecipientFieldType.TELEPHONE_2)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS_.WORK_TELEPHONE2;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS_.HOME_TELEPHONE2;
			}
		} else if (fieldType.equals(RecipientFieldType.FAX)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS_.WORK_FAX;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS_.HOME_FAX;
			}
		} else if (fieldType.equals(RecipientFieldType.MOBILE)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS_.WORK_MOBILE;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS_.HOME_MOBILE;
			}
		} else if (fieldType.equals(RecipientFieldType.PAGER)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS_.WORK_PAGER;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS_.HOME_PAGER;
			}
		} else if (fieldType.equals(RecipientFieldType.EMAIL)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS_.WORK_EMAIL;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS_.HOME_EMAIL;
			} else if (fieldCategory.equals(RecipientFieldCategory.OTHER)) {
				return CONTACTS_.OTHER_EMAIL;
			}
		} else if (fieldType.equals(RecipientFieldType.IM)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS_.WORK_EMAIL;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS_.HOME_EMAIL;
			} else if (fieldCategory.equals(RecipientFieldCategory.OTHER)) {
				return CONTACTS_.OTHER_EMAIL;
			}
		}
		return null;
	}
	
	/*
	public byte[] readPhoto(Connection con, int contactId) throws IOException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			DSLContext dsl = getDSL(con);
			String sql = dsl
				.select(
					CONTACTS_.PHOTO
				)
				.from(CONTACTS_)
				.where(
						CONTACTS_.CONTACT_ID.equal(contactId)
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
	
	private static Collection<SortField<?>> toContactsOrderByClause(final Set<SortInfo> sortInfo) {
		ArrayList<SortField<?>> fields = new ArrayList<>();
		for (SortInfo si : sortInfo) {
			if (si.getField().equals("displayName")) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CONTACTS_.DISPLAY_NAME, "")), si).nullsLast());
			else if (si.getField().equals("firstName")) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CONTACTS_.FIRSTNAME, "")), si).nullsLast());
			else if (si.getField().equals("lastName")) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CONTACTS_.LASTNAME, "")), si).nullsLast());
			else if (si.getField().equals("company")) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CONTACTS_.COMPANY, "")), si).nullsLast());
		}
		return fields;
	}
	
	private ArrayList<SortField<?>> toSortClause(Collection<OrderField> orderFields) {
		ArrayList<SortField<?>> fields = new ArrayList<>();
		for (OrderField of : orderFields) {
			if (OrderField.DISPLAYNAME.equals(of)) {
				fields.add(DSL.upper(DSL.nullif(CONTACTS_.DISPLAY_NAME, "")).asc().nullsLast());
			} else if (OrderField.FIRSTNAME.equals(of)) {
				fields.add(DSL.upper(DSL.nullif(CONTACTS_.FIRSTNAME, "")).asc().nullsLast());
			} else if (OrderField.LASTNAME.equals(of)) {
				fields.add(DSL.upper(DSL.nullif(CONTACTS_.LASTNAME, "")).asc().nullsLast());
			} else if (OrderField.COMPANY.equals(of)) {
				fields.add(DSL.upper(DSL.nullif(CONTACTS_.COMPANY, "")).asc().nullsLast());
			}
		}
		return fields;
	}
	
	private Condition toContactTypeCondition(ContactType type) {
		if (ContactType.CONTACT.equals(type)) {
			return CONTACTS_.IS_LIST.isFalse();
		} else if (ContactType.LIST.equals(type)) {
			return CONTACTS_.IS_LIST.isTrue();
		} else {
			return DSL.trueCondition();
		}
	}
	
	private Condition toListOnlyCondition(boolean listOnly) {
		return listOnly ? CONTACTS_.IS_LIST.isTrue() : DSL.trueCondition();
	}
	
	private Condition toSearchPatternCondition(String pattern) {
		Condition cndt = DSL.trueCondition();
		if (!StringUtils.isBlank(pattern)) {
			return CONTACTS_.WORK_EMAIL.likeIgnoreCase(pattern)
				.or(CONTACTS_.HOME_EMAIL.likeIgnoreCase(pattern))
				.or(CONTACTS_.OTHER_EMAIL.likeIgnoreCase(pattern))
				.or(CONTACTS_.COMPANY.likeIgnoreCase(pattern));
		}
		return cndt;
	}
	
	public static enum OrderField {
		DISPLAYNAME, FIRSTNAME, LASTNAME, COMPANY
	}
}
