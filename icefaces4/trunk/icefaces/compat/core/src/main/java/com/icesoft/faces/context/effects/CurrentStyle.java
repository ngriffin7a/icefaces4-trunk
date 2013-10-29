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

package com.icesoft.faces.context.effects;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.util.CoreUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import java.util.HashMap;
import java.util.Map;

/**
 * Effects can change a components style. This class keeps track of these
 * changes
 */
public class CurrentStyle implements Serializable {

    /**
     * Name of field used to send CSS Updated
     */
    public static final String CSS_UPDATE_FIELD = "icefacesCssUpdates";
    /**
     * String uploaded
     */
    private String cssString;

    /**
     * Last string uplodaed
     */
    private String lastCssString;

    /**
     * Constant for visible  = true
     */
    private static final String DISPLAY_ON = "display:block;";

    /**
     * Constant for visible =false
     */
    private static final String DISPLAY_OFF = "display:none;";

    private static Logger log = Logger.getLogger("com.icesoft.faces.compat");

    /**
     * @param cssString
     */
    public CurrentStyle(String cssString) {
        this.cssString = cssString;
    }

    public String getCssString() {
        return cssString;
    }

    public String getLastCssString() {
        return lastCssString;
    }

    public void setLastCssString(String lastCssString) {
        this.lastCssString = lastCssString;
    }

    /**
     * Apply CSS changes to the rendered componenent
     *
     * @param facesContext
     * @param uiComponent
     */
    public static void apply(FacesContext facesContext,
                             UIComponent uiComponent) {
        apply(facesContext, uiComponent, null, null, null);
    }

    public static void apply(FacesContext facesContext, UIComponent uiComponent, 
            Element targetElement, String style) {
        apply(facesContext, uiComponent, targetElement, style, null);
    }
    
    public static void apply(FacesContext facesContext, UIComponent uiComponent, 
            ResponseWriter writer) {
        apply(facesContext, uiComponent, null, null, writer);        
    }
    /**
     * Apply css changes to rendered component
     *
     * @param facesContext
     * @param uiComponent
     * @param targetElement
     * @param style
     */
    public static void apply(FacesContext facesContext, UIComponent uiComponent, 
                             Element targetElement, String style, ResponseWriter writer) {
        if(targetElement == null && writer == null) {
            DOMContext domContext =
                    DOMContext.getDOMContext(facesContext, uiComponent);
            Object node = domContext.getRootNode();
            if (node == null || !(node instanceof Element)) {
                return;
            }
            Element root = (Element) node;
            targetElement = root;
        }
        String jspStyle = (String) uiComponent.getAttributes().get("style");
        if (log.isLoggable(Level.FINEST)) {
            if (jspStyle != null) {
                log.finest("Existing style [" + jspStyle + "]");
            }
        }

        if (style != null) {
            if (jspStyle == null) {
                jspStyle = "";
            }
            jspStyle += style;
        }

        Boolean visibility =
                (Boolean) uiComponent.getAttributes().get("visible");
        // default to true if visibility is null
        boolean visible = true;
        if (visibility != null) {
            visible = visibility.booleanValue();
        }
        CurrentStyle currentStyle =
                (CurrentStyle) uiComponent.getAttributes().get("currentStyle");
        if (currentStyle != null) {
            String appendedStyle = currentStyle.cssString;

            currentStyle.lastCssString = currentStyle.cssString;
            if (appendedStyle != null) {
                if (jspStyle == null) {
                    jspStyle = appendedStyle;
                } else {
                    jspStyle += ";" + appendedStyle;
                }
            }

        }

        if (visible) {
            if (jspStyle != null) {
                int startI = jspStyle.indexOf(DISPLAY_OFF);
                if (startI != -1) {
                    String start = "";
                    if (startI > 0) {
                        start = jspStyle.substring(0, startI);
                    }
                    int endI = startI + DISPLAY_OFF.length();
                    String end = "";
                    if (endI < jspStyle.length()) {
                        end = jspStyle.substring(endI);
                    }
                    jspStyle = start + end;
                }
            }
        } else {

            if (jspStyle == null) {
                jspStyle = DISPLAY_OFF;
            } else {
                jspStyle += DISPLAY_OFF;
            }
        }
        if (log.isLoggable(Level.FINEST)) {
            if (jspStyle != null) {
                log.finest("JSP Style [" + jspStyle + "]");
            }
        }
        if (targetElement != null) {
            if(jspStyle != null && jspStyle.length() > 0)
                targetElement.setAttribute(HTML.STYLE_ATTR, jspStyle);
            else
                targetElement.removeAttribute(HTML.STYLE_ATTR);
        }
        if (writer != null && jspStyle != null) {
            try {
                writer.writeAttribute(HTML.STYLE_ATTR, jspStyle, null);
            } catch (IOException exception) {
                if (log.isLoggable(Level.SEVERE)) {
                    log.log(Level.SEVERE,"Exception setting style attribute", exception);
                }
            }
        }
    }

