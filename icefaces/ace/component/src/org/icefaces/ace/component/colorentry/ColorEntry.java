/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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
package org.icefaces.ace.component.colorentry;

import org.icefaces.ace.component.clientValidator.Validateable;
import org.icefaces.component.Focusable;



public class ColorEntry extends ColorEntryBase implements Focusable {
    public final static String INPUT_STYLE_CLASS = "ui-inputfield ui-widget ui-state-default ui-corner-all ui-colorpicker-input";
    public static String POPUP_ICON = "colorentry/ui-colorpicker.png";

    public String getValidatedElementId() {
         return getClientId() + "_input";
     }
    public String getFocusedElementId() {
         return getClientId() + "_input";
     }
}
