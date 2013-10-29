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

package org.icefaces.ace.component.autocompleteentry;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.baseMeta.HtmlInputTextMeta;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

import javax.el.ValueExpression;
import javax.el.MethodExpression;

import java.util.List;

@Component(
        tagName = "autoCompleteEntry",
        componentClass = "org.icefaces.ace.component.autocompleteentry.AutoCompleteEntry",
        generatedClass = "org.icefaces.ace.component.autocompleteentry.AutoCompleteEntryBase",
        extendsClass = "javax.faces.component.html.HtmlInputText",
		rendererClass   = "org.icefaces.ace.component.autocompleteentry.AutoCompleteEntryRenderer",
        componentFamily = "org.icefaces.ace.AutoCompleteEntry",
        componentType = "org.icefaces.ace.component.AutoCompleteEntry",
		rendererType    = "org.icefaces.ace.component.AutoCompleteEntryRenderer",
        tlddoc = "AutoCompleteEntry is a text input component that presents possible valid options as the user types. " +
				"The options can be a list of SelectItem's specified in a child <f:selectItems /> tag. It is also possible " +
				"to specify a list of arbitrary data objects (i.e. POJOs) through the listValue attribute. In this case, a facet " +
				"named \"row\" should be nested inside this component. This allows for more flexible rendering of each row, making it possible " +
				"to render other components or HTML for each row and to display different properties of the data object. " +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/AutoCompleteEntry\">AutoCompleteEntry Wiki Documentation</a>."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent( name="submit",
		javadoc="Fired any time the value of the text input field is submitted to the server, either by typing a character, clicking on an option from the list, selecting an option with the keyboard, or pressing enter on the text field. If there are also textChange and/or valueChange events registered for this component, this event will not fire in those cases, since the other events are more specific and have precedence.",
		tlddoc="Fired any time the value of the text input field is submitted to the server, either by typing a character, clicking on an option from the list, selecting an option with the keyboard, or pressing enter on the text field. If there are also textChange and/or valueChange events registered for this component, this event will not fire in those cases, since the other events are more specific and have precedence.",
		defaultRender="@all", defaultExecute="@this" ),
	@ClientEvent( name="blur",
		javadoc="Fired any time the text input field loses focus.",
		tlddoc="Fired any time the text input field loses focus.",
		defaultRender="@all", defaultExecute="@this" ),
	@ClientEvent( name="textChange",
		javadoc="Fired any time the user adds or removes characters from the text field by typing or by pasting text.",
		tlddoc="Fired any time the user adds or removes characters from the text field by typing or by pasting text.",
		defaultRender="@all", defaultExecute="@this" ),
	@ClientEvent( name="valueChange",
		javadoc="Fired when the user gives a more definite input for this component either by clicking on an option from the list, or selecting an option with the keyboard, or pressing enter on the text field.",
		tlddoc="Fired when the user gives a more definite input for this component either by clicking on an option from the list, or selecting an option with the keyboard, or pressing enter on the text field.",
		defaultRender="@all", defaultExecute="@this" )},
	defaultEvent="submit" )
public class AutoCompleteEntryMeta extends HtmlInputTextMeta {
	
    @Property(tlddoc = "Style class name of the container element.", defaultValue="")
    private String styleClass;
	
    @Property(tlddoc = "Variable name to use for referencing each data object in the list when rendering via a facet.")
    private String listVar;
	
    @Property(tlddoc = "The maximum number of possible options to show to the user.", defaultValue="10")
    private int rows;
	
    @Property(tlddoc = "The width of the text input field, in pixels.", defaultValue="150")
    private int width;
	
    @Property(tlddoc = "When rendering via a facet, this attribute specifies the list of data objects that contains all possible options.")
    private List listValue;
	
	@Property(tlddoc="Defines the method of filter comparison used, default is \"startsWith\". " +
            "Types available include: \"contains\", \"exact\", \"startsWith\", \"endsWith\" and \"none\". " +
			"Typically, \"none\" will be used in cases where more complex, custom filtering is needed or when " +
			"option values need to be loaded lazily (e.g. from a data base).", defaultValue="startsWith")
	private String filterMatchMode;
	
