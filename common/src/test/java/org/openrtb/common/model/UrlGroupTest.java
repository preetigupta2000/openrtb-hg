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

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA. UrlGroupTest
 *
 * @author jdrahos
 */
public class UrlGroupTest {
	private static final String GROUP_NAME = "Test Group";
	private static final List<String> LANDING_PAGES = new LinkedList<String>();

	static {
		LANDING_PAGES.add("test.com");
		LANDING_PAGES.add("test.us");
	}

	private UrlGroup test = new UrlGroup();

	@Test
	public void create() {
		UrlGroup urlGroup = new UrlGroup(GROUP_NAME, LANDING_PAGES);
		assertNotNull("Group name is required", urlGroup.getGroupName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_GroupNameIsNull() {
		String groupName = null;
		UrlGroup urlGroup = new UrlGroup(groupName, LANDING_PAGES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_GroupNameIsEmpty() {
		String groupName = "";
		UrlGroup urlGroup = new UrlGroup(groupName, LANDING_PAGES);
	}

	@Test
	public void setGroupName() {
		test.setGroupName(GROUP_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setGroupName_Null() {
		String groupName = null;
		test.setGroupName(groupName);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setGroupName_Empty() {
		String groupName = "";
		test.setGroupName(groupName);
	}

	@Test
	public void addLandingPage() {
		test.addLandingPage(LANDING_PAGES.get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addLandingPage_Null() {
		String landingPage = null;
		test.addLandingPage(landingPage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addLandingPage_Empty() {
		String landingPage = "";
		test.addLandingPage(landingPage);
	}
}
