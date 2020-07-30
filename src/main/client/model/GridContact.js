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
	
	idProperty: 'uid',
	fields: [
		WTF.roField('uid', 'string'),
		WTF.roField('id', 'int'),
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
		WTF.roField('_pid', 'string'),
		WTF.roField('_frights', 'string'),
		WTF.roField('_erights', 'string'),
		WTF.calcField('fullName', 'string', ['title', 'firstName', 'lastName'], function(v, rec) {
			return Sonicle.String.join(' ', rec.get('title'), rec.get('firstName'), rec.get('lastName'));
		}),
		WTF.calcField('avatarName', 'string', ['displayName'], function(v, rec) {
			return Sonicle.webtop.contacts.model.GridContact.calcDisplayName(
					rec.get('isList') === true,
					rec.get('firstName'),
					rec.get('lastName'),
					rec.get('displayName')
			);
		}),
		WTF.calcField('calcDisplayName', 'string', ['displayName'], function(v, rec) {
			return Sonicle.webtop.contacts.model.GridContact.calcDisplayName(
					rec.get('isList') === true,
					rec.get('firstName'),
					rec.get('lastName'),
					rec.get('displayName')
			);
		}),
		WTF.calcField('letter', 'string', ['firstName', 'lastName'], function(v, rec) {
			var name = Sonicle.webtop.contacts.model.GridContact.calcDisplayName(
					rec.get('isList') === true,
					rec.get('firstName'),
					rec.get('lastName'),
					rec.get('displayName')
			);
			return Sonicle.webtop.contacts.model.GridContact.calcLetter(name);
		})
	],
	
	statics: {
		showBy: 'dn',
		
		setShowBy: function(value) {
			this.showBy = value;
		},
		
		calcDisplayName: function(isList, firstName, lastName, displayName) {
			var sb = this.showBy;
			if (!isList && sb === 'fnln') {
				return Sonicle.String.join(' ', firstName, lastName);
			} else if (!isList && sb === 'lnfn') {
				return Sonicle.String.join(', ', lastName, firstName);
			} else {
				return displayName;
			}
		},
		
		calcLetter: function(name) {
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
				/*
				match = name.match(/[a-zA-Z0-9]/);
				if ((match !== null) && /^[\d]$/.test(match[0])) { // Digit
					return '#';
				} else if ((match !== null) && /^[a-zA-Z]$/.test(match[0])) { // Letter
					return match[0].toUpperCase();
				} else {
					return '!';
				}
				*/
			}
		}
	}
});
