/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */

/*
 * Generated, Do Not Modify
 */

package org.icefaces.ace.component.roweditor;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.event.*;
import org.icefaces.ace.model.table.RowState;

import javax.el.MethodExpression;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.el.ValueExpression;
import org.icefaces.resources.ICEResourceDependencies;
import javax.faces.event.AbortProcessingException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@ICEResourceDependencies({

})
public class RowEditor extends RowEditorBase {
    @Override
    public void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        String outcome = null;
        MethodExpression me = null;

        DataTable table = RowEditorRenderer.findParentTable(context, this);
        RowState state = (RowState)(context.getExternalContext().getRequestMap().get(table.getRowStateVar()));

        if (event instanceof RowEditEvent)
            me = getRowEditListener();
        else if (event instanceof RowEditCancelEvent)
            me = getRowEditCancelListener();

        if (me != null) outcome = (String) me.invoke(context.getELContext(), new Object[] {event});

        if (outcome != null) {
            NavigationHandler navHandler = context.getApplication().getNavigationHandler();
            navHandler.handleNavigation(context, null, outcome);
            context.renderResponse();
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        String clientId = getClientId(context);

        //Decode row edit request triggered by this editor
        if (params.containsKey(clientId)) {
            DataTable table = RowEditorRenderer.findParentTable(context, this);
            RowState state = (RowState)(context.getExternalContext().getRequestMap().get(table.getRowStateVar()));
            String tableId = table.getClientId(context);
            tableId = tableId.substring(0, tableId.lastIndexOf(UINamingContainer.getSeparatorChar(context)));

            if (params.containsKey(tableId + "_editSubmit") &&
                    !table.isToggleOnInvalidEdit()) {
                for (Column c : table.getColumns()) {
                    state.removeActiveCellEditor(c.getCellEditor());
                }
            } else if (params.containsKey(tableId + "_editShow")) {
                for (Column c : table.getColumns()) {
                    state.addActiveCellEditor(c.getCellEditor());
                }
            }
        }
    }
}