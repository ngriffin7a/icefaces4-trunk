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

package com.icesoft.icefaces.tutorial.component.outputProgress.mode;

import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;
import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;

import java.io.Serializable;
import java.util.Random;
import javax.faces.context.FacesContext;
/**
 * Class used to allow the toggling of the two progress bar modes
 * Basically just track whether we should be determinate or indeterminate
 */
public class ModeBean implements Serializable
{
    private static final int PAUSE_AMOUNT_S = 300; // milliseconds to pause between progress increases
    protected int percent = 0;
    private static final long serialVersionUID = 7316741045420993728L;
    protected boolean isRunning = false;
    protected Thread progressThread;
    protected PortableRenderer renderer;
 	private static final String PUSH_GROUP = "all";

    public int getPercent() {
        return percent;
    }
    
    public boolean getIsRunning() {
        return isRunning;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
    
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    
    /**
     * Method to start the thread process
     * It is assumed the front end pages will handle the validity of when this method
     *  is called
     * For example, a start button will not be displayed if the progress bar is
     *  already running
     */
    public void start() {
        percent = 0;
        isRunning = true;
        // Create the progress thread
        progressThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    // Break the progress loop if we are asked to
                    if (!isRunning) {
                        break;
                    }
                    
                    // Sleep for the specified amount of time
                    try{
                        Thread.sleep(PAUSE_AMOUNT_S);
                    }catch (InterruptedException failedSleep) { }
                    
                    // Increase the finished percent, and render the page
                    // The standard approach would be to use the RenderManager
                    // But since this is a simple tutorial, we'll instead go directly
                    //  to the PersistentFacesState
                    try {
                        percent += 10;
                        
                        // Stop running if we reach the end
                        if (percent >= 100) {
                            isRunning = false;
                        }
                        
						renderer.render(PUSH_GROUP);
                    }catch (Exception failedProgress) {
                        failedProgress.printStackTrace();
                    }
                }
            }
        });
        
        progressThread.start();
    }
    
    /**
     * Method to stop the progress thread
     * This will toggle the isRunning state to false, and interrupt the thread
     * Since the isRunning boolean is checked inside the progress loop in the
     *  thread, switching it to false will stop the process from running
     */
    public void stop() {
        // Set the thread to stop running
        isRunning = false;
        
        // Interrupt the thread so the user gets an immediate response
        if (progressThread != null) {
            progressThread.interrupt();
        }
    }	
	
    private static final SelectItem[] AVAILABLE_MODES = {new SelectItem(Boolean.FALSE, "Determinate"),
                                                         new SelectItem(Boolean.TRUE, "Indeterminate")};
    private Boolean isIndeterminate = (Boolean)AVAILABLE_MODES[0].getValue();
    
    public ModeBean() {
	}
    
    public SelectItem[] getAvailableModes() {
        return AVAILABLE_MODES;
    }
    
    public Boolean getIsIndeterminate() {
        return isIndeterminate;
    }
    
    public boolean getIsIndeterminatePrimitive() {
        return isIndeterminate.booleanValue();
    }
    
    public void setIsIndeterminate(Boolean isIndeterminate) {
        this.isIndeterminate = isIndeterminate;
    }
    
    /**
     * Method to start the progress bar
     *
     *@return "startProgress" String for faces-config navigation
     */
    public String startProgress() {
    	PushRenderer.addCurrentSession(PUSH_GROUP);   
   		renderer = PushRenderer.getPortableRenderer();
		start();
        
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
    
    /**
     * Method called when the radio buttons are changed between the modes
     * This will just reset the percentage so the new mode has a fresh start
     *
     *@param vce event of the change
     */
    public void modeChanged(ValueChangeEvent vce) {
        percent = 0;
    }
}