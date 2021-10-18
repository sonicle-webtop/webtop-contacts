/*
 * Copyright (C) 2021 Sonicle S.r.l.
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
 * display the words "Copyright (C) 2021 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts;

import com.sonicle.commons.AlgoUtils;
import com.sonicle.commons.qbuilders.conditions.Condition;
import com.sonicle.commons.web.json.JsonResult;
import com.sonicle.commons.web.json.MapItem;
import com.sonicle.commons.web.json.bean.StringMap;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.api.ListsApi;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AddListMembers;
import com.sonicle.webtop.contacts.mailchimp.cli.model.BatchUpdateListMembers;
import com.sonicle.webtop.contacts.mailchimp.cli.model.BatchUpdateListMembersErrors;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CollectionOfSegments;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List4;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List6;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListMembers1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListMembers4;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MembersToSubscribeUnsubscribeTofromAListInBatch;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberList1;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.contacts.model.Contact;
import com.sonicle.webtop.contacts.model.ContactLookup;
import com.sonicle.webtop.contacts.model.ContactQuery;
import com.sonicle.webtop.contacts.model.ContactType;
import com.sonicle.webtop.contacts.model.Grouping;
import com.sonicle.webtop.contacts.model.ListContactsResult;
import com.sonicle.webtop.contacts.model.ShowBy;
import com.sonicle.webtop.contacts.msg.MailchimpSyncEndSM;
import com.sonicle.webtop.contacts.msg.MailchimpSyncLogSM;
import com.sonicle.webtop.core.CoreManager;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.WebTopSession;
import com.sonicle.webtop.core.model.Tag;
import com.sonicle.webtop.core.sdk.UserProfileId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 *
 * @author gabriele.bulfon
 */
public class MailchimpSyncThread extends Thread {
	
	public static final Logger logger=Service.logger;
	
	String serviceId;
	WebTopSession wts;
	
	String oid;
	String srcPid;
	String srcCatId;
	String audienceId;
	boolean syncTags;
	String tags[];
	String incomingAudienceId;
	String incomingCategoryId;
	
	public MailchimpSyncThread(String threadName, WebTopSession wts, 
			String oid, String srcPid, String srcCatId, String audienceId, boolean syncTags, String tags[], 
			String incomingAudienceId, String incomingCategoryId) {
		
		super(threadName);
		
		this.serviceId="com.sonicle.webtop.contacts";
		this.wts=wts;
		
		this.oid=oid;
		this.srcPid=srcPid;
		this.srcCatId=srcCatId;
		this.audienceId=audienceId;
		this.syncTags=syncTags;
		this.tags=tags;
		this.incomingAudienceId=incomingAudienceId;
		this.incomingCategoryId=incomingCategoryId;
	}
	
