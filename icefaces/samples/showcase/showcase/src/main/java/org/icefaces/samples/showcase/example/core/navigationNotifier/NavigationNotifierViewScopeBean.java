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

package org.icefaces.samples.showcase.example.core.navigationNotifier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.view.ViewScoped;
import java.awt.event.ComponentEvent;
import java.io.Serializable;

@ManagedBean(name = NavigationNotifierViewScopeBean.BEAN_NAME)
@ViewScoped
public class NavigationNotifierViewScopeBean implements Serializable {
    public static final String BEAN_NAME = "navigationNotifierViewScopeBean";

    public NavigationNotifierViewScopeBean() {
        app = FacesContext.getCurrentInstance().getApplication();
        app.subscribeToEvent(PreValidateEvent.class, resetState);
	}

    private boolean navigationDetected;
    private Application app;
    private SystemEventListener resetState = new SystemEventListener() {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            navigationDetected = false;
        }

        public boolean isListenerForSource(Object source) {
            return source instanceof UIViewRoot;
        }
    };

    public void navigationDetected() {
        navigationDetected = true;
    }

    public boolean getNavigationDetected() {
        return navigationDetected;
    }

    public String getNavigateBackURI() {
        final FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getViewHandler().getResourceURL(context, "/showcase.jsf?grp=aceMenu&exp=navigationNotifierBean");
    }

    @PreDestroy
    public void reset() {
        app.unsubscribeFromEvent(PreValidateEvent.class, resetState);
    }
}
