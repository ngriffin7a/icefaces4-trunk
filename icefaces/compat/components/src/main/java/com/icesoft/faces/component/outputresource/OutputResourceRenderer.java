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

package com.icesoft.faces.component.outputresource;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.w3c.dom.Element;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.util.CoreUtils;

public class OutputResourceRenderer extends DomBasicInputRenderer {

	protected static final String CONTAINER_DIV_SUFFIX = "_cont";

	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
			throws IOException {

		String clientId = uiComponent.getClientId(facesContext);
		OutputResource outputResource = (OutputResource) uiComponent;
        boolean disabled = outputResource.isDisabled();
        if( outputResource.getResource() != null ){
			DOMContext domContext = DOMContext.attachDOMContext(facesContext,
					uiComponent);
			if (!domContext.isInitialized()) {
				domContext.createRootElement(HTML.DIV_ELEM);
			}

			Element root = (Element) domContext.getRootNode();
			root.setAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext)
					+ CONTAINER_DIV_SUFFIX);
			domContext.setCursorParent(root);
			root.setAttribute(HTML.CLASS_ATTR, CSS_DEFAULT.OUTPUT_RESOURCE_DEFAULT_STYLE_CLASS);
			String style = outputResource.getStyle();
			String styleClass = outputResource.getStyleClass();
			
			Element resource = null;
		        		
			if( OutputResource.TYPE_BUTTON.equals(outputResource.getType())){
                resource = domContext.createElement(HTML.INPUT_ELEM);
                if (disabled) {
                    resource.setAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR);
                }
                resource.setAttribute(HTML.TYPE_ATTR, "button");
				resource.setAttribute(HTML.VALUE_ATTR, outputResource.getLabel());
				resource.setAttribute(HTML.ONCLICK_ATTR, "window.open('" + outputResource.getPath() + "');");
			}
			else{
                if (disabled) {
                    resource = domContext.createElement(HTML.SPAN_ELEM);
                } else {
                    resource = domContext.createElement(HTML.ANCHOR_ELEM);
                    resource.setAttribute(HTML.HREF_ATTR, outputResource.getPath());
                    PassThruAttributeRenderer.renderNonBooleanHtmlAttributes(uiComponent, resource, new String[]{"target"});
                }

                if( outputResource.getImage() != null ){
					Element img = domContext.createElement(HTML.IMG_ELEM);
					String image = outputResource.getImage();
					if (image != null) {
    					img.setAttribute(HTML.SRC_ATTR, CoreUtils.resolveResourceURL(facesContext, image));
					}
					resource.appendChild(img);
					img.setAttribute(HTML.ALT_ATTR, outputResource.getLabel());
				}
				else{
					resource.appendChild(domContext.createTextNode(outputResource
							.getLabel()));
				}
				
			}
			resource.setAttribute(HTML.ID_ATTR, clientId);
			root.appendChild(resource);
			if( style != null ){
				resource.setAttribute(HTML.STYLE_ATTR, style);
			}
			if (styleClass != null) {
				resource.setAttribute("class", styleClass);
	        }
			
			domContext.stepOver();
		}
		
	}

}
