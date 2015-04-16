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

package org.icefaces.ace.component.textentry;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.HtmlInputTextMeta;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "textEntry",
        componentClass = "org.icefaces.ace.component.textentry.TextEntry",
        rendererClass = "org.icefaces.ace.component.textentry.TextEntryRenderer",
        generatedClass = "org.icefaces.ace.component.textentry.TextEntryBase",
        extendsClass = "javax.faces.component.html.HtmlInputText",
        componentType = "org.icefaces.ace.component.TextEntry",
        rendererType = "org.icefaces.ace.component.TextEntryRenderer",
        disinheritProperties = {"onclick","onblur", "onchange", "ondblclick", "onselect", "onmouseup", "onmousedown","onfocus", "onkeydown",
                                 "onkeypress", "onkeyup", "onmousemove", "onmouseover", "onmouseout"},
        componentFamily = "org.icefaces.ace.TextEntry",
        tlddoc = "TextEntry is a text input component that can display some placeholder text inside the input field when the component doesn't have a value and is not focussed." +
                " It also has custom styling for invalid state and required status." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/TextEntry\">TextEntry Wiki Documentation</a>."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
	@ICEResourceDependency(name = "textentry/textentry.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name = "blur", javadoc = "Fired when the text input field loses focus (default event).",
                tlddoc = "Fired when the text input field loses focus (default event).", defaultRender = "@all", defaultExecute = "@this"),
        @ClientEvent(name = "charCount", javadoc = "Fired when the number of characters entered in the component changes, either by the " +
                "user typing a new character, deleting one or more characters, or cutting/pasting characters into the component.",
                tlddoc = "Fired when the number of characters entered in the component changes, either by the user typing a new " +
                        "character, deleting one or more characters, or cutting/pasting characters into the component.", defaultRender = "@all", defaultExecute = "@this"),
        @ClientEvent(name="valueChange", javadoc="Fired when the component detects value is changed.",
                tlddoc="Fired when the component detects value is changed.",
                defaultRender="@all", defaultExecute="@this")
}, defaultEvent = "valueChange")
public class TextEntryMeta extends HtmlInputTextMeta {

    @Property(tlddoc = "Name of the widget variable to access client-side API.")
    private String widgetVar;

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

    @Property(tlddoc = "When true the component will automatically tab to the next component once the maxLength number of characters have been entered.")
    private boolean autoTab;

    @Property(defaultValue = "false", tlddoc="If true, when the user types a string into this field, a row of asterisks will be  displayed instead of the text the user typed.")
    private boolean secret;

    @Property(defaultValue = "true", tlddoc="If false, the component will not redisplay its value when the page reloads.")
    private boolean redisplay;

	
	// ----------------------------------------
	// ----- imported from mobi:inputText -----
	// ----------------------------------------

    @Property(defaultValue = "text", tlddoc = " The HTML5 type attribute for the input element. " +
    		"Currently supports text, phone, url, email, number, date, time, datetime.  Depending " +
    		"on browser and/or device capability, a type-specific keyboard may be displayed and various other " +
            "attributes may apply for the html5 input element. It is up to the developer to ensure that " +
            "the browsers and devices they are targetting are supported properly.  Alternatively, the Mojarra HTML5 'type' passthrough attribute may also be used with this component.")
    private String type;

    @Property(tlddoc = "The HTML5 placeholder attribute represents a short hint" +
    		" (a word or short phrase) intended to aid the user with data entry " +
    		"when the input element has no value. Alternatively, the Mojarra HTML5 'placeholder' passthrough attribute may also be used with this component.")
    private String placeholder;

    @Property(tlddoc = "The HTML5 pattern attribute specifies a regular expression against which " +
    		"the control's value, or, when the multiple attribute applies and is set, " +
    		"the control's values, are to be checked. Alternatively, the Mojarra HTML5 'pattern' passthrough attribute may also be used with this component.")
    private String pattern;

    @Property(defaultValue = "off",
            tlddoc = "An HTML5 feature supported by mobile browsers which will automatically capitalize the first character of the field. Alternatively, the Mojarra HTML5 'autocapitalize' passthrough attribute may also be used with this component.")
    private String autocapitalize;

    @Property(defaultValue = "off",
            tlddoc = "The autocorrect is an HTML5 attribute that may correct spelling errors in the field if supported in the browser. Alternatively, the Mojarra HTML5 'autocorrect' passthrough attribute may also be used with this component.")
    private String autocorrect;

    @Property(defaultValue = "3", tlddoc = "Magnifying glass for webkit used to show last three searches on a search field. Alternatively, the Mojarra HTML5 'results' passthrough attribute may also be used with this component.")
    private int results;
    
    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="Minimum value is an HTML5 passthrough attribute and" +
            " only applicable to type number. " +
            "Will not be rendered if not present. Alternatively, the Mojarra HTML5 'min' passthrough attribute may be used with this component.")
    private int min;

    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="Maximum value is an html5 passthrough attribute, and " +
            " only applicable to type number. " +
            " Will not be rendered if attribute is not present. Alternatively, the Mojarra HTML5 'max' passthrough attribute may be used with this component.")
    private int max;
    
    @Property(defaultValue = "Integer.MIN_VALUE", tlddoc="The HTML5 step attributeto increase/decrease the value of the number input. " +
            "Requires min and max atribute. " +
            "Applicable only to type \"number\". Alternatively, the Mojarra HTML5 'step' passthrough attribute may also be used with this component.")
    private int step;

}
