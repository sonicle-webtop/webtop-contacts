/*
 * webtop-contacts is a WebTop Service developed by Sonicle S.r.l.
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
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * "Powered by Sonicle WebTop" logo. If the display of the logo is not reasonably
 * feasible for technical reasons, the Appropriate Legal Notices must display
 * the words "Powered by Sonicle WebTop".
 */
package com.sonicle.webtop.contacts;

import com.sonicle.commons.db.DbUtils;
import com.sonicle.webtop.contacts.bol.OCategory;
import com.sonicle.webtop.contacts.bol.OContact;
import com.sonicle.webtop.contacts.bol.OContactPicture;
import com.sonicle.webtop.contacts.bol.OListRecipient;
import com.sonicle.webtop.contacts.bol.VContact;
import com.sonicle.webtop.contacts.bol.model.CategoryFolder;
import com.sonicle.webtop.contacts.bol.model.CategoryRoot;
import com.sonicle.webtop.contacts.bol.model.Contact;
import com.sonicle.webtop.contacts.bol.model.ContactPicture;
import com.sonicle.webtop.contacts.bol.model.ContactsList;
import com.sonicle.webtop.contacts.bol.model.ContactsListRecipient;
import com.sonicle.webtop.contacts.dal.CategoryDAO;
import com.sonicle.webtop.contacts.dal.ContactDAO;
import com.sonicle.webtop.contacts.dal.ContactPictureDAO;
import com.sonicle.webtop.contacts.dal.ListRecipientDAO;
import com.sonicle.webtop.contacts.directory.DirectoryResult;
import com.sonicle.webtop.contacts.io.ContactFileReader;
import com.sonicle.webtop.contacts.io.ContactReadResult;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.WT;
import com.sonicle.webtop.core.bol.OShare;
import com.sonicle.webtop.core.dal.BaseDAO.RevisionInfo;
import com.sonicle.webtop.core.sdk.BaseManager;
import com.sonicle.webtop.core.RunContext;
import com.sonicle.webtop.core.bol.Owner;
import com.sonicle.webtop.core.bol.model.IncomingShareRoot;
import com.sonicle.webtop.core.bol.model.SharePermsFolder;
import com.sonicle.webtop.core.bol.model.SharePermsElements;
import com.sonicle.webtop.core.bol.model.SharePermsRoot;
import com.sonicle.webtop.core.bol.model.Sharing;
import com.sonicle.webtop.core.dal.CustomerDAO;
import com.sonicle.webtop.core.dal.DAOException;
import com.sonicle.webtop.core.sdk.AuthException;
import com.sonicle.webtop.core.sdk.BaseReminder;
import com.sonicle.webtop.core.sdk.ReminderEmail;
import com.sonicle.webtop.core.sdk.ReminderInApp;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.sdk.WTRuntimeException;
import com.sonicle.webtop.core.sdk.interfaces.IManagerUsesReminders;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
import com.sonicle.webtop.core.util.NotificationHelper;
import eu.medsea.mimeutil.MimeType;
import freemarker.template.TemplateException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.mail.internet.InternetAddress;
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
public class ContactsManager extends BaseManager implements IManagerUsesReminders {
	public static final Logger logger = WT.getLogger(ContactsManager.class);
	private static final String RESOURCE_CATEGORY = "CATEGORY";
	
	//private final LinkedHashMap<String, DirectoryManager> cacheGlobalDirectories = new LinkedHashMap<>();
	//private final LinkedHashMap<Integer, DirectoryManager> cacheDirectories = new LinkedHashMap<>();
	
	private final HashMap<Integer, UserProfile.Id> cacheCategoryToOwner = new HashMap<>();
	private final Object shareCacheLock = new Object();
	private final HashMap<UserProfile.Id, String> cacheOwnerToRootShare = new HashMap<>();
	private final HashMap<UserProfile.Id, String> cacheOwnerToWildcardFolderShare = new HashMap<>();
	private final HashMap<Integer, String> cacheCategoryToFolderShare = new HashMap<>();

	public ContactsManager(RunContext context) {
		super(context);
	}
	
	public ContactsManager(RunContext context, UserProfile.Id targetProfileId) {
		super(context, targetProfileId);
	}
	
	private String getAnniversaryReminderDelivery(HashMap<UserProfile.Id, String> cache, UserProfile.Id pid) {
		if(!cache.containsKey(pid)) {
			ContactsUserSettings cus = new ContactsUserSettings(SERVICE_ID, pid);
			String value = cus.getAnniversaryReminderDelivery();
			cache.put(pid, value);
			return value;
		} else {
			return cache.get(pid);
		}
	}
	
	private DateTime getAnniversaryReminderTime(HashMap<UserProfile.Id, DateTime> cache, UserProfile.Id pid, LocalDate date) {
		if(!cache.containsKey(pid)) {
			LocalTime time = new ContactsUserSettings(SERVICE_ID, pid).getAnniversaryReminderTime();
			//TODO: valutare se uniformare i minuti a quelli consentiti (ai min 0 e 30), se errato non verrà mai preso in considerazione
			UserProfile.Data ud = WT.getUserData(pid);
			DateTime value = new DateTime(ud.getTimezone()).withDate(date).withTime(time);
			cache.put(pid, value);
			return value;
		} else {
			return cache.get(pid);
		}
	}
	
