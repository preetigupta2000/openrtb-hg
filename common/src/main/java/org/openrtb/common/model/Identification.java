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

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * <p/>
 * The shared secret may take any form (i.e. text string or key encryption) and is communicated between the parties
 * outside of this protocol.
 * <p/>
 * This object encapsulates the <code>identification</code> object found in the requests and responses between a DSP and
 * SSP.
 *
 * @since 1.0
 */
@JsonSerialize(include = Inclusion.NON_DEFAULT)
@JsonPropertyOrder({"organization", "timestamp"})
public class Identification {

	@JsonProperty
	private String organization;
	@JsonProperty
	private long timestamp;

	public Identification() {
	}

	/**
	 * Creates a minimal identification object. The associated {@link #timestamp} is assumed to be equal to {@link
	 * System#currentTimeMillis()}.
	 *
	 * @param organization used to identify the ownership of the organization sending the request or response..
	 */
	public Identification(String organization) {
		this(organization, System.currentTimeMillis());
	}

	public Identification(String organization, long timestamp) {
		setOrganization(organization);
		setTimestamp(timestamp);
	}

	/**
	 * The identifier used by the receiver to identify the requesting organization.
	 */
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		if (organization == null || "".equals(organization.trim())) {
			throw new IllegalArgumentException("Identifier passed to Identification#setOrganization() must be non-null and not all whitespace");
		}

		this.organization = organization;
	}

	/**
	 * The number of milliseconds since EPOC this request was made.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Identification that = (Identification) o;

		if (timestamp != that.timestamp) {
			return false;
		}
		if (organization != null ? !organization.equals(that.organization) : that.organization != null) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = organization != null ? organization.hashCode() : 0;
		result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Identification");
		sb.append("{organization='").append(organization).append('\'');
		sb.append(", timestamp=").append(timestamp);
		sb.append('}');
		return sb.toString();
	}
}
