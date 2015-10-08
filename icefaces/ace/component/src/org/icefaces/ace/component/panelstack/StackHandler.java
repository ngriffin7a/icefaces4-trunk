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

package org.icefaces.ace.component.panelstack;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import java.util.List;
import org.icefaces.ace.api.StackPaneController;
import org.icefaces.ace.component.panelstack.PanelStack;
import org.icefaces.ace.component.stackpane.StackPane;

import java.io.IOException;
import java.lang.Override;
import javax.faces.context.FacesContext;
import javax.el.ValueExpression;
import javax.el.ELContext;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.faces.view.facelets.*;

public class StackHandler extends ComponentHandler {
    private static final Logger logger = Logger.getLogger(StackHandler.class.getName());
    private static final String SELECTED_ID_KEY = "org.icefaces.ace.panelStack.KEY";

    private final TagAttribute selectedIdTag;

    public StackHandler(ComponentConfig componentConfig) {
        super(componentConfig);
        Tag tag = componentConfig.getTag();
        selectedIdTag = tag.getAttributes().get("selectedId");
    }


    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        String applySelId = selectedIdTag.getValue(ctx);
        String key= SELECTED_ID_KEY+c.getClientId();
        if (applySelId!=null && applySelId.trim().length()>0){
          // logger.info(" applynext handler has id="+applySelId);
           c.getAttributes().put(key, applySelId);
           c.getAttributes().put("parentId", c.getId());
           ctx.setAttribute(key,applySelId);
            //setSelectedId(ctx, c, applySelId);
        } else {
            logger.finer("PROBLEM>.... selectedId null but still apply next handler-> get from attribute map...") ;
          //  logger.info(" namingContainer ID="+c.getContainerClientId(facesContext));


        }
    }

}
