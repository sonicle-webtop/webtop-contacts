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
package com.sonicle.webtop.contacts.io.input;

import com.sonicle.webtop.contacts.bol.model.Contact;
import com.sonicle.webtop.core.io.input.FileRowsReader;
import com.sonicle.webtop.core.io.input.TextFileReader;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.jooq.tools.StringUtils;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author malbinola
 */
public class MemoryContactTextFileReader extends TextFileReader implements MemoryContactFileReader {
	
	public static final String[] MAPPING_TARGETS = new String[]{
		"Title","FirstName","LastName","Nickname",/*"Gender",*/
		"WorkAddress","WorkPostalCode","WorkCity","WorkState","WorkCountry",
		"WorkTelephone","WorkTelephone2","WorkMobile","WorkFax","WorkPager","WorkEmail","WorkInstantMsg",
		"HomeAddress","HomePostalCode","HomeCity","HomeState","HomeCountry",
		"HomeTelephone","HomeTelephone2","HomeFax","HomePager","HomeEmail","HomeInstantMsg",
		"OtherAddress","OtherPostalCode","OtherCity","OtherState","OtherCountry",
		"OtherEmail","OtherInstantMsg",
		"Company","Function","Department","Manager","Assistant","AssistantTelephone",
		"Partner",/*"Birthday","Anniversary",*/
		"Url","Notes"
	};
	
	protected List<FileRowsReader.FieldMapping> mappings = null;
	
	public MemoryContactTextFileReader(CsvPreference pref, String charsetName) {
		super(pref, charsetName);
	}
	
	public void setMappings(List<FileRowsReader.FieldMapping> mappings) {
		this.mappings = mappings;
	}
	
	@Override
	public ArrayList<ContactReadResult> listContacts(LogEntries log, File file) throws IOException, UnsupportedOperationException {
		ArrayList<ContactReadResult> results = new ArrayList<>();
		HashMap<String, Integer> headersIndexes = listColumnIndexes(file);
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			CsvListReader lr = new CsvListReader(new InputStreamReader(fis, charset), pref);

			LogEntries rowlog = null;
			List<String> line = null;
			while((line = lr.read()) != null) {
				if(lr.getLineNumber() < firstDataRow) continue;
				if((lastDataRow != -1) && (lr.getLineNumber() > lastDataRow)) break;

				rowlog = new LogEntries();
				try {
					results.add(readRow(rowlog, headersIndexes, line));
					if(!rowlog.isEmpty()) {
						log.addMaster(new MessageLogEntry(LogEntry.LEVEL_WARN, "ROW [{0}]", lr.getLineNumber()));
						log.addAll(rowlog);
					}
				} catch(Throwable t) {
					log.addMaster(new MessageLogEntry(LogEntry.LEVEL_ERROR, "ROW [{0}]. Reason: {1}", lr.getLineNumber(), t.getMessage()));
				}
			}
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return results;
	}
	
	private ContactReadResult readRow(LogEntries log, HashMap<String, Integer> headersIndexes, List<String> line) throws Exception {
		Contact contact = new Contact();
		contact.setHasPicture(false);
		for(FileRowsReader.FieldMapping mapping : mappings) {
			if(StringUtils.isBlank(mapping.source)) continue;
			Integer index = headersIndexes.get(mapping.source);
			fillContactByMapping(contact, mapping.target, line.get(index));
		}
		return new ContactReadResult(contact, null);
	}
	
	private void fillContactByMapping(Contact contact, String target, String value) {
		if(target.equals("Title")) {
			contact.setTitle(value);
		} else if(target.equals("FirstName")) {
			contact.setFirstName(value);
		} else if(target.equals("LastName")) {
			contact.setLastName(value);
		} else if(target.equals("Nickname")) {
			contact.setNickname(value);
		} else if(target.equals("Gender")) {
			//TODO: gestire sesso
			//contact.setGender(value);
		} else if(target.equals("WorkAddress")) {
			contact.setWorkAddress(value);
		} else if(target.equals("WorkPostalCode")) {
			contact.setWorkPostalCode(value);
		} else if(target.equals("WorkCity")) {
			contact.setWorkCity(value);
		} else if(target.equals("WorkState")) {
			contact.setWorkState(value);
		} else if(target.equals("WorkCountry")) {
			contact.setWorkCountry(value);
		} else if(target.equals("WorkTelephone")) {
			contact.setWorkTelephone(value);
		} else if(target.equals("WorkTelephone2")) {
			contact.setWorkTelephone2(value);
		} else if(target.equals("WorkMobile")) {
			contact.setWorkMobile(value);
		} else if(target.equals("WorkFax")) {
			contact.setWorkFax(value);
		} else if(target.equals("WorkPager")) {
			contact.setWorkPager(value);
		} else if(target.equals("WorkEmail")) {
			contact.setWorkEmail(value);
		} else if(target.equals("WorkInstantMsg")) {
			contact.setWorkInstantMsg(value);
		} else if(target.equals("HomeAddress")) {
			contact.setHomeAddress(value);
		} else if(target.equals("HomePostalCode")) {
			contact.setHomePostalCode(value);
		} else if(target.equals("HomeCity")) {
			contact.setHomeCity(value);
		} else if(target.equals("HomeState")) {
			contact.setHomeState(value);
		} else if(target.equals("HomeCountry")) {
			contact.setHomeCountry(value);
		} else if(target.equals("HomeTelephone")) {
			contact.setHomeTelephone(value);
		} else if(target.equals("HomeTelephone2")) {
			contact.setHomeTelephone2(value);
		} else if(target.equals("HomeFax")) {
			contact.setHomeFax(value);
		} else if(target.equals("HomePager")) {
			contact.setHomePager(value);
		} else if(target.equals("HomeEmail")) {
			contact.setHomeEmail(value);
		} else if(target.equals("HomeInstantMsg")) {
			contact.setHomeInstantMsg(value);
		} else if(target.equals("OtherAddress")) {
			contact.setOtherAddress(value);
		} else if(target.equals("OtherPostalCode")) {
			contact.setOtherPostalCode(value);
		} else if(target.equals("OtherCity")) {
			contact.setOtherCity(value);
		} else if(target.equals("OtherState")) {
			contact.setOtherState(value);
		} else if(target.equals("OtherCountry")) {
			contact.setOtherCountry(value);
		} else if(target.equals("OtherEmail")) {
			contact.setOtherEmail(value);
		} else if(target.equals("OtherInstantMsg")) {
			contact.setOtherInstantMsg(value);
		} else if(target.equals("Company")) {
			contact.setCompany(value);
		} else if(target.equals("Function")) {
			contact.setFunction(value);
		} else if(target.equals("Department")) {
			contact.setDepartment(value);
		} else if(target.equals("Manager")) {
			contact.setManager(value);
		} else if(target.equals("Assistant")) {
			contact.setAssistant(value);
		} else if(target.equals("AssistantTelephone")) {
			contact.setAssistantTelephone(value);
		} else if(target.equals("Partner")) {
			contact.setPartner(value);
		} else if(target.equals("Birthday")) {
			//TODO: gestire compleanno
		} else if(target.equals("Anniversary")) {
			//TODO: gestire anniversario
		} else if(target.equals("Url")) {
			contact.setUrl(value);
		} else if(target.equals("Notes")) {
			contact.setNotes(value);
		}
	}
}
