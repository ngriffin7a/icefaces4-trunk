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

package org.icefaces.impl.application;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.lifecycle.ClientWindowFactory;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class CustomClientWindowFactory extends ClientWindowFactory {
    private ClientWindowFactory clientWindowFactory;
    private boolean customWindowTracking;

    public CustomClientWindowFactory(ClientWindowFactory clientWindowFactory) {
        this.clientWindowFactory = clientWindowFactory;
        this.customWindowTracking = !"url".equals(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("javax.faces.CLIENT_WINDOW_MODE"));
    }

    public ClientWindow getClientWindow(FacesContext context) {
        if (customWindowTracking) {
            try {
                String id = WindowScopeManager.determineWindowID(context, true);
                ClientWindow clientWindow = new CustomClientWindow(id);
                clientWindow.disableClientWindowRenderMode(context);

                return clientWindow;
            } catch (Exception e) {
                return null;
            }
        } else {
            return clientWindowFactory.getClientWindow(context);
        }
    }

    private static class CustomClientWindow extends ClientWindow {
        private final String id;

        public CustomClientWindow(String id) {
            this.id = id;
        }

        public Map<String, String> getQueryURLParameters(FacesContext context) {
            return Collections.EMPTY_MAP;
        }

        public String getId() {
            return id;
        }

        public void decode(FacesContext context) {

        }
    }
}
