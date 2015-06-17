/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.ace.component.panelstack;


import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class PanelStackRenderer extends CoreRenderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         writer.startElement(HTML.DIV_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.CLASS_ATTR, "ace-panelstack", null);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         writer.endElement(HTML.DIV_ELEM);

    }


}
