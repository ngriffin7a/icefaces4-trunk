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

package org.icefaces.ace.component.gmap;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.ace.util.JSONBuilder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

@MandatoryResourceComponent(tagName = "gMap", value = "org.icefaces.ace.component.gmap.GMap")
public class GMapInfoWindowRenderer extends CoreRenderer {

    public void decode(FacesContext context, UIComponent component) {
        String clientID = component.getClientId(context);
        Map<String,String> parameterMap = context.getExternalContext().getRequestParameterMap();
        if (clientID.equals(parameterMap.get("javax.faces.source"))) {
            if ("close".equals(parameterMap.get(clientID))) {
                ((GMapInfoWindow) component).setDisabled(true);
            }
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        GMapInfoWindow infoWindow = (GMapInfoWindow) component;
        String clientId = infoWindow.getClientId(context);
        String mapId;
        String markerId = "none";
		boolean addressBasedMarker = false;

        // main container
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("style", "display:none;", null);

        // content container
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_content", null);

        if (!infoWindow.getChildren().toString().contains("GMapEvent"))
            renderChildren(context, infoWindow);

        writer.endElement("div");

        // script
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
		GMapMarker parentMarker = getParentMarker(infoWindow);
            if (parentMarker != null) {
                markerId = parentMarker.getClientId(context);
                mapId = GMapRenderer.getMapClientId(context, infoWindow);
				String address = parentMarker.getAddress();
				addressBasedMarker = address != null && !"".equals(address);
            } else {
                mapId = GMapRenderer.getMapClientId(context, infoWindow);
            }
		JSONBuilder jb;
		String content = infoWindow.getContent();
        if (!infoWindow.isDisabled()) {
            if (infoWindow.getChildCount() == 0) {
				jb = JSONBuilder.create();
				jb.beginFunction("ice.ace.gMap.addGWindow")
					.item(mapId)
					.item(clientId)
					.item(content == null ? "" : content)
					.item("new google.maps.LatLng(" + infoWindow.getLatitude() + "," + infoWindow.getLongitude() + ")", false)
					.item(infoWindow.getOptions())
					.item(markerId)
					.item(infoWindow.isShowOnClick())
					.item(infoWindow.isStartOpen())
					.item(addressBasedMarker)
				.endFunction();
                writer.write(jb.toString());
				writer.write("});");
                writer.endElement("script");
            } else {
                if (!infoWindow.getChildren().toString().contains("GMapEvent")) {
					jb = JSONBuilder.create();
					jb.beginFunction("ice.ace.gMap.addGWindow")
						.item(mapId)
						.item(clientId)
						.item("document.getElementById('" + clientId + "_content')", false)
						.item("new google.maps.LatLng(" + infoWindow.getLatitude() + "," + infoWindow.getLongitude() + ")", false)
						.item(infoWindow.getOptions())
						.item(markerId)
						.item(infoWindow.isShowOnClick())
						.item(infoWindow.isStartOpen())
						.item(addressBasedMarker)
					.endFunction();
                    writer.write(jb.toString());
                    writer.write("});");
                    writer.endElement("script");
                } else {
					jb = JSONBuilder.create();
					jb.beginFunction("ice.ace.gMap.addGWindow")
						.item(mapId)
						.item(clientId)
						.item(content == null ? "" : content)
						.item("new google.maps.LatLng(" + infoWindow.getLatitude() + "," + infoWindow.getLongitude() + ")", false)
						.item(infoWindow.getOptions())
						.item(markerId)
						.item(infoWindow.isShowOnClick())
						.item(infoWindow.isStartOpen())
						.item(addressBasedMarker)
					.endFunction();
                    writer.write(jb.toString());
                    writer.write("});");
                    writer.endElement("script");
                    renderChildren(context, infoWindow);
                }
            }
        }
        else{
			jb = JSONBuilder.create();
			jb.beginFunction("ice.ace.gMap.removeGWindow")
				.item(mapId)
				.item(clientId)
			.endFunction();
            writer.write(jb.toString());
            writer.write("});");
            writer.endElement("script");
        }
        writer.endElement("span"); // main container
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    public boolean getRendersChildren() {
        return true;
    }

	protected static GMapMarker getParentMarker(UIComponent component) {
		UIComponent parent = component.getParent();
		while(parent != null) {
			if (parent instanceof GMapMarker) return ((GMapMarker) parent);
			parent = parent.getParent();
		}
		return null;
	}
}