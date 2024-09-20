/* 
 * Copyright (C) 2024 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2024 Sonicle S.r.l.".
 */
Ext.define('Sonicle.webtop.contacts.view.ContactsList', {
	extend: 'WTA.sdk.ModelView',
	requires: [
		'Sonicle.Data',
		'Sonicle.String',
		'Sonicle.VMUtils',
		'Sonicle.form.FieldSection',
		'Sonicle.form.FieldHGroup',
		'Sonicle.plugin.FieldTabOut',
		'Sonicle.webtop.core.ux.grid.Recipients',
		'Sonicle.webtop.contacts.model.CategoryLkp',
		'Sonicle.webtop.contacts.model.ContactsList',
		'Sonicle.webtop.core.store.RcptType'
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
	actionsResPrefix: 'contactsList',
	confirm: 'yn',
	modelName: 'Sonicle.webtop.contacts.model.ContactsList',
	
	constructor: function(cfg) {
		var me = this;
		me.callParent([cfg]);
		
		Sonicle.VMUtils.applyFormulas(me.getVM(), {
			isView: WTF.foIsEqual(null, '_mode', 'view'),
			foTags: WTF.foTwoWay('record', 'tags', function(v) {
					return Sonicle.String.split(v, '|');
				}, function(v) {
					return Sonicle.String.join('|', v);
			}),
			foHasTags: WTF.foIsEmpty('record', 'tags', true)
		});
	},
	
	initComponent: function() {
		var me = this;
		me.callParent(arguments);
		
		me.add({
			region: 'center',
			xtype: 'wtfieldspanel',
			paddingBottom: true,
			paddingSides: true,
			layout: 'vbox',
			defaults: {
				width: '100%'
			},
			items: [
				{
					xtype: 'wtfieldspanel',
					modelValidation: true,
					defaults: {
						labelAlign: 'top',
						labelSeparator: ''
					},
					items: me.prepareMainFields()
				}, {
					xtype: 'wtrecipientsgridnew',
					reference: 'gprecipients',
					sid: me.mys.ID,
					border: true,
					hideHeaders: true,
					bind: {
						store: '{record.recipients}'
						// TODO: uncomment when setReadOnly is supported in WTA.ux.grid.Recipients
						//readOnly: '{isView}'
					},
					fields: { recipientType: 'recipientType', email: 'recipient' },
					viewConfig: {
						deferEmptyText: false,
						emptyText: WT.res('grid.emp')
					},
					automaticRecipientAtEnd: true,
					showRecipientLink: true,
					recipientLinkField: 'recipientContactId',
					recipientValueHdText: me.res('contactsList.gp-recipients.recipient.lbl'),
					recipientValueEmptyText: me.res('contactsList.gp-recipients.recipient.emp'),
					recipientLinkIconTooltip: me.res('contactsList.gp-recipients.link.tip'),
					actionItems: [
						{
							iconCls: 'wt-icon-trash',
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
					flex: 1
				}
			]
		});
		
		me.on('viewload', me.onViewLoad);
	},
	
	initTBar: function() {
		var me = this,
			SoU = Sonicle.Utils;
		
		me.dockedItems = SoU.mergeDockedItems(me.dockedItems, 'top', [
			me.createTopToolbar1Cfg(me.prepareTopToolbarItems())
		]);
		me.dockedItems = SoU.mergeDockedItems(me.dockedItems, 'bottom', [
			me.createStatusbarCfg()
		]);
	},
	
	deleteContactsListUI: function() {
		var me = this,
			mo = me.getModel();
		
		WT.confirmDelete(me.res('contactsList.confirm.delete', Ext.String.ellipsis(mo.get('name'), 50)), function(bid) {
			if (bid === 'ok') {
				me.wait();
				me.mys.deleteContactsLists(mo.get('id'), {
					callback: function(success) {
						me.unwait();
						if (success) {
							me.fireEvent('viewsave', me, true, mo);
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
			title: me.res("act-pasteList.tit"),
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
		prepareTopToolbarItems: function() {
			var me = this;
			return [
				WTF.lookupCombo('categoryId', '_label', {
					xtype: 'socombo',
					reference: 'fldcategory',
					bind: {
						value: '{record.categoryId}',
						readOnly: '{foIsView}'
					},
					listConfig: {
						displayField: 'name'
					},
					swatchGeometry: 'circle',
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
					width: 300
				}),
				me.addAct('tags', {
					text: null,
					tooltip: me.res('act-manageTags.lbl'),
					iconCls: 'wt-icon-tags',
					handler: function() {
						me.manageTagsUI(Sonicle.String.split(me.getModel().get('tags'), '|'));
					}
				}),
				me.addAct('delete', {
					text: null,
					tooltip: WT.res('act-delete.lbl'),
					iconCls: 'wt-icon-delete',
					hidden: true,
					handler: function() {
						me.deleteContactsListUI();
					}
				})
			];
		},
		
		prepareMainFields: function() {
			var me = this;
			return [
				me.createTagsFieldCfg(),
				{
					xtype: 'textfield',
					reference: 'fldname',
					bind: '{record.name}',
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
					},
					fieldLabel: me.res('contactsList.fld-name.lbl'),
					emptyText: me.res('contactsList.fld-name.emp'),
					anchor: '100%'
				}, {
					xtype: 'sofieldhgroup',
					items: [
						{
							xtype: 'button',
							bind: {
								disabled: '{isView}'
							},
							ui: '{secondary|toolbar}',
							text: me.res('contactsList.act-addRecipient.lbl'),
							iconCls: 'wt-icon-add',
							handler: function() {
								var gp = me.lref('gprecipients'),
									sto = gp.getStore(),
									rec;
								if (sto.getCount() === 0 || sto.getAt(sto.getCount()-1).get('recipient')) {
									rec = gp.addRecipient(null);
									if (rec) gp.startEdit(rec);
								}
							}
						}, {
							xtype: 'sohspacer'
						}, {
							xtype: 'button',
							bind: {
								disabled: '{isView}'
							},
							ui: '{tertiary|toolbar}',
							text: me.res('contactsList.act-pasteList.lbl'),
							iconCls: 'wt-icon-clipboard-paste',
							handler: function() {
								//Ext.defer(function() {
									me.pasteList();
								//}, 100);
							}
						}
					]
				}
			];
		},
		
		createTagsFieldCfg: function(cfg) {
			return Ext.apply({
			xtype: 'sotagdisplayfield',
				bind: {
					value: '{foTags}',
					hidden: '{!foHasTags}'
				},
				valueField: 'id',
				displayField: 'name',
				colorField: 'color',
				store: WT.getTagsStore(),
				dummyIcon: 'loading',
				hidden: true,
				hideLabel: true
			}, cfg);
		},
		
		createStatusbarCfg: function() {
			var me = this;
			return me.mys.hasAuditUI() ? {
				xtype: 'statusbar',
				items: [
					me.addAct('contactsListAuditLog', {
						text: null,
						tooltip: WT.res('act-auditLog.lbl'),
						iconCls: 'fas fa-history',
						handler: function() {
							me.mys.openAuditUI(me.getModel().getId(), 'CONTACT');
						},
						scope: me
					})
				]
			} : null;
		},
		
		onViewLoad: function(s, success) {
			if (!success) return;
			var me = this;

			if (me.isMode(me.MODE_NEW)) {
				me.getAct('saveClose').setDisabled(false);
				me.getAct('delete').setHidden(true);
				me.getAct('tags').setHidden(false);
				me.lref('fldcategory').setReadOnly(false);
				if (me.mys.hasAuditUI()) me.getAct('contactsListAuditLog').setDisabled(true);
			} else if (me.isMode(me.MODE_VIEW)) {
				me.getAct('saveClose').setDisabled(true);
				me.getAct('delete').setHidden(true);
				me.getAct('tags').setHidden(true);
				me.lref('fldcategory').setReadOnly(true);
				if (me.mys.hasAuditUI()) me.getAct('contactsListAuditLog').setDisabled(false);
			} else if (me.isMode(me.MODE_EDIT)) {
				me.getAct('saveClose').setDisabled(false);
				me.getAct('delete').setHidden(false);
				me.getAct('tags').setHidden(false);
				me.lref('fldcategory').setReadOnly(false);
				if (me.mys.hasAuditUI()) me.getAct('contactsListAuditLog').setDisabled(false);
			}
			me.lref('fldname').focus(true);
		}
	}
});
