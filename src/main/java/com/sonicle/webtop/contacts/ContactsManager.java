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
import com.sonicle.webtop.contacts.VCardHelper.ParseResult;
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
import com.sonicle.webtop.contacts.directory.DBDirectoryManager;
import com.sonicle.webtop.contacts.directory.DirectoryManager;
import com.sonicle.webtop.contacts.directory.DirectoryResult;
import com.sonicle.webtop.contacts.directory.LDAPDirectoryManager;
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
import com.sonicle.webtop.core.sdk.ReminderAlert;
import com.sonicle.webtop.core.sdk.ReminderAlertEmail;
import com.sonicle.webtop.core.sdk.ReminderAlertWeb;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.sdk.WTRuntimeException;
import com.sonicle.webtop.core.sdk.interfaces.IManagerUsesReminders;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
import eu.medsea.mimeutil.MimeType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.sql.DataSource;
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
	public List<ReminderAlert> returnReminderAlerts(DateTime now) {
		ArrayList<ReminderAlert> alerts = new ArrayList<>();
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
							alerts.add(createAnniversaryReminderAlertEmail(ud.getLocale(), true, cont, dateTime));
						} else if(delivery.equals(ContactsUserSettings.ANNIVERSARY_REMINDER_DELIVERY_APP)) {
							alerts.add(createAnniversaryReminderAlertWeb(ud.getLocale(), true, cont, dateTime));
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
							alerts.add(createAnniversaryReminderAlertEmail(ud.getLocale(), false, cont, dateTime));
						} else if(delivery.equals(ContactsUserSettings.ANNIVERSARY_REMINDER_DELIVERY_APP)) {
							alerts.add(createAnniversaryReminderAlertWeb(ud.getLocale(), false, cont, dateTime));
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
	
	public Collection<CategoryFolder> listIncomingCategoryFolders(String rootShareId) throws WTException {
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
		return folders.values();
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
				vcs = condao.viewByCategoryQuery(con, cat.getCategoryId(), searchMode, pattern);
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
			if(cont == null) throw new WTException("Unable to retrieve contact [{}]", contactId);
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
			
			doInsertContact(con, contact, picture);
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
			
			doUpdateContact(con, contact, picture);
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
			if(cont == null) throw new WTException("Unable to retrieve contact [{}]", contactId);
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
			if(cont == null) throw new WTException("Unable to retrieve contact [{}]", contactId);
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
	
	public ContactsList getContactsList(int contactId) throws WTException {
		ContactDAO cntdao = ContactDAO.getInstance();
		ListRecipientDAO rcptdao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			
			OContact cont = cntdao.selectById(con, contactId);
			if(cont == null) throw new WTException("Unable to retrieve contact [{}]", contactId);
			checkRightsOnCategoryFolder(cont.getCategoryId(), "READ"); // Rights check!
			
			List<OListRecipient> recipients = rcptdao.selectByList(con, cont.getListId());
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
	
	public void importVCard(int categoryId, InputStream is) throws WTException {
		LogEntries log = new LogEntries();
		Connection con = null;
		
		try {
			checkRightsOnCategoryElements(categoryId, "CREATE"); // Rights check!
			
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Started at {0}", new DateTime()));
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Parsing vCard file..."));
			ArrayList<ParseResult> parsed = null;
			try {
				parsed = VCardHelper.parseVCard(log, is);
			} catch(IOException ex) {
				log.addMaster(new MessageLogEntry(LogEntry.LEVEL_ERROR, "Unable to complete parsing. Reason: {0}", ex.getMessage()));
				throw new WTException(ex);
			}
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "{0} contacts/s found!", parsed.size()));
			
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Importing..."));
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			int count = 0;
			for(ParseResult parse : parsed) {
				parse.contact.setCategoryId(categoryId);
				try {
					doInsertContact(con, parse.contact, parse.picture);
					DbUtils.commitQuietly(con);
					count++;
				} catch(Exception ex) {
					logger.trace("Error inserting contact", ex);
					DbUtils.rollbackQuietly(con);
					log.addMaster(new MessageLogEntry(LogEntry.LEVEL_ERROR, "Unable to import contact [{0}, {1}, {2}]. Reason: {3}", parse.contact.getFirstName(), parse.contact.getLastName(), parse.contact.getPublicUid(), ex.getMessage()));
				}
			}
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "{0} contacts/s imported!", count));
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
			log.addMaster(new MessageLogEntry(LogEntry.LEVEL_INFO, "Ended at {0}", new DateTime()));
			//TODO: inviare email report
			for(LogEntry entry : log) {
				logger.trace("{}", ((MessageLogEntry)entry).getMessage());
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void doInsertContact(Connection con, Contact contact, ContactPicture picture) throws WTException {
		ContactDAO cntdao = ContactDAO.getInstance();
		
		try {
			OContact item = new OContact(contact);
			if(StringUtils.isEmpty(contact.getPublicUid())) contact.setPublicUid(WT.generateUUID());
			item.setStatus(OContact.STATUS_NEW);
			item.setRevisionInfo(createRevisionInfo());
			item.setSearchfield(StringUtils.lowerCase(buildSearchfield(item)));
			item.setContactId(cntdao.getSequence(con).intValue());
			cntdao.insert(con, item);
			
			if(contact.getHasPicture()) {
				if(picture != null) {
					doInsertContactPicture(con, item.getContactId(), picture);
				}
			}
			
		} catch(WTException ex) {
			throw ex;
		}
	}
	
	private void doUpdateContact(Connection con, Contact contact, ContactPicture picture) throws WTException {
		ContactDAO cntdao = ContactDAO.getInstance();
		
		try {
			OContact item = new OContact(contact);
			item.setStatus(OContact.STATUS_MODIFIED);
			item.setRevisionInfo(createRevisionInfo());
			item.setSearchfield(StringUtils.lowerCase(buildSearchfield(item)));
			cntdao.update(con, item);
			
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
	
	private void doDeleteContact(Connection con, int contactId) throws WTException {
		ContactDAO cntdao = ContactDAO.getInstance();
		cntdao.logicDeleteById(con, contactId, createRevisionInfo());
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
			doInsertContact(con, createContact(list), null);
			
			for(ContactsListRecipient rcpt : list.getRecipients()) {
				OListRecipient item = new OListRecipient(rcpt);
				item.setListId(list.getListId());
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
			doUpdateContact(con, createContact(list), null);
			//TODO: gestire la modifica determinando gli eliminati e gli aggiunti?
			lrdao.deleteByList(con, list.getListId());
			for(ContactsListRecipient rcpt : list.getRecipients()) {
				OListRecipient item = new OListRecipient(rcpt);
				item.setListId(list.getListId());
				item.setListRecipientId(lrdao.getSequence(con).intValue());
				lrdao.insert(con, item);
			}
			
		} catch(WTException ex) {
			throw ex;
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
	
	/*
	private String ofGroup(String group) {
		return MessageFormat.format("_{0}", group);
	}
	
	private String lookupHeader(Locale locale, String key) {
		return StringUtils.defaultIfBlank(lookupResource(locale, key), StringUtils.removeStart(key, "fields.")).toUpperCase();
	}
	
	private DBDirectoryManager createDBDManager(UserProfile.Id pid, Locale locale, int categoryId) throws SQLException {
		String groupwork = lookupHeader(locale, ContactsLocale.FIELDS_GROUPWORK);
		String grouphome = lookupHeader(locale, ContactsLocale.FIELDS_GROUPHOME);
		String groupother = lookupHeader(locale, ContactsLocale.FIELDS_GROUPOTHER);
		String clickfields = lookupHeader(locale, ContactsLocale.FIELDS_FIRSTNAME) + ","
				+ lookupHeader(locale, ContactsLocale.FIELDS_LASTNAME) + ","
				+ lookupHeader(locale, ContactsLocale.FIELDS_COMPANY);
		String searchfields = "searchfield,cemail,company";
		String mailfield = lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ofGroup(groupwork);
		String faxfield = lookupHeader(locale, ContactsLocale.FIELDS_FAX) + ofGroup(groupwork);
		String firstnamefield = lookupHeader(locale, ContactsLocale.FIELDS_FIRSTNAME);
		String lastnamefield = lookupHeader(locale, ContactsLocale.FIELDS_LASTNAME);
		String companyfield = lookupHeader(locale, ContactsLocale.FIELDS_COMPANY);
		
		DBDirectoryManager dbdm = new DBDirectoryManager(
				DirectoryManager.DirectoryType.USER,
				"privatedb: " + pid.toString(), pid.toString(),
				WT.getDataSource(SERVICE_ID),
				"contacts.contacts",
				lookupResource(locale, ContactsLocale.FIELDS_GROUPMAIN) + "{"		
				+ "+TITLE " + lookupHeader(locale, ContactsLocale.FIELDS_TITLE) + ","
				+ "+FIRSTNAME " + lookupHeader(locale, ContactsLocale.FIELDS_FIRSTNAME) + ","
				+ "+LASTNAME " + lookupHeader(locale, ContactsLocale.FIELDS_LASTNAME) + ","
				+ "+NICKNAME " + lookupHeader(locale, ContactsLocale.FIELDS_NICKNAME) + ","
				+ "GENDER " + lookupHeader(locale, ContactsLocale.FIELDS_GENDER) + ","
				+ "+COMPANY " + lookupHeader(locale, ContactsLocale.FIELDS_COMPANY) + ","
				+ "FUNCTION " + lookupHeader(locale, ContactsLocale.FIELDS_FUNCTION) + ","
				+ "PHOTO " + lookupHeader(locale, ContactsLocale.FIELDS_PHOTO) + ","
				+ "URL " + lookupHeader(locale, ContactsLocale.FIELDS_URL) + ","
				+ "NOTES " + lookupHeader(locale, ContactsLocale.FIELDS_NOTES) + "}|"
				+ groupwork + "{"
				+ "CADDRESS " + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ofGroup(groupwork) + ","
				+ "+CCITY " + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ofGroup(groupwork) + ","
				+ "CSTATE " + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ofGroup(groupwork) + ","
				+ "CPOSTALCODE " + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ofGroup(groupwork) + ","
				+ "CCOUNTRY " + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ofGroup(groupwork) + ","
				+ "+CTELEPHONE " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE) + ofGroup(groupwork) + ","
				+ "CTELEPHONE2 " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE2) + ofGroup(groupwork) + ","
				+ "CFAX " + lookupHeader(locale, ContactsLocale.FIELDS_FAX) + ofGroup(groupwork) + ","
				+ "+CMOBILE " + lookupHeader(locale, ContactsLocale.FIELDS_MOBILE) + ofGroup(groupwork) + ","
				+ "CPAGER " + lookupHeader(locale, ContactsLocale.FIELDS_PAGER) + ofGroup(groupwork) + ","
				+ "+CEMAIL " + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ofGroup(groupwork) + ","	
				+ "CINSTANT_MSG " + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + ofGroup(groupwork) + ","
				+ "CDEPARTMENT " + lookupHeader(locale, ContactsLocale.FIELDS_DEPARTMENT) + ofGroup(groupwork) + ","
				+ "CMANAGER " + lookupHeader(locale, ContactsLocale.FIELDS_MANAGER) + ","
				+ "CASSISTANT " + lookupHeader(locale, ContactsLocale.FIELDS_ASSISTANT) + ","
				+ "CTELEPHONEASSISTANT " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONEASSISTANT) + "}|"
				+ grouphome + "{"
				+ "HADDRESS " + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ofGroup(grouphome) + ","
				+ "HCITY " + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ofGroup(grouphome) + ","
				+ "HSTATE " + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ofGroup(grouphome) + ","
				+ "HPOSTALCODE " + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ofGroup(grouphome) + ","
				+ "HCOUNTRY " + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ofGroup(grouphome) + ","
				+ "+HTELEPHONE " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE) + ofGroup(grouphome) + ","
				+ "HTELEPHONE2 " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE2) + ofGroup(grouphome) + ","
				+ "HFAX " + lookupHeader(locale, ContactsLocale.FIELDS_FAX) + ofGroup(grouphome) + ","
				+ "HMOBILE " + lookupHeader(locale, ContactsLocale.FIELDS_MOBILE) + ofGroup(grouphome) + ","
				+ "HPAGER " + lookupHeader(locale, ContactsLocale.FIELDS_PAGER) + ofGroup(grouphome) + ","
				+ "+HEMAIL " + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ofGroup(grouphome) + ","	
				+ "HINSTANT_MSG " + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + ofGroup(grouphome) + ","
				+ "HPARTNER " + lookupHeader(locale, ContactsLocale.FIELDS_PARTNER) + ","
				+ "+HBIRTHDAY " + lookupHeader(locale, ContactsLocale.FIELDS_BIRTHDAY) + ","
				+ "HANNIVERSARY " + lookupHeader(locale, ContactsLocale.FIELDS_ANNIVERSARY) + "}|"
				+ groupother + "{"
				+ "OADDRESS " + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ofGroup(groupother) + ","
				+ "OCITY " + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ofGroup(groupother) + ","
				+ "OSTATE " + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ofGroup(groupother) + ","
				+ "OPOSTALCODE " + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ofGroup(groupother) + ","
				+ "OCOUNTRY " + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ofGroup(groupother) + ","
				+ "OEMAIL " + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ofGroup(groupother) + ","
				+ "OINSTANT_MSG " + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + ofGroup(groupother) + "}|"
				+ "!" + lookupHeader(locale, ContactsLocale.FIELDS_GROUPSTATUS) + "{"
				//+ "+category_id category_id,"
				+ "STATUS STATUS,"
				+ "LIST_ID LIST_ID }",
				null,
				clickfields, null, searchfields, mailfield, faxfield, firstnamefield, lastnamefield, companyfield,
				"contact_id",
				"(status is null or status!='D') and (category_id=" + categoryId + ")",
				"lastname,firstname,company",
				"contact_id=nextval('contacts.seq_contacts'),category_id=" + categoryId
		);
		dbdm.setWritable(true);
		dbdm.setIsContact(true);
		return dbdm;
	}
	
	
	
	/*
	+ "+TITLE " + lookupHeader(locale, ContactsLocale.FIELDS_TITLE) + ","
				+ "+FIRSTNAME " + lookupHeader(locale, ContactsLocale.FIELDS_FIRSTNAME) + ","
				+ "+LASTNAME " + lookupHeader(locale, ContactsLocale.FIELDS_LASTNAME) + ","
				+ "+NICKNAME " + lookupHeader(locale, ContactsLocale.FIELDS_NICKNAME) + ","
				+ "GENDER " + lookupHeader(locale, ContactsLocale.FIELDS_GENDER) + ","
				+ "+COMPANY " + lookupHeader(locale, ContactsLocale.FIELDS_COMPANY) + ","
				+ "FUNCTION " + lookupHeader(locale, ContactsLocale.FIELDS_FUNCTION) + ","
				+ "PHOTO " + lookupHeader(locale, ContactsLocale.FIELDS_PHOTO) + ","
				+ "URL " + lookupHeader(locale, ContactsLocale.FIELDS_URL) + ","
				+ "NOTES " + lookupHeader(locale, ContactsLocale.FIELDS_NOTES) + "}|"
				+ groupwork + "{"
				+ "CADDRESS " + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ofGroup(groupwork) + ","
				+ "+CCITY " + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ofGroup(groupwork) + ","
				+ "CSTATE " + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ofGroup(groupwork) + ","
				+ "CPOSTALCODE " + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ofGroup(groupwork) + ","
				+ "CCOUNTRY " + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ofGroup(groupwork) + ","
				+ "+CTELEPHONE " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE) + ofGroup(groupwork) + ","
				+ "CTELEPHONE2 " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE2) + ofGroup(groupwork) + ","
				+ "CFAX " + lookupHeader(locale, ContactsLocale.FIELDS_FAX) + ofGroup(groupwork) + ","
				+ "+CMOBILE " + lookupHeader(locale, ContactsLocale.FIELDS_MOBILE) + ofGroup(groupwork) + ","
				+ "CPAGER " + lookupHeader(locale, ContactsLocale.FIELDS_PAGER) + ofGroup(groupwork) + ","
				+ "+CEMAIL " + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ofGroup(groupwork) + ","	
				+ "CINSTANT_MSG " + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + ofGroup(groupwork) + ","
				+ "CDEPARTMENT " + lookupHeader(locale, ContactsLocale.FIELDS_DEPARTMENT) + ofGroup(groupwork) + ","
				+ "CMANAGER " + lookupHeader(locale, ContactsLocale.FIELDS_MANAGER) + ","
				+ "CASSISTANT " + lookupHeader(locale, ContactsLocale.FIELDS_ASSISTANT) + ","
				+ "CTELEPHONEASSISTANT" + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONEASSISTANT) + "}|"
				+ grouphome + "{"
				+ "HADDRESS " + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ofGroup(grouphome) + ","
				+ "HCITY " + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ofGroup(grouphome) + ","
				+ "HSTATE " + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ofGroup(grouphome) + ","
				+ "HPOSTALCODE " + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ofGroup(grouphome) + ","
				+ "HCOUNTRY " + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ofGroup(grouphome) + ","
				+ "+HTELEPHONE " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE) + ofGroup(grouphome) + ","
				+ "HTELEPHONE2 " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE2) + ofGroup(grouphome) + ","
				+ "HFAX " + lookupHeader(locale, ContactsLocale.FIELDS_FAX) + ofGroup(grouphome) + ","
				+ "HMOBILE " + lookupHeader(locale, ContactsLocale.FIELDS_MOBILE) + ofGroup(grouphome) + ","
				+ "HPAGER " + lookupHeader(locale, ContactsLocale.FIELDS_PAGER) + ofGroup(grouphome) + ","
				+ "+HEMAIL " + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ofGroup(grouphome) + ","	
				+ "HINSTANT_MSG " + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + ofGroup(grouphome) + ","
				+ "HPARTNER " + lookupHeader(locale, ContactsLocale.FIELDS_PARTNER) + ","
				+ "+HBIRTHDAY " + lookupHeader(locale, ContactsLocale.FIELDS_BIRTHDAY) + ","
				+ "HANNIVERSARY " + lookupHeader(locale, ContactsLocale.FIELDS_ANNIVERSARY) + "}|"
				+ groupother + "{"
				+ "OADDRESS " + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ofGroup(groupother) + ","
				+ "OCITY " + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ofGroup(groupother) + ","
				+ "OSTATE " + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ofGroup(groupother) + ","
				+ "OPOSTALCODE " + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ofGroup(groupother) + ","
				+ "OCOUNTRY " + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ofGroup(groupother) + ","
				+ "OEMAIL " + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ofGroup(groupother) + ","
				+ "OINSTANT_MSG " + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + ofGroup(groupother) + "}|"
				+ "!" + lookupHeader(locale, ContactsLocale.FIELDS_GROUPSTATUS) + "{"
				+ "+CATEGORY_ID CATEGORY_ID,"
				+ "STATUS STATUS,"
				+ "LIST_ID LIST_ID }",
	*/
	
	/*
	private void initUserDirectories(UserProfile profile) throws SQLException {
		Locale locale = profile.getLocale();
		String domainId = profile.getDomainId();
		String clickfields = lookupResource(locale, ContactsLocale.FIELDS_FIRSTNAME) + ","
				+ lookupResource(locale, ContactsLocale.FIELDS_LASTNAME) + ","
				+ lookupResource(locale, ContactsLocale.FIELDS_COMPANY);
		String searchfields = "searchfield,cemail,company";
		String mailfield = lookupResource(locale, ContactsLocale.FIELDS_CEMAIL);
		String faxfield = lookupResource(locale, ContactsLocale.FIELDS_FAX);
		String firstnamefield = lookupResource(locale, ContactsLocale.FIELDS_FIRSTNAME);
		String lastnamefield = lookupResource(locale, ContactsLocale.FIELDS_LASTNAME);
		String companyfield = lookupResource(locale, ContactsLocale.FIELDS_COMPANY);
		
		DBDirectoryManager dbdm = new DBDirectoryManager(
				DirectoryManager.DirectoryType.USER,
				"privatedb: " + profile.getUserId(), profile.getDisplayName(),
				WT.getDataSource(SERVICE_ID),
				"contacts",
				lookupResource(locale, ContactsLocale.FIELDS_GROUPMAIN) + "{"
				+ "+TITLE " + lookupResource(locale, ContactsLocale.FIELDS_TITLE) + ","
				+ "+LASTNAME " + lookupResource(locale, ContactsLocale.FIELDS_LASTNAME) + ","
				+ "+FIRSTNAME " + lookupResource(locale, ContactsLocale.FIELDS_FIRSTNAME) + ","
				+ "+COMPANY " + lookupResource(locale, ContactsLocale.FIELDS_COMPANY) + "}|"
				+ lookupResource(locale, ContactsLocale.FIELDS_GROUPCOMPANY) + "{"
				+ "FUNCTION " + lookupResource(locale, ContactsLocale.FIELDS_FUNCTION) + ","
				+ "PHOTO " + lookupResource(locale, ContactsLocale.FIELDS_PHOTO) + ","
				+ "+CEMAIL " + lookupResource(locale, ContactsLocale.FIELDS_CEMAIL) + ","
				+ "CDEPARTMENT " + lookupResource(locale, ContactsLocale.FIELDS_DEPARTMENT) + ","
				+ "CADDRESS " + lookupResource(locale, ContactsLocale.FIELDS_ADDRESS) + ","
				+ "+CCITY " + lookupResource(locale, ContactsLocale.FIELDS_CITY) + ","
				+ "CSTATE " + lookupResource(locale, ContactsLocale.FIELDS_STATE) + ","
				+ "CPOSTALCODE " + lookupResource(locale, ContactsLocale.FIELDS_POSTALCODE) + ","
				+ "CCOUNTRY " + lookupResource(locale, ContactsLocale.FIELDS_COUNTRY) + ","
				+ "+CTELEPHONE " + lookupResource(locale, ContactsLocale.FIELDS_CTELEPHONE) + ","
				+ "+CMOBILE " + lookupResource(locale, ContactsLocale.FIELDS_CMOBILE) + ","
				+ "CTELEXTENSION " + lookupResource(locale, ContactsLocale.FIELDS_EXTENSION) + ","
				+ "CFAX " + lookupResource(locale, ContactsLocale.FIELDS_FAX) + ","
				+ "CFAXEXTENSION X_" + lookupResource(locale, ContactsLocale.FIELDS_EXTENSION) + ","
				+ "CTELEPHONE2 " + lookupResource(locale, ContactsLocale.FIELDS_TELEPHONE2) + ","
				+ "CTEL2EXTENSION X__" + lookupResource(locale, ContactsLocale.FIELDS_EXTENSION) + ","
				+ "CPAGER " + lookupResource(locale, ContactsLocale.FIELDS_PAGER) + ","
				+ "CPAGEREXTENSION X___" + lookupResource(locale, ContactsLocale.FIELDS_EXTENSION) + ","
				+ "CASSISTANT " + lookupResource(locale, ContactsLocale.FIELDS_ASSISTANT) + ","
				+ "CTELEPHONEASSISTANT X__" + lookupResource(locale, ContactsLocale.FIELDS_TELEPHONEASSISTANT) + ","
				+ "URL " + lookupResource(locale, ContactsLocale.FIELDS_URL) + ","
				+ "CMANAGER " + lookupResource(locale, ContactsLocale.FIELDS_MANAGER) + ","
				+ "HINSTANT_MSG " + lookupResource(locale, ContactsLocale.FIELDS_HINSTANT_MSG) + ","
				+ "CINSTANT_MSG X_" + lookupResource(locale, ContactsLocale.FIELDS_CINSTANT_MSG) + ","
				+ "OINSTANT_MSG X__" + lookupResource(locale, ContactsLocale.FIELDS_OINSTANT_MSG) + ","
				+ "OEMAIL X__" + lookupResource(locale, ContactsLocale.FIELDS_OEMAIL) + ","
				+ "OADDRESS X__" + lookupResource(locale, ContactsLocale.FIELDS_OADDRESS) + ","
				+ "OCITY X__" + lookupResource(locale, ContactsLocale.FIELDS_OCITY) + ","
				+ "OSTATE X__" + lookupResource(locale, ContactsLocale.FIELDS_OSTATE) + ","
				+ "OPOSTALCODE X__" + lookupResource(locale, ContactsLocale.FIELDS_OPOSTALCODE) + ","
				+ "OCOUNTRY X__" + lookupResource(locale, ContactsLocale.FIELDS_OCOUNTRY) + ","
				+ "CNOTES " + lookupResource(locale, ContactsLocale.FIELDS_NOTES) + "}|"
				+ lookupResource(locale, ContactsLocale.FIELDS_GROUPHOME) + "{"
				+ "HADDRESS X_" + lookupResource(locale, ContactsLocale.FIELDS_ADDRESS) + ","
				+ "HCITY X_" + lookupResource(locale, ContactsLocale.FIELDS_CITY) + ","
				+ "HSTATE X_" + lookupResource(locale, ContactsLocale.FIELDS_STATE) + ","
				+ "HPOSTALCODE X_" + lookupResource(locale, ContactsLocale.FIELDS_POSTALCODE) + ","
				+ "HCOUNTRY X_" + lookupResource(locale, ContactsLocale.FIELDS_COUNTRY) + ","
				+ "+HEMAIL " + lookupResource(locale, ContactsLocale.FIELDS_HEMAIL) + ","
				+ "+HTELEPHONE " + lookupResource(locale, ContactsLocale.FIELDS_HTELEPHONE) + ","
				+ "HTELEPHONE2 X_" + lookupResource(locale, ContactsLocale.FIELDS_TELEPHONE2) + ","
				+ "HFAX X_" + lookupResource(locale, ContactsLocale.FIELDS_FAX) + ","
				+ "HMOBILE X__" + lookupResource(locale, ContactsLocale.FIELDS_HMOBILE) + ","
				+ "HPAGER X_" + lookupResource(locale, ContactsLocale.FIELDS_PAGER) + ","
				+ "HPARTNER " + lookupResource(locale, ContactsLocale.FIELDS_PARTNER) + ","
				+ "+HBIRTHDAY " + lookupResource(locale, ContactsLocale.FIELDS_BIRTHDAY) + ","
				+ "HANNIVERSARY " + lookupResource(locale, ContactsLocale.FIELDS_ANNIVERSARY) + ","
				+ "+CATEGORY " + lookupResource(locale, ContactsLocale.FIELDS_CATEGORY) + ","
				+ "HNOTES X_" + lookupResource(locale, ContactsLocale.FIELDS_NOTES) + "}|"
				+ "!" + lookupResource(locale, ContactsLocale.FIELDS_GROUPSTATUS) + "{"
				+ "STATUS " + lookupResource(locale, ContactsLocale.FIELDS_STATUS) + ","
				+ "IDLIST IDLIST }",
				null,
				clickfields, null, searchfields, mailfield, faxfield, firstnamefield, lastnamefield, companyfield,
				"contact_id",
				"(status is null or status!='D')",
				"lastname,firstname,company",
				"contact_id=nextval('seq_contacts'),user_id='" + profile.getUserId() + "',domain_id='" + profile.getDomainId() + "'"
		);
		dbdm.setWritable(true);
		dbdm.setIsContact(true);
		directories.put(dbdm.getId(), dbdm);
		
		
		CoreManager core = WT.getCoreManager(getRunContext());
		List<IncomingShare> shares = core.listIncomingSharesForUser(SERVICE_ID, profile.getId(), SHARE_RESOURCE_CONTACTS);
		String resource = AuthResourceShareInstance.buildName("GROUP");
		for(IncomingShare share : shares) {
			if(!WT.isPermitted(SERVICE_ID, resource, AuthResourceShareInstance.ACTION_READ, share.getShareId())) continue;
			dbdm = new DBDirectoryManager(
					DirectoryManager.DirectoryType.GROUP,
					"privatedb: " + share.getUserId(), wg.getDescription(),
					WT.getDataSource(SERVICE_ID),
					"contacts",
					lookupResource(locale, ContactsLocale.FIELDS_GROUPMAIN) + "{"
					+ "+TITLE " + lookupResource(locale, ContactsLocale.FIELDS_TITLE) + ","
					+ "+LASTNAME " + lookupResource(locale, ContactsLocale.FIELDS_LASTNAME) + ","
					+ "+FIRSTNAME " + lookupResource(locale, ContactsLocale.FIELDS_FIRSTNAME) + ","
					+ "+COMPANY " + lookupResource(locale, ContactsLocale.FIELDS_COMPANY) + "}|"
					+ lookupResource(locale, ContactsLocale.FIELDS_GROUPCOMPANY) + "{"
					+ "FUNCTION " + lookupResource(locale, ContactsLocale.FIELDS_FUNCTION) + ","
					+ "PHOTO " + lookupResource(locale, ContactsLocale.FIELDS_PHOTO) + ","
					+ "+CEMAIL " + lookupResource(locale, ContactsLocale.FIELDS_CEMAIL) + ","
					+ "CDEPARTMENT " + lookupResource(locale, ContactsLocale.FIELDS_DEPARTMENT) + ","
					+ "CADDRESS " + lookupResource(locale, ContactsLocale.FIELDS_ADDRESS) + ","
					+ "+CCITY " + lookupResource(locale, ContactsLocale.FIELDS_CITY) + ","
					+ "CSTATE " + lookupResource(locale, ContactsLocale.FIELDS_STATE) + ","
					+ "CPOSTALCODE " + lookupResource(locale, ContactsLocale.FIELDS_POSTALCODE) + ","
					+ "CCOUNTRY " + lookupResource(locale, ContactsLocale.FIELDS_COUNTRY) + ","
					+ "+CTELEPHONE " + lookupResource(locale, ContactsLocale.FIELDS_CTELEPHONE) + ","
					+ "+CMOBILE " + lookupResource(locale, ContactsLocale.FIELDS_CMOBILE) + ","
					+ "CTELEXTENSION " + lookupResource(locale, ContactsLocale.FIELDS_EXTENSION) + ","
					+ "CFAX " + lookupResource(locale, ContactsLocale.FIELDS_FAX) + ","
					+ "CFAXEXTENSION X_" + lookupResource(locale, ContactsLocale.FIELDS_EXTENSION) + ","
					+ "CTELEPHONE2 " + lookupResource(locale, ContactsLocale.FIELDS_TELEPHONE2) + ","
					+ "CTEL2EXTENSION X__" + lookupResource(locale, ContactsLocale.FIELDS_EXTENSION) + ","
					+ "CPAGER " + lookupResource(locale, ContactsLocale.FIELDS_PAGER) + ","
					+ "CPAGEREXTENSION X___" + lookupResource(locale, ContactsLocale.FIELDS_EXTENSION) + ","
					+ "CASSISTANT " + lookupResource(locale, ContactsLocale.FIELDS_ASSISTANT) + ","
					+ "CTELEPHONEASSISTANT X__" + lookupResource(locale, ContactsLocale.FIELDS_TELEPHONEASSISTANT) + ","
					+ "URL " + lookupResource(locale, ContactsLocale.FIELDS_URL) + ","
					+ "CMANAGER " + lookupResource(locale, ContactsLocale.FIELDS_MANAGER) + ","
					+ "HINSTANT_MSG " + lookupResource(locale, ContactsLocale.FIELDS_HINSTANT_MSG) + ","
					+ "CINSTANT_MSG X_" + lookupResource(locale, ContactsLocale.FIELDS_CINSTANT_MSG) + ","
					+ "OINSTANT_MSG X__" + lookupResource(locale, ContactsLocale.FIELDS_OINSTANT_MSG) + ","
					+ "OEMAIL X__" + lookupResource(locale, ContactsLocale.FIELDS_OEMAIL) + ","
					+ "OADDRESS X__" + lookupResource(locale, ContactsLocale.FIELDS_OADDRESS) + ","
					+ "OCITY X__" + lookupResource(locale, ContactsLocale.FIELDS_OCITY) + ","
					+ "OSTATE X__" + lookupResource(locale, ContactsLocale.FIELDS_OSTATE) + ","
					+ "OPOSTALCODE X__" + lookupResource(locale, ContactsLocale.FIELDS_OPOSTALCODE) + ","
					+ "OCOUNTRY X__" + lookupResource(locale, ContactsLocale.FIELDS_OCOUNTRY) + ","
					+ "CNOTES " + lookupResource(locale, ContactsLocale.FIELDS_NOTES) + "}|"
					+ lookupResource(locale, ContactsLocale.FIELDS_GROUPHOME) + "{"
					+ "HADDRESS X_" + lookupResource(locale, ContactsLocale.FIELDS_ADDRESS) + ","
					+ "HCITY X_" + lookupResource(locale, ContactsLocale.FIELDS_CITY) + ","
					+ "HSTATE X_" + lookupResource(locale, ContactsLocale.FIELDS_STATE) + ","
					+ "HPOSTALCODE X_" + lookupResource(locale, ContactsLocale.FIELDS_POSTALCODE) + ","
					+ "HCOUNTRY X_" + lookupResource(locale, ContactsLocale.FIELDS_COUNTRY) + ","
					+ "+HEMAIL " + lookupResource(locale, ContactsLocale.FIELDS_HEMAIL) + ","
					+ "+HTELEPHONE " + lookupResource(locale, ContactsLocale.FIELDS_HTELEPHONE) + ","
					+ "HTELEPHONE2 X_" + lookupResource(locale, ContactsLocale.FIELDS_TELEPHONE2) + ","
					+ "HFAX X_" + lookupResource(locale, ContactsLocale.FIELDS_FAX) + ","
					+ "HMOBILE X__" + lookupResource(locale, ContactsLocale.FIELDS_HMOBILE) + ","
					+ "HPAGER X_" + lookupResource(locale, ContactsLocale.FIELDS_PAGER) + ","
					+ "HPARTNER " + lookupResource(locale, ContactsLocale.FIELDS_PARTNER) + ","
					+ "+HBIRTHDAY " + lookupResource(locale, ContactsLocale.FIELDS_BIRTHDAY) + ","
					+ "+CATEGORY " + lookupResource(locale, ContactsLocale.FIELDS_CATEGORY) + ","
					+ "HANNIVERSARY " + lookupResource(locale, ContactsLocale.FIELDS_ANNIVERSARY) + ","
					+ "HNOTES X_" + lookupResource(locale, ContactsLocale.FIELDS_NOTES) + "}|"
					+ "!" + lookupResource(locale, ContactsLocale.FIELDS_GROUPSTATUS) + "{"
					+ "STATUS " + lookupResource(locale, ContactsLocale.FIELDS_STATUS) + ","
					+ "IDLIST IDLIST }",
					null,
					clickfields, null, searchfields, mailfield, faxfield, firstnamefield, lastnamefield, companyfield,
					"contact_id",
					"' and (status is null or status!='D')",
					"lastname,firstname,company",
					"contact_id=nextval('SEQ_CONTACTS'),user_id='" + share.getUserId() + "',domain_id='" + domainId + "'",
					share.getUserId(),
					domainId
			);
			dbdm.setWritable(wg.isWriteable("contacts"));
			dbdm.setIsContact(true);
			directories.put(dbdm.getId(), dbdm);
		}
	}
	
	private synchronized void initGlobalDirectories(UserProfile.Id pid) throws SQLException {
		ContactsServiceSettings css = new ContactsServiceSettings(SERVICE_ID);
		
		int index = 0;
		while (true) {
			String value = css.getDirectory(index);
			if (value == null) break;
			
			if (value.startsWith("db:")) {
				StringTokenizer st = new StringTokenizer(value, ";");
				String sid = st.nextToken().trim();
				String sdescription = st.nextToken().trim();
				String sname = st.nextToken().trim();
				boolean iswebtop = sname.equalsIgnoreCase("webtop");
				String stable = st.nextToken().trim();
				String sfields = st.nextToken().trim();
				String sclickfields = st.nextToken().trim();
				String ssearchfields = st.nextToken().trim();
				String smailfield = st.nextToken().trim();
				String sfirstnamefield = st.nextToken().trim();
				String slastnamefield = st.nextToken().trim();
				String sorderfields = st.nextToken().trim();
				
				DBDirectoryManager dm = null;
				if (!iswebtop) {
					DataSource ds = null;
					try {
						ds = WT.getDataSource(SERVICE_ID, sname);
					} catch (Exception ex) {
						logger.error("Unable to get DataSouce [{}, {}]", ex, SERVICE_ID, sname);
						++index;
						continue;
					}
					
					dm = new DBDirectoryManager(
						DirectoryManager.DirectoryType.GLOBAL,
						sid, sdescription,
						ds,
						stable, sfields, null, sclickfields, null, ssearchfields,
						smailfield, null, sfirstnamefield, slastnamefield, null, null, null,
						sorderfields, null
					);
				} else {
					dm = new DBDirectoryManager(
							DirectoryManager.DirectoryType.GLOBAL,
							sid, sdescription,
							WT.getDataSource(SERVICE_ID),
							stable, sfields, null, sclickfields, null, ssearchfields,
							smailfield, null, sfirstnamefield, slastnamefield, null, null, null,
							sorderfields, null
					);
				}
				cacheGlobalDirectories.put(sid, dm);
				
			} else if (value.startsWith("ldap:")) {
				StringTokenizer st = new StringTokenizer(value, ";");
				String sid = st.nextToken().trim();
				String sdescription = st.nextToken().trim();
				String sserver = st.nextToken().trim();
				String sport = st.nextToken().trim();
				String sbase = "";
				if (st.hasMoreTokens()) {
					sbase = st.nextToken().trim();
				}
				LDAPDirectoryManager dm = new LDAPDirectoryManager(
						sid, sdescription,
						sserver, Integer.parseInt(sport), sbase
				);
				cacheGlobalDirectories.put(sid, dm);
			}
			
			++index;
		}
	}
	*/
	
	private ContactsList createContactsList(OContact cnt, List<OListRecipient> rcpts) {
		ContactsList item = new ContactsList(cnt.getContactId(), cnt.getCategoryId());
		item.setListId(cnt.getListId());
		item.setName(cnt.getLastname());
		for(OListRecipient rcpt : rcpts) {
			item.addRecipient(new ContactsListRecipient(rcpt));
		}
		return item;
	}
	
	private Contact createContact(ContactsList cl) {
		Contact item = new Contact(cl.getContactId(), cl.getCategoryId());
		item.setCategoryId(cl.getCategoryId());
		item.setListId(cl.getListId());
		item.setLastName(cl.getName());
		item.setHasPicture(false);
		return item;
	}
	
	private Contact createContact(OContact cnt, boolean hasPicture) {
		Contact item = new Contact(cnt.getContactId(), cnt.getCategoryId());
		item.setListId(cnt.getListId());
		item.setStatus(cnt.getStatus());
		item.setTitle(cnt.getTitle());
		item.setFirstName(cnt.getFirstname());
		item.setLastName(cnt.getLastname());
		item.setNickname(cnt.getNickname());
		item.setGender(cnt.getGender());
		item.setWorkAddress(cnt.getCaddress());
		item.setWorkPostalCode(cnt.getCpostalcode());
		item.setWorkCity(cnt.getCcity());
		item.setWorkState(cnt.getCstate());
		item.setWorkCountry(cnt.getCcountry());
		item.setWorkTelephone(cnt.getCtelephone());
		item.setWorkTelephone2(cnt.getCtelephone2());
		item.setWorkMobile(cnt.getCmobile());
		item.setWorkFax(cnt.getCfax());
		item.setWorkPager(cnt.getCpager());
		item.setWorkEmail(cnt.getCemail());
		item.setWorkInstantMsg(cnt.getCinstantMsg());
		item.setHomeAddress(cnt.getHaddress());
		item.setHomePostalCode(cnt.getHpostalcode());
		item.setHomeCity(cnt.getHcity());
		item.setHomeState(cnt.getHstate());
		item.setHomeCountry(cnt.getHcountry());
		item.setHomeTelephone(cnt.getHtelephone());
		//item.setHomeTelephone2(cont.getHtelephone2());
		item.setHomeMobile(cnt.getHmobile());
		item.setHomeFax(cnt.getHfax());
		item.setHomePager(cnt.getHpager());
		item.setHomeEmail(cnt.getHemail());
		item.setHomeInstantMsg(cnt.getHinstantMsg());
		item.setOtherAddress(cnt.getOaddress());
		item.setOtherPostalCode(cnt.getOpostalcode());
		item.setOtherCity(cnt.getOcity());
		item.setOtherState(cnt.getOstate());
		item.setOtherCountry(cnt.getOcountry());
		item.setOtherEmail(cnt.getOemail());
		item.setOtherInstantMsg(cnt.getOinstantMsg());
		item.setCompany(cnt.getCompany());
		item.setFunction(cnt.getFunction());
		item.setDepartment(cnt.getCdepartment());
		item.setManager(cnt.getCmanager());
		item.setAssistant(cnt.getCassistant());
		item.setAssistantTelephone(cnt.getCtelephoneassistant());
		item.setPartner(cnt.getHpartner());
		item.setBirthday(cnt.getHbirthday());
		item.setAnniversary(cnt.getHbirthday());
		item.setUrl(cnt.getUrl());
		item.setNotes(cnt.getNotes());
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
		
		// Checks rights on calendar instance
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
	
	private ReminderAlertWeb createAnniversaryReminderAlertWeb(Locale locale, boolean birthday, VContact contact, DateTime date) {
		String type = (birthday) ? "birthday" : "anniversary";
		String resKey = (birthday) ? ContactsLocale.FIELDS_BIRTHDAY : ContactsLocale.FIELDS_ANNIVERSARY;
		String fullName = StringUtils.defaultString(contact.getFirstname()) + " " + StringUtils.defaultString(contact.getLastname());
		String title = MessageFormat.format("{0}: {1}", lookupResource(locale, resKey), StringUtils.trim(fullName));
		
		ReminderAlertWeb alert = new ReminderAlertWeb(SERVICE_ID, contact.getCategoryProfileId(), type, contact.getContactId().toString());
		alert.setTitle(title);
		alert.setDate(date);
		alert.setTimezone(date.getZone().getID());
		return alert;
	}
	
	private ReminderAlertEmail createAnniversaryReminderAlertEmail(Locale locale, boolean birthday, VContact contact, DateTime date) {
		String type = (birthday) ? "birthday" : "anniversary";
		String resKey = (birthday) ? ContactsLocale.FIELDS_BIRTHDAY : ContactsLocale.FIELDS_ANNIVERSARY;
		
		ReminderAlertEmail alert = new ReminderAlertEmail(SERVICE_ID, contact.getCategoryProfileId(), type, contact.getContactId().toString());
		//TODO: completare email
		return alert;
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
	
	
	
	
	/*
	public Contact getContact999(String uid) throws WTException {
		ContactPictureDAO dao = ContactPictureDAO.getInstance();
		Connection con = null;
		Contact item = null;
		
		try {
			CompositeId cuid = new CompositeId().parse(uid, 2);
			int categoryId = Integer.parseInt(cuid.getToken(0));
			String contactId = cuid.getToken(1);
			
			DirectoryManager dm = getDirectoryManager(categoryId);
			DirectoryResult dr = dm.lookup(Arrays.asList("CONTACT_ID"), Arrays.asList(contactId), getLocale(), true, true, false);
			DirectoryElement de = dr.elementAt(0);
			if(de == null) throw new WTException("Contact not found [{0}, {1}]", categoryId, contactId);
			
			con = WT.getConnection(SERVICE_ID);
			boolean hasPicture = dao.hasPicture(con, Integer.valueOf(contactId));
			
			item = new Contact(uid);
			item.setContactId(contactId);
			item.setCategoryId(String.valueOf(categoryId));
			item.setListId(StringUtils.defaultIfEmpty(de.getField("LIST_ID"), null));
			item.setStatus(de.getField("STATUS"));
			item.setTitle(de.getField(dm.getAliasField("TITLE")));
			item.setFirstName(de.getField(dm.getAliasField("FIRSTNAME")));
			item.setLastName(de.getField(dm.getAliasField("LASTNAME")));
			item.setNickname(de.getField(dm.getAliasField("NICKNAME")));
			item.setGender(de.getField(dm.getAliasField("GENDER")));
			item.setWorkAddress(de.getField(dm.getAliasField("CADDRESS")));
			item.setWorkPostalCode(de.getField(dm.getAliasField("CPOSTALCODE")));
			item.setWorkCity(de.getField(dm.getAliasField("CCITY")));
			item.setWorkState(de.getField(dm.getAliasField("CSTATE")));
			item.setWorkCountry(de.getField(dm.getAliasField("CCOUNTRY")));
			item.setWorkTelephone(de.getField(dm.getAliasField("CTELEPHONE")));
			item.setWorkTelephone2(de.getField(dm.getAliasField("CTELEPHONE2")));
			item.setWorkMobile(de.getField(dm.getAliasField("CMOBILE")));
			item.setWorkFax(de.getField(dm.getAliasField("CFAX")));
			item.setWorkPager(de.getField(dm.getAliasField("CPAGER")));
			item.setWorkEmail(de.getField(dm.getAliasField("CEMAIL")));
			item.setWorkInstantMsg(de.getField(dm.getAliasField("CINSTANT_MSG")));
			item.setHomeAddress(de.getField(dm.getAliasField("HADDRESS")));
			item.setHomePostalCode(de.getField(dm.getAliasField("HPOSTALCODE")));
			item.setHomeCity(de.getField(dm.getAliasField("HCITY")));
			item.setHomeState(de.getField(dm.getAliasField("HSTATE")));
			item.setHomeCountry(de.getField(dm.getAliasField("HCOUNTRY")));
			item.setHomeTelephone(de.getField(dm.getAliasField("HTELEPHONE")));
			item.setHomeMobile(de.getField(dm.getAliasField("HMOBILE")));
			item.setHomeFax(de.getField(dm.getAliasField("HFAX")));
			item.setHomePager(de.getField(dm.getAliasField("HPAGER")));
			item.setHomeEmail(de.getField(dm.getAliasField("HEMAIL")));
			item.setHomeInstantMsg(de.getField(dm.getAliasField("HINSTANT_MSG")));
			item.setOtherAddress(de.getField(dm.getAliasField("OADDRESS")));
			item.setOtherPostalCode(de.getField(dm.getAliasField("OPOSTALCODE")));
			item.setOtherCity(de.getField(dm.getAliasField("OCITY")));
			item.setOtherState(de.getField(dm.getAliasField("OSTATE")));
			item.setOtherCountry(de.getField(dm.getAliasField("OCOUNTRY")));
			item.setOtherEmail(de.getField(dm.getAliasField("OEMAIL")));
			item.setOtherInstantMsg(de.getField(dm.getAliasField("OINSTANT_MSG")));
			item.setCompany(de.getField(dm.getAliasField("COMPANY")));
			item.setFunction(de.getField(dm.getAliasField("FUNCTION")));
			item.setDepartment(de.getField(dm.getAliasField("CDEPARTMENT")));
			item.setManager(de.getField(dm.getAliasField("CMANAGER")));
			item.setAssistant(de.getField(dm.getAliasField("CASSISTANT")));
			item.setAssistantTelephone(de.getField(dm.getAliasField("CTELEPHONEASSISTANT")));
			item.setPartner(de.getField(dm.getAliasField("HPARTNER")));
			item.setBirthday(de.getField(dm.getAliasField("HBIRTHDAY")));
			item.setAnniversary(de.getField(dm.getAliasField("HANNIVERSARY")));
			item.setUrl(de.getField(dm.getAliasField("URL")));
			item.setNotes(de.getField(dm.getAliasField("NOTES")));
			item.setHasPicture(hasPicture);
			return item;
		
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	*/
	
	/* con DirectoryManager
	public ContactsList getContactsList999(String uid) throws WTException {
		ListRecipientDAO dao = ListRecipientDAO.getInstance();
		Connection con = null;
		ContactsList item = null;
		
		try {
			CompositeId cuid = new CompositeId().parse(uid, 2);
			int categoryId = Integer.parseInt(cuid.getToken(0));
			String contactId = cuid.getToken(1);
			
			checkRightsOnCategoryFolder(categoryId, "READ"); // Rights check!
			
			DirectoryManager dm = getDirectoryManager(categoryId);
			DirectoryResult dr = dm.lookup(Arrays.asList("CONTACT_ID"), Arrays.asList(contactId), getLocale(), true, true, false);
			DirectoryElement de = dr.elementAt(0);
			if(de == null) throw new WTException("Contact not found [{0}, {1}]", categoryId, contactId);
			
			int listId = Integer.valueOf(de.getField("LIST_ID"));
			con = WT.getConnection(SERVICE_ID);
			List<OListRecipient> recipients = dao.selectByList(con, listId);
			
			item = new ContactsList(uid);
			item.setContactId(contactId);
			item.setCategoryId(categoryId);
			item.setListId(LangUtils.value(de.getField("LIST_ID"), (Integer)null));
			item.setName(de.getField(dm.getAliasField("LASTNAME")));
			for(OListRecipient recipient : recipients) {
				item.addRecipient(new ContactsListRecipient(recipient));
			}
			
			return item;
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	*/
	
	/*
	private void privateAddContactsList999(int categoryId, ContactsList contactsList) throws WTException {
		ListRecipientDAO lrdao = ListRecipientDAO.getInstance();
		ListDAO ldao = ListDAO.getInstance();
		Connection con = null;
		
		try {
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			int listId = ldao.getSequence(con).intValue();
			
			DirectoryManager dm = getDirectoryManager(categoryId);
			DirectoryElement de = dm.createEmptyElement(getLocale());
			//de.setField("CATEGORY_ID", String.valueOf(categoryId));
			de.setField("LIST_ID", String.valueOf(listId));
			de.setField(dm.getLastNameField(), contactsList.getName());
			//de.setField(dm.getMailField(), cl.getName());
			de.setField("STATUS", "N");
			dm.insert(de);
			
			for(ContactsListRecipient recipient : contactsList.getRecipients()) {
				OListRecipient lr = new OListRecipient(recipient);
				lr.setListId(listId);
				lr.setListRecipientId(lrdao.getSequence(con).intValue());
				lrdao.insert(con, lr);
			}
			
			DbUtils.commitQuietly(con);
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	*/
	
	/*
	private void privateUpdateContactsList999(int categoryId, ContactsList contactsList) throws WTException {
		ListRecipientDAO lrdao = ListRecipientDAO.getInstance();
		Connection con = null;
		
		try {
			DirectoryManager dm = getDirectoryManager(categoryId);
			DirectoryResult dr = dm.lookup(Arrays.asList("CONTACT_ID"), Arrays.asList(contactsList.getContactId()), getLocale(), true, true, false);
			DirectoryElement de = dr.elementAt(0);
			if(de == null) throw new WTException("Contact not found [{0}, {1}]", categoryId, contactsList.getContactId());
			
			de.updateField("CATEGORY_ID", String.valueOf(contactsList.getCategoryId()));
			de.updateField(dm.getLastNameField(), contactsList.getName());
			de.updateField("STATUS", "M");
			dm.update(de);
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			
			lrdao.deleteByList(con, contactsList.getListId());
			for(ContactsListRecipient recipient : contactsList.getRecipients()) {
				OListRecipient lr = new OListRecipient(recipient);
				lr.setListId(contactsList.getListId());
				lr.setListRecipientId(lrdao.getSequence(con).intValue());
				lrdao.insert(con, lr);
			}
			
			DbUtils.commitQuietly(con);
			
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(WTException ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	*/
	
	/*
	public void copyContactsList999(String sourceContactListUid, int destCategoryId) throws WTException {
		try {
			ContactsList scl = getContactsList(sourceContactListUid);
			
			//TODO: controllo autorizzatione scrittura
			
			privateAddContactsList999(destCategoryId, scl);
		} catch(WTException ex) {
			throw ex;
		}
	}
	
	public void deleteContactList999(String contactListUid) throws WTException {
		
		try {
			CompositeId cuid = new CompositeId().parse(contactListUid, 2);
			int categoryId = Integer.valueOf(cuid.getToken(0));
			String contactId = cuid.getToken(1);
			
			//TODO: controllo autorizzatione
			
			DirectoryManager dm = getDirectoryManager(categoryId);
			DirectoryResult dr = dm.lookup(Arrays.asList("CONTACT_ID"), Arrays.asList(contactId), getLocale(), true, true, false);
			DirectoryElement de = dr.elementAt(0);
			if(de == null) throw new WTException("Contact not found [{0}, {1}]", categoryId, contactId);
			
			de.updateField("STATUS", "D");
			dm.update(de);
			
		} catch(Exception ex) {
			throw new WTException(ex);
		}
	}
	*/
	
	/*
	public byte[] getContactPhoto(String uid) throws WTException {
		ContactDAO dao = ContactDAO.getInstance();
		Connection con = null;
		
		try {
			CompositeId cid = new CompositeId().parse(uid, 2);
			int categoryId = Integer.parseInt(cid.getToken(0));
			String contactId = cid.getToken(1);
			
			//DirectoryManager dm = getDirectoryManager(categoryId);
			//DirectoryResult dr = dm.lookup(Arrays.asList("CONTACT_ID"), Arrays.asList(contactId), getRunContext().getLocale(), true, true, false);
			//DirectoryElement de = dr.elementAt(0);
			//if(de == null) throw new WTException("Contact not found [{0}, {1}]", categoryId, contactId);
			
			con = WT.getConnection(SERVICE_ID);
			con.setAutoCommit(false);
			byte[] bytes = dao.readPhoto(con, Integer.valueOf(contactId));
			DbUtils.commitQuietly(con);
			return bytes;
		
		} catch(SQLException | DAOException ex) {
			DbUtils.rollbackQuietly(con);
			throw new WTException(ex, "DB error");
		} catch(IOException ex) {
			throw new WTException(ex, "Error reading bytes");
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	*/
}
