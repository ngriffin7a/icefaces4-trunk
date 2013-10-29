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

package org.icefaces.ace.component.datetimeentry;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import org.icefaces.ace.api.IceClientBehaviorHolder;

import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "dateTimeEntry",
        componentClass = "org.icefaces.ace.component.datetimeentry.DateTimeEntry",
        generatedClass = "org.icefaces.ace.component.datetimeentry.DateTimeEntryBase",
        extendsClass = "javax.faces.component.UIInput",
        rendererClass = "org.icefaces.ace.component.datetimeentry.DateTimeEntryRenderer",
        componentFamily = "org.icefaces.ace.component.DateTimeEntry",
        componentType = "org.icefaces.ace.component.DateTimeEntry",
        rendererType = "org.icefaces.ace.component.DateTimeEntryRenderer",
        tlddoc = "The DateTime Entry is a component that allows the user to configure and input a date and/or time in various ways." +
                 "<p>For more information, see the " +
                "<a href=\"http://wiki.icefaces.org/display/ICE/DateTimeEntry\">DateTimeEntry Wiki Documentation</a>.")
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
     @ICEResourceDependency(name=ACEResourceNames.COMPONENTS_JS)
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="dateSelect", javadoc="Fired when a date is selected from the calendar (default event).", tlddoc="Fired when a date is selected from the calendar (default event).", defaultRender="@all", defaultExecute="@this", argumentClass="org.icefaces.ace.event.DateSelectEvent"),
	@ClientEvent(name="dateTextChange", defaultExecute="@this", defaultRender="@all", tlddoc="Fired when date text is changed and input field loses focus.", javadoc="Fired when date text is changed and input field loses focus.", argumentClass="org.icefaces.ace.event.DateTextChangeEvent")
}, defaultEvent="dateSelect")
public class DateTimeEntryMeta extends UIInputMeta {
    @Property(tlddoc = "Name of the client side widget.")
    private String widgetVar;

    @Property(tlddoc = "Set a minimum selectable date. Date string or java.util.Date object. Default is no limit.")
    private Object mindate;

    @Property(tlddoc = "Set a maximum selectable date. Date string or java.util.Date object. Default is no limit.")
    private Object maxdate;

    @Property(defaultValue = "1", tlddoc = "Enables multiple page rendering.")
    private int pages;

    @Property(defaultValue = "false", tlddoc = "Whether to render the calendar inline or as a popup.")
    private boolean renderAsPopup;

    @Property(defaultValue = "MM/dd/yyyy", tlddoc = "DateFormat pattern for localization. See the " +
            "<a href=\"http://wiki.icefaces.org/display/ICE/DateTimeEntry\">DateTimeEntry Wiki Documentation</a> for limitations. " +
            "Ignored if the converter attribute is used to do format conversion.")
    private String pattern;

    @Property(tlddoc = "Locale to be used for labels and conversion. Locale string or java.util.Locale object. Default is locale of view root.")
    private Object locale;

    @Property(tlddoc = "The URL for the popup button image. Default is the file \"META-INF/resources/icefaces.ace/datetimeentry/calendar_icon.png\" in the components jar. " +
            "showOn attribute must be set to \"button\" or \"both\".")
    private String popupIcon;

    @Property(tlddoc = "When enabled, popup icon is rendered without it appearing on a button. Default: \"false\".")
    private boolean popupIconOnly;

    @Property(tlddoc = "Enables month/year navigator.")
    private boolean navigator;

    @Property(tlddoc = "String or a java.util.TimeZone instance to specify the timezone used for date " +
            "conversion, defaults to TimeZone.getDefault()")
    private Object timeZone;

    @Property(tlddoc = "Makes input text of a popup calendar readonly.")
    private boolean readOnlyInputText;

    @Property(tlddoc = "Whether to show the panel containing today button and close button for popup. " +
            "Default is false without time component and true with time component.")
    private Boolean showButtonPanel;

    @Property(tlddoc = "Name of the animation used to show/hide the calendar. Use \"show\", \"slideDown\", \"fadeIn\", any of the show/hide " +
            "<a href=\"http://docs.jquery.com/UI/Effects\">jQuery UI effects</a>, or \"\" for no animation.", defaultValue = "show")
    private String effect;

