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
Ext.define('Sonicle.webtop.contacts.model.GridContact', {
	extend: 'WTA.ux.data.EmptyModel',
	mixins: [
		'WTA.sdk.mixin.ItemWithinFolder'	
	],
	
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
		WTF.roField('email', 'string'),
		WTF.roField('telephone', 'string'),
		WTF.roField('mobile', 'string'),
		WTF.roField('pic', 'boolean'),
		WTF.roField('catId', 'int'),
		WTF.roField('catName', 'string'),
		WTF.roField('catColor', 'string'),
		WTF.roField('tags', 'string'),
		WTF.roField('_orDN', 'string'), // Empty when mine!
		WTF.roField('_owPid', 'string'),
		WTF.roField('_foPerms', 'string'),
		WTF.roField('_itPerms', 'string'),
		WTF.calcField('fullName', 'string', ['title', 'firstName', 'lastName'], function(v, rec, title, fn, ln) {
			return Sonicle.String.join(' ', title, fn, ln);
		}),
		// Name field for avatar column
		WTF.calcField('avatarName', 'string', ['displayName', 'company'], function(v, rec, dn, company) {
			return Sonicle.webtop.contacts.model.GridContact.calcDisplayName(
				rec.get('isList') === true,
				rec.get('firstName'),
				rec.get('lastName'),
				dn,
				company
			);
		}),
		// Name field for contact column when type is strictly a contact person or a list
		WTF.calcField('calcDisplayName', 'string', ['displayName'], function(v, rec, dn) {
			return Sonicle.webtop.contacts.model.GridContact.calcDisplayName(
				rec.get('isList') === true,
				rec.get('firstName'),
				rec.get('lastName'),
				dn
			);
		}),
		// Ordering field when groupBy is 'letter'...
		WTF.calcField('letter', 'string', ['firstName', 'lastName'], function(v, rec, fn, ln) {
			var name = Sonicle.webtop.contacts.model.GridContact.calcDisplayName(
				rec.get('isList') === true,
				fn,
				ln,
				rec.get('displayName')
			);
			return Sonicle.webtop.contacts.model.GridContact.calcAddressbookLetter(name);
		})
	],
	
	statics: {
		showBy: 'dn',
		
		setShowBy: function(value) {
			this.showBy = value;
		},
		
		calcDisplayName: function(isList, firstName, lastName, displayName, company) {
			var SoS = Sonicle.String,
				sb = this.showBy,
				name;
			
			if (!isList && sb === 'fnln') {
				name = SoS.join(' ', firstName, lastName);
			} else if (!isList && sb === 'lnfn') {
				name = SoS.join(' ', lastName, firstName);
			} else {
				name = displayName;
			}
			return SoS.coalesce(name, company);
		},
		
		calcAddressbookLetter: function(name) {
			if (Ext.isEmpty(name)) {
				return '*';
			} else {
				name = name.substr(0, 1);
				if (/^[\d]$/.test(name)) { // Digit
					return '#';
				} else if (/^[a-zA-Z]$/.test(name)) { // Letter
					return name.toUpperCase();
				} else {
					return '!';
				}
			}
		}
	}
});
