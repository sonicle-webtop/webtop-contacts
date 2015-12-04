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

import com.sonicle.commons.web.Crud;
import com.sonicle.commons.web.ServletUtils;
import com.sonicle.commons.web.json.CompositeId;
import com.sonicle.commons.web.json.PayloadAsList;
import com.sonicle.commons.web.json.JsonResult;
import com.sonicle.commons.web.json.MapItem;
import com.sonicle.commons.web.json.Payload;
import com.sonicle.commons.web.json.extjs.ExtFieldMeta;
import com.sonicle.commons.web.json.extjs.ExtGridColumnMeta;
import com.sonicle.commons.web.json.extjs.ExtGridMetaData;
import com.sonicle.commons.web.json.extjs.ExtTreeNode;
import com.sonicle.webtop.contacts.ContactsUserSettings.CheckedFolders;
import com.sonicle.webtop.contacts.ContactsUserSettings.CheckedRoots;
import com.sonicle.webtop.contacts.bol.OCategory;
import com.sonicle.webtop.contacts.bol.VContact;
import com.sonicle.webtop.contacts.bol.js.JsContact;
import com.sonicle.webtop.contacts.bol.js.JsCategory;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLkp;
import com.sonicle.webtop.contacts.bol.js.JsFolderNode;
import com.sonicle.webtop.contacts.bol.js.JsFolderNode.JsFolderNodeList;
import com.sonicle.webtop.contacts.bol.js.JsSharing;
import com.sonicle.webtop.contacts.bol.model.CategoryFolder;
import com.sonicle.webtop.contacts.bol.model.CategoryRoot;
import com.sonicle.webtop.contacts.bol.model.Contact;
import com.sonicle.webtop.contacts.bol.model.ContactPicture;
import com.sonicle.webtop.contacts.bol.model.ContactsList;
import com.sonicle.webtop.contacts.bol.model.MyCategoryFolder;
import com.sonicle.webtop.contacts.bol.model.MyCategoryRoot;
import com.sonicle.webtop.contacts.directory.DirectoryElement;
import com.sonicle.webtop.contacts.directory.DirectoryManager;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.WT;
import com.sonicle.webtop.core.WebTopSession.UploadedFile;
import com.sonicle.webtop.core.bol.OCustomer;
import com.sonicle.webtop.core.bol.js.JsSimple;
import com.sonicle.webtop.core.bol.model.SharePermsRoot;
import com.sonicle.webtop.core.bol.model.Sharing;
import com.sonicle.webtop.core.sdk.BaseService;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.WTException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 *
 * @author malbinola
 */
public class Service extends BaseService {
	public static final Logger logger = WT.getLogger(Service.class);
	
	private ContactsManager manager;
	private ContactsUserSettings us;
	
	public static final String EVENTS_EXPORT_FILENAME = "events_{0}-{1}-{2}.{3}";
	
	private final LinkedHashMap<String, CategoryRoot> roots = new LinkedHashMap<>();
	private CheckedRoots checkedRoots = null;
	private CheckedFolders checkedFolders = null;
	private ArrayList<ExtFieldMeta> gridFieldsW = null;
	private ArrayList<ExtFieldMeta> gridFieldsH = null;
	private ArrayList<ExtGridColumnMeta> gridColsInfoW = null;
	private ArrayList<ExtGridColumnMeta> gridColsInfoH = null;
	
	//private ExportWizard wizard = null;
	
	@Override
	public void initialize() throws Exception {
		UserProfile profile = getEnv().getProfile();
		manager = new ContactsManager(getRunContext());
		//manager.initializeDirectories(profile);
		us = new ContactsUserSettings(SERVICE_ID, profile.getId());
		initFolders();
		gridFieldsW = buildFields("w");
		gridFieldsH = buildFields("h");
		gridColsInfoW = buildColsInfo("w");
		gridColsInfoH = buildColsInfo("h");
	}
	
	@Override
	public void cleanup() throws Exception {
		checkedFolders.clear();
		checkedFolders = null;
		checkedRoots.clear();
		checkedRoots = null;
		us = null;
		//manager.cleanupDirectories();
		manager = null;
	}
	
	@Override
	public ClientOptions returnClientOptions() {
		ClientOptions co = new ClientOptions();
		co.put("view", us.getView());
		return co;
	}
	
