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
package com.sonicle.webtop.contacts.bol.model;

import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.ContactLookup;
import com.sonicle.webtop.core.util.JRHelper;
import java.awt.Image;

/**
 *
 * @author malbinola
 */
public class RBAddressbook {
	public Boolean isList;
	public Integer categoryId;
	public String categoryName;
	public String categoryColor;
	public Image categoryColorImage;
	public Integer contactId;
	public String title;
	public String firstName;
	public String lastName;
	public String workTelephone;
	public String workMobile;
	public String workEmail;
	public String homeTelephone;
	public String homeEmail;
	public String company;
	
	public RBAddressbook(Category category, CategoryPropSet folderProps, ContactLookup contact) {
		this.isList = contact.getIsList();
		this.categoryId = contact.getCategoryId();
		this.categoryName = category.getName();
		String color = (folderProps != null) ? folderProps.getColorOrDefault(category.getColor()) : category.getColor();
		this.categoryColor = Category.getHexColor(color);
		this.categoryColorImage = JRHelper.colorAsImage(Category.getHexColor(color));
		this.contactId = contact.getContactId();
		this.title = contact.getTitle();
		this.firstName = contact.getFirstName();
		this.lastName = contact.getLastName();
		this.company = contact.getCompany();
		this.workTelephone = contact.getWorkTelephone();
		this.workMobile = contact.getWorkMobile();
		this.workEmail = contact.getWorkEmail();
		this.homeTelephone = contact.getHomeTelephone();
		this.homeEmail = contact.getHomeEmail();
	}
	
	public Boolean getIsList() {
		return isList;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getCategoryColor() {
		return categoryColor;
	}
	
	public Image getCategoryColorImage() {
		return categoryColorImage;
	}

	public Integer getContactId() {
		return contactId;
	}

	public String getTitle() {
		return title;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getCompany() {
		return company;
	}

	public String getWorkTelephone() {
		return workTelephone;
	}

	public String getWorkMobile() {
		return workMobile;
	}

	public String getWorkEmail() {
		return workEmail;
	}

	public String getHomeTelephone() {
		return homeTelephone;
	}

	public String getHomeEmail() {
		return homeEmail;
	}
}
