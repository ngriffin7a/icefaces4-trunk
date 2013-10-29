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

package org.icefaces.impl.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;

public class RendererWrapper extends Renderer {
    private Renderer renderer;

    public RendererWrapper(Renderer renderer) {
        this.renderer = renderer;
    }

    public Renderer getWrappedRenderer() {
        return renderer;
    }

    public void setWrappedRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void decode(FacesContext context, UIComponent component) {
        renderer.decode(context, component);
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        renderer.encodeBegin(context, component);
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        renderer.encodeChildren(context, component);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        renderer.encodeEnd(context, component);
    }

    public String convertClientId(FacesContext context, String clientId) {
        return renderer.convertClientId(context, clientId);
    }

    public boolean getRendersChildren() {
        return renderer.getRendersChildren();
    }

    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return renderer.getConvertedValue(context, component, submittedValue);
    }
}
