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
package org.icefaces.ace.component.splitpane;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.Utils;
import org.icefaces.ace.renderkit.CoreRenderer;

public class SplitPaneRenderer extends CoreRenderer {
    private static final Logger logger = Logger.getLogger(SplitPaneRenderer.class.getName());
    private static final String JS_NAME = "splitpane.js";
    private static final String JS_MIN_NAME = "splitpane-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.splitpane";

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        SplitPane pane = (SplitPane)uiComponent;
        UIComponent leftFacet = pane.getFacet(SplitPane.LEFT_FACET);
        UIComponent rightFacet = pane.getFacet(SplitPane.RIGHT_FACET);
        if ( leftFacet ==null && rightFacet ==null){
           logger.warning("This component may ONLY have  " +
                   "both the left and right facets together");
            return;
        }
        SplitPaneCoreRenderer renderer = new SplitPaneCoreRenderer();
        ResponseWriter writer = facesContext.getResponseWriter();
        renderer.encodeBegin(pane, writer);
        if ((leftFacet!=null) && (rightFacet !=null)){
           writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        }
        renderer.encodePane(pane, writer, "left");
        Utils.renderChild(facesContext, leftFacet);
        renderer.encodePaneEnd(writer);
        renderer.encodeColumnDivider(pane, writer) ;
        renderer.encodePane(pane, writer, "right");
        Utils.renderChild(facesContext, rightFacet);
        renderer.encodePaneEnd(writer);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component)
        throws IOException{
        SplitPane pane = (SplitPane)component;
        SplitPaneCoreRenderer renderer = new SplitPaneCoreRenderer();
        ResponseWriter writer = facesContext.getResponseWriter();
        renderer.encodeEnd(pane, writer);
    }
    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeBegin and encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void encodeScript(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        ResponseWriter writer = facesContext.getResponseWriter();
        SplitPane pane = (SplitPane) uiComponent;
        String clientId = pane.getClientId(facesContext);
        writer.startElement("span", uiComponent);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-hidden", null);
        writer.startElement("script", uiComponent);
        writer.writeAttribute("type", "text/javascript", null);
        StringBuilder sb = new StringBuilder("ice.mobi.splitpane.initClient('").append(clientId).append("'");
        sb.append(",{ scrollable: '").append(pane.isScrollable()).append("'");
     //   sb.append(", resize: ").append(pane.isResizable()); not yet implemented.
        int width = pane.getColumnDivider();
        sb.append(",width: '").append(width).append("'");
        sb.append("});");
        writer.write(sb.toString());
        writer.endElement("script");
        writer.endElement("span");
    }
}
