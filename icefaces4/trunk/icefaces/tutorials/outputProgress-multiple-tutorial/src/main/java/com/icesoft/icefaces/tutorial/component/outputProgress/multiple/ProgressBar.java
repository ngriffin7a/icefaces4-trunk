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

package com.icesoft.icefaces.tutorial.component.outputProgress.multiple;

import java.util.Random;
import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import javax.faces.context.FacesContext;
/**
 * Class used to track a name and percent
 * This will be used to represent a progress bar for a server
 */
public class ProgressBar
{
    private Random loadRandomizer;
    private String name = "Unknown";
    private int percent = 0;
    
    public ProgressBar(String name, Random loadRandomizer) {
        this.name = name;
        this.loadRandomizer = loadRandomizer;
        percent = loadRandomizer.nextInt(100);
    }
    
    public String getName() {
        return name;
    }
    
    public int getPercent() {
        return percent;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPercent(int percent) {
        this.percent = percent;
    }
    
    /**
     * Method to update the percent value
     * This will randomly increase or decrease the current percent, to
     *  give the illusion of real data
     */
    public void update() {
        // Determine how much to increase or decrease the percent by
        int changeAmount = 1+loadRandomizer.nextInt(14);
        
        // Randomly increase or decrease the percent using the value
        if (loadRandomizer.nextInt(100) >= 50) {
            percent -= changeAmount;
        }
        else {
            percent += changeAmount;
        }
        
        // Ensure the new percent is within the valid 0-100 range
        if (percent < 0) {
            percent = 0;
        }
        if (percent > 100) {
            percent = 100;
        }
    }
}
