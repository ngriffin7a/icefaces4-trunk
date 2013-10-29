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

package org.icefaces.impl.event;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;
import java.util.logging.Logger;

/**
 * Writer to add common javascript blurb to form as a &lt;script&gt; element
 */
public class ScriptWriter extends UIOutput {

    private String javascriptContents;
    private UIComponent parent;

    private static final Logger Log = Logger.getLogger(ScriptWriter.class.getName());

    /**
     * Write an adhoc snippet of un-escaped javascript. It is apparently unnecessary for
     * javascript elements to have id's, but if one is passed, it's used. A side effect
     * of having an id is to cause the script tag to be enclosed in a <span> tag
     * that itself has an id.
     *
     * @param parent the UIComponent to add the javascript node to
     * @param javascriptContents The intended contents intended for between the <script></script> tags
     * @param optionalId An id for the script element.
     */
    public ScriptWriter(UIComponent parent, final String javascriptContents, String optionalId) {
        this.parent = parent; 
        this.setTransient(true);
        this.javascriptContents = javascriptContents;
        if (optionalId != null) {
            this.setId(optionalId);
        }
    }

    public void encodeBegin(FacesContext context) {
        ResponseWriter writer = context.getResponseWriter();
        try {
            writer.startElement("span", this);
            writer.writeAttribute("id", getClientId(context), null);
            writer.startElement("script", this);
            writer.writeAttribute("type", "text/javascript", "type");
            writer.write(javascriptContents);
        } catch (Exception ioe) {
            Log.severe("Exception encoding script tag: " +  ioe);
        }
    }

    public void encodeEnd(FacesContext context) {
        ResponseWriter writer = context.getResponseWriter();
        try {
            writer.endElement("script");
            writer.endElement("span");
        } catch (Exception ioe) {
            Log.severe("Exception encoding script tag: " +  ioe);
        }
    }
}
