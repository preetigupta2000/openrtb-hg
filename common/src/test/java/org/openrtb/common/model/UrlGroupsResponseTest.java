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

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA. UrlGroupsResponseTest
 *
 * @author jdrahos
 */
public class UrlGroupsResponseTest {
	private static final Identification IDENTIFICATION = new Identification("test");
	private static final Status STATUS = new Status("44ab444914088e855ad1f948ec4a1fc7", 0, "success");
	private UrlGroupsResponse test = new UrlGroupsResponse(IDENTIFICATION);

	@Test
	public void create() {
		UrlGroupsResponse response = new UrlGroupsResponse(IDENTIFICATION);
		assertNotNull("Identification is required", response.getIdentification());
		assertNotNull("Status is required", response.getStatus());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_IdentificationIsNull() {
		Identification identification = null;
		UrlGroupsResponse response = new UrlGroupsResponse(identification);
	}

	@Test
	public void create2() {
		UrlGroupsResponse response = new UrlGroupsResponse(IDENTIFICATION, STATUS);
		assertNotNull("Identification is required", response.getIdentification());
		assertNotNull("Status is required", response.getStatus());
	}

	@Test(expected = IllegalArgumentException.class)
	public void create2Invalid_IdentificationIsNull() {
		Identification identification = null;
		UrlGroupsResponse response = new UrlGroupsResponse(identification, STATUS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void create2Invalid_StatusIsNull() {
		Status status = null;
		UrlGroupsResponse response = new UrlGroupsResponse(IDENTIFICATION, status);
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

	@Test
	public void setStatus() {
		test.setStatus(STATUS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setStatus_Null() {
		Status status = null;
		test.setStatus(status);
	}
}
