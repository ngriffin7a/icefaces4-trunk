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

package org.icefaces.mobi.component.cloudpush;

import org.icefaces.impl.event.ResourceOutputUtil;
import org.icefaces.impl.util.CoreUtils;

import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import java.util.Map;

public class AddBridgeitJsResource implements PhaseListener {
    public void afterPhase(PhaseEvent event) {

    }

    public void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        Map sessionMap = context.getExternalContext().getSessionMap();
        if (sessionMap != null && sessionMap.get(CloudPushRenderer.class.getName()) == Boolean.TRUE) {
            String name = "core/bridgeit.js";
            String library = "icefaces.mobi";
            String target = "head";
            String rendererType = context.getApplication().getResourceHandler().getRendererTypeForResourceName(name);
            UIViewRoot viewRoot = context.getViewRoot();
            CoreUtils.setInView(viewRoot, target, false);
            viewRoot.addComponentResource(FacesContext.getCurrentInstance(), ResourceOutputUtil.createResourceComponent(name, library, rendererType, true), target);
            CoreUtils.setInView(viewRoot, target, true);
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}