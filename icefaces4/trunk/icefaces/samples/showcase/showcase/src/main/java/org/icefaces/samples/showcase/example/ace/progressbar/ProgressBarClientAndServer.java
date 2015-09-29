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

package org.icefaces.samples.showcase.example.ace.progressbar; 

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.ace.event.ProgressBarChangeEvent;

@ManagedBean(name= ProgressBarClientAndServer.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarClientAndServer implements Serializable {

    public static final String BEAN_NAME = "progressBarClientAndServer";
	public String getBeanName() { return BEAN_NAME; }
    private int progressValue;
    private String message;
    /////////////---- CONSTRUCTOR BEGIN
    public ProgressBarClientAndServer() 
    {
        progressValue = 0;
        message = "";
    }

    /////////////---- EVENT LISTENERS BEGIN
    public void changeListener(ProgressBarChangeEvent event) 
    {
         progressValue= (int)event.getPercentage();
         message = (int)event.getPercentage() + "%";
    }
    /////////////---- GETTERS & SETTERS BEGIN
    public int getProgressValue() { return progressValue; }
    public void setProgressValue(int progressValue) { this.progressValue = progressValue; }
    public String getMessage() { return message; }
    public void setMessage(String message) {
        this.message = message;
    }


}
