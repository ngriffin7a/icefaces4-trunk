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

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TabSet extends TabSetBase {
    
    public TabSet() {
    }

    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);
        if (event != null && event instanceof ValueChangeEvent) {
            ValueExpression ve = getValueExpression("selectedIndex");
            if(isCancelOnInvalid()) {
                getFacesContext().renderResponse();
            }

            if (ve != null) {
                try {
                    ve.setValue(getFacesContext().getELContext(), ((ValueChangeEvent)event).getNewValue());
                } catch (ELException ee) {
                    ee.printStackTrace();
                }
            } else {
                setSelectedIndex((Integer)((ValueChangeEvent)event).getNewValue());
            }
            MethodExpression method = getTabChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }
    
    public void queueEvent(FacesEvent event) {
        if (event.getComponent() instanceof TabSet) {
            if (isImmediate() || !isCancelOnInvalid()) {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            }
            else {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
        }
        super.queueEvent(event);
    }

    public void processDecodes(FacesContext context) {
        if (!isRendered()) {
            return;
        }
        setPreDecodeSelectedIndex(getSelectedIndex());
        super.processDecodes(context);
    }

    /**
     * When a tabSet is in server mode, and changes tabs from the old one to
     * the new one, the old one is executed and the new one is rendered. In
     * client mode, if the server has been contacted, they all execute. This
     * method is used by the TabPane(s) to control their execution.
     * @param tabPane
     */
    boolean isExecutingTabPaneContents(FacesContext context, TabPane tabPane) {
        if (this.isClientSide()) {
            return true;
        }
        Integer executeIndex = getPreDecodeSelectedIndex();
//System.out.println("isExecutingTabPaneContents()  executeIndex: " + executeIndex + "  tabPane.clientId: " + tabPane.getClientId(context));
        if (executeIndex == null) {
            return true;
        }
        List<String> tabPaneClientIds = new ArrayList<String>();
        try {
            TabSetRenderer.doTabs(context, this,
                    TabSetRenderer.Do.GET_CLIENT_IDS_ONLY, tabPaneClientIds,
                    null, null);
//System.out.println("isExecutingTabPaneContents()    tabPaneClientIds[executeIndex]: " + tabPaneClientIds.get(executeIndex));
        } catch(IOException e) {
            throw new FacesException("Problem retrieving list of TabPane children of TabSet", e);
        }
        String tabPaneClientId = tabPane.getClientId(context);
        boolean ret = executeIndex >= 0 && executeIndex < tabPaneClientIds.size() &&
                tabPaneClientIds.get(executeIndex).equals(tabPaneClientId);
//System.out.println("isExecutingTabPaneContents()    ret: " + ret);
        return ret;
    }
}
