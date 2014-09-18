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

package org.icefaces.samples.showcase.example.ace.notificationpanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.notificationpanel.title",
        description = "example.ace.notificationpanel.description",
        example = "/resources/examples/ace/notificationpanel/notificationPanel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="notificationPanel.xhtml",
                    resource = "/resources/examples/ace/notificationpanel/notificationPanel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="NotificationPanel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/notificationpanel/NotificationPanelBean.java")
        }
)
@Menu(
	title = "menu.ace.notificationpanel.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.notificationpanel.subMenu.main", isDefault = true,
                                         exampleBeanName = NotificationPanelBean.BEAN_NAME),
                        @MenuLink(title = "menu.ace.notificationpanel.subMenu.clientSide",
                        exampleBeanName = NotificationPanelClientBean.BEAN_NAME)
    }
)
@ManagedBean(name= NotificationPanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class NotificationPanelBean extends ComponentExampleImpl<NotificationPanelBean> implements Serializable {
    public static final String BEAN_NAME = "notificationPanelBean";
    
    private String imageLocation;
    private String imageAlt;
    private String imageDescription;
    private boolean visible;

    public NotificationPanelBean()
    {
        super(NotificationPanelBean.class);
        initializeBeanVariables();
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

   public void showAppropriateButton(ActionEvent e)
   {
       visible = !visible;
   }
   
   public void closeListener(AjaxBehaviorEvent event) {
        visible = false;
    }

    public void displayListener(AjaxBehaviorEvent event) {
        visible = true;
    }

   private void initializeBeanVariables() 
    {
        imageLocation = "/resources/css/images/dragdrop/vwBeatle.png";
        imageAlt = "VW Beatle";
        imageDescription = "The Volkswagen Type 1, widely known as the Volkswagen Beetle and Volkswagen Bug, is an economy car"
                                + " produced by the German auto maker Volkswagen (VW) from 1938."
                                + " With over 21 million manufactured in an air-cooled, rear-engined, rear-wheel drive configuration,"
                                + " the Beetle is the longest-running and most-manufactured automobile of a single design platform anywhere in the world. (source: Wikipedia)";
    }
   
    public String getImageDescription() { return imageDescription; }
    public void setImageDescription(String imageDescription) { this.imageDescription = imageDescription; }
    public String getImageAlt() { return imageAlt; }
    public void setImageAlt(String imageAlt) { this.imageAlt = imageAlt; }
    public String getImageLocation() { return imageLocation; }
    public void setImageLocation(String imageLocation) { this.imageLocation = imageLocation; }
    public boolean isVisible() {return visible;}
    public void setVisible(boolean visible) {this.visible = visible;}
}