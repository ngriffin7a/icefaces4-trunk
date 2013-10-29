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
package org.icefaces.ace.component.expansiontoggler;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.DataModel;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.datatable.DataTableConstants;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.model.table.TreeDataModel;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent(tagName="expansionToggler", value="org.icefaces.ace.component.expansiontoggler.ExpansionToggler")
public class ExpansionTogglerRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        RowState rowState = (RowState) context.getExternalContext().getRequestMap().get("rowState");
        boolean expanded = rowState.isExpanded();
        boolean expandable  = rowState.isExpandable();
        UIComponent parent = component.getParent();
        while (!(parent instanceof DataTable)) { parent = parent.getParent(); }
        DataTable table = (DataTable)parent;
        DataModel model = table.getModel();
        boolean isPanel = (table.getPanelExpansion() != null);
        boolean isRow = table.getRowExpansion() != null;
        boolean hasChildren = true;
        if (table.hasTreeDataModel())
            hasChildren = ((TreeDataModel) model).getCurrentRowChildCount() > 0;

        if (((isPanel && (rowState.getExpansionType() != RowState.ExpansionType.ROW)) || (isRow && hasChildren))
                && expandable) {
            ResponseWriter writer = context.getResponseWriter();
            ExpansionToggler toggler = (ExpansionToggler) component;
            String togglerClass = "ui-icon ";

            if (isPanel && isRow) {
                if (rowState.getExpansionType() == RowState.ExpansionType.PANEL)
                    togglerClass += DataTableConstants.ROW_PANEL_TOGGLER_CLASS;
                else if (rowState.getExpansionType() == RowState.ExpansionType.ROW)
                    togglerClass += DataTableConstants.ROW_TOGGLER_CLASS;
            }
            else if (isPanel) { togglerClass += DataTableConstants.ROW_PANEL_TOGGLER_CLASS; }
            else if (isRow) { togglerClass += DataTableConstants.ROW_TOGGLER_CLASS; }

            if (expanded) togglerClass += " ui-icon-circle-triangle-s";
            else togglerClass += " ui-icon-circle-triangle-e";

            int chironWidth = 0;
            if (isRow) {
                String clientId = table.getClientId(context);                
                for (Character c : clientId.substring(clientId.lastIndexOf(UINamingContainer.getSeparatorChar(context))+1).toCharArray())
                    if (c.equals('.')) chironWidth++;
            }


            writer.startElement("div", null);
            writer.writeAttribute("class", DataTableConstants.ROW_TOGGLER_CONTAINER, null);

            if (chironWidth > 0) {
                writer.startElement("span", null);
                writer.writeAttribute("class", DataTableConstants.ROW_TOGGLER_SPAN, null);
                writer.writeAttribute("style", "width:"+ chironWidth * 15  + "px", null);
                writer.endElement("span");
            }

            writer.startElement("a", toggler);
            writer.writeAttribute("tabindex", "0", null);
            writer.writeAttribute("class", togglerClass, null);
            writer.endElement("a");

            writer.endElement("div");
        }
    }
}
