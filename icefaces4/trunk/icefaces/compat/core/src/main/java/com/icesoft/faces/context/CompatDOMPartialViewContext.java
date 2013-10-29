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

package com.icesoft.faces.context;

import com.icesoft.faces.context.effects.JavascriptContext;
import org.icefaces.impl.context.DOMPartialViewContext;
import org.icefaces.impl.context.DOMResponseWriter;
import org.icefaces.util.EnvUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class CompatDOMPartialViewContext extends DOMPartialViewContext {

    public CompatDOMPartialViewContext(PartialViewContext partialViewContext, FacesContext facesContext) {
        super(partialViewContext, facesContext);
    }

    protected Writer getResponseOutputWriter() throws IOException {
        return new WriteViewStateMarkup(super.getResponseOutputWriter());
    }

    protected void renderExtensions() {
        super.renderExtensions();

        String javascriptCalls = JavascriptContext.getJavascriptCalls(facesContext);
        if (javascriptCalls != null && javascriptCalls.trim().length() > 0) {
            try {
                PartialResponseWriter partialWriter = getPartialResponseWriter();
                partialWriter.startEval();
                partialWriter.write(javascriptCalls);
                partialWriter.endEval();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class WriteViewStateMarkup extends FilterWriter {
        protected WriteViewStateMarkup(Writer out) {
            super(out);
        }

        public void write(String str) throws IOException {
            if (EnvUtils.getStateMarker().equals(str)) {
                out.write("<input id=\"javax.faces.ViewState\" type=\"hidden\" autocomplete=\"off\" value=\"");
                out.write(facesContext.getApplication().getStateManager().getViewState(facesContext));
                out.write("\" name=\"javax.faces.ViewState\"/>");
            } else {
                out.write(str);
            }
        }
    }
}
