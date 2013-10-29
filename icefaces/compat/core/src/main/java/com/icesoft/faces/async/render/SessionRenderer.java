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

package com.icesoft.faces.async.render;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.logging.Logger;

public class SessionRenderer extends PushRenderer {
    private static Logger log = Logger.getLogger(SessionRenderer.class.getName());
    //avoid referencing PortableRenderer class in static context so that application still works when ICEpush not present
    private static Object portableRenderer;

    public static void render(String groupName) {
        if (portableRenderer != null) {
            if (FacesContext.getCurrentInstance() == null) {
                ((PortableRenderer) portableRenderer).render(groupName);
            } else {
                PushRenderer.render(groupName);
            }
        }
    }

    public static class StartupListener implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            try {
                portableRenderer = PushRenderer.getPortableRenderer();
            } catch (NoClassDefFoundError e) {
                log.info("ICEpush library missing. Cannot enable push functionality.");
            }
        }

        public boolean isListenerForSource(Object source) {
            return true;
        }
    }
}
