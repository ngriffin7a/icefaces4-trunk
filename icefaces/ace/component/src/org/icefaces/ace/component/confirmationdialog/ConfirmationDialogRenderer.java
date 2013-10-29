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
package org.icefaces.ace.component.confirmationdialog;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

@MandatoryResourceComponent(tagName="confirmationDialog", value="org.icefaces.ace.component.confirmationdialog.ConfirmationDialog")
public class ConfirmationDialogRenderer extends CoreRenderer {

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ConfirmationDialog dialog = (ConfirmationDialog) component;
		
		encodeMarkup(context, dialog);
	}

	protected void encodeMarkup(FacesContext context, ConfirmationDialog dialog) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = dialog.getClientId(context);
        String messageText = dialog.getMessage();
        UIComponent messageFacet = dialog.getFacet("message");
		
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId , null);
		
		writer.startElement("div", null);
		String header = dialog.getHeader();
		if(header != null) {
			writer.writeAttribute("title", header, null);
		}
		
		//body		
		writer.startElement("p", null);
		
		//severity
		writer.startElement("span", null);
		writer.writeAttribute("style", "float: left; margin: 0pt 7px 20px 0pt;", null);
		writer.writeAttribute("class", "ui-icon ui-icon-" + dialog.getSeverity(), null);
		writer.endElement("span");

        if(messageFacet != null) {
            messageFacet.encodeAll(context);
        }
        else if(messageText != null) {
			writer.write(messageText);
		}
		writer.endElement("p");
		
		//buttons
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_buttons", null);
		renderChildren(context, dialog);
		writer.endElement("div");
		
		writer.endElement("div");
		
		encodeScript(context, dialog);
		
		writer.endElement("div");
	}

	protected void encodeScript(FacesContext context, ConfirmationDialog dialog) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = dialog.getClientId();
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);

		writer.startElement("script", dialog);
		writer.writeAttribute("type", "text/javascript", null);
		
		JSONBuilder jb = JSONBuilder.create();
		
		jb.initialiseVar(this.resolveWidgetVar(dialog))
          .beginFunction("ice.ace.create")
          .item("ConfirmDialog")
          .beginArray()
          .item(clientId)
          .beginMap()
          .entry("minHeight", 0);
		
		String styleClass = dialog.getStyleClass();
		String style = dialog.getStyle();
        String showEffect = dialog.getShowEffect();
        String hideEffect = dialog.getHideEffect();
        int width = dialog.getWidth();
        int height = dialog.getHeight();
        int zIndex = dialog.getZindex();
		String handle = dialog.getDragHandle();

        if(styleClass != null) jb.entry("dialogClass", styleClass);
		if(style != null) jb.entry("dialogStyle", style);
		if(width != 300) jb.entry("width", width);
		if(height != Integer.MIN_VALUE) jb.entry("height", height);
		if(!dialog.isDraggable()) jb.entry("draggable", false);
		if(dialog.isModal()) jb.entry("modal", true);
		if(zIndex != 1000) jb.entry("zIndex", zIndex);
		if(showEffect != null) jb.entry("show", showEffect);
		if(hideEffect != null) jb.entry("hide", hideEffect);
		if(!dialog.isCloseOnEscape()) jb.entry("closeOnEscape", false);
		if(!dialog.isClosable()) jb.entry("closable", false);
		if (handle != null) jb.entry("handle", handle);

		//Position
		String position = dialog.getPosition();
        if (position != null) {
			if (position.contains(","))
				jb.entry("position", "[" + position + "]", true);
			else
				jb.entry("position", position);
		}
		
        jb.entry("ariaEnabled", ariaEnabled);
        jb.endMap().endArray().endFunction();

        writer.write(jb.toString());
		writer.endElement("script");
	}

    @Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//Do Nothing
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}
}
