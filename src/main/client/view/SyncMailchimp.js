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
Ext.define('Sonicle.webtop.contacts.view.SyncMailchimp', {
	extend: 'WTA.sdk.WizardView',
	requires: [
		'Sonicle.form.field.LabelTag',
		'WTA.ux.panel.Form'
	],
	
	dockableConfig: {
		title: '{syncMailchimp.tit}',
		iconCls: 'wtcon-icon-mailchimp',
		width: 450,
		height: 300
	},
	useTrail: true,
	showDoButton: true,
	
	srcDescription: null,
	
	viewModel: {
		data: {
			data: {
				srcPid: null,
				srcCatId: null,
				audienceId: null,
				syncTags: true,
				tags: null,
				incomingAudienceId: null,
				incomingCategoryId: null
			}
		},
		formulas: {
			foSyncTags: WTF.checkboxBind(null, 'data.syncTags')
		}
		
	},
	
	initComponent: function() {
		var me = this,
			ic = me.getInitialConfig(),
			vm = me.getVM();
		
		if (ic.data) vm.set('data', Ext.apply(vm.get("data"),ic.data));
		me.callParent(arguments);
		me.on('beforenavigate', me.onBeforeNavigate);
		me.on('dosuccess', function() {
			var btnCancel = me.lookupReference('btncancel');
			btnCancel.setDisabled(true);
		});
	},
	
	initPages: function() {
		return ['s1','s2','end'];
	},
	
	initAction: function() {
		return 'SyncMailchimp';
	},
	
	doOperationParams: function() {
		var me = this,
			vm = me.getVM();
	
		return {
			srcPid: vm.get("data.srcPid"),
			srcCatId: vm.get("data.srcCatId"),
			audienceId: vm.get("data.audienceId"),
			syncTags: vm.get("data.syncTags"),
			tags: vm.get("data.tags"),
			incomingAudienceId: vm.get("data.incomingAudienceId"),
			incomingCategoryId: vm.get("data.incomingCategoryId")
		};
	},	
	
	createPages: function(path) {
		var me = this,
			SoString = Sonicle.String,
			vm = me.getVM();
	
		return [
		  {
			itemId: 's1',
			xtype: 'wtwizardpage',
			items: [
				{
					xtype: 'label',
					html: me.mys.res('syncMailchimp.s1.tit',me.srcDescription),
					cls: 'x-window-header-title-default'
				}, {
					xtype: 'sospacer'
				}, {
					xtype: 'label',
					html: SoString.htmlLineBreaks(me.mys.res('syncMailchimp.s1.txt'))
				}, {
					xtype: 'sospacer'
				}, {
					xtype: 'wtform',
					defaults: {
						labelWidth: 80
					},
					items: [
						WTF.lookupCombo('id', 'desc', {
							bind: '{data.audienceId}',
							store: {
								autoLoad: true,
								model: 'WTA.model.Simple',
								proxy: WTF.proxy(me.mys.ID, 'LookupMailchimpAudience', 'audience')
							},
							allowBlank: false,
							fieldLabel: me.res('syncMailchimp.fld-audience.lbl'),
							width: 80+170
						}), {
							xtype: 'checkbox',
							reference: 'fldsynctags', // Publishes field into viewmodel...
							bind: '{foSyncTags}',
							margin: '0 20 0 0',
							hideEmptyLabel: true,
							boxLabel: me.res('syncMailchimp.fld-synctags.lbl')
						}, {
							xtype: 'solabeltagfield',
							bind: {
								value: '{data.tags}',
								disabled: '{!fldsynctags.checked}'
							},
							store: WT.getTagsStore(),
							fieldLabel: me.res('syncMailchimp.field-tags.lbl'),
							valueField: 'id',
							displayField: 'name',
							colorField: 'color',
							createNewOnEnter: false,
							createNewOnBlur: false,
							filterPickList: true,
							forceSelection: true,
							emptyText: me.res('syncMailchimp.field-tags.emptyText'),
							margin: '0 5 0 0',
							anchor: '100%'
						}	
					]
				}
			]
		  }, {
			itemId: 's2',
			xtype: 'wtwizardpage',
			items: [
				{
					xtype: 'label',
					html: me.mys.res('syncMailchimp.s2.tit'),
					cls: 'x-window-header-title-default'
				}, {
					xtype: 'sospacer'
				}, {
					xtype: 'label',
					html: SoString.htmlLineBreaks(me.mys.res('syncMailchimp.s2.txt'))
				}, {
					xtype: 'sospacer'
				}, {
					xtype: 'wtform',
					defaults: {
						labelWidth: 80
					},
					items: [
						WTF.lookupCombo('id', 'desc', {
							bind: '{data.incomingAudienceId}',
							store: {
								autoLoad: true,
								model: 'WTA.model.Simple',
								proxy: WTF.proxy(me.mys.ID, 'LookupMailchimpAudience', 'audience'),
								filters: [{
									filterFn: function(rec) {
										return rec.get('id')!==vm.get("data.audienceId");
									}
								}]
							},
							allowBlank: false,
							fieldLabel: me.res('syncMailchimp.fld-incomingAudience.lbl'),
							width: 80+170
						}),
						WTF.lookupCombo('categoryId', '_label', {
							xtype: 'socombo',
							reference: 'fldincategory',
							bind: '{data.incomingCategoryId}',
							listConfig: {
								displayField: 'name'
							},
							autoLoadOnValue: true,
							store: {
								model: me.mys.preNs('model.CategoryLkp'),
								proxy: WTF.proxy(me.mys.ID, 'LookupCategoryFolders', 'folders'),
								autoLoad: true,
								grouper: {
									property: '_profileId',
									sortProperty: '_order'
								},
								filters: [{
									filterFn: function(rec) {
										return rec.get('_writable');
									}
								}]
							},
							allowBlank: false,
							groupField: '_profileDescription',
							colorField: 'color',
							fieldLabel: me.mys.res('contact.fld-category.lbl'),
							labelAlign: 'right',
							width: 400
						})
						
					]
				}
			]
		  },
		  me.createEndPage(path)
		];
	},
	
	onBeforeNavigate: function(s, dir, np, pp) {
		if (dir === -1) return;
		var me = this,
				ret = true,
				ppcmp = me.getPageCmp(pp),
				vm = me.getVM();
		
		if (pp === 's1') {
			ret = ppcmp.down('wtform').isValid();
			if (!ret) return false;
			
		} else if (pp === 's2') {
			ret = ppcmp.down('wtform').isValid();
			if (!ret) return false;
			me.mys.onPushMessage('mailchimpSyncLog-' + me.getUId(), me.onMailchimpSyncMessage, me);
			me.mys.onPushMessage('mailchimpSyncEnd', me.onMailchimpSyncEnd, me);
		}
	},
	
	buildDoParams: function(path) {
		var vm = this.getVM();
		return Ext.apply(this.callParent(arguments), {
			srcPid: vm.get('data.srcPid'),
			srcCatId: vm.get('data.srcCatId'),
			audienceId: vm.get('data.audienceId'),
			syncTags: vm.get('data.syncTags'),
			tags: vm.get('data.tags')			
		});
	},
	
	onMailchimpSyncMessage: function(msg) {
		var cmp = this.getPageCmp('end').lookupReference('log'),
			newValue = Sonicle.String.join('\n', cmp.getValue(), msg.payload.log),
			newLen = newValue.length;
		cmp.setValue(newValue);
		//cmp.selectText(newLen);
	},
	
	onMailchimpSyncEnd: function(msg) {
		var me = this,
			btnCancel = me.lookupReference('btncancel');
		btnCancel.setDisabled(false);
	}	
});
