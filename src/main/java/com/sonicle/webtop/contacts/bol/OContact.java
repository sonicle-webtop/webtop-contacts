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
package com.sonicle.webtop.contacts.bol;

import com.sonicle.webtop.contacts.jooq.tables.pojos.Contacts;
import com.sonicle.webtop.core.model.RecipientFieldCategory;
import com.sonicle.webtop.core.model.RecipientFieldType;

/**
 *
 * @author malbinola
 */
public class OContact extends Contacts {
	
	public String getValueBy(RecipientFieldType type, RecipientFieldCategory category) {
		if (type.equals(RecipientFieldType.TELEPHONE)) {
			if (category.equals(RecipientFieldCategory.WORK)) {
				return getWorkTelephone();
			} else if (category.equals(RecipientFieldCategory.HOME)) {
				return getHomeTelephone();
			}
		} else if (type.equals(RecipientFieldType.TELEPHONE_2)) {
			if (category.equals(RecipientFieldCategory.WORK)) {
				return getWorkTelephone2();
			} else if (category.equals(RecipientFieldCategory.HOME)) {
				return getHomeTelephone2();
			}
		} else if (type.equals(RecipientFieldType.FAX)) {
			if (category.equals(RecipientFieldCategory.WORK)) {
				return getWorkFax();
			} else if (category.equals(RecipientFieldCategory.HOME)) {
				return getHomeFax();
			}
		} else if (type.equals(RecipientFieldType.MOBILE)) {
			if (category.equals(RecipientFieldCategory.WORK)) {
				return getWorkMobile();
			} else if (category.equals(RecipientFieldCategory.HOME)) {
				return getHomeMobile();
			}
		} else if (type.equals(RecipientFieldType.PAGER)) {
			if (category.equals(RecipientFieldCategory.WORK)) {
				return getWorkPager();
			} else if (category.equals(RecipientFieldCategory.HOME)) {
				return getHomePager();
			}
		} else if (type.equals(RecipientFieldType.EMAIL)) {
			if (category.equals(RecipientFieldCategory.WORK)) {
				return getWorkEmail();
			} else if (category.equals(RecipientFieldCategory.HOME)) {
				return getHomeEmail();
			} else if (category.equals(RecipientFieldCategory.OTHER)) {
				return getOtherEmail();
			}
		} else if (type.equals(RecipientFieldType.IM)) {
			if (category.equals(RecipientFieldCategory.WORK)) {
				return getWorkIm();
			} else if (category.equals(RecipientFieldCategory.HOME)) {
				return getHomeIm();
			} else if (category.equals(RecipientFieldCategory.OTHER)) {
				return getOtherIm();
			}
		}
		return null;
	}
	
	public void setCompanyData(CompanyData companyData) {
		if (companyData != null) {
			setCompany(companyData.company);
			setCompanyMasterDataId(companyData.companyMasterDataId);
		}
	}
	
	public static class CompanyData {
		public String company;
		public String companyMasterDataId;
	}
}
