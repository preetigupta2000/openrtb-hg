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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. UrlGroup
 *
 * @author jdrahos
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"groupName", "landingPageTLDs"})
public class UrlGroup {
	@JsonProperty
	private String groupName;
	@JsonProperty("landingPageTLDs")
	private List<String> landingPages;

	protected UrlGroup() {
		landingPages = new LinkedList<String>();
	}

	public UrlGroup(final String groupName, final List<String> landingPages) {
		this();
		setGroupName(groupName);
		setLandingPages(landingPages);
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(final String groupName) {
		if (groupName == null || groupName.isEmpty()) {
			throw new IllegalArgumentException("Group name passed to UrlGroup#setGroupName() must be non-null and at least one character long");
		}

		this.groupName = groupName;
	}

	@JsonIgnore
	public List<String> getLandingPages() {
		return landingPages;
	}

	public void setLandingPages(final List<String> landingPages) {
		this.landingPages.clear();

		if (landingPages != null) {
			this.landingPages.addAll(landingPages);
		}
	}

	public void addLandingPage(String landingPage) {
		if (landingPage == null || landingPage.isEmpty()) {
			throw new IllegalArgumentException("Landing page passed to UrlGroup#addLandingPage() must be non-null and at least one character long");
		}

		this.landingPages.add(landingPage);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		UrlGroup urlGroup = (UrlGroup) o;

		if (!groupName.equals(urlGroup.groupName)) {
			return false;
		}
		if (!landingPages.equals(urlGroup.landingPages)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = groupName.hashCode();
		result = 31 * result + landingPages.hashCode();
		return result;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("UrlGroup");
		sb.append("{groupName='").append(groupName).append('\'');
		sb.append(", landingPages=").append(landingPages);
		sb.append('}');
		return sb.toString();
	}
}
