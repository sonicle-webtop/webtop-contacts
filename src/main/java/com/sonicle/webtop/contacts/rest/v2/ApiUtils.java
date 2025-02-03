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

import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.beans.ItemsListResult;
import com.sonicle.commons.time.DateTimeUtils;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryBase;
import com.sonicle.webtop.contacts.model.CategoryRemoteParameters;
import com.sonicle.webtop.contacts.model.ChangedItem;
import com.sonicle.webtop.contacts.model.Delta;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.contacts.model.ContactObject;
import com.sonicle.webtop.contacts.model.ContactObjectWithBean;
import com.sonicle.webtop.contacts.model.ContactPictureWithSize;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoriesResult;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategory;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoryBase;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoryBase.EasSyncEnum;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoryBase.ProviderEnum;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContact;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactBase;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactChanged;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactEx;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactPictureMeta;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactsResult;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactsResultDelta;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiOwnerInfo;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.sdk.WTParseException;
import com.sonicle.webtop.core.sdk.BaseRestApiUtils;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import static com.sonicle.webtop.core.sdk.BaseRestApiUtils.shouldSet;

/**
 *
 * @author malbinola
 */
public class ApiUtils {
	
	public static CategoryBase fillCategoryBase(final CategoryBase tgt, final Set<String> fields2set, final ApiCategoryBase src) {
		if (shouldSet(fields2set, "provider")) {
			tgt.setProvider(EnumUtils.forName(src.getProvider(), CategoryBase.Provider.class));
			if (CategoryBase.Provider.CARDDAV.equals(tgt.getProvider())) {
				tgt.setParametersAsObject(CategoryRemoteParameters.fromJson(src.getProviderParams()), CategoryRemoteParameters.class);
			}
		}
		if (shouldSet(fields2set, "name")) tgt.setName(src.getName());
		if (shouldSet(fields2set, "description")) tgt.setDescription(src.getDescription());
		if (shouldSet(fields2set, "color")) tgt.setColor(src.getColor());
		if (shouldSet(fields2set, "easSync")) tgt.setSync(EnumUtils.forName(src.getEasSync(), CategoryBase.Sync.class));
		if (shouldSet(fields2set, "isPrivate")) tgt.setIsPrivate(src.getIsPrivate());
		if (shouldSet(fields2set, "remoteSyncFrequency")) tgt.setRemoteSyncFrequency(BaseRestApiUtils.toShort(src.getRemoteSyncFrequency()));
		return tgt;
	}
	
	public static ApiCategoriesResult fillApiCategoriesResult(final ApiCategoriesResult tgt, final Set<String> fields2set, final ItemsListResult<Category> result, final Map<Integer, DateTime> itemsLastRevisionMap) {
		tgt.setTotalCount(result.getFullCount());
		ArrayList<ApiCategory> items = new ArrayList<>(result.getItems().size());
		for (Category item : result.getItems()) {
			DateTime itemsLastRevision = (itemsLastRevisionMap != null) ? itemsLastRevisionMap.get(item.getCategoryId()) : null;
			items.add(fillApiCategory(new ApiCategory(), fields2set, item, itemsLastRevision));
		}
		tgt.items(items);
		return tgt;
	}
	
	public static ApiOwnerInfo fillApiOwnerInfo(final ApiOwnerInfo tgt, final UserProfileId profileId) {
		tgt.userId(profileId.getUserId());
		UserProfile.Data ud = WT.getProfileData(profileId);
		tgt.emailAddress(ud.getPersonalEmailAddress());
		tgt.displayName(ud.getDisplayName());
		return tgt;
	}
	
