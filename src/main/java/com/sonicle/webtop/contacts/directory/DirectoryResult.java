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
package com.sonicle.webtop.contacts.directory;

import java.util.*;

public class DirectoryResult {

	ArrayList<DirectoryElement> elements = new ArrayList<>();
	String columns[] = null;
	String trimmedcolumns[] = null;
	String source = null;
	int mailColumn = 0;
	DirectoryManager manager = null;

	public DirectoryResult(String source, int cols, int mailcol, DirectoryManager dm) {
		columns = new String[cols];
		trimmedcolumns = new String[cols];
		this.source = source;
		mailColumn = mailcol;
		manager = dm;
	}

	public DirectoryManager getDirectoryManager() {
		return manager;
	}

	public String getSource() {
		return source;
	}

	public void setColumn(int c, String name) {
		columns[c] = name;
		trimmedcolumns[c] = trimchars(name);
	}

	public String getMailColumn() {
		return columns[mailColumn];
	}

	public String getColumn(int col) {
		return columns[col];
	}

	public String getTrimmedColumn(int col) {
		return trimmedcolumns[col];
	}

	public int getColumnCount() {
		return columns.length;
	}

	public void addElement(DirectoryElement de) {
		elements.add(de);
	}

	public int getElementsCount() {
		return elements.size();
	}

	public DirectoryElement elementAt(int i) {
		return (DirectoryElement) elements.get(i);
	}

	public static String trimchars(String s) {
		String str = s.trim();
		if (str.startsWith("X_")) {
			int x = 2;
			while (str.charAt(x) == '_') {
				++x;
			}
			str = str.substring(x);
		}
		return str;
	}
}
