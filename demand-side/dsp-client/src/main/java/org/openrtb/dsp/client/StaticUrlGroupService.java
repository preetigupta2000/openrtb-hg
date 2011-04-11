/*
 * Copyright (c) 2010, The OpenRTB Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *   3. Neither the name of the OpenRTB nor the names of its contributors
 *      may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.openrtb.dsp.client;

import org.openrtb.common.model.UrlGroup;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.UrlGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. StaticUrlGroupService
 *
 * @author jdrahos
 */
public class StaticUrlGroupService implements UrlGroupService {
	@Override
	public Long getLastSynchronizationTimestamp(final SupplySidePlatform ssp) {
		//requesting all groups
		return null;
	}

	@Override
	public void replaceUrlGroups(final SupplySidePlatform ssp, final Collection<UrlGroup> urlGroups) {
		if (urlGroups == null || urlGroups.size() == 0) {
			return;
		}

		for (UrlGroup urlGroup : urlGroups) {
			if (urlGroup.getLandingPages() == null || urlGroup.getLandingPages().size() == 0) {
				UrlGroupStore.removeUrlGroup(urlGroup.getGroupName());
				continue;
			}

			if (!UrlGroupStore.urlGroupExists(urlGroup.getGroupName())) {
				UrlGroupStore.createUrlGroup(urlGroup);
			} else {
				UrlGroupStore.updateUrlGroup(urlGroup);
			}
		}
	}

	private static final class UrlGroupStore {
		private static Logger logger = LoggerFactory.getLogger(UrlGroupStore.class);
		private static Map<String, Set<String>> urlGroups = new HashMap<String, Set<String>>();

		static boolean urlGroupExists(String groupName) {
			return urlGroups.containsKey(groupName);
		}

		static void createUrlGroup(UrlGroup urlGroup) {
			urlGroups.put(urlGroup.getGroupName(), urlGroup.getLandingPages());
			logger.debug("Url group {} created", urlGroup.getGroupName());
		}

		static void updateUrlGroup(UrlGroup urlGroup) {
			Set<String> domains = urlGroups.get(urlGroup.getGroupName());
			domains.clear();
			domains.addAll(urlGroup.getLandingPages());
			logger.debug("Url group {} updated", urlGroup.getGroupName());
		}

		static void removeUrlGroup(String groupName) {
			urlGroups.remove(groupName);
			logger.debug("Url group {} removed", groupName);
		}
	}
}
