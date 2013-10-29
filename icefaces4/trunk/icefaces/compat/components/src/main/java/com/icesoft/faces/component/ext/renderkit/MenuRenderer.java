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
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Set;


public class MenuRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.MenuRenderer {

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {

        super.encodeEnd(facesContext, uiComponent);
    }

    protected void addJavaScript(FacesContext facesContext,
                                 UIComponent uiComponent, Element root,
                                 String currentValue, Set excludes) {
        if (((IceExtended) uiComponent).getPartialSubmit()) {
            boolean isSelectMenu =
                    (uiComponent instanceof HtmlSelectOneMenu) ||
                    (uiComponent instanceof HtmlSelectManyMenu);
            if (isSelectMenu) {

            }
            else {
                root.setAttribute(getEventType(uiComponent), "setFocus('');" +
                        ICESUBMITPARTIAL);
            }
            excludes.add(getEventType(uiComponent));
        }
    }
	
    protected void addJavaScriptOverride(FacesContext facesContext,
                                 UIComponent uiComponent, Element root,
                                 String currentValue, Set excludes) {
        if (((IceExtended) uiComponent).getPartialSubmit()) {
            if (uiComponent instanceof HtmlSelectOneMenu) {
                Number partialSubmitDelay = (Number) uiComponent.getAttributes().get("partialSubmitDelay");
                String script = ";if (this.previousValue != this.value){this.previousValue = this.value; Ice.selectChange(form,this,event," + partialSubmitDelay + ");} else {this.previousValue = this.value;}";
                Boolean partialSubmitOnBlur = (Boolean) uiComponent.getAttributes().get("partialSubmitOnBlur");
				if (partialSubmitOnBlur != null && partialSubmitOnBlur.booleanValue()) {
					String originalOnblur = root.getAttribute(HTML.ONBLUR_ATTR);
					originalOnblur = originalOnblur == null ? "" : originalOnblur;
					root.setAttribute(HTML.ONBLUR_ATTR, originalOnblur + script);
				} else {
					String originalOnkeyup = root.getAttribute(HTML.ONKEYUP_ATTR);
					originalOnkeyup = originalOnkeyup == null ? "" : originalOnkeyup;
					root.setAttribute(HTML.ONKEYUP_ATTR, originalOnkeyup + script);
					String originalOnclick = root.getAttribute(HTML.ONCLICK_ATTR);
					originalOnclick = originalOnclick == null ? "" : originalOnclick;
                    root.setAttribute(HTML.ONCLICK_ATTR, originalOnclick + script);
                    root.setAttribute(HTML.ONCHANGE_ATTR, script);
                }
				String originalOnfocus = root.getAttribute(HTML.ONFOCUS_ATTR);
				originalOnfocus = originalOnfocus == null ? "" : originalOnfocus;
				root.setAttribute(HTML.ONFOCUS_ATTR, originalOnfocus + ";if (!this.previousValue) this.previousValue = this.value;");
            } else if (uiComponent instanceof HtmlSelectManyMenu) {
                Number partialSubmitDelay = (Number) uiComponent.getAttributes().get("partialSubmitDelay");
                String script = ";var v = []; var o = this.options; for (var i = 0; i < o.length; i++) if (o[i].selected) v.push(i); var d = false; if (!this.p) d = true; else if (this.p.length != v.length) d = true; else for (var j = 0; j < v.length; j++) if (this.p[j] != v[j]) d = true; if (d) {this.p = v; Ice.selectChange(form,this,event," + partialSubmitDelay+"); } else this.p = v;";
                Boolean partialSubmitOnBlur = (Boolean) uiComponent.getAttributes().get("partialSubmitOnBlur");
				if (partialSubmitOnBlur != null && partialSubmitOnBlur.booleanValue()) {
					String originalOnblur = root.getAttribute(HTML.ONBLUR_ATTR);
					originalOnblur = originalOnblur == null ? "" : originalOnblur;
					root.setAttribute(HTML.ONBLUR_ATTR, originalOnblur + script);
				} else {
					String originalOnkeyup = root.getAttribute(HTML.ONKEYUP_ATTR);
					originalOnkeyup = originalOnkeyup == null ? "" : originalOnkeyup;
					root.setAttribute(HTML.ONKEYUP_ATTR, originalOnkeyup + script);
					String originalOnclick = root.getAttribute(HTML.ONCLICK_ATTR);
					originalOnclick = originalOnclick == null ? "" : originalOnclick;
					root.setAttribute(HTML.ONCLICK_ATTR, originalOnclick + script);
				}
				String originalOnfocus = root.getAttribute(HTML.ONFOCUS_ATTR);
				originalOnfocus = originalOnfocus == null ? "" : originalOnfocus;
				root.setAttribute(HTML.ONFOCUS_ATTR, originalOnfocus + ";if (!this.p) { var v = []; var o = this.options; for (var i = 0; i < o.length; i++) if (o[i].selected) v.push(i); this.p = v; }");
			}
        }
    }
}