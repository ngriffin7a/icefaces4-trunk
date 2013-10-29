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

package org.icefaces.ace.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import java.util.List;
import java.util.Map;

public class ExpansionChangeEvent extends FacesEvent {
    private Object rowData;
    private boolean expanded;
    private List<Map.Entry<Object, List>> rowChildren = null;

    public ExpansionChangeEvent(UIComponent component, Object object, boolean expanded) {
        super(component);
        this.expanded = expanded;
        this.rowData = object;
    }

    public ExpansionChangeEvent(UIComponent component, Object object, boolean expanded, List<Map.Entry<Object, List>> children) {
        super(component);
        this.rowData = object;
        this.expanded = expanded;
        this.rowChildren = children;
    }

    public Object getRowData() {
        return rowData;
    }

    public void setRowData(Object rowData) {
        this.rowData = rowData;
    }

    public List<Map.Entry<Object, List>> getRowChildren() {
        return rowChildren;
    }

    public void setRowChildren(List<Map.Entry<Object, List>> rowChildren) {
        this.rowChildren = rowChildren;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }

    @Override
    public void processListener(FacesListener facesListener) {
        throw new UnsupportedOperationException();
    }
}
