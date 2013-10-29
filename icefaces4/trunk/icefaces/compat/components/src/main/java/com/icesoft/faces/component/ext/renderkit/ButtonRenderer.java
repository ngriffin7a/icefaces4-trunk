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

import com.icesoft.faces.component.ext.HtmlCommandButton;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import com.icesoft.faces.component.panelconfirmation.PanelConfirmationRenderer;

import java.util.Iterator;
import java.util.Map;

public class ButtonRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.ButtonRenderer {

    protected void renderOnClick(UIComponent uiComponent, Element root) {
        HtmlCommandButton button = (HtmlCommandButton) uiComponent;
        String onclick = (String) uiComponent.getAttributes().get("onclick");
        String submitCode;
        if (button.getPartialSubmit()) {
            submitCode = getJavascriptHiddenFieldSetters(uiComponent) + this.ICESUBMITPARTIAL + getJavascriptHiddenFieldReSetters(uiComponent) + "return false;";
        } else {
            submitCode = getJavascriptHiddenFieldSetters(uiComponent) + this.ICESUBMIT + getJavascriptHiddenFieldReSetters(uiComponent) + "return false;";
        }
        
        if (null != uiComponent.getAttributes().get("panelConfirmation")) {
            root.setAttribute("onclick", PanelConfirmationRenderer.renderOnClickString(
                uiComponent, combinedPassThru(onclick, submitCode)));
        } else {
            root.setAttribute("onclick", combinedPassThru(onclick, submitCode));
        }
    }


    private static String getJavascriptHiddenFieldSetters(UIComponent component) {
        StringBuffer buffer = new StringBuffer("");
        Map parameters = getParameterMap(component);
        Iterator parameterKeys = parameters.keySet().iterator();
        while (parameterKeys.hasNext()) {
            String nextParamName = (String) parameterKeys.next();
            Object nextParamValue = parameters.get(nextParamName);
            buffer.append("form['");
            buffer.append(nextParamName);
            buffer.append("'].value='");
            buffer.append(nextParamValue);
            buffer.append("';");
        }
        return buffer.toString();
    }

    private static String getJavascriptHiddenFieldReSetters(UIComponent component) {
        StringBuffer buffer = new StringBuffer("");
        Map parameters = getParameterMap(component);
        Iterator parameterKeys = parameters.keySet().iterator();
        while (parameterKeys.hasNext()) {
            String nextParamName = (String) parameterKeys.next();
            Object nextParamValue = parameters.get(nextParamName);
            buffer.append("form['");
            buffer.append(nextParamName);
            buffer.append("'].value='");
//            buffer.append(nextParamValue);
            buffer.append("';");
        }
        return buffer.toString();
    }
}
