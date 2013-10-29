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

package com.icesoft.faces.component.menubar;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.PORTLET_CSS_DEFAULT;
import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.faces.component.menupopup.MenuPopup;

import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class MenuBarRenderer extends DomBasicRenderer {
    private static final String[] passThruAttributes =
            ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_MENUBAR);

    public static final String PATH_DELIMITER = "-";

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, MenuBar.class);

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            domContext.createRootElement(HTML.DIV_ELEM);
        }
        Element menuDiv = (Element) domContext.getRootNode();
        menuDiv.setAttribute(HTML.ID_ATTR,
                             uiComponent.getClientId(facesContext));
        MenuBar menuComponent = (MenuBar) uiComponent;

        if (!(uiComponent instanceof MenuPopup) && (!((MenuBar)uiComponent).isDisplayOnClick())) {
            menuDiv.setAttribute(HTML.ONMOUSEOUT_ATTR, "Ice.Menu.hideOnMouseOut('" + uiComponent.getClientId(facesContext) + "',event);");
        }

        String defaultStyle = menuComponent.getComponentRootStyle();
        if (MenuBar.ORIENTATION_VERTICAL.equalsIgnoreCase(
                menuComponent.getOrientation())){
            defaultStyle+=CSS_DEFAULT.MENU_BAR_VERTICAL_SUFFIX_STYLE;
        }
        
        String styleClass = menuComponent.getStyleClass();
        menuDiv.setAttribute(HTML.CLASS_ATTR, CoreUtils.
        		addPortletStyleClassToQualifiedClass(styleClass, defaultStyle, PORTLET_CSS_DEFAULT.PORTLET_MENU));
        String style = menuComponent.getStyle();
        if(style != null && style.length() > 0)
            menuDiv.setAttribute(HTML.STYLE_ATTR, style);
        else
            menuDiv.removeAttribute(HTML.STYLE_ATTR);
        DOMContext.removeChildren(menuDiv);

        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);

        domContext.stepInto(uiComponent);
        
        trailingEncodeBegin(facesContext, uiComponent);
    }

    /**
     * For subclasses to add in any required rendering, without having to
     * override the whole encodeBegin(-)
     * @param facesContext
     * @param uiComponent
     */
    protected void trailingEncodeBegin(FacesContext facesContext, UIComponent uiComponent) {
    }

    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        for (int i = 0; i < component.getChildCount(); i++) {
            encodeParentAndChildren(context, (UIComponent) component
                    .getChildren().get(i));
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        super.encodeEnd(facesContext, uiComponent);
        String call = ((MenuBar)uiComponent).getJsCall(facesContext);
        if (call != null) {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
            writer.write(call);
            writer.endElement(HTML.SCRIPT_ELEM);
        }

        recursivelyRemoveTransientChildren(uiComponent.getParent(), uiComponent);
    }

    /**
     * It's not sufficient to merely not state save the transient
     * components, like the built links and the MenuItems children,
     * we have to actually disconnect them all, so that next
     * lifecycle we won't be adding duplicates to the MenuItems,
     * since its list of MenuItem children is scoped to live as
     * long as the bean, so it outlives typical components.
     */
    protected void recursivelyRemoveTransientChildren(
            UIComponent parent, UIComponent uiComponent) {
        int size = uiComponent.getChildCount();
        if (size > 0) {
            for (int i = size-1; i >= 0; i--) {
                recursivelyRemoveTransientChildren(
                        uiComponent, uiComponent.getChildren().get(i));
            }
        }
        if (uiComponent.isTransient()) {
            if (parent != null) {
                parent.getChildren().remove(uiComponent);
            }
            uiComponent.setParent(null);
        }
    }
}
