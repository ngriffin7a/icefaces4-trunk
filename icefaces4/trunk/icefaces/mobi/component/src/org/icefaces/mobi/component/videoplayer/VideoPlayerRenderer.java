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

package org.icefaces.mobi.component.videoplayer;

import static org.icefaces.mobi.util.HTML.ID_ATTR;
import static org.icefaces.mobi.util.HTML.SPAN_ELEM;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.mobi.util.MediaPlayerUtils;

public class VideoPlayerRenderer extends Renderer {
    private static final Logger log = Logger.getLogger(VideoPlayerRenderer.class.getName());

    public void encodeEnd(FacesContext facesContext, UIComponent uic)
            throws IOException {
        VideoPlayer videoPlayer = (VideoPlayer)uic;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uic.getClientId();
        writer. startElement(SPAN_ELEM, uic);
        writer.writeAttribute(ID_ATTR, clientId, null);
        String src = MediaPlayerUtils.deriveAndSetSourceAttribute(videoPlayer, facesContext);
        if (src!=null){
            videoPlayer.setSrcAttribute(src);
        }
        MediaPlayerUtils.writeStandardLayoutAttributes(writer, videoPlayer, "mobi-video");
        MediaPlayerUtils.encodeBaseMediaElementStart(writer, uic, facesContext, "video", src);
        encodeCustomMediaAttributes(videoPlayer, writer);
        MediaPlayerUtils.encodeBaseMediaElementEnd(writer, uic, facesContext, "video", src);
        writer.endElement(SPAN_ELEM);
    }
    

    void encodeCustomMediaAttributes(VideoPlayer videoPlayer, ResponseWriter writer)
        throws IOException{
        
        //poster attribute
        String poster = videoPlayer.getPoster();
        if( poster != null && poster.length() > 0 ){
            writer.writeAttribute("poster", poster, null);
        }
        //height attribute
        int height = videoPlayer.getHeight();
        if (height > 0) {
            writer.writeAttribute("height", String.valueOf(height), null);
        }
        //width attribute
        int width = videoPlayer.getWidth();
        if (width > 0) {
            writer.writeAttribute("width", String.valueOf(width), null);
        }
        //playsinline attribute
        if( videoPlayer.isPlaysinline() ){
            writer.writeAttribute("webkit-playsinline", "yes", null);
        }
    }

}
