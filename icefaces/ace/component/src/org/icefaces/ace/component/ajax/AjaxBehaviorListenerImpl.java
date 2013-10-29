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

package org.icefaces.ace.component.ajax;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import java.io.Serializable;

public class AjaxBehaviorListenerImpl implements AjaxBehaviorListener, Serializable {
    /**
     * Bean:
     * public Object listener() { ... }
     */
    private MethodExpression noArg;

    /**
     * Bean:
     * public Object listener(ComponentSpecificEvent event) { ... }
     */
    private MethodExpression oneArg;

    /**
     * Bean:
     * public Object listener(javax.faces.event.AjaxBehaviorEvent event) { ... }
     */
    private MethodExpression superArg;

    // Necessary for state saving
    public AjaxBehaviorListenerImpl() {
        super();
    }

    public AjaxBehaviorListenerImpl(MethodExpression noArg, MethodExpression oneArg, MethodExpression superArg) {
        this.noArg = noArg;
        this.oneArg = oneArg;
        this.superArg = superArg;
    }

    public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
        //System.out.println("AjaxBehaviorListenerImpl.processAjaxBehavior()  event: " + event);
        final ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        MethodNotFoundException last = null;
        if (noArg != null) {
            try {
                noArg.invoke(elContext, new Object[0]);
                return;
            } catch (MethodNotFoundException e) {
                //System.out.println("AjaxBehaviorListenerImpl.processAjaxBehavior()  noArg MNFE: " + e.getMessage());
                last = e;
            }
        }
        if (oneArg != null) {
            try {
                oneArg.invoke(elContext, new Object[] {event});
                return;
            } catch (MethodNotFoundException e) {
                //System.out.println("AjaxBehaviorListenerImpl.processAjaxBehavior()  oneArg MNFE: " + e.getMessage());
                last = e;
            }
        }
        if (superArg != null) {
            try {
                superArg.invoke(elContext, new Object[] {event});
                return;
            } catch (MethodNotFoundException e) {
                //System.out.println("AjaxBehaviorListenerImpl.processAjaxBehavior()  superArg MNFE: " + e.getMessage());
                last = e;
            }
        }
        if (last != null) {
            throw last;
        }
    }
}
