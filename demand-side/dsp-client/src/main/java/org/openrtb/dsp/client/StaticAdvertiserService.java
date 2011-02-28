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
package org.openrtb.dsp.client;

import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.Blocklist;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.AdvertiserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is simply a reference implementation of a static service for testing,
 * etc. For more information about this service, please see the interface listed
 * below.
 *
 * @see AdvertiserService
 */
public class StaticAdvertiserService extends AbstractStaticService
                                     implements AdvertiserService {

    private static final Logger log = LoggerFactory.getLogger(StaticAdvertiserService.class);

    public static List<Advertiser> advertisers = new ArrayList<Advertiser>();
    static {
        // ssp-client configured advertiser
        advertisers.add(new Advertiser("acmeluxuryfurniture.com"));

        // unknowns
        advertisers.add(new Advertiser("intriguing.biz"));
        advertisers.add(new Advertiser("sports-advertiser.com", "Some Sports Advertiser"));
        advertisers.add(new Advertiser("unknown.info"));
    }

    @Override
    public List<Advertiser> getAdvertiserList() {
        return new ArrayList<Advertiser>(advertisers);
    }

    @Override
    public void replaceBlocklists(SupplySidePlatform ssp, 
                                  Collection<Advertiser> advertisers) {
        Set<String> publisher = new HashSet<String>();
        Set<String> site = new HashSet<String>();
        StringBuilder blocklistBuilder = new StringBuilder();

        for(Advertiser advertiser : advertisers) {
            blocklistBuilder.delete(0, blocklistBuilder.length());
            for(Blocklist blocklist : advertiser.getBlocklist()) {
                publisher.add(blocklist.getPublisherId());
                site.add(blocklist.getSiteId());
                blocklistBuilder.append("[").append(blocklist.getPublisherId()).append(":").append(blocklist.getSiteId()).append("],");
            }
            String blocklistValue = (blocklistBuilder.length() > 0 ? blocklistBuilder.substring(0, blocklistBuilder.length()-1) : "");

            log.info("received advertiser ["+advertiser.getLandingPage()+"] blocked on ["+publisher.size()+"] publishers and ["+site.size()+"] sites");
            log.info("received advertiser ["+advertiser.getLandingPage()+"] w/ blocklist ["+ blocklistValue+"]");
        }
    }

}
