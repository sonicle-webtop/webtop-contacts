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
package com.sonicle.webtop.contacts;

import com.sonicle.commons.BitFlag;
import com.sonicle.commons.BitFlagEnum;
import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.IdentifierUtils;
import com.sonicle.commons.InternetAddressUtils;
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.LangUtils.CollectionChangeSet;
import com.sonicle.commons.PathUtils;
import com.sonicle.commons.concurrent.KeyedReentrantLocks;
import com.sonicle.commons.db.DbUtils;
import com.sonicle.commons.time.DateTimeUtils;
import com.sonicle.commons.web.json.CId;
import com.sonicle.commons.web.json.CompositeId;
import com.sonicle.commons.web.json.JsonResult;
import com.sonicle.dav.CardDav;
import com.sonicle.dav.CardDavFactory;
import com.sonicle.dav.DavSyncStatus;
import com.sonicle.dav.DavUtil;
import com.sonicle.dav.carddav.DavAddressbook;
import com.sonicle.dav.carddav.DavAddressbookCard;
import com.sonicle.dav.impl.DavException;
import com.sonicle.webtop.contacts.bol.OCategory;
import com.sonicle.webtop.contacts.bol.OCategoryPropSet;
import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.OContactAttachment;
import com.sonicle.webtop.contacts.bol.OContactAttachmentData;
import com.sonicle.webtop.contacts.bol.OContactCustomValue;
import com.sonicle.webtop.contacts.bol.OContactInfo;
import com.sonicle.webtop.contacts.bol.OContactPicture;
import com.sonicle.webtop.contacts.bol.OContactPictureMetaOnly;
import com.sonicle.webtop.contacts.bol.OListRecipient;
import com.sonicle.webtop.contacts.bol.VContact;
import com.sonicle.webtop.contacts.bol.VContactObject;
import com.sonicle.webtop.contacts.bol.VContactObjectChanged;
import com.sonicle.webtop.contacts.bol.VContactCompany;
import com.sonicle.webtop.contacts.bol.VContactHrefSync;
import com.sonicle.webtop.contacts.bol.VListRecipient;
import com.sonicle.webtop.contacts.bol.VContactLookup;
import com.sonicle.webtop.contacts.bol.model.MyShareRootCategory;
import com.sonicle.webtop.contacts.model.ShareFolderCategory;
import com.sonicle.webtop.contacts.model.ShareRootCategory;
import com.sonicle.webtop.contacts.model.ContactListRecipient;
import com.sonicle.webtop.contacts.dal.CategoryDAO;
import com.sonicle.webtop.contacts.dal.CategoryPropsDAO;
import com.sonicle.webtop.contacts.dal.ContactAttachmentDAO;
import com.sonicle.webtop.contacts.dal.ContactCustomValueDAO;
import com.sonicle.webtop.contacts.dal.ContactDAO;
import com.sonicle.webtop.contacts.dal.ContactPictureDAO;
import com.sonicle.webtop.contacts.dal.ContactVCardDAO;
import com.sonicle.webtop.contacts.dal.ContactPredicateVisitor;
import com.sonicle.webtop.contacts.dal.ContactTagDAO;
import com.sonicle.webtop.contacts.dal.ListRecipientDAO;
import com.sonicle.webtop.contacts.io.ContactInput;
import com.sonicle.webtop.contacts.io.VCardInput;
import com.sonicle.webtop.contacts.io.VCardOutput;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.CategoryRemoteParameters;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactAttachment;
import com.sonicle.webtop.contacts.model.ContactAttachmentWithBytes;
import com.sonicle.webtop.contacts.model.ContactAttachmentWithStream;
import com.sonicle.webtop.contacts.model.ContactBase;
import com.sonicle.webtop.contacts.model.ContactObject;
import com.sonicle.webtop.contacts.model.ContactObjectChanged;
import com.sonicle.webtop.contacts.model.ContactObjectWithBean;
import com.sonicle.webtop.contacts.model.ContactObjectWithVCard;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.contacts.model.ContactList;
import com.sonicle.webtop.contacts.model.ContactListEx;
import com.sonicle.webtop.contacts.model.ContactLookup;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.model.ContactPictureWithBytes;
import com.sonicle.webtop.contacts.model.ContactPictureWithSize;
import com.sonicle.webtop.contacts.model.ContactQuery;
import com.sonicle.webtop.contacts.model.ListContactsResult;
import com.sonicle.webtop.contacts.model.Grouping;
import com.sonicle.webtop.contacts.model.ShowBy;
import com.sonicle.webtop.contacts.model.ContactType;
import com.sonicle.webtop.contacts.products.MailchimpProduct;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.io.BatchBeanHandler;
import com.sonicle.webtop.core.app.io.input.WTReaderException;
import com.sonicle.webtop.core.app.provider.RecipientsProviderBase;
import com.sonicle.webtop.core.app.sdk.AuditReferenceDataEntry;
import com.sonicle.webtop.core.app.sdk.WTNotFoundException;
import com.sonicle.webtop.core.app.util.ExceptionUtils;
import com.sonicle.webtop.core.app.util.log.LogHandler;
import com.sonicle.webtop.core.app.util.log.LogMessage;
import com.sonicle.webtop.core.bol.OShare;
import com.sonicle.webtop.core.sdk.BaseManager;
import com.sonicle.webtop.core.bol.Owner;
import com.sonicle.webtop.core.model.Recipient;
import com.sonicle.webtop.core.model.IncomingShareRoot;
import com.sonicle.webtop.core.model.SharePermsFolder;
import com.sonicle.webtop.core.model.SharePermsElements;
import com.sonicle.webtop.core.model.SharePermsRoot;
import com.sonicle.webtop.core.bol.model.Sharing;
import com.sonicle.webtop.core.dal.BaseDAO;
import com.sonicle.webtop.core.dal.DAOException;
import com.sonicle.webtop.core.dal.DAOIntegrityViolationException;
import com.sonicle.webtop.core.model.BaseMasterData;
import com.sonicle.webtop.core.model.CustomFieldValue;
import com.sonicle.webtop.core.model.MasterData;
import com.sonicle.webtop.core.model.MasterDataLookup;
import com.sonicle.webtop.core.model.RecipientFieldCategory;
import com.sonicle.webtop.core.model.RecipientFieldType;
import com.sonicle.webtop.core.sdk.AbstractMapCache;
import com.sonicle.webtop.core.sdk.AbstractShareCache;
import com.sonicle.webtop.core.sdk.AuthException;
import com.sonicle.webtop.core.sdk.BaseReminder;
import com.sonicle.webtop.core.sdk.ReminderEmail;
import com.sonicle.webtop.core.sdk.ReminderInApp;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.sdk.WTRuntimeException;
import com.sonicle.webtop.core.sdk.interfaces.IRecipientsProvidersSource;
import com.sonicle.webtop.core.util.NotificationHelper;
import com.sonicle.webtop.core.util.VCardUtils;
import eu.medsea.mimeutil.MimeType;
import ezvcard.VCard;
import freemarker.template.TemplateException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import jakarta.mail.internet.InternetAddress;
import net.sf.qualitycheck.Check;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.imgscalr.Scalr;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import com.sonicle.webtop.contacts.io.ContactFileReader;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author malbinola
 */
public class ContactsManager extends BaseManager implements IContactsManager, IRecipientsProvidersSource {
	private static final Logger logger = WT.getLogger(ContactsManager.class);
	private static final String GROUPNAME_CATEGORY = "CATEGORY";
	
	public final boolean VCARD_CARETENCODINGENABLED;
	private final OwnerCache ownerCache = new OwnerCache();
	private final ShareCache shareCache = new ShareCache();
	private final KeyedReentrantLocks<String> locks = new KeyedReentrantLocks<>();
	private static final ConcurrentHashMap<String, UserProfileId> pendingRemoteCategorySyncs = new ConcurrentHashMap<>();
	
	public final MailchimpProduct MAILCHIMP_PRODUCT;
	private boolean hasMailchimp=false;
	
	public ContactsManager(boolean fastInit, UserProfileId targetProfileId) {
		super(fastInit, targetProfileId);
		VCARD_CARETENCODINGENABLED = ContactsProps.getVCardWriterCaretEncodingEnabled(WT.getProperties());
		if (!fastInit) {
			shareCache.init();
		}
		
		// targetProfile can be null in case of public context where 
		// we have no logged user. So check it!
		//TODO: evaluate whether to create a dedicated dummy user for this (eg. wt-public@domain, ...)
		if (!RunContext.isSysAdmin() && targetProfileId != null) {
			MAILCHIMP_PRODUCT = new MailchimpProduct(targetProfileId.getDomainId());
			hasMailchimp = WT.isLicensed(MAILCHIMP_PRODUCT) && WT.isLicensed(MAILCHIMP_PRODUCT, targetProfileId.getUserId()) > 0;
		} else {
			MAILCHIMP_PRODUCT = null;
		}
	}
	
	private CoreManager getCoreManager() {
		return WT.getCoreManager(getTargetProfileId());
	}
	
	private ContactsServiceSettings getServiceSettings() {
		return new ContactsServiceSettings(SERVICE_ID, getTargetProfileId().getDomainId());
	}
	
	private ContactsUserSettings getUserSettings() {
		return new ContactsUserSettings(SERVICE_ID, getTargetProfileId());
	}
	
	private CardDav getCardDav(String username, String password) {
		if (!StringUtils.isBlank(username) && !StringUtils.isBlank(username)) {
			return CardDavFactory.begin(username, password);
		} else {
			return CardDavFactory.begin();
		}
	}
	
	private List<ShareRootCategory> internalListIncomingCategoryShareRoots() throws WTException {
		CoreManager coreMgr = getCoreManager();
		List<ShareRootCategory> roots = new ArrayList();
		HashSet<String> hs = new HashSet<>();
		for (IncomingShareRoot share : coreMgr.listIncomingShareRoots(SERVICE_ID, GROUPNAME_CATEGORY)) {
			final SharePermsRoot perms = coreMgr.getShareRootPermissions(share.getShareId());
			ShareRootCategory root = new ShareRootCategory(share, perms);
			if (hs.contains(root.getShareId())) continue; // Avoid duplicates ??????????????????????
			hs.add(root.getShareId());
			roots.add(root);
		}
		return roots;
	}
	
	@Override
	public List<RecipientsProviderBase> returnRecipientsProviders() {
		try {
			ArrayList<RecipientsProviderBase> providers = new ArrayList<>();
			UserProfile.Data ud = WT.getUserData(getTargetProfileId());
			providers.add(new RootRecipientsProvider(getTargetProfileId().toString(), ud.getDisplayName(), getTargetProfileId(), listCategoryIds()));
			for(ShareRootCategory root : shareCache.getShareRoots()) {
				final List<Integer> catIds = shareCache.getFolderIdsByShareRoot(root.getShareId());
				providers.add(new RootRecipientsProvider(root.getOwnerProfileId().toString(), root.getDescription(), root.getOwnerProfileId(), catIds));
			}
			return providers;
		} catch(WTException ex) {
			logger.error("Unable to return providers");
			return null;
		}
	}
	
	private String getAnniversaryReminderDelivery(HashMap<UserProfileId, String> cache, UserProfileId pid) {
		if (!cache.containsKey(pid)) {
			ContactsUserSettings cus = new ContactsUserSettings(SERVICE_ID, pid);
			String value = cus.getAnniversaryReminderDelivery();
			cache.put(pid, value);
			return value;
		} else {
			return cache.get(pid);
		}
	}
	
	private DateTime getAnniversaryReminderTime(HashMap<UserProfileId, DateTime> cache, UserProfileId pid, LocalDate date) {
		if(!cache.containsKey(pid)) {
			LocalTime time = new ContactsUserSettings(SERVICE_ID, pid).getAnniversaryReminderTime();
			//TODO: valutare se uniformare i minuti a quelli consentiti (ai min 0 e 30), se errato non verrà mai preso in considerazione
			UserProfile.Data ud = WT.getUserData(pid);
			DateTime value = new DateTime(ud.getTimeZone()).withDate(date).withTime(time);
			cache.put(pid, value);
			return value;
		} else {
			return cache.get(pid);
		}
	}
	
	public String buildSharingId(int categoryId) throws WTException {
		UserProfileId targetPid = getTargetProfileId();
		
		// Skip rights check if running user is resource's owner
		UserProfileId owner = ownerCache.get(categoryId);
		if (owner == null) throw new WTException("owner({0}) -> null", categoryId);
		
		String rootShareId = null;
		if (owner.equals(targetPid)) {
			rootShareId = MyShareRootCategory.SHARE_ID;
		} else {
			rootShareId = shareCache.getShareRootIdByFolderId(categoryId);
		}
		if (rootShareId == null) throw new WTException("Unable to find a root share [{0}]", categoryId);
		return new CompositeId().setTokens(rootShareId, categoryId).toString();
	}
	
	public Sharing getSharing(String shareId) throws WTException {
		CoreManager coreMgr = getCoreManager();
		return coreMgr.getSharing(SERVICE_ID, GROUPNAME_CATEGORY, shareId);
	}
	
	public void updateSharing(Sharing sharing) throws WTException {
		CoreManager coreMgr = getCoreManager();
		coreMgr.updateSharing(SERVICE_ID, GROUPNAME_CATEGORY, sharing);
	}
	
	public UserProfileId getCategoryOwner(int categoryId) throws WTException {
		return ownerCache.get(categoryId);
	}
	
	public String getIncomingCategoryShareRootId(int categoryId) throws WTException {
		return shareCache.getShareRootIdByFolderId(categoryId);
	}
	
	@Override
	public List<ShareRootCategory> listIncomingCategoryRoots() throws WTException {
		return shareCache.getShareRoots();
	}
	
	@Override
	public Map<Integer, ShareFolderCategory> listIncomingCategoryFolders(String rootShareId) throws WTException {
		CoreManager coreMgr = getCoreManager();
		LinkedHashMap<Integer, ShareFolderCategory> folders = new LinkedHashMap<>();
		
		for (Integer folderId : shareCache.getFolderIdsByShareRoot(rootShareId)) {
			final String shareFolderId = shareCache.getShareFolderIdByFolderId(folderId);
			if (StringUtils.isBlank(shareFolderId)) continue;
			SharePermsFolder fperms = coreMgr.getShareFolderPermissions(shareFolderId);
			SharePermsElements eperms = coreMgr.getShareElementsPermissions(shareFolderId);
			if (folders.containsKey(folderId)) {
				final ShareFolderCategory shareFolder = folders.get(folderId);
				if (shareFolder == null) continue;
				shareFolder.getPerms().merge(fperms);
				shareFolder.getElementsPerms().merge(eperms);
			} else {
				final Category category = getCategory(folderId);
				if (category == null) continue;
				folders.put(folderId, new ShareFolderCategory(shareFolderId, fperms, eperms, category));
			}
		}
		return folders;
	}
	
	@Override
	public Set<Integer> listCategoryIds() throws WTException {
		return listCategoryIds(getTargetProfileId());
	}
	
	@Override
	public Set<Integer> listIncomingCategoryIds() throws WTException {
		return shareCache.getFolderIds();
	}
	
	@Override
	public Set<Integer> listIncomingCategoryIds(final UserProfileId owner) throws WTException {
		if (owner == null) {
			return listIncomingCategoryIds();
		} else {
			String rootId = shareCache.getShareRootIdByOwner(owner);
			if (rootId == null) return null;
			return shareCache.getFolderIds().stream()
				.filter(categoryId -> rootId.equals(shareCache.getShareRootIdByFolderId(categoryId)))
				.collect(Collectors.toCollection(LinkedHashSet::new));
		}
	}
	
