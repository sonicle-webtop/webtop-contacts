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
		'Sonicle.form.field.ComboBox',
		'Sonicle.form.field.TagDisplay',
		'Sonicle.webtop.core.ux.grid.Recipients',
		'Sonicle.webtop.contacts.model.CategoryLkp',
		'Sonicle.webtop.contacts.model.ContactsList',
		'Sonicle.webtop.core.store.RcptType',
		'Sonicle.plugin.FieldTabOut'
	],
	uses: [
		'Sonicle.webtop.core.view.Tags'
	],
	
	dockableConfig: {
		title: '{contactsList.tit}',
		iconCls: 'wtcon-icon-contactsList',
		width: 650,
		height: 450
	},
	confirm: 'yn',
	autoToolbar: false,
	modelName: 'Sonicle.webtop.contacts.model.ContactsList',
	actionsResPrefix: 'contactsList',
	
	constructor: function(cfg) {
		var me = this;
		me.callParent([cfg]);
		
		WTU.applyFormulas(me.getVM(), {
			foHasTags: WTF.foIsEmpty('record', 'tags', true),
			isView: WTF.foIsEqual(null, '_mode', 'view')
		});
	},
	
	initComponent: function() {
		var me = this;
		Ext.apply(me, {
			dockedItems: [
				{
					xtype: 'toolbar',
					items: [
						me.addAct('saveClose', {
							text: WT.res('act-saveClose.lbl'),
							tooltip: null,
							iconCls: 'wt-icon-saveClose',
							handler: function() {
								me.saveView(true);
							}
						}),
						'-',
						me.addAct('delete', {
							text: null,
							iconCls: 'wt-icon-delete',
							handler: function() {
								me.delectContactsList();
							}
						}),
						me.addAct('tags', {
							text: null,
							tooltip: me.mys.res('act-manageTags.lbl'),
							iconCls: 'wt-icon-tag',
							handler: function() {
								me.manageTagsUI(Sonicle.String.split(me.getModel().get('tags'), '|'));
							}
						}),
						'->',
						WTF.lookupCombo('categoryId', '_label', {
							xtype: 'socombo',
							reference: 'fldcategory',
							bind: '{record.categoryId}',
							listConfig: {
								displayField: 'name',
								groupCls: 'wt-theme-text-lighter2'
							},
							autoLoadOnValue: true,
							store: {
								model: me.mys.preNs('model.CategoryLkp'),
								proxy: WTF.proxy(me.mys.ID, 'LookupCategoryFolders', 'folders'),
								grouper: {
									property: '_profileId',
									sortProperty: '_order'
								},
								filters: [{
									filterFn: function(rec) {
										var mo = me.getModel();
										if (mo && me.isMode(me.MODE_NEW)) {
											return rec.get('_writable');
										} else if (mo && me.isMode(me.MODE_VIEW)) {
											if (rec.getId() === mo.get('categoryId')) return true;
										} else if (mo && me.isMode(me.MODE_EDIT)) {
											if (rec.getId() === mo.get('categoryId')) return true;
											if (rec.get('_profileId') === mo.get('_profileId') && rec.get('_writable')) return true;
										}
										return false;
									}
								}]
							},
							groupField: '_profileDescription',
							colorField: 'color',
							fieldLabel: me.mys.res('contact.fld-category.lbl'),
							labelAlign: 'right',
							width: 400
						})
					]
				}, {
					xtype: 'sotagdisplayfield',
					dock : 'top',
					bind: {
						value: '{record.tags}',
						hidden: '{!foHasTags}'
					},
					delimiter: '|',
					valueField: 'id',
					displayField: 'name',
					colorField: 'color',
					store: WT.getTagsStore(),
					dummyIcon: 'loading',
					hidden: true,
					hideLabel: true,
					margin: '0 0 5 0'
				}
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
				enableKeyEvents: true,
				plugins: [
					{
						ptype: 'sofieldtabout',
						rootHierarchyContainer: me,
						nextReference: 'gprecipients',
						focusSelectText: true
					}
				],
				listeners: {
					beforetaboutfocus: function() {
						var gp = me.lref('gprecipients'),
								sto = gp.getStore();
						
						if (sto.getCount() === 0) gp.addRecipient(null);
					}
				}
			}]
		});
		
		me.add({
			region: 'center',
			xtype: 'wtfieldspanel',
			layout: 'fit',
			items: [
				{
					xtype: 'wtrecipientsgridnew',
					reference: 'gprecipients',
					sid: me.mys.ID,
					border: true,
					hideHeaders: false,
					bind: {
						store: '{record.recipients}'
						// TODO: uncomment when setReadOnly is supported in WTA.ux.grid.Recipients
						//readOnly: '{isView}'
					},
					fields: { recipientType: 'recipientType', email: 'recipient' },
					automaticRecipientAtEnd: true,
					showRecipientLink: true,
					recipientLinkField: 'recipientContactId',
					recipientValueHdText: me.res('contactsList.gp-recipients.recipient.lbl'),
					actionItems: [
						{
							iconCls: 'far fa-trash-alt',
							tooltip: WT.res('act-remove.lbl'),
							isActionDisabled: function() {
								return !!me.getVM().get('isView');
							},
							handler: function(g, ridx) {
								var sto = g.getStore();
								sto.removeAt(ridx);
							}	
						}
					],
					tbar: [
						{
							xtype: 'button',
							bind: {
								disabled: '{isView}'
							},
							text: WT.res('act-add.lbl'),
							iconCls: 'wtcon-icon-addListRecipient',
							handler: function() {
								var gp = me.lref('gprecipients'),
									sto = gp.getStore(),
									rec;
								if (sto.getCount() === 0 || sto.getAt(sto.getCount()-1).get('recipient')) {
									rec = gp.addRecipient(null);
									if (rec) gp.startEdit(rec);
								}
							}
						},
						'->',
						{
							xtype: 'button',
							bind: {
								disabled: '{isView}'
							},
							tooltip: me.res('contactsList.act-pasteList.tip'),
							iconCls: 'wt-icon-clipboard-paste',
							handler: function() {
								//Ext.defer(function() {
									me.pasteList();
								//}, 100);
							}
						}
					]
				}
			]
		});
		
		me.on('viewload', me.onViewLoad);
	},
	
	delectContactsList: function() {
		var me = this,
				rec = me.getModel();
		
		WT.confirm(me.res('contactsList.confirm.delete', Ext.String.ellipsis(rec.get('name'), 40)), function(bid) {
			if (bid === 'yes') {
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
		var me = this;
		
		WT.prompt('',{
			title: me.mys.res("act-pasteList.tit"),
			fn: function(btn, text) {
				if (btn === 'ok') {
					var gp = me.lref('gprecipients'),
						sel = gp.getSelectionModel().getSelectionStart();
					me.lref('gprecipients').pasteRecipients(text, sel);
				}
			},
			scope: me,
			width: 400,
			multiline: 200,
			value: ''
		});
	},
	
	manageTagsUI: function(selTagIds) {
		var me = this,
			vw = WT.createView(WT.ID, 'view.Tags', {
				swapReturn: true,
				viewCfg: {
					data: {
						selection: selTagIds
					}
				}
			});
		vw.on('viewok', function(s, data) {
			me.getModel().set('tags', Sonicle.String.join('|', data.selection));
		});
		vw.showView();
	},
	
	privates: {
		onViewLoad: function(s, success) {
			if (!success) return;
			var me = this;

			if (me.isMode(me.MODE_NEW)) {
				me.getAct('saveClose').setDisabled(false);
				me.getAct('delete').setDisabled(true);
				//me.getAct('addListRecipient').setDisabled(false);
				me.lref('fldcategory').setReadOnly(false);
			} else if (me.isMode(me.MODE_VIEW)) {
				me.getAct('saveClose').setDisabled(true);
				me.getAct('delete').setDisabled(true);
				//me.getAct('addListRecipient').setDisabled(true);
				me.lref('fldcategory').setReadOnly(true);
			} else if (me.isMode(me.MODE_EDIT)) {
				me.getAct('saveClose').setDisabled(false);
				me.getAct('delete').setDisabled(false);
				//me.getAct('addListRecipient').setDisabled(false);
				me.lref('fldcategory').setReadOnly(false);
			}
			me.lref('fldname').focus(true);
		}
	}
});
