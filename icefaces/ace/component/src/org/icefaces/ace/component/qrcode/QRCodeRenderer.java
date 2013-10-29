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

package org.icefaces.ace.component.qrcode;


import org.icefaces.ace.component.qrcode.generator.QRCodeResourceHandler;
import org.icefaces.ace.util.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.logging.Logger;


public class QRCodeRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(QRCodeRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        QRCode qrcode = (QRCode) uiComponent;
        writer.startElement(HTML.IMG_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        String value = String.valueOf(qrcode.getValue());
        String url = QRCodeResourceHandler.getQRImageURL(value);
        writer.writeAttribute(HTML.SRC_ATTR, url, null);
        writer.writeAttribute(HTML.CLASS_ATTR, 
                qrcode.getStyleClass(), null);
        writer.writeAttribute(HTML.STYLE_ATTR, qrcode.getStyle(), HTML.STYLE_ATTR);
        writer.endElement(HTML.IMG_ELEM);
    }

}
