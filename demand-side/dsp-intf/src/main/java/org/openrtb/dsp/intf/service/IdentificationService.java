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
package org.openrtb.dsp.intf.service;

import java.util.Collection;

import org.openrtb.common.model.Identification;
import org.openrtb.dsp.intf.model.SupplySidePlatform;

/**
 * Single interface for interacting with identification data, specifically:
 * <ol>
 * <li>identify your organization for making communication
 * {@link #getOrganizationIdentifier()}, and</li>
 * <li>identifying who you want to send requests/response to, along with their
 * service urls and shared secrets</li>.
 * </ol>
 *
 * @since 1.0
 */
public interface IdentificationService {

    public static final String SPRING_NAME = "dsp.intf.IdentificationService";

    /**
     * @deprecated this method has been deprecated in lieu of using
     *             {@link SupplySidePlatform#getDemandSideName()}. The return
     *             results will only be used if no value is supplied for
     *             specific {@link SupplySidePlatform} as a single DSP can be
     *             identified to by multiple SSPs with a different unique key.
     */
    @Deprecated
    String getOrganizationIdentifier();

    /**
     * Returns a non-<tt>null</tt> list of supply-side platforms that requests
     * will be sent to. dsp-core will be iterating through these inventory
     * suppliers, building a list of appropriate information for the request
     * from other service calls.
     *
     * Information about the SSPs organization identifier, shared secret, and
     * batch service url are populated in the {@link SupplySidePlatform}. For
     * more information about that information, please see the javadoc for
     * {@link SupplySidePlatform}.
     *
     * @return a non-<tt>null</tt> list of supply-side platforms to request
     *         information from.
     */
    Collection<SupplySidePlatform> getServiceEndpoints();

}
