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

import org.openrtb.common.model.UrlGroup;
import org.openrtb.dsp.intf.model.SupplySidePlatform;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA. UrlGroupService
 *
 * @author jdrahos
 */
public interface UrlGroupService {
	public static final String SPRING_NAME = "dsp.intf.UrlGroupService";

	/**
	 * returns posix timestamp of beginning of the last url group synchronization with provided ssp
	 *
	 * @param ssp supply side platform
	 * @return posix timestamp
	 */
	public Long getLastSynchronizationTimestamp(SupplySidePlatform ssp);

	/**
	 * replaces url groups found in the collection for the specified ssp
	 *
	 * @param ssp	   supply side platform
	 * @param urlGroups url groups to be replaced
	 */
	public void replaceUrlGroups(SupplySidePlatform ssp, Collection<UrlGroup> urlGroups);
}
