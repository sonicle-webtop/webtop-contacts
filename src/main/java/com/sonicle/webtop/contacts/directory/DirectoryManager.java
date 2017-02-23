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

import java.util.List;
import java.util.Locale;

public interface DirectoryManager {

    public enum DirectoryType {
        USER,
        GROUP,
        GLOBAL,
        TABLE
    }

    public DirectoryResult  lookup(String pattern, Locale locale, boolean isAnd, boolean caseSensitive);
    public DirectoryResult  lookup(String searchfield, String pattern, Locale locale, boolean isAnd, boolean caseSensitive);
    public DirectoryResult  lookup(List<String> searchfields, List<String> patterns, Locale locale, boolean isAnd, boolean caseSensitive, boolean distinct);
	public String           getId();
	public String           getDescription();
	public List<String>     getFields();
	public List<String>     getKeyFields();
	public List<String>     getSearchFields();
	public String           getMailField();
        public String           getFaxField();
	public String           getFirstNameField();
	public String           getLastNameField();
	public String           getCompanyField();
	public int              getSize(String field);
	public DirectoryType    getType();
	public boolean          isWritable();
	public boolean          isClickField(String field);
    public boolean          isListField(int fieldIndex);
    public int              getListColumnCount();
	public void             update(DirectoryElement de) throws Exception;
	public void             insert(DirectoryElement de) throws Exception;
	public void             delete(DirectoryElement de) throws Exception;
	public DirectoryElement createEmptyElement(Locale locale);
	public DirectoryGroup[] getGroups();
	public String[]         getFields(DirectoryGroup g);
    public boolean          isRealFieldVisible(String field);
    public String           getAliasField(String realfield);
}
