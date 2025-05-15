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
package com.sonicle.webtop.contacts;

import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.db.DbUtils;
import com.sonicle.commons.time.DateTimeUtils;
import com.sonicle.commons.time.JodaTimeUtils;
import com.sonicle.commons.web.Crud;
import com.sonicle.commons.web.ServletUtils;
import com.sonicle.commons.web.json.JsonResult;
import com.sonicle.commons.web.json.MapItem;
import com.sonicle.commons.web.json.Payload;
import com.sonicle.webtop.contacts.bol.js.JsUserOptions;
import com.sonicle.webtop.core.app.RunContext;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.sdk.BaseUserOptionsService;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

/**
 *
 * @author malbinola
 */
public class UserOptionsService extends BaseUserOptionsService {
	public static final Logger logger = WT.getLogger(UserOptionsService.class);
	
	private ContactsManager manager;
	
	public UserOptionsService() {
		super();
		manager=(ContactsManager)WT.getServiceManager(SERVICE_ID);
	}
	
	@Override
	public void processUserOptions(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		Connection con = null;
		
		try {
			String crud = ServletUtils.getStringParameter(request, "crud", true);
			ContactsUserSettings cus = new ContactsUserSettings(SERVICE_ID, getTargetProfileId());
			DateTimeFormatter hmf = JodaTimeUtils.createFormatterHM();
			
			if(crud.equals(Crud.READ)) {
				JsUserOptions jso = new JsUserOptions(getTargetProfileId().toString());
				
				// Main
				jso.view = EnumUtils.toSerializedName(cus.getView());
				jso.showBy = EnumUtils.toSerializedName(cus.getShowBy());
				jso.groupBy = EnumUtils.toSerializedName(cus.getGroupBy());
				jso.anniversaryReminderDelivery = cus.getAnniversaryReminderDelivery();
				jso.anniversaryReminderTime = hmf.print(cus.getAnniversaryReminderTime());
				jso.hasMailchimp=manager.isMailchimpEnabled();
				if (jso.hasMailchimp) {
					jso.mailchimpApiKey=cus.getUserMailchimpApiKey();
				}
				
				new JsonResult(jso).printTo(out);
				
			} else if(crud.equals(Crud.UPDATE)) {
				Payload<MapItem, JsUserOptions> pl = ServletUtils.getPayload(request, JsUserOptions.class);
				
				// Main
				if (pl.map.has("view")) cus.setView(pl.data.view);
				if (pl.map.has("showBy")) cus.setShowBy(pl.data.showBy);
				if (pl.map.has("anniversaryReminderDelivery")) cus.setAnniversaryReminderDelivery(pl.data.anniversaryReminderDelivery);
				if (pl.map.has("anniversaryReminderTime")) cus.setAnniversaryReminderTime(hmf.parseLocalTime(pl.data.anniversaryReminderTime));
				if (pl.map.has("groupBy")) cus.setGroupBy(pl.data.groupBy);
				if (manager.isMailchimpEnabled() && pl.map.has("mailchimpApiKey")) cus.setMailchimpApiKey(pl.data.mailchimpApiKey);
				
				new JsonResult().printTo(out);
			}
			
		} catch (Exception ex) {
			logger.error("Error executing UserOptions", ex);
			new JsonResult(false).printTo(out);
		} finally {
			DbUtils.closeQuietly(con);
		}
	}
}
