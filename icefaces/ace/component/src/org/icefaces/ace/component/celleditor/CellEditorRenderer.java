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
package org.icefaces.ace.component.celleditor;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.ace.component.datatable.DataTableConstants;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent(tagName="cellEditor", value="org.icefaces.ace.component.celleditor.CellEditor")
public class CellEditorRenderer extends CoreRenderer {
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CellEditor editor = (CellEditor) component;

        String rowStateVar = getRowStateVar(editor);

        RowState rowState = (RowState) context.getExternalContext().getRequestMap().get(rowStateVar);
        List<String> selectedEditorIds = rowState.getActiveCellEditorIds();

        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTR, component.getClientId(context), null);
        if (selectedEditorIds.contains(editor.getId()))
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-state-highlight " + DataTableConstants.CELL_EDITOR_CLASS, null);
        else
            writer.writeAttribute(HTML.CLASS_ATTR, DataTableConstants.CELL_EDITOR_CLASS, null);

        writer.startElement(HTML.SPAN_ELEM, null);
        if (!selectedEditorIds.contains(editor.getId())) {
            editor.getFacet("output").encodeAll(context);
        } else {
            editor.getFacet("input").encodeAll(context);
        }
        writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    @Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//Rendering happens on encodeEnd
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}


    private DataTable findParentTable(CellEditor editor) {
        UIComponent parent = editor.getParent();

        while (parent != null)
            if (parent instanceof DataTable) return (DataTable) parent;
            else parent = parent.getParent();

        return null;
    }

    private String getRowStateVar(CellEditor editor) {
        DataTable table = findParentTable(editor);

        return table.getRowStateVar();
    }
}
