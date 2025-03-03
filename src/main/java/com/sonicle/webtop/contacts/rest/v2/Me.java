/*
 * Copyright (C) 2024 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2024 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.rest.v2;

import com.sonicle.commons.beans.ItemsListResult;
import com.sonicle.commons.flags.BitFlags;
import com.sonicle.webtop.contacts.ContactObjectOutputType;
import com.sonicle.webtop.contacts.ContactsManager;
import com.sonicle.webtop.contacts.ContactsServiceSettings;
import com.sonicle.webtop.contacts.IContactsManager.ContactGetOption;
import com.sonicle.webtop.contacts.IContactsManager.ContactUpdateOption;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryBase;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.contacts.model.ContactObject;
import com.sonicle.webtop.contacts.model.ContactPictureWithBytes;
import com.sonicle.webtop.contacts.model.ContactPictureWithSize;
import com.sonicle.webtop.contacts.swagger.v2.api.MeApi;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoriesResult;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategory;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoryBase;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContact;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactEx;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactPictureMeta;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactsResult;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactsResultDelta;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiError;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.model.Delta;
import com.sonicle.webtop.core.sdk.BaseRestApiUtils;
import com.sonicle.webtop.core.sdk.UserProfileId;
import com.sonicle.webtop.core.sdk.WTException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ms outlook rest apis
 * https://learn.microsoft.com/en-us/previous-versions/office/office-365-api/api/version-2.0/use-outlook-rest-api
 * ms delta
 * https://learn.microsoft.com/en-us/graph/delta-query-events?tabs=http#example-synchronize-events-in-a-calendar-view
 * ms graph
 * https://learn.microsoft.com/en-us/graph/api/resources/contact?view=graph-rest-1.0&preserve-view=true
 * ms openapi spec
 * https://learn.microsoft.com/en-us/copilot/finance/get-started/custom%20connectors/openapi-specification
 * bird rest apis
 * https://docs.bird.com/api/contacts-api/api-reference/manage-workspace-contacts/update-a-contact
 * google sync
 * https://developers.google.com/calendar/api/guides/sync?hl=it
 * google people api
 * https://developers.google.com/people/api/rest/v1/people/searchContacts?hl=it
 * 
 * 
 * pipeDelimited and spaceDelimited style examples can be unresolvable
 * https://github.com/OAI/OpenAPI-Specification/issues/3737
 * https://stackoverflow.com/questions/76544610/in-openapi-given-an-array-of-objects-using-multipart-form-data-is-there-a-way
 * https://stackoverflow.com/questions/78158768/pass-array-as-query-parameter-in-openapi
 * 
 * OpenAPI Generator: Initialize List properties as null and not as empty Lists
 * https://stackoverflow.com/questions/73021872/openapi-generator-initialize-list-properties-as-null-and-not-as-empty-lists
 * https://github.com/OpenAPITools/openapi-generator/issues/17206
 * https://github.com/OpenAPITools/openapi-generator/issues/15891
 * 
 * @author malbinola
 */
