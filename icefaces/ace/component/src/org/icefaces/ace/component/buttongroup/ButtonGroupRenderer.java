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
 * Code Modification 2: (ICE-6978) Used JSONBuilder to add the functionality of escaping JS output.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 */
package org.icefaces.ace.component.buttongroup;

import org.icefaces.ace.component.checkboxbutton.CheckboxButton;
import org.icefaces.ace.component.radiobutton.RadioButton;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

@MandatoryResourceComponent(tagName = "buttonGroup", value = "org.icefaces.ace.component.buttongroup.ButtonGroup")
public class ButtonGroupRenderer extends CoreRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        ButtonGroup buttonGroup = (ButtonGroup) component;
        String style = (style = buttonGroup.getStyle()) == null ? "" : style.trim();
        String styleClass = (styleClass = buttonGroup.getStyleClass()) == null ? "" : styleClass.trim();
        styleClass += (styleClass.length() > 0 ? " " : "") + "ui-widget ui-widget-content ui-corner-all";

        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(context), "id");
        if (style.length() > 0) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("class", styleClass, "styleClass");

        UIComponent headerFacet = component.getFacet("header");
        String headerText = (headerText = buttonGroup.getHeader()) == null ? "" : headerText.trim();

        if (headerFacet != null || headerText.length() > 0) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "ui-widget-header ui-corner-top", null);
            if (headerFacet != null) {
                renderChild(context, headerFacet);
            } else if (headerText.length() > 0) {
                writer.write(headerText);
            }
            writer.endElement("div");
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (component.getChildCount() > 0) {
            Iterator<UIComponent> kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = kids.next();
                kid.encodeAll(context);
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        ButtonGroup buttonGroup = (ButtonGroup) component;
        writer.endElement("div");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
