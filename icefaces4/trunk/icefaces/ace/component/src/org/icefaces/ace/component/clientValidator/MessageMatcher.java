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
import org.icefaces.ace.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class MessageMatcher implements SystemEventListener {
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final Message message = (Message) event.getSource();
        final UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        final String id = message.getFor();
        final UIComponent c = ComponentUtils.findComponent(viewRoot, id);
        c.getAttributes().put(Message.class.getName(), message.getClientId());
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof Message;
    }
}
