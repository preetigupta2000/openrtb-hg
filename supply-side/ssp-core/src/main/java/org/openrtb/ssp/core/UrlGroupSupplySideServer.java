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
package org.openrtb.ssp.core;

import org.openrtb.common.json.AbstractJsonTranslator;
import org.openrtb.common.json.UrlGroupsRequestTranslator;
import org.openrtb.common.json.UrlGroupsResponseTranslator;
import org.openrtb.common.model.Status;
import org.openrtb.common.model.UrlGroup;
import org.openrtb.common.model.UrlGroupsRequest;
import org.openrtb.common.model.UrlGroupsResponse;
import org.openrtb.ssp.UrlGroupSupplySideService;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA. UrlGroupSupplySideServerTest
 *
 * @author jdrahos
 */
public class UrlGroupSupplySideServer extends SupplySideServer<UrlGroupSupplySideService, UrlGroupsRequest, UrlGroupsResponse> {
	private static UrlGroupsRequestTranslator requestTranslator = new UrlGroupsRequestTranslator();
	private static UrlGroupsResponseTranslator responseTranslator = new UrlGroupsResponseTranslator();

	public UrlGroupSupplySideServer(UrlGroupSupplySideService urlGroupSupplySideService) {
		super(urlGroupSupplySideService);
	}

	@Override
	protected UrlGroupsResponse processRequest(final UrlGroupsRequest urlGroupsRequest) {
		Long timestamp = urlGroupsRequest.getTimestamp();
		Collection<UrlGroup> urlGroups = service.getUrlGroups(timestamp);
		UrlGroupsResponse response = new UrlGroupsResponse(getIdentification());
		response.setUrlGroups(urlGroups);

		return response;
	}

	@Override
	protected UrlGroupsResponse createEmptyResponse(Status status) {
		return new UrlGroupsResponse(getIdentification(), status);
	}

	@Override
	protected AbstractJsonTranslator<UrlGroupsResponse> getResponseTranslator() {
		return responseTranslator;
	}

	@Override
	protected AbstractJsonTranslator<UrlGroupsRequest> getRequestTranslator() {
		return requestTranslator;
	}
}