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
    private static final String MESSAGES_MAP = MessageMatcher.class.getName() + ".messagesMap";
    private static final String GROWL_MESSAGES_MAP = MessageMatcher.class.getName() + ".growlMessagesMap";
    private static final String GROWL_MESSAGES_CONFIGURATION_MAP = MessageMatcher.class.getName() + ".growlMessagesConfigurationMap";
    private static final String LABEL_MAP = MessageMatcher.class.getName() + ".labelMap";
    private static final String ALL = "@all";
    private static final String IN_VIEW = "@inView";

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
        } else if (component instanceof GrowlMessages) {
            final String target = ((GrowlMessages) component).getFor();
            final String configuration = GrowlMessagesRenderer.generateGrowlOptionsScript((GrowlMessages) component);
            if (target == null || target.isEmpty() || ALL.equals(target)) {
                getMap(GROWL_MESSAGES_MAP).put(ALL, component.getClientId());
                getMap(GROWL_MESSAGES_CONFIGURATION_MAP).put(ALL, configuration);
            } else if (IN_VIEW.equals(target)) {
                getMap(GROWL_MESSAGES_MAP).put(IN_VIEW, component.getClientId());
                getMap(GROWL_MESSAGES_CONFIGURATION_MAP).put(IN_VIEW, configuration);
            } else {
                getMap(GROWL_MESSAGES_MAP).put(target, component.getClientId());
                getMap(GROWL_MESSAGES_CONFIGURATION_MAP).put(target, configuration);
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

    private static String lookupByIdOrClientId(UIComponent component, String mapType) {
        final Map<String, String> messagesMap = getMap(mapType);
        String id = messagesMap.get(component.getId());
        if (id == null) {
            return messagesMap.get(component.getClientId());
        } else {
            return id;
        }
    }

    static String lookupMessageConfig(UIComponent validatedComponent) {
        String id = lookupByIdOrClientId(validatedComponent, MESSAGE_MAP);
        if (id != null) {
            return "{aceMessage: true, id: '" + id + "'}";
        }
        id = lookupByIdOrClientId(validatedComponent, MESSAGES_MAP);
        if (id != null) {
            return "{aceMessages: true, id: '" + id + "'}";
        }
        id = lookupByIdOrClientId(validatedComponent, GROWL_MESSAGES_MAP);
        if (id != null) {
            String configuration = lookupByIdOrClientId(validatedComponent, GROWL_MESSAGES_CONFIGURATION_MAP);
            return "{aceGrowlMessages: true, id: '" + id + "', configuration: " + configuration + "}";
        }

        //use the catch all ace:messages component if there is one
        if (id == null) {
            final Map<String, String> messagesMap = getMap(MESSAGES_MAP);
            final String allMessagesId = messagesMap.get(ALL);

            if (allMessagesId == null) {
                final String messagesForInViewId = messagesMap.get(IN_VIEW);
                if (messagesForInViewId != null) {
                    final List idsInView = ComponentUtils.findIdsInView(FacesContext.getCurrentInstance());
                    if (idsInView.contains(validatedComponent.getClientId())) {
                        return "{aceMessages: true, id: '" + messagesForInViewId + "'}";
                    }
                }

                final Map<String, String> growlMap = getMap(GROWL_MESSAGES_MAP);
                final String growlMessagesForAllId = growlMap.get(ALL);
                if (growlMessagesForAllId != null) {
                    String configuration = getMap(GROWL_MESSAGES_CONFIGURATION_MAP).get(ALL);
                    return "{aceGrowlMessages: true, id: '" + growlMessagesForAllId + "', configuration: " + configuration + "}";
                }

                final String growlMessagesForInViewId = growlMap.get(IN_VIEW);
                if (growlMessagesForInViewId != null) {
                    final List idsInView = ComponentUtils.findIdsInView(FacesContext.getCurrentInstance());
                    if (idsInView.contains(validatedComponent.getClientId())) {
                        String configuration = getMap(GROWL_MESSAGES_CONFIGURATION_MAP).get(IN_VIEW);
                        return "{aceGrowlMessages: true, id: '" + growlMessagesForInViewId + "', configuration: " + configuration + "}";
                    }
                }
            } else {
                return "{aceMessages: true, id: '" + allMessagesId + "'}";
            }
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
            final Map<String, String> labelMap = getMap(LABEL_MAP);
            label = labelMap.get(validatedComponent.getId());
            if (label == null) {
                label = labelMap.get(validatedComponent.getClientId());
            }
            if (label == null) {
                //fallback to using Id as the label
                label = validatedComponent.getId();
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
