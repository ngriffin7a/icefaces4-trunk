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

import org.icefaces.ace.component.message.Message;
import org.icefaces.ace.component.messages.Messages;
import org.icefaces.ace.util.ComponentUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.Map;

public class MessageMatcher implements SystemEventListener {
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final UIComponent component = (UIComponent) event.getSource();
        final String target;
        if (component instanceof Message) {
            target = ((Message) component).getFor();
        } else if (component instanceof Messages) {
            target = ((Messages) component).getFor();
        } else {
            throw new FacesException("Unknown message type component");
        }
        if (target == null || target.isEmpty() || "@all".equals(target)) {
            if (component instanceof Message) {
                throw new FacesException("'for' attribute undefined for message component " + component.getId());
            } else {
                final UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                viewRoot.getAttributes().put(Messages.class.getName(), component.getClientId());
            }
        } else {
            final UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
            //search by component ID
            UIComponent c = ComponentUtils.findComponent(viewRoot, target);
            if (c == null) {
                //search by client ID
                c = viewRoot.findComponent(target);
            }
            if (c == null) {
                throw new FacesException("Cannot find component " + target);
            }
            c.getAttributes().put(component.getClass().getName(), component.getClientId());
        }
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof Message || source instanceof Messages;
    }


    static boolean isMultipleMessage(UIComponent validatedComponent){
        final Map<String, Object> attributes = validatedComponent.getAttributes();
        return attributes.containsKey(Messages.class.getName()) || FacesContext.getCurrentInstance().getViewRoot().getAttributes().containsKey(Messages.class.getName());
    }

    static String lookupMessageClientId(UIComponent validatedComponent) {
        final Map<String, Object> componentAttributes = validatedComponent.getAttributes();
        final String id = (String) componentAttributes.get(isMultipleMessage(validatedComponent) ? Messages.class.getName() : Message.class.getName());
        if (id == null) {
            final Map<String, Object> rootAttributes = FacesContext.getCurrentInstance().getViewRoot().getAttributes();
            return (String) rootAttributes.get(Messages.class.getName());
        } else {
            return id;
        }
    }
}
