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

import com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode;
import com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator;
import com.sonicle.commons.web.json.CompId;
import java.util.Collection;
import org.jooq.Condition;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_CUSTOM_VALUES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_TAGS;
import com.sonicle.webtop.core.app.sdk.JOOQPredicateVisitorWithCValues;
import com.sonicle.webtop.core.app.sdk.QBuilderWithCValues;
import org.apache.commons.lang3.StringUtils;
import static org.jooq.impl.DSL.*;

/**
 *
 * @author malbinola
 */
public class ContactPredicateVisitor extends JOOQPredicateVisitorWithCValues {
	
	public ContactPredicateVisitor() {
		super(false);
	}
	
	@Override
	protected Condition toCondition(String fieldName, ComparisonOperator operator, Collection<?> values, ComparisonNode node) {
		if ("name".equals(fieldName)) {
			return CONTACTS.DISPLAY_NAME.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values)))
				.or(CONTACTS.FIRSTNAME.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.LASTNAME.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))));
			
		} else if ("company".equals(fieldName)) {
			return CONTACTS.COMPANY.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values)))
				.or(CONTACTS.COMPANY_MASTER_DATA_ID.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.DEPARTMENT.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.FUNCTION.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))));
			
		} else if ("email".equals(fieldName)) {
			return CONTACTS.WORK_EMAIL.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values)))
				.or(CONTACTS.HOME_EMAIL.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.OTHER_EMAIL.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))));
			
		} else if ("phone".equals(fieldName)) {
			return CONTACTS.WORK_MOBILE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values)))
				.or(CONTACTS.WORK_TELEPHONE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_TELEPHONE2.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_PAGER.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_FAX.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_TELEPHONE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_TELEPHONE2.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_PAGER.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_FAX.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))));
			
		} else if ("address".equals(fieldName)) {
			return CONTACTS.WORK_ADDRESS.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values)))
				.or(CONTACTS.WORK_POSTALCODE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_CITY.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_STATE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_COUNTRY.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_ADDRESS.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_POSTALCODE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_CITY.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_STATE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_COUNTRY.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.OTHER_ADDRESS.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.OTHER_POSTALCODE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.OTHER_CITY.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.OTHER_STATE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.OTHER_COUNTRY.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))));
			
		} else if ("notes".equals(fieldName)) {
			return CONTACTS.NOTES.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values)));
			
		} else if ("tag".equals(fieldName)) {
			return exists(
					selectOne()
					.from(CONTACTS_TAGS)
					.where(
						CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS.CONTACT_ID)
						.and(CONTACTS_TAGS.TAG_ID.equal(singleAsString(values)))
					)
				);
			
		} else if ("any".equals(fieldName)) {
			return CONTACTS.DISPLAY_NAME.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values)))
				.or(CONTACTS.FIRSTNAME.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.LASTNAME.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.COMPANY.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.COMPANY_MASTER_DATA_ID.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_EMAIL.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_EMAIL.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.OTHER_EMAIL.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_MOBILE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_TELEPHONE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_TELEPHONE2.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_PAGER.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.WORK_FAX.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_TELEPHONE.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_TELEPHONE2.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_PAGER.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))))
				.or(CONTACTS.HOME_FAX.likeIgnoreCase(valueToSmartLikePattern(singleAsString(values))));
			
		} else if (StringUtils.startsWith(fieldName, "CV")) {
			CompId fn = new CompId(2).parse(fieldName, false);
			if (fn.isTokenEmpty(1)) throw new UnsupportedOperationException("Field name invalid: " + fieldName);
			
			CValueCondition cvCondition = getCustomFieldCondition(fn, operator, values);
			if (cvCondition.negated) {
				return notExists(
					selectOne()
					.from(CONTACTS_CUSTOM_VALUES)
					.where(
						CONTACTS_CUSTOM_VALUES.CONTACT_ID.equal(CONTACTS.CONTACT_ID)
						.and(CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID.equal(fn.getToken(1)))
						.and(cvCondition.condition)
					)
				);
				
			} else {
				return exists(
					selectOne()
					.from(CONTACTS_CUSTOM_VALUES)
					.where(
						CONTACTS_CUSTOM_VALUES.CONTACT_ID.equal(CONTACTS.CONTACT_ID)
						.and(CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID.equal(fn.getToken(1)))
						.and(cvCondition.condition)
					)
				);
			}
			
		} else {
			throw new UnsupportedOperationException("Field not supported: " + fieldName);
		}
	}
	
	@Override
	protected Condition cvalueCondition(QBuilderWithCValues.Type cvalueType, ComparisonOperator operator, Collection<?> values) {
		if (QBuilderWithCValues.Type.CVSTRING.equals(cvalueType)) {
			return defaultCondition(CONTACTS_CUSTOM_VALUES.STRING_VALUE, operator, values);
			
		} else if (QBuilderWithCValues.Type.CVNUMBER.equals(cvalueType)) {
			return defaultCondition(CONTACTS_CUSTOM_VALUES.NUMBER_VALUE, operator, values);
			
		} else if (QBuilderWithCValues.Type.CVBOOL.equals(cvalueType)) {
			return defaultCondition(CONTACTS_CUSTOM_VALUES.BOOLEAN_VALUE, operator, values);
			
		} else if (QBuilderWithCValues.Type.CVDATE.equals(cvalueType)) {
			return defaultCondition(CONTACTS_CUSTOM_VALUES.DATE_VALUE, operator, values);
			
		} else if (QBuilderWithCValues.Type.CVTEXT.equals(cvalueType)) {
			return defaultCondition(CONTACTS_CUSTOM_VALUES.TEXT_VALUE, operator, values);
			
		} else {
			return null;
		}
	}
}
