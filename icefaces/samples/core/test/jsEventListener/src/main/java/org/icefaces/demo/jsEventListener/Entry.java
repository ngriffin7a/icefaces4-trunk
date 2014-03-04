/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.jsEventListener;

import org.icefaces.application.ResourceRegistry;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "Entry")
@ViewScoped
public class Entry implements Serializable {
    private StringBuffer capturedEvents = new StringBuffer();

    public Entry() {
    }

    public String getCapturedEvents() {
        return capturedEvents.toString();
    }

    public void captureEvent(ActionEvent event) {
        capturedEvents.append(event.toString());
        capturedEvents.append("\n................................\n");
    }
}
