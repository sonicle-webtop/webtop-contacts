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
import com.sonicle.commons.PathUtils;
import com.sonicle.commons.URIUtils;
import com.sonicle.webtop.contacts.io.input.MemoryContactTextFileReader;
import com.sonicle.commons.web.Crud;
import com.sonicle.commons.web.ServletUtils;
import com.sonicle.commons.web.ServletUtils.IntegerArray;
import com.sonicle.commons.web.ServletUtils.StringArray;
import com.sonicle.commons.web.json.CompositeId;
import com.sonicle.commons.web.json.PayloadAsList;
import com.sonicle.commons.web.json.JsonResult;
import com.sonicle.commons.web.json.MapItem;
import com.sonicle.commons.web.json.Payload;
import com.sonicle.commons.web.json.extjs.FieldMeta;
import com.sonicle.commons.web.json.extjs.GridColumnMeta;
import com.sonicle.commons.web.json.extjs.GridMetadata;
import com.sonicle.commons.web.json.extjs.ExtTreeNode;
import com.sonicle.commons.web.json.extjs.GroupMeta;
import com.sonicle.webtop.contacts.ContactsUserSettings.CheckedFolders;
import com.sonicle.webtop.contacts.ContactsUserSettings.CheckedRoots;
import com.sonicle.webtop.contacts.bol.js.JsContact;
import com.sonicle.webtop.contacts.bol.js.JsCategory;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLinks;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLkp;
import com.sonicle.webtop.contacts.bol.js.JsContactPreview;
import com.sonicle.webtop.contacts.bol.js.JsContactsList;
import com.sonicle.webtop.contacts.bol.js.JsFolderNode;
import com.sonicle.webtop.contacts.bol.js.JsGridContact;
import com.sonicle.webtop.contacts.bol.js.JsSharing;
import com.sonicle.webtop.contacts.bol.js.ListFieldMapping;
import com.sonicle.webtop.contacts.model.ShareFolderCategory;
import com.sonicle.webtop.contacts.model.ShareRootCategory;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactsList;
import com.sonicle.webtop.contacts.bol.model.MyShareFolderCategory;
import com.sonicle.webtop.contacts.bol.model.MyShareRootCategory;
import com.sonicle.webtop.contacts.bol.model.RBAddressbook;
import com.sonicle.webtop.contacts.bol.model.RBContactDetail;
import com.sonicle.webtop.contacts.bol.model.SetupDataCategoryRemote;
import com.sonicle.webtop.contacts.io.VCardOutput;
import com.sonicle.webtop.contacts.io.input.ContactExcelFileReader;
import com.sonicle.webtop.contacts.io.input.ContactLDIFFileReader;
import com.sonicle.webtop.contacts.io.input.ContactTextFileReader;
import com.sonicle.webtop.contacts.io.input.ContactVCardFileReader;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.ContactAttachment;
import com.sonicle.webtop.contacts.model.ContactAttachmentWithBytes;
import com.sonicle.webtop.contacts.model.ContactAttachmentWithStream;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactItem;
import com.sonicle.webtop.contacts.model.ContactPictureWithBytes;
import com.sonicle.webtop.contacts.model.ContactsListRecipient;
import com.sonicle.webtop.contacts.model.ListContactsResult;
import com.sonicle.webtop.contacts.model.Grouping;
import com.sonicle.webtop.contacts.model.ShowBy;
import com.sonicle.webtop.contacts.msg.RemoteSyncResult;
import com.sonicle.webtop.contacts.rpt.RptAddressbook;
import com.sonicle.webtop.contacts.rpt.RptContactsDetail;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.CoreUserSettings;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.WebTopSession;
import com.sonicle.webtop.core.app.WebTopSession.UploadedFile;
import com.sonicle.webtop.core.bol.js.JsSimple;
import com.sonicle.webtop.core.bol.js.JsValue;
import com.sonicle.webtop.core.bol.js.JsWizardData;
import com.sonicle.webtop.core.model.SharePermsRoot;
import com.sonicle.webtop.core.bol.model.Sharing;
import com.sonicle.webtop.core.io.output.AbstractReport;
import com.sonicle.webtop.core.io.input.ExcelFileReader;
import com.sonicle.webtop.core.io.input.FileRowsReader;
import com.sonicle.webtop.core.io.output.ReportConfig;
import com.sonicle.webtop.core.io.input.TextFileReader;
import com.sonicle.webtop.core.model.SharePermsElements;
import com.sonicle.webtop.core.sdk.AsyncActionCollection;
import com.sonicle.webtop.core.sdk.BaseService;
import com.sonicle.webtop.core.sdk.BaseServiceAsyncAction;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.VCardUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author malbinola
 */
public class Service extends BaseService {
	public static final Logger logger = WT.getLogger(Service.class);
	public static final String WORK_VIEW = "w";
	public static final String HOME_VIEW = "h";
	
	private ContactsManager manager;
	private ContactsUserSettings us;
	private ContactsServiceSettings ss;
	
	private final LinkedHashMap<String, ShareRootCategory> roots = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, ShareFolderCategory> folders = new LinkedHashMap<>();
	private final HashMap<Integer, CategoryPropSet> folderProps = new HashMap<>();
	private final HashMap<String, ArrayList<ShareFolderCategory>> foldersByRoot = new HashMap<>();
	private final HashMap<Integer, ShareRootCategory> rootByFolder = new HashMap<>();
	private final AsyncActionCollection<Integer, SyncRemoteCategoryAA> syncRemoteCategoryAAs = new AsyncActionCollection<>();
	
	private CheckedRoots checkedRoots = null;
	private CheckedFolders checkedFolders = null;
	private final Object gridLock = new Object();
	
	//private ExportWizard wizard = null;
	
	@Override
	public void initialize() throws Exception {
		UserProfile up = getEnv().getProfile();
		manager = (ContactsManager)WT.getServiceManager(SERVICE_ID);
		ss = new ContactsServiceSettings(SERVICE_ID, up.getDomainId());
		us = new ContactsUserSettings(SERVICE_ID, up.getId());
		initFolders();
	}
	
	@Override
	public void cleanup() throws Exception {
		checkedFolders.clear();
		checkedFolders = null;
		checkedRoots.clear();
		checkedRoots = null;
		rootByFolder.clear();
		foldersByRoot.clear();
		folderProps.clear();
		folders.clear();
		roots.clear();
		us = null;
		ss = null;
		manager = null;
	}
	
	@Override
	public ServiceVars returnServiceVars() {
		ServiceVars co = new ServiceVars();
		co.put("categoryRemoteSyncEnabled", ss.getCategoryRemoteAutoSyncEnabled());
		co.put("defaultCategorySync", EnumUtils.toSerializedName(ss.getDefaultCategorySync()));
		co.put("view", EnumUtils.toSerializedName(us.getView()));
		co.put("showBy", EnumUtils.toSerializedName(us.getShowBy()));
		return co;
	}
	
	private WebTopSession getWts() {
		return getEnv().getWebTopSession();
	}
	
