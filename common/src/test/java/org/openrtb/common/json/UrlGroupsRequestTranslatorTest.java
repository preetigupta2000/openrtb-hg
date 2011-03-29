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
import org.openrtb.common.model.UrlGroupsRequest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA. UrlGroupsRequestTranslatorTest
 *
 * @author jdrahos
 */
public class UrlGroupsRequestTranslatorTest {
	private static final Identification IDENT;

	static {
		IDENT = new Identification("The_DSP", System.currentTimeMillis());
		IDENT.setToken("44ab444914088e855ad1f948ec4a1fc7");
	}


	private static final UrlGroupsRequest URL_GROUPS_REQUEST = new UrlGroupsRequest(IDENT, 0L);

	private static final String PRETTY_VALUE = "{" + "  \"identification\" : {" + "    \"organization\" : \"" + IDENT.getOrganization() + "\",\n" + "    \"timestamp\" : " + IDENT.getTimestamp() + ",\n" + "    \"token\" : \"" + IDENT.getToken() + "\"\n" + "  },\n" + "\"sinceThisTimestamp\": " + 0L + "    }";

	private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

	private UrlGroupsRequestTranslator test = new UrlGroupsRequestTranslator();

	@Test
	public void serializeObject() throws IOException {
		assertEquals(EXPECTED_VALUE, test.toJSON(URL_GROUPS_REQUEST));
	}

	@Test
	public void deserializeObject() throws IOException {
		validateObject(URL_GROUPS_REQUEST, test.fromJSON(PRETTY_VALUE));
	}

	@Test
	public void serializeEmptyObject() throws IOException {
		assertEquals("{}", test.toJSON(new TestObject()));
	}

	@Test
	public void deserializeEmptyObject() throws IOException {
		validateObject(new TestObject(), test.fromJSON("{}"));
	}

	public static void validateObject(UrlGroupsRequest expectedObject, UrlGroupsRequest actualObject) {
		assertEquals("unable to deserialize identifiation", expectedObject.getIdentification(), actualObject.getIdentification());
	}

	private static class TestObject extends UrlGroupsRequest {
	}
}
