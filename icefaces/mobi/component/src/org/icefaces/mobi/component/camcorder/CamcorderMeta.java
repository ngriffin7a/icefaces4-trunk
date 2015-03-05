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

package org.icefaces.mobi.component.camcorder;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.util.TLDConstants;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

import javax.el.MethodExpression;
import java.util.Map;


@Component(
        tagName = "camcorder",
        componentClass = "org.icefaces.mobi.component.camcorder.Camcorder",
        rendererClass = "org.icefaces.mobi.component.camcorder.CamcorderRenderer",
        generatedClass = "org.icefaces.mobi.component.camcorder.CamcorderBase",
        componentType = "org.icefaces.Camcorder",
        rendererType = "org.icefaces.CamcorderRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Camcorder",
        tlddoc = "camcorder renders a button to access the device video recording capabilities. " +
        		"For non-supported clients it renders an input type file.  The " +
        		"recorded or selected video file can be uploaded to the server.")


@ResourceDependencies({
        @ResourceDependency(library = "icefaces.mobi", name = "core/bridgeit.js"),
        @ResourceDependency(library = "org.icefaces.component.camera", name = "camera.css"),
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class CamcorderMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "false", tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = TLDConstants.TABINDEX)
    private int tabindex;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

    @Property(tlddoc = "The map object for the uploaded contents. Must resolve to a java.util.Map<String,Object>. " +
            "The uploaded file will be available in the map with the key of \"file\". ")
    private Map<String, Object> value;

    @Property(defaultValue = "false", tlddoc = TLDConstants.IMMEDIATE_INPUT)
    private boolean immediate;

    @Property(expression= Expression.METHOD_EXPRESSION, 
            methodExpressionArgument="javax.faces.event.ValueChangeEvent",
    	    tlddoc = TLDConstants.VALUECHANGELISTENER)
    private MethodExpression valueChangeListener;

    @Property(defaultValue="Video Captured", tlddoc = "The message to be displayed on the button on a successful " +
            "video capture. ")
    private String captureMessageLabel;

    @Property(defaultValue="Camcorder", tlddoc = "The button label. ")
    private String buttonLabel;
}
