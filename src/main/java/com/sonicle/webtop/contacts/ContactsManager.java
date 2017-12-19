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

import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.InternetAddressUtils;
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.db.DbUtils;
import com.sonicle.commons.web.json.CompositeId;
import com.sonicle.dav.CardDav;
import com.sonicle.dav.CardDavFactory;
import com.sonicle.dav.DavSyncStatus;
import com.sonicle.dav.DavUtil;
import com.sonicle.dav.carddav.DavAddressbook;
import com.sonicle.dav.carddav.DavAddressbookCard;
import com.sonicle.dav.impl.DavException;
import com.sonicle.webtop.contacts.bol.OCategory;
import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.OContactPicture;
import com.sonicle.webtop.contacts.bol.OListRecipient;
import com.sonicle.webtop.contacts.bol.VContact;
import com.sonicle.webtop.contacts.bol.model.MyCategoryRoot;
import com.sonicle.webtop.contacts.model.CategoryFolder;
import com.sonicle.webtop.contacts.model.CategoryRoot;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.model.ContactsList;
import com.sonicle.webtop.contacts.model.ContactsListRecipient;
import com.sonicle.webtop.contacts.dal.CategoryDAO;
import com.sonicle.webtop.contacts.dal.ContactDAO;
import com.sonicle.webtop.contacts.dal.ContactPictureDAO;
import com.sonicle.webtop.contacts.dal.ListRecipientDAO;
import com.sonicle.webtop.contacts.io.ContactInput;
import com.sonicle.webtop.contacts.io.VCardInput;
import com.sonicle.webtop.contacts.io.input.ContactFileReader;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryRemoteParameters;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.contacts.model.FolderContacts;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.provider.RecipientsProviderBase;
import com.sonicle.webtop.core.bol.OShare;
import com.sonicle.webtop.core.sdk.BaseManager;
import com.sonicle.webtop.core.bol.Owner;
import com.sonicle.webtop.core.model.Recipient;
import com.sonicle.webtop.core.model.IncomingShareRoot;
import com.sonicle.webtop.core.model.SharePermsFolder;
import com.sonicle.webtop.core.model.SharePermsElements;
import com.sonicle.webtop.core.model.SharePermsRoot;
import com.sonicle.webtop.core.bol.model.Sharing;
import com.sonicle.webtop.core.dal.DAOException;
import com.sonicle.webtop.core.io.BatchBeanHandler;
import com.sonicle.webtop.core.io.input.FileReaderException;
import com.sonicle.webtop.core.model.MasterData;
import com.sonicle.webtop.core.model.RecipientFieldCategory;
import com.sonicle.webtop.core.model.RecipientFieldType;
import com.sonicle.webtop.core.sdk.AuthException;
import com.sonicle.webtop.core.sdk.BaseReminder;
import com.sonicle.webtop.core.sdk.ReminderEmail;
import com.sonicle.webtop.core.sdk.ReminderInApp;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.sdk.WTRuntimeException;
import com.sonicle.webtop.core.sdk.interfaces.IRecipientsProvidersSource;
import com.sonicle.webtop.core.util.IdentifierUtils;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
import com.sonicle.webtop.core.util.NotificationHelper;
import com.sonicle.webtop.core.util.VCardUtils;
import eu.medsea.mimeutil.MimeType;
import freemarker.template.TemplateException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.mail.internet.InternetAddress;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
	private static final Pattern PATTERN_VIRTUALRCPT_LIST = Pattern.compile("^" + RCPT_ORIGIN_LIST + "-(\\d+)$");
	
	private final HashSet<String> cacheReady = new HashSet<>();
	private final HashMap<UserProfileId, CategoryRoot> cacheOwnerToRootShare = new HashMap<>();
	private final HashMap<UserProfileId, String> cacheOwnerToWildcardFolderShare = new HashMap<>();
	private final MultiValueMap cacheRootShareToFolderShare = MultiValueMap.decorate(new HashMap<String, Integer>());
	private final HashMap<Integer, String> cacheCategoryToFolderShare = new HashMap<>();
	private final HashMap<Integer, String> cacheCategoryToWildcardFolderShare = new HashMap<>();
	private final HashMap<Integer, UserProfileId> cacheCategoryToOwner = new HashMap<>();
	
	public ContactsManager(boolean fastInit, UserProfileId targetProfileId) {
		super(fastInit, targetProfileId);
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
	
	private void buildShareCache() {
		CoreManager core = WT.getCoreManager(getTargetProfileId());
		
		try {
			cacheOwnerToRootShare.clear();
			cacheOwnerToWildcardFolderShare.clear();
			cacheRootShareToFolderShare.clear();
			cacheCategoryToFolderShare.clear();
			cacheCategoryToWildcardFolderShare.clear();
			for(CategoryRoot root : listIncomingCategoryRoots()) {
				cacheOwnerToRootShare.put(root.getOwnerProfileId(), root);
				for(OShare folder : core.listIncomingShareFolders(root.getShareId(), GROUPNAME_CATEGORY)) {
					if(folder.hasWildcard()) {
						UserProfileId ownerPid = core.userUidToProfileId(folder.getUserUid());
						cacheOwnerToWildcardFolderShare.put(ownerPid, folder.getShareId().toString());
						for(Category category : listCategories(ownerPid)) {
							cacheRootShareToFolderShare.put(root.getShareId(), category.getCategoryId());
							cacheCategoryToWildcardFolderShare.put(category.getCategoryId(), folder.getShareId().toString());
						}
					} else {
						cacheRootShareToFolderShare.put(root.getShareId(), Integer.valueOf(folder.getInstance()));
						cacheCategoryToFolderShare.put(Integer.valueOf(folder.getInstance()), folder.getShareId().toString());
					}
				}
			}
			cacheReady.add("shareCache");
		} catch(WTException ex) {
			throw new WTRuntimeException(ex.getMessage());
		}
	}
	
	private List<CategoryRoot> getCategoryRoots() {
		synchronized(cacheReady) {
			if(!cacheReady.contains("shareCache")) buildShareCache();
			return new ArrayList<>(cacheOwnerToRootShare.values());
		}
	}
	
	private CategoryRoot ownerToRootShare(UserProfileId owner) {
		synchronized(cacheReady) {
			if(!cacheReady.contains("shareCache") || !cacheOwnerToRootShare.containsKey(owner)) buildShareCache();
			return cacheOwnerToRootShare.get(owner);
		}
	}
	
	private String ownerToRootShareId(UserProfileId owner) {
		CategoryRoot root = ownerToRootShare(owner);
		return (root != null) ? root.getShareId() : null;
	}
	
	private String ownerToWildcardFolderShareId(UserProfileId ownerPid) {
		synchronized(cacheReady) {
			if (!cacheReady.contains("shareCache") || (!cacheOwnerToWildcardFolderShare.containsKey(ownerPid) && cacheOwnerToRootShare.isEmpty())) buildShareCache();
			return cacheOwnerToWildcardFolderShare.get(ownerPid);
		}
	}
	
	private String categoryToFolderShareId(int category) {
		synchronized(cacheReady) {
			if (!cacheReady.contains("shareCache") || !cacheCategoryToFolderShare.containsKey(category)) buildShareCache();
			return cacheCategoryToFolderShare.get(category);
		}
	}
	
	private UserProfileId categoryToOwner(int categoryId) {
		synchronized(cacheCategoryToOwner) {
			if (cacheCategoryToOwner.containsKey(categoryId)) {
				return cacheCategoryToOwner.get(categoryId);
			} else {
				try {
					UserProfileId owner = findCategoryOwner(categoryId);
					if (owner != null) cacheCategoryToOwner.put(categoryId, owner);
					return owner;
				} catch(WTException ex) {
					throw new WTRuntimeException(ex.getMessage());
				}
			}
		}
	}
	
	private List<Integer> cachedCategoryFolderKeys() {
		List<Integer> keys = new ArrayList<>();
		synchronized(cacheReady) {
			if (!cacheReady.contains("shareCache")) buildShareCache();
			keys.addAll(cacheCategoryToFolderShare.keySet());
			keys.addAll(cacheCategoryToWildcardFolderShare.keySet());
		}
		return keys;
	}
	
	private List<Integer> rootShareToCategoryFolderIds(String rootShareId) {
		List<Integer> keys = new ArrayList<>();
		synchronized(cacheReady) {
			if (!cacheReady.contains("shareCache")) buildShareCache();
			if (cacheRootShareToFolderShare.containsKey(rootShareId)) {
				keys.addAll(cacheRootShareToFolderShare.getCollection(rootShareId));
			}
		}
		return keys;
	}
	
	@Override
	public List<RecipientsProviderBase> returnRecipientsProviders() {
		try {
			ArrayList<RecipientsProviderBase> providers = new ArrayList<>();
			UserProfile.Data ud = WT.getUserData(getTargetProfileId());
			providers.add(new RootRecipientsProvider(getTargetProfileId().toString(), ud.getDisplayName(), getTargetProfileId(), listCategoryIds()));
			for(CategoryRoot root : getCategoryRoots()) {
				final List<Integer> catIds = rootShareToCategoryFolderIds(root.getShareId());
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
	
	public Sharing getSharing(String shareId) throws WTException {
		CoreManager core = WT.getCoreManager(getTargetProfileId());
		return core.getSharing(SERVICE_ID, GROUPNAME_CATEGORY, shareId);
	}
	
	public void updateSharing(Sharing sharing) throws WTException {
		CoreManager core = WT.getCoreManager(getTargetProfileId());
		core.updateSharing(SERVICE_ID, GROUPNAME_CATEGORY, sharing);
	}
	
	public UserProfileId getCategoryOwner(int categoryId) throws WTException {
		return categoryToOwner(categoryId);
	}
	
	public String buildCategoryFolderShareId(int categoryId) throws WTException {
		UserProfileId targetPid = getTargetProfileId();
		
		UserProfileId ownerPid = categoryToOwner(categoryId);
		if (ownerPid == null) throw new WTException("categoryToOwner({0}) -> null", categoryId);
		
		String rootShareId = null;
		if (ownerPid.equals(targetPid)) {
			rootShareId = MyCategoryRoot.SHARE_ID;
		} else {
			for (CategoryRoot root : listIncomingCategoryRoots()) {
				HashMap<Integer, CategoryFolder> folders = listIncomingCategoryFolders(root.getShareId());
				if (folders.containsKey(categoryId)) {
					rootShareId = root.getShareId();
					break;
				}
			}
		}
		
		if (rootShareId == null) throw new WTException("Unable to find a root share [{0}]", categoryId);
		return new CompositeId().setTokens(rootShareId, categoryId).toString();
	}
	
	@Override
	public List<CategoryRoot> listIncomingCategoryRoots() throws WTException {
		CoreManager core = WT.getCoreManager(getTargetProfileId());
		ArrayList<CategoryRoot> roots = new ArrayList();
		HashSet<String> hs = new HashSet<>();
		
		List<IncomingShareRoot> shares = core.listIncomingShareRoots(SERVICE_ID, GROUPNAME_CATEGORY);
		for (IncomingShareRoot share : shares) {
			SharePermsRoot perms = core.getShareRootPermissions(share.getShareId());
			CategoryRoot root = new CategoryRoot(share, perms);
			if (hs.contains(root.getShareId())) continue; // Avoid duplicates ??????????????????????
			hs.add(root.getShareId());
			roots.add(root);
		}
		return roots;
	}
	
	@Override
	public HashMap<Integer, CategoryFolder> listIncomingCategoryFolders(String rootShareId) throws WTException {
		CoreManager core = WT.getCoreManager(getTargetProfileId());
		LinkedHashMap<Integer, CategoryFolder> folders = new LinkedHashMap<>();
		
		// Retrieves incoming folders (from sharing). This lookup already 
		// returns readable shares (we don't need to test READ permission)
		List<OShare> shares = core.listIncomingShareFolders(rootShareId, GROUPNAME_CATEGORY);
		for (OShare share : shares) {
			
			List<Category> cats = null;
			if (share.hasWildcard()) {
				UserProfileId ownerId = core.userUidToProfileId(share.getUserUid());
				cats = listCategories(ownerId);
			} else {
				cats = Arrays.asList(getCategory(Integer.valueOf(share.getInstance())));
			}
			
			for (Category cat : cats) {
				SharePermsFolder fperms = core.getShareFolderPermissions(share.getShareId().toString());
				SharePermsElements eperms = core.getShareElementsPermissions(share.getShareId().toString());
				
				if (folders.containsKey(cat.getCategoryId())) {
					CategoryFolder folder = folders.get(cat.getCategoryId());
					folder.getPerms().merge(fperms);
					folder.getElementsPerms().merge(eperms);
				} else {
					folders.put(cat.getCategoryId(), new CategoryFolder(share.getShareId().toString(), fperms, eperms, cat));
				}
			}
		}
		return folders;
	}
	
	@Override
	public List<Integer> listCategoryIds() throws WTException {
		ArrayList<Integer> ids = new ArrayList<>();
		for (Category category : listCategories()) {
			ids.add(category.getCategoryId());
		}
		return ids;
	}
	
	@Override
	public List<Integer> listIncomingCategoryIds() throws WTException {
		ArrayList<Integer> ids = new ArrayList<>();
		for (CategoryRoot root : listIncomingCategoryRoots()) {
			ids.addAll(listIncomingCategoryFolders(root.getShareId()).keySet());
		}
		return ids;
	}
	
	@Override
	public List<Category> listCategories() throws WTException {
		return listCategories(getTargetProfileId());
	}
	
	private List<Category> listCategories(UserProfileId pid) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		ArrayList<Category> items = new ArrayList<>();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			for (OCategory ocat : catDao.selectByProfile(con, pid.getDomainId(), pid.getUserId())) {
				items.add(createCategory(ocat));
			}
			return items;
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Category getCategory(int categoryId) throws WTException {
		CategoryDAO catdao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(categoryId, "READ");
			
			con = WT.getConnection(SERVICE_ID);
			OCategory ocat = catdao.selectById(con, categoryId);
			
			return createCategory(ocat);
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
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
			if(ocat == null) return null;
			
			checkRightsOnCategoryFolder(ocat.getCategoryId(), "READ");
			
			return createCategory(ocat);
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Category addCategory(Category category) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryRoot(category.getProfileId(), "MANAGE");
			
			con = WT.getConnection(SERVICE_ID, false);
			category.setBuiltIn(false);
			category = doCategoryUpdate(true, con, category);
			DbUtils.commitQuietly(con);
			writeLog("CATEGORY_INSERT", category.getCategoryId().toString());
			
			return category;
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
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
			cat = doCategoryUpdate(true, con, cat);
			DbUtils.commitQuietly(con);
			writeLog("CATEGORY_INSERT", cat.getCategoryId().toString());
			
			return cat;
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Category updateCategory(Category cat) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(cat.getCategoryId(), "UPDATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			cat = doCategoryUpdate(false, con, cat);
			DbUtils.commitQuietly(con);
			writeLog("CATEGORY_UPDATE", String.valueOf(cat.getCategoryId()));
			
			return cat;
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public boolean deleteCategory(int categoryId) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(categoryId, "DELETE");
			
			// Retrieve sharing status (for later)
			String shareId = buildCategoryFolderShareId(categoryId);
			Sharing sharing = getSharing(shareId);
			
			con = WT.getConnection(SERVICE_ID, false);
			Category cat = createCategory(catDao.selectById(con, categoryId));
			if (cat == null) return false;
			
			int ret = catDao.deleteById(con, categoryId);
			doDeleteContactsByCategory2(con, categoryId, !cat.isRemoteProvider());
			
			// Cleanup sharing, if necessary
			if ((sharing != null) && !sharing.getRights().isEmpty()) {
				logger.debug("Removing {} active sharing [{}]", sharing.getRights().size(), sharing.getId());
				sharing.getRights().clear();
				updateSharing(sharing);
			}
			
			DbUtils.commitQuietly(con);
			
			final String ref = String.valueOf(categoryId);
			writeLog("CATEGORY_DELETE", ref);
			writeLog("CONTACT_DELETE", "*@"+ref);
			writeLog("CONTACTLIST_DELETE", "*@"+ref);
			
			return ret == 1;
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public List<FolderContacts> listFolderContacts(Collection<Integer> categoryFolderIds, String searchMode, String pattern) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			ArrayList<FolderContacts> foConts = new ArrayList<>();
			List<OCategory> ocats = catDao.selectByDomainIn(con, getTargetProfileId().getDomainId(), categoryFolderIds);
			for (OCategory ocat : ocats) {
				if (!quietlyCheckRightsOnCategoryFolder(ocat.getCategoryId(), "READ")) continue;
				
				final List<VContact> vconts = contDao.viewByCategoryPattern(con, ocat.getCategoryId(), searchMode, pattern);
				final ArrayList<ContactEx> conts = new ArrayList<>();
				for (VContact vcont : vconts) {
					conts.add(fillContactEx(new ContactEx(), vcont));
				}
				foConts.add(new FolderContacts(createCategory(ocat), conts));
			}
			return foConts;
			
		} catch (SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Contact getContact(int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact ocont = contDao.selectById(con, contactId);
			if (ocont == null || ocont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryFolder(ocont.getCategoryId(), "READ");
			
			boolean hasPicture = cpicDao.hasPicture(con, contactId);
			return createContact(ocont, hasPicture);
		
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void addContact(Contact contact) throws WTException {
		addContact(contact, null);
	}
	
	@Override
	public void addContact(Contact contact, ContactPicture picture) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(contact.getCategoryId(), "CREATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			
			Category category = createCategory(catDao.selectById(con, contact.getCategoryId()));
			if (category == null) throw new WTException("Unable to retrieve category [{}]", contact.getCategoryId());
			if (category.isRemoteProvider()) throw new WTException("Category is read only");
			
			OContact result = doContactInsert(coreMgr, con, false, contact, picture);
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_INSERT", String.valueOf(result.getContactId()));
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContact(Contact contact) throws WTException {
		updateContact(contact, null);
	}
	
	@Override
	public void updateContact(Contact contact, ContactPicture picture) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(contact.getCategoryId(), "UPDATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			//TODO: controllare se categoryId non è readonly (remoto)
			doContactUpdate(coreMgr, con, false, contact, picture);
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_UPDATE", String.valueOf(contact.getContactId()));
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactPicture getContactPicture(int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = contDao.selectById(con, contactId);
			if(cont == null) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ");
			
			OContactPicture pic = cpicDao.select(con, contactId);
			return createContactPicture(pic);
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactPicture(int contactId, ContactPicture picture) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			if (picture == null) throw new WTException("Specified picture is null");
			OContact ocont = contDao.selectById(con, contactId);
			if (ocont == null || ocont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryElements(ocont.getCategoryId(), "UPDATE");
			
			contDao.updateRevision(con, contactId, createRevisionTimestamp());
			doUpdateContactPicture(con, contactId, picture);
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_UPDATE", String.valueOf(contactId));
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteContact(int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = contDao.selectById(con, contactId);
			if(cont == null || cont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE");
			
			con.setAutoCommit(false);
			doDeleteContact(con, contactId, true);
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_DELETE", String.valueOf(contactId));
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteContact(Collection<Integer> contactIds) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			for (Integer contactId : contactIds) {
				if (contactId == null) continue;
				OContact ocont = contDao.selectById(con, contactId);
				if (ocont == null || ocont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
				checkRightsOnCategoryElements(ocont.getCategoryId(), "DELETE");
				
				doDeleteContact(con, contactId, true);
			}
			
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_DELETE", "*");
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void moveContact(boolean copy, int contactId, int targetCategoryId) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		ContactDAO contDao = ContactDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			OContact ocont = contDao.selectById(con, contactId);
			if (ocont == null || ocont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryFolder(ocont.getCategoryId(), "READ");
			
			if(copy || (targetCategoryId != ocont.getCategoryId())) {
				checkRightsOnCategoryElements(targetCategoryId, "CREATE");
				if (!copy) checkRightsOnCategoryElements(ocont.getCategoryId(), "DELETE");
				
				boolean hasPicture = cpicDao.hasPicture(con, contactId);
				Contact contact = createContact(ocont, hasPicture);

				con.setAutoCommit(false);
				doMoveContact(coreMgr, con, copy, contact, targetCategoryId);
				DbUtils.commitQuietly(con);
				writeLog("CONTACT_UPDATE", String.valueOf(contact.getContactId()));
			}
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactsList getContactsList(int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = contDao.selectById(con, contactId);
			if(cont == null || !cont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ"); // Rights check!
			
			List<OListRecipient> recipients = lrecDao.selectByContact(con, contactId);
			return createContactsList(cont, recipients);
		
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
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
			checkRightsOnCategoryElements(list.getCategoryId(), "CREATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			
			Category category = createCategory(catDao.selectById(con, list.getCategoryId()));
			if (category == null) throw new WTException("Unable to retrieve category [{}]", list.getCategoryId());
			if (category.isRemoteProvider()) throw new WTException("Category is read only");
			
			OContact result = doInsertContactsList(coreMgr, con, list);
			DbUtils.commitQuietly(con);
			writeLog("CONTACTLIST_INSERT", String.valueOf(result.getContactId()));
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContactsList(ContactsList list) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(list.getCategoryId(), "UPDATE"); // Rights check!
			
			con = WT.getConnection(SERVICE_ID, false);
			doUpdateContactsList(coreMgr, con, list);
			DbUtils.commitQuietly(con);
			writeLog("CONTACTLIST_UPDATE", String.valueOf(list.getContactId()));
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
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
			if(cont == null || !cont.getIsList()) throw new WTException("Unable to retrieve contactsList [{0}]", contactsListId);
			
			checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE");
			
			doDeleteContact(con, contactsListId, true);
			DbUtils.commitQuietly(con);
			writeLog("CONTACTLIST_DELETE", String.valueOf(contactsListId));
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteContactsList(Collection<Integer> contactsListIds) throws WTException {
		ContactDAO condao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			for(Integer contactsListId : contactsListIds) {
				if (contactsListId == null) continue;
				OContact cont = condao.selectById(con, contactsListId);
				if (cont == null || !cont.getIsList()) throw new WTException("Unable to retrieve contactsList [{0}]", contactsListId);

				checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE");
				
				doDeleteContact(con, contactsListId, true);
			}
			
			DbUtils.commitQuietly(con);
			writeLog("CONTACTLIST_DELETE", "*");
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void moveContactsList(boolean copy, int contactsListId, int targetCategoryId) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		ContactDAO cdao = ContactDAO.getInstance();
		ListRecipientDAO lrdao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact ocont = cdao.selectById(con, contactsListId);
			if (ocont == null || !ocont.getIsList()) throw new WTException("Unable to retrieve contactsList [{0}]", contactsListId);
			checkRightsOnCategoryFolder(ocont.getCategoryId(), "READ");
			
			if (copy || (targetCategoryId != ocont.getCategoryId())) {
				checkRightsOnCategoryElements(targetCategoryId, "CREATE");
				if (!copy) checkRightsOnCategoryElements(ocont.getCategoryId(), "DELETE");
				
				List<OListRecipient> recipients = lrdao.selectByContact(con, contactsListId);
				ContactsList clist = createContactsList(ocont, recipients);

				con.setAutoCommit(false);
				doMoveContactsList(coreMgr, con, copy, clist, targetCategoryId);
				DbUtils.commitQuietly(con);
				writeLog("CONTACTLIST_UPDATE", String.valueOf(contactsListId));
			}	
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void eraseData(boolean deep) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		ContactDAO contDao = ContactDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		//TODO: controllo permessi
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			UserProfileId pid = getTargetProfileId();
			
			// Erase contact and all related tables
			if (deep) {
				for (OCategory ocat : catDao.selectByProfile(con, pid.getDomainId(), pid.getUserId())) {
					cpicDao.deleteByCategory(con, ocat.getCategoryId());
					lrecDao.deleteByCategory(con, ocat.getCategoryId());
					contDao.deleteByCategory(con, ocat.getCategoryId());
				}
			} else {
				DateTime revTs = createRevisionTimestamp();
				for (OCategory ocat : catDao.selectByProfile(con, pid.getDomainId(), pid.getUserId())) {
					contDao.logicDeleteByCategory(con, ocat.getCategoryId(), revTs);
				}
			}
			
			// Erase categories
			catDao.deleteByProfile(con, pid.getDomainId(), pid.getUserId());
			
			DbUtils.commitQuietly(con);
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(t);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public LogEntries importContacts(int categoryId, ContactFileReader rea, File file, String mode) throws WTException {
		LogEntries log = new LogEntries();
		Connection con = null;
		
		checkRightsOnCategoryElements(categoryId, "CREATE");
		if (mode.equals("copy")) checkRightsOnCategoryElements(categoryId, "DELETE");
		
		log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Started at {0}", new DateTime()));
		
		try {
			con = WT.getConnection(SERVICE_ID, false);

			if (mode.equals("copy")) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.INFO, "Cleaning contacts..."));
				int del = doDeleteContactsByCategory2(con, categoryId, true);
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
	
	public void syncRemoteCategory(int calendarId) throws WTException {
		syncRemoteCategory(calendarId, false);
	}
	
	public void syncRemoteCategory(int categoryId, boolean full) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		final VCardInput icalInput = new VCardInput();
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			//checkRightsOnCategoryFolder(calendarId, "READ");
			
			con = WT.getConnection(SERVICE_ID, false);
			Category cal = createCategory(catDao.selectById(con, categoryId));
			if (!Category.Provider.CARDDAV.equals(cal.getProvider())) {
				throw new WTException("Specified category is not remote (CardDAV) [{0}]", categoryId);
			}
			
			CategoryRemoteParameters params = LangUtils.deserialize(cal.getParameters(), CategoryRemoteParameters.class);
			if (params == null) throw new WTException("Unable to deserialize remote parameters");
			if (params.url == null) throw new WTException("Remote URL is undefined");
			
			if (Category.Provider.CARDDAV.equals(cal.getProvider())) {
				CardDav dav = getCardDav(params.username, params.password);
				
				try {
					DavAddressbook dbook = dav.getAddressbookSyncToken(params.url.toString());
					if (dbook == null) throw new WTException("DAV addressbook not found");
					
					final boolean syncIsSupported = !StringUtils.isBlank(dbook.getSyncToken());
					final String savedSyncToken = params.syncToken;
					
					if (!syncIsSupported || (syncIsSupported && StringUtils.isBlank(savedSyncToken)) || full) { // Full update
						// If supported, saves last sync-token issued
						if (syncIsSupported) {
							params.syncToken = dbook.getSyncToken();
						}
						
						// Retrieves events list from DAV endpoint
						logger.debug("Retrieving whole list [{}]", params.url.toString());
						List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString());
						logger.debug("Endpoint returns {} items", dcards.size());
						
						// Inserts data...
						try {
							logger.debug("Processing results...");
							// Define a simple map in order to check duplicates.
							// eg. SOGo passes same card twice :(
							HashSet<String> hrefs = new HashSet<>();
							doDeleteContactsByCategory2(con, categoryId, false);
							for (DavAddressbookCard dcard : dcards) {
								if (hrefs.contains(dcard.getPath())) {
									logger.trace("Card duplicated. Skipped! [{}]", dcard.getPath());
									continue;
								}
								
								final ContactInput ci = icalInput.fromVCard(dcard.getCard(), null);
								ci.contact.setCategoryId(categoryId);
								ci.contact.setHref(dcard.getPath());
								ci.contact.setEtag(dcard.geteTag());
								doContactInsert(coreMgr, con, false, ci.contact, ci.picture);
								hrefs.add(dcard.getPath());
							}
							hrefs.clear();
							
							catDao.updateParametersById(con, categoryId, LangUtils.serialize(params, CategoryRemoteParameters.class));
							DbUtils.commitQuietly(con);
							
						} catch(Throwable t) {
							DbUtils.rollbackQuietly(con);
							throw new WTException(t, "Error importing vCard");
						}
						
					} else { // Partial update
						params.syncToken = dbook.getSyncToken();
						
						logger.debug("Retrieving changes [{}, {}]", params.url.toString(), savedSyncToken);
						List<DavSyncStatus> changes = dav.getAddressbookChanges(params.url.toString(), savedSyncToken);
						logger.debug("Endpoint returns {} items", changes.size());
						
						try {
							if (!changes.isEmpty()) {
								ContactDAO contDao = ContactDAO.getInstance();
								Map<String, Integer> contactIdsByHref = contDao.selectHrefsByByCategory(con, categoryId);
								
								// Process changes...
								logger.debug("Processing changes...");
								HashSet<String> hrefs = new HashSet<>();
								for (DavSyncStatus change : changes) {
									if (DavUtil.HTTP_SC_TEXT_OK.equals(change.getResponseStatus())) {
										hrefs.add(change.getPath());

									} else { // Event deleted
										final Integer contactId = contactIdsByHref.get(change.getPath());
										if (contactId == null) throw new WTException("Card path not found [{0}]", change.getPath());
										doDeleteContact(con, contactId, false);
									}
								}

								// Retrieves events list from DAV endpoint (using multiget)
								logger.debug("Retrieving inserted/updated events [{}]", hrefs.size());
								List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString(), hrefs);

								// Inserts/Updates data...
								logger.debug("Inserting/Updating events...");
								for (DavAddressbookCard dcard : dcards) {
									final Integer contactId = contactIdsByHref.get(dcard.getPath());
									if (contactId != null) {
										doDeleteContact(con, contactId, false);
									}
									final ContactInput ci = icalInput.fromVCard(dcard.getCard(), null);
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(dcard.getPath());
									ci.contact.setEtag(dcard.geteTag());
									doContactInsert(coreMgr, con, false, ci.contact, ci.picture);
								}
							}
							
							catDao.updateParametersById(con, categoryId, LangUtils.serialize(params, CategoryRemoteParameters.class));
							DbUtils.commitQuietly(con);
							
						} catch(Throwable t) {
							DbUtils.rollbackQuietly(con);
							throw new WTException(t, "Error importing vCard");
						}
					}
					
				} catch(DavException ex) {
					throw new WTException(ex, "CardDAV error");
				}
			}
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private Category doCategoryUpdate(boolean insert, Connection con, Category cat) throws DAOException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		
		OCategory ocat = createOCategory(cat);
		if (insert) {
			ocat.setCategoryId(catDao.getSequence(con).intValue());
		}
		if (ocat.getIsDefault()) catDao.resetIsDefaultByProfile(con, ocat.getDomainId(), ocat.getUserId());
		if (insert) {
			catDao.insert(con, ocat);
		} else {
			catDao.update(con, ocat);
		}
		return createCategory(ocat);
	}
	
	private String buildContactUid(int contactId, String internetName) {
		return buildContactUid(IdentifierUtils.getUUIDTimeBased(true), contactId, internetName);
	}
	
	private String buildContactUid(String timeBasedPart, int eventId, String internetName) {
		return buildContactUid(timeBasedPart, DigestUtils.md5Hex(String.valueOf(eventId)), internetName);
	}
	
	private String buildContactUid(String timeBasedPart, String contactPart, String internetName) {
		// Generates the uid joining a dynamic time-based string with one 
		// calculated from the real event id. This may help in subsequent phases
		// especially to determine if the event is original or is coming from 
		// an invitation.
		return VCardUtils.buildUid(timeBasedPart + "." + contactPart, internetName);
	}
	
	private int doBatchInsertContacts(CoreManager coreMgr, Connection con, int categoryId, ArrayList<Contact> contacts) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ArrayList<OContact> ocontacts = new ArrayList<>();
		//TODO: eventualmente introdurre supporto alle immagini
		for (Contact contact : contacts) {
			OContact ocont = createOContact(contact);
			ocont.setIsList(false);
			ocont.setSearchfield(buildSearchfield(coreMgr, ocont));
			ocont.setCategoryId(categoryId);
			ocont.setContactId(contDao.getSequence(con).intValue());
			fillDefaultsForInsert(ocont);
			ocontacts.add(ocont);
		}
		return contDao.batchInsert(con, ocontacts, createRevisionTimestamp());
	}
	
	private OContact doContactInsert(CoreManager coreMgr, Connection con, boolean isList, Contact contact, ContactPicture picture) throws IOException, DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		OContact ocont = createOContact(contact);
		ocont.setIsList(isList);
		ocont.setContactId(contDao.getSequence(con).intValue());
		fillDefaultsForInsert(ocont);
		if (isList) {
			ocont.setSearchfield(buildSearchfield(ocont));
			// Compose list workEmail as: "list-{contactId}@{serviceId}"
			ocont.setWorkEmail(RCPT_ORIGIN_LIST + "-" + ocont.getContactId() + "@" + SERVICE_ID);
		} else {
			ocont.setSearchfield(buildSearchfield(coreMgr, ocont));
		}
		contDao.insert(con, ocont, createRevisionTimestamp());

		if (contact.getHasPicture()) {
			if (picture != null) {
				doInsertContactPicture(con, ocont.getContactId(), picture);
			}
		}
		return ocont;
	}
	
	private void doContactUpdate(CoreManager coreMgr, Connection con, boolean isList, Contact contact, ContactPicture picture) throws IOException, DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		OContact ocont = createOContact(contact);
		if (isList) {
			ocont.setSearchfield(buildSearchfield(ocont));
			contDao.updateList(con, ocont, createRevisionTimestamp());
		} else {
			ocont.setSearchfield(buildSearchfield(coreMgr, ocont));
			contDao.update(con, ocont, createRevisionTimestamp());
		}

		if (contact.getHasPicture()) {
			if (picture != null) {
				doUpdateContactPicture(con, ocont.getContactId(), picture);
			}
		} else {
			doDeleteContactPicture(con, ocont.getContactId());
		}
	}
	
	private int doDeleteContact(Connection con, int contactId, boolean logicDelete) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		if (logicDelete) {
			return contDao.logicDeleteById(con, contactId, createRevisionTimestamp());
		} else {
			// List are not supported here
			doDeleteContactPicture(con, contactId);
			return contDao.deleteById(con, contactId);
		}
	}
	
	private int doDeleteContactsByCategory2(Connection con, int categoryId, boolean logicDelete) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		if (logicDelete) {
			return contDao.logicDeleteByCategory(con, categoryId, createRevisionTimestamp());
			
		} else {
			ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
			ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
			
			cpicDao.deleteByCategory(con, categoryId);
			lrecDao.deleteByCategory(con, categoryId);
			return contDao.deleteByCategory(con, categoryId);
		}
	}
	
	private void doMoveContact(CoreManager coreMgr, Connection con, boolean copy, Contact contact, int targetCategoryId) throws IOException, DAOException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		
		if (copy) {
			contact.setCategoryId(targetCategoryId);
			if(contact.getHasPicture()) {
				OContactPicture pic = cpicDao.select(con, contact.getContactId());
				doContactInsert(coreMgr, con, false, contact, createContactPicture(pic));
			} else {
				doContactInsert(coreMgr, con, false, contact, null);
			}
		} else {
			ContactDAO cdao = ContactDAO.getInstance();
			cdao.updateCategory(con, contact.getContactId(), targetCategoryId, createRevisionTimestamp());
		}
	}
	
	private void doInsertContactPicture(Connection con, int contactId, ContactPicture picture) throws IOException, DAOException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		
		OContactPicture ocpic = new OContactPicture();
		ocpic.setContactId(contactId);
		ocpic.setMediaType(picture.getMediaType());

		BufferedImage bi = ImageIO.read(new ByteArrayInputStream(picture.getBytes()));
		if((bi.getWidth() > 720) || (bi.getHeight() > 720)) {
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
		cpicDao.insert(con, ocpic);
	}
	
	public void doUpdateContactPicture(Connection con, int contactId, ContactPicture picture) throws IOException, DAOException {
		doDeleteContactPicture(con, contactId);
		doInsertContactPicture(con, contactId, picture);
	}
	
	private void doDeleteContactPicture(Connection con, int contactId) throws DAOException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		cpicDao.delete(con, contactId);
	}
	
	private OContact doInsertContactsList(CoreManager coreMgr, Connection con, ContactsList list) throws DAOException {
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		try {
			OContact ocont = doContactInsert(coreMgr, con, true, createContact(list), null);
			for(ContactsListRecipient rcpt : list.getRecipients()) {
				OListRecipient olrec = new OListRecipient(rcpt);
				olrec.setContactId(ocont.getContactId());
				olrec.setListRecipientId(lrecDao.getSequence(con).intValue());
				lrecDao.insert(con, olrec);
			}
			return ocont;
		} catch(IOException ex) { /* Do nothing... */ }
		return null;
	}
	
	private void doUpdateContactsList(CoreManager coreMgr, Connection con, ContactsList list) throws DAOException {
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		try {
			doContactUpdate(coreMgr, con, true, createContact(list), null);
			//TODO: gestire la modifica determinando gli eliminati e gli aggiunti?
			lrecDao.deleteByContact(con, list.getContactId());
			for(ContactsListRecipient rcpt : list.getRecipients()) {
				OListRecipient olrec = new OListRecipient(rcpt);
				olrec.setContactId(list.getContactId());
				olrec.setListRecipientId(lrecDao.getSequence(con).intValue());
				lrecDao.insert(con, olrec);
			}
			
		} catch(IOException ex) { /* Do nothing... */ }
	}
	
	private void doMoveContactsList(CoreManager coreMgr, Connection con, boolean copy, ContactsList clist, int targetCategoryId) throws DAOException {
		clist.setCategoryId(targetCategoryId);
		if(copy) {
			doInsertContactsList(coreMgr, con, clist);
		} else {
			doUpdateContactsList(coreMgr, con, clist);
		}
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
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private void checkRightsOnCategoryRoot(UserProfileId ownerPid, String action) throws WTException {
		UserProfileId targetPid = getTargetProfileId();
		
		if (RunContext.isWebTopAdmin()) return;
		if (ownerPid.equals(targetPid)) return;
		
		String shareId = ownerToRootShareId(ownerPid);
		if (shareId == null) throw new WTException("ownerToRootShareId({0}) -> null", ownerPid);
		CoreManager coreMgr = WT.getCoreManager(targetPid);
		if (coreMgr.isShareRootPermitted(shareId, action)) return;
		//if (core.isShareRootPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, shareId)) return;
		
		throw new AuthException("Action not allowed on root share [{0}, {1}, {2}, {3}]", shareId, action, GROUPNAME_CATEGORY, targetPid.toString());
	}
	
	private boolean quietlyCheckRightsOnCategoryFolder(int categoryId, String action) {
		try {
			checkRightsOnCategoryFolder(categoryId, action);
			return true;
		} catch(AuthException ex1) {
			return false;
		} catch(WTException ex1) {
			logger.warn("Unable to check rights [{}]", categoryId);
			return false;
		}
	}
	
	private void checkRightsOnCategoryFolder(int categoryId, String action) throws WTException {
		UserProfileId targetPid = getTargetProfileId();
		
		if (RunContext.isWebTopAdmin()) return;
		
		// Skip rights check if running user is resource's owner
		UserProfileId ownerPid = categoryToOwner(categoryId);
		if (ownerPid == null) throw new WTException("categoryToOwner({0}) -> null", categoryId);
		if (ownerPid.equals(targetPid)) return;
		
		// Checks rights on the wildcard instance (if present)
		CoreManager core = WT.getCoreManager(targetPid);
		String wildcardShareId = ownerToWildcardFolderShareId(ownerPid);
		if (wildcardShareId != null) {
			if (core.isShareFolderPermitted(wildcardShareId, action)) return;
			//if (core.isShareFolderPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, wildcardShareId)) return;
		}
		
		// Checks rights on category instance
		String shareId = categoryToFolderShareId(categoryId);
		if (shareId == null) throw new WTException("categoryToLeafShareId({0}) -> null", categoryId);
		if (core.isShareFolderPermitted(shareId, action)) return;
		//if (core.isShareFolderPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, shareId)) return;
		
		throw new AuthException("Action not allowed on folder share [{0}, {1}, {2}, {3}]", shareId, action, GROUPNAME_CATEGORY, targetPid.toString());
	}
	
	private void checkRightsOnCategoryElements(int categoryId, String action) throws WTException {
		UserProfileId targetPid = getTargetProfileId();
		
		if (RunContext.isWebTopAdmin()) return;
		
		// Skip rights check if running user is resource's owner
		UserProfileId ownerPid = categoryToOwner(categoryId);
		if (ownerPid == null) throw new WTException("categoryToOwner({0}) -> null", categoryId);
		if (ownerPid.equals(targetPid)) return;
		
		// Checks rights on the wildcard instance (if present)
		CoreManager core = WT.getCoreManager(targetPid);
		String wildcardShareId = ownerToWildcardFolderShareId(ownerPid);
		if (wildcardShareId != null) {
			if (core.isShareElementsPermitted(wildcardShareId, action)) return;
			//if (core.isShareElementsPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, wildcardShareId)) return;
		}
		
		// Checks rights on calendar instance
		String shareId = categoryToFolderShareId(categoryId);
		if (shareId == null) throw new WTException("categoryToLeafShareId({0}) -> null", categoryId);
		if (core.isShareElementsPermitted(shareId, action)) return;
		//if (core.isShareElementsPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, shareId)) return;
		
		throw new AuthException("Action not allowed on folderEls share [{0}, {1}, {2}, {3}]", shareId, action, GROUPNAME_CATEGORY, targetPid.toString());
	}
	
	private Category createCategory(OCategory with) {
		return fillCategory(new Category(), with);
	}
	
	private Category fillCategory(Category fill, OCategory with) {
		if ((fill != null) && (with != null)) {
			fill.setCategoryId(with.getCategoryId());
			fill.setDomainId(with.getDomainId());
			fill.setUserId(with.getUserId());
			fill.setBuiltIn(with.getBuiltIn());
			fill.setProvider(EnumUtils.forSerializedName(with.getProvider(), Category.Provider.class));
			fill.setName(with.getName());
			fill.setDescription(with.getDescription());
			fill.setColor(with.getColor());
			fill.setSync(EnumUtils.forSerializedName(with.getSync(), Category.Sync.class));
			fill.setIsDefault(with.getIsDefault());
			// TODO: aggiungere supporto campo is_private
			//cat.setIsPrivate(ocat.getIsPrivate());
			fill.setParameters(with.getParameters());
		}
		return fill;
	}
	
	private OCategory createOCategory(Category with) {
		return fillOCategoryWithDefaults(fillOCategory(new OCategory(), with));
	}
	
	private OCategory fillOCategory(OCategory fill, Category with) {
		if ((fill != null) && (with != null)) {
			fill.setCategoryId(with.getCategoryId());
			fill.setDomainId(with.getDomainId());
			fill.setUserId(with.getUserId());
			fill.setBuiltIn(with.getBuiltIn());
			fill.setProvider(EnumUtils.toSerializedName(with.getProvider()));
			fill.setName(with.getName());
			fill.setDescription(with.getDescription());
			fill.setColor(with.getColor());
			fill.setSync(EnumUtils.toSerializedName(with.getSync()));
			fill.setIsDefault(with.getIsDefault());
			// TODO: aggiungere supporto campo is_private
			//ocat.setIsPrivate(cat.getIsPrivate());
			fill.setParameters(with.getParameters());
		}
		return fill;
	}
	
	private OCategory fillOCategoryWithDefaults(OCategory fill) {
		if (fill != null) {
			ContactsServiceSettings ss = getServiceSettings();
			if (fill.getDomainId() == null) fill.setDomainId(getTargetProfileId().getDomainId());
			if (fill.getUserId() == null) fill.setUserId(getTargetProfileId().getUserId());
			if (fill.getBuiltIn() == null) fill.setBuiltIn(false);
			if (StringUtils.isBlank(fill.getProvider())) fill.setProvider(EnumUtils.toSerializedName(Category.Provider.LOCAL));
			if (StringUtils.isBlank(fill.getColor())) fill.setColor("#FFFFFF");
			if (StringUtils.isBlank(fill.getSync())) fill.setSync(EnumUtils.toSerializedName(ss.getDefaultCategorySync()));
			if (fill.getIsDefault() == null) fill.setIsDefault(false);
			//if (fill.getIsPrivate() == null) fill.setIsPrivate(false);
			
			Category.Provider provider = EnumUtils.forSerializedName(fill.getProvider(), Category.Provider.class);
			if (Category.Provider.CARDDAV.equals(provider)) {
				fill.setIsDefault(false);
			}
		}
		return fill;
	}
	
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
	
	private ContactsList createContactsList(OContact ocontlst, List<OListRecipient> olstrecs) {
		if (ocontlst == null) return null;
		ContactsList item = new ContactsList();
		item.setContactId(ocontlst.getContactId());
		item.setCategoryId(ocontlst.getCategoryId());
		item.setName(ocontlst.getLastname());
		for(OListRecipient rcpt : olstrecs) {
			item.addRecipient(createContactsListRecipient(rcpt));
		}
		return item;
	}
	
	private ContactsListRecipient createContactsListRecipient(OListRecipient olstrec) {
		if (olstrec == null) return null;
		ContactsListRecipient lstrec = new ContactsListRecipient();
		lstrec.setListRecipientId(olstrec.getListRecipientId());
		lstrec.setRecipient(olstrec.getRecipient());
		lstrec.setRecipientType(olstrec.getRecipientType());
		return lstrec;
	}
	
	private Contact createContact(ContactsList contlst) {
		if (contlst == null) return null;
		Contact cont = new Contact();
		cont.setContactId(contlst.getContactId());
		cont.setCategoryId(contlst.getCategoryId());
		cont.setLastName(contlst.getName());
		cont.setHasPicture(false);
		return cont;
	}
	
	private Contact createContact(OContact with, boolean hasPicture) {
		Contact obj = fillContact(new Contact(), with);
		obj.setHasPicture(hasPicture);
		return obj;
	}
	
	private Contact fillContact(Contact fill, OContact with) {
		if ((fill != null) && (with != null)) {
			fill.setContactId(with.getContactId());
			fill.setCategoryId(with.getCategoryId());
			fill.setRevisionStatus(EnumUtils.forSerializedName(with.getRevisionStatus(), Contact.RevisionStatus.class));
			fill.setPublicUid(with.getPublicUid());
			fill.setTitle(with.getTitle());
			fill.setFirstName(with.getFirstname());
			fill.setLastName(with.getLastname());
			fill.setNickname(with.getNickname());
			fill.setGender(EnumUtils.forSerializedName(with.getGender(), Contact.Gender.class));
			fill.setWorkAddress(with.getWorkAddress());
			fill.setWorkPostalCode(with.getWorkPostalcode());
			fill.setWorkCity(with.getWorkCity());
			fill.setWorkState(with.getWorkState());
			fill.setWorkCountry(with.getWorkCountry());
			fill.setWorkTelephone(with.getWorkTelephone());
			fill.setWorkTelephone2(with.getWorkTelephone2());
			fill.setWorkMobile(with.getWorkMobile());
			fill.setWorkFax(with.getWorkFax());
			fill.setWorkPager(with.getWorkPager());
			fill.setWorkEmail(with.getWorkEmail());
			fill.setWorkInstantMsg(with.getWorkIm());
			fill.setHomeAddress(with.getHomeAddress());
			fill.setHomePostalCode(with.getHomePostalcode());
			fill.setHomeCity(with.getHomeCity());
			fill.setHomeState(with.getHomeState());
			fill.setHomeCountry(with.getHomeCountry());
			fill.setHomeTelephone(with.getHomeTelephone());
			fill.setHomeTelephone2(with.getHomeTelephone2());
			fill.setHomeFax(with.getHomeFax());
			fill.setHomePager(with.getHomePager());
			fill.setHomeEmail(with.getHomeEmail());
			fill.setHomeInstantMsg(with.getHomeIm());
			fill.setOtherAddress(with.getOtherAddress());
			fill.setOtherPostalCode(with.getOtherPostalcode());
			fill.setOtherCity(with.getOtherCity());
			fill.setOtherState(with.getOtherState());
			fill.setOtherCountry(with.getOtherCountry());
			fill.setOtherEmail(with.getOtherEmail());
			fill.setOtherInstantMsg(with.getOtherIm());
			fill.setCompany(with.getCompany());
			fill.setFunction(with.getFunction());
			fill.setDepartment(with.getDepartment());
			fill.setManager(with.getManager());
			fill.setAssistant(with.getAssistant());
			fill.setAssistantTelephone(with.getAssistantTelephone());
			fill.setPartner(with.getPartner());
			fill.setBirthday(with.getBirthday());
			fill.setAnniversary(with.getAnniversary());
			fill.setUrl(with.getUrl());
			fill.setNotes(with.getNotes());
			fill.setHref(with.getHref());
			fill.setEtag(with.getEtag());
		}
		return fill;
	}
	
	private OContact createOContact(Contact with) {
		return fillOContact(new OContact(), with);
	}
	
	private OContact fillOContact(OContact fill, Contact with) {
		if ((fill != null) && (with != null)) {
			fill.setContactId(with.getContactId());
			fill.setCategoryId(with.getCategoryId());
			fill.setPublicUid(with.getPublicUid());
			fill.setRevisionStatus(EnumUtils.toSerializedName(with.getRevisionStatus()));
			fill.setIsList(false);
			fill.setTitle(with.getTitle());
			fill.setFirstname(with.getFirstName());
			fill.setLastname(with.getLastName());
			fill.setNickname(with.getNickname());
			fill.setGender(EnumUtils.toSerializedName(with.getGender()));
			fill.setWorkAddress(with.getWorkAddress());
			fill.setWorkPostalcode(with.getWorkPostalCode());
			fill.setWorkCity(with.getWorkCity());
			fill.setWorkState(with.getWorkState());
			fill.setWorkCountry(with.getWorkCountry());
			fill.setWorkTelephone(with.getWorkTelephone());
			fill.setWorkTelephone2(with.getWorkTelephone2());
			fill.setWorkMobile(with.getWorkMobile());
			fill.setWorkFax(with.getWorkFax());
			fill.setWorkPager(with.getWorkPager());
			fill.setWorkEmail(with.getWorkEmail());
			fill.setWorkIm(with.getWorkInstantMsg());
			fill.setHomeAddress(with.getHomeAddress());
			fill.setHomePostalcode(with.getHomePostalCode());
			fill.setHomeCity(with.getHomeCity());
			fill.setHomeState(with.getHomeState());
			fill.setHomeCountry(with.getHomeCountry());
			fill.setHomeTelephone(with.getHomeTelephone());
			fill.setHomeTelephone2(with.getHomeTelephone2());
			fill.setHomeFax(with.getHomeFax());
			fill.setHomePager(with.getHomePager());
			fill.setHomeEmail(with.getHomeEmail());
			fill.setHomeIm(with.getHomeInstantMsg());
			fill.setOtherAddress(with.getOtherAddress());
			fill.setOtherPostalcode(with.getOtherPostalCode());
			fill.setOtherCity(with.getOtherCity());
			fill.setOtherState(with.getOtherState());
			fill.setOtherCountry(with.getOtherCountry());
			fill.setOtherEmail(with.getOtherEmail());
			fill.setOtherIm(with.getOtherInstantMsg());
			fill.setCompany(with.getCompany());
			fill.setFunction(with.getFunction());
			fill.setDepartment(with.getDepartment());
			fill.setManager(with.getManager());
			fill.setAssistant(with.getAssistant());
			fill.setAssistantTelephone(with.getAssistantTelephone());
			fill.setPartner(with.getPartner());
			fill.setBirthday(with.getBirthday());
			fill.setAnniversary(with.getAnniversary());
			fill.setUrl(with.getUrl());
			fill.setNotes(with.getNotes());
			fill.setHref(with.getHref());
			fill.setEtag(with.getEtag());
		}
		return fill;
	}
	
	private OContact fillDefaultsForInsert(OContact fill) {
		if (fill != null) {
			if (StringUtils.isBlank(fill.getPublicUid())) {
				fill.setPublicUid(buildContactUid(fill.getContactId(), WT.getDomainInternetName(getTargetProfileId().getDomainId())));
			}
		}
		return fill;
	}
	
	private ContactPicture createContactPicture(OContactPicture with) {
		return fillContactPicture(new ContactPicture(), with);
	}
	
	private ContactPicture fillContactPicture(ContactPicture fill, OContactPicture with) {
		if ((fill != null) && (with != null)) {
			fill.setWidth(with.getWidth());
			fill.setHeight(with.getHeight());
			fill.setMediaType(with.getMediaType());
			fill.setBytes(with.getBytes());
		}
		return fill;
	}
	
	private ContactEx fillContactEx(ContactEx fill, VContact with) {
		if ((fill != null) && (with != null)) {
			fillContact(fill, with);
			fill.setIsList(with.getIsList());
			fill.setCompanyAsMasterDataId(with.getCompanyAsMasterDataId());
			fill.setCategoryDomainId(with.getCategoryDomainId());
			fill.setCategoryUserId(with.getCategoryUserId());
		}
		return fill;
	}
	
	private ReminderInApp createAnniversaryInAppReminder(Locale locale, boolean birthday, VContact contact, DateTime date) {
		String type = (birthday) ? "birthday" : "anniversary";
		String resKey = (birthday) ? ContactsLocale.REMINDER_TITLE_BIRTHDAY : ContactsLocale.REMINDER_TITLE_ANNIVERSARY;
		String title = MessageFormat.format(lookupResource(locale, resKey), Contact.buildFullName(contact.getFirstname(), contact.getLastname()));
		
		ReminderInApp alert = new ReminderInApp(SERVICE_ID, contact.getCategoryProfileId(), type, contact.getContactId().toString());
		alert.setTitle(title);
		alert.setDate(date);
		alert.setTimezone(date.getZone().getID());
		return alert;
	}
	
	private ReminderEmail createAnniversaryEmailReminder(Locale locale, InternetAddress recipient, boolean birthday, VContact contact, DateTime date) {
		String type = (birthday) ? "birthday" : "anniversary";
		String resKey = (birthday) ? ContactsLocale.REMINDER_TITLE_BIRTHDAY : ContactsLocale.REMINDER_TITLE_ANNIVERSARY;
		String fullName = Contact.buildFullName(contact.getFirstname(), contact.getLastname());
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
	
	private DateTime createRevisionTimestamp() {
		return DateTime.now(DateTimeZone.UTC);
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
						if (vcont.getIsList() && fieldCategory.equals(RecipientFieldCategory.WORK) && fieldType.equals(RecipientFieldType.EMAIL)) {
							items.add(new Recipient(this.getId(), this.getDescription(), RCPT_ORIGIN_LIST, vcont.getLastname(), value));
							
						} else {
							if (fieldType.equals(RecipientFieldType.EMAIL) && !InternetAddressUtils.isAddressValid(value)) continue;
							
							final String personal = InternetAddressUtils.buildPersonal(vcont.getFirstname(), vcont.getLastname());
							items.add(new Recipient(this.getId(), this.getDescription(), origin, personal, value));
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
				Matcher matcher = PATTERN_VIRTUALRCPT_LIST.matcher(virtualRecipient);
				if (matcher.matches()) {
					int contactId = Integer.valueOf(matcher.group(1));
					UserProfileId pid = new UserProfileId(getId());
					List<OListRecipient> recipients = dao.selectByProfileContact(con, pid.getDomainId(), pid.getUserId(), contactId);
					for (OListRecipient recipient : recipients) {
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
				
			} catch(Throwable t) {
				logger.error("Error listing recipients", t);
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
			checkRightsOnCategoryElements(categoryId, "CREATE"); // Rights check!
			if(mode.equals("copy")) checkRightsOnCategoryElements(categoryId, "DELETE"); // Rights check!
			
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
			throw new WTException(ex, "DB error");
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
			checkRightsOnCategoryElements(categoryId, "CREATE"); // Rights check!
			if(mode.equals("copy")) checkRightsOnCategoryElements(categoryId, "DELETE"); // Rights check!
			
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
			throw new WTException(ex, "DB error");
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
