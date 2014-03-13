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

package org.icefaces.mobi.component.cloudpush;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import static org.icefaces.mobi.util.HTML.*;

public class CloudPushRenderer extends Renderer {

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        CloudPush cloudPush = (CloudPush) uiComponent;
        ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        String clientId = cloudPush.getClientId();
		// button element
		writer.startElement(BUTTON_ELEM, cloudPush);
		writer.writeAttribute(ID_ATTR, clientId);
		writer.writeAttribute(NAME_ATTR, clientId + "_button");
		writer.writeAttribute(TYPE_ATTR, "button");
		String script = "bridgeit.register();return false;";
		writer.writeAttribute(ONCLICK_ATTR, script);
		writer.startElement(SPAN_ELEM, cloudPush);
		writer.writeText(cloudPush.getButtonLabel());
		writer.endElement(SPAN_ELEM);
		// themeroller support
		writer.startElement("span", cloudPush);
		writer.startElement("script", cloudPush);
		writer.writeAttribute("type", "text/javascript");
		writer.writeText("ice.ace.jq(ice.ace.escapeClientId('" + clientId + "')).button();");
		writer.endElement("script");
		writer.endElement("span");
		writer.endElement(BUTTON_ELEM);
    }
}
