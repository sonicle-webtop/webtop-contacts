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
	extend: 'WT.sdk.Service',
	requires: [
		'Sonicle.grid.column.Icon',
		'Sonicle.grid.column.Color',
		'WT.ux.data.EmptyModel',
		'Sonicle.webtop.contacts.model.FolderNode',
		'Sonicle.webtop.contacts.model.GridContact',
		'Sonicle.webtop.contacts.view.Category',
		'Sonicle.webtop.calendar.view.Sharing'
	],
	mixins: [
		'WT.mixin.FoldersTree'
	],
	
	activeView: null,
	
	init: function() {
		var me = this, ies, iitems = [];
		
		me.activeView = me.getOption('view');
		me.initActions();
		me.initCxm();
		
		me.on('activate', me.onActivate, me);
		
		me.setToolbar(Ext.create({
			xtype: 'toolbar',
			
			items: [
				'->',
				me.getAction('workview'),
				me.getAction('homeview'),
				' ',
				me.addRef('txtsearch', Ext.create({
					xtype: 'textfield',
					width: 200,
					triggers: {
						search: {
							cls: Ext.baseCSSPrefix + 'form-search-trigger',
							handler: function(s) {
								me.queryContacts(s.getValue());
							}
						}
					},
					listeners: {
						specialkey: function(s, e) {
							if(e.getKey() === e.ENTER) me.queryContacts(s.getValue());
						}
					}
				}))
			]
		}));
		
		me.setToolComponent(Ext.create({
			xtype: 'panel',
			layout: 'border',
			title: me.getName(),
			items: [
				me.addRef('folderstree', Ext.create({
					region: 'center',
					xtype: 'treepanel',
					border: false,
					useArrows: true,
					rootVisible: false,
					store: {
						autoLoad: true,
						autoSync: true,
						model: 'Sonicle.webtop.contacts.model.FolderNode',
						proxy: WTF.apiProxy(me.ID, 'ManageFoldersTree', 'children', {
							writer: {
								allowSingle: false // Make update/delete using array payload
							}
						}),
						root: {
							id: 'root',
							expanded: true
						},
						listeners: {
							write: function(s,op) {
								me.refreshContacts();
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
				}))
			]
		}));
		
		ies = ['#','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','*'];
		for(var i=0; i<ies.length; i++) {
			iitems.push({
				itemId: 'chr'+ies[i].charCodeAt(0),
				text: ies[i],
				toggleGroup: 'ix',
				handler: function(s) {
					me.refreshContacts(s.getText(), null);
				}
			});
		}
		
		me.setMainComponent(Ext.create({
			xtype: 'container',
			layout: 'border',
			referenceHolder: true,
			items: [{
				region: 'center',
				xtype: 'gridpanel',
				reference: 'gpcontacts',
				store: {
					model: 'Sonicle.webtop.contacts.model.GridContact',
					proxy: WTF.apiProxy(me.ID, 'ManageGridContacts', 'contacts', {
						extraParams: {
							view: null,
							letter: null,
							query: null
						}
					}),
					listeners: {
						load: function(s, rec, success, op) {
							var pars = op.getProxy().getExtraParams();
							if(pars.letter != null) {
								me.tbIndex().getComponent('chr'+pars.letter.charCodeAt(0)).toggle(true);
								me.txtSearch().setValue(null);
							} else if(pars.query != null) {
								me.tbIndex().getComponent(0).toggle(true);
								me.tbIndex().getComponent(0).toggle(false);
							} else {
								me.tbIndex().getComponent('chr'+'A'.charCodeAt(0)).toggle(true);
								me.txtSearch().setValue(null);
							}
						},
						metachange: function(s, meta) {
							var gp = me.gpContacts(),
									colsInfo = [];
							
							gp.isReconfiguring = true;
							if(meta.sortInfo) {
								s.getSorters().removeAll();
								s.getSorters().add(new Ext.util.Sorter({
									property: meta.sortInfo.field,
									direction: meta.sortInfo.direction
								}));
							}
							
							if(meta.groupInfo) {
								s.group(meta.groupInfo.field, meta.groupInfo.direction);
								gp.getView().getFeature('grouping').enable();
							} else {
								gp.getView().getFeature('grouping').disable();
							}
							
							if(meta.colsInfo) {
								colsInfo.push({
									xtype: 'soiconcolumn',
									iconField: function(v,rec) {
										return WTF.cssIconCls(me.XID, (rec.get('listId')>0) ? 'contacts-list' : 'contact', 'xs');
									},
									iconSize: WTU.imgSizeToPx('xs'),
									width: 30,
									groupable: false
								});

								Ext.iterate(meta.colsInfo, function(col,i) {
									if(col.dataIndex === 'categoryName') {
										col.xtype = 'socolorcolumn',
										col.header = me.res('gpcontacts.category.lbl');
										col.colorField = 'categoryColor',
										col.displayField = 'categoryName',
										col.width = 100;
										col.hidden = false;
									} else {
										col.header = me.res('gpcontacts.'+col.dataIndex+'.lbl');
									}
									if(col.xtype === 'datecolumn') {
										col['format'] = WT.getShortDateFmt();
									}
									colsInfo.push(col);
								});
								
								me.gpContacts().reconfigure(s, colsInfo);
							}
							gp.isReconfiguring = false;
						}
					}
				},
				selModel: WTF.multiRowSelection(false),
				columns: [],
				features: [{
					id: 'grouping',
					ftype: 'grouping',
					groupHeaderTpl: '{columnName}: {name} ({children.length})'
				}],
				listeners: {
					rowdblclick: function(s, rec) {
						var er = me.toRightsObj(rec.get('_erights'));
						me.showContact(er.UPDATE, rec.get('id'), rec.get('listId'));
					},
					rowcontextmenu: function(s, rec, itm, i, e) {
						s.setSelection(rec);
						WT.showContextMenu(e, me.getRef('cxmGrid'), {contact: rec});
					},
					groupchange: function(sto, group) {
						if(me.gpContacts().isReconfiguring) return;
						if(group == null) me.saveGroupInfo(null, null);
						else me.saveGroupInfo(group.getProperty(), group.getDirection());
					},
					sortchange: function(ct, col, dir) {
						if(me.gpContacts().isReconfiguring) return;
						me.saveSortInfo(col.dataIndex, dir);
					}
				}
			}, {
				region: 'east',
				xtype: 'toolbar',
				reference: 'tbindex',
				vertical: true,
				overflowHandler: 'scroller',
				defaults: {
					padding: 0
				},
				items: iitems
			}]
		}));
	},
	
	saveGroupInfo: function(field, direction) {
		var me = this;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'save',
				view: me.activeView,
				context: 'group',
				field: field,
				direction: direction
			}
		});
	},
	
	saveSortInfo: function(field, direction) {
		var me = this;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'save',
				view: me.activeView,
				context: 'sort',
				field: field,
				direction: direction
			}
		});
	},
	
	txtSearch: function() {
		return this.getRef('txtsearch');
	},
	
	gpContacts: function() {
		return this.getMainComponent().lookupReference('gpcontacts');
	},
	
	tbIndex: function() {
		return this.getMainComponent().lookupReference('tbindex');
	},
	
	initActions: function() {
		var me = this;
		
		me.addAction('new', 'newContact', {
			handler: function() {
				me.getAction('addContact').execute();
			}
		});
		me.addAction('workview', {
			pressed: me.activeView === 'w',
			toggleGroup: 'view',
			handler: function() {
				me.changeView('w');
			}
		});
		me.addAction('homeview', {
			pressed: me.activeView === 'h',
			toggleGroup: 'view',
			handler: function() {
				me.changeView('h');
			}
		});
		me.addAction('editSharing', {
			text: WT.res('sharing.tit'),
			iconCls: WTF.cssIconCls(WT.XID, 'sharing', 'xs'),
			handler: function() {
				var node = me.getSelectedNode(me.getRef('folderstree'));
				if(node) me.editShare(node.getId());
			}
		});
		me.addAction('addCategory', {
			handler: function() {
				var node = me.getSelectedFolder(me.getRef('folderstree'));
				if(node) me.addCategory(node.get('_domainId'), node.get('_userId'));
			}
		});
		me.addAction('editCategory', {
			handler: function() {
				var node = me.getSelectedFolder(me.getRef('folderstree'));
				if(node) me.editCategory(node.get('_catId'));
			}
		});
		me.addAction('deleteCategory', {
			handler: function() {
				var node = me.getSelectedFolder(me.getRef('folderstree'));
				if(node) me.deleteCategory(node);
			}
		});
		me.addRef('uploaders', 'importContacts', Ext.create('Sonicle.upload.Item', {
			text: WT.res(me.ID, 'act-importContacts.lbl'),
			iconCls: WTF.cssIconCls(me.XID, 'importContacts', 'xs'),
			uploaderConfig: WTF.uploader(me.ID, 'VCardUpload', {
				mimeTypes: [
					{title: 'vCard files', extensions: 'vcf,vcard'}
				],
				listeners: {
					uploadstarted: function(up) {
						//TODO: caricamento
						//me.wait();
					},
					uploadcomplete: function(up) {
						//TODO: caricamento
						//me.unwait();
					},
					uploaderror: function(up) {
						//TODO: caricamento
						//me.unwait();
					},
					fileuploaded: function(up, file) {
						var node = me.getSelectedFolder(me.getRef('folderstree'));
						if(node) me.importVCard(node.get('_catId'), file.uploadId);
					}
				}
			})
		}));
		me.addAction('viewAllFolders', {
			iconCls: 'wt-icon-select-all-xs',
			handler: function() {
				me.showHideAllFolders(me.getSelectedRootFolder(me.getRef('folderstree')), true);
			}
		});
		me.addAction('viewNoneFolders', {
			iconCls: 'wt-icon-select-none-xs',
			handler: function() {
				me.showHideAllFolders(me.getSelectedRootFolder(me.getRef('folderstree')), false);
			}
		});
		me.addAction('showContact', {
			text: WT.res('act-open.lbl'),
			handler: function() {
				var rec = WT.getContextMenuData().contact,
						er = me.toRightsObj(rec.get('_erights'));
				me.showContact(er.UPDATE, rec.get('id'), rec.get('listId'));
			}
		});
		me.addAction('addContact', {
			handler: function() {
				var node = me.getSelectedFolder(me.getRef('folderstree'));
				if(node) me.addContact(node.get('_pid'), node.get('_catId'));
			}
		});
		me.addAction('addContactsList', {
			handler: function() {
				var node = me.getSelectedFolder(me.getRef('folderstree'));
				if(node) me.addContactsList(node.get('_pid'), node.get('_catId'));
			}
		});
		me.addAction('addContactsListFromSel', {
			handler: function() {
				var node = me.getSelectedFolder(me.getRef('folderstree')), rcpts = [], email;
				if(node) {
					Ext.iterate(me.gpContacts().getSelection(), function(rec) {
						email = rec.get('cemail');
						if(!Ext.isEmpty(email)) rcpts.push({recipientType:'to', recipient: email});
					});
					me.addContactsList(node.get('_pid'), node.get('_catId'), rcpts);
				}
			}
		});
		me.addAction('deleteContact', {
			text: WT.res('act-delete.lbl'),
			iconCls: 'wt-icon-delete-xs',
			handler: function() {
				var rec = WT.getContextMenuData().contact, id, text;
				id = rec.get('contactId');
				text = Sonicle.String.join(' ', rec.get('firstName'), rec.get('lastName'));
				if(rec.get('listId')>0) {
					me.deleteContactsList(id, text);
				} else {
					me.deleteContact(id, text);
				}
			}
		});
		me.addAction('copyContact', {
			handler: function() {
				var rec = WT.getContextMenuData().contact;
				if(rec.get('listId')>0) {
					me.copyContactsList(false, rec.get('id'), rec.get('_profileId'), rec.get('categoryId'));
				} else {
					me.copyContact(false, rec.get('id'), rec.get('_profileId'), rec.get('categoryId'));
				}
			}
		});
		me.addAction('moveContact', {
			handler: function() {
				var rec = WT.getContextMenuData().contact;
				if(rec.get('listId')>0) {
					me.copyContactsList(true, rec.get('id'), rec.get('_profileId'), rec.get('categoryId'));
				} else {
					me.copyContact(true, rec.get('id'), rec.get('_profileId'), rec.get('categoryId'));
				}
			}
		});
	},
	
	initCxm: function() {
		var me = this;
		
		me.addRef('cxmRootFolder', Ext.create({
			xtype: 'menu',
			items: [
				me.getAction('addCategory'),
				'-',
				me.getAction('editSharing')
				//TODO: azioni altri servizi?
			],
			listeners: {
				beforeshow: function() {
					var rec = WT.getContextMenuData().folder,
							rr = me.toRightsObj(rec.get('_rrights'));
					me.getAction('addCategory').setDisabled(!rr.MANAGE);
					me.getAction('editSharing').setDisabled(!rr.MANAGE);
				}
			}
		}));
		
		me.addRef('cxmFolder', Ext.create({
			xtype: 'menu',
			items: [
				me.getAction('editCategory'),
				me.getAction('deleteCategory'),
				me.getAction('addCategory'),
				'-',
				me.getAction('editSharing'),
				'-',
				me.getAction('viewAllFolders'),
				me.getAction('viewNoneFolders'),
				'-',
				me.getAction('addContact'),
				me.getAction('addContactsList'),
				me.getAction('addContactsListFromSel'),
				me.getRef('uploaders', 'importContacts')
				//TODO: azioni altri servizi?
			],
			listeners: {
				beforeshow: function() {
					var rec = WT.getContextMenuData().folder,
							rr = me.toRightsObj(rec.get('_rrights')),
							fr = me.toRightsObj(rec.get('_frights')),
							er = me.toRightsObj(rec.get('_erights'));
					me.getAction('editCategory').setDisabled(!fr.UPDATE);
					me.getAction('deleteCategory').setDisabled(!fr.DELETE || rec.get('_builtIn'));
					me.getAction('addCategory').setDisabled(!rr.MANAGE);
					me.getAction('editSharing').setDisabled(!rr.MANAGE);
					me.getAction('addContact').setDisabled(!er.CREATE);
					me.getAction('addContactsList').setDisabled(!er.CREATE);
					me.getAction('addContactsListFromSel').setDisabled(!er.CREATE);
					me.getRef('uploaders', 'importContacts').setDisabled(!er.CREATE);
				}
			}
		}));
		
		me.addRef('cxmGrid', Ext.create({
			xtype: 'menu',
			items: [
				me.getAction('showContact'),
				'-',
				me.getAction('deleteContact'),
				me.getAction('copyContact'),
				me.getAction('moveContact')
			],
			listeners: {
				beforeshow: function() {
					var rec = WT.getContextMenuData().contact,
							er = me.toRightsObj(rec.get('_erights'));
					me.getAction('deleteContact').setDisabled(!er.DELETE);
					me.getAction('moveContact').setDisabled(!er.UPDATE);
				}
			}
		}));
	},
	
	onActivate: function() {
		var me = this;
		me.refreshContacts();
	},
	
	onCategoryViewSave: function(s, success, model) {
		if(!success) return;
		var me = this,
				store = me.getRef('folderstree').getStore(),
				node;
		
		// Look for root folder and reload it!
		node = store.findNode('_pid', model.get('_profileId'), false);
		if(node) store.load({node: node});
	},
	
	changeView: function(view) {
		var me = this;
		me.activeView = view;
		me.refreshContacts('A');
	},
	
	queryContacts: function(txt) {
		this.refreshContacts(null, txt);
	},
	
	refreshContacts: function(letter, query) {
		var me = this,
				sto = me.gpContacts().getStore(), pars = {};
		Ext.apply(pars, {
			view: me.activeView
		});
		if(letter !== undefined) Ext.apply(pars, {letter: letter});
		if(query !== undefined) Ext.apply(pars, {query: query});
		WTU.loadWithExtraParams(sto, pars);
	},
	
	editShare: function(id) {
		var me = this,
				vw = WT.createView(me.ID, 'view.Sharing');
		
		vw.show(false, function() {
			vw.getView().begin('edit', {
				data: {
					id: id
				}
			});
		});
	},
	
	addCategory: function(domainId, userId) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.Category');
		
		vwc.getView().on('viewsave', me.onCategoryViewSave, me);
		vwc.show(false, function() {
			vwc.getView().begin('new', {
				data: {
					domainId: domainId,
					userId: userId
				}
			});
		});
	},
	
	editCategory: function(categoryId) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.Category');
		
		vwc.getView().on('viewsave', me.onCategoryViewSave, me);
		vwc.show(false, function() {
			vwc.getView().begin('edit', {
				data: {
					categoryId: categoryId
				}
			});
		});
	},
	
	deleteCategory: function(rec) {
		WT.confirm(this.res('category.confirm.delete', rec.get('text')), function(bid) {
			if(bid === 'yes') rec.drop();
		}, this);
	},
	
	showContact: function(edit, id, listId) {
		if(listId > 0) {
			this.openContactsList(edit, id);
		} else {
			this.openContact(edit, id);
		}
	},
	
	addContact: function(ownerId, categoryId) {
		var me = this,
				vw = WT.createView(me.ID, 'view.Contact');
		
		vw.getView().on('viewsave', me.onContactViewSave, me);
		vw.show(false, function() {
			vw.getView().begin('new', {
				data: {
					_profileId: ownerId,
					categoryId: categoryId
				}
			});
		});
	},
	
	addContactsList: function(ownerId, categoryId, recipients) {
		var me = this,
				vw = WT.createView(me.ID, 'view.ContactsList');
		
		vw.getView().on('viewsave', me.onContactListViewSave, me);
		vw.show(false, function() {
			vw.getView().begin('new', {
				data: {
					_profileId: ownerId,
					categoryId: categoryId,
					recipients: recipients
				}
			});
		});
	},
	
	openContact: function(edit, id) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.Contact'),
				mode = edit ? 'edit' : 'view';
		
		if(edit) vwc.getView().on('viewsave', me.onContactViewSave, me);
		vwc.show(false, function() {
			vwc.getView().begin(mode, {
				data: {
					id: id
				}
			});
		});
	},
	
	openContactsList: function(edit, id) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.ContactsList'),
				mode = edit ? 'edit' : 'view';
		
		vwc.getView().on('viewsave', me.onContactListViewSave, me);
		vwc.show(false, function() {
			vwc.getView().begin(mode, {
				data: {
					id: id
				}
			});
		});
	},
	
	/*
	deleteContact: function(rec) {
		var me = this,
				key = (rec.get('listId') > 0 ? 'contactslist' : 'contact') + '.confirm.delete';
		WT.confirm(me.res(key, rec.get('text')), function(bid) {
			if(bid === 'yes') rec.drop();
		}, me);
	},
	*/
	
	deleteContact: function(id, text) {
		this.doDeleteContact(false, id, text);
	},
	
	deleteContactsList: function(id, text) {
		this.doDeleteContact(true, id, text);
	},
	
	doDeleteContact: function(isList, id, text) {
		var me = this,
				action = isList === true ? 'ManageContactsLists' : 'ManageContacts',
				key = (isList === true ? 'contactsList' : 'contact') + '.confirm.delete';
		
		WT.confirm(me.res(key, text || ''), function(bid) {
			if(bid === 'yes') {
				WT.ajaxReq(me.ID, action, {
					params: {
						crud: 'delete',
						id: id
					},
					callback: function(success, json) {
						if(success) {
							me.refreshContacts();
						}
					}
				});
			}
		}, me);
	},
	
	copyContact: function(move, id, ownerId, catId) {
		this.doCopyContact(false, move, id, ownerId, catId);
	},
	
	copyContactsList: function(move, id, ownerId, catId) {
		this.doCopyContact(true, move, id, ownerId, catId);
	},
	
	doCopyContact: function(isList, move, id, ownerId, catId) {
		var me = this,
				action = isList === true ? 'ManageContactsList' : 'ManageContacts',
				vw = WT.createView(me.ID, 'view.CategoryChooser', {
					viewCfg: {
						dockableConfig: {
							title: me.res(move ? 'act-moveContact.lbl' : 'act-copyContact.lbl')
						},
						ownerId: ownerId,
						categoryId: catId
					}
				}),
				data;
		
		vw.getView().on('viewok', function(s) {
			data = s.getVMData();
			WT.ajaxReq(me.ID, action, {
				params: {
					crud: 'copy',
					move: move,
					id: id,
					targetCategoryId: data.categoryId
				},
				callback: function(success, json) {
					if(success) {
						me.refreshContacts();
					}
				}
			});
		});
		vw.show();
	},
	
	importVCard: function(categoryId, uploadId) {
		var me = this;
		WT.ajaxReq(me.ID, 'ImportVCard', {
			params: {
				categoryId: categoryId,
				uploadId: uploadId
			},
			callback: function(success, json) {
				if(success) {
					me.refreshContacts();
				}
			}
		});
	},
	
	onContactViewSave: function(s, success, model) {
		if(!success) return;
		this.refreshContacts();
	},
	
	onContactListViewSave: function(s, success, model) {
		if(!success) return;
		this.refreshContacts();
	}
});
