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
Ext.define('Sonicle.webtop.contacts.view.UserOptions', {
	extend: 'WTA.sdk.UserOptionsView',
	requires: [
		'Sonicle.webtop.contacts.store.View',
		'Sonicle.webtop.contacts.store.ShowBy',
		'Sonicle.webtop.contacts.store.GroupBy',
		'Sonicle.webtop.contacts.store.ReminderDelivery'
	],
		
	initComponent: function() {
		var me = this;
		me.callParent(arguments);
		
		vm = me.getViewModel();
		vm.setFormulas(Ext.apply(vm.getFormulas() || {}, {
			foHasMailchimp: function(get) {
				var bool = get('record.hasMailchimp');
				return Ext.isBoolean(bool) ? bool : false;
			}
		}));
		
		me.add({
			xtype: 'wtopttabsection',
			title: WT.res(me.ID, 'opts.main.tit'),
			items: [
			WTF.lookupCombo('id', 'desc', {
				bind: '{record.view}',
				store: Ext.create('Sonicle.webtop.contacts.store.View', {
					autoLoad: true
				}),
				fieldLabel: WT.res(me.ID, 'opts.main.fld-view.lbl'),
				width: 280,
				listeners: {
					blur: {
						fn: me.onBlurAutoSave,
						scope: me
					}
				}
			}),
			WTF.lookupCombo('id', 'desc', {
				bind: '{record.showBy}',
				store: Ext.create('Sonicle.webtop.contacts.store.ShowBy', {
					autoLoad: true
				}),
				fieldLabel: WT.res(me.ID, 'opts.main.fld-showBy.lbl'),
				width: 280,
				listeners: {
					blur: {
						fn: me.onBlurAutoSave,
						scope: me
					}
				},
				needReload: true
			}),
			WTF.lookupCombo('id', 'desc', {
				bind: '{record.groupBy}',
				store: Ext.create('Sonicle.webtop.contacts.store.GroupBy', {
					autoLoad: true
				}),
				fieldLabel: WT.res(me.ID, 'opts.main.fld-groupBy.lbl'),
				width: 280,
				listeners: {
					blur: {
						fn: me.onBlurAutoSave,
						scope: me
					}
				},
				needReload: true
			}), {
				xtype: 'sospacer'
			}, {
				xtype: 'formseparator'
			}, WTF.lookupCombo('id', 'desc', {
				bind: '{record.anniversaryReminderDelivery}',
				store: Ext.create('Sonicle.webtop.contacts.store.ReminderDelivery', {
					autoLoad: true
				}),
				fieldLabel: WT.res(me.ID, 'opts.main.fld-anniversaryReminderDelivery.lbl'),
				width: 280,
				listeners: {
					blur: {
						fn: me.onBlurAutoSave,
						scope: me
					}
				}
			}), {
				xtype: 'timefield',
				bind: '{record.anniversaryReminderTime}',
				format: 'H:i',
				increment : 30,
				snapToIncrement: true,
				fieldLabel: WT.res(me.ID, 'opts.main.fld-anniversaryReminderTime.lbl'),
				width: 220,
				listeners: {
					blur: {
						fn: me.onBlurAutoSave,
						scope: me
					}
				}
			}]
		});
		
		me.add({
			xtype: 'wtopttabsection',
			reference: 'tabmailchimp',
			title: WT.res(me.ID, 'opts.mailchimp.tit'),
			hidden: true,
			items: [
				{
					xtype: 'textfield',
					bind: {
						value: '{record.mailchimpApiKey}'
					},
					fieldLabel: WT.res(me.ID, 'opts.mailchimp.fld-apikey.lbl'),
					emptyText: WT.res(me.ID, 'opts.mailchimp.fld-apikey.emptyText'),
					width: 440,
					listeners: { blur: { fn: me.onBlurAutoSave, scope: me } }
				}
			]
		});
		
		vm.bind('{record.hasMailchimp}', function(nv) {
			if (nv) me.lref('tabmailchimp').tab.show();
		});
	}
});
