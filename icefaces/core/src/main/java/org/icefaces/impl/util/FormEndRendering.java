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

package org.icefaces.impl.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class that can be used to render markup within forms. The markup is rendered just before 'form' end tag.
 */
public class FormEndRendering {

    /**
     * Register FormEndRenderer to the list of renderers.
     *
     * @param context  current FacesContext
     * @param renderer the renderer
     */
    public static void addRenderer(FacesContext context, FormEndRenderer renderer) {
        Map attributes = context.getAttributes();
        ArrayList list = (ArrayList) attributes.get(FormEndRendering.class.getName());
        if (list == null) {
            list = new ArrayList();
            attributes.put(FormEndRendering.class.getName(), list);
        }
        list.add(renderer);
    }

    /**
     * Method used by form renderers to invoke all the registered renderers before encodeEnd is executed.
     *
     * @param context current FacesContext
     * @param form    the form component
     * @throws IOException
     */
    public static void renderIntoForm(FacesContext context, UIComponent form) throws IOException {
        ArrayList o = (ArrayList) context.getAttributes().get(FormEndRendering.class.getName());
        if (o != null) {
            Iterator i = o.iterator();
            while (i.hasNext()) {
                FormEndRenderer formEndRenderer = (FormEndRenderer) i.next();
                formEndRenderer.encode(context, form);
            }
        }
    }
}
