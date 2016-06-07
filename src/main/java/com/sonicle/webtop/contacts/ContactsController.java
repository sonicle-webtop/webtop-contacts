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
package com.sonicle.webtop.contacts;

import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.sdk.BaseController;
import com.sonicle.webtop.core.sdk.BaseReminder;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.sdk.WTOperationException;
import com.sonicle.webtop.core.sdk.interfaces.IControllerHandlesProfiles;
import com.sonicle.webtop.core.sdk.interfaces.IControllerHandlesReminders;
import java.util.List;
import org.joda.time.DateTime;
import org.slf4j.Logger;

/**
 *
 * @author malbinola
 */
public class ContactsController extends BaseController implements IControllerHandlesProfiles, IControllerHandlesReminders {
	public static final Logger logger = WT.getLogger(ContactsController.class);
	
	public ContactsController() {
		super();
	}
	
	@Override
	public void addProfile(UserProfile.Id profileId) throws WTException {
		ContactsManager manager = new ContactsManager(profileId);
		
		// Adds built-in category
		try {
			manager.addBuiltInCategory();
		} catch(WTOperationException ex) {
			// Do nothing...
		} catch(WTException ex) {
			throw ex;
		}
	}
	
	@Override
	public void removeProfile(UserProfile.Id profileId, boolean deep) throws WTException {
		//TODO: implementare cleanup utente
		//ContactsManager manager = new ContactsManager(getRunContext(), profileId);
	}
	
	@Override
	public List<BaseReminder> returnReminders(DateTime now) {
		ContactsManager manager = new ContactsManager();
		return manager.getRemindersToBeNotified(now);
	}
}