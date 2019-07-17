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
Ext.define('Sonicle.webtop.contacts.view.ImportContacts', {
	extend: 'WTA.sdk.ImportWizardView',
	
	dockableConfig: {
		title: '{importContacts.tit}',
		iconCls: 'wtcon-icon-import',
		width: 480,
		height: 380
	},
	
	initComponent: function() {
		var me = this,
				ic = me.getInitialConfig();
		
		if(!Ext.isEmpty(ic.categoryId)) me.getVM().set('categoryId', ic.categoryId);
		me.callParent(arguments);
	},
	
	initPages: function() {
		return Ext.apply(this.callParent(), {
			vcf: ['upload','mode','end'],
			ldif:['upload','mode','end']
		});
	},
	
	initAction: function() {
		return {
			txt: 'ImportContactsFromText',
			xls: 'ImportContactsFromExcel',
			vcf: 'ImportContactsFromVCard',
			ldif:'ImportContactsFromLDIF'
		};
	},
	
	initFiles: function() {
		return Ext.apply(this.callParent(), {
			vcf:  {label: this.mys.res('importContacts.path.fld-path.vcf'), extensions: 'vcf,vcard'},
			ldif: {label: this.mys.res('importContacts.path.fld-path.ldif'), extensions: 'ldif'}
		});
	},
	
	addPathPage: function() {
		this.callParent();
		this.getVM().set('path', 'vcf');
	},
	
	createPages: function(path) {
		var me = this;
		if(path === 'vcf' || path === 'ldif') {
			me.getVM().set('importmode', 'append');
			
			return [
				me.createUploadPage(path),
				me.createModePage(path, [
					{value: 'append', label: WT.res('importwiz.mode.fld-importmode.append')},
					{value: 'copy', label: WT.res('importwiz.mode.fld-importmode.copy')}
				]),
				me.createEndPage(path)
			];
		} else {
			return me.callParent(arguments);
		}
	},
	
	onBeforeNavigate: function(s, dir, np, pp) {
		if(dir === -1) return;
		var me = this,
				ret = true,
				vm = me.getVM(),
				path = vm.get('path'),
				ppcmp = me.getPageCmp(pp);
		
		if(me.callParent(arguments) === false) return false;
		
		if(path === 'vcf' || path === 'ldif') {
			if(pp === 'upload') {
				ret = ppcmp.down('wtform').isValid();
			}
			if(!ret) return false;
		}
		return;
	},
	
	buildDoParams: function(path) {
		var vm = this.getVM();
		if(path === 'vcf' || path=== 'ldif') {
			return {
				path: path,
				uploadId: vm.get('file'),
				importMode: vm.get('importmode'),
				categoryId: vm.get('categoryId')
			};
		} else {
			return Ext.apply(this.callParent(arguments), {
				categoryId: vm.get('categoryId')
			});
		}
	}
});
