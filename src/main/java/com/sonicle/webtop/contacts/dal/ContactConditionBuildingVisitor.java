/*
 * Copyright (C) 2025 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2025 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.dal;

import com.sonicle.commons.rsql.parser.Operator;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_CUSTOM_VALUES;
import static com.sonicle.webtop.contacts.jooq.Tables.CONTACTS_TAGS;
import com.sonicle.webtop.contacts.jooq.tables.ContactsCustomValues;
import com.sonicle.webtop.contacts.jooq.tables.ContactsTags;
import com.sonicle.webtop.contacts.model.ContactQuery;
import com.sonicle.webtop.core.app.sdk.JOOQConditionBuildingVisitorWithCFields;
import com.sonicle.webtop.core.model.CustomFieldBase;
import java.util.Collection;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.TableLike;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.selectOne;

/**
 *
 * @author malbinola
 */
public class ContactConditionBuildingVisitor extends JOOQConditionBuildingVisitorWithCFields {
	private final ContactsCustomValues PV_CONTACTS_CUSTOM_VALUES = CONTACTS_CUSTOM_VALUES.as("pvis_cv");
	private final ContactsTags PV_CONTACTS_TAGS = CONTACTS_TAGS.as("pvis_ct");
	
	public ContactConditionBuildingVisitor() {
		super(false);
	}
	
