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

import com.icesoft.faces.component.AttributeConstants;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import org.w3c.dom.Element;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ButtonRenderer extends DomBasicRenderer {
    private static final String[] passThruAttributes = AttributeConstants.getAttributes(AttributeConstants.H_COMMANDBUTTON);

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
//        printParams();
        validateParameters(facesContext, uiComponent, UICommand.class);
        if (isStatic(uiComponent)) {
            return;
        }
        if (!didThisButtonInvokeSubmit(facesContext, uiComponent)) {
            return;
        }
        String type = ((String) uiComponent.getAttributes().get("type")).toLowerCase();
        if ((type != null) && (type.equals("reset"))) {
            return;
        }
        uiComponent.queueEvent(new ActionEvent(uiComponent));
    }

    /**
     * @param facesContext
     * @param uiComponent
     * @return boolean
     */
    protected boolean didThisButtonInvokeSubmit(
            FacesContext facesContext, UIComponent uiComponent) {
        //find if the form submitted by a textField, workaround to deal with the default behaviour of the browser
        //(e.g.) if a form has a button on it, and enter key pressed on a text field, form submitted by the first intance of button
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String componentClientId = uiComponent.getClientId(facesContext);
        return requestParameterMap.containsKey(componentClientId);
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UICommand.class);

        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        String clientId = uiComponent.getClientId(facesContext);

        if (!domContext.isInitialized()) {
            Element root = domContext.createElement("input");
            domContext.setRootNode(root);
            root.setAttribute("name", clientId);
        }
        Element root = (Element) domContext.getRootNode();

        setRootElementId(facesContext, root, uiComponent);

        // according to the Sun documentation for ButtonRenderer,
        // type: valid values are "submit" and "reset"; default to "submit".
        // But the docs for the image attribute declare that 'image' is also
        // a valid value for the type attribute. 
        // The logic here holds true when there is no image attribute
        // But suns JSF implementation allows for type button, we allow it as well.
        String typeAttribute =
                ((String) uiComponent.getAttributes().get("type"));
        if (typeAttribute == null || (!typeAttribute.equals("reset") &&
                (!typeAttribute.equals("button")))) {
            typeAttribute = "submit";
        }
        typeAttribute = typeAttribute.toLowerCase();
        uiComponent.getAttributes().put("type", typeAttribute);

        // If image attribute is specified, the type is "image"
        // otherwise take the type from the type attribute and set a label
        // with value specified by the value attribute
        String imageAttribute =
                (String) uiComponent.getAttributes().get("image");
        if (imageAttribute != null) {
            typeAttribute = "image";
            root.setAttribute("type", typeAttribute);

            //Paths to resources can be dependent on the environment so
            //we should use proper JSF techniques to resolve them.
            String pathToImage = getResourceURL(facesContext, imageAttribute);

            root.setAttribute("src", pathToImage);
            root.removeAttribute("value");
        } else {
            root.setAttribute("type", typeAttribute);
            String label = "";
            Object componentValue = ((UICommand) uiComponent).getValue();
            if (componentValue != null) {
                label = componentValue.toString();
            } else {
                label = "";
            }
            root.setAttribute("value", label);
            root.removeAttribute("src");
        }

        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        if (styleClass != null) {
            root.setAttribute("class", styleClass);
        }

        JavascriptContext.fireEffect(uiComponent, facesContext);

        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);
        PassThruAttributeRenderer.renderBooleanAttributes(facesContext, 
                uiComponent, root, PassThruAttributeRenderer.EMPTY_STRING_ARRAY);       
        //add iceSubmit for image and submit button only
        if (typeAttribute.equals("submit") || typeAttribute.equals("image")) {
            renderOnClick(uiComponent, root);
        }

        Map parameterMap = getParameterMap(uiComponent);
        Iterator parameterKeys = parameterMap.keySet().iterator();
        while (parameterKeys.hasNext()) {
            String nextKey = (String) parameterKeys.next();
            FormRenderer.addHiddenField(facesContext, nextKey);
        }
        domContext.stepOver();
    }

    /**
     * @param uiComponent
     * @param root
     */
    protected void renderOnClick(UIComponent uiComponent, Element root) {
        String onclick = (String) uiComponent.getAttributes().get("onclick");
        String submitCode = this.ICESUBMIT + "return false;";
        root.setAttribute("onclick", combinedPassThru(onclick, submitCode));
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UICommand.class);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UICommand.class);
    }

    public static void printParams() {
        Map paramValuesMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        Iterator it = paramValuesMap.entrySet().iterator();
        Map.Entry entry;
        String key;
        String[] values;
        while (it.hasNext()) {
            entry = (Map.Entry) it.next();
            key = (String) entry.getKey();
            values = (String[]) entry.getValue();
            System.out.print(key);
            System.out.print(" = ");
            for (int i = 0; i < values.length; i++) {
                System.out.print(values[i]);
                System.out.print(", ");
            }
            System.out.println();
        }
    }
}
