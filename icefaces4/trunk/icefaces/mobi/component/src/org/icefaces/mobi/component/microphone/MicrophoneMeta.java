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

package org.icefaces.mobi.component.microphone;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.util.TLDConstants;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

import java.util.Map;

@Component(
        tagName = "microphone",
        componentClass = "org.icefaces.mobi.component.microphone.Microphone",
        rendererClass = "org.icefaces.mobi.component.microphone.MicrophoneRenderer",
        generatedClass = "org.icefaces.mobi.component.microphone.MicrophoneBase",
        componentType = "org.icefaces.Microphone",
        rendererType = "org.icefaces.MicrophoneRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Microphone",
        tlddoc = "Renders a button to access the device audio recording features. " +
        		"The component will fall back to input file type element on unsupported " +
        		"clients. The audio file can then be uploaded, processed and stored " +
        		"on the server. "
)

@ResourceDependencies({
        @ResourceDependency(library = "icefaces.mobi", name = "core/bridgeit.js"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class MicrophoneMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "false", tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = TLDConstants.TABINDEX)
    private int tabindex;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(tlddoc = "The map object for the uploaded contents. Must " +
            "resolve to a java.util.Map<String,Object>. The uploaded " +
            "file will be available in the map with the key of \"file\". ")
    private Map<String, Object> value;

    @Property(defaultValue="false", tlddoc = TLDConstants.IMMEDIATE_INPUT)
    private boolean immediate;

    @Property(expression= Expression.METHOD_EXPRESSION, 
            methodExpressionArgument="javax.faces.event.ValueChangeEvent",
    	    tlddoc = TLDConstants.VALUECHANGELISTENER)
    private MethodExpression valueChangeListener;

    @Property(defaultValue= "Audio Captured", 
            tlddoc = "The button label to be displayed upon successful " +
            "capture of audio from the device.")
    private String captureMessageLabel;

    @Property(defaultValue="Record", tlddoc = "The button label.")
    private String buttonLabel;

    @Facets
    class FacetsMeta{
        @Facet(tlddoc = "Allows rendering of nested components that are displayed if the BridgeIt app cannot be used, either due to running on an unsupported platform (such as a desktop OS), or the BridgeIt app not being installed on the device.")
        UIComponent fallback;
    }
}
