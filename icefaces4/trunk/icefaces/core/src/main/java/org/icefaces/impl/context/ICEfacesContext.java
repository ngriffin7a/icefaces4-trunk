/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.impl.context;

import org.icefaces.util.EnvUtils;

import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import java.net.URI;
import java.net.URL;

public class ICEfacesContext extends FacesContextWrapper {
    private FacesContext wrapped;

    public ICEfacesContext(FacesContext wrapped) {
        this.wrapped = wrapped;
    }

    public FacesContext getWrapped() {
        return wrapped;
    }

    public ExternalContext getExternalContext() {
        return new ICEfacesExternalContext(wrapped.getExternalContext());
    }

    public static class ICEfacesExternalContext extends ExternalContextWrapper {
        private ExternalContext wrapped;

        public ICEfacesExternalContext(ExternalContext wrapped) {
            this.wrapped = wrapped;
        }

        public ExternalContext getWrapped() {
            return wrapped;
        }

        public String encodeResourceURL(String url) {
            if (EnvUtils.instanceofPortletRequest(wrapped.getRequest())) {
                return wrapped.encodeResourceURL(url);
            }
            //the following tests should not be necessary since the encodeResourceURL should be used only to encode
            //resource URLs, unfortunately Mojarra's OutputLinkRenderer uses this method for encoding the hyperlink URLs
            if (url.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
                return url;
            }
            //avoid encoding external URLs
            String requestServerName = wrapped.getRequestServerName();
            if (!url.contains("://" + requestServerName)) {
                return url;
            }
            String mimeType = wrapped.getMimeType(url);
            if (mimeType != null && (
                    mimeType.startsWith("image/") ||
                            mimeType.startsWith("video/") ||
                            mimeType.startsWith("audio/") ||
                            mimeType.equals("text/css") ||
                            mimeType.equals("text/javascript"))) {
                return url;
            }

            return wrapped.encodeResourceURL(url);
        }
    }
}
