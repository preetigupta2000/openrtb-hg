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

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA. PublisherPreferenceTest
 *
 * @author jdrahos
 */
public class PublisherPreferenceTest {
	private static String PUBLISHER_ID = "464";
	private static String SITE_ID = "645";
	private static Rule RULE = new Rule();
	private PublisherPreference test = new PublisherPreference(PUBLISHER_ID, SITE_ID);

	/*
	*  public PublisherPreference(String publisherID, String siteID)
	*/
	@Test
	public void create(){
		PublisherPreference preference = new PublisherPreference(PUBLISHER_ID,SITE_ID);
		assertNotNull("PublisherID is required", preference.getPublisherID());
		assertNotNull("SiteID is required", preference.getSiteID());
	}

	/*
	*  public PublisherPreference(String publisherID, String siteID)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_PublisherIdIsNull(){
		String publisherId = null;
		PublisherPreference preference = new PublisherPreference(publisherId, SITE_ID);
	}

	/*
	*  public PublisherPreference(String publisherID, String siteID)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_PublisherIdIsEmpty(){
		String publisherId = "";
		PublisherPreference preference = new PublisherPreference(publisherId, SITE_ID);
	}

	/*
	*  public PublisherPreference(String publisherID, String siteID)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_SiteIdIsNull(){
		String siteId = null;
		PublisherPreference preference = new PublisherPreference(PUBLISHER_ID, siteId);
	}
	/*
	*  public PublisherPreference(String publisherID, String siteID)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_SiteIdIsEmpty(){
		String siteId = "";
		PublisherPreference preference = new PublisherPreference(PUBLISHER_ID, siteId);
	}

	/*
	*  public PublisherPreference(String publisherID, String siteID, String siteTLD, List<Rule> rules)
	*/
	@Test
	public void create2(){
		PublisherPreference preference = new PublisherPreference(PUBLISHER_ID,SITE_ID, null, null);
		assertNotNull("PublisherID is required", preference.getPublisherID());
		assertNotNull("SiteID is required", preference.getSiteID());
	}

	/*
	*  public PublisherPreference(String publisherID, String siteID, String siteTLD, List<Rule> rules)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalid2_PublisherIdIsNull(){
		String publisherId = null;
		new PublisherPreference(publisherId,SITE_ID, null, null);
	}

	/*
	*  public PublisherPreference(String publisherID, String siteID, String siteTLD, List<Rule> rules)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalid2_PublisherIdIsEmpty(){
		String publisherId = "";
		new PublisherPreference(publisherId,SITE_ID, null, null);
	}

	/*
	*  public PublisherPreference(String publisherID, String siteID, String siteTLD, List<Rule> rules)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalid2_SiteIdIsNull(){
		String siteId = null;
		new PublisherPreference(PUBLISHER_ID,siteId, null, null);
	}

	/*
	*  public PublisherPreference(String publisherID, String siteID, String siteTLD, List<Rule> rules)
	*/
	@Test(expected = IllegalArgumentException.class)
	public void createInvalid2_SiteIdIsEmpty(){
		String siteId = "";
		new PublisherPreference(PUBLISHER_ID,siteId, null, null);
	}

	@Test
	public void setPublisherID(){
		test.setPublisherID(PUBLISHER_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPublisherID_Null(){
		String publisherId = null;
		test.setPublisherID(publisherId);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPublisherID_Empty(){
		String publisherId = "";
		test.setPublisherID(publisherId);
	}

	@Test
	public void setSiteID(){
		test.setSiteID(SITE_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setSiteID_null(){
		String siteId = null;
		test.setSiteID(siteId);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setSiteID_Empty(){
		String siteId = "";
		test.setSiteID(siteId);
	}

	@Test
	public void addRule(){
		test.addRule(RULE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addRule_Null(){
		Rule rule = null;
		test.addRule(rule);
	}
}
