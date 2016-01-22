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
package com.sonicle.webtop.contacts.bol;

import com.sonicle.webtop.contacts.bol.model.Contact;
import com.sonicle.webtop.contacts.jooq.tables.pojos.Contacts;
import com.sonicle.webtop.core.dal.BaseDAO;

/**
 *
 * @author malbinola
 */
public class OContact extends Contacts {
	public final static String STATUS_NEW = "N";
	public final static String STATUS_MODIFIED = "M";
	public final static String STATUS_DELETED = "D";
	
	public OContact() {
		super();
		setStatus(STATUS_NEW);
	}
	
	public OContact(Contact cnt) {
		setContactId(cnt.getContactId());
		setCategoryId(cnt.getCategoryId());
		setIsList(false);
		setStatus(cnt.getStatus());
		setTitle(cnt.getTitle());
		setFirstname(cnt.getFirstName());
		setLastname(cnt.getLastName());
		setNickname(cnt.getNickname());
		setGender(cnt.getGender());
		setWorkAddress(cnt.getWorkAddress());
		setWorkPostalcode(cnt.getWorkPostalCode());
		setWorkCity(cnt.getWorkCity());
		setWorkState(cnt.getWorkState());
		setWorkCountry(cnt.getWorkCountry());
		setWorkTelephone(cnt.getWorkTelephone());
		setWorkTelephone2(cnt.getWorkTelephone2());
		setWorkMobile(cnt.getWorkMobile());
		setWorkFax(cnt.getWorkFax());
		setWorkPager(cnt.getWorkPager());
		setWorkEmail(cnt.getWorkEmail());
		setWorkIm(cnt.getWorkInstantMsg());
		setHomeAddress(cnt.getHomeAddress());
		setHomePostalcode(cnt.getHomePostalCode());
		setHomeCity(cnt.getHomeCity());
		setHomeState(cnt.getHomeState());
		setHomeCountry(cnt.getHomeCountry());
		setHomeTelephone(cnt.getHomeTelephone());
		setHomeTelephone2(cnt.getHomeTelephone2());
		setHomeFax(cnt.getHomeFax());
		setHomePager(cnt.getHomePager());
		setHomeEmail(cnt.getHomeEmail());
		setHomeIm(cnt.getHomeInstantMsg());
		setOtherAddress(cnt.getOtherAddress());
		setOtherPostalcode(cnt.getOtherPostalCode());
		setOtherCity(cnt.getOtherCity());
		setOtherState(cnt.getOtherState());
		setOtherCountry(cnt.getOtherCountry());
		setOtherEmail(cnt.getOtherEmail());
		setOtherIm(cnt.getOtherInstantMsg());
		setCompany(cnt.getCompany());
		setFunction(cnt.getFunction());
		setDepartment(cnt.getDepartment());
		setManager(cnt.getManager());
		setAssistant(cnt.getAssistant());
		setAssistantTelephone(cnt.getAssistantTelephone());
		setPartner(cnt.getPartner());
		setBirthday(cnt.getBirthday());
		setAnniversary(cnt.getAnniversary());
		setUrl(cnt.getUrl());
		setNotes(cnt.getNotes());
	}
	
	public void setRevisionInfo(BaseDAO.RevisionInfo revision) {
		setLastModified(revision.lastModified);
		setUpdateDevice(revision.lastDevice);
		setUpdateUser(revision.lastUser);
	}
}
