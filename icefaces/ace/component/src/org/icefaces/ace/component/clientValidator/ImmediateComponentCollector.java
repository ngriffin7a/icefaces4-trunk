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

package org.icefaces.ace.component.clientValidator;

import org.icefaces.util.JavaScriptRunner;

import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ImmediateComponentCollector implements SystemEventListener {


    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        ArrayList<String> immediateComponents = new ArrayList();
        ArrayList<UIComponent> queue = new ArrayList();


        FacesContext context = FacesContext.getCurrentInstance();
        queue.add(context.getViewRoot());
        //collect components marked with immediate="true"
        while (!queue.isEmpty()) {
            UIComponent component = queue.remove(0);
            Iterator<UIComponent> kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                queue.add(kids.next());
            }

            if (isImmediate(component)) {
                immediateComponents.add(component.getClientId());
            }
        }

        //define ice.ace.immediateComponents arrays with the list of 'immediate' components
        if (!immediateComponents.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("window.ice.ace.immediateComponents = [");
            for (Iterator<String> iterator = immediateComponents.iterator(); iterator.hasNext(); ) {
                String next = iterator.next();
                buffer.append("'");
                buffer.append(next);
                buffer.append("'");
                if (iterator.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append("];");

            JavaScriptRunner.runScript(context, buffer.toString());
        }
    }

    private boolean isImmediate(UIComponent component) {
        boolean immediate = false;

        if (component instanceof EditableValueHolder) {
            immediate = ((EditableValueHolder) component).isImmediate();
        } else if (component instanceof ActionSource) {
            immediate = ((ActionSource) component).isImmediate();
        }

        return immediate;
    }

}
