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

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.component.AttributeConstants;
//import com.icesoft.faces.component.UIXhtmlComponent;
import com.icesoft.faces.context.DOMContext;
import org.icefaces.impl.util.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;


public class TextRenderer extends DomBasicInputRenderer {

    private static final String output_passThruAttributes[] = AttributeConstants.getAttributes(AttributeConstants.H_OUTPUTTEXT);
    //private static final String input_passThruAttributes[] = AttributeConstants.getAttributes(AttributeConstants.H_INPUTTEXT);
    //handled onmousedown
    private static final String input_passThruAttributes[] = new String[]{ HTML.ACCESSKEY_ATTR,  HTML.ALT_ATTR,  HTML.DIR_ATTR,  HTML.LANG_ATTR,  HTML.MAXLENGTH_ATTR,  HTML.ONBLUR_ATTR,  HTML.ONCHANGE_ATTR,  HTML.ONCLICK_ATTR,  HTML.ONDBLCLICK_ATTR,  HTML.ONFOCUS_ATTR,  HTML.ONKEYDOWN_ATTR,  HTML.ONKEYPRESS_ATTR,  HTML.ONKEYUP_ATTR,  HTML.ONMOUSEMOVE_ATTR,  HTML.ONMOUSEOUT_ATTR,  HTML.ONMOUSEOVER_ATTR,  HTML.ONMOUSEUP_ATTR,  HTML.ONSELECT_ATTR,  HTML.SIZE_ATTR,  HTML.STYLE_ATTR,  HTML.TABINDEX_ATTR,  HTML.TITLE_ATTR };                        
           
    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) {
        validateParameters(facesContext, uiComponent, null);
    }

    protected void renderEnd(FacesContext facesContext, UIComponent uiComponent,
                             String currentValue)
            throws IOException {
        validateParameters(facesContext, uiComponent, null);
        if (isRenderingAsInput(uiComponent)) {
            renderUIInput(facesContext, uiComponent, currentValue);
        } else if (isRenderingAsOutput(uiComponent)) {
            renderUIOutput(facesContext, uiComponent, currentValue);
        }
    }

    protected boolean isRenderingAsInput(UIComponent uiComponent) {
        return (uiComponent instanceof UIInput);
    }

    protected boolean isRenderingAsOutput(UIComponent uiComponent) {
        return (uiComponent instanceof UIOutput);
    }

    /**
     * @param facesContext
     * @param uiComponent
     * @param currentValue
     * @throws IOException
     */
    private void renderUIOutput(FacesContext facesContext,
                                UIComponent uiComponent,
                                String currentValue)
            throws IOException {

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        boolean requiresContainingSpan = requiresContainingSpan(uiComponent);

        if (requiresContainingSpan) {
            renderContainingSpan(facesContext, uiComponent, domContext);
        } else {
            renderTextNode(domContext);
        }

        if (currentValue == null) {
            // We need to set the data in the text node to the empty string.
            // The text node can be either the root node or it can be
            // the first child of the root node in the case where we
            // rendered a containing span
            Text textNode = null;
            if (requiresContainingSpan) {
                textNode = (Text) domContext.getRootNode()
                        .getFirstChild();
            } else {
                textNode = ((Text) domContext.getRootNode());
            }
            if (textNode != null) {
                textNode.setData("");
            }
        } else {
            renderCurrentValue(uiComponent, currentValue, domContext,
                               requiresContainingSpan);
        }
    }

    /**
     * @param domContext
     */
    private void renderTextNode(DOMContext domContext) {
        if (!domContext.isInitialized()) {
            Node root = domContext.createTextNode("");
            domContext.setRootNode(root);
        } else if (!(domContext.getRootNode() instanceof Text)) {
            // Need to switch from a root span to a root text node.
            // This type of change can occur when the binding attribute is defined
            // and the model has altered one of the attributes requiring a span 
            // from non-null to null.
            domContext.getRootNode().getParentNode()
                    .removeChild(domContext.getRootNode());
            domContext.setRootNode(domContext.createTextNode(""));
        }
    }

    /**
     * @param facesContext
     * @param uiComponent
     * @param domContext
     */
    private void renderContainingSpan(FacesContext facesContext,
                                      UIComponent uiComponent,
                                      DOMContext domContext) {
        if (!domContext.isInitialized()) {
            Node root = domContext.createElement(HTML.SPAN_ELEM);
            domContext.setRootNode(root);
        } else if (!(domContext.getRootNode() instanceof Element)) {
            // if we require a span but the existing root node is not a span then we 
            // need to switch from a text node at the root to a span at the root
            // This type of change can occur when the binding attribute is defined
            // and the model has altered one of the attributes requiring a span 
            // from null to some non-null value
            domContext.getRootNode().getParentNode()
                    .removeChild(domContext.getRootNode());
            domContext.setRootNode(domContext.createElement(HTML.SPAN_ELEM));
        }
        Element rootSpan = (Element) domContext.getRootNode();
        setRootElementId(facesContext, rootSpan, uiComponent);
        // render styleClass as the value of the class attribute;
        // leave the style to be passed through
        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            ((Element) rootSpan).setAttribute("class", styleClass);
        }
        PassThruAttributeRenderer
                .renderHtmlAttributes(facesContext, uiComponent, output_passThruAttributes);
    }

    /**
     * @param uiComponent
     * @param currentValue
     * @param domContext
     * @param requiresContainingSpan
     */
    private void renderCurrentValue(UIComponent uiComponent,
                                    String currentValue,
                                    DOMContext domContext,
                                    boolean requiresContainingSpan) {

        // escape 
        boolean valueTextRequiresEscape =
                DOMUtils.escapeIsRequired(uiComponent);
        if (valueTextRequiresEscape) {
            currentValue = DOMUtils.escapeAnsi(currentValue);
        }

        // Avoid severing and recreating the node
        Node rootNode = domContext.getRootNode();
        if (requiresContainingSpan) {
            domContext.setCursorParent(rootNode);
            if (rootNode.getFirstChild() != null
                && rootNode.getFirstChild() instanceof Text) {
                ((Text) rootNode.getFirstChild()).setData(currentValue);
            } else {
                Text text = domContext.getDocument()
                        .createTextNode(currentValue);
                rootNode.appendChild(text);
            }
        } else {
            ((Text) rootNode).setData(currentValue);
        }
    }

    /**
     * @param facesContext
     * @param uiComponent
     * @param currentValue
     * @throws IOException
     */
    private void renderUIInput(FacesContext facesContext,
                               UIComponent uiComponent,
                               String currentValue)
            throws IOException {

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        if (!domContext.isInitialized()) {
            Element root = domContext.createElement("input");
            domContext.setRootNode(root);
            setRootElementId(facesContext, root, uiComponent);
            root.setAttribute("type", "text");
            root.setAttribute("name", uiComponent.getClientId(facesContext));
        }
        Element root = (Element) domContext.getRootNode();

        String bidi = (String) uiComponent.getAttributes().get("dir");
        if (bidi != null) {
            root.setAttribute("dir", bidi);
        }

        if (currentValue != null) {
            root.setAttribute("value", currentValue);
        } else {
            root.removeAttribute("value");
        }

        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            root.setAttribute("class", styleClass);
        }
        
        addJavaScript(facesContext, uiComponent, root, currentValue);

        String mousedownScript = (String)uiComponent.getAttributes().get(HTML.ONMOUSEDOWN_ATTR);
        root.setAttribute(HTML.ONMOUSEDOWN_ATTR, combinedPassThru(mousedownScript, "this.focus();"));
    }

    /**
     * We require a containing span if either styleClass or style attributes are
     * present, or there is an id to accommodate.
     *
     * @param uiComponent
     * @return boolean
     */
    private boolean requiresContainingSpan(UIComponent uiComponent) {

        // special case for title element
        UIComponent parent = uiComponent.getParent();
        //TODO: decide if we need UIXhtmlComponent
//        if (parent != null && parent instanceof UIXhtmlComponent) {
//            String tag = ((UIXhtmlComponent) parent).getTag();
//            if (tag != null && tag.equalsIgnoreCase("title")) {
//                return false;
//            }
//        }

        Boolean nospan = (Boolean) uiComponent.getAttributes().get("nospan");
        if (nospan != null && nospan.booleanValue()) return false;

        String style = (String) uiComponent.getAttributes().get("style");
        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        String title = (String) uiComponent.getAttributes().get("title");
        if (styleClass != null
            || style != null
            || title != null) {
            return true;
        }
        if (idNotNull(uiComponent) && !uiComponent.getId().startsWith("_")) {
            return true;
        }
        return false;
    }

    protected void addJavaScript(FacesContext facesContext,
                                 UIComponent uiComponent, Element root,
                                 String currentValue) {
                                 
        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, input_passThruAttributes);

    }
}

