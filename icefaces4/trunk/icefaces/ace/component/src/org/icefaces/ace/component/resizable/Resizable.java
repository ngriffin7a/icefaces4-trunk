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
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */

/*
 * Generated, Do Not Modify
 */

package org.icefaces.ace.component.resizable;

import javax.faces.FacesException;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.el.MethodExpression;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;

import org.icefaces.ace.util.JSONBuilder;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.render.Renderer;
import java.io.IOException;

public class Resizable extends ResizableBase{

	private static final String OPTIMIZED_PACKAGE = "org.icefaces.ace.component.";

	public void broadcast(javax.faces.event.FacesEvent event) throws AbortProcessingException {
        if (event instanceof AjaxBehaviorEvent) {
            super.broadcast(event);
            return;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
		MethodExpression me = getResizeListener();
		if (me != null) {
			me.invoke(facesContext.getELContext(), new Object[] {event});
		}
	}


    public void attachRenderer() {
        final Resizable resizable = this;
        final UIComponent target = findTarget(resizable);

        UIOutput out = new Setup(target, resizable);
        target.getChildren().add(0, out);
    }

    private UIComponent findTarget(Resizable resizable) {
        String _for = resizable.getFor();

        if (_for != null) {
            UIComponent component = resizable.findComponent(_for);
            if (component == null)
                throw new FacesException("Cannot find component \"" + _for + "\" in view.");
            else
                return component;
        } else {
            return resizable.getParent();
        }
    }

    private class Setup extends UIOutput {
        private final UIComponent target;
        private final Resizable resizable;

        public Setup(UIComponent target, Resizable resizable) {
            this.target = target;
            this.resizable = resizable;
        }

        public void encodeEnd(FacesContext context) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            String clientId = this.getClientId(context);
            String targetId = target.getClientId(context);
            String handles = resizable.getHandles();
            int grid = resizable.getGrid();

            writer.startElement("span", this);
            writer.writeAttribute("id", clientId, null);
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);

            //If it is an image wait until the image is loaded
            if(target instanceof UIGraphic) {
                writer.write("ice.ace.jq(ice.ace.escapeClientId('" + targetId + "')).load(function(){");
            } else {
                writer.write("ice.ace.jq(function(){");
            }

            JSONBuilder jb = JSONBuilder.create();
            jb.beginFunction("ice.ace.create")
                    .item("Resizable")
                    .beginArray()
                    .item(clientId)
                    .beginMap()
                    .entry("target", targetId);

            //Boundaries
            int minWidth = resizable.getMinWidth();
            int maxWidth = resizable.getMaxWidth();
            int minHeight = resizable.getMinHeight();
            int maxHeight = resizable.getMaxHeight();

            if (minWidth != Integer.MIN_VALUE) jb.entry("minWidth", minWidth);
            if (maxWidth != Integer.MAX_VALUE) jb.entry("maxWidth", maxWidth);
            if (minHeight != Integer.MIN_VALUE) jb.entry("minHeight", minHeight);
            if (maxHeight != Integer.MAX_VALUE) jb.entry("maxHeight", maxHeight);

            //Animation
            if(resizable.isAnimate()) {
                jb.entry("animate", true);
                jb.entry("animateEasing", resizable.getEffect());
                jb.entry("animateDuration", resizable.getEffectDuration());
            }


            //Config
            if (resizable.isProxy()) jb.entry("helper", "ui-resizable-proxy");
            if (handles != null) jb.entry("handles", handles);
            if (grid != 1) jb.entry("grid", grid);
            if (resizable.isAspectRatio()) jb.entry("aspectRatio", true);
            if (resizable.isGhost()) jb.entry("ghost", true);
            //use  parent element not the root element of the parent component
            if (resizable.isContainment()) jb.entry("containment", "document.getElementById('" + target.getClientId(context) +"').parentNode", true);

            //Ajax resize
            if(resizable.getResizeListener() != null) {
                jb.entry("ajaxResize", true);
            }

            ((ResizableRenderer) resizable.getRenderer(context)).encodeBehaviors(context, resizable, jb);

            jb.endMap().endArray().endFunction();

            writer.write(jb.toString());
            writer.write("});");
            writer.endElement("script");
            writer.endElement("span");
        }
    }
}