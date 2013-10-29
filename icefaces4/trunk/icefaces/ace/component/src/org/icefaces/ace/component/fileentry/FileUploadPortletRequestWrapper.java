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

package org.icefaces.ace.component.fileentry;

import javax.portlet.PortletRequest;
import javax.portlet.filter.PortletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

public class FileUploadPortletRequestWrapper extends PortletRequestWrapper {

    private Map<String, String[]> parameterMap;

    public FileUploadPortletRequestWrapper(Object request,
                                           Map<String, String[]> parameterMap) {
        super((PortletRequest)request);
        this.parameterMap = parameterMap;
    }

    // Returns a java.util.Map of the parameters of this request.
    public Map<String, String[]> getParameterMap() {
        if (parameterMap != null) {
            return Collections.unmodifiableMap(parameterMap);
        }
        return super.getParameterMap();
    }

    // Returns an Enumeration of String objects containing the names of
    // the parameters contained in this request.
    public Enumeration<String> getParameterNames() {
        if (parameterMap != null) {
            Vector<String> keyVec = new Vector<String>(parameterMap.keySet());
            return keyVec.elements();
        }
        return super.getParameterNames();
    }

    // Returns the value of a request parameter as a String, or null if
    // the parameter does not exist.
    public String getParameter(String name) {
        if (parameterMap != null) {
            if (!parameterMap.containsKey(name)) {
                //ICE-8008: defer to the actual request if the key can't be found
                return super.getParameter(name);
            }
            String[] values = parameterMap.get(name);
            if (values != null && values.length >= 1) {
                return values[0];
            }
            return null; // Or "", since the key does exist?
        }
        return super.getParameter(name);
    }

    // Returns an array of String objects containing all of the values the
    // given request parameter has, or null if the parameter does not exist.
    public String[] getParameterValues(String name) {
        if (parameterMap != null) {
            if (!parameterMap.containsKey(name)) {
                return null;
            }
            return parameterMap.get(name);
        }
        return super.getParameterValues(name);
    }

    public String getContentType() {
        return FileEntryResourceHandler.APPLICATION_FORM_URLENCODED;
    }

}