public class Me extends MeApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(Eas.class);
	
	private ContactsServiceSettings getServiceSettings() {
		return new ContactsServiceSettings(SERVICE_ID, RunContext.getRunProfileId().getDomainId());
	}
	
	private ContactsManager getManager() {
		return getManager(RunContext.getRunProfileId());
	}
	
	private ContactsManager getManager(UserProfileId targetProfileId) {
		ContactsManager manager = (ContactsManager)WT.getServiceManager(SERVICE_ID, targetProfileId);
		manager.setSoftwareName("rest");
		return manager;
	}

	@Override
	public Response listCategories(String _filter, String _select, String _orderBy, Integer _pageNo, Integer _pageSize, Boolean _returnCount) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] listCategories()", RunContext.getRunProfileId());
		}
		
		try {
			boolean returnFullCount = _returnCount == null ? false : _returnCount;
			ItemsListResult<Category> result = manager.listCategories(_filter, BaseRestApiUtils.parseSortInfo(_orderBy), _pageNo, BaseRestApiUtils.pageSizeOrDefault(_pageNo, _pageSize), returnFullCount);
			Map<Integer, DateTime> itemsLastRevisionMap = manager.getCategoriesItemsLastRevision(
				result.items.stream()
					.map((category) -> {
						return category.getCategoryId();
					})
					.collect(Collectors.toList())
			);
			return respOk(ApiUtils.fillApiCategoriesResult(new ApiCategoriesResult(), BaseRestApiUtils.parseSet(_select), result, itemsLastRevisionMap));
			
		} catch (Throwable t) {
			LOGGER.error("[{}] listCategories()", RunContext.getRunProfileId(), t);
			return respError(t);
		}
	}

	@Override
	public Response getCategory(String categoryId) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] getCategory({})", RunContext.getRunProfileId(), categoryId);
		}
		
		try {
			Category category = manager.getCategory(ApiUtils.parseCategory(categoryId));
			if (category == null) return respErrorNotFound();
			Map<Integer, DateTime> itemsLastRevisionMap = manager.getCategoriesItemsLastRevision(Arrays.asList(category.getCategoryId()));
			
			return respOk(ApiUtils.fillApiCategory(new ApiCategory(), null, category, itemsLastRevisionMap.get(category.getCategoryId())));
			
		} catch (Throwable t) {
			LOGGER.error("[{}] getCategory({})", RunContext.getRunProfileId(), categoryId, t);
			return respError(t);
		}
	}

	@Override
	public Response addCategory(String userId, ApiCategoryBase body) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] addCategory({})", RunContext.getRunProfileId(), userId);
		}
		
		try {
			CategoryBase category = ApiUtils.fillCategoryBase(new CategoryBase(), null, body);
			category.setUserId(userIdOrDefault(userId));
			Category newCategory = manager.addCategory(category);
			return respOkCreated(ApiUtils.fillApiCategory(new ApiCategory(), null, newCategory, null));
			
		} catch (Throwable t) {
			LOGGER.error("[{}] addCategory({})", RunContext.getRunProfileId(), userId, t);
			return respError(t);
		}
	}

	@Override
	public Response updateCategory(String categoryId, ApiCategoryBase body) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] updateCategory({})", RunContext.getRunProfileId(), categoryId);
		}
		
		try {
			Category category = manager.getCategory(ApiUtils.parseCategory(categoryId));
			if (category == null) return respErrorNotFound();
			
			ApiUtils.fillCategoryBase(category, null, body);
			manager.updateCategory(category.getCategoryId(), category);
			return respOk();
			
		} catch (Throwable t) {
			LOGGER.error("[{}] addCategory({})", RunContext.getRunProfileId(), categoryId, t);
			return respError(t);
		}
	}

	@Override
	public Response deleteCategory(String categoryId) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] deleteCategory({})", RunContext.getRunProfileId(), categoryId);
		}
		
		try {
			manager.deleteCategory(ApiUtils.parseCategory(categoryId));
			return respOkNoContent();
			
		} catch (Throwable t) {
			LOGGER.error("[{}] deleteCategory({})", RunContext.getRunProfileId(), categoryId, t);
			return respError(t);
		}
	}

	@Override
	public Response listCategoryContacts(String categoryId, String _filter, String _select, String _orderBy, Integer _pageNo, Integer _pageSize, Boolean _returnCount) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] listCategoryContacts({})", RunContext.getRunProfileId(), categoryId);
		}
		
		try {
			boolean returnFullCount = _returnCount == null ? false : _returnCount;
			ItemsListResult<ContactObject> result = manager.listContacts(Arrays.asList(ApiUtils.parseCategory(categoryId)), _filter, BaseRestApiUtils.parseSortInfo(_orderBy), _pageNo, BaseRestApiUtils.pageSizeOrDefault(_pageNo, _pageSize), returnFullCount, ContactObjectOutputType.BEAN);
			return respOk(ApiUtils.fillApiContactsResult(new ApiContactsResult(), BaseRestApiUtils.parseSet(_select), result));
			
		} catch (Throwable t) {
			LOGGER.error("[{}] listCategoryContacts({})", RunContext.getRunProfileId(), categoryId, t);
			return respError(t);
		}
	}
	
	@Override
	public Response listCategoryContactsDelta(String categoryId, String _syncToken, String _select) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] listCategoryContactsDelta({}, {})", RunContext.getRunProfileId(), categoryId, _syncToken);
		}
		
		//x-field-extra-annotation
		//x-class-extra-annotation
		// do not serialize nulls
		//@JsonInclude(JsonInclude.Include.NON_NULL)
		
		try {
			Delta<ContactObject> changes = manager.listContactsDelta(ApiUtils.parseCategory(categoryId), _syncToken, ContactObjectOutputType.BEAN);
			return respOk(ApiUtils.fillApiContactsResultDelta(new ApiContactsResultDelta(), BaseRestApiUtils.parseSet(_select), changes));
			
		} catch (Throwable t) {
			LOGGER.error("[{}] listCategoryContactsDelta({})", RunContext.getRunProfileId(), categoryId, t);
			return respError(t);
		}
	}
	
	@Override
	public Response addCategoryContact(String categoryId, ApiContactEx apiContactEx) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] addCategoryContact({})", RunContext.getRunProfileId(), categoryId);
		}
		
		try {
			ContactEx contact = ApiUtils.fillContactEx(new ContactEx(), null, apiContactEx);
			contact.setCategoryId(ApiUtils.parseCategory(categoryId));
			Contact newContact = manager.addContact(contact);
			return respOkCreated(ApiUtils.fillApiContact(new ApiContact(), null, newContact));
			
		} catch  (Throwable t) {
			LOGGER.error("[{}] addCategoryContact({})", RunContext.getRunProfileId(), categoryId, t);
			return respError(t);
		}
	}

	@Override
	public Response getContact(String contactId, String _select) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] getContact({})", RunContext.getRunProfileId(), contactId);
		}
		
		try {
			//BitFlags<IContactsManager.ContactGetOptions> options = new BitFlags<>(IContactsManager.ContactGetOptions.class);
			Contact contact = manager.getContact(contactId, BitFlags.noneOf(ContactGetOption.class));
			if (contact != null) {
				return respOk(ApiUtils.fillApiContact(new ApiContact(), BaseRestApiUtils.parseSet(_select), contact));
			} else {
				return respErrorNotFound();
			}
			
		} catch (Throwable t) {
			LOGGER.error("[{}] getContact({})", RunContext.getRunProfileId(), contactId, t);
			return respError(t);
		}
	}
	
	@Override
	public Response addContact(String categoryId, ApiContactEx body) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] addContact({})", RunContext.getRunProfileId(), categoryId);
		}
		
		try {
			ContactEx contact = ApiUtils.fillContactEx(new ContactEx(), null, body);
			contact.setCategoryId(ApiUtils.parseCategory(categoryId));
			Contact newContact = manager.addContact(contact);
			return respOkCreated(ApiUtils.fillApiContact(new ApiContact(), null, newContact));
			
		} catch  (Throwable t) {
			LOGGER.error("[{}] addContact({})", RunContext.getRunProfileId(), categoryId, t);
			return respError(t);
		}
	}

	@Override
	public Response updateContact(String contactId, String _update, ApiContactEx body) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] updateContact({})", RunContext.getRunProfileId(), contactId);
		}
		
		try {
			Contact contact = manager.getContact(contactId, BitFlags.noneOf(ContactGetOption.class));
			if (contact == null) return respErrorNotFound();
			
			ApiUtils.fillContactEx(contact, BaseRestApiUtils.parseSet(_update), body);
			BitFlags<ContactUpdateOption> options = BitFlags.noneOf(ContactUpdateOption.class);
			manager.updateContact(contactId, contact, options);
			return respOk();
			
		} catch (Throwable t) {
			LOGGER.error("[{}] updateContact({})", RunContext.getRunProfileId(), contactId, t);
			return respError(t);
		}
	}

	@Override
	public Response deleteContact(String contactId) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] deleteContact({})", RunContext.getRunProfileId(), contactId);
		}
		
		try {
			manager.deleteContact(contactId);
			return respOkNoContent();
			
		} catch (Throwable t) {
			LOGGER.error("[{}] deleteContact({})", RunContext.getRunProfileId(), contactId, t);
			return respError(t);
		}
	}
	
	@Override
	public Response getContactPictureMeta(String contactId) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] getContactPictureMeta({})", RunContext.getRunProfileId(), contactId);
		}
		
		try {
			ContactPictureWithSize picture = (ContactPictureWithSize)manager.getContactPicture(contactId, true);
			if (picture != null) {
				return respOk(ApiUtils.fillApiContactPictureMeta(new ApiContactPictureMeta(), picture));
			} else {
				return respErrorNotFound();
			}
			
		} catch (Throwable t) {
			LOGGER.error("[{}] getContactPictureMeta({})", RunContext.getRunProfileId(), contactId, t);
			return respError(t);
		}
	}
	
	@Override
	public Response getContactPictureBytes(String contactId) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] getContactPictureRaw({})", RunContext.getRunProfileId(), contactId);
		}
		
		try {
			ContactPictureWithBytes picture = manager.getContactPicture(contactId);
			if (picture != null) {
				try (ByteArrayInputStream bais = new ByteArrayInputStream(picture.getBytes())) {
					return respOk(bais, picture.getMediaType());
				}
			} else {
				return respErrorNotFound();
			}
			
		} catch (Throwable t) {
			LOGGER.error("[{}] getContactPictureRaw({})", RunContext.getRunProfileId(), contactId, t);
			return respError(t);
		}
	}

	@Override
	public Response setContactPicture(String contactId, String contentType, File body) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] setContactPicture({})", RunContext.getRunProfileId(), contactId);
		}
		
		try {
			ContactPictureWithBytes picture = parseContactPictureBody(body, contentType);
			manager.updateContactPicture(contactId, picture);
			return respOk();
			
		} catch (Throwable t) {
			LOGGER.error("[{}] setContactPicture({})", RunContext.getRunProfileId(), contactId, t);
			return respError(t);
		}
	}

	@Override
	public Response clearContactPicture(String contactId) {
		ContactsManager manager = getManager();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}] deleteContactPicture({})", RunContext.getRunProfileId(), contactId);
		}
		
		try {
			manager.updateContactPicture(contactId, null);
			return respOkNoContent();
			
		} catch (Throwable t) {
			LOGGER.error("[{}] deleteContactPicture({})", RunContext.getRunProfileId(), contactId, t);
			return respError(t);
		}
	}
	
	@Override
	protected Object createErrorEntity(Response.Status status, String message) {
		return new ApiError()
			.code(status.getStatusCode())
			.description(message);
	}
	
	private ContactPictureWithBytes parseContactPictureBody(final File body, final String contentType) throws WTException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(body);
			ContactPictureWithBytes picture = new ContactPictureWithBytes(IOUtils.toByteArray(fis));
			picture.setMediaType(contentType);
			return picture;
			
		} catch (FileNotFoundException ex) {
			throw new WTException(ex, "File not found {0}");
		} catch (IOException ex) {
			throw new WTException(ex, "Unable to read file {0}");
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}
}
