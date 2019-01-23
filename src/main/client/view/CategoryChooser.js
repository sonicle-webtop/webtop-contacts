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
Ext.define('Sonicle.webtop.contacts.view.CategoryChooser', {
	extend: 'WTA.sdk.UIView',
	requires: [
		'Sonicle.String'
	],
	uses: [
		'Sonicle.webtop.contacts.model.FolderNode'
	],
	
	dockableConfig: {
		width: 400,
		height: 450,
		modal: true,
		minimizable: false,
		maximizable: false
	},
	promptConfirm: false,
	writableOnly: false,
	
	viewModel: {
		data: {
			result: 'cancel',
			profileId: null,
			categoryId: null
		}
	},
	defaultButton: 'btnok',
	
	constructor: function(cfg) {
		var me = this;
		me.callParent([cfg]);
		
		WTU.applyFormulas(me.getVM(), {
			isValid: WTF.foGetFn(null, 'categoryId', function(v) {
				return v !== null;
			})
		});
	},
	
	initComponent: function() {
		var me = this;
		
		Ext.apply(me, {
			buttons: [{
				reference: 'btnok',
				bind: {
					disabled: '{!isValid}'
				},
				text: WT.res('act-ok.lbl'),
				handler: function() {
					me.okView();
				}
			}, {
				text: WT.res('act-cancel.lbl'),
				handler: function() {
					me.closeView(false);
				}
			}]
		});
		me.callParent(arguments);
		
		me.add({
			region: 'center',
			xtype: 'treepanel',
			border: false,
			useArrows: true,
			rootVisible: false,
			store: {
				autoLoad: true,
				model: 'Sonicle.webtop.contacts.model.FolderNode',
				proxy: WTF.apiProxy(me.mys.ID, 'ManageFoldersTree', 'children', {
					extraParams: {
						crud: 'read',
						chooser: true,
						writableOnly: me.writableOnly
					}
				}),
				root: { id: 'root', expanded: true }
			},
			hideHeaders: true,
			listeners: {
				selectionchange: function(s, sel) {
					var me = this,
							rec = sel[0];
					if (rec) {
						me.getVM().set({
							categoryId: rec.get('_catId'),
							profileId: rec.get('_pid')
						});
					}
				},
				scope: me
			}
		});
	},
	
	okView: function() {
		var me = this,
				vm = me.getVM();
		vm.set('result', 'ok');
		me.fireEvent('viewok', me, vm.get('categoryId'), vm.get('profileId'));
		me.closeView(false);
	}
});
