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
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;
import org.icefaces.samples.showcase.util.FacesUtils;


@ManagedBean(name= ProgressBarPush.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarPush implements Serializable {
    
    public static final String BEAN_NAME = "progressBarPush";
	public String getBeanName() { return BEAN_NAME; }
    
    private ImageSet.ImageInfo playImage;
    private ImageSet.ImageInfo stopImage;
    private ImageSet.ImageInfo pauseImage;
	private boolean indeterminate;
    
    public ProgressBarPush() 
    {
        playImage = ImageSet.getImage(ImageSet.ImageSelect.PLAY);
        stopImage = ImageSet.getImage(ImageSet.ImageSelect.STOP);
        pauseImage = ImageSet.getImage(ImageSet.ImageSelect.PAUSE);
    }
    
    public void startTask(ActionEvent event)
    {
        ProgressBarTaskManager threadBean = (ProgressBarTaskManager)FacesUtils.getManagedBean(ProgressBarTaskManager.BEAN_NAME);
        threadBean.startThread(10, 10, 1000);
    }

    public ImageInfo getPauseImage() {
        return pauseImage;
    }

    public void setPauseImage(ImageInfo pauseImage) {
        this.pauseImage = pauseImage;
    }

    public ImageInfo getPlayImage() {
        return playImage;
    }

    public void setPlayImage(ImageInfo playImage) {
        this.playImage = playImage;
    }

    public ImageInfo getStopImage() {
        return stopImage;
    }

    public void setStopImage(ImageInfo stopImage) {
        this.stopImage = stopImage;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }
}