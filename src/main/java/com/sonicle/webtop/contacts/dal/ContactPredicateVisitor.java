/*
 * Copyright (C) 2019 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2019 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.dal;

import com.sonicle.commons.qbuilders.nodes.ComparisonNode;
import com.sonicle.commons.qbuilders.operators.ComparisonOperator;
import com.sonicle.commons.web.json.CId;
import java.util.Collection;
import org.jooq.Condition;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_CUSTOM_VALUES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_TAGS;
import com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues;
import com.sonicle.webtop.contacts.jooq.tables.ContactsTags;
import com.sonicle.webtop.core.app.sdk.JOOQPredicateVisitorWithCValues;
import com.sonicle.webtop.core.app.sdk.QueryBuilderWithCValues;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Field;
import org.jooq.TableLike;
import static org.jooq.impl.DSL.*;

/**
 *
 * @author malbinola
 */
public class ContactPredicateVisitor extends JOOQPredicateVisitorWithCValues {
	private final ContactsCustomValues PV_CONTACTS_CUSTOM_VALUES = CONTACTS_CUSTOM_VALUES.as("pvis_cv");
	private final ContactsTags PV_CONTACTS_TAGS = CONTACTS_TAGS.as("pvis_ct");
	
	public ContactPredicateVisitor() {
		super(false);
	}
	
	@Override
	protected Condition toCondition(String fieldName, ComparisonOperator operator, Collection<?> values, ComparisonNode node) {
		if ("name".equals(fieldName)) {
			String singleAsString = valueToLikePattern(singleAsString(values));
			return CONTACTS_.DISPLAY_NAME.likeIgnoreCase(singleAsString)
				.or(CONTACTS_.FIRSTNAME.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.LASTNAME.likeIgnoreCase(singleAsString));
			
		} else if ("company".equals(fieldName)) {
			String singleAsString = valueToLikePattern(singleAsString(values));
			return CONTACTS_.COMPANY.likeIgnoreCase(singleAsString)
				.or(CONTACTS_.COMPANY_MASTER_DATA_ID.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.DEPARTMENT.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.FUNCTION.likeIgnoreCase(singleAsString));
			
		} else if ("companyId".equals(fieldName)) {
			String singleAsString = valueToLikePattern(singleAsString(values));
			return CONTACTS_.COMPANY_MASTER_DATA_ID.likeIgnoreCase(singleAsString);
			
		} else if ("email".equals(fieldName)) {
			String singleAsString = valueToLikePattern(singleAsString(values));
			return CONTACTS_.WORK_EMAIL.likeIgnoreCase(singleAsString)
				.or(CONTACTS_.HOME_EMAIL.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.OTHER_EMAIL.likeIgnoreCase(singleAsString));
			
		} else if ("phone".equals(fieldName)) {
			String singleAsString = valueToLikePattern(singleAsString(values));
			return CONTACTS_.WORK_MOBILE.likeIgnoreCase(singleAsString)
				.or(CONTACTS_.WORK_TELEPHONE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_TELEPHONE2.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_PAGER.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_FAX.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_TELEPHONE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_TELEPHONE2.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_PAGER.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_FAX.likeIgnoreCase(singleAsString));
			
		} else if ("address".equals(fieldName)) {
			String singleAsString = valueToLikePattern(singleAsString(values));
			return CONTACTS_.WORK_ADDRESS.likeIgnoreCase(singleAsString)
				.or(CONTACTS_.WORK_POSTALCODE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_CITY.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_STATE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_COUNTRY.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_ADDRESS.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_POSTALCODE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_CITY.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_STATE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_COUNTRY.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.OTHER_ADDRESS.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.OTHER_POSTALCODE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.OTHER_CITY.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.OTHER_STATE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.OTHER_COUNTRY.likeIgnoreCase(singleAsString));
			
		} else if ("notes".equals(fieldName)) {
			String singleAsString = valueToLikePattern(singleAsString(values));
			return CONTACTS_.NOTES.likeIgnoreCase(singleAsString);
			
		} else if ("tag".equals(fieldName)) {
			return exists(
					selectOne()
					.from(CONTACTS_TAGS)
					.where(
						CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
						.and(CONTACTS_TAGS.TAG_ID.equal(singleAsString(values)))
					)
				);
			
		} else if ("any".equals(fieldName)) {
			String singleAsString = valueToLikePattern(singleAsString(values));
			return CONTACTS_.DISPLAY_NAME.likeIgnoreCase(singleAsString)
				.or(CONTACTS_.FIRSTNAME.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.LASTNAME.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.COMPANY.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.COMPANY_MASTER_DATA_ID.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_EMAIL.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_EMAIL.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.OTHER_EMAIL.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_MOBILE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_TELEPHONE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_TELEPHONE2.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_PAGER.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.WORK_FAX.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_TELEPHONE.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_TELEPHONE2.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_PAGER.likeIgnoreCase(singleAsString))
				.or(CONTACTS_.HOME_FAX.likeIgnoreCase(singleAsString));
			
		} else if (StringUtils.startsWith(fieldName, "CV")) {
			CId fn = new CId(fieldName, 2);
			if (fn.isTokenEmpty(1)) throw new UnsupportedOperationException("Field name invalid: " + fieldName);
			return generateCValueCondition(fn, operator, values);
			
			/*
			CustomFields PV_CUSTOM_FIELDS = CUSTOM_FIELDS.as("pvis_cf");
			CustomPanelsFields PV_CUSTOM_PANELS_FIELDS = CUSTOM_PANELS_FIELDS.as("pvis_cpf");
			CustomPanelsTags PV_CUSTOM_PANELS_TAGS = CUSTOM_PANELS_TAGS.as("pvis_cpt");
			ContactsTags PV_CONTACTS_TAGS = CONTACTS_TAGS.as("pvis_ct");
			
			Condition fieldOnlineCond = exists(
				selectOne()
				.from(PV_CUSTOM_FIELDS)
				.join(PV_CUSTOM_PANELS_FIELDS).on(PV_CUSTOM_FIELDS.CUSTOM_FIELD_ID.eq(PV_CUSTOM_PANELS_FIELDS.CUSTOM_FIELD_ID))
				.join(PV_CUSTOM_PANELS_TAGS).on(PV_CUSTOM_PANELS_FIELDS.CUSTOM_PANEL_ID.eq(PV_CUSTOM_PANELS_TAGS.CUSTOM_PANEL_ID))
				.join(PV_CONTACTS_TAGS).on(PV_CUSTOM_PANELS_TAGS.TAG_ID.eq(PV_CONTACTS_TAGS.TAG_ID))
				.where(
					PV_CUSTOM_FIELDS.CUSTOM_FIELD_ID.eq(fn.getToken(1))
					.and(PV_CONTACTS_TAGS.CONTACT_ID.eq(CONTACTS_.CONTACT_ID))
				)
			);
			
			CValueConditionObject valueCond = toCValueConditionObject(fn, operator, values);
			if (valueCond.negated) {
				return fieldOnlineCond
						.andNotExists(
							selectOne()
							.from(PV_CONTACTS_CUSTOM_VALUES)
							.where(
								PV_CONTACTS_CUSTOM_VALUES.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
								.and(PV_CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID.equal(fn.getToken(1)))
								.and(valueCond.condition)
							)
						);
				
			} else {
				return fieldOnlineCond
						.andExists(
							selectOne()
							.from(PV_CONTACTS_CUSTOM_VALUES)
							.where(
								PV_CONTACTS_CUSTOM_VALUES.CONTACT_ID.eq(CONTACTS_.CONTACT_ID)
								.and(PV_CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID.eq(fn.getToken(1)))
								.and(valueCond.condition)
							)
				);
			}
			*/
			
		} else {
			throw new UnsupportedOperationException("Field not supported: " + fieldName);
		}
	}
	
