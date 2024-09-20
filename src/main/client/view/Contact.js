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
Ext.define('Sonicle.webtop.contacts.view.Contact', {
	extend: 'WTA.sdk.ModelView',
	requires: [
		'Sonicle.Data',
		'Sonicle.String',
		'Sonicle.VMUtils',
		'Sonicle.form.HSpacer',
		'Sonicle.form.FieldSection',
		'Sonicle.form.FieldHGroup',
		'Sonicle.form.field.ComboBox',
		'Sonicle.form.field.TagDisplay',
		'Sonicle.plugin.DropMask',
		'WTA.util.CustomFields',
		'WTA.ux.UploadButton',
		'WTA.ux.field.Attachments',
		'WTA.ux.panel.CustomFieldsEditor',
		'WTA.model.SubjectLkp',
		'Sonicle.webtop.contacts.model.CategoryLkp',
		'Sonicle.webtop.contacts.model.Contact'
	],
	uses: [
		'Sonicle.ClipboardMgr',
		'Sonicle.URLMgr',
		'Sonicle.webtop.core.view.Tags'
	],
	
	dockableConfig: {
		title: '{contact.tit}',
		iconCls: 'wtcon-icon-contact',
		width: 650,
		height: 550
	},
	actionsResPrefix: 'contact',
	confirm: 'yn',
	fieldTitle: 'title',
	modelName: 'Sonicle.webtop.contacts.model.Contact',
	
	viewModel: {
		data: {
			addButtonsMinWidth: 0,
			largestAddButtonWidth: 0,
			hidden: {
				fldtitle: true,
				fldnickname: true,
				fldgender: true,
				fldfunction: true,
				flddepartment: true,
				fldmanager: true,
				fldassistant: true,
				fldassistanttelephone: true,
				fldemail2: true,
				fldemail3: true,
				fldworktelephone1: true,
				fldworkfax: true,
				fldhometelephone1: true,
				fldhometelephone2: true,
				fldhomefax: true,
				fldpager1: true,
				fldpager2: true,
				fldinstantmsg1: true,
				fldinstantmsg2: true,
				fldinstantmsg3: true,
				fldurl: true,
				fldpartner: true,
				fldbirthday: true,
				fldanniversary: true,
				fldworkaddress: true,
				fldhomeaddress: true,
				fldotheraddress: true
			}
		}
	},
	
	constructor: function(cfg) {
		var me = this;
		me.callParent([cfg]);
		
		Sonicle.VMUtils.applyFormulas(me.getVM(), {
			foIsView: WTF.foIsEqual('_mode', null, me.MODE_VIEW),
			foTags: WTF.foTwoWay('record', 'tags', function(v) {
					return Sonicle.String.split(v, '|');
				}, function(v) {
					return Sonicle.String.join('|', v);
			}),
			foHasTags: WTF.foIsEmpty('record', 'tags', true),
			foHasAttachments: WTF.foAssociationIsEmpty('record', 'attachments', true),
			foDisableAddNameField: WTF.foMultiGetFn('hidden', ['fldtitle', 'fldnickname'], function(v) {
				var disable = true;
				Ext.iterate(v, function(name, value) { disable = disable && !value; });
				return disable;
			}),
			foDisableAddCompanyField: WTF.foMultiGetFn('hidden', ['fldfunction', 'flddepartment', 'fldmanager', 'fldassistant', 'fldassistanttelephone'], function(v) {
				var disable = true;
				Ext.iterate(v, function(name, value) { disable = disable && !value; });
				return disable;
			}),
			foDisableAddEmailField: WTF.foMultiGetFn('hidden', ['fldemail2', 'fldemail3'], function(v) {
				var disable = true;
				Ext.iterate(v, function(name, value) { disable = disable && !value; });
				return disable;
			}),
			foDisableAddTelephoneField: WTF.foMultiGetFn('hidden', ['fldworktelephone2', 'fldworkfax', 'fldhometelephone1', 'fldhometelephone2', 'fldhomefax'], function(v) {
				var disable = true;
				Ext.iterate(v, function(name, value) { disable = disable && !value; });
				return disable;
			}),
			foDisableAddOtherField: WTF.foMultiGetFn('hidden', ['fldurl', 'fldpartner', 'fldbirthday', 'fldanniversary', 'fldworkaddress', 'fldhomeaddress', 'fldotheraddress'], function(v) {
				var disable = true;
				Ext.iterate(v, function(name, value) { disable = disable && !value; });
				return disable;
			})
		});
	},
	
	
	initComponent: function() {
		var me = this,
			vm = me.getViewModel();
		
		me.plugins = Sonicle.Utils.mergePlugins(me.plugins, [
			{
				ptype: 'sodropmask',
				text: WT.res('sofiledrop.text'),
				monitorExtDrag: false,
				shouldSkipMasking: function(dragOp) {
					return !Sonicle.plugin.DropMask.isBrowserFileDrag(dragOp);
				}
			}
		]);
		me.callParent(arguments);
		
		me.addRef('cxmAttachment', Ext.create({
			xtype: 'menu',
			items: [
				{
					iconCls: 'wt-icon-open',
					text: WT.res('act-open.lbl'),
					handler: function(s, e) {
						var rec = e.menuData.rec;
						if (rec) me.openAttachmentUI(rec, false);
					}
				}, {
					iconCls: 'wt-icon-download',
					text: WT.res('act-download.lbl'),
					handler: function(s, e) {
						var rec = e.menuData.rec;
						if (rec) me.openAttachmentUI(rec, true);
					}
				}
			]
		}));
		
		me.add({
			region: 'center',
			xtype: 'wttabpanel',
			reference: 'tpnlmain',
			activeTab: 0,
			deferredRender: false,
			tabBar: {hidden: true},
			items: [
				{
					xtype: 'wtfieldspanel',
					title: me.res('contact.main.tit'),
					paddingTop: true,
					paddingSides: true,
					scrollable: true,
					modelValidation: true,
					items: me.prepareMainFields()
				}, {
					xtype: 'wtcfieldseditorpanel',
					reference: 'tabcfields',
					title: me.res('contact.cfields.tit'),
					bind: {
						store: '{record.cvalues}',
						fieldsDefs: '{record._cfdefs}'
					},
					serviceId: me.mys.ID,
					mainView: me,
					defaultLabelWidth: 120,
					listeners: {
						prioritize: function(s) {
							me.lref('tpnlmain').setActiveItem(s);
						}
					}
				}
			]
		});
		
		me.on('viewload', me.onViewLoad);
		me.on('viewclose', me.onViewClose);
		me.on('beforemodelsave', me.onBeforeModelSave, me);
		vm.bind('{foTags}', me.onTagsChanged, me);
		vm.bind('{largestAddButtonWidth}', function(nv, ov) {
			if (Ext.isNumber(nv) && nv > 0) {
				me.getVM().set('addButtonsMinWidth', nv);
			}
		}, me);
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
	
	deleteContactUI: function() {
		var me = this,
			mo = me.getModel();
		
		WT.confirmDelete(me.res('contact.confirm.delete', Ext.String.ellipsis(mo.get('displayName'), 50), {htmlEncode: true}), function(bid) {
			if (bid === 'ok') {
				me.wait();
				me.mys.deleteContacts(mo.get('id'), {
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
	
	printContactUI: function() {
		var me = this,
			mo = me.getModel();
		if (mo.isDirty()) {
			WT.warn(WT.res('warn.print.notsaved'));
		} else {
			me.mys.printContactsDetail([mo.getId()]);
		}
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
	
	openAttachmentUI: function(rec, download) {
		var me = this,
			name = rec.get('name'),
			uploadId = rec.get('_uplId'),
			url;
		
		if (!Ext.isEmpty(uploadId)) {
			url = WTF.processBinUrl(me.mys.ID, 'DownloadContactAttachment', {
				inline: !download,
				uploadId: uploadId
			});
		} else {
			url = WTF.processBinUrl(me.mys.ID, 'DownloadContactAttachment', {
				inline: !download,
				contactId: me.getModel().getId(),
				attachmentId: rec.get('id')
			});
		}
		if (download) {
			Sonicle.URLMgr.downloadFile(url, {filename: name});
		} else {
			Sonicle.URLMgr.openFile(url, {filename: name});
		}
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
				{
					xtype: 'wtuploadbutton',
					bind: {
						hidden: '{foIsView}',
						disabled: '{foIsView}'
					},
					tooltip: WT.res('act-attach.lbl'),
					iconCls: 'wt-icon-attach',
					sid: me.mys.ID,
					uploadContext: 'ContactAttachment',
					uploadTag: WT.uiid(me.getId()),
					dropElement: me.getId(),
					listeners: {
						beforeupload: function(up, file) {
							me.wait(file.name, true);
						},
						uploaderror: function(up, file, cause, json) {
							me.unwait();
							WTA.mixin.HasUpload.handleUploadError(up, file, cause);
						},
						uploadprogress: function(up, file) {
							me.wait(Ext.String.format('{0}: {1}%', file.name, file.percent), true);
						},
						fileuploaded: function(up, file, resp) {
							me.unwait();
							var sto = me.getModel().attachments();
							sto.add(sto.createModel({
								name: file.name,
								size: file.size,
								_uplId: resp.data.uploadId
							}));
						}
					}
				},
				me.addAct('print', {
					text: null,
					tooltip: WT.res('act-print.lbl'),
					iconCls: 'wt-icon-print',
					handler: function() {
						//TODO: aggiungere l'azione 'salva' permettendo così la stampa senza chiudere la form
						me.printContactUI();
					}
				}),
				me.addAct('delete', {
					text: null,
					tooltip: WT.res('act-delete.lbl'),
					iconCls: 'wt-icon-delete',
					hidden: true,
					handler: function() {
						me.deleteContactUI();
					}
				})
			];
		},
		
		prepareMainFields: function() {
			var me = this;
			return [
				me.createTagsFieldCfg(),
				{
					xtype: 'sofieldsection',
					items: [
						{
							xtype: 'sofieldhgroup',
							layout: {
								align: 'middle'
							},
							items: Ext.Array.push([
								me.createPicFieldCfg({
									reference: 'fldpic',
									bind: '{record.picture}'
								})
							], me.createPicSectionMoreItemsCfg())
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactName',
					items: [
						{
							xtype: 'sofieldhgroup',
							items: [
								{
									xtype: 'textfield',
									reference: 'fldtitle',
									bind: {
										value: '{record.title}',
										hidden: '{hidden.fldtitle}'
									},
									hidden: true,
									fieldLabel: me.res('contact.fld-title.lbl'),
									width: 80
								}, {
									xtype: 'sohspacer',
									bind: {
										hidden: '{hidden.fldtitle}'
									},
									hidden: true
								}, {
									xtype: 'textfield',
									bind: '{record.firstName}',
									fieldLabel: me.res('contact.fld-firstName.lbl'),
									flex: 1
								}, {
									xtype: 'sohspacer'
								}, {
									xtype: 'textfield',
									bind: '{record.lastName}',
									fieldLabel: me.res('contact.fld-lastName.lbl'),
									flex: 1
								}, {
									xtype: 'sohspacer'
								}, 
								me.createAddFieldButtonCfg({
									bind: {
										disabled: '{foDisableAddNameField}'
									},
									text: me.res('contact.btn-addNameField.lbl'),
									menu: {
										items: [
											{
												itemId: 'title',
												text: me.res('contact.fld-title.lbl'),
												handler: function() {
													me.showField('fldtitle', true);
												}
											}, {
												itemId: 'nickname',
												text: me.res('contact.fld-nickname.lbl'),
												handler: function() {
													me.showField('fldnickname', true);
												}
											}
										],
										listeners: {
											beforeshow: function(s) {
												s.getComponent('title').setHidden(me.isFieldHidden('fldtitle'));
												s.getComponent('nickname').setHidden(me.isFieldHidden('fldnickname'));
											}
										}
									}
								}, true)
							]
						}, {
							xtype: 'sofieldhgroup',
							items: [
								{
									xtype: 'textfield',
									bind: {
										value: '{record.displayName}',
										emptyText: '{record.autoDisplayName}'
									},
									fieldLabel: me.res('contact.fld-displayName.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldnickname}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldnickname',
									bind: {
										value: '{record.nickname}'
									},
									fieldLabel: me.res('contact.fld-nickname.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactCompany',
					items: [
						{
							xtype: 'sofieldhgroup',
							items: [
								WTF.remoteCombo('id', 'desc', {
									bind: '{record.company}',
									forceSelection: false,
									autoLoadOnValue: true,
									store: {
										autoLoad: true,
										model: 'WTA.ux.data.SimpleModel',
										proxy: WTF.proxy(WT.ID, 'LookupCustomersSuppliers')
									},
									triggers: {
										clear: WTF.clearTrigger()
									},
									fieldLabel: me.res('contact.fld-company.lbl'),
									flex: 1
								}),
								{
									xtype: 'sohspacer'
								}, 
								me.createAddFieldButtonCfg({
									bind: {
										disabled: '{foDisableAddCompanyField}'
									},
									text: me.res('contact.btn-addCompanyField.lbl'),
									menu: {
										items: [
											{
												itemId: 'function',
												text: me.res('contact.fld-function.lbl'),
												handler: function() {
													me.showField('fldfunction', true);
												}
											}, {
												itemId: 'department',
												text: me.res('contact.fld-department.lbl'),
												handler: function() {
													me.showField('flddepartment', true);
												}
											}, {
												itemId: 'manager',
												text: me.res('contact.fld-manager.lbl'),
												handler: function() {
													me.showField('fldmanager', true);
												}
											}, {
												itemId: 'assistant',
												text: me.res('contact.fld-assistant.lbl'),
												handler: function() {
													me.showField('fldassistant', true);
												}
											}, {
												itemId: 'assistantTelephone',
												text: me.res('contact.fld-assistantTelephone.lbl'),
												handler: function() {
													me.showField('fldassistanttelephone', true);
												}
											}
										],
										listeners: {
											beforeshow: function(s) {
												s.getComponent('function').setHidden(me.isFieldHidden('fldfunction'));
												s.getComponent('department').setHidden(me.isFieldHidden('flddepartment'));
												s.getComponent('manager').setHidden(me.isFieldHidden('fldmanager'));
												s.getComponent('assistant').setHidden(me.isFieldHidden('fldassistant'));
												s.getComponent('assistantTelephone').setHidden(me.isFieldHidden('fldassistanttelephone'));
											}
										}
									}
								}, true)
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldfunction}'
							},
							items: [
								{
									xtype: 'wtsuggestcombo',
									reference: 'fldfunction',
									bind: {
										value: '{record.function}'
									},
									sid: me.mys.ID,
									suggestionContext: 'contactFunction',
									fieldLabel: me.res('contact.fld-function.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.flddepartment}'
							},
							items: [
								{
									xtype: 'wtsuggestcombo',
									reference: 'flddepartment',
									bind: {
										value: '{record.department}'
									},
									sid: me.mys.ID,
									suggestionContext: 'contactDepartment',
									fieldLabel: me.res('contact.fld-department.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldmanager}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldmanager',
									bind: {
										value: '{record.manager}'
									},
									fieldLabel: me.res('contact.fld-manager.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldassistant}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldassistant',
									bind: {
										value: '{record.assistant}'
									},
									fieldLabel: me.res('contact.fld-assistant.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldassistanttelephone}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldassistanttelephone',
									bind: {
										value: '{record.assistantTelephone}'
									},
									fieldLabel: me.res('contact.fld-assistantTelephone.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactEmail',
					items: [
						{
							xtype: 'sofieldhgroup',
							items: [
								{
									xtype: 'textfield',
									bind: '{record.email1}',
									fieldLabel: me.res('contact.fld-email1.lbl'),
									flex: 1
								}, {
									xtype: 'sohspacer'
								}, 
								me.createAddFieldButtonCfg({
									bind: {
										disabled: '{foDisableAddEmailField}'
									},
									text: me.res('contact.btn-addEmailField.lbl'),
									menu: {
										items: [
											{
												itemId: 'email2',
												text: me.res('contact.fld-email2.lbl'),
												handler: function() {
													me.showField('fldemail2', true);
												}
											}, {
												itemId: 'email3',
												text: me.res('contact.fld-email3.lbl'),
												handler: function() {
													me.showField('fldemail3', true);
												}
											}
										],
										listeners: {
											beforeshow: function(s) {
												s.getComponent('email2').setHidden(me.isFieldHidden('fldemail2'));
												s.getComponent('email3').setHidden(me.isFieldHidden('fldemail3'));
											}
										}
									}
								}, true)
							]		
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldemail2}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldemail2',
									bind: {
										value: '{record.email2}'
									},
									fieldLabel: me.res('contact.fld-email2.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldemail3}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldemail3',
									bind: {
										value: '{record.email3}'
									},
									fieldLabel: me.res('contact.fld-email3.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactPhone',
					items: [
						{
							xtype: 'sofieldhgroup',
							items: [
								{
									xtype: 'textfield',
									bind: '{record.mobile}',
									fieldLabel: me.res('contact.fld-mobile.lbl'),
									flex: 1
								}, {
									xtype: 'sohspacer'
								}, 
								me.createAddFieldButtonCfg({
									bind: {
										disabled: '{foDisableAddTelephoneField}'
									},
									text: me.res('contact.btn-addNumberField.lbl'),
									menu: {
										items: [
											{
												itemId: 'workTelephone2',
												text: me.res('contact.fld-workTelephone2.lbl'),
												handler: function() {
													me.showField('fldworktelephone2', true);
												}
											}, {
												itemId: 'workFax',
												text: me.res('contact.fld-workFax.lbl'),
												handler: function() {
													me.showField('fldworkfax', true);
												}
											}, {
												itemId: 'homeTelephone1',
												text: me.res('contact.fld-homeTelephone1.lbl'),
												handler: function() {
													me.showField('fldhometelephone1', true);
												}
											}, {
												itemId: 'homeTelephone2',
												text: me.res('contact.fld-homeTelephone2.lbl'),
												handler: function() {
													me.showField('fldhometelephone2', true);
												}
											}, {
												itemId: 'homeFax',
												text: me.res('contact.fld-homeFax.lbl'),
												handler: function() {
													me.showField('fldhomefax', true);
												}
											}, {
												itemId: 'pager1',
												text: me.res('contact.fld-pager1.lbl'),
												handler: function() {
													me.showField('fldpager1', true);
												}
											}, {
												itemId: 'pager2',
												text: me.res('contact.fld-pager2.lbl'),
												handler: function() {
													me.showField('fldpager2', true);
												}
											}, {
												itemId: 'instantMsg1',
												text: me.res('contact.fld-instantMsg1.lbl'),
												handler: function() {
													me.showField('fldinstantmsg1', true);
												}
											}, {
												itemId: 'instantMsg2',
												text: me.res('contact.fld-instantMsg2.lbl'),
												handler: function() {
													me.showField('fldinstantmsg2', true);
												}
											}, {
												itemId: 'instantMsg3',
												text: me.res('contact.fld-instantMsg3.lbl'),
												handler: function() {
													me.showField('fldinstantmsg3', true);
												}
											}
										],
										listeners: {
											beforeshow: function(s) {
												s.getComponent('workTelephone2').setHidden(me.isFieldHidden('fldworktelephone2'));
												s.getComponent('workFax').setHidden(me.isFieldHidden('fldworkfax'));
												s.getComponent('homeTelephone1').setHidden(me.isFieldHidden('fldhometelephone1'));
												s.getComponent('homeTelephone2').setHidden(me.isFieldHidden('fldhometelephone2'));
												s.getComponent('homeFax').setHidden(me.isFieldHidden('fldhomefax'));
												s.getComponent('pager1').setHidden(me.isFieldHidden('fldpager1'));
												s.getComponent('pager2').setHidden(me.isFieldHidden('fldpager2'));
												s.getComponent('instantMsg1').setHidden(me.isFieldHidden('fldinstantmsg1'));
												s.getComponent('instantMsg2').setHidden(me.isFieldHidden('fldinstantmsg2'));
												s.getComponent('instantMsg3').setHidden(me.isFieldHidden('fldinstantmsg3'));
											}
										}
									}
								}, true)
							]		
						}, {
							xtype: 'sofieldhgroup',
							items: [
								{
									xtype: 'textfield',
									bind: '{record.workTelephone1}',
									fieldLabel: me.res('contact.fld-workTelephone1.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldworktelephone2}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldworktelephone2',
									bind: {
										value: '{record.workTelephone2}'
									},
									fieldLabel: me.res('contact.fld-workTelephone2.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldworkfax}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldworkfax',
									bind: {
										value: '{record.workFax}'
									},
									fieldLabel: me.res('contact.fld-workFax.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldhometelephone1}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldhometelephone1',
									bind: {
										value: '{record.homeTelephone1}'
									},
									fieldLabel: me.res('contact.fld-homeTelephone1.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldhometelephone2}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldhometelephone2',
									bind: {
										value: '{record.homeTelephone2}'
									},
									fieldLabel: me.res('contact.fld-homeTelephone2.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}, {
							xtype: 'sofieldhgroup',
							bind: {
								hidden: '{hidden.fldhomefax}'
							},
							items: [
								{
									xtype: 'textfield',
									reference: 'fldhomefax',
									bind: {
										value: '{record.homeFax}'
									},
									fieldLabel: me.res('contact.fld-homeFax.lbl'),
									flex: 1
								}, {
									// Mocks inline addButtons spacing!
									bind: { minWidth: '{addButtonsMinWidth}' },
									cls: 'so-form-hspacer x-component-default'
								}
							]
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactNotes',
					items: [
						{
							xtype: 'sofieldhgroup',
							items: [
								me.createNotesFieldCfg({
									minHeight: 100,
									flex: 1
								})
							]	
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactUrl',
					bind: {
						hidden: '{hidden.fldurl}'
					},
					hidden: true,
					items: [
						{
							xtype: 'textfield',
							reference: 'fldurl',
							bind: '{record.url}',
							fieldLabel: me.res('contact.fld-url.lbl')
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactPartner',
					bind: {
						hidden: '{hidden.fldpartner}'
					},
					hidden: true,
					items: [
						{
							xtype: 'textfield',
							reference: 'fldpartner',
							bind: '{record.partner}',
							fieldLabel: me.res('contact.fld-partner.lbl')
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactBirthday',
					bind: {
						hidden: '{hidden.fldbirthday}'
					},
					hidden: true,
					items: [
						{
							xtype: 'datefield',
							reference: 'fldbirthday',
							bind: '{record.birthday}',
							startDay: WT.getStartDay(),
							format: WT.getShortDateFmt(),
							fieldLabel: me.res('contact.fld-birthday.lbl')
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactAnniversary',
					bind: {
						hidden: '{hidden.fldanniversary}'
					},
					hidden: true,
					items: [
						{
							xtype: 'datefield',
							reference: 'fldanniversary',
							bind: '{record.anniversary}',
							startDay: WT.getStartDay(),
							format: WT.getShortDateFmt(),
							fieldLabel: me.res('contact.fld-anniversary.lbl')
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactAddress',
					bind: {
						hidden: '{hidden.fldworkaddress}'
					},
					hidden: true,
					items: [
						{
							xtype: 'textfield',
							reference: 'fldworkaddress',
							bind: '{record.workAddress}',
							fieldLabel: me.res('contact.fld-workAddress.lbl')
						}, {
							xtype: 'textfield',
							bind: '{record.workCity}',
							fieldLabel: me.res('contact.fld-city.lbl')
						}, {
							xtype: 'fieldcontainer',
							layout: {
								type: 'hbox',
								align: 'end'
							},
							defaults: {
								labelAlign: 'top',
								labelSeparator: ''
							},
							items: [
								{
									xtype: 'textfield',
									bind: '{record.workState}',
									fieldLabel: me.res('contact.fld-state.lbl'),
									flex: 1
								}, {
									xtype: 'sohspacer'
								}, {
									xtype: 'textfield',
									bind: '{record.workPostalCode}',
									fieldLabel: me.res('contact.fld-postalCode.lbl')
								}
							]
						}, {
							xtype: 'textfield',
							bind: '{record.workCountry}',
							fieldLabel: me.res('contact.fld-country.lbl')
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactAddress',
					bind: {
						hidden: '{hidden.fldhomeaddress}'
					},
					hidden: true,
					items: [
						{
							xtype: 'textfield',
							reference: 'fldhomeaddress',
							bind: '{record.homeAddress}',
							fieldLabel: me.res('contact.fld-homeAddress.lbl')
						}, {
							xtype: 'textfield',
							bind: '{record.homeCity}',
							fieldLabel: me.res('contact.fld-city.lbl')
						}, {
							xtype: 'fieldcontainer',
							layout: {
								type: 'hbox',
								align: 'end'
							},
							defaults: {
								labelAlign: 'top',
								labelSeparator: ''
							},
							items: [
								{
									xtype: 'textfield',
									bind: '{record.homeState}',
									fieldLabel: me.res('contact.fld-state.lbl'),
									flex: 1
								}, {
									xtype: 'sohspacer'
								}, {
									xtype: 'textfield',
									bind: '{record.homePostalCode}',
									fieldLabel: me.res('contact.fld-postalCode.lbl')
								}
							]
						}, {
							xtype: 'textfield',
							bind: '{record.homeCountry}',
							fieldLabel: me.res('contact.fld-country.lbl')
						}
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactAddress',
					bind: {
						hidden: '{hidden.fldotheraddress}'
					},
					hidden: true,
					items: [
						{
							xtype: 'textfield',
							reference: 'fldotheraddress',
							bind: '{record.otherAddress}',
							fieldLabel: me.res('contact.fld-otherAddress.lbl')
						}, {
							xtype: 'textfield',
							bind: '{record.otherCity}',
							fieldLabel: me.res('contact.fld-city.lbl')
						}, {
							xtype: 'fieldcontainer',
							layout: {
								type: 'hbox',
								align: 'end'
							},
							defaults: {
								labelAlign: 'top',
								labelSeparator: ''
							},
							items: [
								{
									xtype: 'textfield',
									bind: '{record.otherState}',
									fieldLabel: me.res('contact.fld-state.lbl'),
									flex: 1
								}, {
									xtype: 'sohspacer'
								}, {
									xtype: 'textfield',
									bind: '{record.otherPostalCode}',
									fieldLabel: me.res('contact.fld-postalCode.lbl')
								}
							]
						}, {
							xtype: 'textfield',
							bind: '{record.otherCountry}',
							fieldLabel: me.res('contact.fld-country.lbl')
						}
					]
				}, {
					xtype: 'sofieldsection',
					stretchWidth: false,
					items: [
						me.createAddFieldButtonCfg({
							bind: {
								disabled: '{foDisableAddOtherField}'
							},
							text: me.res('contact.btn-addOtherField.lbl'),
							menu: {
								items: [
									{
										itemId: 'url',
										text: me.res('contact.fld-url.lbl'),
										handler: function() {
											me.showField('fldurl', true);
										}
									}, {
										itemId: 'partner',
										text: me.res('contact.fld-partner.lbl'),
										handler: function() {
											me.showField('fldpartner', true);
										}
									}, {
										itemId: 'birthday',
										text: me.res('contact.fld-birthday.lbl'),
										handler: function() {
											me.showField('fldbirthday', true);
										}
									}, {
										itemId: 'anniversary',
										text: me.res('contact.fld-anniversary.lbl'),
										handler: function() {
											me.showField('fldanniversary', true);
										}
									}, {
										itemId: 'address',
										text: me.res('contact.fld-address.lbl'),
										menu: [
											{
												itemId: 'work',
												text: me.res('contact.fld-workAddress.lbl'),
												handler: function() {
													me.showField('fldworkaddress', true);
												}
											}, {
												itemId: 'home',
												text: me.res('contact.fld-homeAddress.lbl'),
												handler: function() {
													me.showField('fldhomeaddress', true);
												}
											}, {
												itemId: 'other',
												text: me.res('contact.fld-otherAddress.lbl'),
												handler: function() {
													me.showField('fldotheraddress', true);
												}
											}
										]
									}		
								],
								listeners: {
									beforeshow: function(s) {
										var address = s.getComponent('address').getMenu();
										s.getComponent('url').setHidden(me.isFieldHidden('fldurl'));
										s.getComponent('partner').setHidden(me.isFieldHidden('fldpartner'));
										s.getComponent('birthday').setHidden(me.isFieldHidden('fldbirthday'));
										s.getComponent('anniversary').setHidden(me.isFieldHidden('fldanniversary'));
										address.getComponent('work').setHidden(me.isFieldHidden('fldworkaddress'));
										address.getComponent('home').setHidden(me.isFieldHidden('fldhomeaddress'));
										address.getComponent('other').setHidden(me.isFieldHidden('fldotheraddress'));
									}
								}
							}
						})
					]
				}, {
					xtype: 'sofieldsection',
					labelIconCls: 'wtcon-icon-contactAttachments',
					bind: {
						hidden: '{!foHasAttachments}'
					},
					hidden: true,
					items: [
						{
							xtype: 'wtattachmentsfield',
							bind: {
								itemsStore: '{record.attachments}'
							},
							itemClickHandler: function(s, rec, e) {
								Sonicle.Utils.showContextMenu(e, me.getRef('cxmAttachment'), {rec: rec});
							},
							fieldLabel: me.res('contact.fld-attachments.lbl')
						}
					]
				}
			];
		},
		
		createPicFieldCfg: function(cfg) {
			var me = this;
			return Ext.apply({
				xtype: 'soimagefield',
				imageWidth: 96,
				imageHeight: 96,
				geometry: 'circle',
				baseImageUrl: WTF.processBinUrl(me.mys.ID, 'GetContactPicture'),
				placeholderImageUrl: me.mys.resourceUrl('contact-placeholder.png'),
				clearTriggerCls: 'wtcon-icon-clearContactPicture',
				uploadTriggerCls: 'wtcon-icon-uploadContactPicture',
				triggersOverCls: 'wt-opacity-70',
				clearTriggerTooltip: me.res('contact.fld-picture.clear.tip'),
				uploadTriggerTooltip: me.res('contact.fld-picture.upload.tip'),
				uploaderConfig: WTF.uploader(me.mys.ID, 'ContactPicture', {
					extraParams: { tag: me.uploadTag },
					maxFileSize: 1048576, // 1MB
					mimeTypes: [
						{title: 'Image files', extensions: 'jpeg,jpg,png'}
					]
				}),
				listeners: {
					uploadstarted: function() {
						me.wait();
					},
					uploadcomplete: function() {
						me.unwait();
					},
					uploaderror: function(s, file, cause) {
						me.unwait();
						WTA.ux.UploadBar.handleUploadError(s, file, cause);
					},
					fileuploaded: function(s, file, json) {
						me.getModel().set('picture', json.data.uploadId);
					}
				}
			}, cfg);
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
		
		createNotesFieldCfg: function(cfg) {
			return Ext.apply({
				xtype: 'textareafield',
				bind: '{record.notes}',
				fieldLabel: this.res('contact.fld-notes.lbl'),
				resizable: true,
				smartResize: true
			}, cfg);
		},
		
		createPicSectionMoreItemsCfg: function() {
			return undefined;
		},
		
		createAddFieldButtonCfg: function(cfg, fieldInline) {
			var me = this,
				baseCfg = {
					xtype: 'button',
					ui: '{tertiary|toolbar}',
					iconCls: 'wt-icon-add',
					arrowVisible: false
				};
			if (fieldInline === true) {
				Ext.apply(baseCfg, {
					bind: {
						minWidth: '{addButtonsMinWidth}'
					},
					textAlign: 'left',
					listeners: {
						resize: function(s, width) {
							var vm = me.getVM(),
								w = vm.get('largestAddButtonWidth');
							if (width > w) vm.set('largestAddButtonWidth', width); 
						}
					}
				});
			}
			return Ext.merge(baseCfg, cfg);
		},
		
		createStatusbarCfg: function() {
			var me = this;
			return me.mys.hasAuditUI() ? {
				xtype: 'statusbar',
				items: [
					me.addAct('contactAuditLog', {
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
		
		showHideField: function(vmField, hidden) {
			this.getVM().set('hidden.'+vmField, hidden);
		},
		
		showField: function(vmField, focusRef) {
			this.showHideField(vmField, false);
			if (focusRef) this.lref(vmField).focus(true, true);
		},
		
		isFieldHidden: function(vmField) {
			return !this.getVM().get('hidden.'+vmField);
		},
		
		initHiddenFields: function() {
			var mo = this.getModel();
			Sonicle.VMUtils.set(this.getVM(), 'hidden', {
				fldtitle: mo.isFieldEmpty('title'),
				fldnickname: mo.isFieldEmpty('nickname'),
				fldgender: mo.isFieldEmpty('gender'),
				fldfunction: mo.isFieldEmpty('function'),
				flddepartment: mo.isFieldEmpty('department'),
				fldmanager: mo.isFieldEmpty('manager'),
				fldassistant: mo.isFieldEmpty('assistant'),
				fldassistanttelephone: mo.isFieldEmpty('assistantTelephone'),
				fldworktelephone2: mo.isFieldEmpty('workTelephone2'),
				fldworkFax: mo.isFieldEmpty('workFax'),
				fldhometelephone1: mo.isFieldEmpty('homeTelephone1'),
				fldhometelephone2: mo.isFieldEmpty('homeTelephone2'),
				fldhomefax: mo.isFieldEmpty('homeFax'),
				fldemail2: mo.isFieldEmpty('email2'),
				fldemail3: mo.isFieldEmpty('email3'),
				fldpager1: mo.isFieldEmpty('pager1'),
				fldpager2: mo.isFieldEmpty('pager2'),
				fldinstantmsg1: mo.isFieldEmpty('instantMsg1'),
				fldinstantmsg2: mo.isFieldEmpty('instantMsg2'),
				fldinstantmsg3: mo.isFieldEmpty('instantMsg3'),
				fldurl: mo.isFieldEmpty('url'),
				fldpartner: mo.isFieldEmpty('partner'),
				fldbirthday: mo.isFieldEmpty('birthday'),
				fldanniversary: mo.isFieldEmpty('anniversary'),
				fldworkaddress: mo.isGroupEmpty('workAddress'),
				fldhomeaddress: mo.isGroupEmpty('homeAddress'),
				fldotheraddress: mo.isGroupEmpty('otherAddress')
			});
		},
		
		onViewLoad: function(s, success) {
			var me = this;
			
			if (me.isMode(me.MODE_NEW)) {
				me.getAct('saveClose').setDisabled(false);
				me.getAct('delete').setHidden(true);
				me.getAct('tags').setHidden(false);
				me.lref('fldpic').setDisabled(false);
				if (me.mys.hasAuditUI()) me.getAct('contactAuditLog').setDisabled(true);
				me.reloadCustomFields((me.opts.data || {}).tags, me.opts.cfData, false);
				
			} else if (me.isMode(me.MODE_VIEW)) {
				me.getAct('saveClose').setDisabled(true);
				me.getAct('delete').setHidden(true);
				me.getAct('tags').setHidden(true);
				me.lref('fldpic').setDisabled(true);
				if (me.mys.hasAuditUI()) me.getAct('contactAuditLog').setDisabled(false);
				me.hideCustomFields(me.getModel().cvalues().getCount() < 1);
				
			} else if (me.isMode(me.MODE_EDIT)) {
				me.getAct('saveClose').setDisabled(false);
				me.getAct('delete').setHidden(false);
				me.getAct('tags').setHidden(false);
				me.lref('fldcategory').setReadOnly(false);
				me.lref('fldpic').setDisabled(false);
				if (me.mys.hasAuditUI()) me.getAct('contactAuditLog').setDisabled(false);
				me.hideCustomFields(me.getModel().cvalues().getCount() < 1);
			}
			me.initHiddenFields();
		},
		
		onViewClose: function(s) {
			s.mys.cleanupUploadedFiles(WT.uiid(s.getId()));
		},
		
		onBeforeModelSave: function(s) {
			var me = this,
				cp = me.lref('tabcfields');
			if (!cp.isValid()) {
				me.lref('tpnlmain').getLayout().setActiveItem(cp);
				return false;
			}
		},
		
		onTagsChanged: function(nv, ov) {
			var me = this;
			if (ov && Sonicle.String.difference(nv, ov).length > 0) { // Make sure that there are really differences!
				me.reloadCustomFields(nv, false);
			}
		},
		
		reloadCustomFields: function(tags, cfInitialData) {
			var me = this;
			WTA.util.CustomFields.reloadCustomFields(tags, cfInitialData, {
				serviceId: me.mys.ID,
				model: me.getModel(),
				idField: 'id',
				cfPanel: me.lref('tabcfields'),
				callback: function(success, json) {
					if (success) me.hideCustomFields(json.total < 1);
				},
				scope: me
			});
		},
		
		hideCustomFields: function(hide) {
			this.lref('tpnlmain').getTabBar().setHidden(hide);
		}
	}
});
