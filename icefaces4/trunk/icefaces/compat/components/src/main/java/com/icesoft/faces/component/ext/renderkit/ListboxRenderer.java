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
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.context.FacesContext;
import java.util.Set;


public class ListboxRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.ListboxRenderer {
    protected void addJavaScript(FacesContext facesContext,
                                 UIComponent uiComponent, Element root,
                                 String currentValue, Set excludes) {
        if (((IceExtended) uiComponent).getPartialSubmit()) {
            boolean isSelectListbox =
                    (uiComponent instanceof HtmlSelectOneListbox) ||
                    (uiComponent instanceof HtmlSelectManyListbox);
            if (isSelectListbox) {

            }
            else {
                root.setAttribute(getEventType(uiComponent), "setFocus('');" + ICESUBMITPARTIAL);
            }
            excludes.add(getEventType(uiComponent));
            //bug 419
            if (uiComponent instanceof HtmlSelectOneListbox) {
                excludes.add(HTML.ONBLUR_ATTR);
                //root.setAttribute(HTML.ONBLUR_ATTR, this.ICESUBMITPARTIAL);
            }
        }
    }
	
    protected void addJavaScriptOverride(FacesContext facesContext,
                                 UIComponent uiComponent, Element root,
                                 String currentValue, Set excludes) {
        if (((IceExtended) uiComponent).getPartialSubmit()) {
            if (uiComponent instanceof HtmlSelectOneListbox) {
                Number partialSubmitDelay = (Number) uiComponent.getAttributes().get("partialSubmitDelay");
                Boolean partialSubmitOnBlur = (Boolean) uiComponent.getAttributes().get("partialSubmitOnBlur");
				if (partialSubmitOnBlur != null && partialSubmitOnBlur.booleanValue()) {
					String originalOnblur = root.getAttribute(HTML.ONBLUR_ATTR);
					originalOnblur = originalOnblur == null ? "" : originalOnblur;
					root.setAttribute(HTML.ONBLUR_ATTR, originalOnblur + ";if (this.previousValue != this.value){this.previousValue = this.value; Ice.selectChange(form,this,event," + partialSubmitDelay + ");} else {this.previousValue = this.value;}");
					String originalOnmousedown = root.getAttribute(HTML.ONMOUSEDOWN_ATTR);
					originalOnmousedown = originalOnmousedown == null ? "" : originalOnmousedown;
					root.setAttribute(HTML.ONMOUSEDOWN_ATTR, originalOnmousedown + ";if (!this.previousValue) this.previousValue = this.value;");
				} else {
					String originalOnchange = root.getAttribute(HTML.ONCHANGE_ATTR);
					originalOnchange = originalOnchange == null ? "" : originalOnchange;
					root.setAttribute(HTML.ONCHANGE_ATTR, originalOnchange + ";setFocus('');Ice.selectChange(form,this,event,"+partialSubmitDelay+");");
				}
            } else if (uiComponent instanceof HtmlSelectManyListbox) {
                Number partialSubmitDelay = (Number) uiComponent.getAttributes().get("partialSubmitDelay");
                Boolean partialSubmitOnBlur = (Boolean) uiComponent.getAttributes().get("partialSubmitOnBlur");
				if (partialSubmitOnBlur != null && partialSubmitOnBlur.booleanValue()) {
					String originalOnblur = root.getAttribute(HTML.ONBLUR_ATTR);
					originalOnblur = originalOnblur == null ? "" : originalOnblur;
					root.setAttribute(HTML.ONBLUR_ATTR, originalOnblur + ";var v = []; var o = this.options; for (var i = 0; i < o.length; i++) if (o[i].selected) v.push(i); var d = false; if (!this.p) d = true; else if (this.p.length != v.length) d = true; else for (var j = 0; j < v.length; j++) if (this.p[j] != v[j]) d = true; if (d) {this.p = v; Ice.selectChange(form,this,event," + partialSubmitDelay+"); } else this.p = v;");
					String originalOnmousedown = root.getAttribute(HTML.ONMOUSEDOWN_ATTR);
					originalOnmousedown = originalOnmousedown == null ? "" : originalOnmousedown;
					root.setAttribute(HTML.ONMOUSEDOWN_ATTR, originalOnmousedown + ";if (!this.p) { var v = []; var o = this.options; for (var i = 0; i < o.length; i++) if (o[i].selected) v.push(i); this.p = v; }");
				} else {
					String originalOnchange = root.getAttribute(HTML.ONCHANGE_ATTR);
					originalOnchange = originalOnchange == null ? "" : originalOnchange;
					root.setAttribute(HTML.ONCHANGE_ATTR, originalOnchange + "setFocus('');Ice.selectChange(form,this,event,"+partialSubmitDelay+");");
				}			
			}
        }
    }
}
