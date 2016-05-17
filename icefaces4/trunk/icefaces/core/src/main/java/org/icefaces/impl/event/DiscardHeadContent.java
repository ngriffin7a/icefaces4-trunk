/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.ArrayList;
import java.util.List;

public class DiscardHeadContent implements SystemEventListener {
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        UIComponent c = (UIComponent) event.getSource();
        ArrayList filteredChildren = new ArrayList();
        List<UIComponent> headChildren = c.getChildren();
        //stop rendering head content with the exception of resources defined by h:outputScript or h:outputStylesheet
        for (UIComponent child: headChildren) {
            final String type = child.getRendererType();
            if (type != null && type.startsWith("javax.faces.resource")) {
                filteredChildren.add(child);
            }
        }
        headChildren.clear();
        headChildren.addAll(filteredChildren);

    }

    public boolean isListenerForSource(Object source) {
        return EnvUtils.isPortal() &&
                source instanceof UIComponent && "javax.faces.Head".equals(((UIComponent) source).getRendererType());
    }
}
