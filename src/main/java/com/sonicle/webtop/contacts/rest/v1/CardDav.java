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

import com.sonicle.commons.LangUtils.CollectionChangeSet;
import com.sonicle.commons.time.DateTimeUtils;
import com.sonicle.webtop.contacts.ContactsManager;
import com.sonicle.webtop.contacts.ContactsServiceSettings;
import com.sonicle.webtop.contacts.ManagerUtils;
import com.sonicle.webtop.contacts.NotFoundException;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.ContactCard;
import com.sonicle.webtop.contacts.model.ContactCardChanged;
import com.sonicle.webtop.contacts.swagger.v1.api.CarddavApi;
import com.sonicle.webtop.contacts.swagger.v1.model.AddressBook;
import com.sonicle.webtop.contacts.swagger.v1.model.AddressBookNew;
import com.sonicle.webtop.contacts.swagger.v1.model.AddressBookUpdate;
import com.sonicle.webtop.contacts.swagger.v1.model.Card;
import com.sonicle.webtop.contacts.swagger.v1.model.CardChanged;
import com.sonicle.webtop.contacts.swagger.v1.model.CardNew;
import com.sonicle.webtop.contacts.swagger.v1.model.CardsChanges;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
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
	private static final DateTimeFormatter ETAG_FORMATTER = DateTimeUtils.createFormatter("yyyyMMddHHmmssSSS", DateTimeZone.UTC);
	
	@Override
	public Response getAddressBooks() {
		ContactsManager manager = getManager();
		List<AddressBook> items = new ArrayList<>();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getAddressBooks()", RunContext.getRunProfileId());
		}
		
		try {
			Map<Integer, Category> cats = manager.listCategories();
			Map<Integer, DateTime> revisions = manager.getCategoriesLastRevision(cats.keySet());
			for (Category cat : cats.values()) {
				if (cat.isProviderRemote()) continue;
				items.add(createAddressBook(cat, revisions.get(cat.getCategoryId())));
			}
			return respOk(items);
			
		} catch(WTException ex) {
			logger.error("[{}] getAddressBooks()", ex, RunContext.getRunProfileId());
			return respError(ex);
		}
	}

	@Override
	public Response getAddressBook(String addressBookUid) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getAddressBook({})", RunContext.getRunProfileId(), addressBookUid);
		}
		
		try {
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorNotFound();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			Map<Integer, DateTime> revisions = manager.getCategoriesLastRevision(Arrays.asList(cat.getCategoryId()));
			return respOk(createAddressBook(cat, revisions.get(cat.getCategoryId())));
			
		} catch(WTException ex) {
			logger.error("[{}] getAddressBook({})", ex, RunContext.getRunProfileId(), addressBookUid);
			return respError(ex);
		}
	}

	@Override
	public Response addAddressBook(AddressBookNew body) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] addAddressBook(...)", RunContext.getRunProfileId());
			logger.debug("{}", body);
		}
		
		try {
			Category cat = new Category();
			cat.setName(body.getDisplayName());
			cat.setDescription(body.getDescription());
			cat = manager.addCategory(cat);
			return respOkCreated(createAddressBook(cat, null));
			
		} catch(WTException ex) {
			logger.error("[{}] addAddressBook(...)", ex, RunContext.getRunProfileId());
			return respError(ex);
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
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorNotFound();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			if (body.getUpdatedFields().contains("displayName")) {
				cat.setName(body.getDisplayName());
			}
			if (body.getUpdatedFields().contains("description")) {
				cat.setDescription(body.getDescription());
			}
			manager.updateCategory(cat);
			return respOk();
			
		} catch(NotFoundException ex) {
			return respErrorNotFound();
		} catch(WTException ex) {
			logger.error("[{}] updateAddressBook({}, ...)", ex, RunContext.getRunProfileId(), addressBookUid);
			return respError(ex);
		}
	}

	@Override
	public Response deleteAddressBook(String addressBookUid) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] deleteAddressBook({})", RunContext.getRunProfileId(), addressBookUid);
		}
		
		try {
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			ContactsServiceSettings css = new ContactsServiceSettings(SERVICE_ID, RunContext.getRunProfileId().getDomainId());
			if (css.getDavAddressbookDeleteEnabled()) {
				manager.deleteCategory(categoryId);
				return respOkNoContent();
			} else {
				return respErrorNotAllowed();
			}
			
		} catch(NotFoundException ex) {
			return respErrorNotFound();
		} catch(WTException ex) {
			logger.error("[{}] deleteAddressBook({})", ex, RunContext.getRunProfileId(), addressBookUid);
			return respError(ex);
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
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorBadRequest();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			if ((hrefs == null) || hrefs.isEmpty()) {
				List<ContactCard> cards = manager.listContactCards(categoryId);
				for (ContactCard card : cards) {
					items.add(createCardWithData(card));
				}
				return respOk(items);
				
			} else {
				List<ContactCard> cards = manager.getContactCards(categoryId, hrefs);
				for (ContactCard card : cards) {
					items.add(createCardWithData(card));
				}
				return respOk(items);
			}
		} catch(WTException ex) {
			logger.error("[{}] getCards({})", ex, RunContext.getRunProfileId(), addressBookUid);
			return respError(ex);
		}
	}

	@Override
	public Response getCardsChanges(String addressBookUid, String syncToken, Integer limit) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getCardsChanges({}, {}, {})", RunContext.getRunProfileId(), addressBookUid, syncToken, limit);
		}
		
		try {
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorNotFound();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			Map<Integer, DateTime> revisions = manager.getCategoriesLastRevision(Arrays.asList(categoryId));
			
			DateTime since = null;
			if (!StringUtils.isBlank(syncToken)) {
				since = ETAG_FORMATTER.parseDateTime(syncToken);
				if (since == null) return respErrorBadRequest();
			}
			
			CollectionChangeSet<ContactCardChanged> changes = manager.listContactCardsChanges(categoryId, since, limit);
			return respOk(createCardsChanges(revisions.get(categoryId), changes));
			
		} catch(WTException ex) {
			logger.error("[{}] getCardsChanges({}, {}, {})", ex, RunContext.getRunProfileId(), addressBookUid, syncToken, limit);
			return respError(ex);
		}
	}

	@Override
	public Response getCard(String addressBookUid, String href) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] getCard({}, {})", RunContext.getRunProfileId(), addressBookUid, href);
		}
		
		try {
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			Category cat = manager.getCategory(categoryId);
			if (cat == null) return respErrorBadRequest();
			if (cat.isProviderRemote()) return respErrorBadRequest();
			
			ContactCard card = manager.getContactCard(categoryId, href);
			if (card != null) {
				return respOk(createCardWithData(card));
			} else {
				return respErrorNotFound();
			}
			
		} catch(WTException ex) {
			logger.error("[{}] getCard({}, {})", ex, RunContext.getRunProfileId(), addressBookUid, href);
			return respError(ex);
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
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			// Manager's call is already ro protected for remoteProviders
			VCard vCard = parseVCard(body.getVcard());
			manager.addContactCard(categoryId, body.getHref(), vCard);
			return respOk();
			
		} catch(WTException ex) {
			logger.error("[{}] addCard({}, ...)", ex, RunContext.getRunProfileId(), addressBookUid);
			return respError(ex);
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
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			// Manager's call is already ro protected for remoteProviders
			VCard vCard = parseVCard(body);
			manager.updateContactCard(categoryId, href, vCard);
			return respOkNoContent();
			
		} catch(NotFoundException ex) {
			return respErrorNotFound();
		} catch(WTException ex) {
			logger.error("[{}] updateCard({}, {}, ...)", ex, RunContext.getRunProfileId(), addressBookUid, href);
			return respError(ex);
		}
	}

	@Override
	public Response deleteCard(String addressBookUid, String href) {
		ContactsManager manager = getManager();
		
		if (logger.isDebugEnabled()) {
			logger.debug("[{}] deleteCard({}, {})", RunContext.getRunProfileId(), addressBookUid, href);
		}
		
		try {
			int categoryId = ManagerUtils.decodeAsCategoryId(addressBookUid);
			manager.deleteContactCard(categoryId, href);
			return respOkNoContent();
			
		} catch(NotFoundException ex) {
			return respErrorNotFound();
		} catch(WTException ex) {
			logger.error("[{}] deleteCard({}, {})", ex, RunContext.getRunProfileId(), addressBookUid, href);
			return respError(ex);
		}
	}
	
	private AddressBook createAddressBook(Category cat, DateTime lastRevisionTimestamp) {
		return new AddressBook()
				.id(cat.getCategoryId())
				.uid(ManagerUtils.encodeAsCategoryUid(cat.getCategoryId()))
				.displayName(cat.getName())
				.description(cat.getDescription())
				.syncToken(buildEtag(lastRevisionTimestamp));
	}
	
	private Card createCard(ContactCard card) {
		return new Card()
				.id(card.getContactId())
				.uid(card.getPublicUid())
				.href(card.getHref())
				.lastModified(card.getRevisionTimestamp().withZone(DateTimeZone.UTC).getMillis()/1000)
				.etag(buildEtag(card.getRevisionTimestamp()))
				.size(card.getSize());
	}
	
	private Card createCardWithData(ContactCard card) {
		return createCard(card)
				.vcard(card.getVcard());
	}
	
	private CardChanged createCardChanged(ContactCardChanged card) {
		return new CardChanged()
				.id(card.getContactId())
				.href(card.getHref())
				.etag(buildEtag(card.getRevisionTimestamp()));
	}
	
	private CardsChanges createCardsChanges(DateTime lastRevisionTimestamp, CollectionChangeSet<ContactCardChanged> changes) {
		ArrayList<CardChanged> inserted = new ArrayList<>();
		for (ContactCardChanged card : changes.inserted) {
			inserted.add(createCardChanged(card));
		}
		
		ArrayList<CardChanged> updated = new ArrayList<>();
		for (ContactCardChanged card : changes.updated) {
			updated.add(createCardChanged(card));
		}
		
		ArrayList<CardChanged> deleted = new ArrayList<>();
		for (ContactCardChanged card : changes.deleted) {
			deleted.add(createCardChanged(card));
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
			return VCardUtils.parse(s, true).get(0);
		} catch(IOException ex) {
			throw new WTException(ex, "Unable to parse vcard data");
		}
	}
	
	private ContactsManager getManager() {
		return getManager(RunContext.getRunProfileId());
	}
	
	private ContactsManager getManager(UserProfileId targetProfileId) {
		return (ContactsManager)WT.getServiceManager(SERVICE_ID, targetProfileId);
	}
}
