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

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA. PublisherPreferencesRequestTest
 *
 * @author jdrahos
 */
public class PublisherPreferencesRequestTest {
	private PublisherPreferencesRequest test;
	private static final Collection<Publisher> PUBLISHERS;
	static {
		PUBLISHERS = new LinkedList<Publisher>();
		PUBLISHERS.add(new Publisher());
	}
	private static final Identification IDENTIFICATION = new Identification("test");

	@Before
	public void setup() {
		test = new PublisherPreferencesRequest(IdentificationTest.ORGANIZATION);
	}

	/*
	* public PublisherPreferencesRequest(Identification identification)
	*/
	@Test
	public void createRequest() {
		PublisherPreferencesRequest request = new PublisherPreferencesRequest(IDENTIFICATION);
		assertNotNull("Identification is required", request.getIdentification());
		assertNotNull("Publishers is required", request.getPublishers());
	}

	/*
	* public PublisherPreferencesRequest(Identification identification)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidRequest_IdentificationIsNull() {
		Identification identification = null;
		new PublisherPreferencesRequest(identification);
	}

	/*
	* public PublisherPreferencesRequest(String organization)
	*/
	@Test
	public void createRequest2() {
		PublisherPreferencesRequest request = new PublisherPreferencesRequest("test");
		assertNotNull("Identification is required", request.getIdentification());
		assertNotNull("Publishers is required", request.getPublishers());
	}

	/*
	* public PublisherPreferencesRequest(String organization)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidRequest2_OrganizationIsEmpty() {
		new PublisherPreferencesRequest("");
	}

	/*
	* public PublisherPreferencesRequest(String organization)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidRequest2_OrganizationIsNull() {
		String oraganization = null;
		new PublisherPreferencesRequest(oraganization);
	}

	/*
	* public PublisherPreferencesRequest(
	* 	Identification identification,
	* 	Collection<Publisher> PUBLISHERS)
	*/
	@Test
	public void createRequest3() {
		PublisherPreferencesRequest request = new PublisherPreferencesRequest(IDENTIFICATION, PUBLISHERS);
		assertNotNull("Identification is required", request.getIdentification());
		assertNotNull("Publishers is required", request.getPublishers());
	}

	/*
	* public PublisherPreferencesRequest(
	* 	Identification identification,
	* 	Collection<Publisher> PUBLISHERS)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidRequest3_IdentificationIsNull() {
		Identification identification = null;
		new PublisherPreferencesRequest(identification, PUBLISHERS);
	}

	/*
	* public PublisherPreferencesRequest(
	* 	Identification identification,
	* 	Collection<Publisher> PUBLISHERS)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidRequest3_PublishersIsNull() {
		Collection<Publisher> publishers = null;
		new PublisherPreferencesRequest(IDENTIFICATION, publishers);
	}

	/*
	* public PublisherPreferencesRequest(
	* 	Identification identification,
	* 	Collection<Publisher> PUBLISHERS)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalidRequest3_PublishersSizeIs0() {
		Collection<Publisher> publishers = new LinkedList<Publisher>();
		new PublisherPreferencesRequest(IDENTIFICATION, publishers);
	}

	@Test
	public void setIdentification(){
		test.setIdentification(IDENTIFICATION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setIdentification_Null(){
		test.setIdentification(null);
	}


	@Test
	public void setPublishers(){
		test.setPublishers(PUBLISHERS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPublishers_Null(){
		test.setPublishers(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPublishers_SizeIs0(){
		test.setPublishers(new LinkedList<Publisher>());
	}

	@Test
	public void addPublisher(){
		test.addPublisher(new Publisher());
	}

	@Test(expected = IllegalArgumentException.class)
	public void addPublisher_Null(){
		test.addPublisher(null);
	}

	/**
	 * The identification object should default the timestamp to the current
	 * time when none is specified.
	 */
	@Test
	public void verifyTimestampOfRequest() {
		GregorianCalendar calendar = new GregorianCalendar();
		assertTrue("difference between default timestamp and now is greater than 1 seconds",
				   (1L*1000) > (calendar.getTime().getTime() - test.getTimestamp()));

	}
}
