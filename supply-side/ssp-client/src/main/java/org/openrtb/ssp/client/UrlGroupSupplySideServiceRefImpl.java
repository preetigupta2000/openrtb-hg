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
package org.openrtb.ssp.client;

import org.openrtb.common.model.UrlGroup;
import org.openrtb.ssp.UrlGroupSupplySideService;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. UrlGroupSupplySideServiceRefImpl
 *
 * @author jdrahos
 */
public class UrlGroupSupplySideServiceRefImpl implements UrlGroupSupplySideService {
	public static Map<UrlGroup, Long> urlGroups = new Hashtable<UrlGroup, Long>();

	static {
		urlGroups.put(new UrlGroup("test_group1", Arrays.asList("test.com", "test.ca")), 100L);
		urlGroups.put(new UrlGroup("test_group2", Arrays.asList("test2.com", "test2.ca")), 150L);
		urlGroups.put(new UrlGroup("test_group3", Arrays.asList("test3.com", "test3.ca")), 200L);
	}

	@Override
	public String getOrganization() {
		return "The SSP";
	}

	@Override
	public byte[] getSharedSecret(final String ssp) {
		return "RTB".getBytes();
	}

	@Override
	public List<UrlGroup> getUrlGroups(Long timestamp) {
		if (timestamp == null) {
			timestamp = -1L;
		}

		List<UrlGroup> result = new LinkedList<UrlGroup>();

		for (UrlGroup urlGroup : urlGroups.keySet()) {
			if (urlGroups.get(urlGroup) > timestamp) {
				result.add(urlGroup);
			}
		}

		return result;
	}
}
