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
package org.icefaces.ace.component.graphicimage;

import org.icefaces.ace.util.PassThruAttributeWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GraphicImageRenderer extends Renderer {

    private static final Logger logger =
            Logger.getLogger(GraphicImageRenderer.class.toString());


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        //
        ResponseWriter writer = facesContext.getResponseWriter();
        assert (null != facesContext);
        assert (null != writer);
        assert (null != uiComponent);
        String clientId = uiComponent.getClientId(facesContext);
        GraphicImage uiGraphic = (GraphicImage) uiComponent;
        if (logger.isLoggable(Level.FINER)) {
            checkAttributes(uiGraphic);
        }
        writer.startElement("img", uiComponent);
        writer.writeAttribute("id", clientId, null);
        //let pass through attributes deal with height and weight

//        String altAttribute = uiGraphic.getAlt();
//        if (altAttribute == null) {
//            altAttribute = "";
//        }

        if (writer.getContentType().equals("application/xhtml+xml") &&
                null == uiComponent.getAttributes().get("alt")) {
            // write out an empty alt
            writer.writeAttribute("alt", "", "alt");
        }

        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, uiGraphic.getAttributesNames());
        PassThruAttributeWriter.renderBooleanAttributes(writer, uiComponent, uiGraphic.getBooleanAttNames());

        String styleClass;
        if (null != (styleClass = (String)
                uiComponent.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        String mimeType = uiGraphic.getMimeType();
        if (null == mimeType) mimeType = "image/jpeg";
        String scope = uiGraphic.getScope().toLowerCase().trim();
        if (!scope.equals("flash") && !(scope.equals("window")) && !(scope.equals("application"))
                && (!scope.equals("request")) && (!scope.equals("view"))) {
            scope = "session";
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("setting scope to session for addition of resrouce");
            }
        }
        String name = uiGraphic.getName();
        if (null == name || name.equals("")) name = "image" + clientId;
        Object value = uiGraphic.getValue();
        String srcAttribute = uiGraphic.processSrcAttribute(facesContext, value, name, mimeType, scope);
        writer.writeAttribute("src", srcAttribute, null);
        writer.endElement("img");
    }

    /*
      * just a diagnostic method that isn't currently being used.
      */
    private void checkAttributes(GraphicImage uiGraphic) {
        Map<String, Object> tempMap = uiGraphic.getAttributes();
        for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
            logger.finer(" attribute entry is " + entry.getKey() + " value is " + entry.getValue());
        }
    }

}
