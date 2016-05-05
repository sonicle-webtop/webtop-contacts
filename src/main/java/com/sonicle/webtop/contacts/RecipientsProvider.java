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

import com.sonicle.commons.db.DbUtils;
import com.sonicle.webtop.contacts.dal.ContactDAO;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.provider.IProfileRecipientsProvider;
import com.sonicle.webtop.core.app.provider.RecipientsProviderBase;
import com.sonicle.webtop.core.bol.model.InternetRecipient;
import com.sonicle.webtop.core.dal.DAOException;
import com.sonicle.webtop.core.sdk.UserProfile;
import com.sonicle.webtop.core.sdk.interfaces.IConnectionProvider;
import com.sonicle.webtop.core.sdk.interfaces.IServiceSettingReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author malbinola
 */
public class RecipientsProvider extends RecipientsProviderBase implements IProfileRecipientsProvider {
	public final String SERVICE_ID;

	public RecipientsProvider(IConnectionProvider conp, IServiceSettingReader setm) {
		super(conp, setm);
		SERVICE_ID = WT.findServiceId(this.getClass());
	}

	@Override
	public List<InternetRecipient> getRecipients(UserProfile.Id profileId, String text) {
		ContactDAO dao = ContactDAO.getInstance();
		Connection con = null;
		
		/*
		try {
			con = WT.getConnection(SERVICE_ID);
			
			dao.viewByCategoryPattern(con, categoryId, text, text)
			
			
			return dao.selectByDomainUser(con, pid.getDomainId(), pid.getUserId());
			
		} catch(SQLException | DAOException ex) {
			throw new WTException(ex, "DB error");
		} finally {
			DbUtils.closeQuietly(con);
		}
		*/
		
		return null;
	}
}
