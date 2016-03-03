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
		'WT.ux.data.SimpleModel',
		'Sonicle.webtop.contacts.model.FolderNode',
		'Sonicle.webtop.contacts.model.GridContact',
		'Sonicle.webtop.contacts.view.Sharing',
		'Sonicle.webtop.contacts.view.Category',
		'Sonicle.webtop.contacts.view.Contact',
		'Sonicle.webtop.contacts.view.ContactsList',
		'Sonicle.webtop.contacts.view.CategoryChooser'
	],
	mixins: [
		'WT.mixin.FoldersTree'
	],
	
	needsReload: true,
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
				'-',
				me.getAction('printAddressbook'),
				me.getAction('deleteContact2'),
				'-',
				me.getAction('addContact2'),
				me.getAction('addContactsList2'),
				'->',
				me.getAction('workview'),
				me.getAction('homeview'),
				'-',
				me.addRef('cbogroup', Ext.create(WTF.lookupCombo('id', 'desc', {
					queryMode: 'local',
					store: {
						model: 'WT.ux.data.SimpleModel'
					},
					fieldLabel: me.res('fld-group.lbl'),
					labelWidth: 70,
					width: 190,
					listeners: {
						select: function(s, rec) {
							me.applyGrouping((rec.get('id') === '-') ? null : rec.get('id'));
						}
					}
				}))),
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
					me.reloadContacts(s.getText(), null);
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
							var pars = op.getProxy().getExtraParams(),
									tb = me.getToolbar(),
									tbi = me.tbIndex();
							if(!Ext.isEmpty(pars.view)) tb.getComponent(pars.view).toggle(true);
							if(pars.letter != null) {
								tbi.getComponent('chr'+pars.letter.charCodeAt(0)).toggle(true);
								me.txtSearch().setValue(null);
							} else if(pars.query != null) {
								tbi.getComponent(0).toggle(true);
								tbi.getComponent(0).toggle(false);
							} else {
								tbi.getComponent('chr'+'A'.charCodeAt(0)).toggle(true);
								me.txtSearch().setValue(null);
							}
						},
						metachange: function(s, meta) {
							var gp = me.gpContacts(),
									colsInfo = [],
									data = [];
							
							s.isReconfiguring = true;
							if(meta.colsInfo) {
								colsInfo.push({
									xtype: 'soiconcolumn',
									iconField: function(v,rec) {
										return me.cssIconCls((rec.get('isList') === true) ? 'contacts-list' : 'contact', 'xs');
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
									} else if(col.dataIndex === 'birthday') {
										col.format = WT.getShortDateFmt();
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
							
							// Fill group combo
							Ext.iterate(colsInfo, function(col,i) {
								if(col.groupable && !col.hidden) {
									data.push({id: col.dataIndex, desc: col.header});
								}
							});
							
							if(meta.groupInfo) {
								me.loadCboGroup(data, meta.groupInfo.field);
								me.applyGrouping(meta.groupInfo.field, meta.groupInfo.direction);
							} else {
								me.loadCboGroup(data);
								me.applyGrouping(null);
							}
							if(meta.sortInfo) {
								s.getSorters().removeAll();
								s.getSorters().add(new Ext.util.Sorter({
									property: meta.sortInfo.field,
									direction: meta.sortInfo.direction
								}));
							}
							
							s.isReconfiguring = false;
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
					selectionchange: function() {
						me.updateDisabled('showContact');
						me.updateDisabled('printContact');
						me.updateDisabled('copyContact');
						me.updateDisabled('moveContact');
						me.updateDisabled('deleteContact');
						me.updateDisabled('addContactsListFromSel');
					},
					rowdblclick: function(s, rec) {
						var er = me.toRightsObj(rec.get('_erights'));
						me.showContact(rec.get('isList'), er.UPDATE, rec.get('id'));
					},
					rowcontextmenu: function(s, rec, itm, i, e) {
						WT.showContextMenu(e, me.getRef('cxmGrid'), {
							contact: rec,
							contacts: s.getSelection()
						});
					},
					groupchange: function(sto, group) {
						if(sto.isReconfiguring) return;
						if(group == null) {
							me.getRef('cbogroup').setValue('-');
							me.saveGroupInfo(null, null);
						} else {
							me.getRef('cbogroup').setValue(group.getProperty());
							me.saveGroupInfo(group.getProperty(), group.getDirection());
						}
					},
					sortchange: function(ct, col, dir) {
						if(me.gpContacts().getStore().isReconfiguring) return;
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
	
	loadCboGroup: function(data, value) {
		var cbo = this.getRef('cbogroup');
		data.unshift({id: '-', desc: WT.res('word.no')});
		cbo.getStore().setData(data);
		cbo.setValue((value) ? value : '-');
	},
	
	applyGrouping: function(field, dir) {
		var gp = this.gpContacts();
		if(field) {
			gp.getStore().group(field, dir || 'asc');
			gp.getView().getFeature('grouping').enable();
		} else {
			gp.getView().getFeature('grouping').disable();
		}
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
		me.addAction('new', 'newContactsList', {
			handler: function() {
				me.getAction('addContactsList').execute();
			}
		});
		me.addAction('workview', {
			itemId: 'w',
			toggleGroup: 'view',
			handler: function() {
				me.changeView('w');
			}
		});
		me.addAction('homeview', {
			itemId: 'h',
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
				if(node) me.confirmDeleteCategory(node);
			}
		});
		me.addAction('importContacts', {
			handler: function() {
				var node = me.getSelectedFolder(me.getRef('folderstree'));
				if(node) me.importContacts(node.get('_catId'));
			}
		});
		/*
		me.addRef('uploaders', 'importContacts', Ext.create('Sonicle.upload.Item', {
			text: WT.res(me.ID, 'act-importContacts.lbl'),
			iconCls: me.cssIconCls('importContacts', 'xs'),
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
		*/
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
				var rec = me.getSelectedContact(), er;
				if(rec) {
					er = me.toRightsObj(rec.get('_erights'));
					me.showContact(rec.get('isList'), er.UPDATE, rec.get('id'));
				}
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
				var node = me.getSelectedFolder(me.getRef('folderstree')),
						er = me.toRightsObj(node.get('_erights')),
						rcpts = me.buildRcpts(me.activeView, me.getSelectedContacts());
				
				if(er.CREATE) {
					me.addContactsList(node.get('_pid'), node.get('_catId'), rcpts);
				} else {
					me.addContactsList(WT.getOption('profileId'), null, rcpts);
				}
			}
		});
		me.addAction('deleteContact', {
			text: WT.res('act-delete.lbl'),
			iconCls: 'wt-icon-delete-xs',
			handler: function() {
				var sel = me.getSelectedContacts();
				if(sel.length > 0) me.deleteSelContacts(sel);
			}
		});
		me.addAction('copyContact', {
			handler: function() {
				me.moveContactsSel(true, me.getSelectedContacts());
			}
		});
		me.addAction('moveContact', {
			handler: function() {
				me.moveContactsSel(false, me.getSelectedContacts());
			}
		});
		me.addAction('printContact', {
			text: WT.res('act-print.lbl'),
			iconCls: 'wt-icon-print-xs',
			handler: function() {
				var sel = me.getSelectedContacts();
				if(sel.length > 0) me.printSelContacts(sel);
			}
		});
		me.addAction('printAddressbook', {
			text: null,
			tooltip: me.res('act-printAddressbook.tip'),
			iconCls: 'wt-icon-print-xs',
			handler: function() {
				var params = Ext.clone(me.gpContacts().getStore().getProxy().getExtraParams());
				var url = WTF.processBinUrl(me.ID, 'PrintAddressbook', params);
				Sonicle.URLMgr.openFile(url, {filename: 'addressbook', newWindow: true});
			}
		});
		me.addAction('deleteContact2', {
			text: null,
			tooltip: WT.res('act-delete.tip'),
			iconCls: 'wt-icon-delete-xs',
			handler: function() {
				me.getAction('deleteContact').execute();
			}
		});
		me.addAction('addContact2', {
			text: null,
			tooltip: me.res('act-addContact.lbl'),
			iconCls: me.cssIconCls('addContact', 'xs'),
			handler: function() {
				me.getAction('addContact').execute();
			}
		});
		me.addAction('addContactsList2', {
			text: null,
			tooltip: me.res('act-addContactsList.lbl'),
			iconCls: me.cssIconCls('addContactsList', 'xs'),
			handler: function() {
				me.getAction('addContactsList').execute();
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
				beforeshow: function(s) {
					var rec = s.menuData.folder,
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
				me.getAction('importContacts')
				//TODO: azioni altri servizi?
			],
			listeners: {
				beforeshow: function(s) {
					var rec = s.menuData.folder,
							rr = me.toRightsObj(rec.get('_rrights')),
							fr = me.toRightsObj(rec.get('_frights')),
							er = me.toRightsObj(rec.get('_erights'));
					me.getAction('editCategory').setDisabled(!fr.UPDATE);
					me.getAction('deleteCategory').setDisabled(!fr.DELETE || rec.get('_builtIn'));
					me.getAction('addCategory').setDisabled(!rr.MANAGE);
					me.getAction('editSharing').setDisabled(!rr.MANAGE);
					me.getAction('addContact').setDisabled(!er.CREATE);
					me.getAction('addContactsList').setDisabled(!er.CREATE);
					me.getAction('importContacts').setDisabled(!er.CREATE);
				}
			}
		}));
		
		me.addRef('cxmGrid', Ext.create({
			xtype: 'menu',
			items: [
				me.getAction('showContact'),
				{
					text: me.res('copyormove.lbl'),
					menu: {
						items: [
							me.getAction('copyContact'),
							me.getAction('moveContact')
						]
					}
				},
				me.getAction('printContact'),
				'-',
				me.getAction('deleteContact'),
				'-',
				me.getAction('addContactsListFromSel')
			]
		}));
	},
	
	onActivate: function() {
		var me = this,
				gp = me.gpContacts();
		
		if(me.needsReload) {
			me.needsReload = false;
			if(gp.getStore().loadCount === 0) { // The first time...
				// ...sets startup letter!
				me.reloadContacts('A');
			} else {
				me.reloadContacts();
			}
		}
		
		me.updateDisabled('showContact');
		me.updateDisabled('printContact');
		me.updateDisabled('copyContact');
		me.updateDisabled('moveContact');
		me.updateDisabled('deleteContact');
		me.updateDisabled('addContactsListFromSel');
	},
	
	onCategoryViewSave: function(s, success, model) {
		if(!success) return;
		var me = this,
				store = me.getRef('folderstree').getStore(),
				node;
		
		// Look for root folder and reload it!
		node = store.findNode('_pid', model.get('_profileId'), false);
		if(node) {
			store.load({node: node});
			if(node.get('checked'))	me.reloadContacts();
		}
	},
	
	onContactViewSave: function(s, success, model) {
		if(!success) return;
		this.reloadContacts();
	},
	
	onContactListViewSave: function(s, success, model) {
		if(!success) return;
		this.reloadContacts();
	},
	
	changeView: function(view) {
		var me = this;
		me.activeView = view;
		me.reloadContacts('A');
	},
	
	queryContacts: function(txt) {
		this.reloadContacts(null, txt);
	},
	
	reloadContacts: function(letter, query) {
		var me = this, sto, pars;
		
		if(me.isActive()) {
			sto = me.gpContacts().getStore();
			pars = {view: me.activeView};
			if(letter !== undefined) Ext.apply(pars, {letter: letter});
			if(query !== undefined) Ext.apply(pars, {query: query});
			WTU.loadWithExtraParams(sto, pars);
		} else {
			me.needsReload = true;
		}
	},
	
	getSelectedContact: function(forceSingle) {
		if(forceSingle === undefined) forceSingle = true;
		var sel = this.getSelectedContacts();
		if(forceSingle && sel.length !== 1) return null;
		return (sel.length > 0) ? sel[0] : null;
	},
	
	getSelectedContacts: function() {
		return this.gpContacts().getSelection();
	},
	
	printSelContacts: function(sel) {
		var me = this;
		me.printContactsDetail(me.selectionIds(sel));
	},
	
	selectionIds: function(sel) {
		var ids = [];
		Ext.iterate(sel, function(rec) {
			ids.push(rec.getId());
		});
		return ids;
	},
	
	deleteSelContacts: function(sel) {
		var me = this,
			sto = me.gpContacts().getStore(),
			isl = sel[0].get('isList') === true,
			ids = me.selectionIds(sel),
			msg, name;
		
		if(sel.length === 1) {
			name = Sonicle.String.join(' ', sel[0].get('firstName'), sel[0].get('lastName'));
			msg = me.res('contact.confirm.delete', Ext.String.ellipsis(name, 40));
		} else {
			msg = me.res('gpcontacts.confirm.delete.selection');
		}
		
		WT.confirm(msg, function(bid) {
			if(bid === 'yes') {
				if(isl) {
					me.deleteContactsLists(ids, {
						cb: function(success) {
							if(success) sto.remove(sel);
							//me.reloadContacts();
						}
					});
				} else {
					me.deleteContacts(ids, {
						cb: function(success) {
							if(success) sto.remove(sel);
							//me.reloadContacts();
						}
					});
				}
			}
		});
	},
	
	moveContactsSel: function(copy, sel) {
		var me = this,
				id = sel[0].get('contactId'),
				isl = sel[0].get('isList') === true,
				pid = sel[0].get('_profileId'),
				cat = sel[0].get('categoryId');
		
		if(isl) {
			me.confirmMoveContactsList0(copy, id, pid, cat, {
				cb: function() {
					me.reloadContacts();
				}
			});
			
		} else {
			me.confirmMoveContact(copy, id, pid, cat, {
				cb: function() {
					me.reloadContacts();
				}
			});
		}
	},
	
	confirmDeleteCategory: function(rec) {
		WT.confirm(this.res('category.confirm.delete', Ext.String.ellipsis(rec.get('text'), 40)), function(bid) {
			if(bid === 'yes') rec.drop();
		}, this);
	},
	
	confirmMoveContact: function(copy, id, ownerId, catId) {
		var me = this,
				vw = me.createCategoryChooser(copy, ownerId, catId);
		
		vw.getView().on('viewok', function(s) {
			me.moveContact(copy, id, s.getVMData().categoryId);
		});
		vw.show();
	},
	
	confirmMoveContactsList: function(copy, id, ownerId, catId) {
		var me = this,
				vw = me.createCategoryChooser(copy, ownerId, catId);
		
		vw.getView().on('viewok', function(s) {
			me.moveContactsList(copy, id, s.getVMData().categoryId);
		});
		vw.show();
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
	
	showContact: function(isList, edit, id) {
		if(isList === true) {
			this.openContactsList(edit, id);
		} else if(isList === false) {
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
	
	deleteContacts: function(ids, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageContacts', {
			params: {
				crud: 'delete',
				ids: WTU.arrayAsParam(ids)
			},
			callback: function(success, json) {
				Ext.callback(opts.cb, opts.scope || me, [success, json]);
			}
		});
	},
	
	deleteContactsLists: function(ids, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageContactsLists', {
			params: {
				crud: 'delete',
				ids: WTU.arrayAsParam(ids)
			},
			callback: function(success, json) {
				Ext.callback(opts.cb, opts.scope || me, [success, json]);
			}
		});
	},
	
	moveContact: function(copy, id, targetCategoryId, opts) {
		opts = opts || {};
		var me = this;
		
		WT.ajaxReq(me.ID, 'ManageContacts', {
			params: {
				crud: 'move',
				copy: copy,
				id: id,
				targetCategoryId: targetCategoryId
			},
			callback: function(success, json) {
				Ext.callback(opts.cb, opts.scope || me, [success, json]);
			}
		});
	},
	
	moveContactsList: function(copy, id, targetCategoryId, opts) {
		opts = opts || {};
		var me = this;
		
		WT.ajaxReq(me.ID, 'ManageContactsLists', {
			params: {
				crud: 'move',
				copy: copy,
				id: id,
				targetCategoryId: targetCategoryId
			},
			callback: function(success, json) {
				Ext.callback(opts.cb, opts.scope || me, [success, json]);
			}
		});
	},
	
	importContacts: function(categoryId) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.ImportContacts', {
					viewCfg: {
						categoryId: categoryId
					}
				});
		
		vwc.getView().on('dosuccess', function() {
			me.reloadContacts();
		});
		vwc.show();
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
					me.reloadContacts();
				}
			}
		});
	},
	
	printContactsDetail: function(ids) {
		var me = this, url;
		url = WTF.processBinUrl(me.ID, 'PrintContactsDetail', {ids: WTU.arrayAsParam(ids)});
		Sonicle.URLMgr.openFile(url, {filename: 'contacts-detail', newWindow: true});
	},
	
	/**
	 * @private
	 */
	updateDisabled: function(action) {
		var me = this,
				dis = me.isDisabled(action);
		
		if(action === 'printContact') {
			me.setActionDisabled(action, dis);
		} else if(action === 'deleteContact') {
			me.setActionDisabled(action, dis);
			me.setActionDisabled('deleteContact2', dis);
		} else {
			me.setActionDisabled(action, dis);
		}
	},
	
	/**
	 * @private
	 */
	isDisabled: function(action) {
		var me = this, sel, er;
		
		switch(action) {
			case 'showContact':
			case 'copyContact':
				sel = me.getSelectedContacts();
				if(sel.length === 1) {
					return false;
				} else {
					return true;
				}
			case 'printContact':
				sel = me.getSelectedContacts();
				if(sel.length === 1 && (sel[0].get('isList') === false)) {
					return false;
				} else {
					return true;
				}
				break;
			case 'moveContact':
				sel = me.getSelectedContacts();
				if(sel.length === 1) {
					er = me.toRightsObj(sel[0].get('_erights'));
					return !er.UPDATE;
				} else {
					return true;
				}
			case 'deleteContact':
				sel = me.getSelectedContacts();
				if(sel.length === 0) {
					return true;
				} else if(sel.length === 1) {
					er = me.toRightsObj(sel[0].get('_erights'));
					return !er.DELETE;
				} else {
					var isl = sel[0].get('isList') === true;
					for(var i=0; i<sel.length; i++) {
						if(sel[i].get('isList') !== isl) return true;
						if(!me.toRightsObj(sel[i].get('_erights')).DELETE) return true;
					}
					return false;
				}
			case 'addContactsListFromSel':
				sel = me.getSelectedContacts();
				if(sel.length === 0) {
					return true;
				} else {
					var isl = sel[0].get('isList') === true;
					for(var i=0; i<sel.length; i++) {
						if(sel[i].get('isList') !== isl) return true;
					}
					return false;
				}
		}
	},
	
	/**
	 * @private
	 */
	buildRcpts: function(view, recs) {
		var rcpts = [], email;
		Ext.iterate(recs, function(rec) {
			email = (view === 'w') ? rec.get('workEmail') : rec.get('homeEmail');
			if(!Ext.isEmpty(email)) rcpts.push({recipientType:'to', recipient: email});
		});
		return rcpts;
	},
	
	/**
	 * @private
	 */
	createCategoryChooser: function(copy, ownerId, catId) {
		var me = this;
		return WT.createView(me.ID, 'view.CategoryChooser', {
			viewCfg: {
				dockableConfig: {
					title: me.res(copy ? 'act-copyContact.lbl' : 'act-moveContact.lbl')
				},
				ownerId: ownerId,
				categoryId: catId
			}
		});
	}
});