	@Property(expression = Expression.VALUE_EXPRESSION,
            tlddoc="ValueExpression that specifies the property of the data object to use for filtering values. " +
			"This only applies when listvar is used and the rendering is done by means of a facet.")
	private Object filterBy;

    @Property(tlddoc = "Indicator indicating that the user is required to provide a submitted value for this input component.")
    private String requiredIndicator;

    @Property(tlddoc = "Indicator indicating that the user is NOT required to provide a submitted value for this input component.")
    private String optionalIndicator;

    @Property(tlddoc = "Position of label relative to input field. Supported values are \"left/right/top/bottom/inField/none\". Default is \"none\".")
    private String labelPosition;

    @Property(tlddoc = "Position of input-required or input-optional indicator relative to input field or label. " +
            "Supported values are \"left/right/top/bottom/labelLeft/labelRight/none\". " +
            "Default is \"labelRight\" if labelPosition is \"inField\", \"right\" otherwise.")
    private String indicatorPosition;
	
    @Property(tlddoc = "Delay in milliseconds for showing the list of possible matches after typing a character.", defaultValue="400")
    private int delay;

    @Property(tlddoc = "Minimum number of characters that must be in the text field before submitting and before producing the list of possible matches.", defaultValue="0")
    private int minChars;

    @Property(tlddoc = "Boolean value that indicates whether the filtering should be case sensitive or not.", defaultValue="false")
    private boolean caseSensitive;

    @Property(tlddoc = "Maximum height in pixels of the list of possible matches (if 0, then the size is automatically adjusted to show all possible matches).")
    private int height;

    @Property(tlddoc = "Direction in which to show the list of possible matches. Possible values are \"up\", \"down\", and \"auto\".")
    private String direction;
	
	@Property(tlddoc = "Boolean value that indicates whether the autocomplete functionality should be done on the client or on the server. Client-side mode can be faster, as no round trips to the server need to be made. However, if the list of possible results is too large, the browser might become slower, since the entire list has to be stored in the client as HTML nodes. It is recommended not to use lists of more than 1000 items when using the client-side mode.", defaultValue="false")
	private boolean clientSide;
	
    @Property(
    	expression= Expression.METHOD_EXPRESSION, methodExpressionArgument="org.icefaces.ace.event.TextChangeEvent",
    	tlddoc = "MethodExpression representing a text change listener method that will be notified when the text of the input field changes after the user types new characters or removes them. This is different from a value change event in that in this case the user has not yet given a definite input and is just typing strings to to obtain lists of possible values. The value change event differs in that it only fires once the user has selected a value from the list or has pressed 'enter' on the input field. The expression must evaluate to a public method that takes a org.icefaces.ace.event.TextChageEvent parameter, with a return type of void.")
    private MethodExpression textChangeListener;
	
	@Property(tlddoc="Effect to use when showing the list. Possible values are 'blind', 'bounce', 'clip', 'drop', 'explode', 'fade', 'fold', 'puff', 'pulsate', 'scale', 'slide', and 'shake'.", defaultValue="fade")
	private String showEffect;

	@Property(tlddoc="Length of time in milliseconds the show effect will last for.", defaultValue="150")
	private int showEffectLength;

	@Property(tlddoc="Effect to use when hiding the list. Possible values are 'blind', 'bounce', 'clip', 'drop', 'explode', 'fade', 'fold', 'puff', 'pulsate', 'scale', and 'slide'.", defaultValue="fade")
	private String hideEffect;

	@Property(tlddoc="Length of time in milliseconds the hide effect will last for.", defaultValue="150")
	private int hideEffectLength;
	
    @Property(tlddoc = "The HTML5 placeholder attribute represents a short hint (a word or short phrase) intended to aid the user with data entry " +
    		"when the input element has no value. If the placeholder attribute is not supported by the browser, the label 'inField' functionality " +
			"will be used instead.")
    private String placeholder;

    @Field(defaultValue="false")
    private Boolean populateList;
	
    @Field()
    private List itemList;
	
	@Field()
	private String text;
	
    @Field()
    private String submittedText;
}
