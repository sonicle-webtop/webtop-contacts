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

import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.bol.OCustomer;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.util.JRHelper;
import java.awt.Image;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class RBContactDetail {
	public Integer categoryId;
	public String categoryName;
	public String categoryColor;
	public Image categoryColorImage;
	public Integer contactId;
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
	public String homeTelephone2;
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
	public Date birthday;
	public Date anniversary;
	public String url;
	public String notes;
	public Image picture;
		
	public RBContactDetail(CoreManager core, Category category, Contact contact, ContactPicture picture) throws WTException {
		this.categoryId = contact.getCategoryId();
		this.categoryName = category.getName();
		this.categoryColor = category.getHexColor();
		this.categoryColorImage = JRHelper.colorAsImage(category.getHexColor());
		this.contactId = contact.getContactId();
		this.title = contact.getTitle();
		this.firstName = contact.getFirstName();
		this.lastName = contact.getLastName();
		this.nickname = contact.getNickname();
		this.gender = contact.getGender();
		this.workAddress = contact.getWorkAddress();
		this.workPostalCode = contact.getWorkPostalCode();
		this.workCity = contact.getWorkCity();
		this.workState = contact.getWorkState();
		this.workCountry = contact.getWorkCountry();
		this.workTelephone = contact.getWorkTelephone();
		this.workTelephone2 = contact.getWorkTelephone2();
		this.workMobile = contact.getWorkMobile();
		this.workFax = contact.getWorkFax();
		this.workPager = contact.getWorkPager();
		this.workEmail = contact.getWorkEmail();
		this.workInstantMsg = contact.getWorkInstantMsg();
		this.homeAddress = contact.getHomeAddress();
		this.homePostalCode = contact.getHomePostalCode();
		this.homeCity = contact.getHomeCity();
		this.homeState = contact.getHomeState();
		this.homeCountry = contact.getHomeCountry();
		this.homeTelephone = contact.getHomeTelephone();
		this.homeTelephone2 = contact.getHomeTelephone2();
		this.homeFax = contact.getHomeFax();
		this.homePager = contact.getHomePager();
		this.homeEmail = contact.getHomeEmail();
		this.homeInstantMsg = contact.getHomeInstantMsg();
		this.otherAddress = contact.getOtherAddress();
		this.otherPostalCode = contact.getOtherPostalCode();
		this.otherCity = contact.getOtherCity();
		this.otherState = contact.getOtherState();
		this.otherCountry = contact.getOtherCountry();
		this.otherEmail = contact.getOtherEmail();
		this.otherInstantMsg = contact.getOtherInstantMsg();
		this.company = StringUtils.defaultIfBlank(lookupCustomer(core, contact.getCompany()), contact.getCompany());	
		this.function = contact.getFunction();
		this.department = contact.getDepartment();
		this.manager = contact.getManager();
		this.assistant = contact.getAssistant();
		this.assistantTelephone = contact.getAssistantTelephone();
		this.partner = contact.getPartner();
		if(contact.getBirthday() != null) this.birthday = contact.getBirthday().toDate();
		if(contact.getAnniversary() != null) this.anniversary = contact.getAnniversary().toDate();
		this.url = contact.getUrl();
		this.notes = contact.getNotes();
		this.picture = null;
		
		//TODO: riempire l'immagine del contatto (sembra che le due immagini facciano interferenza)
		/*
		if(picture != null) {
			try {
				try(ByteArrayInputStream bais = new ByteArrayInputStream(picture.getBytes())) {
					this.picture = ImageIO.read(bais);
				}
			} catch (IOException ex) {}
		}
		*/
	}
	
	private String lookupCustomer(CoreManager core, String customerId) throws WTException {
		if(customerId == null) return null;
		OCustomer customer = core.getCustomer(customerId);
		return (customer != null) ? customer.getDescription() : null;
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

	public String getNickname() {
		return nickname;
	}

	public String getGender() {
		return gender;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public String getWorkPostalCode() {
		return workPostalCode;
	}

	public String getWorkCity() {
		return workCity;
	}

	public String getWorkState() {
		return workState;
	}

	public String getWorkCountry() {
		return workCountry;
	}

	public String getWorkTelephone() {
		return workTelephone;
	}

	public String getWorkTelephone2() {
		return workTelephone2;
	}

	public String getWorkMobile() {
		return workMobile;
	}

	public String getWorkFax() {
		return workFax;
	}

	public String getWorkPager() {
		return workPager;
	}

	public String getWorkEmail() {
		return workEmail;
	}

	public String getWorkInstantMsg() {
		return workInstantMsg;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public String getHomePostalCode() {
		return homePostalCode;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public String getHomeState() {
		return homeState;
	}

	public String getHomeCountry() {
		return homeCountry;
	}

	public String getHomeTelephone() {
		return homeTelephone;
	}

	public String getHomeTelephone2() {
		return homeTelephone2;
	}

	public String getHomeFax() {
		return homeFax;
	}

	public String getHomePager() {
		return homePager;
	}

	public String getHomeEmail() {
		return homeEmail;
	}

	public String getHomeInstantMsg() {
		return homeInstantMsg;
	}

	public String getOtherAddress() {
		return otherAddress;
	}

	public String getOtherPostalCode() {
		return otherPostalCode;
	}

	public String getOtherCity() {
		return otherCity;
	}

	public String getOtherState() {
		return otherState;
	}

	public String getOtherCountry() {
		return otherCountry;
	}

	public String getOtherEmail() {
		return otherEmail;
	}

	public String getOtherInstantMsg() {
		return otherInstantMsg;
	}

	public String getCompany() {
		return company;
	}

	public String getFunction() {
		return function;
	}

	public String getDepartment() {
		return department;
	}

	public String getManager() {
		return manager;
	}

	public String getAssistant() {
		return assistant;
	}

	public String getAssistantTelephone() {
		return assistantTelephone;
	}

	public String getPartner() {
		return partner;
	}

	public Date getBirthday() {
		return birthday;
	}

	public Date getAnniversary() {
		return anniversary;
	}

	public String getUrl() {
		return url;
	}

	public String getNotes() {
		return notes;
	}
	
	public Image getPicture() {
		return categoryColorImage;
	}
}
