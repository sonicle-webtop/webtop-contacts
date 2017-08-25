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

import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactPicture;
import com.sonicle.webtop.contacts.util.VCardHelper;
import com.sonicle.webtop.core.io.BeanHandler;
import com.sonicle.webtop.core.io.input.FileReaderException;
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
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 *
 * @author malbinola
 */
public class ContactVCardFileReader implements ContactFileReader {

	@Override
	public void readContacts(File file, BeanHandler beanHandler) throws IOException, FileReaderException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			readContacts(fis, beanHandler);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}
	
	public void readContacts(InputStream is, BeanHandler beanHandler) throws IOException, FileReaderException {
		// See https://tools.ietf.org/html/rfc6350
		// See http://www.w3.org/TR/vcard-rdf/
		
		List<VCard> vcs = Ezvcard.parse(is).all();
		for(VCard vc : vcs) {
			LogEntries log = new LogEntries();
			ContactReadResult result = null;
			try {
				LogEntries vclog = new LogEntries();
				result = readVCard(vclog, vc);
				if(!vclog.isEmpty()) {
					log.addMaster(new MessageLogEntry(LogEntry.Level.WARN, "VCARD [{0}]", vc.getUid()));
					log.addAll(vclog);
				}
				
			} catch(Throwable t) {
				log.addMaster(new MessageLogEntry(LogEntry.Level.ERROR, "VCARD [{0}]. Reason: {1}", vc.getUid(), t.getMessage()));
			} finally {
				try {
					beanHandler.handle(result, log);
				} catch(Exception ex) {
					throw new FileReaderException(ex);
				}
			}
		}
	}
	
	public ContactReadResult readVCard(LogEntries log, VCard vc) throws Exception {
		VCardHelper.ToContactResult result = VCardHelper.toContact(vc, log);
		return new ContactReadResult(result.contact, result.picture);
	}
	
	/*
	public ContactReadResult readVCard(LogEntries log, VCard vc) throws Exception {
		Contact contact = new Contact();
		ContactPicture picture = null;
		
		// UID
		if(vc.getUid() != null) {
			contact.setPublicUid(deflt(vc.getUid().getValue()));
		}
		
		// TITLE
		if(!vc.getTitles().isEmpty()) {
			Title ti = vc.getTitles().get(0);
			contact.setTitle(deflt(ti.getValue()));
			if(vc.getTitles().size() > 1) log.add(new MessageLogEntry(LogEntry.Level.WARN, "Many TITLE properties found"));
		}
		
		// N -> FirstName/LastName
		if(!vc.getStructuredNames().isEmpty()) {
			StructuredName sn = vc.getStructuredNames().get(0);
			contact.setFirstName(deflt(sn.getGiven()));
			contact.setLastName(deflt(sn.getFamily()));
			if(vc.getStructuredNames().size() > 1) log.add(new MessageLogEntry(LogEntry.Level.WARN, "Many N properties found"));
		}
		
		// NICKNAME
		if(!vc.getNicknames().isEmpty()) {
			Nickname ni = vc.getNicknames().get(0);
			contact.setNickname(deflt(flatten(ni)));
			if(vc.getNicknames().size() > 1) log.add(new MessageLogEntry(LogEntry.Level.WARN, "Many NICKNAME properties found"));
		}
		
		// GENDER
		if(vc.getGender() != null) {
			contact.setGender(parseGender(vc.getGender()));
		}
		
		// ADR
		if(!vc.getAddresses().isEmpty()) {
			for(Address ad : vc.getAddresses()) {
				Set<AddressType> types = ad.getTypes();
				if(types.contains(AddressType.WORK)) {
					contact.setWorkAddress(deflt(ad.getStreetAddress()));
					contact.setWorkPostalCode(deflt(ad.getPostalCode()));
					contact.setWorkCity(deflt(ad.getLocality()));
					contact.setWorkState(deflt(ad.getRegion()));
					contact.setWorkCountry(deflt(ad.getCountry()));
				} else if(types.contains(AddressType.HOME)) {
					contact.setHomeAddress(deflt(ad.getStreetAddress()));
					contact.setHomePostalCode(deflt(ad.getPostalCode()));
					contact.setHomeCity(deflt(ad.getLocality()));
					contact.setHomeState(deflt(ad.getRegion()));
					contact.setHomeCountry(deflt(ad.getCountry()));
				} else if(!types.contains(AddressType.WORK) && !types.contains(AddressType.HOME)) {
					contact.setOtherAddress(deflt(ad.getStreetAddress()));
					contact.setOtherPostalCode(deflt(ad.getPostalCode()));
					contact.setOtherCity(deflt(ad.getLocality()));
					contact.setOtherState(deflt(ad.getRegion()));
					contact.setOtherCountry(deflt(ad.getCountry()));
				}
			}
		}
		
		// TEL
		if(!vc.getTelephoneNumbers().isEmpty()) {
			for(Telephone te : vc.getTelephoneNumbers()) {
				Set<TelephoneType> types = te.getTypes();
				if(types.contains(TelephoneType.WORK)) {
					if(types.contains(TelephoneType.VOICE)) {
						contact.setWorkTelephone(deflt(te.getText()));
					} else if(types.contains(TelephoneType.CELL)) {
						contact.setWorkMobile(deflt(te.getText()));
					} else if(types.contains(TelephoneType.FAX)) {
						contact.setWorkFax(deflt(te.getText()));
					} else if(types.contains(TelephoneType.PAGER)) {
						contact.setWorkPager(deflt(te.getText()));
					} else if(types.contains(TelephoneType.TEXT)) {
						contact.setWorkTelephone2(deflt(te.getText()));
					}
				} else if(types.contains(TelephoneType.HOME)) {
					if(types.contains(TelephoneType.VOICE)) {
						contact.setHomeTelephone(deflt(te.getText()));
					} else if(types.contains(TelephoneType.FAX)) {
						contact.setHomeFax(deflt(te.getText()));
					} else if(types.contains(TelephoneType.PAGER)) {
						contact.setHomePager(deflt(te.getText()));
					} else if(types.contains(TelephoneType.CELL)) {
						contact.setHomeTelephone2(deflt(te.getText()));
					} else if(types.contains(TelephoneType.TEXT)) {
						contact.setHomeTelephone2(deflt(te.getText()));
					}
				}
			}
		}
		
		// EMAIL
		if(!vc.getEmails().isEmpty()) {
			for(Email em : vc.getEmails()) {
				Set<EmailType> types = em.getTypes();
				if(types.contains(EmailType.WORK)) {
					contact.setWorkEmail(deflt(em.getValue()));
				} else if(types.contains(EmailType.HOME)) {
					contact.setHomeEmail(deflt(em.getValue()));
				} else if(!types.contains(EmailType.WORK) && !types.contains(EmailType.HOME)) {
					contact.setOtherEmail(deflt(em.getValue()));
				}
			}
		}
		
		// IMPP -> InstantMsg
		if(!vc.getImpps().isEmpty()) {
			for(Impp im : vc.getImpps()) {
				Set<ImppType> types = im.getTypes();
				URI uri = im.getUri();
				if(uri == null) continue;
				if(types.contains(ImppType.WORK)) {
					contact.setWorkInstantMsg(deflt(uri.toString()));
				} else if(types.contains(ImppType.HOME)) {
					contact.setHomeInstantMsg(deflt(uri.toString()));
				} else if(!types.contains(ImppType.WORK) && !types.contains(ImppType.HOME)) {
					contact.setOtherInstantMsg(deflt(uri.toString()));
				}
			}
		}
		
		// ORG -> Company/Department
		if(vc.getOrganization() != null) {
			List<String> values = vc.getOrganization().getValues();
			if(!values.isEmpty()) {
				contact.setCompany(deflt(values.get(0)));
				contact.setDepartment(deflt(values.get(1)));
			}
		}
		
		// ROLE -> Function
		if(!vc.getRoles().isEmpty()) {
			Role ro = vc.getRoles().get(0);
			contact.setFunction(deflt(ro.getValue()));
			if(vc.getRoles().size() > 1) log.add(new MessageLogEntry(LogEntry.Level.WARN, "Many ROLE properties found"));
		}
		
		//TODO: come riempiamo il campo manager?
		//TODO: come riempiamo il campo assistant?
		//TODO: come riempiamo il campo telephoneAssistant?
		
		// BDAY
		if(vc.getBirthday() != null) {
			contact.setBirthday(new LocalDate(vc.getBirthday().getDate()));
		}
		
		// ANNIVERSARY
		if(vc.getAnniversary()!= null) {
			contact.setAnniversary(new LocalDate(vc.getAnniversary().getDate()));
		}
		
		// URL
		if(!vc.getUrls().isEmpty()) {
			Url ur = vc.getUrls().get(0);
			contact.setUrl(deflt(ur.getValue()));
			if(vc.getUrls().size() > 1) log.add(new MessageLogEntry(LogEntry.Level.WARN, "Many URL properties found"));
		}
		
		// NOTE
		if(!vc.getNotes().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(Note no : vc.getNotes()) {
				sb.append(no.getValue());
				sb.append("\n");
			}
			contact.setNotes(sb.toString());
		}
		
		// PHOTO
		if(!vc.getPhotos().isEmpty()) {
			Photo po = vc.getPhotos().get(0);
			picture = new ContactPicture(po.getContentType().getMediaType(), po.getData());
			if(vc.getPhotos().size() > 1) log.add(new MessageLogEntry(LogEntry.Level.WARN, "Many PHOTO properties found"));
		} else {
			contact.setHasPicture(false);
		}
		
		return new ContactReadResult(contact, picture);
	}
	
	public static String parseGender(Gender ge) {
		if(ge.isMale()) return Contact.Gender.MALE.toString();
		if(ge.isFemale()) return Contact.Gender.FEMALE.toString();
		if(ge.isOther()) return Contact.Gender.OTHER.toString();
		return null;
	}
	
	private String deflt(String s) {
		return StringUtils.defaultIfEmpty(s, null);
	}
	
	private String flatten(TextListProperty textListProp) {
		return flatten(textListProp, " ");
	}
	
	private String flatten(TextListProperty textListProp, String separator) {
		return StringUtils.join(textListProp.getValues(), separator);
	}
	*/
}
