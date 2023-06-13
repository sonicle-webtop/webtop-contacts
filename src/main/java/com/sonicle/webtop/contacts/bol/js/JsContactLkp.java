/*
 * Copyright (C) 2022 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2022 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.bol.js;

import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryFSFolder;
import com.sonicle.webtop.contacts.model.CategoryFSOrigin;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.ContactLookup;
import com.sonicle.webtop.core.sdk.UserProfileId;

/**
 *
 * @author malbinola
 */
public class JsContactLkp {
	public String id;
	public String displayName;
	public String firstName;
	public String lastName;
	public String company;
	public String function;
	public String email1;
	public String email2;
	public String workTelephone1;
	public String homeTelephone1;
	public String mobile;
	public String tags;
	public Integer catId;
	public String catName;
	public String catColor;
	public String _owPid;
	public String _foPerms;
	public String _itPerms;
	
	public JsContactLkp(CategoryFSOrigin origin, CategoryFSFolder folder, CategoryPropSet folderProps, ContactLookup item) {
		Category category = folder.getCategory();
		
		this.id = String.valueOf(item.getContactId());
		this.displayName = item.getDisplayName();
		this.firstName = item.getFirstName();
		this.lastName = item.getLastName();
		this.company = item.getCompanyDescription();
		this.function = item.getFunction();
		this.email1 = item.getEmail1();
		this.workTelephone1 = item.getWorkTelephone1();
		this.mobile = item.getMobile();
		this.email2 = item.getEmail2();
		this.homeTelephone1 = item.getHomeTelephone1();
		this.mobile = item.getMobile();
		this.tags = item.getTags();
		
		this.catId = category.getCategoryId();
		this.catName = category.getName();
		this.catColor = (folderProps != null) ? folderProps.getColorOrDefault(category.getColor()) : folder.getCategory().getColor();
		this._owPid = new UserProfileId(category.getDomainId(), category.getUserId()).toString();
		this._foPerms = folder.getPermissions().getFolderPermissions().toString();
		this._itPerms = folder.getPermissions().getItemsPermissions().toString();
	}
}
