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

package org.icefaces.mobi.component.geotrack;

import org.icefaces.impl.util.CoreUtils;
import org.icefaces.impl.util.Base64;
import org.icefaces.impl.util.Util;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletInputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.servlet.http.HttpSession;

public class GeotrackResourceHandler extends ResourceHandlerWrapper {
    private static final byte[] NO_BYTES = new byte[0];
    private ResourceHandler handler;

	private static final String UPLOAD_RESOURCE = "geotrack.txt";
	private static final String LIB = "org.icefaces.component.geotrack";

    private Resource resource = null;

	private static GeotrackResourceHandler instance = null;

	public static String getPostURL() {
		if (instance.resource == null) {
			instance.resource = instance.createResource(UPLOAD_RESOURCE, LIB);
		}
		return instance.resource.getRequestPath();
	}

    public GeotrackResourceHandler(ResourceHandler handler) {
		instance = this;
        this.handler = handler;
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    public Resource createResource(String resourceName) {
        return createResource(resourceName, null, null);
    }

    public Resource createResource(String resourceName, String libraryName) {
        return createResource(resourceName, libraryName, null);
    }

    public Resource createResource(String resourceName, String libraryName, String contentType) {
        if (UPLOAD_RESOURCE.equals(resourceName)) {
            if (resource == null) {
                resource = super.createResource(resourceName, LIB);
            }
            return resource;
        } else {
            return super.createResource(resourceName, libraryName, contentType);
        }
    }

    public void handleResourceRequest(FacesContext facesContext) throws IOException {
		if (resource.getRequestPath().startsWith(getResourcePath(facesContext))) {
			// get id
			ExternalContext externalContext = facesContext.getExternalContext();
			Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
			String id = requestParameterMap.get("_id");
			if (id == null || "".equals(id)) return;

			// get data
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
			ServletInputStream sis = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(sis);
			BufferedReader br =  new BufferedReader(isr);
			String data = br.readLine();

			// get value expression
			Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();
			Object o = applicationMap.get(id);
			if (!(o instanceof ValueExpression)) return;

			// set value expression
			ValueExpression ve = (ValueExpression) o;
			ve.setValue(facesContext.getELContext(), data);
		}
        handler.handleResourceRequest(facesContext);
    }

    private static String getResourcePath(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();
        String path = externalContext.getRequestServletPath();
        if (null == path)  {
            path = externalContext.getRequestPathInfo();
        }
        return (externalContext.getRequestContextPath() + path);
    }
}
