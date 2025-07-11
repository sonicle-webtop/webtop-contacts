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

import com.sonicle.commons.time.JodaTimeUtils;
import com.sonicle.webtop.contacts.ContactObjectOutputType;
import com.sonicle.webtop.contacts.ContactsManager;
import com.sonicle.webtop.contacts.ContactsServiceSettings;
import com.sonicle.webtop.contacts.ContactsUtils;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryFSFolder;
import com.sonicle.webtop.contacts.model.CategoryFSOrigin;
import com.sonicle.webtop.contacts.model.ContactObject;
import com.sonicle.webtop.contacts.model.ContactObjectWithBean;
import com.sonicle.webtop.contacts.model.ContactObjectWithVCard;
import com.sonicle.webtop.contacts.swagger.v1.api.CarddavApi;
import com.sonicle.webtop.contacts.swagger.v1.model.AddressBook;
import com.sonicle.webtop.contacts.swagger.v1.model.AddressBookNew;
import com.sonicle.webtop.contacts.swagger.v1.model.AddressBookUpdate;
import com.sonicle.webtop.contacts.swagger.v1.model.ApiError;
import com.sonicle.webtop.contacts.swagger.v1.model.Card;
import com.sonicle.webtop.contacts.swagger.v1.model.CardChanged;
import com.sonicle.webtop.contacts.swagger.v1.model.CardNew;
import com.sonicle.webtop.contacts.swagger.v1.model.CardsChanges;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.model.FolderShare;
import com.sonicle.webtop.core.app.sdk.WTNotFoundException;
import com.sonicle.webtop.core.model.ChangedItem;
import com.sonicle.webtop.core.model.Delta;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.util.VCardUtils;
import ezvcard.VCard;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class CardDav extends CarddavApi {
	private static final Logger logger = LoggerFactory.getLogger(CardDav.class);
	private static final String DEFAULT_ETAG = "19700101000000000";
	private static final DateTimeFormatter ETAG_FORMATTER = JodaTimeUtils.createFormatter("yyyyMMddHHmmssSSS", DateTimeZone.UTC);
	
	@Override
	public Response getAddressBooks() {
		UserProfileId currentProfileId = RunContext.getRunProfileId();
		ContactsManager manager = getManager();
		List<AddressBook> items = new ArrayList<>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getAddressBooks()", currentProfileId);
		}
		
		try {
			Map<Integer, Category> cats = manager.listCategories();
			Map<Integer, DateTime> revisions = manager.getCategoriesItemsLastRevision(cats.keySet());
			for (Category category : cats.values()) {
				if (category.isProviderRemote()) continue;
				items.add(createAddressBook(currentProfileId, category, revisions.get(category.getCategoryId()), FolderShare.Permissions.full()));
			}
			for (CategoryFSOrigin origin : manager.listIncomingCategoryOrigins().values()) {
				Map<Integer, CategoryFSFolder> folders = manager.listIncomingCategoryFolders(origin);
				revisions = manager.getCategoriesItemsLastRevision(folders.keySet());
				for (CategoryFSFolder folder : folders.values()) {
					Category category = folder.getCategory();
					if (category.isProviderRemote()) continue;
					items.add(createAddressBook(currentProfileId, category, revisions.get(category.getCategoryId()), folder.getPermissions()));
				}
			}
			
			return respOk(items);
			
		} catch (Throwable t) {
			logger.error("[{}] getAddressBooks()", currentProfileId, t);
			return respError(t);
		}
	}

	@Override
	public Response getAddressBook(String addressBookUid) {
		UserProfileId currentProfileId = RunContext.getRunProfileId();
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getAddressBook({})", currentProfileId, addressBookUid);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			Category category = manager.getCategory(categoryId);
			if (category == null) return respErrorNotFound();
			if (category.isProviderRemote()) return respErrorBadRequest();
			
			Map<Integer, DateTime> revisions = manager.getCategoriesItemsLastRevision(Arrays.asList(category.getCategoryId()));
			CategoryFSOrigin origin = manager.getIncomingCategoryOriginByFolderId(categoryId);
			if (origin != null) {
				Map<Integer, CategoryFSFolder> folders = manager.listIncomingCategoryFolders(origin);
				return respOk(createAddressBook(currentProfileId, category, revisions.get(category.getCategoryId()), folders.get(categoryId).getPermissions()));
			} else {
				return respOk(createAddressBook(currentProfileId, category, revisions.get(category.getCategoryId()), FolderShare.Permissions.full()));
			}
			
		} catch (Throwable t) {
			logger.error("[{}] getAddressBook({})", currentProfileId, addressBookUid, t);
			return respError(t);
		}
	}

	@Override
	public Response addAddressBook(AddressBookNew body) {
		UserProfileId currentProfileId = RunContext.getRunProfileId();
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] addAddressBook(...)", currentProfileId);
			logger.debug("{}", body);
		}
		
		try {
			Category category = new Category();
			category.setName(body.getDisplayName());
			category.setDescription(body.getDescription());
			category = manager.addCategory(category);
			// Categories are always added in currentProfile so we do not handle perms here (passing null = full rights)
			return respOkCreated(createAddressBook(currentProfileId, category, null, FolderShare.Permissions.full()));
			
		} catch (Throwable t) {
			logger.error("[{}] addAddressBook(...)", currentProfileId, t);
			return respError(t);
		}
	}

	@Override
	public Response updateAddressBook(String addressBookUid, AddressBookUpdate body) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] updateAddressBook({}, ...)", RunContext.getRunProfileId(), addressBookUid);
			logger.debug("{}", body);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorNotFound();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			if (body.getUpdatedFields().contains("displayName")) {
				cat.setName(body.getDisplayName());
			}
			if (body.getUpdatedFields().contains("description")) {
				cat.setDescription(body.getDescription());
			}
			manager.updateCategory(categoryId, cat);
			return respOk();
			
		} catch (WTNotFoundException ex) {
			return respErrorNotFound();
		} catch (Throwable t) {
			logger.error("[{}] updateAddressBook({}, ...)", RunContext.getRunProfileId(), addressBookUid, t);
			return respError(t);
		}
	}

	@Override
	public Response deleteAddressBook(String addressBookUid) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] deleteAddressBook({})", RunContext.getRunProfileId(), addressBookUid);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			if (getServiceSettings().getDavAddressbookDeleteEnabled()) {
				manager.deleteCategory(categoryId);
				return respOkNoContent();
			} else {
				return respErrorNotAllowed();
			}
			
		} catch (WTNotFoundException ex) {
			return respErrorNotFound();
		} catch (Throwable t) {
			logger.error("[{}] deleteAddressBook({})", RunContext.getRunProfileId(), addressBookUid, t);
			return respError(t);
		}
	}
	
	@Override
	public Response getCards(String addressBookUid, List<String> hrefs) {
		ContactsManager manager = getManager();
		List<Card> items = new ArrayList<>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getCards({})", RunContext.getRunProfileId(), addressBookUid);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorBadRequest();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			if ((hrefs == null) || hrefs.isEmpty()) {
				List<ContactObject> cards = manager.listContactObjects(categoryId, ContactObjectOutputType.VCARD);
				for (ContactObject card : cards) {
					items.add(createDavObject((ContactObjectWithVCard)card));
				}
				return respOk(items);
				
			} else {
				List<ContactObject> objs = manager.getContactObjects(categoryId, hrefs, ContactObjectOutputType.VCARD);
				for (ContactObject obj : objs) {
					items.add(createDavObject(obj));
				}
				return respOk(items);
			}
			
		} catch (Throwable t) {
			logger.error("[{}] getCards({})", RunContext.getRunProfileId(), addressBookUid, t);
			return respError(t);
		}
	}

	@Override
	public Response getCardsChanges(String addressBookUid, String syncToken, Integer limit) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getCardsChanges({}, {}, {})", RunContext.getRunProfileId(), addressBookUid, syncToken, limit);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorNotFound();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			Map<Integer, DateTime> revisions = manager.getCategoriesItemsLastRevision(Arrays.asList(categoryId));
			
			DateTime since = null;
			if (!StringUtils.isBlank(syncToken)) {
				since = ETAG_FORMATTER.parseDateTime(syncToken);
				if (since == null) return respErrorBadRequest();
			}
			
			Delta<ContactObject> delta = manager.listContactsDelta(categoryId, since, ContactObjectOutputType.STAT);
			return respOk(createCardsChanges(revisions.get(categoryId), delta));
			
		} catch (Throwable t) {
			logger.error("[{}] getCardsChanges({}, {}, {})", RunContext.getRunProfileId(), addressBookUid, syncToken, limit, t);
			return respError(t);
		}
	}

	@Override
	public Response getCard(String addressBookUid, String href) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getCard({}, {})", RunContext.getRunProfileId(), addressBookUid, href);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorBadRequest();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			ContactObject card = manager.getContactObject(categoryId, href, ContactObjectOutputType.VCARD);
			if (card != null) {
				return respOk(createDavObject(card));
			} else {
				return respErrorNotFound();
			}
			
		} catch (Throwable t) {
			logger.error("[{}] getCard({}, {})", RunContext.getRunProfileId(), addressBookUid, href, t);
			return respError(t);
		}
	}

	@Override
	public Response addCard(String addressBookUid, CardNew body) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] addCard({}, ...)", RunContext.getRunProfileId(), addressBookUid);
			logger.debug("{}", body);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			// Manager's call is already ro protected for remoteProviders
			VCard vCard = parseVCard(body.getVcard());
			manager.addContactObject(categoryId, body.getHref(), vCard);
			return respOk();
			
		} catch (Throwable t) {
			logger.error("[{}] addCard({}, ...)", RunContext.getRunProfileId(), addressBookUid, t);
			return respError(t);
		}
	}

	@Override
	public Response updateCard(String addressBookUid, String href, String body) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] updateCard({}, {}, ...)", RunContext.getRunProfileId(), addressBookUid, href);
			logger.debug("{}", body);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			// Manager's call is already ro protected for remoteProviders
			VCard vCard = parseVCard(body);
			manager.updateContactObject(categoryId, href, vCard);
			return respOkNoContent();
			
		} catch (WTNotFoundException ex) {
			return respErrorNotFound();
		} catch (Throwable t) {
			logger.error("[{}] updateCard({}, {}, ...)", RunContext.getRunProfileId(), addressBookUid, href, t);
			return respError(t);
		}
	}

	@Override
	public Response deleteCard(String addressBookUid, String href) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] deleteCard({}, {})", RunContext.getRunProfileId(), addressBookUid, href);
		}
		
		try {
			int categoryId = ContactsUtils.decodeAsCategoryId(addressBookUid);
			manager.deleteContactObject(categoryId, href);
			return respOkNoContent();
			
		} catch (WTNotFoundException ex) {
			return respErrorNotFound();
		} catch (Throwable t) {
			logger.error("[{}] deleteCard({}, {})", RunContext.getRunProfileId(), addressBookUid, href, t);
			return respError(t);
		}
	}
	
	private AddressBook createAddressBook(UserProfileId currentProfileId, Category cat, DateTime lastRevisionTimestamp, FolderShare.Permissions permissions) {
		UserProfile.Data owud = WT.getUserData(cat.getProfileId());
		
		String displayName = cat.getName();
		if (!currentProfileId.equals(cat.getProfileId())) {
			//String apn = LangUtils.abbreviatePersonalName(false, owud.getDisplayName());
			displayName = "[" + owud.getDisplayName() + "] " + displayName;
		}
		String ownerUsername = owud.getProfileEmailAddress();
		
		return new AddressBook()
			.id(cat.getCategoryId())
			.uid(ContactsUtils.encodeAsCategoryUid(cat.getCategoryId()))
			.displayName(displayName)
			.description(cat.getDescription())
			.syncToken(buildEtag(lastRevisionTimestamp))
			.aclFol(permissions.getFolderPermissions().toString())
			.aclEle(permissions.getItemsPermissions().toString())
			.ownerUsername(ownerUsername);
	}
	
	private Card createDavObject(ContactObject obj) {
		Card ret = new Card()
			.id(Integer.valueOf(obj.getContactId()))
			.uid(obj.getPublicUid())
			.href(obj.getHref())
			.lastModified(obj.getRevisionTimestamp().withZone(DateTimeZone.UTC).getMillis()/1000)
			.etag(buildEtag(obj.getRevisionTimestamp()));
		
		if (obj instanceof ContactObjectWithVCard) {
			ContactObjectWithVCard objwv = (ContactObjectWithVCard)obj;
			return ret.size(objwv.getSize())
					.vcard(objwv.getVcard());
		} else if (obj instanceof ContactObjectWithBean) {
			return ret;
		} else {
			return ret;
		}
	}
	
	private CardChanged createCardChanged(ContactObject contactObject) {
		return new CardChanged()
			.id(Integer.valueOf(contactObject.getContactId()))
			.href(contactObject.getHref())
			.etag(buildEtag(contactObject.getRevisionTimestamp()));
	}
	
	private CardsChanges createCardsChanges(DateTime lastRevisionTimestamp, Delta<ContactObject> delta) {
		ArrayList<CardChanged> inserted = new ArrayList<>();
		ArrayList<CardChanged> updated = new ArrayList<>();
		ArrayList<CardChanged> deleted = new ArrayList<>();
		
		for (ChangedItem<ContactObject> item : delta.getItems()) {
			if (ChangedItem.ChangeType.ADDED.equals(item.getChangeType())) {
				inserted.add(createCardChanged(item.getObject()));
			} else if (ChangedItem.ChangeType.DELETED.equals(item.getChangeType())) {
				deleted.add(createCardChanged(item.getObject()));
			} else {
				updated.add(createCardChanged(item.getObject()));
			}
		}
		
		return new CardsChanges()
			.syncToken(buildEtag(lastRevisionTimestamp))
			.inserted(inserted)
			.updated(updated)
			.deleted(deleted);
	}
	
	private String buildEtag(DateTime revisionTimestamp) {
		if (revisionTimestamp != null) {
			return ETAG_FORMATTER.print(revisionTimestamp);
		} else {
			return DEFAULT_ETAG;
		}
	}
	
	private VCard parseVCard(String s) throws WTException {
		try {
			return VCardUtils.parseFirst(s);
		} catch(IOException ex) {
			throw new WTException(ex, "Unable to parse vcard data");
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
		manager.setSoftwareName("rest-carddav");
		return manager;
	}
	
	@Override
	protected Object createErrorEntity(Response.Status status, String message) {
		return new ApiError()
				.code(status.getStatusCode())
				.description(message);
	}
}
