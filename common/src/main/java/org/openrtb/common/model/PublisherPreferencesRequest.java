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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. PublisherPreferencesRequest
 *
 * @author jdrahos
 * @since 1.2
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"identification", "publishers"})
public class PublisherPreferencesRequest extends Signable {

	@JsonProperty
	private Identification identification;
	@JsonProperty
	private List<Publisher> publishers;

	/**
	 * Needed for JSON serialization/deserialization.
	 */
	protected PublisherPreferencesRequest() {
		publishers = new LinkedList<Publisher>();
	}

	public PublisherPreferencesRequest(Identification identification) {
		this();
		setIdentification(identification);
	}

	/**
	 * Creates a minimal <code>PublisherPreferencesRequest</code>.
	 *
	 * @param organization The unique name of the organization making the request. This value represents the unique key
	 *                     between the SSP and the requestor for identifying who sent the request.
	 * @throws IllegalArgumentException this exception is thrown if <code>organization</code> is <code>null</code>.
	 */
	public PublisherPreferencesRequest(String organization) {
		this(new Identification(organization));
	}

	public PublisherPreferencesRequest(Identification identification, Collection<Publisher> publishers) {
		this(identification);
		setPublishers(publishers);
	}

	/**
	 * {@link Identification} of who is making this request.
	 * <p/>
	 * This attribute is required.
	 */
	@Override
	public Identification getIdentification() {
		return identification;
	}

	public void setIdentification(Identification identification) {
		validateIdentification(identification);
		this.identification = identification;
	}

	@JsonIgnore
	public long getTimestamp() {
		return identification.getTimestamp();
	}

	public List<Publisher> getPublishers() {
		return publishers;
	}

	public void setPublishers(Collection<Publisher> publishers) {
		if (publishers == null || publishers.size() < 1) {
			throw new IllegalArgumentException("At least one Publisher must be present for call to PublisherPreferencesRequest#setPublishers()");
		} else {
			this.publishers.clear();
			this.publishers.addAll(publishers);
		}
	}

	public void addPublisher(Publisher publisher) {
		if (publisher == null) {
			throw new IllegalArgumentException("Publisher passed to PublisherPreferencesRequest#addPublisher() must be non-null");
		}

		this.publishers.add(publisher);
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("PublisherPreferencesRequest");
		sb.append("{identification=").append(identification);
		sb.append(", publishers=").append(publishers);
		sb.append('}');
		return sb.toString();
	}
}
