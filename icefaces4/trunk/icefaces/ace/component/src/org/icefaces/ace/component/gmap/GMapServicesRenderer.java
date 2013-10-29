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

package org.icefaces.ace.component.gmap;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

@MandatoryResourceComponent(tagName = "gMap", value = "org.icefaces.ace.component.gmap.GMap")
public class GMapServicesRenderer extends CoreRenderer {


    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        GMapServices service = (GMapServices) component;
        String clientId = service.getClientId(context);
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId + "_services", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        if (service.getPoints() != null && service.getName() != null) {
			JSONBuilder jb = JSONBuilder.create();
			jb.beginFunction("ice.ace.gMap.gService")
				.item(service.getParent().getClientId(context))
				.item(service.getName())
				.item(service.getPoints())
				.item(service.getOptions())
				.item(service.getDiv())
			.endFunction();
			writer.write(jb.toString());
        }
        writer.write("});");
        writer.endElement("script");
        writer.endElement("span");
    }
}