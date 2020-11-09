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

import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.IdentifierUtils;
import com.sonicle.commons.InternetAddressUtils;
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.LangUtils.CollectionChangeSet;
import com.sonicle.commons.PathUtils;
import com.sonicle.commons.db.DbUtils;
import com.sonicle.commons.time.DateTimeUtils;
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
import com.sonicle.webtop.contacts.bol.OContactPicture;
import com.sonicle.webtop.contacts.bol.OContactPictureMetaOnly;
import com.sonicle.webtop.contacts.bol.OContactVCard;
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
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactPictureWithBytesOld;
import com.sonicle.webtop.contacts.model.ContactsList;
import com.sonicle.webtop.contacts.model.ContactsListRecipient;
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
import com.sonicle.webtop.contacts.io.input.ContactFileReader;
import com.sonicle.webtop.contacts.model.BaseContact;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.CategoryRemoteParameters;
import com.sonicle.webtop.contacts.model.ContactAttachment;
import com.sonicle.webtop.contacts.model.ContactAttachmentWithBytes;
import com.sonicle.webtop.contacts.model.ContactAttachmentWithStream;
import com.sonicle.webtop.contacts.model.ContactObject;
import com.sonicle.webtop.contacts.model.ContactObjectChanged;
import com.sonicle.webtop.contacts.model.ContactObjectWithBean;
import com.sonicle.webtop.contacts.model.ContactObjectWithVCard;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactLookup;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.model.ContactPictureWithBytes;
import com.sonicle.webtop.contacts.model.ContactQuery;
import com.sonicle.webtop.contacts.model.ListContactsResult;
import com.sonicle.webtop.contacts.model.Grouping;
import com.sonicle.webtop.contacts.model.ShowBy;
import com.sonicle.webtop.contacts.model.ContactType;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.provider.RecipientsProviderBase;
import com.sonicle.webtop.core.app.sdk.AuditReferenceDataEntry;
import com.sonicle.webtop.core.app.sdk.WTNotFoundException;
import com.sonicle.webtop.core.app.util.ExceptionUtils;
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
import com.sonicle.webtop.core.io.BatchBeanHandler;
import com.sonicle.webtop.core.io.input.FileReaderException;
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
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
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
import javax.mail.internet.InternetAddress;
import org.apache.commons.codec.digest.DigestUtils;
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
	
	private static final ConcurrentHashMap<String, UserProfileId> pendingRemoteCategorySyncs = new ConcurrentHashMap<>();
	
	public ContactsManager(boolean fastInit, UserProfileId targetProfileId) {
		super(fastInit, targetProfileId);
		VCARD_CARETENCODINGENABLED = Boolean.valueOf(WT.getProperties().getProperty("webtop.contacts.vcardwriter.caretencodingenabled", "true"));
		if (!fastInit) {
			shareCache.init();
		}
	}
	
	private CoreManager getCoreManager() {
		return WT.getCoreManager(getTargetProfileId());
	}
	
	private ContactsServiceSettings getServiceSettings() {
		return new ContactsServiceSettings(SERVICE_ID, getTargetProfileId().getDomainId());
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
	public Set<Integer> listAllCategoryIds() throws WTException {
		return Stream.concat(listCategoryIds().stream(), listIncomingCategoryIds().stream())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	@Override
	public Map<Integer, Category> listCategories() throws WTException {
		return listCategories(getTargetProfileId());
	}
	
	private Set<Integer> listCategoryIds(UserProfileId pid) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			return catDao.selectIdsByProfile(con, pid.getDomainId(), pid.getUserId());
			
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
			cat.setIsDefault(true);
			cat = doCategoryInsert(con, cat);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CATEGORY, AuditAction.CREATE, cat.getCategoryId(), null);
			}
			
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
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CATEGORY, AuditAction.DELETE, categoryId, null);
				writeAuditLog(AuditContext.CATEGORY, AuditAction.DELETE, "*", categoryId);
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
	public List<ContactObject> listContactObjects(int categoryId, ContactObjectOutputType outputType) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ");
			
			ArrayList<ContactObject> items = new ArrayList<>();
			Map<String, List<VContactObject>> map = contDao.viewContactObjectByCategory(con, categoryId);
			for (List<VContactObject> vconts : map.values()) {
				if (vconts.isEmpty()) continue;
				VContactObject vcont = vconts.get(vconts.size()-1);
				if (vconts.size() > 1) {
					logger.trace("Many contacts ({}) found for same href [{} -> {}]", vconts.size(), vcont.getHref(), vcont.getContactId());
				}
				
				items.add(doContactObjectPrepare(con, vcont, outputType));
			}
			return items;
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public CollectionChangeSet<ContactObjectChanged> listContactObjectsChanges(int categoryId, DateTime since, Integer limit) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			checkRightsOnCategory(categoryId, CheckRightsTarget.FOLDER, "READ");
			
			ArrayList<ContactObjectChanged> inserted = new ArrayList<>();
			ArrayList<ContactObjectChanged> updated = new ArrayList<>();
			ArrayList<ContactObjectChanged> deleted = new ArrayList<>();
			
			if (limit == null) limit = Integer.MAX_VALUE;
			if (since == null) {
				List<VContactObjectChanged> vconts = contDao.viewLiveContactObjectsChangedByCategory(con, categoryId, limit);
				for (VContactObjectChanged vcont : vconts) {
					inserted.add(new ContactObjectChanged(vcont.getContactId(), vcont.getRevisionTimestamp(), vcont.getHref()));
				}
			} else {
				List<VContactObjectChanged> vconts = contDao.viewChangedByCategorySince(con, categoryId, since, limit);
				for (VContactObjectChanged vcont : vconts) {
					Contact.RevisionStatus revStatus = EnumUtils.forSerializedName(vcont.getRevisionStatus(), Contact.RevisionStatus.class);
					if (Contact.RevisionStatus.DELETED.equals(revStatus)) {
						deleted.add(new ContactObjectChanged(vcont.getContactId(), vcont.getRevisionTimestamp(), vcont.getHref()));
					} else {
						if (Contact.RevisionStatus.NEW.equals(revStatus) || (vcont.getCreationTimestamp().compareTo(since) >= 0)) {
							inserted.add(new ContactObjectChanged(vcont.getContactId(), vcont.getRevisionTimestamp(), vcont.getHref()));
						} else {
							updated.add(new ContactObjectChanged(vcont.getContactId(), vcont.getRevisionTimestamp(), vcont.getHref()));
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
	
	@Override
	public ContactObject getContactObject(int contactId, ContactObjectOutputType outputType) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			VContactObject cobj = contDao.viewContactObjectById(con, contactId);
			if (cobj != null) {
				checkRightsOnCategory(cobj.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
				return doContactObjectPrepare(con, cobj, outputType);
			} else {
				return null;
			}
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void addContactObject(int categoryId, String href, VCard vCard) throws WTException {
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
	}
	
	@Override
	public void updateContactObject(int categoryId, String href, VCard vCard) throws WTException {
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
	}
	
	@Override
	public void deleteContactObject(int categoryId, String href) throws WTException {
		int contactId = getContactIdByCategoryHref(categoryId, href, true);
		deleteContact(contactId);
	}
	
	private int getContactIdByCategoryHref(int categoryId, String href, boolean throwExIfManyMatchesFound) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			List<Integer> ids = contDao.selectAliveIdsByCategoryHrefs(con, categoryId, href);
			if (ids.isEmpty()) throw new WTNotFoundException("Contact card not found [{}, {}]", categoryId, href);
			if (throwExIfManyMatchesFound && (ids.size() > 1)) throw new WTException("Many matches for href [{}]", href);
			return ids.get(ids.size()-1);
			
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
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
	
	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public ListContactsResult listContacts(Collection<Integer> categoryIds, boolean listOnly, Grouping groupBy, ShowBy showBy, String pattern, int page, int limit, boolean returnFullCount) throws WTException {
		ContactType type = listOnly ? ContactType.LIST : ContactType.ANY;
		return listContacts(categoryIds, type, groupBy, showBy, ContactQuery.toCondition(pattern), page, limit, returnFullCount);
	}
	
	@Override
	public ListContactsResult listContacts(Collection<Integer> categoryIds, ContactType type, Grouping groupBy, ShowBy showBy, String pattern) throws WTException {
		return listContacts(categoryIds, type, groupBy, showBy, ContactQuery.toCondition(pattern), 1, Integer.MAX_VALUE, false);
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
	public Contact getContact(int contactId) throws WTException {
		return getContact(contactId, true, true, true, true);
	}
	
	public Contact getContact(int contactId, boolean processPicture, boolean processAttachments, boolean processTags, boolean processCustomValues) throws WTException {
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			Contact cont = doContactGet(con, contactId, processPicture, processAttachments, processTags, processCustomValues);
			if (cont == null) return null;
			checkRightsOnCategory(cont.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
			
			return cont;
		
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactAttachmentWithBytes getContactAttachment(int contactId, String attachmentId) throws WTException {
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
			
			OContactAttachmentData oattData = attDao.selectBytes(con, attachmentId);
			return ManagerUtils.fillContactAttachment(new ContactAttachmentWithBytes(oattData.getBytes()), oatt);
		
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactCompany getContactCompany(int contactId) throws WTException {
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
	public Map<String, CustomFieldValue> getContactCustomValues(int contactId) throws WTException {
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
	public Contact addContact(Contact contact) throws WTException {
		return addContact(contact, null);
	}
	
	@Override
	public Contact addContact(Contact contact, String vCardRawData) throws WTException {
		CoreManager coreMgr = getCoreManager();
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(contact.getCategoryId(), CheckRightsTarget.ELEMENTS, "CREATE");
			con = WT.getConnection(SERVICE_ID, false);
			
			String provider = catDao.selectProviderById(con, contact.getCategoryId());
			if (Category.isProviderRemote(provider)) throw new WTException("Category is remote and therefore read-only [{}]", contact.getCategoryId());
			
			Set<String> validTags = coreMgr.listTagIds();
			ContactInsertResult result = doContactInsert(coreMgr, con, false, contact, vCardRawData, true, true, true, true, validTags);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.CREATE, result.ocontact.getContactId(), null);
			}
			
			Contact newContact = ManagerUtils.createContact(result.ocontact);
			newContact.setPicture(ManagerUtils.createContactPicture(result.opicture));
			newContact.setAttachments(ManagerUtils.createContactAttachmentList(result.oattachments));
			return newContact;
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContact(Contact contact) throws WTException {
		updateContact(contact, false);
	}
	
	@Override
	public void updateContact(Contact contact, boolean processPicture) throws WTException {
		updateContact(contact, processPicture, true, true, true);
	}
	
	public void updateContact(Contact contact, boolean processPicture, boolean processAttachments, boolean processTags, boolean processCustomValues) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(contact.getCategoryId(), CheckRightsTarget.ELEMENTS, "UPDATE");
			con = WT.getConnection(SERVICE_ID, false);
			
			String provider = catDao.selectProviderById(con, contact.getCategoryId());
			if (Category.isProviderRemote(provider)) throw new WTException("Category is remote and therefore read-only [{}]", contact.getCategoryId());
			
			Set<String> validTags = processTags ? coreMgr.listTagIds() : null;
			boolean ret = doContactUpdate(coreMgr, con, false, contact, processPicture, processAttachments, processTags, processCustomValues, validTags);
			if (!ret) throw new WTNotFoundException("Contact not found [{}]", contact.getContactId());
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, contact.getContactId(), null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactPictureWithBytes getContactPicture(int contactId) throws WTException {
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
	public void updateContactPicture(int contactId, ContactPictureWithBytesOld picture) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			if (picture == null) throw new WTException("Picture is null");
			OContact ocont = contDao.selectById(con, contactId);
			if (ocont == null || ocont.getIsList()) throw new WTNotFoundException("Contact not found [{}]", contactId);
			checkRightsOnCategory(ocont.getCategoryId(), CheckRightsTarget.ELEMENTS, "UPDATE");
			
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
	public void deleteContact(int contactId) throws WTException {
		deleteContact(Arrays.asList(contactId));
	}
	
	@Override
	public void deleteContact(Collection<Integer> contactIds) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			Set<Integer> deleteOkCache = new HashSet<>();
			Map<Integer, Integer> map = contDao.selectCategoriesByIdsType(con, contactIds, false);
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
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void moveContact(boolean copy, int contactId, int targetCategoryId) throws WTException {
		moveContact(copy, Arrays.asList(contactId), targetCategoryId);
	}
	
	@Override
	public void moveContact(boolean copy, Collection<Integer> contactIds, int targetCategoryId) throws WTException {
		CoreManager coreMgr = getCoreManager();
		CategoryDAO catDao = CategoryDAO.getInstance();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(targetCategoryId, CheckRightsTarget.ELEMENTS, "CREATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			
			String provider = catDao.selectProviderById(con, targetCategoryId);
			if (Category.isProviderRemote(provider)) throw new WTException("Category is remote and therefore read-only [{}]", targetCategoryId);
			
			Set<Integer> readOkCache = new HashSet<>();
			Set<Integer> deleteOkCache = new HashSet<>();
			Map<Integer, Integer> map = contDao.selectCategoriesByIdsType(con, contactIds, false);
			ArrayList<AuditReferenceDataEntry> copied = new ArrayList<>();
			ArrayList<AuditReferenceDataEntry> moved = new ArrayList<>();
			for (Integer contactId : contactIds) {
				if (contactId == null) continue;
				if (!map.containsKey(contactId)) throw new WTNotFoundException("Contact not found [{}]", contactId);
				int categoryId = map.get(contactId);
				checkRightsOnCategory(readOkCache, categoryId, CheckRightsTarget.FOLDER, "READ");
				
				if (copy || (targetCategoryId != categoryId)) {
					if (copy) {
						Contact origContact = doContactGet(con, contactId, true, false, true, true);
						if (origContact == null) throw new WTNotFoundException("Contact not found [{}]", contactId);
						ContactInsertResult result = doContactCopy(coreMgr, con, origContact, targetCategoryId);
						
						copied.add(new AuditContactCopyObj(result.ocontact.getContactId(), origContact.getContactId()));
						
					} else {
						checkRightsOnCategory(deleteOkCache, categoryId, CheckRightsTarget.ELEMENTS, "DELETE");
						boolean ret = doContactMove(con, contactId, targetCategoryId);
						if (!ret) throw new WTNotFoundException("Contact not found [{}]", contactId);
						
						moved.add(new AuditContactMoveObj(contactId, categoryId));
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
	public ContactsList getContactsList(int contactId) throws WTException {
		return getContactsList(contactId, true);
	}
	
	public ContactsList getContactsList(int contactId, boolean processTags) throws WTException {
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			ContactsList contList = doContactsListGet(con, contactId, processTags);
			if (contList == null) return null;
			checkRightsOnCategory(contList.getCategoryId(), CheckRightsTarget.FOLDER, "READ");
			
			return contList;
		
		} catch (Throwable t) {
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void addContactsList(ContactsList list) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(list.getCategoryId(), CheckRightsTarget.ELEMENTS, "CREATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			
			Category category = ManagerUtils.createCategory(catDao.selectById(con, list.getCategoryId()));
			if (category == null) throw new WTException("Unable to get category [{}]", list.getCategoryId());
			if (category.isProviderRemote()) throw new WTException("Category is read only");
			
			Set<String> validTags = coreMgr.listTagIds();
			ContactsListInsertResult result = doContactsListInsert(coreMgr, con, list, true, validTags);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.CREATE, result.ocontact.getContactId(), null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactCategoryTags(final UpdateTagsOperation operation, final int categoryId, final Set<String> tagIds) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		ContactTagDAO ctagDao = ContactTagDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(categoryId, CheckRightsTarget.ELEMENTS, "UPDATE");
			
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
				
			} else if (UpdateTagsOperation.UNSET.equals(operation)) {
				con = WT.getConnection(SERVICE_ID, false);
				ctagDao.deleteByCategoryTags(con, categoryId, tagIds);
			}
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, "*", categoryId);
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
				if (UpdateTagsOperation.RESET.equals(operation)) ctagDao.deleteByCategoriesContacts(con, okCategoryIds, contactIds);
				for (String tagId : okTagIds) {
					ctagDao.insertByCategoriesContacts(con, okCategoryIds, contactIds, tagId);
				}
				
			} else if (UpdateTagsOperation.UNSET.equals(operation)) {
				con = WT.getConnection(SERVICE_ID, false);
				ctagDao.deleteByCategoriesContactsTags(con, okCategoryIds, contactIds, tagIds);
			}
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				ArrayList<AuditReferenceDataEntry> updated = new ArrayList<>();
				Iterator it = contactIds.iterator();
				while (it.hasNext()) updated.add(new AuditContactObj((Integer)it.next()));
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, updated);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactsList(ContactsList list) throws WTException {
		updateContactsList(list, true);
	}
	
	public void updateContactsList(ContactsList list, boolean processTags) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		Connection con = null;
		
		try {
			checkRightsOnCategory(list.getCategoryId(), CheckRightsTarget.ELEMENTS, "UPDATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			Set<String> validTags = processTags ? coreMgr.listTagIds() : null;
			boolean ret = doContactsListUpdate(coreMgr, con, list, processTags, validTags);
			if (!ret) throw new WTNotFoundException("Contacts-list not found [{}]", list.getContactId());
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, list.getContactId(), null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactsListRecipients(int contactsListId, Collection<ContactsListRecipient> recipients, boolean append) throws WTException {
		ContactDAO conDao = ContactDAO.getInstance();
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			OContact cont = conDao.selectById(con, contactsListId);
			if (cont == null || !cont.getIsList()) throw new WTNotFoundException("Contact-list not found [{}]", contactsListId);
			checkRightsOnCategory(cont.getCategoryId(), CheckRightsTarget.ELEMENTS, "UPDATE");
			
			if (!append) lrecDao.deleteByContact(con, contactsListId);
			lrecDao.batchInsert(con, contactsListId, recipients);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.UPDATE, contactsListId, null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteContactsList(int contactsListId) throws WTException {
		ContactDAO condao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			OContact cont = condao.selectById(con, contactsListId);
			if (cont == null || !cont.getIsList()) throw new WTNotFoundException("Contact-list not found [{}]", contactsListId);
			checkRightsOnCategory(cont.getCategoryId(), CheckRightsTarget.ELEMENTS, "DELETE");
			
			boolean ret = doContactDelete(con, contactsListId, true);
			if (!ret) throw new WTNotFoundException("Contact-list not found [{}]", contactsListId);
			
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.DELETE, contactsListId, null);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteContactsList(Collection<Integer> contactsListIds) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			Set<Integer> deleteOkCache = new HashSet<>();
			Map<Integer, Integer> map = contDao.selectCategoriesByIdsType(con, contactsListIds, true);
			ArrayList<AuditReferenceDataEntry> deleted = new ArrayList<>();
			for (Integer contactsListId : contactsListIds) {
				if (contactsListId == null) continue;
				if (!map.containsKey(contactsListId)) throw new WTNotFoundException("Contact-list not found [{}]", contactsListId);
				checkRightsOnCategory(deleteOkCache, map.get(contactsListId), CheckRightsTarget.ELEMENTS, "DELETE");
				
				if (doContactDelete(con, contactsListId, true)) {
					deleted.add(new AuditContactObj(contactsListId));
				} else {
					throw new WTNotFoundException("Contact-list not found [{}]", contactsListId);
				}
			}
			DbUtils.commitQuietly(con);
			if (isAuditEnabled()) {
				writeAuditLog(AuditContext.CONTACT, AuditAction.DELETE, deleted);
			}
			
		} catch (Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw ExceptionUtils.wrapThrowable(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void moveContactsList(boolean copy, int contactsListId, int targetCategoryId) throws WTException {
		moveContactsList(copy, Arrays.asList(contactsListId), targetCategoryId);
	}
	
	@Override
	public void moveContactsList(boolean copy, Collection<Integer> contactsListIds, int targetCategoryId) throws WTException {
		CoreManager coreMgr = getCoreManager();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategory(targetCategoryId, CheckRightsTarget.ELEMENTS, "CREATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			Set<Integer> readOkCache = new HashSet<>();
			Set<Integer> deleteOkCache = new HashSet<>();
			Map<Integer, Integer> map = contDao.selectCategoriesByIdsType(con, contactsListIds, true);
			ArrayList<AuditReferenceDataEntry> copied = new ArrayList<>();
			ArrayList<AuditReferenceDataEntry> moved = new ArrayList<>();
			for (Integer contactListId : contactsListIds) {
				if (contactListId == null) continue;
				if (!map.containsKey(contactListId)) throw new WTNotFoundException("Contact-list not found [{}]", contactListId);
				int categoryId = map.get(contactListId);
				checkRightsOnCategory(readOkCache, categoryId, CheckRightsTarget.FOLDER, "READ");
				
				if (copy || (targetCategoryId != categoryId)) {
					if (copy) {
						ContactsList origContactsList = doContactsListGet(con, contactListId, true);
						if (origContactsList == null) throw new WTNotFoundException("Contact-list not found [{}]", contactListId);
						ContactsListInsertResult result = doContactsListCopy(coreMgr, con, origContactsList, targetCategoryId);
						
						copied.add(new AuditContactCopyObj(result.ocontact.getContactId(), origContactsList.getContactId()));
						
					} else {
						checkRightsOnCategory(deleteOkCache, categoryId, CheckRightsTarget.ELEMENTS, "DELETE");
						boolean ret = doContactsListMove(con, contactListId, targetCategoryId);
						if (!ret) throw new WTNotFoundException("Contact-list not found [{}]", contactListId);
						
						moved.add(new AuditContactMoveObj(contactListId, categoryId));
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
		final VCardInput icalInput = new VCardInput();
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
									final ContactInput ci = icalInput.fromVCardFile(dcard.getCard(), null);
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(href);
									ci.contact.setEtag(dcard.geteTag());
									doContactInsert(coreMgr, con, false, ci.contact, null, true, false, false, false, null);
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
									String prodId = VCardUtils.buildProdId(ManagerUtils.getProductName());
									String hash = DigestUtils.md5Hex(new VCardOutput(prodId).write(dcard.getCard(), true));
									
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
									final ContactInput ci = icalInput.fromVCardFile(dcard.getCard(), null);
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(href);
									ci.contact.setEtag(etag);
									
									if (matchingContactId == null) {
										doContactInsert(coreMgr, con, false, ci.contact, null, true, false, false, false, null);
									} else {
										ci.contact.setContactId(matchingContactId);
										boolean updated = doContactUpdate(coreMgr, con, false, ci.contact, true, false, false, false, null);
										if (!updated) throw new WTException("Contact not found [{}]", ci.contact.getContactId());
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
	
	private ContactObject doContactObjectPrepare(Connection con, VContactObject vcont, ContactObjectOutputType outputType) throws WTException {
		if (ContactObjectOutputType.STAT.equals(outputType)) {
			return ManagerUtils.fillContactCard(new ContactObject(), vcont);
			
		} else {
			ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
			
			Contact cont = ManagerUtils.fillContact(new Contact(), vcont);
			if (vcont.getHasPicture()) {
				OContactPicture opic = cpicDao.select(con, vcont.getContactId());
				if (opic != null) cont.setPicture(ManagerUtils.fillContactPicture(new ContactPictureWithBytes(opic.getBytes()), opic));
			}
			
			if (ContactObjectOutputType.VCARD.equals(outputType)) {
				ContactObjectWithVCard cc = ManagerUtils.fillContactCard(new ContactObjectWithVCard(), vcont);
				
				VCardOutput out = new VCardOutput(VCardUtils.buildProdId(ManagerUtils.getProductName()))
						.withEnableCaretEncoding(VCARD_CARETENCODINGENABLED);
				VCard vCard = out.toVCard(cont);
				if (vcont.getHasVcard()) {
					//TODO: in order to be fully compliant, merge generated vcard with the original one in db table!
				}
				cc.setVcard(out.write(vCard));
				return cc;
				
			} else {
				ContactObjectWithBean cc = ManagerUtils.fillContactCard(new ContactObjectWithBean(), vcont);
				cc.setContact(cont);
				return cc;
			}
		}
	}
	
	private Contact doContactGet(Connection con, int contactId, boolean processPicture, boolean processAttachments, boolean processTags, boolean processCustomValues) throws DAOException, WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactTagDAO tagDao = ContactTagDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		ContactAttachmentDAO attDao = ContactAttachmentDAO.getInstance();
		ContactCustomValueDAO cvalDao = ContactCustomValueDAO.getInstance();
		
		OContact ocont = contDao.selectById(con, contactId);
		if ((ocont == null) || (ocont.getIsList())) return null;
		
		Contact cont = ManagerUtils.createContact(ocont);
		if (processTags) {
			cont.setTags(tagDao.selectTagsByContact(con, contactId));
		}
		if (processPicture) {
			OContactPictureMetaOnly opic = cpicDao.selectMeta(con, contactId);
			cont.setPicture(ManagerUtils.createContactPicture(opic));
		}
		if (processAttachments) {
			List<OContactAttachment> oatts = attDao.selectByContact(con, contactId);
			cont.setAttachments(ManagerUtils.createContactAttachmentList(oatts));
		}
		if (processCustomValues) {
			List<OContactCustomValue> ovals = cvalDao.selectByContact(con, contactId);
			cont.setCustomValues(ManagerUtils.createCustomValuesMap(ovals));
		}
		return cont;
	}
	
	private int doBatchInsertContacts(CoreManager coreMgr, Connection con, int categoryId, ArrayList<Contact> contacts) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ArrayList<OContact> ocontacts = new ArrayList<>();
		//TODO: eventualmente introdurre supporto alle immagini
		
		ArrayList<String> masterDataIds = new ArrayList<>();
		for (Contact contact : contacts) {
			if (contact.hasCompany() && !StringUtils.isBlank(contact.getCompany().getCompanyId())) {
				masterDataIds.add(contact.getCompany().getCompanyId());
			}
		}
		
		Map<String, MasterDataLookup> masterDataMap = null;
		if (!masterDataIds.isEmpty()) masterDataMap = coreMgr.lookupMasterData(masterDataIds);
		
		for (Contact contact : contacts) {
			OContact ocont = ManagerUtils.createOContact(contact);
			ocont.setIsList(false);
			ocont.setCategoryId(categoryId);
			ocont.setContactId(contDao.getSequence(con).intValue());
			fillDefaultsForInsert(ocont);
			if (contact.hasCompany()) {
				ocont.setCompanyData(lookupRealCompanyData(masterDataMap, contact.getCompany()));
			}
			
			ocontacts.add(ocont);
		}
		return contDao.batchInsert(con, ocontacts, BaseDAO.createRevisionTimestamp());
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
	
	private ContactInsertResult doContactInsert(CoreManager coreMgr, Connection con, boolean isList, Contact contact, String rawVCard, boolean processPicture, boolean processAttachments, boolean processTags, boolean processCustomValues, Set<String> validTags) throws DAOException, IOException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactTagDAO tagDao = ContactTagDAO.getInstance();
		ContactCustomValueDAO cvalDao = ContactCustomValueDAO.getInstance();
		
		OContact ocont = ManagerUtils.createOContact(contact);
		ocont.setContactId(contDao.getSequence(con).intValue());
		ocont.setIsList(isList);
		fillDefaultsForInsert(ocont);
		if (!isList && contact.hasCompany()) {
			try {
				ocont.setCompanyData(lookupRealCompanyData(coreMgr, contact.getCompany()));
			} catch(WTException ex) {
				logger.error("Unable to lookup company data", ex);
			}
		}
		
		contDao.insert(con, ocont, BaseDAO.createRevisionTimestamp());
		
		Set<String> otags = null;
		if (processTags && contact.hasTags()) {
			otags = new LinkedHashSet<>();
			for (String tag : contact.getTags()) {
				if (validTags != null && !validTags.contains(tag)) continue;
				//TODO: optimize insertion using multivalue insert
				tagDao.insert(con, ocont.getContactId(), tag);
			}
		}
		
		if (isList) {
			return new ContactInsertResult(ocont, otags, null, null, null);
			
		} else {
			if (!StringUtils.isBlank(rawVCard)) {
				doContactVCardInsert(con, ocont.getContactId(), rawVCard);
			}
			
			OContactPicture ocpic = null;
			if (processPicture && contact.hasPicture()) {
				ContactPicture pic = contact.getPicture();
				if (!(pic instanceof ContactPictureWithBytes)) throw new IOException("Picture bytes not available");
				ocpic = doContactPictureInsert(con, ocont.getContactId(), (ContactPictureWithBytes)pic);
			}
			
			ArrayList<OContactAttachment> oatts = null;
			if (processAttachments && contact.hasAttachments()) {
				oatts = new ArrayList<>();
				for (ContactAttachment att : contact.getAttachments()) {
					if (!(att instanceof ContactAttachmentWithStream)) throw new IOException("Attachment stream not available [" + att.getAttachmentId() + "]");
					oatts.add(doContactAttachmentInsert(con, ocont.getContactId(), (ContactAttachmentWithStream)att));
				}
			}
			
			ArrayList<OContactCustomValue> ocvals = null;
			if (processCustomValues && contact.hasCustomValues()) {
				ocvals = new ArrayList<>(contact.getCustomValues().size());
				for (CustomFieldValue cfv : contact.getCustomValues().values()) {
					OContactCustomValue ocv = ManagerUtils.createOContactCustomValue(cfv);
					ocv.setContactId(ocont.getContactId());
					ocvals.add(ocv);
				}
				cvalDao.batchInsert(con, ocvals);
			}
			
			return new ContactInsertResult(ocont, otags, ocpic, oatts, ocvals);
		}
	}
	
	private boolean doContactUpdate(CoreManager coreMgr, Connection con, boolean isList, Contact contact, boolean processPicture, boolean processAttachments, boolean processTags, boolean processCustomValues, Set<String> validTags) throws DAOException, IOException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactTagDAO tagDao = ContactTagDAO.getInstance();
		ContactAttachmentDAO attDao = ContactAttachmentDAO.getInstance();
		ContactCustomValueDAO cvalDao = ContactCustomValueDAO.getInstance();
		
		OContact ocont = ManagerUtils.createOContact(contact);
		fillDefaultsForUpdate(ocont);
		if (!isList && contact.hasCompany()) {
			try {
				ocont.setCompanyData(lookupRealCompanyData(coreMgr, contact.getCompany()));
			} catch(WTException ex) {
				logger.error("Unable to lookup company data", ex);
			}
		}
		
		boolean ret = false;
		if (isList) {
			ret = contDao.updateList(con, ocont, BaseDAO.createRevisionTimestamp()) == 1;
		} else {
			ret = contDao.update(con, ocont, BaseDAO.createRevisionTimestamp()) == 1;
		}
		
		if (processTags && contact.hasTags()) {
			Set<String> oldTags = tagDao.selectTagsByContact(con, contact.getContactId());
			CollectionChangeSet<String> changeSet = LangUtils.getCollectionChanges(oldTags, contact.getTags());
			for (String tag : changeSet.inserted) {
				if (validTags != null && !validTags.contains(tag)) continue;
				tagDao.insert(con, contact.getContactId(), tag);
			}
			for (String tag : changeSet.deleted) {
				tagDao.delete(con, contact.getContactId(), tag);
			}
		}
		
		if (!isList) {
			if (processPicture) {
				ContactPicture pic = contact.getPicture();
				if (contact.hasPicture()) {
					if (!(pic instanceof ContactPictureWithBytes)) throw new IOException("Picture bytes not available");
					doContactPictureUpdate(con, ocont.getContactId(), (ContactPictureWithBytes)pic);
				} else {
					doContactPictureDelete(con, ocont.getContactId());
				}
			}
			if (processAttachments && contact.hasAttachments()) {
				List<ContactAttachment> oldAtts = ManagerUtils.createContactAttachmentList(attDao.selectByContact(con, contact.getContactId()));
				CollectionChangeSet<ContactAttachment> changeSet = LangUtils.getCollectionChanges(oldAtts, contact.getAttachments());
				
				for (ContactAttachment att : changeSet.inserted) {					
					if (!(att instanceof ContactAttachmentWithStream)) throw new IOException("Attachment stream not available [" + att.getAttachmentId() + "]");
					doContactAttachmentInsert(con, ocont.getContactId(), (ContactAttachmentWithStream)att);
				}
				for (ContactAttachment att : changeSet.updated) {
					if (!(att instanceof ContactAttachmentWithStream)) continue;
					doContactAttachmentUpdate(con, (ContactAttachmentWithStream)att);
				}
				for (ContactAttachment att : changeSet.deleted) {
					attDao.delete(con, att.getAttachmentId());
				}
			}
			if (processCustomValues && contact.hasCustomValues()) {
				ArrayList<String> customFieldIds = new ArrayList<>();
				ArrayList<OContactCustomValue> ocvals = new ArrayList<>(contact.getCustomValues().size());
				for (CustomFieldValue cfv : contact.getCustomValues().values()) {
					OContactCustomValue ocv = ManagerUtils.createOContactCustomValue(cfv);
					ocv.setContactId(ocont.getContactId());
					ocvals.add(ocv);
					customFieldIds.add(ocv.getCustomFieldId());
				}
				//TODO: use upsert when available
				cvalDao.deleteByContactFields(con, ocont.getContactId(), customFieldIds);
				cvalDao.batchInsert(con, ocvals);
			}
		}
		return ret;
	}
	
	private boolean doContactDelete(Connection con, int contactId, boolean logicDelete) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		if (logicDelete) {
			return contDao.logicDeleteById(con, contactId, BaseDAO.createRevisionTimestamp()) == 1;
		} else {
			// List are not supported here
			//doContactPictureDelete(con, contactId);
			//doContactVCardDelete(con, contactId);
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
	
	private ContactInsertResult doContactCopy(CoreManager coreMgr, Connection con, Contact contact, int targetCategoryId) throws DAOException, IOException, WTException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
		
		contact.setCategoryId(targetCategoryId);
		contact.setPublicUid(null); // Reset value in order to make inner function generate new one!
		contact.setHref(null); // Reset value in order to make inner function generate new one!
		
		if (contact.hasPicture()) {
			OContactPicture opic = cpicDao.select(con, contact.getContactId());
			if (opic != null) {
				contact.setPicture(ManagerUtils.fillContactPicture(new ContactPictureWithBytes(opic.getBytes()), opic));
			}
		}
		
		OContactVCard ovca = vcaDao.selectById(con, contact.getContactId());
		String rawVCard = (ovca != null) ? ovca.getRawData() : null;
		//TODO: maybe add support to attachments copy
		
		return doContactInsert(coreMgr, con, false, contact, rawVCard, true, false, true, true, null);
	}
	
	private boolean doContactMove(Connection con, int contactId, int targetCategoryId) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		return contDao.updateCategory(con, contactId, targetCategoryId, BaseDAO.createRevisionTimestamp()) == 1;
	}
	
	private boolean doContactVCardInsert(Connection con, int contactId, String rawVCard) throws DAOException {
		ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
		
		OContactVCard ovca = new OContactVCard();
		ovca.setContactId(contactId);
		ovca.setRawData(rawVCard);
		return vcaDao.insert(con, ovca) == 1;
	}
	
	private boolean doContactVCardDelete(Connection con, int contactId) throws DAOException {
		ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
		return vcaDao.delete(con, contactId) == 1;
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
	
	private void doContactPictureInsert(Connection con, int contactId, ContactPictureWithBytesOld picture) throws DAOException {
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
	}
	
	private void doContactPictureUpdate(Connection con, int contactId, ContactPictureWithBytesOld picture) throws DAOException {
		doContactPictureDelete(con, contactId);
		doContactPictureInsert(con, contactId, picture);
	}
	
	private void doContactPictureDelete(Connection con, int contactId) throws DAOException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		cpicDao.delete(con, contactId);
	}
	
	private OContactAttachment doContactAttachmentInsert(Connection con, int contactId, ContactAttachmentWithStream attachment) throws DAOException, IOException {
		ContactAttachmentDAO attDao = ContactAttachmentDAO.getInstance();
		
		OContactAttachment oatt = ManagerUtils.createOTaskAttachment(attachment);
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
		ContactAttachmentDAO attDao = ContactAttachmentDAO.getInstance();
		
		OContactAttachment oatt = ManagerUtils.createOTaskAttachment(attachment);
		attDao.update(con, oatt, BaseDAO.createRevisionTimestamp());
		
		InputStream is = attachment.getStream();
		try {
			attDao.deleteBytes(con, oatt.getContactAttachmentId());
			return attDao.insertBytes(con, oatt.getContactAttachmentId(), IOUtils.toByteArray(is)) == 1;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	private ContactsList doContactsListGet(Connection con, int contactId, boolean processTags) throws DAOException, WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		ContactTagDAO tagDao = ContactTagDAO.getInstance();
		
		OContact ocont = contDao.selectById(con, contactId);
		if ((ocont == null) || (!ocont.getIsList())) return null;
		
		List<VListRecipient> vlrecs = lrecDao.viewByContact(con, contactId);
		ContactsList contList = ManagerUtils.createContactsList(ocont, vlrecs);
		if (processTags) {
			contList.setTags(tagDao.selectTagsByContact(con, contactId));
		}
		return contList;
	}
	
	private ContactsListInsertResult doContactsListInsert(CoreManager coreMgr, Connection con, ContactsList list, boolean processTags, Set<String> validTags) throws DAOException, IOException {
		ContactInsertResult result = doContactInsert(coreMgr, con, true, ManagerUtils.createContact(list), null, false, false, processTags, false, validTags);
		ArrayList<OListRecipient> orecipients = new ArrayList<>();
		for (ContactsListRecipient recipient : list.getRecipients()) {
			orecipients.add(doContactListRecipientInsert(con, result.ocontact.getContactId(), recipient));
		}
		
		return new ContactsListInsertResult(result.ocontact, result.otags, orecipients);
	}
	
	private OListRecipient doContactListRecipientInsert(Connection con, int contactId, ContactsListRecipient recipient) throws DAOException, IOException {
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		OListRecipient olrec = ManagerUtils.createOListRecipient(recipient);
		olrec.setListRecipientId(lrecDao.getSequence(con).intValue());
		olrec.setContactId(contactId);
		lrecDao.insert(con, olrec);
		
		return olrec;
	}
	
	private boolean doContactsListUpdate(CoreManager coreMgr, Connection con, ContactsList list, boolean processTags, Set<String> validTags) throws DAOException, IOException {
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		if (!doContactUpdate(coreMgr, con, true, ManagerUtils.createContact(list), false, false, processTags, false, validTags)) return false;
		//TODO: maybe evaluate added/deleted records?
		lrecDao.deleteByContact(con, list.getContactId());
		for (ContactsListRecipient recipient : list.getRecipients()) {
			doContactListRecipientInsert(con, list.getContactId(), recipient);
		}
		return true;
	}
	
	private ContactsListInsertResult doContactsListCopy(CoreManager coreMgr, Connection con, ContactsList contactList, int targetCategoryId) throws DAOException, IOException {
		contactList.setCategoryId(targetCategoryId);
		return doContactsListInsert(coreMgr, con, contactList, true, null);
	}
	
	private boolean doContactsListMove(Connection con, int contactId, int targetCategoryId) throws DAOException {
		return doContactMove(con, contactId, targetCategoryId);
	}
	
	private boolean doContactsListAddTo(Connection con, ContactsList list) throws DAOException {
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		for (ContactsListRecipient rcpt : list.getRecipients()) {
			OListRecipient olrec = new OListRecipient(rcpt);
			olrec.setContactId(list.getContactId());
			olrec.setListRecipientId(lrecDao.getSequence(con).intValue());
			lrecDao.insert(con, olrec);
		}
		return true;
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
				if (StringUtils.isBlank(tgt.getDisplayName())) tgt.setDisplayName(BaseContact.buildFullName(tgt.getFirstname(), tgt.getLastname()));
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
				if (StringUtils.isBlank(tgt.getDisplayName())) tgt.setDisplayName(BaseContact.buildFullName(tgt.getFirstname(), tgt.getLastname()));
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
		String title = MessageFormat.format(lookupResource(locale, resKey), BaseContact.buildFullName(contact.getFirstname(), contact.getLastname()));
		
		ReminderInApp alert = new ReminderInApp(SERVICE_ID, contact.getCategoryProfileId(), type, contact.getContactId().toString());
		alert.setTitle(title);
		alert.setDate(date);
		alert.setTimezone(date.getZone().getID());
		return alert;
	}
	
	private ReminderEmail createAnniversaryEmailReminder(Locale locale, InternetAddress recipient, boolean birthday, VContact contact, DateTime date) {
		String type = (birthday) ? "birthday" : "anniversary";
		String resKey = (birthday) ? ContactsLocale.REMINDER_TITLE_BIRTHDAY : ContactsLocale.REMINDER_TITLE_ANNIVERSARY;
		String fullName = BaseContact.buildFullName(contact.getFirstname(), contact.getLastname());
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
		private ArrayList<Contact> contacts = new ArrayList<>();
		public Connection con;
		public int categoryId;
		public int insertedCount = 0;
		
		public ContactBatchImportBeanHandler(LogEntries log, Connection con, int categoryId) {
			super(log);
			this.con = con;
			this.categoryId = categoryId;
		}
		
		@Override
		protected int getCurrentBeanBufferSize() {
			return contacts.size();
		}
		
		@Override
		protected void clearBeanBuffer() {
			contacts.clear();
		}

		@Override
		protected void addBeanToBuffer(ContactInput bean) {
			contacts.add(bean.contact);
		}
		
		@Override
		public boolean handleBufferedBeans() {
			CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
			try {
				insertedCount = insertedCount + doBatchInsertContacts(coreMgr, con, categoryId, contacts);
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
		public final Set<String> otags;
		public final OContactPicture opicture;
		public final List<OContactAttachment> oattachments;
		public final List<OContactCustomValue> ocustomvalues;
		
		public ContactInsertResult(OContact ocontact, Set<String> otags, OContactPicture opicture, List<OContactAttachment> oattachments, ArrayList<OContactCustomValue> ocustomvalues) {
			this.ocontact = ocontact;
			this.otags = otags;
			this.opicture = opicture;
			this.oattachments = oattachments;
			this.ocustomvalues = ocustomvalues;
		}
	}
	
	private static class ContactsListInsertResult {
		public final OContact ocontact;
		public final Set<String> otags;
		public final List<OListRecipient> orecipients;
		
		public ContactsListInsertResult(OContact ocontact, Set<String> otags, List<OListRecipient> orecipients) {
			this.ocontact = ocontact;
			this.otags = otags;
			this.orecipients = orecipients;
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
					for (OShare folder : coreMgr.listIncomingShareFolders(root.getShareId(), GROUPNAME_CATEGORY)) {
						if (folder.hasWildcard()) {
							final UserProfileId ownerPid = coreMgr.userUidToProfileId(folder.getUserUid());
							ownerToWildcardShareFolder.put(ownerPid, folder.getShareId().toString());
							for (Category category : listCategories(ownerPid).values()) {
								folderTo.add(category.getCategoryId());
								rootShareToFolderShare.put(root.getShareId(), category.getCategoryId());
								folderToWildcardShareFolder.put(category.getCategoryId(), folder.getShareId().toString());
							}
						} else {
							int categoryId = Integer.valueOf(folder.getInstance());
							folderTo.add(categoryId);
							rootShareToFolderShare.put(root.getShareId(), categoryId);
							folderToShareFolder.put(categoryId, folder.getShareId().toString());
						}
					}
				}
				ready = true;
			} catch(WTException ex) {
				throw new WTRuntimeException(ex.getMessage());
			}
		}
	}
	
	private enum CheckRightsTarget {
		FOLDER, ELEMENTS
	}
	
	private enum AuditContext {
		CATEGORY, CONTACT
	}
	
	private enum AuditAction {
		CREATE, UPDATE, DELETE, MOVE
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
