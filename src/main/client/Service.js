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
		'Sonicle.menu.Header',
		'Sonicle.tree.Column',
		'WTA.ux.data.EmptyModel',
		'WTA.ux.data.SimpleModel',
		'WTA.ux.field.Search',
		'WTA.ux.menu.TagMenu',
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
		'Sonicle.Data',
		'Sonicle.String',
		'Sonicle.Utils',
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
		me.onPushMessage('remoteSyncResult', function(msg) {
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
								store: WT.getTagsStore(), // This is filterable, let's do a separate copy!
								valueField: 'id',
								displayField: 'name',
								colorField: 'color',
								sourceField: 'source',
								sourceCls: 'wt-source'
							}
						}, {
							name: 'lists',
							type: 'boolean',
							boolKeyword: 'only',
							label: me.res('fld-search.field.onlyLists.lbl')
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
							fields: ['name', 'company', 'email', 'phone', 'address', 'notes', 'tag', 'lists']
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
						defaultText: me.res('trfolders.default')
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
		
		var viewGroup = Ext.id(null, 'view-'),
				groupByGroup = Ext.id(null, 'groupby-');
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
					hdNotCollapsibleCls: Ext.baseCSSPrefix + 'grid-group-hd-not-collapsible' + ' wt-theme-bg-alt',
					groupHeaderTpl: [
						'<span class="{[this.getSpanCls(values)]}">{name}</span>',
						{
							getSpanCls: function(values) {
								return 'wt-theme-text-header1' + ((values.groupField.indexOf('letter') !== -1) ? ' wtcon-group-letter' : '');
							}
						}
					]
				}/*, {
					id: 'scrolltooltip',
					ftype: 'wtscrolltooltip'
				}*/],
				tbar: [
					{
						xtype: 'button',
						iconCls: 'wtcon-icon-viewWork',
						tooltip: me.res('gpcontacts.viewOptions.view.lbl') + ': ' + me.res('store.view.work'),
						toggleGroup: viewGroup,
						enableToggle: true,
						allowDepress: false,
						pressed: me.activeView === 'work',
						toggleHandler: function() {
							me.reloadContacts({view: 'work'});
						}
					}, {
						xtype: 'button',
						iconCls: 'wtcon-icon-viewHome',
						tooltip: me.res('gpcontacts.viewOptions.view.lbl') + ': ' + me.res('store.view.home'),
						toggleGroup: viewGroup,
						enableToggle: true,
						allowDepress: false,
						pressed: me.activeView === 'home',
						toggleHandler: function() {
							me.reloadContacts({view: 'home'});
						}
					},
					'->',
					me.getAct('deleteContact2'),
					'-',
					{
						xtype: 'button',
						iconCls: 'wt-icon-viewOptions',
						menu: {
							items: [
								{
									xtype: 'somenuheader',
									text: me.res('gpcontacts.viewOptions.groupBy.lbl')
								}, {
									itemId: 'groupby-alpha',
									group: groupByGroup,
									text: me.res('store.groupBy.alpha'),
									checked: false,
									checkHandler: function(s, checked) {
										if (checked) me.applyGroupBy(Sonicle.String.removeStart(s.getItemId(), 'groupby-'));
									}
								}, {
									itemId: 'groupby-company',
									group: groupByGroup,
									text: me.res('store.groupBy.company'),
									checked: false,
									checkHandler: function(s, checked) {
										if (checked) me.applyGroupBy(Sonicle.String.removeStart(s.getItemId(), 'groupby-'));
									}
								}
							],
							listeners: {
								beforeshow: function(s) {
									var itm = s.getComponent('groupby-' +  me.activeGroupBy);
									if (itm) itm.setChecked(true);
								}
							}
						}
					},
					' '
				],
				listeners: {
					selectionchange: function(s) {
						me.updateDisabled('showContact');
						me.updateDisabled('printContact');
						me.updateDisabled('copyContact');
						me.updateDisabled('moveContact');
						me.updateDisabled('deleteContact');
						me.updateDisabled('tags');
						me.updateDisabled('addContactsListFromSel');
						me.updateDisabled('addToContactsListFromSel');
						me.updateDisabled('callTelephone');
						me.updateDisabled('callMobile');
						me.updateDisabled('sendSms');
						me.updateDisabled('createEvent');
						me.updateDisabled('sendContact');
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
				minWidth: 350
			}]
		}));
	},
	
	hasMailchimp: function() {
		return this.getVar("hasMailchimp");
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
		
		me.addAct('toolbox', 'manageTags', {
			text: WT.res('act-manageTags.lbl'),
			tooltip: WT.res('act-manageTags.tip'),
			iconCls: 'wt-icon-tag',
			handler: function() {
				me.showManageTagsUI();
			}
		});
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
		if (me.hasMailchimp()) {
			me.addAct('syncMailchimp', {
				tooltip: null,
				iconCls: 'wtcon-icon-mailchimp',
				handler: function(s, e) {
					var node = e.menuData.node;
					if (node) me.syncMailchimpUI(node);
				}
			});
		}
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
				if (node) me.addRemoteCategoryUI(node.getProfileId());
			}
		});
		me.addAct('viewCategoryLinks', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.viewCategoryLinks(node.getFolderId());
			}
		});
		me.addAct('editCategory', {
			ignoreSize: true,
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.editCategoryUI(node.getFolderId());
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
				if (node) me.syncRemoteCategoryUI(node.getFolderId());
			}
		});
		me.addAct('importContacts', {
			tooltip: null,
			handler: function(s, e) {
				var node = e.menuData.node;
				if (node) me.importContactsUI(node.getFolderId());
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
				xtype: 'wttagmenu',
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
				restoreSelectedTags: function() {
					return me.toMutualTags(me.getSelectedContacts());
				},
				listeners: {
					tagclick: function(s, tagId, checked) {
						var ids = Sonicle.Data.collectValues(me.getSelectedContacts());
						me.updateContactsItemsTags(ids, !checked ? 'unset' : 'set', [tagId], {
							callback: function(success) {
								if (success) me.reloadContacts();
							}
						});
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
				items: [
					{
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
				var folderId = (e && e.menuData) ? e.menuData.node.getFolderId() : null,
						node = WTA.util.FoldersTree.getFolderForAdd(me.trFolders(), folderId);
				if (node) me.addContactUI(node.getFolderId());
			}
		});
		me.addAct('addContactsList', {
			ignoreSize: true,
			tooltip: null,
			handler: function(s, e) {
				var folderId = (e && e.menuData) ? e.menuData.node.getFolderId() : null,
						node = WTA.util.FoldersTree.getFolderForAdd(me.trFolders(), folderId);
				if (node) me.addContactsListUI(node.getFolderId());
			}
		});
		me.addAct('sendContact', {
			tooltip: null,
			handler: function() {
				var sel = me.getSelectedContacts();
				if (sel.length > 0) me.sendContactsAsEmail(Sonicle.Data.collectValues(sel, 'id'));
			}
		});
		me.addAct('addContactsListFromSel', {
			tooltip: null,
			handler: function() {
				var node = WTA.util.FoldersTree.getDefaultOrBuiltInFolder(me.trFolders()),
						er = WTA.util.FoldersTree.toRightsObj(node.get('_erights')),
						rcpts = me.toRcpts(me.getSelectedContacts());
				
				if (er.CREATE) {
					me.addContactsListUI(node.getFolderId(), rcpts);
				} else {
					me.addContactsListUI(null, rcpts);
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
				if (sel.length > 0) me.printContactsDetail(Sonicle.Data.collectValues(sel, 'id'));
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
		var me = this,
			rootItems = [
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
			folderItems = [
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
							me.getAct('categorySync'),
							{
								itemId: 'defaultCategory',
								text: me.res('mni-defaultCategory.lbl'),
								group: 'defaultCategory',
								checked: false,
								listeners: {
									click: function(s, e) {
										var node = e.menuData.node;
										if (node && s.checked) me.updateDefaultCategoryUI(node);
									}
								}
							}
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
			];
		
		if (me.hasMailchimp()) {
			rootItems.push('-');
			rootItems.push(me.getAct('syncMailchimp'));
			folderItems.push('-');
			folderItems.push(me.getAct('syncMailchimp'));
		}
		me.addRef('cxmRootFolder', Ext.create({
			xtype: 'menu',
			items: rootItems,
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
			items: folderItems,
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
					
					var defltCmp = s.down('menuitem#defaultCategory');
					defltCmp.setChecked(rec.get('_default'));
					defltCmp.setDisabled(!er.CREATE);
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
		me.updateDisabled('sendContact');
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
			callback: function(success, data) {
				if (success) {
					var calSvc = WT.getServiceApi('com.sonicle.webtop.calendar');
					if (calSvc) {
						var SoString = Sonicle.String,
								lArgs = [', '], dArgs = ['\n'];
						data = data || {};
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
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
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
			preventDuplicates: true,
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
			preventDuplicates: true,
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
					callback: function(success, data, json) {
						WT.handleError(success, json);
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
				me.updateCategoryVisibility(node.getFolderId(), true, {
					callback: function(success) {
						if (success) {
							me.loadRootNode(node.getProfileId());
							node.setActive(false);
						}
					}
				});
			}
		}, this);
	},
	
	updateCategoryColorUI: function(node, color) {
		var me = this;
		me.updateCategoryColor(node.getFolderId(), color, {
			callback: function(success) {
				if (success) {
					me.loadRootNode(node.getProfileId());
					if (node.isActive()) me.reloadContacts();
				}
			}
		});
	},
	
	updateCategorySyncUI: function(node, sync) {
		var me = this;
		me.updateCategorySync(node.getFolderId(), sync, {
			callback: function(success) {
				if (success) {
					me.loadRootNode(node.getProfileId());
				}
			}
		});
	},
	
	updateDefaultCategoryUI: function(node) {
		var me = this;
		me.updateDefaultCategory(node.getFolderId(), {
			callback: function(success, data) {
				if (success) {
					var FT = WTA.util.FoldersTree,
							tree = me.trFolders(),
							nn = FT.getFolderById(tree, data);
					if (nn) FT.setFolderAsDefault(tree, nn.getId());
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
						me.updateCategoryTags(node.getFolderId(), op, value, {
							callback: function(success) {
								if (success) me.reloadContacts();
							}
						});
					}
				}, this);
			}
		}, me);
	},
	
	syncMailchimpUI: function(node) {
		var me = this,
			srcPid=node.get("_pid"),
			srcCatId=node.get("_catId");
		
		WT.ajaxReq(me.ID, 'MailchimpGetUserSettings', {
			params: {
				srcPid: srcPid,
				srcCatId: srcCatId
			},
			callback: function(success, json) {
				var d=json.data,
					vw = WT.createView(me.ID, 'view.SyncMailchimp', {
						swapReturn: true,
						viewCfg: {
							data: {
								srcPid: srcPid,
								srcCatId: srcCatId,
								audienceId: d.audienceId,
								syncTags: d.syncTags,
								tags: d.tags,
								incomingAudienceId: d.incomingAudienceId,
								incomingCategoryId: d.incomingCategoryId
							},
							srcDescription: node.get("_type") === "root" ?  node.get("text") : node.parentNode.get("text")+" / "+node.get("text")
						}
					});

				vw.showView();
			}
		});		
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
			ids = Sonicle.Data.collectValues(recs),
			r = recs[0],
			contactType = r.get('isList') ? 'contactsList' : 'contact',
			displayName = r.get('isList') ? r.get('displayName') : r.get('fullName'),
			msg;
		
		if (recs.length === 1) {
			msg = me.res(contactType + '.confirm.delete', Ext.String.ellipsis(displayName, 40));
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
			ids = Sonicle.Data.collectValues(recs),
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
				ids = Sonicle.Data.collectValues(recs),
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
	
	addContactUI: function(categoryId) {
		var me = this;
		me.addContact(categoryId, {
			callback: function(success) {
				if (success) me.reloadContacts();
			}
		});
	},
	
	addContactsListUI: function(categoryId, recipients) {
		var me = this;
		me.addContactsList(categoryId, recipients, {
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
				//TODO: change cb params to 'success, json.data, json' after updating method usages in api
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
				Ext.callback(opts.callback, opts.scope, [success, json.data, json]);
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
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
			}
		});
	},
	
	updateDefaultCategory: function(categoryId, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'SetDefaultCategory', {
			params: {
				id: categoryId
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
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
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
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
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
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
				tags: Sonicle.Utils.toJSONArray(tagIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope, [success, json.data, json]);
			}
		});
	},
	
	deleteContactsItems: function(contactIds, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'delete',
				ids: Sonicle.Utils.toJSONArray(contactIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
			}
		});
	},
	
	updateContactsItemsTags: function(contactIds, op, tagIds, opts) {
		opts = opts || {};
		var me = this,
				SU = Sonicle.Utils;
		WT.ajaxReq(me.ID, 'ManageGridContacts', {
			params: {
				crud: 'updateTag',
				ids: SU.toJSONArray(contactIds),
				op: op,
				tags: SU.toJSONArray(tagIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope, [success, json.data, json]);
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
				ids: Sonicle.Utils.toJSONArray(contactIds),
				targetCategoryId: targetCategoryId
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
			}
		});
	},
	
	addContact: function(categoryId, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.Contact', {swapReturn: true});
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('new', {
				data: {
					categoryId: categoryId
				}
			});
		});
	},
	
	addContact2: function(data, opts) {
		opts = opts || {};
		var me = this,
				data2 = me.parseContactApiData(data),
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
				data: data2,
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
	
	addContactsList: function(categoryId, recipients, opts) {
		opts = opts || {};
		var me = this,
				vw = WT.createView(me.ID, 'view.ContactsList', {swapReturn: true});
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('new', {
				data: {
					categoryId: categoryId,
					recipients: recipients
				}
			});
		});
	},
	
	addContactsList2: function(data, opts) {
		opts = opts || {};
		var me = this,
				data2 = me.parseContactsListApiData(data),
				vw = WT.createView(me.ID, 'view.ContactsList', {swapReturn: true});	
		
		vw.on('viewsave', function(s, success, model) {
			Ext.callback(opts.callback, opts.scope || me, [success, model]);
		});
		vw.showView(function(s) {
			vw.begin('new', {
				data: data2,
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
				ids: Sonicle.Utils.toJSONArray(contactIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
			}
		});
	},
	
	deleteContactsLists: function(contactsListIds, opts) {
		opts = opts || {};
		var me = this;
		WT.ajaxReq(me.ID, 'ManageContactsLists', {
			params: {
				crud: 'delete',
				ids: Sonicle.Utils.toJSONArray(contactsListIds)
			},
			callback: function(success, json) {
				Ext.callback(opts.callback, opts.scope || me, [success, json.data, json]);
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
		url = WTF.processBinUrl(me.ID, 'PrintContactsDetail', {ids: Sonicle.Utils.toJSONArray(contactIds)});
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
					ids: Sonicle.Utils.toJSONArray(contactIds)
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
				Ext.callback(opts.callback, opts.scope, [success, json.data, json]);
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
		parseContactsListApiData: function(data) {
			data = data || {};
			var obj = {};
			
			obj.categoryId = WTA.util.FoldersTree.getFolderForAdd(this.trFolders(), data.categoryId).getFolderId();
			if (Ext.isDefined(data.name)) obj.firstName = data.name;
			if (Ext.isDefined(data.recipients)) obj.recipients = data.recipients;
			
			return obj;
		},
		
		parseContactApiData: function(data) {
			data = data || {};
			var obj = {};
			
			obj.categoryId = WTA.util.FoldersTree.getFolderForAdd(this.trFolders(), data.categoryId).getFolderId();
			if (Ext.isDefined(data.displayName)) obj.displayName = data.displayName;
			if (Ext.isDefined(data.title)) obj.title = data.title;
			if (Ext.isDefined(data.firstName)) obj.firstName = data.firstName;
			if (Ext.isDefined(data.lastName)) obj.lastName = data.lastName;
			if (Ext.isDefined(data.nickname)) obj.nickname = data.nickname;
			if (Ext.isDefined(data.gender)) obj.gender = data.gender;
			if (Ext.isDefined(data.mobile)) obj.mobile = data.mobile;
			if (Ext.isDefined(data.pager1)) obj.pager1 = data.pager1;
			if (Ext.isDefined(data.pager2)) obj.pager2 = data.pager2;
			if (Ext.isDefined(data.email1)) obj.email1 = data.email1;
			if (Ext.isDefined(data.email2)) obj.email2 = data.email2;
			if (Ext.isDefined(data.email3)) obj.email3 = data.email3;
			if (Ext.isDefined(data.instantMsg1)) obj.instantMsg1 = data.instantMsg1;
			if (Ext.isDefined(data.instantMsg2)) obj.instantMsg2 = data.instantMsg2;
			if (Ext.isDefined(data.instantMsg3)) obj.instantMsg3 = data.instantMsg3;
			if (Ext.isDefined(data.workAddress)) obj.workAddress = data.workAddress;
			if (Ext.isDefined(data.workPostalCode)) obj.workPostalCode = data.workPostalCode;
			if (Ext.isDefined(data.workCity)) obj.workCity = data.workCity;
			if (Ext.isDefined(data.workState)) obj.workState = data.workState;
			if (Ext.isDefined(data.workCountry)) obj.workCountry = data.workCountry;
			if (Ext.isDefined(data.workTelephone1)) obj.workTelephone1 = data.workTelephone1;
			if (Ext.isDefined(data.workTelephone2)) obj.workTelephone2 = data.workTelephone2;
			if (Ext.isDefined(data.workFax)) obj.workFax = data.workFax;
			if (Ext.isDefined(data.homeAddress)) obj.homeAddress = data.homeAddress;
			if (Ext.isDefined(data.homePostalCode)) obj.homePostalCode = data.homePostalCode;
			if (Ext.isDefined(data.homeCity)) obj.homeCity = data.homeCity;
			if (Ext.isDefined(data.homeState)) obj.homeState = data.homeState;
			if (Ext.isDefined(data.homeCountry)) obj.homeCountry = data.homeCountry;
			if (Ext.isDefined(data.homeTelephone1)) obj.homeTelephone1 = data.homeTelephone1;
			if (Ext.isDefined(data.homeTelephone2)) obj.homeTelephone2 = data.homeTelephone2;
			if (Ext.isDefined(data.homeFax)) obj.homeFax = data.homeFax;
			if (Ext.isDefined(data.otherAddress)) obj.otherAddress = data.otherAddress;
			if (Ext.isDefined(data.otherPostalCode)) obj.otherPostalCode = data.otherPostalCode;
			if (Ext.isDefined(data.otherCity)) obj.otherCity = data.otherCity;
			if (Ext.isDefined(data.otherState)) obj.otherState = data.otherState;
			if (Ext.isDefined(data.otherCountry)) obj.otherCountry = data.otherCountry;
			if (Ext.isDefined(data.company)) obj.company = data.company;
			if (Ext.isDefined(data.function)) obj.function = data.function;
			if (Ext.isDefined(data.department)) obj.department = data.department;
			if (Ext.isDefined(data.birthday)) obj.birthday = data.birthday;
			if (Ext.isDefined(data.anniversary)) obj.anniversary = data.anniversary;
			if (Ext.isDefined(data.url)) obj.url = data.url;
			if (Ext.isDefined(data.notes)) obj.notes = data.notes;
			if (Ext.isDefined(data.picture)) obj.picture = data.picture;

			// OLD compatibility mappings...
			if (Ext.isDefined(data.workTelephone)) obj.workTelephone1 = data.workTelephone;
			if (Ext.isDefined(data.homeTelephone)) obj.homeTelephone1 = data.homeTelephone;
			if (Ext.isDefined(data.workMobile)) obj.mobile = data.workMobile;
			if (Ext.isDefined(data.workPager)) obj.pager1 = data.workPager;
			if (Ext.isDefined(data.homePager)) obj.pager2 = data.homePager;
			if (Ext.isDefined(data.workEmail)) obj.email1 = data.workEmail;
			if (Ext.isDefined(data.homeEmail)) obj.email2 = data.homeEmail;
			if (Ext.isDefined(data.workInstantMsg)) obj.instantMsg1 = data.workInstantMsg;
			if (Ext.isDefined(data.homeInstantMsg)) obj.instantMsg2 = data.homeInstantMsg;

			return obj;
		},
		
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
				case 'sendContact':
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
				case 'tags':
					sel = me.getSelectedContacts();
					if (sel.length === 0) {
						return true;
					} else if (sel.length === 1) {
						er = FT.toRightsObj(sel[0].get('_erights'));
						return !er.UPDATE;
					} else {
						for (var i=0; i<sel.length; i++) {
							if (!FT.toRightsObj(sel[i].get('_erights')).UPDATE) return true;
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
					sel = me.getSelectedContact();
					if(sel && (sel.get('isList') === false)) {
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
	
	buildPushMessageEventName: function(msg) {
		var name = this.callParent(arguments);
		if ('contactImportLog' === msg.action && msg.payload && msg.payload.oid) {
			name += '-' + msg.payload.oid;
		}
		return name;
	},
	
	statics: {
		NOTAG_REMOTESYNC: 'remsync-',

		noTagRemoteSync: function(categoryId) {
			return this.NOTAG_REMOTESYNC + categoryId;
		}
	}
});
