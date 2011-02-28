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
import org.openrtb.common.model.PublisherPreference;
import org.openrtb.common.model.Rule;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA. PublisherPreferenceTranslatorTest
 *
 * @author jdrahos
 */
public class PublisherPreferenceTranslatorTest {
	private static final List<Object> VALUES1 = new LinkedList<Object>();
	static {
		VALUES1.add("test1.co");
		VALUES1.add("test2.co");
	}
	private static final Rule RULE1 = new Rule("include","URL", VALUES1);

	private static final List<Object> VALUES2 = new LinkedList<Object>();
	static {
		VALUES2.add(5);
		VALUES2.add(10);
	}
	private static final Rule RULE2 = new Rule("include","creativeCategories", VALUES2);

	private static final List<Rule> RULES = new LinkedList<Rule>();
	static {
		RULES.add(RULE1);
		RULES.add(RULE2);
	}

	private static final PublisherPreference PUBLISHER_PREFERENCE = new PublisherPreference("test1","test2","tests.co", RULES);

	private static final String PRETTY_VALUE ="{\n" +
		"  \"publisherID\" : \""+PUBLISHER_PREFERENCE.getPublisherID()+"\",\n" +
		"  \"siteID\" : \""+PUBLISHER_PREFERENCE.getSiteID()+"\",\n" +
		"  \"siteTLD\" : \""+PUBLISHER_PREFERENCE.getSiteTLD()+"\",\n" +
		"  \"rules\": [\n" +
		"    {\n" +
		"      \"operator\": \""+ RULE1.getOperator() +"\",\n" +
		"      \"type\": \""+ RULE1.getType() +"\",\n" +
		"      \"values\": [\""+VALUES1.get(0) +"\",\""+VALUES1.get(1) +"\"]\n"+
		"    },\n"+
		"    {\n" +
		"      \"operator\": \""+ RULE2.getOperator() +"\",\n" +
		"      \"type\": \""+ RULE2.getType() +"\",\n" +
		"      \"values\": ["+VALUES2.get(0) +", "+VALUES2.get(1) +"]\n"+
		"    }\n"+
		"  ]\n" +
		"}\n";

	private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");
	private PublisherPreferenceTranslator test = new PublisherPreferenceTranslator();

	@Test
	public void serializeObject() throws IOException {
		assertEquals(EXPECTED_VALUE, test.toJSON(PUBLISHER_PREFERENCE));
	}

	@Test
	public void deserializeObject() throws IOException {
		validateObject(PUBLISHER_PREFERENCE, test.fromJSON(PRETTY_VALUE));
	}

	@Test
	public void serializeEmptyObject() throws IOException {
		assertEquals("{}", test.toJSON(new TestClass()));
	}

	@Test
	public void deserializeEmptyObject() throws IOException {
		validateObject(new TestClass(), test.fromJSON("{}"));
	}

	public static void validateObject(final PublisherPreference expected, final PublisherPreference actual) {
		assertEquals("unable to deserialize the publisher id",
					 expected.getPublisherID(), actual.getPublisherID());
		assertEquals("unable to deserialize the site id",
					 expected.getSiteID(), actual.getSiteID());
		assertEquals("unable to deserialize the site TLD",
					 expected.getSiteTLD(), actual.getSiteTLD());

		if(actual.getRules() != null){
			assertNotNull(expected.getRules());
		}

		if(expected.getRules() != null){
			assertNotNull(actual.getRules());
			assertEquals("unable to deserialize preference types", actual.getRules().size(),expected.getRules().size());

			Map<String, Rule> expectedRules = convertListToMap(expected.getRules());
			for(Rule actualRule: actual.getRules()){
				Rule expectedRule = expectedRules.get(getKey(actualRule));
				assertNotNull("unexpected rule value in returned request", expectedRule);
				RuleTranslatorTest.validateObject(expectedRule, actualRule);
			}
		}
	}

	private static Map<String, Rule> convertListToMap(Collection<Rule> list) {
		Map<String, Rule> retval = new HashMap<String, Rule>();

		for(Rule rule : list) {
			retval.put(getKey(rule), rule);
		}

		return retval;
	}

	private static String getKey(Rule rule){
		return rule.getOperator()+"_"+rule.getType();
	}


	private static class PublisherPreferenceTranslator extends AbstractJsonTranslator<PublisherPreference> {
		public PublisherPreferenceTranslator() {
			super(PublisherPreferenceTranslator.class);
		}
	}

	private static class TestClass extends PublisherPreference{}
}
