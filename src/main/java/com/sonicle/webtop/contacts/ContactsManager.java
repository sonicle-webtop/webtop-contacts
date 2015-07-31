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
import com.sonicle.webtop.contacts.bol.OFolder;
import com.sonicle.webtop.contacts.dal.FolderDAO;
import com.sonicle.webtop.contacts.directory.DBDirectoryManager;
import com.sonicle.webtop.contacts.directory.DirectoryManager;
import com.sonicle.webtop.contacts.directory.DirectoryResult;
import com.sonicle.webtop.contacts.directory.LDAPDirectoryManager;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.WT;
import com.sonicle.webtop.core.bol.OShare;
import com.sonicle.webtop.core.bol.OUser;
import com.sonicle.webtop.core.bol.model.FolderBase;
import com.sonicle.webtop.core.bol.model.IncomingFolder;
import com.sonicle.webtop.core.bol.model.MyFolder;
import com.sonicle.webtop.core.dal.BaseDAO.RevisionInfo;
import com.sonicle.webtop.core.dal.ShareDAO;
import com.sonicle.webtop.core.dal.UserDAO;
import com.sonicle.webtop.core.sdk.BaseServiceManager;
import com.sonicle.webtop.core.RunContext;
import com.sonicle.webtop.core.bol.IncomingShare;
import com.sonicle.webtop.core.bol.model.AuthResourceShareInstance;
import com.sonicle.webtop.core.sdk.UserProfile;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 *
 * @author malbinola
 */
public class ContactsManager extends BaseServiceManager {
	public static final Logger logger = WT.getLogger(ContactsManager.class);
	private static final String SHARE_RESOURCE_CONTACTS = "CONTACTS";
	
	private final LinkedHashMap<String, DirectoryManager> globalDirectories;
	private final LinkedHashMap<String, DirectoryManager> directories;

	public ContactsManager(String serviceId, RunContext context) {
		super(serviceId, context);
		globalDirectories = new LinkedHashMap<>();
		directories = new LinkedHashMap<>();
	}
	
	public void initializeDirectories(UserProfile profile) throws SQLException {
		initGlobalDirectories(profile.getId());
		// Adds global DMs to the sessions
		for (DirectoryManager dm : globalDirectories.values()) {
			directories.put(dm.getId(), dm);
		}
	}
	
	public void cleanupDirectories() {
		globalDirectories.clear();
		directories.clear();
	};
	
	
	
	
	
	
	
	
	
	
	private RevisionInfo createRevisionInfo() {
		return new RevisionInfo("WT", getRunContext().getProfileId().toString());
	}
	
	public LinkedHashMap<String, FolderBase> listRootFolders(UserProfile.Id pid) throws Exception {
		LinkedHashMap<String, FolderBase> folders = new LinkedHashMap();
		MyFolder myFolder = null;
		IncomingFolder incFolder = null;
		
		// Defines personal folders
		myFolder = new MyFolder(pid);
		folders.put(myFolder.getId(), myFolder);
		
		// Reads incoming folders
		Connection coreCon = null;
		try {
			CoreManager core = WT.getCoreManager(getRunContext());
			List<IncomingShare> shares = core.listIncomingSharesForUser(getServiceId(), pid, SHARE_RESOURCE_CONTACTS);
			
			//coreCon = WT.getCoreConnection();
			//UserDAO useDao = UserDAO.getInstance();
			//OUser user = null;
			//UserProfile.Id inProfileId = null;
			for(IncomingShare share : shares) {
				incFolder = new IncomingFolder(share);
				folders.put(incFolder.getId(), incFolder);
				
				/*
				inProfileId = new UserProfile.Id(share.getDomainId(), share.getUserId());
				user = useDao.selectByDomainUser(coreCon, inProfileId.getDomainId(), inProfileId.getUserId());
				if(user != null) {
					incFolder = new IncomingFolder(user);
					folders.put(incFolder.getId(), incFolder);
				}
				*/
			}
			
		} catch(Exception ex) {
			logger.error("Unable to build root folders", ex);
			throw ex;
		} finally {
			DbUtils.closeQuietly(coreCon);
		}
		return folders;
	}
	
