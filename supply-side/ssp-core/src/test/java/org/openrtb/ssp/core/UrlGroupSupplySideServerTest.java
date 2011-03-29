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
package org.openrtb.ssp.core;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.json.PublisherPreferencesRequestTranslator;
import org.openrtb.common.json.UrlGroupsResponseTranslator;
import org.openrtb.common.model.PublisherPreferencesRequest;
import org.openrtb.common.model.Status;
import org.openrtb.common.model.UrlGroup;
import org.openrtb.common.model.UrlGroupsResponse;
import org.openrtb.ssp.service.UrlGroupSupplySideService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA. UrlGroupSupplySideServerTest
 *
 * @author jdrahos
 */
public class UrlGroupSupplySideServerTest {
	class TestService implements UrlGroupSupplySideService {

		@Override
		public byte[] getSharedSecret(String dsp) {
			return "RTB".getBytes();
		}

		@Override
		public String getOrganization() {
			return "ORG";
		}

		@Override
		public Collection<UrlGroup> getUrlGroups(final Long timestamp) {
			Collection<UrlGroup> urlGroups = new LinkedList<UrlGroup>();
			urlGroups.add(new UrlGroup("test_group1", Arrays.asList("test.com", "test.ca")));
			urlGroups.add(new UrlGroup("test_group2", Arrays.asList("test2.com", "test2.ca")));

			return urlGroups;
		}
	}

	private static final String DSP = "The_DSP";
	private static final String REQUEST = "{" + "  \"identification\" : {" + "    \"organization\" : \"" + DSP + "\",\n" + "    \"timestamp\" : " + System.currentTimeMillis() + ",\n" + "    \"token\" : \"1234567890\"\n" + "  },\n" + "\"sinceThisTimestamp\": " + 0L + "    }";

	private UrlGroupSupplySideService service;
	private UrlGroupSupplySideServer server;

	@Before
	public void setup() {
		service = new TestService();
		server = new UrlGroupSupplySideServer(service);
	}

	@Test
	public void invalidMD5ChecksumRequest() throws IOException {
		String jsonRequest = REQUEST.replaceAll("[ \n]", "");

		String jsonResponse = server.process(jsonRequest);
		System.out.println(" IN:" + jsonRequest);
		System.out.println("OUT:" + jsonResponse);

		UrlGroupsResponseTranslator resTrans = new UrlGroupsResponseTranslator();
		UrlGroupsResponse response = resTrans.fromJSON(jsonResponse);
		assertTrue("expected AUTH error (invalid signature)", response.getStatus().getCode() == Status.AUTH_ERROR_CODE);
	}

	@Test
	public void validMD5ChecksumRequest() throws IOException {
		PublisherPreferencesRequestTranslator reqTrans = new PublisherPreferencesRequestTranslator();
		UrlGroupsResponseTranslator resTrans = new UrlGroupsResponseTranslator();
		String digest;

		//set the request checksum
		String jsonRequest = REQUEST.replaceAll("[ \n]", "");
		PublisherPreferencesRequest request = reqTrans.fromJSON(jsonRequest);
		request.sign(service.getSharedSecret(DSP), reqTrans);
		jsonRequest = reqTrans.toJSON(request);

		//request --> response
		String jsonResponse = server.process(jsonRequest);
		System.out.println(" IN:" + jsonRequest);
		System.out.println("OUT:" + jsonResponse);

		//validate success
		UrlGroupsResponse response = resTrans.fromJSON(jsonResponse);
		assertTrue("expected success status code", response.getStatus().getCode() == Status.SUCCESS_CODE);

		//verify the response checksum
		assertTrue("expected successful verification", response.verify(service.getSharedSecret(DSP), resTrans));
	}

	@Test
	public void malformedRequest() throws JsonMappingException, JsonParseException, IOException {
		String jsonRequest = "{xyz}";

		String jsonResponse = server.process(jsonRequest);
		System.out.println(" IN:" + jsonRequest);
		System.out.println("OUT:" + jsonResponse);

		UrlGroupsResponseTranslator resTrans = new UrlGroupsResponseTranslator();
		UrlGroupsResponse response = resTrans.fromJSON(jsonResponse);
		assertTrue("bad MD5 status code", response.getStatus().getCode() == Status.OTHER_ERROR_CODE);
	}
}
