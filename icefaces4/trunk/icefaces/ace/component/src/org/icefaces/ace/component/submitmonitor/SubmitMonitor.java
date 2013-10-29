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

package org.icefaces.ace.component.submitmonitor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.AbortProcessingException;

@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class SubmitMonitor extends SubmitMonitorBase
        implements ComponentSystemEventListener, java.io.Serializable {

    boolean isHidingIdleSubmitMonitor() {
        // When using an overlay, the submitMonitor is not shown when idle
        return !"@none".equals(getBlockUI());
    }

    String resolveBlockUI() {
        String blockUI = getBlockUI();
        if ("@all".equals(blockUI) || "@source".equals(blockUI) ||
            "@none".equals(blockUI)) {
            return blockUI;
        }
        UIComponent comp = findComponent(blockUI);
        if (comp != null) {
            return comp.getClientId();
        }
        return blockUI;
    }

    String resolveFor() {
        String origFor = getFor();
        if (origFor == null || origFor.trim().length() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String curFor : origFor.split(" ")) {
            UIComponent comp = findComponent(curFor);
            if (comp != null) {
                curFor = comp.getClientId();
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(curFor);
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        org.icefaces.util.EnvUtils.setBlockUIOnSubmit(facesContext, false);
        org.icefaces.util.EnvUtils.setDisableDefaultErrorPopups(facesContext, true);
    }
}
