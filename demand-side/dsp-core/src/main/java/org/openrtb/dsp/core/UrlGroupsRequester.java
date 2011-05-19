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
import org.openrtb.common.json.UrlGroupsRequestTranslator;
import org.openrtb.common.json.UrlGroupsResponseTranslator;
import org.openrtb.common.model.Identification;
import org.openrtb.common.model.UrlGroupsRequest;
import org.openrtb.common.model.UrlGroupsResponse;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.IdentificationService;
import org.openrtb.dsp.intf.service.UrlGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA. UrlGroupsRequester
 *
 * @author jdrahos
 */
public class UrlGroupsRequester {
	public static final String SPRING_NAME = "dsp.core.UrlGroupsRequester";

	private static final Logger logger = LoggerFactory.getLogger(UrlGroupsRequester.class);

	private static final UrlGroupsRequestTranslator REQUEST_TRANSFORM;
	private static final UrlGroupsResponseTranslator RESPONSE_TRANSFORM;

	static {
		REQUEST_TRANSFORM = new UrlGroupsRequestTranslator();
		RESPONSE_TRANSFORM = new UrlGroupsResponseTranslator();
	}

	private UrlGroupService urlGroupService;
	private IdentificationService identificationService;

	public UrlGroupsRequester(UrlGroupService urlGroupService, IdentificationService identificationService) {
		this.urlGroupService = urlGroupService;
		this.identificationService = identificationService;
	}

	/**
	 * triggers synchronization of url groups between dsp and all ssps
	 */
	public void getUrlGroupsFromAllSSPs() {
		Collection<SupplySidePlatform> platforms = identificationService.getServiceEndpoints();

		if (platforms == null || platforms.isEmpty()) {
			logger.info("Unable to sync url groups with supply-side platforms; no platforms returned from IdentificationService#getServiceEndpoints().");
			return;
		}

		for (SupplySidePlatform ssp : platforms) {
			getUrlGroups(ssp);
		}
	}

	/**
	 * call this method to upload the data about url groups to particular ssp
	 *
	 * @param ssp the ssp to upload the data to
	 * @return true if we were able to build the request and there was successful response from the ssp otherwise false
	 */
	public void getUrlGroups(SupplySidePlatform ssp) {
		if (ssp == null) {
			logger.info("Unable to sync url groups with supply-side platform; no platform provided in UrlGroupsRequester#getUrlGroups(ssp).");
			return;
		}

		String organization = identificationService.getOrganizationIdentifier();
		Identification dsp = new Identification(organization);
		Long timestamp = urlGroupService.getLastSynchronizationTimestamp(ssp);

		if (timestamp == null) {
			logger.info("Synchronizing all url groups for [{}]", ssp.getOrganization());
		} else {
			logger.info("Synchronizing url groups modified since [{}] for [{}]", timestamp, ssp.getOrganization());
		}

		UrlGroupsRequest request = new UrlGroupsRequest(dsp, timestamp);
		UrlGroupsResponse response = null;

		try {
			response = makeRequest(ssp, REQUEST_TRANSFORM.toJSON(request));
			if (response != null) {
				urlGroupService.replaceUrlGroups(ssp, response.getUrlGroups());
			} else {
				logger.error("Response from [" + ssp.getOrganization() + "] was null");
			}
		} catch (IOException e) {
			logger.error("Unable to verify json response from [" + ssp.getOrganization() + "] due to exception", e);
		}
	}

	protected UrlGroupsResponse makeRequest(SupplySidePlatform ssp, String request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Organization Name [" + ssp.getOrganization() + "]");
			logger.debug("Organization Endpoint [" + ssp.getUrlGroupsBatchServiceUrl() + "]");
			logger.debug("Organization Secret [" + new String(ssp.getSharedSecret()) + "]");
			logger.debug("Organization Request: " + request);
		}

		UrlGroupsResponse response = null;
		String batchServiceUrl = ssp.getUrlGroupsBatchServiceUrl();

		if (batchServiceUrl == null || batchServiceUrl.isEmpty()) {
			logger.error("The url groups service url wasn't set for ssp [{}]", ssp.getOrganization());
			return null;
		}

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(batchServiceUrl);

		try {
			post.setRequestEntity(new StringRequestEntity(request, "application/json", null));
		} catch (UnsupportedEncodingException e) {
			logger.error("Unable to set [" + ssp.getOrganization() + "] request's encoding type: application/json", e);
			return null;
		}

		try {
			int statusCode = client.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Request for url groups failed w/ code [" + statusCode + "] " + "for supply-side platform [" + ssp.getOrganization() + "] " + "w/ url [" + ssp.getUrlGroupsBatchServiceUrl() + "]");
				return null;
			}
			response = RESPONSE_TRANSFORM.fromJSON(new InputStreamReader(post.getResponseBodyAsStream()));
			if (logger.isDebugEnabled()) {
				logger.debug("Organization Response: " + RESPONSE_TRANSFORM.toJSON(response));
			}
		} catch (HttpException e) {
			logger.error("Unable to send JSON request to [" + ssp.getOrganization() + "] " + "at [" + ssp.getUrlGroupsBatchServiceUrl() + "]", e);
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