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
package org.icefaces.ace.component.printer;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.*;

import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

@MandatoryResourceComponent(tagName="printer", value="org.icefaces.ace.component.printer.Printer")
public class PrinterRenderer extends CoreRenderer {

    @Override
    public void decode(FacesContext facesContext, UIComponent component) {
		Printer printer = (Printer) component;
		String clientId = printer.getClientId(facesContext);
		
		Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
		Object sourceId = requestMap.get("ice.event.captured");
		if (sourceId != null && sourceId.toString().equals(clientId)) {
			printer.queueEvent(new ActionEvent(printer) {{ setPhaseId(PhaseId.INVOKE_APPLICATION); }});
		}
		
		decodeBehaviors(facesContext, printer);
	}
	
	@Override
	public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		Printer printer = (Printer) component;
		String clientId = printer.getClientId(facesContext);
		String parentClientId = printer.getParent().getClientId(facesContext);
		UIComponent forValue = printer.findComponent(printer.getFor());
		if(forValue == null)
			throw new FacesException("Cannot find component \"" + printer.getFor() + "\" in view.");
		
		writer.startElement("span", printer);
		writer.writeAttribute("id", clientId, null);
		writer.startElement("script", printer);
		writer.writeAttribute("type", "text/javascript", null);
			
		writer.write("ice.ace.jq(ice.ace.escapeClientId('" + parentClientId + "')).click(function(e) {\n");
		writer.write("e.preventDefault();\n");

        // ClientBehaviors
        Map<String,List<ClientBehavior>> behaviorEvents = printer.getClientBehaviors();
        if(!behaviorEvents.isEmpty()) {
            List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
			for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get("activate").iterator(); behaviorIter.hasNext();) {
				ClientBehavior behavior = behaviorIter.next();
				ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(facesContext, printer, "activate", clientId, params);
				String script = behavior.getScript(cbc);    //could be null if disabled

				if(script != null) {
                    writer.write("ice.ace.ab(");
					writer.write(script);
					writer.write(");\n");
				}
			}
		}
		if (printer.isIgnoreValidation())
			writer.write("ice.ace.jq(ice.ace.escapeClientId('" + forValue.getClientId(facesContext) + "')).jqprint();\n");
		writer.write("});");
		writer.endElement("script");
		
		// ignoreValidation
		writer.startElement("span", printer);
		writer.writeAttribute("id", clientId + "_ignoreValidation", null);
		writer.startElement("script", printer);
		writer.writeAttribute("type", "text/javascript", null);
		if (!printer.isIgnoreValidation() && printer.isPassedValidation()) {
			Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
			Object sourceId = requestMap.get("ice.event.captured");
			if (sourceId != null && sourceId.toString().equals(clientId))
				writer.write("ice.ace.jq(ice.ace.escapeClientId('" + forValue.getClientId(facesContext) + "')).jqprint();\n");
				writer.write("// " + System.currentTimeMillis());
        }
		writer.endElement("script");
		writer.endElement("span");
		writer.endElement("span");
		
		printer.setPassedValidation(false);
	}
}
