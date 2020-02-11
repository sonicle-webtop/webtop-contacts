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
package com.sonicle.webtop.contacts.rest.v1;

import com.sonicle.commons.time.DateTimeUtils;
import com.sonicle.webtop.contacts.ContactObjectOutputType;
import com.sonicle.webtop.contacts.ContactsManager;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactObject;
import com.sonicle.webtop.contacts.model.ContactObjectWithBean;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ShareFolderCategory;
import com.sonicle.webtop.contacts.model.ShareRootCategory;
import com.sonicle.webtop.contacts.swagger.v1.api.EasApi;
import com.sonicle.webtop.contacts.swagger.v1.model.ApiError;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncContact;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncContactStat;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncContactUpdate;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncFolder;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.sdk.WTNotFoundException;
import com.sonicle.webtop.core.model.SharePerms;
import com.sonicle.webtop.core.model.SharePermsElements;
import com.sonicle.webtop.core.model.SharePermsFolder;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbinola
 */
public class Eas extends EasApi {
	private static final Logger logger = LoggerFactory.getLogger(Eas.class);
	private static final String DEFAULT_ETAG = "19700101000000000";
	private static final DateTimeFormatter ETAG_FMT = DateTimeUtils.createFormatter("yyyyMMddHHmmssSSS", DateTimeZone.UTC);
	private static final DateTimeFormatter ISO_DATE_FMT = DateTimeUtils.createFormatter("yyyyMMdd", DateTimeZone.UTC);
	
	@Override
	public Response getFolders() {
		UserProfileId currentProfileId = RunContext.getRunProfileId();
		ContactsManager manager = getManager();
		List<SyncFolder> items = new ArrayList<>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getFolders()", currentProfileId);
		}
		
