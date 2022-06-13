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
package com.sonicle.webtop.contacts.old.io.input;

import com.sonicle.webtop.contacts.io.ContactInput;
import com.sonicle.webtop.contacts.model.ContactCompany;
import com.sonicle.webtop.contacts.model.ContactEx;
import com.sonicle.webtop.core.io.BeanHandler;
import com.sonicle.webtop.core.io.input.ExcelFileReader;
import com.sonicle.webtop.core.io.input.FileReaderException;
import com.sonicle.webtop.core.io.input.FileRowsReader;
import com.sonicle.webtop.core.io.input.RowHandler;
import com.sonicle.webtop.core.io.input.RowValues;
import com.sonicle.webtop.core.io.input.XlsRowsProcessor;
import com.sonicle.webtop.core.io.input.XlsxRowsHandler;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author malbinola
 */
public class ContactExcelFileReader extends ExcelFileReader implements ContactFileReader {
	public static final String[] MAPPING_TARGETS = new String[]{
		"Title","FirstName","LastName","DisplayName","Nickname",/*"Gender",*/
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
	
	public ContactExcelFileReader(boolean binary) {
		super(binary);
	}
	
	public void setMappings(List<FileRowsReader.FieldMapping> mappings) {
		this.mappings = mappings;
	}
	
	@Override
	public void readContacts(File file, BeanHandler beanHandler) throws IOException, FileReaderException {
		if (binary) {
			readXlsContacts(file, beanHandler);
		} else {
			readXlsxContacts(file, beanHandler);
		}
	}
	
	private void readXlsxContacts(File file, BeanHandler beanHandler) throws IOException, FileReaderException {
		OPCPackage opc = null;
		HashMap<String, Integer> columnIndexes = listXlsxColumnIndexes(file);
		XlsRowHandler rowHandler = new XlsRowHandler(this, columnIndexes, beanHandler);
		
		try {
			opc = OPCPackage.open(file, PackageAccess.READ);
			XSSFReader reader = new XSSFReader(opc);
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			StylesTable styles = reader.getStylesTable();
			
			XlsxRowsHandler rowsHandler = null;
			XSSFReader.SheetIterator sit = (XSSFReader.SheetIterator) reader.getSheetsData();
			while (sit.hasNext()) {
				InputStream is = null;
				try {
					is = sit.next();
					if (StringUtils.equals(sit.getSheetName(), sheet)) {
						XMLReader xmlReader = SAXHelper.newXMLReader();
						rowsHandler = new XlsxRowsHandler(is, headersRow, firstDataRow, lastDataRow, rowHandler);
						ContentHandler xhandler = new XSSFSheetXMLHandler(styles, null, strings, rowsHandler, fmt, false);
						xmlReader.setContentHandler(xhandler);
						xmlReader.parse(new InputSource(is));
					}
				} catch(SAXException | ParserConfigurationException ex) {
					throw new FileReaderException(ex, "Error processing file content");
				} catch(NullPointerException ex) {
					// Thrown when stream is forcibly closed. Simply ignore this!
				} finally {
					IOUtils.closeQuietly(is);
				}
				if(rowsHandler != null) break;
			}
			
		} catch(OpenXML4JException | SAXException ex) {
			throw new FileReaderException(ex, "Error opening file");
		} finally {
			IOUtils.closeQuietly(opc);
		}
	}
	
	private void readXlsContacts(File file, BeanHandler beanHandler) throws IOException, FileReaderException {
		POIFSFileSystem pfs = null;
		InputStream is = null;
		HashMap<String, Integer> columnIndexes = listXlsColumnIndexes(file);
		
		try {
			pfs = new POIFSFileSystem(file);
			is = pfs.createDocumentInputStream("Workbook");
			XlsRowHandler rowHandler = new XlsRowHandler(this, columnIndexes, beanHandler);
			XlsRowsProcessor rows = new XlsRowsProcessor(is, headersRow, firstDataRow, lastDataRow, sheet, rowHandler);
			rows.process();
			
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(pfs);
		}
	}
	
	protected void readRow(HashMap<String, Integer> columnIndexes, BeanHandler beanHandler, int row, RowValues rowBean) throws Exception {
		LogEntries log = new LogEntries();
		ContactEx contact = null;
		try {
			LogEntries ilog = new LogEntries();
			contact = new ContactEx();
			for (FileRowsReader.FieldMapping mapping : mappings) {
				if (StringUtils.isBlank(mapping.source)) continue;
				if (!columnIndexes.containsKey(mapping.source)) throw new Exception("Index not found");
				Integer index = columnIndexes.get(mapping.source);
				fillContactByMapping(contact, mapping.target, rowBean.get(index));
			}
			if (contact.trimFieldLengths()) {
				ilog.add(new MessageLogEntry(LogEntry.Level.WARN, "Some fields were truncated due to max-length"));
			}
			if (!ilog.isEmpty()) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.WARN, "ROW [{0}]", row+1));
				log.addAll(ilog);
			}
			
		} catch(Throwable t) {
			log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "ROW [{0}]. Reason: {1}", row+1, t.getMessage()));
		} finally {
			boolean ret = beanHandler.handle(new ContactInput(contact, contact != null ? contact.getCompany() : null, null, null, null), log);
			if (!ret) throw new Exception("Handle not succesful");
		}
	}
	
	private void fillContactByMapping(ContactEx contact, String target, String value) {
		if (target.equals("Title")) {
			contact.setTitle(value);
		} else if (target.equals("FirstName")) {
			contact.setFirstName(value);
		} else if (target.equals("LastName")) {
			contact.setLastName(value);
		} else if (target.equals("DisplayName")) {
			contact.setDisplayName(value);
		} else if (target.equals("Nickname")) {
			contact.setNickname(value);
		} else if (target.equals("Gender")) {
			//TODO: gestire sesso
			//contact.setGender(value);
		} else if (target.equals("Mobile") || target.equals("WorkMobile")) {
			contact.setMobile(value);	
		} else if (target.equals("Pager1") || target.equals("WorkPager")) {
			contact.setPager1(value);
		} else if (target.equals("Pager2") || target.equals("HomePager")) {
			contact.setPager2(value);
		} else if (target.equals("Email1") || target.equals("WorkEmail")) {
			contact.setEmail1(value);
		} else if (target.equals("Email2") || target.equals("HomeEmail")) {
			contact.setEmail2(value);
		} else if (target.equals("Email3") || target.equals("OtherEmail")) {
			contact.setEmail3(value);
		} else if (target.equals("InstantMsg1") || target.equals("WorkInstantMsg")) {
			contact.setInstantMsg1(value);
		} else if (target.equals("InstantMsg2") || target.equals("HomeInstantMsg")) {
			contact.setInstantMsg2(value);
		} else if (target.equals("InstantMsg3") || target.equals("OtherInstantMsg")) {
			contact.setInstantMsg3(value);
		} else if (target.equals("WorkAddress")) {
			contact.setWorkAddress(value);
		} else if (target.equals("WorkPostalCode")) {
			contact.setWorkPostalCode(value);
		} else if (target.equals("WorkCity")) {
			contact.setWorkCity(value);
		} else if (target.equals("WorkState")) {
			contact.setWorkState(value);
		} else if (target.equals("WorkCountry")) {
			contact.setWorkCountry(value);
		} else if (target.equals("WorkTelephone1") || target.equals("WorkTelephone")) {
			contact.setWorkTelephone1(value);
		} else if (target.equals("WorkTelephone2")) {
			contact.setWorkTelephone2(value);
		} else if (target.equals("WorkFax")) {
			contact.setWorkFax(value);
		} else if (target.equals("HomeAddress")) {
			contact.setHomeAddress(value);
		} else if (target.equals("HomePostalCode")) {
			contact.setHomePostalCode(value);
		} else if (target.equals("HomeCity")) {
			contact.setHomeCity(value);
		} else if (target.equals("HomeState")) {
			contact.setHomeState(value);
		} else if (target.equals("HomeCountry")) {
			contact.setHomeCountry(value);
		} else if (target.equals("HomeTelephone1") || target.equals("HomeTelephone")) {
			contact.setHomeTelephone1(value);
		} else if (target.equals("HomeTelephone2")) {
			contact.setHomeTelephone2(value);
		} else if (target.equals("HomeFax")) {
			contact.setHomeFax(value);
		} else if (target.equals("OtherAddress")) {
			contact.setOtherAddress(value);
		} else if (target.equals("OtherPostalCode")) {
			contact.setOtherPostalCode(value);
		} else if (target.equals("OtherCity")) {
			contact.setOtherCity(value);
		} else if (target.equals("OtherState")) {
			contact.setOtherState(value);
		} else if (target.equals("OtherCountry")) {
			contact.setOtherCountry(value);		
		} else if (target.equals("Company")) {
			if (!StringUtils.isBlank(value)) {
				contact.setCompany(new ContactCompany(null, value));
			}
		} else if (target.equals("Function")) {
			contact.setFunction(value);
		} else if (target.equals("Department")) {
			contact.setDepartment(value);
		} else if (target.equals("Manager")) {
			contact.setManager(value);
		} else if (target.equals("Assistant")) {
			contact.setAssistant(value);
		} else if (target.equals("AssistantTelephone")) {
			contact.setAssistantTelephone(value);
		} else if (target.equals("Partner")) {
			contact.setPartner(value);
		} else if (target.equals("Birthday")) {
			//TODO: gestire compleanno
		} else if (target.equals("Anniversary")) {
			//TODO: gestire anniversario
		} else if (target.equals("Url")) {
			contact.setUrl(value);
		} else if (target.equals("Notes")) {
			contact.setNotes(value);
		}
	}
	
	private class XlsRowHandler implements RowHandler<RowValues> {
		private final ContactExcelFileReader excelReader;
		private final HashMap<String, Integer> columnIndexes;
		private final BeanHandler beanHandler;
		
		public XlsRowHandler(ContactExcelFileReader excelReader, HashMap<String, Integer> columnIndexes, BeanHandler beanHandler) {
			this.excelReader = excelReader;
			this.columnIndexes = columnIndexes;
			this.beanHandler = beanHandler;
		}
		
		@Override
		public void handle(int row, RowValues rowBean) throws Exception {
			excelReader.readRow(columnIndexes, beanHandler, row, rowBean);
		}
	}
}
