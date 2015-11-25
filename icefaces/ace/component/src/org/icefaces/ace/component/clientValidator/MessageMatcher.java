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
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseId;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.view.facelets.FaceletException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MessageMatcher implements SystemEventListener {
    private static final String MESSAGE_MAP = MessageMatcher.class.getName() + ".messageMap";
    private static final String MESSAGES_MAP = MessageMatcher.class.getName() + ".messagesMap";
    private static final String LABEL_MAP = MessageMatcher.class.getName() + ".labelMap";
    private static final String ALL = "@all";

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final UIComponent component = (UIComponent) event.getSource();
        if (component instanceof Message) {
            final String target = ((Message) component).getFor();
            if (target == null || target.isEmpty() || ALL.equals(target)) {
                throw new FacesException("'for' attribute undefined for message component " + component.getId());
            } else {
                getMap(MESSAGE_MAP).put(target, component.getClientId());
            }
        } else if (component instanceof Messages) {
            final String target = ((Messages) component).getFor();
            if (target == null || target.isEmpty() || ALL.equals(target)) {
                getMap(MESSAGES_MAP).put(ALL, component.getClientId());
            } else {
                getMap(MESSAGES_MAP).put(target, component.getClientId());
            }
        } else if (component instanceof HtmlOutputLabel) {
            final String target = ((HtmlOutputLabel) component).getFor();
            if (target != null && !target.isEmpty()) {
                String label = (String) ((HtmlOutputLabel) component).getValue();
                getMap(LABEL_MAP).put(target, label);
            }
        }
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof Message || source instanceof Messages || source instanceof HtmlOutputLabel;
    }

    static boolean isMultipleMessage(UIComponent validatedComponent) {
        final Map messageMap = getMap(MESSAGE_MAP);
        if (messageMap.containsKey(validatedComponent.getId()) || messageMap.containsKey(validatedComponent.getClientId())) {
            return false;
        } else {
            final Map messagesMap = getMap(MESSAGES_MAP);
            return messagesMap.containsKey(validatedComponent.getId()) || messagesMap.containsKey(validatedComponent.getClientId()) || messagesMap.containsKey(ALL);
        }
    }

    static String lookupMessageClientId(UIComponent validatedComponent) {
        String id;
        if (isMultipleMessage(validatedComponent)) {
            final Map<String, String> messagesMap = getMap(MESSAGES_MAP);
            id = messagesMap.get(validatedComponent.getId());
            if (id == null) {
                id = messagesMap.get(validatedComponent.getClientId());
            }
        } else {
            final Map<String, String>  messageMap = getMap(MESSAGE_MAP);
            id = messageMap.get(validatedComponent.getId());
            if (id == null) {
                id = messageMap.get(validatedComponent.getClientId());
            }
        }
        //use the catch all ace:messages component if the is one
        if (id == null) {
            final Map<String, String> messagesMap = getMap(MESSAGES_MAP);
            String allMessagesId = messagesMap.get(ALL);
            if (allMessagesId == null) {
                throw new FaceletException("Cannot find message/s component assigned to " + validatedComponent.getId());
            } else {
                return allMessagesId;
            }
        } else {
            return id;
        }
    }

    static String lookupLabel(UIComponent validatedComponent) {
        String label;
        //try finding label attribute
        try {
            Method m = validatedComponent.getClass().getMethod("getLabel");
            label = (String) m.invoke(validatedComponent);
        } catch (NoSuchMethodException e) {
            label = "";
        } catch (InvocationTargetException e) {
            label = "";
        } catch (IllegalAccessException e) {
            label = "";
        }
        //try finding a referencing h:outputLabel
        if (label == null || label.isEmpty()) {
            final Map<String, String> labelMap = getMap(LABEL_MAP);
            label = labelMap.get(validatedComponent.getId());
            if (label == null) {
                label = labelMap.get(validatedComponent.getClientId());
            }
            if (label == null) {
                label = "";
            }
        }

        return label;
    }

    private static Map<String, String> getMap(String type) {
        Map attributes = FacesContext.getCurrentInstance().getViewRoot().getAttributes();
        Map messageMap = (Map) attributes.get(type);
        if (messageMap == null) {
            messageMap = new HashMap();
            attributes.put(type, messageMap);
        }
        return (Map) messageMap;
    }
}
