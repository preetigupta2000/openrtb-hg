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
package org.openrtb.dsp.core;

import org.junit.Test;
import org.openrtb.common.json.PublisherPreferencesRequestTranslator;
import org.openrtb.common.json.PublisherPreferencesResponseTranslator;
import org.openrtb.common.model.Identification;
import org.openrtb.common.model.Publisher;
import org.openrtb.common.model.PublisherPreference;
import org.openrtb.common.model.PublisherPreferencesRequest;
import org.openrtb.common.model.PublisherPreferencesResponse;
import org.openrtb.common.model.Status;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.IdentificationService;
import org.openrtb.dsp.intf.service.PublisherService;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by IntelliJ IDEA. PublisherPreferencesRequesterTest
 *
 * @author jdrahos
 */
public class PublisherPreferencesRequesterTest {
	private PublisherPreferencesRequester test;

	@Test
	public void requestAllPublisherPreferences_PlatformsAreNull() {
		Collection<SupplySidePlatform> platforms = null;

		IdentificationService identificationService = mock(IdentificationService.class);
		PublisherService publisherService = mock(PublisherService.class);

		when(identificationService.getServiceEndpoints()).thenReturn(platforms);

		test = new PublisherPreferencesRequester(null, identificationService);
		test.requestAllPublisherPreferences();

		verify(identificationService).getServiceEndpoints();
		verify(publisherService, never()).replacePublisherPreferencesList(any(SupplySidePlatform.class), anyCollectionOf(PublisherPreference.class));
	}

	@Test
	public void requestAllPublisherPreferences_PlatformsAreEmpty() {
		Collection<SupplySidePlatform> platforms = new LinkedList<SupplySidePlatform>();

		IdentificationService identificationService = mock(IdentificationService.class);
		PublisherService publisherService = mock(PublisherService.class);

		when(identificationService.getServiceEndpoints()).thenReturn(platforms);

		test = new PublisherPreferencesRequester(null, identificationService);
		test.requestAllPublisherPreferences();

		verify(identificationService).getServiceEndpoints();
		verify(publisherService, never()).replacePublisherPreferencesList(any(SupplySidePlatform.class), anyCollectionOf(PublisherPreference.class));
	}

	@Test
	public void requestPublisherPreferences_PublishersAreNull() {
		Collection<Publisher> publishers = null;
		SupplySidePlatform ssp = new SupplySidePlatform("test", "http://test.co/adv", "http://test.co/pub", "test".getBytes());

		PublisherService publisherService = mock(PublisherService.class);
		IdentificationService identificationService = mock(IdentificationService.class);

		when(identificationService.getOrganizationIdentifier()).thenReturn("test");
		when(publisherService.getPublisherList(ssp)).thenReturn(publishers);

		test = new PublisherPreferencesRequester(publisherService, identificationService);
		test.requestPublisherPreferences(ssp);

		verify(publisherService).getPublisherList(ssp);
		verify(publisherService, never()).replacePublisherPreferencesList(any(SupplySidePlatform.class), anyCollectionOf(PublisherPreference.class));
	}

	@Test
	public void requestPublisherPreferences_PublishersAreEmpty() {
		Collection<Publisher> publishers = new LinkedList<Publisher>();
		SupplySidePlatform ssp = new SupplySidePlatform("test", "http://test.co/adv", "http://test.co/pub", "test".getBytes());

		PublisherService publisherService = mock(PublisherService.class);
		IdentificationService identificationService = mock(IdentificationService.class);

		when(identificationService.getOrganizationIdentifier()).thenReturn("test");
		when(publisherService.getPublisherList(ssp)).thenReturn(publishers);

		test = new PublisherPreferencesRequester(publisherService, identificationService);
		test.requestPublisherPreferences(ssp);

		verify(publisherService).getPublisherList(ssp);
		verify(publisherService, never()).replacePublisherPreferencesList(any(SupplySidePlatform.class), anyCollectionOf(PublisherPreference.class));
	}

