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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFT:) WARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.openrtb.ssp.core;

import org.openrtb.common.json.AbstractJsonTranslator;
import org.openrtb.common.json.PublisherPreferencesRequestTranslator;
import org.openrtb.common.json.PublisherPreferencesResponseTranslator;
import org.openrtb.common.model.Publisher;
import org.openrtb.common.model.PublisherPreference;
import org.openrtb.common.model.PublisherPreferencesRequest;
import org.openrtb.common.model.PublisherPreferencesResponse;
import org.openrtb.common.model.Status;
import org.openrtb.ssp.service.PublisherSupplySideService;

import java.util.Collection;

/**
 * The stateless processing of batch Open RTB JSON requests resulting in JSON responses. Besides translation of JSON to
 * internal model objects it verifies the requests and signs the responses. Its dependency on an SSP implementor is
 * defined by the {@link org.openrtb.ssp.service.PublisherSupplySideService} interface.
 *
 * @since 1.0.1
 */
public class PublisherSupplySideServer extends SupplySideServer<PublisherSupplySideService, PublisherPreferencesRequest, PublisherPreferencesResponse> {
	private static final PublisherPreferencesRequestTranslator reqTrans = new PublisherPreferencesRequestTranslator();
	private static final PublisherPreferencesResponseTranslator resTrans = new PublisherPreferencesResponseTranslator();

	public PublisherSupplySideServer(PublisherSupplySideService publisherSupplySideService) {
		super(publisherSupplySideService);
	}

	@Override
	protected PublisherPreferencesResponse processRequest(final PublisherPreferencesRequest publisherPreferencesRequest) {
		Collection<Publisher> publishers = publisherPreferencesRequest.getPublishers();
		Collection<PublisherPreference> publisherPreferences = service.getPublisherPreferences(publishers);
		PublisherPreferencesResponse publisherPreferencesResponse = new PublisherPreferencesResponse(getIdentification());
		publisherPreferencesResponse.setPublisherPreferences(publisherPreferences);

		return publisherPreferencesResponse;
	}

	@Override
	protected PublisherPreferencesResponse createEmptyResponse(final Status status) {
		return new PublisherPreferencesResponse(getIdentification(), status);
	}

	@Override
	protected AbstractJsonTranslator<PublisherPreferencesResponse> getResponseTranslator() {
		return resTrans;
	}

	@Override
	protected AbstractJsonTranslator<PublisherPreferencesRequest> getRequestTranslator() {
		return reqTrans;
	}
}
