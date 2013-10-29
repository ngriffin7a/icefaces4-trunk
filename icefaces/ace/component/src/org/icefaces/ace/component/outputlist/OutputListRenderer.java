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

package org.icefaces.ace.component.outputlist;


import org.icefaces.ace.util.HTML;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OutputListRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(OutputListRenderer.class.getName());


     public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         OutputList list = (OutputList) uiComponent;
         //check to ensure children are all of type OutputListItem
         writer.startElement(HTML.UL_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);

         // apply component style classes.
         String userDefinedClass = list.getStyleClass();
         StringBuilder styleClasses = new StringBuilder(OutputList.OUTPUTLIST_CLASS);
         if (list.isInset()) {
             styleClasses.append(" ").append(OutputList.OUTPUTLISTINSET_CLASS);
         }
         if (userDefinedClass != null) {
             styleClasses.append(" ").append(userDefinedClass);
         }
         writer.writeAttribute("class", styleClasses.toString(), "styleClass");
         if (list.getStyle() !=null){
             writer.writeAttribute(HTML.STYLE_ATTR, list.getStyle(), HTML.STYLE_ATTR);
         }
         // verify the children are OutputListItem only
         if (facesContext.isProjectStage(ProjectStage.Development) ||
                 logger.isLoggable(Level.FINER)) {
             List<UIComponent> children = uiComponent.getChildren();
             for (UIComponent child : children) {
                 if (!(child instanceof OutputListItem)) {
                     logger.finer("The OutputList component allows only children of type OutputListItem or OutputListItems");
                 }
             }
         }
     }

     public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         writer.endElement(HTML.UL_ELEM);
     }
}
