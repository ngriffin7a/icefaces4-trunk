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

package org.icefaces.ace.component.textareaentry;

import org.icefaces.ace.component.textareaentry.TextAreaEntryBase;
import org.icefaces.component.Focusable;

import javax.faces.context.FacesContext;

public class TextAreaEntry extends TextAreaEntryBase implements Focusable {
    public final static String THEME_INPUT_CLASS = "ui-inputfield ui-textareaentry ui-widget ui-state-default ui-corner-all";
    public final static String PLAIN_INPUT_CLASS = "ui-textareaentry";

    public String getFocusedElementId() {
        return getClientId(FacesContext.getCurrentInstance()) + "_input";
    }
}
