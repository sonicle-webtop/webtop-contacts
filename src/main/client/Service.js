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


Ext.define('Sonicle.webtop.contacts.Service', {
	extend: 'WTA.sdk.Service',
	requires: [
		'Sonicle.grid.column.Icon',
		'Sonicle.grid.column.Color',
		'Sonicle.grid.column.Avatar',
		'WTA.ux.data.EmptyModel',
		'WTA.ux.data.SimpleModel',
		'WTA.ux.field.Search',
		'Sonicle.webtop.contacts.model.FolderNode',
		'Sonicle.webtop.contacts.model.GridContact',
		'Sonicle.webtop.contacts.store.View',
		'Sonicle.webtop.contacts.store.GroupBy',
		'Sonicle.webtop.contacts.store.ShowBy',
		//'Sonicle.webtop.contacts.ux.ScrollTooltip',
		'Sonicle.webtop.contacts.ux.panel.ContactPreview'
	],
	uses: [
		'Sonicle.webtop.contacts.view.Map',
		'Sonicle.webtop.contacts.view.Sharing',
		'Sonicle.webtop.contacts.view.Category',
		'Sonicle.webtop.contacts.view.CategoryLinks',
		'Sonicle.webtop.contacts.view.Contact',
		'Sonicle.webtop.contacts.view.ContactsList',
		'Sonicle.webtop.contacts.view.CategoryChooser',
		'Sonicle.webtop.contacts.view.ListChooser',
		'Sonicle.webtop.contacts.view.HiddenCategories'
	],
	mixins: [
		'WTA.mixin.FoldersTree'
	],
	
	activeView: 'work',
	activeGroupBy: 'alpha',
	
	needsReload: true,
	api: null,
	
	getApiInstance: function() {
		var me = this;
		if (!me.api) me.api = Ext.create('Sonicle.webtop.contacts.ServiceApi', {service: me});
		return me.api;
	},
	
	init: function() {
		var me = this;
		
		me.activeView = me.getVar('view');
		Sonicle.webtop.contacts.model.GridContact.setOrderField((me.getVar('showBy') === 'firstName' ? 'firstName' : 'lastName'));
		
		me.initActions();
		me.initCxm();
		
		me.on('activate', me.onActivate, me);
		me.onMessage('remoteSyncResult', function(msg) {
			var pl = msg.payload,
					ok = (pl.success === true),
					tag = me.self.noTagRemoteSync(pl.categoryId),
					title = pl.categoryName;
			if (pl.start === true) {
				WT.showDesktopNotification(me.ID, {
					tag: tag,
					title: title,
					body: me.res('not.category.rsync.start.body')
				});
			} else {
				WT.showNotification(me.ID, false, {
					tag: tag,
					title: title,
					//iconCls: me.cssIconCls('im-chat', 'm'),
					body: ok ? me.res('not.category.rsync.end.body.ok') : me.res('not.category.rsync.end.body.err', pl.message),
					data: {
						categoryId: pl.categoryId
					}
				}/*, {callbackService: ok}*/);
				if (ok) me.reloadContacts();
			}
		});
		
		me.setToolbar(Ext.create({
			xtype: 'toolbar',
			referenceHolder: true,			
			items: [
				'-',
				me.getAct('refresh'),
				me.getAct('printAddressbook'),
				'->',
				{
					xtype: 'wtsearchfield',
					reference: 'fldsearch',
					tooltip: me.res('fld-search.tip'),
					listeners: {
						query: function(s, value) {
							me.queryContacts(value);
						}
					}
				},
				'->'
			]
		}));
		
		me.setToolComponent(Ext.create({
			xtype: 'panel',
			layout: 'border',
			referenceHolder: true,
			title: me.getName(),
			items: [{
				region: 'center',
				xtype: 'treepanel',
				reference: 'trfolders',
				border: false,
				useArrows: true,
				rootVisible: false,
				store: {
					autoLoad: true,
					autoSync: true,
					model: 'Sonicle.webtop.contacts.model.FolderNode',
					proxy: WTF.apiProxy(me.ID, 'ManageFoldersTree', 'children', {
						writer: {
							allowSingle: false // Always wraps records into an array
						}
					}),
					root: {
						id: 'root',
						expanded: true
					},
					listeners: {
						write: function(s,op) {
							me.reloadContacts();
						}
					}
				},
				hideHeaders: true,
				listeners: {
					checkchange: function(n, ck) {
						me.showHideFolder(n, ck);
					},
					itemcontextmenu: function(vw, rec, itm, i, e) {
						if(rec.get('_type') === 'root') {
							WT.showContextMenu(e, me.getRef('cxmRootFolder'), {folder: rec});
						} else {
							WT.showContextMenu(e, me.getRef('cxmFolder'), {folder: rec});
						}
					}
				}
			}]
		}));
		
		me.setMainComponent(Ext.create({
			xtype: 'container',
			layout: 'border',
			referenceHolder: true,
			items: [{
				region: 'center',
				xtype: 'gridpanel',
				reference: 'gpcontacts',
				cls: 'wtcon-gpcontacts',
				store: {
					type: 'buffered',
					model: 'Sonicle.webtop.contacts.model.GridContact',
					proxy: WTF.apiProxy(me.ID, 'ManageGridContacts', 'data', {
						extraParams: {
							view: null,
							groupBy: null,
							showBy: null,
							query: null
						}
					}),
					pageSize: 50,
					leadingBufferZone: 50,
					trailingBufferZone: 50,
					listeners: {
						beforeload: function(s) {
							WTU.applyExtraParams(s, {
								view: me.activeView,
								groupBy: me.activeGroupBy,
								showBy: me.getVar('showBy')
							});
						},
						load: function(s) {
							/*
							var rec = me.getSelectedContact(), cmp;
							if (rec && (s.getById(rec.getId()) === null)) { // Record is selected but no more existent in store
								// I don't know if it is a ExtJs bug, anyway make sure to clear the preview panel and refresh actions affected by strange selections
								cmp = me.pnlPreview();
								if (cmp.isLoaded(rec.getId())) cmp.setContacts(null);
								//me.updateDisabled('deleteContact');
							}
							*/
							// TODO: add lastupdate to model to check if the contact has been updated and refresh preview accordingly (this can reduce openContact code)
						}
					}
				},
				selModel: {
					type: 'rowmodel',
					mode : 'MULTI'
				},
				viewConfig: {
					deferEmptyText: false,
					emptyText: WT.res('grid.emp')
				},
				columns: [{
					dataIndex: 'id',
					hidden: true,
					sortable: false,
					groupable: false,
					text: 'ID',
					width: 80
				}, {
					xtype: 'socolorcolumn',
					dataIndex: 'catId',
					hidden: true,
					sortable: false,
					groupable: false,
					colorField: 'catColor',
					displayField: 'catName',
					text: me.res('gpcontacts.category.lbl'),
					width: 80
				}, {
					xtype: 'soavatarcolumn',
					dataIndex: 'id',
					sortable: false,
					groupable: false,
					nameField: 'fullName',
					getPictureUrl: function(v, rec) {
						return (rec.get('pic') === true) ? Ext.String.urlAppend(WTF.processBinUrl(me.ID, 'GetContactPicture'), Ext.Object.toQueryString({id: v})) : null;
					},
					/*
					getIconCls: function(v,rec) {
						return (rec.get('isList') === true) ? me.cssIconCls('contactsList') : null;
					},
					*/
					width: 60
				}, {
					xtype: 'templatecolumn',
					sortable: false,
					groupable: false,
					text: me.res('gpcontacts.contacts.lbl'),
					flex: 2,
					tpl: [
						'<div>{fullName}<span class="wtcon-cell-caption" style="padding-left:10px;">{[this.getCompany(values)]}</span></div>',
						'<div class="wtcon-cell-caption">{[this.getRefs(values)]}</div>',
						{
							getCompany: function(values) {
								return (me.activeGroupBy === 'company') ? '' : this.value(values['company']);
							},
							getRefs: function(values) {
								var phone = false,
										args = ['&nbsp;&nbsp;'], s;
								
								if (values['isList'] === true) {
									args.push(Ext.String.htmlEncode(me.res('contactsList.tit')));
								} else {
									s = values['email'];
									if (!Ext.isEmpty(s)) {
										args.push('<i class="fa fa-envelope-o" aria-hidden="true"></i>&nbsp;' + this.value(s));
									}
									s = values['mobile'];
									if (!Ext.isEmpty(s)) {
										args.push('<i class="fa fa-mobile" aria-hidden="true"></i>&nbsp;' + this.value(s));
										phone = true;
									}
									s = values['telephone'];
									if (!phone && !Ext.isEmpty(s)) {
										args.push('<i class="fa fa-phone" aria-hidden="true"></i>&nbsp;' + this.value(s));
									}
								}	
								return Sonicle.String.join.apply(null, args);
							},
							value: function(s) {
								return !Ext.isEmpty(s) ? Ext.String.htmlEncode(s) : '';
							}
						}
					]
				}],
				features: [{
					id: 'grouping',
					ftype: 'grouping',
					groupHeaderTpl: [
						'<span class="{[this.getSpanCls(values)]}">{name}</span>',
						{
							getSpanCls: function(values) {
								return 'wt-theme-text-tit' + ((values.groupField.indexOf('letter') !== -1) ? ' wtcon-group-letter' : '');
							}
						}
					]
				}/*, {
					id: 'scrolltooltip',
					ftype: 'wtscrolltooltip'
				}*/],
				tbar: [
					'->',
					WTF.lookupCombo('id', 'desc', {
						store: {
							type: 'wtcongroupby',
							autoLoad: true
						},
						value: me.activeGroupBy,
						listeners: {
							select: function(s, rec) {
								me.applyGroupBy(rec.getId());
							}
						},
						fieldLabel: me.res('fld-group.lbl'),
						labelWidth: 70,
						width: 180
					}),
					WTF.lookupCombo('id', 'desc', {
						store: {
							type: 'wtconview',
							autoLoad: true
						},
						value: me.activeView,
						listeners: {
							select: function(s, rec) {
								me.reloadContacts({view: rec.getId()});
							}
						},
						width: 130
					}),
					'-',
					me.getAct('deleteContact2')
				],
				listeners: {
					selectionchange: function(s) {
						me.updateDisabled('showContact');
						me.updateDisabled('printContact');
						me.updateDisabled('copyContact');
						me.updateDisabled('moveContact');
						me.updateDisabled('deleteContact');
						me.updateDisabled('addContactsListFromSel');
						me.updateDisabled('addToContactsListFromSel');
						me.updateDisabled('callTelephone');
						me.updateDisabled('callMobile');
						me.updateDisabled('sendSms');
						me.pnlPreview().setContacts(s.getSelection());
					},
					rowdblclick: function(s, rec) {
						var er = me.toRightsObj(rec.get('_erights'));
						me.openContactItemUI(er.UPDATE, rec);
					},
					rowcontextmenu: function(s, rec, itm, i, e) {
						WT.showContextMenu(e, me.getRef('cxmGrid'), {
							task: rec,
							tasks: s.getSelection()
						});
					},
					rowkeydown: function(s, rec, itm, indx, e) {
						if (e.getKey() === e.DELETE) {
							me.getAct('deleteContact').execute();
						}
					}
				}
			}, {
				region: 'east',
				xtype: 'wtconcontactpreviewpanel',
				reference: 'pnlpreview',
				split: true,
				hidden: !WT.plTags.desktop,
				mys: me,
				listeners: {
					writeemail: function(s, rcpts) {
						WT.handleNewMailMessage(rcpts);
					},
					callnumber: function(s, number) {
						WT.handlePbxCall(number);
					},
					writesms: function(s, number, id, fn) {
						WT.handleSendSMS(fn, number, null);
					},
					mapaddress: function(s, address) {
						WT.openGoogleMaps({query: address});
					},
					clearselection: function(s) {
						me.gpContacts().getSelectionModel().deselectAll();
					}
				},
				width: '60%',
				minWidth: 600
			}]
		}));
	},
	
	notificationCallback: function(type, tag, data) {
		var me = this;
		if (Ext.String.startsWith(tag, me.self.NOTAG_REMOTESYNC)) {
			me.reloadContacts();
		}
	},
	
	fldSearch: function() {
		return this.getToolbar().lookupReference('fldsearch');
	},
	
	trFolders: function() {
		return this.getToolComponent().lookupReference('trfolders');
	},
	
	gpContacts: function() {
		return this.getMainComponent().lookupReference('gpcontacts');
	},
	
	pnlPreview: function() {
		return this.getMainComponent().lookupReference('pnlpreview');
	},
	
	tbIndex: function() {
		return this.getMainComponent().lookupReference('tbindex');
	},
	
	initActions: function() {
		var me = this,
				hdscale = WT.getHeaderScale();
		
		me.addAct('new', 'newContact', {
			ignoreSize: true,
			handler: function() {
				me.getAct('addContact').execute();
			}
		});
		me.addAct('new', 'newContactsList', {
			ignoreSize: true,
			handler: function() {
				me.getAct('addContactsList').execute();
			}
		});
		
		me.addAct('editSharing', {
			text: WT.res('sharing.tit'),
			tooltip: null,
			iconCls: 'wt-icon-sharing',
			handler: function() {
				var node = me.getSelectedNode(me.trFolders());
				if (node) me.editShare(node.getId());
			}
		});
		me.addAct('manageHiddenCategories', {
			tooltip: null,
			handler: function() {
				var node = me.getSelectedRootFolder(me.trFolders());
				if (node) me.manageHiddenCategoriesUI(node);
			}
		});
		me.addAct('hideCategory', {
			tooltip: null,
			handler: function() {
				var node = me.getSelectedNode(me.trFolders());
				if (node) me.hideCategoryUI(node);
			}
		});
		me.addAct('addCategory', {
			ignoreSize: true,
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.addCategoryUI(node.get('_domainId'), node.get('_userId'));
			}
		});
		me.addAct('addRemoteCategory', {
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.addRemoteCategoryUI(node.get('_pid'));
			}
		});
		me.addAct('viewCategoryLinks', {
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.viewCategoryLinks(node.get('_catId'));
			}
		});
		me.addAct('editCategory', {
			ignoreSize: true,
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.editCategoryUI(node.get('_catId'));
			}
		});
		me.addAct('deleteCategory', {
			ignoreSize: true,
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.deleteCategoryUI(node);
			}
		});
		me.addAct('syncRemoteCategory', {
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.syncRemoteCategoryUI(node.get('_catId'));
			}
		});
		me.addAct('importContacts', {
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.importContactsUI(node.get('_catId'));
			}
		});
		me.addAct('categoryColor', {
			text: me.res('mni-categoryColor.lbl'),
			tooltip: null,
			menu: {
				showSeparator: false,
				itemId: 'categoryColor',
				items: [{
						xtype: 'colorpicker',
						colors: WT.getColorPalette(),
						listeners: {
							select: function(s, color) {
								var node = me.getSelectedFolder(me.trFolders());
								me.getRef('cxmFolder').hide();
								if (node) me.updateCategoryPropColorUI(node, '#'+color);
							}
						}
					},
					'-',
					me.addAct('restoreCategoryColor', {
						tooltip: null,
						handler: function() {
							var node = me.getSelectedFolder(me.trFolders());
							if (node) me.updateCategoryPropColorUI(node, null);
						}
					})
				]
			}
		});
		var onItemClick = function(s) {
			var node = me.getSelectedFolder(me.trFolders());
			if (node && s.checked) me.updateCategoryPropSyncUI(node, s.getItemId());
		};
		me.addAct('categorySync', {
			text: me.res('mni-categorySync.lbl'),
			tooltip: null,
			menu: {
				itemId: 'categorySync',
				items: [{
						itemId: 'O',
						text: me.res('store.sync.O'),
						group: 'categorySync',
						checked: false,
						listeners: {
							click: onItemClick
						}
					}, {
						itemId: 'R',
						text: me.res('store.sync.R'),
						group: 'categorySync',
						checked: false,
						listeners: {
							click: onItemClick
						}
					}, {
						itemId: 'W',
						text: me.res('store.sync.W'),
						group: 'categorySync',
						checked: false,
						listeners: {
							click: onItemClick
						}
					}
				]
			}
		});
		me.addAct('viewThisFolderOnly', {
			tooltip: null,
			iconCls: 'wt-icon-select-one',
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if(node) me.showOneF3FolderOnly(me.getSelectedRootFolder(me.trFolders()), node.getId());
			}
		});
		me.addAct('viewAllFolders', {
			tooltip: null,
			iconCls: 'wt-icon-select-all',
			handler: function() {
				var node = me.getSelectedRootFolder(me.trFolders());
				if (node) {
					if (node.isLoaded()) {
						me.showHideAllF3Folders(node, true);
					} else {
						me.updateCheckedFoldersUI(node, true);
					}
				}
			}
		});
		me.addAct('viewNoneFolders', {
			tooltip: null,
			iconCls: 'wt-icon-select-none',
			handler: function() {
				var node = me.getSelectedRootFolder(me.trFolders());
				if (node) {
					if (node.isLoaded()) {
						me.showHideAllF3Folders(node, false);
					} else {
						me.updateCheckedFoldersUI(node, false);
					}
				}
			}
		});
		me.addAct('showContact', {
			text: WT.res('act-open.lbl'),
			tooltip: null,
			handler: function() {
				var rec = me.getSelectedContact(), er;
				if (rec) {
					er = me.toRightsObj(rec.get('_erights'));
					me.openContactItemUI(er.UPDATE, rec);
				}
			}
		});
		me.addAct('addContact', {
			ignoreSize: true,
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.addContactUI(node.get('_pid'), node.get('_catId'));
			}
		});
		me.addAct('addContactsList', {
			ignoreSize: true,
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders());
				if (node) me.addContactsListUI(node.get('_pid'), node.get('_catId'));
			}
		});
		me.addAct('sendContact', {
			tooltip: null,
			handler: function() {
				var sel = me.getSelectedContacts();
				if (sel.length > 0) me.sendContactsAsEmail(me.extractIds(sel, 'id'));
			}
		});
		me.addAct('addContactsListFromSel', {
			tooltip: null,
			handler: function() {
				var node = me.getSelectedFolder(me.trFolders()),
						er = me.toRightsObj(node.get('_erights')),
						rcpts = me.toRcpts(me.getSelectedContacts());
				
				if (er.CREATE) {
					me.addContactsListUI(node.get('_pid'), node.get('_catId'), rcpts);
				} else {
					me.addContactsListUI(WT.getVar('profileId'), null, rcpts);
				}
			}
		});
		me.addAct('addToContactsListFromSel', {
			tooltip: null,
			handler: function() {
				var sel = me.getSelectedContacts();
				if (sel.length > 0) me.addAsContactsListRecipientUI(sel);
			}
		});
		me.addAct('deleteContact', {
			text: WT.res('act-delete.lbl'),
			tooltip: null,
			iconCls: 'wt-icon-delete',
			handler: function() {
				var sel = me.getSelectedContacts();
				if (sel.length > 0) me.deleteContactItemsUI(sel);
			}
		});
		me.addAct('deleteContact2', {
			text: null,
			tooltip: WT.res('act-delete.lbl'),
			iconCls: 'wt-icon-delete',
			disabled: true,
			handler: function() {
				me.getAct('deleteContact').execute();
			}
		});
		me.addAct('copyContact', {
			tooltip: null,
			handler: function() {
				var sel = me.getSelectedContacts();
				if (sel.length > 0) me.moveContactItemsUI(true, sel);
			}
		});
		me.addAct('moveContact', {
			tooltip: null,
			handler: function() {
				var sel = me.getSelectedContacts();
				if (sel.length > 0) me.moveContactItemsUI(false, sel);
			}
		});
		me.addAct('printContact', {
			text: WT.res('act-print.lbl'),
			tooltip: null,
			iconCls: 'wt-icon-print',
			handler: function() {
				var sel = me.getSelectedContacts();
				if (sel.length > 0) me.printContactsDetail(me.extractIds(sel, 'id'));
			}
		});
		me.addAct('callTelephone', {
			tooltip: null,
			handler: function() {
				var rec = me.getSelectedContact();
				if (rec) WT.handlePbxCall(rec.get('telephone'));
			}
		});
		me.addAct('callMobile', {
			tooltip: null,
			handler: function() {
				var rec = me.getSelectedContact();
				if (rec) WT.handlePbxCall(rec.get('mobile'));
			}
		});
		me.addAct('sendSms', {
			tooltip: null,
			handler: function() {
				var rec = me.getSelectedContact();
				if (rec) WT.handleSendSMS(rec.get('fullName'), rec.get('mobile'), null);
			}
		});
		
		me.addAct('refresh', {
			scale: hdscale,
			text: '',
			tooltip: WT.res('act-refresh.lbl'),
			iconCls: 'wt-icon-refresh',
			handler: function() {
				me.reloadContacts();
			}
		});
		me.addAct('printAddressbook', {
			scale: hdscale,
			text: null,
			tooltip: WT.res('act-print.lbl'),
			iconCls: 'wt-icon-print',
			handler: function() {
				var params = Ext.clone(me.gpContacts().getStore().getProxy().getExtraParams());
				delete params.action;
				var url = WTF.processBinUrl(me.ID, 'PrintAddressbook', params);
				Sonicle.URLMgr.openFile(url, {filename: 'addressbook', newWindow: true});
			}
		});
		/*
		me.addAct('addContact2', {
			scale: hdscale,
			text: null,
			tooltip: me.res('act-addContact.lbl'),
			iconCls: me.cssIconCls('addContact'),
			handler: function() {
				me.getAct('addContact').execute();
			}
		});
		*/
		me.addAct('addContactsList2', {
			scale: hdscale,
			text: null,
			tooltip: me.res('act-addContactsList.lbl'),
			iconCls: me.cssIconCls('addContactsList'),
			handler: function() {
				me.getAct('addContactsList').execute();
			}
		});
	},
	
	initCxm: function() {
		var me = this;
		
		me.addRef('cxmRootFolder', Ext.create({
			xtype: 'menu',
			items: [
				me.getAct('addCategory'),
				me.getAct('addRemoteCategory'),
				'-',
				{
					text: me.res('mni-viewFolders.lbl'),
					menu: {
						items: [
							me.getAct('viewAllFolders'),
							me.getAct('viewNoneFolders')
						]
					}
				},
				'-',
				me.getAct('editSharing'),
				me.getAct('manageHiddenCategories')
				//TODO: azioni altri servizi?
			],
			listeners: {
				beforeshow: function(s) {
					var rec = s.menuData.folder,
							mine = Ext.String.startsWith(rec.getId(), '0'),
							rr = me.toRightsObj(rec.get('_rrights'));
					me.getAct('addCategory').setDisabled(!rr.MANAGE);
					me.getAct('addRemoteCategory').setDisabled(!rr.MANAGE);
					me.getAct('editSharing').setDisabled(!rr.MANAGE);
					me.getAct('manageHiddenCategories').setDisabled(mine);
				}
			}
		}));
		
		me.addRef('cxmFolder', Ext.create({
			xtype: 'menu',
			items: [
				me.getAct('editCategory'),
				me.getAct('deleteCategory'),
				me.getAct('addCategory'),
				me.getAct('addRemoteCategory'),
				'-',
				{
					text: me.res('mni-viewFolder.lbl'),
					menu: {
						items: [
							me.getAct('viewThisFolderOnly'),
							me.getAct('viewAllFolders'),
							me.getAct('viewNoneFolders')
						]
					}
				},
				'-',
				me.getAct('editSharing'),
				me.getAct('viewCategoryLinks'),
				{
					text: me.res('mni-customizeFolder.lbl'),
					menu: {
						items: [
							me.getAct('hideCategory'),
							me.getAct('categoryColor'),
							me.getAct('categorySync')
						]
					}
				},
				me.getAct('syncRemoteCategory'),
				'-',
				me.getAct('addContact'),
				me.getAct('addContactsList'),
				me.getAct('importContacts')
				//TODO: azioni altri servizi?
			],
			listeners: {
				beforeshow: function(s) {
					var rec = s.menuData.folder,
							mine = Ext.String.startsWith(rec.getId(), '0'),
							rr = me.toRightsObj(rec.get('_rrights')),
							fr = me.toRightsObj(rec.get('_frights')),
							er = me.toRightsObj(rec.get('_erights'));
					me.getAct('editCategory').setDisabled(!fr.UPDATE);
					me.getAct('deleteCategory').setDisabled(!fr.DELETE || rec.get('_builtIn'));
					me.getAct('addCategory').setDisabled(!rr.MANAGE);
					me.getAct('addRemoteCategory').setDisabled(!rr.MANAGE);
					me.getAct('editSharing').setDisabled(!rr.MANAGE);
					me.getAct('addContact').setDisabled(!er.CREATE);
					me.getAct('addContactsList').setDisabled(!er.CREATE);
					me.getAct('importContacts').setDisabled(!er.CREATE);
					me.getAct('hideCategory').setDisabled(mine);
					me.getAct('categoryColor').setDisabled(mine);
					me.getAct('categorySync').setDisabled(mine);
					me.getAct('syncRemoteCategory').setDisabled(!Sonicle.webtop.contacts.view.Category.isRemote(rec.get('_provider')));
					if (!mine) {
						s.down('menu#categoryColor').down('colorpicker').select(rec.get('_color'), true);
						s.down('menu#categorySync').getComponent(rec.get('_sync')).setChecked(true);
					}
				}
			}
		}));
		
		me.addRef('cxmGrid', Ext.create({
			xtype: 'menu',
			items: [
				me.getAct('showContact'),
				{
					text: me.res('mni-copyormove.lbl'),
					menu: {
						items: [
							me.getAct('moveContact'),
							me.getAct('copyContact')
						]
					}
				},
				me.getAct('printContact'),
				'-',
				me.getAct('deleteContact'),
				'-',
				me.getAct('addContactsListFromSel'),
				me.getAct('addToContactsListFromSel'),
				'-',
				{
					text: WT.res('act-call.lbl'),
					iconCls: 'wt-icon-call',
					menu: {
						items: [
							me.getAct('callTelephone'),
							me.getAct('callMobile')
						]
					}
				},
				me.getAct('sendSms'),
				'-',
				me.getAct('sendContact')
			]
		}));
	},
	
	onActivate: function() {
		var me = this;
		
		if (me.needsReload) {
			me.needsReload = false;
			me.reloadContacts();
		}
		
		me.updateDisabled('showContact');
		me.updateDisabled('printContact');
		me.updateDisabled('copyContact');
		me.updateDisabled('moveContact');
		me.updateDisabled('deleteContact');
		me.updateDisabled('addContactsListFromSel');
		me.updateDisabled('addToContactsListFromSel');
		me.updateDisabled('callTelephone');
		me.updateDisabled('callMobile');
		me.updateDisabled('sendSms');
	},
	
	loadRootNode: function(pid, reloadItemsIf) {
		var me = this,
				sto = me.trFolders().getStore(),
				node;
		
		node = sto.findNode('_pid', pid, false);
		if (node) {
			sto.load({node: node});
			if (reloadItemsIf && node.get('checked')) me.reloadContacts();
		}
	},
	
	
	

	
	calcGroupField: function(groupBy) {
		if (groupBy === 'company') {
			return 'company';
		} else {
			return 'letter';
		}
	},
	
	applyGroupBy: function(groupBy, reload) {
		var me = this;
		me.activeGroupBy = groupBy;
		if (reload !== false) {
			me.gpContacts().getStore().setGroupField(me.calcGroupField(groupBy));
		}	
	},
	
	reloadContacts: function(opts) {
		opts = opts || {};
		var me = this, sto, pars = {};
		// NB: opts.query is not persisted if service is not active!
		if (Ext.isString(opts.groupBy)) me.activeGroupBy = opts.groupBy;
		if (Ext.isString(opts.view)) me.activeView = opts.view;
		if (me.isActive()) {
			sto = me.gpContacts().getStore();
			if (!sto.getGroupField()) {
				sto.setGroupField(me.calcGroupField(me.activeGroupBy));
			} else {
				if (opts.query !== undefined) Ext.apply(pars, {query: opts.query});
				WTU.loadWithExtraParams(sto, pars);
			}
		} else {
			me.needsReload = true;
		}
	},
	
	queryContacts: function(txt) {
		this.reloadContacts({query: txt});
	},
	
	getSelectedContacts: function() {
		return this.gpContacts().getSelection();
	},
	
	getSelectedContact: function(forceSingle) {
		if (forceSingle === undefined) forceSingle = true;
		var sel = this.getSelectedContacts();
		if (forceSingle && sel.length !== 1) return null;
		return (sel.length > 0) ? sel[0] : null;
	},
	
	addCategoryUI: function(domainId, userId) {
		var me = this;
		me.addCategory(domainId, userId, {
			callback: function(success, model) {
				if (success) me.loadRootNode(model.get('_profileId'));
			}
		});
	},
	
	addRemoteCategoryUI: function(profileId) {
		var me = this;
		me.setupRemoteCategory(profileId, {
			callback: function(success, mo) {
				if (success) me.loadRootNode(profileId);
			}
		});
	},
	
	editCategoryUI: function(categoryId) {
		var me = this;
		me.editCategory(categoryId, {
			callback: function(success, model) {
				if (success) me.loadRootNode(model.get('_profileId'), true);
			}
		});
	},
	
	deleteCategoryUI: function(node) {
		WT.confirm(this.res('category.confirm.delete', Ext.String.ellipsis(node.get('text'), 40)), function(bid) {
			if (bid === 'yes') node.drop();
		}, this);
	},
	
	syncRemoteCategoryUI: function(categoryId) {
		var me = this;
		WT.confirm(this.res('category.confirm.remotesync'), function(bid) {
			if (bid === 'yes') {
				me.syncRemoteCategory(categoryId, false, {
					callback: function(success, json) {
						if (!success) WT.error(json.message);
					}
				});
			}
		}, this);
	},
	
	manageHiddenCategoriesUI: function(node) {
		var me = this,
				vw = me.createHiddenCategories(node.getId());
		
		vw.on('viewcallback', function(s, success, json) {
			if (success) {
				Ext.iterate(json.data, function(pid) {
					me.loadRootNode(pid);
				});
			}
		});
		vw.showView();
	},
	
	hideCategoryUI: function(node) {
		var me = this;
		WT.confirm(this.res('category.confirm.hide', Ext.String.ellipsis(node.get('text'), 40)), function(bid) {
			if(bid === 'yes') {
				me.updateCategoryVisibility(node.get('_catId'), true, {
					callback: function(success) {
						if(success) {
							me.loadRootNode(node.get('_pid'));
							me.showHideF3Node(node, false);
						}
					}
				});
			}
		}, this);
	},
	
	updateCategoryPropColorUI: function(node, color) {
		var me = this;
		me.updateCategoryPropColor(node.get('_catId'), color, {
			callback: function(success) {
				if (success) {
					me.loadRootNode(node.get('_pid'));
					if (node.get('_visible')) me.reloadContacts();
				}
			}
		});
	},
	
	updateCategoryPropSyncUI: function(node, sync) {
		var me = this;
		me.updateCategoryPropSync(node.get('_catId'), sync, {
			callback: function(success) {
				if (success) {
					me.loadRootNode(node.get('_pid'));
				}
			}
		});
	},
	
	updateCheckedFoldersUI: function(node, checked) {
		var me = this;
		me.updateCheckedFolders(node.getId(), checked, {
			callback: function(success) {
				if(success) {
					if (node.get('_visible')) {
						me.reloadContacts();
					} else {
						if (checked) me.showHideF3Node(node, checked);
					}
				}
			}
		});
	},
	
	openContactItemUI: function(edit, rec) {
		var me = this,
				id = rec.get('id'),
				isList = rec.get('isList') === true,
				sel;
		if (isList) {
			me.openContactsList(edit, id, {
				callback: function(success, mo) {
					if (success && edit) {
						me.reloadContacts();
						sel = me.getSelectedContacts();
						if ((sel.length === 1) && (sel[0].get('id') === mo.getId())) {
							me.pnlPreview().setContacts(sel);
						}
					}
				}
			});
		} else {
			me.openContact(edit, id, {
				callback: function(success, mo) {
					if (success && edit) {
						me.reloadContacts();
						sel = me.getSelectedContacts();
						if ((sel.length === 1) && (sel[0].get('id') === mo.getId())) {
							me.pnlPreview().setContacts(sel);
						}
					}
				}
			});
		}
	},
	
	deleteContactItemsUI: function(recs) {
		var me = this,
			ids = me.extractIds(recs),
			msg;
		
		if (recs.length === 1) {
			msg = me.res('contact.confirm.delete', Ext.String.ellipsis(recs[0].get('fullName'), 40));
		} else {
			msg = me.res('gpcontacts.confirm.delete.selection');
		}
		WT.confirm(msg, function(bid) {
			if (bid === 'yes') {
				me.deleteContactsItems(ids, {
					callback: function(success) {
						if (success) me.reloadContacts();
					}
				});
			}
		});
	},
	
	moveContactItemsUI: function(copy, recs) {
		var me = this,
			ids = me.extractIds(recs),
			vw = me.createCategoryChooser(copy);
		
		vw.on('viewok', function(s, categoryId) {
			me.gpContacts().setLoading(true);
			me.moveContactsItems(copy, ids, categoryId, {
				callback: function(success) {
					me.gpContacts().setLoading(false);
					if (success) me.reloadContacts();
				}
			});
		});
		vw.showView();
	},
	
	addContactUI: function(ownerId, categoryId) {
		var me = this;
		me.addContact(ownerId, categoryId, {
			callback: function(success) {
				if (success) me.reloadContacts();
			}
		});
	},
	
	addContactsListUI: function(ownerId, categoryId, recipients) {
		var me = this;
		me.addContactsList(ownerId, categoryId, recipients, {
			callback: function(success) {
				if (success) me.reloadContacts();
			}
		});
	},
	
	addAsContactsListRecipientUI: function(recs) {
		var me = this,
				vw = me.createContactListChooser(null);
		
		vw.on('viewok', function(s, listAddress, rcptType) {
			me.addAsContactsListRecipient(listAddress, rcptType, me.toEmails(recs));
			/*
			WT.ajaxReq(me.ID, 'AddToContactsList', {
				params: {
					list: list,
					recipientType: recipientType,
					emails: emails
				}
			});
			*/
		});
		vw.showView();
	},
	
	addToContactsListUI: function(emails) {
		var me = this,
				vw = me.createContactListChooser('');
		
		vw.on('viewok', function(s, list, recipientType) {
			WT.ajaxReq(me.ID, 'AddToContactsList', {
				params: {
					list: list,
					recipientType: recipientType,
					emails: emails
				}
			});
		});
		vw.showView();
	},
	
	importContactsUI: function(categoryId) {
		var me = this;
		me.importContacts(categoryId, {
			callback: function(success) {
				if (success) me.reloadContacts();
			}
		});
	},
	
	editShare: function(id) {
		var me = this,
				vw = WT.createView(me.ID, 'view.Sharing', {swapReturn: true});
		
		vw.showView(function(s) {
			vw.begin('edit', {
				data: {
					id: id
				}
			});
		});
	},
	
	addCategory: function(domainId, userId, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.Category', {swapReturn: true});
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('new', {
				data: {
					domainId: domainId,
					userId: userId,
					sync: me.getVar('defaultCategorySync')
				}
			});
		});
	},
	
	setupRemoteCategory: function(profileId, opts) {
		opts = opts || {};
		var me = this,
			vw = WT.createView(me.ID, 'view.CategoryRemoteWiz', {
				swapReturn: true,
				viewCfg: {
					data: {
						profileId: profileId
					}
				}
			});
		
		vw.on('viewclose', function(s) {
			Ext.callback(opts.callback, opts.scope || me, [true, s.getVMData()]);
		});
		vw.showView();
	},
	
	viewCategoryLinks: function(categoryId, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.CategoryLinks', {swapReturn: true});
		
		vw.on('viewclose', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('view', {
				data: {
					categoryId: categoryId
				}
			});
		});
	},
	
	editCategory: function(categoryId, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.Category', {swapReturn: true});
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('edit', {
				data: {
					categoryId: categoryId
				}
			});
		});
	},
	
	syncRemoteCategory: function(categoryId, full, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageCategory', {
			params: {
				crud: 'sync',
				id: categoryId,
				full: full
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope, [success, json]);
			}
		});
	},
	
	updateCategoryVisibility: function(categoryId, hidden, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageHiddenCategories', {
			params: {
				crud: 'update',
				categoryId: categoryId,
				hidden: hidden
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	updateCategoryPropColor: function(categoryId, color, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'SetCategoryPropColor', {
			params: {
				id: categoryId,
				color: color
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	updateCategoryPropSync: function(categoryId, sync, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'SetCategoryPropSync', {
			params: {
				id: categoryId,
				sync: sync
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	updateCheckedFolders: function(rootId, checked, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'UpdateCheckedFolders', {
			params: {
				rootId: rootId,
				checked: checked
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	deleteContactsItems: function(contactItemsIds, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'delete',
				ids: WTU.arrayAsParam(contactItemsIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	moveContactsItems: function(copy, contactItemsIds, targetCategoryId, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'move',
				copy: copy,
				ids: WTU.arrayAsParam(contactItemsIds),
				targetCategoryId: targetCategoryId
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	addContact: function(ownerId, categoryId, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.Contact', {swapReturn: true});
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('new', {
				data: {
					_profileId: ownerId,
					categoryId: categoryId
				}
			});
		});
	},
	
	prepareContactNewData: function(cnt) {
		var me = this,
				rn = me.getF3MyRoot(me.trFolders()),
				n = me.getF3FolderByRoot(rn),
				obj = {};
		
		obj._profileId = rn.get('_pid');
		obj.categoryId = n.get('_catId');
		
		// TODO: abilitare supporto all'inserimento nelle categorie condivise
		
		/*
		if (!Ext.isDefined(evt.calendarId)) {
			var rn = me.getF3MyRoot(),
					n = me.getF3FolderByRoot(rn);
			if (!n) Ext.raise('errorrrrrrrr');
			obj._profileId = rn.get('_pid');
			obj.calendarId = n.get('_calId');
		} else {
			Ext.raise('Not yet supported');
			obj.calendarId = evt.calendarId;
		}
		*/
		if (Ext.isDefined(cnt.title)) obj.title = cnt.title;
		if (Ext.isDefined(cnt.firstName)) obj.firstName = cnt.firstName;
		if (Ext.isDefined(cnt.lastName)) obj.lastName = cnt.lastName;
		if (Ext.isDefined(cnt.nickname)) obj.nickname = cnt.nickname;
		if (Ext.isDefined(cnt.gender)) obj.gender = cnt.gender;
		if (Ext.isDefined(cnt.mobile)) obj.mobile = cnt.mobile;
		if (Ext.isDefined(cnt.pager1)) obj.pager1 = cnt.pager1;
		if (Ext.isDefined(cnt.pager2)) obj.pager2 = cnt.pager2;
		if (Ext.isDefined(cnt.email1)) obj.email1 = cnt.email1;
		if (Ext.isDefined(cnt.email2)) obj.email2 = cnt.email2;
		if (Ext.isDefined(cnt.instantMsg1)) obj.instantMsg1 = cnt.instantMsg1;
		if (Ext.isDefined(cnt.instantMsg2)) obj.instantMsg2 = cnt.instantMsg2;
		if (Ext.isDefined(cnt.workAddress)) obj.workAddress = cnt.workAddress;
		if (Ext.isDefined(cnt.workPostalCode)) obj.workPostalCode = cnt.workPostalCode;
		if (Ext.isDefined(cnt.workCity)) obj.workCity = cnt.workCity;
		if (Ext.isDefined(cnt.workState)) obj.workState = cnt.workState;
		if (Ext.isDefined(cnt.workCountry)) obj.workCountry = cnt.workCountry;
		if (Ext.isDefined(cnt.workTelephone1)) obj.workTelephone1 = cnt.workTelephone1;
		if (Ext.isDefined(cnt.workTelephone2)) obj.workTelephone2 = cnt.workTelephone2;
		if (Ext.isDefined(cnt.workFax)) obj.workFax = cnt.workFax;
		if (Ext.isDefined(cnt.homeAddress)) obj.homeAddress = cnt.homeAddress;
		if (Ext.isDefined(cnt.homePostalCode)) obj.homePostalCode = cnt.homePostalCode;
		if (Ext.isDefined(cnt.homeCity)) obj.homeCity = cnt.homeCity;
		if (Ext.isDefined(cnt.homeState)) obj.homeState = cnt.homeState;
		if (Ext.isDefined(cnt.homeCountry)) obj.homeCountry = cnt.homeCountry;
		if (Ext.isDefined(cnt.homeTelephone1)) obj.homeTelephone1 = cnt.homeTelephone1;
		if (Ext.isDefined(cnt.homeTelephone2)) obj.homeTelephone2 = cnt.homeTelephone2;
		if (Ext.isDefined(cnt.homeFax)) obj.homeFax = cnt.homeFax;
		
		// OLD compatibility mappings...
		if (Ext.isDefined(cnt.workTelephone)) obj.workTelephone1 = cnt.workTelephone;
		if (Ext.isDefined(cnt.homeTelephone)) obj.homeTelephone1 = cnt.homeTelephone;
		if (Ext.isDefined(cnt.workMobile)) obj.mobile = cnt.workMobile;
		if (Ext.isDefined(cnt.workPager)) obj.pager1 = cnt.workPager;
		if (Ext.isDefined(cnt.homePager)) obj.pager2 = cnt.homePager;
		if (Ext.isDefined(cnt.workEmail)) obj.email1 = cnt.workEmail;
		if (Ext.isDefined(cnt.homeEmail)) obj.email2 = cnt.homeEmail;
		if (Ext.isDefined(cnt.workInstantMsg)) obj.instantMsg1 = cnt.workInstantMsg;
		if (Ext.isDefined(cnt.homeInstantMsg)) obj.instantMsg2 = cnt.homeInstantMsg;

		return obj;
	},
	
	addContact2: function(cnt, opts) {
		cnt = cnt || {};
		opts = opts || {};
		var me = this,
				data = me.prepareContactNewData(cnt),
				vw = WT.createView(me.ID, 'view.Contact', {swapReturn: true});	
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('new', {
				data: data,
				dirty: opts.dirty
			});
		});
	},
	
	editContact: function(contactId, opts) {
		this.openContact(true, contactId, opts);
	},
	
	openContact: function(edit, contactId, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.Contact', {swapReturn: true}),
				mode = edit ? 'edit' : 'view';
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin(mode, {
				data: {
					id: contactId
				}
			});
		});
	},
	
	addContactsList: function(ownerId, categoryId, recipients, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.ContactsList', {swapReturn: true});
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('new', {
				data: {
					_profileId: ownerId,
					categoryId: categoryId,
					recipients: recipients
				}
			});
		});
	},
	
	prepareContactsListNewData: function(cnt) {
		var me = this,
				rn = me.getF3MyRoot(me.trFolders()),
				n = me.getF3FolderByRoot(rn),
				obj = {};
		
		obj._profileId = rn.get('_pid');
		obj.categoryId = n.get('_catId');
		
		// TODO: abilitare supporto all'inserimento nelle categorie condivise
		if (Ext.isDefined(cnt.name)) obj.firstName = cnt.name;
		if (Ext.isDefined(cnt.recipients)) obj.recipients = cnt.recipients;

		return obj;
	},
	
	addContactsList2: function(cnt, opts) {
		cnt = cnt || {};
		opts = opts || {};
		var me = this,
				data = me.prepareContactsListNewData(cnt),
				vw = WT.createView(me.ID, 'view.ContactsList', {swapReturn: true});	
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('new', {
				data: data,
				dirty: opts.dirty
			});
		});
	},
	
	editContactsList: function(contactsListId, opts) {
		this.openContactsList(true, contactsListId, opts);
	},
	
	openContactsList: function(edit, contactsListId, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.ContactsList', {swapReturn: true}),
				mode = edit ? 'edit' : 'view';
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin(mode, {
				data: {
					id: contactsListId
				}
			});
		});
	},
	
	/* No more used but working...
	deleteContacts: function(contactIds, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageContacts', {
			params: {
				crud: 'delete',
				ids: WTU.arrayAsParam(contactIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	deleteContactsLists: function(contactsListIds, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageContactsLists', {
			params: {
				crud: 'delete',
				ids: WTU.arrayAsParam(contactsListIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	*/
	
	importContacts: function(categoryId, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.ImportContacts', {
					swapReturn: true,
					viewCfg: {
						categoryId: categoryId
					}
				});
		
		vw.on('dosuccess', function() {
			Ext.callback(opts.callback, opts.scope || me, [true]);
		});
		vw.showView();
	},
	
	printContactsDetail: function(contactIds) {
		var me = this, url;
		url = WTF.processBinUrl(me.ID, 'PrintContactsDetail', {ids: WTU.arrayAsParam(contactIds)});
		Sonicle.URLMgr.openFile(url, {filename: 'contacts-detail', newWindow: true});
	},
	
	sendContactsAsEmail: function(contactIds, opts) {
		opts = opts || {};
		var me = this,
			mapi = WT.getServiceApi('com.sonicle.webtop.mail');
		if (mapi) {
			var meid = mapi.buildMessageEditorId();
			WT.ajaxReq(me.ID, 'PrepareSendContactAsEmail', {
				params: {
					uploadTag: meid,
					ids: WTU.arrayAsParam(contactIds)
				},
				callback: function(success, json) {
					mapi.newMessage({
						messageEditorId: meid,
						format: 'html',
						content: '<br>',
						attachments: json.data
					}, {
						dirty: true,
						contentReady: false,
						appendContent: false
					});
					Ext.callback(opts.callback, opts.scope || me, [true]);
				}
			});
		}
	},
	
	addAsContactsListRecipient: function(listAddress, rcptType, rcptAddresses, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'AddToContactsList', {
			params: {
				list: listAddress,
				recipientType: rcptType,
				emails: rcptAddresses
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope, [success, json]);
			}
		});
	},
	
	/*
	importVCard: function(categoryId, uploadId) {
		var me = this;
		WT.ajaxReq(me.ID, 'ImportVCard', {
			params: {
				categoryId: categoryId,
				uploadId: uploadId
			},
			callback: function(success, json) {
				if(success) {
					me.reloadContacts();
				}
			}
		});
	},
	*/
	
	selectionIds: function(sel) {
		var ids = [];
		Ext.iterate(sel, function(rec) {
			ids.push(rec.getId());
		});
		return ids;
	},
			
	privates: {
		updateDisabled: function(action) {
			var me = this,
					dis = me.isDisabled(action);

			if(action === 'deleteContact') {
				me.setActDisabled(action, dis);
				me.setActDisabled('deleteContact2', dis);
			} else {
				me.setActDisabled(action, dis);
			}
		},
		
		isDisabled: function(action) {
			var me = this, sel, er;

			switch(action) {
				case 'showContact':
				case 'copyContact':
					sel = me.getSelectedContacts();
					if (sel && (sel.length > 0)) {
						return false;
					} else {
						return true;
					}
					break;
				case 'printContact':
					sel = me.getSelectedContact();
					if (sel && (sel.get('isList') === false)) {
						return false;
					} else {
						return true;
					}
					break;
				case 'callTelephone':
					sel = me.getSelectedContact();
					if (sel && (WT.getVar('pbxConfigured') === true) && !Ext.isEmpty(sel.get('telephone'))) {
						return false;
					} else {
						return true;
					}
					break;
				case 'callMobile':
					sel = me.getSelectedContact();
					if (sel && (WT.getVar('pbxConfigured') === true) && !Ext.isEmpty(sel.get('mobile'))) {
						return false;
					} else {
						return true;
					}
					break;
				case 'sendSms':
					sel = me.getSelectedContact();
					if (sel && (WT.getVar('smsConfigured') === true) && !Ext.isEmpty(sel.get('mobile'))) {
						return false;
					} else {
						return true;
					}
					break;
				case 'moveContact':
				case 'deleteContact':
					sel = me.getSelectedContacts();
					if (sel.length === 0) {
						return true;
					} else if (sel.length === 1) {
						er = me.toRightsObj(sel[0].get('_erights'));
						return !er.DELETE;
					} else {
						for (var i=0; i<sel.length; i++) {
							if (!me.toRightsObj(sel[i].get('_erights')).DELETE) return true;
						}
						return false;
					}
					break;
				case 'addContactsListFromSel':
				case 'addToContactsListFromSel':
					sel = me.getSelectedContacts();
					if (sel.length === 0) {
						return true;
					} else {
						var emailMiss = true, firstIsList = sel[0].get('isList') === true;
						for (var i=0; i<sel.length; i++) {
							if (!Ext.isEmpty(sel[i].get('email'))) emailMiss = false;
							if (sel[i].get('isList') !== firstIsList) return true;
						}
						return firstIsList || emailMiss;
					}
					break;
			}
		},
		
		extractIds: function(recs, idField, filterFn) {
			if (arguments.length === 2) {
				if (Ext.isFunction(idField)) {
					filterFn = idField;
					idField = undefined;
				}
			}
			var ids = [];
			if (!Ext.isFunction(filterFn)) filterFn = function(rec) {return true;};
			Ext.iterate(recs, function(rec) {
				if (filterFn(rec)) {
					ids.push(Ext.isEmpty(idField) ? rec.getId() : rec.get(idField));
				}
			});
			return ids;
		},
		
		toRcpts: function(recs) {
			var rcpts = [], email;
			Ext.iterate(recs, function(rec) {
				email = rec.get('email');
				if (!Ext.isEmpty(email)) rcpts.push({recipientType:'to', recipient: email});
			});
			return rcpts;
		},
		
		toEmails: function(recs) {
			var emails = [], email, fn;
			Ext.iterate(recs, function(rec) {
				email = rec.get('email');
				fn = rec.get('fullName');
				if (!Ext.isEmpty(fn)) email = fn + '<' + email + '>';
				if (!Ext.isEmpty(email)) emails.push(email);
			});
			return emails;
		},
		
		createCategoryChooser: function(copy) {
			var me = this;
			return WT.createView(me.ID, 'view.CategoryChooser', {
				swapReturn: true,
				viewCfg: {
					dockableConfig: {
						title: me.res(copy ? 'act-copyContact.lbl' : 'act-moveContact.lbl')
					},
					writableOnly: true
				}
			});
		},
		
		createHiddenCategories: function(rootNodeId) {
			var me = this;
			return WT.createView(me.ID, 'view.HiddenCategories', {
				swapReturn: true,
				viewCfg: {
					action: 'ManageHiddenCategories',
					extraParams: {
						crud: 'list',
						rootId: rootNodeId
					}
				}
			});
		},
		
		createContactListChooser: function(contactListId) {
			var me = this;
			return WT.createView(me.ID, 'view.ListChooser', {
				swapReturn: true,
				viewCfg: {
					dockableConfig: {
						title: me.res('act-addToContactsListFromSel.lbl')
					},
					list: contactListId
				}
			});
		}
	},
	
	statics: {
		NOTAG_REMOTESYNC: 'remsync-',

		noTagRemoteSync: function(categoryId) {
			return this.NOTAG_REMOTESYNC + categoryId;
		}
	}
});
