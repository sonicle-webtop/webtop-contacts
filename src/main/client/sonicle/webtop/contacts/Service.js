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
		'Sonicle.webtop.contacts.view.Category'
	],
	
	init: function() {
		var me = this, ies, iitems = [];
		
		me.initActions();
		me.initCxm();
		
		me.on('activate', me.onActivate, me);
		
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
								me.queryContacts(s.getValue());
							}
						}
					},
					listeners: {
						specialkey: function(s, e) {
							if(e.getKey() === e.ENTER) me.queryContacts(s.getValue());
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
		
		ies = ['#','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','*'];
		for(var i=0; i<ies.length; i++) {
			iitems.push({
				text: ies[i],
				handler: function(s) {
					me.refreshContacts((s.getText() === '*') ? '%' : s.getText()+'%');
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
					model: 'WT.model.Empty',
					proxy: WTF.apiProxy(me.ID, 'ManageGridContacts', 'contacts', {
						extraParams: {
							query: null
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
									if(col.dataIndex === 'categoryId') {
										//col.header = me.res('event.gp-planning.recipient.lbl');
										col.xtype = 'socolorcolumn',
										col.header = 'Gruppo';
										col.colorField = 'categoryColor',
										col.displayField = 'categoryName',
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
								me.gpContacts().reconfigure(s, colsInfo);
							}
						}
					}
				},
				columns: [],
				listeners: {
					rowdblclick: function(s, rec) {
						//TODO: handle edit permission
						me.editContact(rec.get('_profileId'), rec.get('id'));
					}
				}
			}, {
				region: 'east',
				xtype: 'toolbar',
				vertical: true,
				overflowHandler: 'scroller',
				defaults: {
					padding: 0
				},
				items: iitems
			}]
		}));
	},
	
	gpContacts: function() {
		return this.getMainComponent().lookupReference('gpcontacts');
	},
	
	initActions: function() {
		var me = this,
				view = me.getOption('view');
		
		me.addAction('new', 'newContact', {
			handler: function() {
				me.getAction('addContact').execute();
			}
		});
		me.addAction('addCategory', {
			handler: function() {
				var node = me.getSelectedFolder();
				if(node) me.addCategory(node.get('_domainId'), node.get('_userId'));
			}
		});
		me.addAction('editCategory', {
			handler: function() {
				var node = me.getSelectedFolder();
				if(node) me.editCategory(node.getId());
			}
		});
		me.addAction('deleteCategory', {
			handler: function() {
				var node = me.getSelectedFolder();
				if(node) me.deleteCategory(node);
			}
		});
		me.addAction('viewAllCategories', {
			iconCls: 'wt-icon-select-all-xs',
			handler: function() {
				me._showHideAllFolders(me.getSelectedRootFolder(), true);
			}
		});
		me.addAction('viewNoneCategories', {
			iconCls: 'wt-icon-select-none-xs',
			handler: function() {
				me._showHideAllFolders(me.getSelectedRootFolder(), false);
			}
		});
		
		
		
		
		
		
		
		
		me.addAction('dummyEdit', {
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
				me.getAction('addCategory'),
				me.getAction('dummyEdit')
				//TODO: azioni altri servizi?
			]
		}));
		
		me.addRef('cxmFolder', Ext.create({
			xtype: 'menu',
			items: [
				me.getAction('editCategory'),
				me.getAction('deleteCategory'),
				me.getAction('addCategory'),
				'-',
				me.getAction('viewAllFolders'),
				me.getAction('viewNoneFolders'),
				'-',
				me.getAction('addContact'),
				me.getRef('uploaders', 'importContacts')
				//TODO: azioni altri servizi?
			],
			listeners: {
				beforeshow: function() {
					var rec = WT.getContextMenuData().folder;
					me.getAction('deleteCategory').setDisabled(rec.get('_builtIn'));
					//TODO: disabilitare azioni se readonly
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
	
	queryContacts: function(txt) {
		this.refreshContacts('%'+txt+'%');
	},
	
	refreshContacts: function(query) {
		var me = this,
				sto = me.gpContacts().getStore();
		if(query) {
			WTU.loadExtraParams(sto, {
				query: query
			});
		} else {
			sto.load();
		}
	},
	
	addCategory: function(domainId, userId) {
		var me = this,
				vwc = WT.createView(me.ID, 'view.Category');
		
		vwc.getView().on('viewsave', me.onCategoryViewSave, me);
		vwc.show(false, function() {
			vwc.getView().beginNew({
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
			vwc.getView().beginEdit({
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
					id: id
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
			return tree.getStore().findNode('_pid', WT.getOption('profileId'), false);
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
			node = tree.getStore().findNode('_pid', WT.getOption('profileId'), false);
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
