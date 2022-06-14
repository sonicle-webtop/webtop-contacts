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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sonicle.commons.AlgoUtils.MD5HashBuilder;
import com.sonicle.commons.BitFlag;
import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.InternetAddressUtils;
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.PathUtils;
import com.sonicle.commons.URIUtils;
import com.sonicle.commons.cache.AbstractPassiveExpiringBulkMap;
import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.web.Crud;
import com.sonicle.commons.web.ParameterException;
import com.sonicle.commons.web.ServletUtils;
import com.sonicle.commons.web.ServletUtils.IntegerArray;
import com.sonicle.commons.web.ServletUtils.StringArray;
import com.sonicle.commons.web.json.CompositeId;
import com.sonicle.commons.web.json.PayloadAsList;
import com.sonicle.commons.web.json.JsonResult;
import com.sonicle.commons.web.json.MapItem;
import com.sonicle.commons.web.json.Payload;
import com.sonicle.commons.web.json.bean.IntegerSet;
import com.sonicle.commons.web.json.bean.QueryObj;
import com.sonicle.commons.web.json.bean.StringSet;
import com.sonicle.commons.web.json.extjs.GridMetadata;
import com.sonicle.commons.web.json.extjs.ExtTreeNode;
import com.sonicle.commons.web.json.extjs.GroupMeta;
import com.sonicle.webtop.contacts.IContactsManager.ContactGetOptions;
import com.sonicle.webtop.contacts.IContactsManager.ContactUpdateOptions;
import com.sonicle.webtop.contacts.bol.js.JsContact;
import com.sonicle.webtop.contacts.bol.js.JsCategory;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLinks;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLkp;
import com.sonicle.webtop.contacts.bol.js.JsContactLkp;
import com.sonicle.webtop.contacts.bol.js.JsContactPreview;
import com.sonicle.webtop.contacts.bol.js.JsContactsList;
import com.sonicle.webtop.contacts.bol.js.JsEventContact;
import com.sonicle.webtop.contacts.bol.js.JsFolderNode;
import com.sonicle.webtop.contacts.bol.js.JsGridContact;
import com.sonicle.webtop.contacts.bol.js.JsMailchimpUserSettings;
import com.sonicle.webtop.contacts.bol.js.JsSharing;
import com.sonicle.webtop.contacts.bol.js.ListFieldMapping;
import com.sonicle.webtop.contacts.model.ShareFolderCategory;
import com.sonicle.webtop.contacts.model.ShareRootCategory;
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
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.api.ListsApi;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberList3;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactAttachment;
import com.sonicle.webtop.contacts.model.ContactAttachmentWithBytes;
import com.sonicle.webtop.contacts.model.ContactAttachmentWithStream;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.contacts.model.ContactList;
import com.sonicle.webtop.contacts.model.ContactListEx;
import com.sonicle.webtop.contacts.model.ContactLookup;
import com.sonicle.webtop.contacts.model.ContactObjectWithBean;
import com.sonicle.webtop.contacts.model.ContactPictureWithBytes;
import com.sonicle.webtop.contacts.model.ContactQuery;
import com.sonicle.webtop.contacts.model.ContactType;
import com.sonicle.webtop.contacts.model.ContactListRecipient;
import com.sonicle.webtop.contacts.model.ListContactsResult;
import com.sonicle.webtop.contacts.model.Grouping;
import com.sonicle.webtop.contacts.model.ShowBy;
import com.sonicle.webtop.contacts.msg.ContactImportLogSM;
import com.sonicle.webtop.contacts.msg.RemoteSyncResult;
import com.sonicle.webtop.contacts.rpt.RptAddressbook;
import com.sonicle.webtop.contacts.rpt.RptContactsDetail;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.CoreUserSettings;
import com.sonicle.webtop.core.app.CoreManifest;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.WebTopSession;
import com.sonicle.webtop.core.app.WebTopSession.UploadedFile;
import com.sonicle.webtop.core.app.io.input.FileRowsReader;
import com.sonicle.webtop.core.app.util.log.LogEntry;
import com.sonicle.webtop.core.app.util.log.LogHandler;
import com.sonicle.webtop.core.bol.js.ObjCustomFieldDefs;
import com.sonicle.webtop.core.bol.js.JsCustomFieldDefsData;
import com.sonicle.webtop.core.bol.js.JsSimple;
import com.sonicle.webtop.core.bol.js.JsValue;
import com.sonicle.webtop.core.bol.js.JsWizardData;
import com.sonicle.webtop.core.model.SharePermsRoot;
import com.sonicle.webtop.core.bol.model.Sharing;
import com.sonicle.webtop.core.io.output.AbstractReport;
import com.sonicle.webtop.core.io.output.ReportConfig;
import com.sonicle.webtop.core.model.CustomField;
import com.sonicle.webtop.core.model.CustomFieldEx;
import com.sonicle.webtop.core.model.CustomFieldValue;
import com.sonicle.webtop.core.model.CustomPanel;
import com.sonicle.webtop.core.model.Recipient;
import com.sonicle.webtop.core.sdk.AsyncActionCollection;
import com.sonicle.webtop.core.sdk.BaseService;
import com.sonicle.webtop.core.sdk.BaseServiceAsyncAction;
import com.sonicle.webtop.core.sdk.ServiceMessage;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.UserProfileId;
import com.sonicle.webtop.core.sdk.WTException;
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
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import jakarta.mail.internet.InternetAddress;
import java.util.Collection;
import java.util.StringJoiner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
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
	
	private final SearchableCustomFieldTypeCache cacheSearchableCustomFieldType = new SearchableCustomFieldTypeCache(5, TimeUnit.SECONDS);
	private final LinkedHashMap<String, ShareRootCategory> roots = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, ShareFolderCategory> folders = new LinkedHashMap<>();
	private final HashMap<Integer, CategoryPropSet> folderProps = new HashMap<>();
	private final HashMap<String, ArrayList<ShareFolderCategory>> foldersByRoot = new HashMap<>();
	private final HashMap<Integer, ShareRootCategory> rootByFolder = new HashMap<>();
	private final AsyncActionCollection<Integer, SyncRemoteCategoryAA> syncRemoteCategoryAAs = new AsyncActionCollection<>();
	
	private StringSet inactiveRoots = null;
	private IntegerSet inactiveFolders = null;
	//private Map<String, CustomFieldEx> previewableCustomFields;
	private final Object gridLock = new Object();
	
	//private ExportWizard wizard = null;
	
	@Override
	public void initialize() throws Exception {
		UserProfile up = getEnv().getProfile();
		manager = (ContactsManager)WT.getServiceManager(SERVICE_ID);
		ss = new ContactsServiceSettings(SERVICE_ID, up.getDomainId());
		us = new ContactsUserSettings(SERVICE_ID, up.getId());
		initFolders();
		
		// Default lookup: if not yet configured this will implicitly set built-in folder as default!
		manager.getDefaultCategoryId();
		//previewableCustomFields = WT.getCoreManager().listCustomFields(SERVICE_ID, BitFlag.of(CoreManager.CustomFieldListOptions.SEARCHABLE));
	}
	
	@Override
	public void cleanup() throws Exception {
		inactiveFolders.clear();
		inactiveFolders = null;
		inactiveRoots.clear();
		inactiveRoots = null;
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
		co.put("groupBy", EnumUtils.toSerializedName(us.getGroupBy()));
		co.put("cfieldsSearchable", LangUtils.serialize(getSearchableCustomFieldDefs(), ObjCustomFieldDefs.FieldsList.class));
		co.put("hasMailchimp",manager.isMailchimpEnabled());
		co.put("hasAudit", manager.isAuditEnabled() && (RunContext.isImpersonated() || RunContext.isPermitted(true, CoreManifest.ID, "AUDIT")));
		return co;
	}
	
	private ObjCustomFieldDefs.FieldsList getSearchableCustomFieldDefs() {
		CoreManager coreMgr = WT.getCoreManager();
		UserProfile up = getEnv().getProfile();
		
		try {
			ObjCustomFieldDefs.FieldsList scfields = new ObjCustomFieldDefs.FieldsList();
			for (CustomFieldEx cfield : coreMgr.listCustomFields(SERVICE_ID, BitFlag.of(CoreManager.CustomFieldListOptions.SEARCHABLE)).values()) {
				scfields.add(new ObjCustomFieldDefs.Field(cfield, up.getLanguageTag()));
			}
			return scfields;
			
		} catch(Throwable t) {
			return null;
		}
	}
	
	/*
	private ObjCustomFieldDefs getPreviewableCustomFieldDefs() {
		CoreManager coreMgr = WT.getCoreManager();
		UserProfile up = getEnv().getProfile();
		
		try {
			ArrayList<ObjCustomFieldDefs.Panel> panels = new ArrayList<>();
			Map<String, CustomPanel> cpanels = coreMgr.listCustomPanelsUsedBy(SERVICE_ID, coreMgr.listTagIds());
			for (CustomPanel cpanel : cpanels.values()) {
				Set<String> okFieldIds = new LinkedHashSet<>();
				for (String fieldId : cpanel.getFields()) {
					if (previewableCustomFields.containsKey(fieldId)) okFieldIds.add(fieldId);
				}
				if (!okFieldIds.isEmpty()) {
					cpanel.setFields(okFieldIds);
					panels.add(new ObjCustomFieldDefs.Panel(cpanel, up.getLanguageTag()));
				}
			}
			
			ArrayList<ObjCustomFieldDefs.Field> fields = new ArrayList<>();
			for (CustomField field : previewableCustomFields.values()) {
				fields.add(new ObjCustomFieldDefs.Field(field, up.getLanguageTag()));
			}
			
			return new ObjCustomFieldDefs(panels, fields);
			
		} catch(Throwable t) {
			return null;
		}
	}
	*/
	
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
			
			// HANDLE TRANSITION: cleanup code when process is completed!
			StringSet checkedRoots = us.getCheckedCategoryRoots();
			if (checkedRoots != null) { // Migration code... (remove after migration)
				List<String> toInactive = roots.keySet().stream()
						.filter(shareId -> !checkedRoots.contains(shareId))
						.collect(Collectors.toList());
				inactiveRoots = new StringSet(toInactive);
				us.setInactiveCategoryRoots(inactiveRoots);
				us.clearCheckedCategoryRoots();
				
			} else { // New code... (keep after migrarion)
				inactiveRoots = us.getInactiveCategoryRoots();
				// Clean-up orphans
				if (inactiveRoots.removeIf(shareId -> !roots.containsKey(shareId))) {
					us.setInactiveCategoryRoots(inactiveRoots);
				}
			}
			
			IntegerSet checkedFolders = us.getCheckedCategoryFolders();
			if (checkedFolders != null) { // Migration code... (remove after migration)
				List<Integer> toInactive = folders.keySet().stream()
						.filter(categoryId -> !checkedFolders.contains(categoryId))
						.collect(Collectors.toList());
				inactiveFolders = new IntegerSet(toInactive);
				us.setInactiveCategoryFolders(inactiveFolders);
				us.clearCheckedCategoryFolders();
				
			} else { // New code... (keep after migrarion)
				inactiveFolders = us.getInactiveCategoryFolders();
				// Clean-up orphans
				if (inactiveFolders.removeIf(categoryId -> !folders.containsKey(categoryId))) {
					us.setInactiveCategoryFolders(inactiveFolders);
				}
			}
		}
	}
	
	private void updateRootFoldersCache() throws WTException {
		UserProfileId pid = getEnv().getProfile().getId();
		synchronized(roots) {
			roots.clear();
			roots.put(MyShareRootCategory.SHARE_ID, new MyShareRootCategory(pid));
			for (ShareRootCategory root : manager.listIncomingCategoryRoots()) {
				roots.put(root.getShareId(), root);
			}
		}
	}
	
	private void updateFoldersCache() throws WTException {
		synchronized(roots) {
			foldersByRoot.clear();
			folders.clear();
			rootByFolder.clear();
			for (ShareRootCategory root : roots.values()) {
				foldersByRoot.put(root.getShareId(), new ArrayList<ShareFolderCategory>());
				if (root instanceof MyShareRootCategory) {
					for(Category cat : manager.listCategories().values()) {
						final MyShareFolderCategory fold = new MyShareFolderCategory(root.getShareId(), cat);
						foldersByRoot.get(root.getShareId()).add(fold);
						folders.put(cat.getCategoryId(), fold);
						rootByFolder.put(cat.getCategoryId(), root);
					}
				} else {
					for (ShareFolderCategory fold : manager.listIncomingCategoryFolders(root.getShareId()).values()) {
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
					
					Integer defltCategoryId = manager.getDefaultCategoryId();
					if (root instanceof MyShareRootCategory) {
						for (Category cal : manager.listCategories().values()) {
							MyShareFolderCategory folder = new MyShareFolderCategory(node, cal);
							if (writableOnly && !folder.getElementsPerms().implies("CREATE")) continue;
							
							final boolean isDefault = folder.getCategory().getCategoryId().equals(defltCategoryId);
							children.add(createFolderNode(chooser, folder, null, root.getPerms(), isDefault));
						}
					} else {
						if (foldersByRoot.containsKey(root.getShareId())) {
							for (ShareFolderCategory folder : foldersByRoot.get(root.getShareId())) {
								if (writableOnly && !folder.getElementsPerms().implies("CREATE")) continue;
								
								final boolean isDefault = folder.getCategory().getCategoryId().equals(defltCategoryId);
								final ExtTreeNode etn = createFolderNode(chooser, folder, folderProps.get(folder.getCategory().getCategoryId()), root.getPerms(), isDefault);
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
						toggleActiveRoot(node.id, node._active);
						
					} else if (node._type.equals(JsFolderNode.TYPE_FOLDER)) {
						CompositeId cid = new CompositeId().parse(node.id);
						toggleActiveFolder(Integer.valueOf(cid.getToken(1)), node._active);
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
			Integer defltCategoryId = manager.getDefaultCategoryId();
			synchronized(roots) {
				for (ShareRootCategory root : roots.values()) {
					if (foldersByRoot.containsKey(root.getShareId())) {
						for (ShareFolderCategory folder : foldersByRoot.get(root.getShareId())) {
							final boolean isDefault = folder.getCategory().getCategoryId().equals(defltCategoryId);
							items.add(new JsCategoryLkp(root, folder, folderProps.get(folder.getCategory().getCategoryId()), isDefault, items.size()));
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
			logger.error("Error in ManageSharing", ex);
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
				toggleActiveFolder(pl.data.categoryId, true); // forgets it by simply activating it
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
				
			} else if (crud.equals("updateTag")) {
				int id = ServletUtils.getIntParameter(request, "id", true);
				UpdateTagsOperation op = ServletUtils.getEnumParameter(request, "op", true, UpdateTagsOperation.class);
				ServletUtils.StringArray tags = ServletUtils.getObjectParameter(request, "tags", ServletUtils.StringArray.class, true);
				
				manager.updateContactCategoryTags(op, id, new HashSet<>(tags));
				new JsonResult().printTo(out);
			}
			
		} catch(Throwable t) {
			logger.error("Error in ManageCategory", t);
			new JsonResult(t).printTo(out);
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
	
	public void processSetDefaultCategory(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			
			us.setDefaultCategoryFolder(id);
			Integer defltCategoryId = manager.getDefaultCategoryId();
			new JsonResult(String.valueOf(defltCategoryId)).printTo(out);
				
		} catch(Throwable t) {
			logger.error("Error in SetDefaultCategory", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processSetCategoryColor(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			String color = ServletUtils.getStringParameter(request, "color", null);
			
			updateCategoryFolderColor(id, color);
			new JsonResult().printTo(out);
				
		} catch(Throwable t) {
			logger.error("Error in SetCategoryColor", t);
			new JsonResult(t).printTo(out);
		}
	}
				
	public void processSetCategorySync(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			String sync = ServletUtils.getStringParameter(request, "sync", null);
			
			updateCategoryFolderSync(id, EnumUtils.forSerializedName(sync, Category.Sync.class));
			new JsonResult().printTo(out);
				
		} catch(Throwable t) {
			logger.error("Error in SetCategorySync", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	private final Cache<String, Integer> cacheManageGridContactsTotalCount = Caffeine.newBuilder()
		.expireAfterWrite(500, TimeUnit.MILLISECONDS)
		.maximumSize(10)
		.build();
	
	public void processManageGridContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<JsGridContact> items = new ArrayList<>();
		
		try {
			UserProfile up = getEnv().getProfile();
			DateTimeZone utz = up.getTimeZone();
			
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				GridView view = ServletUtils.getEnumParameter(request, "view", GridView.WORK, GridView.class);
				Grouping groupBy = ServletUtils.getEnumParameter(request, "groupBy", Grouping.ALPHABETIC, Grouping.class);
				ShowBy showBy = ServletUtils.getEnumParameter(request, "showBy", ShowBy.DISPLAY, ShowBy.class);
				int page = ServletUtils.getIntParameter(request, "page", true);
				int limit = ServletUtils.getIntParameter(request, "limit", 50);
				QueryObj queryObj = ServletUtils.getObjectParameter(request, "query", new QueryObj(), QueryObj.class);
				
				ContactType type = queryObj.removeCondition("only", "lists") ? ContactType.LIST : ContactType.ANY;
				Map<String, CustomField.Type> map = cacheSearchableCustomFieldType.shallowCopy();
				List<Integer> visibleCategoryIds = getActiveFolderIds();
				
				String reqId = new MD5HashBuilder()
					.append(visibleCategoryIds)
					.append(ServletUtils.getStringParameter(request, "query", null))
					.append(ServletUtils.getStringParameter(request, "groupBy", null))
					.append(ServletUtils.getStringParameter(request, "showBy", null))
					.build();
				
				Integer cachedTotalCount = cacheManageGridContactsTotalCount.getIfPresent(reqId);
				ListContactsResult result = manager.listContacts(visibleCategoryIds, type, groupBy, showBy, ContactQuery.createCondition(queryObj, map, utz), page, limit, cachedTotalCount == null);
				int totalCount;
				if (cachedTotalCount != null) {
					totalCount = cachedTotalCount;
				} else {
					cacheManageGridContactsTotalCount.put(reqId, result.fullCount);
					totalCount = result.fullCount;
				}
				
				//ListContactsResult result = manager.listContacts(visibleCategoryIds, type, groupBy, showBy, ContactQuery.createCondition(queryObj, map, utz), page, limit, true);
				for (ContactLookup item : result.items) {
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
				new JsonResult(items, totalCount)
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
						contactIds.add(contactId);
						//contactsListIds.add(contactId);
					} else {
						contactIds.add(contactId);
					}
				}
				manager.deleteContact(contactIds);
				//manager.deleteContactsList(contactsListIds);
				
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
						//contactsListIds.add(contactId);
						contactIds.add(contactId);
					} else {
						contactIds.add(contactId);
					}
				}
				manager.moveContact(copy, contactIds, categoryId);
				//manager.moveContactsList(copy, contactsListIds, categoryId);
				
				new JsonResult().printTo(out);
				
			} else if (crud.equals("updateTag")) {
				StringArray uids = ServletUtils.getObjectParameter(request, "ids", StringArray.class, true);
				UpdateTagsOperation op = ServletUtils.getEnumParameter(request, "op", true, UpdateTagsOperation.class);
				ServletUtils.StringArray tags = ServletUtils.getObjectParameter(request, "tags", ServletUtils.StringArray.class, true);
				
				ArrayList<Integer> ids = new ArrayList<>();
				for (String uid : uids) {
					CompositeId cid = JsGridContact.Id.parse(uid);
					ids.add(JsGridContact.Id.contactId(cid));
				}
				manager.updateContactTags(op, ids, new HashSet<>(tags));
				
				new JsonResult().printTo(out);
			}
			
		} catch(Throwable t) {
			logger.error("Error in ManageGridContacts", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processCustomFieldContactPicker(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		UserProfile up = getEnv().getProfile();
		DateTimeZone utz = up.getTimeZone();
		
		try {
			StringArray filterCategoryIds = ServletUtils.getObjectParameter(request, "categoryIds", StringArray.class, false);
			String id = ServletUtils.getStringParameter(request, "id", false);
			int page = ServletUtils.getIntParameter(request, "page", true);
			int limit = ServletUtils.getIntParameter(request, "limit", 50);
			String query = ServletUtils.getStringParameter(request, "query", false);
			QueryObj queryObj = ServletUtils.getObjectParameter(request, "queryObj", null, QueryObj.class);
			
			Set<Integer> categoryIds = null;
			if (filterCategoryIds != null) {
				categoryIds = filterCategoryIds.stream().map(catId -> Integer.valueOf(catId)).collect(Collectors.toSet());
			} else {
				categoryIds = manager.listAllCategoryIds();
			}
			
			Condition<ContactQuery> conditionPredicate = null;
			if (!StringUtils.isBlank(id)) { // ID is set, force precise match (eg. custom-fields preview)
				conditionPredicate = new ContactQuery()
					.id().eq(id);
			} else if (!StringUtils.isBlank(query)) { // Query (from type-ahead) is set, try a match on name/email
				conditionPredicate = new ContactQuery()
					.name().eq(query)
					.or().email().eq(query);
			} else if (queryObj != null) { // Full query object is set, try match from that
				Map<String, CustomField.Type> map = cacheSearchableCustomFieldType.shallowCopy();
				conditionPredicate = ContactQuery.createCondition(queryObj, map, utz);
			}
			
			ListContactsResult result = manager.listContacts(categoryIds, ContactType.CONTACT, Grouping.ALPHABETIC, ShowBy.DISPLAY, conditionPredicate, page, limit, true);
			
			ArrayList<JsContactLkp> items = new ArrayList<>(result.items.size());
			for (ContactLookup item : result.items) {
				final ShareRootCategory root = rootByFolder.get(item.getCategoryId());
				if (root == null) continue;
				final ShareFolderCategory fold = folders.get(item.getCategoryId());
				if (fold == null) continue;
				CategoryPropSet pset = folderProps.get(item.getCategoryId());

				items.add(new JsContactLkp(root, fold, pset, item));
			}
			new JsonResult(items, result.fullCount)
				.setPage(page)
				.printTo(out);
			
		} catch (Throwable t) {
			logger.error("Error in CustomFieldContactPicker", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processMailchimpGetUserSettings(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		String srcPid=request.getParameter("srcPid");
		String srcCatId=request.getParameter("srcCatId");
		String pidAndCatId=srcPid;
		if (!StringUtils.isEmpty(srcCatId)) pidAndCatId+="-"+srcCatId;
		JsMailchimpUserSettings mus=new JsMailchimpUserSettings();
		mus.setAudienceId(us.getMailchimpAudienceId(pidAndCatId));
		mus.setSyncTags(us.getMailchimpSyncTags(pidAndCatId));
		mus.setTags(us.getMailchimpTags(pidAndCatId));
		mus.setIncomingAudienceId(us.getMailchimpIncomingAudienceId(pidAndCatId));
		mus.setIncomingCategoryId(us.getMailchimpIncomingCategoryId(pidAndCatId));
		new JsonResult(mus).printTo(out);
	}
	
	public void processSyncMailchimp(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		WebTopSession wts = getWts();
		
		String oid=request.getParameter("oid");
		String srcPid=request.getParameter("srcPid");
		String srcCatId=request.getParameter("srcCatId");
		String audienceId=request.getParameter("audienceId");
		String psyncTags=request.getParameter("syncTags");
		boolean syncTags=(psyncTags!=null && psyncTags.equals("true"));
		String tags[]=request.getParameterValues("tags");
		if (tags!=null && tags.length==1 && tags[0].length()==0) tags=null;
		String incomingAudienceId=request.getParameter("incomingAudienceId");
		String incomingCategoryId=request.getParameter("incomingCategoryId");

		MailchimpSyncThread t=new MailchimpSyncThread("MailchimpSyncThread", wts, oid,
				srcPid, srcCatId, audienceId, syncTags, tags,
				incomingAudienceId, incomingCategoryId);
		
		t.start();
		
		String pidAndCatId=srcPid;
		if (!StringUtils.isEmpty(srcCatId)) pidAndCatId+="-"+srcCatId;
		us.setMailchimpAudienceId(pidAndCatId, audienceId);
		us.setMailchimpSyncTags(pidAndCatId, syncTags);
		us.setMailchimpTags(pidAndCatId, tags);
		us.setMailchimpIncomingAudienceId(pidAndCatId, incomingAudienceId);
		us.setMailchimpIncomingCategoryId(pidAndCatId, incomingCategoryId);
		
		new JsonResult(new JsWizardData(null)).printTo(out);
	}
	
	public void processLookupMailchimpAudience(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			ApiClient cli=manager.getMailchimpApiClient();
			ListsApi api=new ListsApi(cli);
			
			List<JsSimple> items = new ArrayList<>();
			List<SubscriberList3> lists=api.getLists(null,null,null,null,null,null,null,null,null,null,null,null,null).getLists();
			for(SubscriberList3 sub: lists) {
				items.add(new JsSimple(sub.getId(), sub.getName()));
			}
			new JsonResult("audience", items, items.size()).printTo(out);
		} catch(Throwable t) {
			logger.error("Error in SyncMailChimp", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processGetContactPreview(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		CoreManager coreMgr = WT.getCoreManager();
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				String uid = ServletUtils.getStringParameter(request, "id", true);
				
				CompositeId cid = JsGridContact.Id.parse(uid);
				int contactId = JsGridContact.Id.contactId(cid);
				boolean isList = JsGridContact.Id.isList(cid);
				
				if (isList) {
					ContactList contactsList = manager.getContactList(contactId);
					
					ShareFolderCategory fold = folders.get(contactsList.getCategoryId());
					if (fold == null) throw new WTException("Folder not found [{}]", contactsList.getCategoryId());
					CategoryPropSet pset = folderProps.get(contactsList.getCategoryId());
					
					new JsonResult(new JsContactPreview(fold, pset, contactsList)).printTo(out);
					
				} else {
					UserProfile up = getEnv().getProfile();
					Contact contact = manager.getContact(contactId, BitFlag.of(ContactGetOptions.PICTURE, ContactGetOptions.TAGS, ContactGetOptions.CUSTOM_VALUES));
					if (contact == null) throw new WTException("Contact not found [{}]", contactId);
					ContactCompany company = contact.hasCompany() ? manager.getContactCompany(contactId) : null;
					
					ShareFolderCategory fold = folders.get(contact.getCategoryId());
					if (fold == null) throw new WTException("Folder not found [{}]", contact.getCategoryId());
					CategoryPropSet pset = folderProps.get(contact.getCategoryId());
					
					Set<String> pvwfields = coreMgr.listCustomFieldIds(SERVICE_ID, BitFlag.of(CoreManager.CustomFieldListOptions.PREVIEWABLE));
					Map<String, CustomPanel> cpanels = coreMgr.listCustomPanelsUsedBy(SERVICE_ID, contact.getTags());
					Map<String, CustomField> cfields = new HashMap<>();
					for (CustomPanel cpanel : cpanels.values()) {
						for (String fieldId : cpanel.getFields()) {
							if (!pvwfields.contains(fieldId)) continue;
							CustomField cfield = coreMgr.getCustomField(SERVICE_ID, fieldId);
							if (cfield != null) cfields.put(fieldId, cfield);
						}
					}
					
					new JsonResult(new JsContactPreview(fold, pset, contact, company, cpanels.values(), cfields, up.getLanguageTag(), up.getTimeZone())).printTo(out);
				}
			}
			
		} catch(Exception ex) {
			logger.error("Error in ManageContactDetails", ex);
			new JsonResult(ex).printTo(out);	
		}
	}
	
	public void processExpandRecipientsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		CoreManager coreMgr = WT.getCoreManager();
		
		try {
			String emailAddress = ServletUtils.getStringParameter(request, "address", true);
			
			List<String> emails = new ArrayList<>();
			List<Recipient> recipients = coreMgr.expandVirtualProviderRecipient(emailAddress);
			recipients.forEach(recipient -> {
				emails.add(InternetAddressUtils.toFullAddress(recipient.getAddress(), recipient.getPersonal()));
			});
				
			new JsonResult(emails).printTo(out);
		
		} catch(Throwable t) {
			logger.error("Error in ExpandListRecipient", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processManageContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		CoreManager coreMgr = WT.getCoreManager();
		UserProfile up = getEnv().getProfile();
		JsContact item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				
				int contactId = Integer.parseInt(id);
				Contact contact = manager.getContact(contactId);
				UserProfileId ownerId = manager.getCategoryOwner(contact.getCategoryId());
				
				Map<String, CustomPanel> cpanels = coreMgr.listCustomPanelsUsedBy(SERVICE_ID, contact.getTags());
				Map<String, CustomField> cfields = new HashMap<>();
				for (CustomPanel cpanel : cpanels.values()) {
					for (String fieldId : cpanel.getFields()) {
						CustomField cfield = coreMgr.getCustomField(SERVICE_ID, fieldId);
						if (cfield != null) cfields.put(fieldId, cfield);
					}
				}
				item = new JsContact(ownerId, contact, cpanels.values(), cfields, up.getLanguageTag(), up.getTimeZone());
				new JsonResult(item).printTo(out);
				
			} else if (crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsContact> pl = ServletUtils.getPayload(request, JsContact.class);
				
				ContactEx contact = pl.data.createContactForInsert(up.getTimeZone());
				
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
				
				BitFlag<ContactUpdateOptions> processOpts = BitFlag.of(ContactUpdateOptions.TAGS, ContactUpdateOptions.ATTACHMENTS, ContactUpdateOptions.CUSTOM_VALUES);
				Contact contact = pl.data.createContactForUpdate(up.getTimeZone());
				
				// We reuse picture field passing the uploaded file ID.
				// Due to different formats we can be sure that IDs don't collide.
				if (hasUploadedFile(pl.data.picture)) {
					// If found, new picture has been uploaded!
					processOpts.set(ContactUpdateOptions.PICTURE);
					contact.setPicture(getUploadedContactPicture(pl.data.picture));
				} else {
					// If blank, picture has been deleted!
					if (StringUtils.isBlank(pl.data.picture)) processOpts.set(ContactUpdateOptions.PICTURE);
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
				manager.updateContact(contact.getContactId(), contact, processOpts);
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
			
		} catch(Throwable t) {
			logger.error("Error in ManageContacts", t);
			new JsonResult(t).printTo(out);	
		}
	}
	
	public void processGetContactInformationForEventCreation(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			String id = ServletUtils.getStringParameter(request, "id", true);
			String type = ServletUtils.getStringParameter(request, "type", true);
			int contactId = Integer.parseInt(id);
			
			Contact contact = manager.getContact(contactId, BitFlag.of(ContactGetOptions.TAGS));
			JsEventContact eventContact = JsEventContact.createJsEventContact(contact, type);
			
			new JsonResult(eventContact).printTo(out);
			
		} catch (ParameterException ex) {
			logger.error("Error in GetContactInformationForEventCreation", ex);
			new JsonResult(false, "Error").printTo(out);	
		} catch (WTException ex) {
			logger.error("Error in GetContactInformationForEventCreation", ex);
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
			
		} catch(Throwable t) {
			logger.error("Error in DownloadContactAttachment", t);
			ServletUtils.writeErrorHandlingJs(response, t.getMessage());
		}
	}
	
	public void processGetCustomFieldsDefsData(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		CoreManager coreMgr = WT.getCoreManager();
		UserProfile up = getEnv().getProfile();
		
		try {
			ServletUtils.StringArray tags = ServletUtils.getObjectParameter(request, "tags", ServletUtils.StringArray.class, true);
			Integer contactId = ServletUtils.getIntParameter(request, "id", false);
			
			Map<String, CustomPanel> cpanels = coreMgr.listCustomPanelsUsedBy(SERVICE_ID, tags);
			Map<String, CustomFieldValue> cvalues = (contactId != null) ? manager.getContactCustomValues(contactId) : null;
			Map<String, CustomField> cfields = new HashMap<>();
			for (CustomPanel cpanel : cpanels.values()) {
				for (String fieldId : cpanel.getFields()) {
					CustomField cfield = coreMgr.getCustomField(SERVICE_ID, fieldId);
					if (cfield != null) cfields.put(fieldId, cfield);
				}
			}
			new JsonResult(new JsCustomFieldDefsData(cpanels.values(), cfields, cvalues, up.getLanguageTag(), up.getTimeZone())).printTo(out);
			
		} catch(Throwable t) {
			logger.error("Error in GetCustomFieldsDefsData", t);
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
				ContactList list = manager.getContactList(contactId);
				UserProfileId ownerId = manager.getCategoryOwner(list.getCategoryId());
				item = new JsContactsList(ownerId, list);
				
				new JsonResult(item).printTo(out);
				
			} else if(crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsContactsList> pl = ServletUtils.getPayload(request, JsContactsList.class);
				
				ContactListEx contact = pl.data.createContactListForInsert();
				manager.addContactList(contact);
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsContactsList> pl = ServletUtils.getPayload(request, JsContactsList.class);
				
				ContactList contact = pl.data.createContactListForUpdate();
				manager.updateContactList(contact.getContactId(), contact);
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				IntegerArray ids = ServletUtils.getObjectParameter(request, "ids", IntegerArray.class, true);
				
				if (ids.size() == 1) {
					manager.deleteContactList(ids.get(0));
				} else {
					manager.deleteContactList(ids);
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
			String virtualRecipient = ServletUtils.getStringParameter(request, "list", true);
			String recipientType = ServletUtils.getStringParameter(request, "recipientType", true);
			ArrayList<String> emails=ServletUtils.getStringParameters(request, "emails");
			
			InternetAddress iaVirtualRecipient = InternetAddressUtils.toInternetAddress(virtualRecipient);
			if (iaVirtualRecipient == null) throw new WTException("Unable to parse '{}' as internetAddress", virtualRecipient);
			Integer listId = ContactsUtils.virtualRecipientToListId(iaVirtualRecipient);
			if (listId == null) throw new WTException("Recipient address is not a list [{}]", iaVirtualRecipient.getAddress());
			
			ArrayList<ContactListRecipient> recipients = new ArrayList<>();
			for (String email: emails) {
				ContactListRecipient rcpt = new ContactListRecipient();
				rcpt.setRecipient(email);
				rcpt.setRecipientType(recipientType);
				recipients.add(rcpt);
			}
			manager.updateContactsListRecipients(listId, recipients, true);
			new JsonResult().printTo(out);
			
		} catch(Throwable t) {
			logger.error("Error in AddToContactsList", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processImportContactsFromText(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String oid = ServletUtils.getStringParameter(request, "oid", true);
			String op = ServletUtils.getStringParameter(request, "op", true);
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			String encoding = ServletUtils.getStringParameter(request, "encoding", true);
			String delimiter = ServletUtils.getStringParameter(request, "delimiter", true);
			String lineSeparator = ServletUtils.getStringParameter(request, "lineSeparator", true);
			String textQualifier = ServletUtils.getStringParameter(request, "textQualifier", null);
			Integer hr = ServletUtils.getIntParameter(request, "headersRow", true);
			Integer fdr = ServletUtils.getIntParameter(request, "firstDataRow", true);
			Integer ldr = ServletUtils.getIntParameter(request, "lastDataRow", -1);
			
			WebTopSession wts = getWts();
			LogHandler logHandler = !"do".equals(op) ? null : new LogHandler() {
				@Override
				public void handle(Collection<LogEntry> entries) {
					if (entries != null) wts.notify(toContactImportLogSMs(oid, true, entries));
				}
			};
			
			UploadedFile upl = getUploadedFile(uploadId);
			if(upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			File file = new File(WT.getTempFolder(), upl.getUploadId());
			
			CsvPreference pref = ContactTextFileReader.buildCsvPreference(delimiter, lineSeparator, textQualifier);
			ContactTextFileReader rea = new ContactTextFileReader(pref, encoding);
			rea.setHeadersRow(hr);
			rea.setFirstDataRow(fdr);
			if(ldr != -1) rea.setLastDataRow(ldr);
			
			if(op.equals("columns")) {
				ArrayList<JsValue> items = new ArrayList<>();
				for(String sheet : rea.listColumnNames(file).values()) {
					items.add(new JsValue(sheet));
				}
				new JsonResult("columns", items).printTo(out);
				
			} else if(op.equals("mappings")) {
				List<FileRowsReader.FieldMapping> items = rea.listFieldMappings(file, ContactTextFileReader.MAPPING_TARGETS);
				new JsonResult("mappings", items).printTo(out);
				
			} else if(op.equals("do")) {
				Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
				IContactsManager.ImportMode mode = ServletUtils.getEnumParameter(request, "importMode", IContactsManager.ImportMode.COPY, IContactsManager.ImportMode.class);
				ListFieldMapping mappings = ServletUtils.getObjectParameter(request, "mappings", ListFieldMapping.class, true);
				
				rea.setMappings(mappings);
				manager.importContacts(categoryId, rea, file, mode, logHandler);
				removeUploadedFile(uploadId);
				new JsonResult(new JsWizardData(null)).printTo(out);
			}
			
		} catch (Throwable t) {
			logger.error("Error in action ImportContactsFromText", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processImportContactsFromExcel(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String oid = ServletUtils.getStringParameter(request, "oid", true);
			String op = ServletUtils.getStringParameter(request, "op", true);
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			Boolean binary = ServletUtils.getBooleanParameter(request, "binary", false);
			
			UploadedFile upl = getUploadedFile(uploadId);
			if(upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			File file = new File(WT.getTempFolder(), upl.getUploadId());
			
			if(op.equals("sheets")) {
				ArrayList<JsValue> items = new ArrayList<>();
				ContactExcelFileReader rea = new ContactExcelFileReader(binary);
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
				
				WebTopSession wts = getWts();
				LogHandler logHandler = !"do".equals(op) ? null : new LogHandler() {
					@Override
					public void handle(Collection<LogEntry> entries) {
						if (entries != null) wts.notify(toContactImportLogSMs(oid, true, entries));
					}
				};
				
				ContactExcelFileReader rea = new ContactExcelFileReader(binary, logHandler);
				rea.setHeadersRow(hr);
				rea.setFirstDataRow(fdr);
				if(ldr != -1) rea.setLastDataRow(ldr);
				rea.setSheet(sheet);
				
				if(op.equals("columns")) {
					ArrayList<JsValue> items = new ArrayList<>();
					for(String col : rea.listColumnNames(file).values()) {
						items.add(new JsValue(col));
					}
					new JsonResult("columns", items).printTo(out);
					
				} else if(op.equals("mappings")) {
					List<FileRowsReader.FieldMapping> items = rea.listFieldMappings(file, ContactExcelFileReader.MAPPING_TARGETS);
					new JsonResult("mappings", items).printTo(out);
					
				} else if(op.equals("do")) {
					Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
					IContactsManager.ImportMode mode = ServletUtils.getEnumParameter(request, "importMode", IContactsManager.ImportMode.COPY, IContactsManager.ImportMode.class);
					ListFieldMapping mappings = ServletUtils.getObjectParameter(request, "mappings", ListFieldMapping.class, true);
					
					rea.setMappings(mappings);
					manager.importContacts(categoryId, rea, file, mode, logHandler);
					removeUploadedFile(uploadId);
					new JsonResult(new JsWizardData(null)).printTo(out);
				}
			}
			
		} catch (Throwable t) {
			logger.error("Error in action ImportContactsFromExcel", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processImportContactsFromVCard(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String oid = ServletUtils.getStringParameter(request, "oid", true);
			String op = ServletUtils.getStringParameter(request, "op", true);
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			
			WebTopSession wts = getWts();
			LogHandler logHandler = !"do".equals(op) ? null : new LogHandler() {
				@Override
				public void handle(Collection<LogEntry> entries) {
					if (entries != null) wts.notify(toContactImportLogSMs(oid, true, entries));
				}
			};
			
			UploadedFile upl = getUploadedFile(uploadId);
			if(upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			File file = new File(WT.getTempFolder(), upl.getUploadId());
			
			ContactVCardFileReader rea = new ContactVCardFileReader(logHandler);
			
			if(op.equals("do")) {
				Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
				IContactsManager.ImportMode mode = ServletUtils.getEnumParameter(request, "importMode", IContactsManager.ImportMode.COPY, IContactsManager.ImportMode.class);
				
				manager.importContacts(categoryId, rea, file, mode, logHandler);
				removeUploadedFile(uploadId);
				new JsonResult(new JsWizardData(null)).printTo(out);
			}
			
		} catch (Throwable t) {
			logger.error("Error in action ImportContactsFromVCard", t);
			new JsonResult(t).printTo(out);
		}
	}
	
	public void processImportContactsFromLDIF(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String oid = ServletUtils.getStringParameter(request, "oid", true);
			String op = ServletUtils.getStringParameter(request, "op", true);
			String uploadId = ServletUtils.getStringParameter(request, "uploadId", true);
			
			WebTopSession wts = getWts();
			LogHandler logHandler = !"do".equals(op) ? null : new LogHandler() {
				@Override
				public void handle(Collection<LogEntry> entries) {
					if (entries != null) wts.notify(toContactImportLogSMs(oid, true, entries));
				}
			};
			
			UploadedFile upl = getUploadedFile(uploadId);
			if (upl == null) throw new WTException("Uploaded file not found [{0}]", uploadId);
			File file = new File(WT.getTempFolder(), upl.getUploadId());

			ContactLDIFFileReader  rea = new ContactLDIFFileReader();
			
			if(op.equals("do")) {
				Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
				IContactsManager.ImportMode mode = ServletUtils.getEnumParameter(request, "importMode", IContactsManager.ImportMode.COPY, IContactsManager.ImportMode.class);
				
				manager.importContacts(categoryId, rea, file, mode, logHandler);
				removeUploadedFile(uploadId);
				new JsonResult(new JsWizardData(null)).printTo(out);
			}
			
		} catch (Throwable t) {
			logger.error("Error in action ImportContactsFromVCard", t);
			new JsonResult(t).printTo(out);
		}
	}
		
	public void processPrintAddressbook(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<RBAddressbook> items = new ArrayList<>();
		UserProfile up = getEnv().getProfile();
		DateTimeZone utz = up.getTimeZone();
		
		try {
			String filename = ServletUtils.getStringParameter(request, "filename", "print");
			//GridView view = ServletUtils.getEnumParameter(request, "view", GridView.WORK, GridView.class);
			Grouping groupBy = ServletUtils.getEnumParameter(request, "groupBy", Grouping.ALPHABETIC, Grouping.class);
			ShowBy showBy = ServletUtils.getEnumParameter(request, "showBy", ShowBy.DISPLAY, ShowBy.class);
			QueryObj queryObj = ServletUtils.getObjectParameter(request, "query", new QueryObj(), QueryObj.class);
			
			int limit = 500;
			ContactType type = queryObj.removeCondition("only", "lists") ? ContactType.LIST : ContactType.ANY;
			
			Map<String, CustomField.Type> map = cacheSearchableCustomFieldType.shallowCopy();
			List<Integer> visibleCategoryIds = getActiveFolderIds();
			ListContactsResult result = manager.listContacts(visibleCategoryIds, type, groupBy, showBy, ContactQuery.createCondition(queryObj, map, utz), 1, limit, true);
			if (result.fullCount > limit) throw new WTException("Too many elements, limit is {}", limit);
			for (ContactLookup item : result.items) {
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
		
		try {
			String filename = ServletUtils.getStringParameter(request, "filename", "print");
			IntegerArray ids = ServletUtils.getObjectParameter(request, "ids", IntegerArray.class, true);
			
			Category category = null;
			for (Integer id : ids) {
				Contact contact = manager.getContact(id);
				ContactPictureWithBytes picture = null;
				ContactCompany company = null;
				
				category = manager.getCategory(contact.getCategoryId());
				if (contact.hasPicture()) picture = manager.getContactPicture(id);
				if (contact.hasCompany()) company = manager.getContactCompany(id);
				items.add(new RBContactDetail(category, contact, company, picture));
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
			VCardOutput vout = new VCardOutput(prodId)
					.withEnableCaretEncoding(manager.VCARD_CARETENCODINGENABLED);
			for (Integer id : ids) {
				ContactObjectWithBean contactObj = (ContactObjectWithBean)manager.getContactObject(id, ContactObjectOutputType.BEAN);
				final String filename = buildContactFilename(contactObj) + ".vcf";
				UploadedFile upfile = addAsUploadedFile(tag, filename, "text/vcard", IOUtils.toInputStream(vout.writeVCard(contactObj.getContact(), null)));
				
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
	
	private ServiceMessage toContactImportLogSMs(String operationId, boolean pushDown, Collection<LogEntry> entries) {
		StringJoiner sj = new StringJoiner("\n");
		for (LogEntry entry : entries) {
			if (pushDown) entry.pushDown();
			sj.add(entry.toString());
		}
		return new ContactImportLogSM(SERVICE_ID, operationId, sj.toString());
	}
	
	private String buildContactFilename(ContactObjectWithBean cobj) {
		final String full = cobj.getContact().getFullName();
		return (StringUtils.isBlank(full)) ? String.valueOf(cobj.getContactId()) : PathUtils.sanitizeFileName(full);
	}
	
	private ArrayList<Integer> getActiveFolderIds() {
		ArrayList<Integer> ids = new ArrayList<>();
		synchronized(roots) {
			for (ShareRootCategory root : getActiveRoots()) {
				for (ShareFolderCategory folder : foldersByRoot.get(root.getShareId())) {
					if (inactiveFolders.contains(folder.getCategory().getCategoryId())) continue;
					ids.add(folder.getCategory().getCategoryId());
				}
			}
		}
		return ids;
	}
	
	private List<ShareRootCategory> getActiveRoots() {
		return roots.values().stream()
				.filter(root -> !inactiveRoots.contains(root.getShareId()))
				.collect(Collectors.toList());
	}
	
	private void toggleActiveRoot(String shareId, boolean active) {
		toggleActiveRoots(new String[]{shareId}, active);
	}
	
	private void toggleActiveRoots(String[] shareIds, boolean active) {
		synchronized(roots) {
			for (String shareId : shareIds) {
				if (active) {
					inactiveRoots.remove(shareId);
				} else {
					inactiveRoots.add(shareId);
				}
			}	
			us.setInactiveCategoryRoots(inactiveRoots);
		}
	}
	
	private void toggleActiveFolder(Integer folderId, boolean active) {
		toggleActiveFolders(new Integer[]{folderId}, active);
	}
	
	private void toggleActiveFolders(Integer[] folderIds, boolean active) {
		synchronized(roots) {
			for (int folderId : folderIds) {
				if (active) {
					inactiveFolders.remove(folderId);
				} else {
					inactiveFolders.add(folderId);
				}
			}
			us.setInactiveCategoryFolders(inactiveFolders);
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
	
	private void updateCategoryFolderColor(int categoryId, String color) throws WTException {
		synchronized(roots) {
			if (folders.get(categoryId) instanceof MyShareFolderCategory) {
				Category cat = manager.getCategory(categoryId);
				cat.setColor(color);
				manager.updateCategory(cat);
				updateFoldersCache();
			} else {
				CategoryPropSet pset = manager.getCategoryCustomProps(categoryId);
				pset.setColor(color);
				manager.updateCategoryCustomProps(categoryId, pset);
				folderProps.put(categoryId, pset);
			}
		}
	}
	
	private void updateCategoryFolderSync(int categoryId, Category.Sync sync) throws WTException {
		synchronized(roots) {
			if (folders.get(categoryId) instanceof MyShareFolderCategory) {
				Category cat = manager.getCategory(categoryId);
				cat.setSync(sync);
				manager.updateCategory(cat);
				updateFoldersCache();
			} else {
				CategoryPropSet pset = manager.getCategoryCustomProps(categoryId);
				pset.setSync(sync);
				manager.updateCategoryCustomProps(categoryId, pset);
				folderProps.put(categoryId, pset);
			}
		}
	}
	
	private ExtTreeNode createRootNode(boolean chooser, ShareRootCategory root) {
		if(root instanceof MyShareRootCategory) {
			return createRootNode(chooser, root.getShareId(), root.getOwnerProfileId().toString(), root.getPerms().toString(), lookupResource(ContactsLocale.CATEGORIES_MY), false, "wtcon-icon-categoryMy")
					.setExpanded(true);
		} else {
			return createRootNode(chooser, root.getShareId(), root.getOwnerProfileId().toString(), root.getPerms().toString(), root.getDescription(), false, "wtcon-icon-categoryIncoming")
					.setExpanded(true);
		}
	}
	
	private ExtTreeNode createRootNode(boolean chooser, String id, String pid, String rights, String text, boolean leaf, String iconClass) {
		boolean active = !inactiveRoots.contains(id);
		ExtTreeNode node = new ExtTreeNode(id, text, leaf);
		node.put("_type", JsFolderNode.TYPE_ROOT);
		node.put("_pid", pid);
		node.put("_rrights", rights);
		node.put("_active", active);
		node.setIconClass(iconClass);
		if (!chooser) node.setChecked(active);
		node.put("expandable", false);
		
		return node;
	}
	
	private ExtTreeNode createFolderNode(boolean chooser, ShareFolderCategory folder, CategoryPropSet folderProps, SharePermsRoot rootPerms, boolean isDefault) {
		Category cat = folder.getCategory();
		String id = new CompositeId().setTokens(folder.getShareId(), cat.getCategoryId()).toString();
		String color = cat.getColor();
		Category.Sync sync = Category.Sync.OFF;
		boolean active = !inactiveFolders.contains(cat.getCategoryId());
		
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
		node.put("_default", isDefault);
		node.put("_active", active);
		if (!chooser) node.setChecked(active);
		
		return node;
	}
	
	private class SearchableCustomFieldTypeCache extends AbstractPassiveExpiringBulkMap<String, CustomField.Type> {
		
		public SearchableCustomFieldTypeCache(final long timeToLive, final TimeUnit timeUnit) {
			super(timeToLive, timeUnit);
		}
		
		@Override
		protected Map<String, CustomField.Type> internalGetMap() {
			try {
				CoreManager coreMgr = WT.getCoreManager();
				return coreMgr.listCustomFieldTypesById(SERVICE_ID, BitFlag.of(CoreManager.CustomFieldListOptions.SEARCHABLE));
				
			} catch(Throwable t) {
				logger.error("[SearchableCustomFieldTypeCache] Unable to build cache", t);
				throw new UnsupportedOperationException();
			}
		}
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
