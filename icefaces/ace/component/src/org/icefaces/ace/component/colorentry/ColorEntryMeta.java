/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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
package org.icefaces.ace.component.colorentry;

import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.HtmlInputTextMeta;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;
import org.icefaces.ace.model.colorEntry.ColorEntryLayout;

import java.util.List;

@Component(
        tagName = "colorEntry",
        componentClass = "org.icefaces.ace.component.colorentry.ColorEntry",
        rendererClass = "org.icefaces.ace.component.colorentry.ColorEntryRenderer",
        generatedClass = "org.icefaces.ace.component.colorentry.ColorEntryBase",
        extendsClass = "javax.faces.component.UIInput",
        componentType = "org.icefaces.ace.component.ColorEntry",
        rendererType = "org.icefaces.ace.component.ColorEntryRenderer",
        disinheritProperties = {"onclick","onblur", "onchange", "ondblclick", "onselect", "onmouseup", "onmousedown","onfocus", "onkeydown",
                                 "onkeypress", "onkeyup", "onmousemove", "onmouseover", "onmouseout"},
        componentFamily = "org.icefaces.ace.ColorEntry",
        tlddoc = "ColorEntry is a widget that creates a string for input component " +
                "that can display some string information which corresponds to a color" +
                " It also has custom styling for invalid state and required status." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/ColorPIcker\">ColorEntry Wiki Documentation</a>."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
    @ICEResourceDependency(name = "util/ace-jquery-ui.js"),
    @ICEResourceDependency(name = "colorentry/jquery.colorpicker.js"),
    @ICEResourceDependency(name = "colorentry/jquery.colorpicker.css"),
	@ICEResourceDependency(name = "colorentry/colorentry.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name="valueChange", javadoc="Fired when the component detects value is changed.",
                tlddoc="Fired when the original input changes. Only happens when the input is closed or the choose button is clicked",
                defaultRender="@all", defaultExecute="@this")
}, defaultEvent = "valueChange")
public class ColorEntryMeta extends UIInputMeta{

    @Property(defaultValue="false", tlddoc="Whether or not to show the input field for " +
            "the alpha string representing the chosen color")
    private boolean alpha;

 /*   @Property(defaultValue="true",
            tlddoc="Change the opacity of the altField element(s) according to the alpha setting")
    private boolean altAlpha;

    @Property(defaultValue="", tlddoc="Change the background color of the elements specified in this element")
    private String altField; */

    @Property(tlddoc="This is the preferred format to display the chosen color under the input field.  Valid values are hex, hex3, hsl, rbg, name, none",
            defaultValue="ColorFormat.HEX",
            defaultValueType = DefaultValueType.EXPRESSION)
    private ColorFormat colorFormat;

    @Property(tlddoc="title to display in the header")
    private String title;

  /*  @Property(defaultValue="false", tlddoc="If true, will show an inline Frame around the inline component.")
    private boolean inline;   */

    @Property(defaultValue = "", tlddoc = "The text to display on the trigger button. Use in conjunction with the showOn option set to \"button\" or \"all\". Default = \"\".")
    private String buttonText;

    @Property(defaultValue="true", tlddoc="if false, the dialog opens automatically upon page load")
    private boolean renderAsPopup;

    @Property(defaultValue="full", tlddoc="the preset definition of parts to be shown. Valid values are \'full\', \'popup\' or \'inline\'")
    private String presetParts;

    @Property(tlddoc="An array of values which can include \'header\', \'preview\',\'hex\',\'rgb\',\'alpha\', \'hsv\',\'rbgslider\',\'footer\'")
    private List<String> selectedParts;

    @Property(tlddoc="When selectedParts is used for custom configuration, this attribute defines the position of elements in a table layout.  " +
            "Any layout is possible with HTML tables by specifying cell position and size of each part. The value is an array with four coordinates on order [left, top, width, height].  " +
            "The coordinates correspond to cells in a table, so if you want to have a part at top-left and spanning two rows, and three columns, the value would be" +
            "preview, [0, 0, 3, 2] -- to show preview at that location. \'header\' and \'footer\' do not require a position so should only be listed in the selectedParts attribute. ")
    private List<ColorEntryLayout> customLayout;

    @Property(defaultValue="true", tlddoc="Show the Cancel button if buttonpane on popup is visible.  ")
    private boolean showCancelButton;

    @Property(defaultValue="true", tlddoc="Show the Close button if buttonpane on popup is visible")
    private boolean showCloseButton;

    @Property(defaultValue="false", tlddoc="Show the None/Revert button if buttonpane on popup is visible")
    private boolean showNoneButton;

