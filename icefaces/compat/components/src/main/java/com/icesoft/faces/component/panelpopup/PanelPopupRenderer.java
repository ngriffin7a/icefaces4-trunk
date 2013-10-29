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

package com.icesoft.faces.component.panelpopup;

import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.component.ext.renderkit.FormRenderer;
import com.icesoft.faces.component.ext.renderkit.GroupRenderer;
import com.icesoft.faces.component.paneltooltip.PanelTooltip;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.util.CoreComponentUtils;
import com.icesoft.util.pooling.ClientIdPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.impl.context.DOMPartialViewContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * PanelPopupRenderer is an extension of ICEfaces D2D GroupRenderer responsible
 * for rendering the PanelPopup component.
 * </p>
 */
public class PanelPopupRenderer extends GroupRenderer {
    private static final Log log = LogFactory.getLog(PanelPopupRenderer.class);

    // Basically, everything is excluded
    private static final String[] PASSTHRU_EXCLUDE =
            new String[]{HTML.STYLE_ATTR};

    private static final String[] PASSTHRU_JS_EVENTS = LocalEffectEncoder.maskEvents(
            ExtendedAttributeConstants.getAttributes(
                    ExtendedAttributeConstants.ICE_PANELPOPUP));
    private static final String[] PASSTHRU =
            ExtendedAttributeConstants.getAttributes(
                    ExtendedAttributeConstants.ICE_PANELPOPUP,
                    new String[][]{PASSTHRU_EXCLUDE, PASSTHRU_JS_EVENTS});
    private static final Random RANDOM = new Random();


    /*
      * (non-Javadoc)
      *
      * @see com.icesoft.faces.renderkit.dom_html_basic.GroupRenderer#getRendersChildren()
      */

    public boolean getRendersChildren() {
        return true;
    }

    /*
      * (non-Javadoc)
      *
      * @see com.icesoft.faces.component.ext.renderkit.GroupRenderer#encodeBegin(javax.faces.context.FacesContext,
      *      javax.faces.component.UIComponent)
      */

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, PanelPopup.class);

        String styleClass = (String) uiComponent.getAttributes().get(
                "styleClass");
        String headerClass = (String) uiComponent.getAttributes().get(
                "headerClass");
        String bodyClass = (String) uiComponent.getAttributes()
                .get("bodyClass");
        Boolean resizable = null; // resizable functionality has not been
        // implemented yet.
        Boolean modal = (Boolean) uiComponent.getAttributes().get("modal");
        if (log.isTraceEnabled()) {
            log.trace("Value of modal is [" + modal + "]");
        }
        Boolean visible = (Boolean) uiComponent.getAttributes().get("visible");

        String dndType = getDndType(uiComponent);

        DOMContext domContext = DOMContext.attachDOMContext(facesContext,
                uiComponent);

        // initialize DOMContext
        PanelPopup panelPopup = (PanelPopup) uiComponent;

        String clientId = uiComponent.getClientId(facesContext);

        Element rootDiv = domContext.createRootElement(HTML.DIV_ELEM);
        setRootElementId(facesContext, rootDiv, uiComponent);
        rootDiv.setAttribute(HTML.NAME_ATTR, clientId);
        DOMContext.enableOnElementUpdateNotify(rootDiv, clientId);

        if (uiComponent instanceof PanelTooltip) {
            if (((PanelTooltip) uiComponent).isDynamic() && !((PanelTooltip) uiComponent).isVisible()) {
                rootDiv.setAttribute(HTML.STYLE_ATTR, "display:none;");
                domContext.stepOver();
                return;
            }
        }
        Element table = domContext.createElement(HTML.TABLE_ELEM);
        table.setAttribute(HTML.CELLPADDING_ATTR, "0");
        table.setAttribute(HTML.CELLSPACING_ATTR, "0");
//			table.setAttribute(HTML.WIDTH_ATTR, "100%");
        rootDiv.appendChild(table);
