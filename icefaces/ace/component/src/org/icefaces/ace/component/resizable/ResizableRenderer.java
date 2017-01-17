/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2014 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: (ICE-6978) Used JSONBuilder to add the functionality of escaping JS output.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 */
package org.icefaces.ace.component.resizable;

import org.icefaces.ace.event.ResizeEvent;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

@MandatoryResourceComponent(tagName="resizable", value="org.icefaces.ace.component.resizable.Resizable")
public class ResizableRenderer extends CoreRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        Resizable resizable = (Resizable) component;
        String clientId = resizable.getClientId(context);

        if(params.containsKey(clientId + "_ajaxResize")) {
            int width = (new Double(params.get(clientId + "_width"))).intValue();;
            int height = (new Double(params.get(clientId + "_height"))).intValue();;

            resizable.queueEvent(new ResizeEvent(resizable, width, height));
        }
        decodeBehaviors(context, resizable);
    }

    public void encodeBehaviors(FacesContext context, Resizable resizable, JSONBuilder jb) throws IOException {
        encodeClientBehaviors(context, resizable, jb);
    }
}