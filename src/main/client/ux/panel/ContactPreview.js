/* 
 * Copyright (C) 2018 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2018 Sonicle S.r.l.".
 */
Ext.define('Sonicle.webtop.contacts.ux.panel.ContactPreview', {
	extend: 'WTA.ux.panel.Panel',
	alias: 'widget.wtconcontactpreviewpanel',
	requires: [
		'Sonicle.form.field.InitialsAvatar',
		'Sonicle.form.field.DisplayImage',
		'Sonicle.form.field.ColorDisplay',
		'Sonicle.form.field.TagDisplay',
		'WTA.util.FoldersTree',
		'WTA.ux.grid.TileList',
		'WTA.ux.panel.CustomFieldsPreview',
		'Sonicle.webtop.contacts.model.ContactPreview'
	],
	mixins: [
		'WTA.mixin.HasModel'
	],
	
	/*
	 * @cfg {Ext.data.Store} tagsStore
	 * The Store that holds available tags data.
	 */
	tagsStore: null,
	
	/**
	 * @cfg {Number} loadContactBuffer
	 * This is the time in milliseconds to buffer load requests when updating selection.
	 */
	loadContactBuffer: 200,
	
	layout: 'card',
	referenceHolder: true,
	bodyPadding: 10,
	
	viewModel: {},
	modelName: 'Sonicle.webtop.contacts.model.ContactPreview',
	mys: null,
	
	constructor: function(cfg) {
		var me = this;
		me.callParent([cfg]);
		
		WTU.applyFormulas(me.getVM(), {
			foHasPicture: WTF.foIsEqual('record', 'pic', true),
			foIsEditable: WTF.foGetFn('record', '_erights', function(val) {
				var er = WTA.util.FoldersTree.toRightsObj(val);
				return er.UPDATE;
			}),
			foHasData: {
				bind: {bindTo: '{record}'},
				get: function(val) {
					return val ? val.hasData() : true;
				}
			},
			foMainEmail: WTF.foGetFn('record', 'data1', function(val) {
				return (val && val.getCount() > 0) ? val.first().get('value') : null;
			}),
			foMainTelephone: WTF.foGetFn('record', 'data2', function(val) {
				return (val && val.getCount() > 0) ? val.first().get('value') : null;
			}),
			foMobile: WTF.foGetFn('record', 'data2', function(val) {
				if (val && val.getCount() > 0) {
					var rec = val.findRecord('type', 'mobile');
					return rec ? rec.get('value') : null;
				} else {
					return null;
				}
				return (val && val.getCount() > 0) ? val.first().get('value') : null;
			}),
			foHasEmails: WTF.foIsEmpty(null, 'foMainEmail', true),
			foHasTelephones: WTF.foIsEmpty(null, 'foMainTelephone', true),
			foHasMobile: WTF.foIsEmpty(null, 'foMobile', true),
			foHasUser: WTF.foIsEmpty('record', 'userProfile', true),
			foHasBusinessInfo: WTF.foIsEmpty('record', 'businessInfo', true),
			foWriteMessageTip: WTF.foResFormat(null, 'foMainEmail', me.mys.ID, 'contactPreview.single.tb.writeMessage.tip'),
			foWriteListMessageTip: WTF.foResFormat('record', 'avatarName', me.mys.ID, 'contactPreview.single.tb.writeMessage.tip'),
			foOpenChatTip: WTF.foResFormat('record', 'userDisplayName', me.mys.ID, 'contactPreview.single.tb.openChat.tip'),
			foCallNumberTip: WTF.foResFormat(null, 'foMainTelephone', me.mys.ID, 'contactPreview.single.tb.callNumber.tip'),
			foWriteSMSTip: WTF.foResFormat(null, 'foMobile', me.mys.ID, 'contactPreview.single.tb.writeSms.tip'),
			foMultiSelTitle: WTF.foGetFn(null, 'contacts', function(val) {
				return val ? me.mys.res('contactPreview.multi.tit', val.length) : null;
			}),
			foHasTags: WTF.foIsEmpty('record', 'tags', true)
		});
	},
	
	initComponent: function() {
		var me = this;
		me.callParent(arguments);
		me.add([
			Ext.apply(me.createEmptyItemCfg(), {
				itemId: 'empty'
			}),
			Ext.apply(me.createContactItemCfg(), {
				itemId: 'contact'
			}),
			Ext.apply(me.createContactsListItemCfg(), {
				itemId: 'list'
			}),
			Ext.apply(me.createMultiItemCfg(), {
				itemId: 'multi'
			})
		]);
		me.setActiveItem('empty');
		me.getViewModel().bind('{record._cfdefs}', me.onCFDefsUpdate, me);
	},
	
	createEmptyItemCfg: function() {
		var me = this;
		return {
			xtype: 'container',
			layout: {
				type: 'vbox',
				pack: 'center',
				align: 'middle'
			},
			items: [{
				xtype: 'label',
				text: me.mys.res('contactPreview.no.tit'),
				cls: 'wt-theme-text-tit',
				style: 'font-size:1.2em'
			}, {
				xtype: 'label',
				text: me.mys.res('contactPreview.no.txt'),
				cls: 'wt-theme-text-sub',
				style: 'font-size:0.9em'
			}]
		};
	},
	
	createContactsListItemCfg: function() {
		var me = this;
		return {
			xtype: 'container',
			layout: 'fit',
			items: [
				{
					xtype: 'container',
					layout: 'hbox',
					items: [
						{
							xtype: 'container',
							layout: 'anchor',
							items: [
								{
									xtype: 'sointialsavatarfield',
									bind: '{record.avatarName}',
									margin: 5,
									avatarSize: 110
								}
							],
							width: 140
						}, {
							xtype: 'container',
							layout: 'anchor',
							items: [
								{
									xtype: 'displayfield',
									bind: '{record.avatarName}',
									fieldStyle: {
										fontSize: '2em'
									}
								}, {
									xtype: 'label',
									text: me.mys.res('contactsList.tit'),
									cls: 'wt-theme-text-sub',
									// Display as inline to keep height dimension
									style: 'display:inline-block;font-size:0.9em'
								}, {
									xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [
										{
											xtype: 'socolordisplayfield',
											bind: '{record.catColor}'
										}, {
											xtype: 'displayfield',
											bind: '{record.catName}'
										}
									]
								}, {
									xtype: 'container',
									layout: 'hbox',
									defaults: {
										margin: '0 2 0 0',
										ui: 'default-toolbar'
									},
									items: [
										{
											xtype: 'button',
											bind: {
												disabled: '{!foHasEmails}',
												tooltip: '{foWriteListMessageTip}'
											},
											tooltip: me.mys.res('contactPreview.single.tb.writeMessage.tip'),
											iconCls: 'wtcon-icon-writeMessage',
											handler: function() {
												var vm = me.getVM();
												me.fireEvent('writeemail', me, [vm.get('foMainEmail')], vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
											}
										}
									],
									// Bottom margin to space tags
									margin: '0 0 5 0'
								}, {
									xtype: 'sotagdisplayfield',
									bind: {
										value: '{record.tags}',
										hidden: '{!foHasTags}'
									},
									delimiter: '|',
									store: me.tagsStore,
									valueField: 'id',
									displayField: 'name',
									colorField: 'color',
									sourceField: 'source',
									hidden: true,
									hideLabel: true
							}],
							flex: 1,
							margin: '10 0 0 20',
							height: 160
					}],
					padding: 20
				}
			]
		};
	},
	
	createContactItemCfg: function() {
		var me = this;
		return {
			xtype: 'container',
			layout: 'border',
			items: [
				{
					xtype: 'wtpanel',
					region: 'north',
					height: 210,
					bodyPadding: 20,
					layout: 'hbox',
					items: [
						{
							xtype: 'container',
							layout: 'anchor',
							items: [
								{
									xtype: 'sointialsavatarfield',
									bind: {
										value: '{record.avatarName}',
										hidden: '{record.pic}'
									},
									margin: 5,
									avatarSize: 110
								}, {
									xtype: 'sodisplayimage',
									bind: {
										value: '{record.pictureId}',
										hidden: '{!record.pic}'
									},
									hidden: true,
									imageWidth: 110,
									imageHeight: 110,
									geometry: 'circle',
									border: false,
									baseImageUrl: WTF.processBinUrl(me.mys.ID, 'GetContactPicture'),
									placeholderImageUrl: me.mys.resourceUrl('contact-placeholder.png')
								}
							],
							width: 140
						}, {
							xtype: 'container',
							layout: 'anchor',
							items: [
								{
									xtype: 'displayfield',
									bind: '{record.avatarName}',
									fieldStyle: {
										fontSize: '2em'
									}
								}, {
									xtype: 'label',
									bind: {
										text: '{record.businessInfo}'
										//hidden: '{!foHasBusinessInfo}'
									},
									cls: 'wt-theme-text-sub',
									// Display as inline to keep height dimension
									style: 'display:inline-block;font-size:0.9em'
								}, {
									xtype: 'fieldcontainer',
									layout: 'hbox',
									items: [
										{
											xtype: 'socolordisplayfield',
											bind: '{record.catColor}'
										}, {
											xtype: 'displayfield',
											bind: '{record.catName}'
										}
									]
								}, {
									xtype: 'container',
									layout: 'hbox',
									defaults: {
										margin: '0 2 0 0',
										ui: 'default-toolbar'
									},
									items: [
										{
											xtype: 'button',
											bind: {
												disabled: '{!foHasEmails}',
												tooltip: '{foWriteMessageTip}'
											},
											tooltip: me.mys.res('contactPreview.single.tb.writeMessage.tip'),
											iconCls: 'wtcon-icon-writeMessage',
											handler: function() {
												var vm = me.getVM();
												me.fireEvent('writeemail', me, [vm.get('foMainEmail')], vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
											}
										}, {
											xtype: 'button',
											bind: {
												disabled: '{!foHasUser}',
												tooltip: '{foOpenChatTip}'
											},
											hidden: !WT.getVar('imEnabled'),
											//text: me.mys.res('contactPreview.single.tb.openChat.lbl'),
											tooltip: me.mys.res('contactPreview.single.tb.openChat.tip'),
											iconCls: 'wt-icon-im-chat',
											handler: function() {
												me.fireEvent('openchat', me, null);
											}
										}, {
											xtype: 'button',
											bind: {
												disabled: '{!foHasTelephones}',
												tooltip: '{foCallNumberTip}'
											},
											hidden: !WT.getVar('pbxConfigured'),
											//text: me.mys.res('contactPreview.single.tb.callNumber.lbl'),
											tooltip: me.mys.res('contactPreview.single.tb.callNumber.tip'),
											iconCls: 'wt-icon-call',
											handler: function() {
												var vm = me.getVM();
												me.fireEvent('callnumber', me, vm.get('foMainTelephone'), vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
											}
										}, {
											xtype: 'button',
											bind: {
												disabled: '{!foHasMobile}',
												tooltip: '{foWriteSMSTip}'
											},
											hidden: !WT.getVar('smsConfigured'),
											//text: me.mys.res('contactPreview.single.tb.writeSms.lbl'),
											tooltip: me.mys.res('contactPreview.single.tb.writeSms.tip'),
											iconCls: 'wt-icon-sms',
											handler: function() {
												var vm = me.getVM();
												me.fireEvent('writesms', me, vm.get('foMobile'), vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
											}
										}
									],
									// Bottom margin to space tags
									margin: '0 0 5 0'
								}, {
									xtype: 'sotagdisplayfield',
									bind: {
										value: '{record.tags}',
										hidden: '{!foHasTags}'
									},
									delimiter: '|',
									store: me.tagsStore,
									valueField: 'id',
									displayField: 'name',
									colorField: 'color',
									sourceField: 'source',
									hidden: true,
									hideLabel: true
								}
							],
							flex: 1,
							margin: '10 0 0 20',
							height: 160
						}
					]
				}, {
					xtype: 'tabpanel',
					region: 'center',
					border: false,
					items: [
						{
							xtype: 'wtpanel',
							title: me.mys.res('contactPreview.single.contact.tit'),
							layout: 'anchor',
							defaults: {
								anchor: '100%',
								margin: '15 0 15 0'
							},
							items: [
								{
									xtype: 'container',
									items: [{
										xtype: 'container',
										bind: {
											hidden: '{!foHasData}'
										},
										hidden: true,
										layout: 'hbox',
										items: [{
											xtype: 'wttilelist',
											reference: 'gpemaillist',
											linkifyValue: true,
											bind: {
												store: '{record.data1}',
												hidden: '{!foHasEmails}'
											},
											labelField: 'type',
											labelTexts: {
												work: me.mys.res('contactPreview.single.workEmail'),
												home: me.mys.res('contactPreview.single.homeEmail')
											},
											listeners: {
												cellvalueclick: function(s, val) {
													var vm = me.getVM();
													me.fireEvent('writeemail', me, [val], vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
												}
											},
											margin: '0 5 0 5',
											flex: 1,
											maxWidth: 230
										}, {
											xtype: 'wttilelist',
											reference: 'gptelephonelist',
											linkifyValue: true,
											bind: {
												store: '{record.data2}',
												hidden: '{!foHasTelephones}'
											},
											labelField: 'type',
											labelTexts: {
												mobile: me.mys.res('contactPreview.single.mobile'),
												work: me.mys.res('contactPreview.single.workTelephone'),
												home: me.mys.res('contactPreview.single.homeTelephone')
											},
											listeners: {
												cellvalueclick: function(s, val) {
													var vm = me.getVM();
													me.fireEvent('callnumber', me, val, vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
												}
											},
											margin: '0 5 0 5',
											flex: 1,
											maxWidth: 180
										}, {
											xtype: 'wttilelist',
											reference: 'gpmorelist',
											linkifyValue: true,
											bind: {
												store: '{record.data3}'
											},
											labelField: 'type',
											labelTexts: {
												fullName: me.mys.res('contactPreview.single.fullName'),
												company: me.mys.res('contactPreview.single.company'),
												workadd: me.mys.res('contactPreview.single.workAddress'),
												homeadd: me.mys.res('contactPreview.single.homeAddress')
											},
											listeners: {
												cellvalueclick: function(s, val, rec) {
													if (['workadd', 'homeadd'].indexOf(rec.get('type')) !== -1) {
														var vm = me.getVM();
														me.fireEvent('mapaddress', me, val, vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
													}
												}
											},
											margin: '0 5 0 5',
											flex: 1,
											maxWidth: 230
										}]
										//margin: '5 0 5 0',
										//height: '100%'
									}, {
										xtype: 'container',
										bind: {
											hidden: '{foHasData}'
										},
										hidden: true,
										layout: {
											type: 'vbox',
											pack: 'center',
											align: 'middle'
										},
										items: [{
											xtype: 'label',
											text: me.mys.res('contactPreview.single.contact.emp'),
											cls: 'wt-theme-text-sub',
											style: 'font-size:0.9em'
										}]
										//margin: '10 0 10 0'
									}]
								}, {
									xtype: 'formseparator'
								}, {
									xtype: 'textarea',
									bind: {
										value: '{record.notes}'
									},
									fieldLabel: 'Note',
									labelAlign: 'top',
									readOnly: true,
									grow: true,
									growMin: 60,
									growMax: 200
								}
							]
						}, {
							xtype: 'wtcfieldspreviewpanel',
							reference: 'tabcfields',
							title: me.mys.res('contactPreview.single.cfields.tit'),
							bind: {
								store: '{record.cvalues}'
								// Do not use this binding here, it will cause internal exception
								// in Ext.app.bind.Stub during model load with new ID. (see explicit vm.bind in initComponent)
								//fieldsDefs: '{record._cfdefs}'
							}
						}
					],
					tabBar:	{
						items: [
							{
								xtype: 'tbfill'
							}, {
								xtype: 'button',
								ui: 'default-toolbar',
								bind: {
									disabled: '{!foIsEditable}'
								},
								text: me.mys.res('contactPreview.single.contact.tb.edit.lbl'),
								handler: function() {
									var vm = me.getVM();
									me.fireEvent('editcontact', me, vm.get('record.isList'), vm.get('record.id'));
								}
							}
						]
					}
				}
			]
		};
	},
	
	createMultiItemCfg: function() {
		var me = this;
		return {
			xtype: 'container',
			layout: {
				type: 'vbox',
				pack: 'center',
				align: 'middle'
			},
			items: [{
				xtype: 'label',
				bind: {
					text: '{foMultiSelTitle}'
				},
				cls: 'wt-theme-text-tit',
				style: 'font-size:1.2em'
			}, {
				xtype: 'toolbar',
				vertical: true,
				margin: '20 0 0 0',
				items: [{
					text: me.mys.res('contactPreview.multi.tb.writeMessage.lbl'),
					iconCls: 'wtcon-icon-writeMessage',
					handler: function() {
						var emails = me.extractContactsEmails(me.getViewModel().get('contacts'));
						me.fireEvent('writeemail', me, emails, null, null);
					}
				}]
			}, {
				xtype: 'sospacer'
			}, {
				xtype: 'formseparator',
				width: 180
			}, {
				xtype: 'sospacer'
			}, {
				xtype: 'toolbar',
				vertical: true,
				items: [{
					text: me.mys.res('contactPreview.multi.tb.cancel.lbl'),
					tooltip: me.mys.res('contactPreview.multi.tb.cancel.tip'),
					handler: function() {
						me.fireEvent('clearselection', me);
					}
				}]
			}]
		};
	},
	
	setContacts: function(contacts) {
		var me = this,
				card = me;
		me.getViewModel().set('contacts', contacts);
		if (contacts && contacts.length === 1) {
			me.loadContact(contacts[0].getId());
			card.setActiveItem((contacts[0].get('isList') === true) ? 'list' : 'contact');
		} else if (contacts && contacts.length > 1) {
			card.setActiveItem('multi');
		} else {
			me.loadContact(null);
			card.setActiveItem('empty');
		}
	},
	
	isLoaded: function(id, idField) {
		var contacts = this.getViewModel().get('contacts'), val;
		if (contacts && contacts.length === 1) {
			val = Ext.isEmpty(idField) ? contacts[0].getId() : contacts[0].get(idField);
			return id === val;
		} else {
			return false;
		}
	},
	
	loadContact: function(contactUid) {
		var me = this;
		if (me.timLC) clearTimeout(me.timLC);
		me.timLC = setTimeout(function() {
			me.clearModel();
			if (contactUid) {
				me.loadModel({
					data: {uid: contactUid},
					dirty: false
				});
			}
		}, me.loadContactBuffer || 200);
	},
	
	privates: {
		extractContactsEmails: function(contacts) {
			var arr = [], i, email;
			if (contacts) {
				for (i=0; i<contacts.length; i++) {
					email = contacts[i].get('email');
					if (!Ext.isEmpty(email)) arr.push(email);
				}
			}
			return arr;
		},
		
		onCFDefsUpdate: function(nv, ov) {
			var cmp = this.lookupReference('tabcfields');
			if (cmp) cmp.setFieldsDefs(nv);
		}
	}
});