	private void runSyncRemoteCategory(int categoryId, String categoryName, boolean full) throws WTException {
		SyncRemoteCategoryAA aa = new SyncRemoteCategoryAA(categoryId, categoryName, full);
		if (syncRemoteCategoryAAs.putIfAbsent(categoryId, aa) != null) {
			logger.debug("SyncRemoteCategory run skipped [{}]", categoryId);
			return;
		}
		aa.start(RunContext.getSubject(), RunContext.getRunProfileId());
	}
	
	private void initFolders() throws WTException {
		synchronized(roots) {
			updateRootFoldersCache();
			updateFoldersCache();
			
			checkedRoots = us.getCheckedCategoryRoots();
			// If empty, adds MyNode checked by default!
			if(checkedRoots.isEmpty()) {
				checkedRoots.add(MyShareRootCategory.SHARE_ID);
				us.setCheckedCategoryRoots(checkedRoots);
			}
			checkedFolders = us.getCheckedCategoryFolders();
		}
	}
	
	private void updateRootFoldersCache() throws WTException {
		UserProfileId pid = getEnv().getProfile().getId();
		synchronized(roots) {
			roots.clear();
			roots.put(MyShareRootCategory.SHARE_ID, new MyShareRootCategory(pid));
			for(ShareRootCategory root : manager.listIncomingCategoryRoots()) {
				roots.put(root.getShareId(), root);
			}
		}
	}
	
	private void updateFoldersCache() throws WTException {
		synchronized(roots) {
			foldersByRoot.clear();
			folders.clear();
			rootByFolder.clear();
			for(ShareRootCategory root : roots.values()) {
				foldersByRoot.put(root.getShareId(), new ArrayList<ShareFolderCategory>());
				if(root instanceof MyShareRootCategory) {
					for(Category cat : manager.listCategories().values()) {
						final MyShareFolderCategory fold = new MyShareFolderCategory(root.getShareId(), cat);
						foldersByRoot.get(root.getShareId()).add(fold);
						folders.put(cat.getCategoryId(), fold);
						rootByFolder.put(cat.getCategoryId(), root);
					}
				} else {
					for(ShareFolderCategory fold : manager.listIncomingCategoryFolders(root.getShareId()).values()) {
						final int catId = fold.getCategory().getCategoryId();
						foldersByRoot.get(root.getShareId()).add(fold);
						folders.put(catId, fold);
						folderProps.put(catId, manager.getCategoryCustomProps(catId));
						rootByFolder.put(catId, root);
					}
				}
			}
		}
	}
	
