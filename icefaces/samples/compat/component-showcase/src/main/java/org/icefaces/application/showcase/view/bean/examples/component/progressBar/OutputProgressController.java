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

package org.icefaces.application.showcase.view.bean.examples.component.progressBar;

import com.icesoft.faces.async.render.SessionRenderer;

import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import javax.faces.event.ActionEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import java.io.Serializable;

/**
 * <p>The OutputProgressController is responsible for handling all user actions
 * for the OutputProgress demo.  This includes the the starting a long
 * running process to show how the progress bar can be used to monitor a
 * process on the server. </p>
 *
 * @see com.icesoft.faces.async.render.RenderManager
 * @since 1.7
 */
@ManagedBean(name = "outputProgressController")
@SessionScoped
public class OutputProgressController implements Serializable{

    public static Logger log = Logger.getLogger("OutputProgressController");

    // long running thread will sleep 10 times for this duration.
    public static final long PROCCESS_SLEEP_LENGTH = 300;
      
    // Model where we store the dynamic properties associated with outputProgress
    private OutputProgressModel outputProgressModel;

    /**
     * Default constructor where a reference to the PersistentFacesState is made
     * as well as the creation of the OutputProgressModel.  A reference to
     * PersistentFacesState is needed when implementing the Renderable
     * interface.
     */
    public OutputProgressController() {
        outputProgressModel = new OutputProgressModel();
        SessionRenderer.addCurrentSession("progressExample");
    }

    /**
     * A long running task is started when this method is called.  Actually not
     * that long around 10 seconds.   This long process {@link LongOperationRunner}
     * is responsible for updating the percent complete in the model class.
     *
     * @param event
     */
    public void startLongProcress(ActionEvent event) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(
                new LongOperationRunner(timer, outputProgressModel), 
                0, PROCCESS_SLEEP_LENGTH );
    }
 
    /**
     * Gets the outputProgressModel for this instance.
     *
     * @return OutputProgressModel which contains the state of various
     *         dynamic properties that are manipulated by this example.
     */
    public OutputProgressModel getOutputProgressModel() {
        return outputProgressModel;
    }

    /**
     * Utility class to represent some server process that we want to monitor
     * using ouputProgress and server push.
     */
    protected class LongOperationRunner extends TimerTask {
        private OutputProgressModel ouputProgressModel;
        private int percentComplete = 0;
        private Timer timer;

        public LongOperationRunner(Timer timer, OutputProgressModel ouputProgressModel) {
            this.ouputProgressModel = ouputProgressModel;
            this.timer = timer;
        }

        /**
         * Routine that takes time and updates percentage as it runs.
         */
        public void run() {
            if (0 == percentComplete)  {
                ouputProgressModel.setPogressStarted(true);
            }
            ouputProgressModel.setPercentComplete(percentComplete);
            SessionRenderer.render("progressExample");
            if (percentComplete >= 100)  {
                ouputProgressModel.setPogressStarted(false);
                timer.cancel();
            }
            percentComplete += 10;
        }

    }

   
}
