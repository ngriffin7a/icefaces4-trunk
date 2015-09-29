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

package org.icefaces.samples.showcase.example.ace.linkButton;


import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;

@ManagedBean(name=LinkButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class LinkButtonBean implements Serializable {

    public static final String BEAN_NAME = "linkButton";
	public String getBeanName() { return BEAN_NAME; }
    private String staticNavigation;
    ImageSet.ImageInfo currentImage;
    
    public LinkButtonBean() {
        staticNavigation  = "showcase.jsf";
        currentImage = ImageSet.getNextImage(currentImage);
    }
    
    public String executeAction() {
        //application logic can be added here
        return null;
    }
    
    public void executeListener(ActionEvent event) 
    {
        currentImage = ImageSet.getNextImage(currentImage);
    }
    
    public ImageInfo getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(ImageInfo currentImage) {
        this.currentImage = currentImage;
    }

    public String getStaticNavigation() {
        return staticNavigation;
    }

    public void setStaticNavigation(String staticNavigation) {
        this.staticNavigation = staticNavigation;
    }
}
