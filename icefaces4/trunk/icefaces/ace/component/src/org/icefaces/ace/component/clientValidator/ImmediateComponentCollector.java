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

import javax.faces.component.*;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ImmediateComponentCollector implements SystemEventListener {

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        ArrayList<ImmediateEntry> immediateComponents = new ArrayList();
        ArrayList<UIComponent> queue = new ArrayList();


        FacesContext context = FacesContext.getCurrentInstance();
        queue.add(context.getViewRoot());
        //collect components marked with immediate="true"
        while (!queue.isEmpty()) {
            UIComponent component = queue.remove(0);
            Iterator<UIComponent> kids = component.getFacetsAndChildren();
            while (kids.hasNext()) {
                queue.add(kids.next());
            }

            ImmediateEntry entry = isImmediate(component);
            if (entry != null) {
                immediateComponents.add(entry);
            }
        }

        //define ice.ace.immediateComponents arrays with the list of 'immediate' components
        if (!immediateComponents.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("window.ice.ace.immediateComponents = [");
            for (Iterator<ImmediateEntry> iterator = immediateComponents.iterator(); iterator.hasNext(); ) {
                iterator.next().toJSEntry(buffer);
                if (iterator.hasNext()) {
                    buffer.append(", ");
                }
            }
            buffer.append("];");

            JavaScriptRunner.runScript(context, buffer.toString());
        }
    }

    private ImmediateEntry isImmediate(UIComponent component) {
        if (component instanceof EditableValueHolder && ((EditableValueHolder) component).isImmediate()) {
            return new ImmediateEntry(component.getClientId());
        }

        if (component instanceof ActionSource && ((ActionSource) component).isImmediate()) {
            return new ImmediateEntry(component.getClientId());
        }

        if (component instanceof UIInput && ((UIInput) component).isImmediate()) {
            return new ImmediateEntry(component.getClientId());
        }

        if (component instanceof UICommand && ((UICommand) component).isImmediate()) {
            return new ImmediateEntry(component.getClientId());
        }

        if (component instanceof UIComponentBase) {
            Set<Map.Entry<String, List<ClientBehavior>>> clientBehaviors = ((UIComponentBase) component).getClientBehaviors().entrySet();
            for (Map.Entry<String, List<ClientBehavior>> entry : clientBehaviors) {
                List<ClientBehavior> behaviors = entry.getValue();
                for (ClientBehavior behavior : behaviors) {
                    if ((behavior instanceof AjaxBehavior && ((AjaxBehavior) behavior).isImmediate()) ||
                            (behavior instanceof org.icefaces.ace.component.ajax.AjaxBehavior && ((org.icefaces.ace.component.ajax.AjaxBehavior) behavior).isImmediate())) {
                        return new ImmediateEntry(component.getClientId(), entry.getKey());
                    }
                }
            }
        }


        try {
            final Method isImmediateMethod = component.getClass().getMethod("isImmediate");
            if (((Boolean) isImmediateMethod.invoke(component)).booleanValue()) {
                return new ImmediateEntry(component.getClientId());
            }
        } catch (NoSuchMethodException e) {
            //could not find the 'isImmediate' method on this component
        } catch (InvocationTargetException e) {
            //could not find the 'isImmediate' method on this component
        } catch (IllegalAccessException e) {
            //could not find the 'isImmediate' method on this component
        } catch (ClassCastException e) {
            //could not find the 'isImmediate' method on this component
        }

        return null;
    }

    private static class ImmediateEntry {
        private String clientId;
        private String event;

        public ImmediateEntry(String clientId) {
            this.clientId = clientId;
        }

        public ImmediateEntry(String clientId, String event) {
            this.clientId = clientId;
            this.event = event;
        }

        public void toJSEntry(StringBuffer buffer) {
            buffer.append("['");
            buffer.append(clientId);
            buffer.append("'");
            if (event == null) {
                buffer.append("]");
            } else {
                buffer.append(", '");
                buffer.append(event);
                buffer.append("']");
            }
        }
    }
}
