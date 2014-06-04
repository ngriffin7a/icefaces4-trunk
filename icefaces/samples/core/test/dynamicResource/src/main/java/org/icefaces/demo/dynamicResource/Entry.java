/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.dynamicResource;

import org.icefaces.application.ResourceRegistry;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "Entry")
@SessionScoped
public class Entry implements Serializable {
    private Resource dynamic;

    public Entry() throws UnsupportedEncodingException {
        this.dynamic = new TestResource("dynamic resource test");
     }

    public Resource getFilesystemResource() {
        return FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("text.resource.txt");
    }

    public Resource getDynamicResource() {
        return dynamic;
    }

    public Resource getDynamicResourceA() {
        return new TestResource("dynamic resource test - A");
    }

    public Resource getDynamicResourceB() {
        return new TestResource("dynamic resource test - B");
    }

    public Resource getDynamicResourceC() {
        return new TestResource("dynamic resource test - C");
    }

    public Resource getDynamicResourceD() {
        return new TestResource("dynamic resource test - D");
    }

    private class TestResource extends Resource {
        private String name = UUID.randomUUID().toString();
        private String content;

        private TestResource(String content) {
            this.content = content;
            setContentType("text/plain");
        }

        public String getResourceName() {
            return name;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content.getBytes("UTF-8"));
        }

        public Map<String, String> getResponseHeaders() {
            return Collections.emptyMap();
        }

        public String getRequestPath() {
            return null;
        }

        public URL getURL() {
            return null;
        }

        public boolean userAgentNeedsUpdate(FacesContext context) {
            return false;
        }
    }
}
