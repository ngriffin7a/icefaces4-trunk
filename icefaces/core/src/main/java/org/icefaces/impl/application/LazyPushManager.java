/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LazyPushManager {

    public static boolean enablePush(FacesContext context, String viewID) {
        //If push is configured to be non-lazy either through the context param or the
        //ice:config attribute, then we enable it.
        if(!EnvUtils.isLazyPush(context)){
            return true;
        }

        State state = getState(context);
        if (state.sessionViewsEnabled) {
            return true;
        }

        Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
        return no != null && no > 0;
    }

    public static void enablePushForView(FacesContext context, String viewID) {
        State state = getState(context);
        Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
        if (no == null) {
            state.individualyRegisteredViews.put(viewID, 1);
        } else {
            state.individualyRegisteredViews.put(viewID, ++no);
        }
    }

    public static void disablePushForView(FacesContext context, String viewID) {
        State state = getState(context);
        Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
        if (no != null) {
            --no;

            if (no > 0) {
                state.individualyRegisteredViews.put(viewID, no);
            } else {
                state.individualyRegisteredViews.remove(viewID);
            }
        }
    }

    public static void enablePushForSessionViews(FacesContext context) {
        State state = getState(context);
        state.sessionViewsEnabled = true;
    }

    public static void disablePushForSessionViews(FacesContext context) {
        State state = getState(context);
        state.sessionViewsEnabled = false;
    }

    private static State getState(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        State state = (State) sessionMap.get(LazyPushManager.class.getName());
        if (state == null) {
            state = new State();
            sessionMap.put(LazyPushManager.class.getName(), state);
        }

        return state;
    }

    //it is okay to serialize *static* inner classes: http://java.sun.com/javase/6/docs/platform/serialization/spec/serial-arch.html#7182
    private static class State implements Serializable {
        private boolean sessionViewsEnabled = false;
        private HashMap individualyRegisteredViews = new HashMap();
    }
}
