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

import com.icesoft.faces.context.DOMContext;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;


public class MessagesRenderer extends DomBasicRenderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UIMessages.class);
    }

    public void encodeChildren(FacesContext facesContext,
                               UIComponent uiComponent) throws IOException {
        validateParameters(facesContext, uiComponent, UIMessages.class);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, UIMessages.class);
        //this property will only be true when there wasn't any message found 
        //on first rendering request of this component.
        boolean renderLater = uiComponent.getAttributes().containsKey("$render-later$");
        DOMContext domContext = null;
        if (!renderLater) {
            domContext =
                    DOMContext.attachDOMContext(facesContext, uiComponent);
        } else {
            domContext =
                    DOMContext.reattachDOMContext(facesContext, uiComponent);
        }

        // retrieve the messages
        Iterator messagesIterator = null;
        boolean retrieveGlobalMessagesOnly =
                ((UIMessages) uiComponent).isGlobalOnly();
        if (retrieveGlobalMessagesOnly) {
            messagesIterator = facesContext.getMessages(null);
        } else {
            messagesIterator = facesContext.getMessages();
        }

        // the target element to which messages are appended; either td or span
        Element parentTarget = null;

        // layout
        boolean tableLayout = false; // default layout is list
        String layout = (String) uiComponent.getAttributes().get("layout");
        if (layout != null && layout.toLowerCase().equals("table")) {
            tableLayout = true;
            parentTarget = domContext.createElement(HTML.TABLE_ELEM);
        } else {
            parentTarget = domContext.createElement(HTML.UL_ELEM);
        }
        if (!domContext.isInitialized()) {
            domContext.setRootNode(parentTarget);
            setRootElementId(facesContext, parentTarget, uiComponent);
        }

        //dir lang
        String dir = (String) uiComponent.getAttributes().get("dir");
        if (dir != null) {
            parentTarget.setAttribute(HTML.DIR_ATTR, dir);
        }
        String lang = (String) uiComponent.getAttributes().get("lang");
        if (lang != null) {
            parentTarget.setAttribute(HTML.LANG_ATTR, lang);
        }
        // ICE-2174
        Boolean visible = (Boolean) uiComponent.getAttributes().get("visible");
        boolean isVisible = visible == null || visible.booleanValue();
        if (!isVisible || !messagesIterator.hasNext()) parentTarget.setAttribute(HTML.STYLE_ATTR, "display:none;");

        if (renderLater) {
            UIComponent uiform = findForm(uiComponent);
            //second request has been made successfully, now remove the info 
            if (uiform != null) {
                uiform.getAttributes().remove("$ice-msgs$");
                uiComponent.getAttributes().remove("$render-later$");
            }
        }

        if (!messagesIterator.hasNext()) {
            Element emptyChild = null;
            if (tableLayout) {
                emptyChild = (Element) domContext.createElement(HTML.TR_ELEM)
                        .appendChild(domContext.createElement(HTML.TD_ELEM));
            } else {
                emptyChild = (Element) domContext.createElement(HTML.LI_ELEM);
            }
            parentTarget.appendChild(emptyChild);
            UIComponent uiform = findForm(uiComponent);
            //no data found yet, pass it to the FormRenderer to rerender it later
            //on complete rendering of the entire form.
            if (!renderLater && uiform != null) {
                uiform.getAttributes().put("$ice-msgs$", uiComponent);
                uiComponent.getAttributes().put("$render-later$", "true");
            }
            domContext.stepOver();
            return;
        }

        FacesMessage nextFacesMessage = null;
        while (messagesIterator.hasNext()) {

            nextFacesMessage = (FacesMessage) messagesIterator.next();
            if (nextFacesMessage.isRendered() && !((UIMessages) uiComponent).isRedisplay()) {
                return;
            }
            nextFacesMessage.rendered();
            String[] styleAndStyleClass =
                    getStyleAndStyleClass(uiComponent, nextFacesMessage);
            String style = styleAndStyleClass[0];
            String styleClass = styleAndStyleClass[1];
			
            String[] summaryAndDetail = getSummaryAndDetail(nextFacesMessage);
            String summary = summaryAndDetail[0];
            String detail = summaryAndDetail[1];

            boolean showSummary = ((UIMessages) uiComponent).isShowSummary();
            boolean showDetail = ((UIMessages) uiComponent).isShowDetail();
			
			Boolean escape = (Boolean) uiComponent.getAttributes().get("escape");
			boolean isEscape = escape == null || escape.booleanValue();

            Element nextTableData = null;
            if (tableLayout) {
                Element tr = domContext.createElement("tr");
                Element td = domContext.createElement("td");
                tr.appendChild(td);
                parentTarget.appendChild(tr);
                nextTableData = td;
            }

            Element nextMessageSpan = domContext.createElement(HTML.SPAN_ELEM);
            if (tableLayout) {
                nextTableData.appendChild(nextMessageSpan);
            } else {
                Element li = domContext.createElement(HTML.LI_ELEM);
                parentTarget.appendChild(li);
                li.appendChild(nextMessageSpan);
            }

            if (null != styleClass) {
                nextMessageSpan.setAttribute("class", styleClass);
            }
            if (style != null && style.length() > 0) {
                nextMessageSpan.setAttribute("style", style);
            } else {
                nextMessageSpan.removeAttribute("style");
            }

            boolean tooltip = getToolTipAttribute(uiComponent);

            // ICE-2174
            String title = (String) uiComponent.getAttributes().get("title");
            if (title == null && tooltip && showSummary) title = summary;
            if (title == null && tooltip && showDetail) title = detail;
            if (title != null) nextMessageSpan.setAttribute(HTML.TITLE_ATTR, title);

            if (showSummary && showDetail && tooltip) {
                // show summary as tooltip, detail as child span
                // nextMessageSpan.setAttribute("title", summary); commented out for ICE-2174
                Text textNode = domContext.getDocument().createTextNode(detail);
                nextMessageSpan.appendChild(textNode);
            } else {
                if (showSummary) {
                    Text textNode;
					if (isEscape) {
						textNode = domContext.createTextNode(summary);
					} else {
						textNode = domContext.createTextNodeUnescaped(summary);
					}
                    nextMessageSpan.appendChild(textNode);
                }
                if (showDetail) {
                    Text textNode;
					if (isEscape) {
						textNode = domContext.createTextNode(detail);
					} else {
						textNode = domContext.createTextNodeUnescaped(detail);
					}
                    nextMessageSpan.appendChild(textNode);
                }
            }

        }
        domContext.stepOver();
    }

    private void writeStream(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        Element root = domContext.createRootElement(HTML.SPAN_ELEM);
        Text text = domContext.createTextNode("List of Messages");
        Object style = uiComponent.getAttributes().get("style");
        String sstyle = ((style == null) ? null : style.toString());
        if (sstyle != null && sstyle.length() > 0) {
            root.setAttribute(HTML.STYLE_ATTR, sstyle);
        } else {
            root.removeAttribute(HTML.STYLE_ATTR);
        }
        root.appendChild(text);
        domContext.stepOver();
    }

}

