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
import org.openrtb.common.model.UrlGroup;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.IdentificationService;
import org.openrtb.dsp.intf.service.UrlGroupService;

import java.util.Collection;
import java.util.LinkedList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by IntelliJ IDEA. UrlGroupsRequesterTest
 *
 * @author jdrahos
 */
public class UrlGroupsRequesterTest {
	private UrlGroupsRequester test;

	@Test
	public void getUrlGroupsFromAllSSPs_PlatformsAreNull() {
		Collection<SupplySidePlatform> platforms = null;

		IdentificationService identificationService = mock(IdentificationService.class);
		UrlGroupService urlGroupService = mock(UrlGroupService.class);

		when(identificationService.getServiceEndpoints()).thenReturn(platforms);

		test = new UrlGroupsRequester(null, identificationService);
		test.getUrlGroupsFromAllSSPs();

		verify(identificationService).getServiceEndpoints();
		verify(urlGroupService, never()).replaceUrlGroups(any(SupplySidePlatform.class), anyCollectionOf(UrlGroup.class));
	}

	@Test
	public void getUrlGroupsFromAllSSPs_PlatformsAreEmpty() {
		Collection<SupplySidePlatform> platforms = new LinkedList<SupplySidePlatform>();

		IdentificationService identificationService = mock(IdentificationService.class);
		UrlGroupService urlGroupService = mock(UrlGroupService.class);

		when(identificationService.getServiceEndpoints()).thenReturn(platforms);

		test = new UrlGroupsRequester(null, identificationService);
		test.getUrlGroupsFromAllSSPs();

		verify(identificationService).getServiceEndpoints();
		verify(urlGroupService, never()).replaceUrlGroups(any(SupplySidePlatform.class), anyCollectionOf(UrlGroup.class));
	}

	@Test
	public void getUrlGroups_PlatformIsNull() {
		SupplySidePlatform ssp = null;

		UrlGroupService urlGroupService = mock(UrlGroupService.class);
		IdentificationService identificationService = mock(IdentificationService.class);

		test = new UrlGroupsRequester(null, identificationService);
		test.getUrlGroups(ssp);

		verify(urlGroupService, never()).replaceUrlGroups(any(SupplySidePlatform.class), anyCollectionOf(UrlGroup.class));
	}

	@Test
	public void getUrlGroups_ResponseIsNull() {
		SupplySidePlatform ssp = new SupplySidePlatform("test", "http://test.co/adv", "http://test.co/pub", "http://test.co/urlGroup", "test".getBytes());

		UrlGroupService urlGroupService = mock(UrlGroupService.class);
		IdentificationService identificationService = mock(IdentificationService.class);

		when(identificationService.getOrganizationIdentifier()).thenReturn("test");
		when(urlGroupService.getLastSynchronizationTimestamp(ssp)).thenReturn(0L);

		test = new UrlGroupsNeverRequest(urlGroupService, identificationService);
		((UrlGroupsNeverRequest) test).setResponse(null);
		test.getUrlGroups(ssp);

		verify(urlGroupService, never()).replaceUrlGroups(any(SupplySidePlatform.class), anyCollectionOf(UrlGroup.class));
	}
}