    /**
     * Parse cssUpdates from browser. Format id{property:value;property;value}id{property:value}
     *
     */
    public static Map decode(FacesContext facesContext) {

        Map parameters = facesContext.getExternalContext().getRequestParameterMap();
        String cssUpdate = (String) parameters.get(CSS_UPDATE_FIELD);
        Map requestMap = facesContext.getExternalContext().getRequestMap();
        String oldCssUpdate = (String) requestMap.get(CSS_UPDATE_FIELD);

        if (cssUpdate == null || cssUpdate.length() == 0) {
            return null;
        }
        Map updates = null;
        if (!cssUpdate.equals(oldCssUpdate)) {
            updates = new HashMap();

            int rightBrace = 0;
            do {
                rightBrace = cssUpdate.indexOf("}");
                if (rightBrace != -1) {
                    String update = cssUpdate.substring(0, ++rightBrace);

                    cssUpdate = cssUpdate.substring(rightBrace);
                    int leftBrace = update.indexOf("{");
                    String id = update.substring(0, leftBrace);

                    leftBrace++;
                    String style = update.substring(leftBrace, update.length() - 1);
                    if (log.isLoggable(Level.FINEST)) {
                        log.finest("Adding id[" + id + "] Style [" + style + "]");
                    }
                    updates.put(id, style);
                }
            } while (rightBrace != -1);
            facesContext.getExternalContext().getSessionMap().put(CurrentStyle.class.getName(), updates);
            requestMap.put(CSS_UPDATE_FIELD, cssUpdate);
        }
        return updates;
    }

    /**
     * Parse CSS updates for a componenet
     *
     * @param facesContext
     * @param uiComponent
     */
    public static void decode(FacesContext facesContext,
                              UIComponent uiComponent) {

        decode(facesContext);
        Map map = (Map) facesContext.getExternalContext().getSessionMap()
                .get(CurrentStyle.class.getName());
        if (map == null) {
                return;
        }
        if (uiComponent == null) {
            return;
        }
        String clientId = uiComponent.getClientId(facesContext);
        String style = (String) map.get(clientId);
        if (style == null) {
            return;
        }

        if (log.isLoggable(Level.FINEST)) {
            log.finest("Decode Applying Style to [" + clientId + "] Css [" +
                      style + "]");
        }
        CurrentStyle cs =
                (CurrentStyle) uiComponent.getAttributes().get("currentStyle");
        if (cs != null) {
            cs.cssString = style;
        } else {
            cs = new CurrentStyle(style);

            uiComponent.getAttributes()
                    .put("currentStyle", new CurrentStyle(style));
        }

        // sync the component visible attribute with the css display style attribute
        Boolean value = Boolean.valueOf("true");
        if (cs.cssString.endsWith(DISPLAY_OFF)) {
            value = Boolean.valueOf("false");
        }
        ValueBinding vb = uiComponent.getValueBinding("visible");
        if (vb == null) {
            uiComponent.getAttributes().put("visible", value);
        } else {
            try {
                vb.setValue(facesContext, value);
            } catch (Exception e) {

                if (log.isLoggable(Level.SEVERE)) {
                    log.log(Level.SEVERE,"Exception setting visible. Value Binding [" +
                              vb.getExpressionString() + "]", e);
                }
            }
        }
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof CurrentStyle)) {
            return false;
        }
        CurrentStyle cs = (CurrentStyle) obj;
        if (!CoreUtils.objectsEqual(cssString, cs.cssString)) {
            return false;
        }
        if (!CoreUtils.objectsEqual(lastCssString, cs.lastCssString)) {
            return false;
        }
        return true;
    }
}
