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
import org.openrtb.common.model.Operator;
import org.openrtb.common.model.PreferenceType;
import org.openrtb.common.model.PublisherPreference;
import org.openrtb.common.model.PublisherPreferencesResponse;
import org.openrtb.common.model.Rule;
import org.openrtb.common.model.Status;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by IntelliJ IDEA. PublisherPreferencesResponseTranslatorTest
 *
 * @author jdrahos
 */
public class PublisherPreferencesResponseTranslatorTest {

	private static final Identification IDENT;

	static {
		IDENT = new Identification("The_DSP", System.currentTimeMillis());
		IDENT.setToken("44ab444914088e855ad1f948ec4a1fc7");
	}

	private static final Status STATUS = new Status("44ab444914088e855ad1f948ec4a1fc7", 0, "success");

	private static final List<Object> VALUES1 = new LinkedList<Object>();

	static {
		VALUES1.add("test2.co");
		VALUES1.add("test1.co");
	}

	private static final Rule RULE1 = new Rule(Operator.include, PreferenceType.URL, VALUES1);

	private static final List<Rule> RULES1 = new LinkedList<Rule>();

	static {
		RULES1.add(RULE1);
	}

	private static final PublisherPreference PUBLISHER_PREFERENCE1 = new PublisherPreference("test1", "test2", "tests.co", RULES1);


	private static final List<Object> VALUES2 = new LinkedList<Object>();

	static {
		VALUES2.add(5);
		VALUES2.add(10);
	}

	private static final Rule RULE2 = new Rule(Operator.include, PreferenceType.creativeCategories, VALUES2);

	private static final List<Rule> RULES2 = new LinkedList<Rule>();

	static {
		RULES2.add(RULE2);
	}

	private static final PublisherPreference PUBLISHER_PREFERENCE2 = new PublisherPreference("test3", "test4", "tests2.co", RULES2);

	private static final PublisherPreferencesResponse RESPONSE = new PublisherPreferencesResponse(IDENT);

	static {
		RESPONSE.addPublisherPreference(PUBLISHER_PREFERENCE1);
		RESPONSE.addPublisherPreference(PUBLISHER_PREFERENCE2);
		RESPONSE.setStatus(STATUS);
	}

	private static final String PRETTY_VALUE = "{" + "  \"identification\" : {" + "    \"organization\" : \"" + IDENT.getOrganization() + "\",\n" + "    \"timestamp\" : " + IDENT.getTimestamp() + ",\n" + "    \"token\" : \"" + IDENT.getToken() + "\"\n" + "  },\n" + "  \"status\" : {\n" + "    \"requestToken\" : \"" + STATUS.getRequestToken() + "\",\n" + "    \"statusCode\" : " + STATUS.getCode() + ",\n" + "    \"statusMessage\" : \"" + STATUS.getMessage() + "\"\n" + "  },\n" + "  \"publisherPreferences\" : [{\n" + "    \"publisherID\" : \"" + PUBLISHER_PREFERENCE1.getPublisherID() + "\",\n" + "    \"siteID\" : \"" + PUBLISHER_PREFERENCE1.getSiteID() + "\",\n" + "    \"siteTLD\" : \"" + PUBLISHER_PREFERENCE1.getSiteTLD() + "\",\n" + "    \"rules\": [\n" + "      {\n" + "        \"operator\": \"" + RULE1.getOperator() + "\",\n" + "        \"type\": \"" + RULE1.getType() + "\",\n" + "        \"values\": [\"" + VALUES1.get(0) + "\",\"" + VALUES1.get(1) + "\"]\n" + "      }\n" + "    ]\n" + "  },\n" + "  {\n" + "    \"publisherID\" : \"" + PUBLISHER_PREFERENCE2.getPublisherID() + "\",\n" + "    \"siteID\" : \"" + PUBLISHER_PREFERENCE2.getSiteID() + "\",\n" + "    \"siteTLD\" : \"" + PUBLISHER_PREFERENCE2.getSiteTLD() + "\",\n" + "    \"rules\": [\n" + "      {\n" + "        \"operator\": \"" + RULE2.getOperator() + "\",\n" + "        \"type\": \"" + RULE2.getType() + "\",\n" + "        \"values\": [" + VALUES2.get(0) + ", " + VALUES2.get(1) + "]\n" + "      }\n" + "    ]\n" + "  }]\n" + "}";

	private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

	private PublisherPreferencesResponseTranslator test = new PublisherPreferencesResponseTranslator();

	@Test
	public void serializeObject() throws IOException {
		assertEquals(EXPECTED_VALUE, test.toJSON(RESPONSE));
	}

	@Test
	public void deserializeObject() throws IOException {
		validateObject(RESPONSE, test.fromJSON(PRETTY_VALUE));
	}

	@Test
	public void serializeEmptyObject() throws IOException {
		assertEquals("{}", test.toJSON(new TestResponse()));
	}

	@Test
	public void deserializeEmptyObject() throws IOException {
		validateObject(new TestResponse(), test.fromJSON("{}"));
	}

	private void validateObject(PublisherPreferencesResponse expectedValue, PublisherPreferencesResponse actualValue) {
		if (expectedValue.getIdentification() == null) {
			assertNull("actual identification value should be null", actualValue.getIdentification());
		} else {
			IdentificationJsonTranslatorTest.validateObject(expectedValue.getIdentification(), actualValue.getIdentification());
		}

		if (expectedValue.getStatus() == null) {
			assertNull("actual status value should be null", actualValue.getIdentification());
		} else {
			StatusTranslatorTest.validateObject(expectedValue.getStatus(), actualValue.getStatus());
		}

		Map<String, PublisherPreference> expectedPublisherPreferences = convertListToMap(expectedValue.getPublisherPreferences());
		for (PublisherPreference publisherPreference : actualValue.getPublisherPreferences()) {
			PublisherPreference expectedPublisherPreference = expectedPublisherPreferences.get(publisherPreference.getPublisherID());
			assertNotNull("unexpected publisherPreference value in returned request", expectedPublisherPreference);
			PublisherPreferenceTranslatorTest.validateObject(expectedPublisherPreference, publisherPreference);
		}
	}

	private Map<String, PublisherPreference> convertListToMap(Collection<PublisherPreference> list) {
		Map<String, PublisherPreference> retval = new HashMap<String, PublisherPreference>();

		for (PublisherPreference publisherPreference : list) {
			retval.put(publisherPreference.getPublisherID(), publisherPreference);
		}

		return retval;
	}

	private static class TestResponse extends PublisherPreferencesResponse {
	}
}
