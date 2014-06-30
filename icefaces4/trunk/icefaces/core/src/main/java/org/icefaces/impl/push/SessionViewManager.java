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

package org.icefaces.impl.push;

import org.icepush.PushContext;

import javax.faces.context.FacesContext;
import javax.portlet.PortletSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SessionViewManager {
    private static final Logger LOGGER = Logger.getLogger(SessionViewManager.class.getName());

    public static SessionViewManager get(final Map applicationMap, final Map sessionMap, final Object session) {
        return new SessionViewManager() {

            protected State getState() {
                if (sessionMap == null) {
                    throw new IllegalStateException("The session was invalidated/expired.");
                }

                State state = (State) sessionMap.get(SessionViewManager.class.getName());
                if (state == null) {
                    if (session instanceof HttpSession) {
                        state = new State(((HttpSession) session).getId());
                    } else if (session instanceof PortletSession) {
                        state = new State(((PortletSession) session).getId());
                    } else {
                        throw new RuntimeException("Unknown session object: " + session);
                    }
                    sessionMap.put(SessionViewManager.class.getName(), state);
                }

                return state;
            }

            protected PushContext getPushContext() {
                return (PushContext) applicationMap.get(PushContext.class.getName());
            }
        };
    }

    public static SessionViewManager get(final FacesContext context) {
         return new SessionViewManager() {

             protected State getState() {
                Map sessionMap = context.getExternalContext().getSessionMap();
                if (sessionMap == null) {
                     throw new IllegalStateException("The session was invalidated/expired.");
                }

                State state = (State) sessionMap.get(SessionViewManager.class.getName());
                if (state == null) {
                    Object session = context.getExternalContext().getSession(true);
                    if (session instanceof HttpSession) {
                        state = new State(((HttpSession) session).getId());
                    } else if (session instanceof PortletSession) {
                        state = new State(((PortletSession) session).getId());
                    } else {
                        throw new RuntimeException("Unknown session object: " + session);
                    }
                    sessionMap.put(SessionViewManager.class.getName(), state);
                }

                return state;
             }

             protected PushContext getPushContext() {
                 return (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
             }
         };
    }

    public static SessionViewManager get(final HttpSession session) {
         return new SessionViewManager() {
             protected State getState() {
                 State state = (State) session.getAttribute(SessionViewManager.class.getName());
                 if (state == null) {
                     if (session instanceof HttpSession) {
                         state = new State(session.getId());
                     } else if (session instanceof PortletSession) {
                         state = new State(((PortletSession) session).getId());
                     } else {
                         throw new RuntimeException("Unknown session object: " + session);
                     }
                     session.setAttribute(SessionViewManager.class.getName(), state);
                 }

                 return state;
             }

             protected PushContext getPushContext() {
                 return PushContext.getInstance(session.getServletContext());
             }
         };
    }

    public void addCurrentSessionToGroup(final String groupName) {
        try {
            startAddingNewViewsToGroup(groupName);
            PushContext pushContext = getPushContext();
            State state = getState();
            Iterator<String> viewIDs = state.viewIDSet.iterator();
            while (viewIDs.hasNext()) {
                pushContext.addGroupMember(groupName, viewIDs.next());
            }
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
        }
    }

    public void addView(String id) {
        try {
            PushContext pushContext = getPushContext();
            State state = getState();
            state.viewIDSet.add(id);
            pushContext.addGroupMember(state.groupName, id);
            Iterator i = state.groups.iterator();
            while (i.hasNext()) {
                pushContext.addGroupMember((String) i.next(), id);
            }
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
        }
    }

    public Set<String> getCurrentSessionViewSet() {
        return Collections.unmodifiableSet(getState().viewIDSet);
    }

    public void removeCurrentSessionFromGroup(final String groupName) {
        try {
            stopAddingNewViewsToGroup(groupName);
            PushContext pushContext = getPushContext();
            State state = getState();
            Iterator<String> viewIDs = state.viewIDSet.iterator();
            while (viewIDs.hasNext()) {
                pushContext.removeGroupMember(groupName, viewIDs.next());
            }
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
        }
    }

    public void removeView(String id) {
        try {
            PushContext pushContext = getPushContext();
            State state = getState();
            state.viewIDSet.remove(id);
            pushContext.removeGroupMember(state.groupName, id);
            Iterator i = state.groups.iterator();
            while (i.hasNext()) {
                pushContext.removeGroupMember((String) i.next(), id);
            }
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
        }
    }

    public void startAddingNewViewsToGroup(String groupName) {
        State state = getState();
        state.groups.add(groupName);
    }

    public  void stopAddingNewViewsToGroup(String groupName) {
        State state = getState();
        state.groups.remove(groupName);
    }

    //it is okay to serialize *static* inner classes: http://java.sun.com/javase/6/docs/platform/serialization/spec/serial-arch.html#7182 
    private static class State implements Serializable {
        private final CopyOnWriteArraySet<String> viewIDSet = new CopyOnWriteArraySet<String>();
        private String groupName;
        private HashSet groups = new HashSet();

        private State(String groupName) {
            this.groupName = groupName;
        }
    }

    protected abstract State getState();

    protected abstract PushContext getPushContext();
}
