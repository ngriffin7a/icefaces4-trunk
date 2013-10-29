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

package com.icesoft.jsfmeta.util;

import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.FacesConfigBean;
import com.sun.rave.jsfmeta.beans.FacetBean;
import com.sun.rave.jsfmeta.beans.PropertyBean;
import com.sun.rave.jsfmeta.beans.RenderKitBean;
import com.sun.rave.jsfmeta.beans.RendererBean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DebugUtil {
    
    private static Logger logger = Logger.getLogger(DebugUtil.class.getName());
    
    public DebugUtil() {
    }
    
    public static void print(FacesConfigBean config){
        
        ComponentBean cbs[] = config.getComponents();
        for (int i = 0; i < cbs.length; i++) {
            
            ComponentBean cb = cbs[i];
            if(logger.isLoggable(Level.FINE)){
                logger.log(Level.FINE, "Component( componentType="
                        + cb.getComponentType() + ",componentFamily="
                        + cb.getComponentFamily() + ",rendererType="
                        + cb.getRendererType() + ",baseComponentType="
                        + cb.getBaseComponentType() + ")"+
                        "");
            }
            
            if(cbs[i].getComponentFamily() == null || cbs[i].getComponentType() == null){
                continue;
            }
            
            RendererBean rendererBean = config.getRenderKit("HTML_BASIC").getRenderer(cbs[i].getComponentFamily(), cbs[i].getRendererType());
            
            if(rendererBean == null){
                continue;
            }
            
            if(logger.isLoggable(Level.FINE)){
                logger.log(Level.FINE, " tagName="+rendererBean.getTagName());
            }
            
            PropertyBean[] pbs = cbs[i].getProperties();
            for(int j=0; j< pbs.length;j++){
                
                if(logger.isLoggable(Level.FINE)){
                    logger.log(Level.FINE,"categoryName="+pbs[j].getCategory()+" propertyName="+pbs[j].getPropertyName());
                }
                
            }
        }
        
        for (int i = 0; i < cbs.length; i++) {
            ComponentBean cb = cbs[i];
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Component(componentType="
                        + cb.getComponentType() + ",componentFamily="
                        + cb.getComponentFamily() + ",rendererType="
                        + cb.getRendererType() + ",baseComponentType="
                        + cb.getBaseComponentType() + ")");
            }
            FacetBean fbs[] = cbs[i].getFacets();
            for (int j = 0; j < fbs.length; j++) {
                
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "  Facet(facetName="
                            + fbs[j].getFacetName() + ",displayName="
                            + fbs[j].getDisplayName("") + ")");
                }
            }
        }
        
        RenderKitBean rkbs[] = config.getRenderKits();
        for (int i = 0; i < rkbs.length; i++) {
            RenderKitBean rkb = rkbs[i];
            RendererBean rbs[] = rkb.getRenderers();
            for (int j = 0; j < rbs.length; j++) {
                RendererBean rb = rbs[j];
                if(logger.isLoggable(Level.FINE)){
                    logger.log(Level.FINE, "Renderer(renderKitId="
                            + rkb.getRenderKitId() + ",componentFamily="
                            + rb.getComponentFamily() + ",rendererType="
                            + rb.getRendererType() + ")");
                }
                FacetBean fbs[] = rbs[j].getFacets();
                for (int k = 0; k < fbs.length; k++){
                    
                    if(logger.isLoggable(Level.FINE)){
                        logger.log(Level.FINE, "  Facet(facetName="
                                + fbs[k].getFacetName() + ",displayName="
                                + fbs[k].getDisplayName("") + ")");
                    }
                }
                
            }
            
        }
    }
    
}
