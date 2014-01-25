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

package org.icefaces.mobi.component.audioplayer;


import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;


@Component(
        tagName = "audioPlayer",
        componentClass = "org.icefaces.mobi.component.audioplayer.AudioPlayer",
        rendererClass = "org.icefaces.mobi.component.audioplayer.AudioPlayerRenderer",
        generatedClass = "org.icefaces.mobi.component.audioplayer.AudioPlayerBase",
        componentType = "org.icefaces.AudioPlayer",
        rendererType = "org.icefaces.AudioPlayerRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.AudioPlayer",
        tlddoc = "This component renders an HTML5 audio element. The component can be bound to a variety of source types. " +
        		"Playback options can be controlled. JSF Resource management options are also available. "
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class AudioPlayerMeta extends UIComponentBaseMeta {

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLE)
     private String style;

    @Property(tlddoc = org.icefaces.mobi.util.TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(defaultValue = "auto", tlddoc = "Options for preloading the resource. Accepted values are " +
            "\"auto\" (allow the browser to decide), \"none\" or \"metadata\" (preload only the metadata). ")
    private String preload;

    @Property(defaultValue = "true", tlddoc = "If \"true\", will render the media controls. ")
    private boolean controls;

    @Property(tlddoc = "The media source. The value may resolve to a byte array, the String of a file name, " +
            "or a JSF Resource, if used in JSF project ")
    private Object value;

    @Property(defaultValue = "false", tlddoc = "If \"true\", will play the the audio file repeatedly. ")
    private boolean loop;

    @Property (defaultValue="false",
               tlddoc="Will cause the media to automatically play when the page is load. This may be required " +
                    "for some older devices to play. ")
    private boolean autoplay;

    @Property(defaultValue = "session", tlddoc="The JSF Resource scope of the object resolving from the \"value\" " +
            "attribute. Possible values are \"flash\", \"request\", \"view\", \"session\", and \"application\". ")
    private String scope;

    @Property(tlddoc = "The name is used for JSF Resource registration. ")
    private String name;

    @Property(tlddoc = "The library used for JSF Resource registration. ")
    private String library;

    @Property(tlddoc = "The URL or src of the media file. If the \"value\" attribute is empty, the \"url\" attribute will" +
            " be used. ")
    private String url;

    @Property(tlddoc = "The mime type of media file. ")
    private String type;

    @Property( tlddoc = "For certain devices, this adds the ability for a link to launch a system media player. If " +
            "this attribute is present thelink will appear with this text as the label.")
    private String linkLabel;

    @Property(defaultValue = "false", tlddoc=" audio will be muted if true. Default is false")
    private boolean muted;

    @Field
    private String srcAttribute;
}

