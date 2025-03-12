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
import com.sonicle.commons.beans.SortInfo;
import com.sonicle.webtop.contacts.bol.OCategory;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_CATEGORIES;
import static com.sonicle.webtop.contacts.jooq.Tables.CATEGORIES;
import com.sonicle.webtop.contacts.jooq.tables.records.CategoriesRecord;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryQuery;
import com.sonicle.webtop.core.bol.Owner;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.impl.DSL;

/**
 *
 * @author malbinola
 */
public class CategoryDAO extends BaseDAO {
	private final static CategoryDAO INSTANCE = new CategoryDAO();
	public static CategoryDAO getInstance() {
		return INSTANCE;
	}

	public Long getSequence(Connection con) throws DAOException {
		DSLContext dsl = getDSL(con);
		Long nextID = dsl.nextval(SEQ_CATEGORIES);
		return nextID;
	}
	
	public boolean existsById(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.selectCount()
			.from(CATEGORIES)
			.where(
				CATEGORIES.CATEGORY_ID.equal(categoryId)
			)
			.fetchOne(0, Integer.class) == 1;
	}
	
	public Owner selectOwnerById(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.CATEGORY_ID.equal(categoryId)
			)
			.fetchOneInto(Owner.class);
	}
	
	public String selectProviderById(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CATEGORIES.PROVIDER
			)
			.from(CATEGORIES)
			.where(
				CATEGORIES.CATEGORY_ID.equal(categoryId)
			)
			.fetchOneInto(String.class);
	}
	
	public Set<Integer> selectIdsByProfile(Connection con, String domainId, String userId) throws DAOException {
		return selectIdsByProfileIn(con, domainId, userId, null);
	}
	
	public Set<Integer> selectIdsByProfileIn(Connection con, String domainId, String userId, Collection<Integer> categoryIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		Condition cndtIn = categoryIds != null ? CATEGORIES.CATEGORY_ID.in(categoryIds) : DSL.trueCondition();
		return dsl
			.select(
				CATEGORIES.CATEGORY_ID
			)
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
				.and(cndtIn)
			)
			.orderBy(
				CATEGORIES.BUILT_IN.desc(),
				//CATEGORIES.PROVIDER.asc(),
				CATEGORIES.NAME.asc()
			)
			.fetchSet(CATEGORIES.CATEGORY_ID);
	}
	
	public OCategory selectById(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.CATEGORY_ID.equal(categoryId)
			)
			.fetchOneInto(OCategory.class);
	}
	
	public List<OCategory> selectByDomain(Connection con, String domainId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
			)
			.orderBy(
				CATEGORIES.BUILT_IN.desc(),
				//CATEGORIES.PROVIDER.asc(),
				CATEGORIES.NAME.asc()
			)
			.fetchInto(OCategory.class);
	}
	
	private static Collection<SortField<?>> toCategoriesOrderByClause(final Set<SortInfo> sortInfo) {
		ArrayList<SortField<?>> fields = new ArrayList<>();
		for (SortInfo si : sortInfo) {
			if (si.getField().equals(CategoryQuery.ID)) fields.add(BaseDAO.toSortField(CATEGORIES.CATEGORY_ID, si));
			else if (si.getField().equals(CategoryQuery.USER_ID)) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CATEGORIES.USER_ID, "")), si));
			else if (si.getField().equals(CategoryQuery.BUILT_IN)) fields.add(BaseDAO.toSortField(CATEGORIES.BUILT_IN, si));
			else if (si.getField().equals(CategoryQuery.PROVIDER)) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CATEGORIES.PROVIDER, "")), si));
			else if (si.getField().equals(CategoryQuery.NAME)) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CATEGORIES.NAME, "")), si));
			else if (si.getField().equals(CategoryQuery.DESCRIPTION)) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CATEGORIES.DESCRIPTION, "")), si).nullsLast());
			else if (si.getField().equals(CategoryQuery.COLOR)) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CATEGORIES.COLOR, "")), si).nullsLast());
			else if (si.getField().equals(CategoryQuery.SYNC)) fields.add(BaseDAO.toSortField(DSL.upper(DSL.nullif(CATEGORIES.SYNC, "")), si));
			else if (si.getField().equals(CategoryQuery.IS_DEFAULT)) fields.add(BaseDAO.toSortField(CATEGORIES.IS_DEFAULT, si));
		}
		return fields;
	}
	
	public int countByDomainIn(Connection con, String domainId, Collection<Integer> categoryIds, Condition condition) throws DAOException {
		DSLContext dsl = getDSL(con);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		
		return dsl
			.selectCount()
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.CATEGORY_ID.in(categoryIds))
				.and(filterCndt)
			)
			.fetchOne(0, Integer.class);
	}
	
	public List<OCategory> selectByDomainIn(Connection con, String domainId, Collection<Integer> categoryIds, Condition condition, Set<SortInfo> sortInfo, int limit, int offset) throws DAOException {
		DSLContext dsl = getDSL(con);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.CATEGORY_ID.in(categoryIds))
				.and(filterCndt)
			)
			.orderBy(
				toCategoriesOrderByClause(sortInfo)
			)
			.limit(limit)
			.offset(offset)
			.fetchInto(OCategory.class);
	}
	
	/*
	public static Condition createCategoriesChangedNewOrModifiedCondition() {
		return HISTORY_CATEGORIES.CHANGE_TYPE.equal(BaseDAO.CHANGE_TYPE_CREATION)
			.or(HISTORY_CATEGORIES.CHANGE_TYPE.equal(BaseDAO.CHANGE_TYPE_UPDATE));
	}
	
	public static Condition createCategoriesChangedSinceUntilCondition(DateTime since, DateTime until) {
		return HISTORY_CATEGORIES.CHANGE_TIMESTAMP.greaterThan(since)
			.and(HISTORY_CATEGORIES.CHANGE_TIMESTAMP.lessThan(until));
	}
	
	public void lazy_viewChangedCategories(Connection con, Collection<Integer> categoryIds, Condition condition, boolean statFields, int limit, int offset, VCategoryChanged.Consumer consumer) throws DAOException, WTException {
		DSLContext dsl = getDSL(con);
		Condition filterCndt = (condition != null) ? condition : DSL.trueCondition();
		
		Cursor<Record> cursor = dsl
			.select(
				HISTORY_CATEGORIES.CHANGE_TIMESTAMP,
				HISTORY_CATEGORIES.CHANGE_TYPE
			)
			.select(
				CATEGORIES.fields()
			)
			.distinctOn(HISTORY_CATEGORIES.CATEGORY_ID)
			.from(HISTORY_CATEGORIES)
			.leftOuterJoin(CATEGORIES).on(HISTORY_CATEGORIES.CATEGORY_ID.equal(CATEGORIES.CATEGORY_ID))
			.where(
				HISTORY_CATEGORIES.CATEGORY_ID.in(categoryIds)
				.and(filterCndt)
			)
			.orderBy(
				HISTORY_CATEGORIES.CATEGORY_ID.asc(),
				HISTORY_CATEGORIES.ID.desc()
			)
			.limit(limit)
			.offset(offset)
			.fetchLazy();
		
		try {
			for(;;) {
				VCategoryChanged vcc = cursor.fetchNextInto(VCategoryChanged.class);
				if (vcc == null) break;
				consumer.consume(vcc, con);
			}
		} finally {
			cursor.close();
		}
	}
	*/
	
	public List<OCategory> selectByDomainIn(Connection con, String domainId, Collection<Integer> categoryIds) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.CATEGORY_ID.in(categoryIds))
			)
			.orderBy(
				CATEGORIES.BUILT_IN.desc(),
				//CATEGORIES.PROVIDER.asc(),
				CATEGORIES.NAME.asc()
			)
			.fetchInto(OCategory.class);
	}
	
	public List<OCategory> selectByProfile(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
			)
			.orderBy(
				CATEGORIES.BUILT_IN.desc(),
				//CATEGORIES.PROVIDER.asc(),
				CATEGORIES.NAME.asc()
			)
			.fetchInto(OCategory.class);
	}
	
	public List<OCategory> selectByProfileIn(Connection con, String domainId, String userId, Integer[] categories) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
				.and(CATEGORIES.CATEGORY_ID.in(categories))
			)
			.orderBy(
				CATEGORIES.BUILT_IN.desc(),
				//CATEGORIES.PROVIDER.asc(),
				CATEGORIES.NAME.asc()
			)
			.fetchInto(OCategory.class);
	}
	
	public Integer selectBuiltInIdByProfile(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CATEGORIES.CATEGORY_ID
			)
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
				.and(CATEGORIES.BUILT_IN.equal(true))
			)
			.fetchOneInto(Integer.class);
	}
	
	public OCategory selectBuiltInByProfile(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
				.and(CATEGORIES.BUILT_IN.equal(true))
			)
			.fetchOneInto(OCategory.class);
	}

	public List<OCategory> selectNoBuiltInByProfile(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
				.and(CATEGORIES.BUILT_IN.equal(false))
			)
			.orderBy(
				//CATEGORIES.PROVIDER.asc(),
				CATEGORIES.NAME.asc()
			)
			.fetchInto(OCategory.class);
	}
	
	public List<OCategory> selectByProvider(Connection con, Collection<Category.Provider> providers) throws DAOException {
		List<String> providerList = providers.stream().map(prov -> EnumUtils.toSerializedName(prov)).collect(Collectors.toList());
		DSLContext dsl = getDSL(con);
		return dsl
			.select()
			.from(CATEGORIES)
			.where(
				CATEGORIES.PROVIDER.in(providerList)
				.and(CATEGORIES.REMOTE_SYNC_FREQUENCY.isNotNull())
			)
			.orderBy(
				CATEGORIES.CATEGORY_ID.asc()
			)
			.fetchInto(OCategory.class);
	}
	
	public Set<Integer> selectIdsByProfileIn(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.select(
				CATEGORIES.CATEGORY_ID
			)
			.from(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
			)
			.fetchSet(CATEGORIES.CATEGORY_ID);
	}
	
	public int insert(Connection con, OCategory item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setCreationTimestamp(revisionTimestamp);
		item.setRevisionTimestamp(revisionTimestamp);
		CategoriesRecord record = dsl.newRecord(CATEGORIES, item);
		return dsl
			.insertInto(CATEGORIES)
			.set(record)
			.execute();
	}
	
	public int update(Connection con, OCategory item, DateTime revisionTimestamp) throws DAOException {
		DSLContext dsl = getDSL(con);
		item.setRevisionTimestamp(revisionTimestamp);
		return dsl
			.update(CATEGORIES)
			.set(CATEGORIES.REVISION_TIMESTAMP, item.getRevisionTimestamp())
			.set(CATEGORIES.NAME, item.getName())
			.set(CATEGORIES.DESCRIPTION, item.getDescription())
			.set(CATEGORIES.COLOR, item.getColor())
			.set(CATEGORIES.SYNC, item.getSync())
			.set(CATEGORIES.IS_DEFAULT, item.getIsDefault())
			.set(CATEGORIES.PARAMETERS, item.getParameters())
			.set(CATEGORIES.REMOTE_SYNC_FREQUENCY, item.getRemoteSyncFrequency())
			.where(
				CATEGORIES.CATEGORY_ID.equal(item.getCategoryId())
			)
			.execute();
	}
	
	/*
	public int update(Connection con, int categoryId, FieldsMap fieldValues) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CATEGORIES)
			.set(fieldValues)
			.where(
				CATEGORIES.CATEGORY_ID.equal(categoryId)
			)
			.execute();
	}
	*/
	
	public int updateRemoteSyncById(Connection con, int categoryId, DateTime syncTimestamp, String syncTag) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CATEGORIES)
			.set(CATEGORIES.REMOTE_SYNC_TIMESTAMP, syncTimestamp)
			.set(CATEGORIES.REMOTE_SYNC_TAG, syncTag)
			.where(
				CATEGORIES.CATEGORY_ID.equal(categoryId)
			)
			.execute();
	}
	
	public int resetIsDefaultByProfile(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CATEGORIES)
			.set(CATEGORIES.IS_DEFAULT, false)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
			)
			.execute();
	}
	
	public int deleteById(Connection con, int categoryId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CATEGORIES)
			.where(
				CATEGORIES.CATEGORY_ID.equal(categoryId)
			)
			.execute();
	}
	
	public int deleteByProfile(Connection con, String domainId, String userId) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.delete(CATEGORIES)
			.where(
				CATEGORIES.DOMAIN_ID.equal(domainId)
				.and(CATEGORIES.USER_ID.equal(userId))
			)
			.execute();
	}
}
