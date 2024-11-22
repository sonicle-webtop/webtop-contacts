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

import com.sonicle.webtop.contacts.bol.model.CategoryNodeId;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.sonicle.commons.AlgoUtils.MD5HashBuilder;
import com.sonicle.commons.BitFlag;
import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.InternetAddressUtils;
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.PathUtils;
import com.sonicle.commons.URIUtils;
import com.sonicle.commons.cache.AbstractPassiveExpiringBulkMap;
import com.sonicle.commons.concurrent.KeyedReentrantLocks;
import com.sonicle.commons.flags.BitFlags;
import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.web.Crud;
import com.sonicle.commons.web.DispositionType;
import com.sonicle.commons.web.ParameterException;
import com.sonicle.commons.web.ServletUtils;
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
import com.sonicle.webtop.contacts.IContactsManager.ContactGetOption;
import com.sonicle.webtop.contacts.IContactsManager.ContactUpdateOption;
import com.sonicle.webtop.contacts.bol.js.JsContact;
import com.sonicle.webtop.contacts.bol.js.JsCategory;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLinks;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLkp;
import com.sonicle.webtop.contacts.bol.js.JsCategorySharing;
import com.sonicle.webtop.contacts.bol.js.JsContactLkp;
import com.sonicle.webtop.contacts.bol.js.JsContactPreview;
import com.sonicle.webtop.contacts.bol.js.JsContactsList;
import com.sonicle.webtop.contacts.bol.js.JsEventContact;
import com.sonicle.webtop.contacts.bol.js.JsFolderNode;
import com.sonicle.webtop.contacts.bol.js.JsGridContact;
import com.sonicle.webtop.contacts.bol.js.JsMailchimpUserSettings;
import com.sonicle.webtop.contacts.bol.js.JsRecipient;
import com.sonicle.webtop.contacts.bol.js.ListFieldMapping;
import com.sonicle.webtop.contacts.bol.model.MyCategoryFSFolder;
import com.sonicle.webtop.contacts.bol.model.MyCategoryFSOrigin;
import com.sonicle.webtop.contacts.bol.model.RBAddressbook;
import com.sonicle.webtop.contacts.bol.model.RBContactDetail;
import com.sonicle.webtop.contacts.bol.model.SetupDataCategoryRemote;
import com.sonicle.webtop.contacts.io.CSVOutput;
import com.sonicle.webtop.contacts.io.VCardOutput;
import com.sonicle.webtop.contacts.io.input.ContactExcelFileReader;
import com.sonicle.webtop.contacts.io.input.ContactLDIFFileReader;
import com.sonicle.webtop.contacts.io.input.ContactTextFileReader;
import com.sonicle.webtop.contacts.io.input.ContactVCardFileReader;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.api.ListsApi;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberList3;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryFSFolder;
import com.sonicle.webtop.contacts.model.CategoryFSOrigin;
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
import com.sonicle.webtop.contacts.model.ContactListRecipientBase;
import com.sonicle.webtop.contacts.model.ContactObject;
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
import com.sonicle.webtop.core.app.model.FolderShare;
import com.sonicle.webtop.core.app.model.FolderSharing;
import com.sonicle.webtop.core.app.sdk.AbstractFolderTreeCache;
import com.sonicle.webtop.core.app.sdk.WTParseException;
import com.sonicle.webtop.core.app.util.log.LogEntry;
import com.sonicle.webtop.core.app.util.log.LogHandler;
import com.sonicle.webtop.core.bol.js.ObjCustomFieldDefs;
import com.sonicle.webtop.core.bol.js.JsCustomFieldDefsData;
import com.sonicle.webtop.core.bol.js.JsSimple;
import com.sonicle.webtop.core.bol.js.JsValue;
import com.sonicle.webtop.core.bol.js.JsWizardData;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import jakarta.mail.internet.InternetAddress;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author malbinola
 */
public class Service extends BaseService {
	public static final Logger logger = WT.getLogger(Service.class);
	public static final String WORK_VIEW = "w";
	public static final String HOME_VIEW = "h";
	public static final String META_CONTEXT_SEARCH = "mainsearch";
	
	private ContactsManager manager;
	private ContactsUserSettings us;
	private ContactsServiceSettings ss;
	