	@Override
	public List<BaseReminder> returnReminders(DateTime now) {
		ArrayList<BaseReminder> alerts = new ArrayList<>();
		HashMap<UserProfile.Id, Boolean> okCache = new HashMap<>();
		HashMap<UserProfile.Id, DateTime> dateTimeCache = new HashMap<>();
		HashMap<UserProfile.Id, String> deliveryCache = new HashMap<>();
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

						if(delivery.equals(ContactsUserSettings.ANNIVERSARY_REMINDER_DELIVERY_EMAIL)) {
							alerts.add(createAnniversaryEmailReminder(ud.getLocale(), ud.getEmail(), true, cont, dateTime));
						} else if(delivery.equals(ContactsUserSettings.ANNIVERSARY_REMINDER_DELIVERY_APP)) {
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

						if(delivery.equals(ContactsUserSettings.ANNIVERSARY_REMINDER_DELIVERY_EMAIL)) {
							alerts.add(createAnniversaryEmailReminder(ud.getLocale(), ud.getEmail(), false, cont, dateTime));
						} else if(delivery.equals(ContactsUserSettings.ANNIVERSARY_REMINDER_DELIVERY_APP)) {
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
	
	/*
	private DirectoryManager getDirectoryManager(int categoryId) throws WTException {
		try {
			synchronized(cacheDirectories) {
				if(!cacheDirectories.containsKey(categoryId)) {
					cacheDirectories.put(categoryId, createDBDManager(getTargetProfileId(), getLocale(), categoryId));
				}
				return cacheDirectories.get(categoryId);
			}
		} catch(SQLException ex) {
			throw new WTException(ex, "DB error");
		}
	}
	
	public void initializeDirectories(UserProfile profile) throws SQLException {
		initGlobalDirectories(profile.getId());
		// Adds global DMs to the sessions
		for (DirectoryManager dm : cacheGlobalDirectories.values()) {
			cacheDirectories.put(dm.getId(), dm);
		}
	}
	
	public void cleanupDirectories() {
		cacheGlobalDirectories.clear();
		cacheDirectories.clear();
	};
	*/
	
	public List<CategoryRoot> listIncomingCategoryRoots() throws WTException {
		CoreManager core = WT.getCoreManager(getRunContext());
		ArrayList<CategoryRoot> roots = new ArrayList();
		HashSet<String> hs = new HashSet<>();
		
		List<IncomingShareRoot> shares = core.listIncomingShareRoots(getTargetProfileId(), SERVICE_ID, RESOURCE_CATEGORY);
		for(IncomingShareRoot share : shares) {
			SharePermsRoot perms = core.getShareRootPermissions(getTargetProfileId(), SERVICE_ID, RESOURCE_CATEGORY, share.getShareId());
			CategoryRoot root = new CategoryRoot(share, perms);
			if(hs.contains(root.getShareId())) continue; // Avoid duplicates ??????????????????????
			hs.add(root.getShareId());
			roots.add(root);
		}
		return roots;
	}
	
	/*
	public CategoryFolder getIncomingCategoryFolder(int categoryId) throws WTException {
		UserProfile.Id ownerId = categoryToOwner(categoryId);
		String rootShareId = ownerToRootShareId(ownerId);
		HashMap<Integer, CategoryFolder> folds = listIncomingCategoryFolders(rootShareId);
		return folds.get(categoryId);
	}
	*/
	
	public HashMap<Integer, CategoryFolder> listIncomingCategoryFolders(String rootShareId) throws WTException {
		CoreManager core = WT.getCoreManager(getRunContext());
		LinkedHashMap<Integer, CategoryFolder> folders = new LinkedHashMap<>();
		UserProfile.Id pid = getTargetProfileId();
		
		// Retrieves incoming folders (from sharing). This lookup already 
		// returns readable shares (we don't need to test READ permission)
		List<OShare> shares = core.listIncomingShareFolders(pid, rootShareId, SERVICE_ID, RESOURCE_CATEGORY);
		for(OShare share : shares) {
			
			List<OCategory> cats = null;
			if(share.hasWildcard()) {
				UserProfile.Id ownerId = core.userUidToProfileId(share.getUserUid());
				cats = listCategories(ownerId);
			} else {
				cats = Arrays.asList(getCategory(Integer.valueOf(share.getInstance())));
			}
			
			for(OCategory cat : cats) {
				SharePermsFolder fperms = core.getShareFolderPermissions(getTargetProfileId(), SERVICE_ID, RESOURCE_CATEGORY, share.getShareId().toString());
				SharePermsElements eperms = core.getShareElementsPermissions(getTargetProfileId(), SERVICE_ID, RESOURCE_CATEGORY, share.getShareId().toString());
				
				if(folders.containsKey(cat.getCategoryId())) {
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
	
	public Sharing getSharing(String shareId) throws WTException {
		CoreManager core = WT.getCoreManager(getRunContext());
		return core.getSharing(getTargetProfileId(), SERVICE_ID, RESOURCE_CATEGORY, shareId);
	}
	
	public void updateSharing(Sharing sharing) throws WTException {
		CoreManager core = WT.getCoreManager(getRunContext());
		core.updateSharing(getTargetProfileId(), SERVICE_ID, RESOURCE_CATEGORY, sharing);
	}
	
	public UserProfile.Id getCategoryOwner(int categoryId) throws WTException {
		return categoryToOwner(categoryId);
	}
	
	public List<OCategory> listCategories() throws WTException {
		return listCategories(getTargetProfileId());
	}
	
	private List<OCategory> listCategories(UserProfile.Id pid) throws WTException {
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			CategoryDAO dao = CategoryDAO.getInstance();
			return dao.selectByDomainUser(con, pid.getDomainId(), pid.getUserId());
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public OCategory getCategory(int categoryId) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(categoryId, "READ");
			con = WT.getConnection(getManifest());
			CategoryDAO dao = CategoryDAO.getInstance();
			return dao.selectById(con, categoryId);
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public OCategory addCategory(OCategory item) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryRoot(item.getProfileId(), "MANAGE");
			con = WT.getConnection(getManifest());
			con.setAutoCommit(false);
			CategoryDAO dao = CategoryDAO.getInstance();
			
			item.setCategoryId(dao.getSequence(con).intValue());
			item.setBuiltIn(false);
			if(item.getIsDefault()) dao.resetIsDefaultByDomainUser(con, item.getDomainId(), item.getUserId());
			item.setRevisionInfo(createRevisionInfo());
			dao.insert(con, item);
			DbUtils.commitQuietly(con);
			return item;
			
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
	
	public OCategory updateCategory(OCategory item) throws Exception {
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(item.getCategoryId(), "UPDATE");
			con = WT.getConnection(getManifest());
			con.setAutoCommit(false);
			CategoryDAO dao = CategoryDAO.getInstance();
			
			if(item.getIsDefault()) dao.resetIsDefaultByDomainUser(con, item.getDomainId(), item.getUserId());
			item.setRevisionInfo(createRevisionInfo());
			dao.update(con, item);
			DbUtils.commitQuietly(con);
			return item;
			
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
	
	public void deleteCategory(int categoryId) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryFolder(categoryId, "DELETE");
			con = WT.getConnection(getManifest());
			
			CategoryDAO dao = CategoryDAO.getInstance();
			dao.deleteById(con, categoryId);
			//TODO: cancellare contatti collegati
			
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
	
	public List<CategoryContacts> listContacts(CategoryRoot root, Integer[] categoryFolders, String searchMode, String pattern) throws Exception {
		return listContacts(root.getOwnerProfileId(), categoryFolders, searchMode, pattern);
	}
	
	public List<CategoryContacts> listContacts(UserProfile.Id pid, Integer[] categoryFolders, String searchMode, String pattern) throws Exception {
		CategoryDAO catdao = CategoryDAO.getInstance();
		ContactDAO condao = ContactDAO.getInstance();
		ArrayList<CategoryContacts> catContacts = new ArrayList<>();
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			
			// Lists desired groups (tipically visibles) coming from passed list
			// Passed ids should belong to referenced folder(group), 
			// this is ensured using domainId and userId parameters in below query.
			List<OCategory> cats = catdao.selectByDomainUserIn(con, pid.getDomainId(), pid.getUserId(), categoryFolders);
			List<VContact> vcs = null;
			for(OCategory cat : cats) {
				checkRightsOnCategoryFolder(cat.getCategoryId(), "READ");
				vcs = condao.viewByCategoryPattern(con, cat.getCategoryId(), searchMode, pattern);
				catContacts.add(new CategoryContacts(cat, vcs, null));
			}
			return catContacts;
		
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	/*
	public List<CategoryContacts> listContacts999(UserProfile.Id pid, String[] categoryFolders, String pattern) throws Exception {
		CategoryDAO datdao = CategoryDAO.getInstance();
		ArrayList<CategoryContacts> foldContacts = new ArrayList<>();
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			
			// Lists desired groups (tipically visibles) coming from passed list
			// Passed ids should belong to referenced folder(group), 
			// this is ensured using domainId and userId parameters in below query.
			Integer[] categories = toIntArray(categoryFolders);
			List<OCategory> cats = datdao.selectByDomainUserIn(con, pid.getDomainId(), pid.getUserId(), categories);
			DirectoryManager dm = null;
			DirectoryResult dr = null;
			for(OCategory cat : cats) {
				checkRightsOnCategoryFolder(cat.getCategoryId(), "READ"); // Rights check!
				
				dm = getDirectoryManager(cat.getCategoryId());
				dr = dm.lookup(pattern, getLocale(), false, false);
				foldContacts.add(new CategoryContacts(cat, null, dr));
			}
			return foldContacts;
		
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	*/
	
	public Contact getContact(int contactId) throws WTException {
		ContactDAO cntdao = ContactDAO.getInstance();
		ContactPictureDAO picdao = ContactPictureDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = cntdao.selectById(con, contactId);
			if(cont == null || cont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ"); // Rights check!
			
			boolean hasPicture = picdao.hasPicture(con, contactId);
			return createContact(cont, hasPicture);
		
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void addContact(Contact contact) throws WTException {
		addContact(contact, null);
	}
	
	public void addContact(Contact contact, ContactPicture picture) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(contact.getCategoryId(), "CREATE"); // Rights check!
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			doInsertContact(con, false, contact, picture);
			DbUtils.commitQuietly(con);
			
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
	
	public void updateContact(Contact contact) throws WTException {
		updateContact(contact, null);
	}
	
	public void updateContact(Contact contact, ContactPicture picture) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(contact.getCategoryId(), "UPDATE"); // Rights check!
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			doUpdateContact(con, false, contact, picture);
			DbUtils.commitQuietly(con);
			
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
	
	public ContactPicture getContactPicture(int contactId) throws WTException {
		ContactDAO cntdao = ContactDAO.getInstance();
		ContactPictureDAO dao = ContactPictureDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = cntdao.selectById(con, contactId);
			if(cont == null) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ"); // Rights check!
			
			OContactPicture pic = dao.select(con, contactId);
			return (pic == null) ? null : new ContactPicture(pic);
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void updateContactPicture(int contactId, ContactPicture picture) throws WTException {
		ContactDAO cntdao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			OContact cont = cntdao.selectById(con, contactId);
			if(cont == null || cont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryElements(cont.getCategoryId(), "UPDATE"); // Rights check!
			
			cntdao.updateRevision(con, contactId, createRevisionInfo());
			doUpdateContactPicture(con, contactId, picture);
			DbUtils.commitQuietly(con);
			
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
	
	public void deleteContactsByCategory(int categoryId) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(categoryId, "DELETE"); // Rights check!
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			doDeleteContactsByCategory(con, categoryId, false);
			DbUtils.commitQuietly(con);
			
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
	
	public void deleteContact(int contactId) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = cdao.selectById(con, contactId);
			if(cont == null || cont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE"); // Rights check!
			
			con.setAutoCommit(false);
			doDeleteContact(con, contactId);
			DbUtils.commitQuietly(con);
			
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
	
	public void deleteContact(ArrayList<Integer> contactIds) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			for(Integer contactId : contactIds) {
				if(contactId == null) continue;
				OContact cont = cdao.selectById(con, contactId);
				if(cont == null || cont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
				checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE"); // Rights check!
				
				doDeleteContact(con, contactId);
			}
			
			DbUtils.commitQuietly(con);
			
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
	
	public void copyContact(boolean move, int contactId, int targetCategoryId) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		ContactPictureDAO pdao = ContactPictureDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			checkRightsOnCategoryElements(targetCategoryId, "UPDATE"); // Rights check!
			
			OContact cont = cdao.selectById(con, contactId);
			if(cont == null || cont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ"); // Rights check!
			
			boolean hasPicture = pdao.hasPicture(con, contactId);
			Contact contact = createContact(cont, hasPicture);
			
			con.setAutoCommit(false);
			doCopyContact(con, move, contact, targetCategoryId);
			DbUtils.commitQuietly(con);
			
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
	
	public ContactsList getContactsList(int contactId) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		ListRecipientDAO lrdao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = cdao.selectById(con, contactId);
			if(cont == null || !cont.getIsList()) throw new WTException("Unable to retrieve contact [{0}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ"); // Rights check!
			
			List<OListRecipient> recipients = lrdao.selectByContact(con, contactId);
			return createContactsList(cont, recipients);
		
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void addContactsList(ContactsList list) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(list.getCategoryId(), "CREATE"); // Rights check!
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			doInsertContactsList(con, list);
			DbUtils.commitQuietly(con);
			
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
	
	public void updateContactsList(ContactsList list) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(list.getCategoryId(), "UPDATE"); // Rights check!
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			doUpdateContactsList(con, list);
			DbUtils.commitQuietly(con);
			
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
	
	public void deleteContactsList(int contactsListId) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = cdao.selectById(con, contactsListId);
			if(cont == null || !cont.getIsList()) throw new WTException("Unable to retrieve contactsList [{0}]", contactsListId);
			checkRightsOnCategoryElements(cont.getCategoryId(), "DELETE"); // Rights check!
			
			con.setAutoCommit(false);
			doDeleteContact(con, contactsListId);
			DbUtils.commitQuietly(con);
			
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
	
	public void deleteContactsListsByCategory(int categoryId) throws WTException {
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(categoryId, "DELETE"); // Rights check!
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			doDeleteContactsByCategory(con, categoryId, true);
			DbUtils.commitQuietly(con);
			
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
	
	public void copyContactsList(boolean move, int contactsListId, int targetCategoryId) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		ListRecipientDAO lrdao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			checkRightsOnCategoryElements(targetCategoryId, "UPDATE"); // Rights check!
			
			OContact cont = cdao.selectById(con, contactsListId);
			if(cont == null || !cont.getIsList()) throw new WTException("Unable to retrieve contactsList [{0}]", contactsListId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ"); // Rights check!
			
			List<OListRecipient> recipients = lrdao.selectByContact(con, contactsListId);
			ContactsList clist = createContactsList(cont, recipients);
			
			con.setAutoCommit(false);
			doCopyContactsList(con, move, clist, targetCategoryId);
			DbUtils.commitQuietly(con);
			
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
	
	
	
	public LogEntries importEvents(int categoryId, ContactFileReader rea, File file, String mode) throws WTException {
		LogEntries log = new LogEntries();
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(categoryId, "CREATE"); // Rights check!
			if(mode.equals("copy")) checkRightsOnCategoryElements(categoryId, "DELETE"); // Rights check!
			
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Started at {0}", new DateTime()));
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Reading source file..."));
			ArrayList<ContactReadResult> parsed = null;
			try {
				parsed = rea.listEvents(log, file);
			} catch(IOException ex) {
				log.addMaster(new MessageLogEntry(LogEntry.LEVEL_ERROR, "Unable to complete parsing. Reason: {0}", ex.getMessage()));
				throw new WTException(ex);
			}
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "{0} contact/s found!", parsed.size()));
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			if(mode.equals("copy")) {
				log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Cleaning previous contacts..."));
				int del = doDeleteContactsByCategory(con, categoryId, false);
				log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "{0} contact/s deleted!", del));
			}
			
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Importing..."));
			int count = 0;
			for(ContactReadResult parse : parsed) {
				parse.contact.setCategoryId(categoryId);
				try {
					doInsertContact(con, false, parse.contact, parse.picture);
					DbUtils.commitQuietly(con);
					count++;
				} catch(Exception ex) {
					logger.trace("Error inserting contact", ex);
					DbUtils.rollbackQuietly(con);
					log.addMaster(new MessageLogEntry(LogEntry.LEVEL_ERROR, "Unable to import contact [{0}, {1}, {2}]. Reason: {3}", parse.contact.getFirstName(), parse.contact.getLastName(), parse.contact.getPublicUid(), ex.getMessage()));
				}
			}
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "{0} contact/s imported!", count));
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Ended at {0}", new DateTime()));
		}
		return log;
	}
	
	
	
	
	/*
	public LogEntries importVCard(int categoryId, InputStream is, String mode) throws WTException {
		LogEntries log = new LogEntries();
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(categoryId, "CREATE"); // Rights check!
			if(mode.equals("copy")) checkRightsOnCategoryElements(categoryId, "DELETE"); // Rights check!
			
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Started at {0}", new DateTime()));
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Reading vCard file..."));
			ArrayList<ParseResult> parsed = null;
			try {
				parsed = VCardHelper.parseVCard(log, is);
			} catch(IOException ex) {
				log.addMaster(new MessageLogEntry(LogEntry.LEVEL_ERROR, "Unable to complete parsing. Reason: {0}", ex.getMessage()));
				throw new WTException(ex);
			}
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "{0} contacts/s found!", parsed.size()));
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			if(mode.equals("copy")) {
				log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Cleaning previous contacts..."));
				int del = doDeleteContactsByCategory(con, categoryId, false);
				log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "{0} contact/s deleted!", del));
			}
			
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Importing..."));
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
					log.addMaster(new MessageLogEntry(LogEntry.LEVEL_ERROR, "Unable to import contact [{0}, {1}, {2}]. Reason: {3}", parse.contact.getFirstName(), parse.contact.getLastName(), parse.contact.getPublicUid(), ex.getMessage()));
				}
			}
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "{0} contact/s imported!", count));
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Ended at {0}", new DateTime()));
			//TODO: inviare email report
			//for(LogEntry entry : log) {
			//	logger.trace("{}", ((MessageLogEntry)entry).getMessage());
			//}
		}
		return log;
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private OContact doInsertContact(Connection con, boolean isList, Contact contact, ContactPicture picture) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		
		try {
			OContact item = new OContact(contact);
			item.setIsList(isList);
			if(StringUtils.isEmpty(contact.getPublicUid())) contact.setPublicUid(WT.generateUUID());
			item.setStatus(OContact.STATUS_NEW);
			item.setRevisionInfo(createRevisionInfo());
			item.setSearchfield(StringUtils.lowerCase(buildSearchfield(item)));
			item.setContactId(cdao.getSequence(con).intValue());
			cdao.insert(con, item);
			
			if(contact.getHasPicture()) {
				if(picture != null) {
					doInsertContactPicture(con, item.getContactId(), picture);
				}
			}
			
			return item;
			
		} catch(WTException ex) {
			throw ex;
		}
	}
	
	private void doUpdateContact(Connection con, boolean isList, Contact contact, ContactPicture picture) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		
		try {
			OContact item = new OContact(contact);
			item.setIsList(isList); // This is necessary because update method in dao writes all fields!!!
			item.setStatus(OContact.STATUS_MODIFIED);
			item.setRevisionInfo(createRevisionInfo());
			item.setSearchfield(StringUtils.lowerCase(buildSearchfield(item)));
			cdao.update(con, item);
			
			if(contact.getHasPicture()) {
				if(picture != null) {
					doUpdateContactPicture(con, item.getContactId(), picture);
				}
			} else {
				doDeleteContactPicture(con, item.getContactId());
			}
		
		} catch(WTException ex) {
			throw ex;
		}
	}
	
	private int doDeleteContact(Connection con, int contactId) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		return cdao.logicDeleteById(con, contactId, createRevisionInfo());
	}
	
	private int doDeleteContactsByCategory(Connection con, int categoryId, boolean list) throws WTException {
		ContactDAO cdao = ContactDAO.getInstance();
		return cdao.logicDeleteByCategoryId(con, categoryId, list, createRevisionInfo());
	}
	
	private void doCopyContact(Connection con, boolean move, Contact contact, int targetCategoryId) throws WTException {
		ContactPictureDAO pdao = ContactPictureDAO.getInstance();
		
		contact.setCategoryId(targetCategoryId);
		if(move) {
			doUpdateContact(con, false, contact, null);
		} else {
			if(contact.getHasPicture()) {
				OContactPicture pic = pdao.select(con, contact.getContactId());
				doInsertContact(con, false, contact, new ContactPicture(pic));
			} else {
				doInsertContact(con, false, contact, null);
			}
		}
	}
	
	private void doInsertContactPicture(Connection con, int contactId, ContactPicture picture) throws WTException {
		ContactPictureDAO dao = ContactPictureDAO.getInstance();
		
		try {
			OContactPicture pic = new OContactPicture();
			pic.setContactId(contactId);
			pic.setMediaType(picture.getMediaType());
			
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(picture.getBytes()));
			if((bi.getWidth() > 720) || (bi.getHeight() > 720)) {
				bi = Scalr.resize(bi, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 720);
				pic.setWidth(bi.getWidth());
				pic.setHeight(bi.getHeight());
				String formatName = new MimeType(picture.getMediaType()).getSubType();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					ImageIO.write(bi, formatName, baos);
					baos.flush();
					pic.setBytes(baos.toByteArray());
				} catch(IOException ex1) {
					logger.warn("Error resizing image", ex1);
				} finally {
					IOUtils.closeQuietly(baos);
				}
			} else {
				pic.setWidth(bi.getWidth());
				pic.setHeight(bi.getHeight());
				pic.setBytes(picture.getBytes());
			}
			dao.insert(con, pic);
			
		} catch(IOException ex) {
			throw new WTException(ex, "Unable to read image");
		}
	}
	
	public void doUpdateContactPicture(Connection con, int contactId, ContactPicture picture) throws WTException {
		if(picture == null) throw new WTException("Picture not defined");
		doDeleteContactPicture(con, contactId);
		doInsertContactPicture(con, contactId, picture);
	}
	
	private void doDeleteContactPicture(Connection con, int contactId) throws WTException {
		ContactPictureDAO dao = ContactPictureDAO.getInstance();
		dao.delete(con, contactId);
	}
	
	private void doInsertContactsList(Connection con, ContactsList list) throws WTException {
		ListRecipientDAO lrdao = ListRecipientDAO.getInstance();
		
		try {
			OContact cont = doInsertContact(con, true, createContact(list), null);
			for(ContactsListRecipient rcpt : list.getRecipients()) {
				OListRecipient item = new OListRecipient(rcpt);
				item.setContactId(cont.getContactId());
				item.setListRecipientId(lrdao.getSequence(con).intValue());
				lrdao.insert(con, item);
			}
			
		} catch(WTException ex) {
			throw ex;
		}
	}
	
	private void doUpdateContactsList(Connection con, ContactsList list) throws WTException {
		ListRecipientDAO lrdao = ListRecipientDAO.getInstance();
		
		try {
			doUpdateContact(con, true, createContact(list), null);
			//TODO: gestire la modifica determinando gli eliminati e gli aggiunti?
			lrdao.deleteByContact(con, list.getContactId());
			for(ContactsListRecipient rcpt : list.getRecipients()) {
				OListRecipient item = new OListRecipient(rcpt);
				item.setContactId(list.getContactId());
				item.setListRecipientId(lrdao.getSequence(con).intValue());
				lrdao.insert(con, item);
			}
			
		} catch(WTException ex) {
			throw ex;
		}
	}
	
	private void doCopyContactsList(Connection con, boolean move, ContactsList clist, int targetCategoryId) throws WTException {
		clist.setCategoryId(targetCategoryId);
		if(move) {
			doUpdateContactsList(con, clist);
		} else {
			doInsertContactsList(con, clist);
		}
	}
	
	private String buildSearchfield(OContact contact) {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.defaultString(contact.getLastname()));
		sb.append(StringUtils.defaultString(contact.getFirstname()));
		
		String customer = null;
		if(!StringUtils.isEmpty(contact.getCompany())) {
			customer = lookupCustomerDescription(contact.getCompany());
		}
		String company = StringUtils.defaultIfBlank(customer, contact.getCompany());
		sb.append(StringUtils.defaultString(company));
		return sb.toString();
	}
	
	private String lookupCustomerDescription(String customerId) {
		CustomerDAO cusdao = CustomerDAO.getInstance();
		Connection con = null;

		try {
			con = WT.getCoreConnection();
			return cusdao.selectDescriptionById(con, customerId);
		} catch(SQLException | DAOException ex) {
			logger.error("Error getting description from customers [{}]", customerId);
			return null;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private ContactsList createContactsList(OContact clist, List<OListRecipient> rcpts) {
		ContactsList item = new ContactsList(clist.getContactId(), clist.getCategoryId());
		item.setName(clist.getLastname());
		for(OListRecipient rcpt : rcpts) {
			item.addRecipient(new ContactsListRecipient(rcpt));
		}
		return item;
	}
	
	private Contact createContact(ContactsList cont) {
		Contact item = new Contact(cont.getContactId(), cont.getCategoryId());
		item.setCategoryId(cont.getCategoryId());
		item.setLastName(cont.getName());
		item.setHasPicture(false);
		return item;
	}
	
	private Contact createContact(OContact cont, boolean hasPicture) {
		Contact item = new Contact(cont.getContactId(), cont.getCategoryId());
		item.setStatus(cont.getStatus());
		item.setTitle(cont.getTitle());
		item.setFirstName(cont.getFirstname());
		item.setLastName(cont.getLastname());
		item.setNickname(cont.getNickname());
		item.setGender(cont.getGender());
		item.setWorkAddress(cont.getWorkAddress());
		item.setWorkPostalCode(cont.getWorkPostalcode());
		item.setWorkCity(cont.getWorkCity());
		item.setWorkState(cont.getWorkState());
		item.setWorkCountry(cont.getWorkCountry());
		item.setWorkTelephone(cont.getWorkTelephone());
		item.setWorkTelephone2(cont.getWorkTelephone2());
		item.setWorkMobile(cont.getWorkMobile());
		item.setWorkFax(cont.getWorkFax());
		item.setWorkPager(cont.getWorkPager());
		item.setWorkEmail(cont.getWorkEmail());
		item.setWorkInstantMsg(cont.getWorkIm());
		item.setHomeAddress(cont.getHomeAddress());
		item.setHomePostalCode(cont.getHomePostalcode());
		item.setHomeCity(cont.getHomeCity());
		item.setHomeState(cont.getHomeState());
		item.setHomeCountry(cont.getHomeCountry());
		item.setHomeTelephone(cont.getHomeTelephone());
		item.setHomeTelephone2(cont.getHomeTelephone2());
		item.setHomeFax(cont.getHomeFax());
		item.setHomePager(cont.getHomePager());
		item.setHomeEmail(cont.getHomeEmail());
		item.setHomeInstantMsg(cont.getHomeIm());
		item.setOtherAddress(cont.getOtherAddress());
		item.setOtherPostalCode(cont.getOtherPostalcode());
		item.setOtherCity(cont.getOtherCity());
		item.setOtherState(cont.getOtherState());
		item.setOtherCountry(cont.getOtherCountry());
		item.setOtherEmail(cont.getOtherEmail());
		item.setOtherInstantMsg(cont.getOtherIm());
		item.setCompany(cont.getCompany());
		item.setFunction(cont.getFunction());
		item.setDepartment(cont.getDepartment());
		item.setManager(cont.getManager());
		item.setAssistant(cont.getAssistant());
		item.setAssistantTelephone(cont.getAssistantTelephone());
		item.setPartner(cont.getPartner());
		item.setBirthday(cont.getBirthday());
		item.setAnniversary(cont.getAnniversary());
		item.setUrl(cont.getUrl());
		item.setNotes(cont.getNotes());
		item.setHasPicture(hasPicture);
		return item;
	}
	
	private void buildShareCache() {
		CoreManager core = WT.getCoreManager(getRunContext());
		UserProfile.Id pid = getTargetProfileId();
		try {
			cacheOwnerToRootShare.clear();
			cacheOwnerToWildcardFolderShare.clear();
			cacheCategoryToFolderShare.clear();
			for(CategoryRoot root : listIncomingCategoryRoots()) {
				cacheOwnerToRootShare.put(root.getOwnerProfileId(), root.getShareId());
				for(OShare folder : core.listIncomingShareFolders(pid, root.getShareId(), SERVICE_ID, RESOURCE_CATEGORY)) {
					if(folder.hasWildcard()) {
						UserProfile.Id ownerId = core.userUidToProfileId(folder.getUserUid());
						cacheOwnerToWildcardFolderShare.put(ownerId, folder.getShareId().toString());
					} else {
						cacheCategoryToFolderShare.put(Integer.valueOf(folder.getInstance()), folder.getShareId().toString());
					}
				}
			}
		} catch(WTException ex) {
			throw new WTRuntimeException(ex.getMessage());
		}
	}
	
	private String ownerToRootShareId(UserProfile.Id owner) {
		synchronized(shareCacheLock) {
			if(!cacheOwnerToRootShare.containsKey(owner)) buildShareCache();
			return cacheOwnerToRootShare.get(owner);
		}
	}
	
	private String ownerToWildcardFolderShareId(UserProfile.Id ownerPid) {
		synchronized(shareCacheLock) {
			if(!cacheOwnerToWildcardFolderShare.containsKey(ownerPid) && cacheOwnerToRootShare.isEmpty()) buildShareCache();
			return cacheOwnerToWildcardFolderShare.get(ownerPid);
		}
	}
	
	private String categoryToFolderShareId(int category) {
		synchronized(shareCacheLock) {
			if(!cacheCategoryToFolderShare.containsKey(category)) buildShareCache();
			return cacheCategoryToFolderShare.get(category);
		}
	}
	
	private UserProfile.Id categoryToOwner(int categoryId) {
		synchronized(cacheCategoryToOwner) {
			if(cacheCategoryToOwner.containsKey(categoryId)) {
				return cacheCategoryToOwner.get(categoryId);
			} else {
				try {
					UserProfile.Id owner = findCategoryOwner(categoryId);
					cacheCategoryToOwner.put(categoryId, owner);
					return owner;
				} catch(WTException ex) {
					throw new WTRuntimeException(ex.getMessage());
				}
			}
		}
	}
	
	private UserProfile.Id findCategoryOwner(int categoryId) throws WTException {
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			CategoryDAO dao = CategoryDAO.getInstance();
			Owner owner = dao.selectOwnerById(con, categoryId);
			if(owner == null) throw new WTException("Category not found [{0}]", categoryId);
			return new UserProfile.Id(owner.getDomainId(), owner.getUserId());
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private void checkRightsOnCategoryRoot(UserProfile.Id ownerPid, String action) throws WTException {
		if(WT.isWebTopAdmin(getRunProfileId())) return;
		if(ownerPid.equals(getTargetProfileId())) return;
		
		String shareId = ownerToRootShareId(ownerPid);
		if(shareId == null) throw new WTException("ownerToRootShareId({0}) -> null", ownerPid);
		CoreManager core = WT.getCoreManager(getRunContext());
		if(core.isShareRootPermitted(getRunProfileId(), SERVICE_ID, RESOURCE_CATEGORY, action, shareId)) return;
		
		throw new AuthException("Action not allowed on root share [{0}, {1}, {2}, {3}]", shareId, action, RESOURCE_CATEGORY, getRunProfileId().toString());
	}
	
	private void checkRightsOnCategoryFolder(int categoryId, String action) throws WTException {
		if(WT.isWebTopAdmin(getRunProfileId())) return;
		
		// Skip rights check if running user is resource's owner
		UserProfile.Id ownerPid = categoryToOwner(categoryId);
		if(ownerPid.equals(getTargetProfileId())) return;
		
		// Checks rights on the wildcard instance (if present)
		CoreManager core = WT.getCoreManager(getRunContext());
		String wildcardShareId = ownerToWildcardFolderShareId(ownerPid);
		if(wildcardShareId != null) {
			if(core.isShareFolderPermitted(getRunProfileId(), SERVICE_ID, RESOURCE_CATEGORY, action, wildcardShareId)) return;
		}
		
		// Checks rights on category instance
		String shareId = categoryToFolderShareId(categoryId);
		if(shareId == null) throw new WTException("categoryToLeafShareId({0}) -> null", categoryId);
		if(core.isShareFolderPermitted(getRunProfileId(), SERVICE_ID, RESOURCE_CATEGORY, action, shareId)) return;
		
		throw new AuthException("Action not allowed on folder share [{0}, {1}, {2}, {3}]", shareId, action, RESOURCE_CATEGORY, getRunProfileId().toString());
	}
	
	private void checkRightsOnCategoryElements(int categoryId, String action) throws WTException {
		if(WT.isWebTopAdmin(getRunProfileId())) return;
		
		// Skip rights check if running user is resource's owner
		UserProfile.Id ownerPid = categoryToOwner(categoryId);
		if(ownerPid.equals(getTargetProfileId())) return;
		
		// Checks rights on the wildcard instance (if present)
		CoreManager core = WT.getCoreManager(getRunContext());
		String wildcardShareId = ownerToWildcardFolderShareId(ownerPid);
		if(wildcardShareId != null) {
			if(core.isShareElementsPermitted(getRunProfileId(), SERVICE_ID, RESOURCE_CATEGORY, action, wildcardShareId)) return;
		}
		
		// Checks rights on calendar instance
		String shareId = categoryToFolderShareId(categoryId);
		if(shareId == null) throw new WTException("categoryToLeafShareId({0}) -> null", categoryId);
		if(core.isShareElementsPermitted(getRunProfileId(), SERVICE_ID, RESOURCE_CATEGORY, action, shareId)) return;
		
		throw new AuthException("Action not allowed on folderEls share [{0}, {1}, {2}, {3}]", shareId, action, RESOURCE_CATEGORY, getRunProfileId().toString());
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
		String body = buildReminderEmailBody(locale, birthday, recipient.getAddress(), fullName);
		
		ReminderEmail alert = new ReminderEmail(SERVICE_ID, contact.getCategoryProfileId(), type, contact.getContactId().toString());
		alert.setSubject(subject);
		alert.setBody(body);
		return alert;
	}
	
	private String buildReminderEmailBody(Locale locale, boolean birthday, String recipientEmail, String contactFullName) {
		String resKey = (birthday) ? ContactsLocale.TPL_ANNIVERSARY_HEADER_BIRTHDAY : ContactsLocale.TPL_ANNIVERSARY_HEADER_ANNIVERSARY;
		
		try {
			String source = NotificationHelper.buildSource(locale, SERVICE_ID);
			String bodyHeader = MessageFormat.format(lookupResource(locale, resKey), contactFullName);
			String why = lookupResource(locale, ContactsLocale.TPL_ANNIVERSARY_FOOTER_WHY);
			Map map = NotificationHelper.generateNotificationTplStrings(locale, source, recipientEmail, bodyHeader, null, why);
			return WT.buildTemplate(SERVICE_ID, "tpl_anniversary.html", map);
		} catch(IOException | TemplateException ex) {
			logger.error("Error generating reminder body", ex);
			return null;
		}
	}
	
	private RevisionInfo createRevisionInfo() {
		return new RevisionInfo("WT", getRunContext().getProfileId().toString());
	}
	
	private Integer[] toIntArray(String[] in) {
		Integer[] out = new Integer[in.length];
		for(int i=0; i<in.length; i++) out[i] = Integer.valueOf(in[i]);
		return out;
	}
	
	public static class CategoryContacts {
		public final OCategory folder;
		public final List<VContact> contacts;
		public final DirectoryResult result;
		
		public CategoryContacts(OCategory folder, List<VContact> contacts, DirectoryResult result) {
			this.folder = folder;
			this.contacts = contacts;
			this.result = result;
		}
	}
}
