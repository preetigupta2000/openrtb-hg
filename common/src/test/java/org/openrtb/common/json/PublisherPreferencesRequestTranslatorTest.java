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
package org.openrtb.common.json;

import org.junit.Test;
import org.openrtb.common.model.Identification;
import org.openrtb.common.model.Publisher;
import org.openrtb.common.model.PublisherPreferencesRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by IntelliJ IDEA. PublisherPreferencesRequestTranslatorTest
 *
 * @author jdrahos
 */
public class PublisherPreferencesRequestTranslatorTest {
	private static final Identification IDENT;
	static {
		IDENT = new Identification("The_DSP", System.currentTimeMillis());
		IDENT.setToken("44ab444914088e855ad1f948ec4a1fc7");
	}

	private static final Publisher PUBLISHER1 = new Publisher("0","0",null,null,null);
	private static final Publisher PUBLISHER2 = new Publisher("test1","test2","tests.co", Arrays.asList(new String[]{"URL", "creativeAttribute"}),100L);

	private static final PublisherPreferencesRequest REQUEST = new PublisherPreferencesRequest(IDENT);
	static {
		REQUEST.addPublisher(PUBLISHER1);
		REQUEST.addPublisher(PUBLISHER2);
	}

	private static final String PRETTY_VALUE =
		"{" +
		"  \"identification\" : {" +
		"    \"organization\" : \""+IDENT.getOrganization()+"\",\n" +
		"    \"timestamp\" : "+IDENT.getTimestamp()+",\n" +
		"    \"token\" : \""+IDENT.getToken()+"\"\n" +
		"  },\n" +
		"  \"publishers\" : [{" +
		"    \"publisherID\" : \""+PUBLISHER1.getPublisherID()+"\",\n" +
		"    \"siteID\" : \""+PUBLISHER1.getSiteID()+"\"\n" +
		"  }, {" +
		"    \"publisherID\" : \""+PUBLISHER2.getPublisherID()+"\",\n" +
		"    \"siteID\" : \""+PUBLISHER2.getSiteID()+"\",\n" +
		"    \"siteTLD\" : \""+PUBLISHER2.getSiteTLD()+"\",\n" +
		"    \"preferenceTypes\" : [\""+ PUBLISHER2.getPreferenceTypes().get(0) +"\", \""+PUBLISHER2.getPreferenceTypes().get(1)+"\"]," +
		"    \"sinceThisTimestamp\" : "+PUBLISHER2.getTimestamp()+"\n" +
		"  }]" +
		"}";

	private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

	private PublisherPreferencesRequestTranslator test = new PublisherPreferencesRequestTranslator();

	@Test
	public void serializeObject() throws IOException {
		assertEquals(EXPECTED_VALUE, test.toJSON(REQUEST));
	}

	@Test
	public void deserializeObject() throws IOException {
		validateObject(REQUEST, test.fromJSON(PRETTY_VALUE));
	}

	@Test
	public void serializeEmptyObject() throws IOException {
		assertEquals("{}", test.toJSON(new TestRequest()));
	}

	@Test
	public void deserializeEmptyObject() throws IOException {
		validateObject(new TestRequest(), test.fromJSON("{}"));
	}

	private void validateObject(PublisherPreferencesRequest expectedValue, PublisherPreferencesRequest actualValue) {
		if (expectedValue.getIdentification() == null) {
			assertNull("actual identification value should be null",
					   actualValue.getIdentification());
		} else {
			IdentificationJsonTranslatorTest.validateObject(expectedValue.getIdentification(),
															actualValue.getIdentification());
		}

		Map<String, Publisher> expectedPublishers = convertListToMap(expectedValue.getPublishers());
		for(Publisher publisher : actualValue.getPublishers()) {
			Publisher expectedPublisher = expectedPublishers.get(publisher.getPublisherID());
			assertNotNull("unexpected publisher value in returned request", expectedPublisher);
			PublisherTranslatorTest.validateObject(expectedPublisher, publisher);
		}
	}

	private Map<String, Publisher> convertListToMap(Collection<Publisher> list) {
		Map<String, Publisher> retval = new HashMap<String, Publisher>();

		for(Publisher publisher : list) {
			retval.put(publisher.getPublisherID(), publisher);
		}
		
		return retval;
	}

	/**
	 * Class is used to get access to the default constructor in the request.
	 *
	 * It should not be possible otherwise to create a request without an
	 * identification object.
	 */
	private static class TestRequest extends PublisherPreferencesRequest { }
}
