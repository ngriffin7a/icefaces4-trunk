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
package org.icefaces.ace.component.pagepanel;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.PassThruAttributeWriter;
import org.icefaces.ace.util.Utils;


public class PagePanelRenderer extends CoreRenderer {

    private static final Logger logger = Logger.getLogger(PagePanelRenderer.class.getName());

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        PagePanel pagePanel = (PagePanel) component;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);

        // render a top level div and apply the style and style class pass through attributes
        writer.startElement(HTML.DIV_ELEM, pagePanel);

        writer.writeAttribute(HTML.ID_ATTR, clientId + "_pgPnl", HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-pagePanel", null);
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, pagePanel, pagePanel.getCommonAttributeNames());

        if (pagePanel.getStyle()!=null){
            writer.writeAttribute(HTML.STYLE_ATTR, pagePanel.getStyle(), HTML.STYLE_ATTR);
        }

        // find out if header and/or footer facets are present as this will directly
        // effect which style classes to apply
        UIComponent headerFacet = pagePanel.getFacet(PagePanel.HEADER_FACET);
        UIComponent bodyFacet = pagePanel.getFacet(PagePanel.BODY_FACET);
        UIComponent footerFacet = pagePanel.getFacet(PagePanel.FOOTER_FACET);
        String userDefStyle = pagePanel.getStyleClass();

        if (headerFacet == null && bodyFacet == null && footerFacet == null) {
            logger.warning("PagePanel header, body and footer were not defined, " +
                            "no content will be rendered by this component.");
        }

        String headerClass = getHeaderClass(pagePanel, userDefStyle);
        String bodyClass = getBodyClass(pagePanel, userDefStyle, headerFacet, footerFacet);
        String footerClass = getFooterClass(pagePanel, userDefStyle);
        String headerFooterContentsClass = PagePanel.CTR_CLASS + (userDefStyle != null ? " " + userDefStyle : "");

        // write header if present
        if (headerFacet != null) {
            writer.startElement(HTML.DIV_ELEM, pagePanel);
            writer.writeAttribute(HTML.CLASS_ATTR, headerClass, HTML.CLASS_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_pgPnlHdr", HTML.ID_ATTR);
            writer.startElement(HTML.DIV_ELEM, component);
            writer.writeAttribute(HTML.CLASS_ATTR, headerFooterContentsClass, HTML.STYLE_CLASS_ATTR);

            if (headerFacet.isRendered())
                headerFacet.encodeAll(facesContext);

            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.DIV_ELEM);
        }

        /// write body with no-footer or no-header considerations
        if (bodyFacet != null) {
            // build out style class depending on header footer visibility
            writer.startElement(HTML.DIV_ELEM, pagePanel);
            writer.writeAttribute(HTML.CLASS_ATTR, bodyClass, HTML.CLASS_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_pgPnlBdy", HTML.ID_ATTR);

            if (bodyFacet.isRendered())
                bodyFacet.encodeAll(facesContext);

            writer.endElement(HTML.DIV_ELEM);
        }

        // write footer f present
        if (footerFacet != null) {
            writer.startElement(HTML.DIV_ELEM, pagePanel);
            writer.writeAttribute(HTML.CLASS_ATTR, footerClass, HTML.CLASS_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId + "_pgPnlFtr", HTML.ID_ATTR);
            writer.startElement(HTML.DIV_ELEM, component);
            writer.writeAttribute(HTML.CLASS_ATTR, headerFooterContentsClass, HTML.STYLE_CLASS_ATTR);

            if (footerFacet.isRendered())
                footerFacet.encodeAll(facesContext);

            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.DIV_ELEM);
        }

        // close the top level div
        writer.endElement(HTML.DIV_ELEM);
    }

    private String getFooterClass(PagePanel pagePanel, String userDefStyle) {
        return PagePanel.FOOTER_CLASS +
               (Utils.getClientDescriptor().isSupportsFixedPosition() ? " ui-footer-fixed" : "") +
               (userDefStyle != null ? " " + userDefStyle : "");
    }

    private String getBodyClass(PagePanel pagePanel, String userDefStyle,  UIComponent headerFacet, UIComponent footerFacet) {
        return PagePanel.BODY_CLASS +
               (userDefStyle != null ? " " + userDefStyle : "") +
               (headerFacet == null ? " " + PagePanel.BODY_NO_HEADER_CLASS : "") +
               (footerFacet == null ? " " + PagePanel.BODY_NO_FOOTER_CLASS : "");
    }

    private String getHeaderClass(PagePanel panel, String userDefStyle) {
        return PagePanel.HEADER_CLASS +
               (Utils.getClientDescriptor().isSupportsFixedPosition() ? " ui-header-fixed" : "") +
               (userDefStyle != null ? " " + userDefStyle : "");
    }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
