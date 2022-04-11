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
	 * Opens a contact using the choosen editing mode, defaults to edit.
	 * @param {String} id The contact ID.
	 * @param {Object} opts An object containing configuration.
	 * @param {edit|view} [opts.mode="edit"] Opening mode.
	 * @param {Function} [opts.callback] Callback method for 'viewsave' event.
	 * @param {Object} [opts.scope] The callback method scope.
	 * @param {Boolean} [opts.dirty] The dirty state of the model.
	 * @param {Boolean} [opts.uploadTag] A custom upload tag.
	 * @returns {WTA.sdk.ModelView}
	 */
	openContact(id, opts) {
		opts = opts || {};
		return this.service.openContact(opts.mode === 'view' ? false : true, id, {
			callback: opts.callback,
			scope: opts.scope,
			dirty: opts.dirty,
			uploadTag: opts.uploadTag
		});
	},
	
	/**
	 * Adds a new contact.
	 * @param {Object} data An object containing event data.
	 * @param {String} [data.categoryId] The category ID in which to add the item.
	 * @param {String} [data.displayName] The display name.
	 * @param {String} [data.title] The title.
	 * @param {String} [data.firstName] The firstname.
	 * @param {String} [data.lastName] The lastname.
	 * @param {String} [data.nickname] The nickname.
	 * @param {male|female|other} [data.gender] The predefined gender.
	 * @param {String} [data.mobile] The mobile phone.
	 * @param {String} [data.pager1] The pager #1.
	 * @param {String} [data.pager2] The pager #2.
	 * @param {String} [data.email1] The email address #1.
	 * @param {String} [data.email2] The email address #2.
	 * @param {String} [data.email3] The email address #3.
	 * @param {String} [data.instantMsg1] The im account #1.
	 * @param {String} [data.instantMsg2] The im account #2.
	 * @param {String} [data.instantMsg3] The im account #3.
	 * @param {String} [data.workAddress] The work address.
	 * @param {String} [data.workPostalCode] The work address postal-code.
	 * @param {String} [data.workCity] The work address city.
	 * @param {String} [data.workState] The work address state.
	 * @param {String} [data.workCountry] The work address country.
	 * @param {String} [data.workTelephone1] The work telephone #1.
	 * @param {String} [data.workTelephone2] The work telephone #2.
	 * @param {String} [data.workFax] The work fax.
	 * @param {String} [data.homeAddress] The home address.
	 * @param {String} [data.homePostalCode] The home address postal-code.
	 * @param {String} [data.homeCity] The home address city.
	 * @param {String} [data.homeState] The home address state.
	 * @param {String} [data.homeCountry] The home address country.
	 * @param {String} [data.homeTelephone1] The home telephone #1.
	 * @param {String} [data.homeTelephone2] The home telephone #2.
	 * @param {String} [data.homeFax] The home fax.
	 * @param {String} [data.otherAddress] The other address.
	 * @param {String} [data.otherPostalCode] The other address postal-code.
	 * @param {String} [data.otherCity] The other address city.
	 * @param {String} [data.otherState] The other address state.
	 * @param {String} [data.otherCountry] The other address country.
	 * @param {String} [data.company] The company description or ID.
	 * @param {String} [data.function] The job function.
	 * @param {String} [data.department] The job department.
	 * @param {Date} [data.birthday] The birthday date.
	 * @param {Date} [data.anniversary] The anniversary date.
	 * @param {String} [data.url] The Website URL.
	 * @param {String} [data.notes] Arbitrary text notes.
	 * @param {String} [data.tags] Comma-separated list of tag IDs.
	 * @param {Object} opts An object containing configuration.
	 * @param {Function} [opts.callback] Callback method for 'viewsave' event.
	 * @param {Object} [opts.scope] The callback method scope.
	 * @param {Boolean} [opts.dirty] The dirty state of the model.
	 * @param {Boolean} [opts.uploadTag] A custom upload tag.
	 * @returns {WTA.sdk.ModelView}
	 */
	addContact: function(data, opts) {
		opts = opts || {};
		return this.service.addContact2(data, {
			callback: opts.callback,
			scope: opts.scope,
			dirty: opts.dirty,
			uploadTag: opts.uploadTag
		});
	},
	
	/**
	 * Opens a contact-list using the choosen editing mode, defaults to edit.
	 * @param {String} id The contacts-list ID.
	 * @param {Object} opts An object containing configuration.
	 * @param {edit|view} [opts.mode="edit"] Opening mode.
	 * @param {Function} [opts.callback] Callback method for 'viewsave' event.
	 * @param {Object} [opts.scope] The callback method scope.
	 * @param {Boolean} [opts.dirty] The dirty state of the model.
	 * @param {Boolean} [opts.uploadTag] A custom upload tag.
	 * @returns {WTA.sdk.ModelView}
	 */
	openContactsList(id, opts) {
		opts = opts || {};
		return this.service.openContactsList(opts.mode === 'view' ? false : true, id, {
			callback: opts.callback,
			scope: opts.scope,
			dirty: opts.dirty,
			uploadTag: opts.uploadTag
		});
	},
	
	/**
	 * Adds a new contact.
	 * @param {Object} data An object containing event data:
	 * @param {String} [data.categoryId] The category ID in which to add the item.
	 * @param {String} [data.name] The name of the list.
	 * @param {String[]|Object[]} [data.recipients] Array of recipient addresses or an array of recipient object:
	 * @param {to|cc|bcc} [recipient.rcptType="to"] The type of the recipient.
	 * @param {String} [recipient.address] Recipient's internet address.
	 * @param {String} [recipient.refContactId] Recipient's underlyning contact ID.
	 * @param {String} [data.tags] Comma-separated list of tag IDs.
	 * @param {Object} opts An object containing configuration:
	 * @param {Function} [opts.callback] Callback method for 'viewsave' event.
	 * @param {Object} [opts.scope] The callback method scope.
	 * @param {Boolean} [opts.dirty] The dirty state of the model.
	 * @param {Boolean} [opts.uploadTag] A custom upload tag.
	 * @returns {WTA.sdk.ModelView}
	 */
	addContactsList: function(data, opts) {
		opts = opts || {};
		return this.service.addContactsList2(data, {
			callback: opts.callback,
			scope: opts.scope,
			dirty: opts.dirty,
			uploadTag: opts.uploadTag
		});
	},
	
	/**
	 * Get emails list from a contact list
	 */
	expandRecipientsList: function(data, opts) {		
		opts = opts || {};
		this.service.expandRecipientsList(data.address, {
			callback: opts.callback,
			scope: opts.scope
		});
	}
});
