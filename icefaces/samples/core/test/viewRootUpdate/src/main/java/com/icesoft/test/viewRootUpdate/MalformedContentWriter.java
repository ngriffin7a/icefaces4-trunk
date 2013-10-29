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

package com.icesoft.test.viewRootUpdate;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;

public class MalformedContentWriter implements SystemEventListener {

    public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        root.addComponentResource(context, new MalformedMarkup(), "body");
    }

    public boolean isListenerForSource(Object o) {
        return true;
    }

    private static class MalformedMarkup extends UIOutput {
        private MalformedMarkup() {
            setTransient(true);
        }

        public void encodeBegin(FacesContext context) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            writer.write("<br>");
        }
    }
}
