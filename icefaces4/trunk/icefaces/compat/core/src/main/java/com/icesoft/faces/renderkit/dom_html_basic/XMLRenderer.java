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

//import com.icesoft.faces.component.UIXhtmlComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class XMLRenderer extends Renderer {
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
            if (true) {throw new UnsupportedOperationException("Do we need XMLRenderer");}
//        UIXhtmlComponent xhtmlComponent = (UIXhtmlComponent) uiComponent;
//        ResponseWriter writer = facesContext.getResponseWriter();
//        writer.startElement(xhtmlComponent.getTag(), xhtmlComponent);
//
//        Iterator attributeIterator =
//                xhtmlComponent.getTagAttributes().entrySet().iterator();
//        while (attributeIterator.hasNext()) {
//            Map.Entry attribute = (Map.Entry) attributeIterator.next();
//            writer.writeAttribute((String) attribute.getKey(),
//                                  attribute.getValue(), null);
//        }
    }
//
//    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
//            throws IOException {
//        UIXhtmlComponent xhtmlComponent = (UIXhtmlComponent) uiComponent;
//        facesContext.getResponseWriter().endElement(xhtmlComponent.getTag());
//    }
}

