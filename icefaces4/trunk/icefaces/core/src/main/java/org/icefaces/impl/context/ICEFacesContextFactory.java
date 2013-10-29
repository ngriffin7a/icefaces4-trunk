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


package org.icefaces.impl.context;

import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ICEFacesContextFactory extends FacesContextFactory  {
    public static String AJAX_FORCED_VIEWS = "org.icefaces.ajaxforcedviews";

    FacesContextFactory delegate;

    public ICEFacesContextFactory(FacesContextFactory delegate)  {
        this.delegate = delegate;
    }

    public FacesContextFactory getWrapped()  {
        return delegate;
    }


    /**
     * <p>Create and return a {@link FacesContext} instance.</p>
     */
    public FacesContext getFacesContext(Object context,
                                             Object request,
                                             Object response,
                                             Lifecycle lifecycle)  {
                                             
        //request will be wrapped if necessary, otherwise left unchanged
        Object wrappedRequest = request;

        Object ajaxForcedViews = null;
        if (request instanceof HttpServletRequest)  {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpSession session = servletRequest.getSession(false);
            if (null != session)  {
                ajaxForcedViews = session.getAttribute(AJAX_FORCED_VIEWS);
            }
            if (null != ajaxForcedViews)  {
                wrappedRequest = wrapIfMultipart(servletRequest);
            }
        }

        return new ICEfacesContext(delegate.getFacesContext(context,
                wrappedRequest, response, lifecycle));
    }
    
    private Object wrapIfMultipart(HttpServletRequest request)  {
        if ( (null != request.getContentType()) 
                && request.getContentType().contains("multipart") )  {
            return new AjaxForcedRequestWrapper(request);
        }
        return request;
    }

}

class AjaxForcedRequestWrapper extends HttpServletRequestWrapper  {
    private HttpServletRequest wrapped;

    public AjaxForcedRequestWrapper(HttpServletRequest wrapped)  {
        super(wrapped);
        this.wrapped = wrapped;
    }

    public String getHeader(String name)  {
        if (name.equals("Faces-Request"))  {
            return "partial/ajax";
        }
        return wrapped.getHeader(name);
    }
}

