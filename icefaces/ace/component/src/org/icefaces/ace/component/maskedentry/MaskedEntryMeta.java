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

package org.icefaces.ace.component.maskedentry;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import org.icefaces.ace.meta.annotation.*;

import javax.faces.component.html.HtmlInputText;
//import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import org.icefaces.ace.meta.baseMeta.*;

import org.icefaces.ace.api.IceClientBehaviorHolder;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName         = "maskedEntry",
        componentClass  = "org.icefaces.ace.component.maskedentry.MaskedEntry",
        rendererClass   = "org.icefaces.ace.component.maskedentry.MaskedEntryRenderer",
        generatedClass  = "org.icefaces.ace.component.maskedentry.MaskedEntryBase",
        extendsClass    = "javax.faces.component.html.HtmlInputText",
        componentType   = "org.icefaces.ace.component.MaskedEntry",
        rendererType    = "org.icefaces.ace.component.MaskedEntryRenderer",
		componentFamily = "org.icefaces.ace.MaskedEntry",
		tlddoc = "MaskedEntry is an input component that forces text input to be entered according to a specified format." +
                 "<p>For more information, see the " +
                 "<a href=\"http://wiki.icefaces.org/display/ICE/MaskedEntry\">MaskedEntry Wiki Documentation</a>."
        )
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
    @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="valueChange", javadoc="Fired every time the value of the text input changes (default event).", 
	tlddoc="Fired every time the value of the text input changes (default event). When pressing the ENTER key on the text field, this event will be fired as well, even if the value didn't actually change.", defaultRender="@all", defaultExecute="@all"),
    @ClientEvent(name = "keypress", defaultExecute = "@this", defaultRender = "@none", argumentClass = "org.icefaces.ace.event.KeyPressEvent",
               tlddoc = "Fired on a keypress event. Use only to read input char. Don't re-render or do JS-like event handling. Otherwise could cause focus and DOM update problems, losing both focus and value.",
              javadoc = "Fired on a keypress event. Use only to read input char. Don't re-render or do JS-like event handling. Otherwise could cause focus and DOM update problems, losing both focus and value."),
	@ClientEvent(name="blur", javadoc="Fired any time the text input field loses focus.", 
	tlddoc="Fired any time the text input field loses focus.", defaultRender="@all", defaultExecute="@this")
}, defaultEvent="valueChange")
public class MaskedEntryMeta extends HtmlInputTextMeta {

	@Property(tlddoc="Name of the widget variable to access client side api.")
    private String widgetVar;
	
	@Property(required=Required.yes, tlddoc="Masked input for separating input texts with given pattern. \nThese mask definitions can be used: \na - Represents an alpha character (A-Z,a-z) \n9 - Represents a numeric character (0-9) \n* - Represents an alphanumeric character (A-Z,a-z,0-9). All input is optional. Any character not in the definitions list will be automatically entered for the user as they type.")
	private String mask;
	
	@Property(tlddoc="Separator and placeholder in input.")
	private String placeHolder;

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

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Access key that, when pressed, transfers focus to this element.")
	private String accesskey;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Alternate textual description of the element rendered by this component.")
	private String alt;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "If the value of this attribute is \"off\", render \"off\" as the value of the attribute. This indicates that the browser should disable its autocomplete feature for this component. This is useful for components that perform autocompletion and do not want the browser interfering. If this attribute is not set or the value is \"on\", render nothing.")
	private String autocomplete;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Direction indication for text that does not inherit directionality. Valid values are \"LTR\" (left-to-right) and \"RTL\" (right-to-left).")
	private String dir;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "A localized user presentable name for this component.")
	private String label;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Code describing the language used in the generated markup for this component.")
	private String lang;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when this element loses focus.")
	private String onblur;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when this element loses focus and its value has been modified since gaining focus.")
	private String onchange;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is clicked over this element.")
	private String onclick;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is double clicked over this element.")
	private String ondblclick;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when this element receives focus.")
	private String onfocus;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a key is pressed down over this element.")
	private String onkeydown;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a key is pressed and released over this element.")
	private String onkeypress;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a key is released over this element.")
	private String onkeyup;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is pressed down over this element.")
	private String onmousedown;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is moved within this element.")
	private String onmousemove;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is moved away from this element.")
	private String onmouseout;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is moved onto this element.")
	private String onmouseover;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when a pointer button is released over this element.")
	private String onmouseup;

	@Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Javascript code executed when text within this element is selected by the user.")
	private String onselect;

    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "The number of characters used to determine the width of this field.")
    private int size;

    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "CSS style(s) to be applied when this component is rendered.")
    private String style;

    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Space-separated list of CSS style class(es) to be applied when this element is rendered. This value must be passed through as the \"class\" attribute on generated markup.")
    private String styleClass;

    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Position of this element in the tabbing order for the current document. This value must be an integer between 0 and 32767.")
    private String tabindex;

    @Property(implementation = Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Advisory title information about markup elements generated for this component.")
    private String title;

}
