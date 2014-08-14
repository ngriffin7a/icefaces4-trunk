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

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class LazyPushManager {
    private static final Logger LOGGER = Logger.getLogger(LazyPushManager.class.getName());

    public static LazyPushManager get(final HttpSession session) {
        return new LazyPushManager() {
            protected State getState() {
                State state = (State) session.getAttribute(LazyPushManager.class.getName());
                if (state == null) {
                    state = new State();
                    try {
                        session.setAttribute(LazyPushManager.class.getName(), state);
                    } catch (UnsupportedOperationException e) {
                        LOGGER.fine("Cannot update LazyPushManager's state, session has expired.");
                    }
                }

                return state;
            }
        };
    }

    public static LazyPushManager get(final Map sessionMap) {
        return new LazyPushManager() {
            protected State getState() {
                if (sessionMap == null) {
                    throw new IllegalStateException("The session was invalidated/expired.");
                }
                State state = (State) sessionMap.get(LazyPushManager.class.getName());
                if (state == null) {
                    state = new State();
                    try {
                        sessionMap.put(LazyPushManager.class.getName(), state);
                    } catch (UnsupportedOperationException e) {
                        LOGGER.fine("Cannot update LazyPushManager's state, session has expired.");
                    }
                }

                return state;
            }
        };
    }

    public static LazyPushManager get(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        return get(sessionMap);
    }

    public boolean enablePush(FacesContext context, String viewID) {
        try {
            //If push is configured to be non-lazy either through the context param or the
            //ice:config attribute, then we enable it.
            if(!EnvUtils.isLazyPush(context)){
                return true;
            }

            State state = getState();
            if (state.sessionViewsEnabled) {
                return true;
            }

            Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
            return no != null && no > 0;
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
            return false;
        }
    }

    public void enablePushForView(String viewID) {
        try {
            State state = getState();
            Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
            if (no == null) {
                state.individualyRegisteredViews.put(viewID, 1);
            } else {
                state.individualyRegisteredViews.put(viewID, ++no);
            }
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
        }
    }

    public void disablePushForView(String viewID) {
        try {
            State state = getState();
            Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
            if (no != null) {
                --no;

                if (no > 0) {
                    state.individualyRegisteredViews.put(viewID, no);
                } else {
                    state.individualyRegisteredViews.remove(viewID);
                }
            }
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
        }
    }

    public void enablePushForSessionViews() {
        try {
            State state = getState();
            state.sessionViewsEnabled = true;
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
        }
    }

    public void disablePushForSessionViews() {
        try {
            State state = getState();
            state.sessionViewsEnabled = false;
        } catch (IllegalStateException e ) {
            LOGGER.fine("The session was invalidated/expired.");
        }
    }


    //it is okay to serialize *static* inner classes: http://java.sun.com/javase/6/docs/platform/serialization/spec/serial-arch.html#7182
    private static class State implements Serializable {
        private boolean sessionViewsEnabled = false;
        private HashMap individualyRegisteredViews = new HashMap();
    }

    protected abstract State getState();
}
