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
package org.openrtb.dsp.core;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.openrtb.common.json.PublisherPreferencesRequestTranslator;
import org.openrtb.common.json.PublisherPreferencesResponseTranslator;
import org.openrtb.common.model.Identification;
import org.openrtb.common.model.Publisher;
import org.openrtb.common.model.PublisherPreferencesRequest;
import org.openrtb.common.model.PublisherPreferencesResponse;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.IdentificationService;
import org.openrtb.dsp.intf.service.PublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA. PublisherPreferencesRequester
 *
 * @author jdrahos
 */
public class PublisherPreferencesRequester {
	public static final String SPRING_NAME = "dsp.core.PublisherPreferencesRequester";

	private static final Logger logger = LoggerFactory.getLogger(PublisherPreferencesRequester.class);

	private static final PublisherPreferencesRequestTranslator REQUEST_TRANSFORM;
	private static final PublisherPreferencesResponseTranslator RESPONSE_TRANSFORM;

	static {
		REQUEST_TRANSFORM = new PublisherPreferencesRequestTranslator();
		RESPONSE_TRANSFORM = new PublisherPreferencesResponseTranslator();
	}

	private PublisherService publisherService;
	private IdentificationService identificationService;

	public PublisherPreferencesRequester(PublisherService publisherService, IdentificationService identificationService) {
		this.publisherService = publisherService;
		this.identificationService = identificationService;
	}

	public void requestAllPublisherPreferences() {
		Collection<SupplySidePlatform> platforms = identificationService.getServiceEndpoints();

		if (platforms == null || platforms.isEmpty()) {
			logger.info("Unable to sync publisher preferences with supply-side platforms; no platforms returned from IdentificationService#getServiceEndpoints().");
			return;
		}

		for (SupplySidePlatform ssp : platforms) {
			requestPublisherPreferences(ssp);
		}
	}

	public void requestPublisherPreferences(SupplySidePlatform ssp) {
		if (ssp == null) {
			logger.info("Unable to sync publisher preferences with supply-side platform; no platform provided in requestPublisherPreferences#requestPublisherPreferences(ssp).");
			return;
		}

		String organization = identificationService.getOrganizationIdentifier();
		Identification dsp = new Identification(organization);

		Collection<Publisher> publishers = publisherService.getPublisherList(ssp);

		if (publishers == null || publishers.isEmpty()) {
			logger.info("Unable to sync publisher preferences with supply-side platform [" + ssp.getOrganization() + "] no publishers returned from PublisherService#getPublisherList().");
			return;
		}

		PublisherPreferencesRequest request = new PublisherPreferencesRequest(dsp, publishers);
		PublisherPreferencesResponse response = null;

		try {
			response = makeRequest(ssp, REQUEST_TRANSFORM.toJSON(request));
			if (response != null) {
				if (response.getPublisherPreferences() != null && !response.getPublisherPreferences().isEmpty()) {
					publisherService.replacePublisherPreferencesList(ssp, response.getPublisherPreferences());
				} else {
					logger.error("Response from [" + ssp.getOrganization() + "] had no publisher preferences");
				}
			} else {
				logger.error("Response from [" + ssp.getOrganization() + "] was null");
			}
		} catch (IOException e) {
			logger.error("Unable to verify json response from [" + ssp.getOrganization() + "] due to exception", e);
		}
	}

	protected PublisherPreferencesResponse makeRequest(SupplySidePlatform ssp, String request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Organization Name [" + ssp.getOrganization() + "]");
			logger.debug("Organization Endpoint [" + ssp.getPublisherBatchServiceUrl() + "]");
			logger.debug("Organization Secret [" + new String(ssp.getSharedSecret()) + "]");
			logger.debug("Organization Request: " + request);
		}

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(ssp.getPublisherBatchServiceUrl());
		try {
			post.setRequestEntity(new StringRequestEntity(request, "application/json", null));
		} catch (UnsupportedEncodingException e) {
			logger.error("Unable to set [" + ssp.getOrganization() + "] request's encoding type: application/json", e);
			return null;
		}

		PublisherPreferencesResponse response = null;
		try {
			int statusCode = client.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Request for publisher preferences failed w/ code [" + statusCode + "] " + "for supply-side platform [" + ssp.getOrganization() + "] " + "w/ url [" + ssp.getPublisherBatchServiceUrl() + "]");
				return null;
			}
			response = RESPONSE_TRANSFORM.fromJSON(new InputStreamReader(post.getResponseBodyAsStream()));
			if (logger.isDebugEnabled()) {
				logger.debug("Organization Response: " + RESPONSE_TRANSFORM.toJSON(response));
			}
		} catch (HttpException e) {
			logger.error("Unable to send JSON request to [" + ssp.getOrganization() + "] " + "at [" + ssp.getPublisherBatchServiceUrl() + "]", e);
			return null;
		} catch (IOException e) {
			logger.error("Unable to process JSON response from [" + ssp.getOrganization() + "]", e);
			return null;
		} finally {
			post.releaseConnection();
		}

		return response;
	}
}
