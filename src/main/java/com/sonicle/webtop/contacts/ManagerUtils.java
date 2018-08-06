/*
 * Copyright (C) 2018 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2018 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts;

import com.sonicle.commons.Base58;
import com.sonicle.commons.EnumUtils;
import com.sonicle.commons.beans.VirtualAddress;
import static com.sonicle.webtop.contacts.IContactsManager.RCPT_ORIGIN_LIST;
import com.sonicle.webtop.contacts.bol.OCategory;
import com.sonicle.webtop.contacts.bol.OCategoryPropSet;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.CategoryPropSet;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.sdk.WTException;
import com.sonicle.webtop.core.util.IdentifierUtils;
import com.sonicle.webtop.core.util.VCardUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.internet.InternetAddress;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class ManagerUtils {
	
	public static final String CARDDAV_ADDRESSBOOK_URL = "/addressbooks/{0}/{1}";
	public static final String CATEGORY_LINK_CARDDAV = "cardDav";
	private static final Pattern PATTERN_VIRTUALRCPT_LIST = Pattern.compile("^" + RCPT_ORIGIN_LIST + "-(\\d+)$");
	
	public static String getProductName() {
		return WT.getPlatformName() + " Contacts";
	}
	
	public static int decodeAsCategoryId(String categoryPublicUid) throws WTException {
		try {
			return Integer.valueOf(new String(Base58.decode(categoryPublicUid)));
		} catch(RuntimeException ex) { // Not a Base58 input
			throw new WTException(ex, "Invalid category UID encoding");
		}
	}
	
	public static String encodeAsCategoryUid(int categoryId) {
		return Base58.encode(StringUtils.leftPad(String.valueOf(categoryId), 10, "0").getBytes());
	}
	
	public static String buildContactUid(int contactId, String internetName) {
		String id = IdentifierUtils.getUUIDTimeBased(true) + "." + String.valueOf(contactId);
		return VCardUtils.buildUid(DigestUtils.md5Hex(id), internetName);
	}
	
	public static String buildHref(String publicUid) {
		return publicUid + ".vcf";
	}
	
	public static Category createCategory(OCategory src) {
		if (src == null) return null;
		return fillCategory(new Category(), src);
	}
	
	public static Category fillCategory(Category tgt, OCategory src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCategoryId(src.getCategoryId());
			tgt.setDomainId(src.getDomainId());
			tgt.setUserId(src.getUserId());
			tgt.setBuiltIn(src.getBuiltIn());
			tgt.setProvider(EnumUtils.forSerializedName(src.getProvider(), Category.Provider.class));
			tgt.setName(src.getName());
			tgt.setDescription(src.getDescription());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.forSerializedName(src.getSync(), Category.Sync.class));
			tgt.setIsDefault(src.getIsDefault());
			// TODO: aggiungere supporto campo is_private
			//cat.setIsPrivate(ocat.getIsPrivate());
			tgt.setParameters(src.getParameters());
			tgt.setRemoteSyncFrequency(src.getRemoteSyncFrequency());
			tgt.setRemoteSyncTimestamp(src.getRemoteSyncTimestamp());
			tgt.setRemoteSyncTag(src.getRemoteSyncTag());
		}
		return tgt;
	}
	
	public static OCategory createOCategory(Category src) {
		if (src == null) return null;
		return fillOCategory(new OCategory(), src);
	}
	
	public static OCategory fillOCategory(OCategory tgt, Category src) {
		if ((tgt != null) && (src != null)) {
			tgt.setCategoryId(src.getCategoryId());
			tgt.setDomainId(src.getDomainId());
			tgt.setUserId(src.getUserId());
			tgt.setBuiltIn(src.getBuiltIn());
			tgt.setProvider(EnumUtils.toSerializedName(src.getProvider()));
			tgt.setName(src.getName());
			tgt.setDescription(src.getDescription());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.toSerializedName(src.getSync()));
			tgt.setIsDefault(src.getIsDefault());
			// TODO: aggiungere supporto campo is_private
			//ocat.setIsPrivate(cat.getIsPrivate());
			tgt.setParameters(src.getParameters());
			tgt.setRemoteSyncFrequency(src.getRemoteSyncFrequency());
			tgt.setRemoteSyncTimestamp(src.getRemoteSyncTimestamp());
			tgt.setRemoteSyncTag(src.getRemoteSyncTag());
		}
		return tgt;
	}
	
	public static CategoryPropSet createCategoryPropSet(OCategoryPropSet src) {
		if (src == null) return null;
		return fillCategoryPropSet(new CategoryPropSet(), src);
	}
	
	public static CategoryPropSet fillCategoryPropSet(CategoryPropSet tgt, OCategoryPropSet src) {
		if ((tgt != null) && (src != null)) {
			tgt.setHidden(src.getHidden());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.forSerializedName(src.getSync(), Category.Sync.class));
		}
		return tgt;
	}
	
	public static OCategoryPropSet createOCategoryPropSet(CategoryPropSet src) {
		if (src == null) return null;
		return fillOCategoryPropSet(new OCategoryPropSet(), src);
	}
	
	public static OCategoryPropSet fillOCategoryPropSet(OCategoryPropSet tgt, CategoryPropSet src) {
		if ((tgt != null) && (src != null)) {
			tgt.setHidden(src.getHidden());
			tgt.setColor(src.getColor());
			tgt.setSync(EnumUtils.toSerializedName(src.getSync()));
		}
		return tgt;
	}
	
	public static int getListIdFromInternetAddress(InternetAddress ia) {
		VirtualAddress va=new VirtualAddress(ia.getAddress());
		return getListIdFromVirtualRecipient(va.getLocal());
	}
	
	public static int getListIdFromVirtualRecipient(String virtualRecipient) {
		Matcher matcher = PATTERN_VIRTUALRCPT_LIST.matcher(virtualRecipient);
		int contactId=-1;
		if (matcher.matches()) {
			contactId = Integer.valueOf(matcher.group(1));
		}
		return contactId;
	}
	
	
	
}
