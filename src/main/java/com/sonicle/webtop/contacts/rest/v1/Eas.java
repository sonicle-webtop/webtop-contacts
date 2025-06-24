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

import com.sonicle.commons.flags.BitFlags;
import com.sonicle.commons.time.JodaTimeUtils;
import com.sonicle.webtop.contacts.ContactObjectOutputType;
import com.sonicle.webtop.contacts.ContactsManager;
import com.sonicle.webtop.contacts.ContactsServiceSettings;
import com.sonicle.webtop.contacts.IContactsManager;
import com.sonicle.webtop.contacts.IContactsManager.ContactUpdateOption;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryFSFolder;
import com.sonicle.webtop.contacts.model.CategoryFSOrigin;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactObject;
import com.sonicle.webtop.contacts.model.ContactObjectWithBean;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.contacts.model.ContactPictureWithBytes;
import com.sonicle.webtop.contacts.model.ContactPictureWithSize;
import com.sonicle.webtop.contacts.swagger.v1.api.EasApi;
import com.sonicle.webtop.contacts.swagger.v1.model.ApiError;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncContact;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncContactStat;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncContactUpdate;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncFolder;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.model.FolderShare;
import com.sonicle.webtop.core.app.sdk.WTNotFoundException;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import ezvcard.util.DataUri;
import java.nio.charset.StandardCharsets;
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
	private static final DateTimeFormatter ETAG_FMT = JodaTimeUtils.createFormatter("yyyyMMddHHmmssSSS", DateTimeZone.UTC);
	private static final DateTimeFormatter ISO_DATE_FMT = JodaTimeUtils.createFormatter("yyyyMMdd", DateTimeZone.UTC);
	
	@Override
	public Response getFolders() {
		UserProfileId currentProfileId = RunContext.getRunProfileId();
		ContactsManager manager = getManager();
		List<SyncFolder> items = new ArrayList<>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getFolders()", currentProfileId);
		}
		
		try {
			Integer defltCategoryId = manager.getDefaultCategoryId();
			Map<Integer, Category> cats = manager.listCategories();
			Map<Integer, DateTime> revisions = manager.getCategoriesItemsLastRevision(cats.keySet());
			for (Category category : cats.values()) {
				if (category.isProviderRemote()) continue;
				if (Category.Sync.OFF.equals(category.getSync())) continue;
				
				final boolean isDefault = category.getCategoryId().equals(defltCategoryId);
				final FolderShare.Permissions permissions = Category.Sync.READ.equals(category.getSync()) ? FolderShare.Permissions.fullFolderOnly() : FolderShare.Permissions.full();
				items.add(createSyncFolder(currentProfileId, category, revisions.get(category.getCategoryId()), permissions, isDefault));
			}
			
			for (CategoryFSOrigin origin : manager.listIncomingCategoryOrigins().values()) {
				Map<Integer, CategoryFSFolder> folders = manager.listIncomingCategoryFolders(origin);
				Map<Integer, CategoryPropSet> folderProps = manager.getCategoriesCustomProps(folders.keySet());
				revisions = manager.getCategoriesItemsLastRevision(folders.keySet());
				for (CategoryFSFolder folder : folders.values()) {
					Category category = folder.getCategory();
					if (category.isProviderRemote()) continue;
					CategoryPropSet props = folderProps.get(category.getCategoryId());
					if (Category.Sync.OFF.equals(props.getSyncOrDefault(Category.Sync.OFF))) continue;
					
					final boolean isDefault = category.getCategoryId().equals(defltCategoryId);
					final FolderShare.Permissions permissions = Category.Sync.READ.equals(props.getSync()) ? FolderShare.Permissions.withFolderPermissionsOnly(folder.getPermissions()) : folder.getPermissions();
					items.add(createSyncFolder(currentProfileId, category, revisions.get(category.getCategoryId()), permissions, isDefault));
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
			
			ContactObjectWithBean cobj = (ContactObjectWithBean)manager.getContactObject(String.valueOf(id), ContactObjectOutputType.BEAN);
			if (cobj != null) {
				return respOk(createSyncContact(cobj));
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
			boolean photoUpdateEnabled = getServiceSettings().getEasContactPhotoUpdateEnabled();
			ContactEx newContact = mergeContact(new ContactEx(), body);
			newContact.setCategoryId(folderId);
			if (photoUpdateEnabled) mergeContactPicture(newContact, body);
			
			Contact contact = manager.addContact(newContact);
			ContactObject cobj = manager.getContactObject(contact.getContactId(), ContactObjectOutputType.STAT);
			if (cobj == null) return respErrorNotFound();
			
			return respOkCreated(createSyncContactStat(cobj));
			
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
			boolean photoUpdateEnabled = getServiceSettings().getEasContactPhotoUpdateEnabled();
			Contact contact = manager.getContact(String.valueOf(id), BitFlags.with(IContactsManager.ContactGetOption.PICTURE));
			if (contact == null) return respErrorNotFound();
			
			BitFlags<ContactUpdateOption> options = BitFlags.noneOf(ContactUpdateOption.class);
			mergeContact(contact, body);
			if (photoUpdateEnabled && mergeContactPicture(contact, body) == true) {
				options.set(ContactUpdateOption.PICTURE);
			}
			manager.updateContact(String.valueOf(id), contact, options);
			
			ContactObject card = manager.getContactObject(String.valueOf(id), ContactObjectOutputType.STAT);
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
			manager.deleteContact(String.valueOf(id));
			return respOkNoContent();
			
		} catch (WTNotFoundException ex) {
			return respErrorNotFound();
		} catch (Throwable t) {
			logger.error("[{}] deleteMessage({}, {})", RunContext.getRunProfileId(), folderId, id, t);
			return respError(t);
		}
	}
	
	private SyncFolder createSyncFolder(UserProfileId currentProfileId, Category cat, DateTime lastRevisionTimestamp, FolderShare.Permissions permissions, boolean isDefault) {
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
			.deflt(isDefault)
			.foAcl(permissions.getFolderPermissions().toString())
			.elAcl(permissions.getItemsPermissions().toString())
			.ownerId(cat.getProfileId().toString());
	}
	
	private SyncContactStat createSyncContactStat(ContactObject card) {
		return new SyncContactStat()
			.id(Integer.valueOf(card.getContactId()))
			.etag(buildEtag(card.getRevisionTimestamp()));
	}
	
	private SyncContact createSyncContact(ContactObjectWithBean card) {
		ContactEx cont = card.getContact();
		
		String picture = null;
		if (cont.hasPicture()) {
			ContactPictureWithBytes pic = (ContactPictureWithBytes)cont.getPicture();
			picture = new DataUri(pic.getMediaType(), pic.getBytes()).toString(StandardCharsets.UTF_8.name());
		}
		
		return new SyncContact()
			.id(Integer.valueOf(card.getContactId()))
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
			.birthday(JodaTimeUtils.print(ISO_DATE_FMT, cont.getBirthday()))
			.anniversary(JodaTimeUtils.print(ISO_DATE_FMT, cont.getAnniversary()))
			.url(cont.getUrl())
			.notes(cont.getNotes())
			.picture(picture);
	}
	
	private boolean mergeContactPicture(ContactEx orig, SyncContactUpdate src) {
		if (!StringUtils.isBlank(src.getPicture())) {
			DataUri dataUri = parseQuietly(src.getPicture());
			if (dataUri == null) return false;
			if (!StringUtils.startsWith(dataUri.getContentType(), "image/")) return false;
			if (orig.hasPicture() && (((ContactPictureWithSize)orig.getPicture()).getSize() == dataUri.getData().length)) return false;
			
			ContactPictureWithBytes newPicture = new ContactPictureWithBytes(dataUri.getData());
			newPicture.setMediaType(dataUri.getContentType());
			orig.setPicture(newPicture);
		} else {
			orig.setPicture(null);
		}
		return true;
	}
	
	private ContactEx mergeContact(ContactEx orig, SyncContactUpdate src) {
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
		orig.setBirthday(JodaTimeUtils.parseLocalDate(ISO_DATE_FMT, src.getBirthday()));
		orig.setAnniversary(JodaTimeUtils.parseLocalDate(ISO_DATE_FMT, src.getAnniversary()));
		orig.setUrl(src.getUrl());
		orig.setNotes(src.getNotes());
		
		if (!orig.hasCompany() || (!StringUtils.equals(src.getCompanyId(), orig.getCompany().getCompanyId()) && !StringUtils.equals(src.getCompanyName(), orig.getCompany().getValue()))) {
			if (!StringUtils.isBlank(src.getCompanyId()) || !StringUtils.isBlank(src.getCompanyName())) {
				orig.setCompany(new ContactCompany(src.getCompanyName(), src.getCompanyId()));
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
	
	private DataUri parseQuietly(String uri) {
		try {
			return DataUri.parse(uri);
		} catch (IllegalArgumentException ex) {
			if (logger.isTraceEnabled()) logger.trace("Unable to parse data-uri: \"{}\"", uri);
			return null;
		}
	}
	
	private ContactsServiceSettings getServiceSettings() {
		return new ContactsServiceSettings(SERVICE_ID, RunContext.getRunProfileId().getDomainId());
	}
	
	private ContactsManager getManager() {
		return getManager(RunContext.getRunProfileId());
	}
	
	private ContactsManager getManager(UserProfileId targetProfileId) {
		ContactsManager manager = (ContactsManager)WT.getServiceManager(SERVICE_ID, targetProfileId);
		manager.setSoftwareName("rest-eas");
		return manager;
	}
	
	@Override
	protected Object createErrorEntity(Response.Status status, String message) {
		return new ApiError()
			.code(status.getStatusCode())
			.description(message);
	}
}
