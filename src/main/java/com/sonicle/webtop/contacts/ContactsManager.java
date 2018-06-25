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
import com.sonicle.commons.LangUtils.CollectionChangeSet;
import com.sonicle.commons.PathUtils;
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
import com.sonicle.webtop.contacts.bol.OCategoryPropSet;
import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.OContactPicture;
import com.sonicle.webtop.contacts.bol.OContactVCard;
import com.sonicle.webtop.contacts.bol.OListRecipient;
import com.sonicle.webtop.contacts.bol.VContact;
import com.sonicle.webtop.contacts.bol.VContactCard;
import com.sonicle.webtop.contacts.bol.VContactCardChanged;
import com.sonicle.webtop.contacts.bol.VContactHrefSync;
import com.sonicle.webtop.contacts.bol.model.MyShareRootCategory;
import com.sonicle.webtop.contacts.model.ShareFolderCategory;
import com.sonicle.webtop.contacts.model.ShareRootCategory;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.model.ContactsList;
import com.sonicle.webtop.contacts.model.ContactsListRecipient;
import com.sonicle.webtop.contacts.dal.CategoryDAO;
import com.sonicle.webtop.contacts.dal.CategoryPropsDAO;
import com.sonicle.webtop.contacts.dal.ContactDAO;
import com.sonicle.webtop.contacts.dal.ContactPictureDAO;
import com.sonicle.webtop.contacts.dal.ContactVCardDAO;
import com.sonicle.webtop.contacts.dal.ListRecipientDAO;
import com.sonicle.webtop.contacts.io.ContactInput;
import com.sonicle.webtop.contacts.io.VCardInput;
import com.sonicle.webtop.contacts.io.VCardOutput;
import com.sonicle.webtop.contacts.io.input.ContactFileReader;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.CategoryRemoteParameters;
import com.sonicle.webtop.contacts.model.ContactCard;
import com.sonicle.webtop.contacts.model.ContactCardChanged;
import com.sonicle.webtop.contacts.model.ContactItemEx;
import com.sonicle.webtop.contacts.model.ContactItem;
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
import com.sonicle.webtop.core.dal.DAOIntegrityViolationException;
import com.sonicle.webtop.core.io.BatchBeanHandler;
import com.sonicle.webtop.core.io.input.FileReaderException;
import com.sonicle.webtop.core.model.MasterData;
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
import com.sonicle.webtop.core.util.IdentifierUtils;
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
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.mail.internet.InternetAddress;
import org.apache.commons.codec.digest.DigestUtils;
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
	
	private final OwnerCache ownerCache = new OwnerCache();
	private final ShareCache shareCache = new ShareCache();
	
	public ContactsManager(boolean fastInit, UserProfileId targetProfileId) {
		super(fastInit, targetProfileId);
		if (!fastInit) {
			shareCache.init();
		}
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
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
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
		CoreManager core = WT.getCoreManager(getTargetProfileId());
		return core.getSharing(SERVICE_ID, GROUPNAME_CATEGORY, shareId);
	}
	
	public void updateSharing(Sharing sharing) throws WTException {
		CoreManager core = WT.getCoreManager(getTargetProfileId());
		core.updateSharing(SERVICE_ID, GROUPNAME_CATEGORY, sharing);
	}
	
	public UserProfileId getCategoryOwner(int categoryId) throws WTException {
		return ownerCache.get(categoryId);
	}
	
	@Override
	public List<ShareRootCategory> listIncomingCategoryRoots() throws WTException {
		return shareCache.getShareRoots();
	}
	
	@Override
	public Map<Integer, ShareFolderCategory> listIncomingCategoryFolders(String rootShareId) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
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
	public List<Integer> listCategoryIds() throws WTException {
		return new ArrayList<>(listCategories().keySet());
	}
	
	@Override
	public List<Integer> listIncomingCategoryIds() throws WTException {
		return shareCache.getFolderIds();
	}
	
	@Override
	public Map<Integer, Category> listCategories() throws WTException {
		return listCategories(getTargetProfileId());
	}
	
	private Map<Integer, Category> listCategories(UserProfileId pid) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		LinkedHashMap<Integer, Category> items = new LinkedHashMap<>();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			for (OCategory ocat : catDao.selectByProfile(con, pid.getDomainId(), pid.getUserId())) {
				items.put(ocat.getCategoryId(), createCategory(ocat));
			}
			return items;
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
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
					.filter(categoryId -> quietlyCheckRightsOnCategoryFolder(categoryId, "READ"))
					.collect(Collectors.toList());
			
			con = WT.getConnection(SERVICE_ID);
			return contDao.selectMaxRevTimestampByCategoriesType(con, okCategoryIds, false);
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Category getCategory(int categoryId) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(categoryId, "READ");
			
			con = WT.getConnection(SERVICE_ID);
			OCategory ocat = catDao.selectById(con, categoryId);
			return createCategory(ocat);
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
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
			checkRightsOnCategoryFolder(ocat.getCategoryId(), "READ");
			
			return createCategory(ocat);
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public Map<String, String> getCategoryLinks(int categoryId) throws WTException {
		checkRightsOnCategoryFolder(categoryId, "READ");
		
		UserProfile.Data ud = WT.getUserData(getTargetProfileId());
		String davServerBaseUrl = WT.getDavServerBaseUrl(getTargetProfileId().getDomainId());
		String categoryUid = ManagerUtils.encodeAsCategoryUid(categoryId);
		String addressbookUrl = MessageFormat.format(ManagerUtils.CARDDAV_ADDRESSBOOK_URL, ud.getProfileEmailAddress(), categoryUid);
		
		LinkedHashMap<String, String> links = new LinkedHashMap<>();
		links.put(ManagerUtils.CATEGORY_LINK_CARDDAV, PathUtils.concatPathParts(davServerBaseUrl, addressbookUrl));
		return links;
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
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Category updateCategory(Category category) throws WTException {
		Connection con = null;
		
		try {
			int categoryId = category.getCategoryId();
			checkRightsOnCategoryFolder(categoryId, "UPDATE");
			
			con = WT.getConnection(SERVICE_ID, false);
			category = doCategoryUpdate(false, con, category);
			if (category == null) throw new NotFoundException("Category not found [{}]", categoryId);
			
			DbUtils.commitQuietly(con);
			writeLog("CATEGORY_UPDATE", String.valueOf(categoryId));
			
			return category;
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteCategory(int categoryId) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		CategoryPropsDAO psetDao = CategoryPropsDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(categoryId, "DELETE");
			
			// Retrieve sharing status (for later)
			String sharingId = buildSharingId(categoryId);
			Sharing sharing = getSharing(sharingId);
			
			con = WT.getConnection(SERVICE_ID, false);
			Category cat = createCategory(catDao.selectById(con, categoryId));
			if (cat == null) throw new NotFoundException("Category not found [{}]", categoryId);
			
			int deleted = catDao.deleteById(con, categoryId);
			psetDao.deleteByCategory(con, categoryId);
			doContactsDeleteByCategory(con, categoryId, !cat.isProviderRemote());
			
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
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			return (opset == null) ? new CategoryPropSet() : createCategoryPropSet(opset);
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
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
				psets.put(categoryId, (opset == null) ? new CategoryPropSet() : createCategoryPropSet(opset));
			}
			return psets;
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
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
			OCategoryPropSet opset = createOCategoryPropSet(propertySet);
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
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public List<ContactCard> listContactCards(int categoryFolderIds) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			checkRightsOnCategoryFolder(categoryFolderIds, "READ");
			
			ArrayList<ContactCard> items = new ArrayList<>();
			Map<String, List<VContactCard>> vcontMap = contDao.viewCardsByCategory(con, categoryFolderIds);
			for (List<VContactCard> vconts : vcontMap.values()) {
				if (vconts.isEmpty()) continue;
				VContactCard vcont = vconts.get(vconts.size()-1);
				if (vconts.size() > 1) {
					logger.trace("Many Cards ({}) found for same href [{} -> {}]", vconts.size(), vcont.getHref(), vcont.getContactId());
				}
				
				items.add(doContactCardPrepare(con, vcont));
			}
			return items;
			
		} catch (SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public CollectionChangeSet<ContactCardChanged> listContactCardsChanges(int categoryFolderId, DateTime since, Integer limit) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			checkRightsOnCategoryFolder(categoryFolderId, "READ");
			
			ArrayList<ContactCardChanged> inserted = new ArrayList<>();
			ArrayList<ContactCardChanged> updated = new ArrayList<>();
			ArrayList<ContactCardChanged> deleted = new ArrayList<>();
			
			if (limit == null) limit = Integer.MAX_VALUE;
			if (since == null) {
				List<VContactCardChanged> vconts = contDao.viewChangedByCategory(con, categoryFolderId, limit);
				for (VContactCardChanged vcont : vconts) {
					inserted.add(new ContactCardChanged(vcont.getContactId(), vcont.getRevisionTimestamp(), vcont.getHref()));
				}
			} else {
				List<VContactCardChanged> vconts = contDao.viewChangedByCategorySince(con, categoryFolderId, since, limit);
				for (VContactCardChanged vcont : vconts) {
					Contact.RevisionStatus revStatus = EnumUtils.forSerializedName(vcont.getRevisionStatus(), Contact.RevisionStatus.class);
					if (Contact.RevisionStatus.DELETED.equals(revStatus)) {
						deleted.add(new ContactCardChanged(vcont.getContactId(), vcont.getRevisionTimestamp(), vcont.getHref()));
					} else {
						if (Contact.RevisionStatus.NEW.equals(revStatus) || (vcont.getCreationTimestamp().compareTo(since) >= 0)) {
							inserted.add(new ContactCardChanged(vcont.getContactId(), vcont.getRevisionTimestamp(), vcont.getHref()));
						} else {
							updated.add(new ContactCardChanged(vcont.getContactId(), vcont.getRevisionTimestamp(), vcont.getHref()));
						}
					}
				}
			}
			
			return new CollectionChangeSet<>(inserted, updated, deleted);
			
		} catch (SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public ContactCard getContactCard(int categoryFolderId, String href) throws WTException {
		List<ContactCard> ccs = getContactCards(categoryFolderId, Arrays.asList(href));
		return ccs.isEmpty() ? null : ccs.get(0);
	}
	
	public List<ContactCard> getContactCards(int categoryFolderId, Collection<String> hrefs) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			checkRightsOnCategoryFolder(categoryFolderId, "READ");
			
			ArrayList<ContactCard> items = new ArrayList<>();
			Map<String, List<VContactCard>> vcontMap = contDao.viewCardsByCategoryHrefs(con, categoryFolderId, hrefs);
			for (String href : hrefs) {
				List<VContactCard> vconts = vcontMap.get(href);
				if (vconts == null) continue;
				if (vconts.isEmpty()) continue;
				VContactCard vcont = vconts.get(vconts.size()-1);
				if (vconts.size() > 1) {
					logger.trace("Many Cards ({}) found for same href [{} -> {}]", vconts.size(), vcont.getHref(), vcont.getContactId());
				}
				
				items.add(doContactCardPrepare(con, vcont));
			}
			return items;
			
		} catch (SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void addContactCard(int categoryFolderId, String href, VCard vCard) throws WTException {
		VCardInput in = new VCardInput();
		ContactInput ci = in.fromVCard(vCard, null);
		ci.contact.setCategoryId(categoryFolderId);
		ci.contact.setHref(href);
		
		String rawData = null;
		if (vCard != null) {
			String prodId = VCardUtils.buildProdId(ManagerUtils.getProductName());
			rawData = new VCardOutput(prodId).write(vCard);
		}
		
		addContact(ci.contact, ci.picture, rawData);
	}
	
	public void updateContactCard(int categoryFolderId, String href, VCard vCard) throws WTException {
		int contactId = getContactIdByCategoryHref(categoryFolderId, href, true);
		
		VCardInput in = new VCardInput();
		ContactInput ci = in.fromVCard(vCard, null);
		ci.contact.setContactId(contactId);
		ci.contact.setCategoryId(categoryFolderId);
		updateContact(ci.contact, ci.picture, true);
	}
	
	public void deleteContactCard(int categoryFolderId, String href) throws WTException {
		int contactId = getContactIdByCategoryHref(categoryFolderId, href, true);
		deleteContact(contactId);
	}
	
	private int getContactIdByCategoryHref(int categoryFolderId, String href, boolean throwExIfManyMatchesFound) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			List<Integer> ids = contDao.selectAliveIdsByCategoryHrefs(con, categoryFolderId, href);
			if (ids.isEmpty()) throw new NotFoundException("Contact card not found [{}, {}]", categoryFolderId, href);
			if (throwExIfManyMatchesFound && (ids.size() > 1)) throw new WTException("Many matches for href [{}]", href);
			return ids.get(ids.size()-1);
			
		} catch (SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private ContactCard doContactCardPrepare(Connection con, VContactCard vcont) throws WTException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		
		Contact cont = fillContact(new Contact(), vcont);
		ContactPicture cpic = null;
		if (vcont.getHasPicture()) {
			cpic = createContactPicture(cpicDao.select(con, vcont.getContactId()));
		}
		
		String prodId = VCardUtils.buildProdId(ManagerUtils.getProductName());
		VCardOutput out = new VCardOutput(prodId);
		VCard vCard = out.toVCard(cont, cpic);
		if (vcont.getHasVcard()) {
			//TODO: in order to be fully compliant, merge generated vcard with the original one in db table!
		}
		
		String raw = out.write(vCard);
		
		ContactCard cc = fillContactCard(new ContactCard(), vcont);
		cc.setSize(raw.getBytes().length);
		cc.setVcard(raw);
		
		return cc;
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
				final ArrayList<ContactItemEx> conts = new ArrayList<>();
				for (VContact vcont : vconts) {
					conts.add(fillContactEx(new ContactItemEx(), vcont));
				}
				foConts.add(new FolderContacts(createCategory(ocat), conts));
			}
			return foConts;
			
		} catch (SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public ContactItem getContact(int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact ocont = contDao.selectById(con, contactId);
			if (ocont == null || ocont.getIsList()) throw new WTException("Unable to get contact [{0}]", contactId);
			checkRightsOnCategoryFolder(ocont.getCategoryId(), "READ");
			
			boolean hasPicture = cpicDao.hasPicture(con, contactId);
			return createContactItem(ocont, hasPicture);
		
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public Contact addContact(Contact contact) throws WTException {
		return addContact(contact, null);
	}
	
	@Override
	public Contact addContact(Contact contact, ContactPicture picture) throws WTException {
		return addContact(contact, picture, null);
	}
	
	private Contact addContact(Contact contact, ContactPicture picture, String vCardRawData) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(contact.getCategoryId(), "CREATE");
			con = WT.getConnection(SERVICE_ID, false);
			
			String provider = catDao.selectProviderById(con, contact.getCategoryId());
			if (Category.isProviderRemote(provider)) throw new WTException("Calendar is remote and therefore read-only [{}]", contact.getCategoryId());
			
			OContact inserted = doContactInsert(coreMgr, con, false, contact, picture, vCardRawData);
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_INSERT", String.valueOf(inserted.getContactId()));
			return fillContact(new Contact(), inserted);
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void updateContact(Contact contact) throws WTException {
		updateContact(contact, null, false);
	}
	
	@Override
	public void updateContact(Contact contact, ContactPicture picture, boolean deletePictureIfNull) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(contact.getCategoryId(), "UPDATE");
			con = WT.getConnection(SERVICE_ID, false);
			
			String provider = catDao.selectProviderById(con, contact.getCategoryId());
			if (Category.isProviderRemote(provider)) throw new WTException("Calendar is remote and therefore read-only [{}]", contact.getCategoryId());
			
			boolean updated = doContactUpdate(coreMgr, con, false, contact, picture, deletePictureIfNull);
			if (!updated) throw new WTException("Contact not found [{}]", contact.getContactId());
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_UPDATE", String.valueOf(contact.getContactId()));
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			if (cont == null) throw new WTException("Unable to get contact [{0}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ");
			
			OContactPicture pic = cpicDao.select(con, contactId);
			return createContactPicture(pic);
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} catch(Throwable t) {
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
			
			if (picture == null) throw new WTException("Picture is null");
			OContact ocont = contDao.selectById(con, contactId);
			if (ocont == null || ocont.getIsList()) throw new WTException("Unable to get contact [{0}]", contactId);
			checkRightsOnCategoryElements(ocont.getCategoryId(), "UPDATE");
			
			contDao.updateRevision(con, contactId, createRevisionTimestamp());
			doContactPictureUpdate(con, contactId, picture);
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_UPDATE", String.valueOf(contactId));
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void deleteContact(int contactId) throws WTException {
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			
			OContact cont = contDao.selectById(con, contactId);
			//TODO: Speed-up below query...
			if (cont == null || cont.getIsList()) throw new WTException("Contact not found [{0}]", contactId);
			checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE");
			
			doContactDelete(con, contactId, true);
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_DELETE", String.valueOf(contactId));
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
				if (ocont == null || ocont.getIsList()) throw new WTException("Unable to get contact [{0}]", contactId);
				checkRightsOnCategoryElements(ocont.getCategoryId(), "DELETE");
				
				doContactDelete(con, contactId, true);
			}
			
			DbUtils.commitQuietly(con);
			writeLog("CONTACT_DELETE", "*");
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	@Override
	public void moveContact(boolean copy, int contactId, int targetCategoryId) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		ContactDAO contDao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID, false);
			OContact ocont = contDao.selectById(con, contactId);
			if (ocont == null || ocont.getIsList()) throw new WTException("Unable to get contact [{0}]", contactId);
			checkRightsOnCategoryFolder(ocont.getCategoryId(), "READ");
			
			if(copy || (targetCategoryId != ocont.getCategoryId())) {
				checkRightsOnCategoryElements(targetCategoryId, "CREATE");
				if (!copy) checkRightsOnCategoryElements(ocont.getCategoryId(), "DELETE");
				
				Contact contact = fillContact(new Contact(), ocont);
				
				doContactMove(coreMgr, con, copy, contact, targetCategoryId);
				DbUtils.commitQuietly(con);
				writeLog("CONTACT_UPDATE", String.valueOf(contact.getContactId()));
			}
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			if(cont == null || !cont.getIsList()) throw new WTException("Unable to get contact [{0}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ"); // Rights check!
			
			List<OListRecipient> recipients = lrecDao.selectByContact(con, contactId);
			return createContactsList(cont, recipients);
		
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
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
			if (category == null) throw new WTException("Unable to get category [{}]", list.getCategoryId());
			if (category.isProviderRemote()) throw new WTException("Category is read only");
			
			OContact result = doContactsListInsert(coreMgr, con, list);
			DbUtils.commitQuietly(con);
			writeLog("CONTACTLIST_INSERT", String.valueOf(result.getContactId()));
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			boolean updated = doContactsListUpdate(coreMgr, con, list);
			if (!updated) throw new WTException("Contacts list not found [{}]", list.getContactId());
			DbUtils.commitQuietly(con);
			writeLog("CONTACTLIST_UPDATE", String.valueOf(list.getContactId()));
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			if (cont == null) throw new NotFoundException("Contact list not found [{}]", contactsListId);
			if (!cont.getIsList()) throw new WTException("Not a contacts list");
			checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE");
			
			int deleted = doContactDelete(con, contactsListId, true);
			DbUtils.commitQuietly(con);
			writeLog("CONTACTLIST_DELETE", String.valueOf(contactsListId));
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			
			for (Integer contactsListId : contactsListIds) {
				OContact cont = condao.selectById(con, contactsListId);
				if (cont == null) throw new NotFoundException("Contact list not found [{}]", contactsListId);
				if (!cont.getIsList()) throw new WTException("Not a contacts list");
				checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE");
				
				doContactDelete(con, contactsListId, true);
			}
			
			DbUtils.commitQuietly(con);
			writeLog("CONTACTLIST_DELETE", "*");
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			con = WT.getConnection(SERVICE_ID, false);
			
			OContact ocont = cdao.selectById(con, contactsListId);
			if (ocont == null) throw new NotFoundException("Contact list not found [{}]", contactsListId);
			if (!ocont.getIsList()) throw new WTException("Not a contacts list");
			checkRightsOnCategoryFolder(ocont.getCategoryId(), "READ");
			
			if (copy || (targetCategoryId != ocont.getCategoryId())) {
				checkRightsOnCategoryElements(targetCategoryId, "CREATE");
				if (!copy) checkRightsOnCategoryElements(ocont.getCategoryId(), "DELETE");
				
				List<OListRecipient> recipients = lrdao.selectByContact(con, contactsListId);
				ContactsList clist = createContactsList(ocont, recipients);

				doMoveContactsList(coreMgr, con, copy, clist, targetCategoryId);
				DbUtils.commitQuietly(con);
				writeLog("CONTACTLIST_UPDATE", String.valueOf(contactsListId));
			}	
			
		} catch(SQLException | DAOException | WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void eraseData(boolean deep) throws WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		CategoryPropsDAO psetDao = CategoryPropsDAO.getInstance();
		ContactDAO contDao = ContactDAO.getInstance();
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
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
					vcaDao.deleteByCategory(con, ocat.getCategoryId());
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
			psetDao.deleteByProfile(con, pid.getDomainId(), pid.getUserId());
			catDao.deleteByProfile(con, pid.getDomainId(), pid.getUserId());
			
			DbUtils.commitQuietly(con);
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw wrapThrowable(ex);
		} catch(Throwable t) {
			DbUtils.rollbackQuietly(con);
			throw t;
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
			Category cat = createCategory(catDao.selectById(con, categoryId));
			if (cat == null) throw new WTException("Category not found [{0}]", categoryId);
			if (!Category.Provider.CARDDAV.equals(cat.getProvider())) {
				throw new WTException("Specified category is not remote (CardDAV) [{0}]", categoryId);
			}
			
			if (Category.Provider.CARDDAV.equals(cat.getProvider())) {
				CategoryRemoteParameters params = LangUtils.deserialize(cat.getParameters(), CategoryRemoteParameters.class);
				if (params == null) throw new WTException("Unable to deserialize remote parameters");
				if (params.url == null) throw new WTException("Remote URL is undefined");
				
				CardDav dav = getCardDav(params.username, params.password);
				
				try {
					DavAddressbook dbook = dav.getAddressbookSyncToken(params.url.toString());
					if (dbook == null) throw new WTException("DAV addressbook not found");
					
					final boolean syncIsSupported = !StringUtils.isBlank(dbook.getSyncToken());
					final String savedSyncToken = params.syncToken;
					
					if (!full && (syncIsSupported && !StringUtils.isBlank(savedSyncToken))) { // Partial update using SYNC mode
						params.syncToken = dbook.getSyncToken();
						
						logger.debug("Retrieving changes [{}, {}]", params.url.toString(), savedSyncToken);
						List<DavSyncStatus> changes = dav.getAddressbookChanges(params.url.toString(), savedSyncToken);
						logger.debug("Endpoint returns {} items", changes.size());
						
						try {
							if (!changes.isEmpty()) {
								ContactDAO contDao = ContactDAO.getInstance();
								Map<String, List<Integer>> contactIdsByHref = contDao.selectHrefsByByCategory(con, categoryId);
								
								// Process changes...
								logger.debug("Processing changes...");
								HashSet<String> hrefs = new HashSet<>();
								for (DavSyncStatus change : changes) {
									if (DavUtil.HTTP_SC_TEXT_OK.equals(change.getResponseStatus())) {
										hrefs.add(change.getPath());

									} else { // Card deleted
										List<Integer> contactIds = contactIdsByHref.get(change.getPath());
										Integer contactId = (contactIds != null) ? contactIds.get(contactIds.size()-1) : null;
										if (contactId == null) {
											logger.warn("Deletion not possible. Card path not found [{}]", change.getPath());
											continue;
										}
										doContactDelete(con, contactId, false);
									}
								}

								// Retrieves events list from DAV endpoint (using multiget)
								logger.debug("Retrieving inserted/updated cards [{}]", hrefs.size());
								List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString(), hrefs);

								// Inserts/Updates data...
								logger.debug("Inserting/Updating cards...");
								for (DavAddressbookCard dcard : dcards) {
									if (logger.isTraceEnabled()) logger.trace("{}", VCardUtils.print(dcard.getCard()));
									List<Integer> contactIds = contactIdsByHref.get(dcard.getPath());
									Integer contactId = (contactIds != null) ? contactIds.get(contactIds.size()-1) : null;
									
									if (contactId != null) {
										doContactDelete(con, contactId, false);
									}
									final ContactInput ci = icalInput.fromVCardFile(dcard.getCard(), null);
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(dcard.getPath());
									ci.contact.setEtag(dcard.geteTag());
									doContactInsert(coreMgr, con, false, ci.contact, ci.picture, null);
								}
							}
							
							catDao.updateParametersById(con, categoryId, LangUtils.serialize(params, CategoryRemoteParameters.class));
							DbUtils.commitQuietly(con);
							
						} catch(Throwable t) {
							DbUtils.rollbackQuietly(con);
							throw new WTException(t, "Error importing vCard");
						}
						
					} else { // Full update or partial computing hashes
						if (syncIsSupported) { // If supported, saves last sync-token issued by the server
							params.syncToken = dbook.getSyncToken();
						} else {
							params.syncToken = null;
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
							}
							if (!full && !syncIsSupported) {
								// This hash-map is only needed when syncing using hashes
								ContactDAO contDao = ContactDAO.getInstance();
								syncByHref = contDao.viewHrefSyncDataByCategory(con, categoryId);
							}	
							
							logger.debug("Processing results...");
							// Define a simple map in order to check duplicates.
							// eg. SOGo passes same card twice :(
							HashSet<String> hrefs = new HashSet<>();
							for (DavAddressbookCard dcard : dcards) {
								if (logger.isTraceEnabled()) logger.trace("{}", VCardUtils.print(dcard.getCard()));
								if (hrefs.contains(dcard.getPath())) {
									logger.trace("Card duplicated. Skipped! [{}]", dcard.getPath());
									continue;
								}
								
								boolean skip = false;
								Integer contactId = null;
								String etag = dcard.geteTag();
								
								if (syncByHref != null) { // Only if: !full && !syncIsSupported
									String prodId = VCardUtils.buildProdId(ManagerUtils.getProductName());
									String hash = DigestUtils.md5Hex(new VCardOutput(prodId).write(dcard.getCard(), true));
									
									VContactHrefSync hrefSync = syncByHref.remove(dcard.getPath());
									if (hrefSync != null) { // Href found -> maybe updated item
										if (!StringUtils.equals(hrefSync.getEtag(), hash)) {
											contactId = hrefSync.getContactId();
											etag = hash;
											logger.trace("Card updated [{}, {}]", dcard.getPath(), hash);
										} else {
											skip = true;
											logger.trace("Card not modified [{}, {}]", dcard.getPath(), hash);
										}
									} else { // Href not found -> added item
										logger.trace("Card newly added [{}]", dcard.getPath());
										etag = hash;
									}
								}
								
								if (!skip) {
									final ContactInput ci = icalInput.fromVCardFile(dcard.getCard(), null);
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(dcard.getPath());
									ci.contact.setEtag(etag);
									
									if (contactId == null) {
										doContactInsert(coreMgr, con, false, ci.contact, ci.picture, null);
									} else {
										ci.contact.setContactId(contactId);
										boolean updated = doContactUpdate(coreMgr, con, false, ci.contact, ci.picture, true);
										if (!updated) throw new WTException("Contact not found [{}]", ci.contact.getContactId());
									}
								}
								
								hrefs.add(dcard.getPath()); // Marks as processed!
							}
							
							if (syncByHref != null) { // Only if: !full && !syncIsSupported
								// Remaining hrefs -> deleted items
								for (VContactHrefSync hrefSync : syncByHref.values()) {
									logger.trace("Card deleted [{}]", hrefSync.getHref());
									doContactDelete(con, hrefSync.getContactId(), false);
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
			} else {
				throw new WTException("Unsupported provider [{0}]", cat.getProvider());
			}
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void syncRemoteCategory9999(int categoryId, boolean full) throws WTException {
		CoreManager coreMgr = WT.getCoreManager(getTargetProfileId());
		final VCardInput icalInput = new VCardInput();
		CategoryDAO catDao = CategoryDAO.getInstance();
		Connection con = null;
		
		try {
			//checkRightsOnCategoryFolder(calendarId, "READ");
			
			con = WT.getConnection(SERVICE_ID, false);
			Category cat = createCategory(catDao.selectById(con, categoryId));
			if (cat == null) throw new WTException("Category not found [{0}]", categoryId);
			if (!Category.Provider.CARDDAV.equals(cat.getProvider())) {
				throw new WTException("Specified category is not remote (CardDAV) [{0}]", categoryId);
			}
			
			if (Category.Provider.CARDDAV.equals(cat.getProvider())) {
				CategoryRemoteParameters params = LangUtils.deserialize(cat.getParameters(), CategoryRemoteParameters.class);
				if (params == null) throw new WTException("Unable to deserialize remote parameters");
				if (params.url == null) throw new WTException("Remote URL is undefined");
				
				CardDav dav = getCardDav(params.username, params.password);
				
				try {
					DavAddressbook dbook = dav.getAddressbookSyncToken(params.url.toString());
					if (dbook == null) throw new WTException("DAV addressbook not found");
					
					final boolean syncIsSupported = !StringUtils.isBlank(dbook.getSyncToken());
					final String savedSyncToken = params.syncToken;
					
					if (full || (syncIsSupported && StringUtils.isBlank(savedSyncToken))) { // Full update
						if (syncIsSupported) { // If supported, saves last sync-token issued by the server
							params.syncToken = dbook.getSyncToken();
						}
						
						// Retrieves cards list from DAV endpoint
						logger.debug("Retrieving whole list [{}]", params.url.toString());
						List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString());
						logger.debug("Endpoint returns {} items", dcards.size());
						
						// Inserts data...
						try {
							logger.debug("Processing results...");
							// Define a simple map in order to check duplicates.
							// eg. SOGo passes same card twice :(
							HashSet<String> hrefs = new HashSet<>();
							doContactsDeleteByCategory(con, categoryId, false);
							for (DavAddressbookCard dcard : dcards) {
								if (logger.isTraceEnabled()) logger.trace("{}", VCardUtils.print(dcard.getCard()));
								if (hrefs.contains(dcard.getPath())) {
									logger.trace("Card duplicated. Skipped! [{}]", dcard.getPath());
									continue;
								}
								
								final ContactInput ci = icalInput.fromVCardFile(dcard.getCard(), null);
								ci.contact.setCategoryId(categoryId);
								ci.contact.setHref(dcard.getPath());
								ci.contact.setEtag(dcard.geteTag());
								doContactInsert(coreMgr, con, false, ci.contact, ci.picture, null);
								
								hrefs.add(dcard.getPath()); // Marks as processed!
							}
							
							catDao.updateParametersById(con, categoryId, LangUtils.serialize(params, CategoryRemoteParameters.class));
							DbUtils.commitQuietly(con);
							
						} catch(Throwable t) {
							DbUtils.rollbackQuietly(con);
							throw new WTException(t, "Error importing vCard");
						}
						
					} else if (syncIsSupported && !StringUtils.isBlank(savedSyncToken)) { // Partial update using sync
						params.syncToken = dbook.getSyncToken();
						
						logger.debug("Retrieving changes [{}, {}]", params.url.toString(), savedSyncToken);
						List<DavSyncStatus> changes = dav.getAddressbookChanges(params.url.toString(), savedSyncToken);
						logger.debug("Endpoint returns {} items", changes.size());
						
						try {
							if (!changes.isEmpty()) {
								ContactDAO contDao = ContactDAO.getInstance();
								Map<String, List<Integer>> contactIdsByHref = contDao.selectHrefsByByCategory(con, categoryId);
								
								// Process changes...
								logger.debug("Processing changes...");
								HashSet<String> hrefs = new HashSet<>();
								for (DavSyncStatus change : changes) {
									if (DavUtil.HTTP_SC_TEXT_OK.equals(change.getResponseStatus())) {
										hrefs.add(change.getPath());

									} else { // Event deleted
										List<Integer> contactIds = contactIdsByHref.get(change.getPath());
										Integer contactId = (contactIds != null) ? contactIds.get(contactIds.size()-1) : null;
										if (contactId == null) {
											logger.warn("Deletion not possible. Card path not found [{}]", change.getPath());
											continue;
										}
										doContactDelete(con, contactId, false);
									}
								}

								// Retrieves events list from DAV endpoint (using multiget)
								logger.debug("Retrieving inserted/updated events [{}]", hrefs.size());
								List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString(), hrefs);

								// Inserts/Updates data...
								logger.debug("Inserting/Updating events...");
								for (DavAddressbookCard dcard : dcards) {
									if (logger.isTraceEnabled()) logger.trace("{}", VCardUtils.print(dcard.getCard()));
									List<Integer> contactIds = contactIdsByHref.get(dcard.getPath());
									Integer contactId = (contactIds != null) ? contactIds.get(contactIds.size()-1) : null;
									
									if (contactId != null) {
										doContactDelete(con, contactId, false);
									}
									final ContactInput ci = icalInput.fromVCardFile(dcard.getCard(), null);
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(dcard.getPath());
									ci.contact.setEtag(dcard.geteTag());
									doContactInsert(coreMgr, con, false, ci.contact, ci.picture, null);
								}
							}
							
							catDao.updateParametersById(con, categoryId, LangUtils.serialize(params, CategoryRemoteParameters.class));
							DbUtils.commitQuietly(con);
							
						} catch(Throwable t) {
							DbUtils.rollbackQuietly(con);
							throw new WTException(t, "Error importing vCard");
						}
						
					} else { // Partial update using manual hash
						ContactDAO contDao = ContactDAO.getInstance();
						
						params.syncToken = null;
						Map<String, VContactHrefSync> syncByHref = contDao.viewHrefSyncDataByCategory(con, categoryId);
						
						// Retrieves events list from DAV endpoint
						logger.debug("Retrieving whole list [{}]", params.url.toString());
						List<DavAddressbookCard> dcards = dav.listAddressbookCards(params.url.toString());
						logger.debug("Endpoint returns {} items", dcards.size());
						
						// Inserts data...
						try {
							// Define a simple map in order to check duplicates.
							// eg. SOGo passes same card twice :(
							HashSet<String> hrefs = new HashSet<>();
							for (DavAddressbookCard dcard : dcards) {
								if (logger.isTraceEnabled()) logger.trace("{}", VCardUtils.print(dcard.getCard()));
								if (hrefs.contains(dcard.getPath())) {
									logger.trace("Card duplicated. Skipped! [{}]", dcard.getPath());
									continue;
								}
								
								String prodId = VCardUtils.buildProdId(ManagerUtils.getProductName());
								String hash = DigestUtils.md5Hex(new VCardOutput(prodId).write(dcard.getCard(), true));
								
								boolean insertOrUpdate = false;
								Integer contactId = null;
								VContactHrefSync hrefSync = syncByHref.remove(dcard.getPath());
								if (hrefSync != null) { // Href found -> maybe updated item
									if (!StringUtils.equals(hrefSync.getEtag(), hash)) {
										insertOrUpdate = true;
										contactId = hrefSync.getContactId();
										logger.trace("Card updated [{}, {}]", dcard.getPath(), hash);
									} else {
										logger.trace("Card not modified [{}, {}]", dcard.getPath(), hash);
									}
								} else { // Href not found -> added item
									insertOrUpdate = true;
									logger.trace("Card newly added [{}]", dcard.getPath());
								}
								
								if (insertOrUpdate) {
									final ContactInput ci = icalInput.fromVCardFile(dcard.getCard(), null);
									ci.contact.setContactId(contactId);
									ci.contact.setCategoryId(categoryId);
									ci.contact.setHref(dcard.getPath());
									ci.contact.setEtag(hash);
									
									if (contactId == null) {
										doContactInsert(coreMgr, con, false, ci.contact, ci.picture, null);
									} else {
										boolean updated = doContactUpdate(coreMgr, con, false, ci.contact, ci.picture, true);
										if (!updated) throw new WTException("Contact not found [{}]", ci.contact.getContactId());
									}
								}
								
								hrefs.add(dcard.getPath()); // Marks as processed!
							}
							
							// Remaining hrefs -> deleted items
							for (VContactHrefSync hrefSync : syncByHref.values()) {
								logger.trace("Card deleted [{}]", hrefSync.getHref());
								doContactDelete(con, hrefSync.getContactId(), false);
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
			} else {
				throw new WTException("Unsupported provider [{0}]", cat.getProvider());
			}
			
		} catch(SQLException | DAOException ex) {
			throw wrapThrowable(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private Category doCategoryUpdate(boolean insert, Connection con, Category cat) throws DAOException, WTException {
		CategoryDAO catDao = CategoryDAO.getInstance();
		
		OCategory ocat = createOCategory(cat);
		if (insert) {
			ocat.setCategoryId(catDao.getSequence(con).intValue());
		}
		fillOCategoryWithDefaults(ocat);
		if (ocat.getIsDefault()) catDao.resetIsDefaultByProfile(con, ocat.getDomainId(), ocat.getUserId());
		if (insert) {
			catDao.insert(con, ocat);
		} else {
			int ret = catDao.update(con, ocat);
			if (ret != 1) return null;
		}
		return createCategory(ocat);
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
	
	private OContact doContactInsert(CoreManager coreMgr, Connection con, boolean isList, Contact contact, ContactPicture picture, String rawVCard) throws DAOException {
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
		
		if (!isList) {
			if (picture != null) {
				doContactPictureInsert(con, ocont.getContactId(), picture);
			}
			if (!StringUtils.isBlank(rawVCard)) {
				doContactVCardInsert(con, ocont.getContactId(), rawVCard);
			}
		}
		return ocont;
	}
	
	private boolean doContactUpdate(CoreManager coreMgr, Connection con, boolean isList, Contact contact, ContactPicture picture, boolean deletePictureIfNull) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		OContact ocont = createOContact(contact);
		if (isList) {
			ocont.setSearchfield(buildSearchfield(ocont));
			int ret = contDao.updateList(con, ocont, createRevisionTimestamp());
			if (ret != 1) return false;
		} else {
			ocont.setSearchfield(buildSearchfield(coreMgr, ocont));
			int ret = contDao.update(con, ocont, createRevisionTimestamp());
			if (ret != 1) return false;
		}
		
		if (!isList) {
			if (picture != null) {
				// Old picture will be eventually removed and the new one inserted
				doContactPictureUpdate(con, ocont.getContactId(), picture);
			} else {
				if (deletePictureIfNull) doContactPictureDelete(con, ocont.getContactId());
			}
		}
		return true;
	}
	
	private int doContactDelete(Connection con, int contactId, boolean logicDelete) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		if (logicDelete) {
			return contDao.logicDeleteById(con, contactId, createRevisionTimestamp());
		} else {
			// List are not supported here
			doContactPictureDelete(con, contactId);
			doContactVCardDelete(con, contactId);
			return contDao.deleteById(con, contactId);
		}
	}
	
	private int doContactsDeleteByCategory(Connection con, int categoryId, boolean logicDelete) throws DAOException {
		ContactDAO contDao = ContactDAO.getInstance();
		
		if (logicDelete) {
			return contDao.logicDeleteByCategory(con, categoryId, createRevisionTimestamp());
			
		} else {
			ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
			ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
			ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
			
			cpicDao.deleteByCategory(con, categoryId);
			vcaDao.deleteByCategory(con, categoryId);
			lrecDao.deleteByCategory(con, categoryId);
			return contDao.deleteByCategory(con, categoryId);
		}
	}
	
	private void doContactMove(CoreManager coreMgr, Connection con, boolean copy, Contact contact, int targetCategoryId) throws DAOException, WTException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		ContactVCardDAO vcaDao = ContactVCardDAO.getInstance();
		
		if (copy) {
			contact.setCategoryId(targetCategoryId);
			ContactPicture pic = createContactPicture(cpicDao.select(con, contact.getContactId()));
			OContactVCard ovca = vcaDao.selectById(con, contact.getContactId());
			String rawVCard = (ovca != null) ? ovca.getRawData() : null;
			doContactInsert(coreMgr, con, false, contact, pic, rawVCard);
			
		} else {
			ContactDAO cdao = ContactDAO.getInstance();
			cdao.updateCategory(con, contact.getContactId(), targetCategoryId, createRevisionTimestamp());
		}
	}
	
	private void doContactPictureInsert(Connection con, int contactId, ContactPicture picture) throws DAOException {
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
	
	private void doContactPictureUpdate(Connection con, int contactId, ContactPicture picture) throws DAOException {
		doContactPictureDelete(con, contactId);
		doContactPictureInsert(con, contactId, picture);
	}
	
	private void doContactPictureDelete(Connection con, int contactId) throws DAOException {
		ContactPictureDAO cpicDao = ContactPictureDAO.getInstance();
		cpicDao.delete(con, contactId);
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
		return vcaDao.deleteById(con, contactId) == 1;
	}
	
	private OContact doContactsListInsert(CoreManager coreMgr, Connection con, ContactsList list) throws DAOException {
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		OContact ocont = doContactInsert(coreMgr, con, true, createContact(list), null, null);
		for (ContactsListRecipient rcpt : list.getRecipients()) {
			OListRecipient olrec = new OListRecipient(rcpt);
			olrec.setContactId(ocont.getContactId());
			olrec.setListRecipientId(lrecDao.getSequence(con).intValue());
			lrecDao.insert(con, olrec);
		}
		return ocont;
	}
	
	private boolean doContactsListUpdate(CoreManager coreMgr, Connection con, ContactsList list) throws DAOException {
		ListRecipientDAO lrecDao = ListRecipientDAO.getInstance();
		
		if (!doContactUpdate(coreMgr, con, true, createContact(list), null, false)) return false;
		//TODO: gestire la modifica determinando gli eliminati e gli aggiunti?
		lrecDao.deleteByContact(con, list.getContactId());
		for (ContactsListRecipient rcpt : list.getRecipients()) {
			OListRecipient olrec = new OListRecipient(rcpt);
			olrec.setContactId(list.getContactId());
			olrec.setListRecipientId(lrecDao.getSequence(con).intValue());
			lrecDao.insert(con, olrec);
		}
		return true;
	}
	
	private boolean doMoveContactsList(CoreManager coreMgr, Connection con, boolean copy, ContactsList clist, int targetCategoryId) throws DAOException, WTException {
		clist.setCategoryId(targetCategoryId);
		if (copy) {
			doContactsListInsert(coreMgr, con, clist);
			return true;
		} else {
			return doContactsListUpdate(coreMgr, con, clist);
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
			throw wrapThrowable(ex);
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
		UserProfileId owner = ownerCache.get(categoryId);
		if (owner == null) throw new WTException("categoryToOwner({0}) -> null", categoryId);
		if (owner.equals(targetPid)) return;
		
		// Checks rights on the wildcard instance (if present)
		CoreManager core = WT.getCoreManager(targetPid);
		String wildcardShareId = shareCache.getWildcardShareFolderIdByOwner(owner);
		if (wildcardShareId != null) {
			if (core.isShareFolderPermitted(wildcardShareId, action)) return;
			//if (core.isShareFolderPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, wildcardShareId)) return;
		}
		
		// Checks rights on category instance
		String shareId = shareCache.getShareFolderIdByFolderId(categoryId);
		if (shareId == null) throw new WTException("categoryToLeafShareId({0}) -> null", categoryId);
		if (core.isShareFolderPermitted(shareId, action)) return;
		//if (core.isShareFolderPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, shareId)) return;
		
		throw new AuthException("Action not allowed on folder share [{0}, {1}, {2}, {3}]", shareId, action, GROUPNAME_CATEGORY, targetPid.toString());
	}
	
	private void checkRightsOnCategoryElements(int categoryId, String action) throws WTException {
		UserProfileId targetPid = getTargetProfileId();
		
		if (RunContext.isWebTopAdmin()) return;
		
		// Skip rights check if running user is resource's owner
		UserProfileId owner = ownerCache.get(categoryId);
		if (owner == null) throw new WTException("categoryToOwner({0}) -> null", categoryId);
		if (owner.equals(targetPid)) return;
		
		// Checks rights on the wildcard instance (if present)
		CoreManager core = WT.getCoreManager(targetPid);
		String wildcardShareId = shareCache.getWildcardShareFolderIdByOwner(owner);
		if (wildcardShareId != null) {
			if (core.isShareElementsPermitted(wildcardShareId, action)) return;
			//if (core.isShareElementsPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, wildcardShareId)) return;
		}
		
		// Checks rights on calendar instance
		String shareId = shareCache.getShareFolderIdByFolderId(categoryId);
		if (shareId == null) throw new WTException("categoryToLeafShareId({0}) -> null", categoryId);
		if (core.isShareElementsPermitted(shareId, action)) return;
		//if (core.isShareElementsPermitted(SERVICE_ID, RESOURCE_CATEGORY, action, shareId)) return;
		
		throw new AuthException("Action not allowed on folderEls share [{0}, {1}, {2}, {3}]", shareId, action, GROUPNAME_CATEGORY, targetPid.toString());
	}
	
	private Category createCategory(OCategory src) {
		if (src == null) return null;
		return fillCategory(new Category(), src);
	}
	
	private Category fillCategory(Category tgt, OCategory src) {
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
		}
		return tgt;
	}
	
	private OCategory createOCategory(Category src) {
		if (src == null) return null;
		return fillOCategory(new OCategory(), src);
	}
	
	private OCategory fillOCategory(OCategory tgt, Category src) {
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
		}
		return tgt;
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
	
	private CategoryPropSet createCategoryPropSet(OCategoryPropSet src) {
		if (src == null) return null;
		return fillCategoryPropSet(new CategoryPropSet(), src);
	}
	
	private CategoryPropSet fillCategoryPropSet(CategoryPropSet tgt, OCategoryPropSet src) {
		if ((tgt != null) && (src != null)) {
			tgt.setHidden(src.getHidden());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.forSerializedName(src.getSync(), Category.Sync.class));
		}
		return tgt;
	}
	
	private OCategoryPropSet createOCategoryPropSet(CategoryPropSet src) {
		if (src == null) return null;
		return fillOCategoryPropSet(new OCategoryPropSet(), src);
	}
	
	private OCategoryPropSet fillOCategoryPropSet(OCategoryPropSet tgt, CategoryPropSet src) {
		if ((tgt != null) && (src != null)) {
			tgt.setHidden(src.getHidden());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.toSerializedName(src.getSync()));
		}
		return tgt;
	}
	
	private ContactCard fillContactCard(ContactCard tgt, VContactCard src) {
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
	
	private ContactsList createContactsList(OContact ocontlst, List<OListRecipient> olstrecs) {
		if (ocontlst == null) return null;
		ContactsList item = new ContactsList();
		item.setContactId(ocontlst.getContactId());
		item.setCategoryId(ocontlst.getCategoryId());
		item.setName(ocontlst.getLastname());
		for (OListRecipient rcpt : olstrecs) {
			item.addRecipient(createContactsListRecipient(rcpt));
		}
		return item;
	}
	
	private ContactsListRecipient createContactsListRecipient(OListRecipient src) {
		if (src == null) return null;
		ContactsListRecipient lstrec = new ContactsListRecipient();
		lstrec.setListRecipientId(src.getListRecipientId());
		lstrec.setRecipient(src.getRecipient());
		lstrec.setRecipientType(src.getRecipientType());
		return lstrec;
	}
	
	private Contact createContact(ContactsList src) {
		if (src == null) return null;
		Contact cont = new Contact();
		cont.setContactId(src.getContactId());
		cont.setCategoryId(src.getCategoryId());
		cont.setLastName(src.getName());
		return cont;
	}
	
	private ContactItem createContactItem(OContact src, boolean hasPicture) {
		if (src == null) return null;
		ContactItem obj = fillContact(new ContactItem(), src);
		obj.setHasPicture(hasPicture);
		return obj;
	}
	
	private <T extends Contact> T fillContact(T tgt, OContact src) {
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
	
	private OContact createOContact(Contact src) {
		if (src == null) return null;
		return fillOContact(new OContact(), src);
	}
	
	private OContact fillOContact(OContact tgt, Contact src) {
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
	
	private OContact fillDefaultsForInsert(OContact tgt) {
		if (tgt != null) {
			if (StringUtils.isBlank(tgt.getPublicUid())) {
				tgt.setPublicUid(ManagerUtils.buildContactUid(tgt.getContactId(), WT.getDomainInternetName(getTargetProfileId().getDomainId())));
			}
			tgt.setHref(ManagerUtils.buildHref(tgt.getPublicUid()));
		}
		return tgt;
	}
	
	private ContactPicture createContactPicture(OContactPicture src) {
		if (src == null) return null;
		return fillContactPicture(new ContactPicture(), src);
	}
	
	private ContactPicture fillContactPicture(ContactPicture tgt, OContactPicture src) {
		if ((tgt != null) && (src != null)) {
			tgt.setWidth(src.getWidth());
			tgt.setHeight(src.getHeight());
			tgt.setMediaType(src.getMediaType());
			tgt.setBytes(src.getBytes());
		}
		return tgt;
	}
	
	private ContactItemEx fillContactEx(ContactItemEx tgt, VContact src) {
		if ((tgt != null) && (src != null)) {
			fillContact(tgt, src);
			tgt.setIsList(src.getIsList());
			tgt.setCompanyAsMasterDataId(src.getCompanyAsMasterDataId());
			tgt.setCategoryDomainId(src.getCategoryDomainId());
			tgt.setCategoryUserId(src.getCategoryUserId());
		}
		return tgt;
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
	
	private class OwnerCache extends AbstractMapCache<Integer, UserProfileId> {

		@Override
		protected void internalInitCache() {}

		@Override
		protected void internalMissKey(Integer key) {
			try {
				UserProfileId owner = findCategoryOwner(key);
				if (owner == null) throw new WTException("Owner not found [{0}]", key);
				put(key, owner);
			} catch(WTException ex) {
				throw new WTRuntimeException(ex.getMessage());
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