	@Test
	public void requestPublisherPreferences_ResponseIsNull() {
		PublisherPreferencesResponse response = null;

		Collection<Publisher> publishers = new LinkedList<Publisher>();
		publishers.add(new Publisher("0", "0", null, null, null));
		SupplySidePlatform ssp = new SupplySidePlatform("test", "http://test.co/adv", "http://test.co/pub", "test".getBytes());

		PublisherService publisherService = mock(PublisherService.class);
		IdentificationService identificationService = mock(IdentificationService.class);

		when(identificationService.getOrganizationIdentifier()).thenReturn("test");
		when(publisherService.getPublisherList(ssp)).thenReturn(publishers);

		test = new PublisherPreferencesNeverRequest(publisherService, identificationService);
		((PublisherPreferencesNeverRequest) test).setResponse(response);
		test.requestPublisherPreferences(ssp);

		verify(publisherService, never()).replacePublisherPreferencesList(any(SupplySidePlatform.class), anyCollectionOf(PublisherPreference.class));
	}

	@Test
	public void requestPublisherPreferences_PublisherPreferencesAreNull() throws IOException {
		Collection<PublisherPreference> publisherPreferences = null;

		Collection<Publisher> publishers = new LinkedList<Publisher>();
		publishers.add(new Publisher("0", "0", null, null, null));
		SupplySidePlatform ssp = new SupplySidePlatform("test", "http://test.co/adv", "http://test.co/pub", "test".getBytes());

		Identification dsp = new Identification("test");
		PublisherPreferencesRequest request = new PublisherPreferencesRequest(dsp, publishers);
		request.sign("test".getBytes(), new PublisherPreferencesRequestTranslator());
		String requestToken = request.getIdentification().getToken();

		PublisherPreferencesResponse response = new PublisherPreferencesResponse(dsp, new Status(requestToken));
		response.sign("test".getBytes(), new PublisherPreferencesResponseTranslator());
		response.setPublisherPreferences(publisherPreferences);

		PublisherService publisherService = mock(PublisherService.class);
		IdentificationService identificationService = mock(IdentificationService.class);

		when(identificationService.getOrganizationIdentifier()).thenReturn("test");
		when(publisherService.getPublisherList(ssp)).thenReturn(publishers);

		test = new PublisherPreferencesNeverRequest(publisherService, identificationService);
		((PublisherPreferencesNeverRequest) test).setResponse(response);
		test.requestPublisherPreferences(ssp);

		verify(publisherService, never()).replacePublisherPreferencesList(any(SupplySidePlatform.class), anyCollectionOf(PublisherPreference.class));
	}

	@Test
	public void requestPublisherPreferences_PublisherPreferencesAreEmpty() throws IOException {
		Collection<PublisherPreference> publisherPreferences = new LinkedList<PublisherPreference>();

		Collection<Publisher> publishers = new LinkedList<Publisher>();
		publishers.add(new Publisher("0", "0", null, null, null));
		SupplySidePlatform ssp = new SupplySidePlatform("test", "http://test.co/adv", "http://test.co/pub", "test".getBytes());

		Identification dsp = new Identification("test");
		PublisherPreferencesRequest request = new PublisherPreferencesRequest(dsp, publishers);
		request.sign("test".getBytes(), new PublisherPreferencesRequestTranslator());
		String requestToken = request.getIdentification().getToken();

		PublisherPreferencesResponse response = new PublisherPreferencesResponse(dsp, new Status(requestToken));
		response.sign("test".getBytes(), new PublisherPreferencesResponseTranslator());
		response.setPublisherPreferences(publisherPreferences);

		PublisherService publisherService = mock(PublisherService.class);
		IdentificationService identificationService = mock(IdentificationService.class);

		when(identificationService.getOrganizationIdentifier()).thenReturn("test");
		when(publisherService.getPublisherList(ssp)).thenReturn(publishers);

		test = new PublisherPreferencesNeverRequest(publisherService, identificationService);
		((PublisherPreferencesNeverRequest) test).setResponse(response);
		test.requestPublisherPreferences(ssp);

		verify(publisherService, never()).replacePublisherPreferencesList(any(SupplySidePlatform.class), anyCollectionOf(PublisherPreference.class));
	}
}
