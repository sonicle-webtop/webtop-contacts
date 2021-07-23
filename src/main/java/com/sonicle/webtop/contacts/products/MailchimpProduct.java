/*
 * Copyright (C) 2020 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2020 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.products;

import com.sonicle.webtop.core.sdk.BaseServiceProduct;

/**
 *
 * @author gbulfon
 */
public final class MailchimpProduct extends BaseServiceProduct {
	public static final String PRODUCT_ID = "SNCL-WT-CONTACTS-MAILCHIMP";
	public static final String PRODUCT_NAME = "Mailchimp";
	public static final String PUBLIC_KEY = 
		"30819f300d06092a864886f70d010101050003818d003081893032301006\n"+
		"072a8648ce3d02002EC311215SHA512withECDSA106052b81040006031e0\n"+
		"004132a4a544f9caee61320b466bb5b34d6dd1bbfbbda4922664b032a8aG\n"+
		"028181008b454bd2a67190d4b8b7222b25783c101a5adb654b4e416c70bb\n"+
		"3de00a2438620e7d70df2e9fbaa45f29c8b508d991c3094bc5c7dc8d41e7\n"+
		"5375839ea5b02339dcecd4a9c298cea4ee62fb81b54f1f90c46396c1fa5b\n"+
		"5bf837b5835300e7052703RSA4102413SHA512withRSA90c1d747671933c\n"+
		"6e8a1c78390878e580af7ed28224520958714d921461a8ddd0203010001";

	
	public MailchimpProduct(String domainId) {
		super(domainId, HardwareIDSource.DOMAIN_INTERNET_NAME);
	}
	
	@Override
	public String getProductCode() {
		return PRODUCT_ID;
	}
	
	@Override
	public String getProductName() {
		return PRODUCT_NAME;
	}
	
	@Override
	public String getPublicKey() {
		return PUBLIC_KEY;
	}
}
