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
package com.sonicle.webtop.contacts;

import com.sonicle.webtop.contacts.bol.model.Contact;
import com.sonicle.webtop.contacts.bol.model.ContactPicture;
import com.sonicle.webtop.core.util.LogEntries;
import com.sonicle.webtop.core.util.LogEntry;
import com.sonicle.webtop.core.util.MessageLogEntry;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.ImageType;
import ezvcard.parameter.ImppType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Anniversary;
import ezvcard.property.Birthday;
import ezvcard.property.Email;
import ezvcard.property.Gender;
import ezvcard.property.Impp;
import ezvcard.property.Nickname;
import ezvcard.property.Note;
import ezvcard.property.Organization;
import ezvcard.property.Photo;
import ezvcard.property.Role;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.TextListProperty;
import ezvcard.property.Title;
import ezvcard.property.Uid;
import ezvcard.property.Url;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 *
 * @author malbinola
 */
public class VCardHelper {
	
	public static ArrayList<ParseResult> parseVCard(LogEntries log, InputStream is) throws IOException {
		// See https://tools.ietf.org/html/rfc6350
		// See http://www.w3.org/TR/vcard-rdf/
		ArrayList<ParseResult> results = new ArrayList<>();
		
		List<VCard> vcs = Ezvcard.parse(is).all();
		LogEntries vclog = null;
		for(VCard vc : vcs) {
			vclog = new LogEntries();
			try {
				results.add(parseVCard(vclog, vc));
				if(!vclog.isEmpty()) {
					log.addMaster(new MessageLogEntry(LogEntry.LEVEL_WARN, "VCARD [{0}]", vc.getUid()));
					log.addAll(vclog);
				}
			} catch(Throwable t) {
				log.addMaster(new MessageLogEntry(LogEntry.LEVEL_ERROR, "VCARD [{0}]. Reason: {1}", vc.getUid(), t.getMessage()));
			}
		}
		return results;
	}
	
	public static ParseResult parseVCard(LogEntries log, VCard vc) throws Exception {
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
			if(vc.getTitles().size() > 1) log.add(new MessageLogEntry(LogEntry.LEVEL_WARN, "Many TITLE properties found"));
		}
		
		// N -> FirstName/LastName
		if(!vc.getStructuredNames().isEmpty()) {
			StructuredName sn = vc.getStructuredNames().get(0);
			contact.setFirstName(deflt(sn.getGiven()));
			contact.setLastName(deflt(sn.getFamily()));
			if(vc.getStructuredNames().size() > 1) log.add(new MessageLogEntry(LogEntry.LEVEL_WARN, "Many N properties found"));
		}
		
		// NICKNAME
		if(!vc.getNicknames().isEmpty()) {
			Nickname ni = vc.getNicknames().get(0);
			contact.setNickname(deflt(flatten(ni)));
			if(vc.getNicknames().size() > 1) log.add(new MessageLogEntry(LogEntry.LEVEL_WARN, "Many NICKNAME properties found"));
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
			if(vc.getRoles().size() > 1) log.add(new MessageLogEntry(LogEntry.LEVEL_WARN, "Many ROLE properties found"));
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
			if(vc.getUrls().size() > 1) log.add(new MessageLogEntry(LogEntry.LEVEL_WARN, "Many URL properties found"));
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
			if(vc.getPhotos().size() > 1) log.add(new MessageLogEntry(LogEntry.LEVEL_WARN, "Many PHOTO properties found"));
		} else {
			contact.setHasPicture(false);
		}
		