 /*   @Property(defaultValue="false", tlddoc="Close the popup window when pressing the Enter key on the keyboard, keeping the selected color")
    private boolean okOnEnter;  */

    @Property(defaultValue = "all", tlddoc = "Have the colorEntry appear automatically when the field receives focus (\"focus\"), " +
            "appear only when a button (specified by popupIcon attribute) is clicked (\"button\"), or appear when either event takes place (\"all\").")
    private String showOn;


    @Property(tlddoc = "The URL for the popup button image. Default is the file \"META-INF/resources/icefaces.ace/datetimeentry/calendar_icon.png\" in the components jar. " +
            "showOn attribute must be set to \"button\" or \"both\".")
    private String popupIcon;

    @Property(tlddoc = "When enabled, popup icon is rendered without it appearing on a button. Default: \"false\".")
    private boolean popupIconOnly;

    @Property(defaultValue="true", tlddoc="If a popupIcon is specified, change the background color of the image when the color is changed")
    private boolean buttonColorize;

    @Property(tlddoc="Close thewindow when pressing the Escape key on the keyboard")
    private boolean closeOnEscape;

  /*  @Property(tlddoc="A list of available palettes may be used for the user to select from.  This must be a list of String arrays at this time")
    private List<String[]> swatches; */
    @Property( tlddoc="Limit the selectable colors to any of the predefined limits. default is empty string. \'websafe\'=Setof 216 colors composed of 00, 33, 66, 99, cc and ff color channel values in #rrggbb" +
            " \'nibble\'= 4 bits per color, can be easily converted to #rgb format.  The palette is limited to 4096 colors. \'binary\'=Allow only #00 or #ff as color channel values for primary " +
            "colors only; only 8 bolors are available with this limit, or \'name\'=limits to closes color name")
    private String limit;

    @Property(tlddoc = "Indicator indicating that the user is required to provide a submitted value for this input component.")
    private String requiredIndicator;

    @Property(tlddoc = "Indicator indicating that the user is NOT required to provide a submitted value for this input component.")
    private String optionalIndicator;

    @Property(tlddoc = "Position of input-required or input-optional indicator relative to input field or label. " +
            "Supported values are \"left/right/top/bottom/labelLeft/labelRight/none\". " +
            "Default is \"labelRight\" if labelPosition is  \"right\" otherwise.")
    private String indicatorPosition;

    @Property(tlddoc = "A localized user presentable name for this component.")
    private String label;

    @Property(tlddoc = "Position of label relative to input field. Supported values are \"left/right/top/bottom/none\". Default is \"none\".")
    private String labelPosition;

    @Property(
   		tlddoc="Custom inline CSS styles to use for this component. These styles are generally applied to the root DOM element of the component. This is intended for per-component basic style customizations. Note that due to browser CSS precedence rules, CSS rendered on a DOM element will take precedence over the external stylesheets used to provide the ThemeRoller theme on this component. If the CSS properties applied with this attribute do not affect the DOM element you want to style, you may need to create a custom theme styleClass for the theme CSS class that targets the particular DOM elements you wish to customize.")
   	private String style;

   	@Property(
   		tlddoc="Custom CSS style class(es) to use for this component. These style classes can be defined in your page or in a theme CSS file.")
   	private String styleClass;

    @Property(tlddoc = "Name of the animation used to show/hide the colorEntry. Use  \"slideDown\", \"fadeIn\", any of the show/hide " +
            "<a href=\"http://docs.jquery.com/UI/Effects\">jQuery UI effects</a>, or \"\" for no animation.", defaultValue = "show")
    private String effect;

    @Property(defaultValue = "normal", tlddoc = "Control the speed at which colorEntry appears, it may be a time in milliseconds or a string representing one of the three predefined speeds (\"slow\", \"normal\", \"fast\").")
    private String effectDuration;

    @Property(tlddoc="Sets the language to use.  Note that you must load the appropriate language file from the i8n directory. local is included by default")
    private String regional;
    @Property(tlddoc = "If true then this date time entry will be disabled and can not be entered.", defaultValue = "false")
    private boolean disabled;

    @Property(tlddoc = "If true then this date time entry will be read-only and can not be entered.")
    private boolean readonly;

    @Property(tlddoc = "Position of the text field in the tabbing order for the current page. This value must be an integer between 0 and 32767.")
    private String tabindex;

    @Property(tlddoc = "Access key that, when pressed, transfers focus to this component.")
   	String accesskey;

 /*   @Field
    private String color;   */

}