	@Override
	protected Condition buildCondition(String fieldName, Operator operator, Collection<?> values) {
		if (ContactQuery.ID.equals(fieldName)) {
			return defaultCondition(CONTACTS_.CONTACT_ID, operator, values);
			
		} else if (ContactQuery.CREATED_AT.equals(fieldName)) {
			return defaultCondition(CONTACTS_.CREATION_TIMESTAMP, operator, values);
			
		} else if (ContactQuery.UPDATED_AT.equals(fieldName)) {
			return defaultCondition(CONTACTS_.REVISION_TIMESTAMP, operator, values);
			
		} else if (ContactQuery.DISPLAY_NAME.equals(fieldName)) {
			return defaultCondition(CONTACTS_.DISPLAY_NAME, operator, values);
			
		} else if (ContactQuery.TITLE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.TITLE, operator, values);
			
		} else if (ContactQuery.FIRSTNAME.equals(fieldName)) {
			return defaultCondition(CONTACTS_.FIRSTNAME, operator, values);
			
		} else if (ContactQuery.LASTNAME.equals(fieldName)) {
			return defaultCondition(CONTACTS_.LASTNAME, operator, values);
			
		} else if (ContactQuery.NICKNAME.equals(fieldName)) {
			return defaultCondition(CONTACTS_.NICKNAME, operator, values);
			
		} else if (ContactQuery.MOBILE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_MOBILE, operator, values);
			
		} else if (ContactQuery.PAGER1.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_PAGER, operator, values);
			
		} else if (ContactQuery.PAGER2.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_PAGER, operator, values);
			
		} else if (ContactQuery.EMAIL1.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_EMAIL, operator, values);
			
		} else if (ContactQuery.EMAIL2.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_EMAIL, operator, values);
			
		} else if (ContactQuery.EMAIL3.equals(fieldName)) {
			return defaultCondition(CONTACTS_.OTHER_EMAIL, operator, values);
			
		} else if (ContactQuery.IM1.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_IM, operator, values);
			
		} else if (ContactQuery.IM2.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_IM, operator, values);
			
		} else if (ContactQuery.IM3.equals(fieldName)) {
			return defaultCondition(CONTACTS_.OTHER_IM, operator, values);
			
		} else if (ContactQuery.WORK_TELEPHONE1.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_TELEPHONE, operator, values);
			
		} else if (ContactQuery.WORK_TELEPHONE2.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_TELEPHONE2, operator, values);
			
		} else if (ContactQuery.WORK_FAX.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_FAX, operator, values);
			
		} else if (ContactQuery.HOME_TELEPHONE1.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_TELEPHONE, operator, values);
			
		} else if (ContactQuery.HOME_TELEPHONE2.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_TELEPHONE2, operator, values);
			
		} else if (ContactQuery.HOME_FAX.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_FAX, operator, values);
			
		} else if (ContactQuery.WORK_ADDRESS.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_ADDRESS, operator, values);
			
		} else if (ContactQuery.WORK_POSTALCODE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_POSTALCODE, operator, values);
			
		} else if (ContactQuery.WORK_CITY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_CITY, operator, values);
			
		} else if (ContactQuery.WORK_STATE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_STATE, operator, values);
			
		} else if (ContactQuery.WORK_COUNTRY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_COUNTRY, operator, values);
			
		} else if (ContactQuery.HOME_ADDRESS.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_ADDRESS, operator, values);
			
		} else if (ContactQuery.HOME_POSTALCODE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_POSTALCODE, operator, values);
			
		} else if (ContactQuery.HOME_CITY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_CITY, operator, values);
			
		} else if (ContactQuery.HOME_STATE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_STATE, operator, values);
			
		} else if (ContactQuery.HOME_COUNTRY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_COUNTRY, operator, values);
			
		} else if (ContactQuery.OTHER_ADDRESS.equals(fieldName)) {
			return defaultCondition(CONTACTS_.OTHER_ADDRESS, operator, values);
			
		} else if (ContactQuery.OTHER_POSTALCODE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.OTHER_POSTALCODE, operator, values);
			
		} else if (ContactQuery.OTHER_CITY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.OTHER_CITY, operator, values);
			
		} else if (ContactQuery.OTHER_STATE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.OTHER_STATE, operator, values);
			
		} else if (ContactQuery.OTHER_COUNTRY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.OTHER_COUNTRY, operator, values);
			
		} else if (ContactQuery.COMPANY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.COMPANY, operator, values);
			
		} else if (ContactQuery.COMPANY_ID.equals(fieldName)) {
			return defaultCondition(CONTACTS_.COMPANY_MASTER_DATA_ID, operator, values);
			
		} else if (ContactQuery.FUNCTION.equals(fieldName)) {
			return defaultCondition(CONTACTS_.FUNCTION, operator, values);
			
		} else if (ContactQuery.DEPARTMENT.equals(fieldName)) {
			return defaultCondition(CONTACTS_.DEPARTMENT, operator, values);
			
		} else if (ContactQuery.MANAGER.equals(fieldName)) {
			return defaultCondition(CONTACTS_.MANAGER, operator, values);
			
		} else if (ContactQuery.ASSISTANT.equals(fieldName)) {
			return defaultCondition(CONTACTS_.ASSISTANT, operator, values);
			
		} else if (ContactQuery.ASSISTANT_TELEPHONE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.ASSISTANT_TELEPHONE, operator, values);
			
		} else if (ContactQuery.PARTNER.equals(fieldName)) {
			return defaultCondition(CONTACTS_.PARTNER, operator, values);
			
		} else if (ContactQuery.BIRTHDAY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.BIRTHDAY, operator, values);
			
		} else if (ContactQuery.ANNIVERSARY.equals(fieldName)) {
			return defaultCondition(CONTACTS_.ANNIVERSARY, operator, values);
			
		} else if (ContactQuery.URL.equals(fieldName)) {
			return defaultCondition(CONTACTS_.URL, operator, values);
			
		} else if (ContactQuery.NOTES.equals(fieldName)) {
			return defaultCondition(CONTACTS_.NOTES, operator, values);
			
		} else if (ContactQuery.TAG_ID.equals(fieldName)) {
			return exists(
				selectOne()
				.from(CONTACTS_TAGS)
				.where(
					CONTACTS_TAGS.CONTACT_ID.equal(CONTACTS_.CONTACT_ID)
					.and(CONTACTS_TAGS.TAG_ID.equal(singleValueAsString(values)))
				)
			);
			
		} else if (ContactQuery.ANY_NAME.equals(fieldName)) {
			return defaultCondition(CONTACTS_.DISPLAY_NAME, operator, values)
				.or(defaultCondition(CONTACTS_.FIRSTNAME, operator, values)
				.or(defaultCondition(CONTACTS_.LASTNAME, operator, values)));
			
		} else if (ContactQuery.ANY_EMAIL.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_EMAIL, operator, values)
				.or(defaultCondition(CONTACTS_.HOME_EMAIL, operator, values))
				.or(defaultCondition(CONTACTS_.OTHER_EMAIL, operator, values));
			
		} else if (ContactQuery.ANY_PHONES.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_MOBILE, operator, values)
				.or(defaultCondition(CONTACTS_.WORK_PAGER, operator, values)) // aka pager1
				.or(defaultCondition(CONTACTS_.HOME_PAGER, operator, values)) // aka pager2
				.or(defaultCondition(CONTACTS_.WORK_TELEPHONE, operator, values))
				.or(defaultCondition(CONTACTS_.WORK_TELEPHONE2, operator, values))
				.or(defaultCondition(CONTACTS_.WORK_FAX, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_TELEPHONE, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_TELEPHONE2, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_FAX, operator, values));
			
		} else if (ContactQuery.ANY_WORK_PHONE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_TELEPHONE, operator, values)
				.or(defaultCondition(CONTACTS_.WORK_TELEPHONE2, operator, values))
				.or(defaultCondition(CONTACTS_.WORK_FAX, operator, values));
			
		} else if (ContactQuery.ANY_HOME_PHONE.equals(fieldName)) {
			return defaultCondition(CONTACTS_.HOME_TELEPHONE, operator, values)
				.or(defaultCondition(CONTACTS_.HOME_TELEPHONE2, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_FAX, operator, values));
		
		} else if (ContactQuery.ANY_ADDRESS.equals(fieldName)) {
			return defaultCondition(CONTACTS_.WORK_ADDRESS, operator, values)
				.or(defaultCondition(CONTACTS_.WORK_POSTALCODE, operator, values))
				.or(defaultCondition(CONTACTS_.WORK_CITY, operator, values))
				.or(defaultCondition(CONTACTS_.WORK_STATE, operator, values))
				.or(defaultCondition(CONTACTS_.WORK_COUNTRY, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_ADDRESS, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_POSTALCODE, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_CITY, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_STATE, operator, values))
				.or(defaultCondition(CONTACTS_.HOME_COUNTRY, operator, values))
				.or(defaultCondition(CONTACTS_.OTHER_ADDRESS, operator, values))
				.or(defaultCondition(CONTACTS_.OTHER_POSTALCODE, operator, values))
				.or(defaultCondition(CONTACTS_.OTHER_CITY, operator, values))
				.or(defaultCondition(CONTACTS_.OTHER_STATE, operator, values))
				.or(defaultCondition(CONTACTS_.OTHER_COUNTRY, operator, values));
		
		} else if (isCFieldPlainNotation(fieldName) || isCFieldWithRawValueTypeNotation(fieldName)) {
			return evalFieldNameAndGenerateCFieldCondition(fieldName, operator, values);
		}
		
		return null;
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
	protected Field<?> getTableCustomValuesTypeTableField(CustomFieldBase.RawValueType cvalueType) {
		if (CustomFieldBase.RawValueType.CVSTRING.equals(cvalueType)) {
			return PV_CONTACTS_CUSTOM_VALUES.STRING_VALUE;
			
		} else if (CustomFieldBase.RawValueType.CVSTRINGARRAY.equals(cvalueType)) {
			return PV_CONTACTS_CUSTOM_VALUES.STRING_VALUE;
			
		} else if (CustomFieldBase.RawValueType.CVNUMBER.equals(cvalueType)) {
			return PV_CONTACTS_CUSTOM_VALUES.NUMBER_VALUE;
			
		} else if (CustomFieldBase.RawValueType.CVBOOL.equals(cvalueType)) {
			return PV_CONTACTS_CUSTOM_VALUES.BOOLEAN_VALUE;
			
		} else if (CustomFieldBase.RawValueType.CVDATE.equals(cvalueType)) {
			return PV_CONTACTS_CUSTOM_VALUES.DATE_VALUE;
			
		} else if (CustomFieldBase.RawValueType.CVTEXT.equals(cvalueType)) {
			return PV_CONTACTS_CUSTOM_VALUES.TEXT_VALUE;
			
		} else {
			return null;
		}
	}

	@Override
	protected Condition getConditionCustomValuesForCurrentEntityAndField(String fieldId) {
		return PV_CONTACTS_CUSTOM_VALUES.CONTACT_ID.eq(CONTACTS_.CONTACT_ID)
			.and(PV_CONTACTS_CUSTOM_VALUES.CUSTOM_FIELD_ID.eq(fieldId));
	}
}
