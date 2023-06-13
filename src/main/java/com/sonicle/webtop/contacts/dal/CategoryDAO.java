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
import com.sonicle.webtop.contacts.bol.OCategory;
import static com.sonicle.webtop.contacts.jooq.Sequences.SEQ_CATEGORIES;
import static com.sonicle.webtop.contacts.jooq.Tables.CATEGORIES;
import com.sonicle.webtop.contacts.jooq.tables.records.CategoriesRecord;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.core.bol.Owner;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.jooq.Condition;
import org.jooq.DSLContext;
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
	
	public int insert(Connection con, OCategory item) throws DAOException {
		DSLContext dsl = getDSL(con);
		CategoriesRecord record = dsl.newRecord(CATEGORIES, item);
		return dsl
			.insertInto(CATEGORIES)
			.set(record)
			.execute();
	}
	
	public int update(Connection con, OCategory item) throws DAOException {
		DSLContext dsl = getDSL(con);
		return dsl
			.update(CATEGORIES)
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
