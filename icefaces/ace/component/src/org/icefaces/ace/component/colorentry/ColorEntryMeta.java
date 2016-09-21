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

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;
import java.util.List;

@Component(
        tagName = "colorEntry",
        componentClass = "org.icefaces.ace.component.colorentry.ColorEntry",
        rendererClass = "org.icefaces.ace.component.colorentry.ColorEntryRenderer",
        generatedClass = "org.icefaces.ace.component.colorentry.ColorEntryBase",
        extendsClass = "javax.faces.component.html.HtmlInputText",
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
    @ICEResourceDependency(name = "colorentry/spectrum.js"),
    @ICEResourceDependency(name = "colorentry/spectrum.css"),
	@ICEResourceDependency(name = "colorentry/colorentry.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name="colorChange", javadoc="Fired when the component detects value is changed.",
                tlddoc="Fired when the original input changes. Only happens when the input is closed or the choose button is clicked",
                defaultRender="@all", defaultExecute="@this")
}, defaultEvent = "colorChange")
public class ColorEntryMeta extends HtmlInputTextMeta{

    @Property(tlddoc="This is the preferred format to display the chosen color under the input field.  Valid values are hex, hex3, hsl, rbg, name, none",
            defaultValue="ColorFormat.HEX",
            defaultValueType = DefaultValueType.EXPRESSION)
    private ColorFormat valueFormat;

    @Property(defaultValue="false", tlddoc="If true, showInput will show an input field that a value can be entered into.")
    private boolean showInput;

    @Property(defaultValue="false", tlddoc="The buttons on the color chooser may or may not be shown.  The text for these controls may be cusomized using " +
            "cancelButtonText and selectButtonLabel attributes.  The default is false or that they will not be shown.")
    private boolean showButtons;

    @Property(defaultValue="cancel")
    private String cancelButtonText;

    @Property(defaultValue="choose")
    private String selectButtonLabel;

    @Property(defaultValue="true", tlddoc="If showInput is true (default), this attribute will show the initial value within the input field.")
    private boolean showInitial;

    @Property(defaultValue="true", tlddoc="show the palette that can be selected.")
    private boolean showPalette;

    @Property(defaultValue="false", tlddoc="Used when attribute showPalette is true, the colorEntry can show just the palettes specified" +
            " and nothing else")
    private boolean showPaletteOnly;

    @Property(defaultValue="true", tlddoc="When true, the colorpicker will show the previous selections made. This is true by default")
    private boolean showSelectionPalette;

    @Property(defaultValue="2", tlddoc=" when showSelectionPalette is true, this is the maximum number of previous color choices which will be shown in the selection Palette.  Default is 2.")
    private int maxSelectionSize;

    @Property(tlddoc="A list of available palettes may be used for the user to select from.  This must be a list of String arrays at this time")
    private List<String[]> paletteList;

    @Property(defaultValue="false", tlddoc="if true, will toggle through multiple palettes")
    private boolean togglePaletteOnly;

    @Property(defaultValue="more", tlddoc="Text shown for togglePaletteOnly when more palettes than can be viewed in view area.")
    private String togglePaletteMoreText;

    @Property(defaultValue="less", tlddoc="Text shown for togglePaletteOnly when more and less palettes than can be viewed in view area.")
    private String togglePaletteLessText;

 /*   @Property(tlddoc="A color can be selected which is shown when the colorEntry component is first displayed. Afer a color is selected . ")
    private String color; */

    /* from textEntry */
    @Property(defaultValue = "true", tlddoc="If false, the component will not redisplay its value when the page reloads.")
    private boolean redisplay;

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

}
