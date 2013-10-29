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

package com.icesoft.faces.context.effects;

import com.icesoft.util.CoreComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.StringTokenizer;

/**
 * Make HTML Elements draggable or droppable
 * Makes the element drop (Move Down)  and fade out at the same time.
 */
public class DragDrop {


    /**
     * Make an HTML element draggable
     *
     * @param id
     * @param handleId
     * @param options
     * @param mask
     * @param facesContext
     * @return
     */
    public static String addDragable(String id, String handleId, String options,
                                     String mask,
                                     FacesContext facesContext) {
        boolean revert = false;
        boolean ghosting = false;
        boolean solid = false;
        boolean dragGhost = false;
        boolean pointerDraw = false;
        if (options != null) {
            StringTokenizer st = new StringTokenizer(options, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                token = token.trim();
                if ("revert".equalsIgnoreCase(token)) {
                    revert = true;
                } else if ("ghosting".equalsIgnoreCase(token)) {
                    ghosting = true;
                }
                if ("solid".equalsIgnoreCase(token)) {
                    solid = true;
                }
                if ("dragGhost".equalsIgnoreCase(token)) {
                    dragGhost = true;
                }
                if ("pointerDraw".equalsIgnoreCase(token)) {
                    pointerDraw = true;
                }
            }
        }
        return addDragable(id, handleId, revert, ghosting, solid, dragGhost,
                pointerDraw, mask, facesContext);
    }

    /**
     * make an HTML element draggable
     *
     * @param id
     * @param handleId
     * @param revert
     * @param ghosting
     * @param solid
     * @param dragGhost
     * @param pointerDraw
     * @param mask
     * @param facesContext
     * @return
     */
    public static String addDragable(String id, String handleId, boolean revert,
                                     boolean ghosting, boolean solid,
                                     boolean dragGhost, boolean pointerDraw,
                                     String mask, FacesContext facesContext) {

        EffectsArguments ea = new EffectsArguments();
        ea.add("handle", handleId);
        ea.add("revert", revert);
        ea.add("ghosting", ghosting);
        ea.add("mask", mask);
        ea.add("dragGhost", dragGhost);
        ea.add("dragCursor", pointerDraw);
        if (solid) {
            //Setting start and end effect functions to blank to remove transparency while dragging.
            ea.addFunction("starteffect", "function(){}");
            ea.addFunction("endeffect", "function(){}");
        }
        String call = "new Ice.Scriptaculous.Draggable('" + id + "'" + ea.toString();
        return call;
    }
	
    /**
     * Make an HTML element draggable with autoscrolling
     * @param id
     * @param handleId
     * @param options
     * @param mask
     * @param facesContext
     * @return
     */
    public static String addDragable(UIComponent uiComponent, String id, String handleId, String options,
                                     String mask,
                                     FacesContext facesContext) {
        boolean revert = false;
        boolean ghosting = false;
        boolean solid = false;
        boolean dragGhost = false;
        boolean pointerDraw = false;
        if (options != null) {
            StringTokenizer st = new StringTokenizer(options, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                token = token.trim();
                if ("revert".equalsIgnoreCase(token)) {
                    revert = true;
                } else if ("ghosting".equalsIgnoreCase(token)) {
                    ghosting = true;
                }
                if ("solid".equalsIgnoreCase(token)) {
                    solid = true;
                }
                if ("dragGhost".equalsIgnoreCase(token)) {
                    dragGhost = true;
                }
                if ("pointerDraw".equalsIgnoreCase(token)) {
                    pointerDraw = true;
                }
            }
        }
        return addDragable(uiComponent, id, handleId, revert, ghosting, solid, dragGhost,
                           pointerDraw, mask, facesContext);
    }

    /**
     * make an HTML element draggable with autoscrolling
     * @param id
     * @param handleId
     * @param revert
     * @param ghosting
     * @param solid
     * @param dragGhost
     * @param pointerDraw
     * @param mask
     * @param facesContext
     * @return
     */
    public static String addDragable(UIComponent uiComponent, String id, String handleId, boolean revert,
                                     boolean ghosting, boolean solid,
                                     boolean dragGhost, boolean pointerDraw,
                                     String mask, FacesContext facesContext) {

        EffectsArguments ea = new EffectsArguments();
        ea.add("handle", handleId);
        ea.add("revert", revert);
        ea.add("ghosting", ghosting);
        ea.add("mask", mask);
        ea.add("dragGhost", dragGhost);
        ea.add("dragCursor", pointerDraw);
		
		String scrollid = (String) uiComponent.getAttributes().get("dropTargetScrollerId"); 
         if (scrollid != null && scrollid.trim().length() > 0) { 
             UIComponent scroller = CoreComponentUtils.findComponentInView(facesContext.getViewRoot(), scrollid); 
             if (scroller != null) { 
                 scrollid = scroller.getClientId(facesContext); 
             } 
         } 
		if (scrollid != null && scrollid.trim().length() > 0) { 
          		  ea.add("scroll", scrollid); 
         }
		
        if (solid) {
            //Setting start and end effect functions to blank to remove transparency while dragging.
            ea.addFunction("starteffect", "function(){}");
            ea.addFunction("endeffect", "function(){}");
        }
		String call = "new Ice.Scriptaculous.Draggable('" + id + "'" + ea.toString();
        return call;
    }

    /**
     * Make an HTML element droppable
     *
     * @param uiComponent
     * @param acceptClass
     * @param facesContext
     * @param mask
     * @param hoverClass
     * @return
     */
    public static String addDroptarget(UIComponent uiComponent, String acceptClass,
                                       FacesContext facesContext, String mask,
                                       String hoverClass) {
        String id = uiComponent.getClientId(facesContext);
        String scrollid = (String) uiComponent.getAttributes().get("dropTargetScrollerId");
        if (scrollid != null && scrollid.trim().length() > 0) {
            UIComponent scroller = CoreComponentUtils.findComponentInView(facesContext.getViewRoot(), scrollid);
            if (scroller != null) {
                scrollid = scroller.getClientId(facesContext);
            }
        }
        EffectsArguments ea = new EffectsArguments();
        ea.add("accept", acceptClass);
        ea.add("mask", mask);
        ea.add("hoverclass", hoverClass);
        if (scrollid != null && scrollid.trim().length() > 0) {
            ea.add("scrollid", scrollid);
        }
        String call = "Ice.Scriptaculous.Droppables.add('" + id + "'" + ea.toString();
        JavascriptContext.addJavascriptCall(facesContext, call);
        return call;
    }
}
