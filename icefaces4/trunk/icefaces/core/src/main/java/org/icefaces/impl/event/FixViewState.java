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
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.ArrayList;
import java.util.Map;

/**
 *  In order to ensure that all the forms in a view have the correct ViewState associated with them,
 *  we listen for the PreRenderComponentEvent on forms and store the full client id of the component.
 *  Later, we use that id to render out a set of scripts to be evaluated on the client that "fix" the
 *  ViewState value of all the affected forms.  We need to do it this way because:
 *
 *   - Calling getViewState() in the middle of the render phase leads to problems with the components
 *   - Both Mojarra and MyFaces have difficulty keeping the ViewState values consistent
 *
 *  This is particularly true in more complex scenarios. For example portlets which can have multiple
 *  forms per view and multiple views per page.
 */
public class FixViewState implements SystemEventListener {
    private static final String ID_SUFFIX = "_fixviewstate";
    public static final String FORM_LIST_KEY = "ice.faces.formList";

    public void processEvent(final SystemEvent event) throws AbortProcessingException {

        FacesContext fc = FacesContext.getCurrentInstance();
        if(fc.isPostback()){

            final UIForm form = (UIForm) ((ComponentSystemEvent) event).getComponent();
            final String formClientID = form.getClientId();

            Map facesMap = fc.getAttributes();
            ArrayList formIdList =  (ArrayList)facesMap.get(FORM_LIST_KEY);
            if( formIdList == null ){
                formIdList = new ArrayList();
                facesMap.put(FORM_LIST_KEY, formIdList);
            }
            formIdList.add(formClientID);
        }
    }

    public boolean isListenerForSource(final Object source) {
        if (EnvUtils.isICEfacesView(FacesContext.getCurrentInstance()) && (source instanceof UIForm)) {
            UIForm form = (UIForm) source;
            String componentId = form.getId() + ID_SUFFIX;
            // Guard against duplicates within the same JSF lifecycle
            for (UIComponent comp : form.getParent().getChildren()) {
                if (componentId.equals(comp.getId())) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}