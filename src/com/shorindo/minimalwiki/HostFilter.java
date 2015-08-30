/*
 * Copyright 2015 Shorindo, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shorindo.minimalwiki;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 */
public class HostFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(HostFilter.class);
    private List<String> allowList = new ArrayList<String>();

    public void init(FilterConfig config) throws ServletException {
        String allow = config.getInitParameter("allow");
        if (allow != null) {
            for (String host : allow.split("\\s*,\\s*")) {
                allowList.add(host);
            }
        }
    }

    public void destroy() {
    }

    /**
     * If you want to use IPv4 address, add '-Djava.net.preferIPv4Stack=true' to JVM option
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        LOG.debug("remote=" + request.getRemoteHost() + "@" + ((HttpServletRequest)request).getRequestURI());
        if (allowList.contains(request.getRemoteAddr())) {
            chain.doFilter(request, response);
        }
    }


}
