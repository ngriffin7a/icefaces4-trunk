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

package org.icefaces.util;

import javax.faces.component.NamingContainer;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility API for managing the element focus in the browser.
 */
public class FocusController {
    private static final Pattern ClientIdPattern = Pattern.compile("^(([\\w\\_]*)\\" + UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()) + "([\\w\\_]*))*$");

    /**
     * Acquire the ID of the currently focused element in the browser.
     *
     * @param context the FacesContext
     * @return the element ID
     */
    public static String getReceivedFocus(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map map = externalContext.getRequestParameterMap();
        String focusedElement = (String) map.get("ice.focus");
        return focusedElement != null && ClientIdPattern.matcher(focusedElement).matches() && !focusedElement.equals("null") && !focusedElement.equals("undefined") ? focusedElement : null;
    }

    /**
     * Set the element ID that should received focus during next update.
     *
     * @param context the FacesContext
     * @param id      the element ID
     */
    public static void setFocus(FacesContext context, String id) {
        if (id != null && !"".equals(id)) {
            context.getExternalContext().getRequestMap().put(FocusController.class.getName(), id);
        }
    }

    /**
     * Get the element ID the will receive focus during next update.
     *
     * @param context the FacesContext
     * @return the element ID
     */
    public static String getFocus(FacesContext context) {
        return (String) context.getExternalContext().getRequestMap().get(FocusController.class.getName());
    }

    /**
     * Test if focus is defined for a certain element.
     *
     * @param context the FacesContext
     * @return return true if focus is defined
     */
    public static boolean isFocusSet(FacesContext context) {
        return context.getExternalContext().getRequestMap().containsKey(FocusController.class.getName());
    }

    public static void manageFocus(FacesContext facesContext) {
        if (EnvUtils.isFocusManaged(facesContext)) {
            String focusId = FocusController.getReceivedFocus(facesContext);
            boolean focusNotYetSet = !FocusController.isFocusSet(facesContext);

            //preserve focus received if not already set by one of the components
            if (focusNotYetSet && focusId != null) {
                FocusController.setFocus(facesContext, focusId);
            }
            if (FocusController.isFocusSet(facesContext)) {
                JavaScriptRunner.runScript(facesContext, "ice.applyFocus('" + FocusController.getFocus(facesContext) + "');");
            }
        }
    }
}
