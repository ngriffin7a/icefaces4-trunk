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

import org.icefaces.ace.api.StackPaneController;

import javax.faces.component.UIComponent;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.faces.view.facelets.*;

public class StackPaneHandler extends ComponentHandler {
    private static final Logger logger = Logger.getLogger(StackPaneHandler.class.getName());
    private static final String SKIP_CONSTRUCTION_KEY = "org.icefaces.ace.stackpane.SKIP_CONSTRUCTION";

    private final TagAttribute facelet;
    private final TagAttribute client;

    public StackPaneHandler(ComponentConfig componentConfig) {
        super(componentConfig);
        Tag tag = componentConfig.getTag();
        facelet = tag.getAttributes().get("facelet");
        client = tag.getAttributes().get("client");
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
   //     System.out.println("CPH.onComponentCreated()");
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

 //       System.out.println("CPH.applyNextHandler()");
        UIComponent parent = c.getParent();
        if (parent != null) {
            updateFlagShouldOptimiseSkipChildConstruction(ctx, c, parent);
        }
        if ( parent != null) {
            logger.fine("Parent id: " + parent.getId());
        }

        Boolean skip = (Boolean) c.getAttributes().get(SKIP_CONSTRUCTION_KEY);
        if (skip == null || !skip.booleanValue()) {
            logger.fine("  " + c.getId() + "\t\tConstruct children");
            super.applyNextHandler(ctx, c);
        }
        else {
            logger.fine("  " + c.getId() + "\t\tDon't construct children");
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

/*        logger.log(Level.FINE,"shouldOptimiseSkipChildConstruction()");
        logger.log(Level.FINE,"    c: " + c);
        logger.log(Level.FINE,"    c.id: " + c.getId());
        logger.log(Level.FINE,"    c.client: "+ ((StackPane)c).isClient());
        logger.log(Level.FINE,"    facelet: " + (facelet == null ? "<unspecified>" : facelet.getValue(ctx)));
        logger.log(Level.FINE,"    parent: " + parent);
        logger.log(Level.FINE,"    parent instanceof StackPaneController: " + (parent instanceof StackPaneController));*/
        if (parent instanceof StackPaneController) {
            logger.log(Level.FINE,"    parent.getSelectedId: " + ((StackPaneController)parent).getSelectedId());
            logger.log(Level.FINE,"    parent.childCount: " + parent.getChildCount());
            if (parent.getChildren().contains(c)) {
                    logger.log(Level.FINE,"    parent already has child  index: " + parent.getChildren().indexOf(c));
            }
            else {
                logger.log(Level.FINE,"    parent does not have child  index: " + parent.getChildCount());
            }
     /*       for (UIComponent kid : parent.getChildren())  {
                 logger.log(Level.FINE,"    kid: " + kid.getId() + "  attrib MARK_CREATED: " + kid.getAttributes().get("com.sun.faces.facelets.MARK_ID"));
            }  */
        }
        boolean overridden = false;
        if (client != null && client.getBoolean(ctx)==true){
             overridden = true;
        }
        logger.log(Level.FINE," overrid from client!! remove key");
        if (facelet == null || facelet.getBoolean(ctx)!=true || overridden==true) {
            logger.log(Level.FINE," facelet is false--  not tobeconstructed");
            return false;
        }
        logger.log(Level.FINE,"  facelet  is tobeconstructed");
        if (!(parent instanceof StackPaneController)) {
            logger.log(Level.FINE,"  parent not StackPaneController");
            return false;
        }
        logger.log(Level.FINE,"  parent is StackPaneController");
        String selectedId = ((StackPaneController)parent).getSelectedId();
        if (selectedId == null || selectedId.length() == 0) {
            logger.log(Level.FINE,"  selectedId not set");
            return false;
        }
        logger.log(Level.FINE,"  selectedId is set");
        if (selectedId.equals(c.getId())) {
            logger.log(Level.FINE,"  selectedId equal to id");
            return false;
        }
        logger.log(Level.FINE,"  selectedId not equal to id => SKIP");
        return true;
    }
}
