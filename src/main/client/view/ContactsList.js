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
	extend: 'WTA.sdk.ModelView',
	requires: [
		'Sonicle.webtop.core.ux.RecipientsGrid',
		'Sonicle.form.field.ColorComboBox',
		'Sonicle.webtop.contacts.model.CategoryLkp',
		'Sonicle.webtop.contacts.model.ContactsList',
		'Sonicle.webtop.core.store.RcptType'
	],
	
	dockableConfig: {
		title: '{contactsList.tit}',
		iconCls: 'wtcon-icon-contacts-list-xs',
		width: 650,
		height: 450
	},
	confirm: 'yn',
	autoToolbar: false,
	modelName: 'Sonicle.webtop.contacts.model.ContactsList',
	
	initComponent: function() {
		var me = this;
		Ext.apply(me, {
			tbar: [
				me.addAct('saveClose', {
					text: WT.res('act-saveClose.lbl'),
					tooltip: null,
					iconCls: 'wt-icon-saveClose-xs',
					handler: function() {
						me.saveView(true);
					}
				}),
				'-',
				me.addAct('delete', {
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
						model: 'WTA.ux.data.SimpleModel',
						proxy: WTF.proxy(me.mys.ID, 'LookupCategoryRoots', 'roots')
					},
					fieldLabel: me.mys.res('contact.fld-owner.lbl'),
					labelWidth: 75,
					listeners: {
						select: function(s, rec) {
							me.updateCategoryFilters(true);
						}
					}
				}),
				WTF.lookupCombo('categoryId', 'name', {
					xtype: 'socolorcombo',
					reference: 'fldcategory',
					bind: '{record.categoryId}',
					store: {
						autoLoad: true,
						model: me.mys.preNs('model.CategoryLkp'),
						proxy: WTF.proxy(me.mys.ID, 'LookupCategoryFolders', 'folders')
					},
					colorField: 'color'
				})
			]
		});
		me.callParent(arguments);
		
		me.add({
			region: 'north',
			xtype: 'wtfieldspanel',
			modelValidation: true,
			height: 30,
			items: [{
				xtype: 'textfield',
				reference: 'fldname',
				bind: '{record.name}',
				fieldLabel: me.mys.res('contactsList.fld-name.lbl'),
				anchor: '100%',
				listeners: {
					blur: function(s) {
						if (s.validate()) {
							var gp = me.lref('gprecipients'),
								sm = gp.getSelectionModel(),
								rec = (sm.getCount() === 0 ? 0 : gp.getStore().indexOf(sm.getSelection()[0]));
							Ext.defer(function() {
								gp.startEditAt(rec);
							}, 100);
						}						
					}
				}
			}]
		});
		
		me.add({
			xtype: 'wtrecipientsgrid',
			reference: 'gprecipients',
			region: 'center',
			tbar: [
				me.addAct('pasteList', {
					text: null,
					tooltip: me.mys.res('act-pasteList.tip'),
					iconCls: 'wtcon-icon-paste-list-xs',
					handler: function() {
						//Ext.defer(function() {
							me.pasteList();
						//},100);
					}
				})
			],
			fields: { recipientType: 'recipientType', email: 'recipient' },
			bind: {
				store: '{record.recipients}'
			}
		});
		
		me.on('viewload', me.onViewLoad);
	},
	
	onViewLoad: function(s, success) {
		if(!success) return;
		var me = this,
			rg = me.lref('gprecipients'),
			owner = me.lref('fldowner');
		
		me.updateCategoryFilters();
		if(me.isMode(me.MODE_NEW)) {
			me.getAct('saveClose').setDisabled(false);
			me.getAct('delete').setDisabled(true);
			owner.setDisabled(false);
		} else if(me.isMode(me.MODE_VIEW)) {
			me.getAct('saveClose').setDisabled(true);
			me.getAct('delete').setDisabled(true);
			owner.setDisabled(true);
		} else if(me.isMode(me.MODE_EDIT)) {
			me.getAct('saveClose').setDisabled(false);
			me.getAct('delete').setDisabled(false);
			owner.setDisabled(true);
		}
		
		if (rg.getRecipientsCount()===0)
			rg.addRecipient('to','');
		
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
						ids: WTU.arrayAsParam([rec.get('id')])
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
	
	pasteList: function() {
		var me=this;
		
		WT.prompt('',{
			title: me.mys.res("act-pasteList.tit"),
			fn: function(btn,text) {
				if (btn=='ok') {
					me.lref('gprecipients').loadValues(text);
				}
			},
			scope: me,
			width: 400,
			multiline: 200,
			value: ''
		});
	}
	
	/*addRecipient: function() {
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
	}*/
});