	private void initFolders() throws Exception {
		UserProfile.Id pid = getEnv().getProfile().getId();
		synchronized(roots) {
			roots.clear();
			roots.put(MyCategoryRoot.SHARE_ID, new MyCategoryRoot(pid));
			for(CategoryRoot root : manager.listIncomingCategoryRoots()) {
				roots.put(root.getShareId(), root);
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
	
	public void processManageFoldersTree(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<ExtTreeNode> children = new ArrayList<>();
		ExtTreeNode child = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String node = ServletUtils.getStringParameter(request, "node", true);
				
				if(node.equals("root")) { // Node: root -> list roots
					for(CategoryRoot root : roots.values()) {
						children.add(createRootNode(root));
					}
				} else { // Node: folder -> list folders (categories)
					CategoryRoot root = roots.get(node);
					
					if(root instanceof MyCategoryRoot) {
						for(OCategory cal : manager.listCategories()) {
							MyCategoryFolder folder = new MyCategoryFolder(node, cal);
							children.add(createFolderNode(folder, root.getPerms()));
						}
					} else {
						for(CategoryFolder folder : manager.listIncomingCategoryFolders(node)) {
							children.add(createFolderNode(folder, root.getPerms()));
						}
					}
				}
				new JsonResult("children", children).printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				PayloadAsList<JsFolderNodeList> pl = ServletUtils.getPayloadAsList(request, JsFolderNodeList.class);
				
				for(JsFolderNode node : pl.data) {
					if(node._type.equals(JsFolderNode.TYPE_ROOT)) {
						toggleCheckedRoot(node.id, node._visible);
						
					} else if(node._type.equals(JsFolderNode.TYPE_FOLDER)) {
						CompositeId cid = new CompositeId().parse(node.id);
						toggleCheckedFolder(Integer.valueOf(cid.getToken(1)), node._visible);
					}
				}
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				PayloadAsList<JsFolderNodeList> pl = ServletUtils.getPayloadAsList(request, JsFolderNodeList.class);
				
				for(JsFolderNode node : pl.data) {
					if(node._type.equals(JsFolderNode.TYPE_FOLDER)) {
						CompositeId cid = new CompositeId().parse(node.id);
						manager.deleteCategory(Integer.valueOf(cid.getToken(1)));
					}
				}
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error executing action ManageFoldersTree", ex);
		}
	}
	
	public void processLookupCategoryRoots(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		List<JsSimple> items = new ArrayList<>();
		
		try {
			Boolean writableOnly = ServletUtils.getBooleanParameter(request, "writableOnly", true);
			
			for(CategoryRoot root : roots.values()) {
				if(root instanceof MyCategoryRoot) {
					UserProfile up = getEnv().getProfile();
					items.add(new JsSimple(up.getStringId(), up.getDisplayName()));
				} else {
					//TODO: se writableOnly verificare che il gruppo condiviso sia scrivibile
					items.add(new JsSimple(root.getOwnerProfileId().toString(), root.getDescription()));
				}
			}
			
			new JsonResult("roots", items, items.size()).printTo(out);
			
		} catch(Exception ex) {
			logger.error("Error executing action LookupRootFolders", ex);
			new JsonResult(false, "Error").printTo(out);	
		}
	}
	
	public void processLookupCategoryFolders(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		List<JsCategoryLkp> items = new ArrayList<>();
		
		try {
			for(CategoryRoot root : roots.values()) {
				if(root instanceof MyCategoryRoot) {
					for(OCategory cal : manager.listCategories()) {
						items.add(new JsCategoryLkp(cal));
					}
				} else {
					for(CategoryFolder folder : manager.listIncomingCategoryFolders(root.getShareId())) {
						if(!folder.getElementsPerms().implies("CREATE")) continue;
						items.add(new JsCategoryLkp(folder.getCategory()));
					}
				}
			}
			new JsonResult("folders", items, items.size()).printTo(out);
			
		} catch(Exception ex) {
			logger.error("Error in action LookupCategoryFolders", ex);
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
	
	public void processManageCategories(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		OCategory item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				Integer id = ServletUtils.getIntParameter(request, "id", true);
				
				item = manager.getCategory(id);
				new JsonResult(new JsCategory(item)).printTo(out);
				
			} else if(crud.equals(Crud.CREATE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				item = manager.addCategory(JsCategory.buildFolder(pl.data));
				toggleCheckedFolder(item.getCategoryId(), true);
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				manager.updateCategory(JsCategory.buildFolder(pl.data));
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.DELETE)) {
				Payload<MapItem, JsCategory> pl = ServletUtils.getPayload(request, JsCategory.class);
				
				manager.deleteCategory(pl.data.categoryId);
				new JsonResult().printTo(out);
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ManageCategories", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	private ArrayList<ExtFieldMeta> buildFields(String view) {
		ArrayList<ExtFieldMeta> fields = new ArrayList<>();
		
		fields.add(new ExtFieldMeta("uid").setType("string"));
		fields.add(new ExtFieldMeta("contactId").setType("int"));
		fields.add(new ExtFieldMeta("categoryId"));
		fields.add(new ExtFieldMeta("categoryName"));
		fields.add(new ExtFieldMeta("categoryColor"));
		fields.add(new ExtFieldMeta("listId").setType("int"));
		if(view.equals("w")) fields.add(new ExtFieldMeta("title").setType("string"));
		fields.add(new ExtFieldMeta("firstName").setType("string"));
		fields.add(new ExtFieldMeta("lastName").setType("string"));
		if(view.equals("h")) fields.add(new ExtFieldMeta("nickname").setType("string"));
		if(view.equals("w")) {
			fields.add(new ExtFieldMeta("company").setType("string"));
			fields.add(new ExtFieldMeta("function").setType("string"));
			//fields.add(new ExtFieldMeta("caddress").setType("string"));
			//fields.add(new ExtFieldMeta("ccity").setType("string"));
			fields.add(new ExtFieldMeta("ctelephone").setType("string"));
			fields.add(new ExtFieldMeta("cmobile").setType("string"));
			fields.add(new ExtFieldMeta("cemail").setType("string"));
		}
		if(view.equals("h")) {
			fields.add(new ExtFieldMeta("htelephone").setType("string"));
			fields.add(new ExtFieldMeta("hemail").setType("string"));
			fields.add(new ExtFieldMeta("hbirthday").setType("string"));
		}
		
		fields.add(new ExtFieldMeta("_profileId"));
		
		return fields;
	}
	
	private ArrayList<ExtGridColumnMeta> buildColsInfo(String view) {
		ArrayList<ExtGridColumnMeta> colsInfo = new ArrayList<>();
		
		colsInfo.add(new ExtGridColumnMeta("contactId").setHidden(true));
		colsInfo.add(new ExtGridColumnMeta("categoryId"));
		//colsInfo.add(new ExtGridColumnMeta("categoryName").setHidden(true));
		//colsInfo.add(new ExtGridColumnMeta("categoryColor").setHidden(true));
		if(view.equals("w")) colsInfo.add(new ExtGridColumnMeta("title"));
		colsInfo.add(new ExtGridColumnMeta("firstName"));
		colsInfo.add(new ExtGridColumnMeta("lastName"));
		if(view.equals("h")) colsInfo.add(new ExtGridColumnMeta("nickname"));
		if(view.equals("w")) {
			colsInfo.add(new ExtGridColumnMeta("company"));
			colsInfo.add(new ExtGridColumnMeta("function"));
			//colsInfo.add(new ExtGridColumnMeta("caddress"));
			//colsInfo.add(new ExtGridColumnMeta("ccity"));
			colsInfo.add(new ExtGridColumnMeta("ctelephone"));
			colsInfo.add(new ExtGridColumnMeta("cmobile"));
			colsInfo.add(new ExtGridColumnMeta("cemail"));
		}
		if(view.equals("h")) {
			colsInfo.add(new ExtGridColumnMeta("htelephone"));
			colsInfo.add(new ExtGridColumnMeta("hemail"));
			colsInfo.add(new ExtGridColumnMeta("hbirthday"));
		}
		
		return colsInfo;
	}
	
	public void processManageGridContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<MapItem> items = new ArrayList<>();
		MapItem item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String view = ServletUtils.getStringParameter(request, "view", "w");
				String query = ServletUtils.getStringParameter(request, "query", "%");
				String pattern = StringUtils.replace(query, " ", "%");
				
				// Get contacts for each visible folder
				Integer[] checked = getCheckedFolders();
				List<ContactsManager.CategoryContacts> foldContacts = null;
				for(CategoryRoot root : getCheckedRoots()) {
					foldContacts = manager.listContacts(root, checked, pattern);
					
					for(ContactsManager.CategoryContacts foldContact : foldContacts) {
						for(VContact vc : foldContact.contacts) {
							item = new MapItem();
							item.put("uid", manager.buildContactUid(foldContact.folder.getCategoryId(), vc.getContactId()));
							item.put("contactId", vc.getContactId());
							item.put("listId", vc.getListId());
							if(view.equals("w")) item.put("title", vc.getTitle());
							item.put("firstName", vc.getFirstname());
							item.put("lastName", vc.getLastname());
							if(view.equals("h")) item.put("nickname", vc.getNickname());
							if(view.equals("w")) {
								item.put("company", StringUtils.defaultIfEmpty(vc.getCompanyAsCustomer(), vc.getCompany()));
								item.put("function", vc.getFunction());
								//item.put("caddress", vc.getCaddress());
								//item.put("ccity", vc.getCcity());
								item.put("ctelephone", vc.getCtelephone());
								item.put("cmobile", vc.getCmobile());
								item.put("cemail", vc.getCemail());
							}
							if(view.equals("h")) {
								item.put("htelephone", vc.getHtelephone());
								item.put("hemail", vc.getHemail());
								item.put("hbirthday", vc.getHbirthday());
							}
							item.put("categoryId", foldContact.folder.getCategoryId());
							item.put("categoryName", foldContact.folder.getName());
							item.put("categoryColor", foldContact.folder.getColor());
							item.put("_profileId", new UserProfile.Id(foldContact.folder.getDomainId(), foldContact.folder.getUserId()).toString());
							items.add(item);
						}
					}
				}
				
				ExtGridMetaData meta = new ExtGridMetaData(true);
				meta.setFields(view.equals("w") ? gridFieldsW : gridFieldsH);
				meta.setColumnsInfo(view.equals("w") ? gridColsInfoW : gridColsInfoH);
				new JsonResult(items, meta, items.size()).printTo(out);
			}
		
		} catch(Exception ex) {
			logger.error("Error executing action ManageGridContacts", ex);
			new JsonResult(false, "Error").printTo(out);
			
		}
	}
	
	public void processManageContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		JsContact item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				CompositeId cid = new CompositeId().parse(id);
				
				int categoryId = Integer.valueOf(cid.getToken(0));
				UserProfile.Id ownerId = manager.getCategoryOwner(categoryId);
				Contact contact = manager.getContact(id);
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
			}
			
		} catch(Exception ex) {
			logger.error("Error executing action ManageContacts", ex);
			new JsonResult(false, "Error").printTo(out);	
		}
	}
	
	public void processManageContactsLists(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ContactsList item = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				
				item = manager.getContactsList(id);
				new JsonResult(item).printTo(out);
				
			} else if(crud.equals(Crud.CREATE)) {
				Payload<MapItem, ContactsList> pl = ServletUtils.getPayload(request, ContactsList.class);
				
				manager.addContactsList(pl.data);
				new JsonResult().printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Payload<MapItem, ContactsList> pl = ServletUtils.getPayload(request, ContactsList.class);
				
				manager.updateContactsList(pl.data);
				new JsonResult().printTo(out);
				
			}
			
		} catch(Exception ex) {
			logger.error("Error in action ManageContactsLists", ex);
			new JsonResult(false, "Error").printTo(out);
		}
	}
	
	public void processManageGridContacts2(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<MapItem> items = new ArrayList<>();
		MapItem item = null;
		CoreManager corem = WT.getCoreManager(getRunContext());
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				
				String query = ServletUtils.getStringParameter(request, "query", "%");
				String pattern = StringUtils.replace(query, " ", "%");
				
				// Generates fields and columnsInfo dynamically
				ArrayList<ExtFieldMeta> fields = new ArrayList<>();
				ArrayList<ExtGridColumnMeta> colsInfo = new ArrayList<>();
				
				// Get contacts for each visible folder
				Integer[] checked = getCheckedFolders();
				List<ContactsManager.CategoryContacts> foldContacts = null;
				DirectoryManager dm = null;
				DirectoryElement de = null;
				for(CategoryRoot root : getCheckedRoots()) {
					foldContacts = manager.listContacts(root, checked, pattern);
					
					for(ContactsManager.CategoryContacts foldContact : foldContacts) {
						for(int i=0; i<foldContact.result.getElementsCount(); i++) {
							dm = foldContact.result.getDirectoryManager();
							de = foldContact.result.elementAt(i);
							item = new MapItem();
							
							
							fields.add(new ExtFieldMeta("contactId").setType("int"));
							colsInfo.add(new ExtGridColumnMeta("contactId").setHidden(true));
							item.put("contactId", Integer.parseInt(de.getField("CONTACT_ID")));
							fields.add(new ExtFieldMeta("listId").setType("int"));
							colsInfo.add(new ExtGridColumnMeta("listId").setHidden(true));
							item.put("listId", Integer.parseInt(de.getField("LIST_ID")));
							
							for(int j=0; j < foldContact.result.getColumnCount(); j++) {
								if(dm.isListField(j)) {
									String col = foldContact.result.getColumn(j);
									if(dm.getAliasField("COMPANY").equals(col)) {
										OCustomer customer = corem.getCustomer(de.getField(col));
										fields.add(new ExtFieldMeta(col));
										colsInfo.add(new ExtGridColumnMeta(col, col));
										item.put(col, (customer != null) ? customer.getDescription() : de.getField(col));
									} else {
										fields.add(new ExtFieldMeta(col));
										colsInfo.add(new ExtGridColumnMeta(col, col));
										item.put(col, de.getField(col));
									}
								}
							}
							
							fields.add(new ExtFieldMeta("categoryId"));
							colsInfo.add(new ExtGridColumnMeta("categoryId").setHidden(true));
							item.put("categoryId", foldContact.folder.getCategoryId());
							fields.add(new ExtFieldMeta("categoryName"));
							colsInfo.add(new ExtGridColumnMeta("categoryName").setHidden(true));
							item.put("categoryName", foldContact.folder.getName());
							fields.add(new ExtFieldMeta("categoryColor"));
							colsInfo.add(new ExtGridColumnMeta("categoryColor").setHidden(true));
							item.put("categoryColor", foldContact.folder.getColor());
							
							fields.add(new ExtFieldMeta("uid").setType("string"));
							colsInfo.add(new ExtGridColumnMeta("uid").setHidden(true));
							item.put("uid", manager.buildContactUid(foldContact.folder.getCategoryId(), item.get("contactId")));
							item.put("_profileId", new UserProfile.Id(foldContact.folder.getDomainId(), foldContact.folder.getUserId()).toString());
							items.add(item);
						}
					}
				}
				
				ExtGridMetaData meta = new ExtGridMetaData(true);
				meta.setFields(fields);
				meta.setColumnsInfo(colsInfo);
				new JsonResult(items, meta, items.size()).printTo(out);
			}
		
		} catch(Exception ex) {
			logger.error("Error executing action ManageGridContacts", ex);
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
				picture = manager.getContactPicture(id);
			}
			
			if(picture != null) {
				try(ByteArrayInputStream bais = new ByteArrayInputStream(picture.getBytes())) {
					ServletUtils.writeContent(response, bais, picture.getMimeType());
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
			//TODO: logging
			ex.printStackTrace();
		}
	}
	
	/*
	public void processVCardImportUploadStream(HttpServletRequest request, InputStream uploadStream) throws Exception {
		UserProfile up = getEnv().getProfile();
		Integer categoryId = ServletUtils.getIntParameter(request, "categoryId", true);
		//manager.importVCard(categoryId, uploadStream, up.getTimeZone());
	}
	*/
	
	private ContactPicture getUploadedContactPicture(String id) throws WTException {
		UploadedFile upl = getUploadedFile(id);
		if(upl == null) throw new WTException();
		
		ContactPicture pic = null;
		FileInputStream fis = null;
		try {
			File uplFile = new File(WT.getTempFolder(), upl.id);
			fis = new FileInputStream(uplFile);
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
			OCategory category = manager.getCategory(catId);
			sb.append("/");
			sb.append((category != null) ? category.getName() : cid.getToken(1));
		}
		
		return sb.toString();
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
		synchronized(roots) {
			if(checked) {
				checkedRoots.add(shareId);
			} else {
				checkedRoots.remove(shareId);
			}
			us.setCheckedCategoryRoots(checkedRoots);
		}
	}
	
	private void toggleCheckedFolder(int folderId, boolean checked) {
		synchronized(roots) {
			if(checked) {
				checkedFolders.add(folderId);
			} else {
				checkedFolders.remove(folderId);
			}
			us.setCheckedCategoryFolders(checkedFolders);
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
		OCategory cat = folder.getCategory();
		String id = new CompositeId(folder.getShareId(), cat.getCategoryId()).toString();
		boolean visible = checkedFolders.contains(cat.getCategoryId());
		ExtTreeNode node = new ExtTreeNode(id, cat.getName(), true);
		node.put("_type", JsFolderNode.TYPE_FOLDER);
		node.put("_pid", cat.getProfileId().toString());
		node.put("_rrights", rootPerms.toString());
		node.put("_frights", folder.getPerms().toString());
		node.put("_erights", folder.getElementsPerms().toString());
		node.put("_catId", cat.getCategoryId());
		node.put("_builtIn", cat.getBuiltIn());
		node.put("_default", cat.getIsDefault());
		node.put("_color", cat.getColor());
		node.put("_visible", visible);
		
		List<String> classes = new ArrayList<>();
		if(cat.getIsDefault()) classes.add("wtcon-tree-default");
		if(!folder.getElementsPerms().implies("CREATE") 
				&& !folder.getElementsPerms().implies("UPDATE")
				&& !folder.getElementsPerms().implies("DELETE")) classes.add("wtcon-tree-readonly");
		node.setCls(StringUtils.join(classes, " "));
		
		node.setIconClass("wt-palette-" + cat.getHexColor());
		node.setChecked(visible);
		return node;
	}
}
