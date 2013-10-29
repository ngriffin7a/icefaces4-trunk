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

import org.icefaces.bean.ViewRetained;
import org.icefaces.impl.context.DOMResponseWriter;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <code>PropagatingNavigationHandler</code>  ensures that objects in
 * View Scope are available across navigation.
 */

public class PropagatingNavigationHandler extends ConfigurableNavigationHandler {
    private static Logger log = Logger.getLogger(PropagatingNavigationHandler.class.getName());
    NavigationHandler wrapped;

    public PropagatingNavigationHandler(NavigationHandler wrapped) {
        this.wrapped = wrapped;
    }

    public void handleNavigation(FacesContext context, String fromAction, String outcome) {
        if (!EnvUtils.isICEfacesView(context)) {
            wrapped.handleNavigation(context, fromAction, outcome);
            return;
        }

        UIViewRoot viewRoot;
        Map viewMap;

        viewRoot = context.getViewRoot();
        viewMap = viewRoot.getViewMap();
        HashMap propagated = new HashMap(viewMap);
        Iterator keys = propagated.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            if (!propagated.get(key).getClass()
                    .isAnnotationPresent(ViewRetained.class)) {
                keys.remove();
            } else {
                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "Propagating ViewScoped bean " + key);
                }
            }
        }
        Object oldDOM = viewMap.get(DOMResponseWriter.OLD_DOM);

        wrapped.handleNavigation(context, fromAction, outcome);

        viewRoot = context.getViewRoot();
        NavigationCase navigationCase = getNavigationCase(context, fromAction, outcome);
        if (navigationCase != null && !navigationCase.isRedirect()) {
            viewMap = viewRoot.getViewMap();
            viewMap.putAll(propagated);
            if (null != oldDOM) {
                viewMap.put(DOMResponseWriter.OLD_DOM, oldDOM);
            }
        }
    }

    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
        if (wrapped instanceof ConfigurableNavigationHandler) {
            return ((ConfigurableNavigationHandler) wrapped).getNavigationCase(context, fromAction, outcome);
        } else {
            log.warning(wrapped.toString() + " is not a ConfigurableNavigationHandler");
        }

        return null;
    }

    public Map<String, Set<NavigationCase>> getNavigationCases() {
        if (wrapped instanceof ConfigurableNavigationHandler) {
            return ((ConfigurableNavigationHandler) wrapped).getNavigationCases();
        } else {
            log.warning(wrapped.toString() + " is not a ConfigurableNavigationHandler");
        }
        return null;
    }
}
