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
package org.icefaces.ace.component.stackpane;

import org.icefaces.ace.component.panelstack.PanelStack;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PhaseId;
import java.util.*;
import java.util.logging.Logger;

public class StackPane extends StackPaneBase {
    private static final Logger logger = Logger.getLogger(StackPane.class.toString());

    public static final String CONTENT_SELECTED = "ace-contentpane ";
    public static final String CONTENT_HIDDEN = "ace-contentpane-hidden ";
    private Runnable createChildren;

    public void processDecodes(FacesContext context) {
        if (isSelected()) {
            createChildren();
            super.processDecodes(context);
        }
    }

    public void processValidators(FacesContext context) {
        if (isSelected()) {
            super.processValidators(context);
        }
    }

    public void processUpdates(FacesContext context) {
        if (isSelected()) {
            super.processUpdates(context);
        }
    }


    public void processRestoreState(FacesContext context, Object state) {
        if (isSelected()) {
            super.processRestoreState(context, state);
        }
    }

    public Object processSaveState(FacesContext context) {
        if (isSelected()) {
            return super.processSaveState(context);
        } else {
            return null;
        }
    }

    public boolean visitTree(VisitContext context, VisitCallback callback) {
        PhaseId currentPhaseId = context.getFacesContext().getCurrentPhaseId();
        //visit the tree to restore and respectively save the state of the children between JSF lifecycles
        if (context.getHints().contains(VisitHint.SKIP_ITERATION)) {
            return false;
        }
        if (currentPhaseId == PhaseId.RESTORE_VIEW || currentPhaseId == PhaseId.RENDER_RESPONSE) {
            return super.visitTree(context, callback);
        } else {
            if (isSelected()) {
                createChildren();
                return super.visitTree(context, callback);
            }
        }

        return false;
    }

    public boolean isSelected() {
        UIComponent parent = this.getParent();
        while (!(parent instanceof PanelStack) && parent != null) {
            parent = parent.getParent();
        }

        if (parent != null) {
            PanelStack panelStack = (PanelStack) parent;
            String selectedId = panelStack.getSelectedId();
            return getId().equals(selectedId);
        } else {
            logger.warning("StackPane must be nested within a PanelStack: " + this.getClientId());
            return false;
        }
    }

    public void delayChildrenCreation(Runnable createChildren) {
        this.createChildren = createChildren;
    }

    public void createChildren() {
        if (!isClient() && isFacelet()) {
            List children = getChildren();
            if (isSelected()) {
                if (children == null || children.isEmpty()) {
                    FacesContext facesContext = getFacesContext();
                    Map<Object, Object> attributes = facesContext.getAttributes();
                    List cachedChildren = (List) attributes.get(getClientId());
                    this.setInView(false);
                    if (cachedChildren == null) {
                        createChildren.run();
                        attributes.put(getClientId(), new ArrayList(getChildren()));
                    } else {
                        getChildren().addAll(cachedChildren);
                        resetClientId(getChildren());
                    }
                    this.setInView(true);
                }
            } else {
                if (children != null && !children.isEmpty()) {
                    this.setInView(false);
                    Iterator i = new ArrayList(children).iterator();
                    while (i.hasNext()) {
                        Object next = i.next();
                        children.remove(next);
                    }
                    this.setInView(true);
                }
            }
        }
    }

    private void resetClientId(List<UIComponent> children) {
        for (UIComponent c: children) {
            c.setId(c.getId());
            resetClientId(c.getChildren());
        }
    }
}
