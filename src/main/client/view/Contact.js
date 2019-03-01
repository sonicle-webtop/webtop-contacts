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
		'Sonicle.form.field.ColorComboBox',
		'Sonicle.form.field.Image',
		'Sonicle.plugin.FileDrop',
		'WTA.ux.UploadBar',
		'WTA.ux.field.SuggestCombo',
		'WTA.ux.grid.Attachments',
		'Sonicle.webtop.core.store.Gender',
		'Sonicle.webtop.contacts.model.CategoryLkp',
		'Sonicle.webtop.contacts.model.Contact'
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
	
	uploadTag: null,
	
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
							me.updateCategoryFilters();
						}
					}
				}),
				WTF.lookupCombo('categoryId', 'name', {
					xtype: 'socolorcombo',
					reference: 'fldcategory',
					bind: '{record.categoryId}',
					autoLoadOnValue: true,
					store: {
						model: me.mys.preNs('model.CategoryLkp'),
						proxy: WTF.proxy(me.mys.ID, 'LookupCategoryFolders', 'folders')
					},
					colorField: 'color'
				})
			]
		});
		me.callParent(arguments);
		
		if (Ext.isEmpty(me.uploadTag)) me.uploadTag = WT.uiid(me.getId());
		var main, work, more, home, other, notes, attachs;
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
					xtype: 'formseparator'
				}, Ext.create(
					WTF.remoteCombo('id', 'desc', {
						bind: '{record.company}',
						forceSelection: false,
						autoLoadOnValue: true,
						store: {
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
					clearTriggerCls: WT.plTags.touchtheme ? 'wtcon-trash-trigger-touch' : 'wtcon-trash-trigger',
					uploadTriggerCls: WT.plTags.touchtheme ? 'wtcon-add-trigger-touch' : 'wtcon-add-trigger',
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
					me.getComponent(0).getLayout().setActiveItem(s);
				}
			}
		};
		
		me.add({
			region: 'center',
			xtype: 'wttabpanel',
			items: [main, work, home, other, more, notes, attachs]
		});
		
		me.on('viewload', me.onViewLoad);
		me.on('viewclose', me.onViewClose);
	},
	
	onViewLoad: function(s, success) {
		if (!success) return;
		var me = this,
				owner = me.lref('fldowner');
		
		me.updateCategoryFilters();
		if (me.isMode(me.MODE_NEW)) {
			me.getAct('saveClose').setDisabled(false);
			me.getAct('delete').setDisabled(true);
			owner.setDisabled(false);
		} else if (me.isMode(me.MODE_VIEW)) {
			me.getAct('saveClose').setDisabled(true);
			me.getAct('delete').setDisabled(true);
			owner.setDisabled(true);
			me.lref('fldpic').setDisabled(true);
		} else if (me.isMode(me.MODE_EDIT)) {
			me.getAct('saveClose').setDisabled(false);
			me.getAct('delete').setDisabled(false);
			owner.setDisabled(true);
		}
		
		me.lref('fldfirstname').focus(true);
	},
	
	onViewClose: function(s) {
		s.mys.cleanupUploadedFiles(s.uploadTag);
	},
	
	updateCategoryFilters: function() {
		var me = this,
				mo = me.getModel(),
				sto = me.lref('fldcategory').getStore();
		sto.clearFilter();
		sto.addFilter([{
				property: '_profileId',
				value: mo.get('_profileId')
			}, {
				filterFn: function(rec) {
					if (rec.get('_writable') === false) {
						if (me.isMode(me.MODE_NEW)) return false;
						return rec.getId() === mo.get('categoryId');
					} else {
						return true;
					}
				}
		}]);
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
		
		WT.confirm(WT.res('confirm.delete'), function(bid) {
			if (bid === 'yes') {
				me.wait();
				WT.ajaxReq(me.mys.ID, 'ManageContacts', {
					params: {
						crud: 'delete',
						ids: WTU.arrayAsParam([rec.get('id')])
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
	}
});
