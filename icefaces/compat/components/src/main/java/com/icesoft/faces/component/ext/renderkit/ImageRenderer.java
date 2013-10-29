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

import com.icesoft.faces.component.ext.HtmlGraphicImage;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceRegistry;
import com.icesoft.faces.context.ResourceRegistryLocator;

import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ImageRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.ImageRenderer implements java.io.Serializable {
    private static final long serialVersionUID = -1367657940103836512L;
    private static final Log log = LogFactory.getLog(ImageRenderer.class);
    
    protected String processSrcAttribute(FacesContext facesContext, UIGraphic uiGraphic) {
        Object o = uiGraphic.getValue();
        if (o == null) {
            o = uiGraphic.getUrl();
        }
        if (o == null) {
            log.error("The value of graphicImage component is missing", new NullPointerException());
            return new String();
        }
        if (o instanceof byte[]) {
            return ((HtmlGraphicImage)uiGraphic).getByteArrayImagePath(facesContext);
        } else if (o instanceof Resource) {
			ResourceRegistry registry = ResourceRegistryLocator.locate(facesContext);
            return registry.registerResource((Resource)o).getPath();
        } else {
            // delegate to the parent class
            return super.processSrcAttribute(facesContext, uiGraphic);
        }
    }
}