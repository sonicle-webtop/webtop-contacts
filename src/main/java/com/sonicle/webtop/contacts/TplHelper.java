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

import com.sonicle.commons.web.json.MapItem;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.util.NotificationHelper;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

/**
 *
 * @author malbinola
 */
public class TplHelper {
	private static final String SERVICE_ID = "com.sonicle.webtop.contacts";
	
	public static String buildAnniversaryBodyTpl(Locale locale) throws IOException, TemplateException {
		return WT.buildTemplate(SERVICE_ID, "tpl/email/anniversary-body.html", new MapItem());
	}
	
	public static String buildAnniversaryEmail(Locale locale, boolean birthday, String recipientEmail, String contactFullName) throws IOException, TemplateException {
		final String BHD_KEY = (birthday) ? ContactsLocale.TPL_EMAIL_ANNIVERSARY_BODY_HEADER_BDAY : ContactsLocale.TPL_EMAIL_ANNIVERSARY_BODY_HEADER;
		
		String source = NotificationHelper.buildSource(locale, SERVICE_ID);
		String bodyHeader = MessageFormat.format(WT.lookupResource(SERVICE_ID, locale, BHD_KEY), contactFullName);
		String complexBody = buildAnniversaryBodyTpl(locale);
		String because = WT.lookupResource(SERVICE_ID, locale, ContactsLocale.TPL_EMAIL_ANNIVERSARY_FOOTER_BECAUSE);
		
		return NotificationHelper.buildCustomBodyTpl(locale, source, recipientEmail, bodyHeader, complexBody, because);
	}
}
