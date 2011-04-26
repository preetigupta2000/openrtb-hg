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
import org.openrtb.common.model.PreferenceType;
import org.openrtb.common.model.Publisher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA. PublisherTranslatorTest
 *
 * @author jdrahos
 */
public class PublisherTranslatorTest {

	private static final List<PreferenceType> PREFERENCE_TYPES = Arrays.asList(PreferenceType.URL, PreferenceType.creativeAttributes);
	private static final Publisher PUBLISHER = new Publisher("test1", "test2", "tests.co", PREFERENCE_TYPES, 100L);

	private static final String PRETTY_VALUE = "{" + "  \"publisherID\" : \"" + PUBLISHER.getPublisherID() + "\",\n" + "  \"siteID\" : \"" + PUBLISHER.getSiteID() + "\",\n" + "  \"siteTLD\" : \"" + PUBLISHER.getSiteTLD() + "\",\n" + "  \"preferenceTypes\" : [\"" + PREFERENCE_TYPES.get(0) + "\", \"" + PREFERENCE_TYPES.get(1) + "\"]," + "  \"sinceThisTimestamp\" : " + PUBLISHER.getTimestamp() + "\n" + "}";

	private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

	private PublisherTranslator test = new PublisherTranslator();

	@Test
	public void serializeObject() throws IOException {
		assertEquals(EXPECTED_VALUE, test.toJSON(PUBLISHER));
	}

	@Test
	public void deserializeObject() throws IOException {
		validateObject(PUBLISHER, test.fromJSON(PRETTY_VALUE));
	}

	@Test
	public void serializeEmptyObject() throws IOException {
		assertEquals("{}", test.toJSON(new Publisher()));
	}

	@Test
	public void deserializeEmptyObject() throws IOException {
		validateObject(new Publisher(), test.fromJSON("{}"));
	}

	public static void validateObject(final Publisher expected, final Publisher actual) {
		assertEquals("unable to deserialize the publisher id", expected.getPublisherID(), actual.getPublisherID());
		assertEquals("unable to deserialize the site id", expected.getSiteID(), actual.getSiteID());
		assertEquals("unable to deserialize the site TLD", expected.getSiteTLD(), actual.getSiteTLD());
		assertEquals("unable to deserialize the timestamp", expected.getTimestamp(), actual.getTimestamp());

		if (actual.getPreferenceTypes() != null) {
			assertNotNull(expected.getPreferenceTypes());
		}

		if (expected.getPreferenceTypes() != null) {
			assertNotNull(actual.getPreferenceTypes());
			assertEquals("unable to deserialize preference types", actual.getPreferenceTypes().size(), expected.getPreferenceTypes().size());

			findExtraItems:
			for (Object actualVal : actual.getPreferenceTypes()) {
				for (PreferenceType expectedVal : expected.getPreferenceTypes()) {
					if (expectedVal.equals(actualVal)) {
						continue findExtraItems;
					}
				}

				fail("found extra preference type: " + actualVal);
			}

			findMissingItems:
			for (Object expectedVal : expected.getPreferenceTypes()) {
				for (PreferenceType actualVal : actual.getPreferenceTypes()) {
					if (expectedVal.equals(actualVal)) {
						continue findMissingItems;
					}
				}

				fail("found missing preference type: " + expectedVal);
			}
		}
	}

	private static class PublisherTranslator extends AbstractJsonTranslator<Publisher> {
		public PublisherTranslator() {
			super(PublisherTranslator.class);
		}
	}
}
