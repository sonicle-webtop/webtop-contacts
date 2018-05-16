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

/**
 *
 * @author malbinola
 */
public class ContactsSettings {
	
	/**
	 * [system+domain]
	 * [boolean](false)
	 * Enable/Disable addressbooks deletions through DAV rest-api interface.
	 */
	public static final String DAV_ADDRESSBOOK_DELETE_ENABLED = "dav.addressbook.delete.enabled";
	
	/**
	 * [][default]
	 * [enum {O:OFF, R:READ, W:WRITE}] (O)
	 * The default value of the sync field for new categories.
	 */
	public static final String CATEGORY_SYNC = "category.sync";
	
	/**
	 * [user][default]
	 * [enum {w:WORK, h:HOME}](w)
	 * Contacts grid view (w:work, h:home)
	 */
	public static final String VIEW = "view";
	public static final String VIEW_WORK = "w";
	public static final String VIEW_HOME = "h";
	
	/**
	 * [user]
	 * [time(hh:mm)]
	 * Anniversary reminder notification time
	 */
	public static final String ANNIVERSARY_REMINDER_TIME = "anniversary.reminder.time";
	
	/**
	 * [user][default]
	 * [string]
	 * Set anniversary reminder delivery mode
	 */
	public static final String ANNIVERSARY_REMINDER_DELIVERY = "anniversary.reminder.delivery";
	public static final String ANNIVERSARY_REMINDER_DELIVERY_NONE = "none";
	public static final String ANNIVERSARY_REMINDER_DELIVERY_APP = "app";
	public static final String ANNIVERSARY_REMINDER_DELIVERY_EMAIL = "email";
	
	/**
	 * [user]
	 * [string]
	 * Selected folder root node.
	 */
	public static final String SELECTED_ROOT = "roots.selected";
	
	/**
	 * [user]
	 * [string[]]
	 * List of checked (or visible) folder root nodes.
	 */
	public static final String CHECKED_CATEGORY_ROOTS = "category.roots.checked";
	
	/**
	 * [user]
	 * [int[]]
	 * List of checked (or visible) folders (groups).
	 */
	public static final String CHECKED_CATEGORY_FOLDERS = "category.folders.checked";
	
	/**
	 * [user]
	 * [object]
	 * JSON value for sortInfo.
	 */
	public static final String GRID_CONTACTS_SORTINFO_ROOT = "grid.contacts.{0}.sortinfo";
	
	/**
	 * [user]
	 * [object]
	 * JSON value for sortInfo.
	 */
	public static final String GRID_CONTACTS_GROUPINFO_ROOT = "grid.contacts.{0}.groupinfo";
}
