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

package org.icefaces.ace.event;

import org.icefaces.ace.component.column.Column;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

public class DataTableCellClickEvent extends AjaxBehaviorEvent {
    Column column;

    public DataTableCellClickEvent(AjaxBehaviorEvent event, Column column) {
        super(event.getComponent(), event.getBehavior());
        this.column = column;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
