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
package com.sonicle.webtop.contacts.bol.js;

import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.time.DateTimeUtils;
import com.sonicle.commons.web.json.CompositeId;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactAttachment;
import com.sonicle.webtop.contacts.model.ContactBase;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.core.bol.js.ObjCustomFieldDefs;
import com.sonicle.webtop.core.bol.js.ObjCustomFieldValue;
import com.sonicle.webtop.core.model.CustomField;
import com.sonicle.webtop.core.model.CustomFieldValue;
import com.sonicle.webtop.core.model.CustomPanel;
import com.sonicle.webtop.core.sdk.UserProfileId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author malbinola
 */
public class JsContact {
	public Integer id;
	public Integer categoryId;
	public String displayName;
	public String title;
	public String firstName;
	public String lastName;
	public String nickname;
	public String gender;
	public String mobile;
	public String pager1;
	public String pager2;
	public String email1;
	public String email2;
	public String email3;
	public String instantMsg1;
	public String instantMsg2;
	public String instantMsg3;
	public String workAddress;
	public String workPostalCode;
	public String workCity;
	public String workState;
	public String workCountry;
	public String workTelephone1;
	public String workTelephone2;
	public String workFax;
	public String homeAddress;
	public String homePostalCode;
	public String homeCity;
	public String homeState;
	public String homeCountry;
	public String homeTelephone1;
	public String homeTelephone2;
	public String homeFax;
	public String otherAddress;
	public String otherPostalCode;
	public String otherCity;
	public String otherState;
	public String otherCountry;
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
	public String notes;
	public String tags;
	public String picture;
	public ArrayList<Attachment> attachments;
	public ArrayList<ObjCustomFieldValue> cvalues;
	public String _profileId; // Read-only
	public String _cfdefs; // Read-only
	
	public JsContact() {}
	
	public JsContact(UserProfileId ownerPid, Contact contact, Collection<CustomPanel> customPanels, Map<String, CustomField> customFields, String profileLanguageTag, DateTimeZone profileTz) {
		DateTimeFormatter ymdFmt = DateTimeUtils.createYmdFormatter();
		
		id = contact.getContactId();
		categoryId = contact.getCategoryId();
		displayName = contact.getDisplayName();
		title = contact.getTitle();
		firstName = contact.getFirstName();
		lastName = contact.getLastName();
		nickname = contact.getNickname();
		gender = EnumUtils.toSerializedName(contact.getGender());
		mobile = contact.getMobile();
		pager1 = contact.getPager1();
		pager2 = contact.getPager2();
		email1 = contact.getEmail1();
		email2 = contact.getEmail2();
		email3 = contact.getEmail3();
		instantMsg1 = contact.getInstantMsg1();
		instantMsg2 = contact.getInstantMsg2();
		instantMsg3 = contact.getInstantMsg3();
		workAddress = contact.getWorkAddress();
		workPostalCode = contact.getWorkPostalCode();
		workCity = contact.getWorkCity();
		workState = contact.getWorkState();
		workCountry = contact.getWorkCountry();
		workTelephone1 = contact.getWorkTelephone1();
		workTelephone2 = contact.getWorkTelephone2();
		workFax = contact.getWorkFax();
		homeAddress = contact.getHomeAddress();
		homePostalCode = contact.getHomePostalCode();
		homeCity = contact.getHomeCity();
		homeState = contact.getHomeState();
		homeCountry = contact.getHomeCountry();
		homeTelephone1 = contact.getHomeTelephone1();
		homeTelephone2 = contact.getHomeTelephone2();
		homeFax = contact.getHomeFax();
		otherAddress = contact.getOtherAddress();
		otherPostalCode = contact.getOtherPostalCode();
		otherCity = contact.getOtherCity();
		otherState = contact.getOtherState();
		otherCountry = contact.getOtherCountry();
		company = contact.hasCompany() ? contact.getCompany().getIdOrValue() : null;
		function = contact.getFunction();
		department = contact.getDepartment();
		manager = contact.getManager();
		assistant = contact.getAssistant();
		assistantTelephone = contact.getAssistantTelephone();
		partner = contact.getPartner();
		birthday = (contact.getBirthday() != null) ? ymdFmt.print(contact.getBirthday()) : null;
		anniversary = (contact.getAnniversary() != null) ? ymdFmt.print(contact.getAnniversary()) : null;
		url = contact.getUrl();
		notes = contact.getNotes();
		tags = new CompositeId(contact.getTags()).toString();
		picture = contact.hasPicture() ? String.valueOf(id) : null;
		attachments = new ArrayList<>();
		if (contact.hasAttachments()) {
			for (ContactAttachment att : contact.getAttachments()) {
				Attachment jsatt = new Attachment();
				jsatt.id = att.getAttachmentId();
				//jsatt.lastModified = DateTimeUtils.printYmdHmsWithZone(att.getRevisionTimestamp(), profileTz);
				jsatt.name = att.getFilename();
				jsatt.size = att.getSize();
				attachments.add(jsatt);
			}
		}
		
		cvalues = new ArrayList<>();
		ArrayList<ObjCustomFieldDefs.Panel> panels = new ArrayList<>();
		for (CustomPanel panel : customPanels) {
			panels.add(new ObjCustomFieldDefs.Panel(panel, profileLanguageTag));
		}
		ArrayList<ObjCustomFieldDefs.Field> fields = new ArrayList<>();
		for (CustomField field : customFields.values()) {
			CustomFieldValue cvalue = null;
			if (contact.hasCustomValues()) {
				cvalue = contact.getCustomValues().get(field.getFieldId());
			}
			cvalues.add(cvalue != null ? new ObjCustomFieldValue(field.getType(), cvalue, profileTz) : new ObjCustomFieldValue(field.getType(), field.getFieldId()));
			fields.add(new ObjCustomFieldDefs.Field(field, profileLanguageTag));
		}
		
		_profileId = ownerPid.toString();
		_cfdefs = LangUtils.serialize(new ObjCustomFieldDefs(panels, fields), ObjCustomFieldDefs.class);
	}
	
