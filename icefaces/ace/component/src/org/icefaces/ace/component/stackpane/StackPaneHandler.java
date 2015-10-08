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

package org.icefaces.ace.component.stackpane;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.*;
import org.icefaces.ace.api.StackPaneController;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;


public class StackPaneHandler extends ComponentHandler {
    private static final Logger logger = Logger.getLogger(StackPaneHandler.class.getName());
    private static final String SKIP_CONSTRUCTION_KEY = "org.icefaces.ace.stackpane.SKIP_CONSTRUCTION";
    private static final String SELECTED_ID_KEY = "org.icefaces.ace.panelStack.KEY";

    private final TagAttribute facelet;
    private final TagAttribute client;
    private final TagAttribute savedId;

    public StackPaneHandler(ComponentConfig componentConfig) {
        super(componentConfig);
        Tag tag = componentConfig.getTag();
        facelet = tag.getAttributes().get("facelet");
        client = tag.getAttributes().get("client");
        savedId = tag.getAttributes().get("id");
    }
   @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        String savedString = savedId.getValue(ctx);
        c.getAttributes().put("savedId", savedString);
        updateFlagShouldOptimiseSkipChildConstruction(ctx, c, parent);
    }

    @Override
    public void applyNextHandler(FaceletContext ctx, UIComponent c)
            throws java.io.IOException, javax.faces.FacesException, javax.el.ELException {
        // onComponentCreated(-) is called on GET and POST restore view,
        // but not POST render.
        // In applyNextHandler(-), on GET and POST restore view,
        // c.parent is null, but in POST render c.parent is non-null.
        // So for POST render, check if should skip in applyNextHandler(-),
        // using the non-null parent as the switch

        UIComponent parent = c.getParent();
        //parent might be c:forEach between the stackPanes and the panelStack

        if (parent != null) {
            String key = SELECTED_ID_KEY + parent.getClientId();
            String selectedId = (String)ctx.getAttribute(key);
            logger.finer("CPH.applyNextHandler() for StackPaneHandler parent id="+parent.getClientId() +" has selectedId="+selectedId);
            updateFlagShouldOptimiseSkipChildConstruction(ctx, c, parent);
        }
        Boolean skip = (Boolean) c.getAttributes().get(SKIP_CONSTRUCTION_KEY);
        if (skip == null || !skip.booleanValue()) {
            logger.log(Level.FINE,  "pane Id=" + savedId.getValue(ctx) + "\t\tConstruct children");
            super.applyNextHandler(ctx, c);
        }
        else {
            logger.log(Level.FINE,  "paneId=" + savedId.getValue(ctx) + "\t\tDont construct children");
        }
     }

    protected boolean updateFlagShouldOptimiseSkipChildConstruction(
            FaceletContext ctx, UIComponent c, UIComponent parent) {
        boolean skip = shouldOptimiseSkipChildConstruction(ctx, c, parent);
        if (skip) {
            c.getAttributes().put(SKIP_CONSTRUCTION_KEY, Boolean.TRUE);
        }
        else {
            c.getAttributes().remove(SKIP_CONSTRUCTION_KEY);
        }
        return skip;
    }

    protected boolean shouldOptimiseSkipChildConstruction(
            FaceletContext ctx, UIComponent c, UIComponent parent) {

        String selectedId = ((StackPaneController)parent).getSelectedId();
        String savedString = savedId.getValue(ctx);
        boolean overridden = false;
        if (selectedId == null || selectedId.length() == 0) {
         //   logger.log(Level.FINE,"  selectedId not set use fromAttMap");
             String key = SELECTED_ID_KEY + parent.getClientId();
             String fromAttMap = (String)ctx.getAttribute(key);
             if (fromAttMap !=null && fromAttMap.length()>0){
                 selectedId=fromAttMap;
             } else {
                 logger.log(Level.FINE, " without a selectedId facelet true is bypassed");
                 return false;
             }

        }
        if (selectedId !=null && selectedId.equals(savedString)) {
             c.getAttributes().put("selFlag", Boolean.TRUE);
        } else {
            c.getAttributes().remove("selFlag");
        }
        if (!(parent instanceof StackPaneController)) {
            logger.log(Level.FINE,"  parent not StackPaneController");
            return false;
        }
        if (client != null && client.getBoolean(ctx)==true){
            overridden = true;
            return false;
        }
        if (facelet == null || facelet.getBoolean(ctx)!=true || overridden==true) {
            logger.log(Level.FINE," FACELET FALSE FOR is="+c.getId()+" saved id="+savedId.getValue(ctx)) ;
            return false;
        }
        logger.log(Level.FINE,"  facelet  is tobeconstructed");

      //  logger.log(Level.FINE,"  selectedId is set");
        if (selectedId.equals(savedString)) {
            c.setId(c.getId()+savedString);
            return false;
        }
     //   logger.info("  selectedId not equal to id => SKIP for id="+savedString+" selectedId="+selectedId);
        return true;
    }
}
