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
Ext.define('Sonicle.webtop.contacts.model.Category', {
	extend: 'WTA.ux.data.BaseModel',
	proxy: WTF.apiProxy('com.sonicle.webtop.contacts', 'ManageCategory'),
	
	identifier: 'negative',
	idProperty: 'categoryId',
	fields: [
		WTF.field('categoryId', 'int', false),
		WTF.field('domainId', 'string', false),
		WTF.field('userId', 'string', false),
		WTF.field('provider', 'string', false, {defaultValue: 'local'}),
		WTF.field('name', 'string', false),
		WTF.field('description', 'string', true),
		WTF.field('color', 'string', false, {defaultValue: '#F3F4F6'}),
		WTF.field('sync', 'string', false, {defaultValue: 'O'}),
		WTF.field('remoteUrl', 'string', true, {
			validators: [{
				type: 'sopresence',
				ifField: 'provider',
				ifValues: ['carddav']
			}]
		}),
		WTF.field('remoteUsername', 'string', true),
		WTF.field('remotePassword', 'string', true),
		WTF.field('remoteSyncFrequency', 'int', true),
		WTF.roField('remoteLastSync', 'date', {dateFormat: 'Y-m-d H:i:s'}),
		WTF.calcField('_profileId', 'string', ['domainId', 'userId'], function(v, rec) {
			return rec.get('userId') + '@' + rec.get('domainId');
		})
	]
});
