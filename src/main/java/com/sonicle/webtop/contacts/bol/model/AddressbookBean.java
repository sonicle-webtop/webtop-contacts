/*
 * webtop-contacts is a WebTop Service developed by Sonicle S.r.l.
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
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * "Powered by Sonicle WebTop" logo. If the display of the logo is not reasonably
 * feasible for technical reasons, the Appropriate Legal Notices must display
 * the words "Powered by Sonicle WebTop".
 */
package com.sonicle.webtop.contacts.bol.model;

import com.sonicle.webtop.contacts.bol.OCategory;
import com.sonicle.webtop.contacts.bol.OContact;

/**
 *
 * @author malbinola
 */
public class AddressbookBean {
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
	public Integer categoryId;
	public String categoryName;
	public String categoryColor;
	
	public AddressbookBean(OCategory category, OContact contact) {
		this.contactId = contact.getContactId();
		this.title = contact.getTitle();
		this.firstName = contact.getFirstname();
		this.lastName = contact.getLastname();
		this.workTelephone = contact.getWorkTelephone();
		this.workMobile = contact.getWorkMobile();
		this.workEmail = contact.getWorkEmail();
		this.homeTelephone = contact.getHomeTelephone();
		this.homeEmail = contact.getHomeEmail();
		this.company = contact.getCompany();
		this.categoryId = contact.getCategoryId();
		this.categoryName = category.getName();
		this.categoryColor = category.getHexColor();
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

	public String getCompany() {
		return company;
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
}
