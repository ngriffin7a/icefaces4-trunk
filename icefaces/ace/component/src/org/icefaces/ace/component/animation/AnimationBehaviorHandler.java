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

package org.icefaces.ace.component.animation;

import java.io.IOException;
import java.util.Collection;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;


public class AnimationBehaviorHandler extends AnimationBehaviorHandlerBase {
 
    public AnimationBehaviorHandler(BehaviorConfig config) {
		super(config);
	}
	
	private void setAttribute(FaceletContext context, 
            			TagAttribute tagAttribute, 
            				AnimationBehavior behavior,  
            					Class type) {

    	if (tagAttribute != null) {
    		behavior.setValueExpression(tagAttribute.getLocalName(),
    					tagAttribute.getValueExpression(context, type));
    	}    
    }
    

    public void apply(FaceletContext context, UIComponent parent)
    throws IOException {
    
        if (!ComponentHandler.isNew(parent)) {
            return;
        }

        if (!(parent instanceof ClientBehaviorHolder)) {
               throw new TagException(this.tag,
                                           "Effect behavior can not be attach to non clientBehaviorHolder parent");
		} 
        ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder)parent;
        String eventName = getEventName();
        if (eventName == null) {
        	eventName = clientBehaviorHolder.getDefaultEventName();
        	if (eventName == null)  {
                throw new TagException(this.tag,
                        "Event attribute could not be determined " + eventName);        		
        	}
        }else {
            Collection<String> eventNames = clientBehaviorHolder.getEventNames();
            if (!eventNames.contains(eventName)) {
                throw new TagException(this.tag,"Event is not supported by this component "+ clientBehaviorHolder.getClass().getSimpleName());
            }
        }
        
        AnimationBehavior effectBehavior = createEffectBehavior(context, eventName);
        clientBehaviorHolder.addClientBehavior(eventName, effectBehavior);
    }
    
    private AnimationBehavior createEffectBehavior(FaceletContext context, String eventName) {
    	
    	Application application = context.getFacesContext().getApplication();
    	AnimationBehavior effectBehavior = (AnimationBehavior)application.createBehavior(AnimationBehavior.BEHAVIOR_ID);
    	setAttribute(context, run, effectBehavior, Boolean.class);
    	setAttribute(context, effectObject, effectBehavior, Effect.class);   
    	setAttribute(context, name, effectBehavior, String.class);      	
    	setAttribute(context, to, effectBehavior, String.class);
    	setAttribute(context, from, effectBehavior, String.class);   
    	setAttribute(context, easing, effectBehavior, String.class);        	
    	setAttribute(context, iterations, effectBehavior, Integer.class);
    	setAttribute(context, duration, effectBehavior, Double.class);
    	return effectBehavior;
    }
 

 
}