	public ContactEx createContactForInsert(DateTimeZone profileTz) {
		return createContactForUpdate(profileTz);
	}
	
	public Contact createContactForUpdate(DateTimeZone profileTz) {
		DateTimeFormatter ymdFmt = DateTimeUtils.createYmdFormatter();
		
		Contact item = new Contact();
		item.setContactId(id);
		item.setCategoryId(categoryId);
		item.setDisplayName(displayName);
		item.setTitle(title);
		item.setFirstName(firstName);
		item.setLastName(lastName);
		item.setNickname(nickname);
		item.setGender(EnumUtils.forSerializedName(gender, ContactBase.Gender.class));
		item.setMobile(mobile);
		item.setPager1(pager1);
		item.setPager2(pager2);
		item.setEmail1(email1);
		item.setEmail2(email2);
		item.setEmail3(email3);
		item.setInstantMsg1(instantMsg1);
		item.setInstantMsg2(instantMsg2);
		item.setInstantMsg3(instantMsg3);
		item.setWorkAddress(workAddress);
		item.setWorkPostalCode(workPostalCode);
		item.setWorkCity(workCity);
		item.setWorkState(workState);
		item.setWorkCountry(workCountry);
		item.setWorkTelephone1(workTelephone1);
		item.setWorkTelephone2(workTelephone2);
		item.setWorkFax(workFax);
		item.setHomeAddress(homeAddress);
		item.setHomePostalCode(homePostalCode);
		item.setHomeCity(homeCity);
		item.setHomeState(homeState);
		item.setHomeCountry(homeCountry);
		item.setHomeTelephone1(homeTelephone1);
		item.setHomeTelephone2(homeTelephone2);
		item.setHomeFax(homeFax);
		item.setOtherAddress(otherAddress);
		item.setOtherPostalCode(otherPostalCode);
		item.setOtherCity(otherCity);
		item.setOtherState(otherState);
		item.setOtherCountry(otherCountry);
		if (!StringUtils.isBlank(company)) item.setCompany(new ContactCompany(null, company));
		item.setFunction(function);
		item.setDepartment(department);
		item.setManager(manager);
		item.setAssistant(assistant);
		item.setAssistantTelephone(assistantTelephone);
		item.setPartner(partner);
		if (!StringUtils.isEmpty(birthday)) item.setBirthday(ymdFmt.parseLocalDate(birthday));
		if (!StringUtils.isEmpty(anniversary)) item.setAnniversary(ymdFmt.parseLocalDate(anniversary));
		item.setUrl(url);
		item.setNotes(notes);
		item.setTags(new LinkedHashSet<>(new CompositeId().parse(tags).getTokens()));
		
		ArrayList<CustomFieldValue> customValues = new ArrayList<>();
		for (ObjCustomFieldValue jscfv : cvalues) {
			customValues.add(jscfv.toCustomFieldValue(profileTz));
		}
		item.setCustomValues(customValues);
		// Attachment needs to be treated outside this class in order to have complete access to their streams
		return item;
	}
	
	public static class Attachment {
		public String id;
		public String name;
		public Long size;
		public String _uplId;
	}
}
