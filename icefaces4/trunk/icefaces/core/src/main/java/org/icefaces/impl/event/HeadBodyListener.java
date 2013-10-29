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

package org.icefaces.impl.event;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.icefaces.util.EnvUtils;

/**
 * The aim of the class is to listen for HtmlHead and HtmlBody components as we
 * won't attempt to add any ICEfaces scripts and resources unless these components
 * are present in the view.  This class is currently not optimized in how it detects
 * the components but this might be related to a bug in JSF.
 */
public class HeadBodyListener implements SystemEventListener {
    private final static Logger log = Logger.getLogger("org.icefaces.impl.event.HeadBodyListener");

    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        Map viewMap = viewRoot.getViewMap();

        //TODO: improve detection of HtmlHead and HtmlBody components
        // Since the current Sun JSF implementation returns HtmlHead and HtmlBody
        // as UIOutput component instances, we can't rely on that to for detection.
        // Instead, we need to check the rendererType which is potentially fragile
        // as it can be overridden.

        if (!viewMap.containsKey(EnvUtils.HEAD_DETECTED)) {
            List<UIComponent> children = viewRoot.getChildren();
            for (UIComponent c : children) {
                String rendererType = c.getRendererType();
                if ("javax.faces.Head".equals(rendererType)) {
                    viewMap.put(EnvUtils.HEAD_DETECTED, EnvUtils.HEAD_DETECTED);
                    if (log.isLoggable(Level.FINER)) {
                        log.log(Level.FINER, "head detected");
                    }
                }
            }
        }

        if (!viewMap.containsKey(EnvUtils.BODY_DETECTED)) {
            List<UIComponent> children = viewRoot.getChildren();
            for (UIComponent c : children) {
                String rendererType = c.getRendererType();
                if ("javax.faces.Body".equals(rendererType)) {
                    viewMap.put(EnvUtils.BODY_DETECTED, EnvUtils.BODY_DETECTED);
                    if (log.isLoggable(Level.FINER)) {
                        log.log(Level.FINER, "body detected");
                    }
                }
            }
        }
    }

    public boolean isListenerForSource(final Object source) {
        return source instanceof UIViewRoot;
    }
}
