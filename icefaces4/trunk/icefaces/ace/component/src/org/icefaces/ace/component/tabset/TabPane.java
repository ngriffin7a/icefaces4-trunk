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

package org.icefaces.ace.component.tabset;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class TabPane extends TabPaneBase {
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        // Process all relevant facets and children of this component
        Iterator kids = getExecuteIterator(context);
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processDecodes(context);
        }

        // Process this component itself
        try {
            decode(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        } finally {
            popComponentFromEL(context);
        }
    }


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void processValidators(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        Application app = context.getApplication();
        app.publishEvent(context, PreValidateEvent.class, this);
        // Process all relevant facets and children of this component
        Iterator kids = getExecuteIterator(context);
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processValidators(context);
        }
        app.publishEvent(context, PostValidateEvent.class, this);
        popComponentFromEL(context);
    }


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void processUpdates(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        // Process all relevant facets and children of this component
        Iterator kids = getExecuteIterator(context);
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processUpdates(context);

        }
        popComponentFromEL(context);
    }


    protected Iterator getExecuteIterator(FacesContext context) {
        Iterator kids;
        if (allowPhaseToAll(context)) {
            kids = getFacetsAndChildren();
        } else {
            if (getFacetCount() > 0) {
                kids = getFacets().values().iterator();
            } else {
                kids = (new ArrayList<UIComponent>()).iterator();
            }
        }
        return kids;
    }

    protected boolean allowPhaseToAll(FacesContext context) {
        TabSet tabSet = getTabSet();
        if (tabSet == null) {
            return true;
        }
        return tabSet.isExecutingTabPaneContents(context, this);
    }

    protected TabSet getTabSet() {
        UIComponent parent = getParent();
        while (parent != null && !(parent instanceof TabSet)) {
            parent = parent.getParent();
        }
        return (TabSet) parent;
    }
}
