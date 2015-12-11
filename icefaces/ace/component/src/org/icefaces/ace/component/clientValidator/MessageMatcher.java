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

import org.icefaces.ace.component.growlmessages.GrowlMessages;
import org.icefaces.ace.component.growlmessages.GrowlMessagesRenderer;
import org.icefaces.ace.component.message.Message;
import org.icefaces.ace.component.messages.Messages;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;

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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MessageMatcher implements SystemEventListener {
    private static final String MESSAGE_MAP = MessageMatcher.class.getName() + ".messageMap";
    private static final String LABEL_MAP = MessageMatcher.class.getName() + ".labelMap";
    private static final String ALL = "@all";
    private static final String IN_VIEW = "@inView";

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final UIComponent component = (UIComponent) event.getSource();
        final Map<String, Object> messagesMap = getMap(MESSAGE_MAP);
        if (component instanceof Message) {
            final String target = ((Message) component).getFor();
            if (target == null || target.isEmpty() || ALL.equals(target)) {
                throw new FacesException("'for' attribute undefined for message component " + component.getId());
            } else {
                messagesMap.put(target, new MessageEntry(component));
            }
        } else if (component instanceof Messages) {
            final String target = ((Messages) component).getFor();
            if (target == null || target.isEmpty() || ALL.equals(target)) {
                messagesMap.put(ALL, new MessagesEntry(component));
            } else if (IN_VIEW.equals(target)) {
                messagesMap.put(IN_VIEW, new MessagesEntry(component));
            } else {
                messagesMap.put(target, new MessagesEntry(component));
            }
        } else if (component instanceof GrowlMessages) {
            final String target = ((GrowlMessages) component).getFor();
            final String configuration = GrowlMessagesRenderer.generateGrowlOptionsScript((GrowlMessages) component);
            if (target == null || target.isEmpty() || ALL.equals(target)) {
                messagesMap.put(ALL, new GrowlEntry(component, configuration));
            } else if (IN_VIEW.equals(target)) {
                messagesMap.put(IN_VIEW, new GrowlEntry(component, configuration));
            } else {
                messagesMap.put(target, new GrowlEntry(component, configuration));
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
        return source instanceof Message || source instanceof Messages || source instanceof GrowlMessages || source instanceof HtmlOutputLabel;
    }

    private static Entry lookupByIdOrClientId(UIComponent component, Map messagesMap) {
        Entry id = (Entry) messagesMap.get(component.getId());
        if (id == null) {
            return (Entry) messagesMap.get(component.getClientId());
        } else {
            return id;
        }
    }

    static String lookupMessageConfig(UIComponent validatedComponent) {
        final Map<String, Object> messageMap = getMap(MESSAGE_MAP);

        final Entry entry = lookupByIdOrClientId(validatedComponent, messageMap);
        if (entry != null && entry.isInComponentTree()) {
            return entry.getMessageConfiguration();
        }

        final Entry inViewEntry = (Entry) messageMap.get(IN_VIEW);
        if (inViewEntry != null && inViewEntry.isInComponentTree()) {
            final List idsInView = ComponentUtils.findIdsInView(FacesContext.getCurrentInstance());
            if (idsInView.contains(validatedComponent.getClientId())) {
                return inViewEntry.getMessageConfiguration();
            }
        }

        final Entry allEntry = (Entry) messageMap.get(ALL);
        if (allEntry != null && allEntry.isInComponentTree()) {
            return allEntry.getMessageConfiguration();
        }

        throw new FaceletException("Cannot find message/s component assigned to " + validatedComponent.getId());
    }

    static String lookupLabel(UIComponent validatedComponent) {
        String label;
        //try finding label attribute
        try {
            Method m = validatedComponent.getClass().getMethod("getLabel");
            label = (String) m.invoke(validatedComponent);
        } catch (NoSuchMethodException e) {
            label = null;
        } catch (InvocationTargetException e) {
            label = null;
        } catch (IllegalAccessException e) {
            label = null;
        }
        //try finding a referencing h:outputLabel
        if (label == null || label.isEmpty()) {
            final Map<String, Object> labelMap = getMap(LABEL_MAP);
            label = (String) labelMap.get(validatedComponent.getId());
            if (label == null) {
                label = (String) labelMap.get(validatedComponent.getClientId());
            }
            if (label == null) {
                //fallback to using Id as the label
                label = validatedComponent.getId();
            }
        }

        return label;
    }

    private static Map<String, Object> getMap(String type) {
        Map attributes = FacesContext.getCurrentInstance().getViewRoot().getAttributes();
        Map messageMap = (Map) attributes.get(type);
        if (messageMap == null) {
            messageMap = new HashMap();
            attributes.put(type, messageMap);
        }
        return (Map) messageMap;
    }

    private interface Entry {
        boolean isInComponentTree();
        String getMessageConfiguration();
    }

    private class MessageEntry implements Entry {
        private String clientId;

        public MessageEntry(UIComponent component) {
            this.clientId = component.getClientId();
        }

        public boolean isInComponentTree() {
            return FacesContext.getCurrentInstance().getViewRoot().findComponent(clientId) != null;
        }

        public String getMessageConfiguration() {
            return "{aceMessage: true, id: '" + clientId + "'}";
        }
    }

    private class MessagesEntry implements Entry {
        private String clientId;

        public MessagesEntry(UIComponent component) {
            this.clientId = component.getClientId();
        }
        public boolean isInComponentTree() {
            return FacesContext.getCurrentInstance().getViewRoot().findComponent(clientId) != null;
        }

        public String getMessageConfiguration() {
            return "{aceMessages: true, id: '" + clientId + "'}";
        }
    }

    private class GrowlEntry implements Entry {
        private String clientId;
        private String configuration;

        public GrowlEntry(UIComponent component, String configuration) {
            this.clientId = component.getClientId();
            this.configuration = configuration;
        }

        public boolean isInComponentTree() {
            return FacesContext.getCurrentInstance().getViewRoot().findComponent(clientId) != null;
        }

        public String getMessageConfiguration() {
            return "{aceGrowlMessages: true, id: '" + clientId + "', configuration: " + configuration + "}";
        }
    }
}
