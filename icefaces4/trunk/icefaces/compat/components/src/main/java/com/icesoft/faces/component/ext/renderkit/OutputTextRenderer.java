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

import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class OutputTextRenderer extends com.icesoft.faces.renderkit.dom_html_basic.OutputTextRenderer {
    // LocalEffectEncoder takes ownership of any passthrough attributes
    private static final String[] jsEvents = LocalEffectEncoder.maskEvents(
        ExtendedAttributeConstants.getAttributes(
            ExtendedAttributeConstants.ICE_OUTPUTTEXT));
    private static final String[] passThruAttributes =
        ExtendedAttributeConstants.getAttributes(
            ExtendedAttributeConstants.ICE_OUTPUTTEXT,
            jsEvents);
    
    protected void renderHtmlAttributes(
        FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
        throws IOException {
        PassThruAttributeWriter.renderHtmlAttributes(
            writer, uiComponent, passThruAttributes);
        LocalEffectEncoder.encode(
            facesContext, uiComponent, jsEvents, null, null, writer);
    }
}
