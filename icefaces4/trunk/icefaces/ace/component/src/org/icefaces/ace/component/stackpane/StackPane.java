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
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.*;
import java.util.logging.Logger;

public class StackPane extends StackPaneBase {
    private static final Logger logger = Logger.getLogger(StackPane.class.toString());

    public static final String CONTENT_SELECTED = "ace-contentpane ";
    public static final String CONTENT_HIDDEN = "ace-contentpane-hidden ";
    private Runnable createChildren;
    private Map childState = new HashMap();

    public void processDecodes(FacesContext context) {
        if (isSelected()) {
            createChildren();
            restoreChildrenState();
            super.processDecodes(context);
            saveChildrenState();
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
        if (currentPhaseId == PhaseId.RESTORE_VIEW || currentPhaseId == PhaseId.RENDER_RESPONSE) {
            return super.visitTree(context, callback);
        } else {
            if (isSelected()) {
                createChildren();
                boolean result = super.visitTree(context, callback);
                saveChildrenState();
                return result;
            }
        }

        return false;
    }

    //save children state during JSF lifecycle to allow the child state to be restored in case of re-initialisation
    public void saveChildrenState(List<UIComponent> children) {
        for (UIComponent c: children) {
            String id = c.getClientId();
            Object state = c.saveState(getFacesContext());
            childState.put(id, state);
            saveChildrenState(c.getChildren());
        }
    }

    public void saveChildrenState() {
        saveChildrenState(getChildren());
    }

    public void restoreChildrenState(List<UIComponent> children) {
        for (UIComponent c: children) {
            String id = c.getClientId();
            Object state = childState.get(id);
            if (state != null) {
                c.restoreState(getFacesContext(), state);
            }
            restoreChildrenState(c.getChildren());
        }
    }

    public void restoreChildrenState() {
        restoreChildrenState(getChildren());
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
                    this.setInView(false);
                    createChildren.run();
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

    public Object saveState(FacesContext context) {
        Object[] state = new Object[2];
        state[0] = childState;
        state[1] = super.saveState(context);
        return state;
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] restoredState = (Object[]) state;
        childState = (Map) restoredState[0];
        super.restoreState(context, restoredState[1]);
    }
}