	@Override
	public Set<Integer> listAllCategoryIds() throws WTException {
		return Stream.concat(listCategoryIds().stream(), listIncomingCategoryIds().stream())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	@Override
	public Map<Integer, Category> listCategories() throws WTException {
		return listCategories(getTargetProfileId());
	}
	
	@Override
	public Map<Integer, Category> listIncomingCategories() throws WTException {
		return listIncomingCategories(null);
	}
	
	@Override
	public Map<Integer, Category> listIncomingCategories(final UserProfileId owner) throws WTException {
		Set<Integer> ids = listIncomingCategoryIds(owner);
		if (ids == null) return null;
		
		CategoryDAO catDao = CategoryDAO.getInstance();
		LinkedHashMap<Integer, Category> items = new LinkedHashMap<>();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			for (OCategory ocat : catDao.selectByDomainIn(con, getTargetProfileId().getDomainId(), ids)) {
				items.put(ocat.getCategoryId(), ManagerUtils.createCategory(ocat));
			}
			return items;
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private Set<Integer> listCategoryIds(UserProfileId pid) throws WTException {
		return listCategoryIdsIn(pid, null);
	}
	
	private Set<Integer> listCategoryIdsIn(UserProfileId pid, Collection<Integer> categoryIds) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			if (categoryIds == null) {
				return catDao.selectIdsByProfile(con, pid.getDomainId(), pid.getUserId());
			} else {
				return catDao.selectIdsByProfileIn(con, pid.getDomainId(), pid.getUserId(), categoryIds);
			}
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private Map<Integer, Category> listCategories(UserProfileId pid) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		LinkedHashMap<Integer, Category> items = new LinkedHashMap<>();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			for (OCategory ocat : catDao.selectByProfile(con, pid.getDomainId(), pid.getUserId())) {
				items.put(ocat.getCategoryId(), ManagerUtils.createCategory(ocat));
			}
			return items;
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public List<Category> listRemoteCategoriesToBeSynchronized() throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		ArrayList<Category> items = new ArrayList<>();
		Connection con = null;
		
		try {
			ensureSysAdmin();
			con = WT.getConnection(SERVICE_ID);
			for (OCategory ocat : catDao.selectByProvider(con, Arrays.asList(Category.Provider.CARDDAV))) {
				items.add(ManagerUtils.createCategory(ocat));
			}
			return items;
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Map<Integer, DateTime> getCategoriesLastRevision(Collection<Integer> categoryIds) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			List<Integer> okCategoryIds = categoryIds.stream()
					.filter(categoryId -> quietlyCheckRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ"))
					.collect(Collectors.toList());
			
			con = WT.getConnection(SERVICE_ID);
			return contDao.selectMaxRevTimestampByCategoriesType(con, okCategoryIds, false);
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Integer getDefaultCategoryId() throws WTException {
		ContactsUserSettings us = new ContactsUserSettings(SERVICE_ID, getTargetProfileId());
		
		Integer categoryId = null;
		try {
			locks.tryLock("getDefaultCategoryId", 60, TimeUnit.SECONDS);
			categoryId = us.getDefaultCategoryFolder();
			if (categoryId == null || !quietlyCheckRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "CREATE")) {
				try {
					categoryId = getBuiltInCategoryId();
					if (categoryId == null) throw new WTException("Built-in category is null");
					us.setDefaultCategoryFolder(categoryId);
				} catch (Throwable t) {
					logger.error("Unable to get built-in category", t);
				}
			}
		} catch (InterruptedException ex) {
			// Do nothing...
		} finally {
			locks.unlock("getDefaultCategoryId");
		}
		return categoryId;
	}
	
	@Override
	public Integer getBuiltInCategoryId() throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			Integer catId = catDao.selectBuiltInIdByProfile(con, getTargetProfileId().getDomainId(), getTargetProfileId().getUserId());
			if (catId == null) return null;
			
			checkRightsOnCategory(catId, CheckRightsTarget.FOLDER, "READ");
			
			return catId;
			
		} catch(SQLException | DAOException | WTException ex) {
			throw wrapException(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Category getCategory(int categoryId) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ");
			
			con = WT.getConnection(SERVICE_ID);
			OCategory ocat = catDao.selectById(con, categoryId);
			return ManagerUtils.createCategory(ocat);
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Category getBuiltInCategory() throws WTException {
		CategoryDAO catdao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			OCategory ocat = catdao.selectBuiltInByProfile(con, getTargetProfileId().getDomainId(), getTargetProfileId().getUserId());
			if (ocat == null) return null;
			checkRightsOnCategory(ocat.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
			
			return ManagerUtils.createCategory(ocat);
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public Map<String, String> getCategoryLinks(int categoryId) throws WTException {
		checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ");
		
		UserProfile.Data ud = WT.getUserData(getTargetProfileId());
		String davServerBaseUrl = WT.getDavServerBaseUrl(getTargetProfileId().getDomainId());
		String categoryUid = ContactsUtils.encodeAsCategoryUid(categoryId);
		String addressbookUrl = MessageFormat.format(ContactsUtils.CARDDAV_ADDRESSBOOK_URL, ud.getProfileEmailAddress(), categoryUid);
		
		LinkedHashMap<String, String> links = new LinkedHashMap<>();
		links.put(ContactsUtils.CATEGORY_LINK_CARDDAV, PathUtils.concatPathParts(davServerBaseUrl, addressbookUrl));
		return links;
	}
	
	@Override
	public Category addCategory(Category category) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryRoot(category.getProfileId(), "MANAGE");
			
			con = WT.getConnection(SERVICE_ID, false);
			category.setBuiltIn(false);
			category = doCategoryInsert(con, category);
			
			DbUtils.commitQuietly(con);
			onAfterCategoryAction(category.getCategoryId(), category.getProfileId());
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CATEGORY, AuditAction.CREATE, category.getCategoryId(), null);
			}
			
			return category;
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Category addBuiltInCategory() throws WTException {
		CategoryDAO catdao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategoryRoot(getTargetProfileId(), "MANAGE");
			
			con = WT.getConnection(SERVICE_ID, false);
			OCategory ocat = catdao.selectBuiltInByProfile(con, getTargetProfileId().getDomainId(), getTargetProfileId().getUserId());
			if (ocat != null) {
				logger.debug("Built-in category already present");
				return null;
			}
			
			Category cat = new Category();
			cat.setBuiltIn(true);
			cat.setName(WT.getPlatformName());
			cat.setDescription("");
			cat = doCategoryInsert(con, cat);
			
			DbUtils.commitQuietly(con);
			onAfterCategoryAction(cat.getCategoryId(), cat.getProfileId());
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CATEGORY, AuditAction.CREATE, cat.getCategoryId(), null);
			}
			
			// Sets category as default
			ContactsUserSettings us = new ContactsUserSettings(SERVICE_ID, cat.getProfileId());
			us.setDefaultCategoryFolder(cat.getCategoryId());
			
			return cat;
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateCategory(Category category) throws WTException {
		Connection con = null;
		
		try {
			int categoryId = category.getCategoryId();
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "UPDATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			boolean ret = doCategoryUpdate(con, category);
			if (!ret) throw new WTNotFoundException("Category not found [{}]", categoryId);
			
			DbUtils.commitQuietly(con);
			onAfterCategoryAction(categoryId, category.getProfileId());
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CATEGORY, AuditAction.UPDATE, categoryId, null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public boolean deleteCategory(int categoryId) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		CategoryPropsDAO psetDao = CategoryPropsDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "DELETE");
			
			// Retrieve sharing status (for later)
			String sharingId = buildSharingId(categoryId);
			Sharing sharing = getSharing(sharingId);
			
			con = WT.getConnection(SERVICE_ID, false);
			Category cat = ManagerUtils.createCategory(catDao.selectById(con, categoryId));
			if (cat == null) throw new WTNotFoundException("Category not found [{}]", categoryId);
			
			int ret = catDao.deleteById(con, categoryId);
			psetDao.deleteByCategory(con, categoryId);
			doContactsDeleteByCategory(con, categoryId, !cat.isProviderRemote());
			
			// Cleanup sharing, if necessary
			if ((sharing != null) && !sharing.getRights().isEmpty()) {
				logger.debug("Removing {} active sharing [{}]", sharing.getRights().size(), sharing.getId());
				sharing.getRights().clear();
				updateSharing(sharing);
			}
			
			DbUtils.commitQuietly(con);
			onAfterCategoryAction(categoryId, cat.getProfileId());
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CATEGORY, AuditAction.DELETE, categoryId, null);
				// removed due to new audit implementation
				// writeAuditLog(AuditContext.CATEGORY, AuditAction.DELETE, "*", categoryId);
			}
			
			return ret == 1;
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public CategoryPropSet getCategoryCustomProps(int categoryId) throws WTException {
		return getCategoryCustomProps(getTargetProfileId(), categoryId);
	}
	
	private CategoryPropSet getCategoryCustomProps(UserProfileId profileId, int categoryId) throws WTException {
		CategoryPropsDAO psetDao = CategoryPropsDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			OCategoryPropSet opset = psetDao.selectByProfileCategory(con, profileId.getDomainId(), profileId.getUserId(), categoryId);
			return (opset == null) ? new CategoryPropSet() : ManagerUtils.createCategoryPropSet(opset);
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Map<Integer, CategoryPropSet> getCategoryCustomProps(Collection<Integer> categoryIds) throws WTException {
		return getCategoryCustomProps(getTargetProfileId(), categoryIds);
	}
	
	public Map<Integer, CategoryPropSet> getCategoryCustomProps(UserProfileId profileId, Collection<Integer> categoryIds) throws WTException {
		CategoryPropsDAO psetDao = CategoryPropsDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			LinkedHashMap<Integer, CategoryPropSet> psets = new LinkedHashMap<>(categoryIds.size());
			Map<Integer, OCategoryPropSet> map = psetDao.selectByProfileCategoryIn(con, profileId.getDomainId(), profileId.getUserId(), categoryIds);
			for (Integer categoryId : categoryIds) {
				OCategoryPropSet opset = map.get(categoryId);
				psets.put(categoryId, (opset == null) ? new CategoryPropSet() : ManagerUtils.createCategoryPropSet(opset));
			}
			return psets;
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public CategoryPropSet updateCategoryCustomProps(int categoryId, CategoryPropSet propertySet) throws WTException {
		ensureUser();
		return updateCategoryCustomProps(getTargetProfileId(), categoryId, propertySet);
	}
	
	private CategoryPropSet updateCategoryCustomProps(UserProfileId profileId, int categoryId, CategoryPropSet propertySet) throws WTException {
		CategoryPropsDAO psetDao = CategoryPropsDAO.getInstance();
		Connection con = null;
		
		try {
			OCategoryPropSet opset = ManagerUtils.createOCategoryPropSet(propertySet);
			opset.setDomainId(profileId.getDomainId());
			opset.setUserId(profileId.getUserId());
			opset.setCategoryId(categoryId);
			
			con = WT.getConnection(SERVICE_ID);
			try {
				psetDao.insert(con, opset);
			} catch(DAOIntegrityViolationException ex1) {
				psetDao.update(con, opset);
			}
			return propertySet;
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public List<ContactObject> listContactObjects(final int categoryId, final ContactObjectOutputType outputType) throws WTException {
		CoreManager coreMgr = getCoreManager();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ");
			Map<String, String> tagNamesByIdMap = coreMgr.listTagNamesById();
			
			con = WT.getConnection(SERVICE_ID);
			ArrayList<ContactObject> items = new ArrayList<>();
			Map<String, List<VContactObject>> map = contDao.viewOnlineContactObjectsByCategory(con, ContactObjectOutputType.STAT.equals(outputType), categoryId);
			for (List<VContactObject> vcobjs : map.values()) {
				if (vcobjs.isEmpty()) continue;
				VContactObject vcobj = vcobjs.get(vcobjs.size()-1);
				if (vcobjs.size() > 1) {
					logger.trace("Many contacts ({}) found for same href [{} -> {}]", vcobjs.size(), vcobj.getHref(), vcobj.getContactId());
				}
				
				items.add(doContactObjectPrepare(con, vcobj, outputType, tagNamesByIdMap));
			}
			return items;
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public CollectionChangeSet<ContactObjectChanged> listContactObjectsChanges(final int categoryId, final DateTime since, final Integer limit) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			Integer myLimit = limit == null ? Integer.MAX_VALUE : limit;
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ");
			
			con = WT.getConnection(SERVICE_ID);
			ArrayList<ContactObjectChanged> inserted = new ArrayList<>();
			ArrayList<ContactObjectChanged> updated = new ArrayList<>();
			ArrayList<ContactObjectChanged> deleted = new ArrayList<>();
			
			if (since == null) {
				List<VContactObjectChanged> vcobjs = contDao.viewOnlineContactObjectsChangedByCategory(con, categoryId, myLimit);
				for (VContactObjectChanged vcobj : vcobjs) {
					inserted.add(new ContactObjectChanged(vcobj.getContactId(), vcobj.getRevisionTimestamp(), vcobj.getHref()));
				}
			} else {
				List<VContactObjectChanged> vcobjs = contDao.viewContactObjectsChangedByCategorySince(con, categoryId, since, myLimit);
				for (VContactObjectChanged vcobj : vcobjs) {
					ContactBase.RevisionStatus revStatus = EnumUtils.forSerializedName(vcobj.getRevisionStatus(), ContactBase.RevisionStatus.class);
					if (ContactBase.RevisionStatus.DELETED.equals(revStatus)) {
						deleted.add(new ContactObjectChanged(vcobj.getContactId(), vcobj.getRevisionTimestamp(), vcobj.getHref()));
					} else {
						if (ContactBase.RevisionStatus.NEW.equals(revStatus) || (vcobj.getCreationTimestamp().compareTo(since) >= 0)) {
							inserted.add(new ContactObjectChanged(vcobj.getContactId(), vcobj.getRevisionTimestamp(), vcobj.getHref()));
						} else {
							updated.add(new ContactObjectChanged(vcobj.getContactId(), vcobj.getRevisionTimestamp(), vcobj.getHref()));
						}
					}
				}
			}
			
			return new CollectionChangeSet<>(inserted, updated, deleted);
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	/*
	@Override
	public ContactObjectWithVCard getContactObjectWithVCard(int categoryId, String href) throws WTException {
		List<ContactObjectWithVCard> ccs = getContactObjectsWithVCard(categoryId, Arrays.asList(href));
		return ccs.isEmpty() ? null : ccs.get(0);
	}
	
	@Override
	public List<ContactObjectWithVCard> getContactObjectsWithVCard(int categoryId, Collection<String> hrefs) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ");
			
			ArrayList<ContactObjectWithVCard> items = new ArrayList<>();
			Map<String, List<VContactObject>> map = contDao.viewContactObjectByCategoryHrefs(con, categoryId, hrefs);
			for (String href : hrefs) {
				List<VContactObject> vconts = map.get(href);
				if (vconts == null) continue;
				if (vconts.isEmpty()) continue;
				VContactObject vcont = vconts.get(vconts.size()-1);
				if (vconts.size() > 1) {
					logger.trace("Many contacts ({}) found for same href [{} -> {}]", vconts.size(), vcont.getHref(), vcont.getContactId());
				}
				
				items.add((ContactObjectWithVCard)doContactObjectPrepare(con, vcont, ContactObjectOutputType.VCARD));
			}
			return items;
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	*/
	
	@Override
	public ContactObject getContactObject(final int categoryId, final String href, final ContactObjectOutputType outputType) throws WTException {
		List<ContactObject> ccs = getContactObjects(categoryId, Arrays.asList(href), outputType);
		return ccs.isEmpty() ? null : ccs.get(0);
	}
	
	@Override
	public List<ContactObject> getContactObjects(final int categoryId, final Collection<String> hrefs, final ContactObjectOutputType outputType) throws WTException {
		CoreManager coreMgr = getCoreManager();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ");
			con = WT.getConnection(SERVICE_ID);
			
			Map<String, String> tagNamesByIdMap = coreMgr.listTagNamesById();
			ArrayList<ContactObject> items = new ArrayList<>();
			Map<String, List<VContactObject>> map = contDao.viewOnlineContactObjectsByCategoryHrefs(con, false, categoryId, hrefs);
			for (String href : hrefs) {
				List<VContactObject> vcobjs = map.get(href);
				if (vcobjs == null) continue;
				if (vcobjs.isEmpty()) continue;
				VContactObject vcobj = vcobjs.get(vcobjs.size()-1);
				if (vcobjs.size() > 1) {
					logger.trace("Many contacts ({}) found for same href [{} -> {}]", vcobjs.size(), vcobj.getHref(), vcobj.getContactId());
				}
				
				items.add(doContactObjectPrepare(con, vcobj, outputType, tagNamesByIdMap));
			}
			return items;
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactObject getContactObject(final int contactId, final ContactObjectOutputType outputType) throws WTException {
		CoreManager coreMgr = getCoreManager();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			VContactObject vcobj = contDao.viewContactObjectById(con, contactId);
			if (vcobj == null) {
				return null;
			} else {
				checkRightsOnCategory(vcobj.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
				
				Map<String, String> tagNamesByIdMap = coreMgr.listTagNamesById();
				return doContactObjectPrepare(con, vcobj, outputType, tagNamesByIdMap);
			}
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void addContactObject(final int categoryId, final String href, final VCard vCard) throws WTException {
		CoreManager coreMgr = getCoreManager();
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "CREATE");
			
			VCardInput in = new VCardInput()
					.withReturnSourceObject(true);
			ContactInput input = in.parseCardObject(vCard);
			input.contact.setCategoryId(categoryId);
			input.contact.setHref(href);
			
			Map<String, List<String>> tagIdsByNameMap = coreMgr.listTagIdsByName();
			
			con = WT.getConnection(SERVICE_ID, false);
			BitFlag<ContactProcessOpts> processOpts = BitFlag.of(ContactProcessOpts.PICTURE, ContactProcessOpts.RAW_VCARD);
			ContactInsertResult result = doContactInputInsert(coreMgr, con, tagIdsByNameMap, input, processOpts);
			Integer newContactId = result.ocontact.getContactId();
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.CREATE, newContactId, null);
			}
			
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
		
		/*
		VCardInput in = new VCardInput();
		ContactInput ci = in.fromVCard(vCard, null);
		ci.contact.setCategoryId(categoryId);
		ci.contact.setHref(href);
		
		String rawData = null;
		if (vCard != null) {
			String prodId = VCardUtils.buildProdId(ManagerUtils.getProductName());
			rawData = new VCardOutput(prodId)
					.withEnableCaretEncoding(VCARD_CARETENCODINGENABLED)
					.write(vCard);
		}
		
		addContact(ci.contact, rawData);
		*/
	}
	
	@Override
	public void updateContactObject(final int categoryId, final String href, final VCard vCard) throws WTException {
		CoreManager coreMgr = getCoreManager();
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "UPDATE");
			
			VCardInput in = new VCardInput()
					.withReturnSourceObject(true);
			ContactInput input = in.parseCardObject(vCard);
			input.contact.setCategoryId(categoryId);
			input.contact.setHref(href);
			
			Map<String, List<String>> tagIdsByNameMap = coreMgr.listTagIdsByName();
			
			con = WT.getConnection(SERVICE_ID, false);
			Integer contactId = doGetContactIdByCategoryHref(con, categoryId, href, true);
			BitFlag<ContactProcessOpts> processOpts = BitFlag.of(ContactProcessOpts.PICTURE, ContactProcessOpts.RAW_VCARD);
			boolean ret = doContactInputUpdate(coreMgr, con, tagIdsByNameMap, contactId, input, processOpts);
			if (!ret) throw new WTNotFoundException("Contact not found [{}]", contactId);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, contactId, null);
			}
			
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
		
		/*
		int contactId = getContactIdByCategoryHref(categoryId, href, true);
		
		VCardInput in = new VCardInput();
		ContactInput ci = in.fromVCard(vCard, null);
		ci.contact.setContactId(contactId);
		ci.contact.setCategoryId(categoryId);
		
		// Due that in vcard we do not have separate information for company ID 
		// and description, in order to not lose data during the update, we have
		// to apply some checks and then restore the original company in  
		// particular cases:
		// - value as ID matches the company raw id
		// - value as DESCRIPTION matched the company linked description
		if (ci.contact.hasCompany()) {
			ContactCompany origCompany = getContactCompany(contactId);
			if (origCompany != null) {
				ContactCompany currCompany = ci.contact.getCompany();
				if (StringUtils.equals(currCompany.getValueId(), origCompany.getCompanyId()) || StringUtils.equals(currCompany.getCompanyDescription(), origCompany.getValue())) {
					ci.contact.setCompany(origCompany);
				}
			}
		}
		
		updateContact(ci.contact, true);
		*/
	}
	
	@Override
	public void deleteContactObject(final int categoryId, final String href) throws WTException {
		Connection con = null;
		Integer contactId = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			contactId = doGetContactIdByCategoryHref(con, categoryId, href, true);
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
		
		deleteContact(contactId);
	}
	
	private Integer doGetContactIdByCategoryHref(Connection con, int categoryId, String href, boolean throwExIfManyMatchesFound) throws WTException {
		ContactDAO conDao = ContactDAO.getInstance();
		
		List<Integer> ids = conDao.selectAliveIdsByCategoryHrefs(con, categoryId, href);
		if (ids.isEmpty()) throw new WTNotFoundException("Contact not found [{}, {}]", categoryId, href);
		if (throwExIfManyMatchesFound && (ids.size() > 1)) throw new WTException("Many matches for href [{}]", href);
		return ids.get(ids.size()-1);
	}
	
	@Override
	public boolean existContact(Collection<Integer> categoryIds, Condition<ContactQuery> conditionPredicate) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			List<Integer> okCategoryIds = categoryIds.stream()
					.filter(categoryId -> quietlyCheckRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ"))
					.collect(Collectors.toList());
			
			org.jooq.Condition condition = BaseDAO.createCondition(conditionPredicate, new ContactPredicateVisitor()
					.withIgnoreCase(true)
					.withForceStringLikeComparison(true)
			);
			
			con = WT.getConnection(SERVICE_ID);
			return contDao.existByCategoryTypeCondition(con, okCategoryIds, ContactType.CONTACT, condition);
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ListContactsResult listContacts(Collection<Integer> categoryIds, ContactType type, Grouping groupBy, ShowBy showBy, String pattern) throws WTException {
		return listContacts(categoryIds, type, groupBy, showBy, ContactQuery.createCondition(pattern), 1, Integer.MAX_VALUE, false);
	}
	
	@Override
	public ListContactsResult listContacts(Collection<Integer> categoryIds, ContactType type, Grouping groupBy, ShowBy showBy, Condition<ContactQuery> conditionPredicate) throws WTException {
		return listContacts(categoryIds, type, groupBy, showBy, conditionPredicate, 1, Integer.MAX_VALUE, false);
	}
	
	@Override
	public ListContactsResult listContacts(Collection<Integer> categoryIds, ContactType type, Grouping groupBy, ShowBy showBy, Condition<ContactQuery> conditionPredicate, int page, int limit, boolean returnFullCount) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			List<Integer> okCategoryIds = categoryIds.stream()
					.filter(categoryId -> quietlyCheckRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ"))
					.collect(Collectors.toList());
			
			org.jooq.Condition condition = BaseDAO.createCondition(conditionPredicate, new ContactPredicateVisitor()
					.withIgnoreCase(true)
					.withForceStringLikeComparison(true)
			);
			int offset = ManagerUtils.toOffset(page, limit);
			Collection<ContactDAO.OrderField> orderFields = toContactDAOOrderFields(groupBy, showBy);
			
			con = WT.getConnection(SERVICE_ID);
			Integer fullCount = null;
			if (returnFullCount) fullCount = contDao.countByCategoryTypeCondition(con, okCategoryIds, type, condition);
			ArrayList<ContactLookup> items = new ArrayList<>();
			for (VContactLookup vcont : contDao.viewByCategoryTypeCondition(con, orderFields, okCategoryIds, type, condition, limit, offset)) {
				items.add(ManagerUtils.fillContactLookup(new ContactLookup(), vcont));
			}
			
			return new ListContactsResult(items, fullCount);
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private Collection<ContactDAO.OrderField> toContactDAOOrderFields(Grouping groupBy, ShowBy showBy) {
		ArrayList<ContactDAO.OrderField> fields = new ArrayList<>(3);
		if (ShowBy.FIRST_LAST.equals(showBy)) {
			fields.add(ContactDAO.OrderField.FIRSTNAME);
			fields.add(ContactDAO.OrderField.LASTNAME);
		} else if (ShowBy.LAST_FIRST.equals(showBy)) {
			fields.add(ContactDAO.OrderField.LASTNAME);
			fields.add(ContactDAO.OrderField.FIRSTNAME);
		} else {
			fields.add(ContactDAO.OrderField.DISPLAYNAME);
		}
		if (Grouping.COMPANY.equals(groupBy)) {
			fields.add(0, ContactDAO.OrderField.COMPANY);
		} else {
			fields.add(ContactDAO.OrderField.COMPANY);
		}
		return fields;
	}
	
	@Override
	public Contact getContact(final int contactId) throws WTException {
		return getContact(contactId, BitFlag.of(ContactGetOptions.PICTURE, ContactGetOptions.ATTACHMENTS, ContactGetOptions.TAGS, ContactGetOptions.CUSTOM_VALUES));
	}
	
	@Override
	public Contact getContact(final int contactId, final BitFlag<ContactGetOptions> options) throws WTException {
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			BitFlag<ContactProcessOpts> options2 = ContactProcessOpts.parseContactGetOptions(options);
			Contact contact = doContactGet(con, contactId, options2);
			if (contact == null) return null;
			checkRightsOnCategory(contact.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
			
			return contact;
		
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactPictureWithBytes getContactPicture(final int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			Integer catId = contDao.selectCategoryId(con, contactId);
			if (catId == null) return null;
			checkRightsOnCategory(catId, CheckRightsTarget.FOLDER, "READ");
			
			OContactPicture opic = cpicDao.select(con, contactId);
			if (opic == null) return null;
			return ManagerUtils.fillContactPicture(new ContactPictureWithBytes(opic.getBytes()), opic);
		
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactCompany getContactCompany(final int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			VContactCompany vcc = contDao.viewContactCompanyByContact(con, contactId);
			if (vcc == null) return null;
			checkRightsOnCategory(vcc.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
			
			return ManagerUtils.createContactCompany(vcc);
		
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactAttachmentWithBytes getContactAttachment(final int contactId, final String attachmentId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactAttachmentDAO attDao = ContactAttachmentDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			Integer catId = contDao.selectCategoryId(con, contactId);
			if (catId == null) return null;
			checkRightsOnCategory(catId, CheckRightsTarget.FOLDER, "READ");
			
			OContactAttachment oatt = attDao.selectByIdContact(con, attachmentId, contactId);
			if (oatt == null) return null;
			
			OContactAttachmentData oattData = attDao.selectBytesById(con, attachmentId);
			return ManagerUtils.fillContactAttachment(new ContactAttachmentWithBytes(oattData.getBytes()), oatt);
		
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Map<String, CustomFieldValue> getContactCustomValues(final int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactCustomValueDAO cvalDao = ContactCustomValueDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			Integer catId = contDao.selectCategoryId(con, contactId);
			if (catId == null) return null;
			checkRightsOnCategory(catId, CheckRightsTarget.FOLDER, "READ");
			
			List<OContactCustomValue> ovals = cvalDao.selectByContact(con, contactId);
			return ManagerUtils.createCustomValuesMap(ovals);
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Contact addContact(final ContactEx contact) throws WTException {
		return addContact(contact, null);
	}
	
	@Override
	public Contact addContact(final ContactEx contact, final String vCardRawData) throws WTException {
		CoreManager coreMgr = getCoreManager();
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(contact.getCategoryId(), CheckRightsTarget.ELEMENTS, "CREATE");
			con = WT.getConnection(SERVICE_ID, false);
			
			String provider = catDao.selectProviderById(con, contact.getCategoryId());
			if (Category.isProviderRemote(provider)) throw new WTException("Category is remote and therefore read-only [{}]", contact.getCategoryId());
			
			BitFlag<ContactProcessOpts> processOpts = BitFlag.of(ContactProcessOpts.PICTURE, ContactProcessOpts.ATTACHMENTS, ContactProcessOpts.TAGS, ContactProcessOpts.CUSTOM_VALUES);
			if (!StringUtils.isBlank(vCardRawData)) processOpts.set(ContactProcessOpts.RAW_VCARD);
			ContactInsertResult result = doContactInsert(coreMgr, con, false, contact, vCardRawData, processOpts);
			Integer newContactId = result.ocontact.getContactId();
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.CREATE, newContactId, null);
			}
			
			return doContactGet(con, newContactId, BitFlag.of(ContactProcessOpts.PICTURE, ContactProcessOpts.ATTACHMENTS, ContactProcessOpts.TAGS, ContactProcessOpts.CUSTOM_VALUES));
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContact(final int contactId, final ContactEx contact) throws WTException {
		updateContact(contactId, contact, BitFlag.of(ContactUpdateOptions.PICTURE, ContactUpdateOptions.ATTACHMENTS, ContactUpdateOptions.TAGS, ContactUpdateOptions.CUSTOM_VALUES));
	}
	
	@Override
	public void updateContact(final int contactId, final ContactEx contact, final BitFlag<ContactUpdateOptions> opts) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		CategoryDAO catDao = CategoryDAO.getInstance();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			Integer catId = contDao.selectCategoryId(con, contactId);
			if (catId == null) throw new WTNotFoundException("Contact category not found [{}]", contactId);
			
			checkRightsOnCategory(catId, CheckRightsTarget.ELEMENTS, "UPDATE");
			String provider = catDao.selectProviderById(con, catId);
			if (Category.isProviderRemote(provider)) throw new WTException("Category is remote and therefore read-only [{}]", contact.getCategoryId());
			
			OContact ret = doContactUpdate(coreMgr, con, false, contactId, contact, null, ContactProcessOpts.parseContactUpdateOptions(opts).unset(ContactProcessOpts.RAW_VCARD));
			if (ret == null) throw new WTNotFoundException("Contact not found [{}]", contactId);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, contactId, null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactPicture(final int contactId, final ContactPictureWithBytes picture) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			if (picture == null) throw new WTException("Picture is null");
			con = WT.getConnection(SERVICE_ID, false);
			
			OContactInfo ocoinfo = contDao.selectOnlineContactInfoById(con, contactId);
			if (ocoinfo == null || ocoinfo.getIsList()) throw new WTNotFoundException("Contact not found [{}]", contactId);
			checkRightsOnCategory(ocoinfo.getCategoryId(), CheckRightsTarget.ELEMENTS, "UPDATE");
			
			contDao.updateRevision(con, contactId, BaseDAO.createRevisionTimestamp());
			doContactPictureUpdate(con, contactId, picture);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, contactId, null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteContact(final int contactId) throws WTException {
		deleteContact(Arrays.asList(contactId));
	}
	
	@Override
	public void deleteContact(final Collection<Integer> contactIds) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			if (contactIds.size() == 1) {
				Integer contactId = contactIds.iterator().next();
				con = WT.getConnection(SERVICE_ID, false);
				
				OContactInfo ocoinfo = contDao.selectOnlineContactInfoById(con, contactId);
				if (ocoinfo == null) throw new WTNotFoundException("Contact not found [{}]", contactId);
				//if (ocoinfo == null || ocoinfo.getIsList()) throw new WTNotFoundException("Contact not found [{}]", contactId);
				checkRightsOnCategory(ocoinfo.getCategoryId(), CheckRightsTarget.ELEMENTS, "DELETE");
				
				boolean ret = doContactDelete(con, contactId, true);
				if (!ret) throw new WTNotFoundException("Contact not found [{}]", contactId);
				
				DbUtils.commitQuietly(con);
				if (isAuditEnabled()) {
					writeAuditLog(AuditContext.CONTACT, AuditAction.DELETE, contactId, null);
				}
			
			} else {
				con = WT.getConnection(SERVICE_ID, false);
				
				// Collect necessary data
				Map<Integer, Integer> map = contDao.selectCategoriesByIds(con, contactIds);
				//Map<Integer, Integer> map = contDao.selectCategoriesByIdsType(con, contactIds, false);
				// Perform delete operation
				Set<Integer> deleteOkCache = new HashSet<>();
				ArrayList<AuditReferenceDataEntry> deleted = new ArrayList<>();
				for (Integer contactId : contactIds) {
					if (contactId == null) continue;
					if (!map.containsKey(contactId)) throw new WTNotFoundException("Contact not found [{}]", contactId);
					checkRightsOnCategory(deleteOkCache, map.get(contactId), CheckRightsTarget.ELEMENTS, "DELETE");

					if (doContactDelete(con, contactId, true)) {
						deleted.add(new AuditContactObj(contactId));
					} else {
						throw new WTNotFoundException("Contact not found [{}]", contactId);
					}
				}

				DbUtils.commitQuietly(con);
				if (isAuditEnabled()) {
					writeAuditLog(AuditContext.CONTACT, AuditAction.DELETE, deleted);
				}
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void moveContact(final boolean copy, final int contactId, final int targetCategoryId) throws WTException {
		moveContact(copy, Arrays.asList(contactId), targetCategoryId);
	}
	
	@Override
	public void moveContact(final boolean copy, final int contactId, final int targetCategoryId, BitFlag<ContactGetOptions> opts) throws WTException {
		moveContact(copy, Arrays.asList(contactId), targetCategoryId, opts);
	}
	
	@Override
	public void moveContact(final boolean copy, final Collection<Integer> contactIds, final int targetCategoryId) throws WTException {
		moveContact(copy, contactIds, targetCategoryId, BitFlag.of(ContactGetOptions.PICTURE, ContactGetOptions.TAGS, ContactGetOptions.CUSTOM_VALUES, ContactGetOptions.LIST_RECIPIENTS));
	}
	
	@Override
	public void moveContact(final boolean copy, final Collection<Integer> contactIds, final int targetCategoryId, BitFlag<ContactGetOptions> opts) throws WTException {
		CoreManager coreMgr = getCoreManager();
		CategoryDAO catDao = CategoryDAO.getInstance();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			BitFlag<ContactProcessOpts> processOpts = ContactProcessOpts.parseContactGetOptions(opts);
			checkRightsOnCategory(targetCategoryId, CheckRightsTarget.ELEMENTS, "CREATE");
			con = WT.getConnection(SERVICE_ID, false);
			
			String provider = catDao.selectProviderById(con, targetCategoryId);
			if (Category.isProviderRemote(provider)) throw new WTException("Category is remote and therefore read-only [{}]", targetCategoryId);
			
			// Collect necessary data
			Map<Integer, OContactInfo> map = contDao.selectOnlineContactInfoByIds(con, contactIds);
			
			Set<Integer> readOkCache = new HashSet<>();
			Set<Integer> deleteOkCache = new HashSet<>();
			ArrayList<AuditReferenceDataEntry> copied = new ArrayList<>();
			ArrayList<AuditReferenceDataEntry> moved = new ArrayList<>();
			for (Integer contactId : contactIds) {
				if (contactId == null) continue;
				if (!map.containsKey(contactId)) throw new WTNotFoundException("Contact not found [{}]", contactId);
				OContactInfo ocinfo = map.get(contactId);
				checkRightsOnCategory(readOkCache, ocinfo.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
				
				if (copy || (targetCategoryId != ocinfo.getCategoryId())) {
					if (copy) {
						if (!ocinfo.getIsList()) {
							Contact origContact = doContactGet(con, contactId, processOpts);
							if (origContact == null) throw new WTNotFoundException("Contact not found [{}]", contactId);
							ContactInsertResult result = doContactCopy(coreMgr, con, false, contactId, origContact, targetCategoryId, processOpts);
							
							copied.add(new AuditContactCopyObj(result.ocontact.getContactId(), origContact.getContactId()));
							
						} else {
							ContactList origContact = doContactListGet(con, contactId, processOpts);
							if (origContact == null) throw new WTNotFoundException("Contact not found [{}]", contactId);
							ContactsListInsertResult result = doContactListCopy(coreMgr, con, contactId, origContact, targetCategoryId, processOpts);
							
							copied.add(new AuditContactCopyObj(result.ocontact.getContactId(), origContact.getContactId()));
						}
						
					} else {
						checkRightsOnCategory(deleteOkCache, ocinfo.getCategoryId(), CheckRightsTarget.ELEMENTS, "DELETE");
						boolean ret = doContactMove(con, contactId, targetCategoryId);
						if (!ret) throw new WTNotFoundException("Contact not found [{}]", contactId);
						
						moved.add(new AuditContactMoveObj(contactId, ocinfo.getCategoryId()));
					}	
				}
			}
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				if (copy) {
					writeAuditLog(AuditContext.CONTACT, AuditAction.CREATE, copied);
				} else {
					writeAuditLog(AuditContext.CONTACT, AuditAction.MOVE, moved);
				}
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactList getContactList(final int contactId) throws WTException {
		return getContactList(contactId, BitFlag.of(ContactGetOptions.TAGS, ContactGetOptions.LIST_RECIPIENTS));
	}
	
	@Override
	public ContactList getContactList(final int contactId, final BitFlag<ContactGetOptions> options) throws WTException {
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			BitFlag<ContactProcessOpts> options2 = ContactProcessOpts.parseContactGetOptions(options);
			ContactList contact = doContactListGet(con, contactId, options2);
			if (contact == null) return null;
			checkRightsOnCategory(contact.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
			
			return contact;
		
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactList addContactList(final ContactListEx contact) throws WTException {
		CoreManager coreMgr = getCoreManager();
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(contact.getCategoryId(), CheckRightsTarget.ELEMENTS, "CREATE");
			con = WT.getConnection(SERVICE_ID, false);
			
			String provider = catDao.selectProviderById(con, contact.getCategoryId());
			if (Category.isProviderRemote(provider)) throw new WTException("Category is remote and therefore read-only [{}]", contact.getCategoryId());
			
			BitFlag<ContactProcessOpts> processOpts = BitFlag.of(ContactProcessOpts.TAGS, ContactProcessOpts.LIST_RECIPIENTS);
			ContactsListInsertResult result = doContactListInsert(coreMgr, con, contact, processOpts);
			Integer newContactId = result.ocontact.getContactId();
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.CREATE, newContactId, null);
			}
			
			return doContactListGet(con, newContactId, BitFlag.of(ContactProcessOpts.TAGS));
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactList(final int contactId, final ContactListEx contact) throws WTException {
		updateContactList(contactId, contact, BitFlag.of(ContactUpdateOptions.TAGS, ContactProcessOpts.LIST_RECIPIENTS));
	}
	
	@Override
	public void updateContactList(final int contactId, final ContactListEx contact, final BitFlag<ContactUpdateOptions> opts) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		CategoryDAO catDao = CategoryDAO.getInstance();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			Integer catId = contDao.selectCategoryId(con, contactId);
			if (catId == null) throw new WTNotFoundException("Contact category not found [{}]", contactId);
			
			checkRightsOnCategory(catId, CheckRightsTarget.ELEMENTS, "UPDATE");
			String provider = catDao.selectProviderById(con, catId);
			if (Category.isProviderRemote(provider)) throw new WTException("Category is remote and therefore read-only [{}]", contact.getCategoryId());
			
			OContact ocont = doContactListUpdate(coreMgr, con, contactId, contact, ContactProcessOpts.parseContactUpdateOptions(opts));
			if (ocont == null) throw new WTNotFoundException("Contacts-list not found [{}]", contactId);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, contactId, null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactsListRecipients(final int contactId, final Collection<ContactListRecipient> recipients, final boolean append) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			OContactInfo ocoinfo = contDao.selectOnlineContactInfoById(con, contactId);
			if (ocoinfo == null || !ocoinfo.getIsList()) throw new WTNotFoundException("Contact-List not found [{}]", contactId);
			checkRightsOnCategory(ocoinfo.getCategoryId(), CheckRightsTarget.ELEMENTS, "UPDATE");
			
			if (!append) lrecDao.deleteByContact(con, contactId);
			lrecDao.batchInsert(con, contactId, recipients);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, contactId, null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteContactList(final int contactId) throws WTException {
		deleteContact(Arrays.asList(contactId));
	}
	
	@Override
	public void deleteContactList(final Collection<Integer> contactIds) throws WTException {
		deleteContact(contactIds);
	}
	
	@Override
	public void updateContactCategoryTags(final UpdateTagsOperation operation, final int categoryId, final Set<String> tagIds) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		ContactTagDAO ctagDao = ContactTagDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "UPDATE");
			List<String> auditTag = new ArrayList<>();
			String tagAction = UpdateTagsOperation.SET.equals(operation) ? "set" : "unset";
			
			if (UpdateTagsOperation.SET.equals(operation) || UpdateTagsOperation.RESET.equals(operation)) {
				Set<String> validTags = coreMgr.listTagIds();
				List<String> okTagIds = tagIds.stream()
						.filter(tagId -> validTags.contains(tagId))
						.collect(Collectors.toList());
				
				con = WT.getConnection(SERVICE_ID, false);
				if (UpdateTagsOperation.RESET.equals(operation)) ctagDao.deleteByCategory(con, categoryId);
				for (String tagId : okTagIds) {
					ctagDao.insertByCategory(con, categoryId, tagId);
				}
				if (UpdateTagsOperation.SET.equals(operation)) auditTag.addAll(okTagIds);
				
			} else if (UpdateTagsOperation.UNSET.equals(operation)) {
				con = WT.getConnection(SERVICE_ID, false);
				ctagDao.deleteByCategoryTags(con, categoryId, tagIds);
				auditTag.addAll(tagIds);
			}
			
			DbUtils.commitQuietly(con);
			HashMap<String,List<String>> audit = new HashMap<>();
			audit.put(tagAction, auditTag);
			
			if (isAuditEnabled() && !auditTag.isEmpty()) {
				writeAuditLog(
					AuditContext.CATEGORY,
					AuditAction.TAG,
					categoryId,
					JsonResult.gson().toJson(audit)
				);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactTags(final UpdateTagsOperation operation, final Collection<Integer> contactIds, final Set<String> tagIds) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		ContactTagDAO ctagDao = ContactTagDAO.getInstance();
		Connection con = null;
		List<String> auditTag = new ArrayList<>();
		String tagAction = UpdateTagsOperation.SET.equals(operation) ? "set" : "unset";
		
		try {
			List<Integer> okCategoryIds = listAllCategoryIds().stream()
					.filter(categoryId -> quietlyCheckRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "UPDATE"))
					.collect(Collectors.toList());
			
			if (UpdateTagsOperation.SET.equals(operation) || UpdateTagsOperation.RESET.equals(operation)) {
				Set<String> validTags = coreMgr.listTagIds();
				List<String> okTagIds = tagIds.stream()
						.filter(tagId -> validTags.contains(tagId))
						.collect(Collectors.toList());
				
				con = WT.getConnection(SERVICE_ID, false);
				
				if (isAuditEnabled()) {
					if (UpdateTagsOperation.RESET.equals(operation)) {
						for (int cId : contactIds) {
							List<String> oldTagIds = new ArrayList<>();
							List<String> newTagIds = new ArrayList<>();
							
							Contact c = getContact(cId);
							oldTagIds.addAll(c != null ? c.getTags() : getContactList(cId).getTags());
							newTagIds.addAll(okTagIds);
							
							HashMap<String,List<String>> audit = coreMgr.compareTags(oldTagIds, newTagIds);
							
							writeAuditLog(
								AuditContext.CONTACT,
								AuditAction.TAG,
								cId,
								JsonResult.gson().toJson(audit)
							);
						}
					} else {
						auditTag.addAll(okTagIds);
					}
				}
								
				if (UpdateTagsOperation.RESET.equals(operation)) ctagDao.deleteByCategoriesContacts(con, okCategoryIds, contactIds);
				for (String tagId : okTagIds) {
					ctagDao.insertByCategoriesContacts(con, okCategoryIds, contactIds, tagId);
				}
				
			} else if (UpdateTagsOperation.UNSET.equals(operation)) {
				con = WT.getConnection(SERVICE_ID, false);
				ctagDao.deleteByCategoriesContactsTags(con, okCategoryIds, contactIds, tagIds);
			}
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled() && !UpdateTagsOperation.RESET.equals(operation)) {
				for (int cId : contactIds) {
					HashMap<String,List<String>> audit = new HashMap<>();
					audit.put(tagAction, auditTag);
					
					if (isAuditEnabled() && !auditTag.isEmpty()) {
						writeAuditLog(
							AuditContext.CONTACT,
							AuditAction.TAG,
							cId,
							JsonResult.gson().toJson(audit)
						);
					}
				}
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	
	
	
	
	

	
	
	public void eraseData(boolean deep) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		CategoryPropsDAO psetDao = CategoryPropsDAO.getInstance();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		//TODO: controllo permessi
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			UserProfileId pid = getTargetProfileId();
			
			// Erase contact and all related tables
			if (deep) {
				for (OCategory ocat : catDao.selectByProfile(con, pid.getDomainId(), pid.getUserId())) {
					contDao.deleteByCategory(con, ocat.getCategoryId());
				}
			} else {
				DateTime revTs = BaseDAO.createRevisionTimestamp();
				for (OCategory ocat : catDao.selectByProfile(con, pid.getDomainId(), pid.getUserId())) {
					contDao.logicDeleteByCategory(con, ocat.getCategoryId(), revTs);
				}
			}
			
			// Erase categories
			psetDao.deleteByProfile(con, pid.getDomainId(), pid.getUserId());
			catDao.deleteByProfile(con, pid.getDomainId(), pid.getUserId());
			
			DbUtils.commitQuietly(con);
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void importContacts(final int categoryId, final ContactFileReader reader, final File file, final ImportMode mode, final LogHandler logHandler) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "CREATE");
			if (ImportMode.COPY.equals(mode)) checkRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "DELETE");
			
			con = WT.getConnection(SERVICE_ID, false);
			logHandler.handleMessage(0, LogMessage.Level.INFO, "Started at {}", new DateTime());
			
			if (ImportMode.COPY.equals(mode)) {
				logHandler.handleMessage(0, LogMessage.Level.INFO, "Cleaning contacts...");
				int del = doContactsDeleteByCategory(con, categoryId, true);
				logHandler.handleMessage(0, LogMessage.Level.INFO, "{} contact/s deleted!", del);
			}
			
			logHandler.handleMessage(0, LogMessage.Level.INFO, "Importing...");
			ContactBatchImportBeanHandler handler = new ContactBatchImportBeanHandler(categoryId, con, coreMgr);
			try {
				reader.read(file, handler);
				handler.flush();
				
				Throwable lex = handler.getLastException();
				if (lex != null) {
					if (lex.getCause() instanceof BatchUpdateException) {
						SQLException sex = ((BatchUpdateException)lex.getCause()).getNextException();
						if (sex != null) {
							logger.error("DB error", lex);
							throw sex;
						}
					}
					throw new WTImportException(lex, "Unexpected error. Reason: {0}", lex.getMessage());
				}
			} catch (IOException | WTReaderException ex1) {
				throw new WTImportException(ex1, "Problems while opening source file. Reason: {}", ex1.getMessage());
			}
			
			DbUtils.commitQuietly(con);
			logHandler.handle(
				new LogMessage(0, LogMessage.Level.INFO, "{} contact/s found!", handler.getHandledCount()),
				new LogMessage(0, LogMessage.Level.INFO, "{} contact/s imported!", handler.getInsertedCount())
			);
			
		} catch(WTImportException ex) {
			DbUtils.rollbackQuietly(con);
			logger.error("Import error", ex.getCause());
			logHandler.handle(
				new LogMessage(0, LogMessage.Level.WARN, "Problems encountered. No changes have been applied!"),
				new LogMessage(0, LogMessage.Level.ERROR, ex.getMessage())
			);
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			logger.error("DB error", ex);
			logHandler.handle(
				new LogMessage(0, LogMessage.Level.WARN, "Problems encountered. No changes have been applied!"),
				new LogMessage(0, LogMessage.Level.ERROR, "Unexpected DB error. Reason: {}", ex.getMessage())
			);
			
		} finally {
			DbUtils.closeQuietly(con);
		}
		
		logHandler.handle(new LogMessage(0, LogMessage.Level.INFO, "Ended at {}", new DateTime()));
	}
	
	/*
	public LogEntries importContacts(int categoryId, ContactFileReader rea, File file, String mode) throws WTException {
		LogEntries log = new LogEntries();
		Connection con = null;
		
		checkRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "CREATE");
		if (mode.equals("copy")) checkRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "DELETE");
			
		log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Started at {0}", new DateTime()));
			
		try {
			con = WT.getConnection(SERVICE_ID, false);

			if (mode.equals("copy")) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Cleaning contacts..."));
				int del = doContactsDeleteByCategory(con, categoryId, true);
				log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contact/s deleted!", del));
			}

			ContactBatchImportBeanHandler handler = new ContactBatchImportBeanHandler(log, con, categoryId);
			try {
				rea.readContacts(file, handler);
				handler.flush();
				 
				Throwable lex = handler.getLastException();
				if (lex != null) {
					if (lex.getCause() instanceof BatchUpdateException) {
						SQLException sex = ((BatchUpdateException)lex.getCause()).getNextException();
						if (sex != null) {
							logger.error("DB error", lex);
							throw sex;
						}
					}
					throw new ImportException(MessageFormat.format("Unexpected error. Reason: {0}", lex.getMessage()), lex);
				}

			} catch(IOException | FileReaderException ex1) {
				throw new ImportException(MessageFormat.format("Problems while opening source file. Reason: {0}", ex1.getMessage()), ex1);
			}

			DbUtils.commitQuietly(con);

			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contact/s read!", handler.handledCount));
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contact/s imported!", handler.insertedCount));

		} catch(ImportException ex) {
			DbUtils.rollbackQuietly(con);
			logger.error("Import error", ex.getCause());
			log.addMaster(new MessageLogEntry(LogEntry.Level.WARN, "Problems encountered. No changes have been applied!"));
			log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, ex.getMessage()));
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			logger.error("DB error", ex);
			log.addMaster(new MessageLogEntry(LogEntry.Level.WARN, "Problems encountered. No changes have been applied!"));
			log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "Unexpected DB error. Reason: {0}", ex.getMessage()));
			
		} finally {
			DbUtils.closeQuietly(con);
		}
		
		log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Ended at {0}", new DateTime()));
		return log;
	}
	*/
	
	public List<BaseReminder> getRemindersToBeNotified(DateTime now) {
		ArrayList<BaseReminder> alerts = new ArrayList<>();
		HashMap<UserProfileId, Boolean> okCache = new HashMap<>();
		HashMap<UserProfileId, DateTime> dateTimeCache = new HashMap<>();
		HashMap<UserProfileId, String> deliveryCache = new HashMap<>();
		ContactDAO cdao = ContactDAO.getInstance();
		Connection con = null;
		
		// Valid reminder times (see getAnniversaryReminderTime in options) 
		// are only at 0 and 30 min of each hour. So skip unuseful runs...
		if((now.getMinuteOfHour() == 0) || (now.getMinuteOfHour() == 30)) {
			try {
				con = WT.getConnection(SERVICE_ID);
				LocalDate date = now.toLocalDate();

				List<VContact> bdays = cdao.viewOnBirthdayByDate(con, date);
				for(VContact cont : bdays) {
					boolean ok = false;
					if(!okCache.containsKey(cont.getCategoryProfileId())) {
						ok = getAnniversaryReminderTime(dateTimeCache, cont.getCategoryProfileId(), date).withZone(DateTimeZone.UTC).equals(now);
						okCache.put(cont.getCategoryProfileId(), ok);
					}

					if(ok) {
						DateTime dateTime = getAnniversaryReminderTime(dateTimeCache, cont.getCategoryProfileId(), date);
						String delivery = getAnniversaryReminderDelivery(deliveryCache, cont.getCategoryProfileId());
						UserProfile.Data ud = WT.getUserData(cont.getCategoryProfileId());

						if(delivery.equals(ContactsSettings.ANNIVERSARY_REMINDER_DELIVERY_EMAIL)) {
							alerts.add(createAnniversaryEmailReminder(ud.getLocale(), ud.getEmail(), true, cont, dateTime));
						} else if(delivery.equals(ContactsSettings.ANNIVERSARY_REMINDER_DELIVERY_APP)) {
							alerts.add(createAnniversaryInAppReminder(ud.getLocale(), true, cont, dateTime));
						}
					}
				}

				List<VContact> anns = cdao.viewOnAnniversaryByDate(con, date);
				for(VContact cont : anns) {
					boolean ok = false;
					if(!okCache.containsKey(cont.getCategoryProfileId())) {
						ok = getAnniversaryReminderTime(dateTimeCache, cont.getCategoryProfileId(), date).withZone(DateTimeZone.UTC).equals(now);
						okCache.put(cont.getCategoryProfileId(), ok);
					}

					if(ok) {
						DateTime dateTime = getAnniversaryReminderTime(dateTimeCache, cont.getCategoryProfileId(), date);
						String delivery = getAnniversaryReminderDelivery(deliveryCache, cont.getCategoryProfileId());
						UserProfile.Data ud = WT.getUserData(cont.getCategoryProfileId());

						if(delivery.equals(ContactsSettings.ANNIVERSARY_REMINDER_DELIVERY_EMAIL)) {
							alerts.add(createAnniversaryEmailReminder(ud.getLocale(), ud.getEmail(), false, cont, dateTime));
						} else if(delivery.equals(ContactsSettings.ANNIVERSARY_REMINDER_DELIVERY_APP)) {
							alerts.add(createAnniversaryInAppReminder(ud.getLocale(), false, cont, dateTime));
						}
					}
				}

			} catch(Exception ex) {
				logger.error("Error collecting reminder alerts", ex);
			} finally {
				DbUtils.closeQuietly(con);
			}
		} 
		return alerts;
	}
	
	
	public ProbeCategoryRemoteUrlResult probeCategoryRemoteUrl(Category.Provider provider, URI url, String username, String password) throws WTException {
		
		if (!Category.Provider.CARDDAV.equals(provider)) {
			throw new WTException("Provider is not valid or is not remote [{0}]", EnumUtils.toSerializedName(provider));
		}
		if (Category.Provider.CARDDAV.equals(provider)) {
			CardDav dav = getCardDav(username, password);

			try {
				DavAddressbook dbook = dav.getAddressbook(url.toString());
				return (dbook != null) ? new ProbeCategoryRemoteUrlResult(dbook.getDisplayName()) : null;

			} catch(DavException ex) {
				logger.error("DAV error", ex);
				return null;
			}
		} else {
			throw new WTException("Unsupported provider");
		}
	}
	
	public void syncRemoteCategory(int categoryId, boolean full) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		CategoryDAO catDao = CategoryDAO.getInstance();
		final String PENDING_KEY = String.valueOf(categoryId);
		final VCardInput icalInput = new VCardInput()
				.withReturnSourceObject(true);
		Connection con = null;
		
		if (pendingRemoteCategorySyncs.putIfAbsent(PENDING_KEY, RunContext.getRunProfileId()) != null) {
			throw new ConcurrentSyncException("Sync activity is already running [{}, {}]", categoryId, RunContext.getRunProfileId());
		}
		
		try {
			//checkRightsOnCategoryFolder(categoryId, CheckRightsTarget.FOLDER, "READ");
			
			con = WT.getConnection(SERVICE_ID, false);
			Category cat = ManagerUtils.createCategory(catDao.selectById(con, categoryId));
			if (cat == null) throw new WTException("Category not found [{}]", categoryId);
			if (!Category.Provider.CARDDAV.equals(cat.getProvider())) {
				throw new WTException("Specified category is not remote (CardDAV) [{}]", categoryId);
			}
			
			// Force a full update if last-sync date is null
			if (cat.getRemoteSyncTimestamp() == null) full = true;
			
			if (Category.Provider.CARDDAV.equals(cat.getProvider())) {
				CategoryRemoteParameters params = LangUtils.deserialize(cat.getParameters(), CategoryRemoteParameters.class);
				if (params == null) throw new WTException("Unable to deserialize remote parameters");
				if (params.url == null) throw new WTException("Remote URL is undefined");
				
				CardDav dav = getCardDav(params.username, params.password);
				
				try {
					DavAddressbook dbook = dav.getAddressbookSyncToken(params.url.toString());
					if (dbook == null) throw new WTException("DAV addressbook not found");
					
					final boolean syncIsSupported = !StringUtils.isBlank(dbook.getSyncToken());
					final DateTime newLastSync = DateTimeUtils.now();
					
					if (!full && (syncIsSupported && !StringUtils.isBlank(cat.getRemoteSyncTag()))) { // Partial update using SYNC mode
						String newSyncToken = dbook.getSyncToken();
						
						logger.debug("Querying CardDAV endpoint for changes [{}, {}]", params.url.toString(), cat.getRemoteSyncTag());
						List<DavSyncStatus> changes = dav.getAddressbookChanges(params.url.toString(), cat.getRemoteSyncTag());
						logger.debug("Returned {} items", changes.size());
						
						try {
							if (!changes.isEmpty()) {
								ContactDAO contDao = ContactDAO.getInstance();
								Map<String, List<Integer>> contactIdsByHref = contDao.selectHrefsByByCategory(con, categoryId);
								
								// Process changes...
								logger.debug("Processing changes...");
								HashSet<String> hrefs = new HashSet<>();
								for (DavSyncStatus change : changes) {
									String href = FilenameUtils.getName(change.getPath());
									//String href = change.getPath();
									
									if (DavUtil.HTTP_SC_TEXT_OK.equals(change.getResponseStatus())) {
										hrefs.add(href);

									} else { // Card deleted
										List<Integer> contactIds = contactIdsByHref.get(href);
										Integer contactId = (contactIds != null) ? contactIds.get(contactIds.size()-1) : null;
										if (contactId == null) {
											logger.warn("Deletion not possible. Card path not found [{}]", PathUtils.concatPaths(dbook.getPath(), FilenameUtils.getName(href)));
											continue;
										}
										doContactDelete(con, contactId, false);
									}
								}

								// Retrieves events list from DAV endpoint (using multiget)
								logger.debug("Retrieving inserted/updated cards [{}]", hrefs.size());
								Collection<String> paths = hrefs.stream().map(href -> PathUtils.concatPaths(dbook.getPath(), FilenameUtils.getName(href))).collect(Collectors.toList());
								List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString(), paths);
								//List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString(), hrefs);

								// Inserts/Updates data...
								logger.debug("Inserting/Updating cards...");
								for (DavAddressbookCard dcard : dcards) {
									String href = FilenameUtils.getName(dcard.getPath());
									//String href = dcard.getPath();
									
									if (logger.isTraceEnabled()) logger.trace("{}", VCardUtils.print(dcard.getCard()));
									List<Integer> contactIds = contactIdsByHref.get(href);
									Integer contactId = (contactIds != null) ? contactIds.get(contactIds.size()-1) : null;
									
									if (contactId != null) {
										doContactDelete(con, contactId, false);
									}
									final ContactInput ci = icalInput.parseCardObject(dcard.getCard());
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(href);
									ci.contact.setEtag(dcard.geteTag());
									doContactInputInsert(coreMgr, con, new HashMap<>(), ci, BitFlag.of(ContactProcessOpts.PICTURE, ContactProcessOpts.RAW_VCARD));
								}
							}
							
							catDao.updateRemoteSyncById(con, categoryId, newLastSync, newSyncToken);
							DbUtils.commitQuietly(con);
							
						} catch(Exception ex) {
							DbUtils.rollbackQuietly(con);
							throw new WTException(ex, "Error importing vCard");
						}
						
					} else { // Full update or partial computing hashes
						String newSyncToken = null;
						if (syncIsSupported) { // If supported, saves last sync-token issued by the server
							newSyncToken = dbook.getSyncToken();
						}
						
						// Retrieves cards from DAV endpoint
						logger.debug("Retrieving whole list [{}]", params.url.toString());
						List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString());
						logger.debug("Endpoint returns {} items", dcards.size());
						
						// Handles data...
						try {
							Map<String, VContactHrefSync> syncByHref = null;
							
							if (full) {
								doContactsDeleteByCategory(con, categoryId, false);
							} else if (!full && !syncIsSupported) {
								// This hash-map is only needed when syncing using hashes
								ContactDAO contDao = ContactDAO.getInstance();
								syncByHref = contDao.viewHrefSyncDataByCategory(con, categoryId);
							}	
							
							logger.debug("Processing results...");
							// Define a simple map in order to check duplicates.
							// eg. SOGo passes same card twice :(
							HashSet<String> hrefs = new HashSet<>();
							for (DavAddressbookCard dcard : dcards) {
								String href = FilenameUtils.getName(dcard.getPath());
								//String href = dcard.getPath();
								String etag = dcard.geteTag();
								
								if (logger.isTraceEnabled()) logger.trace("{}", VCardUtils.print(dcard.getCard()));
								if (hrefs.contains(href)) {
									logger.trace("Card duplicated. Skipped! [{}]", href);
									continue;
								}
								
								boolean skip = false;
								Integer matchingContactId = null;
								
								if (syncByHref != null) { // Only if... (!full && !syncIsSupported) see above!
									String hash = VCardUtils.computeHashCompat(dcard.getCard());
									
									VContactHrefSync hrefSync = syncByHref.remove(href);
									if (hrefSync != null) { // Href found -> maybe updated item
										if (!StringUtils.equals(hrefSync.getEtag(), hash)) {
											matchingContactId = hrefSync.getContactId();
											etag = hash;
											logger.trace("Card updated [{}, {}]", href, hash);
										} else {
											skip = true;
											logger.trace("Card not modified [{}, {}]", href, hash);
										}
									} else { // Href not found -> added item
										logger.trace("Card newly added [{}, {}]", href, hash);
										etag = hash;
									}
								}
								
								if (!skip) {
									final ContactInput ci = icalInput.parseCardObject(dcard.getCard());
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(href);
									ci.contact.setEtag(etag);
									
									if (matchingContactId == null) {
										doContactInputInsert(coreMgr, con, new HashMap<>(), ci, BitFlag.of(ContactProcessOpts.PICTURE, ContactProcessOpts.RAW_VCARD));
									} else {
										boolean updated = doContactInputUpdate(coreMgr, con, new HashMap<>(), matchingContactId, ci, BitFlag.of(ContactProcessOpts.PICTURE, ContactProcessOpts.RAW_VCARD));
										if (!updated) throw new WTException("Contact not found [{}]", matchingContactId);
									}
								}
								
								hrefs.add(href); // Marks as processed!
							}
							
							if (syncByHref != null) { // Only if... (!full && !syncIsSupported) see above!
								// Remaining hrefs -> deleted items
								for (VContactHrefSync hrefSync : syncByHref.values()) {
									logger.trace("Card deleted [{}]", hrefSync.getHref());
									doContactDelete(con, hrefSync.getContactId(), false);
								}
							}	
							
							catDao.updateRemoteSyncById(con, categoryId, newLastSync, newSyncToken);
							DbUtils.commitQuietly(con);
							
						} catch(Exception ex) {
							DbUtils.rollbackQuietly(con);
							throw new WTException(ex, "Error importing vCard");
						}
					}
					
				} catch(DavException ex) {
					throw new WTException(ex, "CardDAV error");
				}
			} else {
				throw new WTException("Unsupported provider [{0}]", cat.getProvider());
			}
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
			pendingRemoteCategorySyncs.remove(PENDING_KEY);
		}
	}
	
	private Category doCategoryInsert(Connection con, Category cat) throws DAOException, WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		
		OCategory ocat = ManagerUtils.createOCategory(cat);
		ocat.setCategoryId(catDao.getSequence(con).intValue());
		fillOCategoryWithDefaults(ocat);
		if (ocat.getIsDefault()) catDao.resetIsDefaultByProfile(con, ocat.getDomainId(), ocat.getUserId());
		
		catDao.insert(con, ocat);
		return ManagerUtils.createCategory(ocat);
	}
	
	private boolean doCategoryUpdate(Connection con, Category cat) throws DAOException, WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		
		OCategory ocat = ManagerUtils.createOCategory(cat);
		fillOCategoryWithDefaults(ocat);
		if (ocat.getIsDefault()) catDao.resetIsDefaultByProfile(con, ocat.getDomainId(), ocat.getUserId());
		
		return catDao.update(con, ocat) == 1;
	}
	
	private ContactObject doContactObjectPrepare(Connection con, VContactObject vcont, ContactObjectOutputType outputType, Map<String, String> tagNamesByIdMap) throws WTException {
		if (ContactObjectOutputType.STAT.equals(outputType)) {
			return ManagerUtils.fillContactObject(new ContactObject(), vcont);
			
		} else {
			ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
			ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
			
			ContactEx contact = ManagerUtils.fillContact(new ContactEx(), vcont);
			contact.setCompany(ManagerUtils.createContactCompany(vcont));
			
			if (vcont.getHasPicture()) {
				OContactPicture opic = cpicDao.select(con, vcont.getContactId());
				if (opic != null) contact.setPicture(ManagerUtils.fillContactPicture(new ContactPictureWithBytes(opic.getBytes()), opic));
			}
			if (!StringUtils.isBlank(vcont.getTags())) {
				contact.setTags(new LinkedHashSet(new CId(vcont.getTags()).getTokens()));
			}
			//TODO: add support to Attachments and CustomValues. See doContactGet...
			
			if (ContactObjectOutputType.VCARD.equals(outputType)) {
				ContactObjectWithVCard ret = ManagerUtils.fillContactObject(new ContactObjectWithVCard(), vcont);
				VCardOutput out = new VCardOutput(VCardUtils.buildProdId(ManagerUtils.getProductName()))
						.withEnableCaretEncoding(VCARD_CARETENCODINGENABLED);
				
				VCard baseObject = null;
				if (vcont.getHasVcard()) {
					String rawVCard = vcaDao.selectRawDataById(con, vcont.getContactId());
					if (rawVCard != null) {
						try {
							baseObject = VCardUtils.parseFirst(rawVCard);
						} catch (IOException ex) {
							logger.debug("VCardUtils.parseFirst", ex);
						}
					}
				}
				ret.setVcard(out.writeVCard(contact, baseObject));
				return ret;
				
			} else {
				ContactObjectWithBean ret = ManagerUtils.fillContactObject(new ContactObjectWithBean(), vcont);
				ret.setContact(contact);
				return ret;
			}
		}
	}
	
	private enum ContactProcessOpts implements BitFlagEnum {
		PICTURE(1), ATTACHMENTS(2), TAGS(4), CUSTOM_VALUES(8), LIST_RECIPIENTS(16), RAW_VCARD(32);
		
		private int value = 0;
		private ContactProcessOpts(int value) { this.value = value; }
		@Override
		public int value() { return this.value; }
		
		public static BitFlag<ContactProcessOpts> parseContactGetOptions(BitFlag<ContactGetOptions> flags) {
			BitFlag<ContactProcessOpts> ret = new BitFlag<>();
			
			if (flags.has(ContactGetOptions.PICTURE)) ret.set(ContactProcessOpts.PICTURE);
			if (flags.has(ContactGetOptions.ATTACHMENTS)) ret.set(ContactProcessOpts.ATTACHMENTS);
			if (flags.has(ContactGetOptions.TAGS)) ret.set(ContactProcessOpts.TAGS);
			if (flags.has(ContactGetOptions.CUSTOM_VALUES)) ret.set(ContactProcessOpts.CUSTOM_VALUES);
			if (flags.has(ContactGetOptions.LIST_RECIPIENTS)) ret.set(ContactProcessOpts.LIST_RECIPIENTS);
			return ret;
		}
		
		public static BitFlag<ContactProcessOpts> parseContactUpdateOptions(BitFlag<ContactUpdateOptions> flags) {
			BitFlag<ContactProcessOpts> ret = new BitFlag<>();
			if (flags.has(ContactUpdateOptions.PICTURE)) ret.set(ContactProcessOpts.PICTURE);
			if (flags.has(ContactUpdateOptions.ATTACHMENTS)) ret.set(ContactProcessOpts.ATTACHMENTS);
			if (flags.has(ContactUpdateOptions.TAGS)) ret.set(ContactProcessOpts.TAGS);
			if (flags.has(ContactUpdateOptions.CUSTOM_VALUES)) ret.set(ContactProcessOpts.CUSTOM_VALUES);
			if (flags.has(ContactUpdateOptions.LIST_RECIPIENTS)) ret.set(ContactProcessOpts.LIST_RECIPIENTS);
			return ret;
		}
	}
	
	private Contact doContactGet(Connection con, int contactId, BitFlag<ContactProcessOpts> processOpts) throws DAOException, WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactTagDAO tagDao = ContactTagDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		ContactAttachmentDAO attDao = ContactAttachmentDAO.getInstance();
		ContactCustomValueDAO cvalDao = ContactCustomValueDAO.getInstance();
		
		OContact ocont = contDao.selectOnlineById(con, contactId);
		if ((ocont == null) || (ocont.getIsList())) return null;
		
		Contact cont = ManagerUtils.fillContact(new Contact(), ocont);
		cont.setCompany(ManagerUtils.createContactCompany(ocont));
		
		if (processOpts.has(ContactProcessOpts.PICTURE)) {
			OContactPictureMetaOnly opic = cpicDao.selectMeta(con, contactId);
			if (opic != null) {
				cont.setPicture(ManagerUtils.fillContactPicture(new ContactPictureWithSize(opic.getSize()), opic));
			}
		}
		if (processOpts.has(ContactProcessOpts.TAGS)) {
			cont.setTags(tagDao.selectTagsByContact(con, contactId));
		}
		if (processOpts.has(ContactProcessOpts.ATTACHMENTS)) {
			List<OContactAttachment> oatts = attDao.selectByContact(con, contactId);
			cont.setAttachments(ManagerUtils.createContactAttachmentList(oatts));
		}
		if (processOpts.has(ContactProcessOpts.CUSTOM_VALUES)) {
			List<OContactCustomValue> ovals = cvalDao.selectByContact(con, contactId);
			cont.setCustomValues(ManagerUtils.createCustomValuesMap(ovals));
		}
		return cont;
	}
	
	private ContactList doContactListGet(Connection con, int contactId, BitFlag<ContactProcessOpts> processOptions) throws DAOException, WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		ContactTagDAO tagDao = ContactTagDAO.getInstance();
		
		OContact ocont = contDao.selectOnlineById(con, contactId);
		if ((ocont == null) || (!ocont.getIsList())) return null;
		
		ContactList cont = ManagerUtils.fillContactList(new ContactList(), ocont);
		
		if (processOptions.has(ContactProcessOpts.LIST_RECIPIENTS)) {
			List<VListRecipient> vlrecs = lrecDao.viewByContact(con, contactId);
			cont.setRecipients(ManagerUtils.createContactListRecipientList(vlrecs));
		}
		
		if (processOptions.has(ContactProcessOpts.TAGS)) {
			cont.setTags(tagDao.selectTagsByContact(con, contactId));
		}
		return cont;
	}
	
	private int doBatchInsertContacts(CoreManager coreMgr, Connection con, int categoryId, ArrayList<ContactInput> contacts) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ArrayList<OContact> ocontacts = new ArrayList<>();
		//TODO: eventualmente introdurre supporto alle immagini
		
		ArrayList<String> masterDataIds = new ArrayList<>();
		for (ContactInput input : contacts) {
			if ((input.contactCompany != null) && !StringUtils.isBlank(input.contactCompany.getCompanyId())) {
				masterDataIds.add(input.contactCompany.getCompanyId());
			}
		}
		
		Map<String, MasterDataLookup> masterDataMap = null;
		if (!masterDataIds.isEmpty()) masterDataMap = coreMgr.lookupMasterData(masterDataIds);
		
		for (ContactInput input : contacts) {
			if (logger.isTraceEnabled()) logger.trace("[BatchInsert] Preparing entry [{}, {}]", input.contact.getDisplayName(true), input.contact.getEmail1());
			OContact ocont = ManagerUtils.fillOContact(new OContact(), input.contact);
			ocont.setIsList(false);
			ocont.setCategoryId(categoryId);
			ocont.setContactId(contDao.getSequence(con).intValue());
			fillDefaultsForInsert(ocont);
			if (input.contactCompany != null) {
				ocont.setCompanyData(lookupRealCompanyData(masterDataMap, input.contactCompany));
			}
			ocontacts.add(ocont);
		}
		
		if (ocontacts.size() == 1) {
			return contDao.insert(con, ocontacts.get(0), BaseDAO.createRevisionTimestamp());
		} else {
			return contDao.batchInsert(con, ocontacts, BaseDAO.createRevisionTimestamp());
		}
	}
	
	private OContact.CompanyData lookupRealCompanyData(Map<String, ? extends BaseMasterData> masterDataMap, ContactCompany company) {
		OContact.CompanyData cd = new OContact.CompanyData();
		String valueId = company.getValueId();
		if (!StringUtils.isBlank(valueId)) {
			BaseMasterData md = masterDataMap.get(valueId);
			if (md != null) {
				cd.company = md.getDescription(); // Save hystoric data
				cd.companyMasterDataId = valueId;
			} else {
				cd.company = valueId;
				cd.companyMasterDataId = null;
			}
		} else {
			cd.company = company.getValue();
			cd.companyMasterDataId = null;
		}
		return cd;
	}
	
	private OContact.CompanyData lookupRealCompanyData(CoreManager coreMgr, ContactCompany company) throws WTException {
		OContact.CompanyData cd = new OContact.CompanyData();
		String valueId = company.getValueId();
		if (!StringUtils.isBlank(valueId)) {
			MasterData md = coreMgr.getMasterData(valueId);
			if (md != null) {
				cd.company = md.getDescription(); // Save hystoric data
				cd.companyMasterDataId = valueId;
			} else {
				cd.company = valueId;
				cd.companyMasterDataId = null;
			}
		} else {
			cd.company = company.getValue();
			cd.companyMasterDataId = null;
		}
		return cd;
	}
	
	private ContactInsertResult doContactInsert(CoreManager coreMgr, Connection con, boolean isList, ContactEx contact, String rawVCard, BitFlag<ContactProcessOpts> processOpts) throws DAOException, IOException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
		ContactTagDAO tagDao = ContactTagDAO.getInstance();
		ContactCustomValueDAO cvalDao = ContactCustomValueDAO.getInstance();
		DateTime revisionTimestamp = BaseDAO.createRevisionTimestamp();
		
		int newContactId = contDao.getSequence(con).intValue();
		OContact ocont = ManagerUtils.fillOContact(new OContact(), contact);
		ocont.setContactId(newContactId);
		ocont.setIsList(isList);
		ManagerUtils.fillOContactWithDefaultsForInsert(ocont, getTargetProfileId(), revisionTimestamp);
		
		if (!isList && contact.hasCompany()) {
			try {
				ocont.setCompanyData(lookupRealCompanyData(coreMgr, contact.getCompany()));
			} catch (Throwable t) {
				logger.error("Unable to lookup company data", t);
			}
		}
		
		boolean ret = contDao.insert(con, ocont, revisionTimestamp) == 1;
		if (!ret) return null;
		
		if (!isList && processOpts.has(ContactProcessOpts.RAW_VCARD) && !StringUtils.isBlank(rawVCard)) {
			vcaDao.upsert(con, newContactId, rawVCard);
		}
		
		OContactPicture ocpic = null;
		if (!isList && processOpts.has(ContactProcessOpts.PICTURE) && contact.hasPicture()) {
			ContactPicture pic = contact.getPicture();
			if (!(pic instanceof ContactPictureWithBytes)) throw new IOException("Picture bytes not available");
			ocpic = doContactPictureInsert(con, newContactId, (ContactPictureWithBytes)pic);
		}
		
		Set<String> otags = null;
		if (processOpts.has(ContactProcessOpts.TAGS) && contact.hasTags()) {
			otags = new LinkedHashSet<>(contact.getTags());
			tagDao.batchInsert(con, newContactId, contact.getTags());
		}
		
		ArrayList<OContactAttachment> oatts = null;
		if (!isList && processOpts.has(ContactProcessOpts.ATTACHMENTS) && contact.hasAttachments()) {
			oatts = new ArrayList<>(contact.getAttachments().size());
			for (ContactAttachment att : contact.getAttachments()) {
				if (!(att instanceof ContactAttachmentWithStream)) throw new IOException("Attachment stream not available [" + att.getAttachmentId() + "]");
				oatts.add(doContactAttachmentInsert(con, newContactId, (ContactAttachmentWithStream)att));
			}
		}
		
		ArrayList<OContactCustomValue> ocvals = null;
		if (!isList && processOpts.has(ContactProcessOpts.CUSTOM_VALUES) && contact.hasCustomValues()) {
			ocvals = new ArrayList<>(contact.getCustomValues().size());
			for (CustomFieldValue cfv : contact.getCustomValues().values()) {
				OContactCustomValue ocv = ManagerUtils.fillOContactCustomValue(new OContactCustomValue(), cfv);
				ocv.setContactId(newContactId);
				ocvals.add(ocv);
			}
			cvalDao.batchInsert(con, ocvals);
		}
		
		return new ContactInsertResult(ocont, ocpic, otags, oatts, ocvals);
	}
	
	private ContactsListInsertResult doContactListInsert(CoreManager coreMgr, Connection con, ContactListEx contact, BitFlag<ContactProcessOpts> processOpts) throws DAOException, IOException {
		BitFlag<ContactProcessOpts> processOpts2 = processOpts.has(ContactProcessOpts.TAGS) ? BitFlag.of(ContactProcessOpts.TAGS) : BitFlag.none();
		ContactInsertResult result = doContactInsert(coreMgr, con, true, ManagerUtils.createContactEx(contact), null, processOpts2);
		
		ArrayList<OListRecipient> orecipients = null;
		if (processOpts.has(ContactProcessOpts.LIST_RECIPIENTS) && contact.hasRecipients()) {
			orecipients = new ArrayList<>(contact.getRecipients().size());
			for (ContactListRecipient recipient : contact.getRecipients()) {
				orecipients.add(doContactListRecipientInsert(con, result.ocontact.getContactId(), recipient));
			}
		}
		
		return new ContactsListInsertResult(result.ocontact, orecipients, result.otags);
	}
	
	private ContactInsertResult doContactInputInsert(CoreManager coreMgr, Connection con, Map<String, List<String>> tagIdsByNameMap, ContactInput input, BitFlag<ContactProcessOpts> processOpts) throws IOException {
		ContactEx contact = new ContactEx(input.contact);
		
		if (input.contactCompany != null) {
			contact.setCompany(input.contactCompany);
		}
		if (input.contactPicture != null) {
			contact.setPicture(input.contactPicture);
		}
		if (input.tagNames != null) {
			Set<String> tagIds = new HashSet<>();
			for (String tagName : input.tagNames) {
				if (tagIdsByNameMap.containsKey(tagName)) {
					tagIds.addAll(tagIdsByNameMap.get(tagName));
				}
			}
			contact.setTags(tagIds);
		}
		
		String rawVCard = null;
		if (input.sourceObject != null && !input.sourceObject.getProperties().isEmpty()) {
			rawVCard = VCardUtils.write(input.sourceObject);
		}
		
		ContactInsertResult ret = doContactInsert(coreMgr, con, false, contact, rawVCard, processOpts);
		return ret;
	}
	
	private OContact doContactUpdate(CoreManager coreMgr, Connection con, boolean isList, int contactId, ContactEx contact, String rawVCard, BitFlag<ContactProcessOpts> processOpts) throws DAOException, IOException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
		ContactTagDAO tagDao = ContactTagDAO.getInstance();
		ContactAttachmentDAO attcDao = ContactAttachmentDAO.getInstance();
		ContactCustomValueDAO cvalDao = ContactCustomValueDAO.getInstance();
		DateTime revisionTimestamp = BaseDAO.createRevisionTimestamp();
		
		OContact ocontact = ManagerUtils.fillOContact(new OContact(), contact);
		ocontact.setContactId(contactId);
		ManagerUtils.fillOContactWithDefaultsForUpdate(ocontact, revisionTimestamp);
		if (!isList && contact.hasCompany()) {
			try {
				ocontact.setCompanyData(lookupRealCompanyData(coreMgr, contact.getCompany()));
			} catch(WTException ex) {
				logger.error("Unable to lookup company data", ex);
			}
		}
		boolean ret = false;
		if (isList) {
			ret = contDao.updateList(con, ocontact, revisionTimestamp) == 1;
		} else {
			ret = contDao.update(con, ocontact, revisionTimestamp) == 1;
		}
		if (!ret) return null;
		
		if (!isList && processOpts.has(ContactProcessOpts.RAW_VCARD)) {
			vcaDao.upsert(con, contactId, rawVCard);
		}
		
		if (!isList && processOpts.has(ContactProcessOpts.PICTURE)) {
			if (contact.hasPicture()) {
				ContactPicture pic = contact.getPicture();
				if (!(pic instanceof ContactPictureWithBytes)) throw new IOException("Picture bytes not available");
				doContactPictureUpdate(con, contactId, (ContactPictureWithBytes)pic);
			} else {
				doContactPictureDelete(con, contactId);
			}
		}
		
		if (processOpts.has(ContactProcessOpts.TAGS)) {
			Set<String> oldTags = tagDao.selectTagsByContact(con, contactId);
			CollectionChangeSet<String> changeSet = LangUtils.getCollectionChanges(oldTags, contact.getTagsOrEmpty());
			tagDao.batchInsert(con, contactId, changeSet.inserted);
			tagDao.deleteByIdTags(con, contactId, changeSet.deleted);
		}
		
		if (!isList && processOpts.has(ContactProcessOpts.ATTACHMENTS)) {
			List<ContactAttachment> oldAttchs = ManagerUtils.createContactAttachmentList(attcDao.selectByContact(con, contactId));
			CollectionChangeSet<ContactAttachment> changeSet = LangUtils.getCollectionChanges(oldAttchs, contact.getAttachmentsOrEmpty());

			for (ContactAttachment att : changeSet.inserted) {		
				if (!(att instanceof ContactAttachmentWithStream)) throw new IOException("Attachment stream not available [" + att.getAttachmentId() + "]");
				doContactAttachmentInsert(con, contactId, (ContactAttachmentWithStream)att);
			}
			for (ContactAttachment att : changeSet.updated) {
				if (!(att instanceof ContactAttachmentWithStream)) continue;
				doContactAttachmentUpdate(con, (ContactAttachmentWithStream)att);
			}
			attcDao.deleteByIdsContact(con, changeSet.deleted.stream().map(att -> att.getAttachmentId()).collect(Collectors.toList()), contactId);
		}
		
		if (!isList && processOpts.has(ContactProcessOpts.CUSTOM_VALUES) && contact.hasCustomValues()) {
			ArrayList<String> customFieldIds = new ArrayList<>();
			ArrayList<OContactCustomValue> ocvals = new ArrayList<>(contact.getCustomValues().size());
			for (CustomFieldValue cfv : contact.getCustomValues().values()) {
				OContactCustomValue ocv = ManagerUtils.fillOContactCustomValue(new OContactCustomValue(), cfv);
				ocv.setContactId(contactId);
				ocvals.add(ocv);
				customFieldIds.add(ocv.getCustomFieldId());
			}
			//TODO: use upsert when available
			cvalDao.deleteByContactFields(con, contactId, customFieldIds);
			cvalDao.batchInsert(con, ocvals);
		}
		
		return ocontact;
	}
	
	private OContact doContactListUpdate(CoreManager coreMgr, Connection con, int contactId, ContactListEx contact, BitFlag<ContactProcessOpts> processOpts) throws DAOException, IOException {
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		BitFlag<ContactProcessOpts> processOpts2 = processOpts.has(ContactProcessOpts.TAGS) ? BitFlag.of(ContactProcessOpts.TAGS) : BitFlag.none();
		OContact ocont = doContactUpdate(coreMgr, con, true, contactId, ManagerUtils.createContactEx(contact), null, processOpts2);
		if (ocont == null) return ocont;
		
		if (processOpts.has(ContactProcessOpts.LIST_RECIPIENTS) && contact.hasRecipients()) {
			lrecDao.deleteByContact(con, contactId);
			for (ContactListRecipient recipient : contact.getRecipients()) {
				doContactListRecipientInsert(con, contactId, recipient);
			}
		}
		
		return ocont;
	}
	
	private boolean doContactInputUpdate(CoreManager coreMgr, Connection con, Map<String, List<String>> tagIdsByNameMap, int contactId, ContactInput input, BitFlag<ContactProcessOpts> processOpts) throws IOException, WTException {
		ContactEx contact = new ContactEx(input.contact);
		
		if (input.contactCompany != null) {
			// Due that in vcard we do not have separate information for company ID 
			// and description, in order to not lose data during the update, we have
			// to apply some checks and then restore the original company in  
			// particular cases:
			// - value as ID matches the company raw id
			// - value as DESCRIPTION matched the company linked description
			ContactCompany origCompany = getContactCompany(contactId);
			if (origCompany != null) {
				if (StringUtils.equals(input.contactCompany.getValueId(), origCompany.getCompanyId()) || StringUtils.equals(input.contactCompany.getCompanyDescription(), origCompany.getValue())) {
					contact.setCompany(origCompany);
				} else {
					contact.setCompany(input.contactCompany);
				}
			}
		}
		if (input.contactPicture != null) {
			contact.setPicture(input.contactPicture);
		}
		if (input.tagNames != null) {
			Set<String> tagIds = new HashSet<>();
			for (String tagName : input.tagNames) {
				if (tagIdsByNameMap.containsKey(tagName)) {
					tagIds.addAll(tagIdsByNameMap.get(tagName));
				}
			}
			contact.setTags(tagIds);
		}
		
		String rawVCard = null;
		if (input.sourceObject != null && !input.sourceObject.getProperties().isEmpty()) {
			rawVCard = VCardUtils.write(input.sourceObject);
		}
		
		return doContactUpdate(coreMgr, con, false, contactId, contact, rawVCard, processOpts) != null;
	}
	
	private boolean doContactDelete(Connection con, int contactId, boolean logicDelete) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		if (logicDelete) {
			return contDao.logicDeleteById(con, contactId, BaseDAO.createRevisionTimestamp()) == 1;
		} else {
			return contDao.deleteById(con, contactId) == 1;
		}
	}
	
	
	
	private int doContactsDeleteByCategory(Connection con, int categoryId, boolean logicDelete) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		if (logicDelete) {
			return contDao.logicDeleteByCategory(con, categoryId, BaseDAO.createRevisionTimestamp());
			
		} else {
			return contDao.deleteByCategory(con, categoryId);
		}
	}
	
