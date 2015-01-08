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

package org.icefaces.impl.application;

import org.icefaces.impl.util.CoreUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class InitializeHeadAndBodyFacetContainers implements PhaseListener {
    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {
        initializeFacetContainer("head");
        initializeFacetContainer("body");
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    private void initializeFacetContainer(String target) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final UIViewRoot root = context.getViewRoot();
        UIComponent container = CoreUtils.getResourceContainer(root, target);
        if (container == null) {
            UIComponent c = new UIOutput();
            root.addComponentResource(context, c, target);
            root.removeComponentResource(context, c, target);
        }
    }
}
