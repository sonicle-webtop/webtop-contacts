/*
 * Copyright (C) 2018 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2018 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.io.input;

import com.sonicle.webtop.contacts.io.ContactInput;
import com.sonicle.webtop.contacts.io.LDIFInput;
import com.sonicle.webtop.core.io.BeanHandler;
import com.sonicle.webtop.core.io.input.FileReaderException;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.IOUtils;
import org.ldaptive.LdapEntry;
import org.ldaptive.SearchResult;
import org.ldaptive.io.LdifReader;

/**
 *
 * @author Dorian Haxhiaj
 */
public class ContactLDIFFileReader implements ContactFileReader {

	@Override
	public void readContacts(File file, BeanHandler handler) throws IOException, FileReaderException {
		FileReader fis = null;
		try {
			 fis=new FileReader(file);
			readContacts(fis, handler);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}
	
	private void readContacts(FileReader is, BeanHandler handler) throws IOException, FileReaderException {
		try {
			final LdifReader  ldifReader  = new LdifReader(is);
			SearchResult resLDIF = ldifReader.read();
			Collection<LdapEntry> ldapEntries =resLDIF.getEntries();
			readContacts(ldapEntries, handler);
		} catch(Exception ex) {
			throw new FileReaderException(ex, "Unable to read stream");
		}
	}
	
	private void readContacts(Collection<LdapEntry>  ldapEntries, BeanHandler handler) throws IOException, FileReaderException {
		 LDIFInput input = new LDIFInput();
		//TODO: move BeanHandler code into VCardInput in order to avoid duplicated code here
		for (LdapEntry ldapEntry : ldapEntries) {
			LogEntries log = new LogEntries();
			ContactInput result = null;
			try {
				final LogEntries ilog = new LogEntries();
				result = input.fromLDIF(ldapEntry, ilog);
				if (result.contact.trimFieldLengths()) {
					ilog.add(new MessageLogEntry(LogEntry.Level.WARN, "Some fields were truncated due to max-length"));
				}
				if (!ilog.isEmpty()) {
					log.addMaster(new MessageLogEntry(LogEntry.Level.WARN, "LDIF [{0}]"));
					log.addAll(ilog);
				}
			} catch(Throwable t) {
				t.printStackTrace();
				log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "LDIF [{0}]. Reason: {1}", t.getMessage()));
			} finally {
				try {
					handler.handle(result, log);
				} catch(Exception ex) {
					throw new FileReaderException(ex);
				}
			}
		}
	}
	
}
