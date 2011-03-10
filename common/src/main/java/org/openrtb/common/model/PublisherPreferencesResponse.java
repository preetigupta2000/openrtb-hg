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
 * Created by IntelliJ IDEA. PublisherPreferencesResponse
 *
 * @author jdrahos
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"identification", "status", "publisherPreferences"})
public class PublisherPreferencesResponse extends Signable {
	@JsonProperty
	private Identification identification;
	@JsonProperty
	private Status status;
	@JsonProperty
	private List<PublisherPreference> publisherPreferences;

	/**
	 * Needed for JSON serialization/deserialization.
	 */
	protected PublisherPreferencesResponse() {
		publisherPreferences = new LinkedList<PublisherPreference>();
	}

	public PublisherPreferencesResponse(Identification identification) {
		this(identification, new Status());
	}

	public PublisherPreferencesResponse(Identification identification, Status status) {
		this();
		setIdentification(identification);
		setStatus(status);
	}

	@Override
	public Identification getIdentification() {
		return identification;
	}

	public void setIdentification(Identification identification) {
		this.validateIdentification(identification);
		this.identification = identification;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		if (status == null) {
			throw new IllegalArgumentException("status passed to PublisherPreferencesResponse#setStatus() must be non-null");
		}

		this.status = status;
	}

	public List<PublisherPreference> getPublisherPreferences() {
		return publisherPreferences;
	}

	public void setPublisherPreferences(Collection<PublisherPreference> publisherPreferences) {
		this.publisherPreferences.clear();
		if (publisherPreferences != null) {
			this.publisherPreferences.addAll(publisherPreferences);
		}
	}

	public void addPublisherPreference(PublisherPreference publisherPreference) {
		if (publisherPreference == null) {
			throw new IllegalArgumentException("publisherPreference passed to PublisherPreferencesResponse#addPublisherPreference() must be non-null");
		}

		publisherPreferences.add(publisherPreference);
	}
}
