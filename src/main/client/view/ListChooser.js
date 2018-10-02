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
Ext.define('Sonicle.webtop.contacts.view.ListChooser', {
	extend: 'WTA.sdk.DockableView',
	requires: [
		'Sonicle.form.RadioGroup',
		'Sonicle.webtop.core.ux.field.RecipientSuggestCombo'
	],
	
	dockableConfig: {
		width: 400,
		height: 150,
		modal: true,
		minimizable: false,
		maximizable: false
	},
	promptConfirm: false,
	
	viewModel: {
		data: {
			list: null,
			recipientType: 'to'
		}
	},
	
	/**
	 * @cfg {String} list
	 * Initial list value
	*/
	
	initComponent: function() {
		var me = this,
				ic = me.getInitialConfig();
		me.recipientTypeName = Ext.id(null, 'recipientType-');
		
		if(!Ext.isEmpty(ic.ownerId)) me.getVM().set('list', ic.ownerId);
		
		Ext.apply(me, {
			buttons: [{
				text: WT.res('act-ok.lbl'),
				handler: me.onOkClick,
				scope: me
			}, {
				text: WT.res('act-cancel.lbl'),
				handler: me.onCancelClick,
				scope: me
			}]
		});
		me.callParent(arguments);
		
		WTU.applyFormulas(me.getVM(), {
			foRecipientType: WTF.radioGroupBind(null, 'recipientType', me.recipientTypeName)
		});
		
		me.add({
			region: 'center',
			xtype: 'wtfieldspanel',
			modelValidation: true,
			defaults: {
				labelWidth: 50
			},
			items: [
				Ext.create({
					xtype: 'wtrcptsuggestcombo',
					rftype: 'list',
					reference: 'fldlist',
					bind: '{list}',
					fieldLabel: me.mys.res('listChooser.fld-list.lbl'),
					anchor: '100%',
					allowBlank: false,
					matchFieldWidth: false,
					listConfig: {
						width: 400
					},
					minChars: 1
				}),
				{
					xtype: 'radiogroup',
					bind: {
						value: '{foRecipientType}'
					},
					layout: 'hbox',
					defaults: {
						name: me.recipientTypeName,
						margin: '0 20 0 0'
					},
					items: [{
						inputValue: 'to',
						boxLabel: WT.res('com.sonicle.webtop.mail','to')
					},{
						inputValue: 'cc',
						boxLabel: WT.res('com.sonicle.webtop.mail','cc')
					},{
						inputValue: 'bcc',
						boxLabel: WT.res('com.sonicle.webtop.mail','bcc')
					}],
					hideEmptyLabel: false,
					fieldLabel: ''
				}							]
		});
	},
	
	onOkClick: function() {
		var me = this;
		if (!me.lref('fldlist').isValid()) return;
		me.fireEvent('viewok', me, me.getVM().get('list'),me.getVM().get('recipientType'));
		me.closeView(false);
	},
	
	onCancelClick: function() {
		this.closeView(false);
	}
});
