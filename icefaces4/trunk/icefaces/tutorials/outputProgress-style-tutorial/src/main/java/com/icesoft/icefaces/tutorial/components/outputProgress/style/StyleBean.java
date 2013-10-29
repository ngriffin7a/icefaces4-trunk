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

package com.icesoft.icefaces.tutorial.component.outputProgress.style;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import javax.faces.context.FacesContext;

/**
 * Class used to demonstrate the custom styling that can be applied to a progress bar
 * As the styling is done at a page level, this class really just handles the percent
 *  of the progress bar, and starting and stopping it's demonstration process
 */
public class StyleBean extends ProgressBar
{
    public StyleBean() {
        super();
    }
    
    /**
     * Method to start the progress bar
     *
     *@return "startProgress" String for faces-config navigation
     */
    public String startProgress() {
		PushRenderer.addCurrentSession("all");   
   		PortableRenderer renderer = PushRenderer.getPortableRenderer();
        start(renderer);
        
        return "startProgress";
    }
    
    /**
     * Method to stop the progress bar
     *
     *@return "stopProgress" String for faces-config navigation
     */
    public String stopProgress() {
        stop();
        
        return "stopProgress";
    }
}