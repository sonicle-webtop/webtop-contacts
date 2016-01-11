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
		setListId(cnt.getListId());
		setStatus(cnt.getStatus());
		setTitle(cnt.getTitle());
		setFirstname(cnt.getFirstName());
		setLastname(cnt.getLastName());
		setNickname(cnt.getNickname());
		setGender(cnt.getGender());
		setCaddress(cnt.getWorkAddress());
		setCpostalcode(cnt.getWorkPostalCode());
		setCcity(cnt.getWorkCity());
		setCstate(cnt.getWorkState());
		setCcountry(cnt.getWorkCountry());
		setCtelephone(cnt.getWorkTelephone());
		setCtelephone2(cnt.getWorkTelephone2());
		setCmobile(cnt.getWorkMobile());
		setCfax(cnt.getWorkFax());
		setCpager(cnt.getWorkPager());
		setCemail(cnt.getWorkEmail());
		setCinstantMsg(cnt.getWorkInstantMsg());
		setHaddress(cnt.getHomeAddress());
		setHpostalcode(cnt.getHomePostalCode());
		setHcity(cnt.getHomeCity());
		setHstate(cnt.getHomeState());
		setHcountry(cnt.getHomeCountry());
		setHtelephone(cnt.getHomeTelephone());
		//setHtelephone2(cont.getHomeTelephone2());
		setHmobile(cnt.getHomeMobile());
		setHfax(cnt.getHomeFax());
		setHpager(cnt.getHomePager());
		setHemail(cnt.getHomeEmail());
		setHinstantMsg(cnt.getHomeInstantMsg());
		setOaddress(cnt.getOtherAddress());
		setOpostalcode(cnt.getOtherPostalCode());
		setOcity(cnt.getOtherCity());
		setOstate(cnt.getOtherState());
		setOcountry(cnt.getOtherCountry());
		setOemail(cnt.getOtherEmail());
		setOinstantMsg(cnt.getOtherInstantMsg());
		setCompany(cnt.getCompany());
		setFunction(cnt.getFunction());
		setCdepartment(cnt.getDepartment());
		setCmanager(cnt.getManager());
		setCassistant(cnt.getAssistant());
		setCtelephoneassistant(cnt.getAssistantTelephone());
		setHpartner(cnt.getPartner());
		setHbirthday(cnt.getBirthday());
		setHanniversary(cnt.getAnniversary());
		setUrl(cnt.getUrl());
		setNotes(cnt.getNotes());
	}
	
	public void setRevisionInfo(BaseDAO.RevisionInfo revision) {
		setLastModified(revision.lastModified);
		setUpdateDevice(revision.lastDevice);
		setUpdateUser(revision.lastUser);
	}
	
	public boolean isList() {
		if(getListId() == null) return false;
		return getListId() > 0;
	}
}
