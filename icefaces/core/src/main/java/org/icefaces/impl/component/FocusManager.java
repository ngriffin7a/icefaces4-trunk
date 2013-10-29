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

package org.icefaces.impl.component;

import org.icefaces.component.Focusable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class FocusManager extends UIComponentBase {
    private final static Logger log = Logger.getLogger(FocusManager.class.getName());
    private final static Random RANDOM = new Random();

    public String getFamily() {
        return "javax.faces.Output";
    }

    public String getFor() {
        return (String) getStateHelper().eval("for");
    }

    public void setFor(String id) {
        getStateHelper().put("for", id);
    }

    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", this);
        writer.writeAttribute("id", getClientId(context), null);
        //apply focus only if not already specified in the browser
        String iceFocus = context.getExternalContext().getRequestParameterMap().get("ice.focus");
        if (iceFocus == null) {
            Map attributes = context.getAttributes();
            UIComponent source = null;
            UIInput invalidUIInput = (UIInput) attributes.get(DetectInvalidChild.class.getName());
            if (invalidUIInput == null) {
                //set focus on the specified component
                String focusFor = getFor();
                if (focusFor != null && !"".equals(focusFor)) {
                    UIComponent c = findComponent(this, focusFor);
                    if (c instanceof UIInput || c instanceof Focusable) {
                        source = c;
                    } else {
                        log.warning("The \"for\" attribute points to a component that is not an instance of UIInput");
                    }
                }

                //assuming that grand/child components are valid, selecting the first UIInput component
                if (source == null) {
                    LinkedList<UIComponent> queue = new LinkedList();
                    queue.add(this);
                    while (!queue.isEmpty()) {
                        UIComponent c = queue.removeFirst();
                        if (c instanceof UIInput || c instanceof Focusable) {
                            source = c;
                            break;
                        }
                        queue.addAll(c.getChildren());
                    }
                }
            } else {
                source = invalidUIInput;
            }

            if (source != null) {
                writer.startElement("script", null);
                writer.writeAttribute("type", "text/javascript", null);

                String id;
                if (source instanceof Focusable) {
                    id = ((Focusable) source).getFocusedElementId();
                } else {
                    id = source.getClientId(context);
                }

                writer.writeText("try { ice.applyFocus('", null);
                writer.writeText(id, null);
                writer.writeText("'); } catch (ex) {ice.log.warn(ice.logger, 'failed to focus element ", null);
                writer.writeText(id, null);
                writer.writeText("'); }//", null);
                writer.writeText(RANDOM.nextLong(), null);
                writer.endElement("script");
            }
        }
        writer.endElement("span");
    }

    public static class DetectInvalidChild implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            UIInput source = (UIInput) event.getSource();
            Map<Object, Object> attributes = context.getAttributes();
            if (!attributes.containsKey(DetectInvalidChild.class.getName()) && !source.isValid() && source.isRendered()) {
                attributes.put(DetectInvalidChild.class.getName(), source);
            }
        }

        public boolean isListenerForSource(Object source) {
            //test if component is a grand/child
            if (source instanceof UIInput) {
                UIComponent component = (UIComponent) source;
                UIComponent cursor = component.getParent();
                while (cursor != null) {
                    if (cursor instanceof FocusManager) {
                        return true;
                    }
                    cursor = cursor.getParent();
                }
            }

            return false;
        }
    }

    public static UIComponent findComponent(UIComponent base, String id) {
        if (id.equals(base.getId()))
            return base;

        UIComponent kid;
        UIComponent result = null;
        Iterator<UIComponent> kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            kid = kids.next();
            if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
            result = findComponent(kid, id);
            if (result != null) {
                break;
            }
        }
        return result;
    }
}
