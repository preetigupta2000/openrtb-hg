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
import org.openrtb.common.json.PublisherPreferencesResponseTranslator;
import org.openrtb.common.model.Identification;
import org.openrtb.common.model.Operator;
import org.openrtb.common.model.PreferenceType;
import org.openrtb.common.model.Publisher;
import org.openrtb.common.model.PublisherPreference;
import org.openrtb.common.model.PublisherPreferencesRequest;
import org.openrtb.common.model.PublisherPreferencesResponse;
import org.openrtb.common.model.Rule;
import org.openrtb.common.model.Status;
import org.openrtb.ssp.service.PublisherSupplySideService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class PublisherSupplySideServerTest {

	class OpenRtbSspTestClient implements PublisherSupplySideService {

		@Override
		public Collection<PublisherPreference> getPublisherPreferences(Collection<Publisher> publishers) {
			List<PublisherPreference> publisherPreferences = new LinkedList<PublisherPreference>();
			List<Object> urls = new ArrayList<Object>(Arrays.asList("joe.com"));
			List<Rule> rules = Arrays.asList(new Rule(Operator.include, PreferenceType.URL, urls));
			Identification identification = new Identification();
			for (Publisher publisher : publishers) {
				String siteId = publisher.getSiteID() == null ? "0" : publisher.getSiteID();
				publisherPreferences.add(new PublisherPreference(publisher.getPublisherID(), siteId, publisher.getSiteTLD(), rules));
			}
			return publisherPreferences;
		}

		@Override
		public byte[] getSharedSecret(String dsp) {
			return "RTB".getBytes();
		}

		@Override
		public String getOrganization() {
			return "ORG";
		}

	}

	private static final String DSP = "The_DSP";
	private static final String REQUEST = "{" + "  \"identification\" : {" + "    \"organization\" : \"" + DSP + "\",\n" + "    \"timestamp\" : " + System.currentTimeMillis() + ",\n" + "	 \"token\" : \"1234567890\"\n" + "  },\n" + "  \"publishers\" : [{" + "    \"publisherID\" : \"0\",\n" + "    \"preferenceTypes\" : [\"URL\"]" + "  }]" + "}";

	private PublisherSupplySideService ssp;
	private PublisherSupplySideServer server;

	@Before
	public void setup() {
		ssp = new OpenRtbSspTestClient();
		server = new PublisherSupplySideServer(ssp);
	}

	@Test
	public void invalidMD5ChecksumRequest() throws IOException {
		String jsonRequest = REQUEST.replaceAll("[ \n]", "");

		String jsonResponse = server.process(jsonRequest);
		System.out.println(" IN:" + jsonRequest);
		System.out.println("OUT:" + jsonResponse);

		PublisherPreferencesResponseTranslator resTrans = new PublisherPreferencesResponseTranslator();
		PublisherPreferencesResponse response = resTrans.fromJSON(jsonResponse);
		assertTrue("expected AUTH error (invalid signature)", response.getStatus().getCode() == Status.AUTH_ERROR_CODE);
	}

	@Test
	public void validMD5ChecksumRequest() throws IOException {
		PublisherPreferencesRequestTranslator reqTrans = new PublisherPreferencesRequestTranslator();
		PublisherPreferencesResponseTranslator resTrans = new PublisherPreferencesResponseTranslator();
		String digest;

		//set the request checksum
		String jsonRequest = REQUEST.replaceAll("[ \n]", "");
		PublisherPreferencesRequest request = reqTrans.fromJSON(jsonRequest);
		request.sign(ssp.getSharedSecret(DSP), reqTrans);
		jsonRequest = reqTrans.toJSON(request);

		//request --> response
		String jsonResponse = server.process(jsonRequest);
		System.out.println(" IN:" + jsonRequest);
		System.out.println("OUT:" + jsonResponse);

		//validate success
		PublisherPreferencesResponse response = resTrans.fromJSON(jsonResponse);
		assertTrue("expected success status code", response.getStatus().getCode() == Status.SUCCESS_CODE);

		//verify the response checksum
		assertTrue("expected successful verification", response.verify(ssp.getSharedSecret(DSP), resTrans));
	}

	@Test
	public void malformedRequest() throws JsonMappingException, JsonParseException, IOException {
		String jsonRequest = "{xyz}";

		String jsonResponse = server.process(jsonRequest);
		System.out.println(" IN:" + jsonRequest);
		System.out.println("OUT:" + jsonResponse);

		PublisherPreferencesResponseTranslator resTrans = new PublisherPreferencesResponseTranslator();
		PublisherPreferencesResponse response = resTrans.fromJSON(jsonResponse);
		assertTrue("bad MD5 status code", response.getStatus().getCode() == Status.OTHER_ERROR_CODE);
	}

}
