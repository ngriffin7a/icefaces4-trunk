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

package com.icesoft.faces.component.panelcollapsible;

import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.util.CoreUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class PanelCollapsibleRenderer extends DomBasicRenderer {
    private static final String[] passThruAttributes =
            ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_PANELCOLLAPSIBLE);

    private static final Log log = LogFactory.getLog(PanelCollapsibleRenderer.class);


    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        PanelCollapsible panelCollapsible = (PanelCollapsible) uiComponent;
        DOMContext domContext = DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element rootSpan = domContext.createElement(HTML.DIV_ELEM);
            domContext.setRootNode(rootSpan);
            setRootElementId(facesContext, rootSpan, uiComponent);
        }
        Element root = (Element) domContext.getRootNode();
        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);
        root.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getStyleClass());

        //create "header" div and append to the parent, don't render any children yet
        Element header = (Element) domContext.createElement(HTML.DIV_ELEM);
        header.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getHeaderClass());
        root.appendChild(header);

        //create "contents" div and append to the parent, don't render any children yet        
        Element contents = (Element) domContext.createElement(HTML.DIV_ELEM);
        contents.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getContentClass());
		contents.setAttribute(HTML.STYLE_ATTR, "clear: both;"); // ICE-7058
        root.appendChild(contents);

        //add click handler if not disabled and toggleOnClick is set to true
        if (panelCollapsible.isToggleOnClick() &&
                !panelCollapsible.isDisabled()) {
            Element hiddenField = domContext.createElement(HTML.INPUT_ELEM);
            hiddenField.setAttribute(HTML.NAME_ATTR, uiComponent.getClientId(facesContext) + "Expanded");
            hiddenField.setAttribute(HTML.TYPE_ATTR, "hidden");
            hiddenField.setAttribute(HTML.AUTOCOMPLETE_ATTR, "off");
            root.appendChild(hiddenField);
            hiddenField = domContext.createElement(HTML.INPUT_ELEM);
            String clickedNodeName = uiComponent.getClientId(facesContext) + "ClickedNodeName";
            hiddenField.setAttribute(HTML.NAME_ATTR, clickedNodeName);
            hiddenField.setAttribute(HTML.TYPE_ATTR, "hidden");
            hiddenField.setAttribute(HTML.AUTOCOMPLETE_ATTR, "off");
            root.appendChild(hiddenField);
            UIComponent form = findForm(uiComponent);
            if (form == null) {
                throw new FacesException("PanelCollapsible must be contained within a form");
            }
            if (panelCollapsible.hasInitiatedSubmit(facesContext)) {
                JavascriptContext.addJavascriptCall(facesContext,
                        "document.getElementById('" + uiComponent.getClientId(facesContext) + "')." +
                                "getElementsByTagName('a')[0].focus();");
            }
            String hiddenValue = "document.forms['" + form.getClientId(facesContext) + "']" +
                    "['" + uiComponent.getClientId(facesContext) + "Expanded" + "'].value='";
            header.setAttribute(HTML.ONCLICK_ATTR,
                    hiddenValue +
                            panelCollapsible.isExpanded() + "'; " +
                            "var target = (event.target) ? event.target : event.srcElement; " +
                            "document.forms['" + form.getClientId(facesContext) + "']['" + clickedNodeName + "'].value=target.nodeName; " +
                            "iceSubmit(document.forms['" + form.getClientId(facesContext) + "'],this,event);" +
                            hiddenValue + "'; return false;");
            header.setAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext) + "hdr");
            Element div = domContext.createElement(HTML.DIV_ELEM);
            div.setAttribute(HTML.STYLE_ATTR, "padding:0px;background-image:none;width:100%;");
            header.appendChild(div);
            //this anchor should be known by the component only, so we are defining style to the component level
            Element anchor = domContext.createElement(HTML.ANCHOR_ELEM);
            anchor.setAttribute(HTML.ONFOCUS_ATTR, "Ice.pnlClpFocus(this);");
            anchor.setAttribute(HTML.ONBLUR_ATTR, "Ice.pnlClpBlur(this);");
            anchor.setAttribute(HTML.STYLE_ATTR, "float:left;border:none;margin:0px;");
            anchor.setAttribute(HTML.HREF_ATTR, "#");
            String tabindex = panelCollapsible.getTabindex();
            if (tabindex != null) {
                anchor.setAttribute(HTML.TABINDEX_ATTR, tabindex);
            }
            anchor.appendChild(domContext.createTextNodeUnescaped("<img src=\"" + CoreUtils.resolveResourceURL(facesContext,
                    "/xmlhttp/css/xp/css-images/spacer.gif") + "\" alt=\"\"/>"));
            div.appendChild(anchor);
        }

    }


    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, null);
        PanelCollapsible panelCollapsible = (PanelCollapsible) uiComponent;
        DOMContext domContext = DOMContext.getDOMContext(facesContext, uiComponent);

        //if headerfacet found, get the header div and render all its children
        UIComponent headerFacet = uiComponent.getFacet("header");
        if (headerFacet != null) {
            Element header = null;
            if (panelCollapsible.isToggleOnClick() &&
                    !panelCollapsible.isDisabled()) {
                header = (Element) domContext.getRootNode().getFirstChild().getFirstChild();
            } else {
                header = (Element) domContext.getRootNode().getFirstChild();
            }
            domContext.setCursorParent(header);
            CustomComponentUtils.renderChild(facesContext, headerFacet);
        }

        //if expanded get the content div and render all its children 
        if (panelCollapsible.isExpanded()) {
            Element contents = (Element) domContext.getRootNode().getFirstChild().getNextSibling();
            domContext.setCursorParent(contents);
            if (uiComponent.getChildCount() > 0) {
                Iterator children = uiComponent.getChildren().iterator();
                while (children.hasNext()) {
                    UIComponent nextChild = (UIComponent) children.next();
                    if (nextChild.isRendered()) {
                        encodeParentAndChildren(facesContext, nextChild);
                    }
                }
            }
        }
        domContext.stepOver();
    }
}

