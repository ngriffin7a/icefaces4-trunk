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

package com.sun.faces.systest.model.ajax;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.FacesException;

@ManagedBean
@RequestScoped
public class InsertDeleteBean {

    public String insertBefore() {
  System.out.println("InsertDeleteBean: insertBefore() for bean version="+this);
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                writer.startDocument();
                writer.startInsertBefore("hr");
 //swapped both of the next 2 lines to make this work properly ICE-4950
 //               writer.writeAttribute("id", "h2before", "id");
 //             writer.startElement("h2", null);
                writer.startElement("h2", null);
                writer.writeAttribute("id", "h2before", "id");
  //add another "h2", null to the writer:-
                writer.startElement("h2", null);
                writer.writeText("BEFORE", null, null);
                writer.endElement("h2");
                writer.endInsert();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
   System.out.println("InsertDeleteBean: insertBefore returns null for bean="+this);
        return null;

    }

    public String insertAfter() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                writer.startDocument();
                writer.startInsertAfter("hr");
                writer.startElement("h2", null);
                writer.writeAttribute("id", "h2after", "id");
                writer.writeText("AFTER", null, null);
                writer.endElement("h2");
                writer.endInsert();
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;

    }


    public String removeBefore() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                writer.startDocument();
                writer.delete("h2before");
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;

    }

    public String removeAfter() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extContext = ctx.getExternalContext();
        if (ctx.getPartialViewContext().isAjaxRequest()) {
            try {
                PartialResponseWriter writer =
                      ctx.getPartialViewContext().getPartialResponseWriter();
                extContext.setResponseContentType("text/xml");
                extContext.addResponseHeader("Cache-Control", "no-cache");
                writer.startDocument();
                writer.delete("h2after");
                writer.endDocument();
                writer.flush();
                ctx.responseComplete();
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return null;    
    }

}
