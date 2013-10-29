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

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.component.dragdrop.DndEvent;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.dragdrop.DropEvent;
import com.icesoft.faces.component.ext.HtmlPanelGroup;
import com.icesoft.faces.component.menupopup.MenuPopupHelper;
import com.icesoft.faces.component.util.DelimitedProperties;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.CurrentStyle;
import com.icesoft.faces.context.effects.DragDrop;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.faces.utils.DnDCache;
import com.icesoft.util.pooling.ClientIdPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GroupRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.GroupRenderer {

    protected static final String STATUS = "status";

    protected static final String DROP = "dropID";
	protected static final String HIDDEN_FIELD = "iceDND";
    private static final Log log = LogFactory.getLog(GroupRenderer.class);

    // Basically, everything is excluded
    private static final String[] PASSTHRU_EXCLUDE =
            new String[]{HTML.STYLE_ATTR};


    private static final String[] PASSTHRU_JS_EVENTS = LocalEffectEncoder.maskEvents(
            ExtendedAttributeConstants.getAttributes(
                    ExtendedAttributeConstants.ICE_PANELGROUP));
    private static final String[] passThruAttributes =
            ExtendedAttributeConstants.getAttributes(
                    ExtendedAttributeConstants.ICE_PANELGROUP,
                    new String[][]{PASSTHRU_EXCLUDE, PASSTHRU_JS_EVENTS});

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        try {
            String viewID = facesContext.getViewRoot().getViewId();

            String style = ((HtmlPanelGroup) uiComponent).getStyle();
            String styleClass = ((HtmlPanelGroup) uiComponent).getStyleClass();
            String blockingFlag = (String) facesContext.getExternalContext()
                    .getRequestMap().get("BlockingServlet");

            String dndType = getDndType(uiComponent);
            DOMContext domContext =
                    DOMContext.attachDOMContext(facesContext, uiComponent);

            if (!domContext.isInitialized()) {
                Element rootSpan = domContext.createElement(HTML.DIV_ELEM);
                domContext.setRootNode(rootSpan);
                setRootElementId(facesContext, rootSpan, uiComponent);

                if (dndType != null) {
                    // Drag an drop needs some hidden fields
                    UIComponent form = findForm(uiComponent);
                    String formId = form.getClientId(facesContext);

                    FormRenderer.addHiddenField(facesContext, ClientIdPool.get(formId 
						+ UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()) + HIDDEN_FIELD));

                }
            }

            Element rootSpan = (Element) domContext.getRootNode();
            if (dndType != null) {
                DnDCache.getInstance(facesContext, true).put(
                        uiComponent.getClientId(facesContext),
                        (HtmlPanelGroup) uiComponent, facesContext);
                StringBuffer dropCall = new StringBuffer();
                String call = addJavascriptCalls(uiComponent, dndType, null, facesContext, dropCall);

                String clientId = uiComponent.getClientId(facesContext);
                Element script = domContext.createElement(HTML.SCRIPT_ELEM);
                script.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId + "script"));
                script.appendChild(domContext.createTextNodeUnescaped(dropCall.toString()));
                rootSpan.appendChild(script);
                Map rendererJavascriptDraggable = new HashMap();
                rendererJavascriptDraggable.put(HTML.ONMOUSEOUT_ATTR,
                        "Ice.Scriptaculous.Draggable.removeMe(this.id);");
                rendererJavascriptDraggable.put(HTML.ONMOUSEMOVE_ATTR, call);
                rendererJavascriptDraggable.put(HTML.ONMOUSEOVER_ATTR, dropCall.toString());

                LocalEffectEncoder.encode(
                        facesContext, uiComponent, PASSTHRU_JS_EVENTS,
                        rendererJavascriptDraggable, rootSpan, null);
            } else {
                LocalEffectEncoder.encode(
                        facesContext, uiComponent, PASSTHRU_JS_EVENTS, null, rootSpan, null);
            }


            if (styleClass != null) {
                rootSpan.setAttribute("class", styleClass);
            }
            JavascriptContext.fireEffect(uiComponent, facesContext);
            String extraStyle = null;
            String scrollWidth =
                    (String) uiComponent.getAttributes().get("scrollWidth");
            String scrollHeight =
                    (String) uiComponent.getAttributes().get("scrollHeight");


            if (scrollHeight != null || scrollWidth != null) {
                if (extraStyle == null) {
                    extraStyle = "";
                }
                if (scrollHeight == null) {
                    extraStyle += "width:" + scrollWidth + ";overflow:auto;";
                } else if (scrollWidth == null) {
                    extraStyle += "height:" + scrollHeight + ";overflow:auto;";
                } else {
                    extraStyle += "width:" + scrollWidth + ";height:" +
                            scrollHeight + ";overflow:auto;";
                }
            }

            CurrentStyle.apply(facesContext, uiComponent, null, extraStyle);
            MenuPopupHelper.renderMenuPopupHandler(facesContext, uiComponent, rootSpan);
            PassThruAttributeRenderer.renderNonBooleanHtmlAttributes(uiComponent,
                    rootSpan, passThruAttributes);
            domContext.stepInto(uiComponent);
            // domContext.stepOver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String addJavascriptCalls(UIComponent uiComponent, String dndType,
                                        String handleId,
                                        FacesContext facesContext,
                                        StringBuffer dropCall) {
        String calls = "";

        boolean dragListener =
                uiComponent.getAttributes().get("dragListener") != null;
        boolean dropListener =
                uiComponent.getAttributes().get("dropListener") != null;
        String dragMask = DndEvent.parseMask(
                (String) uiComponent.getAttributes().get("dragMask"));
        String dropMask = DndEvent.parseMask(
                (String) uiComponent.getAttributes().get("dropMask"));
        String dragOptions =
                (String) uiComponent.getAttributes().get("dragOptions");
        String hoverClass =
                (String) uiComponent.getAttributes().get("hoverclass");
        if (!dragListener) {
            if (dragMask == null) {
                dragMask = DndEvent.MASK_ALL_BUT_DROPS;
            }
        }
        if (!dropListener) {
            dropMask = DndEvent.MASK_ALL;
        }
        if ("DRAG".equalsIgnoreCase(dndType)) {
			String scrollid = (String) uiComponent.getAttributes().get("dropTargetScrollerId"); 
             if (scrollid != null && scrollid.trim().length() > 0) { 
                 calls += DragDrop.addDragable(uiComponent, uiComponent.getClientId(facesContext), handleId, 
                         dragOptions, dragMask, facesContext); 
             } else { 
                 calls += DragDrop.addDragable(uiComponent.getClientId(facesContext), handleId, dragOptions, dragMask, 
                         facesContext); 
             }
			 
        } else if ("drop".equalsIgnoreCase(dndType)) {
            dropCall.append(DragDrop.addDroptarget(
                    uiComponent, null, facesContext,
                    dropMask, hoverClass));
        } else if ("dragdrop".equalsIgnoreCase(dndType)) {
			String scrollid = (String) uiComponent.getAttributes().get("dropTargetScrollerId"); 
             if (scrollid != null && scrollid.trim().length() > 0) { 
                 calls += DragDrop.addDragable(uiComponent, uiComponent.getClientId(facesContext), handleId, 
                         dragOptions, dragMask, facesContext); 
             } else { 
                 calls += DragDrop.addDragable(uiComponent.getClientId(facesContext), handleId, dragOptions, dragMask, 
                         facesContext); 
             }
            dropCall.append(DragDrop.addDroptarget(
                    uiComponent, null, facesContext,
                    dropMask, hoverClass));
        } else {
            throw new IllegalArgumentException("Value [" + dndType +
                    "] is not valid for dndType. Please use drag or drop");
        }
        return calls;
    }


    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, null);
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        CoreUtils.addPanelTooltip(facesContext, uiComponent);
        domContext.stepOver();

    }

    protected String appendStyle(String currentStyle, String additionalStyle) {
        String result = "";
        if (!isBlank(currentStyle)) {
            result = currentStyle;
        }
        if (!isBlank(additionalStyle)) {
            result += additionalStyle;
        }
        if (isBlank(result)) {
            return null;
        }
        return result;
    }

    private static boolean isBlank(String s) {
        return !(s != null && s.trim().length() > 0);
    }


    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        String clientId = component.getClientId(context);
        if (log.isTraceEnabled()) {
            log.trace("GroupRenderer:decode");
        }
        MenuPopupHelper.decodeMenuContext(context, component);
        if (component instanceof HtmlPanelGroup) {
            HtmlPanelGroup panel = (HtmlPanelGroup) component;
            String dndType = getDndType(component);

            if (panel.getDraggable() != null || panel.getDropTarget() != null) {
/*
                Map paramValuesMap = context.getExternalContext().getRequestParameterValuesMap();
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
*/

                Map requestMap = context.getExternalContext().getRequestParameterMap();

                UIComponent form = findForm(component);
                String formId = form.getClientId(context);
                String hdnFld = ClientIdPool.get(formId 
					+ UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()) + HIDDEN_FIELD);
                if (!requestMap.containsKey(hdnFld)) return;
                String value = String.valueOf(requestMap.get(hdnFld));
                DelimitedProperties delimitedProperties = new DelimitedProperties(value);

                String fieldName = clientId + STATUS;
                String status = delimitedProperties.get(fieldName);

                if (status == null) {
                    if (log.isTraceEnabled()) {
                        log.trace("Drag Drop Status for ID [" +
                                panel.getClientId(context) +
                                "] Field Name [" + fieldName +
                                "] is null. Returning");
                    }
                    return;
                }
                String targetID = delimitedProperties.get(clientId + DROP);

                Object targetDragValue = null;
                Object targetDropValue = null;

                if (targetID != null && targetID.length() > 0) {
                    DnDCache dndCache = DnDCache.getInstance(context, false);
                    if ("drop".equals(dndType)) {
                        targetDragValue = dndCache.getDragValue(targetID);
                        targetDropValue = dndCache.getDropValue(panel.getClientId(context));
                    } else {
                        targetDragValue = dndCache.getDragValue(panel.getClientId(context));
                        targetDropValue = dndCache.getDropValue(targetID);
                    }
                }

                if (log.isTraceEnabled()) {
                    log.trace("Dnd Event Client ID [" +
                            component.getClientId(context) + "] Target ID [" +
                            targetID + "] Status [" + status + "]");
                }


                int type = 0;
                try {
                    type = Integer.parseInt(status);
                } catch (NumberFormatException e) {
                    if (status != null || status.length() != 0)

                    {
                        return;
                    }
                }
                if (type > 0 && type < 4) {
                    panel.getAttributes().put("dragged", Boolean.TRUE);
                }
                if (panel.getDragListener() == null &&
                        panel.getDropListener() == null) {
                    return;
                }

                MethodBinding listener = panel.getDragListener();
                if (listener != null) {

                    DragEvent event = new DragEvent(component, type, targetID,
                            targetDragValue,
                            targetDropValue);
                    panel.queueEvent(event);
                }
                listener = panel.getDropListener();
                if (listener != null) {

                    DropEvent event = new DropEvent(component, type, targetID,
                            targetDragValue,
                            targetDropValue);
                    panel.queueEvent(event);
                }
            }
        }
    }


    protected Element createHiddenField(DOMContext domContext,
                                        FacesContext facesContext,
                                        UIComponent uiComponent, String name) {
        Element ele = domContext.createElement(HTML.INPUT_ELEM);
        ele.setAttribute(HTML.TYPE_ATTR, "hidden");
        String n = ClientIdPool.get(getHiddenFieldName(facesContext, uiComponent, name));
        ele.setAttribute(HTML.NAME_ATTR, n);
        ele.setAttribute(HTML.ID_ATTR, n);
        ele.setAttribute(HTML.VALUE_ATTR, "");
        ele.setAttribute(HTML.AUTOCOMPLETE_ATTR, "off");
        return ele;
    }


    protected String getHiddenFieldName(FacesContext facesContext,
                                        UIComponent uiComponent, String name) {
        UIComponent form = findForm(uiComponent);
        String formId = form.getClientId(facesContext);
        String clientId = uiComponent.getClientId(facesContext);
        return formId
                + UINamingContainer.getSeparatorChar(facesContext)
                + UIViewRoot.UNIQUE_ID_PREFIX
                + clientId
                + name;
    }

    protected String getDndType(UIComponent uiComponent) {
        String dndType = null;
        String draggable =
                (String) uiComponent.getAttributes().get("draggable");
        String droppable =
                (String) uiComponent.getAttributes().get("dropTarget");
        if ("true".equalsIgnoreCase(draggable) &&
                "true".equalsIgnoreCase(droppable)) {
            dndType = "dragdrop";
        } else if ("true".equalsIgnoreCase(draggable)) {
            dndType = "DRAG";
        } else if ("true".equalsIgnoreCase(droppable)) {
            dndType = "drop";
        }
        return dndType;
    }

    /**
     * Safri can return mutile values. The first one blank. This was a BIG
     * problem  to solve!
     *
     * @param sa
     * @return
     */
    private String getParamamterValue(String[] sa) {

        // bail if the sa array is null
        if (sa == null) {
            if (log.isTraceEnabled()) {
                log.trace("Null parameter value");
            }
            return null;
        }

        String result = null;
        for (int i = 0; i < sa.length; i++) {
            String s = sa[i];
            if (log.isTraceEnabled()) {
                log.trace("getParameterValue Checking [" + s + "]");
            }
            if (s != null && s.trim().length() > 0) {
                if (log.isTraceEnabled()) {
                    log.trace("getParameterValue result:" + s);
                }
                result = s;
            }
        }
        if (log.isTraceEnabled()) {
            log.trace("Length [" + sa.length + "] Result [" + result + "]");
        }
        return result;
    }


}