	public static ApiCategory fillApiCategory(final ApiCategory tgt, final Set<String> fields2set, final Category src, final DateTime itemsRevisionTimestamp) {
		fillApiCategoryBase(tgt, fields2set, src);
		tgt.id(String.valueOf(src.getCategoryId()));
		tgt.etag(BaseRestApiUtils.buildETag(src.getRevisionTimestamp()));
		tgt.itemsETag(BaseRestApiUtils.buildETag(itemsRevisionTimestamp));
		tgt.createdOn(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getCreationTimestamp()));
		tgt.updatedOn(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getRevisionTimestamp()));
		tgt.owner(fillApiOwnerInfo(new ApiOwnerInfo(), src.getProfileId()));
		return tgt;
	}
	
	public static ApiCategoryBase fillApiCategoryBase(final ApiCategoryBase tgt, final Set<String> fields2set, final CategoryBase src) {
		if (shouldSet(fields2set, "provider")) {
			tgt.setProvider(EnumUtils.forName(src.getProvider(), ProviderEnum.class));
			tgt.setProviderParams(src.getParameters());
		}
		if (shouldSet(fields2set, "builtIn")) tgt.setBuiltIn(src.getBuiltIn());
		if (shouldSet(fields2set, "name")) tgt.setName(src.getName());
		if (shouldSet(fields2set, "description")) tgt.setDescription(src.getDescription());
		if (shouldSet(fields2set, "color")) tgt.setColor(src.getColor());
		if (shouldSet(fields2set, "easSync")) tgt.setEasSync(EnumUtils.forName(src.getSync(), EasSyncEnum.class));
		if (shouldSet(fields2set, "isPrivate")) tgt.setIsPrivate(src.getIsPrivate());
		if (shouldSet(fields2set, "remoteSyncFrequency")) tgt.setRemoteSyncFrequency(BaseRestApiUtils.toInteger(src.getRemoteSyncFrequency()));
		if (shouldSet(fields2set, "remoteSyncTimestamp")) tgt.setRemoteSyncTimestamp(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getRemoteSyncTimestamp()));
		if (shouldSet(fields2set, "remoteSyncToken")) tgt.setRemoteSyncToken(src.getRemoteSyncTag());
		return tgt;
	}
	
	public static ContactEx fillContactEx(final ContactEx tgt, final Set<String> fields2set, final ApiContactEx src) {
		if (shouldSet(fields2set, "displayName")) tgt.setDisplayName(src.getDisplayName());
		if (shouldSet(fields2set, "title")) tgt.setTitle(src.getTitle());
		if (shouldSet(fields2set, "firstName")) tgt.setFirstName(src.getFirstName());
		if (shouldSet(fields2set, "lastName")) tgt.setLastName(src.getLastName());
		if (shouldSet(fields2set, "nickname")) tgt.setNickname(src.getNickname());
		if (shouldSet(fields2set, "mobile")) tgt.setMobile(src.getMobile());
		if (shouldSet(fields2set, "pager1")) tgt.setPager1(src.getPager1());
		if (shouldSet(fields2set, "pager2")) tgt.setPager2(src.getPager2());
		if (shouldSet(fields2set, "email1")) tgt.setEmail1(src.getEmail1());
		if (shouldSet(fields2set, "email2")) tgt.setEmail2(src.getEmail2());
		if (shouldSet(fields2set, "email3")) tgt.setEmail3(src.getEmail3());
		if (shouldSet(fields2set, "im1")) tgt.setInstantMsg1(src.getIm1());
		if (shouldSet(fields2set, "im2")) tgt.setInstantMsg2(src.getIm2());
		if (shouldSet(fields2set, "im3")) tgt.setInstantMsg3(src.getIm3());
		if (shouldSet(fields2set, "workAddress")) tgt.setWorkAddress(src.getWorkAddress());
		if (shouldSet(fields2set, "workPostalCode")) tgt.setWorkPostalCode(src.getWorkPostalCode());
		if (shouldSet(fields2set, "workCity")) tgt.setWorkCity(src.getWorkCity());
		if (shouldSet(fields2set, "workState")) tgt.setWorkState(src.getWorkState());
		if (shouldSet(fields2set, "workCountry")) tgt.setWorkCountry(src.getWorkCountry());
		if (shouldSet(fields2set, "workTelephone1")) tgt.setWorkTelephone1(src.getWorkTelephone1());
		if (shouldSet(fields2set, "workTelephone2")) tgt.setWorkTelephone2(src.getWorkTelephone2());
		if (shouldSet(fields2set, "workFax")) tgt.setWorkFax(src.getWorkFax());
		if (shouldSet(fields2set, "homeAddress")) tgt.setHomeAddress(src.getHomeAddress());
		if (shouldSet(fields2set, "homePostalCode")) tgt.setHomePostalCode(src.getHomePostalCode());
		if (shouldSet(fields2set, "homeCity")) tgt.setHomeCity(src.getHomeCity());
		if (shouldSet(fields2set, "homeState")) tgt.setHomeState(src.getHomeState());
		if (shouldSet(fields2set, "homeCountry")) tgt.setHomeCountry(src.getHomeCountry());
		if (shouldSet(fields2set, "homeTelephone1")) tgt.setHomeTelephone1(src.getHomeTelephone1());
		if (shouldSet(fields2set, "homeTelephone2")) tgt.setHomeTelephone2(src.getHomeTelephone2());
		if (shouldSet(fields2set, "homeFax")) tgt.setHomeFax(src.getHomeFax());
		if (shouldSet(fields2set, "otherAddress")) tgt.setOtherAddress(src.getOtherAddress());
		if (shouldSet(fields2set, "otherPostalCode")) tgt.setOtherPostalCode(src.getOtherPostalCode());
		if (shouldSet(fields2set, "otherCity")) tgt.setOtherCity(src.getOtherCity());
		if (shouldSet(fields2set, "otherState")) tgt.setOtherState(src.getOtherState());
		if (shouldSet(fields2set, "otherCountry")) tgt.setOtherCountry(src.getOtherCountry());
		if (!tgt.hasCompany() || (!StringUtils.equals(src.getCompanyId(), tgt.getCompany().getCompanyId()) && !StringUtils.equals(src.getCompanyName(), tgt.getCompany().getValue()))) {
			if (!StringUtils.isBlank(src.getCompanyId()) || !StringUtils.isBlank(src.getCompanyName())) {
				tgt.setCompany(new ContactCompany(src.getCompanyName(), src.getCompanyId()));
			}
		}
		if (shouldSet(fields2set, "function")) tgt.setFunction(src.getFunction());
		if (shouldSet(fields2set, "department")) tgt.setDepartment(src.getDepartment());
		if (shouldSet(fields2set, "assistantName")) tgt.setAssistant(src.getAssistantName());
		if (shouldSet(fields2set, "assistantTelephone")) tgt.setAssistantTelephone(src.getAssistantTelephone());
		if (shouldSet(fields2set, "managerName")) tgt.setManager(src.getManagerName());
		if (shouldSet(fields2set, "partnerName")) tgt.setPartner(src.getPartnerName());
		if (shouldSet(fields2set, "birthday")) tgt.setBirthday(DateTimeUtils.parseLocalDate(BaseRestApiUtils.ISO_LOCALDATE_FMT, src.getBirthday()));
		if (shouldSet(fields2set, "anniversary")) tgt.setAnniversary(DateTimeUtils.parseLocalDate(BaseRestApiUtils.ISO_LOCALDATE_FMT, src.getAnniversary()));
		if (shouldSet(fields2set, "url")) tgt.setUrl(src.getUrl());
		if (shouldSet(fields2set, "notes")) tgt.setNotes(src.getNotes());
		//if (shouldSet(fields2set, "picture")) tgt.setPicture(picture);
		return tgt;
	}
	
	public static ApiContactsResult fillApiContactsResult(final ApiContactsResult tgt, final Set<String> fields2set, final ItemsListResult<ContactObject> result) {
		tgt.setTotalCount(result.getFullCount());
		ArrayList<ApiContact> items = new ArrayList<>(result.getItems().size());
		for (ContactObject item : result.getItems()) {
			items.add(fillApiContact(new ApiContact(), fields2set, (ContactObjectWithBean)item));
		}
		tgt.items(items);
		return tgt;
	}
	
	public static ApiContactsResultDelta fillApiContactsResultDelta(final ApiContactsResultDelta tgt, final Set<String> fields2set, final Delta<ContactObject> changes) {
		tgt.setNextSyncToken(changes.getNextSyncToken());
		ArrayList<ApiContactChanged> items = new ArrayList<>(changes.getItems().size());
		for (ChangedItem<ContactObject> item : changes.getItems()) {
			items.add(fillApiContactChanged(item.getChangeType(), new ApiContactChanged(), fields2set, (ContactObjectWithBean)item.getObject()));
		}
		tgt.items(items);
		return tgt;
	}
	
	public static ApiContactChanged fillApiContactChanged(final ChangedItem.ChangeType changeType, final ApiContactChanged tgt, final Set<String> fields2set, final ContactObjectWithBean src) {
		if (changeType.equals(ChangedItem.ChangeType.ADDED)) {
			tgt.$added(true);
		} else if (changeType.equals(ChangedItem.ChangeType.DELETED)) {
			tgt.$deleted(true);
		} else {
			tgt.$updated(true);
		}
		fillApiContactBase(tgt, fields2set, src.getContact());
		tgt.id(String.valueOf(src.getContactId()));
		tgt.etag(BaseRestApiUtils.buildETag(src.getContact().getRevisionTimestamp()));
		tgt.createdOn(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getContact().getCreationTimestamp()));
		tgt.updatedOn(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getContact().getRevisionTimestamp()));
		return tgt;
	}
	
	public static ApiContact fillApiContact(final ApiContact tgt, final Set<String> fields2set, final ContactObjectWithBean src) {
		fillApiContactBase(tgt, fields2set, src.getContact());
		tgt.id(String.valueOf(src.getContactId()));
		tgt.etag(BaseRestApiUtils.buildETag(src.getRevisionTimestamp()));
		tgt.createdOn(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getContact().getCreationTimestamp()));
		tgt.updatedOn(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getRevisionTimestamp()));
		return tgt;
	}
	
	public static ApiContact fillApiContact(final ApiContact tgt, final Set<String> fields2set, final Contact src) {
		fillApiContactBase(tgt, fields2set, src);
		tgt.id(String.valueOf(src.getContactId()));
		tgt.etag(BaseRestApiUtils.buildETag(src.getRevisionTimestamp()));
		tgt.createdOn(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getCreationTimestamp()));
		tgt.updatedOn(DateTimeUtils.print(BaseRestApiUtils.ISO_DATEDIME_FMT, src.getRevisionTimestamp()));
		return tgt;
	}
	
	public static ApiContactPictureMeta fillApiContactPictureMeta(final ApiContactPictureMeta tgt, final ContactPictureWithSize src) {
		tgt.mediaType(src.getMediaType());
		tgt.width(src.getWidth());
		tgt.height(src.getHeight());
		tgt.size(src.getSize());
		return tgt;
	}
	
	public static ApiContactBase fillApiContactBase(final ApiContactBase tgt, final Set<String> fields2set, final ContactEx src) {
		if (shouldSet(fields2set, "displayName")) tgt.setDisplayName(src.getDisplayName());
		if (shouldSet(fields2set, "title")) tgt.setTitle(src.getTitle());
		if (shouldSet(fields2set, "firstName")) tgt.setFirstName(src.getFirstName());
		if (shouldSet(fields2set, "lastName")) tgt.setLastName(src.getLastName());
		if (shouldSet(fields2set, "nickname")) tgt.setNickname(src.getNickname());
		if (shouldSet(fields2set, "mobile")) tgt.setMobile(src.getMobile());
		if (shouldSet(fields2set, "pager1")) tgt.setPager1(src.getPager1());
		if (shouldSet(fields2set, "pager2")) tgt.setPager2(src.getPager2());
		if (shouldSet(fields2set, "email1")) tgt.setEmail1(src.getEmail1());
		if (shouldSet(fields2set, "email2")) tgt.setEmail2(src.getEmail2());
		if (shouldSet(fields2set, "email3")) tgt.setEmail3(src.getEmail3());
		if (shouldSet(fields2set, "im1")) tgt.setIm1(src.getInstantMsg1());
		if (shouldSet(fields2set, "im2")) tgt.setIm2(src.getInstantMsg2());
		if (shouldSet(fields2set, "im3")) tgt.setIm3(src.getInstantMsg3());
		if (shouldSet(fields2set, "workAddress")) tgt.setWorkAddress(src.getWorkAddress());
		if (shouldSet(fields2set, "workPostalCode")) tgt.setWorkPostalCode(src.getWorkPostalCode());
		if (shouldSet(fields2set, "workCity")) tgt.setWorkCity(src.getWorkCity());
		if (shouldSet(fields2set, "workState")) tgt.setWorkState(src.getWorkState());
		if (shouldSet(fields2set, "workCountry")) tgt.setWorkCountry(src.getWorkCountry());
		if (shouldSet(fields2set, "workTelephone1")) tgt.setWorkTelephone1(src.getWorkTelephone1());
		if (shouldSet(fields2set, "workTelephone2")) tgt.setWorkTelephone2(src.getWorkTelephone2());
		if (shouldSet(fields2set, "workFax")) tgt.setWorkFax(src.getWorkFax());
		if (shouldSet(fields2set, "homeAddress")) tgt.setHomeAddress(src.getHomeAddress());
		if (shouldSet(fields2set, "homePostalCode")) tgt.setHomePostalCode(src.getHomePostalCode());
		if (shouldSet(fields2set, "homeCity")) tgt.setHomeCity(src.getHomeCity());
		if (shouldSet(fields2set, "homeState")) tgt.setHomeState(src.getHomeState());
		if (shouldSet(fields2set, "homeCountry")) tgt.setHomeCountry(src.getHomeCountry());
		if (shouldSet(fields2set, "homeTelephone1")) tgt.setHomeTelephone1(src.getHomeTelephone1());
		if (shouldSet(fields2set, "homeTelephone2")) tgt.setHomeTelephone2(src.getHomeTelephone2());
		if (shouldSet(fields2set, "homeFax")) tgt.setHomeFax(src.getHomeFax());
		if (shouldSet(fields2set, "otherAddress")) tgt.setOtherAddress(src.getOtherAddress());
		if (shouldSet(fields2set, "otherPostalCode")) tgt.setOtherPostalCode(src.getOtherPostalCode());
		if (shouldSet(fields2set, "otherCity")) tgt.setOtherCity(src.getOtherCity());
		if (shouldSet(fields2set, "otherState")) tgt.setOtherState(src.getOtherState());
		if (shouldSet(fields2set, "otherCountry")) tgt.setOtherCountry(src.getOtherCountry());
		if (shouldSet(fields2set, "company")) {
			tgt.setCompanyId(src.hasCompany() ? src.getCompany().getCompanyId(): null);
			tgt.setCompanyName(src.hasCompany() ? src.getCompany().getCompanyDescription() : null);
		}
		if (shouldSet(fields2set, "function")) tgt.setFunction(src.getFunction());
		if (shouldSet(fields2set, "department")) tgt.setDepartment(src.getDepartment());
		if (shouldSet(fields2set, "assistantName")) tgt.setAssistantName(src.getAssistant());
		if (shouldSet(fields2set, "assistantTelephone")) tgt.setAssistantTelephone(src.getAssistantTelephone());
		if (shouldSet(fields2set, "managerName")) tgt.setManagerName(src.getManager());
		if (shouldSet(fields2set, "partnerName")) tgt.setPartnerName(src.getPartner());
		if (shouldSet(fields2set, "birthday")) tgt.setBirthday(DateTimeUtils.print(BaseRestApiUtils.ISO_LOCALDATE_FMT, src.getBirthday()));
		if (shouldSet(fields2set, "anniversary")) tgt.setAnniversary(DateTimeUtils.print(BaseRestApiUtils.ISO_LOCALDATE_FMT, src.getAnniversary()));
		if (shouldSet(fields2set, "url")) tgt.setUrl(src.getUrl());
		if (shouldSet(fields2set, "notes")) tgt.setNotes(src.getNotes());
		//if (shouldSet(fields2set, "picture")) tgt.setPicture(picture);
		return tgt;
	}
	
	public static int parseCategory(final String categoryId) throws WTParseException {
		try {
			return Integer.valueOf(categoryId);
		} catch (NumberFormatException ex) {
			throw new WTParseException(ex);
		}
	}
}
