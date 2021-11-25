/*
 * Copyright (C) 2018 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2018 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts;

import com.sonicle.commons.EnumUtils;
import static com.sonicle.webtop.contacts.IContactsManager.RCPT_ORIGIN_LIST;
import com.sonicle.webtop.contacts.bol.OCategory;
import com.sonicle.webtop.contacts.bol.OCategoryPropSet;
import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.OContactAttachment;
import com.sonicle.webtop.contacts.bol.OContactCustomValue;
import com.sonicle.webtop.contacts.bol.OContactPicture;
import com.sonicle.webtop.contacts.bol.OListRecipient;
import com.sonicle.webtop.contacts.bol.VContactBase;
import com.sonicle.webtop.contacts.bol.VContactObject;
import com.sonicle.webtop.contacts.bol.VContactCompany;
import com.sonicle.webtop.contacts.bol.VListRecipient;
import com.sonicle.webtop.contacts.bol.VContactLookup;
import com.sonicle.webtop.contacts.model.BaseContact;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactAttachment;
import com.sonicle.webtop.contacts.model.ContactBase;
import com.sonicle.webtop.contacts.model.ContactObject;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactCompanyJoined;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.contacts.model.ContactList;
import com.sonicle.webtop.contacts.model.ContactListBase;
import com.sonicle.webtop.contacts.model.ContactListEx;
import com.sonicle.webtop.contacts.model.ContactLookup;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.model.ContactListRecipient;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.model.CustomFieldValue;
import com.sonicle.webtop.core.sdk.UserProfileId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 *
 * @author malbinola
 */
public class ManagerUtils {
	
	static String getProductName() {
		return WT.getPlatformName() + " Contacts";
	}
	
	static int toOffset(int page, int limit) {
		return limit * (page-1);
	}
	
	static Category createCategory(OCategory src) {
		if (src == null) return null;
		return fillCategory(new Category(), src);
	}
	