    @Property(defaultValue = "normal", tlddoc = "Control the speed at which the calendar appears, it may be a time in milliseconds or a string representing one of the three predefined speeds (\"slow\", \"normal\", \"fast\").")
    private String effectDuration;

    @Property(defaultValue = "focus", tlddoc = "Have the calendar appear automatically when the field receives focus (\"focus\"), " +
            "appear only when a button (specified by popupIcon attribute) is clicked (\"button\"), or appear when either event takes place (\"both\").")
    private String showOn;

    @Property(tlddoc = "Displays the week number next to each week.")
    private boolean showWeek;

    @Property(tlddoc = "Displays days belonging to other months.")
    private boolean showOtherMonths;

    @Property(tlddoc = "Enables selection of days belonging to other months.")
    private boolean selectOtherMonths;

    @Property(tlddoc = "Control the range of years displayed in the year drop-down: either relative to today's year (-nn:+nn)," +
            " relative to the currently selected year (c-nn:c+nn), absolute (nnnn:nnnn), or combinations of these formats (nnnn:-nn).",
            defaultValue = "c-10:c+10")
    private String yearRange;

    @Property(tlddoc = "Specifies whether to display/input time only.", defaultValue = "false")
    private boolean timeOnly;

    @Property(defaultValue = "1", tlddoc = "Increment/decrement steps when hour slider is dragged.")
    private int stepHour;

    @Property(defaultValue = "1", tlddoc = "Increment/decrement steps when minute slider is dragged.")
    private int stepMinute;

    @Property(defaultValue = "1", tlddoc = "Increment/decrement steps when second slider is dragged.")
    private int stepSecond;

    @Property(defaultValue = "0", tlddoc = "Set the hour range.")
    private int minHour;

    @Property(defaultValue = "23", tlddoc = "Set the hour range.")
    private int maxHour;

    @Property(defaultValue = "0", tlddoc = "Set the minute range.")
    private int minMinute;

    @Property(defaultValue = "59", tlddoc = "Set the minute range.")
    private int maxMinute;

    @Property(defaultValue = "0", tlddoc = "Set the second range.")
    private int minSecond;

    @Property(defaultValue = "59", tlddoc = "Set the second range.")
    private int maxSecond;

    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

    @Property(tlddoc = "If true then this date time entry will be disabled and can not be entered.", defaultValue = "false")
    private boolean disabled;

    @Property(tlddoc = "If true then this date time entry will be read-only and can not be entered.")
    private boolean readonly;

    @Property(tlddoc = "Set to true to disable hover styling to improve performance.", defaultValue = "false")
    private boolean disableHoverStyling;

    @Property(defaultValue = "0", tlddoc = "Zero-based offset indicating which month should be displayed in the leftmost position.")
    private int leftMonthOffset;
	
    @Property(tlddoc = "Position of the text field in the tabbing order for the current page. This value must be an integer between 0 and 32767.")
    private String tabindex;

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

    @Property(tlddoc = "A localized user presentable name for this component.")
    private String label;

    @Property(tlddoc = "The number of characters used to determine the width of the input field in a popup calendar. Default" +
            " is the maximum possible size of a date formatted by the date pattern.")
    private int size;

    @Property(defaultValue = "false", tlddoc = "When singleSubmit is true, changing the value of this component will " +
            "submit and execute this component only (equivalent to &lt;f:ajax execute=\"@this\" render=\"@all\"&gt;). " +
            "When singleSubmit is false, no submit will occur. &lt;ace:ajax&gt; submit events have precedence over singleSubmit.")
    private boolean singleSubmit;

    @Property(defaultValue = "", tlddoc = "The text to display on the trigger button. Use in conjunction with the showOn option set to \"button\" or \"both\". Default = \"\".")
    private String buttonText;

    @Property(defaultValue = "true", tlddoc = "With lenient parsing, the Java date parser may use heuristics to interpret" +
            " inputs that do not precisely match this date's format. With strict parsing, inputs must match this date's format." +
            " Ignored if the converter attribute is used to do format conversion.")
    private boolean lenientParsing;

    @Property(tlddoc = "The maximum number of characters that may be entered in this field.")
    private int maxlength;

    @Property(defaultValue = "false", tlddoc = "The Today (or Now) button can be set to navigate the popup to the current month without selecting date/time, or select the date/time as well.")
    private boolean todayNowButtonsAlsoSelect;
}
