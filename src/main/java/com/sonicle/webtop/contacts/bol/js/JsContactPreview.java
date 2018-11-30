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
package com.sonicle.webtop.contacts.bol.js;

import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactsList;
import com.sonicle.webtop.contacts.model.ShareFolderCategory;
import com.sonicle.webtop.core.sdk.UserProfileId;
import java.util.ArrayList;
import org.jooq.tools.StringUtils;

/**
 *
 * @author malbinola
 */
public class JsContactPreview {
	public String uid;
	public Integer id;
	public boolean isList;
	public String title;
	public String firstName;
	public String lastName;
	public String company;
	public String function;
	public ArrayList<ValueItem> data1;
	public ArrayList<ValueItem> data2;
	public ArrayList<ValueItem> data3;
	public String notes;
	public boolean pic;
	public String userProfile;
	public String userDisplayName;
	public Integer catId;
	public String catName;
	public String catColor;
	public String _pid;
	public String _frights;
	public String _erights;

	public JsContactPreview(ShareFolderCategory folder, CategoryPropSet folderProps, Contact item, ContactCompany itemCompany) {
		Category category = folder.getCategory();

		this.uid = JsGridContact.Id.build(item.getContactId(), false).toString();
		this.id = item.getContactId();
		this.isList = false;
		this.title = item.getTitle();
		this.firstName = item.getFirstName();
		this.lastName = item.getLastName();
		this.company = itemCompany.getCompanyDescription();
		this.data1 = new ArrayList<>();
		addValueItem(this.data1, "rcp1", item.getWorkEmail(), "work");
		addValueItem(this.data1, "rcp2", item.getHomeEmail(), "home");
		this.data2 = new ArrayList<>();
		addValueItem(this.data2, "tel1", item.getWorkMobile(), "mobile");
		addValueItem(this.data2, "tel2", item.getWorkTelephone(), "work");
		addValueItem(this.data2, "tel3", item.getHomeTelephone(), "home");
		this.data3 = new ArrayList<>();
		addValueItem(this.data3, "comp", itemCompany.getCompanyDescription(), "company");
		addValueItem(this.data3, "add1", item.getWorkFullAddress(), "workadd");
		addValueItem(this.data3, "add2", item.getHomeFullAddress(), "homeadd");
		this.notes = item.getNotes();
		this.pic = item.hasPicture();

		this.catId = category.getCategoryId();
		this.catName = category.getName();
		this.catColor = (folderProps != null) ? folderProps.getColorOrDefault(category.getColor()) : folder.getCategory().getColor();
		this._pid = new UserProfileId(category.getDomainId(), category.getUserId()).toString();
		this._frights = folder.getPerms().toString();
		this._erights = folder.getElementsPerms().toString();
	}

	public JsContactPreview(ShareFolderCategory folder, CategoryPropSet folderProps, ContactsList item) {
		Category category = folder.getCategory();

		this.uid = JsGridContact.Id.build(item.getContactId(), true).toString();
		this.id = item.getContactId();
		this.isList = true;
		this.firstName = item.getName();
		this.data1 = new ArrayList<>();
		this.data2 = new ArrayList<>();
		this.data3 = new ArrayList<>();
		this.notes = null;
		this.pic = false;

		this.catId = category.getCategoryId();
		this.catName = category.getName();
		this.catColor = (folderProps != null) ? folderProps.getColorOrDefault(category.getColor()) : folder.getCategory().getColor();
		this._pid = new UserProfileId(category.getDomainId(), category.getUserId()).toString();
		this._frights = folder.getPerms().toString();
		this._erights = folder.getElementsPerms().toString();
	}

	private void addValueItem(ArrayList<ValueItem> array, String id, String value, String type) {
		if (!StringUtils.isBlank(value)) {
			array.add(new ValueItem(id, value, type));
		}
	}

	public static class ValueItem {
		public String id;
		public String value;
		public String type;

		public ValueItem(String id, String value, String type) {
			this.id = id;
			this.value = value;
			this.type = type;
		}
	}
}
