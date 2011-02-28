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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. Publisher
 *
 * @author jdrahos
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_DEFAULT)
@JsonPropertyOrder({"publisherID", "siteID", "siteTLD", "preferenceTypes","sinceThisTimestamp"})
public class Publisher {
	@JsonProperty
	String publisherID;
	@JsonProperty
	String siteID;
	@JsonProperty
	String siteTLD;
	@JsonProperty
	List<String> preferenceTypes;
	@JsonProperty("sinceThisTimestamp")
	Long timestamp;

	public Publisher(){
		this(null,null,null,null,null);
	}
	
	public Publisher(String publisherID,String siteID, String siteTLD, List<String> preferenceTypes, Long timestamp){
		setPublisherID(publisherID);
		setSiteID(siteID);
		setSiteTLD(siteTLD);
		setPreferenceTypes(preferenceTypes);
		setTimestamp(timestamp);
	}

	public String getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(final String publisherID) {
		this.publisherID = publisherID;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(final String siteID) {
		this.siteID = siteID;
	}

	public String getSiteTLD() {
		return siteTLD;
	}

	public void setSiteTLD(final String siteTLD) {
		this.siteTLD = siteTLD;
	}

	public List<String> getPreferenceTypes() {
		return preferenceTypes;
	}

	public void setPreferenceTypes(final Collection<String> preferenceTypes) {
		if(preferenceTypes == null)
			this.preferenceTypes = null;
		else
			this.preferenceTypes = new LinkedList<String>(preferenceTypes);
	}

	@JsonIgnore
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Long timestamp) {
		this.timestamp = timestamp;
	}
}
