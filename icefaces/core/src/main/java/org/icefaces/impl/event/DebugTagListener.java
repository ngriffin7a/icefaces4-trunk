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

import org.icefaces.util.EnvUtils;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * As per ICE-5717, the <ui:debug> tag was causing full page refreshes so we wrap it with
 * a panelGroup in order to contain the updated region and prevent it from searching farther
 * up the tree.  Because this is a Mojarra-specific issue and uses internal Mojarra classes,
 * we adopt a mini-factory approach and delegate the handling of the issue to a class
 * specifically designed for it.  We need to do this in order to allow us to register the
 * SystemEventListener in a way that's independent of the JSF implementation.
 */
public class DebugTagListener implements SystemEventListener {
    private static final Logger LOGGER = Logger.getLogger(DebugTagListener.class.getName());

    private SystemEventListener wrapped;

    public DebugTagListener() {
        if (EnvUtils.isMojarra()) {
            try {
                Class clazz = Class.forName("org.icefaces.impl.event.MojarraDebugTagListener");
                Object obj = clazz.newInstance();
                wrapped = (SystemEventListener) obj;
            } catch (Exception e) {
                //If anything bad happens, the fix will simply not be applied.
//                e.printStackTrace();
            }
        }
    }

    public boolean isListenerForSource(Object source) {
        if (wrapped != null) {
            return wrapped.isListenerForSource(source);
        }
        return false;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (wrapped != null) {
            wrapped.processEvent(event);
        }
    }
}
