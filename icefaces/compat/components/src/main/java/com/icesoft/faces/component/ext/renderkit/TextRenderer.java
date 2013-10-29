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
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.KeyEvent;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class TextRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.TextRenderer {

    //private static final String[] passThruAttributes = ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_INPUTTEXT);
    //handled onkeypress onfocus onblur onmousedown
    private static final String[] passThruAttributes =
            new String[]{HTML.ACCESSKEY_ATTR, HTML.ALT_ATTR, HTML.DIR_ATTR, HTML.LANG_ATTR, HTML.MAXLENGTH_ATTR, HTML.ONCHANGE_ATTR, HTML.ONCLICK_ATTR, HTML.ONDBLCLICK_ATTR, HTML.ONKEYDOWN_ATTR, HTML.ONKEYUP_ATTR, HTML.ONMOUSEMOVE_ATTR, HTML.ONMOUSEOUT_ATTR, HTML.ONMOUSEOVER_ATTR, HTML.ONMOUSEUP_ATTR, HTML.ONSELECT_ATTR, HTML.SIZE_ATTR, HTML.STYLE_ATTR, HTML.TABINDEX_ATTR, HTML.TITLE_ATTR};

    protected void addJavaScript(FacesContext facesContext,
                                 UIComponent uiComponent, Element root,
                                 String currentValue) {
        //exclude following events
//        excludes.add("onkeypress");
//        excludes.add("onfocus");
//        excludes.add("onblur");
        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);
        String onkeyup = ((HtmlInputText) uiComponent).getOnkeyup();
        String onfocus = ((HtmlInputText) uiComponent).getOnfocus();
        String onblur = ((HtmlInputText) uiComponent).getOnblur();

        //Add the enter key behavior by default
        root.setAttribute("onkeyup", combinedPassThru(onkeyup, this.ICESUBMIT));
        // set the focus id
        root.setAttribute("onfocus", combinedPassThru(onfocus, "setFocus(this.id);"));
        // clear focus id
        root.setAttribute("onblur", combinedPassThru(onblur, "setFocus('');"));

        if (((IceExtended) uiComponent).getPartialSubmit()) {
            root.setAttribute("onblur", combinedPassThru(onblur, "setFocus('');" +
                    "iceSubmitPartial(form,this,event); return false;"));
        }

    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        super.decode(facesContext, uiComponent);
        Object focusId = facesContext.getExternalContext()
                .getRequestParameterMap().get(FormRenderer.getFocusElementId());
        if (focusId != null) {
            if (focusId.toString()
                    .equals(uiComponent.getClientId(facesContext))) {
                ((HtmlInputText) uiComponent).setFocus(true);
            } else {
                ((HtmlInputText) uiComponent).setFocus(false);
            }
        }

        if (Util.isEventSource(facesContext, uiComponent)) {
            queueEventIfEnterKeyPressed(facesContext, uiComponent);
        }
    }


    public void queueEventIfEnterKeyPressed(FacesContext facesContext,
                                            UIComponent uiComponent) {
        try {
            KeyEvent keyEvent =
                    new KeyEvent(uiComponent, facesContext.getExternalContext()
                            .getRequestParameterMap());
            if (keyEvent.getKeyCode() == KeyEvent.CARRIAGE_RETURN) {
                uiComponent.queueEvent(new ActionEvent(uiComponent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
