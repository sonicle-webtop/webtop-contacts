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
import com.sonicle.webtop.contacts.bol.OCategory;
import com.sonicle.webtop.contacts.bol.OCategoryPropSet;
import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.OContactAttachment;
import com.sonicle.webtop.contacts.bol.OContactPicture;
import com.sonicle.webtop.contacts.bol.OListRecipient;
import com.sonicle.webtop.contacts.bol.VContactCard;
import com.sonicle.webtop.contacts.bol.VContactCompany;
import com.sonicle.webtop.contacts.bol.VListRecipient;
import com.sonicle.webtop.contacts.bol.VContactLookup;
import com.sonicle.webtop.contacts.model.BaseContact;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactAttachment;
import com.sonicle.webtop.contacts.model.ContactCard;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactItem;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.model.ContactsList;
import com.sonicle.webtop.contacts.model.ContactsListRecipient;
import com.sonicle.webtop.core.app.WT;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author malbinola
 */
public class ManagerUtils {
	
	static String getProductName() {
		return WT.getPlatformName() + " Contacts";
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
			tgt.setIsDefault(src.getIsDefault());
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
			tgt.setIsDefault(src.getIsDefault());
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
	
	
	
	static <T extends ContactCard> T fillContactCard(T tgt, VContactCard src) {
		if ((tgt != null) && (src != null)) {
			tgt.setContactId(src.getContactId());
			tgt.setCategoryId(src.getCategoryId());
			tgt.setRevisionStatus(EnumUtils.forSerializedName(src.getRevisionStatus(), Contact.RevisionStatus.class));
			tgt.setRevisionTimestamp(src.getRevisionTimestamp());
			tgt.setPublicUid(src.getPublicUid());
			tgt.setHref(src.getHref());
		}
		return tgt;
	}
	
	
	
	
	static ContactsList createContactsList(OContact ocontlst, List<VListRecipient> olrecs) {
		if (ocontlst == null) return null;
		ContactsList item = new ContactsList();
		item.setContactId(ocontlst.getContactId());
		item.setCategoryId(ocontlst.getCategoryId());
		item.setName(ocontlst.getLastname());
		for (OListRecipient olrec : olrecs) {
			item.addRecipient(fillContactsListRecipient(new ContactsListRecipient(), olrec));
		}
		return item;
	}
	
	static <T extends ContactsListRecipient> T fillContactsListRecipient(T tgt, OListRecipient src) {
		if ((tgt != null) && (src != null)) {
			tgt.setListRecipientId(src.getListRecipientId());
			tgt.setRecipient(src.getRecipient());
			tgt.setRecipientContactId(src.getRecipientContactId());
			tgt.setRecipientType(src.getRecipientType());
		}
		return tgt;
	}
	
	static <T extends ContactItem, S extends VContactLookup> T fillContactLookup(T tgt, S src) {
		if ((tgt != null) && (src != null)) {
			fillBaseContact(tgt, src);
			tgt.setIsList(src.getIsList());
			tgt.setCompany(src.getCompanyDescription());
			tgt.setFunction(src.getFunction());
			tgt.setWorkCity(src.getWorkCity());
			tgt.setWorkTelephone(src.getWorkTelephone());
			tgt.setWorkMobile(src.getWorkMobile());
			tgt.setWorkEmail(src.getWorkEmail());
			tgt.setHomeTelephone(src.getHomeTelephone());
			tgt.setHomeEmail(src.getHomeEmail());
			tgt.setCategoryName(src.getCategoryName());
			tgt.setCategoryDomainId(src.getCategoryDomainId());
			tgt.setCategoryUserId(src.getCategoryUserId());
			tgt.setHasPicture(src.getHasPicture());
		}
		return tgt;
	}
	
	static <T extends BaseContact, S extends OContact> T fillBaseContact(T tgt, S src) {
		if ((tgt != null) && (src != null)) {
			tgt.setContactId(src.getContactId());
			tgt.setCategoryId(src.getCategoryId());
			tgt.setRevisionStatus(EnumUtils.forSerializedName(src.getRevisionStatus(), BaseContact.RevisionStatus.class));
			tgt.setPublicUid(src.getPublicUid());
			tgt.setTitle(src.getTitle());
			tgt.setFirstName(src.getFirstname());
			tgt.setLastName(src.getLastname());
			tgt.setNickname(src.getNickname());
		}
		return tgt;
	}
	
	static <T extends ContactCompany, S extends VContactCompany> T fillContactCompany(T tgt, S src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCompanyId(src.getCompanyId());
			tgt.setCompanyDescription(src.getCompanyDescription());
		}
		return tgt;
	}
	
	static Contact createContact(OContact src) {
		if (src == null) return null;
		return fillContact(new Contact(), src);
	}
	
