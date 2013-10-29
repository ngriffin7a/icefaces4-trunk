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
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.component.notificationpanel;

import org.icefaces.ace.util.JSONBuilder;
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

@MandatoryResourceComponent(tagName="notificationPanel", value="org.icefaces.ace.component.notificationpanel.NotificationPanel")
public class NotificationPanelRenderer extends CoreRenderer {

    @Override
	public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
		NotificationPanel bar = (NotificationPanel) component;
		
		encodeMarkup(facesContext, bar);
	}
	
	protected void encodeMarkup(FacesContext facesContext, NotificationPanel bar) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
        String barStyleClass = bar.getStyleClass();
        String styleClass = barStyleClass == null ? "ui-notificationbar" : "ui-notificationbar " + barStyleClass;
		UIComponent close = bar.getFacet("close");
		
		writer.startElement("div", bar);
		writer.writeAttribute("id", bar.getClientId(facesContext), null);
		writer.writeAttribute("class", styleClass + " ui-widget ui-widget-content ui-corner-all", null);
        String style = bar.getStyle();
        if(style != null) writer.writeAttribute("style", style, null);
        if (EnvUtils.isAriaEnabled(facesContext)) {
            writer.writeAttribute("role", close == null ? "alert" : "alertdialog", null);
        }

        if(close != null) {
			writer.startElement("span", null);
			writer.writeAttribute("class", "ui-notificationbar-close", null);
			writer.writeAttribute("onclick", this.resolveWidgetVar(bar) + ".hide()", null);
			renderChild(facesContext, close);
			writer.endElement("span");
		}

		renderChildren(facesContext, bar);
		
		encodeScript(facesContext, bar);
		
		writer.endElement("div");
	}

	private void encodeScript(FacesContext facesContext, NotificationPanel bar) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = bar.getClientId(facesContext);

		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("ice.ace.jq(document).ready(function(){");

        JSONBuilder json = JSONBuilder.create();
        json.initialiseWindowVar(this.resolveWidgetVar(bar))
            .beginFunction("ice.ace.create")
            .item("NotificationBar")
            .beginArray()
            .item(clientId)
            .beginMap()

            .entry("position", bar.getPosition())
            .entry("effect", bar.getEffect())
            .entry("effectSpeed", bar.getEffectSpeed())
            .entry("visible", bar.isVisible())
            .entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext));

        encodeClientBehaviors(facesContext, bar, json);

        json.endMap().endArray().endFunction();

        writer.write(json.toString());
		writer.write("});");
		
		writer.endElement("script");
	}

	public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
		//Do nothing
	}

	public boolean getRendersChildren() {
		return true;
	}

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        decodeBehaviors(context, component);
    }
}
