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

package org.icefaces.mobi.component.thumbnail;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.mobi.component.camera.Camera;
import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.util.ClientDescriptor;

import static org.icefaces.ace.util.HTML.*;

public class ThumbnailRenderer extends Renderer {
    private static final Logger logger =
            Logger.getLogger(ThumbnailRenderer.class.toString());

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Thumbnail thumbnail = (Thumbnail) uiComponent;
        String clientId = thumbnail.getClientId();
        java.util.Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
        String data = (String) requestMap.get(clientId + "_data");
        thumbnail.setData(data);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        /* checking to see if thumbnail is required is in core renderer */
        Thumbnail thumbnail = (Thumbnail) uiComponent;
        String compId = thumbnail.getFor();

        if (compId == null &&
                (facesContext.isProjectStage(ProjectStage.Development) ||
                        logger.isLoggable(Level.FINER))) {
            logger.warning("'for' attribute cannot be null");
        }
        UIComponent comp = thumbnail.findComponent(compId);
        if (comp == null) {
            logger.warning("Cannot locate associated component 'for' = " + compId);
            return;
        }
        String mFor = comp.getId();
        if (null != comp) {
            mFor = comp.getClientId(facesContext);
            if (MobiJSFUtils.uploadInProgress(comp)) {
                thumbnail.setBaseClass(Thumbnail.CSS_DONE_CLASS);
            } else {
                thumbnail.setBaseClass(Thumbnail.CSS_CLASS);
            }
            // logger.info("comp id="+comp.getClientId(facesContext)+ " baseClass = "+thumbnail.getBaseClass());
        } else if (facesContext.isProjectStage(ProjectStage.Development) ||
                logger.isLoggable(Level.FINER)) {
            logger.finer(" Cannot find camera or camcorder component with id=" + compId);
        }
        if (null == thumbnail.getMFor()) {  //only have to set it once
            thumbnail.setMFor(mFor);
        }
        ResponseWriter writer = facesContext.getResponseWriter();
        encode(thumbnail, writer, comp.getClientId(facesContext), comp);
    }

    public void encode(Thumbnail component, ResponseWriter writer, String mFor, UIComponent forComp) throws IOException {
        String clientId = component.getClientId();
        ClientDescriptor cd = component.getClient();
        boolean isForCamera = forComp instanceof Camera;
        if (cd.isDesktopBrowser() && !isForCamera ) {
            //    logger.info("desktop browser");
            return;
        }
        boolean renderThumbnail = false;
        if (cd.isICEmobileContainer() || cd.isSXRegistered() || isForCamera ) {
            renderThumbnail = true;
        }
        String thumbId = component.getMFor() + "-thumb";

        if (renderThumbnail) {
            writer.startElement(SPAN_ELEM, component);
            String styleClass = component.getBaseClass();
            if (component.getStyleClass() != null) {
                styleClass += " " + component.getStyleClass();
            }
            writer.writeAttribute(CLASS_ATTR, styleClass, null);
            String style = component.getStyle();
            if (style != null) {
                writer.writeAttribute(STYLE_ATTR, style, null);
            }
            writer.startElement(IMG_ELEM, component);

            //if thumb is for camera, always render the thumb for
            //for client side code, but hide it if the value is null
            //js code will unhide it when a photo is generated
            if( isForCamera ){ 
                Camera cam = (Camera)forComp;
                if( cam.getValue() == null || !cam.getValue().containsKey("file") ){
                    writer.writeAttribute(STYLE_ATTR, "display:none", null);
                }
            }
            writer.writeAttribute(WIDTH_ATTR, "64", null);
            writer.writeAttribute(HEIGHT_ATTR, "64", null);
            writer.writeAttribute(ID_ATTR, thumbId, null);
            String data = component.getData();
            if (data != null) writer.writeAttribute(SRC_ATTR, data, null);
            writer.endElement(IMG_ELEM);
    
            writer.startElement(INPUT_ELEM, component);
            writer.writeAttribute(TYPE_ATTR, "hidden", null);
            writer.writeAttribute(ID_ATTR, clientId + "_data", null);
            writer.writeAttribute(NAME_ATTR, clientId + "_data", null);
            writer.endElement(INPUT_ELEM);
            writer.endElement(SPAN_ELEM);
        }
        
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("if (!window['thumbnails" + mFor + "']) window['thumbnails" + mFor + "'] = {};");
        writer.write("window['thumbnails" + mFor + "']['" + thumbId + "'] = '" + clientId + "_data';");
        writer.endElement("script");
    }
}
