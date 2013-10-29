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

package com.icesoft.faces.application;

import com.icesoft.faces.el.PartialSubmitValueBinding;
import com.icesoft.util.CoreComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PartialSubmitPhaseListener implements PhaseListener {
    private static final String REQUIRED = "required";
    private static final String ALTERED_KEY = "org.icefaces.altered-components";
    private static final String ICE_PARTIAL = "ice.submit.partial";
    private static final String ICE_CAPTURED = "ice.event.captured";
    private static final String TRUE = "true";

    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext facesContext = phaseEvent.getFacesContext();
        ExternalContext externalContext = facesContext.getExternalContext();
        if (PhaseId.RESTORE_VIEW == phaseEvent.getPhaseId()) {
            Map parameterMap = externalContext.getRequestParameterMap();
            if (TRUE.equals(parameterMap.get(ICE_PARTIAL))) {
                String componentID = (String)
                        parameterMap.get(ICE_CAPTURED);
                UIComponent component = CoreComponentUtils
                        .findComponent(componentID, facesContext.getViewRoot());
                Map alteredRequiredComponents =
                        setRequiredFalseInFormContaining(component, componentID);
                Map requestMap = externalContext.getRequestMap();
                requestMap.put(ALTERED_KEY, alteredRequiredComponents);
            }
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {

        // ICE-3884 restore required-flags before state saving
        // writes the component state in stone
        FacesContext facesContext = phaseEvent.getFacesContext();
        ExternalContext externalContext = facesContext.getExternalContext();
        if (PhaseId.RENDER_RESPONSE == phaseEvent.getPhaseId()) {
            Map requestMap = externalContext.getRequestMap();
            Map alteredComponents = (Map) requestMap.get(ALTERED_KEY);
            if (null != alteredComponents) {
                setRequiredTrue((Map) requestMap.get(ALTERED_KEY));
                requestMap.remove(ALTERED_KEY);
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    private void setRequiredTrue(Map requiredComponents) {
        Iterator i = requiredComponents.keySet().iterator();
        UIInput next = null;
        while (i.hasNext()) {
            next = (UIInput) i.next();
            ValueBinding valueBinding = (ValueBinding)
                    requiredComponents.get(next);
            if (null != valueBinding) {
                next.setValueBinding(REQUIRED, valueBinding);
            } else {
                next.setRequired(true);
            }
        }
    }

    private Map setRequiredFalseInFormContaining(
            UIComponent component, String clientId) {
        Map alteredComponents = new HashMap();
        UIComponent form = getContainingForm(component);
        setRequiredFalseOnAllChildrenExceptOne(form, component, clientId,
                alteredComponents);
        return alteredComponents;
    }


    private void setRequiredFalseOnAllChildrenExceptOne(
            UIComponent parent,
            UIComponent componentToAvoid, String clientIdToAvoid,
            Map alteredComponents) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        //turn off required simply with false for all but iterative case
        ValueBinding FALSE_BINDING = facesContext
                .getApplication().createValueBinding("#{false}");

        Iterator kidsAndFacets = parent.getFacetsAndChildren();
        while (kidsAndFacets.hasNext()) {
            UIComponent next = (UIComponent) kidsAndFacets.next();
            if (next instanceof UIInput) {
                UIInput input = (UIInput) next;
                ValueBinding valueBinding =
                        input.getValueBinding(REQUIRED);
                if (null != valueBinding) {
                    ValueBinding replacementBinding = null;
                    if (input == componentToAvoid) {
                        //The component that caused the partialSubmit may
                        //be used iteratively (in a dataTable).  We use
                        //PartialSubmitValueBinding to detect which single
                        //client instance of the component to avoid
                        replacementBinding = new PartialSubmitValueBinding(
                                valueBinding, input, clientIdToAvoid);
                    } else {
                        replacementBinding = FALSE_BINDING;
                    }
                    input.setValueBinding(REQUIRED, replacementBinding);
                    alteredComponents.put(input, valueBinding);
                } else {
                    if (input.isRequired() && input != componentToAvoid &&
                            input.isValid()) {
                        input.setRequired(false);
                        alteredComponents.put(input, null);
                    }
                }
            }
            setRequiredFalseOnAllChildrenExceptOne(next,
                    componentToAvoid, clientIdToAvoid, alteredComponents);
        }
    }


    private UIComponent getContainingForm(UIComponent component) {
        if (null == component) {
            return FacesContext.getCurrentInstance().getViewRoot();
        }
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }

}
