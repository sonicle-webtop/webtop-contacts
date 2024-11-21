/* 
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
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2014 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.bol.js;

import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.web.json.CompositeId;
import com.sonicle.webtop.contacts.model.ContactList;
import com.sonicle.webtop.contacts.model.ContactListEx;
import com.sonicle.webtop.contacts.model.ContactListRecipient;
import com.sonicle.webtop.contacts.model.ContactListRecipientBase;
import com.sonicle.webtop.core.sdk.UserProfileId;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class JsContactsList {
	public String id;
	public String contactId;
	public Integer categoryId;
	public String name;
	public String tags;
	public ArrayList<JsRecipient> recipients;
	public String _profileId;
	
	public JsContactsList() {}
	
	public JsContactsList(UserProfileId ownerId, ContactList<ContactListRecipient> contact) {
		id = contact.getContactId().toString();
		contactId = contact.getContactId();
		categoryId = contact.getCategoryId();
		name = contact.getDisplayName();
		tags = new CompositeId(contact.getTags()).toString();
		recipients = new ArrayList<>();
		for (ContactListRecipient rcpt : contact.getRecipients()) {
			recipients.add(new JsRecipient(rcpt));
		}
		_profileId = ownerId.toString();
	}
	
	public static class JsRecipient {
		public String listRecipientId;
		public String recipient;
		public String recipientType;
		public String recipientContactId;
		
		public JsRecipient() {}
		
		public JsRecipient(ContactListRecipient item) {
			this.listRecipientId = item.getListRecipientId();
			this.recipient = item.getRecipient();
			this.recipientType = EnumUtils.toSerializedName(item.getRecipientType());
			this.recipientContactId = item.getRecipientContactId();
		}
	}
	
	public static ContactListEx createContactListRecipientForInsert(JsContactsList js) {
		return createContactListRecipientForUpdate(js);
	}
	
	public static ContactListEx createContactListRecipientForUpdate(JsContactsList js) {
		ContactListEx item = new ContactListEx();
		item.setCategoryId(js.categoryId);
		item.setDisplayName(js.name);
		item.setTags(new LinkedHashSet<>(new CompositeId().parse(js.tags).getTokens()));
		for (JsRecipient jsrcpt : js.recipients) {
			if (!StringUtils.isBlank(StringUtils.trimToNull(jsrcpt.recipient))) {
				item.addRecipient(createContactListRecipientForAdd(jsrcpt));
			}
		}
		return item;
	}
	
	public static ContactListRecipientBase createContactListRecipientForAdd(JsRecipient js) {
		ContactListRecipientBase item = new ContactListRecipientBase();
		item.setRecipient(js.recipient);
		item.setRecipientContactId(js.recipientContactId);
		item.setRecipientType(EnumUtils.forSerializedName(js.recipientType, ContactListRecipientBase.RecipientType.class));
		return item;
	}
}
