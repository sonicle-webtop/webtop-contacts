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
import com.sonicle.commons.URIUtils;
import com.sonicle.webtop.contacts.io.input.MemoryContactTextFileReader;
import com.sonicle.commons.time.DateTimeUtils;
import com.sonicle.commons.web.Crud;
import com.sonicle.commons.web.ServletUtils;
import com.sonicle.commons.web.ServletUtils.IntegerArray;
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
import com.sonicle.commons.web.json.extjs.SortMeta;
import com.sonicle.webtop.contacts.ContactsUserSettings.CheckedFolders;
import com.sonicle.webtop.contacts.ContactsUserSettings.CheckedRoots;
import com.sonicle.webtop.contacts.bol.VContact;
import com.sonicle.webtop.contacts.bol.js.JsContact;
import com.sonicle.webtop.contacts.bol.js.JsCategory;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLkp;
import com.sonicle.webtop.contacts.bol.js.JsContactsList;
import com.sonicle.webtop.contacts.bol.js.JsFolderNode;
import com.sonicle.webtop.contacts.bol.js.JsSharing;
import com.sonicle.webtop.contacts.bol.js.ListFieldMapping;
import com.sonicle.webtop.contacts.bol.model.CategoryFolderData;
import com.sonicle.webtop.contacts.model.CategoryFolder;
import com.sonicle.webtop.contacts.model.CategoryRoot;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.model.ContactsList;
import com.sonicle.webtop.contacts.bol.model.MyCategoryFolder;
import com.sonicle.webtop.contacts.bol.model.MyCategoryRoot;
import com.sonicle.webtop.contacts.bol.model.RBAddressbook;
import com.sonicle.webtop.contacts.bol.model.RBContactDetail;
import com.sonicle.webtop.contacts.bol.model.SetupDataCategoryRemote;
import com.sonicle.webtop.contacts.io.input.ContactExcelFileReader;
import com.sonicle.webtop.contacts.io.input.ContactTextFileReader;
import com.sonicle.webtop.contacts.io.input.ContactVCardFileReader;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.contacts.model.FolderContacts;
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
import com.sonicle.webtop.core.sdk.AsyncActionCollection;
import com.sonicle.webtop.core.sdk.BaseService;
import com.sonicle.webtop.core.sdk.BaseServiceAsyncAction;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.util.LogEntries;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormatter;
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
	
	public static final String EVENTS_EXPORT_FILENAME = "events_{0}-{1}-{2}.{3}";
	
	private final LinkedHashMap<String, CategoryRoot> roots = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, CategoryFolder> folders = new LinkedHashMap<>();
	private final HashMap<String, ArrayList<CategoryFolder>> foldersByRoot = new HashMap<>();
	private final HashMap<Integer, CategoryRoot> rootByFolder = new HashMap<>();
	private final AsyncActionCollection<Integer, SyncRemoteCategoryAA> syncRemoteCategoryAAs = new AsyncActionCollection<>();
	
	private CheckedRoots checkedRoots = null;
	private CheckedFolders checkedFolders = null;
	private ArrayList<FieldMeta> gridFieldsW = null;
	private ArrayList<FieldMeta> gridFieldsH = null;
	private ArrayList<GridColumnMeta> gridColsInfoW = null;
	private ArrayList<GridColumnMeta> gridColsInfoH = null;
	private final Object gridLock = new Object();
	
	//private ExportWizard wizard = null;
	
	@Override
	public void initialize() throws Exception {
		UserProfile up = getEnv().getProfile();
		manager = (ContactsManager)WT.getServiceManager(SERVICE_ID);
		ss = new ContactsServiceSettings(SERVICE_ID, up.getDomainId());
		us = new ContactsUserSettings(SERVICE_ID, up.getId());
		initFolders();
		gridFieldsW = buildFields(WORK_VIEW);
		gridFieldsH = buildFields(HOME_VIEW);
		gridColsInfoW = buildColsInfo(WORK_VIEW);
		gridColsInfoH = buildColsInfo(HOME_VIEW);
	}
	
	@Override
	public void cleanup() throws Exception {
		checkedFolders.clear();
		checkedFolders = null;
		checkedRoots.clear();
		checkedRoots = null;
		rootByFolder.clear();
		foldersByRoot.clear();
		folders.clear();
		roots.clear();
		us = null;
		ss = null;
		manager = null;
	}
	
	@Override
	public ServiceVars returnServiceVars() {
		ServiceVars co = new ServiceVars();
		co.put("defaultCategorySync", EnumUtils.toSerializedName(ss.getDefaultCategorySync()));
		co.put("view", us.getView());
		return co;
	}
	
	private WebTopSession getWts() {
		return getEnv().getWebTopSession();
	}
	
	private void runSyncRemoteCategory(int categoryId, String categoryName, boolean full) throws WTException {
		synchronized(syncRemoteCategoryAAs) {
			if (syncRemoteCategoryAAs.containsKey(categoryId)) {
				logger.debug("SyncRemoteCategory run skipped [{}]", categoryId);
				return;
			}
			final SyncRemoteCategoryAA aa = new SyncRemoteCategoryAA(categoryId, categoryName, full);
			syncRemoteCategoryAAs.put(categoryId, aa);
			aa.start(RunContext.getSubject(), RunContext.getRunProfileId());
		}
	}
	
	private void initFolders() throws WTException {
		synchronized(roots) {
			updateRootFoldersCache();
			updateFoldersCache();
			
			checkedRoots = us.getCheckedCategoryRoots();
			// If empty, adds MyNode checked by default!
			if(checkedRoots.isEmpty()) {
				checkedRoots.add(MyCategoryRoot.SHARE_ID);
				us.setCheckedCategoryRoots(checkedRoots);
			}
			checkedFolders = us.getCheckedCategoryFolders();
		}
	}
	
	private void updateRootFoldersCache() throws WTException {
		UserProfileId pid = getEnv().getProfile().getId();
		synchronized(roots) {
			roots.clear();
			roots.put(MyCategoryRoot.SHARE_ID, new MyCategoryRoot(pid));
			for(CategoryRoot root : manager.listIncomingCategoryRoots()) {
				roots.put(root.getShareId(), root);
			}
		}
	}
	
	private void updateFoldersCache() throws WTException {
		synchronized(roots) {
			foldersByRoot.clear();
			folders.clear();
			rootByFolder.clear();
			for(CategoryRoot root : roots.values()) {
				foldersByRoot.put(root.getShareId(), new ArrayList<CategoryFolder>());
				if(root instanceof MyCategoryRoot) {
					for(Category cat : manager.listCategories()) {
						final MyCategoryFolder fold = new MyCategoryFolder(root.getShareId(), cat);
						foldersByRoot.get(root.getShareId()).add(fold);
						folders.put(cat.getCategoryId(), fold);
						rootByFolder.put(cat.getCategoryId(), root);
					}
				} else {
					for(CategoryFolder fold : manager.listIncomingCategoryFolders(root.getShareId()).values()) {
						final int catId = fold.getCategory().getCategoryId();
						fold.setData(us.getCategoryFolderData(catId));
						foldersByRoot.get(root.getShareId()).add(fold);
						folders.put(catId, fold);
						rootByFolder.put(catId, root);
					}
				}
			}
		}
	}
	
	/*
	private void initFolders() throws Exception {
		UserProfileId pid = getEnv().getProfile().getId();
		synchronized(roots) {
			roots.clear();
			foldersByRoot.clear();
			folders.clear();
			
			roots.put(MyCategoryRoot.SHARE_ID, new MyCategoryRoot(pid));
			foldersByRoot.put(MyCategoryRoot.SHARE_ID, new ArrayList<CategoryFolder>());
			for(OCategory cat : manager.listCategories()) {
				MyCategoryFolder fold = new MyCategoryFolder(MyCategoryRoot.SHARE_ID, cat);
				foldersByRoot.get(MyCategoryRoot.SHARE_ID).add(fold);
				folders.put(cat.getCategoryId(), fold);
			}
			for(CategoryRoot root : manager.listIncomingCategoryRoots()) {
				roots.put(root.getShareId(), root);
				foldersByRoot.put(root.getShareId(), new ArrayList<CategoryFolder>());
				for(CategoryFolder fold : manager.listIncomingCategoryFolders(root.getShareId()).values()) {
					foldersByRoot.get(root.getShareId()).add(fold);
					folders.put(fold.getCategory().getCategoryId(), fold);
				}
			}

			checkedRoots = us.getCheckedCategoryRoots();
			// If empty, adds MyNode checked by default!
			if(checkedRoots.isEmpty()) {
				checkedRoots.add(MyCategoryRoot.SHARE_ID);
				us.setCheckedCategoryRoots(checkedRoots);
			}
			checkedFolders = us.getCheckedCategoryFolders();
		}
	}
	*/
	
	public void processManageFoldersTree(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<ExtTreeNode> children = new ArrayList<>();
		ExtTreeNode child = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				String node = ServletUtils.getStringParameter(request, "node", true);
				
				if (node.equals("root")) { // Node: root -> list roots
					for(CategoryRoot root : roots.values()) {
						children.add(createRootNode(root));
					}
				} else { // Node: folder -> list folders (categories)
					CategoryRoot root = roots.get(node);
					
					if (root instanceof MyCategoryRoot) {
						for (Category cal : manager.listCategories()) {
							MyCategoryFolder folder = new MyCategoryFolder(node, cal);
							children.add(createFolderNode(folder, root.getPerms()));
						}
					} else {
						if (foldersByRoot.containsKey(root.getShareId())) {
							for (CategoryFolder fold : foldersByRoot.get(root.getShareId())) {
								final ExtTreeNode etn = createFolderNode(fold, root.getPerms());
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
				for(CategoryFolder fold : foldersByRoot.get(rootId)) {
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
				for (CategoryRoot root : roots.values()) {
					if (root instanceof MyCategoryRoot) {
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
				for (CategoryRoot root : roots.values()) {
					if (foldersByRoot.containsKey(root.getShareId())) {
						for (CategoryFolder fold : foldersByRoot.get(root.getShareId())) {
							if (!fold.getElementsPerms().implies("CREATE")) continue;
							items.add(new JsCategoryLkp(fold));
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
	
	public void processManageHiddenCategories(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String rootId = ServletUtils.getStringParameter(request, "rootId", true);
				if (rootId.equals(MyCategoryRoot.SHARE_ID)) throw new WTException();
				
				ArrayList<JsSimple> items = new ArrayList<>();
				synchronized(roots) {
					for(CategoryFolder fold : foldersByRoot.get(rootId)) {
						CategoryFolderData data = (CategoryFolderData)fold.getData();
						if (data != null) {
							if ((data.hidden != null) && data.hidden) {
								items.add(new JsSimple(fold.getCategory().getCategoryId(), fold.getCategory().getName()));
							}
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
						CategoryFolder fold = folders.get(categoryId);
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
	
	public void processManageCategory(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		Category item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				int id = ServletUtils.getIntParameter(request, "id", true);
				
				item = manager.getCategory(id);
				new JsonResult(new JsCategory(item)).printTo(out);
				
			} else if (crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				item = manager.addCategory(JsCategory.createCategory(pl.data, null));
				updateFoldersCache();
				toggleCheckedFolder(item.getCategoryId(), true);
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				item = manager.getCategory(pl.data.categoryId);
				if (item == null) throw new WTException("Category not found [{0}]", pl.data.categoryId);
				manager.updateCategory(JsCategory.createCategory(pl.data, item.getParameters()));
				updateFoldersCache();
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.DELETE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				manager.deleteCategory(pl.data.categoryId);
				updateFoldersCache();
				toggleCheckedFolder(pl.data.categoryId, false);
				new JsonResult().printTo(out);
				
			} else if (crud.equals("sync")) {
				int id = ServletUtils.getIntParameter(request, "id", true);
				boolean full = ServletUtils.getBooleanParameter(request, "full", false);
				
				item = manager.getCategory(id);
				if (item == null) throw new WTException("Category not found [{0}]", id);
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
				if (result == null) throw new WTException("URL problem");
				
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
				
				wts.hasPropertyOrThrow(SERVICE_ID, PROPERTY_PREFIX+tag);
				SetupDataCategoryRemote setup = (SetupDataCategoryRemote) wts.getProperty(SERVICE_ID, PROPERTY_PREFIX+tag);
				setup.setName(name);
				setup.setColor(color);
				
				Category cal = manager.addCategory(setup.toCategory());
				wts.clearProperty(SERVICE_ID, PROPERTY_PREFIX+tag);
				updateFoldersCache();
				toggleCheckedFolder(cal.getCategoryId(), true);
				runSyncRemoteCategory(cal.getCategoryId(), cal.getName(), true); // Starts a full-sync
				
				new JsonResult().printTo(out);
			}
			
		} catch (Exception ex) {
			logger.error("Error in SetupCalendarRemote", ex);
			new JsonResult(false, ex.getMessage()).printTo(out);
		}
	}
	
	public void processSetCategoryColor(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			String color = ServletUtils.getStringParameter(request, "color", null);

			updateCategoryFolderColor(id, color);
			new JsonResult().printTo(out);
			
		} catch(Exception ex) {
			new JsonResult(ex).printTo(out);
		}
	}
	
	private ArrayList<FieldMeta> buildFields(String view) {
		ArrayList<FieldMeta> fields = new ArrayList<>();
		
		fields.add(new FieldMeta("uid").setType("string"));
		fields.add(new FieldMeta("contactId").setType("int"));
		fields.add(new FieldMeta("categoryId"));
		fields.add(new FieldMeta("categoryName"));
		fields.add(new FieldMeta("categoryColor"));
		fields.add(new FieldMeta("isList").setType("boolean"));
		if(view.equals(WORK_VIEW)) fields.add(new FieldMeta("title").setType("string"));
		fields.add(new FieldMeta("firstName").setType("string"));
		fields.add(new FieldMeta("lastName").setType("string"));
		if(view.equals(HOME_VIEW)) fields.add(new FieldMeta("nickname").setType("string"));
		if(view.equals(WORK_VIEW)) {
			fields.add(new FieldMeta("company").setType("string"));
			fields.add(new FieldMeta("function").setType("string"));
			//fields.add(new ExtFieldMeta("workAddress").setType("string"));
			//fields.add(new ExtFieldMeta("workCity").setType("string"));
			fields.add(new FieldMeta("workTelephone").setType("string"));
			fields.add(new FieldMeta("workMobile").setType("string"));
			fields.add(new FieldMeta("workEmail").setType("string"));
		}
		if(view.equals(HOME_VIEW)) {
			fields.add(new FieldMeta("homeTelephone").setType("string"));
			fields.add(new FieldMeta("homeEmail").setType("string"));
			fields.add(new FieldMeta("birthday").setType("date").set("dateFormat", "Y-m-d"));
		}
		
		fields.add(new FieldMeta("_profileId"));
		fields.add(new FieldMeta("_frights"));
		fields.add(new FieldMeta("_erights"));
		
		return fields;
	}
	
	private ArrayList<GridColumnMeta> buildColsInfo(String view) {
		ArrayList<GridColumnMeta> colsInfo = new ArrayList<>();
		
		colsInfo.add(new GridColumnMeta("contactId").setHidden(true).setGroupable(false));
		//colsInfo.add(new GridColumnMeta("categoryId").setHidden(true));
		colsInfo.add(new GridColumnMeta("categoryName").setGroupable(false));
		if(view.equals(WORK_VIEW)) colsInfo.add(new GridColumnMeta("title").setGroupable(false));
		colsInfo.add(new GridColumnMeta("firstName").setGroupable(true));
		colsInfo.add(new GridColumnMeta("lastName").setGroupable(true));
		if(view.equals(HOME_VIEW)) colsInfo.add(new GridColumnMeta("nickname").setGroupable(false));
		if(view.equals(WORK_VIEW)) {
			colsInfo.add(new GridColumnMeta("company").setGroupable(true));
			colsInfo.add(new GridColumnMeta("function").setGroupable(false));
			//colsInfo.add(new GridColumnMeta("workAddress"));
			//colsInfo.add(new GridColumnMeta("workCity"));
			colsInfo.add(new GridColumnMeta("workTelephone").setGroupable(true));
			colsInfo.add(new GridColumnMeta("workMobile").setGroupable(false));
			colsInfo.add(new GridColumnMeta("workEmail").setGroupable(true));
		}
		if(view.equals(HOME_VIEW)) {
			colsInfo.add(new GridColumnMeta("homeTelephone").setGroupable(true));
			colsInfo.add(new GridColumnMeta("homeEmail").setGroupable(true));
			colsInfo.add(new GridColumnMeta("birthday").setXType("datecolumn").setGroupable(false));
		}
		
		return colsInfo;
	}
	
	private String[] buildQueryParameters(String view, String letter, String query) {
		String searchMode = null, pattern = null;
		if (!StringUtils.isEmpty(letter)) {
			searchMode = "lastname";
			if (letter.equals("*")) {
				pattern = "^.*";
			} else if (letter.equals("#")) {
				pattern = "^[0-9]";
			} else {
				pattern = "^[" + letter.toLowerCase() + "]";
			}
		} else if (query != null) {
			searchMode = (view.equals(WORK_VIEW)) ? "work" : "home";
			pattern = "%" + query.toLowerCase() + "%";
		} else {
			searchMode = "lastname";
			pattern = "^[a]";
		}
		return new String[]{searchMode, pattern};
	}
	
	public void processManageGridContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		DateTimeFormatter ymdFmt = DateTimeUtils.createYmdFormatter();
		ArrayList<MapItem> items = new ArrayList<>();
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String view = ServletUtils.getStringParameter(request, "view", WORK_VIEW);
				String letter = ServletUtils.getStringParameter(request, "letter", null);
				String query = ServletUtils.getStringParameter(request, "query", null);
				
				String[] parameters = buildQueryParameters(view, letter, query);
				String searchMode = parameters[0], pattern = parameters[1];
				
				List<Integer> visibleCategoryIds = getVisibleFolderIds(true);
				List<FolderContacts> foContactsObjs = manager.listFolderContacts(visibleCategoryIds, searchMode, pattern);
				for (FolderContacts foContactsObj : foContactsObjs) {
					final CategoryFolder fold = folders.get(foContactsObj.folder.getCategoryId());
					if (fold == null) continue;
					
					for (ContactEx ce : foContactsObj.contacts) {
						final MapItem item = new MapItem();
						item.put("id", Contact.buildUid(ce.getContactId()));
						item.put("contactId", ce.getContactId());
						item.put("isList", ce.getIsList());
						if(view.equals(WORK_VIEW)) item.put("title", ce.getTitle());
						item.put("firstName", ce.getFirstName());
						item.put("lastName", ce.getLastName());
						if(view.equals(HOME_VIEW)) item.put("nickname", ce.getNickname());
						if(view.equals(WORK_VIEW)) {
							item.put("company", StringUtils.defaultIfEmpty(ce.getCompanyAsMasterDataId(), ce.getCompany()));
							item.put("function", ce.getFunction());
							//item.put("workAddress", vc.getCaddress());
							//item.put("workCity", vc.getCcity());
							item.put("workTelephone", ce.getWorkTelephone());
							item.put("workMobile", ce.getWorkMobile());
							item.put("workEmail", ce.getWorkEmail());
						}
						if(view.equals(HOME_VIEW)) {
							item.put("homeTelephone", ce.getHomeTelephone());
							item.put("homeEmail", ce.getHomeEmail());
							item.put("birthday", (ce.getBirthday() != null) ? ymdFmt.print(ce.getBirthday()) : null);
						}
						item.put("categoryId", foContactsObj.folder.getCategoryId());
						item.put("categoryName", foContactsObj.folder.getName());
						item.put("categoryColor", foContactsObj.folder.getColor());
						if (fold.getData() != null) {
							CategoryFolderData data = (CategoryFolderData)fold.getData();
							if (!StringUtils.isBlank(data.color)) item.put("categoryColor", data.color);
						}
						item.put("_profileId", new UserProfileId(foContactsObj.folder.getDomainId(), foContactsObj.folder.getUserId()).toString());
						item.put("_frights", fold.getPerms().toString());
						item.put("_erights", fold.getElementsPerms().toString());
						items.add(item);
					}
				}
				
				GridMetadata meta = new GridMetadata(true);
				meta.setFields(view.equals(WORK_VIEW) ? gridFieldsW : gridFieldsH);
				meta.setColumnsInfo(view.equals(WORK_VIEW) ? gridColsInfoW : gridColsInfoH);
				
				GroupMeta gm = getGridContactsGroupInfo(view);
				if(gm != null) meta.setGroupInfo(gm);
				SortMeta sm = getGridContactsSortInfo(view);
				if(gm != null) meta.setSortInfo(sm);
				
				new JsonResult(items, meta, items.size()).printTo(out);
				
			} /*else if(crud.equals(Crud.DELETE)) {
				PayloadAsList<JsGridContactList> pl = ServletUtils.getPayloadAsList(request, JsGridContactList.class);
				
				for(JsGridContact row : pl.data) {
					logger.debug("deleting {} {}", row.id, row.contactId);
				}
				new JsonResult().printTo(out);
				
			}*/ else if(crud.equals(Crud.SAVE)) {
				String view = ServletUtils.getStringParameter(request, "view", true);
				String context = ServletUtils.getStringParameter(request, "context", true);
				String field = ServletUtils.getStringParameter(request, "field", null);
				String direction = ServletUtils.getStringParameter(request, "direction", null);
				
				if(context.equals("group")) {
					setGridContactsGroupInfo(view, field, direction);
				} else if(context.equals("sort")) {
					setGridContactsSortInfo(view, field, direction);
				}
				new JsonResult().printTo(out);
			}
		
		} catch(Exception ex) {
			logger.error("Error in action ManageGridContacts", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	private void setGridContactsGroupInfo(String view, String field, String direction) {
		synchronized(gridLock) {
			GroupMeta meta = null;
			if(!StringUtils.isEmpty(field) && !StringUtils.isEmpty(direction)) {
				meta = new GroupMeta(field, direction);
			} else if(!StringUtils.isEmpty(field)) {
				meta = new GroupMeta(field);
			}
			us.setGridContactsGroupInfo(view, meta);
		}
	}
	
	private GroupMeta getGridContactsGroupInfo(String view) {
		synchronized(gridLock) {
			return us.getGridContactsGroupInfo(view);
		}
	}
	
	private void setGridContactsSortInfo(String view, String field, String direction) {
		synchronized(gridLock) {
			SortMeta meta = null;
			if(!StringUtils.isEmpty(field) && !StringUtils.isEmpty(direction)) {
				meta = new SortMeta(field, direction);
			} else if(!StringUtils.isEmpty(field)) {
				meta = new SortMeta(field);
			}
			us.setGridContactsSortInfo(view, meta);
		}
	}
	
	private SortMeta getGridContactsSortInfo(String view) {
		synchronized(gridLock) {
			return us.getGridContactsSortInfo(view);
		}
	}
	
	public void processManageContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		JsContact item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				
				int contactId = Integer.parseInt(id);
				Contact contact = manager.getContact(contactId);
				UserProfileId ownerId = manager.getCategoryOwner(contact.getCategoryId());
				item = new JsContact(ownerId, contact);
				
				new JsonResult(item).printTo(out);
				
			} else if(crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsContact> pl = ServletUtils.getPayload(request, JsContact.class);
				
				Contact contact = JsContact.buildContact(pl.data);
				ContactPicture picture = null;
				if(contact.getHasPicture() && hasUploadedFile(pl.data.picture)) {
					picture = getUploadedContactPicture(pl.data.picture);
				}
				
				if(picture != null) {
					manager.addContact(contact, picture);
				} else {
					manager.addContact(contact);
				}
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsContact> pl = ServletUtils.getPayload(request, JsContact.class);
				
				Contact contact = JsContact.buildContact(pl.data);
				ContactPicture picture = null;
				if(contact.getHasPicture() && hasUploadedFile(pl.data.picture)) {
					picture = getUploadedContactPicture(pl.data.picture);
				}
				
				if(picture != null) {
					manager.updateContact(contact, picture);
				} else {
					manager.updateContact(contact);
				}
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				IntegerArray ids = ServletUtils.getObjectParameter(request, "ids", IntegerArray.class, true);
				
				if (ids.size() == 1) {
					manager.deleteContact(ids.get(0));
				} else {
					manager.deleteContact(ids);
				}
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.MOVE)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				Integer categoryId = ServletUtils.getIntParameter(request, "targetCategoryId", true);
				boolean copy = ServletUtils.getBooleanParameter(request, "copy", false);
				
				int contactId = Integer.parseInt(id);
				manager.moveContact(copy, contactId, categoryId);
				
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ManageContacts", ex);
			new JsonResult(false, "Error").printTo(out);	
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
				
			} else if(crud.equals(Crud.MOVE)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				Integer categoryId = ServletUtils.getIntParameter(request, "targetCategoryId", true);
				boolean copy = ServletUtils.getBooleanParameter(request, "copy", false);
				
				int contactId = Integer.parseInt(id);
				manager.moveContactsList(copy, contactId, categoryId);
				
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in ManageContactsLists", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	public void processGetContactPicture(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			String id = ServletUtils.getStringParameter(request, "id", true);
			
			ContactPicture picture = null;
			if(hasUploadedFile(id)) {
				picture = getUploadedContactPicture(id);
			} else {
				int contactId = Integer.parseInt(id);
				picture = manager.getContactPicture(contactId);
			}
			
			if(picture != null) {
				try(ByteArrayInputStream bais = new ByteArrayInputStream(picture.getBytes())) {
					ServletUtils.writeContent(response, bais, picture.getMediaType());
				}
			}
			
			
			/*
			File photoFile = null;
			
			if(hasUploadedFile(id)) {
				UploadedFile uploaded = getUploadedFile(id);
				if(uploaded == null) throw new Exception();
				File tempFolder = WT.getTempFolder();
				photoFile = new File(tempFolder, uploaded.id);
				
				try(FileInputStream fis = new FileInputStream(photoFile)) {
					ServletUtils.writeContent(response, fis, "");
					//ServletUtils.writeInputStream(response, fis);
				}
				
			} else {
				ContactPicture pic = manager.getContactPicture(id);
				try(ByteArrayInputStream bais = new ByteArrayInputStream(pic.getBytes())) {
					ServletUtils.writeContent(response, bais, pic.getMimeType());
				}
				
				//byte[] photo = manager.getContactPhoto(id);
				//try(ByteArrayInputStream bais = new ByteArrayInputStream(photo)) {
				//	ServletUtils.writeContent(response, bais, "image/png");
					//ServletUtils.writeInputStream(response, bais);
				//}
			}
			*/
			
			/*
			if(Validator.isInteger(id)) {
				manager.getContactPhoto(id)
				
				//TODO: 
				
				
			} else {
				UploadedFile uploaded = getUploadedFile(id);
				if(uploaded == null) throw new Exception();
				File tempFolder = WT.getTempFolder();
				photoFile = new File(tempFolder, uploaded.id);
			}
			
			try(FileInputStream fis = new FileInputStream(photoFile)) {
				ServletUtils.writeInputStream(response, fis);
			}
			*/
			
		} catch(Exception ex) {
			logger.error("Error in action GetContactPicture", ex);
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
	
	public void processPrintAddressbook(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<RBAddressbook> items = new ArrayList<>();
		
		try {
			String filename = ServletUtils.getStringParameter(request, "filename", "print");
			String view = ServletUtils.getStringParameter(request, "view", WORK_VIEW);
			String letter = ServletUtils.getStringParameter(request, "letter", null);
			String query = ServletUtils.getStringParameter(request, "query", null);
			
			String[] parameters = buildQueryParameters(view, letter, query);
			String searchMode = parameters[0], pattern = parameters[1];
			
			List<Integer> visibleCategoryIds = getVisibleFolderIds(true);
			List<FolderContacts> foContactsObjs = manager.listFolderContacts(visibleCategoryIds, searchMode, pattern);
			for (FolderContacts foContactsObj : foContactsObjs) {
				final CategoryFolder fold = folders.get(foContactsObj.folder.getCategoryId());
				if (fold == null) continue;

				for (ContactEx ce : foContactsObj.contacts) {
					items.add(new RBAddressbook(fold.getCategory(), ce));
				}
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
			
			Contact contact = null;
			ContactPicture picture = null;
			Category category = null;
			for(Integer id : ids) {
				picture = null;
				contact = manager.getContact(id);
				category = manager.getCategory(contact.getCategoryId());
				if(contact.getHasPicture()) picture = manager.getContactPicture(id);
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
	
	private ContactPicture getUploadedContactPicture(String id) throws WTException {
		UploadedFile upl = getUploadedFile(id);
		if(upl == null) throw new WTException("Uploaded file not found [{0}]", id);
		
		ContactPicture pic = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(upl.getFile());
			pic = new ContactPicture("image/png", IOUtils.toByteArray(fis));
		} catch (FileNotFoundException ex) {
			throw new WTException(ex, "File not found {0}");
		} catch (IOException ex) {
			throw new WTException(ex, "Unable to read file {0}");
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return pic;
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
			CategoryRoot root = roots.get(cid.getToken(0));
			if(root instanceof MyCategoryRoot) {
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
	
	private ArrayList<Integer> getVisibleFolderIds(boolean cleanupOrphans) {
		ArrayList<Integer> ids = new ArrayList<>();
		ArrayList<Integer> orphans = new ArrayList<>();
		
		Integer[] checked = getCheckedFolders();
		for (CategoryRoot root : getCheckedRoots()) {
			for (Integer folderId : checked) {
				final CategoryRoot folderRoot = rootByFolder.get(folderId);
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
	
	private List<CategoryRoot> getCheckedRoots() {
		ArrayList<CategoryRoot> checked = new ArrayList<>();
		for(CategoryRoot root : roots.values()) {
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
	
	private void updateCategoryFolderColor(int categoryId, String color) {
		synchronized(roots) {
			CategoryFolderData data = us.getCategoryFolderData(categoryId);
			data.color = color;
			if (!data.isNull()) {
				us.setCategoryFolderData(categoryId, data);
			} else {
				us.clearCategoryFolderData(categoryId);
			}
			
			// Update internal cache
			CategoryFolder folder = folders.get(categoryId);
			if (!(folder instanceof MyCategoryFolder)) {
				((CategoryFolderData)folder.getData()).update(data);
			}
		}
	}
	
	private void updateCategoryFolderVisibility(int categoryId, Boolean hidden) {
		synchronized(roots) {
			CategoryFolderData data = us.getCategoryFolderData(categoryId);
			data.hidden = hidden;
			if (!data.isNull()) {
				us.setCategoryFolderData(categoryId, data);
			} else {
				us.clearCategoryFolderData(categoryId);
			}
			
			// Update internal cache
			CategoryFolder folder = folders.get(categoryId);
			if (!(folder instanceof MyCategoryFolder)) {
				((CategoryFolderData)folder.getData()).update(data);
			}
		}
	}
	
	private ExtTreeNode createRootNode(CategoryRoot root) {
		if(root instanceof MyCategoryRoot) {
			return createRootNode(root.getShareId(), root.getOwnerProfileId().toString(), root.getPerms().toString(), lookupResource(ContactsLocale.CATEGORIES_MY), false, "wtcon-icon-root-my-xs").setExpanded(true);
		} else {
			return createRootNode(root.getShareId(), root.getOwnerProfileId().toString(), root.getPerms().toString(), root.getDescription(), false, "wtcon-icon-root-incoming-xs");
		}
	}
	
	private ExtTreeNode createRootNode(String id, String pid, String rights, String text, boolean leaf, String iconClass) {
		boolean visible = checkedRoots.contains(id);
		ExtTreeNode node = new ExtTreeNode(id, text, leaf);
		node.put("_type", JsFolderNode.TYPE_ROOT);
		node.put("_pid", pid);
		node.put("_rrights", rights);
		node.put("_visible", visible);
		
		node.setIconClass(iconClass);
		node.setChecked(visible);
		return node;
	}
	
	private ExtTreeNode createFolderNode(CategoryFolder folder, SharePermsRoot rootPerms) {
		Category cat = folder.getCategory();
		String id = new CompositeId().setTokens(folder.getShareId(), cat.getCategoryId()).toString();
		String color = cat.getColor();
		boolean visible = checkedFolders.contains(cat.getCategoryId());
		
		if (folder.getData() != null) {
			CategoryFolderData data = (CategoryFolderData)folder.getData();
			if ((data.hidden != null) && data.hidden) return null;
			if (!StringUtils.isBlank(data.color)) color = data.color;
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
		node.put("_default", cat.getIsDefault());
		node.put("_visible", visible);
		
		List<String> classes = new ArrayList<>();
		if (cat.getIsDefault()) classes.add("wt-tree-node-bold");
		if (!folder.getElementsPerms().implies("CREATE") 
				&& !folder.getElementsPerms().implies("UPDATE")
				&& !folder.getElementsPerms().implies("DELETE")) classes.add("wt-tree-node-grey");
		node.setCls(StringUtils.join(classes, " "));
		
		node.setIconClass("wt-palette-" + Category.getHexColor(color));
		node.setChecked(visible);
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
				
			} catch(Throwable t) {
				logger.error("Remote sync failure", t);
				getWts().notify(new RemoteSyncResult(false)
					.setCategoryId(categoryId)
					.setCategoryName(categoryName)
					.setThrowable(t, true)
				);
			} finally {
				synchronized(syncRemoteCategoryAAs) {
					syncRemoteCategoryAAs.remove(categoryId);
				}
			}
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