		try {
			Map<Integer, Category> cats = manager.listCategories();
			Map<Integer, DateTime> revisions = manager.getCategoriesLastRevision(cats.keySet());
			for (Category cat : cats.values()) {
				if (cat.isProviderRemote()) continue;
				if (Category.Sync.OFF.equals(cat.getSync())) continue;
				
				items.add(createSyncFolder(currentProfileId, cat, revisions.get(cat.getCategoryId()), null, ShareFolderCategory.realElementsPerms(cat.getSync())));
			}
			
			List<ShareRootCategory> shareRoots = manager.listIncomingCategoryRoots();
			for (ShareRootCategory shareRoot : shareRoots) {
				Map<Integer, ShareFolderCategory> folders = manager.listIncomingCategoryFolders(shareRoot.getShareId());
				revisions = manager.getCategoriesLastRevision(folders.keySet());
				Map<Integer, CategoryPropSet> props = manager.getCategoryCustomProps(folders.keySet());
				
				for (ShareFolderCategory folder : folders.values()) {
					Category cat = folder.getCategory();
					if (cat.isProviderRemote()) continue;
					CategoryPropSet catProps = props.get(cat.getCategoryId());
					if (Category.Sync.OFF.equals(catProps.getSyncOrDefault(Category.Sync.OFF))) continue;
					
					items.add(createSyncFolder(currentProfileId, cat, revisions.get(cat.getCategoryId()), folder.getPerms(), folder.getRealElementsPerms(catProps.getSync())));
				}
			}
			
			return respOk(items);
			
		} catch(Throwable t) {
			logger.error("[{}] getFolders()", currentProfileId, t);
			return respError(t);
		}
	}

	@Override
	public Response getMessagesStats(Integer folderId) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getMessagesStats({})", RunContext.getRunProfileId(), folderId);
		}
		
		try {
			Category cat = manager.getCategory(folderId);
			if (cat == null) return respErrorBadRequest();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			List<SyncContactStat> items = new ArrayList<>();
			List<ContactObject> cards = manager.listContactObjects(folderId, ContactObjectOutputType.STAT);
			for (ContactObject card : cards) {
				items.add(createSyncContactStat(card));
			}
			return respOk(items);
			
		} catch(Throwable t) {
			logger.error("[{}] getMessagesStats({})", RunContext.getRunProfileId(), folderId, t);
			return respError(t);
		}
	}

	@Override
	public Response getMessage(Integer folderId, Integer id, Boolean picture) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getMessage({}, {})", RunContext.getRunProfileId(), folderId, id);
		}
		
		try {
			Category cat = manager.getCategory(folderId);
			if (cat == null) return respErrorBadRequest();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			ContactObjectWithBean card = (ContactObjectWithBean)manager.getContactObject(id, ContactObjectOutputType.BEAN);
			if (card != null) {
				return respOk(createSyncContact(card));
			} else {
				return respErrorNotFound();
			}
			
		} catch(Throwable t) {
			logger.error("[{}] getMessage({}, {})", RunContext.getRunProfileId(), folderId, id, t);
			return respError(t);
		}
	}

	@Override
	public Response addMessage(Integer folderId, SyncContactUpdate body) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] addMessage({}, ...)", RunContext.getRunProfileId(), folderId);
			logger.debug("{}", body);
		}
		
		try {
			Contact newContact = mergeContact(new Contact(), null, body);
			newContact.setCategoryId(folderId);
			
			Contact contact = manager.addContact(newContact);
			ContactObject card = manager.getContactObject(contact.getContactId(), ContactObjectOutputType.STAT);
			if (card == null) return respErrorNotFound();
			
			return respOkCreated(createSyncContactStat(card));
			
		} catch(Throwable t) {
			logger.error("[{}] addMessage({}, ...)", RunContext.getRunProfileId(), folderId, t);
			return respError(t);
		}
	}

	@Override
	public Response updateMessage(Integer folderId, Integer id, SyncContactUpdate body, Boolean picture) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] updateMessage({}, {}, ...)", RunContext.getRunProfileId(), folderId, id);
			logger.debug("{}", body);
		}
		
		try {
			Contact contact = manager.getContact(id, false, false, false);
			if (contact == null) return respErrorNotFound();
			ContactCompany contactCompany = null;
			if (contact.hasCompany()) contactCompany = manager.getContactCompany(id);
			
			mergeContact(contact, contactCompany, body);
			manager.updateContact(contact, false, false, false);
			
			ContactObject card = manager.getContactObject(id, ContactObjectOutputType.STAT);
			if (card == null) return respErrorNotFound();
			
			return respOk(createSyncContactStat(card));
			
		} catch(Throwable t) {
			logger.error("[{}] updateMessage({}, {}, ...)", RunContext.getRunProfileId(), folderId, id, t);
			return respError(t);
		}
	}

	@Override
	public Response deleteMessage(Integer folderId, Integer id) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] deleteMessage({}, {})", RunContext.getRunProfileId(), folderId, id);
		}
		
		try {
			manager.deleteContact(id);
			return respOkNoContent();
			
		} catch(WTNotFoundException ex) {
			return respErrorNotFound();
		} catch(Throwable t) {
			logger.error("[{}] deleteMessage({}, {})", RunContext.getRunProfileId(), folderId, id, t);
			return respError(t);
		}
	}
	
	private SyncFolder createSyncFolder(UserProfileId currentProfileId, Category cat, DateTime lastRevisionTimestamp, SharePerms folderPerms, SharePerms elementPerms) {
		String displayName = cat.getName();
		if (!currentProfileId.equals(cat.getProfileId())) {
			UserProfile.Data owud = WT.getUserData(cat.getProfileId());
			//String apn = LangUtils.abbreviatePersonalName(false, owud.getDisplayName());
			displayName = "[" + owud.getDisplayName() + "] " + displayName;
		}
		//String ownerUsername = owud.getProfileEmailAddress();
		
		return new SyncFolder()
				.id(cat.getCategoryId())
				.displayName(displayName)
				.etag(buildEtag(lastRevisionTimestamp))
				.deflt(cat.getIsDefault())
				.foAcl((folderPerms == null) ? SharePermsFolder.full().toString() : folderPerms.toString())
				.elAcl((elementPerms == null) ? SharePermsElements.full().toString() : elementPerms.toString())
				.ownerId(cat.getProfileId().toString());
	}
	
	private SyncContactStat createSyncContactStat(ContactObject card) {
		return new SyncContactStat()
				.id(card.getContactId())
				.etag(buildEtag(card.getRevisionTimestamp()));
	}
	
	private SyncContact createSyncContact(ContactObjectWithBean card) {
		Contact cont = card.getContact();
		return new SyncContact()
				.id(card.getContactId())
				.etag(buildEtag(card.getRevisionTimestamp()))
				.title(cont.getTitle())
				.firstName(cont.getFirstName())
				.lastName(cont.getLastName())
				.nickname(cont.getNickname())
				.mobile(cont.getMobile())
				.pager1(cont.getPager1())
				.pager2(cont.getPager2())
				.email1(cont.getEmail1())
				.email2(cont.getEmail2())
				.email3(cont.getEmail3())
				.im1(cont.getInstantMsg1())
				.im2(cont.getInstantMsg2())
				.im3(cont.getInstantMsg3())
				.workAddress(cont.getWorkAddress())
				.workPostalCode(cont.getWorkPostalCode())
				.workCity(cont.getWorkCity())
				.workState(cont.getWorkState())
				.workCountry(cont.getWorkCountry())
				.workTelephone1(cont.getWorkTelephone1())
				.workTelephone2(cont.getWorkTelephone2())
				.workFax(cont.getWorkFax())
				.homeAddress(cont.getHomeAddress())
				.homePostalCode(cont.getHomePostalCode())
				.homeCity(cont.getHomeCity())
				.homeState(cont.getHomeState())
				.homeCountry(cont.getHomeCountry())
				.homeTelephone1(cont.getHomeTelephone1())
				.homeTelephone2(cont.getHomeTelephone2())
				.homeFax(cont.getHomeFax())
				.otherAddress(cont.getOtherAddress())
				.otherPostalCode(cont.getOtherPostalCode())
				.otherCity(cont.getOtherCity())
				.otherState(cont.getOtherState())
				.otherCountry(cont.getOtherCountry())
				.companyId(cont.hasCompany() ? cont.getCompany().getCompanyId(): null)
				.companyName(cont.hasCompany() ? cont.getCompany().getCompanyDescription() : null)
				.function(cont.getFunction())
				.department(cont.getDepartment())
				.assistant(cont.getAssistant())
				.assistantTelephone(cont.getAssistantTelephone())
				.manager(cont.getManager())
				.partner(cont.getPartner())
				.birthday(DateTimeUtils.print(ISO_DATE_FMT, cont.getBirthday()))
				.anniversary(DateTimeUtils.print(ISO_DATE_FMT, cont.getAnniversary()))
				.url(cont.getUrl())
				.notes(cont.getNotes());
	}
	
	private Contact mergeContact(Contact orig, ContactCompany origCompany, SyncContactUpdate src) {
		orig.setTitle(src.getTitle());
		orig.setFirstName(src.getFirstName());
		orig.setLastName(src.getLastName());
		orig.setNickname(src.getNickname());
		//tgt.setGender(EnumUtils.forSerializedName(src.getGender(), Contact.Gender.class));
		orig.setMobile(src.getMobile());
		orig.setPager1(src.getPager1());
		orig.setPager2(src.getPager2());
		orig.setEmail1(src.getEmail1());
		orig.setEmail2(src.getEmail2());
		orig.setEmail3(src.getEmail3());
		orig.setInstantMsg1(src.getIm1());
		orig.setInstantMsg2(src.getIm2());
		orig.setInstantMsg3(src.getIm3());
		orig.setWorkAddress(src.getWorkAddress());
		orig.setWorkPostalCode(src.getWorkPostalCode());
		orig.setWorkCity(src.getWorkCity());
		orig.setWorkState(src.getWorkState());
		orig.setWorkCountry(src.getWorkCountry());
		orig.setWorkTelephone1(src.getWorkTelephone1());
		orig.setWorkTelephone2(src.getWorkTelephone2());
		orig.setWorkFax(src.getWorkFax());
		orig.setHomeAddress(src.getHomeAddress());
		orig.setHomePostalCode(src.getHomePostalCode());
		orig.setHomeCity(src.getHomeCity());
		orig.setHomeState(src.getHomeState());
		orig.setHomeCountry(src.getHomeCountry());
		orig.setHomeTelephone1(src.getHomeTelephone1());
		orig.setHomeTelephone2(src.getHomeTelephone2());
		orig.setHomeFax(src.getHomeFax());
		orig.setOtherAddress(src.getOtherAddress());
		orig.setOtherPostalCode(src.getOtherPostalCode());
		orig.setOtherCity(src.getOtherCity());
		orig.setOtherState(src.getOtherState());
		orig.setOtherCountry(src.getOtherCountry());
		orig.setFunction(src.getFunction());
		orig.setDepartment(src.getDepartment());
		orig.setManager(src.getManager());
		orig.setAssistant(src.getAssistant());
		orig.setAssistantTelephone(src.getAssistantTelephone());
		orig.setPartner(src.getPartner());
		orig.setBirthday(DateTimeUtils.parseLocalDate(ISO_DATE_FMT, src.getBirthday()));
		orig.setAnniversary(DateTimeUtils.parseLocalDate(ISO_DATE_FMT, src.getAnniversary()));
		orig.setUrl(src.getUrl());
		orig.setNotes(src.getNotes());
		
		if (origCompany == null) {
			if (!StringUtils.isBlank(src.getCompanyId()) || !StringUtils.isBlank(src.getCompanyName())) {
				orig.setCompany(new ContactCompany(src.getCompanyName(), src.getCompanyId()));
			}
		} else {
			if (StringUtils.equals(src.getCompanyId(), origCompany.getCompanyId()) || StringUtils.equals(src.getCompanyName(), origCompany.getValue())) {
				orig.setCompany(origCompany);
			} else {
				if (!StringUtils.isBlank(src.getCompanyId()) || !StringUtils.isBlank(src.getCompanyName())) {
					orig.setCompany(new ContactCompany(src.getCompanyName(), src.getCompanyId()));
				}
			}
		}
		
		return orig;
	}
	
	private String buildEtag(DateTime revisionTimestamp) {
		if (revisionTimestamp != null) {
			return ETAG_FMT.print(revisionTimestamp);
		} else {
			return DEFAULT_ETAG;
		}
	}
	
	private ContactsManager getManager() {
		return getManager(RunContext.getRunProfileId());
	}
	
	private ContactsManager getManager(UserProfileId targetProfileId) {
		return (ContactsManager)WT.getServiceManager(SERVICE_ID, targetProfileId);
	}
	
	@Override
	protected Object createErrorEntity(Response.Status status, String message) {
		return new ApiError()
				.code(status.getStatusCode())
				.description(message);
	}
}
