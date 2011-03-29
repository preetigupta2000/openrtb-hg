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
package org.openrtb.common.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA. UrlGroupsRequestTest
 *
 * @author jdrahos
 */
public class UrlGroupsRequestTest {

	private UrlGroupsRequest test;
	private static final Identification IDENTIFICATION = new Identification("test");
	private static final Long TIMESTAMP = 1L;

	@Before
	public void setup() {
		test = new UrlGroupsRequest(IdentificationTest.ORGANIZATION);
	}

	@Test
	public void create() {
		UrlGroupsRequest request = new UrlGroupsRequest(IDENTIFICATION);
		assertNotNull("Identification is required", request.getIdentification());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_IdentificationIsNull() {
		Identification identification = null;
		UrlGroupsRequest request = new UrlGroupsRequest(identification);
	}

	@Test
	public void create2() {
		UrlGroupsRequest request = new UrlGroupsRequest("test");
		assertNotNull("Identification is required", request.getIdentification());
	}

	@Test(expected = IllegalArgumentException.class)
	public void create2Invalid_OrganizationIsNull() {
		String organization = null;
		UrlGroupsRequest request = new UrlGroupsRequest(organization);
	}

	@Test(expected = IllegalArgumentException.class)
	public void create2Invalid_OrganizationIsEmpty() {
		String organization = "";
		UrlGroupsRequest request = new UrlGroupsRequest(organization);
	}

	@Test
	public void create3() {
		UrlGroupsRequest request = new UrlGroupsRequest(IDENTIFICATION, TIMESTAMP);
		assertNotNull("Identification is required", request.getIdentification());
	}

	@Test(expected = IllegalArgumentException.class)
	public void create3Invlaid_IdentificationIsNull() {
		Identification identification = null;
		UrlGroupsRequest request = new UrlGroupsRequest(identification, TIMESTAMP);
	}

	@Test
	public void setIdentification() {
		test.setIdentification(IDENTIFICATION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setIdentification_Null() {
		Identification identification = null;
		test.setIdentification(identification);
	}
}
