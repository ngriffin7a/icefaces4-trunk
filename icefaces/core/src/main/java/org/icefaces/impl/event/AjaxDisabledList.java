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

package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIInput;
import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;

public class AjaxDisabledList implements SystemEventListener {
    private static final Logger log = Logger.getLogger(AjaxDisabledList.class.getName());
    public static String DISABLED_LIST = "org.icefaces.ajaxdisabledlist";

    public AjaxDisabledList() {
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(context)) {
            return;
        }
        UIComponentBase component = (UIComponentBase) 
                ((PreRenderComponentEvent) event).getComponent();

        boolean isDisabled = isAjaxDisabled(component);
        if (isDisabled)  {
            UIForm theForm = getContainingForm(component);
            if ((component instanceof UIForm) || isAjaxDisabled(theForm))  {
                log.finer("Ajax is disabled, so disabling form submit capturing");
                theForm.getAttributes().put(
                        FormSubmit.DISABLE_CAPTURE_SUBMIT, "true");
                //no need to list components if the form is ajaxDisabled
                return;
            }

            if (null != theForm)  {
                String disabledList = (String) theForm.getAttributes()
                    .get(DISABLED_LIST);
                if (null == disabledList)  {
                    //ensure that the final string contains spaces around
                    //each id for accurate JavaScript test with indexOf
                    disabledList = " ";
                }
                disabledList = disabledList + component.getClientId() + " ";
                theForm.getAttributes().put(DISABLED_LIST, disabledList);
            }
        }
    }

    public boolean isListenerForSource(Object source) {
        return ( (source instanceof UIInput) || 
                 (source instanceof UIForm) ||
                 (source instanceof UICommand) );
    }

    public static UIForm getContainingForm(UIComponent component)  {
        if (component instanceof UIForm)  {
            return (UIForm) component;
        }
        UIComponent parent = component.getParent();
        while ( (!(parent instanceof UIForm)) &&
                (!(parent instanceof UIViewRoot)) )  {
            parent = parent.getParent();
        }
        if (parent instanceof UIForm)  {
            return (UIForm) parent;
        }
        return null;
    }

    public static boolean isAjaxDisabled(UIComponentBase component)  {
        Map behaviors = component.getClientBehaviors(); 
        if (null == behaviors)  {
            return false;
        }
        boolean isDisabled = false;
        Iterator theBehaviors = behaviors.values().iterator();
        while (theBehaviors.hasNext())  {
        	ClientBehavior behavior = (ClientBehavior)(
                (List) theBehaviors.next() ).get(0);
        	if(!(behavior instanceof AjaxBehavior)) continue;
            if (((AjaxBehavior)behavior).isDisabled())  {
                isDisabled = true;
                break;
            }
        }
        return isDisabled;
    }
}

