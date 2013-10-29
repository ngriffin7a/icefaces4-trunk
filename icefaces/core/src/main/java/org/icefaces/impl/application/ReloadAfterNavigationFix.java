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

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ReloadAfterNavigationFix extends ConfigurableNavigationHandler {
    private static final Logger log = Logger.getLogger(ReloadAfterNavigationFix.class.getName());
    private static final String NavigationKey = NavigationInfo.class.getName();
    private NavigationHandler handler;

    public ReloadAfterNavigationFix(NavigationHandler handler) {
        this.handler = handler;
    }

    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
        if (handler instanceof ConfigurableNavigationHandler) {
            return ((ConfigurableNavigationHandler) handler).getNavigationCase(context, fromAction, outcome);
        } else {
            log.warning(handler.toString() + " is not a ConfigurableNavigationHandler");
            return null;
        }
    }

    public Map<String, Set<NavigationCase>> getNavigationCases() {
        if (handler instanceof ConfigurableNavigationHandler) {
            return ((ConfigurableNavigationHandler) handler).getNavigationCases();
        } else {
            log.warning(handler.toString() + " is not a ConfigurableNavigationHandler");
            return null;
        }
    }

    public void handleNavigation(FacesContext context, String fromAction, String outcome) {
        if (EnvUtils.isICEfacesView(context) && EnvUtils.isReplayNavigationOnReload(context)) {
            NavigationCase navigationCase = getNavigationCase(context, fromAction, outcome);
            if (navigationCase != null && !navigationCase.isRedirect()) {
                UIViewRoot viewRoot = context.getViewRoot();
                String fromViewId = viewRoot.getViewId();
                Map map = WindowScopeManager.lookupWindowScope(context);
                map.put(NavigationKey, new NavigationInfo(fromViewId, fromAction, outcome, handler));
            }
        }

        handler.handleNavigation(context, fromAction, outcome);
    }

    public static class TriggerNavigation implements PhaseListener {

        public void afterPhase(PhaseEvent event) {
            FacesContext context = event.getFacesContext();
            if (EnvUtils.isReplayNavigationOnReload(context) && !context.isPostback()) {
                Map map = WindowScopeManager.lookupWindowScope(context);
                NavigationInfo navigation = (NavigationInfo) map.get(NavigationKey);
                if (navigation != null) {
                    navigation.navigate(context);
                }
            }
        }

        public void beforePhase(PhaseEvent event) {
        }

        public PhaseId getPhaseId() {
            return PhaseId.RESTORE_VIEW;
        }
    }

    private static class NavigationInfo implements Serializable {
        private String fromViewId;
        private String navigateFrom;
        private String navigateTo;
        private transient NavigationHandler handler;

        private NavigationInfo(String fromViewId, String navigateFrom, String navigateTo, NavigationHandler handler) {
            this.fromViewId = fromViewId;
            this.navigateFrom = navigateFrom;
            this.navigateTo = navigateTo;
            this.handler = handler;
        }

        public void navigate(FacesContext context) {
            if (navigateTo != null && handler != null) {
                context.getViewRoot().setViewId(fromViewId);
                handler.handleNavigation(context, navigateFrom, navigateTo);
            }
        }
    }
}
