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

import org.openrtb.common.model.PreferenceType;
import org.openrtb.common.model.Publisher;
import org.openrtb.common.model.PublisherPreference;
import org.openrtb.common.model.Rule;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.PublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. StaticPublisherService
 *
 * @author jdrahos
 */
public class StaticPublisherService extends AbstractStaticService implements PublisherService {

	private static final Logger log = LoggerFactory.getLogger(StaticPublisherService.class);
	private static final Long lastSyncTimeStamp = (System.currentTimeMillis() / 1000) - (24 * 60 * 60);

	private static final List<PreferenceType> preferenceTypes = new LinkedList<PreferenceType>();

	static {
		preferenceTypes.add(PreferenceType.URL);
		preferenceTypes.add(PreferenceType.creativeCategories);
		preferenceTypes.add(PreferenceType.creativeAttribute);
		preferenceTypes.add(PreferenceType.richmediaVendor);
		preferenceTypes.add(PreferenceType.trackingVendor);
	}

	private static final Map<String, Collection<Publisher>> publisherStore = new HashMap<String, Collection<Publisher>>();

	static {
		//publishers not blocked on the dsp side from the testssp ssp
		publisherStore.put("testssp", new LinkedList<Publisher>());
		publisherStore.get("testssp").add(new Publisher("1", "101", "test.co", preferenceTypes, lastSyncTimeStamp));
		publisherStore.get("testssp").add(new Publisher("2", "102", "test2.co", preferenceTypes, lastSyncTimeStamp));
		publisherStore.get("testssp").add(new Publisher("3", "103", "test3.co", preferenceTypes, lastSyncTimeStamp));
		publisherStore.get("testssp").add(new Publisher("4", "104", "test4.co", preferenceTypes, lastSyncTimeStamp));
	}

	@Override
	public Collection<Publisher> getPublisherList(final SupplySidePlatform ssp) {
		Collection<Publisher> pubs = new LinkedList<Publisher>();
		Collection<Publisher> existingPublishers = getPublishersForSSP(ssp);

		if (existingPublishers != null) {
			//request publisher preferences for publishers of the ssp which are already in the system
			pubs.addAll(existingPublishers);
			log.info("requesting [" + existingPublishers.size() + "] publisher preferences from [" + ssp.getOrganization() + "] for existing publishers");
		} else {
			//request all publisher preferences for the ssp which wasn't synced yet
			pubs.add(new Publisher("0", "0", null, preferenceTypes, null));
			log.info("requesting all publisher preferences from [" + ssp.getOrganization() + "]");
		}

		return pubs;
	}

	@Override
	public void replacePublisherPreferencesList(final SupplySidePlatform ssp, final Collection<PublisherPreference> publisherPreferences) {
		StringBuilder valuesBuilder = new StringBuilder();
		Iterator<Object> iter;

		for (PublisherPreference publisherPreference : publisherPreferences) {
			log.info("received preference for publisher [" + publisherPreference.getPublisherID() + "] for site [" + publisherPreference.getSiteID() + "][" + publisherPreference.getSiteTLD() + "] with [" + publisherPreference.getRules().size() + "] rules");

			for (Rule rule : publisherPreference.getRules()) {
				valuesBuilder.delete(0, valuesBuilder.length());
				if (rule.getValues() != null && rule.getValues().size() != 0) {
					iter = rule.getValues().iterator();
					valuesBuilder.append(iter.next());
					while (iter.hasNext()) {
						valuesBuilder.append(", ").append(iter.next());
					}
				}
				log.info("- rule with operator [" + rule.getOperator() + "] type [" + rule.getType() + "] and values [" + valuesBuilder.toString() + "]");
			}

			processPublisherPreference(ssp.getOrganization(), publisherPreference);
		}
	}

	private Collection<Publisher> getPublishersForSSP(SupplySidePlatform ssp) {
		String sspOrganization = ssp.getOrganization().toLowerCase();

		if (publisherStore.containsKey(sspOrganization)) {
			return publisherStore.get(sspOrganization);
		}

		return null;
	}

	private void processPublisherPreference(String sspOrganization, PublisherPreference publisherPreference) {
		sspOrganization = sspOrganization.toLowerCase();
		Long timestamp = System.currentTimeMillis() / 1000;

		if (!publisherStore.containsKey(sspOrganization)) {
			publisherStore.put(sspOrganization, new LinkedList<Publisher>());
		}

		Collection<Publisher> publishers = publisherStore.get(sspOrganization);

		for (Publisher existingPublisher : publishers) {
			if (existingPublisher.getPublisherID().equals(publisherPreference.getPublisherID()) && existingPublisher.getSiteID().equals(publisherPreference.getSiteID())) {
				existingPublisher.setTimestamp(timestamp);
				return;
			}
		}

		Publisher newPublisher = new Publisher();
		newPublisher.setPublisherID(publisherPreference.getPublisherID());
		newPublisher.setSiteID(publisherPreference.getSiteID());
		newPublisher.setSiteTLD(publisherPreference.getSiteTLD());
		newPublisher.setTimestamp(timestamp);

		publishers.add(newPublisher);
	}
}
