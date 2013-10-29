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

package org.icefaces.ace.component.sliderentry;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.resources.ICEResourceLibrary;

import javax.el.MethodExpression;

@Component(
        // The tag name, as it will be used in view definitions (.xhtml files)
        tagName ="sliderEntry",
        // The end class that will be used by applications. Hand-coded.
        // The componentClass extends generatedClass, which extends extendsClass.
        componentClass ="org.icefaces.ace.component.sliderentry.SliderEntry",
        // The renderer, which outputs the html markup and javascript. Hand-coded. 
        rendererClass ="org.icefaces.ace.component.sliderentry.SliderEntryRenderer",
        // Generated, to contain all of the properties, getters, setters, and
        //  state saving methods. 
        generatedClass = "org.icefaces.ace.component.sliderentry.SliderEntryBase",
        // The super-class of the component. Did not extend UIInput, because
        //  none of the conversion nor validation facilities it provides are
        //  relevant to a slider.
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.ace.component.SliderEntry",
        rendererType = "org.icefaces.ace.component.SliderEntryRenderer",
        componentFamily="org.icefaces.ace.SliderEntry",
        tlddoc="<p>The Slider Entry is a component that enables the user to adjust values in a finite range along a " +
                "horizontal or vertical axis via dragging the slider control along the slider bar, or pressing the " +
                "arrow-keys. It can be used as a visual replacement for an input box that takes a " +
                "number as input.</p><p>For more information, see the " +
                "<a href=\"htp://wiki.icefaces.org/display/ICE/SliderEntry\">SliderEntry Wiki Documentation</a>.</p>"
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})

@ClientBehaviorHolder(events = {
	@ClientEvent( name="slideStart",
		javadoc="Fired when a drag operation on the slider control is initiated.",
		tlddoc="Fired when a drag operation on the slider control is initiated.",
		defaultRender="@all", defaultExecute="@this" ),
	@ClientEvent( name="slide",
		javadoc="Fired each time the the slider control is moved during a drag operation.",
		tlddoc="Fired each time the the slider control is moved during a drag operation.",
		defaultRender="@all", defaultExecute="@this"),
	@ClientEvent( name="slideEnd",
		javadoc="Fired when a drag operation is completed by releasing the slider control (default event).",
		tlddoc="Fired when a drag operation is completed by releasing the slider control (default event).", 
		defaultRender="@all", defaultExecute="@this") },
	defaultEvent="slideEnd" )
	
public class SliderEntryMeta extends UIComponentBaseMeta {

	@Property( tlddoc="The JavaScript object name that implements the client-side JavaScript API for this component." )
	private String widgetVar;
	
	@Property( 
		tlddoc="The minimum int value that can be selected in the value-entry range represented by the slider bar.",
		defaultValue="0" )
	private int min;
	
	@Property( 
		tlddoc="The maximum int value that can be selected in the value-entry range represented by the slider bar.", 
		defaultValue="100" )
	private int max;
	
	@Property( 
		tlddoc="Custom inline CSS styles to use for this component. These styles are generally applied to the root DOM element of the component. This is intended for per-component basic style customizations. Note that due to browser CSS precedence rules, CSS rendered on a DOM element will take precedence over the external stylesheets used to provide the ThemeRoller theme on this component. If the CSS properties applied with this attribute do not affect the DOM element you want to style, you may need to create a custom theme styleClass for the theme CSS class that targets the particular DOM elements you wish to customize.")
	private String style;
	
	@Property( 
		tlddoc="Custom CSS style class(es) to use for this component. These style classes can be defined in your page or in a theme CSS file.")
	private String styleClass;
	
	@Property( 
		tlddoc="Defines whether or not the slider control will use an animated transition to move to a new location when the user clicks outside handle on the slider rail.",
		defaultValue="true" )
	private boolean animate;
	
	@Property( 
		tlddoc="The orientation that the slider is rendered in, either vertical ('y'), or horizontal ('x').",
		defaultValue="x" )
	private String axis;
	
	@Property( 
		tlddoc="The amount to move the slider position in response to keyboard arrow-key input. This float value represents a percentage of the value-entry range defined by the min and max attributes. For example, with min='0', max='50', and stepPercent='10', each arrow keypress will increment/decrement the slider value by 5 (10% of 50).",
		defaultValue="1f" )
	private float stepPercent;
	
	@Property( 
		tlddoc="Defines whether or not the component is disabled. When disabled='true', this component is unable to receive focus and cannot be interacted with by the user.",
		defaultValue="false" )
	private boolean disabled;
	
	@Property( tlddoc="This event is fired when a drag operation on the slider control is initiated." )
	private String onSlideStart;
	
	@Property( tlddoc="This event is fired each time the the slider control is moved during a drag operation." )
	private String onSlide;
	
	@Property( tlddoc="This event is fired when a drag operation is completed by releasing the slider control." )
	private String onSlideEnd;
	
    @Property ( 
    	tlddoc="The value of the slider control.",
    	defaultValue="0" )
    private int value;
	
    @Property (
            tlddoc="The length of slider bar." +
            "Note: If the range of the slider (max-min) is greater than the length, " +
            "then the slider can not accurately represent every value in the range. " +
            "If the discrepancy is too great, then arrow key stepping may not " +
            "precisely reflect the stepPercent property.",
            defaultValue="150px" )
    private String length;
	
    @Property( 
    	tlddoc="Defines whether or not a mouse-click at a location along the slider rail should reposition the slider control to that location (and adjust the value accordingly).",
    	defaultValue="true")
    private boolean clickableRail;
	
    @Property( 
    	tlddoc="Defines whether or not labels for the min and max values should be rendered at the ends of the rail.",
    	defaultValue = "false")
    private boolean showLabels;
	
    @Property(
    	tlddoc="Defines whether or not conversion and validation of this component's value " +
            "should occur during Apply Request Values phase instead of Process Validations phase.",
    	defaultValue="false" )
    private boolean immediate;
	
    @Property ( tlddoc="The browser tabindex (int) of the component.")
    private Integer tabindex;
	
    // A MethodExpression Property is a special type, that does not generate
    //  the same code, as it does not use a ValueExpression, but instead
    //  describes a method to be called, and the parameter to pass to it.
    @Property(
    	expression= Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
    	tlddoc = "MethodExpression representing a value change listener method that will be notified when a new value has " +
            "been set for this input component. The expression must evaluate to a public method that takes a " +
            "ValueChangeEvent  parameter, with a return type of void, or to a public method that takes no arguments " +
            "with a return type of void. In the latter case, the method has no way of easily knowing what the new value " +
            "is, but this can be useful in cases where a notification is needed that \"this value changed\"." )
    private MethodExpression valueChangeListener;
}
