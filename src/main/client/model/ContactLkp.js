/* 
 * Copyright (C) 2022 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2022 Sonicle S.r.l.".
 */
Ext.define('Sonicle.webtop.contacts.model.ContactLkp', {
	extend: 'WTA.ux.data.EmptyModel',
	requires: [
		'Sonicle.String'
	],
	
	idProperty: 'id',
	fields: [
		WTF.roField('id', 'string'),
		WTF.roField('displayName', 'string'),
		WTF.roField('title', 'string'),
		WTF.roField('firstName', 'string'),
		WTF.roField('lastName', 'string'),
		WTF.roField('company', 'string'),
		WTF.roField('function', 'string'),
		WTF.roField('email1', 'string'),
		WTF.roField('email2', 'string'),
		WTF.roField('workTelephone1', 'string'),
		WTF.roField('homeTelephone1', 'string'),
		WTF.roField('mobile', 'string'),
		WTF.roField('catId', 'int'),
		WTF.roField('catName', 'string'),
		WTF.roField('catColor', 'string'),
		WTF.roField('tags', 'string'),
		WTF.roField('_ownerId', 'string'),
		WTF.roField('_frights', 'string'),
		WTF.roField('_erights', 'string'),
		WTF.calcField('fullName', 'string', ['title', 'firstName', 'lastName'], function(v, rec) {
			return Sonicle.String.join(' ', rec.get('title'), rec.get('firstName'), rec.get('lastName'));
		}),
		WTF.calcField('name', 'string', ['displayName', 'firstName', 'lastName'], function(v, rec, dn, fn, ln) {
			var s = Sonicle.String.coalesce(dn, Sonicle.String.join(' ', fn, ln));
			return Ext.isEmpty(s) ? WT.res('com.sonicle.webtop.contacts', 'contact.fld-displayName.emp') : s;
		}),
		WTF.calcField('email', 'string', ['email1', 'email2'], function(v, rec, ema1, ema2) {
			return Sonicle.String.coalesce(ema1, ema2);
		}),
		WTF.calcField('phone', 'string', ['mobile', 'workTelephone1', 'homeTelephone1'], function(v, rec, mob, wt1, ht1) {
			return Sonicle.String.coalesce(mob, wt1, ht1);
		}),
		WTF.calcField('emails', 'string', ['email1', 'email2'], function(v, rec, ema1, ema2) {
			return Sonicle.String.join(', ', ema1, ema2);
		}),
		WTF.calcField('phones', 'string', ['mobile', 'workTelephone1', 'homeTelephone1'], function(v, rec, mob, wt1, ht1) {
			return Sonicle.String.join(', ', mob, wt1, ht1);
		})
	]
});