	public void processManageFoldersTree(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<ExtTreeNode> children = new ArrayList<>();
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				String node = ServletUtils.getStringParameter(request, "node", true);
				boolean chooser = ServletUtils.getBooleanParameter(request, "chooser", false);
				
				if (node.equals("root")) { // Node: root -> list roots
					for (ShareRootCategory root : roots.values()) {
						children.add(createRootNode(chooser, root));
					}
				} else { // Node: folder -> list folders (categories)
					boolean writableOnly = ServletUtils.getBooleanParameter(request, "writableOnly", false);
					ShareRootCategory root = roots.get(node);
					
					if (root instanceof MyShareRootCategory) {
						for (Category cal : manager.listCategories().values()) {
							MyShareFolderCategory folder = new MyShareFolderCategory(node, cal);
							if (writableOnly && !folder.getElementsPerms().implies("CREATE")) continue;
							
							children.add(createFolderNode(chooser, folder, null, root.getPerms()));
						}
					} else {
						if (foldersByRoot.containsKey(root.getShareId())) {
							for (ShareFolderCategory folder : foldersByRoot.get(root.getShareId())) {
								if (writableOnly && !folder.getElementsPerms().implies("CREATE")) continue;
								
								final ExtTreeNode etn = createFolderNode(chooser, folder, folderProps.get(folder.getCategory().getCategoryId()), root.getPerms());
								if (etn != null) children.add(etn);
							}
						}
					}
				}
				new JsonResult("children", children).printTo(out);
				
			} else if (crud.equals(Crud.UPDATE)) {
				PayloadAsList<JsFolderNode.List> pl = ServletUtils.getPayloadAsList(request, JsFolderNode.List.class);
				
				for (JsFolderNode node : pl.data) {
					if (node._type.equals(JsFolderNode.TYPE_ROOT)) {
						toggleCheckedRoot(node.id, node._visible);
						
					} else if (node._type.equals(JsFolderNode.TYPE_FOLDER)) {
						CompositeId cid = new CompositeId().parse(node.id);
						toggleCheckedFolder(Integer.valueOf(cid.getToken(1)), node._visible);
					}
				}
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				PayloadAsList<JsFolderNode.List> pl = ServletUtils.getPayloadAsList(request, JsFolderNode.List.class);
				
				for (JsFolderNode node : pl.data) {
					if (node._type.equals(JsFolderNode.TYPE_FOLDER)) {
						CompositeId cid = new CompositeId().parse(node.id);
						manager.deleteCategory(Integer.valueOf(cid.getToken(1)));
					}
				}
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ManageFoldersTree", ex);
		}
	}
	
	public void processUpdateCheckedFolders(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			String rootId = ServletUtils.getStringParameter(request, "rootId", true);
			Boolean checked = ServletUtils.getBooleanParameter(request, "checked", true);
			
			synchronized(roots) {
				ArrayList<Integer> catIds = new ArrayList<>();
				for(ShareFolderCategory fold : foldersByRoot.get(rootId)) {
					catIds.add(fold.getCategory().getCategoryId());
				}
				toggleCheckedFolders(catIds.toArray(new Integer[catIds.size()]), checked);
			}
			
			new JsonResult().printTo(out);
			
		} catch(Exception ex) {
			logger.error("Error in UpdateCheckedFolders", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processLookupCategoryRoots(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		List<JsSimple> items = new ArrayList<>();
		
		try {
			boolean writableOnly = ServletUtils.getBooleanParameter(request, "writableOnly", true);
			
			synchronized(roots) {
				for (ShareRootCategory root : roots.values()) {
					if (root instanceof MyShareRootCategory) {
						UserProfile up = getEnv().getProfile();
						items.add(new JsSimple(up.getStringId(), up.getDisplayName()));
					} else {
						//TODO: se writableOnly verificare che il gruppo condiviso sia scrivibile
						items.add(new JsSimple(root.getOwnerProfileId().toString(), root.getDescription()));
					}
				}
			}
			
			new JsonResult("roots", items, items.size()).printTo(out);
			
		} catch(Exception ex) {
			logger.error("Error in LookupCategoryRoots", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	public void processLookupCategoryFolders(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		List<JsCategoryLkp> items = new ArrayList<>();
		
		try {
			synchronized(roots) {
				for (ShareRootCategory root : roots.values()) {
					if (foldersByRoot.containsKey(root.getShareId())) {
						for (ShareFolderCategory fold : foldersByRoot.get(root.getShareId())) {
							items.add(new JsCategoryLkp(fold, folderProps.get(fold.getCategory().getCategoryId())));
						}
					}
				}
			}
			new JsonResult("folders", items, items.size()).printTo(out);
			
		} catch(Exception ex) {
			logger.error("Error in LookupCategoryFolders", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	public void processManageSharing(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				
				Sharing sharing = manager.getSharing(id);
				String description = buildSharingPath(sharing);
				new JsonResult(new JsSharing(sharing, description)).printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Payload<MapItem, Sharing> pl = ServletUtils.getPayload(request, Sharing.class);
				
				manager.updateSharing(pl.data);
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ManageSharing", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	public void processManageCategory(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		Category item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				int id = ServletUtils.getIntParameter(request, "id", true);
				
				item = manager.getCategory(id);
				new JsonResult(new JsCategory(item, getEnv().getProfile().getTimeZone())).printTo(out);
				
			} else if (crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				item = manager.addCategory(JsCategory.createCategory(pl.data));
				updateFoldersCache();
				toggleCheckedFolder(item.getCategoryId(), true);
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				manager.updateCategory(JsCategory.createCategory(pl.data));
				updateFoldersCache();
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.DELETE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				manager.deleteCategory(pl.data.categoryId);
				updateFoldersCache();
				toggleCheckedFolder(pl.data.categoryId, false);
				new JsonResult().printTo(out);
				
			} else if (crud.equals("readLinks")) {
				int id = ServletUtils.getIntParameter(request, "id", true);
				
				ShareFolderCategory fold = folders.get(id);
				if (fold == null) throw new WTException("Category not found [{}]", id);
				Map<String, String> links = manager.getCategoryLinks(id);
				new JsonResult(new JsCategoryLinks(id, fold.getCategory().getName(), links)).printTo(out);
				
			} else if (crud.equals("sync")) {
				int id = ServletUtils.getIntParameter(request, "id", true);
				boolean full = ServletUtils.getBooleanParameter(request, "full", false);
				
				item = manager.getCategory(id);
				if (item == null) throw new WTException("Category not found [{}]", id);
				runSyncRemoteCategory(id, item.getName(), full);
				
				new JsonResult().printTo(out);
			}
			
		} catch(Throwable t) {
			logger.error("Error in ManageCategory", t);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	public void processSetupCategoryRemote(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		WebTopSession wts = getWts();
		final String PROPERTY_PREFIX = "setupremotecategory-";
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals("s1")) {
				String tag = ServletUtils.getStringParameter(request, "tag", true);
				String profileId = ServletUtils.getStringParameter(request, "profileId", true);
				String provider = ServletUtils.getStringParameter(request, "provider", true);
				String url = ServletUtils.getStringParameter(request, "url", true);
				String username = ServletUtils.getStringParameter(request, "username", null);
				String password = ServletUtils.getStringParameter(request, "password", null);
				
				Category.Provider remoteProvider = EnumUtils.forSerializedName(provider, Category.Provider.class);
				URI uri = URIUtils.createURI(url);
				
				ContactsManager.ProbeCategoryRemoteUrlResult result = manager.probeCategoryRemoteUrl(remoteProvider, uri, username, password);
				if (result == null) throw new CategoryProbeException(this.lookupResource("setupCategoryRemote.error.probe"));
				
				SetupDataCategoryRemote setup = new SetupDataCategoryRemote();
				setup.setProfileId(profileId);
				setup.setProvider(remoteProvider);
				setup.setUrl(uri);
				setup.setUsername(username);
				setup.setPassword(password);
				setup.setName(result.displayName);
				wts.setProperty(SERVICE_ID, PROPERTY_PREFIX+tag, setup);
				
				new JsonResult(setup).printTo(out);
				
			} else if (crud.equals("s2")) {
				String tag = ServletUtils.getStringParameter(request, "tag", true);
				String name = ServletUtils.getStringParameter(request, "name", true);
				String color = ServletUtils.getStringParameter(request, "color", true);
				Short syncFrequency = ServletUtils.getShortParameter(request, "syncFrequency", null);
				
				wts.hasPropertyOrThrow(SERVICE_ID, PROPERTY_PREFIX+tag);
				SetupDataCategoryRemote setup = (SetupDataCategoryRemote) wts.getProperty(SERVICE_ID, PROPERTY_PREFIX+tag);
				setup.setName(name);
				setup.setColor(color);
				setup.setSyncFrequency(syncFrequency);
				
				Category cal = manager.addCategory(setup.toCategory());
				wts.clearProperty(SERVICE_ID, PROPERTY_PREFIX+tag);
				updateFoldersCache();
				toggleCheckedFolder(cal.getCategoryId(), true);
				runSyncRemoteCategory(cal.getCategoryId(), cal.getName(), true); // Starts a full-sync
				
				new JsonResult().printTo(out);
			}
			
		} catch (CategoryProbeException ex) {
			new JsonResult(ex).printTo(out);
		} catch (Exception ex) {
			logger.error("Error in SetupCategoryRemote", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processManageHiddenCategories(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String rootId = ServletUtils.getStringParameter(request, "rootId", true);
				if (rootId.equals(MyShareRootCategory.SHARE_ID)) throw new WTException("Personal root is not supported");
				
				ArrayList<JsSimple> items = new ArrayList<>();
				synchronized(roots) {
					for (ShareFolderCategory folder : foldersByRoot.get(rootId)) {
						CategoryPropSet pset = folderProps.get(folder.getCategory().getCategoryId());
						if ((pset != null) && pset.getHiddenOrDefault(false)) {
							items.add(new JsSimple(folder.getCategory().getCategoryId(), folder.getCategory().getName()));
						}
					}
				}
				new JsonResult(items).printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
				Boolean hidden = ServletUtils.getBooleanParameter(request, "hidden", false);
				
				updateCategoryFolderVisibility(categoryId, hidden);
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				ServletUtils.StringArray ids = ServletUtils.getObjectParameter(request, "ids", ServletUtils.StringArray.class, true);
				
				HashSet<String> pids = new HashSet<>();
				synchronized(roots) {
					for(String id : ids) {
						int categoryId = Integer.valueOf(id);
						ShareFolderCategory fold = folders.get(categoryId);
						if (fold != null) {
							updateCategoryFolderVisibility(categoryId, null);
							pids.add(fold.getCategory().getProfileId().toString());
						}
					}
				}
				new JsonResult(pids).printTo(out);
			}
			
		} catch(Exception ex) {
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processSetCategoryPropColor(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			String color = ServletUtils.getStringParameter(request, "color", null);

			updateCategoryFolderColor(id, color);
			new JsonResult().printTo(out);
			
		} catch(Exception ex) {
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processSetCategoryPropSync(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			String sync = ServletUtils.getStringParameter(request, "sync", null);
			
			updateCategoryFolderSync(id, EnumUtils.forSerializedName(sync, Category.Sync.class));
			new JsonResult().printTo(out);
			
		} catch(Exception ex) {
			logger.error("Error in SetCategorySync", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processManageGridContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<JsGridContact> items = new ArrayList<>();
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				GridView view = ServletUtils.getEnumParameter(request, "view", GridView.WORK, GridView.class);
				Grouping groupBy = ServletUtils.getEnumParameter(request, "groupBy", Grouping.ALPHABETIC, Grouping.class);
				ShowBy showBy = ServletUtils.getEnumParameter(request, "showBy", ShowBy.LASTNAME, ShowBy.class);
				
				int page = ServletUtils.getIntParameter(request, "page", true);
				int limit = ServletUtils.getIntParameter(request, "limit", 50);
				String query = ServletUtils.getStringParameter(request, "query", null);
				
				//TODO: optimize call to skip fullCount for subsequent calls
				boolean listOnly = GridView.CONTACTS_LIST.equals(view);
				String pattern = StringUtils.isBlank(query) ? null : "%" + query + "%";
				
				List<Integer> visibleCategoryIds = getVisibleFolderIds(true);
				ListContactsResult result = manager.listContacts(visibleCategoryIds, listOnly, groupBy, showBy, pattern, page, limit, true);
				for (ContactItem item : result.items) {
					final ShareRootCategory root = rootByFolder.get(item.getCategoryId());
					if (root == null) continue;
					final ShareFolderCategory fold = folders.get(item.getCategoryId());
					if (fold == null) continue;
					CategoryPropSet pset = folderProps.get(item.getCategoryId());
					
					items.add(new JsGridContact(view, root, fold, pset, item));
				}
				
				GridMetadata meta = new GridMetadata(true);
				if (Grouping.COMPANY.equals(groupBy)) {
					meta.setGroupInfo(new GroupMeta("company", "ASC"));
				} else {
					meta.setGroupInfo(new GroupMeta("letter", "ASC"));
				}
				
				new JsonResult(items, result.fullCount)
						.setPage(page)
						.setMetaData(meta)
						.printTo(out);
				
			} else if (crud.equals(Crud.DELETE)) {
				StringArray uids = ServletUtils.getObjectParameter(request, "ids", StringArray.class, true);
				
				ArrayList<Integer> contactIds = new ArrayList<>();
				ArrayList<Integer> contactsListIds = new ArrayList<>();
				for (String uid : uids) {
					CompositeId cid = JsGridContact.Id.parse(uid);
					int contactId = JsGridContact.Id.contactId(cid);
					boolean isList = JsGridContact.Id.isList(cid);
					if (isList) {
						contactsListIds.add(contactId);
					} else {
						contactIds.add(contactId);
					}
				}
				manager.deleteContact(contactIds);
				manager.deleteContactsList(contactsListIds);
				
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.MOVE)) {
				StringArray uids = ServletUtils.getObjectParameter(request, "ids", StringArray.class, true);
				Integer categoryId = ServletUtils.getIntParameter(request, "targetCategoryId", true);
				boolean copy = ServletUtils.getBooleanParameter(request, "copy", false);
				
				ArrayList<Integer> contactIds = new ArrayList<>();
				ArrayList<Integer> contactsListIds = new ArrayList<>();
				for (String uid : uids) {
					CompositeId cid = JsGridContact.Id.parse(uid);
					int contactId = JsGridContact.Id.contactId(cid);
					boolean isList = JsGridContact.Id.isList(cid);
					if (isList) {
						contactsListIds.add(contactId);
					} else {
						contactIds.add(contactId);
					}
				}
				manager.moveContacts(copy, contactIds, categoryId);
				manager.moveContactsList(copy, contactsListIds, categoryId);
				
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in ManageGridContacts", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processGetContactPreview(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				String uid = ServletUtils.getStringParameter(request, "id", true);
				
				CompositeId cid = JsGridContact.Id.parse(uid);
				int contactId = JsGridContact.Id.contactId(cid);
				boolean isList = JsGridContact.Id.isList(cid);
				
				if (isList) {
					ContactsList contactsList = manager.getContactsList(contactId);
					
					ShareFolderCategory fold = folders.get(contactsList.getCategoryId());
					if (fold == null) throw new WTException("Folder not found [{}]", contactsList.getCategoryId());
					CategoryPropSet pset = folderProps.get(contactsList.getCategoryId());
					
					new JsonResult(new JsContactPreview(fold, pset, contactsList)).printTo(out);
					
				} else {
					Contact contact = manager.getContact(contactId);
					if (contact == null) throw new WTException("Contact not found [{}]", contactId);
					ContactCompany contactCompany = !StringUtils.isBlank(contact.getCompany()) ? manager.getContactCompany(contactId) : new ContactCompany();
					
					ShareFolderCategory fold = folders.get(contact.getCategoryId());
					if (fold == null) throw new WTException("Folder not found [{}]", contact.getCategoryId());
					CategoryPropSet pset = folderProps.get(contact.getCategoryId());
					
					new JsonResult(new JsContactPreview(fold, pset, contact, contactCompany)).printTo(out);
				}
			}
			
		} catch(Exception ex) {
			logger.error("Error in ManageContactDetails", ex);
			new JsonResult(ex).printTo(out);	
		}
	}
	
	public void processManageContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		JsContact item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				
				int contactId = Integer.parseInt(id);
				Contact contact = manager.getContact(contactId);
				UserProfileId ownerId = manager.getCategoryOwner(contact.getCategoryId());
				item = new JsContact(ownerId, contact);
				
				new JsonResult(item).printTo(out);
				
			} else if (crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsContact> pl = ServletUtils.getPayload(request, JsContact.class);
				
				Contact contact = JsContact.buildContact(pl.data);
				
				// We reuse picture field passing the uploaded file ID.
				// Due to different formats we can be sure that IDs don't collide.
				if (hasUploadedFile(pl.data.picture)) {
					// If found, new picture has been uploaded!
					contact.setPicture(getUploadedContactPicture(pl.data.picture));
				}
				for (JsContact.Attachment jsatt : pl.data.attachments) {
					UploadedFile upFile = getUploadedFileOrThrow(jsatt._uplId);
					ContactAttachmentWithStream att = new ContactAttachmentWithStream(upFile.getFile());
					att.setAttachmentId(jsatt.id);
					att.setFilename(upFile.getFilename());
					att.setSize(upFile.getSize());
					att.setMediaType(upFile.getMediaType());
					contact.getAttachments().add(att);
				}
				manager.addContact(contact);
				
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsContact> pl = ServletUtils.getPayload(request, JsContact.class);
				
				boolean processPicture = false;
				Contact contact = JsContact.buildContact(pl.data);
				
				// We reuse picture field passing the uploaded file ID.
				// Due to different formats we can be sure that IDs don't collide.
				if (hasUploadedFile(pl.data.picture)) {
					// If found, new picture has been uploaded!
					processPicture = true;
					contact.setPicture(getUploadedContactPicture(pl.data.picture));
				} else {
					// If blank, picture has been deleted!
					if (StringUtils.isBlank(pl.data.picture)) processPicture = true;
				}
				for (JsContact.Attachment jsatt : pl.data.attachments) {
					if (!StringUtils.isBlank(jsatt._uplId)) {
						UploadedFile upFile = getUploadedFileOrThrow(jsatt._uplId);
						ContactAttachmentWithStream att = new ContactAttachmentWithStream(upFile.getFile());
						att.setAttachmentId(jsatt.id);
						att.setFilename(upFile.getFilename());
						att.setSize(upFile.getSize());
						att.setMediaType(upFile.getMediaType());
						contact.getAttachments().add(att);
					} else {
						ContactAttachment att = new ContactAttachment();
						att.setAttachmentId(jsatt.id);
						att.setFilename(jsatt.name);
						att.setSize(jsatt.size);
						contact.getAttachments().add(att);
					}
				}
				manager.updateContact(contact, processPicture);
				
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.DELETE)) {
				IntegerArray ids = ServletUtils.getObjectParameter(request, "ids", IntegerArray.class, true);
				
				if (ids.size() == 1) {
					manager.deleteContact(ids.get(0));
				} else {
					manager.deleteContact(ids);
				}
				
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in ManageContacts", ex);
			new JsonResult(false, "Error").printTo(out);	
		}
	}
	
	public void processGetContactPicture(HttpServletRequest request, HttpServletResponse response) {
		try {
			String id = ServletUtils.getStringParameter(request, "id", true);
			
			ContactPictureWithBytes picture = null;
			if (hasUploadedFile(id)) {
				picture = getUploadedContactPicture(id);
			} else {
				int contactId = Integer.parseInt(id);
				picture = manager.getContactPicture(contactId);
			}
			if (picture != null) {
				try (ByteArrayInputStream bais = new ByteArrayInputStream(picture.getBytes())) {
					ServletUtils.writeContent(response, bais, picture.getMediaType());
				}
			}
			
		} catch(Exception ex) {
			logger.error("Error in GetContactPicture", ex);
		}
	}
	
	public void processDownloadContactAttachment(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			boolean inline = ServletUtils.getBooleanParameter(request, "inline", false);
			String attachmentId = ServletUtils.getStringParameter(request, "attachmentId", null);
			
			if (!StringUtils.isBlank(attachmentId)) {
				Integer contactId = ServletUtils.getIntParameter(request, "contactId", true);
				
				ContactAttachmentWithBytes attch = manager.getContactAttachment(contactId, attachmentId);
				InputStream is = null;
				try {
					is = new ByteArrayInputStream(attch.getBytes());
					ServletUtils.writeFileResponse(response, inline, attch.getFilename(), null, attch.getSize(), is);
				} finally {
					IOUtils.closeQuietly(is);
				}
			} else {
				String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
				
				UploadedFile uplFile = getUploadedFileOrThrow(uploadId);
				InputStream is = null;
				try {
					is = new FileInputStream(uplFile.getFile());
					ServletUtils.writeFileResponse(response, inline, uplFile.getFilename(), null, uplFile.getSize(), is);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
			
		} catch(Exception ex) {
			logger.error("Error in DownloadContactAttachment", ex);
			ServletUtils.writeErrorHandlingJs(response, ex.getMessage());
		}
	}
	
	public void processManageContactsLists(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		JsContactsList item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				
				int contactId = Integer.parseInt(id);
				ContactsList list = manager.getContactsList(contactId);
				UserProfileId ownerId = manager.getCategoryOwner(list.getCategoryId());
				item = new JsContactsList(ownerId, list);
				
				new JsonResult(item).printTo(out);
				
			} else if(crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsContactsList> pl = ServletUtils.getPayload(request, JsContactsList.class);
				
				ContactsList contact = JsContactsList.buildContactsList(pl.data);
				manager.addContactsList(contact);
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsContactsList> pl = ServletUtils.getPayload(request, JsContactsList.class);
				
				ContactsList contact = JsContactsList.buildContactsList(pl.data);
				manager.updateContactsList(contact);
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				IntegerArray ids = ServletUtils.getObjectParameter(request, "ids", IntegerArray.class, true);
				
				if (ids.size() == 1) {
					manager.deleteContactsList(ids.get(0));
				} else {
					manager.deleteContactsList(ids);
				}
				
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in ManageContactsLists", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	public void processAddToContactsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			String list = ServletUtils.getStringParameter(request, "list", true);
			String recipientType = ServletUtils.getStringParameter(request, "recipientType", true);
			ArrayList<String> emails=ServletUtils.getStringParameters(request, "emails");
			
			int idlist=ContactsUtils.getListIdFromInternetAddress(new InternetAddress(list));
			
			ContactsList cl=new ContactsList();
			cl.setContactId(idlist);
			for(String email: emails) {
				ContactsListRecipient rcpt = new ContactsListRecipient();
				rcpt.setRecipient(email);
				rcpt.setRecipientType(recipientType);
				cl.getRecipients().add(rcpt);
			}
			manager.addToContactsList(idlist, cl);
			
			new JsonResult().printTo(out);
		} catch(Exception ex) {
			logger.error("Error in AddToContactsList", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	public void processImportContactsFromText(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			String crud = ServletUtils.getStringParameter(request, "op", true);
			String encoding = ServletUtils.getStringParameter(request, "encoding", true);
			String delimiter = ServletUtils.getStringParameter(request, "delimiter", true);
			String lineSeparator = ServletUtils.getStringParameter(request, "lineSeparator", true);
			String textQualifier = ServletUtils.getStringParameter(request, "textQualifier", null);
			Integer hr = ServletUtils.getIntParameter(request, "headersRow", true);
			Integer fdr = ServletUtils.getIntParameter(request, "firstDataRow", true);
			Integer ldr = ServletUtils.getIntParameter(request, "lastDataRow", -1);
			
			UploadedFile upl = getUploadedFile(uploadId);
			if(upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			File file = new File(WT.getTempFolder(), upl.getUploadId());
			
			CsvPreference pref = TextFileReader.buildCsvPreference(delimiter, lineSeparator, textQualifier);
			ContactTextFileReader rea = new ContactTextFileReader(pref, encoding);
			rea.setHeadersRow(hr);
			rea.setFirstDataRow(fdr);
			if(ldr != -1) rea.setLastDataRow(ldr);
			
			if(crud.equals("columns")) {
				ArrayList<JsValue> items = new ArrayList<>();
				for(String sheet : rea.listColumnNames(file).values()) {
					items.add(new JsValue(sheet));
				}
				new JsonResult("columns", items).printTo(out);
				
			} else if(crud.equals("mappings")) {
				List<FileRowsReader.FieldMapping> items = rea.listFieldMappings(file, MemoryContactTextFileReader.MAPPING_TARGETS);
				new JsonResult("mappings", items).printTo(out);
				
			} else if(crud.equals("do")) {
				Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
				String mode = ServletUtils.getStringParameter(request, "importMode", true);
				ListFieldMapping mappings = ServletUtils.getObjectParameter(request, "mappings", ListFieldMapping.class, true);
				
				rea.setMappings(mappings);
				LogEntries log = manager.importContacts(categoryId, rea, file, mode);
				removeUploadedFile(uploadId);
				new JsonResult(new JsWizardData(log.print())).printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ImportContactsFromText", ex);
			new JsonResult(false, ex.getMessage()).printTo(out);
		}
	}
	
	public void processImportContactsFromExcel(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			Boolean binary = ServletUtils.getBooleanParameter(request, "binary", false);
			String crud = ServletUtils.getStringParameter(request, "op", true);
			
			UploadedFile upl = getUploadedFile(uploadId);
			if(upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			File file = new File(WT.getTempFolder(), upl.getUploadId());
			
			if(crud.equals("sheets")) {
				ArrayList<JsValue> items = new ArrayList<>();
				ExcelFileReader rea = new ExcelFileReader(binary);
				List<String> sheets = rea.listSheets(file);
				for(String sheet : sheets) {
					items.add(new JsValue(sheet));
				}
				new JsonResult("sheets", items).printTo(out);
				
			} else {
				String sheet = ServletUtils.getStringParameter(request, "sheet", true);
				Integer hr = ServletUtils.getIntParameter(request, "headersRow", true);
				Integer fdr = ServletUtils.getIntParameter(request, "firstDataRow", true);
				Integer ldr = ServletUtils.getIntParameter(request, "lastDataRow", -1);
				
				ContactExcelFileReader rea = new ContactExcelFileReader(binary);
				rea.setHeadersRow(hr);
				rea.setFirstDataRow(fdr);
				if(ldr != -1) rea.setLastDataRow(ldr);
				rea.setSheet(sheet);
				
				if(crud.equals("columns")) {
					ArrayList<JsValue> items = new ArrayList<>();
					for(String col : rea.listColumnNames(file).values()) {
						items.add(new JsValue(col));
					}
					new JsonResult("columns", items).printTo(out);
					
				} else if(crud.equals("mappings")) {
					List<FileRowsReader.FieldMapping> items = rea.listFieldMappings(file, MemoryContactTextFileReader.MAPPING_TARGETS);
					new JsonResult("mappings", items).printTo(out);
					
				} else if(crud.equals("do")) {
					Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
					String mode = ServletUtils.getStringParameter(request, "importMode", true);
					ListFieldMapping mappings = ServletUtils.getObjectParameter(request, "mappings", ListFieldMapping.class, true);
				
					rea.setMappings(mappings);
					LogEntries log = manager.importContacts(categoryId, rea, file, mode);
					removeUploadedFile(uploadId);
					new JsonResult(new JsWizardData(log.print())).printTo(out);
				}
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ImportContactsFromExcel", ex);
			new JsonResult(false, ex.getMessage()).printTo(out);
		}
	}
	
	public void processImportContactsFromVCard(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			String crud = ServletUtils.getStringParameter(request, "op", true);
			
			UploadedFile upl = getUploadedFile(uploadId);
			if(upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			File file = new File(WT.getTempFolder(), upl.getUploadId());
			
			ContactVCardFileReader rea = new ContactVCardFileReader();
			
			if(crud.equals("do")) {
				Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
				String mode = ServletUtils.getStringParameter(request, "importMode", true);
				
				LogEntries log = manager.importContacts(categoryId, rea, file, mode);
				removeUploadedFile(uploadId);
				new JsonResult(new JsWizardData(log.print())).printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ImportContactsFromVCard", ex);
			new JsonResult(false, ex.getMessage()).printTo(out);
		}
	}
		public void processImportContactsFromLDIF(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			String crud = ServletUtils.getStringParameter(request, "op", true);
			
			UploadedFile upl = getUploadedFile(uploadId);
			if(upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			File file = new File(WT.getTempFolder(), upl.getUploadId());

			ContactLDIFFileReader  rea = new ContactLDIFFileReader();
			
			if(crud.equals("do")) {
				Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
				String mode = ServletUtils.getStringParameter(request, "importMode", true);
				
				LogEntries log = manager.importContacts(categoryId, rea, file, mode);
				removeUploadedFile(uploadId);
				new JsonResult(new JsWizardData(log.print())).printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ImportContactsFromVCard", ex);
			new JsonResult(false, ex.getMessage()).printTo(out);
		}
	}
		
	public void processPrintAddressbook(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<RBAddressbook> items = new ArrayList<>();
		
		try {
			String filename = ServletUtils.getStringParameter(request, "filename", "print");
			GridView view = ServletUtils.getEnumParameter(request, "view", GridView.WORK, GridView.class);
			Grouping groupBy = ServletUtils.getEnumParameter(request, "groupBy", Grouping.ALPHABETIC, Grouping.class);
			ShowBy showBy = ServletUtils.getEnumParameter(request, "showBy", ShowBy.LASTNAME, ShowBy.class);
			String query = ServletUtils.getStringParameter(request, "query", null);
			
			boolean listOnly = GridView.CONTACTS_LIST.equals(view);
			String pattern = StringUtils.isBlank(query) ? null : "%" + query + "%";
			int limit = 500;
			
			List<Integer> visibleCategoryIds = getVisibleFolderIds(true);
			ListContactsResult result = manager.listContacts(visibleCategoryIds, listOnly, groupBy, showBy, pattern, 1, limit, true);
			if (result.fullCount > limit) throw new WTException("Too many elements, limit is {}", limit);
			for (ContactItem item : result.items) {
				final ShareFolderCategory fold = folders.get(item.getCategoryId());
				if (fold == null) continue;
				CategoryPropSet pset = folderProps.get(item.getCategoryId());

				items.add(new RBAddressbook(fold.getCategory(), pset, item));
			}
			
			ReportConfig.Builder builder = reportConfigBuilder();
			RptAddressbook rpt = new RptAddressbook(builder.build());
			rpt.setDataSource(items);
			
			ServletUtils.setFileStreamHeaders(response, filename + ".pdf");
			WT.generateReportToStream(rpt, AbstractReport.OutputType.PDF, response.getOutputStream());
			
		} catch(Exception ex) {
			logger.error("Error in PrintAddressbook", ex);
			ServletUtils.writeErrorHandlingJs(response, ex.getMessage());
		}
	}
	
	public void processPrintContactsDetail(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<RBContactDetail> items = new ArrayList<>();
		CoreManager core = WT.getCoreManager();
		
		try {
			String filename = ServletUtils.getStringParameter(request, "filename", "print");
			IntegerArray ids = ServletUtils.getObjectParameter(request, "ids", IntegerArray.class, true);
			
			Category category = null;
			for (Integer id : ids) {
				Contact contact = manager.getContact(id);
				ContactPictureWithBytes picture = null;
				
				category = manager.getCategory(contact.getCategoryId());
				if (contact.hasPicture()) picture = manager.getContactPicture(id);
				items.add(new RBContactDetail(core, category, contact, picture));
			}
			
			ReportConfig.Builder builder = reportConfigBuilder();
			RptContactsDetail rpt = new RptContactsDetail(builder.build());
			rpt.setDataSource(items);
			
			ServletUtils.setFileStreamHeaders(response, filename + ".pdf");
			WT.generateReportToStream(rpt, AbstractReport.OutputType.PDF, response.getOutputStream());
			
		} catch(Exception ex) {
			logger.error("Error in action PrintContact", ex);
			ServletUtils.writeErrorHandlingJs(response, ex.getMessage());
		}
	}
	
	public void processPrepareSendContactAsEmail(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<MapItem> items = new ArrayList<>();
		
		try {
			String tag = ServletUtils.getStringParameter(request, "uploadTag", true);
			IntegerArray ids = ServletUtils.getObjectParameter(request, "ids", IntegerArray.class, true);
			
			String prodId = VCardUtils.buildProdId(ManagerUtils.getProductName());
			VCardOutput vcout = new VCardOutput(prodId);
			for (Integer id : ids) {
				final Contact contact = manager.getContact(id);
				if (contact == null) continue;
				
				if (contact.hasPicture()) {
					ContactPictureWithBytes picture = manager.getContactPicture(id);
					if (picture != null) {
						contact.setPicture(picture);
					} else {
						logger.warn("Unable to extract picture [{}]", id);
					}
				}
				
				final String filename = buildContactFilename(contact) + ".vcf";
				UploadedFile upfile = addAsUploadedFile(tag, filename, "text/vcard", IOUtils.toInputStream(vcout.write(vcout.toVCard(contact))));
				
				items.add(new MapItem()
					.add("uploadId", upfile.getUploadId())
					.add("fileName", filename)
					.add("fileSize", upfile.getSize())
				);
			}
			new JsonResult(items).printTo(out);
			
		} catch(Exception ex) {
			logger.error("Error in PrepareSendContactAsEmail", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	private ContactPictureWithBytes getUploadedContactPicture(String uploadId) throws WTException {
		UploadedFile upl = getUploadedFileOrThrow(uploadId);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(upl.getFile());
			ContactPictureWithBytes pic = new ContactPictureWithBytes(IOUtils.toByteArray(fis));
			pic.setMediaType(upl.getMediaType());
			return pic;
			
		} catch (FileNotFoundException ex) {
			throw new WTException(ex, "File not found {0}");
		} catch (IOException ex) {
			throw new WTException(ex, "Unable to read file {0}");
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}
	
	private ReportConfig.Builder reportConfigBuilder() {
		UserProfile.Data ud = getEnv().getProfile().getData();
		CoreUserSettings cus = getEnv().getCoreUserSettings();
		return new ReportConfig.Builder()
				.useLocale(ud.getLocale())
				.useTimeZone(ud.getTimeZone().toTimeZone())
				.dateFormatShort(cus.getShortDateFormat())
				.dateFormatLong(cus.getLongDateFormat())
				.timeFormatShort(cus.getShortTimeFormat())
				.timeFormatLong(cus.getLongTimeFormat())
				.generatedBy(WT.getPlatformName() + " " + lookupResource(ContactsLocale.SERVICE_NAME))
				.printedBy(ud.getDisplayName());
	}
	
	private String buildSharingPath(Sharing sharing) throws WTException {
		StringBuilder sb = new StringBuilder();
		
		// Root description part
		CompositeId cid = new CompositeId().parse(sharing.getId());
		if(roots.containsKey(cid.getToken(0))) {
			ShareRootCategory root = roots.get(cid.getToken(0));
			if(root instanceof MyShareRootCategory) {
				sb.append(lookupResource(ContactsLocale.CATEGORIES_MY));
			} else {
				sb.append(root.getDescription());
			}
		}
		
		// Folder description part
		if(sharing.getLevel() == 1) {
			int catId = Integer.valueOf(cid.getToken(1));
			Category category = manager.getCategory(catId);
			sb.append("/");
			sb.append((category != null) ? category.getName() : cid.getToken(1));
		}
		
		return sb.toString();
	}
	
	private String buildContactFilename(Contact contact) {
		final String full = contact.getFullName();
		return (StringUtils.isBlank(full)) ? String.valueOf(contact.getContactId()) : PathUtils.sanitizeFileName(full);
	}
	
	private ArrayList<Integer> getVisibleFolderIds(boolean cleanupOrphans) {
		ArrayList<Integer> ids = new ArrayList<>();
		ArrayList<Integer> orphans = new ArrayList<>();
		
		Integer[] checked = getCheckedFolders();
		for (ShareRootCategory root : getCheckedRoots()) {
			for (Integer folderId : checked) {
				final ShareRootCategory folderRoot = rootByFolder.get(folderId);
				if (folderRoot == null) {
					if (cleanupOrphans) orphans.add(folderId);
					continue;
				}
				
				if (root.getShareId().equals(folderRoot.getShareId())) {
					ids.add(folderId);
				}
			}
		}
		if (cleanupOrphans) toggleCheckedFolders(orphans.toArray(new Integer[orphans.size()]), false);
		return ids;
	}
	
	private List<ShareRootCategory> getCheckedRoots() {
		ArrayList<ShareRootCategory> checked = new ArrayList<>();
		for(ShareRootCategory root : roots.values()) {
			if(!checkedRoots.contains(root.getShareId())) continue; // Skip folder if not visible
			checked.add(root);
		}
		return checked;
	}
	
	private Integer[] getCheckedFolders() {
		return checkedFolders.toArray(new Integer[checkedFolders.size()]);
	}
	
	private void toggleCheckedRoot(String shareId, boolean checked) {
		toggleCheckedRoots(new String[]{shareId}, checked);
	}
	
	private void toggleCheckedRoots(String[] shareIds, boolean checked) {
		synchronized(roots) {
			for(String shareId : shareIds) {
				if(checked) {
					checkedRoots.add(shareId);
				} else {
					checkedRoots.remove(shareId);
				}
			}	
			us.setCheckedCategoryRoots(checkedRoots);
		}
	}
	
	private void toggleCheckedFolder(Integer folderId, boolean checked) {
		toggleCheckedFolders(new Integer[]{folderId}, checked);
	}
	
	private void toggleCheckedFolders(Integer[] folderIds, boolean checked) {
		synchronized(roots) {
			for(int folderId : folderIds) {
				if(checked) {
					checkedFolders.add(folderId);
				} else {
					checkedFolders.remove(folderId);
				}
			}
			us.setCheckedCategoryFolders(checkedFolders);
		}
	}
	
	private void updateCategoryFolderVisibility(int categoryId, Boolean hidden) {
		synchronized(roots) {
			try {
				CategoryPropSet pset = manager.getCategoryCustomProps(categoryId);
				pset.setHidden(hidden);
				manager.updateCategoryCustomProps(categoryId, pset);
				
				// Update internal cache
				ShareFolderCategory folder = folders.get(categoryId);
				if (!(folder instanceof MyShareFolderCategory)) {
					folderProps.put(categoryId, pset);
				}
			} catch(WTException ex) {
				logger.error("Error saving custom category props", ex);
			}
		}
	}
	
	private void updateCategoryFolderColor(int categoryId, String color) {
		synchronized(roots) {
			try {
				CategoryPropSet pset = manager.getCategoryCustomProps(categoryId);
				pset.setColor(color);
				manager.updateCategoryCustomProps(categoryId, pset);
				
				// Update internal cache
				ShareFolderCategory folder = folders.get(categoryId);
				if (!(folder instanceof MyShareFolderCategory)) {
					folderProps.put(categoryId, pset);
				}
			} catch(WTException ex) {
				logger.error("Error saving custom category props", ex);
			}
		}
	}
	
	private void updateCategoryFolderSync(int categoryId, Category.Sync sync) {
		synchronized(roots) {
			try {
				CategoryPropSet pset = manager.getCategoryCustomProps(categoryId);
				pset.setSync(sync);
				manager.updateCategoryCustomProps(categoryId, pset);
				
				// Update internal cache
				ShareFolderCategory folder = folders.get(categoryId);
				if (!(folder instanceof MyShareFolderCategory)) {
					folderProps.put(categoryId, pset);
				}
			} catch(WTException ex) {
				logger.error("Error saving custom category props", ex);
			}
		}
	}
	
	private ExtTreeNode createRootNode(boolean chooser, ShareRootCategory root) {
		if(root instanceof MyShareRootCategory) {
			return createRootNode(chooser, root.getShareId(), root.getOwnerProfileId().toString(), root.getPerms().toString(), lookupResource(ContactsLocale.CATEGORIES_MY), false, "wtcon-icon-categoryMy").setExpanded(true);
		} else {
			return createRootNode(chooser, root.getShareId(), root.getOwnerProfileId().toString(), root.getPerms().toString(), root.getDescription(), false, "wtcon-icon-categoryIncoming");
		}
	}
	
	private ExtTreeNode createRootNode(boolean chooser, String id, String pid, String rights, String text, boolean leaf, String iconClass) {
		boolean visible = checkedRoots.contains(id);
		ExtTreeNode node = new ExtTreeNode(id, text, leaf);
		node.put("_type", JsFolderNode.TYPE_ROOT);
		node.put("_pid", pid);
		node.put("_rrights", rights);
		node.put("_visible", visible);
		
		node.setIconClass(iconClass);
		if (!chooser) node.setChecked(visible);
		return node;
	}
	
	private ExtTreeNode createFolderNode(boolean chooser, ShareFolderCategory folder, CategoryPropSet folderProps, SharePermsRoot rootPerms) {
		Category cat = folder.getCategory();
		String id = new CompositeId().setTokens(folder.getShareId(), cat.getCategoryId()).toString();
		String color = cat.getColor();
		Category.Sync sync = Category.Sync.OFF;
		boolean visible = checkedFolders.contains(cat.getCategoryId());
		
		if (folderProps != null) { // Props are not null only for incoming folders
			if (folderProps.getHiddenOrDefault(false)) return null;
			color = folderProps.getColorOrDefault(color);
			sync = folderProps.getSyncOrDefault(sync);
		} else {
			sync = cat.getSync();
		}
		
		ExtTreeNode node = new ExtTreeNode(id, cat.getName(), true);
		node.put("_type", JsFolderNode.TYPE_FOLDER);
		node.put("_pid", cat.getProfileId().toString());
		node.put("_rrights", rootPerms.toString());
		node.put("_frights", folder.getPerms().toString());
		node.put("_erights", folder.getElementsPerms().toString());
		node.put("_catId", cat.getCategoryId());
		node.put("_builtIn", cat.getBuiltIn());
		node.put("_provider", EnumUtils.toSerializedName(cat.getProvider()));
		node.put("_color", Category.getHexColor(color));
		node.put("_sync", EnumUtils.toSerializedName(sync));
		node.put("_default", cat.getIsDefault());
		node.put("_visible", visible);
		
		List<String> classes = new ArrayList<>();
		if (cat.getIsDefault()) classes.add("wt-tree-node-bold");
		if (!folder.getElementsPerms().implies("CREATE") 
				&& !folder.getElementsPerms().implies("UPDATE")
				&& !folder.getElementsPerms().implies("DELETE")) classes.add("wt-tree-node-grey");
		node.setCls(StringUtils.join(classes, " "));
		
		node.setIconClass("wt-palette-" + Category.getHexColor(color));
		if (!chooser) node.setChecked(visible);
		
		return node;
	}
	
	private class SyncRemoteCategoryAA extends BaseServiceAsyncAction {
		private final int categoryId;
		private final String categoryName;
		private final boolean full;
		
		public SyncRemoteCategoryAA(int categoryId, String categoryName, boolean full) {
			super();
			setName(this.getClass().getSimpleName());
			this.categoryId = categoryId;
			this.categoryName = categoryName;
			this.full = full;
		}

		@Override
		public void executeAction() {
			getWts().notify(new RemoteSyncResult(true)
				.setCategoryId(categoryId)
				.setCategoryName(categoryName)
				.setSuccess(true)
			);
			try {
				manager.syncRemoteCategory(categoryId, full);
				this.completed();
				getWts().notify(new RemoteSyncResult(false)
					.setCategoryId(categoryId)
					.setCategoryName(categoryName)
					.setSuccess(true)
				);
			
			} catch(ConcurrentSyncException ex) {
				logger.debug("SyncRemoteCategoryAA already running", ex);
				//TODO: add localized message for this well known situation
				getWts().notify(new RemoteSyncResult(false)
					.setCategoryId(categoryId)
					.setCategoryName(categoryName)
					.setThrowable(ex, true)
				);
			} catch(Throwable t) {
				logger.error("SyncRemoteCategoryAA failure", t);
				getWts().notify(new RemoteSyncResult(false)
					.setCategoryId(categoryId)
					.setCategoryName(categoryName)
					.setThrowable(t, true)
				);
			} finally {
				syncRemoteCategoryAAs.remove(categoryId);
				/*
				synchronized(syncRemoteCategoryAAs) {
					syncRemoteCategoryAAs.remove(categoryId);
				}
				*/
			}
		}
	}
	
	private static class CategoryProbeException extends WTException {
		public CategoryProbeException(String message, Object... arguments) {
			super(message, arguments);
		}
	}
	
	/*
	public void processUploadStreamVCardImport(HttpServletRequest request, InputStream uploadStream) throws Exception {
		Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
		manager.importVCard(categoryId, uploadStream);
	}
	*/
	/*
	public void processImportVCard(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		FileInputStream fis = null;
		
		try {
			Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			
			UploadedFile upl = getUploadedFile(uploadId);
			if(upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			
			fis = new FileInputStream(new File(WT.getTempFolder(), upl.id));
			manager.importVCard(categoryId, fis, "append");
			
			new JsonResult().printTo(out);
			
		} catch(Exception ex) {
			logger.error("Error in action ImportVCard", ex);
			new JsonResult(false, ex.getMessage()).printTo(out);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}
	*/
}