/*
        Text iframe = domContext.createTextNode("<!--[if lte IE 6.5]><iframe src=\"" +
                CoreUtils.resolveResourceURL(FacesContext.getCurrentInstance(), "/xmlhttp/blank") +
                "\" class=\"iceIEIFrameFix\" style=\"width:100%;height:100%;\"></iframe><![endif]-->");
        rootDiv.appendChild(iframe);
*/
        // extracted from GroupRenderer encodeBegin
        if (dndType != null) {
            // Drag an drop needs some hidden fields
            Element statusField = createHiddenField(domContext,
                    facesContext, uiComponent, STATUS);
            rootDiv.appendChild(statusField);
            Element targetID = createHiddenField(domContext, facesContext,
                    uiComponent, DROP);
            rootDiv.appendChild(targetID);
            UIComponent form = findForm(uiComponent);
            String formId = form.getClientId(facesContext);
            FormRenderer.addHiddenField(facesContext, ClientIdPool.get(formId 
				+ UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance()) + HIDDEN_FIELD));
        }
        // Write Modal Javascript so that on refresh it will still be modal.
        String script = modalJavascript(uiComponent, modal, visible, facesContext, clientId);
        Element scriptWrapper = domContext.createElement(HTML.SPAN_ELEM);
        scriptWrapper.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId + "script"));
        Element scriptEle = domContext.createElement(HTML.SCRIPT_ELEM);
        scriptEle.setAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT);
        Text scriptTextNode = domContext.createTextNodeUnescaped(script);
        rootDiv.appendChild(scriptWrapper);
        scriptWrapper.appendChild(scriptEle);
        scriptEle.appendChild(scriptTextNode);

        Element root = (Element) domContext.getRootNode();

        try {
            root.setAttribute(HTML.CLASS_ATTR, styleClass);
        } catch (Exception e) {
            log.error("Error rendering Modal Panel Popup ", e);
        }
        JavascriptContext.fireEffect(uiComponent, facesContext);

        // get tables , our table is the first and only one
        NodeList tables = root.getElementsByTagName(HTML.TABLE_ELEM);
        // assumption we want the first table in tables. there should only be
        // one
        Element t = (Element) tables.item(0);
        // clean out child nodes and build a fresh selectinputdate
        DOMContext.removeChildrenByTagName(t, HTML.TR_ELEM);

        doPassThru(facesContext, uiComponent, root);
        String handleId = null;
        if (panelPopup.getHeader() != null) {
            Element headerTr = domContext.createElement(HTML.TR_ELEM);
            Element headerTd = domContext.createElement(HTML.TD_ELEM);
            headerTd.setAttribute(HTML.CLASS_ATTR, headerClass);
            handleId = ClientIdPool.get(uiComponent.getClientId(facesContext) + "Handle");
            headerTd.setAttribute(HTML.ID_ATTR, handleId);
//            headerTd.setAttribute(HTML.STYLE_ATTR, "width:100%;");
            headerTr.appendChild(headerTd);
            Element headerTdSpacer = domContext.createElement(HTML.TD_ELEM);
            Element headerDiv = domContext.createElement("div");
            headerDiv.setAttribute(HTML.STYLE_ATTR, "width:1px;");
            headerTdSpacer.setAttribute(HTML.CLASS_ATTR, headerClass);
            headerTdSpacer.appendChild(headerDiv);
            headerTr.appendChild(headerTdSpacer);
            // add header facet to header tr and add to table
            t.appendChild(headerTr);
            // set the cursor parent to the new table row Element
            // to the new table row Element
            domContext.setCursorParent(headerTd);

            UIComponent header = panelPopup.getHeader();
            CustomComponentUtils.renderChild(facesContext, header);
        }

        if (panelPopup.getBody() != null) {

            Element bodyTr = domContext.createElement(HTML.TR_ELEM);
            Element bodyTd = domContext.createElement(HTML.TD_ELEM);

            bodyTd.setAttribute(HTML.CLASS_ATTR, bodyClass);
            bodyTr.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId + "-tr"));
            bodyTr.appendChild(bodyTd);
            bodyTd.setAttribute(HTML.COLSPAN_ATTR, "2");
            // add body facet to body tr then add to table
            t.appendChild(bodyTr);
            // set the cursor parent to the new table row Element
            // this will cause the renderChild method to append the child nodes
            // to the new table row Element
            domContext.setCursorParent(bodyTd);

            UIComponent body = panelPopup.getBody();

            CustomComponentUtils.renderChild(facesContext, body);
        }
        // if the popup is resizable render a resize handle
        if (resizable != null && resizable.booleanValue()) {
            Element footerTr = domContext.createElement(HTML.TR_ELEM);
            footerTr.setAttribute(HTML.HEIGHT_ATTR, "15px");
            footerTr.setAttribute(HTML.STYLE_ATTR,
                    "text-align: right; float: right;");
            Element footerTd = domContext.createElement(HTML.TD_ELEM);
            footerTd.setAttribute(HTML.STYLE_CLASS_ATTR, "panelPopupFooter");
            footerTd.setAttribute(HTML.COLSPAN_ATTR, "2");
            Element img = domContext.createElement(HTML.IMG_ELEM);
            img.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(
                    facesContext, "/xmlhttp/css/xp/css-images/resize.gif"));
            img.setAttribute(HTML.STYLE_ATTR, "cursor: se-resize");
            footerTd.appendChild(img);
            footerTr.appendChild(footerTd);
            t.appendChild(footerTr);
        }

        panelPopup.applyStyle(facesContext, root);
        domContext.stepOver();

        // Rebroadcast Javascript to survive refresh
        if (dndType != null) {
            scriptTextNode.appendData("; Ice.DnD.adjustPosition('" + uiComponent.getClientId(facesContext) + "');");
            StringBuffer dropCall = new StringBuffer();
            String call = addJavascriptCalls(uiComponent, "DRAG", handleId,
                    facesContext, dropCall);
            scriptTextNode.appendData("; " + call + (" //" + RANDOM.nextLong()));
            if (panelPopup.isClientOnly()) {
                //the "submit" method in the dragdrop_custom.js would check for this
                //element inside the panelPopup and will not fire submit if found
                Element clientOnly = domContext.createElement(HTML.INPUT_ELEM);
                clientOnly.setAttribute(HTML.TYPE_ATTR, "hidden");
                clientOnly.setAttribute(HTML.ID_ATTR, clientId + "clientOnly");
                clientOnly.setAttribute(HTML.AUTOCOMPLETE_ATTR, "off");
                root.appendChild(clientOnly);
            }
        }

        // autoPosition handling
        String autoPositionJS = null;
        boolean positionOnLoadOnly = panelPopup.isPositionOnLoadOnly();
        boolean dragged = panelPopup.isDragged();
        String positions = panelPopup.getAutoPosition();
        if (positions != null && !positions.equalsIgnoreCase("manual") && (!positionOnLoadOnly || (positionOnLoadOnly && !dragged))) {
            if (positions.indexOf(',') < 1) {
                if (positions.trim().length() != 0) {
                    log.warn("The autoPosition attribute should be used with an "
                            + " x and y value for the position, such as '20,40'");
                }
            } else {
                String x = positions.substring(0, positions.indexOf(','));
                String y = positions.substring(positions.indexOf(',') + 1);
                autoPositionJS = "Ice.autoPosition.start('" + clientId + "'," + x
                        + "," + y + "," + positionOnLoadOnly + ");";
            }

        } else {
            autoPositionJS = "Ice.autoPosition.stop('" + clientId + "');";
        }
        JavascriptContext.addJavascriptCall(facesContext, "; " + autoPositionJS);

        // autoCentre handling
        boolean autoCentre = panelPopup.isAutoCentre();
        String centreJS;
        if (autoCentre && (!positionOnLoadOnly || (positionOnLoadOnly && !dragged))) {
            centreJS = "Ice.autoCentre.start('" + clientId + "'," + positionOnLoadOnly + ");";
        } else {
            centreJS = "Ice.autoCentre.stop('" + clientId + "');";
        }

        JavascriptContext.addJavascriptCall(facesContext, "; " + centreJS);
        JavascriptContext.addJavascriptCall(facesContext, "; Ice.iFrameFix.start('" + clientId + "','" +
                CoreUtils.resolveResourceURL(facesContext, "/xmlhttp/blank") + "');");
    }

    protected void doPassThru(FacesContext facesContext, UIComponent uiComponent,
                              Element root) {
        PassThruAttributeRenderer.renderNonBooleanHtmlAttributes(uiComponent,
                root, PASSTHRU);
        LocalEffectEncoder.encode(
                facesContext, uiComponent, PASSTHRU_JS_EVENTS, null, root, null);
    }

    private String modalJavascript(UIComponent uiComponent, Boolean modal, Boolean visible,
                                   FacesContext facesContext, String clientId) {
        String call = "";
        String iframeUrl = CoreUtils.resolveResourceURL(facesContext,
                "/xmlhttp/blank");
        if (modal != null) {
            if (modal.booleanValue() && visible.booleanValue()) {
                String trigger = "";
                // ICE-3563
                if (!((PanelPopup) uiComponent).isRunningModal()) {
                    Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
                    if (requestParameterMap.get("ice.focus") != null) {
                        trigger = (String) requestParameterMap.get("ice.focus");
                    }
                    ((PanelPopup) uiComponent).setRunningModal(true);
                    CoreComponentUtils.setFocusId("");
                }

                String autoPosition = (String) uiComponent.getAttributes().get("autoPosition");
                boolean positionOnLoadOnly = ((Boolean) uiComponent.getAttributes().get("positionOnLoadOnly")).booleanValue();
                call = "Ice.modal.start('" + clientId + "', '" + iframeUrl
                        + "', '" + trigger + "'," + "manual".equalsIgnoreCase(autoPosition) + "," + positionOnLoadOnly + "," +
						getIE8DisableModalFrame(facesContext) + ");" +
                        "ice.onElementUpdate('" + clientId + "',function() {Ice.modal.stop('" +
                        clientId + "');});";

                if (log.isTraceEnabled()) {
                    log.trace("Starting Modal Function");
                }
            } else {
                // ICE-3563
                if (((PanelPopup) uiComponent).isRunningModal()) {
                    ((PanelPopup) uiComponent).setRunningModal(false);
                    CoreComponentUtils.setFocusId("");
                }
                call = "Ice.modal.stop('" + clientId + "');";
                if (log.isTraceEnabled()) {
                    log.trace("Stopping modal function");
                }
            }
        }
        return call;
    }

    /*
      * (non-Javadoc)
      *
      * @see com.icesoft.faces.renderkit.dom_html_basic.GroupRenderer#encodeChildren(javax.faces.context.FacesContext,
      *      javax.faces.component.UIComponent)
      */

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) throws IOException {
    }

    /*
      * (non-Javadoc)
      *
      * @see com.icesoft.faces.component.ext.renderkit.GroupRenderer#encodeEnd(javax.faces.context.FacesContext,
      *      javax.faces.component.UIComponent)
      */

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("Encode End Called");
        }
    }
	
	private String getIE8DisableModalFrame(FacesContext facesContext) {
		String value = facesContext.getExternalContext().getInitParameter("com.icesoft.faces.IE8DisableModalFrame");
		if ("true".equalsIgnoreCase(value)) {
			return "true";
		} else {
			return "false";
		}
	}
}
