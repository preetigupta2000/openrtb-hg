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
import org.openrtb.common.json.AdvertiserBlocklistRequestTranslator;
import org.openrtb.common.json.AdvertiserBlocklistResponseTranslator;
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.AdvertiserBlocklistRequest;
import org.openrtb.common.model.AdvertiserBlocklistResponse;
import org.openrtb.common.model.Status;
import org.openrtb.ssp.service.AdvertiserSupplySideService;

import java.util.Collection;

/**
 * The stateless processing of batch Open RTB JSON requests resulting in JSON responses. Besides translation of JSON to
 * internal model objects it verifies the requests and signs the responses. Its dependency on an SSP implementor is
 * defined by the {@link AdvertiserSupplySideService} interface.
 *
 * @since 1.0.1
 */
public class AdvertiserSupplySideServer extends SupplySideServer<AdvertiserSupplySideService, AdvertiserBlocklistRequest, AdvertiserBlocklistResponse> {
	private static AdvertiserBlocklistRequestTranslator reqTrans = new AdvertiserBlocklistRequestTranslator();
	private static AdvertiserBlocklistResponseTranslator resTrans = new AdvertiserBlocklistResponseTranslator();

	public AdvertiserSupplySideServer(AdvertiserSupplySideService advertiserSupplySideService) {
		super(advertiserSupplySideService);
	}

	@Override
	protected AdvertiserBlocklistResponse processRequest(final AdvertiserBlocklistRequest advertiserBlocklistRequest) {
		Collection<Advertiser> advertisers = advertiserBlocklistRequest.getAdvertisers();
		advertisers = service.setBlocklists(advertisers);
		AdvertiserBlocklistResponse response = new AdvertiserBlocklistResponse(getIdentification());
		response.setAdvertisers(advertisers);

		return response;
	}

	@Override
	protected AdvertiserBlocklistResponse createEmptyResponse(final Status status) {
		return new AdvertiserBlocklistResponse(getIdentification(), status);
	}

	@Override
	protected AbstractJsonTranslator<AdvertiserBlocklistResponse> getResponseTranslator() {
		return resTrans;
	}

	@Override
	protected AbstractJsonTranslator<AdvertiserBlocklistRequest> getRequestTranslator() {
		return reqTrans;
	}

}
