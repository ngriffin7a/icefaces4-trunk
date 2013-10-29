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
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@MandatoryResourceComponent(tagName = "gMap", value = "org.icefaces.ace.component.gmap.GMap")
public class GMapRenderer extends CoreRenderer {

	public void decode(FacesContext context, UIComponent component) {
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        GMap map = (GMap) component;
        String clientId = map.getClientId(context);
        String lat = String.valueOf(requestParameterMap.get(clientId + "_lat"));
        String lng = String.valueOf(requestParameterMap.get(clientId + "_lng"));
        String zoom = String.valueOf(requestParameterMap.get(clientId + "_zoom"));
        String type = String.valueOf(requestParameterMap.get(clientId + "_type"));
        if (lat != null && !lat.equals("") && !lat.equals("null")) {
            map.setLatitude(lat);
        }
        if (lng != null && !lng.equals("") && !lng.equals("null")) {
            map.setLongitude(lng);
        }
        if (zoom != null && !zoom.equals("") && !zoom.equals("null")) {
            map.setZoomLevel(zoom);
        }
        if (type != null && !type.equals("") && !type.equals("null")) {
            map.setType(type);
        }
	}

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        GMap gmap = (GMap) component;
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_wrapper", null);
        writer.writeAttribute("class", "ice-ace-gmap " + gmap.getStyleClass(), null);
        writer.writeAttribute("style", gmap.getStyle(), null);
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("style", "height:100%; width:100%", null);
        writer.endElement("div");
        writer.endElement("div");
        makeFields(writer, clientId, "lat");
        makeFields(writer, clientId, "lng");
        makeFields(writer, clientId, "type");
        makeFields(writer, clientId, "zoom");
        writer.startElement("span", null);
        writer.writeAttribute("id", clientId + "_script", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
		JSONBuilder jb;
        if ((gmap.isLocateAddress() || !gmap.isIntialized()) && (gmap.getAddress() != null && gmap.getAddress().length() > 2)) {
			jb = JSONBuilder.create();
			jb.beginFunction("ice.ace.gMap.locateAddress").item(clientId).item(gmap.getAddress()).endFunction();
            writer.write(jb.toString());
        } else {
            writer.write("ice.ace.gMap.getGMapWrapper('" + clientId + "').getRealGMap().setCenter(new google.maps.LatLng(" + gmap.getLatitude() + "," + gmap.getLongitude() + "));");
		}
        writer.write("ice.ace.gMap.getGMapWrapper('" + clientId + "').getRealGMap().setZoom(" + gmap.getZoomLevel() + ");");
		jb = JSONBuilder.create();
		jb.beginFunction("ice.ace.gMap.setMapType").item(clientId).item(gmap.getType().toUpperCase()).endFunction();
        writer.write(jb.toString());
        if (gmap.getOptions() != null && gmap.getOptions().length() > 1) {
			jb = JSONBuilder.create();
			jb.beginFunction("ice.ace.gMap.addOptions").item(clientId).item(gmap.getOptions()).endFunction();
            writer.write(jb.toString());
		}
        if (gmap.getParent().getClass().getSimpleName().equalsIgnoreCase("HtmlPanelGrid")) {
            writer.write("google.maps.event.addDomListener(ice.ace.gMap.getGMapWrapper('" + clientId + "').getRealGMap(),'bounds_changed', " +
                    "function(){" +
                    "var mapCenter = ice.ace.gMap.getGMapWrapper('" + clientId + "').getRealGMap().getCenter();" +
                    "google.maps.event.trigger(ice.ace.gMap.getGMapWrapper('" + clientId + "').getRealGMap(),'resize');" +
                    "ice.ace.gMap.getGMapWrapper('" + clientId + "').getRealGMap().setCenter(mapCenter);});");
        }
        writer.write("});");
        writer.endElement("script");
        writer.endElement("span");
        gmap.setIntialized(true);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (component.getChildCount() == 0) return;
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
        }

    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public void makeFields(ResponseWriter writer, String clientId, String fieldName) throws IOException {
        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", clientId + "_" + fieldName, null);
        writer.writeAttribute("name", clientId + "_" + fieldName, null);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
        writer.endElement("input");
    }
}