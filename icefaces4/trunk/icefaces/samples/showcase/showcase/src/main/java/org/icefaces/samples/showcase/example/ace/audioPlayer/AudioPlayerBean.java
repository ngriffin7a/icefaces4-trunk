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

package org.icefaces.samples.showcase.example.ace.audioPlayer;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.audioPlayer.title",
        description = "example.ace.audioPlayer.description",
        example = "/resources/examples/ace/audioPlayer/audioPlayer.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="audioPlayer.xhtml",
                    resource = "/resources/examples/ace/audioPlayer/audioPlayer.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AudioPlayerBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/audioPlayer/AudioPlayerBean.java")
        }
)
@Menu(
    title = "menu.ace.audioPlayer.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.ace.audioPlayer.subMenu.main", isDefault = true, exampleBeanName = AudioPlayerBean.BEAN_NAME)
    }
)

@ManagedBean(name = AudioPlayerBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AudioPlayerBean extends ComponentExampleImpl<AudioPlayerBean> implements Serializable {
    public static final String BEAN_NAME = "audioPlayerBean";

    public AudioPlayerBean() {
        super(AudioPlayerBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
        setGroup(13);
    }

    private boolean autoplay = false;
    private boolean newWindow = false;
    private boolean loop = false;
    private boolean muted = false;

    private String linkLabel = null;
    private String value = "./resources/media/canary.mp3";

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

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public String getLinkLabel() {
        return linkLabel;
    }

    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }
}
