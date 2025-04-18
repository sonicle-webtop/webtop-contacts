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
		'Sonicle.VMUtils',
		'Sonicle.form.field.InitialsAvatar',
		'Sonicle.form.field.DisplayImage',
		'Sonicle.form.field.ColorDisplay',
		'Sonicle.form.field.TagDisplay',
		'Sonicle.grid.TileList',
		'WTA.util.FoldersTree2',
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
	 * @event writeemail
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 * @param {String[]} recipients
	 * @param {String} contactId
	 * @param {String} contactDisplayName
	 * @param {String} contactFullName
	 */
	
	/**
	 * @event callnumber
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 * @param {String} telephoneNumber
	 * @param {String} contactId
	 * @param {String} contactDisplayName
	 * @param {String} contactFullName
	 */
	
	/**
	 * @event writesms
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 * @param {String} telephoneNumber
	 * @param {String} contactId
	 * @param {String} contactDisplayName
	 * @param {String} contactFullName
	 */
	
	/**
	 * @event mapaddress
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 * @param {String} address
	 * @param {String} contactId
	 * @param {String} contactDisplayName
	 * @param {String} contactFullName
	 */
	
	/**
	 * @event clearselection
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 */
	
	/**
	 * @event openchat
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 */
	
	/**
	 * @event showmenu
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 * @param {Ext.event.Event} event
	 */
	
	/**
	 * @event showaudit
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 * @param {Boolean} isList
	 * @param {String} contactId
	 */
	
	/**
	 * @event print
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 */
	
	/**
	 * @event opencontact
	 * @param {Sonicle.webtop.contacts.ux.panel.ContactPreview} this
	 * @param {Boolean} isList
	 * @param {String} contactId
	 */
	
	layout: 'card',
	cls: 'wtcon-preview',
	referenceHolder: true,
	
	viewModel: true,
	modelName: 'Sonicle.webtop.contacts.model.ContactPreview',
	mys: null,
	
	constructor: function(cfg) {
		var me = this;
		me.config.viewModel = Ext.create('Ext.app.ViewModel');
		me.callParent([cfg]);
		
		Sonicle.VMUtils.applyFormulas(me.getVM(), {
			foHasPicture: WTF.foIsEqual('record', 'pic', true),
			foIsEditable: WTF.foGetFn('record', '_itPerms', function(val) {
				return WTA.util.FoldersTree2.toRightsObj(val).UPDATE;
			}),
			foHasTileData: {
				bind: {bindTo: '{record}'},
				get: function(val) {
					return val ? val.hasData() : true;
				}
			},
			foCategory: WTF.foMultiGetFn('record', ['catName', '_orDN'], function(v) {
				return Sonicle.webtop.contacts.Service.calcCategoryLabel(v['catName'], v['_orDN']);
			}),
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
			foHideOpenChat: WTF.foMultiGetFn(undefined, ['record.isList', 'foHasUser'], function(v) {
				return !!v['record.isList'] || !v['foHasUser'];
			}),
			foHasBusinessInfo: WTF.foIsEmpty('record', 'businessInfo', true),
			foHasNotes: WTF.foIsEmpty('record', 'notes', true),
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
		me.loadContactBuffered = Ext.Function.createBuffered(me.loadContact, 200);
	},
	
	initComponent: function() {
		var me = this;
		me.callParent(arguments);
		me.add([
			me.createEmptyItemCfg({itemId: 'empty'}),
			me.createContactItemCfg({itemId: 'contact'}),
			me.createMultiItemCfg({itemId: 'multi'})
		]);
		me.setActiveItem('empty');
		me.getViewModel().bind('{record._cfdefs}', me.onCFDefsUpdate, me);
	},
	
	getContacts: function() {
		return this.getViewModel().get('contacts');
	},
	
	setContacts: function(contacts) {
		var me = this,
			card = me;
		me.getViewModel().set('contacts', contacts);
		if (contacts && contacts.length === 1) {
			me.loadContactBuffered(contacts[0].getId(), 'contact'/*(contacts[0].get('isList') === true) ? 'list' : 'contact'*/);
		} else if (contacts && contacts.length > 1) {
			card.setActiveItem('multi');
		} else {
			me.loadContactBuffered(null, 'empty');
		}
	},
	
	isLoaded: function(id, idField) {
		var contacts = this.getContacts(), val;
		if (contacts && contacts.length === 1) {
			val = Ext.isEmpty(idField) ? contacts[0].getId() : contacts[0].get(idField);
			return id === val;
		} else {
			return false;
		}
	},
	
	loadContact: function(contactUid, /*private*/ activateCard) {
		var me = this;
		me.clearModel();
		if (contactUid) {
			me.loadModel({
				data: {uid: contactUid},
				dirty: false
			});
		}
		if (!Ext.isEmpty(activateCard)) {
			me.setActiveItem(activateCard);
		}
	},
	
	privates: {
		createEmptyItemCfg: function(cfg) {
			var me = this;
			return Ext.apply({
				xtype: 'container',
				layout: {
					type: 'vbox',
					pack: 'center',
					align: 'middle'
				},
				items: [
					{
						xtype: 'label',
						text: me.mys.res('contactPreview.no.tit'),
						cls: 'wt-pane-body-title wt-theme-text-color-title'
					}, {
						xtype: 'label',
						text: me.mys.res('contactPreview.no.txt'),
						cls: 'wt-pane-body-subtitle wt-theme-text-color-subtitle'
					}
				]
			}, cfg);
		},
		
		createContactActionsToolbarCfg: function(cfg) {
			var me = this;
			return Ext.apply({
				xtype: 'toolbar',
				border: false,
				items: Ext.Array.join(
					{
						xtype: 'button',
						ui: '{primary}',
						text: me.mys.res('contactPreview.single.contact.tb.open.lbl'),
						iconCls: 'wtcon-icon-openContact',
						handler: function() {
							var vm = me.getVM();
							me.fireEvent('opencontact', me, WTA.util.FoldersTree2.toRightsObj(vm.get('record._itPerms')).UPDATE, vm.get('record.isList'), vm.get('record.id'));
						}
					}, '-', {
						xtype: 'button',
						ui: '{tertiary}',
						bind: {
							hidden: '{!foHasEmails}',
							tooltip: '{foWriteMessageTip}'
						},
						tooltip: me.mys.res('contactPreview.single.tb.writeMessage.tip'),
						iconCls: 'wtcon-icon-writeMessage',
						hidden: true,
						handler: function() {
							var vm = me.getVM();
							me.fireEvent('writeemail', me, [vm.get('foMainEmail')], vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
						}
					},
					!WT.getVar('imEnabled') ? null : {
						xtype: 'button',
						ui: '{tertiary}',
						bind: {
							hidden: '{foHideOpenChat}',
							//hidden: '{record.isList}',
							//disabled: '{!foHasUser}',
							tooltip: '{foOpenChatTip}'
						},
						//text: me.mys.res('contactPreview.single.tb.openChat.lbl'),
						tooltip: me.mys.res('contactPreview.single.tb.openChat.tip'),
						iconCls: 'wt-icon-im-chat',
						handler: function() {
							me.fireEvent('openchat', me, null);
						}
					},
					!WT.getVar('pbxConfigured') ? null : {
						xtype: 'button',
						ui: '{tertiary}',
						bind: {
							hidden: '{record.isList}',
							disabled: '{!foHasTelephones}',
							tooltip: '{foCallNumberTip}'
						},
						//text: me.mys.res('contactPreview.single.tb.callNumber.lbl'),
						tooltip: me.mys.res('contactPreview.single.tb.callNumber.tip'),
						iconCls: 'wt-icon-call',
						handler: function() {
							var vm = me.getVM();
							me.fireEvent('callnumber', me, vm.get('foMainTelephone'), vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
						}
					},
					!WT.getVar('smsConfigured') ? null : {
						xtype: 'button',
						ui: '{tertiary}',
						bind: {
							hidden: '{record.isList}',
							disabled: '{!foHasMobile}',
							tooltip: '{foWriteSMSTip}'
						},
						//text: me.mys.res('contactPreview.single.tb.writeSms.lbl'),
						tooltip: me.mys.res('contactPreview.single.tb.writeSms.tip'),
						iconCls: 'wt-icon-sms',
						handler: function() {
							var vm = me.getVM();
							me.fireEvent('writesms', me, vm.get('foMobile'), vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
						}
					}
				)
			}, cfg);
		},
		
		createContactTopToolbarCfg: function(cfg) {
			var me = this;
			return Ext.apply({
				xtype: 'toolbar',
				border: false,
				items: [
					'->',
					{
						xtype: 'button',
						tooltip: WT.res('act-print.lbl'),
						iconCls: 'wt-icon-print',
						handler: function() {
							me.fireEvent('print', me);
						}
					}, {
						xtype: 'button',
						iconCls: 'wt-glyph-menu-kebab',
						arrowVisible: false,
						handler: function(s, e) {
							me.fireEvent('showmenu', me, e);
						}
					}
				]
			}, cfg);
		},
		
		createContactItemCfg: function(cfg) {
			var me = this;
			
			return Ext.apply({
				xtype: 'container',
				layout: 'vbox',
				defaults: {
					width: '100%'
				},
				items: [
					{
						xtype: 'wtpanel',
						cls: me.cls + '-header',
						layout: 'hbox',
						items: [
							{
								// picture/avatar item
								xtype: 'container',
								layout: 'anchor',
								items: [
									{
										xtype: 'sointialsavatarfield',
										bind: {
											value: '{record.avatarName}',
											hidden: '{record.pic}'
										},
										cls: me.cls + '-header-avatar',
										margin: 5,
										avatarSize: 64
									}, {
										xtype: 'sodisplayimage',
										bind: {
											value: '{record.pictureId}',
											hidden: '{!record.pic}'
										},
										hidden: true,
										imageWidth: 64,
										imageHeight: 64,
										geometry: 'circle',
										border: false,
										baseImageUrl: WTF.processBinUrl(me.mys.ID, 'GetContactPicture'),
										placeholderImageUrl: me.mys.resourceUrl('contact-placeholder.png'),
										cls: me.cls + '-header-picture'
									}
								]
							}, {
								xtype: 'container',
								layout: 'vbox',
								items: [
									{
										xtype: 'displayfield',
										bind: '{record.avatarName}',
										cls: me.cls + '-header-dn',
										flex: 1
									}, {
										xtype: 'fieldcontainer',
										layout: 'hbox',
										items: [
											{
												xtype: 'so-displayfield',
												bind: {
													value: '{record.businessInfo}',
													hidden: '{!foHasBusinessInfo}'
												},
												iconCls: 'fas fa-building',
												cls: me.cls + '-header-info',
												hidden: true
											}, {
												xtype: 'so-displayfield',
												bind: {
													hidden: '{!record.isList}'
												},
												iconCls: 'fas fa-user-group',
												cls: me.cls + '-header-info',
												value: me.mys.res('contactsList.tit'),
												hidden: true
											}, {
												xtype: 'so-displayfield',
												bind: {
													value: '{foCategory}',
													color: '{record.catColor}'
												},
												colorize: true,
												swatchGeometry: 'circle',
												cls: me.cls + '-header-category'
											}
										],
										flex: 1
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
										hideLabel: true,
										flex: 1
									}
								],
								flex: 1,
								margin: '10 0 0 20'
							}
						],
						dockedItems: [
							me.createContactTopToolbarCfg({dock: 'top'}),
							me.createContactActionsToolbarCfg({dock: 'bottom', cls: me.cls + '-header-tbactions'})
						]
					}, {
						xtype: 'tabpanel',
						bind: {
							hidden: '{record.isList}'
						},
						cls: me.cls + '-tab',
						border: false,
						items: [
							{
								xtype: 'wtpanel',
								title: me.mys.res('contactPreview.single.contact.tit'),
								scrollable: true,
								cls: me.cls + '-main',
								layout: 'vbox',
								defaults: {
									width: '100%'
								},
								items: [
									{
										xtype: 'container',
										cls: me.cls + '-tiles',
										bind: {
											hidden: '{!foHasTileData}'
										},
										hidden: true,
										layout: 'hbox',
										items: [
											me.createMailTileListCfg({
												reference: 'gpemaillist',
												flex: 1
											}),
											me.createTelTileListCfg({
												reference: 'gptelephonelist',
												flex: 1
											}),
											me.createMoreTileListCfg({
												reference: 'gpmorelist',
												flex: 1
											})
										]
									}, {
										xtype: 'container',
										cls: me.cls + '-tiles',
										bind: {
											hidden: '{foHasTileData}'
										},
										hidden: true,
										layout: {
											type: 'vbox',
											pack: 'center',
											align: 'middle'
										},
										items: [
											{
												xtype: 'label',
												text: me.mys.res('contactPreview.single.contact.emp'),
												cls: 'wt-form-body-subtitle wt-theme-text-color-subtitle'
											}
										]
									},
									me.createNoteFieldCfg({
										minHeight: 100,
										flex: 1
									})
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
								},
								serviceId: me.mys.ID,
								cls: me.cls + '-cfields'
							}
						],
						tabBar:	{
							items: [
								{
									xtype: 'tbfill'
								}, me.mys.hasAuditUI() ? {
									xtype: 'button',
									margin: '0 5 0 5',
									ui: 'default-toolbar',
									text: null,
									tooltip: WT.res('act-auditLog.lbl'),
									iconCls: 'fas fa-history',
									handler: function() {
										var vm = me.getVM();
										me.fireEvent('showaudit', me, vm.get('record.isList'), vm.get('record.id'));
									}
								} : null
							]
						},
						flex: 1
					}
				]
			}, cfg);
		},
		
		createMailTileListCfg: function(cfg) {
			var me = this;
			return Ext.apply({
				xtype: 'sotilelist',
				cls: me.cls + '-tilelist',
				linkifyValue: true,
				bind: {
					store: '{record.data1}',
					hidden: '{!foHasEmails}'
				},
				captionField: 'type',
				captionTexts: {
					work: me.mys.res('contactPreview.single.workEmail'),
					home: me.mys.res('contactPreview.single.homeEmail')
				},
				captionIcons: {
					work: 'fas fa-envelope',
					home: 'fas fa-envelope'
				},
				clipboardTooltipText: WT.res('sotilelist.clipboardTooltipText'),
				listeners: {
					cellvalueclick: function(s, val) {
						var vm = me.getVM();
						me.fireEvent('writeemail', me, [val], vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
					}
				}
			}, cfg);
		},
		
		createTelTileListCfg: function(cfg) {
			var me = this;
			return Ext.apply({
				xtype: 'sotilelist',
				cls: me.cls + '-tilelist',
				linkifyValue: true,
				bind: {
					store: '{record.data2}',
					hidden: '{!foHasTelephones}'
				},
				captionField: 'type',
				captionTexts: {
					mobile: me.mys.res('contactPreview.single.mobile'),
					work: me.mys.res('contactPreview.single.workTelephone'),
					home: me.mys.res('contactPreview.single.homeTelephone')
				},
				captionIcons: {
					mobile: 'fas fa-mobile-alt',
					work: 'fas fa-phone',
					home: 'fas fa-phone'
				},
				clipboardTooltipText: WT.res('sotilelist.clipboardTooltipText'),
				listeners: {
					cellvalueclick: function(s, val) {
						var vm = me.getVM();
						me.fireEvent('callnumber', me, val, vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
					}
				}
			}, cfg);
		},
		
		createMoreTileListCfg: function(cfg) {
			var me = this;
			return Ext.apply({
				xtype: 'sotilelist',
				cls: me.cls + '-tilelist',
				linkifyValue: true,
				bind: {
					store: '{record.data3}'
				},
				captionField: 'type',
				captionTexts: {
					fullName: me.mys.res('contactPreview.single.fullName'),
					company: me.mys.res('contactPreview.single.company'),
					workadd: me.mys.res('contactPreview.single.workAddress'),
					homeadd: me.mys.res('contactPreview.single.homeAddress')
				},
				captionIcons: {
					fullName: 'fas fa-signature',
					company: 'fas fa-building',
					workadd: 'fas fa-location-dot',
					homeadd: 'fas fa-location-dot'
				},
				clipboardTooltipText: WT.res('sotilelist.clipboardTooltipText'),
				listeners: {
					cellvalueclick: function(s, val, rec) {
						if (['workadd', 'homeadd'].indexOf(rec.get('type')) !== -1) {
							var vm = me.getVM();
							me.fireEvent('mapaddress', me, val, vm.get('record.id'), vm.get('record.avatarName'), vm.get('record.fullName'));
						}
					}
				}
			}, cfg);
		},
		
		createNoteFieldCfg: function(cfg) {
			return Ext.apply({
				xtype: 'textarea',
				bind: {
					value: '{record.notes}',
					hidden: '{!foHasNotes}'
				},
				hidden: true,
				cls: this.cls + '-notes',
				fieldLabel: this.mys.res('contactPreview.single.fld-notes.lbl'),
				labelAlign: 'top',
				readOnly: true
				//grow: true,
				//growMin: 60,
				//growMax: 200,
				//anchor: '100%'
			}, cfg);
		},
		
		createMultiItemCfg: function(cfg) {
			var me = this;
			return Ext.apply({
				xtype: 'container',
				layout: {
					type: 'vbox',
					pack: 'center',
					align: 'middle'
				},
				items: [
					{
						xtype: 'label',
						bind: {
							text: '{foMultiSelTitle}'
						},
						cls: 'wt-pane-body-title wt-theme-text-color-title'
					}, {
						xtype: 'toolbar',
						vertical: true,
						margin: '20 0 0 0',
						items: [
							{
								text: me.mys.res('contactPreview.multi.tb.writeMessage.lbl'),
								iconCls: 'wtcon-icon-writeMessage',
								handler: function() {
									var emails = me.extractContactsEmails(me.getViewModel().get('contacts'));
									me.fireEvent('writeemail', me, emails, null, null);
								}
							}
						]
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
						items: [
							{
								text: me.mys.res('contactPreview.multi.tb.cancel.lbl'),
								tooltip: me.mys.res('contactPreview.multi.tb.cancel.tip'),
								handler: function() {
									me.fireEvent('clearselection', me);
								}
							}
						]
					}
				]
			}, cfg);
		},
		
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
