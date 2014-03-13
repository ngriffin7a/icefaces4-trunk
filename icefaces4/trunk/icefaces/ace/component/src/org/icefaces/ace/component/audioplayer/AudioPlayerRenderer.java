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

package org.icefaces.ace.component.audioplayer;

import static org.icefaces.ace.util.HTML.ID_ATTR;
import static org.icefaces.ace.util.HTML.SPAN_ELEM;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.ace.util.MediaPlayerUtils;


public class AudioPlayerRenderer extends Renderer {
    private static final Logger log = Logger.getLogger(AudioPlayerRenderer.class.getName());


    public void encodeEnd(FacesContext facesContext, UIComponent uic)
            throws IOException {
            
        AudioPlayer audioPlayer = (AudioPlayer)uic;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uic.getClientId();
        writer. startElement(SPAN_ELEM, uic);
        writer.writeAttribute(ID_ATTR, clientId, null);
        String src = MediaPlayerUtils.deriveAndSetSourceAttribute(audioPlayer, facesContext);
        if (src!=null){
            audioPlayer.setSrcAttribute(src);
        }
        MediaPlayerUtils.writeStandardLayoutAttributes(writer, audioPlayer, "mobi-audio");
        MediaPlayerUtils.encodeBaseMediaElementStart(writer, uic, facesContext, "audio", src);
        MediaPlayerUtils.encodeBaseMediaElementEnd(writer, uic, facesContext, "audio", src);
        writer.endElement(SPAN_ELEM);
    }
    


}
