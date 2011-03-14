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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. PublisherPreference
 *
 * @author jdrahos
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"publisherID", "siteID", "siteTLD", "rules"})
public class PublisherPreference {
	@JsonProperty
	String publisherID;
	@JsonProperty
	String siteID;
	@JsonProperty
	String siteTLD;
	@JsonProperty
	List<Rule> rules;

	protected PublisherPreference() {
		rules = new LinkedList<Rule>();
	}

	public PublisherPreference(String publisherID, String siteID) {
		this(publisherID, siteID, null, null);
	}

	public PublisherPreference(String publisherID, String siteID, String siteTLD, List<Rule> rules) {
		this();
		setPublisherID(publisherID);
		setSiteID(siteID);
		setSiteTLD(siteTLD);
		setRules(rules);
	}

	public String getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(String publisherID) {
		if (publisherID == null || publisherID.length() == 0) {
			throw new IllegalArgumentException("publisherID passed to PublisherPreference#setPublisherID() must be non-null and at least one character long");
		}

		this.publisherID = publisherID;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		if (siteID == null || siteID.length() == 0) {
			throw new IllegalArgumentException("siteID passed to PublisherPreference#setSiteID() must be non-null and at least one character long");
		}

		this.siteID = siteID;
	}

	public String getSiteTLD() {
		return siteTLD;
	}

	public void setSiteTLD(String siteTLD) {
		this.siteTLD = siteTLD;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(Collection<Rule> rules) {
		this.rules.clear();
		if (rules != null) {
			this.rules.addAll(rules);
		}
	}

	public void addRule(Rule rule) {
		if (rule == null) {
			throw new IllegalArgumentException("Rule passed to PublisherPreference#addRule() must be non-null");
		}

		rules.add(rule);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		PublisherPreference that = (PublisherPreference) o;

		if (publisherID != null ? !publisherID.equals(that.publisherID) : that.publisherID != null) {
			return false;
		}
		if (rules != null ? !rules.equals(that.rules) : that.rules != null) {
			return false;
		}
		if (siteID != null ? !siteID.equals(that.siteID) : that.siteID != null) {
			return false;
		}
		if (siteTLD != null ? !siteTLD.equals(that.siteTLD) : that.siteTLD != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = publisherID != null ? publisherID.hashCode() : 0;
		result = 31 * result + (siteID != null ? siteID.hashCode() : 0);
		result = 31 * result + (siteTLD != null ? siteTLD.hashCode() : 0);
		result = 31 * result + (rules != null ? rules.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("PublisherPreference");
		sb.append("{publisherID='").append(publisherID).append('\'');
		sb.append(", siteID='").append(siteID).append('\'');
		sb.append(", siteTLD='").append(siteTLD).append('\'');
		sb.append(", rules=").append(rules);
		sb.append('}');
		return sb.toString();
	}
}
