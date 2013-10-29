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

import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * As per http://jira.icefaces.org/browse/ICE-7118, we add a simple AjaxBehavior to MyFaces
 * h:commandLink components in order to ensure that the onclick handler for the link renders
 * out the Ajax specific code for form submission rather than the non-Ajax handler.
 */
public class CommandLinkModifier implements SystemEventListener {

    private static final Collection<String> AJAX_ALL = Collections.singletonList("@all");

    public boolean isListenerForSource(Object source) {

        //We only need to process this if it's an HtmlCommandLink in an ICEfaces view.
        //The listener is only registered if running under MyFaces.
        if (source instanceof HtmlCommandLink &&
                EnvUtils.isICEfacesView(FacesContext.getCurrentInstance())) {
            return true;
        }

        return false;
    }

    public void processEvent(SystemEvent event) {
        HtmlCommandLink link = (HtmlCommandLink) event.getSource();

        String onClick = link.getOnclick();
        Map behaviors = link.getClientBehaviors();

        //This is the same check that MyFaces makes to see if the link should render
        //out with the Ajax handler.
        if (onClick == null && (behaviors.isEmpty() ||
                (!behaviors.containsKey("click") &&
                        !behaviors.containsKey("action")))) {
            //Here we add an 'action' AjaxBehavior to help MyFaces process the link
            //as an 'ajax-ified' one. The normal ICEfaces mode is to execute/render
            //@all so we'll do the same by default here. If an f:ajax tag is present,
            //our various checks should prevent this from being applied at all.

            AjaxBehavior actionAjax = new AjaxBehavior();
            actionAjax.setExecute(AJAX_ALL);
            actionAjax.setRender(AJAX_ALL);
            link.addClientBehavior("action", actionAjax);

//            AjaxBehavior clickAjax = new AjaxBehavior();
//            clickAjax.setExecute(AJAX_ALL);
//            clickAjax.setRender(AJAX_ALL);
//            link.addClientBehavior("click", clickAjax);

        }
    }
}
