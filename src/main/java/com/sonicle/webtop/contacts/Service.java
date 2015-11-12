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

import com.sonicle.commons.validation.Validator;
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
import com.sonicle.webtop.contacts.bol.js.JsContact;
import com.sonicle.webtop.contacts.bol.js.JsCategory;
import com.sonicle.webtop.contacts.bol.js.JsCategoryLkp;
import com.sonicle.webtop.contacts.bol.js.JsFolderNode;
import com.sonicle.webtop.contacts.bol.js.JsFolderNode.JsFolderNodeList;
import com.sonicle.webtop.contacts.bol.model.CategoryFolder;
import com.sonicle.webtop.contacts.bol.model.CategoryRoot;
import com.sonicle.webtop.contacts.bol.model.Contact;
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
import com.sonicle.webtop.core.sdk.BaseService;
import com.sonicle.webtop.core.sdk.UserProfile;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	
	//private ExportWizard wizard = null;
	
	@Override
	public void initialize() throws Exception {
		UserProfile profile = getEnv().getProfile();
		manager = new ContactsManager(getId(), getRunContext());
		//manager.initializeDirectories(profile);
		us = new ContactsUserSettings(profile.getDomainId(), profile.getUserId(), getId());
		initFolders();
	}
	
	@Override
	public void cleanup() throws Exception {
		checkedFolders.clear();
		checkedFolders = null;
		checkedRoots.clear();
		checkedRoots = null;
		us = null;
		manager.cleanupDirectories();
		manager = null;
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
						if(!folder.getElsPerms().implies("CREATE")) continue;
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
	
	public void processManageContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		JsContact item = null;
		
		try {
			UserProfile up = getEnv().getProfile();
			
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				String id = ServletUtils.getStringParameter(request, "id", true);
				CompositeId cid = new CompositeId().parse(id); 
				
				int categoryId = Integer.valueOf(cid.getToken(0));
				UserProfile.Id ownerId = manager.getCategoryOwner(categoryId);
				Contact contact = manager.getContact(categoryId, cid.getToken(1), up.getLocale());
				item = new JsContact(ownerId, contact);
				
				/*
				item = new JsContact();
				item.contactId = 1;
				item.folderId = 1;
				item.title = "Mr";
				item.firstName = "firstName";
				item.lastName = "lastName";
				item.nickname = "nickname";
				item.gender = "gender";
				item.workAddress = "workAddress";
				item.workPostalCode = "workPostalCode";
				item.workCity = "workCity";
				item.workState = "workState";
				item.workCountry = "workCountry";
				item.workTelephone = "workTelephone";
				item.workTelephone2 = "workTelephone2";
				item.workMobile = "workMobile";
				item.workFax = "workFax";
				item.workPager = "workPager";
				item.workEmail = "workEmail";
				item.workInstantMsg = "workInstantMsg";
				item.homeAddress = "homeAddress";
				item.homePostalCode = "homePostalCode";
				item.homeCity = "homeCity";
				item.homeState = "homeState";
				item.homeCountry = "homeCountry";
				item.homeTelephone = "homeTelephone";
				item.homeMobile = "homeMobile";
				item.homeFax = "homeFax";
				item.homePager = "homePager";
				item.homeEmail = "homeEmail";
				item.homeInstantMsg = "homeInstantMsg";
				item.otherAddress = "otherAddress";
				item.otherPostalCode = "otherPostalCode";
				item.otherCity = "otherCity";
				item.otherState = "otherState";
				item.otherCountry = "otherCountry";
				item.otherTelephone = "otherTelephone";
				item.otherEmail = "otherEmail";
				item.otherInstantMsg = "otherInstantMsg";
				item.partner = "partner";
				item.birthday = "1983-11-28";
				item.anniversary = "2015-09-12";
				item.company = "company";
				item.function = "function";
				item.department = "department";
				item.manager = "manager";
				item.assistant = "assistant";
				item.assistantTelephone = "assistantTelephone";
				item.url = "url";
				item.photo = "http://www.sentieriselvaggi.it/wp-content/uploads/public/articoli/46507/Images/brad-pitt.jpg";
				item.notes = "notes";
				*/
				
				//Event evt = manager.readEvent(id);
				//item = new JsContact(evt, manager.getCalendarGroupId(evt.getCalendarId()));
				new JsonResult(item).printTo(out);
				
			} else if(crud.equals(Crud.CREATE)) {
				/*
				Payload<MapItem, JsContact> pl = ServletUtils.getPayload(request, JsContact.class);
				
				//TODO: verificare che il calendario supporti la scrittura (specialmente per quelli condivisi)
				
				Event evt = JsEvent.buildEvent(pl.data, us.getWorkdayStart(), us.getWorkdayEnd());
				// Adds an organizer if event doesn't have it
				if(evt.hasAttendees()) {
					EventAttendee org = evt.getOrganizer();
					if(org == null) {
						org = new EventAttendee();
						org.setRecipient(up.getEmailAddress());
						org.setRecipientType(EventAttendee.RECIPIENT_TYPE_ORGANIZER);
						org.setResponseStatus(EventAttendee.RESPONSE_STATUS_ACCEPTED);
						org.setNotify(false);
						evt.getAttendees().add(org);
					}
				}
				manager.addEvent(evt);
				new JsonResult().printTo(out);
				*/
				
			} else if(crud.equals(Crud.UPDATE)) {
				/*
				String target = ServletUtils.getStringParameter(request, "target", "this");
				Payload<MapItem, JsEvent> pl = ServletUtils.getPayload(request, JsEvent.class);
				
				Event evt = JsEvent.buildEvent(pl.data, us.getWorkdayStart(), us.getWorkdayEnd());
				manager.editEvent(target, evt, up.getTimeZone());
				new JsonResult().printTo(out);
				*/
			}
			
		} catch(Exception ex) {
			logger.error("Error executing action ManageContacts", ex);
			new JsonResult(false, "Error").printTo(out);	
		}
	}
	
	public void processManageGridContacts(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		ArrayList<MapItem> items = new ArrayList<>();
		MapItem item = null;
		CoreManager corem = WT.getCoreManager(getRunContext());
		
		try {
			UserProfile up = getEnv().getProfile();
			//DateTimeZone utz = up.getTimeZone();
			
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			if(crud.equals(Crud.READ)) {
				
				String query = ServletUtils.getStringParameter(request, "query", "%");
				String pattern = StringUtils.replace(query, " ", "%");
				
				// Generates fields and columnsInfo dynamically
				ArrayList<ExtFieldMeta> fields = new ArrayList<>();
				ArrayList<ExtGridColumnMeta> colsInfo = new ArrayList<>();
				
				// Get contacts for each visible folder
				Integer[] checked = getCheckedFolders();
				List<ContactsManager.FolderContacts> foldContacts = null;
				DirectoryManager dm = null;
				DirectoryElement de = null;
				for(CategoryRoot root : getCheckedRoots()) {
					foldContacts = manager.listContacts(root, checked, up.getLocale(), pattern);
					
					for(ContactsManager.FolderContacts foldContact : foldContacts) {
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
									} else if(dm.getAliasField("FOLDER_ID").equals(col)) {
										fields.add(new ExtFieldMeta("folderId"));
										colsInfo.add(new ExtGridColumnMeta("folderId").setHidden(true));
										item.put("folderId", Integer.parseInt(de.getField("FOLDER_ID")));
										
										OCategory folder = manager.getCategory(Integer.parseInt(de.getField(col)));
										if(folder != null) {
											fields.add(new ExtFieldMeta("folderName"));
											colsInfo.add(new ExtGridColumnMeta("folderName").setHidden(true));
											item.put("folderName", folder.getName());
											fields.add(new ExtFieldMeta("folderColor"));
											colsInfo.add(new ExtGridColumnMeta("folderColor").setHidden(true));
											item.put("folderColor", folder.getColor());
										}
									} else {
										fields.add(new ExtFieldMeta(col));
										colsInfo.add(new ExtGridColumnMeta(col, col));
										item.put(col, de.getField(col));
									}
								}
							}
							
							fields.add(new ExtFieldMeta("id").setType("string"));
							colsInfo.add(new ExtGridColumnMeta("id").setHidden(true));
							item.put("id", new CompositeId(item.get("folderId"), item.get("contactId")).toString());
							item.put("_profileId", new UserProfile.Id(foldContact.folder.getDomainId(), foldContact.folder.getUserId()).toString());
							items.add(item);
						}
					}
				}
				
				ExtGridMetaData meta = new ExtGridMetaData(true);
				meta.setFields(fields);
				meta.setColumnsInfo(colsInfo);
				new JsonResult(items, meta, items.size()).printTo(out);
				
				
				/*
				// Get contacts for each visible folder
				Integer[] checked = getCheckedFolders();
				List<ContactsManager.FolderContacts> foldContacts = null;
				for(FolderBase root : getCheckedRoots()) {
					foldContacts = manager.listContacts(root, checked, up.getLocale(), pattern);
					for(ContactsManager.FolderContacts foldContact : foldContacts) {
						for(int i=0; i<foldContact.result.getElementsCount(); i++) {
							items.add(new JsGridContact(foldContact.folder, foldContact.result.elementAt(i)));
						}
					}
				}
				
				new JsonResult("contacts", items).printTo(out);
				*/
			}
		
		} catch(Exception ex) {
			logger.error("Error executing action ManageGridContacts", ex);
			new JsonResult(false, "Error").printTo(out);
			
		}
	}
	
	public void processGetContactPhoto(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			String id = ServletUtils.getStringParameter(request, "id", true);
			File photoFile = null;
			
			if(Validator.isInteger(id)) {
				
			} else {
				UploadedFile uploaded = getUploadedFile(id);
				if(uploaded == null) throw new Exception();
				File tempFolder = WT.getTempFolder();
				photoFile = new File(tempFolder, uploaded.id);
			}
			
			try(FileInputStream fis = new FileInputStream(photoFile)) {
				ServletUtils.writeInputStream(response, fis);
			}
			
		} catch(Exception ex) {
			//TODO: logging
			ex.printStackTrace();
		}
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
		node.put("_erights", folder.getElsPerms().toString());
		node.put("_catId", cat.getCategoryId());
		node.put("_builtIn", cat.getBuiltIn());
		node.put("_default", cat.getIsDefault());
		node.put("_color", cat.getColor());
		node.put("_visible", visible);
		
		List<String> classes = new ArrayList<>();
		if(cat.getIsDefault()) classes.add("wtcon-tree-default");
		if(!folder.getElsPerms().implies("CREATE") 
				&& !folder.getElsPerms().implies("UPDATE")
				&& !folder.getElsPerms().implies("DELETE")) classes.add("wtcon-tree-readonly");
		node.setCls(StringUtils.join(classes, " "));
		
		node.setIconClass("wt-palette-" + cat.getHexColor());
		node.setChecked(visible);
		return node;
	}
}
