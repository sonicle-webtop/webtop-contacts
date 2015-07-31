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
		'WT.model.Empty',
		'Sonicle.webtop.contacts.model.FolderNode',
		'Sonicle.webtop.contacts.view.Folder'
	],
	
	init: function() {
		var me = this;
		
		me.initActions();
		me.initCxm();
		
		//me.on('activate', me.onActivate, me);
		
		me.setToolbar(Ext.create({
			xtype: 'toolbar',
			
			items: [
				
				'->',
				{
					xtype: 'textfield',
					width: 200,
					triggers: {
						search: {
							cls: Ext.baseCSSPrefix + 'form-search-trigger',
							handler: function(s) {
								me.refreshContacts(s.getValue());
							}
						}
					},
					listeners: {
						specialkey: function(s, e) {
							if(e.getKey() === e.ENTER) {
								me.refreshContacts(s.getValue());
							}
						}
					}
				}
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
							me._showHideFolder(n, ck);
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
		
		me.setMainComponent(Ext.create({
			xtype: 'gridpanel',
			store: {
				model: 'WT.model.Empty',
				proxy: WTF.apiProxy(me.ID, 'ManageGridContacts', 'contacts', {
					extraParams: {
						query: 'Bul'
					}
				}),
				listeners: {
					metachange: function(s, meta) {
						if(meta.colsInfo) {
							var colsInfo = [];
							colsInfo.push({
								xtype: 'soiconcolumn',
								iconField: function(rec) {
									return WTF.cssIconCls(me.XID, (rec.get('listId')>0) ? 'contact-list' : 'contact', 'xs');
								},
								iconSize: WTU.imgSizeToPx('xs'),
								width: 30
							});
							
							Ext.iterate(meta.colsInfo, function(col,i) {
								if(col.dataIndex === 'folderId') {
									//col.header = me.res('event.gp-planning.recipient.lbl');
									col.xtype = 'socolorcolumn',
									col.header = 'Gruppo';
									col.colorField = 'folderColor',
									col.displayField = 'folderName',
									col.width = 100;
									col.hidden = false;
								}
								colsInfo.push(col);
							});
							
							/*
							var colsInfo = [];
							Ext.iterate(meta.colsInfo, function(col,i) {
								if(col.dataIndex === 'recipient') {
									col.header = me.mys.res('event.gp-planning.recipient.lbl');
									col.locked = true;
									col.width = 200;

									// Add this column as is... skip nesting
									colsInfo.push(col);

								} else {
									col.renderer = WTF.clsColRenderer({
										clsPrefix: 'wtcal-planning-',
										moreCls: (col.overlaps) ? 'wtcal-planning-overlaps' : null
									});
									col.lockable = false;
									col.sortable = false;
									col.hideable = false;
									col.menuDisabled = true;
									col.draggable = false;
									col.width = 55;

									// Nest this column under right day date
									if(colsInfo[colsInfo.length-1].date !== col.date) {
										colsInfo.push({
											date: col.date,
											text: col.date,
											columns: []
										});
									}
									colsInfo[colsInfo.length-1].columns.push(col);
								}
							});
							*/
							me.getMainComponent().reconfigure(s, colsInfo);
						}
					}
				}
			},
			columns: []
		}));
		
		Ext.defer(function() {
			me.refreshContacts();
		}, 1000);
	},
	
	initActions: function() {
		var me = this,
				view = me.getOption('view');
		
		me.addAction('new', 'newContact', {
			handler: function() {
				me.getAction('addContact').execute();
			}
		});
		me.addAction('addFolder', {
			handler: function() {
				var node = me.getSelectedFolder();
				if(node) me.addFolder(node.get('_domainId'), node.get('_userId'));
			}
		});
		me.addAction('editFolder', {
			handler: function() {
				var node = me.getSelectedFolder();
				if(node) me.editFolder(node.getId());
			}
		});
		me.addAction('deleteFolder', {
			handler: function() {
				var node = me.getSelectedFolder();
				if(node) me.deleteFolder(node);
			}
		});
		me.addAction('viewAllFolders', {
			iconCls: 'wt-icon-select-all-xs',
			handler: function() {
				me._showHideAllFolders(me.getSelectedRootFolder(), true);
			}
		});
		me.addAction('viewNoneFolders', {
			iconCls: 'wt-icon-select-none-xs',
			handler: function() {
				me._showHideAllFolders(me.getSelectedRootFolder(), false);
			}
		});
		me.addAction('editContact', {
			handler: function() {
				//var node = me.getSelectedFolder();
				//if(node) me.addContact('matteo.albinola@sonicleldap', 1);
				me.editContact('matteo.albinola@sonicleldap', 1);
			}
		});
	},
	
	initCxm: function() {
		var me = this;
		
		me.addRef('cxmRootFolder', Ext.create({
			xtype: 'menu',
			items: [
				me.getAction('addFolder'),
				'-',
				me.getAction('addContact'),
				me.getAction('editContact')
				//TODO: azioni altri servizi?
			]
		}));
		
		me.addRef('cxmFolder', Ext.create({
			xtype: 'menu',
			items: [
				me.getAction('editFolder'),
				me.getAction('deleteFolder'),
				'-',
				me.getAction('addFolder'),
				me.getRef('uploaders', 'importContacts'),
				'-',
				me.getAction('viewAllFolders'),
				me.getAction('viewNoneFolders'),
				'-',
				me.getAction('addContact')
				//TODO: azioni altri servizi?
			],
			listeners: {
				beforeshow: function() {
					var rec = WT.getContextMenuData().folder;
					me.getAction('deleteFolder').setDisabled(rec.get('_builtIn'));
					//TODO: disabilitare azioni se readonly
				}
			}
		}));
	},
	
	onFolderViewSave: function(s, success, model) {
		if(!success) return;
		var me = this,
				store = me.getRef('folderstree').getStore(),
				node;
		
		// Look for root folder and reload it!
		node = store.getNodeById(model.get('_profileId'));
		if(node) store.load({node: node});
	},
	
	addFolder: function(domainId, userId) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.Folder');
		
		vwc.getView().on('viewsave', me.onFolderViewSave, me);
		vwc.show(false, function() {
			vwc.getView().beginNew({
				data: {
					domainId: domainId,
					userId: userId
				}
			});
		});
	},
	
	editFolder: function(folderId) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.Folder');
		
		vwc.getView().on('viewsave', me.onFolderViewSave, me);
		vwc.show(false, function() {
			vwc.getView().beginEdit({
				data: {
					folderId: folderId
				}
			});
		});
	},
	
	deleteFolder: function(rec) {
		WT.confirm(this.res('folder.confirm.delete', rec.get('text')), function(bid) {
			if(bid === 'yes') rec.drop();
		}, this);
	},
	
	refreshContacts: function(query) {
		var me = this,
				sto = me.getMainComponent().getStore();
		if(query) {
			WTU.loadExtraParams(sto, {
				query: query
			});
		} else {
			sto.load();
		}
	},
	
	editContact: function(profileId, id) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.Contact', {
					viewCfg: {
						profileId: profileId
					}
				});
		
		//vwc.getView().on('viewsave', me.onEventViewSave, me);
		vwc.show(false, function() {
			vwc.getView().beginEdit({
				data: {
					contactId: id
				}
			});
		});
	},
	
	/**
	 * @private
	 * Returns selected root folder. If force param is 'true', this method 
	 * returns a default value if no selection is available.
	 * @param {Boolean} [force=false] 'true' to always return a value.
	 * @returns {Ext.data.NodeInterface}
	 */
	getSelectedRootFolder: function(force) {
		var tree = this.getRef('folderstree'),
				sel = tree.getSelection();
		
		if(sel.length === 0) {
			if(!force) return null;
			// As default returns myFolder, which have id equals to principal option
			return tree.getStore().getNodeById(WT.getOption('principal'));
		}
		return (sel[0].get('_type') === 'root') ? sel[0] : sel[0].parentNode;
	},
	
	/*
	 * @private
	 * Returns selected folder (calendar). If no selection is available, 
	 * this method tries to return the default folder and then the built-in one.
	 * @returns {Ext.data.NodeInterface}
	 */
	getSelectedFolder: function() {
		var me = this,
				tree = me.getRef('folderstree'),
				sel = tree.getSelection(),
				node;
		
		if(sel.length > 0) {
			if(sel[0].get('_type') === 'root') {
				return me.getFolderByRoot(sel[0]);
			} else {
				return sel[0];
			}
		} else {
			node = tree.getStore().getNodeById(WT.getOption('principal'));
			if(node) return me.getFolderByRoot(node);
		}
		return null;
	},
	
	/*
	 * @private
	 */
	getFolderByRoot: function(rootNode) {
		var cal = this.getDefaultFolder(rootNode);
		return (cal) ? cal : this.getBuiltInFolder(rootNode);
	},
	
	/*
	 * @private
	 */
	getDefaultFolder: function(rootNode) {
		return rootNode.findChildBy(function(n) {
			return (n.get('_default') === true);
		});
	},
	
	/*
	 * @private
	 */
	getBuiltInFolder: function(rootNode) {
		return rootNode.findChildBy(function(n) {
			return (n.get('_builtIn') === true);
		});
	},
	
	_showHideFolder: function(node, show) {
		node.beginEdit();
		node.set('_visible', show);
		node.endEdit();
	},
	
	_showHideAllFolders: function(parent, show) {
		var me = this,
				store = parent.getTreeStore();
		
		store.suspendAutoSync();
		parent.cascadeBy(function(n) {
			if(n !== parent) {
				n.set('checked', show);
				me._showHideFolder(n, show);
			}
		});
		store.resumeAutoSync();
		store.sync();
	}
});