	@Override
	protected Field<?> getFieldEntityIdOfEntityTable() {
		return CONTACTS_.CONTACT_ID;
	}
	
	@Override
	protected TableLike<?> getTableTags() {
		return PV_CONTACTS_TAGS;
	}
	
	@Override
	protected Field<String> getFieldTagIdOfTableTags() {
		return PV_CONTACTS_TAGS.TAG_ID;
	}
	
	@Override
	protected Condition getConditionTagsForCurrentEntity() {
		return PV_CONTACTS_TAGS.CONTACT_ID.eq(CONTACTS_.CONTACT_ID);
	}
	
	@Override
	protected TableLike<?> getTableCustomValues() {
		return PV_CONTACTS_CUSTOM_VALUES;
	}
	
	@Override
	protected Condition getConditionCustomValuesForCurrentEntityAndField(String fieldId) {
		return PV_CONTACTS_CUSTOM_VALUES.CONTACT_ID.eq(CONTACTS_.CONTACT_ID)
				.and(PV_CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID.eq(fieldId));
	}
	
	@Override
	protected Condition getConditionCustomValuesForFieldValue(QueryBuilderWithCValues.Type cvalueType, ComparisonOperator operator, Collection<?> values) {
		if (QueryBuilderWithCValues.Type.CVSTRING.equals(cvalueType)) {
			return defaultCondition(PV_CONTACTS_CUSTOM_VALUES.STRING_VALUE, operator, values);
			
		} else if (QueryBuilderWithCValues.Type.CVNUMBER.equals(cvalueType)) {
			return defaultCondition(PV_CONTACTS_CUSTOM_VALUES.NUMBER_VALUE, operator, values);
			
		} else if (QueryBuilderWithCValues.Type.CVBOOL.equals(cvalueType)) {
			return defaultCondition(PV_CONTACTS_CUSTOM_VALUES.BOOLEAN_VALUE, operator, values);
			
		} else if (QueryBuilderWithCValues.Type.CVDATE.equals(cvalueType)) {
			return defaultCondition(PV_CONTACTS_CUSTOM_VALUES.DATE_VALUE, operator, values);
			
		} else if (QueryBuilderWithCValues.Type.CVTEXT.equals(cvalueType)) {
			return defaultCondition(PV_CONTACTS_CUSTOM_VALUES.TEXT_VALUE, operator, values);
			
		} else {
			return null;
		}
	}
}
