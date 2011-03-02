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

import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.model.Operator;
import org.openrtb.common.model.PreferenceType;
import org.openrtb.common.model.Publisher;
import org.openrtb.common.model.PublisherPreference;
import org.openrtb.common.model.Rule;
import org.openrtb.ssp.SupplySideService;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SupplySideServiceRefImplTest {

	private SupplySideService ssp;

	@Before
	public void setup() {
		ssp = new SupplySideServiceRefImpl();
	}

	@Test
	public void getPublisherPreferences() {
		Publisher publisher1 = new Publisher("3422", "0", null, Arrays.asList(PreferenceType.URL), System.currentTimeMillis());
		Collection<PublisherPreference> preferences = ssp.getPublisherPreferences(Arrays.asList(publisher1));
		assertNotNull(preferences);
		assertEquals(1, preferences.size());
		PublisherPreference publisherPreference = preferences.iterator().next();
		PublisherPreference expectedPublisherPreference = new PublisherPreference("3422", "0");
		expectedPublisherPreference.addRule(new Rule(Operator.include, PreferenceType.URL, Arrays.asList((Object) "abc.com")));
		assertEquals(publisherPreference, expectedPublisherPreference);
	}

	@Test
	public void getPublisherPreferences_SiteId() {
		Publisher publisher1 = new Publisher("2342", "2", "joeads.com", Arrays.asList(PreferenceType.creativeAttribute), System.currentTimeMillis());
		Collection<PublisherPreference> preferences = ssp.getPublisherPreferences(Arrays.asList(publisher1));
		assertNotNull(preferences);
		assertEquals(1, preferences.size());
		PublisherPreference publisherPreference = preferences.iterator().next();
		PublisherPreference expectedPublisherPreference = new PublisherPreference("2342", "2");
		expectedPublisherPreference.setSiteTLD("joeads.com");
		expectedPublisherPreference.addRule(new Rule(Operator.include, PreferenceType.creativeAttribute, Arrays.asList((Object) "1", "2")));
		assertEquals(publisherPreference, expectedPublisherPreference);
	}

	@Test
	public void getPublisherPreferences_All() {
		Publisher publisher1 = new Publisher("0", "0", null, Arrays.asList(PreferenceType.creativeAttribute), System.currentTimeMillis());
		Set<PublisherPreference> preferences = new HashSet<PublisherPreference>(ssp.getPublisherPreferences(Arrays.asList(publisher1)));
		assertNotNull(preferences);
		assertEquals(3, preferences.size());
		PublisherPreference expectedPublisherPreference1 = new PublisherPreference("3422", "0");
		expectedPublisherPreference1.addRule(new Rule(Operator.exclude, PreferenceType.creativeAttribute, Arrays.asList((Object) "1", "2")));
		PublisherPreference expectedPublisherPreference2 = new PublisherPreference("2342", "1");
		expectedPublisherPreference2.setSiteTLD("joe.com");
		expectedPublisherPreference2.addRule(new Rule(Operator.include, PreferenceType.creativeAttribute, Arrays.asList((Object) "1", "2", "9")));
		PublisherPreference expectedPublisherPreference3 = new PublisherPreference("2342", "2");
		expectedPublisherPreference3.setSiteTLD("joeads.com");
		expectedPublisherPreference3.addRule(new Rule(Operator.include, PreferenceType.creativeAttribute, Arrays.asList((Object) "1", "2")));

		assertTrue(preferences.contains(expectedPublisherPreference1));
		assertTrue(preferences.contains(expectedPublisherPreference2));
		assertTrue(preferences.contains(expectedPublisherPreference3));
	}

}
