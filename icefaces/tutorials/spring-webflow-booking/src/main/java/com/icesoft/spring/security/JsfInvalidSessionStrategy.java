/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.icesoft.spring.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

public class JsfInvalidSessionStrategy implements InvalidSessionStrategy {
    private final Log logger = LogFactory.getLog(getClass());
    private final String invalidSessionUrl;
    private final RedirectStrategy redirectStrategy = new JsfRedirectStrategy();
    private boolean createNewSession = true;

    public JsfInvalidSessionStrategy(String invalidSessionUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
        this.invalidSessionUrl = invalidSessionUrl;
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Starting new session (if required) and redirecting to '" + invalidSessionUrl + "'");
        if (createNewSession) {
            request.getSession();
        }
        redirectStrategy.sendRedirect(request, response, invalidSessionUrl);
    }

    /**
     * Determines whether a new session should be created before redirecting (to avoid possible looping issues where
     * the same session ID is sent with the redirected request). Alternatively, ensure that the configured URL
     * does not pass through the {@code SessionManagementFilter}.
     *
     * @param createNewSession defaults to {@code true}.
     */
    public void setCreateNewSession(boolean createNewSession) {
        this.createNewSession = createNewSession;
    }
}
