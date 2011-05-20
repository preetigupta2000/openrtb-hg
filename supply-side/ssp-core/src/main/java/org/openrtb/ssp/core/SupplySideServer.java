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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openrtb.common.json.AbstractJsonTranslator;
import org.openrtb.common.model.Identification;
import org.openrtb.common.model.Request;
import org.openrtb.common.model.Response;
import org.openrtb.common.model.Status;
import org.openrtb.ssp.SupplySideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA. SupplySideServer
 *
 * @author jdrahos
 */
public abstract class SupplySideServer<TService extends SupplySideService, TRequest extends Request, TResponse extends Response> {
	private static final Logger log = LoggerFactory.getLogger(SupplySideServer.class);
	protected TService service;

	public SupplySideServer(TService service) {
		this.service = service;
	}

	public String process(String jsonRequest) {
		TRequest request = null;
		TResponse response = null;
		Status status = new Status();
		String jsonResponse = null;

		//process request
		try {
			//translate and verify request
			AbstractJsonTranslator<TRequest> requestTranslator = getRequestTranslator();
			request = requestTranslator.fromJSON(jsonRequest);
			if (request.getIdentification() == null || request.getIdentification().getOrganization() == null) {
				throw new IllegalArgumentException("Invalid Identity");
			}
			response = processRequest(request);
			//set success code
			status.setResponseCode(Status.SUCCESS_CODE, Status.SUCCESS_MESSAGE);
		} catch (IllegalArgumentException e) {
			status.setResponseCode(Status.AUTH_ERROR_CODE, e.getMessage());
		} catch (JsonMappingException e) {
			//e.printStackTrace();
			status.setResponseCode(Status.OTHER_ERROR_CODE, e.getMessage());
		} catch (JsonParseException e) {
			//e.printStackTrace();
			status.setResponseCode(Status.OTHER_ERROR_CODE, e.getMessage());
		} catch (IOException e) {
			//e.printStackTrace();
			status.setResponseCode(Status.OTHER_ERROR_CODE, e.getMessage());
		}

		if (response == null) {
			response = createEmptyResponse(status);
		} else {
			response.setStatus(status);
		}

		//translate response
		try {
			AbstractJsonTranslator<TResponse> responseTranslator = getResponseTranslator();
			jsonResponse = responseTranslator.toJSON(response);
		} catch (Exception e) {
			//what to do in this case? ... HTTP error?
			log.error("Response signing/translation failed", e);
			jsonResponse = null;
		}
		return jsonResponse;
	}

	protected Identification getIdentification() {
		return new Identification(service.getOrganization(), System.currentTimeMillis());
	}

	protected abstract TResponse processRequest(final TRequest request);

	protected abstract TResponse createEmptyResponse(Status status);

	protected abstract AbstractJsonTranslator<TResponse> getResponseTranslator();

	protected abstract AbstractJsonTranslator<TRequest> getRequestTranslator();
}
