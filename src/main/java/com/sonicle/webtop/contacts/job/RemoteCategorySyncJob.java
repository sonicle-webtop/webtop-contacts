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
package com.sonicle.webtop.contacts.job;

import com.sonicle.commons.time.DateTimeUtils;
import com.sonicle.webtop.contacts.ConcurrentSyncException;
import com.sonicle.webtop.contacts.ContactsManager;
import com.sonicle.webtop.contacts.ContactsServiceSettings;
import com.sonicle.webtop.contacts.JobService;
import com.sonicle.webtop.contacts.model.Category;
import com.sonicle.webtop.core.app.SessionManager;
import com.sonicle.webtop.core.app.WT;
import com.sonicle.webtop.core.app.WebTopApp;
import com.sonicle.webtop.core.sdk.BaseJobService;
import com.sonicle.webtop.core.sdk.BaseJobServiceTask;
import com.sonicle.webtop.core.sdk.WTException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;

/**
 *
 * @author malbinola
 */
public class RemoteCategorySyncJob extends BaseJobServiceTask {
	private static final Logger logger = WT.getLogger(RemoteCategorySyncJob.class);
	private static final AtomicBoolean running = new AtomicBoolean(false);
	private JobService jobService = null;

	@Override
	public void setJobService(BaseJobService jobService) {
		this.jobService = (JobService)jobService;
	}
	
	@Override
	public void executeWork() {
		if (running.compareAndSet(false, true)) {
			try {
				internalExecuteWork();
			} finally {
				running.set(false);
			}
		}
	}
	
	private void internalExecuteWork() {
		DateTime now = DateTimeUtils.now();
		SessionManager sesMgr = WebTopApp.getInstance().getSessionManager();
		ContactsManager jobManager = (ContactsManager)WT.getServiceManager(jobService.SERVICE_ID);
		
		logger.debug("RemoteCategorySyncJob START [{}]", now);
		try {
			Map<String, Boolean> crseCache = new HashMap<>();
			Map<String, Boolean> rsowoCache = new HashMap<>();
			List<Category> cats = jobManager.listRemoteCategoriesToBeSynchronized();
			for (Category cat : cats) {
				if (shouldStop()) break; // Speed-up shutdown process!
				if (!isCategoryRemoteSyncEnabled(crseCache, cat.getDomainId())) continue; // Skip if sync is disabled!
				if (isRemoteSyncOnlyWhenOnline(rsowoCache, cat.getDomainId()) && !sesMgr.isOnline(cat.getProfileId())) continue; // Skip offline profiles!
				
				logger.debug("Checking category [{}, {}]", cat.getCategoryId(), cat.getName());
				if (isSyncNeeded(cat, now)) {
					logger.debug("Sync required. Last sync at: {}", cat.getRemoteSyncTimestamp());
					try {
						ContactsManager manager = (ContactsManager)WT.getServiceManager(jobService.SERVICE_ID, true, cat.getProfileId());
						manager.syncRemoteCategory(cat.getCategoryId(), false);
					} catch(ConcurrentSyncException ex1) {
						logger.trace("Remote sync skipped", ex1);
					} catch(Throwable t) {
						logger.error("Unable to run remote sync", t);
					}
				} else {
					logger.debug("Sync not needed. Last sync at: {}", cat.getRemoteSyncTimestamp());
				}
			}
		} catch(WTException ex) {
			logger.error("Error executing work", ex);
		} finally {
			logger.debug("RemoteCategorySyncJob END", now);
		}
	}
	
	private boolean isCategoryRemoteSyncEnabled(Map<String, Boolean> cache, String domainId) {
		if (!cache.containsKey(domainId)) {
			ContactsServiceSettings css = new ContactsServiceSettings(jobService.SERVICE_ID, domainId);
			cache.put(domainId, css.getCategoryRemoteAutoSyncEnabled());
		}
		return cache.get(domainId);
	}
	
	private boolean isRemoteSyncOnlyWhenOnline(Map<String, Boolean> cache, String domainId) {
		if (!cache.containsKey(domainId)) {
			ContactsServiceSettings css = new ContactsServiceSettings(jobService.SERVICE_ID, domainId);
			cache.put(domainId, css.getCategoryRemoteAutoSyncOnlyWhenOnline());
		}
		return cache.get(domainId);
	}

	private boolean isSyncNeeded(Category cat, DateTime now) {
		if (cat.getRemoteSyncTimestamp() == null) return true;
		return Minutes.minutesBetween(cat.getRemoteSyncTimestamp(), now).getMinutes() >= cat.getRemoteSyncFrequency();
	}
}
