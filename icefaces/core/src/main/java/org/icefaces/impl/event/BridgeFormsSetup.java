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

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.List;

public class BridgeFormsSetup implements SystemEventListener {
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent c = (UIComponent) event.getSource();
        String viewID = BridgeSetup.getViewID(context.getExternalContext());
        //add the form used by ice.retrieveUpdate function to retrieve the updates
        //use viewID and '-retrieve-update' suffix as element ID
        addNewTransientForm(viewID + "-retrieve-update", c);
        //add the form used by ice.singleSubmit function for submitting event data
        //use viewID and '-single-submit' suffix as element ID
        addNewTransientForm(viewID + "-single-submit", c);
    }

    public boolean isListenerForSource(final Object source) {
        return EnvUtils.isICEfacesView(FacesContext.getCurrentInstance()) && (source instanceof UIComponent) &&
                "javax.faces.Body".equals(((UIComponent) source).getRendererType());
    }

    private static void addNewTransientForm(String id, UIComponent parent) {
        if( parent.findComponent(id) == null ){
            UIForm uiForm = new ShortIdForm();
            uiForm.setTransient(true);
            uiForm.setId(id);
            //disable capture submit for this form
            uiForm.getAttributes().put(FormSubmit.DISABLE_CAPTURE_SUBMIT, FormSubmit.DISABLE_CAPTURE_SUBMIT);
            parent.getChildren().add(uiForm);
        }
    }

    public static class ShortIdForm extends UIForm {
        //ID is assigned uniquely by ICEpush so no need to prepend
        public String getClientId(FacesContext context) {
            return getId();
        }
    }
}
