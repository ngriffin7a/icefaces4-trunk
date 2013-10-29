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

package org.icefaces.ace.component.tableconfigpanel;


import org.icefaces.ace.component.datatable.DataTable;

import javax.faces.FacesException;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
public class TableConfigPanel extends TableConfigPanelBase {
    // Find component cache
    private DataTable table;

    public DataTable getTargetedDatatable() {
        if (this.table != null) return table;

        String target = this.getFor();
        if (target == null) {
            // If nested in table
            UIComponent parent = this;
            while ((parent = parent.getParent()) != null)
                if (parent instanceof DataTable) {
                    ((DataTable) parent).setTableConfigPanel(this);
                    this.table = (DataTable)parent;
                    return (DataTable)parent;
                }
            throw new FacesException("TableConfigPanel: Must be nested within a DataTable or target one using the 'for' attribute.");
        }

        DataTable table = (DataTable)this.findComponent(target);

        if (table == null) throw new FacesException("TableConfigPanel: DataTable with clientId determined by 'for' attribute value '" + target + "' could not be found.");

       // table.setTableConfigPanel(this);
        this.table = table;
        return table;
    }

    public void setInView(boolean isInView) {
        getTargetedDatatable().setTableConfigPanel(this);
        super.setInView(isInView);
    }
}
