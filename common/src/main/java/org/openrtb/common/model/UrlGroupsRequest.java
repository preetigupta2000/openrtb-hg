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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. UrlGroupsRequest
 *
 * @author jdrahos
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"identification", "urlGroups"})
public class UrlGroupsRequest extends Signable {
	@JsonProperty
	private Identification identification;
	@JsonProperty
	private List<UrlGroup> urlGroups;

	protected UrlGroupsRequest() {
		urlGroups = new LinkedList<UrlGroup>();
	}

	public UrlGroupsRequest(final Identification identification) {
		this();
		setIdentification(identification);
	}

	public UrlGroupsRequest(String organization) {
		this(new Identification(organization));
	}

	public UrlGroupsRequest(final Identification identification, final List<UrlGroup> urlGroups) {
		this(identification);
		setUrlGroups(urlGroups);
	}

	@Override
	public Identification getIdentification() {
		return identification;
	}

	public void setIdentification(Identification identification) {
		validateIdentification(identification);
		this.identification = identification;
	}

	public List<UrlGroup> getUrlGroups() {
		return urlGroups;
	}

	public void setUrlGroups(final List<UrlGroup> urlGroups) {
		if (urlGroups == null || urlGroups.size() < 1) {
			throw new IllegalArgumentException("At least one url group must be present for call to UrlGroupsRequest#setUrlGroups()");
		} else {
			this.urlGroups.clear();
			this.urlGroups.addAll(urlGroups);
		}
	}

	public void addUrlGroup(UrlGroup urlGroup) {
		if (urlGroup == null) {
			throw new IllegalArgumentException("Url group passed to UrlGroupsRequest#addUrlGroup() must be non-null");
		}

		this.urlGroups.add(urlGroup);
	}
}
