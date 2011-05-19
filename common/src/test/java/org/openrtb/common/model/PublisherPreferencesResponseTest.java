/*
 * Copyright (c) 2010, The OpenRTB Project All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the OpenRTB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
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

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA. PublisherPreferencesResponseTest
 *
 * @author jdrahos
 */
public class PublisherPreferencesResponseTest {

	private static final PublisherPreference PUBLISHER_PREFERENCE = new PublisherPreference();
	private static final Collection<PublisherPreference> PUBLISHER_PREFERENCES;

	static {
		PUBLISHER_PREFERENCES = new LinkedList<PublisherPreference>();
		PUBLISHER_PREFERENCES.add(PUBLISHER_PREFERENCE);
	}

	private static final Identification IDENTIFICATION = new Identification("test");
	private static final Status STATUS = new Status(0, "success");
	private PublisherPreferencesResponse test = new PublisherPreferencesResponse(IDENTIFICATION);

	/*
	* 	public PublisherPreferencesResponse(Identification identification)
	*/
	@Test
	public void createResponse() {
		PublisherPreferencesResponse response = new PublisherPreferencesResponse(IDENTIFICATION);
		assertNotNull("Identification is required", response.getIdentification());
		assertNotNull("Status is required", response.getStatus());
		assertNotNull("PublisherPreferences is required", response.getPublisherPreferences());
	}

	/*
	* 	public PublisherPreferencesResponse(Identification identification)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidResponse_IdentificationIsNull() {
		Identification identification = null;
		new PublisherPreferencesResponse(identification);
	}

	/*
	*   public PublisherPreferencesResponse(Identification identification, Status status)
	*/
	@Test
	public void createResponse2() {
		PublisherPreferencesResponse response = new PublisherPreferencesResponse(IDENTIFICATION, STATUS);
		assertNotNull("Identification is required", response.getIdentification());
		assertNotNull("Status is required", response.getStatus());
		assertNotNull("PublisherPreferences is required", response.getPublisherPreferences());
	}

	/*
	*   public PublisherPreferencesResponse(Identification identification, Status status)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidResponse2_IdentificationIsNull() {
		Identification identification = null;
		new PublisherPreferencesResponse(identification, STATUS);
	}

	/*
	*   public PublisherPreferencesResponse(Identification identification, Status status)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidResponse2_StatusIsNull() {
		Status status = null;
		new PublisherPreferencesResponse(IDENTIFICATION, status);
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

	@Test
	public void addPublisherPreference() {
		test.addPublisherPreference(PUBLISHER_PREFERENCE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addPublisherPreference_Null() {
		PublisherPreference publisherPreference = null;
		test.addPublisherPreference(publisherPreference);
	}
}
