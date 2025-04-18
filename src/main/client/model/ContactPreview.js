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
Ext.define('Sonicle.webtop.contacts.model.ContactPreview', {
	extend: 'WTA.ux.data.EmptyModel',
	mixins: [
		'WTA.sdk.mixin.ItemWithinFolder'	
	],
	requires: [
		'Sonicle.webtop.contacts.model.ContactValueItem'
	],
	proxy: WTF.apiProxy('com.sonicle.webtop.contacts', 'GetContactPreview'),
	
	idProperty: 'uid',
	fields: [
		WTF.roField('uid', 'string'),
		WTF.roField('id', 'string'),
		WTF.roField('isList', 'boolean'),
		WTF.roField('displayName', 'string'),
		WTF.roField('title', 'string'),
		WTF.roField('firstName', 'string'),
		WTF.roField('lastName', 'string'),
		WTF.roField('company', 'string'),
		WTF.roField('function', 'string'),
		WTF.roField('notes', 'string'),
		WTF.roField('pic', 'boolean'),
		WTF.roField('userProfile', 'string'),
		WTF.roField('userDisplayName', 'string'),
		WTF.roField('catId', 'int'),
		WTF.roField('catName', 'string'),
		WTF.roField('catColor', 'string'),
		WTF.roField('tags', 'string'),
		WTF.roField('_orDN', 'string'), // Empty when mine!
		WTF.roField('_owPid', 'string'),
		WTF.roField('_foPerms', 'string'),
		WTF.roField('_itPerms', 'string'),
		WTF.calcField('businessInfo', 'string', ['company', 'function'], function(v, rec) {
			return Sonicle.String.join(', ', rec.get('function'), rec.get('company'));
		}),
		WTF.calcField('pictureId', 'string', ['pic'], function(v, rec) {
			return rec.get('pic') === true ? rec.get('id') : null;
		}),
		WTF.calcField('fullName', 'string', ['title', 'firstName', 'lastName'], function(v, rec, title, fn, ln) {
			return Sonicle.String.join(' ', title, fn, ln);
		}),
		WTF.calcField('avatarName', 'string', ['displayName', 'company'], function(v, rec, dn, company) {
			return Sonicle.webtop.contacts.model.GridContact.calcDisplayName(
				rec.get('isList') === true,
				rec.get('firstName'),
				rec.get('lastName'),
				dn,
				company
			);
		}),
		WTF.roField('_cfdefs', 'string')
	],
	hasMany: [
		WTF.hasMany('data1', 'Sonicle.webtop.contacts.model.ContactValueItem'), // Email addresses
		WTF.hasMany('data2', 'Sonicle.webtop.contacts.model.ContactValueItem'), // Telephones
		WTF.hasMany('data3', 'Sonicle.webtop.contacts.model.ContactValueItem'), // Other fields
		WTF.hasMany('cvalues', 'Sonicle.webtop.core.ux.data.CustomFieldValueModel')
	],
	
	hasData: function() {
		return (this.data1().getCount() > 0)
			|| (this.data2().getCount() > 0)
			|| (this.data3().getCount() > 0);
	}
});
