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
package com.sonicle.webtop.contacts.bol.js;

import com.sonicle.webtop.contacts.bol.model.ContactsList;
import com.sonicle.webtop.contacts.bol.model.ContactsListRecipient;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.bol.js.JsFkModel;
import java.util.ArrayList;

/**
 *
 * @author malbinola
 */
public class JsContactsList {
	public String id;
	public Integer contactId;
	public Integer categoryId;
	public String name;
	public ArrayList<Recipient> recipients;
	public String _profileId;
	
	public JsContactsList() {}
	
	public JsContactsList(UserProfile.Id ownerId, ContactsList cl) {
		id = cl.getContactId().toString();
		contactId = cl.getContactId();
		categoryId = cl.getCategoryId();
		name = cl.getName();
		recipients = new ArrayList<>();
		for(ContactsListRecipient rcpt : cl.getRecipients()) {
			recipients.add(new Recipient(id, rcpt));
		}
		_profileId = ownerId.toString();
	}
	
	public static class Recipient extends JsFkModel {
		public Integer listRecipientId;
		public String recipient;
		public String recipientType;
		
		public Recipient() {}
		
		public Recipient(String fk, ContactsListRecipient rcpt) {
			super(fk);
			listRecipientId = rcpt.getListRecipientId();
			recipient = rcpt.getRecipient();
			recipientType = rcpt.getRecipientType();
		}
	}
	
	public static ContactsList buildContactsList(JsContactsList js) {
		ContactsList cl = new ContactsList(js.contactId, js.categoryId);
		cl.setName(js.name);
		for(Recipient jsRcpt : js.recipients) {
			ContactsListRecipient rcpt = new ContactsListRecipient();
			rcpt.setListRecipientId(jsRcpt.listRecipientId);
			rcpt.setRecipient(jsRcpt.recipient);
			rcpt.setRecipientType(jsRcpt.recipientType);
			cl.getRecipients().add(rcpt);
		}
		return cl;
	}
}
