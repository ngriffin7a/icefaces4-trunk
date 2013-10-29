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

import com.icesoft.faces.component.ext.HtmlCommandLink;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import org.icefaces.util.EnvUtils;
import org.w3c.dom.Element;

import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

import com.icesoft.faces.component.panelconfirmation.PanelConfirmationRenderer;

public class CommandLinkRenderer extends com.icesoft.faces.renderkit.dom_html_basic.CommandLinkRenderer {
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        super.encodeBegin(facesContext, uiComponent);
    }

    public void renderOnClick(FacesContext facesContext, UIComponent uiComponent, Element root, Map parameters) {
        HtmlCommandLink link = (HtmlCommandLink) uiComponent;
        if (link.isDisabled()) {
            root.removeAttribute("onclick");
            root.removeAttribute("href");
        } else {
            UIComponent uiForm = findForm(uiComponent);
            if (uiForm == null) {
                throw new FacesException("CommandLink must be contained in a form");
            }
            Object passThruOnClick = uiComponent.getAttributes().get(HTML.ONCLICK_ATTR);
            // if onClick attribute set by the user, pre append it.
            String rendererOnClick;
            if (link.getPartialSubmit()) {
                rendererOnClick = getJavaScriptPartialOnClickString(facesContext, uiComponent, parameters);
            } else {
                rendererOnClick = getJavaScriptOnClickString(facesContext, uiComponent, parameters);
            }
            if (null != link.getPanelConfirmation()) {
                root.setAttribute("onclick", PanelConfirmationRenderer.renderOnClickString(
                    uiComponent, combinedPassThru((String) passThruOnClick, rendererOnClick)));
            } else {
                root.setAttribute("onclick", combinedPassThru((String) passThruOnClick, rendererOnClick));
            }
        }
    }

    private String getJavaScriptPartialOnClickString(FacesContext facesContext, UIComponent uiComponent, Map parameters) {
        String str1 = "";
        String str2 = "";
        //myfaces queue does not serialize
        //until the request is sent, so we must delay
        if (EnvUtils.isMyFaces()) {
            str1 = "ice.onAfterUpdate(function() {";
            str2 = "});";
        }
        return com.icesoft.faces.renderkit.dom_html_basic.CommandLinkRenderer
                .getJavascriptHiddenFieldSetters(facesContext, (UICommand) uiComponent, parameters) +
                "iceSubmitPartial(form,this,event);" +
                str1 + getJavascriptHiddenFieldReSetters(facesContext, (UICommand) uiComponent, parameters) + str2 + "return false;";
    }

    private String getJavaScriptOnClickString(FacesContext facesContext, UIComponent uiComponent, Map parameters) {
        String str1 = "";
        String str2 = "";
        //myfaces queue does not serialize
        //until the request is sent, so we must delay
        if (EnvUtils.isMyFaces()) {
            str1 = "ice.onAfterUpdate(function() {";
            str2 = "});";
        }
        return com.icesoft.faces.renderkit.dom_html_basic.CommandLinkRenderer
                .getJavascriptHiddenFieldSetters(facesContext, (UICommand) uiComponent, parameters) +
                "iceSubmit(form,this,event);" +
                str1 + getJavascriptHiddenFieldReSetters(facesContext, (UICommand) uiComponent, parameters) + str2 + "return false;";
    }
}
