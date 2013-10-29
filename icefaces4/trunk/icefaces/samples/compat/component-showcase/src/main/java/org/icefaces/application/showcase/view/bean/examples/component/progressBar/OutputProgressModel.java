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

import java.io.Serializable;

/**
 * <p>The OutputProgressModel class stores properties that are used to dynamically
 * change the state of the respective inputProgressBar component</p>
 *
 * @see org.icefaces.application.showcase.view.bean.examples.component.progressBar.OutputProgressController
 * @since 1.7
 */
public class OutputProgressModel implements Serializable  {
  
	
    // true indicates intermdiate mode, false indicates standard output progress
    // mode.
    private boolean intermediatMode;

    // Default value for progress bar label position. can be one of many different
    // types, see the TLD for outputProgress for more information.
    private String labelPosition = "embed";

    // Custom progress bar progress label and disable flag
    private String progressMessage;
    private boolean progressMessageEnabled;

    // Custom progress bar completed label and disable flag
    private String completedMessage;
    private boolean completedMessageEnabled;

    // flat indicating process is running, disables start button to avoid
    // queuing of actions.
    private boolean pogressStarted;

    // percent completed so far, value used directly by outputProgress
    // component.
    private int percentComplete;

    public boolean isIntermediatMode() {
        return intermediatMode;
    }

    public void setIntermediatMode(boolean intermediatMode) {
        this.intermediatMode = intermediatMode;
    }

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    /**
     * Gets the Progress message. If the progressMessageEnabled attribute is
     * set to false an empty String is returned.  This ensure that the component
     * will use its default percent value as the progress message.
     *
     * @return current value of progress bar if progressMessageEnabled is true,
     *         otherwise an empty string is returned.
     */
    public String getProgressMessage() {
        if (progressMessageEnabled) {
            return progressMessage;
        } else {
            return "";
        }
    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    /**
     * Gets the Progress completed message. If the completedMessageEnabled attribute is
     * set to false an empty String is returned.  This ensure that the component
     * will use its default completed message instead of the last entered
     * custom message.
     *
     * @return current value of progress bar if progressMessageEnabled is true,
     *         otherwise an empty string is returned.
     */
    public String getCompletedMessage() {
        if (completedMessageEnabled) {
            return completedMessage;
        } else {
            return "";
        }
    }

    public void setCompletedMessage(String completedMessage) {
        this.completedMessage = completedMessage;
    }

    public boolean isPogressStarted() {
        return pogressStarted;
    }

    public void setPogressStarted(boolean pogressStarted) {
        this.pogressStarted = pogressStarted;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public boolean isProgressMessageEnabled() {
        return progressMessageEnabled;
    }

    public void setProgressMessageEnabled(boolean progressMessageEnabled) {
        this.progressMessageEnabled = progressMessageEnabled;
    }

    public boolean isCompletedMessageEnabled() {
        return completedMessageEnabled;
    }

    public void setCompletedMessageEnabled(boolean completedMessageEnabled) {
        this.completedMessageEnabled = completedMessageEnabled;
    }


}
