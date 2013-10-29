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

package org.icefaces.ace.component.tabset;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.ace.util.HTML;
import org.icefaces.util.CoreComponentUtils;

public class TabSetProxy extends TabSetProxyBase{

    public void encodeBegin(FacesContext context) throws IOException {
    	super.encodeBegin(context);
    	ResponseWriter writer = context.getResponseWriter();
    	String id = getFor() + "_tsc";
    	writer.startElement(HTML.INPUT_ELEM, this); 	
    	writer.writeAttribute(HTML.ID_ATTR, id, HTML.ID_ATTR);
    	writer.writeAttribute(HTML.NAME_ATTR, id, HTML.NAME_ATTR);    	
    	writer.writeAttribute(HTML.TYPE_ATTR, "hidden", HTML.TYPE_ATTR);
        writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, "off", null);
    	writer.endElement(HTML.INPUT_ELEM);
    }

    @Override
    public String getFor() {
        String forComponentId = super.getFor();
        UIViewRoot root = getFacesContext().getViewRoot();
        UIComponent forComponent = CoreComponentUtils.findComponentInView(root, forComponentId);
        if( forComponent != null ){
            forComponentId = forComponent.getClientId();
        }
        return forComponentId;
    }

}



