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
import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.VContact;
import com.sonicle.webtop.contacts.bol.VContactCard;
import com.sonicle.webtop.contacts.bol.VContactCardChanged;
import com.sonicle.webtop.contacts.bol.VContactHrefSync;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_CONTACTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CATEGORIES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_PICTURES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_VCARDS;
import com.sonicle.webtop.contacts.jooq.tables.records.ContactsRecord;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import static com.sonicle.webtop.core.jooq.core.Tables.MASTER_DATA;
import com.sonicle.webtop.core.model.RecipientFieldCategory;
import com.sonicle.webtop.core.model.RecipientFieldType;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
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
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
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
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				CONTACTS.LASTNAME.asc(),
				CONTACTS.FIRSTNAME.asc()
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
	
	public Map<String, List<VContactCard>> viewCardsByCategory(Connection con, int categoryId) throws DAOException {
		return viewCardsByCategoryHrefs(con, categoryId, null);
	}
	
	public Map<String, List<VContactCard>> viewCardsByCategoryHrefs(Connection con, int categoryId, Collection<String> hrefs) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		Condition inHrefsCndt = DSL.trueCondition();
		if (hrefs != null) {
			inHrefsCndt = CONTACTS.HREF.in(hrefs);
		}
		
		return dsl
			.select(
				CONTACTS.fields()
			)
			.select(
				DSL.nvl2(CONTACTS_PICTURES.CONTACT_ID, true, false).as("has_picture"),
				DSL.nvl2(CONTACTS_VCARDS.CONTACT_ID, true, false).as("has_vcard")
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(CONTACTS_PICTURES).on(CONTACTS.CONTACT_ID.equal(CONTACTS_PICTURES.CONTACT_ID))
			.leftOuterJoin(CONTACTS_VCARDS).on(CONTACTS.CONTACT_ID.equal(CONTACTS_VCARDS.CONTACT_ID))
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS.IS_LIST.equal(false))
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
				.and(inHrefsCndt)
			)
			.orderBy(
				CONTACTS.CONTACT_ID.asc()
			)
			.fetchGroups(CONTACTS.HREF, VContactCard.class);
	}
	
	public List<VContactCardChanged> viewChangedByCategory(Connection con, int categoryId, int limit) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		return dsl
			.select(
				CONTACTS.CONTACT_ID,
				CONTACTS.REVISION_STATUS,
				CONTACTS.REVISION_TIMESTAMP,
				CONTACTS.CREATION_TIMESTAMP,
				CONTACTS.HREF
			)
			.from(CONTACTS)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS.IS_LIST.equal(false))
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				CONTACTS.CONTACT_ID
			)
			.limit(limit)
			.fetchInto(VContactCardChanged.class);
	}
	
	public List<VContactCardChanged> viewChangedByCategorySince(Connection con, int categoryId, DateTime since, int limit) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		return dsl
			.select(
				CONTACTS.CONTACT_ID,
				CONTACTS.REVISION_STATUS,
				CONTACTS.REVISION_TIMESTAMP,
				CONTACTS.CREATION_TIMESTAMP,
				CONTACTS.HREF
			)
			.from(CONTACTS)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS.IS_LIST.equal(false))
				.and(CONTACTS.REVISION_TIMESTAMP.greaterThan(since))
			)
			.orderBy(
				CONTACTS.CREATION_TIMESTAMP
			)
			.limit(limit)
			.fetchInto(VContactCardChanged.class);
	}
	
	public List<VContact> viewByCategoryPattern(Connection con, int categoryId, String searchMode, String pattern) throws DAOException {
		DSLContext dsl = getDSL(con);
		
		Condition patternCndt = DSL.trueCondition();
		if (!StringUtils.isBlank(pattern)) {
			if (searchMode.equals("lastname")) {
				//searchCndt = CONTACTS.LASTNAME.lower().like(query);
				patternCndt = CONTACTS.LASTNAME.lower().likeRegex(pattern);
				if (pattern.equals("^.*")) patternCndt = patternCndt.or(CONTACTS.LASTNAME.isNull());
			} else {
				patternCndt = CONTACTS.WORK_EMAIL.likeIgnoreCase(pattern)
					.or(CONTACTS.HOME_EMAIL.likeIgnoreCase(pattern))
					.or(CONTACTS.OTHER_EMAIL.likeIgnoreCase(pattern))
					.or(CONTACTS.COMPANY.likeIgnoreCase(pattern))
					.or(CONTACTS.SEARCHFIELD.likeIgnoreCase(pattern));
			}
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
				MASTER_DATA.DESCRIPTION.as("company_as_master_data_id"),
				CATEGORIES.DOMAIN_ID.as("category_domain_id"),
				CATEGORIES.USER_ID.as("category_user_id")
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS.COMPANY.equal(MASTER_DATA.MASTER_DATA_ID))
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
				.and(
					patternCndt
				)
			)
			.orderBy(
				CONTACTS.LASTNAME.asc(),
				CONTACTS.FIRSTNAME.asc(),
				CONTACTS.COMPANY.asc()
			)
			.fetchInto(VContact.class);
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
			
			searchCndt = CONTACTS.FIRSTNAME.likeIgnoreCase(patt1)
				.or(CONTACTS.LASTNAME.likeIgnoreCase(patt1)
				.or(targetField.likeIgnoreCase(patt1)
				.or(CONTACTS.COMPANY.likeIgnoreCase(patt2)
				.or(MASTER_DATA.DESCRIPTION.likeIgnoreCase(patt2)
			))));
			
			if (!fieldType.equals(RecipientFieldType.EMAIL)) {
				searchCndt = searchCndt.or(
					CONTACTS.WORK_EMAIL.likeIgnoreCase(patt1)
				);
				searchCndt = searchCndt.or(
					CONTACTS.HOME_EMAIL.likeIgnoreCase(patt1)
				);
				searchCndt = searchCndt.or(
					CONTACTS.OTHER_EMAIL.likeIgnoreCase(patt1)
				);
			}
			
			if (StringUtils.contains(queryText, "@")) {
				patt3 = "%" + queryText + "%";
				searchCndt = searchCndt.or(
					CONTACTS.WORK_EMAIL.likeIgnoreCase(patt3)
				);
			}			
		}
		
		return dsl
			.select(
				CONTACTS.IS_LIST,
				CONTACTS.FIRSTNAME,
				CONTACTS.LASTNAME,
				targetField
			)
			.select(
				MASTER_DATA.DESCRIPTION.as("company_as_master_data_id")
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.leftOuterJoin(MASTER_DATA).on(CONTACTS.COMPANY.equal(MASTER_DATA.MASTER_DATA_ID))
			.where(
				CONTACTS.CATEGORY_ID.in(categoryIds)
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
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
				CONTACTS.CONTACT_ID,
				CONTACTS.HREF,
				CONTACTS.ETAG
			)
			.from(CONTACTS)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
			)
			.fetchMap(CONTACTS.HREF, VContactHrefSync.class);
	}
	
	public OContact selectById(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CONTACTS)
			.where(CONTACTS.CONTACT_ID.equal(contactId))
			.fetchOneInto(OContact.class);
	}
	
	public List<Integer> selectAliveIdsByCategoryHrefs(Connection con, int categoryId, String href) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS.CONTACT_ID
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS.IS_LIST.equal(false))
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
				.and(CONTACTS.HREF.equal(href))
			)
			.orderBy(
				CONTACTS.CONTACT_ID.asc()
			)
			.fetchInto(Integer.class);
	}
	
	public Map<Integer, DateTime> selectMaxRevTimestampByCategoriesType(Connection con, Collection<Integer> categoryIds, boolean isList) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS.CATEGORY_ID,
				DSL.max(CONTACTS.REVISION_TIMESTAMP)
			)
			.from(CONTACTS)
			.where(
				CONTACTS.CATEGORY_ID.in(categoryIds)
				.and(CONTACTS.IS_LIST.equal(isList))
			)
			.groupBy(
				CONTACTS.CATEGORY_ID
			)
			.fetchMap(CONTACTS.CATEGORY_ID, DSL.max(CONTACTS.REVISION_TIMESTAMP));
	}
	
	public Map<String, List<Integer>> selectHrefsByByCategory(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS.CONTACT_ID,
				CONTACTS.HREF
			)
			.from(CONTACTS)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
			)
			.orderBy(
				CONTACTS.CONTACT_ID.asc()
			)
			.fetchGroups(CONTACTS.HREF, CONTACTS.CONTACT_ID);
	}
	
	public Map<Integer, OContact> selectByCategoryHrefs(Connection con, int categoryId, Collection<String> hrefs) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CONTACTS.fields()
			)
			.from(CONTACTS)
			.join(CATEGORIES).on(CONTACTS.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS.IS_LIST.equal(false))
				.and(
					CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW))
					.or(CONTACTS.REVISION_STATUS.equal(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED)))
				)
				.and(CONTACTS.HREF.in(hrefs))
			)
			.orderBy(
				CONTACTS.CONTACT_ID.asc()
			)
			.fetchMap(CONTACTS.CONTACT_ID, OContact.class);
	}
	
	public int insert(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(EnumUtils.toSerializedName(Contact.RevisionStatus.NEW));
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence(0);
		item.setCreationTimestamp(revisionTimestamp);
		ContactsRecord record = dsl.newRecord(CONTACTS, item);
		return dsl
			.insertInto(CONTACTS)
			.set(record)
			.execute();
	}
	
	public int batchInsert(Connection con, ArrayList<OContact> items, DateTime revisionTimestamp) throws DAOException {
		final String NEW = EnumUtils.toSerializedName(Contact.RevisionStatus.NEW);
		DSLContext dsl = getDSL(con);
		ArrayList<ContactsRecord> records = new ArrayList<>();
		for (OContact item : items) {
			item.setRevisionStatus(NEW);
			item.setRevisionTimestamp(revisionTimestamp);
			item.setRevisionSequence(0);
			item.setCreationTimestamp(revisionTimestamp);
			records.add(dsl.newRecord(CONTACTS, item));
		}
		dsl.batchInsert(records).execute();
		return items.size();
	}
	
	public int update(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED));
		item.setRevisionTimestamp(revisionTimestamp);
		item.setRevisionSequence(0);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.CATEGORY_ID, item.getCategoryId())
			.set(CONTACTS.REVISION_STATUS, item.getRevisionStatus())
			.set(CONTACTS.REVISION_TIMESTAMP, item.getRevisionTimestamp())
			.set(CONTACTS.SEARCHFIELD, item.getSearchfield())
			.set(CONTACTS.TITLE, item.getTitle())
			.set(CONTACTS.FIRSTNAME, item.getFirstname())
			.set(CONTACTS.LASTNAME, item.getLastname())
			.set(CONTACTS.NICKNAME, item.getNickname())
			.set(CONTACTS.GENDER, item.getGender())
			.set(CONTACTS.COMPANY, item.getCompany())
			.set(CONTACTS.FUNCTION, item.getFunction())
			.set(CONTACTS.WORK_ADDRESS, item.getWorkAddress())
			.set(CONTACTS.WORK_CITY, item.getWorkCity())
			.set(CONTACTS.WORK_STATE, item.getWorkState())
			.set(CONTACTS.WORK_POSTALCODE, item.getWorkPostalcode())
			.set(CONTACTS.WORK_COUNTRY, item.getWorkCountry())
			.set(CONTACTS.WORK_TELEPHONE, item.getWorkTelephone())
			.set(CONTACTS.WORK_TELEPHONE2, item.getWorkTelephone2())
			.set(CONTACTS.WORK_FAX, item.getWorkFax())
			.set(CONTACTS.WORK_MOBILE, item.getWorkMobile())
			.set(CONTACTS.WORK_PAGER, item.getWorkPager())
			.set(CONTACTS.WORK_EMAIL, item.getWorkEmail())
			.set(CONTACTS.WORK_IM, item.getWorkIm())
			.set(CONTACTS.ASSISTANT, item.getAssistant())
			.set(CONTACTS.ASSISTANT_TELEPHONE, item.getAssistantTelephone())
			.set(CONTACTS.DEPARTMENT, item.getDepartment())
			.set(CONTACTS.MANAGER, item.getManager())
			.set(CONTACTS.HOME_ADDRESS, item.getHomeAddress())
			.set(CONTACTS.HOME_CITY, item.getHomeCity())
			.set(CONTACTS.HOME_STATE, item.getHomeState())
			.set(CONTACTS.HOME_POSTALCODE, item.getHomePostalcode())
			.set(CONTACTS.HOME_COUNTRY, item.getHomeCountry())
			.set(CONTACTS.HOME_TELEPHONE, item.getHomeTelephone())
			.set(CONTACTS.HOME_TELEPHONE2, item.getHomeTelephone2())
			.set(CONTACTS.HOME_FAX, item.getHomeFax())
			.set(CONTACTS.HOME_MOBILE, item.getHomeMobile())
			.set(CONTACTS.HOME_PAGER, item.getHomePager())
			.set(CONTACTS.HOME_EMAIL, item.getHomeEmail())
			.set(CONTACTS.HOME_IM, item.getHomeIm())
			.set(CONTACTS.PARTNER, item.getPartner())
			.set(CONTACTS.BIRTHDAY, item.getBirthday())
			.set(CONTACTS.ANNIVERSARY, item.getAnniversary())
			.set(CONTACTS.OTHER_ADDRESS, item.getOtherAddress())
			.set(CONTACTS.OTHER_CITY, item.getOtherCity())
			.set(CONTACTS.OTHER_STATE, item.getOtherState())
			.set(CONTACTS.OTHER_POSTALCODE, item.getOtherPostalcode())
			.set(CONTACTS.OTHER_COUNTRY, item.getOtherCountry())
			.set(CONTACTS.OTHER_EMAIL, item.getOtherEmail())
			.set(CONTACTS.OTHER_IM, item.getOtherIm())
			.set(CONTACTS.URL, item.getUrl())
			.set(CONTACTS.NOTES, item.getNotes())
			.set(CONTACTS.ETAG, item.getEtag())
			.where(
				CONTACTS.CONTACT_ID.equal(item.getContactId())
			)
			.execute();
	}
	
	public int updateList(Connection con, OContact item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionStatus(EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED));
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
			.set(CONTACTS.REVISION_STATUS, EnumUtils.toSerializedName(Contact.RevisionStatus.MODIFIED))
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
	
	public int updateRevisionStatus(Connection con, int contactId, Contact.RevisionStatus revisionStatus, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.REVISION_STATUS, EnumUtils.toSerializedName(revisionStatus))
			.set(CONTACTS.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int deleteById(Connection con, int contactId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS)
			.where(
				CONTACTS.CONTACT_ID.equal(contactId)
			)
			.execute();
	}
	
	public int deleteByCategory(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CONTACTS)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
			)
			.execute();
	}
	
	public int logicDeleteById(Connection con, int contactId, DateTime revisionTimestamp) throws DAOException {
		final String DELETED = EnumUtils.toSerializedName(Contact.RevisionStatus.DELETED);
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.REVISION_STATUS, DELETED)
			.set(CONTACTS.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS.CONTACT_ID.equal(contactId)
				.and(CONTACTS.REVISION_STATUS.notEqual(DELETED))
			)
			.execute();
	}
	
	public int logicDeleteByCategory(Connection con, int categoryId, DateTime revisionTimestamp) throws DAOException {
		final String DELETED = EnumUtils.toSerializedName(Contact.RevisionStatus.DELETED);
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CONTACTS)
			.set(CONTACTS.REVISION_STATUS, DELETED)
			.set(CONTACTS.REVISION_TIMESTAMP, revisionTimestamp)
			.where(
				CONTACTS.CATEGORY_ID.equal(categoryId)
				.and(CONTACTS.REVISION_STATUS.notEqual(DELETED))
			)
			.execute();
	}
	
	public boolean hasTableFieldFor(RecipientFieldType fieldType, RecipientFieldCategory fieldCategory) {
		return getTableFieldBy(fieldType, fieldCategory) != null;
	}
	
	private Field<?> getTableFieldBy(RecipientFieldType fieldType, RecipientFieldCategory fieldCategory) {
		if (fieldType.equals(RecipientFieldType.TELEPHONE)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS.WORK_TELEPHONE;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS.HOME_TELEPHONE;
			}
		} else if (fieldType.equals(RecipientFieldType.TELEPHONE_2)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS.WORK_TELEPHONE2;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS.HOME_TELEPHONE2;
			}
		} else if (fieldType.equals(RecipientFieldType.FAX)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS.WORK_FAX;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS.HOME_FAX;
			}
		} else if (fieldType.equals(RecipientFieldType.MOBILE)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS.WORK_MOBILE;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS.HOME_MOBILE;
			}
		} else if (fieldType.equals(RecipientFieldType.PAGER)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS.WORK_PAGER;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS.HOME_PAGER;
			}
		} else if (fieldType.equals(RecipientFieldType.EMAIL)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS.WORK_EMAIL;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS.HOME_EMAIL;
			} else if (fieldCategory.equals(RecipientFieldCategory.OTHER)) {
				return CONTACTS.OTHER_EMAIL;
			}
		} else if (fieldType.equals(RecipientFieldType.IM)) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return CONTACTS.WORK_EMAIL;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return CONTACTS.HOME_EMAIL;
			} else if (fieldCategory.equals(RecipientFieldCategory.OTHER)) {
				return CONTACTS.OTHER_EMAIL;
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
