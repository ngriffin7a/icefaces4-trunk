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

package org.icefaces.samples.showcase.example.ace.videoPlayer;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.event.ActionEvent;

@ComponentExample(
        title = "example.ace.videoPlayer.title",
        description = "example.ace.videoPlayer.description",
        example = "/resources/examples/ace/videoPlayer/videoPlayer.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="videoPlayer.xhtml",
                    resource = "/resources/examples/ace/videoPlayer/videoPlayer.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="VideoPlayerBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/videoPlayer/VideoPlayerBean.java")
        }
)
@Menu(
    title = "menu.ace.videoPlayer.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.ace.videoPlayer.subMenu.main", isDefault = true, exampleBeanName = VideoPlayerBean.BEAN_NAME)
    }
)

@ManagedBean(name = VideoPlayerBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class VideoPlayerBean extends ComponentExampleImpl<VideoPlayerBean> implements Serializable {
    public static final String BEAN_NAME = "videoPlayerBean";
	public String getBeanName() { return BEAN_NAME; }
    
    public VideoPlayerBean() {
        super(VideoPlayerBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private boolean autoplay = false;
    private boolean newWindow = false;
    private boolean loop = false;

    private String linkLabel = null;
    private String value = "./resources/media/penrosetriangle.mp4";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isAutoplay() {
        return autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }

    public boolean isNewWindow() {
        return newWindow;
    }

    public void setNewWindow(boolean newWindow) {
        this.newWindow = newWindow;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public String getLinkLabel() {
        return linkLabel;
    }

    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }
}
