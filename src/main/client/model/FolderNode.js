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
Ext.define('Sonicle.webtop.contacts.model.FolderNode', {
	extend: 'Ext.data.Model',
	
	fields: [
		WTF.field('_type', 'string', false),
		WTF.field('_pid', 'string', false),
		WTF.roField('_rperms', 'string'),
		WTF.roField('_fperms', 'string'),
		WTF.roField('_eperms', 'string'),
		WTF.roField('_catId', 'string'),
		WTF.roField('_builtIn', 'boolean'),
		WTF.roField('_provider', 'string'),
		WTF.roField('_color', 'string'),
		WTF.roField('_sync', 'string'),
		WTF.roField('_default', 'boolean'),
		WTF.field('_active', 'boolean', true), // Same as checked
		WTF.calcField('_domainId', 'string', '_pid', function(v, rec) {
			return (rec.get('_pid')) ? rec.get('_pid').split('@')[1] : null;
		}),
		WTF.calcField('_userId', 'string', '_pid', function(v, rec) {
			return (rec.get('_pid')) ? rec.get('_pid').split('@')[0] : null;
		})
	],
	
	isFolderRoot: function() {
		return this.get('_type') === 'root';
	},
	
	isFolder: function() {
		return this.get('_type') === 'folder';
	},
	
	hasProfile: function(profileId) {
		return this.get('_pid') === profileId;
	},
	
	refreshActive: function() {
		this.set('_active', this.get('checked') === true);
	},
	
	setActive: function(active) {
		var me = this;
		me.beginEdit();
		me.set('checked', active);
		me.set('_active', active);
		me.endEdit();
	},
	
	isActive: function() {
		return this.get('_active') === true;
	},
	
	getFolderNode: function() {
		return this.isFolder() ? this : null;
	},
	
	getFolderRootNode: function() {
		return this.isFolderRoot() ? this : this.parentNode;
	},
	
	isPersonalNode: function() {
		return this.self.isNodePersonal(this.getId());
	},
	
	statics: {
		isNodePersonal: function(nodeId) {
			return (nodeId === '0') || Ext.String.startsWith(nodeId, '0|');
		}
	}
});
