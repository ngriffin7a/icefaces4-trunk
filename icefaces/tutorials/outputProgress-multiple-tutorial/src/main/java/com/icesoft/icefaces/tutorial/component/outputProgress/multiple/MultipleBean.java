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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import javax.faces.context.FacesContext;

/**
 * Class used to manage multiple progress bars
 * This will also use a thread to update the percent value of each progress bar
 */
public class MultipleBean implements Serializable
{
    private static final int PAUSE_AMOUNT_S = 1000; // milliseconds to pause between progress updates
    private Random randomizer = new Random(System.currentTimeMillis());
    private static final long serialVersionUID = -8801335546851905408L;
    private ArrayList progressBarList = generateDefaultList();
    private Thread updateThread;
    private boolean isRunning = true;
    
    public MultipleBean() {        
        startUpdateThread();
    }
    
    public ArrayList getProgressBarList() {
        return progressBarList;
    }
    
    public void setProgressBarList(ArrayList progressBarList) {
        this.progressBarList = progressBarList;
    }
    
    /**
     * Convience method to generate a list of data to test with
     *
     *@return the list of progress bars
     */
    private ArrayList generateDefaultList() {
        ArrayList toReturn = new ArrayList(7);
        
        toReturn.add(new ProgressBar("Calgary", randomizer));
        toReturn.add(new ProgressBar("Moscow", randomizer));
        toReturn.add(new ProgressBar("Tokyo", randomizer));
        toReturn.add(new ProgressBar("Vancouver", randomizer));
        toReturn.add(new ProgressBar("New York", randomizer));
        toReturn.add(new ProgressBar("Ashton", randomizer));
        toReturn.add(new ProgressBar("Rome", randomizer));
        
        return toReturn;
    }
    
    /**
     * Method to start the thread that will update the progress bars
     * Basically sleep for PAUSE_AMOUNT_S time, then request an update of each percent value
     */
	PortableRenderer renderer;
    private void startUpdateThread() {
		PushRenderer.addCurrentSession("all");   
   	 	renderer = PushRenderer.getPortableRenderer();
	
        updateThread = new Thread(new Runnable() {
            public void run() {
                final int size = progressBarList.size();
                
                while (isRunning) {
                    // Request an update of each progress bar
                    for (int i = 0; i < size; i++) {
                        ((ProgressBar)progressBarList.get(i)).update();
                    }
                    
                    try{
                        Thread.sleep(PAUSE_AMOUNT_S);
                    }catch (InterruptedException failedSleep) { }
                    
                    // Render the page
                    // The standard approach would be to use the RenderManager
                    // But since this is a simple tutorial, we'll instead go directly
                    //  to the PersistentFacesState
                    // If the render fails, we'll stop this thread from running
                    try{
                        renderer.render("all");
                    }catch (Exception failedRender) {
                        isRunning = false;
                    }
                }
            }
        });
        
        updateThread.start();
    }
}