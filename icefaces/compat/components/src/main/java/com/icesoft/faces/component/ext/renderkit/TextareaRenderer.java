
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

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class TextareaRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.TextareaRenderer {

    protected static final String ONMOUSEDOWN_FOCUS = "this.focus();";
    private static final String[] PASSTHRU_EXCLUDE =
        new String[] { HTML.ROWS_ATTR, HTML.COLS_ATTR };
    private static final String[] PASSTHRU_JS_EVENTS = LocalEffectEncoder.maskEvents(
            ExtendedAttributeConstants.getAttributes(
                ExtendedAttributeConstants.ICE_INPUTTEXTAREA));
    private static final String[] PASSTHRU =
            ExtendedAttributeConstants.getAttributes(
                ExtendedAttributeConstants.ICE_INPUTTEXTAREA,
                new String[][] {PASSTHRU_EXCLUDE, PASSTHRU_JS_EVENTS});

    protected void renderHtmlAttributes(
        FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
            throws IOException {
        HtmlInputTextarea inputTextarea = (HtmlInputTextarea) uiComponent;
        PassThruAttributeWriter.renderHtmlAttributes(
            writer, uiComponent, PASSTHRU);
        Map rendererJS = new HashMap(4);
        rendererJS.put(
            HTML.ONMOUSEDOWN_ATTR, ONMOUSEDOWN_FOCUS);
        int maxlength = inputTextarea.getMaxlength();
        if (maxlength >= 0) {
            // The most recent char is not a part of the textarea's value when
            // the onkey* events fire, so if you mouse away, it'll have one 
            // too many chars, hence the need for onchange. Can't just be
            // onchange though, since it doesn't fire as you type, in FF3.
            String handler = "Ice.txtAreaMaxLen(this,"+maxlength+");"; 
            rendererJS.put(HTML.ONKEYDOWN_ATTR, handler);
            rendererJS.put(HTML.ONCHANGE_ATTR, handler);
        }
        if (inputTextarea.getPartialSubmit()) {
            rendererJS.put(
                HTML.ONBLUR_ATTR, "setFocus('');" + DomBasicRenderer.ICESUBMITPARTIAL);
        }
        LocalEffectEncoder.encode(
            facesContext, uiComponent, PASSTHRU_JS_EVENTS, rendererJS, null, writer);                
    }
}
