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

import com.sonicle.commons.web.json.JsonResult;
import com.sonicle.commons.web.json.extjs.GroupMeta;
import com.sonicle.commons.web.json.extjs.SortMeta;
import static com.sonicle.webtop.contacts.ContactsSettings.*;
import com.sonicle.webtop.core.sdk.BaseUserSettings;
import com.sonicle.webtop.core.sdk.UserProfile;
import java.text.MessageFormat;
import java.util.HashSet;
import org.joda.time.LocalTime;

/**
 *
 * @author malbinola
 */
public class ContactsUserSettings extends BaseUserSettings {
	private ContactsServiceSettings ss;
	
	public ContactsUserSettings(String serviceId, UserProfile.Id profileId) {
		super(serviceId, profileId);
		ss = new ContactsServiceSettings(serviceId, profileId.getDomainId());
	}
	
	public String getView() {
		String value = getString(VIEW, null);
		if(value != null) return value;
		return ss.getDefaultView();
	}
	
	public boolean setView(String value) {
		return setString(VIEW, value);
	}
	
	public LocalTime getAnniversaryReminderTime() {
		LocalTime value = getTime(ANNIVERSARY_REMINDER_TIME, (LocalTime)null, "HH:mm");
		if(value != null) return value;
		return new LocalTime(0, 0, 0, 0);
	}
	
	public boolean setAnniversaryReminderTime(LocalTime value) {
		return setTime(ANNIVERSARY_REMINDER_TIME, value, "HH:mm");
	}
	
	public String getAnniversaryReminderDelivery() {
		String value = getString(ANNIVERSARY_REMINDER_DELIVERY, null);
		if(value != null) return value;
		return ss.getDefaultAnniversaryReminderDelivery();
	}
	
	public boolean setAnniversaryReminderDelivery(String value) {
		return setString(ANNIVERSARY_REMINDER_DELIVERY, value);
	}
	
	public String getSelectedRoot() {
		return getString(SELECTED_ROOT, null);
	}
	
	public boolean setSelectedRoot(String value) {
		return setString(SELECTED_ROOT, value);
	}
	
	public CheckedRoots getCheckedCategoryRoots() {
		return getObject(CHECKED_CATEGORY_ROOTS, new CheckedRoots(), CheckedRoots.class);
	}
	
	public boolean setCheckedCategoryRoots(CheckedRoots value) {
		return setObject(CHECKED_CATEGORY_ROOTS, value, CheckedRoots.class);
	}
	
	public CheckedFolders getCheckedCategoryFolders() {
		return getObject(CHECKED_CATEGORY_FOLDERS, new CheckedFolders(), CheckedFolders.class);
	}
	
	public boolean setCheckedCategoryFolders(CheckedFolders value) {
		return setObject(CHECKED_CATEGORY_FOLDERS, value, CheckedFolders.class);
	}
	
	public GroupMeta getGridContactsGroupInfo(String view) {
		String key = MessageFormat.format(GRID_CONTACTS_GROUPINFO_ROOT, view);
		return getObject(key, null, GroupMeta.class);
	}
	
	public boolean setGridContactsGroupInfo(String view, GroupMeta groupInfo) {
		String key = MessageFormat.format(GRID_CONTACTS_GROUPINFO_ROOT, view);
		if(groupInfo == null) {
			return clear(key);
		} else {
			return setObject(key, groupInfo, GroupMeta.class);
		}
	}
	
	public SortMeta getGridContactsSortInfo(String view) {
		String key = MessageFormat.format(GRID_CONTACTS_SORTINFO_ROOT, view);
		return getObject(key, null, SortMeta.class);
	}
	
	public boolean setGridContactsSortInfo(String view, SortMeta sortInfo) {
		String key = MessageFormat.format(GRID_CONTACTS_SORTINFO_ROOT, view);
		if(sortInfo == null) {
			return clear(key);
		} else {
			return setObject(key, sortInfo, SortMeta.class);
		}
	}
	
	public static class CheckedRoots extends HashSet<String> {
		public CheckedRoots() {
			super();
		}
		
		public static CheckedRoots fromJson(String value) {
			return JsonResult.gson.fromJson(value, CheckedRoots.class);
		}
		
		public static String toJson(CheckedRoots value) {
			return JsonResult.gson.toJson(value, CheckedRoots.class);
		}
	}
	
	public static class CheckedFolders extends HashSet<Integer> {
		public CheckedFolders() {
			super();
		}
		
		public static CheckedFolders fromJson(String value) {
			return JsonResult.gson.fromJson(value, CheckedFolders.class);
		}
		
		public static String toJson(CheckedFolders value) {
			return JsonResult.gson.toJson(value, CheckedFolders.class);
		}
	}
}
