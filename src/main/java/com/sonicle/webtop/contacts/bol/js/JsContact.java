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
package com.sonicle.webtop.contacts.bol.js;

import com.sonicle.commons.web.json.CompositeId;
import com.sonicle.webtop.contacts.bol.OFolder;
import com.sonicle.webtop.contacts.bol.model.Contact;
import com.sonicle.webtop.core.sdk.UserProfile;

/**
 *
 * @author malbinola
 */
public class JsContact {
	public String id;
	public String contactId;
	public String folderId;
	public String title;
	public String firstName;
	public String lastName;
	public String nickname;
	public String gender;
	public String workAddress;
	public String workPostalCode;
	public String workCity;
	public String workState;
	public String workCountry;
	public String workTelephone;
	public String workTelephone2;
	public String workMobile;
	public String workFax;
	public String workPager;
	public String workEmail;
	public String workInstantMsg;
	public String homeAddress;
	public String homePostalCode;
	public String homeCity;
	public String homeState;
	public String homeCountry;
	public String homeTelephone;
	public String homeMobile;
	public String homeFax;
	public String homePager;
	public String homeEmail;
	public String homeInstantMsg;
	public String otherAddress;
	public String otherPostalCode;
	public String otherCity;
	public String otherState;
	public String otherCountry;
	public String otherEmail;
	public String otherInstantMsg;
	public String company;
	public String function;
	public String department;
	public String manager;
	public String assistant;
	public String assistantTelephone;
	public String partner;
	public String birthday;
	public String anniversary;
	public String url;
	public String photo;
	public String notes;
	public String _profileId;
	
	public JsContact() {
		
	}
	
	public JsContact(OFolder folder, Contact contact) {
		id = new CompositeId(contact.getFolderId(), contact.getContactId()).toString();
		contactId = contact.getContactId();
		folderId = contact.getFolderId();
		title = contact.getTitle();
		firstName = contact.getFirstName();
		lastName = contact.getLastName();
		nickname = contact.getNickname();
		gender = contact.getGender();
		workAddress = contact.getWorkAddress();
		workPostalCode = contact.getWorkPostalCode();
		workCity = contact.getWorkCity();
		workState = contact.getWorkState();
		workCountry = contact.getWorkCountry();
		workTelephone = contact.getWorkTelephone();
		workTelephone2 = contact.getWorkTelephone2();
		workMobile = contact.getWorkMobile();
		workFax = contact.getWorkFax();
		workPager = contact.getWorkPager();
		workEmail = contact.getWorkEmail();
		workInstantMsg = contact.getWorkInstantMsg();
		homeAddress = contact.getHomeAddress();
		homePostalCode = contact.getHomePostalCode();
		homeCity = contact.getHomeCity();
		homeState = contact.getHomeState();
		homeCountry = contact.getHomeCountry();
		homeTelephone = contact.getHomeTelephone();
		homeMobile = contact.getHomeMobile();
		homeFax = contact.getHomeFax();
		homePager = contact.getHomePager();
		homeEmail = contact.getHomeEmail();
		homeInstantMsg = contact.getHomeInstantMsg();
		otherAddress = contact.getOtherAddress();
		otherPostalCode = contact.getOtherPostalCode();
		otherCity = contact.getOtherCity();
		otherState = contact.getOtherState();
		otherCountry = contact.getOtherCountry();
		otherEmail = contact.getOtherEmail();
		otherInstantMsg = contact.getOtherInstantMsg();
		company = contact.getCompany();
		function = contact.getFunction();
		department = contact.getDepartment();
		manager = contact.getManager();
		assistant = contact.getAssistant();
		assistantTelephone = contact.getAssistantTelephone();
		partner = contact.getPartner();
		birthday = contact.getBirthday();
		anniversary = contact.getAnniversary();
		url = contact.getUrl();
		photo = contact.getPhoto();
		notes = contact.getNotes();
		_profileId = new UserProfile.Id(folder.getDomainId(), folder.getUserId()).toString();
	}
}
