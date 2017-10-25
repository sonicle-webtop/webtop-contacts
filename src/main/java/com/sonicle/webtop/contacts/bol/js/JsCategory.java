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
import com.sonicle.commons.LangUtils;
import com.sonicle.commons.URIUtils;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryRemoteParameters;

/**
 *
 * @author malbinola
 */
public class JsCategory {
	public Integer categoryId;
	public String domainId;
	public String userId;
	public Boolean builtIn;
	public String provider;
	public String name;
	public String description;
	public String color;
	public String sync;
	public Boolean isDefault;
	public String remoteUrl;
	public String remoteUsername;
	public String remotePassword;
	
	public JsCategory(Category cat) {
		categoryId = cat.getCategoryId();
		domainId = cat.getDomainId();
		userId = cat.getUserId();
		builtIn = cat.getBuiltIn();
		provider = EnumUtils.toSerializedName(cat.getProvider());
		name = cat.getName();
		description = cat.getDescription();
		color = cat.getColor();
		sync = EnumUtils.toSerializedName(cat.getSync());
		isDefault = cat.getIsDefault();
		
		CategoryRemoteParameters params = LangUtils.deserialize(cat.getParameters(), new CategoryRemoteParameters(), CategoryRemoteParameters.class);
		remoteUrl = URIUtils.toString(params.url);
		remoteUsername = params.username;
		remotePassword = params.password;
	}
		
	public static Category createCategory(JsCategory js, String origParameters) {
		Category cat = new Category();
		cat.setCategoryId(js.categoryId);
		cat.setDomainId(js.domainId);
		cat.setUserId(js.userId);
		cat.setBuiltIn(js.builtIn);
		cat.setProvider(EnumUtils.forSerializedName(js.provider, Category.Provider.class));
		cat.setName(js.name);
		cat.setDescription(js.description);
		cat.setColor(js.color);
		cat.setSync(EnumUtils.forSerializedName(js.sync, Category.Sync.class));
		cat.setIsDefault(js.isDefault);
		//cal.setIsPrivate(js.isPrivate);
		
		if (cat.isRemoteProvider()) {
			CategoryRemoteParameters params = LangUtils.deserialize(origParameters, new CategoryRemoteParameters(), CategoryRemoteParameters.class);
			params.url = URIUtils.createURIQuietly(js.remoteUrl);
			params.username = js.remoteUsername;
			params.password = js.remotePassword;
			cat.setParameters(LangUtils.serialize(params, CategoryRemoteParameters.class));
		} else {
			cat.setParameters(null);
		}
		
		return cat;
	}
}
