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
package com.sonicle.webtop.contacts.io.input;

import com.sonicle.webtop.contacts.io.ContactInput;
import com.sonicle.webtop.contacts.io.VCardInput;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactPictureWithBytes;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.ImppType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Email;
import ezvcard.property.Gender;
import ezvcard.property.Impp;
import ezvcard.property.Nickname;
import ezvcard.property.Note;
import ezvcard.property.Photo;
import ezvcard.property.Role;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.TextListProperty;
import ezvcard.property.Title;
import ezvcard.property.Url;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 *
 * @author malbinola
 */
public class MemoryContactVCardFileReader implements MemoryContactFileReader {

	@Override
	public ArrayList<ContactInput> listContacts(LogEntries log, File file) throws IOException, UnsupportedOperationException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return listContacts(log, fis);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}
	
	public ArrayList<ContactInput> listContacts(LogEntries log, InputStream is) throws IOException, UnsupportedOperationException {
		// See https://tools.ietf.org/html/rfc6350
		// See http://www.w3.org/TR/vcard-rdf/
		final VCardInput input = new VCardInput();
		ArrayList<ContactInput> results = new ArrayList<>();
		
		List<VCard> vcs = Ezvcard.parse(is).all();
		for(VCard vc : vcs) {
			ContactInput result = null;
			try {
				final LogEntries ilog = new LogEntries();
				result = input.fromVCard(vc, ilog);
				if (result.contact.trimFieldLengths()) {
					ilog.add(new MessageLogEntry(LogEntry.Level.WARN, "Some fields were truncated due to max-length"));
				}
				if (!ilog.isEmpty()) {
					log.addMaster(new MessageLogEntry(LogEntry.Level.WARN, "VCARD [{0}]", vc.getUid()));
					log.addAll(ilog);
				}
				results.add(result);
				
			} catch(Throwable t) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "VCARD [{0}]. Reason: {1}", vc.getUid(), t.getMessage()));
			}
		}
		return results;
	}
}
