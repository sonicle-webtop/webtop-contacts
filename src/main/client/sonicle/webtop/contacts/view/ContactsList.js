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
Ext.define('Sonicle.webtop.contacts.view.ContactsList', {
	extend: 'WT.sdk.ModelView',
	requires: [
		'Sonicle.form.field.IconComboBox',
		'Sonicle.webtop.contacts.model.CategoryLkp',
		'Sonicle.webtop.contacts.model.ContactsList',
		'Sonicle.webtop.core.store.RcptType'
	],
	
	dockableConfig: {
		title: '{contactsList.tit}',
		iconCls: 'wtcon-icon-contacts-list-xs',
		width: 450,
		height: 450
	},
	confirm: 'yn',
	autoToolbar: false,
	modelName: 'Sonicle.webtop.contacts.model.ContactsList',
	
	initComponent: function() {
		var me = this;
		Ext.apply(me, {
			tbar: [
				me.addAction('saveClose', {
					text: WT.res('act-saveClose.lbl'),
					iconCls: 'wt-icon-saveClose-xs',
					handler: function() {
						me.saveView(true);
					}
				}),
				'-',
				me.addAction('delete', {
					text: null,
					tooltip: WT.res('act-delete.lbl'),
					iconCls: 'wt-icon-delete-xs',
					handler: function() {
						me.delectContactsList();
					}
				}),
				'->',
				WTF.localCombo('id', 'desc', {
					reference: 'fldowner',
					bind: '{record._profileId}',
					store: {
						autoLoad: true,
						model: 'WT.ux.data.SimpleModel',
						proxy: WTF.proxy(me.mys.ID, 'LookupCategoryRoots', 'roots')
					},
					fieldLabel: me.mys.res('contact.fld-owner.lbl'),
					labelWidth: 75,
					listeners: {
						select: function(s, rec) {
							me.updateCategoryFilters(true);
						}
					}
				})
			]
		});
		me.callParent(arguments);
		
		me.add({
			region: 'north',
			xtype: 'wtform',
			modelValidation: true,
			height: 60,
			items: [
			WTF.lookupCombo('categoryId', 'name', {
				xtype: 'soiconcombo',
				reference: 'fldcategory',
				bind: '{record.categoryId}',
				store: {
					autoLoad: true,
					model: 'Sonicle.webtop.contacts.model.CategoryLkp',
					proxy: WTF.proxy(me.mys.ID, 'LookupCategoryFolders', 'folders')
				},
				iconClsField: 'colorCls',
				fieldLabel: me.mys.res('contactsList.fld-category.lbl'),
				anchor: '100%'
			}), {
				xtype: 'textfield',
				reference: 'fldname',
				bind: '{record.name}',
				fieldLabel: me.mys.res('contactsList.fld-name.lbl'),
				anchor: '100%'
			}]
		});
		
		me.add({
			region: 'center',
			xtype: 'wtfieldspanel',
			layout: 'fit',
			items: [{
				xtype: 'gridpanel',
				reference: 'gprecipients',
				bind: {
					store: '{record.recipients}'
				},
				border: true,
				columns: [{
					dataIndex: 'recipientType',
					renderer: WTF.resColRenderer({
						id: WT.ID,
						key: 'store.rcptType'
					}),
					editor: Ext.create(WTF.localCombo('id', 'desc', {
						store: Ext.create('Sonicle.webtop.core.store.RcptType', {
							autoLoad: true
						})
					})),
					header: me.mys.res('contactsList.gp-recipients.recipientType.lbl'),
					width: 60
				}, {
					dataIndex: 'recipient',
					editor: {
						xtype: 'textfield'
					},
					header: me.mys.res('contactsList.gp-recipients.recipient.lbl'),
					flex: 1
				}],
				plugins: [
					Ext.create('Ext.grid.plugin.RowEditing', {
						pluginId: 'rowediting',
						clicksToMoveEditor: 2,
						saveBtnText: WT.res('act-confirm.lbl'),
						cancelBtnText: WT.res('act-cancel.lbl')
					})
				],
				tbar: [
					me.addAction('addRecipient', {
						text: WT.res('act-add.lbl'),
						tooltip: null,
						iconCls: 'wt-icon-add-xs',
						handler: function() {
							me.addRecipient();
						}
					}),
					me.addAction('deleteRecipient', {
						text: WT.res('act-delete.lbl'),
						tooltip: null,
						iconCls: 'wt-icon-delete-xs',
						handler: function() {
							var sm = me.lref('gprecipients').getSelectionModel();
							me.deleteRecipient(sm.getSelection());
						},
						disabled: true
					})
				],
				listeners: {
					selectionchange: function(s,recs) {
						me.getAction('deleteRecipient').setDisabled(!recs.length);
					}
				}
			}]
		});
		
		me.on('viewload', me.onViewLoad);
	},
	
	onViewLoad: function(s, success) {
		if(!success) return;
		var me = this,
				owner = me.lref('fldowner');
		
		me.updateCategoryFilters();
		if(me.isMode(me.MODE_NEW)) {
			owner.setDisabled(false);
		} else if(me.isMode(me.MODE_VIEW)) {
			me.getAction('saveClose').setDisabled(true);
			me.getAction('delete').setDisabled(true);
			owner.setDisabled(true);
		} else if(me.isMode(me.MODE_EDIT)) {
			owner.setDisabled(true);
		}
		
		me.lref('fldname').focus(true);
	},
	
	updateCategoryFilters: function(clear) {
		var me = this,
				fld = me.lref('fldcategory');
		if(clear) fld.setValue(null);
		fld.getStore().addFilter({
			property: '_profileId',
			value: me.lref('fldowner').getValue()
		});
	},
	
	delectContactsList: function() {
		var me = this,
				rec = me.getModel();
		
		WT.confirm(WT.res('confirm.delete'), function(bid) {
			if(bid === 'yes') {
				me.wait();
				WT.ajaxReq(me.mys.ID, 'ManageContactsLists', {
					params: {
						crud: 'delete',
						id: rec.get('id')
					},
					callback: function(success) {
						me.unwait();
						if(success) {
							me.fireEvent('viewsave', me, true, rec);
							me.closeView(false);
						}
					}
				});
			}
		}, me);
	},
	
	addRecipient: function() {
		var me = this,
				gp = me.lref('gprecipients'),
				sto = gp.getStore(),
				re = gp.getPlugin('rowediting'),
				rec;
		
		re.cancelEdit();
		rec = sto.add(Ext.create('Sonicle.webtop.contacts.model.ContactsListRecipient', {}))[0];
		re.startEdit(rec);
		return rec;
	},
	
	deleteRecipient: function(rec) {
		var me = this,
				gp = me.lref('gprecipients');
		gp.getStore().remove(rec);
	}
});
