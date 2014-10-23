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

package org.icefaces.mobi.component.smallview;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.icefaces.ace.util.Utils;

public class SmallViewRenderer extends Renderer {

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        Iterator<UIComponent> iter = component.getChildren().iterator();
        while( iter.hasNext() ){
            UIComponent child = iter.next();
            Utils.renderChild(facesContext, child);
        }
        
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}

