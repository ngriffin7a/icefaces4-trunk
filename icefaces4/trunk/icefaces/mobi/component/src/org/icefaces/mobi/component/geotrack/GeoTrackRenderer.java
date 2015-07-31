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

package org.icefaces.mobi.component.geotrack;

import org.icefaces.impl.util.CoreUtils;
import org.icefaces.ace.util.HTML;
import org.icefaces.mobi.renderkit.CoreRenderer;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.util.ClientDescriptor;
import org.icefaces.util.EnvUtils;

import javax.el.ValueExpression;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class GeoTrackRenderer extends CoreRenderer {

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        GeoTrack geotrack = (GeoTrack) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = geotrack.getClientId();
		UIComponent fallbackFacet = geotrack.getFacet("fallback");

		writer.startElement(HTML.SPAN_ELEM, geotrack);
		writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		writer.writeAttribute(HTML.CLASS_ATTR, "mobi-geotrack", null);

		writer.startElement(HTML.BUTTON_ELEM, geotrack);
		writer.writeAttribute(HTML.ID_ATTR, clientId + "_button", null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId + "_button", null);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		if (geotrack.isDisabled()) writer.writeAttribute(HTML.DISABLED_ATTR, "disabled", null);
		String style = geotrack.getStyle();
		if (style != null) writer.writeAttribute(HTML.STYLE_ATTR, style, null);
		String styleClass = geotrack.getStyleClass();
		if (styleClass != null) writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
		writer.writeAttribute(HTML.TABINDEX_ATTR, geotrack.getTabindex(), null);
		String launchFailed = fallbackFacet != null ? "ice.mobi.fallback.setupLaunchFailed('"+clientId+"_button','"+clientId+"_fallback');" : "";
		String script = launchFailed + "bridgeit.geoTrack('" + clientId + "', '', {postURL:'" + GeoTrackResourceHandler.getPostURL();
		script += "&__id=" + storeExpression(facesContext, geotrack) + "', ";
		String strategy = geotrack.getStrategy();
        script += "strategy:'" + strategy + "', duration:" + ("stop".equalsIgnoreCase(strategy) ? "0" : geotrack.getDuration());
		String params = geotrack.getParameters();
		if (params != null && !"".equals(params.trim())) script += ", " + params;
		script += "});";
		writer.writeAttribute(HTML.ONCLICK_ATTR, script, null);
		writer.startElement(HTML.SPAN_ELEM, geotrack);
		writer.write(geotrack.getButtonLabel());
		writer.endElement(HTML.SPAN_ELEM);

		// themeroller support
		writer.startElement("span", geotrack);
		writer.startElement("script", geotrack);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("new ice.mobi.button('"+clientId+"_button');");
		writer.endElement("script");
		writer.endElement("span");

		writer.endElement(HTML.BUTTON_ELEM);

		if (fallbackFacet != null) {
			writer.startElement(HTML.SPAN_ELEM, geotrack);
			writer.writeAttribute(HTML.ID_ATTR, clientId + "_fallback", null);
			writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
			if (fallbackFacet.isRendered()) fallbackFacet.encodeAll(facesContext);
			writer.endElement(HTML.SPAN_ELEM);
		}
		writer.startElement("script", geotrack);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeText("if (!bridgeit.isSupportedPlatform('geospy') && document.getElementById('"+clientId+"_fallback')) {", null);
		writer.writeText("document.getElementById('"+clientId+"_button').style.display='none';", null);
		writer.writeText("document.getElementById('"+clientId+"_fallback').style.display='inline';", null);
		writer.writeText("}", null);
		writer.endElement("script");

		writer.endElement(HTML.SPAN_ELEM);
    }

	private String storeExpression(FacesContext facesContext, GeoTrack geotrack) {
		String name = CoreUtils.getSessionId(facesContext) + "_"
			+ facesContext.getViewRoot().getViewId() + "_" + geotrack.getId();
		Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();
		applicationMap.put(name, geotrack.getValueExpression("publish"));
		return name;
	}
}