		return new ParseResult(contact, picture);
	}
	
	public static String parseGender(Gender ge) {
		if(ge.isMale()) return Contact.Gender.MALE.toString();
		if(ge.isFemale()) return Contact.Gender.FEMALE.toString();
		if(ge.isOther()) return Contact.Gender.OTHER.toString();
		return null;
	}
	
	public static VCard convertContact(Contact contact, ContactPicture picture) throws Exception {
		VCard vc = new VCard();
		
		// UID
		vc.setUid(new Uid(contact.getPublicUid()));
		
		// TITLE
		Title ti = extractTitle(contact);
		if(ti != null) vc.getTitles().add(ti);
		
		// N -> FirstName/LastName
		StructuredName sn = extractNames(contact);
		if(sn != null) vc.setStructuredName(sn);
		
		// NICKNAME
		Nickname ni = extractNickname(contact);
		if(ni != null) vc.setNickname(ni);
		
		// GENDER
		Gender ge = extractGender(contact);
		if(ge != null) vc.setGender(ge);
		
		// ADR
		List<Address> addrs = extractAddresses(contact);
		if(!addrs.isEmpty()) vc.getAddresses().addAll(addrs);
		
		// TEL
		List<Telephone> tels = extractTelephoneNumbers(contact);
		if(!tels.isEmpty()) vc.getTelephoneNumbers().addAll(tels);
		
		// EMAIL
		List<Email> ems = extractEmails(contact);
		if(!ems.isEmpty()) vc.getEmails().addAll(ems);
		
		// IMPP -> InstantMsg
		List<Impp> impps = extractImpps(contact);
		if(!impps.isEmpty()) vc.getImpps().addAll(impps);
		
		// ORG -> Company/Department
		Organization or = extractOrganization(contact);
		if(or != null) vc.setOrganization(or);
		
		// ROLE <- Function
		Role ro = extractRole(contact);
		if(ro != null) vc.getRoles().add(ro);
		
		//TODO: come riempiamo il campo manager?
		//TODO: come riempiamo il campo assistant?
		//TODO: come riempiamo il campo telephoneAssistant?
		
		// BDAY
		Birthday bd = extractBirthday(contact);
		if(bd != null) vc.setBirthday(bd);
		
		// ANNIVERSARY
		Anniversary an = extractAnniversary(contact);
		if(an != null) vc.setAnniversary(an);
		
		// URL
		Url ur = extractUrl(contact);
		if(ur != null) vc.getUrls().add(ur);
		
		// NOTE
		Note no = extractNotes(contact);
		if(no != null) vc.getNotes().add(no);
		
		// PHOTO
		if(contact.getHasPicture()) {
			Photo ph = extractPhoto(picture);
			if(ph != null) vc.getPhotos().add(ph);
		}
		
		return vc;
	}
	
	public static Photo extractPhoto(ContactPicture picture) {
		Photo prop = null;
		if(picture != null) {
			ImageType it = null;
			if(StringUtils.equals(picture.getMediaType(), ImageType.GIF.getMediaType())) {
				it = ImageType.GIF;
			} else if(StringUtils.equals(picture.getMediaType(), ImageType.JPEG.getMediaType())) {
				it = ImageType.JPEG;
			} else if(StringUtils.equals(picture.getMediaType(), ImageType.PNG.getMediaType())) {
				it = ImageType.PNG;
			}
			if(it != null) prop = new Photo(picture.getBytes(), it);
		}
		return prop;
	}
	
	public static Title extractTitle(Contact contact) {
		Title prop = null;
		if(!StringUtils.isEmpty(contact.getTitle())) {
			prop = new Title(contact.getTitle());
		}
		return prop;
	}
	
	public static StructuredName extractNames(Contact contact) {
		StructuredName prop = null;
		if(!contact.isNameEmpty()) {
			prop = new StructuredName();
			prop.setGiven(deflt(contact.getFirstName()));
			prop.setFamily(deflt(contact.getLastName()));
		}
		return prop;
	}
	
	public static Nickname extractNickname(Contact contact) {
		Nickname prop = null;
		if(!StringUtils.isEmpty(contact.getNickname())) {
			prop = new Nickname();
			prop.getValues().add(contact.getNickname());
		}
		return prop;
	}
	
	public static Gender extractGender(Contact contact) {
		Gender prop = null;
		if(!StringUtils.equals(contact.getGender(), Contact.Gender.MALE.toString())) {
			prop = new Gender("MALE");
		} else if(!StringUtils.equals(contact.getGender(), Contact.Gender.FEMALE.toString())) {
			prop = new Gender("FEMALE");
		} else if(!StringUtils.equals(contact.getGender(), Contact.Gender.OTHER.toString())) {
			prop = new Gender("OTHER");
		}
		return prop;
	}
	
	public static List<Address> extractAddresses(Contact contact) {
		List<Address> props = new ArrayList<>();
		if(!contact.isWorkAddressEmpty()) {
			Address addr = new Address();
			addr.getTypes().add(AddressType.WORK);
			addr.setStreetAddress(deflt(contact.getWorkAddress()));
			addr.setPostalCode(deflt(contact.getWorkPostalCode()));
			addr.setLocality(deflt(contact.getWorkCity()));
			addr.setRegion(deflt(contact.getWorkState()));
			addr.setCountry(deflt(contact.getWorkCountry()));
		}
		if(!contact.isHomeAddressEmpty()) {
			Address addr = new Address();
			addr.getTypes().add(AddressType.HOME);
			addr.setStreetAddress(deflt(contact.getHomeAddress()));
			addr.setPostalCode(deflt(contact.getHomePostalCode()));
			addr.setLocality(deflt(contact.getHomeCity()));
			addr.setRegion(deflt(contact.getHomeState()));
			addr.setCountry(deflt(contact.getHomeCountry()));
		}
		if(!contact.isOtherAddressEmpty()) {
			Address addr = new Address();
			addr.getTypes().add(AddressType.POSTAL); //TODO: che tipo usiamo?
			addr.setStreetAddress(deflt(contact.getOtherAddress()));
			addr.setPostalCode(deflt(contact.getOtherPostalCode()));
			addr.setLocality(deflt(contact.getOtherCity()));
			addr.setRegion(deflt(contact.getOtherState()));
			addr.setCountry(deflt(contact.getOtherCountry()));
		}
		return props;
	}
	
	public static List<Telephone> extractTelephoneNumbers(Contact contact) {
		List<Telephone> props = new ArrayList<>();
		if(!StringUtils.isEmpty(contact.getWorkTelephone())) {
			Telephone tel = new Telephone(contact.getWorkTelephone());
			tel.getTypes().add(TelephoneType.WORK);
			tel.getTypes().add(TelephoneType.VOICE);
			props.add(tel);
		}
		if(!StringUtils.isEmpty(contact.getWorkMobile())) {
			Telephone tel = new Telephone(contact.getWorkMobile());
			tel.getTypes().add(TelephoneType.WORK);
			tel.getTypes().add(TelephoneType.CELL);
			props.add(tel);
		}
		if(!StringUtils.isEmpty(contact.getWorkFax())) {
			Telephone tel = new Telephone(contact.getWorkFax());
			tel.getTypes().add(TelephoneType.WORK);
			tel.getTypes().add(TelephoneType.FAX);
			props.add(tel);
		}
		if(!StringUtils.isEmpty(contact.getWorkPager())) {
			Telephone tel = new Telephone(contact.getWorkPager());
			tel.getTypes().add(TelephoneType.WORK);
			tel.getTypes().add(TelephoneType.PAGER);
			props.add(tel);
		}
		if(!StringUtils.isEmpty(contact.getWorkTelephone2())) {
			Telephone tel = new Telephone(contact.getWorkTelephone2());
			tel.getTypes().add(TelephoneType.WORK);
			tel.getTypes().add(TelephoneType.TEXT);
			props.add(tel);
		}
		if(!StringUtils.isEmpty(contact.getHomeTelephone())) {
			Telephone tel = new Telephone(contact.getHomeTelephone());
			tel.getTypes().add(TelephoneType.HOME);
			tel.getTypes().add(TelephoneType.VOICE);
			props.add(tel);
		}
		if(!StringUtils.isEmpty(contact.getHomeTelephone2())) {
			Telephone tel = new Telephone(contact.getHomeTelephone2());
			tel.getTypes().add(TelephoneType.HOME);
			tel.getTypes().add(TelephoneType.TEXT);
			props.add(tel);
		}
		if(!StringUtils.isEmpty(contact.getHomeFax())) {
			Telephone tel = new Telephone(contact.getHomeFax());
			tel.getTypes().add(TelephoneType.HOME);
			tel.getTypes().add(TelephoneType.FAX);
			props.add(tel);
		}
		if(!StringUtils.isEmpty(contact.getHomePager())) {
			Telephone tel = new Telephone(contact.getHomePager());
			tel.getTypes().add(TelephoneType.HOME);
			tel.getTypes().add(TelephoneType.PAGER);
			props.add(tel);
		}
		return props;
	}
	
	public static List<Email> extractEmails(Contact contact) {
		List<Email> props = new ArrayList<>();
		if(!StringUtils.isEmpty(contact.getWorkEmail())) {
			Email em = new Email(contact.getWorkEmail());
			em.getTypes().add(EmailType.WORK);
			props.add(em);
		}
		if(!StringUtils.isEmpty(contact.getHomeEmail())) {
			Email em = new Email(contact.getHomeEmail());
			em.getTypes().add(EmailType.HOME);
			props.add(em);
		}
		if(!StringUtils.isEmpty(contact.getOtherEmail())) {
			Email em = new Email(contact.getOtherEmail());
			em.getTypes().add(EmailType.AOL); //TODO: che tipo mettiamo?
			props.add(em);
		}
		return props;
	}
	
	public static List<Impp> extractImpps(Contact contact) {
		List<Impp> props = new ArrayList<>();
		if(!StringUtils.isEmpty(contact.getWorkInstantMsg())) {
			Impp impp = new Impp(contact.getWorkInstantMsg());
			impp.getTypes().add(ImppType.WORK);
			props.add(impp);
		}
		if(!StringUtils.isEmpty(contact.getHomeInstantMsg())) {
			Impp impp = new Impp(contact.getHomeInstantMsg());
			impp.getTypes().add(ImppType.HOME);
			props.add(impp);
		}
		if(!StringUtils.isEmpty(contact.getOtherInstantMsg())) {
			Impp impp = new Impp(contact.getOtherInstantMsg());
			impp.getTypes().add(ImppType.PERSONAL); //TODO: che tipo mettiamo?
			props.add(impp);
		}
		return props;
	}
	
	public static Organization extractOrganization(Contact contact) {
		Organization prop = null;
		if(!StringUtils.isEmpty(contact.getCompany()) || !StringUtils.isEmpty(contact.getDepartment())) {
			prop = new Organization();
			prop.getValues().add(StringUtils.defaultString(contact.getCompany()));
			prop.getValues().add(StringUtils.defaultString(contact.getDepartment()));
		}
		return prop;
	}
	
	public static Role extractRole(Contact contact) {
		Role prop = null;
		if(contact.getFunction() != null) {
			prop = new Role(contact.getFunction());
		}
		return prop;
	}
	
	public static Birthday extractBirthday(Contact contact) {
		Birthday prop = null;
		if(contact.getBirthday() != null) {
			prop = new Birthday(contact.getBirthday().toDate());
		}
		return prop;
	}
	
	public static Anniversary extractAnniversary(Contact contact) {
		Anniversary prop = null;
		if(contact.getAnniversary() != null) {
			prop = new Anniversary(contact.getAnniversary().toDate());
		}
		return prop;
	}
	
	public static Url extractUrl(Contact contact) {
		Url prop = null;
		if(!StringUtils.isEmpty(contact.getUrl())) {
			prop = new Url(contact.getUrl());
		}
		return prop;
	}
	
	public static Note extractNotes(Contact contact) {
		Note prop = null;
		if(!StringUtils.isEmpty(contact.getNotes())) {
			prop = new Note(contact.getNotes());
		}
		return prop;
	}
	
	private static String deflt(String s) {
		return StringUtils.defaultIfEmpty(s, null);
	}
	
	private static String flatten(TextListProperty textListProp) {
		return flatten(textListProp, " ");
	}
	
	private static String flatten(TextListProperty textListProp, String separator) {
		return StringUtils.join(textListProp.getValues(), separator);
	}
	
	public static class ParseResult {
		Contact contact;
		ContactPicture picture;
		
		public ParseResult(Contact contact, ContactPicture picture) {
			this.contact = contact;
			this.picture = picture;
		}
	}
}
