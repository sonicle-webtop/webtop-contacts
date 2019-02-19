/*
 * Copyright (C) 2019 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2019 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.bol.js;

import com.sonicle.webtop.contacts.model.Contact;

/**
 *
 * @author Inis
 */
public class JsEventContact {
	public String address;
	public String postalCode;
	public String city;
	public String state;
	public String country;
	public String department;
	public String email;
	public String mobile;
	public String telephone;
	
	
	public static JsEventContact createJsEventContact(Contact src, String type) {
		JsEventContact jsEventContact = null;
		if(src != null) {
			if(type.equals("home")) {
				jsEventContact = createJsEventContactHome(src);
			} else if(type.equals("work")) {
				jsEventContact = createJsEventContactWork(src);
			} else if(type.equals("other")) {
				jsEventContact = createJsEventContactOther(src);
			}
		}
		return jsEventContact;
	}
	
	public static JsEventContact createJsEventContactHome(Contact src) {
		JsEventContact contact = new JsEventContact();
		contact.address = src.getHomeAddress();
		contact.postalCode = src.getHomePostalCode();
		contact.city = src.getHomeCity();
		contact.state = src.getHomeState();
		contact.country = src.getHomeCountry();
		contact.email = src.getEmail2();
		contact.mobile = src.getMobile();
		contact.telephone = src.getHomeTelephone1();
		contact.department = src.getDepartment();
		return contact;
	}
	
	public static JsEventContact createJsEventContactWork(Contact src) {
		JsEventContact contact = new JsEventContact();
		contact.address = src.getWorkAddress();
		contact.postalCode = src.getWorkPostalCode();
		contact.city = src.getWorkCity();
		contact.state = src.getWorkState();
		contact.country = src.getWorkCountry();
		contact.email = src.getEmail1();
		contact.mobile = src.getMobile();
		contact.telephone = src.getWorkTelephone1();
		contact.department = src.getDepartment();
		return contact;
	}
	
	public static JsEventContact createJsEventContactOther(Contact src) {
		JsEventContact contact = new JsEventContact();
		contact.address = src.getOtherAddress();
		contact.postalCode = src.getOtherPostalCode();
		contact.city = src.getOtherCity();
		contact.state = src.getOtherState();
		contact.country = src.getOtherCountry();
		contact.email = src.getEmail3();
		contact.department = src.getDepartment();
		return contact;
	}
}
