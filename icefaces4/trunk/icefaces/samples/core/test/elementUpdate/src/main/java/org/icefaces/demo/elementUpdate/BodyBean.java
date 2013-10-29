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

package org.icefaces.demo.elementUpdate;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;

@ManagedBean(name = "BodyBean")
@SessionScoped
public class BodyBean {

    public void generateBodyUpdate() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer = ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startUpdate("javax.faces.ViewBody");
                writer.startElement("body", null);
                writer.writeAttribute("class", "foo", "class");
                writer.writeAttribute("title", "fooTitle", "title");
                writer.writeAttribute("lang", "fooLang", "lang");
                writer.startElement("span", null);
                writer.writeAttribute("id", "status", "id");
                writer.endElement("span");
                writer.endElement("body");
                writer.endUpdate();
                writer.startEval();
                writer.write("var body = document.getElementsByTagName('body')[0];");
                writer.write("document.getElementById('status').innerHTML='BODY CLASS:'+body.className+' BODY TITLE:'+body.title+' BODY LANG:'+body.lang");
                writer.endEval();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
