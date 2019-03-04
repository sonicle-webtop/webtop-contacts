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
Ext.define('Sonicle.webtop.contacts.ServiceApi', {
	extend: 'WTA.sdk.ServiceApi',
	
	/**
	 * Force a reload for the current view.
	 */
	reloadContacts: function() {
		this.service.reloadContacts();
	},
	
	/**
	 * Adds a new contact.
	 * @param {Object} data An object containing event data.
	 * @param {String} [data.title]
	 * @param {String} [data.firstName]
	 * @param {String} [data.lastName]
	 * @param {String} [data.nickname]
	 * @param {male|female|other} [data.gender]
	 * @param {String} [data.mobile]
	 * @param {String} [data.pager1]
	 * @param {String} [data.pager2]
	 * @param {String} [data.email1]
	 * @param {String} [data.email2]
	 * @param {String} [data.email3]
	 * @param {String} [data.instantMsg1]
	 * @param {String} [data.instantMsg2]
	 * @param {String} [data.instantMsg3]
	 * @param {String} [data.workAddress]
	 * @param {String} [data.workPostalCode]
	 * @param {String} [data.workCity]
	 * @param {String} [data.workState]
	 * @param {String} [data.workCountry]
	 * @param {String} [data.workTelephone1]
	 * @param {String} [data.workTelephone2]
	 * @param {String} [data.workFax]
	 * @param {String} [data.homeAddress]
	 * @param {String} [data.homePostalCode]
	 * @param {String} [data.homeCity]
	 * @param {String} [data.homeState]
	 * @param {String} [data.homeCountry]
	 * @param {String} [data.homeTelephone1]
	 * @param {String} [data.homeTelephone2]
	 * @param {String} [data.homeFax]
	 * @param {String} [data.otherAddress]
	 * @param {String} [data.otherPostalCode]
	 * @param {String} [data.otherCity]
	 * @param {String} [data.otherState]
	 * @param {String} [data.otherCountry]
	 * @param {String} [data.company]
	 * @param {String} [data.function]
	 * @param {String} [data.department]
	 * @param {Date} [data.birthday]
	 * @param {Date} [data.anniversary]
	 * @param {String} [data.url]
	 * @param {String} [data.notes]
	 * @param {Object} opts An object containing configuration.
	 * @param {Function} [opts.callback] Callback method for 'viewsave' event.
	 * @param {Object} [opts.scope] The callback method scope.
	 * @param {Boolean} [opts.dirty] The dirty state of the model.
	 * @param {Boolean} [opts.uploadTag] A custom upload tag.
	 */
	addContact: function(data, opts) {
		opts = opts || {};
		this.service.addContact2(data, {
			callback: opts.callback,
			scope: opts.scope,
			dirty: opts.dirty,
			uploadTag: opts.uploadTag
		});
	}
});