	public List<OFolder> listFolders(UserProfile.Id user) throws Exception {
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			FolderDAO folDao = FolderDAO.getInstance();
			return folDao.selectByDomainUser(con, user.getDomainId(), user.getUserId());
			
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public OFolder getFolder(int folderId) throws Exception {
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			FolderDAO folDao = FolderDAO.getInstance();
			return folDao.select(con, folderId);
			
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public OFolder insertFolder(OFolder item) throws Exception {
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			con.setAutoCommit(false);
			FolderDAO folDao = FolderDAO.getInstance();
			
			item.setFolderId(folDao.getSequence(con).intValue());
			item.setBuiltIn(false);
			if(item.getIsDefault()) folDao.resetIsDefaultByDomainUser(con, item.getDomainId(), item.getUserId());
			item.setRevisionInfo(createRevisionInfo());
			folDao.insert(con, item);
			DbUtils.commitQuietly(con);
			return item;
			
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public OFolder updateFolder(OFolder item) throws Exception {
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			con.setAutoCommit(false);
			FolderDAO folDao = FolderDAO.getInstance();
			
			if(item.getIsDefault()) folDao.resetIsDefaultByDomainUser(con, item.getDomainId(), item.getUserId());
			item.setRevisionInfo(createRevisionInfo());
			folDao.update(con, item);
			DbUtils.commitQuietly(con);
			return item;
			
		} catch(Exception ex) {
			DbUtils.rollbackQuietly(con);
			throw ex;
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public void deleteFolder(int folderId) throws Exception {
		Connection con = null;
		
		try {
			con = WT.getConnection(getManifest());
			FolderDAO folDao = FolderDAO.getInstance();
			folDao.delete(con, folderId);
			//TODO: cancellare contatti collegati
			
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	public List<FolderContacts> listContacts(FolderBase folder, Integer[] folders, Locale locale, String pattern) throws Exception {
		UserProfile.Id profileId = new UserProfile.Id(folder.getDomainId(), folder.getUserId());
		return listContacts(profileId, folders, locale, pattern);
	}
	
	public List<FolderContacts> listContacts(UserProfile.Id pid, Integer[] folders, Locale locale, String pattern) throws Exception {
		Connection con = null;
		ArrayList<FolderContacts> foldContacts = new ArrayList<>();
		FolderDAO folDao = FolderDAO.getInstance();
		
		try {
			con = WT.getConnection(getManifest());
			
			// Lists desired groups (tipically visibles) coming from passed list
			// Passed ids should belong to referenced folder(group), 
			// this is ensured using domainId and userId parameters in below query.
			List<OFolder> folds = folDao.selectByDomainUserIn(con, pid.getDomainId(), pid.getUserId(), folders);
			DBDirectoryManager dbdm = null;
			DirectoryResult dr = null;
			for(OFolder fold : folds) {
				dbdm = createDBDManager(pid, fold, locale);
				dr = dbdm.lookup(pattern, locale, false, false);
				foldContacts.add(new FolderContacts(fold, dr));
			}
			return foldContacts;
		
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
	
	private String ofGroup(String group) {
		return MessageFormat.format("_{0}", group);
	}
	
	private String lookupHeader(Locale locale, String key) {
		return StringUtils.defaultIfBlank(lookupResource(locale, key), StringUtils.removeStart(key, "fields.")).toUpperCase();
	}
	
	private DBDirectoryManager createDBDManager(UserProfile.Id pid, OFolder folder, Locale locale) throws SQLException {
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
				WT.getDataSource(getServiceId()),
				"contacts.contacts",
				lookupResource(locale, ContactsLocale.FIELDS_GROUPMAIN) + "{"		
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
				+ "CADDRESS " + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ","
				+ "+CCITY " + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ","
				+ "CSTATE " + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ","
				+ "CPOSTALCODE " + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ","
				+ "CCOUNTRY " + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ","
				+ "+CTELEPHONE " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE) + ","
				+ "CTELEPHONE2 " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE2) + ","
				+ "CFAX " + lookupHeader(locale, ContactsLocale.FIELDS_FAX) + ","
				+ "+CMOBILE " + lookupHeader(locale, ContactsLocale.FIELDS_MOBILE) + ","
				+ "CPAGER " + lookupHeader(locale, ContactsLocale.FIELDS_PAGER) + ","
				+ "+CEMAIL " + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ","	
				+ "CINSTANT_MSG " + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + ","
				+ "CDEPARTMENT " + lookupHeader(locale, ContactsLocale.FIELDS_DEPARTMENT) + ","
				+ "CMANAGER " + lookupHeader(locale, ContactsLocale.FIELDS_MANAGER) + ","
				+ "CASSISTANT " + lookupHeader(locale, ContactsLocale.FIELDS_ASSISTANT) + ","
				+ "CTELEPHONEASSISTANT " + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONEASSISTANT) + "}|"
				+ grouphome + "{"
				+ "HADDRESS X_" + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ","
				+ "HCITY X_" + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ","
				+ "HSTATE X_" + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ","
				+ "HPOSTALCODE X_" + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ","
				+ "HCOUNTRY X_" + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ","
				+ "+HTELEPHONE X_" + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE) + ","
				+ "HTELEPHONE2 X_" + lookupHeader(locale, ContactsLocale.FIELDS_TELEPHONE2) + ","
				+ "HFAX X_" + lookupHeader(locale, ContactsLocale.FIELDS_FAX) + ","
				+ "HMOBILE X_" + lookupHeader(locale, ContactsLocale.FIELDS_MOBILE) + ","
				+ "HPAGER X_" + lookupHeader(locale, ContactsLocale.FIELDS_PAGER) + ","
				+ "+HEMAIL X_" + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ","	
				+ "HINSTANT_MSG X_" + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + ","
				+ "HPARTNER " + lookupHeader(locale, ContactsLocale.FIELDS_PARTNER) + ","
				+ "+HBIRTHDAY " + lookupHeader(locale, ContactsLocale.FIELDS_BIRTHDAY) + ","
				+ "HANNIVERSARY " + lookupHeader(locale, ContactsLocale.FIELDS_ANNIVERSARY) + "}|"
				+ groupother + "{"
				+ "OADDRESS X__" + lookupHeader(locale, ContactsLocale.FIELDS_ADDRESS) + ","
				+ "OCITY X__" + lookupHeader(locale, ContactsLocale.FIELDS_CITY) + ","
				+ "OSTATE X__" + lookupHeader(locale, ContactsLocale.FIELDS_STATE) + ","
				+ "OPOSTALCODE X__" + lookupHeader(locale, ContactsLocale.FIELDS_POSTALCODE) + ","
				+ "OCOUNTRY X__" + lookupHeader(locale, ContactsLocale.FIELDS_COUNTRY) + ","
				+ "OEMAIL X__" + lookupHeader(locale, ContactsLocale.FIELDS_EMAIL) + ","
				+ "OINSTANT_MSG X__" + lookupHeader(locale, ContactsLocale.FIELDS_INSTANT_MSG) + "}|"
				+ "!" + lookupHeader(locale, ContactsLocale.FIELDS_GROUPSTATUS) + "{"
				+ "+FOLDER_ID FOLDER_ID,"
				+ "STATUS STATUS,"
				+ "LIST_ID LIST_ID }",
				*/
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
				+ "+FOLDER_ID FOLDER_ID,"
				+ "STATUS STATUS,"
				+ "LIST_ID LIST_ID }",
				
				
				
				null,
				clickfields, null, searchfields, mailfield, faxfield, firstnamefield, lastnamefield, companyfield,
				"contact_id",
				"(status is null or status!='D') and (folder_id=" + folder.getFolderId() + ")",
				"lastname,firstname,company",
				"contact_id=nextval('seq_contacts'),folder_id=" + folder.getFolderId()
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
				+ "+FOLDER_ID FOLDER_ID,"
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
				WT.getDataSource(getServiceId()),
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
		List<IncomingShare> shares = core.listIncomingSharesForUser(getServiceId(), profile.getId(), SHARE_RESOURCE_CONTACTS);
		String resource = AuthResourceShareInstance.buildName("GROUP");
		for(IncomingShare share : shares) {
			if(!WT.isPermitted(getServiceId(), resource, AuthResourceShareInstance.ACTION_READ, share.getShareId())) continue;
			dbdm = new DBDirectoryManager(
					DirectoryManager.DirectoryType.GROUP,
					"privatedb: " + share.getUserId(), wg.getDescription(),
					WT.getDataSource(getServiceId()),
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
	*/
	
	private synchronized void initGlobalDirectories(UserProfile.Id pid) throws SQLException {
		ContactsServiceSettings css = new ContactsServiceSettings(pid.getDomainId(), getServiceId());
		
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
						ds = WT.getDataSource(getServiceId(), sname);
					} catch (Exception ex) {
						logger.error("Unable to get DataSouce [{}, {}]", ex, getServiceId(), sname);
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
							WT.getDataSource(getServiceId()),
							stable, sfields, null, sclickfields, null, ssearchfields,
							smailfield, null, sfirstnamefield, slastnamefield, null, null, null,
							sorderfields, null
					);
				}
				globalDirectories.put(sid, dm);
				
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
				globalDirectories.put(sid, dm);
			}
			
			++index;
		}
	}
	
	public static class FolderContacts {
		public final OFolder folder;
		public final DirectoryResult result;
		
		public FolderContacts(OFolder folder, DirectoryResult result) {
			this.folder = folder;
			this.result = result;
		}
	}
}
