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
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.AdvertiserBlocklistRequest;
import org.openrtb.common.model.Identification;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Verifies the {@link AdvertiserBlocklistRequest}'s translation to/from JSON.
 */
public class AdvertiserBlocklistRequestTranslatorTest {

	private static final Identification IDENT;

	static {
		IDENT = new Identification("The_DSP", System.currentTimeMillis());
	}

	private static final Advertiser ADVERTISER1 = new Advertiser("CoolComputer.com");
	private static final Advertiser ADVERTISER2 = new Advertiser("http://www.MyCarCompany.com", "My_Car_Company");

	private static final AdvertiserBlocklistRequest REQUEST = new AdvertiserBlocklistRequest(IDENT);

	static {
		REQUEST.addAdvertiser(ADVERTISER1);
		REQUEST.addAdvertiser(ADVERTISER2);
	}

	private static final String PRETTY_VALUE = "{" + "  \"identification\" : {" + "    \"organization\" : \"" + IDENT.getOrganization() + "\",\n" + "    \"timestamp\" : " + IDENT.getTimestamp() + "\n" + "  },\n" + "  \"advertisers\" : [{" + "    \"landingPageTLD\" : \"" + ADVERTISER1.getLandingPage() + "\"\n" + "  }, {" + "    \"landingPageTLD\" : \"" + ADVERTISER2.getLandingPage() + "" + "\",\n" + "    \"name\" : \"" + ADVERTISER2.getName() + "\"" + "  }]" + "}";

	private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

	private AdvertiserBlocklistRequestTranslator test = new AdvertiserBlocklistRequestTranslator();

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

	private void validateObject(AdvertiserBlocklistRequest expectedValue, AdvertiserBlocklistRequest actualValue) {
		if (expectedValue.getIdentification() == null) {
			assertNull("actual identification value should be null", actualValue.getIdentification());
		} else {
			IdentificationJsonTranslatorTest.validateObject(expectedValue.getIdentification(), actualValue.getIdentification());
		}

		Map<String, Advertiser> expectedAdvertisers = convertListToMap(expectedValue.getAdvertisers());
		for (Advertiser advertiser : actualValue.getAdvertisers()) {
			Advertiser expectedAdvertiser = expectedAdvertisers.get(advertiser.getLandingPage());
			assertNotNull("unexpected advertiser value in returned request", expectedAdvertiser);
			AdvertiserTranslatorTest.validateObject(expectedAdvertiser, advertiser);
		}
	}

	private Map<String, Advertiser> convertListToMap(Collection<Advertiser> list) {
		Map<String, Advertiser> retval = new HashMap<String, Advertiser>();

		for (Advertiser advertiser : list) {
			retval.put(advertiser.getLandingPage(), advertiser);
		}
		return retval;
	}

	/**
	 * Class is used to get access to the default constructor in the request.
	 * <p/>
	 * It should not be possible otherwise to create a request without an identification object.
	 */
	private static class TestRequest extends AdvertiserBlocklistRequest {
	}
}
