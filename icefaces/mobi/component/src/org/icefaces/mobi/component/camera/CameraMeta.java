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

package org.icefaces.mobi.component.camera;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.el.MethodExpression;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.mobi.util.TLDConstants;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import java.util.Map;

@Component(
        tagName = "camera",
        componentClass = "org.icefaces.mobi.component.camera.Camera",
        rendererClass = "org.icefaces.mobi.component.camera.CameraRenderer",
        generatedClass = "org.icefaces.mobi.component.camera.CameraBase",
        componentType = "org.icefaces.Camera",
        rendererType = "org.icefaces.CameraRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Camera",
        tlddoc = "Renders a button to access the camera on supported clients, and" +
        		" allows uploading and storing an image from the camera or image gallery. " +
        		"The component will fallback to a input file type on unsupported browsers. "
)

@ResourceDependencies({
        @ResourceDependency(library = "icefaces.mobi", name = "core/bridgeit.js"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class CameraMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "The maxium allowed width of image on those platforms where image cropping is supported. ")
    private int maxwidth;

    @Property(defaultValue = "Integer.MIN_VALUE",
            tlddoc = "The maximum allowed height of image on those platforms where image cropping is supported. ")
    private int maxheight;

    @Property(defaultValue = "false", tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

    @Property( tlddoc = TLDConstants.TABINDEX)
    private int tabindex;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(tlddoc = "The map object for the uploaded contents. Must resolve to a java.util.Map<String,Object>. " +
    		"The uploaded file will be available in the map with the key of \"file\". ")
    private Map<String, Object> value;

    @Property(expression= Expression.METHOD_EXPRESSION, 
            methodExpressionArgument="javax.faces.event.ValueChangeEvent",
    	    tlddoc = TLDConstants.VALUECHANGELISTENER)
    private MethodExpression valueChangeListener;

    @Property(defaultValue="false", tlddoc=TLDConstants.IMMEDIATE_INPUT)
    private boolean immediate;

    @Property(defaultValue="false", tlddoc = "When this attribute is \"true\" an HTML5 input " +
    		"acceptType=\"image/*\" will be used when the ICEmobile container is not available. ")
    private boolean useNative;

    @Property(defaultValue="Photo Captured", tlddoc = "The message to be displayed on the button on a successful " +
            "image capture. ")
    private String captureMessageLabel;

    @Property(defaultValue="Camera", tlddoc="The label to be displayed on the button. ")
    private String buttonLabel;

}
