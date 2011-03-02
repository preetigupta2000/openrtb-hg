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
package org.openrtb.ssp.client;

import org.openrtb.common.model.Operator;
import org.openrtb.common.model.PreferenceType;
import org.openrtb.common.model.Publisher;
import org.openrtb.common.model.PublisherPreference;
import org.openrtb.common.model.Rule;
import org.openrtb.ssp.SupplySideService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A sample reference implementation in order to demonstrate the role of SSP implementor.
 *
 * @since 1.0.1
 */
public class SupplySideServiceRefImpl implements SupplySideService {

	private Map<String, Map<String, PublisherPreference>> publisherPreferencesDB = new HashMap<String, Map<String, PublisherPreference>>();
	private String secret = "RTB";
	private String org = "The SSP";

	public SupplySideServiceRefImpl() {
		//Publisher no. 1 - All sites
		List<Rule> rules1 = new ArrayList<Rule>();
		rules1.add(new Rule(Operator.include, PreferenceType.URL, Arrays.asList((Object) "abc.com")));
		rules1.add(new Rule(Operator.exclude, PreferenceType.creativeAttribute, Arrays.asList((Object) "1", "2")));
		Map<String, PublisherPreference> publisherSiteMap1 = new HashMap<String, PublisherPreference>();
		//ID 0 means all sites for publisher
		publisherSiteMap1.put("0", new PublisherPreference("3422", "0", null, rules1));
		publisherPreferencesDB.put("3422", publisherSiteMap1);

		//Publisher no. 2 - Site no 1
		List<Rule> rules2 = new ArrayList<Rule>();
		rules2.add(new Rule(Operator.exclude, PreferenceType.URL, Arrays.asList((Object) "abc.com")));
		rules2.add(new Rule(Operator.include, PreferenceType.creativeAttribute, Arrays.asList((Object) "1", "2", "9")));
		Map<String, PublisherPreference> publisherSiteMap2 = new HashMap<String, PublisherPreference>();
		publisherSiteMap2.put("1", new PublisherPreference("2342", "1", "joe.com", rules2));

		//Publisher no. 3 - Site no 2
		List<Rule> rules3 = new ArrayList<Rule>();
		rules3.add(new Rule(Operator.include, PreferenceType.URL, Arrays.asList((Object) "cnn.com")));
		rules3.add(new Rule(Operator.include, PreferenceType.creativeAttribute, Arrays.asList((Object) "1", "2")));
		publisherSiteMap2.put("2", new PublisherPreference("2342", "2", "joeads.com", rules3));
		publisherPreferencesDB.put("2342", publisherSiteMap2);
	}

	@Override
	public Collection<PublisherPreference> getPublisherPreferences(final Collection<Publisher> publishers) {
		List<PublisherPreference> publisherPreferences = new ArrayList<PublisherPreference>();
		if (publishers == null || publishers.size() == 0) {
			return publisherPreferences;
		}
		//if publisher ID==0, return all preferences
		Publisher firstPublisher = publishers.iterator().next();
		if ("0".equals(firstPublisher.getPublisherID())) {
			List<PreferenceType> preferenceTypes = firstPublisher.getPreferenceTypes();
			for (Map<String, PublisherPreference> publisherPreferenceMap : publisherPreferencesDB.values()) {
				for (PublisherPreference publisherPreference : publisherPreferenceMap.values()) {
					publisherPreferences.add(getForPreferenceTypes(publisherPreference, preferenceTypes));
				}
			}
		} else {
			for (Publisher publisher : publishers) {
				List<PreferenceType> preferenceTypes = publisher.getPreferenceTypes();
				Map<String, PublisherPreference> sites = publisherPreferencesDB.get(publisher.getPublisherID());
				if (publisher.getSiteID().equals("0")) {
					for (PublisherPreference publisherPreference : sites.values()) {
						publisherPreferences.add(getForPreferenceTypes(publisherPreference, preferenceTypes));
					}
				} else {
					publisherPreferences.add(getForPreferenceTypes(sites.get(publisher.getSiteID()), preferenceTypes));
				}
			}
		}
		return publisherPreferences;
	}

	@Override
	public byte[] getSharedSecret(String dsp) {
		return secret.getBytes();
	}

	@Override
	public String getOrganization() {
		return org;
	}

	private static PublisherPreference getForPreferenceTypes(PublisherPreference currentPublisherPreference, List<PreferenceType> preferenceTypes) {
		PublisherPreference publisherPreference = new PublisherPreference(currentPublisherPreference.getPublisherID(), currentPublisherPreference.getSiteID());
		publisherPreference.setSiteTLD(currentPublisherPreference.getSiteTLD());
		Set<PreferenceType> preferenceTypeSet = new HashSet<PreferenceType>(preferenceTypes);
		for (Rule rule : currentPublisherPreference.getRules()) {
			if (preferenceTypeSet.contains(rule.getType())) {
				publisherPreference.addRule(rule);
			}
		}
		return publisherPreference;
	}

}
