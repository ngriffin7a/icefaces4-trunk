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

package com.icesoft.faces.component.ext;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * Created by IntelliJ IDEA. User: rmayhew Date: Sep 5, 2006 Time: 2:29:29 PM To
 * change this template use File | Settings | File Templates.
 */
public class ClickActionEvent extends ActionEvent {

    private int row;
    private boolean dblClick;
    private RowSelectorEvent rowSelectorEvent = null;
    
    public ClickActionEvent(UIComponent uiComponent, int row, int clickCount) {
        super(uiComponent);
        this.row = row;
        this.dblClick = clickCount == 2 ? true : false;
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }

    public void processListener(FacesListener facesListener) {
    }

    public int getRow() {
        return row;
    }

    public boolean isDblClick() {
        return dblClick;
    }
    
    public RowSelectorEvent getRowSelectorEvent() {
        return rowSelectorEvent;
    }
    
    public void setRowSelectorEvent(RowSelectorEvent rowSelectorEvent) {
        this.rowSelectorEvent = rowSelectorEvent;
    }
}
