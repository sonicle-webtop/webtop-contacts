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
		'Sonicle.menu.TagItem',
		'Sonicle.tree.Column',
		'WTA.ux.data.EmptyModel',
		'WTA.ux.data.SimpleModel',
		'WTA.ux.field.Search',
		'Sonicle.webtop.contacts.model.FolderNode',
		'Sonicle.webtop.contacts.model.GridContact',
		'Sonicle.webtop.contacts.store.View',
		'Sonicle.webtop.contacts.store.GroupBy',
		'Sonicle.webtop.contacts.store.ShowBy',
		//'Sonicle.webtop.contacts.ux.ScrollTooltip',
		'Sonicle.webtop.contacts.ux.grid.column.Contact',
		'Sonicle.webtop.contacts.ux.panel.ContactPreview'
	],
	uses: [
		'Sonicle.picker.Color',
		'WTA.util.FoldersTree',
		'WTA.ux.SelectTagsBox',
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
	
	treeSelEnabled: false,
	
	getApiInstance: function() {
		var me = this;
		if (!me.api) me.api = Ext.create('Sonicle.webtop.contacts.ServiceApi', {service: me});
		return me.api;
	},
	
	init: function() {
		var me = this,
				tagsStore = WT.getTagsStore(),
				scfields = WTA.ux.field.Search.customFieldDefs2Fields(me.getVar('cfieldsSearchable'));
		
		me.activeView = me.getVar('view');
		me.activeGroupBy = me.getVar('groupBy');
		Sonicle.webtop.contacts.model.GridContact.setShowBy(me.getVar('showBy'));
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
					highlightKeywords: ['name', 'company', 'email', 'phone'],
					fields: Ext.Array.push([
						{
							name: 'name',
							type: 'string',
							label: me.res('fld-search.field.name.lbl')
						}, {
							name: 'company',
							type: 'string',
							label: me.res('fld-search.field.company.lbl')
						}, {
							name: 'email',
							type: 'string',
							label: me.res('fld-search.field.email.lbl')
						}, {
							name: 'phone',
							type: 'string',
							label: me.res('fld-search.field.phone.lbl')
						}, {
							name: 'address',
							type: 'string',
							label: me.res('fld-search.field.address.lbl')
						}, {
							name: 'notes',
							type: 'string',
							label: me.res('fld-search.field.notes.lbl')
						}, {
							name: 'tag',
							type: 'tag',
							label: me.res('fld-search.field.tags.lbl'),
							customConfig: {
								valueField: 'id',
								displayField: 'name',
								colorField: 'color',
								store: WT.getTagsStore() // This is filterable, let's do a separate copy!
							}
						}//, {
							//name: 'any',
							//type: 'string',
							//textSink: true,
							//label: me.res('fld-search.field.any.lbl')
						//}
					], scfields),
					tabs: Ext.isEmpty(scfields) ? undefined: [
						{
							title: WT.res('wtsearchfield.main.tit'),
							fields: ['name', 'company', 'email', 'phone', 'address', 'notes', 'tag']
						}, {
							title: WT.res('wtsearchfield.customFields.tit'),
							fields: Ext.Array.pluck(scfields, 'name')
						}
					],
					tooltip: me.res('fld-search.tip'),
					searchTooltip: me.res('fld-search.tip'),
					emptyText: me.res('fld-search.emp'),
					listeners: {
						query: function(s, value, qObj) {
							me.queryContacts(qObj);
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
				//disableSelection: true,
				selModel: {
					mode: 'SIMPLE',
					toggleOnClick: true,
					ignoreRightMouseSelection: true
				},
				hideHeaders: true,
				columns: [{
					xtype: 'sotreecolumn',
					dataIndex: 'text',
					renderer: WTA.util.FoldersTree.coloredCheckboxTreeRenderer({
						defaultText: me.res('category.fld-default.lbl').toLowerCase()
					}),
					flex: 1
				}],
				listeners: {
					checkchange: function(n, ck) {
						n.refreshActive();
					},
					beforeselect: function(s, rec) {
						if (me.treeSelEnabled === false) return false;
					},
					beforedeselect: function(s, rec) {
						if (me.treeSelEnabled === false) return false;
					},
					itemclick: function(s, rec, itm, i, e) {
						if (me.treeSelEnabled === false) {
							if (!e.getTarget(s.checkboxSelector, itm)) {
								s.setChecked(rec, !rec.get('checked'), e);
							}
						}
					},
					itemcontextmenu: function(vw, rec, itm, i, e) {
						if (rec.isFolderRoot()) {
							WT.showContextMenu(e, me.getRef('cxmRootFolder'), {node: rec});
						} else {
							WT.showContextMenu(e, me.getRef('cxmFolder'), {node: rec});
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
							me.pnlPreview().setContacts(me.getSelectedContacts(true));
							me.fldSearch().highlight(me.gpContacts().getEl(), '.x-grid-item-container');
							/*
							var rec = me.getSelectedContact(), cmp;
							if (rec && (s.getById(rec.getId()) === null)) { // Record is selected but no more existent in store
								// I don't know if it is a ExtJs bug, anyway make sure to clear the preview panel and refresh actions affected by strange selections
								cmp = me.pnlPreview();
								if (cmp.isLoaded(rec.getId())) cmp.setContacts(null);
								//me.updateDisabled('deleteContact');
							}
							*/
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
					nameField: 'avatarName',
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
					xtype: 'wtcon-contactcolumn',
					itemId: 'main',
					text: me.res('gpcontacts.contacts.lbl'),
					showCompany: me.activeGroupBy !== 'company',
					tagsStore: tagsStore,
					flex: 2
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
						me.updateDisabled('createEvent');
						me.pnlPreview().setContacts(s.getSelection());
					},
					rowdblclick: function(s, rec) {
						var er = WTA.util.FoldersTree.toRightsObj(rec.get('_erights'));
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
				mys: me,
				tagsStore: tagsStore,
				//customFieldDefs: me.getVar('cfieldsPreviewable'),
				hidden: !WT.plTags.desktop,
				split: true,
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
					},
					editcontact: function(s, isList, id) {
						me.openContactItemUI(true, isList, id);
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
	
	setActiveGroupBy: function(value) {
		this.activeGroupBy = value;
		var gp = this.gpContacts(), col;
		if (gp) {
			col = gp.getColumnManager().getHeaderById('main');
			if (gp) col.setShowCompany(value !== 'company');
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
		
		if (WT.isPermitted(WT.ID, 'TAGS', 'MANAGE')) {
			me.addAct('toolbox', 'manageTags', {
				text: WT.res('act-manageTags.lbl'),
				tooltip: WT.res('act-manageTags.tip'),
				iconCls: 'wt-icon-tag',
				handler: function() {
					me.showManageTagsUI();
				}
			});
		}
		if (WT.isPermitted(WT.ID, 'CUSTOM_FIELDS', 'MANAGE')) {		
			me.addAct('toolbox', 'manageCustomFields', {
				text: WT.res('act-manageCustomFields.lbl'),
				tooltip: WT.res('act-manageCustomFields.tip'),
				iconCls: 'wt-icon-customField',
				handler: function() {
					me.showCustomFieldsUI();
				}
			});
			me.addAct('toolbox', 'manageCustomPanels', {
				text: WT.res('act-manageCustomPanels.lbl'),
				tooltip: WT.res('act-manageCustomPanels.tip'),
				iconCls: 'wt-icon-customPanel',
				handler: function() {
					me.showCustomPanelsUI();
				}
			});
		}
		
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
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.editShare(node.getId());
			}
		});
		me.addAct('manageHiddenCategories', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.manageHiddenCategoriesUI(node);
			}
		});
		me.addAct('hideCategory', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.hideCategoryUI(node);
			}
		});
		me.addAct('addCategory', {
			ignoreSize: true,
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.addCategoryUI(node.get('_domainId'), node.get('_userId'));
			}
		});
		me.addAct('addRemoteCategory', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.addRemoteCategoryUI(node.get('_pid'));
			}
		});
		me.addAct('viewCategoryLinks', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.viewCategoryLinks(node.get('_catId'));
			}
		});
		me.addAct('editCategory', {
			ignoreSize: true,
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.editCategoryUI(node.get('_catId'));
			}
		});
		me.addAct('deleteCategory', {
			ignoreSize: true,
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.deleteCategoryUI(node);
			}
		});
		me.addAct('syncRemoteCategory', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.syncRemoteCategoryUI(node.get('_catId'));
			}
		});
		me.addAct('importContacts', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.importContactsUI(node.get('_catId'));
			}
		});
		me.addAct('applyTags', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.applyCategoryTagsUI(node);
			}
		});
		me.addAct('tags', {
			text: me.res('mni-tags.lbl'),
			tooltip: null,
			menu: {
				xtype: 'sostoremenu',
				useItemIdPrefix: true,
				store: WT.getTagsStore(),
				textField: 'name',
				tagField: 'id',
				bottomStaticItems: [
					'-',
					me.addAct('manageTags', {
						tooltip: null,
						handler: function(s, e) {
							var sel = me.getSelectedContacts();
							if (sel.length > 0) me.manageContactItemTagsUI(sel);
						}
					})
				],
				itemCfgCreator: function(rec) {
					return {
						xclass: 'Sonicle.menu.TagItem',
						color: rec.get('color'),
						hideOnClick: true
					};
				},
				listeners: {
					beforeshow: function(s) {
						s.setCheckedItems(me.toMutualTags(me.getSelectedContacts()) || []);
					},
					click: function(s, itm, e) {
						if (itm.tag) {
							var ids = WTU.collectIds(me.getSelectedContacts());
							me.updateContactsItemsTags(ids, !itm.checked ? 'unset' : 'set', [itm.tag], {
								callback: function(success) {
									if (success) me.reloadContacts();
								}
							});
						}
					}
				}
			}
		});
		me.addAct('categoryColor', {
			text: me.res('mni-categoryColor.lbl'),
			tooltip: null,
			menu: {
				showSeparator: false,
				itemId: 'categoryColor',
				items: [
					{
						xtype: 'socolorpicker',
						colors: WT.getColorPalette('default'),
						tilesPerRow: 11,
						listeners: {
							select: function(s, color) {
								var node = s.menuData.node;
								me.getRef('cxmFolder').hide();
								if (node) me.updateCategoryColorUI(node, Sonicle.String.prepend(color, '#', true));
							}
						}
					},
					'-',
					me.addAct('restoreCategoryColor', {
						tooltip: null,
						handler: function(s, e) {
							var node = e.menuData.node;
							if (node) me.updateCategoryColorUI(node, null);
						}
					})
				]
			}
		});
		var onItemClick = function(s, e) {
			var node = e.menuData.node;
			if (node && s.checked) me.updateCategorySyncUI(node, s.getItemId());
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
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) WTA.util.FoldersTree.activateSingleFolder(node.getFolderRootNode(), node.getId());
			}
		});
		me.addAct('viewAllFolders', {
			tooltip: null,
			iconCls: 'wt-icon-select-all',
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) WTA.util.FoldersTree.setActiveAllFolders(node.getFolderRootNode(), true);
			}
		});
		me.addAct('viewNoneFolders', {
			tooltip: null,
			iconCls: 'wt-icon-select-none',
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) WTA.util.FoldersTree.setActiveAllFolders(node.getFolderRootNode(), false);
			}
		});
		me.addAct('showContact', {
			text: WT.res('act-open.lbl'),
			tooltip: null,
			handler: function() {
				var rec = me.getSelectedContact(), er;
				if (rec) {
					er = WTA.util.FoldersTree.toRightsObj(rec.get('_erights'));
					me.openContactItemUI(er.UPDATE, rec);
				}
			}
		});
		me.addAct('addContact', {
			ignoreSize: true,
			tooltip: null,
			handler: function(s, e) {
				var node = (e && e.menuData) ? e.menuData.node : WTA.util.FoldersTree.getTargetFolder(me.trFolders());
				if (node) me.addContactUI(node.get('_pid'), node.get('_catId'));
			}
		});
		me.addAct('addContactsList', {
			ignoreSize: true,
			tooltip: null,
			handler: function(s, e) {
				var node = (e && e.menuData) ? e.menuData.node : WTA.util.FoldersTree.getTargetFolder(me.trFolders());
				if (node) me.addContactsListUI(node.get('_pid'), node.get('_catId'));
			}
		});
		me.addAct('sendContact', {
			tooltip: null,
			handler: function() {
				var sel = me.getSelectedContacts();
				if (sel.length > 0) me.sendContactsAsEmail(WTU.collectIds(sel, 'id'));
			}
		});
		me.addAct('addContactsListFromSel', {
			tooltip: null,
			handler: function() {
				var node = WTA.util.FoldersTree.getTargetFolder(me.trFolders()),
						er = WTA.util.FoldersTree.toRightsObj(node.get('_erights')),
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
				if (sel.length > 0) me.printContactsDetail(WTU.collectIds(sel, 'id'));
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
		me.addAct('createEvent', {
			tooltip: null,
			handler: function() {
				var rec = me.getSelectedContact();
				if (rec) me.createEventUI(rec);
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
					var rec = s.menuData.node,
							mine = rec.isPersonalNode(),
							rr = WTA.util.FoldersTree.toRightsObj(rec.get('_rrights'));
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
				me.getAct('applyTags'),
				'-',
				me.getAct('addContact'),
				me.getAct('addContactsList'),
				me.getAct('importContacts')
				//TODO: azioni altri servizi?
			],
			listeners: {
				beforeshow: function(s) {
					var FT = WTA.util.FoldersTree,
							rec = s.menuData.node,
							mine = rec.isPersonalNode(),
							rr = FT.toRightsObj(rec.get('_rrights')),
							fr = FT.toRightsObj(rec.get('_frights')),
							er = FT.toRightsObj(rec.get('_erights'));
					me.getAct('editCategory').setDisabled(!fr.UPDATE);
					me.getAct('deleteCategory').setDisabled(!fr.DELETE || rec.get('_builtIn'));
					me.getAct('editSharing').setDisabled(!rr.MANAGE);
					me.getAct('importContacts').setDisabled(!er.CREATE);
					me.getAct('hideCategory').setDisabled(mine);
					me.getAct('restoreCategoryColor').setDisabled(mine);
					me.getAct('syncRemoteCategory').setDisabled(!Sonicle.webtop.contacts.view.Category.isRemote(rec.get('_provider')));
					me.getAct('applyTags').setDisabled(!er.UPDATE);
					
					var picker = s.down('menu#categoryColor').down('colorpicker');
					picker.menuData = s.menuData; // Picker's handler doesn't carry the event, injects menuData inside the picket itself
					picker.select(rec.get('_color'), true);
					s.down('menu#categorySync').getComponent(rec.get('_sync')).setChecked(true);
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
				me.getAct('tags'),
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
				me.getAct('createEvent'),
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
		me.updateDisabled('createEvent');
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
	
	confirmEventCreation: function(msg, cb, scope) {
		var me = this,
		    defaultValue = me.activeView;
		
		if(defaultValue === 'list')
			defaultValue = 'work';
		
		WT.confirm(msg, cb, scope, {
			buttons: Ext.Msg.OKCANCEL,
			instClass: 'Sonicle.webtop.contacts.ux.RecurringConfirmBox',
			instConfig: {
				homeText: me.res('contact.type.home'),
				workText: me.res('contact.type.work'),
				otherText: me.res('contact.type.other')
			},
			config: {
				value: defaultValue
			}
		});
	},
	
	createEvent: function(type, contactId, contactInfo) {
		var me = this;
		me.getContactInformationForEvent(contactId, type, {
			callback: function(success, json) {
				if (success) {
					var calSvc = WT.getServiceApi('com.sonicle.webtop.calendar');
					if (calSvc) {
						var SoString = Sonicle.String,
								data = json.data || {},
								lArgs = [', '], dArgs = ['\n'];

						lArgs.push(data.address);
						lArgs.push(data.postalCode);
						lArgs.push(data.city);
						lArgs.push(data.state);
						lArgs.push(data.country);
						dArgs.push(SoString.join(' ', contactInfo.firstName, contactInfo.lastName));
						dArgs.push(SoString.join(', ', contactInfo.company, data.department, contactInfo.role));
						dArgs.push(data.email);
						dArgs.push(data.mobile);
						dArgs.push(data.telephone);

						calSvc.addEvent({
							location: SoString.join.apply(me, lArgs),
							description: SoString.join.apply(me, dArgs)
						});
					}
				}
			}
		});
	},
	
	getContactInformationForEvent: function(contactId, type, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'GetContactInformationForEventCreation', {
			params: {
				id: contactId,
				type: type
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
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
		me.setActiveGroupBy(groupBy);
		if (reload !== false) {
			me.gpContacts().getStore().setGroupField(me.calcGroupField(groupBy));
		}	
	},
	
	reloadContacts: function(opts) {
		opts = opts || {};
		var me = this, sto, pars = {};
		// NB: opts.query is not persisted if service is not active!
		if (Ext.isString(opts.groupBy)) me.setActiveGroupBy(opts.groupBy);
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
	
	queryContacts: function(query) {
		var isString = Ext.isString(query),
			obj = {
				allText: isString ? query : query.anyText,
				conditions: isString ? [] : query.conditionArray
			};
		this.reloadContacts({query: Ext.JSON.encode(obj)});
	},
	
	getSelectedContacts: function(forceVisible) {
		if (forceVisible === undefined) forceVisible = false;
		var gp = this.gpContacts(),
				view = gp.getView(),
				sel = gp.getSelection(),
				arr = [];
		if (sel && (forceVisible === true)) {
			for(var i=0; i<sel.length; i++) {
				if (view.getNode(sel[i])) arr.push(sel[i]);
			}
			return arr;
		} else {
			return sel;
		}
	},
	
	getSelectedContact: function(forceSingle) {
		if (forceSingle === undefined) forceSingle = true;
		var sel = this.getSelectedContacts();
		if (forceSingle && sel.length !== 1) return null;
		return (sel.length > 0) ? sel[0] : null;
	},
	
	showManageTagsUI: function() {
		var me = this,
				vw = WT.createView(WT.ID, 'view.Tags', {
					swapReturn: true,
					viewCfg: {
						enableSelection: false
					}
				});
		vw.on('viewclose', function(s) {
			if (s.syncCount > 0) me.reloadContacts();
		});
		vw.showView();
	},
	
	showCustomFieldsUI: function() {
		var me = this;
		WT.createView(WT.ID, 'view.CustomFields', {
			swapReturn: true,
			viewCfg: {
				serviceId: me.ID,
				serviceName: me.getName()
			}
		}).showView();
	},
	
	showCustomPanelsUI: function() {
		var me = this;
		WT.createView(WT.ID, 'view.CustomPanels', {
			swapReturn: true,
			viewCfg: {
				serviceId: me.ID,
				serviceName: me.getName()
			}
		}).showView();
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
						if (success) {
							me.loadRootNode(node.get('_pid'));
							node.setActive(false);
						}
					}
				});
			}
		}, this);
	},
	
	updateCategoryColorUI: function(node, color) {
		var me = this;
		me.updateCategoryColor(node.get('_catId'), color, {
			callback: function(success) {
				if (success) {
					me.loadRootNode(node.get('_pid'));
					if (node.isActive()) me.reloadContacts();
				}
			}
		});
	},
	
	updateCategorySyncUI: function(node, sync) {
		var me = this;
		me.updateCategorySync(node.get('_catId'), sync, {
			callback: function(success) {
				if (success) {
					me.loadRootNode(node.get('_pid'));
				}
			}
		});
	},
	
	applyCategoryTagsUI: function(node) {
		var me = this, op;
		WT.confirmSelectTags(function(bid, value) {
			if (bid === 'yes' || bid === 'no') {
				op = (bid === 'yes') ? 'set' : ((bid === 'no') ? 'unset' : ''); 
				WT.confirm(me.res('category.confirm.tags.' + op, Ext.String.ellipsis(node.get('text'), 40)), function(bid2) {
					if (bid2 === 'yes') {
						me.updateCategoryTags(node.get('_catId'), op, value, {
							callback: function(success) {
								if (success) me.reloadContacts();
							}
						});
					}
				}, this);
			}
		}, me);
	},
	
	/*
	updateCheckedFoldersUI: function(node, checked) {
		var me = this;
		me.updateCheckedFolders(node.getId(), checked, {
			callback: function(success) {
				if(success) {
					if (node.get('_active')) {
						me.reloadContacts();
					} else {
						if (checked) node.setActive(checked);
					}
				}
			}
		});
	},
	*/
	
	openContactItemUI: function(edit, isList, contactItemId) {
		var me = this;
		if ((arguments.length === 2) && (isList.isModel)) {
			contactItemId = isList.get('id');
			isList = isList.get('isList') === true;
		}
		if (isList) {
			me.openContactsList(edit, contactItemId, {
				callback: function(success, mo) {
					if (success && edit) {
						me.reloadContacts();
					}
				}
			});
		} else {
			me.openContact(edit, contactItemId, {
				callback: function(success, mo) {
					if (success && edit) {
						me.reloadContacts();
					}
				}
			});
		}
	},
	
	deleteContactItemsUI: function(recs) {
		var me = this,
			ids = WTU.collectIds(recs),
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
			ids = WTU.collectIds(recs),
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
	
	manageContactItemTagsUI: function(recs) {
		var me = this,
				ids = WTU.collectIds(recs),
				tags = me.toMutualTags(recs),
				vw = WT.createView(WT.ID, 'view.Tags', {
					swapReturn: true,
					viewCfg: {
						data: {
							selection: tags
						}
					}
				});
		vw.on('viewok', function(s, data) {
			if (Sonicle.String.difference(tags, data.selection).length > 0) {
				me.updateContactsItemsTags(ids, 'reset', data.selection, {
					callback: function(success) {
						if (success) me.reloadContacts();
					}
				});
			}	
		});
		//FIXME: reload contacts (like showManageTagsUI) when closing view with reload. Keep attention to avoid multiple reloads.
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
	
	expandRecipientsList : function(address, opts) {
		var me = this;
		
		WT.ajaxReq(me.ID, 'ExpandRecipientsList', {
			params: {
				address: address
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope, [success, json]);
			}
		});
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
	
	createEventUI: function(rec) {
		var me = this;
		me.confirmEventCreation(me.res('contact.confirm.eventCreation'), function(bid, value) {
			if (bid === 'ok') {
				me.createEvent(value, rec.get('id'), {
					firstName: rec.get('firstName'),
					lastName: rec.get('lastName'),
					company: rec.get('company'),
					role: rec.get('function')
				});
			}
		}, me);
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
	
	updateCategoryColor: function(categoryId, color, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'SetCategoryColor', {
			params: {
				id: categoryId,
				color: color
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	updateCategorySync: function(categoryId, sync, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'SetCategorySync', {
			params: {
				id: categoryId,
				sync: sync
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	updateCategoryTags: function(categoryId, op, tagIds, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageCategory', {
			params: {
				crud: 'updateTag',
				id: categoryId,
				op: op,
				tags: WTU.arrayAsParam(tagIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope, [success, json]);
			}
		});
	},
	
	/*
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
	*/
	
	deleteContactsItems: function(contactIds, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'delete',
				ids: WTU.arrayAsParam(contactIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json]);
			}
		});
	},
	
	updateContactsItemsTags: function(contactIds, op, tagIds, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'updateTag',
				ids: WTU.arrayAsParam(contactIds),
				op: op,
				tags: WTU.arrayAsParam(tagIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope, [success, json]);
			}
		});
	},
	
	moveContactsItems: function(copy, contactIds, targetCategoryId, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'move',
				copy: copy,
				ids: WTU.arrayAsParam(contactIds),
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
				n = WTA.util.FoldersTree.getTargetFolder(me.trFolders()),
				obj = {};
		
		obj._profileId = n.get('_pid');
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
		if (Ext.isDefined(cnt.displayName)) obj.displayName = cnt.displayName;
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
		if (Ext.isDefined(cnt.email3)) obj.email3 = cnt.email3;
		if (Ext.isDefined(cnt.instantMsg1)) obj.instantMsg1 = cnt.instantMsg1;
		if (Ext.isDefined(cnt.instantMsg2)) obj.instantMsg2 = cnt.instantMsg2;
		if (Ext.isDefined(cnt.instantMsg3)) obj.instantMsg3 = cnt.instantMsg3;
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
		if (Ext.isDefined(cnt.otherAddress)) obj.otherAddress = cnt.otherAddress;
		if (Ext.isDefined(cnt.otherPostalCode)) obj.otherPostalCode = cnt.otherPostalCode;
		if (Ext.isDefined(cnt.otherCity)) obj.otherCity = cnt.otherCity;
		if (Ext.isDefined(cnt.otherState)) obj.otherState = cnt.otherState;
		if (Ext.isDefined(cnt.otherCountry)) obj.otherCountry = cnt.otherCountry;
		if (Ext.isDefined(cnt.company)) obj.company = cnt.company;
		if (Ext.isDefined(cnt.function)) obj.function = cnt.function;
		if (Ext.isDefined(cnt.department)) obj.department = cnt.department;
		if (Ext.isDefined(cnt.birthday)) obj.birthday = cnt.birthday;
		if (Ext.isDefined(cnt.anniversary)) obj.anniversary = cnt.anniversary;
		if (Ext.isDefined(cnt.url)) obj.url = cnt.url;
		if (Ext.isDefined(cnt.notes)) obj.notes = cnt.notes;
		if (Ext.isDefined(cnt.picture)) obj.picture = cnt.picture;
		
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
				vw = WT.createView(me.ID, 'view.Contact', {
					swapReturn: true,
					viewCfg: {
						uploadTag: opts.uploadTag
					}
				});	
		
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
				n = WTA.util.FoldersTree.getTargetFolder(me.trFolders()),
				obj = {};
		
		obj._profileId = n.get('_pid');
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
			var me = this,
					FT = WTA.util.FoldersTree,
					sel, er;

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
						er = FT.toRightsObj(sel[0].get('_erights'));
						return !er.DELETE;
					} else {
						for (var i=0; i<sel.length; i++) {
							if (!FT.toRightsObj(sel[i].get('_erights')).DELETE) return true;
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
				case 'createEvent': 
					sel = me.getSelectedContacts();
					if(sel.length === 1) {
						return false;
					}
					else {
						return true;
					}
			}
		},
		
		toMutualTags: function(recs) {
			var arr, ids;
			Ext.iterate(recs, function(rec) {
				ids = Sonicle.String.split(rec.get('tags'), '|');
				if (!arr) {
					arr = ids;
				} else {
					arr = Ext.Array.intersect(arr, ids);
				}
				if (arr.length === 0) return false;
			});
			return arr;
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