	static Category fillCategory(Category tgt, OCategory src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCategoryId(src.getCategoryId());
			tgt.setDomainId(src.getDomainId());
			tgt.setUserId(src.getUserId());
			tgt.setBuiltIn(src.getBuiltIn());
			tgt.setProvider(EnumUtils.forSerializedName(src.getProvider(), Category.Provider.class));
			tgt.setName(src.getName());
			tgt.setDescription(src.getDescription());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.forSerializedName(src.getSync(), Category.Sync.class));
			// TODO: aggiungere supporto campo is_private
			//cat.setIsPrivate(ocat.getIsPrivate());
			tgt.setParameters(src.getParameters());
			tgt.setRemoteSyncFrequency(src.getRemoteSyncFrequency());
			tgt.setRemoteSyncTimestamp(src.getRemoteSyncTimestamp());
			tgt.setRemoteSyncTag(src.getRemoteSyncTag());
		}
		return tgt;
	}
	
	static OCategory createOCategory(Category src) {
		if (src == null) return null;
		return fillOCategory(new OCategory(), src);
	}
	
	static OCategory fillOCategory(OCategory tgt, Category src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCategoryId(src.getCategoryId());
			tgt.setDomainId(src.getDomainId());
			tgt.setUserId(src.getUserId());
			tgt.setBuiltIn(src.getBuiltIn());
			tgt.setProvider(EnumUtils.toSerializedName(src.getProvider()));
			tgt.setName(src.getName());
			tgt.setDescription(src.getDescription());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.toSerializedName(src.getSync()));
			// TODO: aggiungere supporto campo is_private
			//ocat.setIsPrivate(cat.getIsPrivate());
			tgt.setParameters(src.getParameters());
			tgt.setRemoteSyncFrequency(src.getRemoteSyncFrequency());
			tgt.setRemoteSyncTimestamp(src.getRemoteSyncTimestamp());
			tgt.setRemoteSyncTag(src.getRemoteSyncTag());
		}
		return tgt;
	}
	
	static CategoryPropSet createCategoryPropSet(OCategoryPropSet src) {
		if (src == null) return null;
		return fillCategoryPropSet(new CategoryPropSet(), src);
	}
	
	static CategoryPropSet fillCategoryPropSet(CategoryPropSet tgt, OCategoryPropSet src) {
		if ((tgt != null) && (src != null)) {
			tgt.setHidden(src.getHidden());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.forSerializedName(src.getSync(), Category.Sync.class));
		}
		return tgt;
	}
	
	static OCategoryPropSet createOCategoryPropSet(CategoryPropSet src) {
		if (src == null) return null;
		return fillOCategoryPropSet(new OCategoryPropSet(), src);
	}
	
	static OCategoryPropSet fillOCategoryPropSet(OCategoryPropSet tgt, CategoryPropSet src) {
		if ((tgt != null) && (src != null)) {
			tgt.setHidden(src.getHidden());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.toSerializedName(src.getSync()));
		}
		return tgt;
	}
	
	static <T extends ContactObject> T fillContactObject(T tgt, VContactObject src) {
		if ((tgt != null) && (src != null)) {
			tgt.setContactId(src.getContactId());
			tgt.setCategoryId(src.getCategoryId());
			tgt.setRevisionStatus(EnumUtils.forSerializedName(src.getRevisionStatus(), ContactBase.RevisionStatus.class));
			tgt.setRevisionTimestamp(src.getRevisionTimestamp());
			tgt.setPublicUid(src.getPublicUid());
			tgt.setHref(src.getHref());
		}
		return tgt;
	}
	
	static <T extends ContactListRecipient> T fillContactsListRecipient(T tgt, OListRecipient src) {
		if ((tgt != null) && (src != null)) {
			tgt.setListRecipientId(src.getListRecipientId());
			tgt.setRecipient(src.getRecipient());
			tgt.setRecipientContactId(src.getRecipientContactId());
			tgt.setRecipientType(src.getRecipientType());
		}
		return tgt;
	}
	
	static <T extends ContactLookup, S extends VContactLookup> T fillContactLookup(T tgt, S src) {
		if ((tgt != null) && (src != null)) {
			fillBaseContact(tgt, src);
			tgt.setIsList(src.getIsList());
			tgt.setCompany(createContactCompany(src));
			tgt.setFunction(src.getFunction());
			tgt.setMobile(src.getWorkMobile());
			tgt.setEmail1(src.getWorkEmail());
			tgt.setEmail2(src.getHomeEmail());
			tgt.setWorkCity(src.getWorkCity());
			tgt.setWorkTelephone1(src.getWorkTelephone());
			tgt.setHomeTelephone1(src.getHomeTelephone());
			tgt.setCategoryName(src.getCategoryName());
			tgt.setCategoryDomainId(src.getCategoryDomainId());
			tgt.setCategoryUserId(src.getCategoryUserId());
			tgt.setTags(src.getTags());
			tgt.setHasPicture(src.getHasPicture());
		}
		return tgt;
	}
	
	static <T extends BaseContact, S extends OContact> T fillBaseContact(T tgt, S src) {
		//TODO: candidate for remove!!
		if ((tgt != null) && (src != null)) {
			tgt.setContactId(src.getContactId());
			tgt.setCategoryId(src.getCategoryId());
			tgt.setRevisionStatus(EnumUtils.forSerializedName(src.getRevisionStatus(), BaseContact.RevisionStatus.class));
			tgt.setPublicUid(src.getPublicUid());
			tgt.setTitle(src.getTitle());
			tgt.setFirstName(src.getFirstname());
			tgt.setLastName(src.getLastname());
			tgt.setDisplayName(src.getDisplayName());
			tgt.setNickname(src.getNickname());
		}
		return tgt;
	}
	
	static ContactEx createContactEx(ContactListEx src) {
		if (src == null) return null;
		ContactEx tgt = new ContactEx();
		tgt.setCategoryId(src.getCategoryId());
		tgt.setDisplayName(src.getDisplayName());
		tgt.setFirstName(src.getDisplayName());
		tgt.setLastName(src.getDisplayName());
		tgt.setTags(src.getTags());
		return tgt;
	}
	
	static <T extends ContactList> T fillContactList(T tgt, OContact src) {
		fillContactList((ContactListBase)tgt, src);
		if ((tgt != null) && (src != null)) {
			tgt.setContactId(src.getContactId());
		}
		return tgt;
	}
	
	static <T extends ContactListBase> T fillContactList(T tgt, OContact src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCategoryId(src.getCategoryId());
			tgt.setPublicUid(src.getPublicUid());
			tgt.setRevisionStatus(EnumUtils.forSerializedName(src.getRevisionStatus(), ContactBase.RevisionStatus.class));
			tgt.setRevisionTimestamp(src.getRevisionTimestamp());
			tgt.setRevisionSequence(src.getRevisionSequence());
			tgt.setDisplayName(src.getDisplayName());
			tgt.setEmail(src.getWorkEmail());
		}
		return tgt;
	}
	
	static List<ContactListRecipient> createContactListRecipientList(List<VListRecipient> items) {
		ArrayList<ContactListRecipient> list = new ArrayList<>(items.size());
		for (VListRecipient item : items) {
			list.add(fillContactsListRecipient(new ContactListRecipient(), item));
		}
		return list;
	}
	
	static <T extends Contact> T fillContact(T tgt, OContact src) {
		fillContact((ContactBase)tgt, src);
		if ((tgt != null) && (src != null)) {
			tgt.setContactId(src.getContactId());
		}
		return tgt;
	}
	
	static <T extends ContactBase> T fillContact(T tgt, OContact src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCategoryId(src.getCategoryId());
			tgt.setPublicUid(src.getPublicUid());
			tgt.setRevisionStatus(EnumUtils.forSerializedName(src.getRevisionStatus(), ContactBase.RevisionStatus.class));
			tgt.setRevisionTimestamp(src.getRevisionTimestamp());
			tgt.setRevisionSequence(src.getRevisionSequence());
			tgt.setCreationTimestamp(src.getCreationTimestamp());
			tgt.setTitle(src.getTitle());
			tgt.setFirstName(src.getFirstname());
			tgt.setLastName(src.getLastname());
			tgt.setDisplayName(src.getDisplayName());
			tgt.setNickname(src.getNickname());
			tgt.setGender(EnumUtils.forSerializedName(src.getGender(), ContactBase.Gender.class));
			tgt.setMobile(src.getWorkMobile());
			tgt.setPager1(src.getWorkPager());
			tgt.setPager2(src.getHomePager());
			tgt.setEmail1(src.getWorkEmail());
			tgt.setEmail2(src.getHomeEmail());
			tgt.setEmail3(src.getOtherEmail());
			tgt.setInstantMsg1(src.getWorkIm());
			tgt.setInstantMsg2(src.getHomeIm());
			tgt.setInstantMsg3(src.getOtherIm());
			tgt.setWorkAddress(src.getWorkAddress());
			tgt.setWorkPostalCode(src.getWorkPostalcode());
			tgt.setWorkCity(src.getWorkCity());
			tgt.setWorkState(src.getWorkState());
			tgt.setWorkCountry(src.getWorkCountry());
			tgt.setWorkTelephone1(src.getWorkTelephone());
			tgt.setWorkTelephone2(src.getWorkTelephone2());
			tgt.setWorkFax(src.getWorkFax());
			tgt.setHomeAddress(src.getHomeAddress());
			tgt.setHomePostalCode(src.getHomePostalcode());
			tgt.setHomeCity(src.getHomeCity());
			tgt.setHomeState(src.getHomeState());
			tgt.setHomeCountry(src.getHomeCountry());
			tgt.setHomeTelephone1(src.getHomeTelephone());
			tgt.setHomeTelephone2(src.getHomeTelephone2());
			tgt.setHomeFax(src.getHomeFax());
			tgt.setOtherAddress(src.getOtherAddress());
			tgt.setOtherPostalCode(src.getOtherPostalcode());
			tgt.setOtherCity(src.getOtherCity());
			tgt.setOtherState(src.getOtherState());
			tgt.setOtherCountry(src.getOtherCountry());
			tgt.setFunction(src.getFunction());
			tgt.setDepartment(src.getDepartment());
			tgt.setManager(src.getManager());
			tgt.setAssistant(src.getAssistant());
			tgt.setAssistantTelephone(src.getAssistantTelephone());
			tgt.setPartner(src.getPartner());
			tgt.setBirthday(src.getBirthday());
			tgt.setAnniversary(src.getAnniversary());
			tgt.setUrl(src.getUrl());
			tgt.setNotes(src.getNotes());
			tgt.setHref(src.getHref());
			tgt.setEtag(src.getEtag());
		}
		return tgt;
	}
	
	static ContactCompany createContactCompany(OContact src) {
		if (src == null) return null;
		if ((src.getCompany() != null) || (src.getCompanyMasterDataId() != null)) {
			return new ContactCompany(src.getCompany(), src.getCompanyMasterDataId());
		} else {
			return null;
		}
	}
	
	static ContactCompany createContactCompany(VContactBase src) {
		if (src == null) return null;
		if ((src.getCompany() != null) || (src.getCompanyMasterDataId() != null)) {
			return new ContactCompanyJoined(src.getCompany(), src.getCompanyMasterDataId(), src.getMasterDataId(), src.getMasterDataDescription());
		} else {
			return null;
		}
	}
	
	static ContactCompany createContactCompany(VContactCompany src) {
		if (src == null) return null;
		if ((src.getCompany() != null) || (src.getCompanyRawId() != null)) {
			return new ContactCompanyJoined(src.getCompany(), src.getCompanyRawId(), src.getMasterDataId(), src.getMasterDataDescription());
		} else {
			return null;
		}
	}
	
	static <T extends ContactPicture> T fillContactPicture(T tgt, OContactPicture src) {
		if ((tgt != null) && (src != null)) {
			tgt.setWidth(src.getWidth());
			tgt.setHeight(src.getHeight());
			tgt.setMediaType(src.getMediaType());
		}
		return tgt;
	}
	
	static OContact fillOContactWithDefaultsForInsert(OContact tgt, UserProfileId targetProfile, DateTime defaultTimestamp) {
		if (tgt != null) {
			if (StringUtils.isBlank(tgt.getPublicUid())) {
				tgt.setPublicUid(ContactsUtils.buildContactUid(tgt.getContactId(), WT.getDomainInternetName(targetProfile.getDomainId())));
			}
			if (tgt.getRevisionTimestamp()== null) tgt.setRevisionTimestamp(defaultTimestamp);
			if (tgt.getRevisionSequence() == null) tgt.setRevisionSequence(0);
			if (tgt.getCreationTimestamp() == null) tgt.setCreationTimestamp(defaultTimestamp);
			if (StringUtils.isBlank(tgt.getHref())) tgt.setHref(ContactsUtils.buildHref(tgt.getPublicUid()));
			if (!tgt.getIsList()) {
				if (StringUtils.isBlank(tgt.getDisplayName())) tgt.setDisplayName(BaseContact.buildFullName(tgt.getFirstname(), tgt.getLastname()));
			} else {
				// Compose list workEmail as: "list-{contactId}@{serviceId}"
				tgt.setWorkEmail(RCPT_ORIGIN_LIST + "-" + tgt.getContactId() + "@com.sonicle.webtop.contacts");
			}
		}
		return tgt;
	}
	
	static OContact fillOContactWithDefaultsForUpdate(OContact tgt, DateTime defaultTimestamp) {
		if (tgt != null) {
			if (!tgt.getIsList()) {
				if (StringUtils.isBlank(tgt.getDisplayName())) tgt.setDisplayName(BaseContact.buildFullName(tgt.getFirstname(), tgt.getLastname()));
			} else {
				// Compose list workEmail as: "list-{contactId}@{serviceId}"
				tgt.setWorkEmail(RCPT_ORIGIN_LIST + "-" + tgt.getContactId() + "@com.sonicle.webtop.contacts");
			}
		}
		return tgt;
	}
	
	static OContact fillOContact(OContact tgt, ContactBase src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCategoryId(src.getCategoryId());
			tgt.setPublicUid(src.getPublicUid());
			tgt.setRevisionStatus(EnumUtils.toSerializedName(src.getRevisionStatus()));
			tgt.setRevisionTimestamp(src.getRevisionTimestamp());
			tgt.setRevisionSequence(src.getRevisionSequence());
			tgt.setCreationTimestamp(src.getCreationTimestamp());
			tgt.setIsList(false);
			tgt.setTitle(src.getTitle());
			tgt.setFirstname(src.getFirstName());
			tgt.setLastname(src.getLastName());
			tgt.setDisplayName(src.getDisplayName());
			tgt.setNickname(src.getNickname());
			tgt.setGender(EnumUtils.toSerializedName(src.getGender()));
			tgt.setWorkMobile(src.getMobile());
			tgt.setWorkPager(src.getPager1());
			tgt.setHomePager(src.getPager2());
			tgt.setWorkEmail(src.getEmail1());
			tgt.setHomeEmail(src.getEmail2());
			tgt.setOtherEmail(src.getEmail3());
			tgt.setWorkIm(src.getInstantMsg1());
			tgt.setHomeIm(src.getInstantMsg2());
			tgt.setOtherIm(src.getInstantMsg3());
			tgt.setWorkAddress(src.getWorkAddress());
			tgt.setWorkPostalcode(src.getWorkPostalCode());
			tgt.setWorkCity(src.getWorkCity());
			tgt.setWorkState(src.getWorkState());
			tgt.setWorkCountry(src.getWorkCountry());
			tgt.setWorkTelephone(src.getWorkTelephone1());
			tgt.setWorkTelephone2(src.getWorkTelephone2());
			tgt.setWorkFax(src.getWorkFax());
			tgt.setHomeAddress(src.getHomeAddress());
			tgt.setHomePostalcode(src.getHomePostalCode());
			tgt.setHomeCity(src.getHomeCity());
			tgt.setHomeState(src.getHomeState());
			tgt.setHomeCountry(src.getHomeCountry());
			tgt.setHomeTelephone(src.getHomeTelephone1());
			tgt.setHomeTelephone2(src.getHomeTelephone2());
			tgt.setHomeFax(src.getHomeFax());
			tgt.setOtherAddress(src.getOtherAddress());
			tgt.setOtherPostalcode(src.getOtherPostalCode());
			tgt.setOtherCity(src.getOtherCity());
			tgt.setOtherState(src.getOtherState());
			tgt.setOtherCountry(src.getOtherCountry());
			tgt.setFunction(src.getFunction());
			tgt.setDepartment(src.getDepartment());
			tgt.setManager(src.getManager());
			tgt.setAssistant(src.getAssistant());
			tgt.setAssistantTelephone(src.getAssistantTelephone());
			tgt.setPartner(src.getPartner());
			tgt.setBirthday(src.getBirthday());
			tgt.setAnniversary(src.getAnniversary());
			tgt.setUrl(src.getUrl());
			tgt.setNotes(src.getNotes());
			tgt.setHref(src.getHref());
			tgt.setEtag(src.getEtag());
		}
		return tgt;
	}
	
	static List<ContactAttachment> createContactAttachmentList(List<OContactAttachment> items) {
		ArrayList<ContactAttachment> list = new ArrayList<>(items.size());
		for (OContactAttachment item : items) {
			list.add(createContactAttachment(item));
		}
		return list;
	}
	
	static ContactAttachment createContactAttachment(OContactAttachment src) {
		if (src == null) return null;
		return fillContactAttachment(new ContactAttachment(), src);
	}
	
	static <T extends ContactAttachment> T fillContactAttachment(T tgt, OContactAttachment src) {
		if ((tgt != null) && (src != null)) {
			tgt.setAttachmentId(src.getContactAttachmentId());
			tgt.setRevisionTimestamp(src.getRevisionTimestamp());
			tgt.setRevisionSequence(src.getRevisionSequence());
			tgt.setFilename(src.getFilename());
			tgt.setSize(src.getSize());
			tgt.setMediaType(src.getMediaType());
		}
		return tgt;
	}
	
	static Map<String, CustomFieldValue> createCustomValuesMap(List<OContactCustomValue> items) {
		LinkedHashMap<String, CustomFieldValue> map = new LinkedHashMap<>(items.size());
		for (OContactCustomValue item : items) {
			map.put(item.getCustomFieldId(), createCustomValue(item));
		}
		return map;
	}
	
	static CustomFieldValue createCustomValue(OContactCustomValue src) {
		if (src == null) return null;
		return fillCustomFieldValue(new CustomFieldValue(), src);
	}
	
	static <T extends CustomFieldValue> T fillCustomFieldValue(T tgt, OContactCustomValue src) {
		if ((tgt != null) && (src != null)) {
			tgt.setFieldId(src.getCustomFieldId());
			tgt.setStringValue(src.getStringValue());
			tgt.setNumberValue(src.getNumberValue());
			tgt.setBooleanValue(src.getBooleanValue());
			tgt.setDateValue(src.getDateValue());
			tgt.setTextValue(src.getTextValue());
		}
		return tgt;
	}
	
	static <T extends OContactCustomValue> T fillOContactCustomValue(T tgt, CustomFieldValue src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCustomFieldId(src.getFieldId());
			tgt.setStringValue(src.getStringValue());
			tgt.setNumberValue(src.getNumberValue());
			tgt.setBooleanValue(src.getBooleanValue());
			tgt.setDateValue(src.getDateValue());
			tgt.setTextValue(src.getTextValue());
		}
		return tgt;
	}
	
	static <T extends OListRecipient> T fillOListRecipient(T tgt, ContactListRecipient src) {
		if ((tgt != null) && (src != null)) {
			tgt.setListRecipientId(src.getListRecipientId());
			tgt.setRecipient(src.getRecipient());
			tgt.setRecipientType(src.getRecipientType());
			tgt.setRecipientContactId(src.getRecipientContactId());
		}
		return tgt;
	}
	
	static <T extends OContactAttachment> T fillOContactAttachment(T tgt, ContactAttachment src) {
		if ((tgt != null) && (src != null)) {
			tgt.setContactAttachmentId(src.getAttachmentId());
			tgt.setRevisionTimestamp(src.getRevisionTimestamp());
			tgt.setRevisionSequence(src.getRevisionSequence());
			tgt.setFilename(src.getFilename());
			tgt.setSize(src.getSize());
			tgt.setMediaType(src.getMediaType());
		}
		return tgt;
	}
}
