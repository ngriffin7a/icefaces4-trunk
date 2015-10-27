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

package org.icefaces.ace.component.clientValidator;

import org.icefaces.impl.event.UIOutputWriter;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

class ScriptOutputWriter extends UIOutputWriter {
    private final String script;

    public ScriptOutputWriter(String script) {
        this.script = script;
        setTransient(true);
    }

    public void encode(ResponseWriter writer, FacesContext context) throws IOException {
        writer.startElement("span", this);
        writer.writeAttribute("id", getClientId(), null);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write(script);
        writer.endElement("script");
        writer.endElement("span");
    }
}
