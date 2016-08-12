package org.icefaces.ace.component.colorpicker;


import org.icefaces.ace.meta.annotation.*;
import org.icefaces.ace.meta.baseMeta.HtmlInputTextMeta;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "colorPicker",
        componentClass = "org.icefaces.ace.component.colorpicker.ColorPicker",
        rendererClass = "org.icefaces.ace.component.colorpicker.ColorPickerRenderer",
        generatedClass = "org.icefaces.ace.component.colorpicker.ColorPickerBase",
        extendsClass = "javax.faces.component.html.HtmlInputText",
        componentType = "org.icefaces.ace.component.ColorPicker",
        rendererType = "org.icefaces.ace.component.ColorPickerRenderer",
        disinheritProperties = {"onclick","onblur", "onchange", "ondblclick", "onselect", "onmouseup", "onmousedown","onfocus", "onkeydown",
                                 "onkeypress", "onkeyup", "onmousemove", "onmouseover", "onmouseout"},
        componentFamily = "org.icefaces.ace.ColorPicker",
        tlddoc = "ColorPicker is a widget that creates a string for input component " +
                "that can display some string information which corresponds to a color" +
                " It also has custom styling for invalid state and required status." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/ColorPIcker\">ColorPicker Wiki Documentation</a>."
)

@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
	@ICEResourceDependency(name = "util/ace-core.js"),
	@ICEResourceDependency(name = "jquery/jquery.js"),
    @ICEResourceDependency(name = "colorpicker/spectrum.js"),
    @ICEResourceDependency(name = "colorpicker/spectrum.css"),
	@ICEResourceDependency(name = "colorpicker/colorpicker.js")
})
@ClientBehaviorHolder(events = {
        @ClientEvent(name="valueChange", javadoc="Fired when the component detects value is changed.",
                tlddoc="Fired when the component detects value is changed.",
                defaultRender="@all", defaultExecute="@this")
}, defaultEvent = "valueChange")
public class ColorPickerMeta extends HtmlInputTextMeta{

    @Property(tlddoc="preferred format to display the chosen color under the input field.  Valid values are hex, hex3, hsl, rbg,name, none",
            defaultValue="ColorFormat.HEX",
            defaultValueType = DefaultValueType.EXPRESSION)
    private ColorFormat preferredFormat;

    @Property(defaultValue="true")
    private boolean showInput;

    @Property(defaultValue="true")
    private boolean showButtons;

    @Property(defaultValue="cancel")
    private String cancelButtonText;

    @Property(defaultValue="choose")
    private String chooseText;

    @Property(defaultValue="true", tlddoc="If showInput is true, this attribute will show the initial value within the input field.")
    private boolean showInitial;

    @Property(defaultValue="false", tlddoc="If clickoutFiresChange is true, then any click outside of the control will fire a change event")
    private boolean clickoutFiresChange;

    @Property(defaultValue="true")
    private boolean showPalette;

    @Property(defaultValue="false", tlddoc="Used when attribute showPalette is true, the colorPicker can show just the palette")
    private boolean showPaletteOnly;

    @Property(defaultValue="true", tlddoc="When true, the colorpicker will show the previous selections made")
    private boolean showSelectionPalette;

    @Property(defaultValue="2", tlddoc=" when showSelectionPalette is true, this is the maximum number of previous color choices which will be shown.  Default is 2.")
    private int maxSelectionSize;

/*    @Property(defaultValue="false", tlddoc="if true, will toggle through multiple palettes")
    private boolean togglePaletteOnly;*/

    @Property(defaultValue="yellow", tlddoc="The initial color shown by the widget.")
    private String color;

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
}
