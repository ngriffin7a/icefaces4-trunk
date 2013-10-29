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

package com.icesoft.faces.component.commandsortheader;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.renderkit.CommandLinkRenderer;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class CommandSortHeaderRenderer extends CommandLinkRenderer {
    
    /*
     *  (non-Javadoc)
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, null);
        //Render if user is in given role
        if (Util.isEnabledOnUserRole(uiComponent)) {
            CommandSortHeader sortHeader = (CommandSortHeader) uiComponent;
            HtmlDataTable dataTable = sortHeader.findParentDataTable();

            Node child = null;
            DOMContext domContext =
                    DOMContext.getDOMContext(facesContext, uiComponent);
            Element root = (Element) domContext.getRootNode();
            String headerClass = sortHeader.getStyleClass();
            if (headerClass != null) {
                root.setAttribute(HTML.CLASS_ATTR, headerClass);
            }
            if (sortHeader.getColumnName().equals(dataTable.getSortColumn())) {
                child = root.getFirstChild();
                if (dataTable.isSortAscending()) {
                    headerClass += "Asc";
                } else {
                    headerClass += "Desc";
                }
                if (child != null) {
                    if (child.getNodeType() == 1) { //span
                        child = child.getFirstChild();
                    }
                    String value = child.getNodeValue();
                    Element table = domContext.createElement(HTML.TABLE_ELEM);
                    Element tr = domContext.createElement(HTML.TR_ELEM);
                    table.appendChild(tr);
                    Element textTd = domContext.createElement(HTML.TD_ELEM);
                    textTd.appendChild(domContext.createTextNodeUnescaped(value));
                    Element arrowTd = domContext.createElement(HTML.TD_ELEM);
                    tr.appendChild(textTd);
                    tr.appendChild(arrowTd);
                    Element arrowDiv = domContext.createElement(HTML.DIV_ELEM);
                    arrowDiv.setAttribute(HTML.CLASS_ATTR, headerClass);
                    arrowDiv.setAttribute("valign", "middle");
                    arrowTd.appendChild(arrowDiv);
                    child.setNodeValue("");
                    child.getParentNode().appendChild(table);                   
                }
            }
        }
        super.encodeEnd(facesContext, uiComponent);
    }
}