	static <T extends Contact> T fillContact(T tgt, OContact src) {
		if ((tgt != null) && (src != null)) {
			tgt.setContactId(src.getContactId());
			tgt.setCategoryId(src.getCategoryId());
			tgt.setRevisionStatus(EnumUtils.forSerializedName(src.getRevisionStatus(), Contact.RevisionStatus.class));
			tgt.setPublicUid(src.getPublicUid());
			tgt.setTitle(src.getTitle());
			tgt.setFirstName(src.getFirstname());
			tgt.setLastName(src.getLastname());
			tgt.setNickname(src.getNickname());
			tgt.setGender(EnumUtils.forSerializedName(src.getGender(), Contact.Gender.class));
			tgt.setWorkAddress(src.getWorkAddress());
			tgt.setWorkPostalCode(src.getWorkPostalcode());
			tgt.setWorkCity(src.getWorkCity());
			tgt.setWorkState(src.getWorkState());
			tgt.setWorkCountry(src.getWorkCountry());
			tgt.setWorkTelephone(src.getWorkTelephone());
			tgt.setWorkTelephone2(src.getWorkTelephone2());
			tgt.setWorkMobile(src.getWorkMobile());
			tgt.setWorkFax(src.getWorkFax());
			tgt.setWorkPager(src.getWorkPager());
			tgt.setWorkEmail(src.getWorkEmail());
			tgt.setWorkInstantMsg(src.getWorkIm());
			tgt.setHomeAddress(src.getHomeAddress());
			tgt.setHomePostalCode(src.getHomePostalcode());
			tgt.setHomeCity(src.getHomeCity());
			tgt.setHomeState(src.getHomeState());
			tgt.setHomeCountry(src.getHomeCountry());
			tgt.setHomeTelephone(src.getHomeTelephone());
			tgt.setHomeTelephone2(src.getHomeTelephone2());
			tgt.setHomeFax(src.getHomeFax());
			tgt.setHomePager(src.getHomePager());
			tgt.setHomeEmail(src.getHomeEmail());
			tgt.setHomeInstantMsg(src.getHomeIm());
			tgt.setOtherAddress(src.getOtherAddress());
			tgt.setOtherPostalCode(src.getOtherPostalcode());
			tgt.setOtherCity(src.getOtherCity());
			tgt.setOtherState(src.getOtherState());
			tgt.setOtherCountry(src.getOtherCountry());
			tgt.setOtherEmail(src.getOtherEmail());
			tgt.setOtherInstantMsg(src.getOtherIm());
			tgt.setCompany(src.getCompany());
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
	
	static ContactPicture createContactPicture(OContactPicture src) {
		if (src == null) return null;
		return fillContactPicture(new ContactPicture(), src);
	}
	
	static <T extends ContactPicture> T fillContactPicture(T tgt, OContactPicture src) {
		if ((tgt != null) && (src != null)) {
			tgt.setWidth(src.getWidth());
			tgt.setHeight(src.getHeight());
			tgt.setMediaType(src.getMediaType());
		}
		return tgt;
	}
	
	static OContact createOContact(Contact src) {
		if (src == null) return null;
		return fillOContact(new OContact(), src);
	}
	
	static <T extends OContact> T fillOContact(T tgt, Contact src) {
		if ((tgt != null) && (src != null)) {
			tgt.setContactId(src.getContactId());
			tgt.setCategoryId(src.getCategoryId());
			tgt.setPublicUid(src.getPublicUid());
			tgt.setRevisionStatus(EnumUtils.toSerializedName(src.getRevisionStatus()));
			tgt.setIsList(false);
			tgt.setTitle(src.getTitle());
			tgt.setFirstname(src.getFirstName());
			tgt.setLastname(src.getLastName());
			tgt.setNickname(src.getNickname());
			tgt.setGender(EnumUtils.toSerializedName(src.getGender()));
			tgt.setWorkAddress(src.getWorkAddress());
			tgt.setWorkPostalcode(src.getWorkPostalCode());
			tgt.setWorkCity(src.getWorkCity());
			tgt.setWorkState(src.getWorkState());
			tgt.setWorkCountry(src.getWorkCountry());
			tgt.setWorkTelephone(src.getWorkTelephone());
			tgt.setWorkTelephone2(src.getWorkTelephone2());
			tgt.setWorkMobile(src.getWorkMobile());
			tgt.setWorkFax(src.getWorkFax());
			tgt.setWorkPager(src.getWorkPager());
			tgt.setWorkEmail(src.getWorkEmail());
			tgt.setWorkIm(src.getWorkInstantMsg());
			tgt.setHomeAddress(src.getHomeAddress());
			tgt.setHomePostalcode(src.getHomePostalCode());
			tgt.setHomeCity(src.getHomeCity());
			tgt.setHomeState(src.getHomeState());
			tgt.setHomeCountry(src.getHomeCountry());
			tgt.setHomeTelephone(src.getHomeTelephone());
			tgt.setHomeTelephone2(src.getHomeTelephone2());
			tgt.setHomeFax(src.getHomeFax());
			tgt.setHomePager(src.getHomePager());
			tgt.setHomeEmail(src.getHomeEmail());
			tgt.setHomeIm(src.getHomeInstantMsg());
			tgt.setOtherAddress(src.getOtherAddress());
			tgt.setOtherPostalcode(src.getOtherPostalCode());
			tgt.setOtherCity(src.getOtherCity());
			tgt.setOtherState(src.getOtherState());
			tgt.setOtherCountry(src.getOtherCountry());
			tgt.setOtherEmail(src.getOtherEmail());
			tgt.setOtherIm(src.getOtherInstantMsg());
			tgt.setCompany(src.getCompany());
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
	
	static OContactAttachment createOTaskAttachment(ContactAttachment src) {
		if (src == null) return null;
		return fillOContactAttachment(new OContactAttachment(), src);
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
