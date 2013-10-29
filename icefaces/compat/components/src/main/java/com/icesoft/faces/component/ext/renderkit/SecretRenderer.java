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

import com.icesoft.faces.component.IceExtended;
import com.icesoft.faces.component.ext.HtmlInputSecret;
import com.icesoft.faces.component.ext.KeyEvent;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

public class SecretRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.SecretRenderer {

    //private static final String[] passThruAttributes = ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_INPUTSECRET);
    //handled onkeypress onfocus onblur onmousedown
    private final static String[] passThruAttributes =
            new String[]{HTML.ACCESSKEY_ATTR, HTML.ALT_ATTR, HTML.DIR_ATTR, HTML.LANG_ATTR, HTML.MAXLENGTH_ATTR, HTML.ONCHANGE_ATTR, HTML.ONCLICK_ATTR, HTML.ONDBLCLICK_ATTR, HTML.ONKEYDOWN_ATTR, HTML.ONKEYUP_ATTR, HTML.ONMOUSEMOVE_ATTR, HTML.ONMOUSEOUT_ATTR, HTML.ONMOUSEOVER_ATTR, HTML.ONMOUSEUP_ATTR, HTML.ONSELECT_ATTR, HTML.SIZE_ATTR, HTML.STYLE_ATTR, HTML.TABINDEX_ATTR, HTML.TITLE_ATTR};

    protected void renderEnd(FacesContext facesContext, UIComponent uiComponent,
                             String currentValue) throws IOException {

        validateParameters(facesContext, uiComponent, UIInput.class);

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);

        if (!domContext.isInitialized()) {
            Element root = domContext.createElement("input");
            domContext.setRootNode(root);
            setRootElementId(facesContext, root, uiComponent);
            root.setAttribute("type", "password");
            root.setAttribute("name", uiComponent.getClientId(facesContext));
        }

        Element root = (Element) domContext.getRootNode();

        String dir = (String) uiComponent.getAttributes().get("dir");
        if (dir != null) {
            root.setAttribute("dir", dir);
        }

        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            root.setAttribute("class", styleClass);
        }
        PassThruAttributeRenderer.renderHtmlAttributes(
                facesContext, uiComponent, passThruAttributes);
        String[] attributes = new String[]{HTML.DISABLED_ATTR, HTML.READONLY_ATTR};
        Object attribute;
        for (int i = 0; i < attributes.length; i++) {
            attribute = uiComponent.getAttributes().get(attributes[i]);
            if (attribute instanceof Boolean && ((Boolean) attribute).booleanValue()) {
                root.setAttribute(attributes[i], attributes[i]);
            }
        }

        // render the current value of the component as the value of the "value"
        // attribute  if and only if the value of the component attribute 
        // "redisplay" is the string "true"
        if (redisplayAttributeIsTrue(uiComponent, facesContext) && currentValue != null) {
            root.setAttribute("value", currentValue);
        } else {
            root.setAttribute("value", "");
        }

        HtmlInputSecret secret = (HtmlInputSecret) uiComponent;
        //Add the enter key behavior by default
        root.setAttribute("onkeyup", combinedPassThru(secret.getOnkeyup(), this.ICESUBMIT));
		// avoid double-submission
		root.setAttribute("onkeypress", combinedPassThru(secret.getOnkeypress(), "ice.cancelEnterKeyEvent(event, this);"));
        // set the focus id
        root.setAttribute("onfocus", combinedPassThru(secret.getOnfocus(), "setFocus(this.id);"));
        // clear focus id
        String applicationOnblur = secret.getOnblur();
        String rendererOnblur = null;
        if (((IceExtended) uiComponent).getPartialSubmit()) {
            rendererOnblur = "setFocus('');iceSubmitPartial(form,this,event); return false;";
        } else {
            rendererOnblur = "setFocus('');";
        }
        root.setAttribute("onblur", combinedPassThru(applicationOnblur, rendererOnblur));

        //fix for ICE-2514
        String mousedownScript = (String) uiComponent.getAttributes().get(HTML.ONMOUSEDOWN_ATTR);
        root.setAttribute(HTML.ONMOUSEDOWN_ATTR, combinedPassThru(mousedownScript, "this.focus();"));
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        HtmlInputSecret inputSecret = (HtmlInputSecret) uiComponent;
        // check if we are processing a partial submit
        Map requestParameterMap =
                facesContext.getExternalContext().getRequestParameterMap();
        String partial = "partial";
        boolean test = Boolean.valueOf(
                (String) requestParameterMap.get(partial)).booleanValue();
        if (test) {
            // force the redisplay for partialSubmit enabled inputSecret components
            if (!inputSecret.isRedisplay()) {
                inputSecret.setRedisplay(true);
            }
        }

        super.decode(facesContext, uiComponent);
        if (Util.isEventSource(facesContext, uiComponent)) {
            queueEventIfEnterKeyPressed(facesContext, uiComponent);
        }

    }

    public void queueEventIfEnterKeyPressed(FacesContext facesContext,
                                            UIComponent uiComponent) {
        try {
            KeyEvent keyEvent =
                    new KeyEvent(uiComponent, facesContext.getExternalContext().getRequestParameterMap());
            if (keyEvent.getKeyCode() == KeyEvent.CARRIAGE_RETURN) {
                uiComponent.queueEvent(new ActionEvent(uiComponent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param uiComponent
     * @return boolean
     */
    private boolean redisplayAttributeIsTrue(UIComponent uiComponent, FacesContext facesContext) {
        if (Util.isEventSource(facesContext, uiComponent)) {
            return true;
        }
        Object redisplayAttribute =
                uiComponent.getAttributes().get("redisplay");
        return redisplayAttribute != null && redisplayAttribute.toString().toLowerCase().equals("true");
    }
}