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

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.datatable.DataTable;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import java.util.List;

public class TableFilterEvent extends FacesEvent {
    private Column column;

    public TableFilterEvent(UIComponent component, Column filteredColumn) {
        super(component);
        this.column = filteredColumn;
        this.setPhaseId(PhaseId.INVOKE_APPLICATION);
    }

    @Override
    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }

    @Override
    public void processListener(FacesListener facesListener) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the column of this TableFilterEvent.
     * @return Column that has had it's filter altered
     */
    public Column getColumn() {
        return column;
    }
}