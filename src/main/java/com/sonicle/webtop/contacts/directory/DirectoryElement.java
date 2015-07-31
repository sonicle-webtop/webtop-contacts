/*
* WebTop Groupware is a bundle of WebTop Services developed by Sonicle S.r.l.
* Copyright (C) 2011 Sonicle S.r.l.
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

package com.sonicle.webtop.contacts.directory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DirectoryElement {

	DirectoryManager manager = null;
	HashMap<String, String> fields = new HashMap<String, String>();
	HashMap<String, String> newfields = new HashMap<String, String>();
	Locale locale = null;
	HashMap<String, String> lookupvalues = new HashMap<String, String>();

	public DirectoryElement(DirectoryManager manager, Locale locale) {
		this.manager = manager;
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	public void updateField(String field, String value) {
		newfields.put(field, value);
	}

	public String getNewField(String field) {
		return newfields.get(field);
	}

	public void setField(String field, String value) {
		if (value == null) {
			value = "";
		}
		fields.put(field, value);
	}

	public String getField(String field) {
		return fields.get(field);
	}

	public Set<String> getFields() {
		return fields.keySet();
	}

	public Set<String> getNewFields() {
		return newfields.keySet();
	}

	public void setLookupValue(String field, String value) {
		lookupvalues.put(field, value);
	}

	public String getLookupValue(String field) {
		return lookupvalues.get(field);
	}

	public DirectoryManager getDirectoryManager() {
		return manager;
	}

	public String getKeyUrlParameters() {
		String params = "";
		List<String> keyfields = manager.getKeyFields();
		for (String keyfield : keyfields) {
			String value = null;
			if (manager.isRealFieldVisible(keyfield)) {
				value = getField(manager.getAliasField(keyfield));
			} else {
				value = getField(keyfield);
			}
			if (params.length() > 0) {
				params += "&";
			}
			try {
				params += keyfield + "=" + URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException ex) {

			}
		}
		return params;
	}

}
