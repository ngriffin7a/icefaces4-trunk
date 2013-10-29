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

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.context.DOMContext;
import org.w3c.dom.Element;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class LinkRenderer extends DomBasicRenderer {

    protected CommandLinkRenderer commandLinkRendererDelegate;
    protected OutputLinkRenderer outputLinkRendererDelegate;

    public LinkRenderer() {
        commandLinkRendererDelegate = new CommandLinkRenderer();
        outputLinkRendererDelegate = new OutputLinkRenderer();
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        boolean isCommandLink = uiComponent instanceof UICommand;
        boolean isOutputLink = uiComponent instanceof UIOutput;
        if (isCommandLink) {
            commandLinkRendererDelegate.decode(facesContext, uiComponent);
        }
        if (isOutputLink) {
            outputLinkRendererDelegate.decode(facesContext, uiComponent);
        }
        return;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        boolean isCommandLink = uiComponent instanceof UICommand;
        boolean isOutputLink = uiComponent instanceof UIOutput;
        if (isCommandLink) {
            commandLinkRendererDelegate.encodeBegin(facesContext, uiComponent);
        }
        if (isOutputLink) {
            outputLinkRendererDelegate.encodeBegin(facesContext, uiComponent);
        }

        return;
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent)
            throws IOException {
        boolean isCommandLink = uiComponent instanceof UICommand;
        boolean isOutputLink = uiComponent instanceof UIOutput;
        if (isCommandLink) {
            commandLinkRendererDelegate
                    .encodeChildren(facesContext, uiComponent);
        }
        if (isOutputLink) {
            outputLinkRendererDelegate
                    .encodeChildren(facesContext, uiComponent);
        }
        return;
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        boolean isCommandLink = uiComponent instanceof UICommand;
        boolean isOutputLink = uiComponent instanceof UIOutput;
        if (isCommandLink) {
            commandLinkRendererDelegate.encodeEnd(facesContext, uiComponent);
        }
        if (isOutputLink) {
            outputLinkRendererDelegate.encodeEnd(facesContext, uiComponent);
        }
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        Element root = (Element) domContext.getRootNode();
        return;
    }

}
