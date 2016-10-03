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

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;

import javax.faces.application.StateManager;
import javax.faces.application.ViewExpiredException;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.*;
import javax.faces.render.ResponseStateManager;
import java.io.IOException;
import java.util.*;


public class SessionStateManagerSetup implements SystemEventListener {
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (EnvUtils.isCustomStateManager(context)) {
            context.getApplication().setStateManager(new SessionStateManager(EnvUtils.getCustomStateManagerMaxViews(context)));
        }
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    static class SessionStateManager extends StateManager {
        private final static String viewStateCounterKey = "com.sun.faces.util.ViewStateCounterKey";
        private final static Random random = new Random();
        private int maxActiveViews;

        public SessionStateManager(int maxActiveViews) {
            this.maxActiveViews = maxActiveViews;
        }

        public void writeState(FacesContext context, Object state) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            String viewStateParam = ResponseStateManager.VIEW_STATE_PARAM;

            writer.startElement("input", null);
            writer.writeAttribute("type", "hidden", null);
            writer.writeAttribute("name", viewStateParam, null);
            String viewStateId = getViewStateId(context);
            writer.writeAttribute("id", viewStateId, null);
            writer.writeAttribute("value", getViewState(context), null);
            writer.writeAttribute("autocomplete", "off", null);
            writer.endElement("input");
        }


        public String getViewState(FacesContext context) {
            String viewState = (String) context.getViewRoot().getAttributes().get(this.getClass().getName());
            if (viewState == null) {
                //acquire the view ID from the request parameter, if possible
                return context.getExternalContext().getRequestParameterMap().get(ResponseStateManager.VIEW_STATE_PARAM);
            } else {
                return viewState;
            }
        }

        public Object saveView(FacesContext context) {
            if (getViewState(context) == null) {
                //create new view when no view state key can be found
                String viewStateKey = generateID();
                context.getViewRoot().getAttributes().put(this.getClass().getName(), viewStateKey);
                mapping().put(viewStateKey, new Entry(viewStateKey, context.getViewRoot()));

            }

            discardOldViews();
            return context.getViewRoot();
        }

        public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {
            String viewStateKey = getViewState(context);
            Entry entry = (Entry) mapping().get(viewStateKey);
            if (entry == null) {
                //no view can be matched, view was expired
                throw new ViewExpiredException();
            }
            entry.timestamp();
            return discardTransientComponents(entry);
        }

        private void discardOldViews() {
            Map<String, Entry> mapping = mapping();
            TreeSet<Entry> orderedEntries = new TreeSet(mapping.values());
            int overflow = orderedEntries.size() - maxActiveViews;
            if (overflow > 0) {
                int i = 0;
                for (Entry e: orderedEntries) {
                    if (i < overflow) {
                        mapping.remove(e.viewState);
                    }
                    i++;
                }
            }
        }

        private Map<String, Entry> mapping() {
            FacesContext context = FacesContext.getCurrentInstance();
            Map sessionMap = context.getExternalContext().getSessionMap();
            Map mapping = (Map) sessionMap.get(this.getClass().getName());
            if (mapping == null) {
                mapping = new HashMap();
                sessionMap.put(this.getClass().getName(), mapping);
            }

            return mapping;
        }

        private String generateID() {
            return Long.toString(Math.abs(random.nextLong()), 36) + ":" + Long.toString(System.currentTimeMillis(), 36);
        }

        static String getViewStateId(FacesContext context) {
            Map<Object, Object> contextAttrs = context.getAttributes();
            Integer counter = (Integer) contextAttrs.get(viewStateCounterKey);
            if (null == counter) {
                counter = Integer.valueOf(0);
            }

            char sep = UINamingContainer.getSeparatorChar(context);
            UIViewRoot root = context.getViewRoot();
            String result = root.getContainerClientId(context) + sep + ResponseStateManager.VIEW_STATE_PARAM + sep + counter;
            contextAttrs.put(viewStateCounterKey, ++counter);

            return result;
        }

        private static UIViewRoot discardTransientComponents(Entry entry) {
            LinkedList<UIComponent> queue = new LinkedList();
            UIViewRoot view = entry.view;
            queue.add(view);
            while (!queue.isEmpty()) {
                UIComponent c = queue.removeFirst();
                queue.addAll(c.getChildren());

                if (c.isTransient()) {
                    c.getParent().getChildren().remove(c);
                }
            }

            for (SystemEventListener s : view.getViewListenersForEventClass(PostAddToViewEvent.class)) {
                if (s.getClass().getName().contains("DynamicAddRemoveListener")) {
                    view.unsubscribeFromViewEvent(PostAddToViewEvent.class, s);
                }
            }
            for (SystemEventListener s : view.getViewListenersForEventClass(PreRemoveFromViewEvent.class)) {
                if (s.getClass().getName().contains("DynamicAddRemoveListener")) {
                    view.unsubscribeFromViewEvent(PreRemoveFromViewEvent.class, s);
                }
            }

            return view;
        }
    }

    static class Entry implements Comparable {
        private String viewState;
        private UIViewRoot view;
        private long timestamp;

        public Entry(String viewState, UIViewRoot view) {
            this.viewState = viewState;
            this.view = view;
            this.timestamp = System.currentTimeMillis();
        }

        public int compareTo(Object o) {
            Entry e = (Entry) o;
            return timestamp >= e.timestamp ? 1 : -1;
        }

        public void timestamp() {
            timestamp = System.currentTimeMillis();
        }
    }
}
