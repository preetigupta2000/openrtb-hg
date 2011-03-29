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
import org.openrtb.common.model.UrlGroup;
import org.openrtb.ssp.service.UrlGroupSupplySideService;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA. UrlGroupSupplySideServiceRefImplTest
 *
 * @author jdrahos
 */
public class UrlGroupSupplySideServiceRefImplTest {
	private UrlGroupSupplySideService service;

	@Before
	public void setup() {
		service = new UrlGroupSupplySideServiceRefImpl();
	}

	@Test
	public void getPublisherPreferences_NullTimestamp() {
		Long timestamp = null;
		Collection<UrlGroup> urlGroups = service.getUrlGroups(timestamp);
		assertNotNull(urlGroups);
		assertEquals(3, urlGroups.size());
	}

	@Test
	public void getPublisherPreferences_Timestamp() {
		Long timestamp = 50L;
		Collection<UrlGroup> urlGroups = service.getUrlGroups(timestamp);
		assertNotNull(urlGroups);
		assertEquals(3, urlGroups.size());

		timestamp = 100L;
		urlGroups = service.getUrlGroups(timestamp);
		assertNotNull(urlGroups);
		assertEquals(2, urlGroups.size());

		Iterator<UrlGroup> iterator = urlGroups.iterator();
		while (iterator.hasNext()) {
			UrlGroup urlGroup = iterator.next();
			assertTrue(UrlGroupSupplySideServiceRefImpl.urlGroups.get(urlGroup) > timestamp);
		}

		timestamp = 150L;
		urlGroups = service.getUrlGroups(timestamp);
		assertNotNull(urlGroups);
		assertEquals(1, urlGroups.size());

		iterator = urlGroups.iterator();
		while (iterator.hasNext()) {
			UrlGroup urlGroup = iterator.next();
			assertTrue(UrlGroupSupplySideServiceRefImpl.urlGroups.get(urlGroup) > timestamp);
		}

		timestamp = 2000L;
		urlGroups = service.getUrlGroups(timestamp);
		assertNotNull(urlGroups);
		assertEquals(0, urlGroups.size());
	}
}