	public void run() {
		boolean errors=false;
		
		try {
			ContactsManager manager = (ContactsManager)WT.getServiceManager(serviceId);
			CoreManager cm=WT.getCoreManager();
			UserProfileId srcPidUserProfileId=new UserProfileId(srcPid);
			
			ApiClient cli=manager.getMailchimpApiClient();
			ListsApi lists=new ListsApi(cli);
			
			wts.notify(new MailchimpSyncLogSM(oid, WT.lookupResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.prepareCategories")));
			//prepare categories to be synchronized
			ArrayList<Category> categories=new ArrayList<>();
			if (!StringUtils.isEmpty(srcCatId)) {
				categories.add(manager.getCategory(Integer.parseInt(srcCatId)));
			} else {
				String myPid=wts.getProfileId().toString();
				Map<Integer, Category> cats=null;
				if (myPid.equals(srcPid)) {
					cats=manager.listCategories();
					for(Category cat: cats.values()) {
						if (!cat.isProviderRemote()) categories.add(cat);
					}
				} else {
					Set<Integer> icats=manager.listIncomingCategoryIds(srcPidUserProfileId);
					for(Integer icat: icats)
						categories.add(manager.getCategory(icat));
				}
			}
			
			//delete tags of selected categories
			wts.notify(new MailchimpSyncLogSM(oid,WT.lookupResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.removeTagsForCategories")));
			CollectionOfSegments audienceTags=lists.previewASegment(audienceId, null, null, 1000, null, null, null, null, null, null, null, null, null);
			for(List6 s: audienceTags.getSegments()) {
				String sid=s.getId().toString();
				String sname=s.getName();
				for(Category cat: categories) {
					if (cat.getName().equals(sname)) {
						lists.deleteListsIdSegmentsId(audienceId, sid);
						wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.deletedTag", sname)));
						break;
					}
				}
			}
			
			//prepare and run query on contacts
			if (syncTags) {
				wts.notify(new MailchimpSyncLogSM(oid,WT.lookupResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.prepareTags")));
				//prepare all wt tags
				Collection<Tag> values=cm.listTags().values();
				Tag allWtTags[]=new Tag[values.size()];
				allWtTags=values.toArray(allWtTags);
				
				Tag wtTags[];
				//do all tags?
				if (tags==null) {
					wtTags=allWtTags;
				} else {
					//prepare selected wt tags
					ArrayList<Tag> arr=new ArrayList<>();
					for(Tag wtTag: allWtTags) {
						for(String stag: tags) {
							if (stag.equals(wtTag.getTagId()))
								arr.add(wtTag);
						}
					} 
					wtTags=new Tag[arr.size()];
					wtTags=arr.toArray(wtTags);
				}
				
				wts.notify(new MailchimpSyncLogSM(oid,WT.lookupResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.removeTagsForTags")));
				//first delete all tags in wtTags
				for(List6 s: audienceTags.getSegments()) {
					String sid=s.getId().toString();
					String sname=s.getName();
					for(Tag tag: wtTags) {
						if (getMailchimpTagName(tag).equals(sname)) {
							wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.deletedTag", sname)));
							lists.deleteListsIdSegmentsId(audienceId, sid);
							break;
						}
					}
				}
				
				
				//remember all emails done
				ArrayList<String> allEmails=new ArrayList<>();
				//remember emails for each tag
				HashMap<String,ArrayList<String>> tagMap=new HashMap<>();
				//for each category run entire loop
				int count=0;
				for(Category category: categories) {
					wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.syncCategory", srcPidUserProfileId.getName(), category.getName())));
					ArrayList<Integer> icats=new ArrayList<>();
					icats.add(category.getCategoryId());
					//for each tag select and create/update contacts, then create tag with members
					for(Tag tag: wtTags) {
						ContactQuery q = new ContactQuery();
						Condition<ContactQuery> cp=q.tag().eq(tag.getTagId());
						ListContactsResult lcr=manager.listContacts(icats, ContactType.CONTACT, Grouping.ALPHABETIC, ShowBy.DISPLAY, cp);
						ArrayList<String> emails=updateMailchimpContacts(lists,audienceId,lcr,null,tag.getName());
						count+=emails.size();
						//save emails in tag map for this category
						updateMailchimpTagsMap(tagMap, category.getName(), emails);
						//save emails in tag map for this tag
						updateMailchimpTagsMap(tagMap, getMailchimpTagName(tag), emails);
						//save emails in full list
						allEmails.addAll(emails);
					}
					//create/update contacts without tags
					ContactQuery q = new ContactQuery();
					Condition<ContactQuery> cp=q.trueCondition();
					ListContactsResult lcr=manager.listContacts(icats, ContactType.CONTACT, Grouping.ALPHABETIC, ShowBy.DISPLAY, cp);
					ArrayList<String> emails=updateMailchimpContacts(lists,audienceId,lcr,allEmails,category.getName());
					count+=emails.size();
					//save emails in tag map for this category
					updateMailchimpTagsMap(tagMap, category.getName(), emails);
				}
				wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.doneCategories", count)));
				
				//create tags in map tagging saved emails
				wts.notify(new MailchimpSyncLogSM(oid,WT.lookupResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.createTags")));
				for(String tagName: tagMap.keySet()) {
					ArrayList<String> emails=tagMap.get(tagName);
					try {
						createMailchimpTag(lists, audienceId, tagName, emails);
					} catch(ApiException exc) {
						wts.notify(new MailchimpSyncLogSM(oid, "Error on tag \""+tagName+"\": "+exc.getMessage()));
						logger.error("Error in SyncMailChimp", exc);
						errors=true;
					}
				}
			} else {
				//for each category add all contacts creating necessary tags
				int count=0;
				for(Category category: categories) {
					wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.syncCategory", srcPidUserProfileId.getName(), category.getName())));
					
					ArrayList<Integer> icats=new ArrayList<>();
					icats.add(category.getCategoryId());
					
					ContactQuery q = new ContactQuery();
					Condition<ContactQuery> cp=q.trueCondition();
					//select and create contacts
					ListContactsResult lcr=manager.listContacts(icats, ContactType.CONTACT, Grouping.ALPHABETIC, ShowBy.DISPLAY, cp);
					ArrayList<String> catEmails=updateMailchimpContacts(lists,audienceId,lcr,null,category.getName());
					count+=catEmails.size();
					//create tag for category, including all cat emails
					try {
						createMailchimpTag(lists, audienceId, category.getName(), catEmails);
					} catch(ApiException exc) {
						wts.notify(new MailchimpSyncLogSM(oid, "Error on tag \""+category.getName()+"\": "+exc.getMessage()));
						logger.error("Error in SyncMailChimp", exc);
						errors=true;
					}
				}
				wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.doneCategories", count)));
			}
			
			wts.notify(new MailchimpSyncLogSM(oid,WT.lookupResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.receiveNew")));
			List<ListMembers4> members=lists.getListsIdMembers(incomingAudienceId, null, null, 1000, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null).getMembers();
			for(ListMembers4 member: members) {
				Map<String,Object> mfields=member.getMergeFields();
				Contact contact=new Contact();
				contact.setEmail1(member.getEmailAddress());
				contact.setFirstName((String)mfields.get("FNAME"));
				contact.setLastName((String)mfields.get("LNAME"));
				contact.setWorkTelephone1((String)mfields.get("PHONE"));
				try {
					HashMap<String,Object> addressMap=(HashMap)mfields.get("ADDRESS");
					String address=(String)addressMap.get("addr1");
					String addr2=(String)addressMap.get("addr2");
					if (!StringUtils.isEmpty(addr2)) address+=" "+addr2;
					contact.setWorkAddress(address);
					contact.setWorkCity((String)addressMap.get("city"));
					contact.setWorkState((String)addressMap.get("state"));
					contact.setWorkPostalCode((String)addressMap.get("zip"));
					contact.setWorkCountry((String)addressMap.get("country"));
				} catch(Exception exc) {}
				contact.setCategoryId(Integer.parseInt(incomingCategoryId));
				manager.addContact(contact);
				
				lists.deleteListsIdMembersId(incomingAudienceId, AlgoUtils.md5Hex(member.getEmailAddress().toLowerCase()));
			}
			wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.doneNew", members.size())));
			
			wts.notify(new MailchimpSyncLogSM(oid,WT.lookupResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.complete")));
			
		} catch(Throwable t) {
			wts.notify(new MailchimpSyncLogSM(oid, "Error: "+t.getMessage()));
			logger.error("Error in SyncMailChimp", t);
			errors=true;
		}
					
		wts.notify(new MailchimpSyncEndSM(errors));
	}
	
	private String getMailchimpTagName(Tag tag) {
		return "["+tag.getName()+"]";
	}
	
	private void updateMailchimpTagsMap(HashMap<String,ArrayList<String>> tagMap, String tagName, ArrayList<String> emails) {
		ArrayList tagEmails=tagMap.get(tagName);
		if (tagEmails==null) tagMap.put(tagName, tagEmails=new ArrayList<>());
		tagEmails.addAll(emails);
	}
	
	private ArrayList<String> updateMailchimpContacts(ListsApi lists, String audienceId, ListContactsResult lcr, ArrayList<String> excludeEmails, String tagName) throws ApiException {
		//Mailchimp allows only a maximum of 500 members updates/insert per api call
		ArrayList<MembersToSubscribeUnsubscribeTofromAListInBatch> bulks=new ArrayList<>();
		int count=0;
		MembersToSubscribeUnsubscribeTofromAListInBatch m500=new MembersToSubscribeUnsubscribeTofromAListInBatch();
		m500.setUpdateExisting(true);
		bulks.add(m500);
		ArrayList<String> emails=new ArrayList();
		for(ContactLookup cl: lcr.items) {
			AddListMembers member=new AddListMembers();
			String email=cl.getEmail1();
			if (StringUtils.isEmpty(email)) continue;
			if (excludeEmails!=null && excludeEmails.contains(email)) continue;
			emails.add(email);
			member.setEmailAddress(email);
			member.setEmailType("html");
			member.setStatus(com.sonicle.webtop.contacts.mailchimp.cli.model.AddListMembers.StatusEnum.SUBSCRIBED);
			HashMap<String,Object> merge_fields=new HashMap<>();
			merge_fields.put("FNAME", cl.getFirstName());
			merge_fields.put("LNAME", cl.getLastName());
			member.setMergeFields(merge_fields);			
			m500.addMembersItem(member);
			++count;
			if ((count%500)==0) {
				m500=new MembersToSubscribeUnsubscribeTofromAListInBatch();
				m500.setUpdateExisting(true);
				bulks.add(m500);
			}
		}
		for(MembersToSubscribeUnsubscribeTofromAListInBatch m: bulks) {
			BatchUpdateListMembers result=lists.postListsId(m, audienceId, true, true);
			if (result.getNewMembers().size()>0) {
				wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.newMembers", tagName, result.getNewMembers().size())));
			}
			if (result.getUpdatedMembers().size()>0) {
				wts.notify(new MailchimpSyncLogSM(oid,WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.updatedMembers", tagName, result.getUpdatedMembers().size())));
			}
			if (result.getErrors().size()>0) {
				StringBuffer errors=new StringBuffer();
				errors.append('\n');
				for(BatchUpdateListMembersErrors bulme: result.getErrors()) {
					errors.append(WT.lookupFormattedResource(serviceId, wts.getUserProfile().getLocale(), "syncMailchimp.log.contactError", bulme.getError()));
					errors.append('\n');
				}
				wts.notify(new MailchimpSyncLogSM(oid,errors.toString()));
			}
		}
		return emails;
	}
	
	private void createMailchimpTag(ListsApi lists, String audienceId, String tagName, ArrayList<String> emails) throws ApiException {
		List3 body=new List3();
		body.setName(tagName);
		body.setStaticSegment(emails);
		List4 ret=lists.postListsIdSegments(body, audienceId);
	}
	
}
