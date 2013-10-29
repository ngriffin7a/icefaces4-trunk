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

package com.icesoft.spring.security;

import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.View;
import org.icefaces.impl.event.BridgeSetup;
import org.icefaces.impl.application.WindowScopeManager;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 *  WebflowListener to check to duplicate the missing
 *  functionality provided by AssignViewID PhaseListener
 */
public class WebflowListener extends FlowExecutionListenerAdapter {

    private Method mAssignViewId;
    private final static Logger log = Logger.getLogger(WebflowListener.class.getName());

    public WebflowListener() {

        try {
            mAssignViewId = BridgeSetup.class.
            getDeclaredMethod("assignViewID",
                                     new Class[]{javax.faces.context.ExternalContext.class});
            mAssignViewId.setAccessible( true );

        } catch (Throwable t) {
            log.severe("Exception finding BridgeSetup.assignViewId method: " + t);
        }
    }
    /**
     * Called when a view is about to render in a view-state, before any render actions are executed.
     * @param context the current flow request context
     * @param view the view that is about to render
     * @param viewState the current view state
     */
    public void viewRendering(RequestContext context, View view, StateDefinition viewState) {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String viewId = BridgeSetup.getViewID(ec);
        if (viewId == null ) {
            WindowScopeManager.determineWindowID( fc );
            if (mAssignViewId != null) {
                try {
                    mAssignViewId.invoke( BridgeSetup.class,
                                                new Object[] { ec }  );
                } catch (Throwable t) {
                    log.severe("Exception executing assignViewId: " + t);
                }
            }
        }
    }
}