	private final KeyedReentrantLocks<String> locks = new KeyedReentrantLocks<>();
	private final SearchableCustomFieldTypeCache cacheSearchableCustomFieldType = new SearchableCustomFieldTypeCache(5, TimeUnit.SECONDS);
	private final FoldersTreeCache foldersTreeCache = new FoldersTreeCache();
	private final LoadingCache<Integer, Optional<CategoryPropSet>> foldersPropsCache = Caffeine.newBuilder().build(new FoldersPropsCacheLoader());
	private StringSet inactiveOrigins = null;
	private IntegerSet inactiveFolders = null;	
	private final AsyncActionCollection<Integer, SyncRemoteCategoryAA> syncRemoteCategoryAAs = new AsyncActionCollection<>();
	private final Cache<String, Integer> cacheManageGridContactsTotalCount = Caffeine.newBuilder()
		.expireAfterWrite(500, TimeUnit.MILLISECONDS)
		.maximumSize(10)
		.build();
	
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
		synchronized(syncRemoteCategoryAAs) {
			syncRemoteCategoryAAs.clear();
		}
		if (inactiveFolders != null) inactiveFolders.clear();
		if (foldersTreeCache != null) foldersTreeCache.clear();
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
		foldersTreeCache.init();
		// Retrieves inactive origins set
		inactiveOrigins = us.getInactiveCategoryOrigins();
		if (inactiveOrigins.removeIf(key -> shouldCleanupInactiveOriginKey(key))) { // Clean-up orphans
			us.setInactiveCategoryOrigins(inactiveOrigins);
		}
		// Retrieves inactive folders set
		inactiveFolders = us.getInactiveCategoryFolders();
		if (inactiveFolders.removeIf(calendarId -> !foldersTreeCache.existsFolder(calendarId))) { // Clean-up orphans
			us.setInactiveCategoryFolders(inactiveFolders);
		}
	}
	
	private void appendOriginFolderNodes(final ArrayList<ExtTreeNode> children, final CategoryFSOrigin origin, final Integer defaultCalendarId, final boolean writableOnly, final boolean chooser) {
		for (CategoryFSFolder folder : foldersTreeCache.getFoldersByOrigin(origin)) {
			if (writableOnly && !folder.getPermissions().getItemsPermissions().has(FolderShare.ItemsRight.CREATE)) continue;

			final boolean isDefault = folder.getFolderId().equals(defaultCalendarId);
			final ExtTreeNode xnode = createCategoryFolderNode(chooser, origin, folder, isDefault);
			if (xnode != null) children.add(xnode);
		}
	}
	
	public void processManageFoldersTree(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<ExtTreeNode> children = new ArrayList<>();
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (Crud.READ.equals(crud)) {
				String node = ServletUtils.getStringParameter(request, "node", true);
				boolean chooser = ServletUtils.getBooleanParameter(request, "chooser", false);
				boolean bulk = ServletUtils.getBooleanParameter(request, "bulk", false);
				
				if (bulk && node.equals("root")) {
					boolean writableOnly = ServletUtils.getBooleanParameter(request, "writableOnly", false);
					final Integer defaultCategoryId = manager.getDefaultCategoryId();
					boolean hasOthers = false;
					
					// Classic root nodes
					for (CategoryFSOrigin origin : foldersTreeCache.getOrigins()) {
						if (!chooser && !(origin instanceof MyCategoryFSOrigin)) {
							hasOthers = true;
							continue;
						}
						final ExtTreeNode onode = createOriginFolderNode(chooser, origin);
						if (onode != null) {
							ArrayList<ExtTreeNode> ochildren = new ArrayList<>();
							// Tree node -> append folders of specified origin
							appendOriginFolderNodes(ochildren, origin, defaultCategoryId, writableOnly, chooser);
							if (!ochildren.isEmpty()) {
								onode.setChildren(ochildren);
								if (chooser) onode.setExpanded(true);
								children.add(onode.setLoaded(true));
							}
						}
					}
					// Others root node
					if (!chooser && hasOthers) {
						final ExtTreeNode gnode = createOthersFolderNode(chooser);
						if (gnode != null) {
							ArrayList<ExtTreeNode> gchildren = new ArrayList<>();
							for (CategoryFSOrigin origin : foldersTreeCache.getOrigins()) {
								if (origin instanceof MyCategoryFSOrigin) continue;
								
								final ExtTreeNode onode = createOriginFolderNode(chooser, origin);
								if (onode != null) {
									ArrayList<ExtTreeNode> ochildren = new ArrayList<>();
									// Tree node -> append folders of specified incoming origin
									appendOriginFolderNodes(ochildren, origin, defaultCategoryId, writableOnly, chooser);
									onode.setChildren(ochildren);
									gchildren.add(onode.setLoaded(true));
								}
							}
							gnode.setChildren(gchildren);
							children.add(gnode.setLoaded(true));
						}
					}
					new JsonResult("children", children).printTo(out);
					
				} else {
					if (node.equals("root")) { // Tree ROOT node -> list folder origins (incoming origins will be grouped)
						boolean hasOthers = false;
						
						// Classic root nodes
						for (CategoryFSOrigin origin : foldersTreeCache.getOrigins()) {
							if (!chooser && !(origin instanceof MyCategoryFSOrigin)) {
								hasOthers = true;
								continue;
							}
							final ExtTreeNode xnode = createOriginFolderNode(chooser, origin);
							if (xnode != null) children.add(xnode);
						}
						// Others root node
						if (!chooser && hasOthers) {
							final ExtTreeNode xnode = createOthersFolderNode(chooser);
							if (xnode != null) children.add(xnode);
						}
						
					} else {
						boolean writableOnly = ServletUtils.getBooleanParameter(request, "writableOnly", false);
						CategoryNodeId nodeId = new CategoryNodeId(node);
						if (nodeId.isGrouperOther() && !chooser) { // Tree node (others grouper) -> list all incoming origins
							for (CategoryFSOrigin origin : foldersTreeCache.getOrigins()) {
								if (origin instanceof MyCategoryFSOrigin) continue;

								// Will pass here incoming origins only (resources' origins excluded)
								final ExtTreeNode xnode = createOriginFolderNode(chooser, origin);
								children.add(xnode);
							}

						} else if (CategoryNodeId.Type.ORIGIN.equals(nodeId.getType())) { // Tree node -> list folder of specified origin
							final Integer defaultCategoryId = manager.getDefaultCategoryId();
							final CategoryFSOrigin origin = foldersTreeCache.getOriginByProfile(nodeId.getOriginAsProfileId());
							for (CategoryFSFolder folder : foldersTreeCache.getFoldersByOrigin(origin)) {
								if (writableOnly && !folder.getPermissions().getItemsPermissions().has(FolderShare.ItemsRight.CREATE)) continue;

								final boolean isDefault = folder.getFolderId().equals(defaultCategoryId);
								final ExtTreeNode xnode = createCategoryFolderNode(chooser, origin, folder, isDefault);
								if (xnode != null) children.add(xnode);
							}	

						} else {
							throw new WTParseException("Unable to parse '{}' as node ID", node);
						}
					}
					new JsonResult("children", children).printTo(out);
				}
				
			} else if (crud.equals(Crud.UPDATE)) {
				PayloadAsList<JsFolderNode.List> pl = ServletUtils.getPayloadAsList(request, JsFolderNode.List.class);
				
				for (JsFolderNode node : pl.data) {
					CategoryNodeId nodeId = new CategoryNodeId(node.id);
					if (CategoryNodeId.Type.ORIGIN.equals(nodeId.getType()) || CategoryNodeId.Type.GROUPER.equals(nodeId.getType())) {
						toggleActiveOrigin(nodeId.getOrigin(), node._active);
					} else if (CategoryNodeId.Type.FOLDER.equals(nodeId.getType())) {
						toggleActiveFolder(nodeId.getFolderId(), node._active);
					}
				}
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				PayloadAsList<JsFolderNode.List> pl = ServletUtils.getPayloadAsList(request, JsFolderNode.List.class);
				
				for (JsFolderNode node : pl.data) {
					CategoryNodeId nodeId = new CategoryNodeId(node.id);
					if (CategoryNodeId.Type.FOLDER.equals(nodeId.getType())) {
						manager.deleteCategory(nodeId.getFolderId());
						toggleActiveFolder(nodeId.getFolderId(), true); // forgets it by simply activating it
					}
				}
				foldersTreeCache.init(AbstractFolderTreeCache.Target.FOLDERS);
				new JsonResult().printTo(out);
			}
			
		} catch (Exception ex) {
			logger.error("Error in action ManageFoldersTree", ex);
		}
	}
	
	private ExtTreeNode createOriginFolderNode(boolean chooser, CategoryFSOrigin origin) {
		CategoryNodeId nodeId = CategoryNodeId.build(CategoryNodeId.Type.ORIGIN, origin.getProfileId());
		boolean checked = isOriginActive(toInactiveOriginKey(origin));
		if (origin instanceof MyCategoryFSOrigin) {
			return createOriginFolderNode(chooser, nodeId, "{trfolders.origin.my}", "wtcon-icon-categoryMy", origin.getWildcardPermissions(), checked);
		} else {
			return createOriginFolderNode(chooser, nodeId, origin.getDisplayName(), "wtcon-icon-categoryIncoming", origin.getWildcardPermissions(), checked);
		}
	}
	
	private ExtTreeNode createOriginFolderNode(boolean chooser, CategoryNodeId nodeId, String text, String iconClass, FolderShare.Permissions originPermissions, boolean isActive) {
		ExtTreeNode node = new ExtTreeNode(nodeId.toString(), text, false);
		node.put("_orPerms", originPermissions.getFolderPermissions().toString(true));
		node.put("_active", isActive);
		node.setIconClass(iconClass);
		if (!chooser) node.setChecked(isActive);
		node.put("expandable", true);
		return node;
	}
	
	private ExtTreeNode createOthersFolderNode(boolean chooser) {
		CategoryNodeId nodeId = CategoryNodeId.build(CategoryNodeId.Type.GROUPER, CategoryNodeId.GROUPER_OTHERS_ORIGIN);
		boolean checked = isOriginActive(toInactiveOriginKey(nodeId));
		return createOthersFolderNode(chooser, nodeId, checked);
	}
	
	private ExtTreeNode createOthersFolderNode(boolean chooser, CategoryNodeId nodeId, boolean isActive) {
		ExtTreeNode node = new ExtTreeNode(nodeId.toString(), "{trfolders.origin.others}", false);
		node.put("_active", isActive);
		if (!chooser) node.setChecked(isActive);
		node.put("expandable", true);
		node.setIconClass("wtcon-icon-categoryOthers");
		return node;
	}
	
	private ExtTreeNode createCategoryFolderNode(boolean chooser, CategoryFSOrigin origin, CategoryFSFolder folder, boolean isDefault) {
		CategoryNodeId.Type type = CategoryNodeId.Type.FOLDER;
		final CategoryNodeId nodeId = CategoryNodeId.build(type, origin.getProfileId(), folder.getFolderId());
		final String name = folder.getDisplayName();
		final CategoryPropSet props = foldersPropsCache.get(folder.getFolderId()).orElse(null);
		final boolean active = !inactiveFolders.contains(folder.getFolderId());
		return createCategoryFolderNode(chooser, nodeId, name, folder.getPermissions(), folder.getCategory(), props, isDefault, active);
	}
	
	private ExtTreeNode createCategoryFolderNode(boolean chooser, CategoryNodeId nodeId, String name, FolderShare.Permissions folderPermissions, Category category, CategoryPropSet folderProps, boolean isDefault, boolean isActive) {
		String color = category.getColor();
		Category.Sync sync = Category.Sync.OFF;
		
		if (folderProps != null) { // Props are not null only for incoming folders
			if (folderProps.getHiddenOrDefault(false)) return null;
			color = folderProps.getColorOrDefault(color);
			sync = folderProps.getSyncOrDefault(sync);
		} else {
			sync = category.getSync();
		}
		
		ExtTreeNode node = new ExtTreeNode(nodeId.toString(), name, true);
		node.put("_foPerms", folderPermissions.getFolderPermissions().toString());
		node.put("_itPerms", folderPermissions.getItemsPermissions().toString());
		node.put("_builtIn", category.getBuiltIn());
		node.put("_provider", EnumUtils.toSerializedName(category.getProvider()));
		node.put("_color", Category.getHexColor(color));
		node.put("_sync", EnumUtils.toSerializedName(sync));
		node.put("_default", isDefault);
		node.put("_active", isActive);
		node.put("_isPrivate", category.getIsPrivate());
		if (!chooser) node.setChecked(isActive);
		return node;
	}
	
	public void processLookupCategoryFolders(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		List<JsCategoryLkp> items = new ArrayList<>();
		
		try {
			Integer defltCategoryId = manager.getDefaultCategoryId();
			for (CategoryFSOrigin origin : foldersTreeCache.getOrigins()) {
				for (CategoryFSFolder folder : foldersTreeCache.getFoldersByOrigin(origin)) {
					final CategoryPropSet props = foldersPropsCache.get(folder.getFolderId()).orElse(null);
					final boolean isDefault = folder.getFolderId().equals(defltCategoryId);
					items.add(new JsCategoryLkp(origin, folder, props, isDefault, items.size()));
				}
			}
			new JsonResult("folders", items, items.size()).printTo(out);
			
		} catch (Exception ex) {
			logger.error("Error in LookupCategoryFolders", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processManageSharing(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if (Crud.READ.equals(crud)) {
				String node = ServletUtils.getStringParameter(request, "id", true);
				
				CategoryNodeId nodeId = new CategoryNodeId(node);
				FolderSharing.Scope scope = JsCategorySharing.toFolderSharingScope(nodeId);
				Set<FolderSharing.SubjectConfiguration> configurations = manager.getFolderShareConfigurations(nodeId.getOriginAsProfileId(), scope);
				String[] sdn = buildSharingDisplayNames(nodeId);
				new JsonResult(new JsCategorySharing(nodeId, sdn[0], sdn[1], configurations)).printTo(out);
				
			} else if (Crud.UPDATE.equals(crud)) {
				Payload<MapItem, JsCategorySharing> pl = ServletUtils.getPayload(request, JsCategorySharing.class);
				
				CategoryNodeId nodeId = new CategoryNodeId(pl.data.id);
				FolderSharing.Scope scope = JsCategorySharing.toFolderSharingScope(nodeId);
				manager.updateFolderShareConfigurations(nodeId.getOriginAsProfileId(), scope, pl.data.toSubjectConfigurations());
				new JsonResult().printTo(out);
			}
			
		} catch (Exception ex) {
			logger.error("Error in ManageSharing", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	private String[] buildSharingDisplayNames(CategoryNodeId nodeId) throws WTException {
		String originDn = null, folderDn = null;
	
		CategoryFSOrigin origin = foldersTreeCache.getOrigin(nodeId.getOriginAsProfileId());
		if (origin instanceof MyCategoryFSOrigin) {
			originDn = lookupResource(ContactsLocale.CATEGORIES_MY);
		} else if (origin instanceof CategoryFSOrigin) {
			originDn = origin.getDisplayName();
		}
		
		if (CategoryNodeId.Type.FOLDER.equals(nodeId.getType())) {
			CategoryFSFolder folder = foldersTreeCache.getFolder(nodeId.getFolderId());
			folderDn = (folder != null) ? folder.getCategory().getName() : String.valueOf(nodeId.getFolderId());
		}
		
		return new String[]{originDn, folderDn};
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
				
				item = manager.addCategory(pl.data.createCategoryForInsert());
				foldersTreeCache.init(AbstractFolderTreeCache.Target.FOLDERS);
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				manager.updateCategory(pl.data.categoryId, pl.data.createCategoryForUpdate());
				foldersTreeCache.init(AbstractFolderTreeCache.Target.FOLDERS);
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.DELETE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				manager.deleteCategory(pl.data.categoryId);
				foldersTreeCache.init(AbstractFolderTreeCache.Target.FOLDERS);
				toggleActiveFolder(pl.data.categoryId, true); // forgets it by simply activating it
				new JsonResult().printTo(out);
				
			} else if (crud.equals("readLinks")) {
				int id = ServletUtils.getIntParameter(request, "id", true);
				final CategoryFSFolder folder = foldersTreeCache.getFolder(id);
				if (folder == null) throw new WTException("Category not found [{}]", id);
				
				Map<String, String> links = manager.getCategoryLinks(id);
				new JsonResult(new JsCategoryLinks(id, folder.getCategory().getName(), links)).printTo(out);
				
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
			
		} catch (Exception ex) {
			logger.error("Error in ManageCategory", ex);
			new JsonResult(ex).printTo(out);
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
				foldersTreeCache.init(AbstractFolderTreeCache.Target.FOLDERS);
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
			if (Crud.READ.equals(crud)) {
				String node = ServletUtils.getStringParameter(request, "node", true);
				
				CategoryNodeId nodeId = new CategoryNodeId(node);
				if (CategoryNodeId.Type.ORIGIN.equals(nodeId.getType())) {
					final CategoryFSOrigin origin = foldersTreeCache.getOrigin(nodeId.getOriginAsProfileId());
					if (origin instanceof MyCategoryFSOrigin) throw new WTException("Unsupported for personal origin");
					
					ArrayList<JsSimple> items = new ArrayList<>();
					for (CategoryFSFolder folder : foldersTreeCache.getFoldersByOrigin(origin)) {
						final CategoryPropSet props = foldersPropsCache.get(folder.getFolderId()).orElse(null);
						if ((props != null) && props.getHiddenOrDefault(false)) {
							items.add(new JsSimple(folder.getFolderId(), folder.getCategory().getName()));
						}
					}
					new JsonResult(items).printTo(out);
					
				} else {
					throw new WTException("Invalid node [{}]", node);
				}
				
			} else if (Crud.UPDATE.equals(crud)) {
				Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
				Boolean hidden = ServletUtils.getBooleanParameter(request, "hidden", false);
				
				updateCategoryFolderVisibility(categoryId, hidden);
				new JsonResult().printTo(out);
				
			} else if (Crud.DELETE.equals(crud)) {
				ServletUtils.StringArray ids = ServletUtils.getObjectParameter(request, "ids", ServletUtils.StringArray.class, true);
				
				HashSet<String> originProfileIds = new HashSet<>();
				for (String folderId : ids) {
					int categoryId = Integer.valueOf(folderId);
					CategoryFSOrigin origin = foldersTreeCache.getOriginByFolder(categoryId);
					if (origin == null) continue;
					originProfileIds.add(origin.getProfileId().toString());
					updateCategoryFolderVisibility(categoryId, null);
				}
				new JsonResult(originProfileIds).printTo(out);
			}
			
		} catch (Exception ex) {
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processSetDefaultCategory(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			
			us.setDefaultCategoryFolder(id);
			Integer defltCategoryId = manager.getDefaultCategoryId();
			new JsonResult(String.valueOf(defltCategoryId)).printTo(out);
				
		} catch (Exception ex) {
			logger.error("Error in SetDefaultCategory", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processSetCategoryColor(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			String color = ServletUtils.getStringParameter(request, "color", null);
			
			updateCategoryFolderColor(id, color);
			new JsonResult().printTo(out);
				
		} catch (Exception ex) {
			logger.error("Error in SetCategoryColor", ex);
			new JsonResult(ex).printTo(out);
		}
	}
				
	public void processSetCategorySync(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			Integer id = ServletUtils.getIntParameter(request, "id", true);
			String sync = ServletUtils.getStringParameter(request, "sync", null);
			
			updateCategoryFolderSync(id, EnumUtils.forSerializedName(sync, Category.Sync.class));
			new JsonResult().printTo(out);
				
		} catch (Exception ex) {
			logger.error("Error in SetCategorySync", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
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
				String queryText = ServletUtils.getStringParameter(request, "queryText", null);
				if (!StringUtils.isBlank(queryText)) {
					CoreManager core = WT.getCoreManager();
					core.saveMetaEntry(SERVICE_ID, META_CONTEXT_SEARCH, queryText, queryText, false);
				}
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
					final CategoryFSOrigin origin = foldersTreeCache.getOriginByFolder(item.getCategoryId());
					if (origin == null) continue;
					final CategoryFSFolder folder = foldersTreeCache.getFolder(item.getCategoryId());
					if (folder == null) continue;
					
					final CategoryPropSet props = foldersPropsCache.get(folder.getFolderId()).orElse(null);
					items.add(new JsGridContact(view, origin, folder, props, item));
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
				
				ArrayList<String> contactIds = new ArrayList<>();
				ArrayList<String> contactsListIds = new ArrayList<>();
				for (String uid : uids) {
					CompositeId cid = JsGridContact.Id.parse(uid);
					String contactId = JsGridContact.Id.contactId(cid);
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
				
				ArrayList<String> contactIds = new ArrayList<>();
				ArrayList<String> contactsListIds = new ArrayList<>();
				for (String uid : uids) {
					CompositeId cid = JsGridContact.Id.parse(uid);
					String contactId = JsGridContact.Id.contactId(cid);
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
				
				ArrayList<String> ids = new ArrayList<>();
				for (String uid : uids) {
					CompositeId cid = JsGridContact.Id.parse(uid);
					ids.add(JsGridContact.Id.contactId(cid));
				}
				manager.updateContactTags(op, ids, new HashSet<>(tags));
				
				new JsonResult().printTo(out);
			}
			
		} catch (Exception ex) {
			logger.error("Error in ManageGridContacts", ex);
			new JsonResult(ex).printTo(out);
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
				final CategoryFSOrigin origin = foldersTreeCache.getOriginByFolder(item.getCategoryId());
				if (origin == null) continue;
				final CategoryFSFolder folder = foldersTreeCache.getFolder(item.getCategoryId());
				if (folder == null) continue;

				final CategoryPropSet props = foldersPropsCache.get(folder.getFolderId()).orElse(null);
				items.add(new JsContactLkp(origin, folder, props, item));
			}
			new JsonResult(items, result.fullCount)
				.setPage(page)
				.printTo(out);
			
		} catch (Exception ex) {
			logger.error("Error in CustomFieldContactPicker", ex);
			new JsonResult(ex).printTo(out);
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
				String contactId = JsGridContact.Id.contactId(cid);
				boolean isList = JsGridContact.Id.isList(cid);
				
				if (isList) {
					ContactList contactsList = manager.getContactList(contactId);
					final CategoryFSFolder folder = foldersTreeCache.getFolder(contactsList.getCategoryId());
					if (folder == null) throw new WTException("Folder not found [{}]", contactsList.getCategoryId());
					final CategoryPropSet props = foldersPropsCache.get(folder.getFolderId()).orElse(null);
					
					new JsonResult(new JsContactPreview(folder, props, contactsList)).printTo(out);
					
				} else {
					UserProfile up = getEnv().getProfile();
					Contact contact = manager.getContact(contactId, BitFlags.with(ContactGetOption.PICTURE, ContactGetOption.TAGS, ContactGetOption.CUSTOM_VALUES));
					if (contact == null) throw new WTException("Contact not found [{}]", contactId);
					ContactCompany company = contact.hasCompany() ? manager.getContactCompany(contactId) : null;
					
					final CategoryFSOrigin origin = foldersTreeCache.getOriginByFolder(contact.getCategoryId());
					if (origin == null) throw new WTException("Origin not found [{}]", contact.getCategoryId());
					final CategoryFSFolder folder = foldersTreeCache.getFolder(contact.getCategoryId());
					if (folder == null) throw new WTException("Folder not found [{}]", contact.getCategoryId());
					final CategoryPropSet props = foldersPropsCache.get(folder.getFolderId()).orElse(null);
					
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
					
					new JsonResult(new JsContactPreview(origin, folder, props, contact, company, cpanels.values(), cfields, up.getLanguageTag(), up.getTimeZone())).printTo(out);
				}
			}
			
		} catch (Exception ex) {
			logger.error("Error in ManageContactDetails", ex);
			new JsonResult(ex).printTo(out);	
		}
	}
	
	public void processExpandRecipientsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		CoreManager coreMgr = WT.getCoreManager();
		
		try {
			String emailAddress = ServletUtils.getStringParameter(request, "address", true);
			
			List<JsRecipient> jsrcpts = new ArrayList<>();
			List<Recipient> recipients = coreMgr.expandVirtualProviderRecipient(emailAddress);
			recipients.forEach(recipient -> {
				jsrcpts.add(new JsRecipient(recipient.getType().toString(), InternetAddressUtils.toFullAddress(recipient.getAddress(), recipient.getPersonal())));
			});
				
			new JsonResult(jsrcpts).printTo(out);
		
		} catch (Exception ex) {
			logger.error("Error in ExpandListRecipient", ex);
			new JsonResult(ex).printTo(out);
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
				
				Contact contact = manager.getContact(id);
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
					contact.addAttachment(att);
				}
				manager.addContact(contact);
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsContact> pl = ServletUtils.getPayload(request, JsContact.class);
				
				BitFlags<ContactUpdateOption> processOpts = BitFlags.with(ContactUpdateOption.TAGS, ContactUpdateOption.ATTACHMENTS, ContactUpdateOption.CUSTOM_VALUES);
				Contact contact = pl.data.createContactForUpdate(up.getTimeZone());
				
				// We reuse picture field passing the uploaded file ID.
				// Due to different formats we can be sure that IDs don't collide.
				if (hasUploadedFile(pl.data.picture)) {
					// If found, new picture has been uploaded!
					processOpts.set(ContactUpdateOption.PICTURE);
					contact.setPicture(getUploadedContactPicture(pl.data.picture));
				} else {
					// If blank, picture has been deleted!
					if (StringUtils.isBlank(pl.data.picture)) processOpts.set(ContactUpdateOption.PICTURE);
				}
				for (JsContact.Attachment jsatt : pl.data.attachments) {
					if (!StringUtils.isBlank(jsatt._uplId)) {
						UploadedFile upFile = getUploadedFileOrThrow(jsatt._uplId);
						ContactAttachmentWithStream att = new ContactAttachmentWithStream(upFile.getFile());
						att.setAttachmentId(jsatt.id);
						att.setFilename(upFile.getFilename());
						att.setSize(upFile.getSize());
						att.setMediaType(upFile.getMediaType());
						contact.addAttachment(att);
					} else {
						ContactAttachment att = new ContactAttachment();
						att.setAttachmentId(jsatt.id);
						att.setFilename(jsatt.name);
						att.setSize(jsatt.size);
						contact.addAttachment(att);
					}
				}
				manager.updateContact(contact.getContactId(), contact, processOpts);
				new JsonResult().printTo(out);
				
			} else if (crud.equals(Crud.DELETE)) {
				StringArray ids = ServletUtils.getObjectParameter(request, "ids", StringArray.class, true);
				
				if (ids.size() == 1) {
					manager.deleteContact(ids.get(0));
				} else {
					manager.deleteContact(ids);
				}
				
				new JsonResult().printTo(out);
			}
			
		} catch (Exception ex) {
			logger.error("Error in ManageContacts", ex);
			new JsonResult(ex).printTo(out);	
		}
	}
	
	public void processGetContactInformationForEventCreation(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			String id = ServletUtils.getStringParameter(request, "id", true);
			String type = ServletUtils.getStringParameter(request, "type", true);
			
			Contact contact = manager.getContact(id, BitFlags.with(ContactGetOption.TAGS));
			JsEventContact eventContact = JsEventContact.createJsEventContact(contact, type);
			
			new JsonResult(eventContact).printTo(out);
			
		} catch (ParameterException ex) {
			logger.error("Error in GetContactInformationForEventCreation", ex);
			new JsonResult(false, "Error").printTo(out);	
		} catch (Exception ex) {
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
				picture = manager.getContactPicture(id);
			}
			if (picture != null) {
				try (ByteArrayInputStream bais = new ByteArrayInputStream(picture.getBytes())) {
					ServletUtils.writeContent(response, bais, picture.getMediaType());
				}
			}
			
		} catch (Exception ex) {
			logger.error("Error in GetContactPicture", ex);
		}
	}
	
	public void processDownloadContactAttachment(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			boolean inline = ServletUtils.getBooleanParameter(request, "inline", false);
			String attachmentId = ServletUtils.getStringParameter(request, "attachmentId", null);
			
			if (!StringUtils.isBlank(attachmentId)) {
				String contactId = ServletUtils.getStringParameter(request, "contactId", true);
				
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
			
		} catch (Throwable t) {
			logger.error("Error in DownloadContactAttachment", t);
			ServletUtils.writeErrorHandlingJs(response, t.getMessage());
		}
	}
	
	public void processGetCustomFieldsDefsData(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		CoreManager coreMgr = WT.getCoreManager();
		UserProfile up = getEnv().getProfile();
		
		try {
			ServletUtils.StringArray tags = ServletUtils.getObjectParameter(request, "tags", ServletUtils.StringArray.class, true);
			String contactId = ServletUtils.getStringParameter(request, "id", false);
			
			Map<String, CustomPanel> cpanels = coreMgr.listCustomPanelsUsedBy(SERVICE_ID, tags);
			Map<String, CustomFieldValue> cvalues = (contactId != null) ? manager.getContactCustomValues(contactId) : null;
			Map<String, CustomField> cfields = new HashMap<>();
			for (CustomPanel cpanel : cpanels.values()) {
				for (String fieldId : cpanel.getFields()) {
					CustomField cfield = coreMgr.getCustomField(SERVICE_ID, fieldId);
					if (cfield != null) cfields.put(fieldId, cfield);
				}
			}
			new JsonResult(new JsCustomFieldDefsData(cpanels.values(), cfields, cvalues, up.getLanguageTag(), up.getTimeZone())).setTotal(cfields.size()).printTo(out);
			
		} catch (Exception ex) {
			logger.error("Error in GetCustomFieldsDefsData", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processManageContactsLists(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		JsContactsList item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				
				ContactList list = manager.getContactList(id);
				UserProfileId ownerId = manager.getCategoryOwner(list.getCategoryId());
				item = new JsContactsList(ownerId, list);
				
				new JsonResult(item).printTo(out);
				
			} else if(crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsContactsList> pl = ServletUtils.getPayload(request, JsContactsList.class);
				
				ContactListEx contact = JsContactsList.createContactListRecipientForInsert(pl.data);
				manager.addContactList(contact);
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsContactsList> pl = ServletUtils.getPayload(request, JsContactsList.class);
				
				ContactListEx contact = JsContactsList.createContactListRecipientForUpdate(pl.data);
				manager.updateContactList(pl.data.contactId, contact);
				
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				StringArray ids = ServletUtils.getObjectParameter(request, "ids", StringArray.class, true);
				
				if (ids.size() == 1) {
					manager.deleteContactList(ids.get(0));
				} else {
					manager.deleteContactList(ids);
				}
				
				new JsonResult().printTo(out);
			}
			
		} catch (Exception ex) {
			logger.error("Error in ManageContactsLists", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processAddToContactsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		try {
			String virtualRecipient = ServletUtils.getStringParameter(request, "list", true);
			String recipientType = ServletUtils.getStringParameter(request, "recipientType", true);
			ArrayList<String> emails=ServletUtils.getStringParameters(request, "emails");
			
			InternetAddress iaVirtualRecipient = InternetAddressUtils.toInternetAddress(virtualRecipient);
			if (iaVirtualRecipient == null) throw new WTException("Unable to parse '{}' as internetAddress", virtualRecipient);
			String listId = ContactsUtils.virtualRecipientToListId(iaVirtualRecipient);
			if (listId == null) throw new WTException("Recipient address is not a list [{}]", iaVirtualRecipient.getAddress());
			
			ArrayList<ContactListRecipientBase> recipients = new ArrayList<>();
			for (String email: emails) {
				ContactListRecipient rcpt = new ContactListRecipient();
				rcpt.setRecipient(email);
				rcpt.setRecipientType(EnumUtils.forSerializedName(recipientType, ContactListRecipientBase.RecipientType.class));
				recipients.add(rcpt);
			}
			manager.updateContactsListRecipients(listId, recipients, true);
			new JsonResult().printTo(out);
			
		} catch (Exception ex) {
			logger.error("Error in AddToContactsList", ex);
			new JsonResult(ex).printTo(out);
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
			
		} catch (Exception ex) {
			logger.error("Error in action ImportContactsFromText", ex);
			new JsonResult(ex).printTo(out);
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
			
		} catch (Exception ex) {
			logger.error("Error in action ImportContactsFromExcel", ex);
			new JsonResult(ex).printTo(out);
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
			
		} catch (Exception ex) {
			logger.error("Error in action ImportContactsFromVCard", ex);
			new JsonResult(ex).printTo(out);
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
			
		} catch (Exception ex) {
			logger.error("Error in action ImportContactsFromVCard", ex);
			new JsonResult(ex).printTo(out);
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
				final CategoryFSFolder folder = foldersTreeCache.getFolder(item.getCategoryId());
				if (folder == null) continue;

				final CategoryPropSet props = foldersPropsCache.get(folder.getFolderId()).orElse(null);
				items.add(new RBAddressbook(folder.getCategory(), props, item));
			}
			
			ReportConfig.Builder builder = reportConfigBuilder();
			RptAddressbook rpt = new RptAddressbook(builder.build());
			rpt.setDataSource(items);
			
			ServletUtils.setFileStreamHeaders(response, filename + ".pdf");
			WT.generateReportToStream(rpt, AbstractReport.OutputType.PDF, response.getOutputStream());
			
		} catch (Throwable t) {
			logger.error("Error in PrintAddressbook", t);
			ServletUtils.writeErrorHandlingJs(response, t.getMessage());
		}
	}
	
	public void processPrintContactsDetail(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<RBContactDetail> items = new ArrayList<>();
		
		try {
			String filename = ServletUtils.getStringParameter(request, "filename", "print");
			StringArray ids = ServletUtils.getObjectParameter(request, "ids", StringArray.class, true);
			
			Category category = null;
			for (String id : ids) {
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
			
		} catch (Throwable t) {
			logger.error("Error in action PrintContact", t);
			ServletUtils.writeErrorHandlingJs(response, t.getMessage());
		}
	}
	
	public void processPrepareSendContactAsEmail(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<MapItem> items = new ArrayList<>();
		
		try {
			String tag = ServletUtils.getStringParameter(request, "uploadTag", true);
			StringArray ids = ServletUtils.getObjectParameter(request, "ids", StringArray.class, true);
			
			String prodId = VCardUtils.buildProdId(ManagerUtils.getProductName());
			VCardOutput vout = new VCardOutput(prodId)
				.withEnableCaretEncoding(manager.VCARD_CARETENCODINGENABLED);
			for (String id : ids) {
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
			
		} catch (Exception ex) {
			logger.error("Error in PrepareSendContactAsEmail", ex);
			new JsonResult(ex).printTo(out);
		}
	}
	
	public void processExportContactsToText(HttpServletRequest request, HttpServletResponse response) {
		try {
			String node = ServletUtils.getStringParameter(request, "id", true);
			CategoryNodeId nodeId = new CategoryNodeId(node);

			CSVOutput csvout = new CSVOutput();
			
			if (nodeId.getType().equals(CategoryNodeId.Type.FOLDER)) {
				int categoryId = nodeId.getFolderId();
				Category category = manager.getCategory(nodeId.getFolderId());
				ServletUtils.setFileStreamHeaders(response, "text/csv", DispositionType.INLINE, 
						"Contacts-"+nodeId.getOriginAsProfileId().getUserId()+"-"+category.getName()+".csv");
				CsvListWriter wr=new CsvListWriter(new PrintWriter(response.getOutputStream()), CsvPreference.STANDARD_PREFERENCE);
				csvout.writeHeader(wr);
				outputCSVContacts(categoryId, category.getName(), csvout, wr);
				wr.close();
			} 
			else if (nodeId.getType().equals(CategoryNodeId.Type.ORIGIN)) {
				ServletUtils.setFileStreamHeaders(response, "text/csv", DispositionType.INLINE, 
						"Contacts-"+nodeId.getOriginAsProfileId().getUserId()+".csv");
				CsvListWriter wr=new CsvListWriter(new PrintWriter(response.getOutputStream()), CsvPreference.STANDARD_PREFERENCE);
				csvout.writeHeader(wr);
				for (CategoryFSFolder folder : foldersTreeCache.getFoldersByOrigin(nodeId.getOriginAsProfileId())) {
					Category category = folder.getCategory();
					outputCSVContacts(category.getCategoryId(), category.getName(), csvout, wr);
					wr.flush();
				}
				wr.close();
			}
		} catch (Exception exc) {
			Service.logger.error("Exception",exc);
			ServletUtils.writeErrorHandlingJs(response, exc.getMessage());
		}
	}
	
	public void processExportContactsToVCard(HttpServletRequest request, HttpServletResponse response) {
		try {
			String node = ServletUtils.getStringParameter(request, "id", true);
			CategoryNodeId nodeId = new CategoryNodeId(node);

			if (nodeId.getType().equals(CategoryNodeId.Type.FOLDER)) {
				OutputStream out = response.getOutputStream();
				Category category = manager.getCategory(nodeId.getFolderId());
				ServletUtils.setFileStreamHeaders(response, "text/x-vcard", DispositionType.INLINE, 
						"Contacts-"+nodeId.getOriginAsProfileId().getUserId()+"-"+category.getName()+".vcf");
				manager.outputVCardContactsByCategoryId(category, out);
				out.close();
			} 
			else if (nodeId.getType().equals(CategoryNodeId.Type.ORIGIN)) {
				ServletUtils.setFileStreamHeaders(response, "application/x-zip-compressed", DispositionType.INLINE, 
						"Contacts-"+nodeId.getOriginAsProfileId().getUserId()+".zip");
				ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
				ArrayList<Category> categories=new ArrayList<>();
				for (CategoryFSFolder folder : foldersTreeCache.getFoldersByOrigin(nodeId.getOriginAsProfileId()))
					categories.add(folder.getCategory());
				manager.outputVCardContactsAsZipEntries(categories, zos);
				zos.close();
			}
		} catch (Exception exc) {
			Service.logger.error("Exception",exc);
			ServletUtils.writeErrorHandlingJs(response, exc.getMessage());
		}
	}
	
	private void outputCSVContacts(int categoryId, String categoryName, CSVOutput csvout, CsvListWriter wr) throws WTException, IOException {
		List<ContactObject> contacts = manager.listContactObjects(categoryId, ContactObjectOutputType.BEAN);
		Map<String, String> tagNamesById = WT.getCoreManager().listTagNamesById();
		for (ContactObject contact : contacts) {
			ContactObjectWithBean contactObj = (ContactObjectWithBean)contact;
			csvout.writeContact(contactObj.getContact(), categoryName, wr, tagNamesById);
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
		for (CategoryFSOrigin origin : getActiveOrigins()) {
			boolean isOthersChildren = !(origin instanceof MyCategoryFSOrigin);
			for (CategoryFSFolder folder: foldersTreeCache.getFoldersByOrigin(origin)) {
				if ((isOthersChildren && inactiveOrigins.contains(CategoryNodeId.GROUPER_OTHERS_ORIGIN))
					|| (inactiveFolders.contains(folder.getFolderId()))
				) continue;
				ids.add(folder.getFolderId());
			}
		}
		return ids;
	}
	
	private List<CategoryFSOrigin> getActiveOrigins() {
		return foldersTreeCache.getOrigins().stream()
			.filter(origin -> !inactiveOrigins.contains(toInactiveOriginKey(origin)))
			.collect(Collectors.toList());
	}
	
	private boolean shouldCleanupInactiveOriginKey(String originKey) {
		return !foldersTreeCache.existsOrigin(UserProfileId.parseQuielty(originKey));
	}
	
	private boolean isOriginActive(String originKey) {
		return !inactiveOrigins.contains(originKey);
	}
	
	private String toInactiveOriginKey(CategoryNodeId nodeId) {
		return nodeId.getOrigin();
	}
	
	private String toInactiveOriginKey(CategoryFSOrigin origin) {
		return origin.getProfileId().toString();
	}
	
	private void toggleActiveOrigin(String originKey, boolean active) {
		toggleActiveOrigins(new String[]{originKey}, active);
	}
	
	private void toggleActiveOrigins(String[] originKeys, boolean active) {	
		try {
			locks.tryLock("inactiveOrigins", 60, TimeUnit.SECONDS);
			for (String originId : originKeys) {
				if (active) {
					inactiveOrigins.remove(originId);
				} else {
					inactiveOrigins.add(originId);
				}
			}
			us.setInactiveCategoryOrigins(inactiveOrigins);
			
		} catch (InterruptedException ex) {
			// Do nothing...
		} finally {
			locks.unlock("inactiveOrigins");
		}
	}
	
	private void toggleActiveFolder(Integer folderId, boolean active) {
		toggleActiveFolders(new Integer[]{folderId}, active);
	}
	
	private void toggleActiveFolders(Integer[] folderIds, boolean active) {
		try {
			locks.tryLock("inactiveFolders", 60, TimeUnit.SECONDS);
			for (int folderId : folderIds) {
				if (active) {
					inactiveFolders.remove(folderId);
				} else {
					inactiveFolders.add(folderId);
				}
			}
			us.setInactiveCategoryFolders(inactiveFolders);
			
		} catch (InterruptedException ex) {
			// Do nothing...
		} finally {
			locks.unlock("inactiveFolders");
		}
	}
	
	private void updateCategoryFolderVisibility(int categoryId, Boolean hidden) {
		try {
			locks.tryLock("folderVisibility-"+categoryId, 60, TimeUnit.SECONDS);
			CategoryPropSet pset = manager.getCategoryCustomProps(categoryId);
			pset.setHidden(hidden);
			manager.updateCategoryCustomProps(categoryId, pset);
			
			final CategoryFSFolder folder = foldersTreeCache.getFolder(categoryId);
			if (!(folder instanceof MyCategoryFSFolder)) {
				foldersPropsCache.put(categoryId, Optional.of(pset));
			}
		
		} catch (WTException ex) {
			logger.error("Error saving custom calendar props", ex);
		} catch (InterruptedException ex) {
			// Do nothing...
		} finally {
			locks.unlock("folderVisibility-"+categoryId);
		}
	}
	
	private void updateCategoryFolderColor(int categoryId, String color) throws WTException {
		final CategoryFSOrigin origin = foldersTreeCache.getOriginByFolder(categoryId);
		if (origin instanceof MyCategoryFSOrigin) {
			Category category = manager.getCategory(categoryId);
			category.setColor(color);
			manager.updateCategory(category.getCategoryId(), category);
			foldersTreeCache.init(AbstractFolderTreeCache.Target.FOLDERS);
			
		} else if (origin instanceof CategoryFSOrigin) {
			CategoryPropSet props = manager.getCategoryCustomProps(categoryId);
			props.setColor(color);
			manager.updateCategoryCustomProps(categoryId, props);
			foldersPropsCache.put(categoryId, Optional.of(props));
		}
	}
	
	private void updateCategoryFolderSync(int categoryId, Category.Sync sync) throws WTException {
		final CategoryFSOrigin origin = foldersTreeCache.getOriginByFolder(categoryId);
		if (origin instanceof MyCategoryFSOrigin) {
			Category category = manager.getCategory(categoryId);
			category.setSync(sync);
			manager.updateCategory(category.getCategoryId(), category);
			foldersTreeCache.init(AbstractFolderTreeCache.Target.FOLDERS);
			
		} else if (origin instanceof CategoryFSOrigin) {
			CategoryPropSet props = manager.getCategoryCustomProps(categoryId);
			props.setSync(sync);
			manager.updateCategoryCustomProps(categoryId, props);
			foldersPropsCache.put(categoryId, Optional.of(props));
		}
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
	
	private class FoldersPropsCacheLoader implements CacheLoader<Integer, Optional<CategoryPropSet>> {
		@Override
		public Optional<CategoryPropSet> load(Integer k) throws Exception {
			try {
				logger.trace("[FoldersPropsCache] Loading... [{}]", k);
				final CategoryFSOrigin origin = foldersTreeCache.getOriginByFolder(k);
				if (origin == null) return Optional.empty(); // Disable lookup for unknown folder IDs
				if (origin instanceof MyCategoryFSOrigin) return Optional.empty(); // Disable lookup for personal folder IDs
				return Optional.ofNullable(manager.getCategoryCustomProps(k));
				
			} catch (Exception ex) {
				logger.error("[FoldersPropsCache] Unable to load [{}]", k);
				return null;
			}
		}
	}
	
	private class FoldersTreeCache extends AbstractFolderTreeCache<Integer, CategoryFSOrigin, CategoryFSFolder, Object> {
		
		@Override
		protected void internalBuildCache(AbstractFolderTreeCache.Target options) {
			UserProfileId pid = getEnv().getProfile().getId();
				
			if (AbstractFolderTreeCache.Target.ALL.equals(options) || AbstractFolderTreeCache.Target.ORIGINS.equals(options)) {
				try {
					this.internalClear(AbstractFolderTreeCache.Target.ORIGINS);
					this.origins.put(pid, new MyCategoryFSOrigin(pid));
					for (CategoryFSOrigin origin : manager.listIncomingCategoryOrigins().values()) {
						this.origins.put(origin.getProfileId(), origin);
					}
					
				} catch (WTException ex) {
					logger.error("[FoldersTreeCache] Error updating Origins", ex);
				}
			}	
			if (AbstractFolderTreeCache.Target.ALL.equals(options) || AbstractFolderTreeCache.Target.FOLDERS.equals(options)) {
				try {
					this.internalClear(AbstractFolderTreeCache.Target.FOLDERS);
					for (CategoryFSOrigin origin : this.origins.values()) {
						if (origin instanceof MyCategoryFSOrigin) {
							for (Category category : manager.listMyCategories().values()) {
								final MyCategoryFSFolder folder = new MyCategoryFSFolder(category.getCategoryId(), category);
								this.folders.put(folder.getFolderId(), folder);
								this.foldersByOrigin.put(origin.getProfileId(), folder);
								this.originsByFolder.put(folder.getFolderId(), origin);
							}
						} else if (origin instanceof CategoryFSOrigin) {
							for (CategoryFSFolder folder : manager.listIncomingCategoryFolders(origin.getProfileId()).values()) {
								// Make sure to track only folders with at least READ premission: 
								// it is ugly having in UI empty folder nodes for just manage update/delete/sharing operations.
								if (!folder.getPermissions().getFolderPermissions().has(FolderShare.FolderRight.READ)) continue;
								
								this.folders.put(folder.getFolderId(), folder);
								this.foldersByOrigin.put(origin.getProfileId(), folder);
								this.originsByFolder.put(folder.getFolderId(), origin);
							}
						}
					}
					
				} catch (WTException ex) {
					logger.error("[FoldersTreeCache] Error updating Folders", ex);
				}
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
