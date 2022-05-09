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
Ext.define('Sonicle.webtop.contacts.view.Contact', {
	extend: 'WTA.sdk.ModelView',
	requires: [
		'Sonicle.String',
		'Sonicle.Utils',
		'Sonicle.form.field.ComboBox',
		'Sonicle.form.field.Image',
		'Sonicle.form.field.TagDisplay',
		'Sonicle.plugin.FileDrop',
		'WTA.ux.UploadBar',
		'WTA.ux.field.SuggestCombo',
		'WTA.ux.grid.Attachments',
		'WTA.ux.panel.CustomFieldsEditor',
		'Sonicle.webtop.core.store.Gender',
		'Sonicle.webtop.contacts.model.CategoryLkp',
		'Sonicle.webtop.contacts.model.Contact'
	],
	uses: [
		'Sonicle.webtop.core.view.Tags'
	],
	
	dockableConfig: {
		title: '{contact.tit}',
		iconCls: 'wtcon-icon-contact',
		width: 650,
		height: 550
	},
	confirm: 'yn',
	autoToolbar: false,
	modelName: 'Sonicle.webtop.contacts.model.Contact',
	actionsResPrefix: 'contact',
	
	uploadTag: null,
	
	constructor: function(cfg) {
		var me = this;
		me.callParent([cfg]);
		
		WTU.applyFormulas(me.getVM(), {
			foTags: WTF.foTwoWay('record', 'tags', function(v) {
					return Sonicle.String.split(v, '|');
				}, function(v) {
					return Sonicle.String.join('|', v);
			}),
			foHasTags: WTF.foIsEmpty('record', 'tags', true)
		});
	},
	
	initComponent: function() {
		var me = this,
				vm = me.getViewModel();
		
		Ext.apply(me, {
			dockedItems: [
				{
					xtype: 'toolbar',
					dock: 'top',
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
								me.deleteContact();
							}
						}),
						'-',
						me.addAct('print', {
							text: null,
							tooltip: WT.res('act-print.lbl'),
							iconCls: 'wt-icon-print',
							handler: function() {
								//TODO: aggiungere l'azione 'salva' permettendo così la stampa senza chiudere la form
								me.printContact(me.getModel().getId());
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
						value: '{foTags}',
						hidden: '{!foHasTags}'
					},
					store: WT.getTagsStore(),
					valueField: 'id',
					displayField: 'name',
					colorField: 'color',
					dummyIcon: 'loading',
					hidden: true,
					hideLabel: true,
					margin: '0 0 5 0'
				},
				me.mys.hasAudit() ? {
					xtype: 'statusbar',
					dock: 'bottom',
					items: [
						me.addAct('contactAuditLog', {
							text: null,
							tooltip: WT.res('act-auditLog.lbl'),
							iconCls: 'fas fa-history wt-theme-glyph',
							handler: function() {
								me.mys.openAuditUI(me.getModel().getId(), 'CONTACT');
							},
							scope: me
						})
					]
				} : null
			]
		});
		me.callParent(arguments);
		
		if (Ext.isEmpty(me.uploadTag)) me.uploadTag = WT.uiid(me.getId());
		var main, work, more, home, other, notes, attachs, cfields;
		main = {
			xtype: 'wtform',
			layout: 'column',
			title: me.mys.res('contact.main.tit'),
			defaults: {
				xtype: 'container',
				layout: 'anchor',
				modelValidation: true
			},
			items: [{
				defaults: {
					labelWidth: 120,
					width: 400
				},
				items: [{
					xtype: 'textfield',
					bind: '{record.title}',
					fieldLabel: me.mys.res('contact.fld-title.lbl')
				}, {
					xtype: 'textfield',
					reference: 'fldfirstname',
					bind: '{record.firstName}',
					fieldLabel: me.mys.res('contact.fld-firstName.lbl')
				}, {
					xtype: 'textfield',
					bind: '{record.lastName}',
					fieldLabel: me.mys.res('contact.fld-lastName.lbl')
				}, {
					xtype: 'textfield',
					bind: {
						value: '{record.displayName}',
						emptyText: '{record.calcDisplayName}'
					},
					fieldLabel: me.mys.res('contact.fld-displayName.lbl')
				}, {
					xtype: 'formseparator'
				}, Ext.create(
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
						fieldLabel: me.mys.res('contact.fld-company.lbl')
					})
				), {
					xtype: 'wtsuggestcombo',
					bind: '{record.function}',
					sid: me.mys.ID,
					suggestionContext: 'contactFunction',
					fieldLabel: me.mys.res('contact.fld-function.lbl')
				}, {
					xtype: 'wtsuggestcombo',
					bind: '{record.department}',
					sid: me.mys.ID,
					suggestionContext: 'contactDepartment',
					fieldLabel: me.mys.res('contact.fld-department.lbl')
				}, {
					xtype: 'formseparator'
				}, {
					xtype: 'textfield',
					bind: '{record.mobile}',
					fieldLabel: me.mys.res('contact.fld-mobile.lbl')
				}, {
					xtype: 'textfield',
					bind: '{record.email1}',
					fieldLabel: me.mys.res('contact.fld-workEmail.lbl')
				}, {
					xtype: 'textfield',
					bind: '{record.workTelephone1}',
					fieldLabel: me.mys.res('contact.fld-workTelephone.lbl')
				}, {
					xtype: 'textfield',
					bind: '{record.email2}',
					fieldLabel: me.mys.res('contact.fld-homeEmail.lbl')
				}, {
					xtype: 'textfield',
					bind: '{record.homeTelephone1}',
					fieldLabel: me.mys.res('contact.fld-homeTelephone.lbl')
				}]
			}, {
				margin: '20 0 0 30',
				items: [{
					xtype: 'soimagefield',
					reference: 'fldpic',
					bind: '{record.picture}',
					imageWidth: 150,
					imageHeight: 150,
					geometry: 'circle',
					baseImageUrl: WTF.processBinUrl(me.mys.ID, 'GetContactPicture'),
					placeholderImageUrl: me.mys.resourceUrl('contact-placeholder.png'),
					clearTriggerCls: 'far fa-trash-alt wt-theme-glyph-lighter',
					uploadTriggerCls: 'far fa-plus-square wt-theme-glyph-lighter',
					triggersOverCls: 'wt-theme-glyph',
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
				}]
			}]
		};
		
		work = {
			xtype: 'wtform',
			modelValidation: true,
			title: me.mys.res('contact.work.tit'),
			defaults: {
				labelWidth: 120,
				width: 400
			},
			items: [{
				xtype: 'textfield',
				bind: '{record.email1}',
				fieldLabel: me.mys.res('contact.fld-email.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.workAddress}',
				fieldLabel: me.mys.res('contact.fld-address.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.workPostalCode}',
				fieldLabel: me.mys.res('contact.fld-postalCode.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.workCity}',
				fieldLabel: me.mys.res('contact.fld-city.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.workState}',
				fieldLabel: me.mys.res('contact.fld-state.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.workCountry}',
				fieldLabel: me.mys.res('contact.fld-country.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.workTelephone1}',
				fieldLabel: me.mys.res('contact.fld-telephone.lbl')	
			}, {
				xtype: 'textfield',
				bind: '{record.workTelephone2}',
				fieldLabel: me.mys.res('contact.fld-telephone2.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.workFax}',
				fieldLabel: me.mys.res('contact.fld-fax.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.pager1}',
				fieldLabel: me.mys.res('contact.fld-pager.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.instantMsg1}',
				fieldLabel: me.mys.res('contact.fld-instantMsg.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.mobile}',
				fieldLabel: me.mys.res('contact.fld-mobile.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.manager}',
				fieldLabel: me.mys.res('contact.fld-manager.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.assistant}',
				fieldLabel: me.mys.res('contact.fld-assistant.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.assistantTelephone}',
				fieldLabel: me.mys.res('contact.fld-assistantTelephone.lbl')
			}]
		};
		
		home = {
			xtype: 'wtform',
			modelValidation: true,
			title: me.mys.res('contact.home.tit'),
			bodyPadding: 5,
			defaults: {
				labelWidth: 120,
				width: 400
			},
			items: [{
				xtype: 'textfield',
				bind: '{record.email2}',
				fieldLabel: me.mys.res('contact.fld-email.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.homeAddress}',
				fieldLabel: me.mys.res('contact.fld-address.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.homePostalCode}',
				fieldLabel: me.mys.res('contact.fld-postalCode.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.homeCity}',
				fieldLabel: me.mys.res('contact.fld-city.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.homeState}',
				fieldLabel: me.mys.res('contact.fld-state.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.homeCountry}',
				fieldLabel: me.mys.res('contact.fld-country.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.homeTelephone1}',
				fieldLabel: me.mys.res('contact.fld-telephone.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.homeTelephone2}',
				fieldLabel: me.mys.res('contact.fld-telephone2.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.homeFax}',
				fieldLabel: me.mys.res('contact.fld-fax.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.pager2}',
				fieldLabel: me.mys.res('contact.fld-pager.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.instantMsg2}',
				fieldLabel: me.mys.res('contact.fld-instantMsg.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.mobile}',
				fieldLabel: me.mys.res('contact.fld-mobile.lbl')
			}]
		};
		
		other = {
			xtype: 'wtform',
			modelValidation: true,
			title: me.mys.res('contact.other.tit'),
			defaults: {
				labelWidth: 120,
				width: 400
			},
			items: [{
				xtype: 'textfield',
				bind: '{record.email3}',
				fieldLabel: me.mys.res('contact.fld-email.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.otherAddress}',
				fieldLabel: me.mys.res('contact.fld-address.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.otherPostalCode}',
				fieldLabel: me.mys.res('contact.fld-postalCode.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.otherCity}',
				fieldLabel: me.mys.res('contact.fld-city.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.otherState}',
				fieldLabel: me.mys.res('contact.fld-state.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.otherCountry}',
				fieldLabel: me.mys.res('contact.fld-country.lbl')
			}, {
				xtype: 'textfield',
				bind: '{record.instantMsg3}',
				fieldLabel: me.mys.res('contact.fld-instantMsg.lbl')
			}]
		};
		
		more = {
			xtype: 'wtform',
			modelValidation: true,
			title: me.mys.res('contact.more.tit'),
			bodyPadding: 5,
			defaults: {
				labelWidth: 120,
				width: 400
			},
			items: [{
				xtype: 'textfield',
				bind: '{record.nickname}',
				fieldLabel: me.mys.res('contact.fld-nickname.lbl')
			}, Ext.create(
				WTF.remoteCombo('id', 'desc', {
					bind: '{record.gender}',
					autoLoadOnValue: true,
					store: Ext.create('Sonicle.webtop.core.store.Gender', {
						autoLoad: true
					}),
					triggers: {
						clear: WTF.clearTrigger()
					},
					fieldLabel: me.mys.res('contact.fld-gender.lbl')
				})
			), {
				xtype: 'textfield',
				bind: '{record.url}',
				fieldLabel: me.mys.res('contact.fld-url.lbl')
			}, {
				xtype: 'formseparator'
			}, {
				xtype: 'textfield',
				bind: '{record.partner}',
				fieldLabel: me.mys.res('contact.fld-partner.lbl')
			}, {
				xtype: 'datefield',
				bind: '{record.birthday}',
				startDay: WT.getStartDay(),
				format: WT.getShortDateFmt(),
				fieldLabel: me.mys.res('contact.fld-birthday.lbl')
			}, {
				xtype: 'datefield',
				bind: '{record.anniversary}',
				startDay: WT.getStartDay(),
				format: WT.getShortDateFmt(),
				fieldLabel: me.mys.res('contact.fld-anniversary.lbl')
			}]
		};
		
		notes = {
			xtype: 'wtform',
			layout: 'fit',
			modelValidation: true,
			title: me.mys.res('contact.notes.tit'),
			items: [{
				xtype: 'textarea',
				bind: '{record.notes}'
			}]
		};
		
		attachs = {
			xtype: 'wtattachmentsgrid',
			title: me.mys.res('contact.attachments.tit'),
			bind: {
				store: '{record.attachments}'
			},
			sid: me.mys.ID,
			uploadContext: 'ContactAttachment',
			uploadTag: me.uploadTag,
			dropElementId: null,
			highlightDrop: true,
			typeField: 'ext',
			listeners: {
				attachmentlinkclick: function(s, rec) {
					me.openAttachmentUI(rec, false);
				},
				attachmentdownloadclick: function(s, rec) {
					me.openAttachmentUI(rec, true);
				},
				attachmentdeleteclick: function(s, rec) {
					s.getStore().remove(rec);
				},
				attachmentuploaded: function(s, uploadId, file) {
					var sto = s.getStore();
					sto.add(sto.createModel({
						name: file.name,
						size: file.size,
						_uplId: uploadId
					}));
					me.lref('tpnlmain').setActiveItem(s);
				}
			}
		};
		
		cfields = {
			xtype: 'wtcfieldseditorpanel',
			reference: 'tabcfields',
			title: me.mys.res('contact.cfields.tit'),
			bind: {
				store: '{record.cvalues}',
				fieldsDefs: '{record._cfdefs}'
			},
			serviceId: me.mys.ID,
			defaultLabelWidth: 120,
			listeners: {
				prioritize: function(s) {
					me.lref('tpnlmain').setActiveItem(s);
				}
			}
		};
		
		me.add({
			region: 'center',
			xtype: 'wttabpanel',
			reference: 'tpnlmain',
			items: [main, work, home, other, more, notes, attachs, cfields]
		});
		
		me.on('viewload', me.onViewLoad);
		me.on('viewclose', me.onViewClose);
		me.on('beforemodelsave', me.onBeforeModelSave, me);
		vm.bind('{foTags}', me.onTagsChanged, me);
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
	
	deleteContact: function() {
		var me = this,
				rec = me.getModel();
		
		WT.confirm(me.res('contact.confirm.delete', Ext.String.ellipsis(rec.get('displayName'), 40)), function(bid) {
			if (bid === 'yes') {
				me.wait();
				WT.ajaxReq(me.mys.ID, 'ManageContacts', {
					params: {
						crud: 'delete',
						ids: Sonicle.Utils.toJSONArray([rec.get('id')])
					},
					callback: function(success) {
						me.unwait();
						if (success) {
							me.fireEvent('viewsave', me, true, rec);
							me.closeView(false);
						}
					}
				});
			}
		}, me);
	},
	
	printContact: function(contactId) {
		var me = this;
		if (me.getModel().isDirty()) {
			WT.warn(WT.res('warn.print.notsaved'));
		} else {
			me.mys.printContactsDetail([contactId]);
		}
	},
	
	privates: {
		onViewLoad: function(s, success) {
			if (!success) return;
			var me = this;

			if (me.isMode(me.MODE_NEW)) {
				me.getAct('saveClose').setDisabled(false);
				me.getAct('delete').setDisabled(true);
				me.getAct('tags').setDisabled(false);
				me.lref('fldcategory').setReadOnly(false);
				me.lref('fldpic').setDisabled(false);
				if (me.mys.hasAudit()) me.getAct('contactAuditLog').setDisabled(true);
				me.reloadCustomFields([]);
			} else if (me.isMode(me.MODE_VIEW)) {
				me.getAct('saveClose').setDisabled(true);
				me.getAct('delete').setDisabled(true);
				me.getAct('tags').setDisabled(true);
				me.lref('fldcategory').setReadOnly(true);
				me.lref('fldpic').setDisabled(true);
				if (me.mys.hasAudit()) me.getAct('contactAuditLog').setDisabled(false);
			} else if (me.isMode(me.MODE_EDIT)) {
				me.getAct('saveClose').setDisabled(false);
				me.getAct('delete').setDisabled(false);
				me.getAct('tags').setDisabled(false);
				me.lref('fldcategory').setReadOnly(false);
				me.lref('fldpic').setDisabled(false);
				if (me.mys.hasAudit()) me.getAct('contactAuditLog').setDisabled(false);
			}
			me.lref('fldfirstname').focus(true);
		},
		
		onViewClose: function(s) {
			s.mys.cleanupUploadedFiles(s.uploadTag);
		},
		
		onBeforeModelSave: function(s) {
			var cp = this.lref('tabcfields');
			if (!cp.isValid()) {
				this.lref('tpnlmain').getLayout().setActiveItem(cp);
				return false;
			}
		},
		
		onTagsChanged: function(nv, ov) {
			if (ov && Sonicle.String.difference(nv, ov).length > 0) { // Make sure that there are really differences!
				this.reloadCustomFields(nv);
			}
		},
		
		reloadCustomFields: function(tags) {
			var me = this,
					mo = me.getModel(),
					cftab = me.lref('tabcfields');
			me.getCustomFieldsDefsData(tags, mo.getId(), {
				callback: function(success, json) {
					if (success) {
						Ext.iterate(json.data.cvalues, function(cval) {
							var rec = mo.cvalues().getById(cval.id);
							if (!rec) {
								mo.cvalues().add(cval);
							} else {
								rec.set(cval);
							}
						});
						mo.set('_cfdefs', json.data.cfdefs);
						me.lref('tabcfields').setStore(mo.cvalues());
					}
					cftab.unwait();
				}
			});
		},
		
		getCustomFieldsDefsData: function(tags, contactId, opts) {
			opts = opts || {};
			var me = this;
			WT.ajaxReq(me.mys.ID, 'GetCustomFieldsDefsData', {
				params: {
					tags: Sonicle.Utils.toJSONArray(tags),
					contactId: (contactId !== null && contactId > 0) ? contactId : null
				},
				callback: function(success, json) {
					Ext.callback(opts.callback, opts.scope || me, [success, json]);
				}
			});
		}
	}
});