	private ContactInsertResult doContactCopy(CoreManager coreMgr, Connection con, boolean isList, int sourceContactId, ContactEx contact, int targetCategoryId, BitFlag<ContactProcessOpts> processOpts) throws DAOException, IOException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
		
		contact.setCategoryId(targetCategoryId);
		contact.setPublicUid(null); // Reset value in order to make inner function generate new one!
		contact.setHref(null); // Reset value in order to make inner function generate new one!
		
		if (!isList && processOpts.has(ContactProcessOpts.PICTURE) && contact.hasPicture()) {
			OContactPicture opic = cpicDao.select(con, sourceContactId);
			if (opic != null) {
				contact.setPicture(ManagerUtils.fillContactPicture(new ContactPictureWithBytes(opic.getBytes()), opic));
			}
		}
		
		String rawVCard = null;
		if (!isList) {
			rawVCard = vcaDao.selectRawDataById(con, sourceContactId);
		}
		//TODO: maybe support attachments copy
		
		return doContactInsert(coreMgr, con, isList, contact, rawVCard, processOpts.copy().set(ContactProcessOpts.RAW_VCARD));
	}
	
	private ContactsListInsertResult doContactListCopy(CoreManager coreMgr, Connection con, int sourceContactId, ContactListEx contact, int targetCategoryId, BitFlag<ContactProcessOpts> processOpts) throws DAOException, IOException {
		BitFlag<ContactProcessOpts> processOpts2 = processOpts.has(ContactProcessOpts.TAGS) ? BitFlag.of(ContactProcessOpts.TAGS) : BitFlag.none();
		ContactInsertResult result = doContactCopy(coreMgr, con, true, sourceContactId, ManagerUtils.createContactEx(contact), targetCategoryId, processOpts2);
		
		ArrayList<OListRecipient> orecipients = null;
		if (processOpts.has(ContactProcessOpts.LIST_RECIPIENTS) && contact.hasRecipients()) {
			orecipients = new ArrayList<>(contact.getRecipients().size());
			for (ContactListRecipient recipient : contact.getRecipients()) {
				orecipients.add(doContactListRecipientInsert(con, result.ocontact.getContactId(), recipient));
			}
		}
		
		return new ContactsListInsertResult(result.ocontact, orecipients, result.otags);
	}
	
	private boolean doContactMove(Connection con, int contactId, int targetCategoryId) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();		
		return contDao.updateCategory(con, contactId, targetCategoryId, BaseDAO.createRevisionTimestamp()) == 1;
	}
	
	private OContactPicture doContactPictureInsert(Connection con, int contactId, ContactPictureWithBytes picture) throws DAOException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		
		OContactPicture ocpic = new OContactPicture();
		ocpic.setContactId(contactId);
		ocpic.setMediaType(picture.getMediaType());
		
		try {
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(picture.getBytes()));
			if ((bi.getWidth() > 720) || (bi.getHeight() > 720)) {
				bi = Scalr.resize(bi, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 720);
				ocpic.setWidth(bi.getWidth());
				ocpic.setHeight(bi.getHeight());
				String formatName = new MimeType(picture.getMediaType()).getSubType();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					ImageIO.write(bi, formatName, baos);
					baos.flush();
					ocpic.setBytes(baos.toByteArray());
				} catch(IOException ex1) {
					logger.warn("Error resizing image", ex1);
				} finally {
					IOUtils.closeQuietly(baos);
				}
			} else {
				ocpic.setWidth(bi.getWidth());
				ocpic.setHeight(bi.getHeight());
				ocpic.setBytes(picture.getBytes());
			}
		} catch(IOException ex) {
			throw new WTRuntimeException(ex, "Error handling picture");
		}
		cpicDao.insert(con, ocpic);
		return ocpic;
	}
	
	private void doContactPictureUpdate(Connection con, int contactId, ContactPictureWithBytes picture) throws DAOException {
		doContactPictureDelete(con, contactId);
		doContactPictureInsert(con, contactId, picture);
	}
	
	private void doContactPictureDelete(Connection con, int contactId) throws DAOException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		cpicDao.delete(con, contactId);
	}
	
	private OContactAttachment doContactAttachmentInsert(Connection con, int contactId, ContactAttachmentWithStream attachment) throws DAOException, IOException {
		Check.notNull(attachment, "attachment");
		ContactAttachmentDAO attDao = ContactAttachmentDAO.getInstance();
		
		OContactAttachment oatt = ManagerUtils.fillOContactAttachment(new OContactAttachment(), attachment);
		oatt.setContactAttachmentId(IdentifierUtils.getUUIDTimeBased());
		oatt.setContactId(contactId);
		attDao.insert(con, oatt, BaseDAO.createRevisionTimestamp());
		
		InputStream is = attachment.getStream();
		try {
			attDao.insertBytes(con, oatt.getContactAttachmentId(), IOUtils.toByteArray(is));
		} finally {
			IOUtils.closeQuietly(is);
		}
		
		return oatt;
	}
	
	private boolean doContactAttachmentUpdate(Connection con, ContactAttachmentWithStream attachment) throws DAOException, IOException {
		Check.notNull(attachment, "attachment");
		ContactAttachmentDAO attDao = ContactAttachmentDAO.getInstance();
		
		OContactAttachment oatt = ManagerUtils.fillOContactAttachment(new OContactAttachment(), attachment);
		attDao.update(con, oatt, BaseDAO.createRevisionTimestamp());
		
		InputStream is = attachment.getStream();
		try {
			attDao.deleteBytes(con, oatt.getContactAttachmentId());
			return attDao.insertBytes(con, oatt.getContactAttachmentId(), IOUtils.toByteArray(is)) == 1;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	
	
	
	
	private OListRecipient doContactListRecipientInsert(Connection con, int contactId, ContactListRecipient recipient) throws DAOException, IOException {
		Check.notNull(recipient, "recipient");
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		OListRecipient olrec = ManagerUtils.fillOListRecipient(new OListRecipient(), recipient);
		olrec.setListRecipientId(lrecDao.getSequence(con).intValue());
		olrec.setContactId(contactId);
		lrecDao.insert(con, olrec);
		
		return olrec;
	}
	
	private String lookupMasterDataDescription(CoreManager coreMgr, String masterDataId) throws WTException {
		if (masterDataId == null) return null;
		MasterData md = coreMgr.getMasterData(masterDataId);
		return (md != null) ? md.getDescription() : null;
	}
	
	private UserProfileId findCategoryOwner(int categoryId) throws WTException {
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			CategoryDAO dao = CategoryDAO.getInstance();
			Owner owner = dao.selectOwnerById(con, categoryId);
			return (owner == null) ? null : new UserProfileId(owner.getDomainId(), owner.getUserId());
			
		} catch(Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private void onAfterCategoryAction(int categoryId, UserProfileId owner) {
		if (!owner.equals(getTargetProfileId())) shareCache.init();
	}
	
	private void checkRightsOnCategoryRoot(UserProfileId owner, String action) throws WTException {
		UserProfileId targetPid = getTargetProfileId();
		
		if (RunContext.isWebTopAdmin()) return;
		if (owner.equals(targetPid)) return;
		
		String shareId = shareCache.getShareRootIdByOwner(owner);
		if (shareId == null) throw new WTException("ownerToRootShareId({0}) -> null", owner);
		CoreManager coreMgr = WT.getCoreManager(targetPid);
		if (coreMgr.isShareRootPermitted(shareId, action)) return;
		
		UserProfileId runPid = RunContext.getRunProfileId();
		throw new AuthException("Action '{}' not allowed for '{}' on root share '{}' [{}, {}]", action, runPid, shareId, GROUPNAME_CATEGORY, targetPid.toString());
	}
	
	private boolean quietlyCheckRightsOnCategory(int categoryId, CheckRightsTarget target, String action) {
		try {
			checkRightsOnCategory(categoryId, target, action);
			return true;
		} catch(AuthException ex1) {
			return false;
		} catch(WTException ex1) {
			logger.warn("Unable to check rights [{}]", categoryId);
			return false;
		}
	}
	
	private void checkRightsOnCategory(Set<Integer> okCache, int categoryId, CheckRightsTarget target, String action) throws WTException {
		if (!okCache.contains(categoryId)) {
			checkRightsOnCategory(categoryId, target, action);
			okCache.add(categoryId);
		}
	}
	
	private void checkRightsOnCategory(int categoryId, CheckRightsTarget target, String action) throws WTException {
		UserProfileId targetPid = getTargetProfileId();
		Subject subject = RunContext.getSubject();
		UserProfileId runPid = RunContext.getRunProfileId(subject);
		UserProfileId owner = ownerCache.get(categoryId);
		if (owner == null) throw new WTException("categoryToOwner({0}) -> null", categoryId);
		
		if (RunContext.isWebTopAdmin(subject)) {
			// Skip checks for running wtAdmin and sysAdmin target
			if (targetPid.equals(RunContext.getSysAdminProfileId())) return;
			
			// Skip checks if target is the resource owner
			if (owner.equals(targetPid)) return;
			
			// Skip checks if resource is a valid incoming folder
			if (shareCache.getFolderIds().contains(categoryId)) return;
			
			String exMsg = null;
			if (CheckRightsTarget.FOLDER.equals(target)) {
				exMsg = "Action '{}' not allowed for '{}' on folder '{}' [{}, {}]";
			} else if (CheckRightsTarget.ELEMENTS.equals(target)) {
				exMsg = "Action '{}' not allowed for '{}' on elements of folder '{}' [{}, {}]";
			}
			throw new AuthException(exMsg, action, runPid, categoryId, GROUPNAME_CATEGORY, targetPid.toString());
			
		} else {
			// Skip checks if target is the resource owner and it's the running profile
			if (owner.equals(targetPid) && targetPid.equals(runPid)) return;
			
			// Checks rights on the wildcard instance (if present)
			CoreManager core = WT.getCoreManager(targetPid);
			String wildcardShareId = shareCache.getWildcardShareFolderIdByOwner(owner);
			if (wildcardShareId != null) {
				if (CheckRightsTarget.FOLDER.equals(target)) {
					if (core.isShareFolderPermitted(wildcardShareId, action)) return;
				} else if (CheckRightsTarget.ELEMENTS.equals(target)) {
					if (core.isShareElementsPermitted(wildcardShareId, action)) return;
					//if(core.isShareElementsPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, wildcardShareId)) return;
				}
			}
			
			// Checks rights on category instance
			String shareId = shareCache.getShareFolderIdByFolderId(categoryId);
			if (shareId == null) throw new WTException("categoryToLeafShareId({0}) -> null", categoryId);
			if (CheckRightsTarget.FOLDER.equals(target)) {
				if (core.isShareFolderPermitted(shareId, action)) return;
			} else if (CheckRightsTarget.ELEMENTS.equals(target)) {
				if (core.isShareElementsPermitted(shareId, action)) return;
				//if(core.isShareElementsPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, wildcardShareId)) return;
			}
			
			String exMsg = null;
			if (CheckRightsTarget.FOLDER.equals(target)) {
				exMsg = "Action '{}' not allowed for '{}' on folder '{}' [{}, {}, {}]";
			} else if (CheckRightsTarget.ELEMENTS.equals(target)) {
				exMsg = "Action '{}' not allowed for '{}' on elements of folder '{}' [{}, {}, {}]";
			}
			throw new AuthException(exMsg, action, runPid, categoryId, shareId, GROUPNAME_CATEGORY, targetPid.toString());
		}
	}
	
	private OCategory fillOCategoryWithDefaults(OCategory tgt) {
		if (tgt != null) {
			ContactsServiceSettings ss = getServiceSettings();
			if (tgt.getDomainId() == null) tgt.setDomainId(getTargetProfileId().getDomainId());
			if (tgt.getUserId() == null) tgt.setUserId(getTargetProfileId().getUserId());
			if (tgt.getBuiltIn() == null) tgt.setBuiltIn(false);
			if (StringUtils.isBlank(tgt.getProvider())) tgt.setProvider(EnumUtils.toSerializedName(Category.Provider.LOCAL));
			if (StringUtils.isBlank(tgt.getColor())) tgt.setColor("#FFFFFF");
			if (StringUtils.isBlank(tgt.getSync())) tgt.setSync(EnumUtils.toSerializedName(ss.getDefaultCategorySync()));
			if (tgt.getIsDefault() == null) tgt.setIsDefault(false);
			//if (fill.getIsPrivate() == null) fill.setIsPrivate(false);
			
			Category.Provider provider = EnumUtils.forSerializedName(tgt.getProvider(), Category.Provider.class);
			if (Category.Provider.CARDDAV.equals(provider)) {
				tgt.setIsDefault(false);
			}
		}
		return tgt;
	}
	
	private OContact fillDefaultsForInsert(OContact tgt) {
		if (tgt != null) {
			if (StringUtils.isBlank(tgt.getPublicUid())) {
				tgt.setPublicUid(ContactsUtils.buildContactUid(tgt.getContactId(), WT.getDomainInternetName(getTargetProfileId().getDomainId())));
			}
			if (StringUtils.isBlank(tgt.getHref())) tgt.setHref(ContactsUtils.buildHref(tgt.getPublicUid()));
			if (!tgt.getIsList()) {
				if (StringUtils.isBlank(tgt.getDisplayName())) tgt.setDisplayName(ContactBase.buildFullName(tgt.getFirstname(), tgt.getLastname()));
			} else {
				// Compose list workEmail as: "list-{contactId}@{serviceId}"
				tgt.setWorkEmail(RCPT_ORIGIN_LIST + "-" + tgt.getContactId() + "@" + SERVICE_ID);
			}
		}
		return tgt;
	}
	
	private OContact fillDefaultsForUpdate(OContact tgt) {
		if (tgt != null) {
			if (!tgt.getIsList()) {
				if (StringUtils.isBlank(tgt.getDisplayName())) tgt.setDisplayName(ContactBase.buildFullName(tgt.getFirstname(), tgt.getLastname()));
			} else {
				// Compose list workEmail as: "list-{contactId}@{serviceId}"
				tgt.setWorkEmail(RCPT_ORIGIN_LIST + "-" + tgt.getContactId() + "@" + SERVICE_ID);
			}
		}
		return tgt;
	}
	
	/*
	private String buildSearchfield(OContact contact) {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.defaultString(contact.getLastname()));
		sb.append(StringUtils.defaultString(contact.getFirstname()));
		return sb.toString().toLowerCase();
	}
	
	private String buildSearchfield(CoreManager coreMgr, OContact contact) {
		StringBuilder sb = new StringBuilder();
		sb.append(buildSearchfield(contact));
		
		String masterData = null;
		if (!StringUtils.isEmpty(contact.getCompany())) {
			try {
				masterData = lookupMasterDataDescription(coreMgr, contact.getCompany());
			} catch (WTException ex) {
				logger.warn("Problems looking-up master data description", ex);
			}
		}
		final String company = StringUtils.defaultIfBlank(masterData, contact.getCompany());
		sb.append(StringUtils.defaultString(company).toLowerCase());
		return sb.toString();
	}
	*/
	
	private ReminderInApp createAnniversaryInAppReminder(Locale locale, boolean birthday, VContact contact, DateTime date) {
		String type = (birthday) ? "birthday" : "anniversary";
		String resKey = (birthday) ? ContactsLocale.REMINDER_TITLE_BIRTHDAY : ContactsLocale.REMINDER_TITLE_ANNIVERSARY;
		String title = MessageFormat.format(lookupResource(locale, resKey), ContactBase.buildFullName(contact.getFirstname(), contact.getLastname()));
		
		ReminderInApp alert = new ReminderInApp(SERVICE_ID, contact.getCategoryProfileId(), type, contact.getContactId().toString());
		alert.setTitle(title);
		alert.setDate(date);
		alert.setTimezone(date.getZone().getID());
		return alert;
	}
	
	private ReminderEmail createAnniversaryEmailReminder(Locale locale, InternetAddress recipient, boolean birthday, VContact contact, DateTime date) {
		String type = (birthday) ? "birthday" : "anniversary";
		String resKey = (birthday) ? ContactsLocale.REMINDER_TITLE_BIRTHDAY : ContactsLocale.REMINDER_TITLE_ANNIVERSARY;
		String fullName = ContactBase.buildFullName(contact.getFirstname(), contact.getLastname());
		String title = MessageFormat.format(lookupResource(locale, resKey), StringUtils.trim(fullName));
		String subject = NotificationHelper.buildSubject(locale, SERVICE_ID, title);
		String body = null;
		try {
			body = TplHelper.buildAnniversaryEmail(locale, birthday, recipient.getAddress(), fullName);
		} catch(IOException | TemplateException ex) {
			logger.error("Error building anniversary email", ex);
		}
		
		ReminderEmail alert = new ReminderEmail(SERVICE_ID, contact.getCategoryProfileId(), type, contact.getContactId().toString());
		alert.setSubject(subject);
		alert.setBody(body);
		return alert;
	}
	
	public class RootRecipientsProvider extends RecipientsProviderBase {
		public final UserProfileId ownerId;
		private final Collection<Integer> categoryIds;
		
		public RootRecipientsProvider(String id, String description, UserProfileId ownerId, Collection<Integer> categoryIds) {
			super(id, description);
			this.ownerId = ownerId;
			this.categoryIds = categoryIds;
		}
		
		@Override
		public List<Recipient> getRecipients(RecipientFieldType fieldType, String queryText, int max) {
			ContactDAO dao = ContactDAO.getInstance();
			ArrayList<Recipient> items = new ArrayList<>();
			Connection con = null;
			
			boolean listsOnly=fieldType.equals(RecipientFieldType.LIST);
			if (listsOnly) fieldType=RecipientFieldType.EMAIL;
			
			try {
				con = WT.getConnection(SERVICE_ID);
				
				RecipientFieldCategory[] fieldCategories = new RecipientFieldCategory[]{
					RecipientFieldCategory.WORK, RecipientFieldCategory.HOME, RecipientFieldCategory.OTHER
				};
				for(RecipientFieldCategory fieldCategory : fieldCategories) {
					if (!dao.hasTableFieldFor(fieldType, fieldCategory)) continue;
					
					final String origin = getContactOriginBy(fieldCategory);
					final List<VContact> vconts = dao.viewRecipientsByFieldCategoryQuery(con, fieldType, fieldCategory, categoryIds, queryText);
					for(VContact vcont : vconts) {
						final String value = vcont.getValueBy(fieldType, fieldCategory);
						final String recipientId=vcont.getContactId()!=null?vcont.getContactId().toString():null;
						if (vcont.getIsList() && fieldCategory.equals(RecipientFieldCategory.WORK) && fieldType.equals(RecipientFieldType.EMAIL)) {
							items.add(new Recipient(this.getId(), this.getDescription(), RCPT_ORIGIN_LIST, vcont.getDisplayName(), value, Recipient.Type.TO, recipientId));
							
						} else if (!listsOnly) {
							if (fieldType.equals(RecipientFieldType.EMAIL) && !InternetAddressUtils.isAddressValid(value)) continue;
							
							String personal = vcont.getDisplayName();
							if (StringUtils.isBlank(personal)) personal = InternetAddressUtils.toPersonal(vcont.getFirstname(), vcont.getLastname());
							items.add(new Recipient(this.getId(), this.getDescription(), origin, personal, value, Recipient.Type.TO, recipientId));
						}
					}
				}
				
				return items;
				
			} catch(Throwable t) {
				logger.error("Error listing recipients", t);
				return null;
			} finally {
				DbUtils.closeQuietly(con);
			}
		}
		
		@Override
		public List<Recipient> expandToRecipients(String virtualRecipient) {
			ListRecipientDAO dao = ListRecipientDAO.getInstance();
			ArrayList<Recipient> items = new ArrayList<>();
			Connection con = null;
			
			try {
				con = WT.getConnection(SERVICE_ID);
				Integer contactId = ContactsUtils.virtualRecipientToListId(virtualRecipient);
				if (contactId != null) {
					UserProfileId pid = new UserProfileId(getId());
					List<VListRecipient> recipients = dao.selectByProfileContact(con, pid.getDomainId(), pid.getUserId(), contactId);
					for (VListRecipient recipient : recipients) {
						Recipient.Type rcptType = EnumUtils.forSerializedName(recipient.getRecipientType(), Recipient.Type.class);
						InternetAddress ia = InternetAddressUtils.toInternetAddress(recipient.getRecipient());
						if (ia != null) {
							items.add(new Recipient(this.getId(), this.getDescription(), RCPT_ORIGIN_LISTITEM, ia.getPersonal(), ia.getAddress(), rcptType));
						}
					}
				} else {
					throw new WTException("Bad key format [{0}]", virtualRecipient);
				}
				
				return items;
				
			} catch(Exception ex) {
				logger.error("Error listing recipients", ex);
				return null;
			} finally {
				DbUtils.closeQuietly(con);
			}
		}
		
		private String getContactOriginBy(RecipientFieldCategory fieldCategory) {
			if (fieldCategory.equals(RecipientFieldCategory.WORK)) {
				return RCPT_ORIGIN_CONTACT_WORK;
			} else if (fieldCategory.equals(RecipientFieldCategory.HOME)) {
				return RCPT_ORIGIN_CONTACT_HOME;
			} else if (fieldCategory.equals(RecipientFieldCategory.OTHER)) {
				return RCPT_ORIGIN_CONTACT_OTHER;
			} else {
				return null;
			}
		}
	}
	
	private class ContactBatchImportBeanHandler extends BatchBeanHandler<ContactInput> {
		private final ArrayList<ContactInput> buffer;
		private int insertedCount = 0;
		private int categoryId;
		private Connection con;
		private CoreManager coreMgr;
		
		public ContactBatchImportBeanHandler(int categoryId, Connection con, CoreManager coreMgr) {
			super();
			this.batchSize = ContactsProps.getBatchSize(WT.getProperties());
			this.buffer = new ArrayList<>(batchSize);
			this.categoryId = categoryId;
			this.con = con;
			this.coreMgr = coreMgr;
		}
		
		@Override
		protected int getCurrentBeanBufferSize() {
			return buffer.size();
		}
		
		@Override
		protected void clearBeanBuffer() {
			buffer.clear();
		}

		@Override
		protected void addBeanToBuffer(ContactInput bean) {
			buffer.add(bean);
		}
		
		public int getInsertedCount() {
			return insertedCount;
		}
		
		@Override
		public boolean handleBufferedBeans() {
			try {
				insertedCount = insertedCount + doBatchInsertContacts(coreMgr, con, categoryId, buffer);
				return true;
			} catch(Throwable t) {
				lastException = t;
				return false;
			}
		}
	}
	
	private static class ImportException extends Exception {
		
		public ImportException(Throwable cause) {
			super(cause);
		}
		
		public ImportException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
	public static class CategoryContacts {
		public final OCategory folder;
		public final List<VContact> contacts;
		
		public CategoryContacts(OCategory folder, List<VContact> contacts) {
			this.folder = folder;
			this.contacts = contacts;
		}
	}
	
	public static class ProbeCategoryRemoteUrlResult {
		public final String displayName;
		
		public ProbeCategoryRemoteUrlResult(String displayName) {
			this.displayName = displayName;
		}
	}
	
	private static class ContactInsertResult {
		public final OContact ocontact;
		public final OContactPicture opicture;
		public final Set<String> otags;
		public final List<OContactAttachment> oattachments;
		public final List<OContactCustomValue> ocustomvalues;
		
		public ContactInsertResult(OContact ocontact, OContactPicture opicture, Set<String> otags, List<OContactAttachment> oattachments, ArrayList<OContactCustomValue> ocustomvalues) {
			this.ocontact = ocontact;
			this.opicture = opicture;
			this.otags = otags;
			this.oattachments = oattachments;
			this.ocustomvalues = ocustomvalues;
		}
	}
	
	private static class ContactsListInsertResult {
		public final OContact ocontact;
		public final List<OListRecipient> orecipients;
		public final Set<String> otags;
		
		public ContactsListInsertResult(OContact ocontact, List<OListRecipient> orecipients, Set<String> otags) {
			this.ocontact = ocontact;
			this.orecipients = orecipients;
			this.otags = otags;
		}
	}
	
	private class OwnerCache extends AbstractMapCache<Integer, UserProfileId> {

		@Override
		protected void internalInitCache(Map<Integer, UserProfileId> mapObject) {}

		@Override
		protected void internalMissKey(Map<Integer, UserProfileId> mapObject, Integer key) {
			try {
				UserProfileId owner = findCategoryOwner(key);
				if (owner == null) throw new WTException("Owner not found [{0}]", key);
				mapObject.put(key, owner);
			} catch(WTException ex) {
				logger.trace("OwnerCache miss", ex);
			}
		}
	}
	
	private class ShareCache extends AbstractShareCache<Integer, ShareRootCategory> {

		@Override
		protected void internalInitCache() {
			final CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
			try {
				for (ShareRootCategory root : internalListIncomingCategoryShareRoots()) {
					shareRoots.add(root);
					ownerToShareRoot.put(root.getOwnerProfileId(), root);
					
					// While getting folders, we need to keep implicit order defined
					// in DB access layer. When dealing with wildcards, it's already
					// ensured by the situation while with explicit share we have to
					// use the internal query to ensure the correct semantic 
					// ordering of folder IDs!
					
					boolean wildcardFound = false;
					Map<Integer, OShare> explicitShares = new LinkedHashMap<>();
					for (OShare folder : coreMgr.listIncomingShareFolders(root.getShareId(), GROUPNAME_CATEGORY)) {
						if (folder.hasWildcard()) {
							wildcardFound = true;
							final UserProfileId ownerPid = coreMgr.userUidToProfileId(folder.getUserUid());
							for (Integer category : listCategoryIds(ownerPid)) {
								folderTo.add(category);
								rootShareToFolderShare.put(root.getShareId(), category);
								folderToWildcardShareFolder.put(category, folder.getShareId().toString());
							}
							ownerToWildcardShareFolder.put(ownerPid, folder.getShareId().toString());
							break; // If we have wildcard, it's enought...skip other folders!
							
						} else {
							explicitShares.put(Integer.valueOf(folder.getInstance()), folder);
						}
					}
					
					if (!wildcardFound) {
						for (Integer explicitCategoryId : listCategoryIdsIn(root.getOwnerProfileId(), explicitShares.keySet())) {
							folderTo.add(explicitCategoryId);
							rootShareToFolderShare.put(root.getShareId(), explicitCategoryId);
							final OShare share = explicitShares.get(explicitCategoryId);
							folderToShareFolder.put(explicitCategoryId, share.getShareId().toString());
						}
					}
				}
				ready = true;
			} catch(WTException ex) {
				throw new WTRuntimeException(ex.getMessage());
			}
		}
	}
	
	public boolean isMailchimpEnabled() {
		return hasMailchimp && (RunContext.isImpersonated()||RunContext.isPermitted(true, SERVICE_ID, "MAILCHIMP"));
	}
	
	public ApiClient getMailchimpApiClient() throws WTException {
		String apikey=getUserSettings().getMailchimpApiKey();
		if (StringUtils.isEmpty(apikey)) throw new WTException("No Mailchimp ApiKey configured!");
		ApiClient api=new ApiClient();
		int ix=apikey.indexOf("-");
		if (ix<0) throw new WTException("Mailchimp ApiKey "+apikey+" does not contain server name!");
		String serverName=apikey.substring(ix+1);
		api.setBasePath("https://"+serverName+".api.mailchimp.com/3.0");
		api.addDefaultHeader("Authorization", "Bearer "+apikey);
		return api;
	}
	
	private enum CheckRightsTarget {
		FOLDER, ELEMENTS
	}
	
	private enum AuditContext {
		CATEGORY, CONTACT
	}
	
	private enum AuditAction {
		CREATE, UPDATE, DELETE, MOVE, TAG
	}
	
	private void writeAuditLog(AuditContext context, AuditAction action, Object reference, Object data) {
		writeAuditLog(EnumUtils.getName(context), EnumUtils.getName(action), (reference != null) ? String.valueOf(reference) : null, (data != null) ? String.valueOf(data) : null);
	}
	
	private void writeAuditLog(AuditContext context, AuditAction action, Collection<AuditReferenceDataEntry> entries) {
		writeAuditLog(EnumUtils.getName(context), EnumUtils.getName(action), entries);
	}
	
	private class AuditContactObj implements AuditReferenceDataEntry {
		public final int contactId;
		
		public AuditContactObj(int contactId) {
			this.contactId = contactId;
		}

		@Override
		public String getReference() {
			return String.valueOf(contactId);
		}

		@Override
		public String getData() {
			return null;
		}
	}
	
	private class AuditContactMoveObj implements AuditReferenceDataEntry {
		public final int contactId;
		public final int origCategoryId;
		
		public AuditContactMoveObj(int contactId, int origCategoryId) {
			this.contactId = contactId;
			this.origCategoryId = origCategoryId;
		}

		@Override
		public String getReference() {
			return String.valueOf(contactId);
		}

		@Override
		public String getData() {
			return String.valueOf(origCategoryId);
		}
	}
	
	private class AuditContactCopyObj implements AuditReferenceDataEntry {
		public final int contactId;
		public final int origContactId;
		
		public AuditContactCopyObj(int contactId, int origContactId) {
			this.contactId = contactId;
			this.origContactId = origContactId;
		}

		@Override
		public String getReference() {
			return String.valueOf(contactId);
		}

		@Override
		public String getData() {
			return String.valueOf(origContactId);
		}
	}
	
	/*
	private static class ContactResultBeanHandler extends DefaultBeanHandler<ContactReadResult> {
		private LogEntries log;
		public ArrayList<ContactReadResult> parsed;
		
		public ContactResultBeanHandler(LogEntries log) {
			this.log = log;
			parsed = new ArrayList<>();
		}
		
		@Override
		public void handle(ContactReadResult bean, LogEntries log) throws Exception {
			parsed.add(bean);
			log.addAll(log);
		}
	}
	
	public LogEntries importContacts22(int categoryId, ContactFileReader rea, File file, String mode) throws WTException {
		LogEntries log = new LogEntries();
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(categoryId, "CREATE"); // Rights check!
			if(mode.equals("copy")) checkRightsOnCategoryFolder(categoryId, "DELETE"); // Rights check!
			
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Started at {0}", new DateTime()));
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Reading source file..."));
			
			ContactResultBeanHandler handler = new ContactResultBeanHandler(log);
			try {
				rea.readContacts(file, handler);
			} catch(IOException | FileReaderException ex) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "Unable to complete reading. Reason: {0}", ex.getMessage()));
				throw new WTException(ex);
			}
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contact/s found!", handler.parsed.size()));
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			if(mode.equals("copy")) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Cleaning previous contacts..."));
				int del = doDeleteContactsByCategory(con, categoryId, false);
				log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contact/s deleted!", del));
			}
			
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Importing..."));
			int count = 0;
			for(ContactReadResult parse : handler.parsed) {
				parse.contact.setCategoryId(categoryId);
				try {
					doInsertContact(con, false, parse.contact, parse.picture);
					DbUtils.commitQuietly(con);
					count++;
				} catch(Exception ex) {
					logger.trace("Error inserting contact", ex);
					DbUtils.rollbackQuietly(con);
					log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "Unable to import contact [{0}, {1}, {2}]. Reason: {3}", parse.contact.getFirstName(), parse.contact.getLastName(), parse.contact.getPublicUid(), ex.getMessage()));
				}
			}
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contact/s imported!", count));
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Ended at {0}", new DateTime()));
		}
		return log;
	}
	*/
	
	/*
	public LogEntries importVCard(int categoryId, InputStream is, String mode) throws WTException {
		LogEntries log = new LogEntries();
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(categoryId, CheckRightsTarget.ELEMENTS, "CREATE"); // Rights check!
			if(mode.equals("copy")) checkRightsOnCategoryFolder(categoryId, CheckRightsTarget.ELEMENTS, "DELETE"); // Rights check!
			
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Started at {0}", new DateTime()));
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Reading vCard file..."));
			ArrayList<ParseResult> parsed = null;
			try {
				parsed = VCardHelper.parseVCard(log, is);
			} catch(IOException ex) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "Unable to complete parsing. Reason: {0}", ex.getMessage()));
				throw new WTException(ex);
			}
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contacts/s found!", parsed.size()));
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			if(mode.equals("copy")) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Cleaning previous contacts..."));
				int del = doDeleteContactsByCategory(con, categoryId, false);
				log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contact/s deleted!", del));
			}
			
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Importing..."));
			int count = 0;
			for(ParseResult parse : parsed) {
				parse.contact.setCategoryId(categoryId);
				try {
					doInsertContact(con, false, parse.contact, parse.picture);
					DbUtils.commitQuietly(con);
					count++;
				} catch(Exception ex) {
					logger.trace("Error inserting contact", ex);
					DbUtils.rollbackQuietly(con);
					log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "Unable to import contact [{0}, {1}, {2}]. Reason: {3}", parse.contact.getFirstName(), parse.contact.getLastName(), parse.contact.getPublicUid(), ex.getMessage()));
				}
			}
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "{0} contact/s imported!", count));
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
			log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Ended at {0}", new DateTime()));
			//TODO: inviare email report
			//for(LogEntry entry : log) {
			//	logger.trace("{}", ((MessageLogEntry)entry).getMessage());
			//}
		}
		return log;
	}
	*/
}
