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
Ext.define('Sonicle.webtop.contacts.model.Contact', {
	extend: 'WT.ux.data.BaseModel',
	proxy: WTF.apiProxy('com.sonicle.webtop.contacts', 'ManageContacts'),
	
	identifier: 'negative',
	idProperty: 'id',
	fields: [
		WTF.field('id', 'int', false),
		WTF.field('categoryId', 'int', false),
		WTF.field('title', 'string', true),
		WTF.field('firstName', 'string', true),
		WTF.field('lastName', 'string', true),
		WTF.field('nickname', 'string', true),
		WTF.field('gender', 'string', true),
		WTF.field('workAddress', 'string', true),
		WTF.field('workPostalCode', 'string', true),
		WTF.field('workCity', 'string', true),
		WTF.field('workState', 'string', true),
		WTF.field('workCountry', 'string', true),
		WTF.field('workTelephone', 'string', true),
		WTF.field('workTelephone2', 'string', true),
		WTF.field('workMobile', 'string', true),
		WTF.field('workFax', 'string', true),
		WTF.field('workPager', 'string', true),
		WTF.field('workEmail', 'string', true),
		WTF.field('workInstantMsg', 'string', true),
		WTF.field('homeAddress', 'string', true),
		WTF.field('homePostalCode', 'string', true),
		WTF.field('homeCity', 'string', true),
		WTF.field('homeState', 'string', true),
		WTF.field('homeCountry', 'string', true),
		WTF.field('homeTelephone', 'string', true),
		WTF.field('homeTelephone2', 'string', true),
		WTF.field('homeFax', 'string', true),
		WTF.field('homePager', 'string', true),
		WTF.field('homeEmail', 'string', true),
		WTF.field('homeInstantMsg', 'string', true),
		WTF.field('otherAddress', 'string', true),
		WTF.field('otherPostalCode', 'string', true),
		WTF.field('otherCity', 'string', true),
		WTF.field('otherState', 'string', true),
		WTF.field('otherCountry', 'string', true),
		WTF.field('otherEmail', 'string', true),
		WTF.field('otherInstantMsg', 'string', true),
		WTF.field('company', 'string', true),
		WTF.field('function', 'string', true),
		WTF.field('department', 'string', true),
		WTF.field('manager', 'string', true),
		WTF.field('assistant', 'string', true),
		WTF.field('assistantTelephone', 'string', true),
		WTF.field('partner', 'string', true),
		WTF.field('birthday', 'date', true, {dateFormat: 'Y-m-d'}),
		WTF.field('anniversary', 'date', true, {dateFormat: 'Y-m-d'}),
		WTF.field('url', 'string', true),
		WTF.field('notes', 'string', true),
		WTF.field('picture', 'string', true),
		WTF.field('_profileId', 'string', false)
	]
});
