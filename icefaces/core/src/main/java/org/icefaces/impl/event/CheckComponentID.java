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

import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.logging.Logger;

public class CheckComponentID implements SystemEventListener {
    private static final Logger log = Logger.getLogger(CheckComponentID.class.getName());

    private final boolean devProjectStage;

    public CheckComponentID() {
        devProjectStage = FacesContext.getCurrentInstance().getApplication().getProjectStage() == ProjectStage.Development;
    }

    public boolean isListenerForSource(Object source) {
        return devProjectStage;
    }

    public void processEvent(SystemEvent event) {
        UIComponent addedComponent = (UIComponent) event.getSource();
        if ("submit".equals(addedComponent.getId())) {
            log.warning("Component " + addedComponent.getClass().getName() +
                    " uses the 'submit' string for the ID attribute. This is known to cause problems in Internet Explorer.");
        }
    }
}
