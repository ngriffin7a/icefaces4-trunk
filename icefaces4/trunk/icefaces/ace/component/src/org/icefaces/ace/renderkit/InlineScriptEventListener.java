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
package org.icefaces.ace.renderkit;

import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.Map;
import java.util.HashMap;

public class InlineScriptEventListener implements SystemEventListener  {
    static final String MARKER_MAP = 
            InlineScriptEventListener.class.getName();

    public InlineScriptEventListener() {
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.getPartialViewContext().isAjaxRequest())  {
            getMarkerMap(facesContext).clear();
        }
    }

    public static Map getMarkerMap(FacesContext facesContext)  {
        Map viewMap = facesContext.getViewRoot().getViewMap();
        Map markerMap = (Map) viewMap.get(MARKER_MAP);
        if (null == markerMap)  {
            markerMap = new HashMap();
            viewMap.put(MARKER_MAP, markerMap);
        }
        return markerMap;
    }

    public static void setScriptLoaded(FacesContext facesContext, 
            String JS_NAME) {
        Map markerMap = getMarkerMap(facesContext);
        markerMap.put(JS_NAME, "true");
    }

    public static boolean isScriptLoaded(FacesContext facesContext, String JS_NAME) {
        Map markerMap = getMarkerMap(facesContext);
        return markerMap.containsKey(JS_NAME);
    }

}
