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

package org.icefaces.ace.component.timespinner;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.PassThruAttributeWriter;
import org.icefaces.ace.util.Utils;
import org.icefaces.ace.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

public class TimeSpinnerRenderer extends InputRenderer {
    private static final Logger logger = Logger.getLogger(TimeSpinnerRenderer.class.getName());
    private static final String JS_NAME = "timespinner.js";
    private static final String JS_MIN_NAME = "timespinner-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.timespinner";

    public static final String TOUCH_START_EVENT = "ontouchstart";
    public static final String CLICK_EVENT = "onclick";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        TimeSpinner timeSpinner = (TimeSpinner) component;
        String clientId = timeSpinner.getClientId(context);
        if (timeSpinner.isDisabled()) {
            return;
        }
        String inputField = clientId + "_input";
        String inputValue = context.getExternalContext().getRequestParameterMap().get(inputField);
        
        if (shouldUseNative(timeSpinner)) {
            inputValue = context.getExternalContext().getRequestParameterMap().get(clientId);
            String twenty4HrString = convertToClock(inputValue, 24);
            timeSpinner.setSubmittedValue(twenty4HrString);
        } else {
            timeSpinner.setSubmittedValue(inputValue);
        }
        decodeBehaviors(context, timeSpinner);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        TimeSpinner spinner = (TimeSpinner) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = spinner.getClientId(context);
        ClientBehaviorHolder cbh = (ClientBehaviorHolder) component;
        boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();
        boolean singleSubmit = spinner.isSingleSubmit();
        String initialValue = ComponentUtils.getStringValueToRender(context, component);
        spinner.setTouchEnabled(Utils.isTouchEventEnabled(context));

        if (shouldUseNative(spinner)) {
            writer.startElement("input", component);
            writer.writeAttribute("type", "time", "type");
            writer.writeAttribute("id", clientId, null);
            writer.writeAttribute("name", clientId, null);
            if (spinner.getStyle()!=null){
                writer.writeAttribute(HTML.STYLE_ATTR, spinner.getStyle(), HTML.STYLE_ATTR);
            }
            boolean disabled = spinner.isDisabled();
            boolean readonly = spinner.isReadonly();
            String defaultPattern = "HH:mm";
            SimpleDateFormat df2 = new SimpleDateFormat(defaultPattern);
            if (isValueBlank(initialValue)) {
                Date aDate = new Date();
                writer.writeAttribute("value", df2.format(aDate), "value");
            } else {
                String clockVal24 = initialValue;
                if (!isFormattedDate(initialValue, "HH:mm")) {
                    clockVal24 = convertStringInput("EEE MMM dd hh:mm:ss zzz yyyy", defaultPattern, initialValue);
                }
                //check that only 24 hour clock came in.... as html5 input type="date" uses 24 hr clock
                writer.writeAttribute("value", clockVal24, "value");
            }
            if (disabled) {
                writer.writeAttribute("disabled", component, "disabled");
            }
            if (readonly) {
                writer.writeAttribute("readonly", component, "readonly");
            }
            if (!readonly && !disabled && hasBehaviors) {
                String event = spinner.getDefaultEventName(context);
                String cbhCall = this.buildAjaxRequest(context, cbh, event);
                writer.writeAttribute("onblur", cbhCall, null);
            } else if (!readonly && !disabled && singleSubmit) {
                writer.writeAttribute("onblur", "ice.se(event, this);", null);
            }
            writer.endElement("input");
        } else {
            writeJavascriptFile(context, component, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
            String value = this.encodeValue(spinner, initialValue);
            encodeMarkup(context, component, value, hasBehaviors);
            encodeScript(context, component);
        }
    }

    protected void encodeMarkup(FacesContext context, UIComponent uiComponent,
                                String value, boolean hasBehaviors) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        TimeSpinner timeEntry = (TimeSpinner) uiComponent;
        String clientId = timeEntry.getClientId(context);
        ClientBehaviorHolder cbh = (ClientBehaviorHolder) uiComponent;
        String eventStr = timeEntry.isTouchEnabled() ?
                TOUCH_START_EVENT : CLICK_EVENT;
        boolean readonly = timeEntry.isReadonly();
        boolean disabled = timeEntry.isDisabled();
        boolean disabledOrReadonly = false;
        if (readonly || disabled){
            disabledOrReadonly = true;
        }
        //first do the input field and the button
        // build out first input field
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        if (timeEntry.getStyle()!=null){
            writer.writeAttribute(HTML.STYLE_ATTR, timeEntry.getStyle(), HTML.STYLE_ATTR);
        }
        writer.startElement("span", uiComponent);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("name", clientId, "name");
        writer.writeAttribute("class", "mobi-time-wrapper", "class");
        writer.startElement("input", uiComponent);
        writer.writeAttribute("id", clientId + "_input", "id");
        writer.writeAttribute("name", clientId + "_input", "name");
        // apply class attribute and pass though attributes for style.
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent,
                timeEntry.getCommonAttributeNames());
        // apply class attribute and pass though attributes for style.
        StringBuilder classNames = new StringBuilder(TimeSpinner.INPUT_CLASS)
                .append(" ").append(timeEntry.getStyleClass());
        writer.writeAttribute("class", classNames.toString(), null);
        if (value != null) {
            writer.writeAttribute("value", value, null);
        }
        writer.writeAttribute("type", "text", "type");
        if (readonly)
            writer.writeAttribute("readonly", "readonly", null);
        if (disabled)
            writer.writeAttribute("disabled", "disabled", null);
        writer.endElement("input");
        writer.endElement("span");
        // build out command button for show/hide of date select popup.
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "button", "type");
        writer.writeAttribute("value", "", null);
        writer.writeAttribute("class", TimeSpinner.POP_UP_CLASS, null);
        if (timeEntry.isDisabled())
            writer.writeAttribute("disabled", "disabled", null);
        if (!disabledOrReadonly){
            // touch event can be problematic sometime not actualy getting called
            // for ui widgets that don't require rapid response then stick with onClick
            writer.writeAttribute(CLICK_EVENT, "mobi.timespinner.toggle('" + clientId + "');", null);
        }
        writer.endElement("input");
        writer.endElement(HTML.SPAN_ELEM);
        // div that is use to hide/show the popup screen black out.
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", clientId + "_bg", "id");
        writer.writeAttribute("class", TimeSpinner.BLACKOUT_PNL_INVISIBLE_CLASS, "class");
        writer.endElement("div");
        // actual popup code.
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", clientId + "_popup", "id");
        writer.writeAttribute("class", TimeSpinner.CONTAINER_INVISIBLE_CLASS, null);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", clientId + "_title", "id");
        writer.writeAttribute("class", TimeSpinner.TITLE_CLASS, null);
        writer.startElement("span", uiComponent);
        writer.writeAttribute("class", "icon-time", null);
        writer.endElement("span");
        writer.startElement("span", uiComponent);
        writer.writeAttribute("class", "mobi-time-title", null);
        writer.endElement("span");
        writer.endElement("div");
        writer.startElement("div", uiComponent);                            //entire selection container
        writer.writeAttribute("class", TimeSpinner.SELECT_CONT_CLASS, null);
        //hour
        writer.startElement("div", uiComponent);                             //hour select container
        writer.writeAttribute("class", TimeSpinner.VALUE_CONT_CLASS, null);
        writer.startElement("div", uiComponent);                            //button increment
        writer.writeAttribute("class", TimeSpinner.BUTTON_INC_CONT_CLASS, null);

        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", TimeSpinner.BUTTON_INC_CLASS, null);
        writer.writeAttribute("id", clientId + "_hrUpBtn", null);
        writer.writeAttribute(eventStr, "mobi.timespinner.hrUp('" + clientId + "');", null);
        writePlusIcon(writer);
        writer.endElement("button");

        writer.endElement("div");                                         //end button incr
        writer.startElement("div", uiComponent);                          //hour value
        writer.writeAttribute("class", TimeSpinner.SEL_VALUE_CLASS, null);
        writer.writeAttribute("id", clientId + "_hrInt", null);
        writer.write(String.valueOf(timeEntry.getHourInt()));
        writer.endElement("div");                                         //end of hour value
        writer.startElement("div", uiComponent);                          //button decrement
        writer.writeAttribute("class", TimeSpinner.BUTTON_DEC_CONT_CLASS, null);
        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", TimeSpinner.BUTTON_DEC_CLASS, null);
        writer.writeAttribute("id", clientId + "_hrDnBtn", null);
        writer.writeAttribute(eventStr, "mobi.timespinner.hrDn('" + clientId + "');", null);
        writeMinusIcon(writer);
        writer.endElement("button");
        writer.endElement("div");                                         //end button decrement
        writer.endElement("div");                                         //end of timeEntry select container
        //minute
        writer.startElement("div", uiComponent);                             //minute select container
        writer.writeAttribute("class", TimeSpinner.VALUE_CONT_CLASS, null);
        writer.startElement("div", uiComponent);                            //button increment
        writer.writeAttribute("class", TimeSpinner.BUTTON_INC_CONT_CLASS, null);
        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", TimeSpinner.BUTTON_INC_CLASS, null);
        writer.writeAttribute("id", clientId + "_mUpBtn", null);
        writer.writeAttribute(eventStr, "mobi.timespinner.mUp('" + clientId + "');", null);
        writePlusIcon(writer);
        writer.endElement("button");
        writer.endElement("div");                                         //end button incr
        writer.startElement("div", uiComponent);                          //minute value
        writer.writeAttribute("class", TimeSpinner.SEL_VALUE_CLASS, null);
        writer.writeAttribute("id", clientId + "_mInt", null);
        writer.write(String.valueOf(timeEntry.getMinuteInt()));
        writer.endElement("div");                                         //end of minute value
        writer.startElement("div", uiComponent);                          //button decrement
        writer.writeAttribute("class", TimeSpinner.BUTTON_DEC_CONT_CLASS, null);
        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", TimeSpinner.BUTTON_DEC_CLASS, null);
        writer.writeAttribute("id", clientId + "_mDnBtn", null);
        writer.writeAttribute(eventStr, "mobi.timespinner.mDn('" + clientId + "');", null);
        writeMinusIcon(writer);
        writer.endElement("button");
        writer.endElement("div");                                         //end button decrement
        writer.endElement("div");                                         //end of minute  select container
        //ampm
        writer.startElement("div", uiComponent);                             //mpm select container
        writer.writeAttribute("class", TimeSpinner.VALUE_CONT_CLASS, null);
        writer.startElement("div", uiComponent);                            //button increment
        writer.writeAttribute("class", TimeSpinner.BUTTON_INC_CONT_CLASS, null);
        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", TimeSpinner.BUTTON_INC_CLASS, null);
        writer.writeAttribute("id", clientId + "_ampmUpBtn", null);
        writer.writeAttribute(eventStr, "mobi.timespinner.ampmToggle('" + clientId + "');", null);
        writePlusIcon(writer);
        writer.endElement("button");
        writer.endElement("div");                                         //end button incr
        writer.startElement("div", uiComponent);                          //year value
        writer.writeAttribute("class", TimeSpinner.SEL_VALUE_CLASS, null);
        writer.writeAttribute("id", clientId + "_ampmInt", null);
        String ampm = "AM";
        if (timeEntry.getAmpm() > 0) {
            ampm = "PM";
        }
        writer.write(ampm);
        writer.endElement("div");                                         //end of year value
        writer.startElement("div", uiComponent);                          //button decrement
        writer.writeAttribute("class", TimeSpinner.BUTTON_DEC_CONT_CLASS, null);
        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", TimeSpinner.BUTTON_DEC_CLASS, null);
        writer.writeAttribute("id", clientId + "_ampmBtn", null);
        writer.writeAttribute(eventStr, "mobi.timespinner.ampmToggle('" + clientId + "');", null);
        writeMinusIcon(writer);
        writer.endElement("button");
        writer.endElement("div");                                         //end button decrement
        writer.endElement("div");                                         //end of ampm select container

        writer.endElement("div");                                         //end of selection container
        writer.startElement("div", uiComponent);                          //button container for set or cancel
        writer.writeAttribute("class", "mobi-time-submit-container", null);
        writer.startElement("input", uiComponent);
        writer.writeAttribute("class", "mobi-button ui-btn-up-c", null);
        writer.writeAttribute("type", "button", "type");
        writer.writeAttribute("value", "Set", null);
        //prep for singleSubmit
        boolean singleSubmit = timeEntry.isSingleSubmit();
        StringBuilder builder = new StringBuilder(255);
        builder.append("mobi.timespinner.select('").append(clientId).append("',{ event: event,");
        builder.append("singleSubmit: ").append(singleSubmit);
        if (hasBehaviors) {
            String behaviors = this.encodeClientBehaviors(context, cbh, "change").toString();
            behaviors = behaviors.replace("\"", "\'");
            builder.append(behaviors);
        }
        builder.append("});");
        String jsCall = builder.toString();
        if (!timeEntry.isDisabled() || !timeEntry.isReadonly()) {
            writer.writeAttribute(CLICK_EVENT, jsCall, null);
        }
        writer.endElement("input");
        writer.startElement("input", uiComponent);
        writer.writeAttribute("class", "mobi-button ui-btn-up-c", null);
        writer.writeAttribute("type", "button", "type");
        writer.writeAttribute("value", "Cancel", null);
        writer.writeAttribute(CLICK_EVENT, "mobi.timespinner.close('" + clientId + "');", null);
        writer.endElement("input");
        writer.endElement("div");                                        //end of button container

        writer.endElement("div");                                         //end of entire container
    }

    public void encodeScript(FacesContext context, UIComponent uiComponent) throws IOException {
        //need to initialize the component on the page and can also
        ResponseWriter writer = context.getResponseWriter();
        TimeSpinner spinner = (TimeSpinner) uiComponent;
        String clientId = spinner.getClientId(context);
        //separate the value into yrInt, mthInt, dateInt for now just use contstants
        int hourInt = spinner.getHourInt();
        int minuteInt = spinner.getMinuteInt();
        int ampm = spinner.getAmpm();
        writer.startElement("span", uiComponent);
        writer.writeAttribute("id", clientId + "_script", "id");
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-hidden", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("mobi.timespinner.init('" + clientId + "'," + hourInt +
                "," + minuteInt + "," + ampm + ",'" + spinner.getPattern() + "');");
        writer.endElement("script");
        writer.endElement("span");
    }


    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object value) throws ConverterException {
        TimeSpinner spinner = (TimeSpinner) component;
        String submittedValue = String.valueOf(value);
        Object objVal;
        Converter converter = spinner.getConverter();

        //Delegate to user supplied converter if defined
        if (converter != null) {
            objVal = converter.getAsObject(context, spinner, submittedValue);
            return objVal;
        }

        try {
            Locale locale = spinner.calculateLocale(context);
            if (!shouldUseNative(spinner)) {
                SimpleDateFormat format = new SimpleDateFormat(spinner.getPattern(), locale);
                return customConversion(context, spinner, format, submittedValue);
            } else {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", locale);
                return customConversion(context, spinner, format, submittedValue);
            }
        } catch (ParseException e) {
            throw new ConverterException(e);
        }
    }

    private Object customConversion(FacesContext context, TimeSpinner spinner,
                                    SimpleDateFormat format, String submittedValue) throws ParseException {
        Locale locale = spinner.calculateLocale(context);
        format.setTimeZone(spinner.calculateTimeZone());
        Date nativeValue = format.parse(submittedValue);
        return nativeValue;
    }

    private void setIntValues(TimeSpinner spinner, Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        int hourInt = cal.get(Calendar.HOUR);
        if (0 == hourInt) {
            hourInt = 12;
        }
        spinner.setHourInt(hourInt);
        spinner.setMinuteInt(cal.get(Calendar.MINUTE));
        spinner.setAmpm(cal.get(Calendar.AM_PM));
    }

    private String convertStringInput(String patternIn, String patternOut, String inString) {
        SimpleDateFormat df1 = new SimpleDateFormat(patternIn);   //default date pattern
        SimpleDateFormat df2 = new SimpleDateFormat(patternOut);
        String returnString = inString;
        try {
            Date aDate = df1.parse(inString);
            returnString = df2.format(aDate);
        } catch (Exception e) {
            //means that it was already in the pattern format so just return the inString
            e.printStackTrace();
        }
        return returnString;
    }

    private String encodeValue(TimeSpinner spinner, String initialValue) {
        String value = "";
        Date aDate = new Date();
        SimpleDateFormat df2 = new SimpleDateFormat(spinner.getPattern());
        if (isValueBlank(initialValue)) {
            //nothing values already set as default
        } else {
            try {
                if (isFormattedDate(initialValue, spinner.getPattern())) {
                    value = initialValue;
                    aDate = df2.parse(value);
                } else if (isFormattedDate(initialValue, "EEE MMM dd hh:mm:ss zzz yyyy")) {
                    value = convertStringInput("EEE MMM dd hh:mm:ss zzz yyyy", spinner.getPattern(), initialValue); //converts to the patter the spinner is set for
                    aDate = df2.parse(value);
                }
            } catch (Exception e) {
                throw new ConverterException();
            }

        }
        this.setIntValues(spinner, aDate);
        return value;
    }

    private boolean isFormattedDate(String inStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(inStr, new ParsePosition(0)) != null;
    }

    private String convertToClock(String inputVal, int hours) {
        if (!isValueEmpty(inputVal)) {
            String delim = ":";
            String tmp[] = inputVal.split(delim);
            String hr = tmp[0];
            String min = tmp[1];
            if (min.length() == 1) {
                min += "0";
            }
            String retVal = null;
            try {
                int hour = Integer.parseInt(hr);
                int minute = Integer.parseInt(min);
                if (hours == 12) {
                    if (hour < 13 && minute <= 59) {
                        retVal = hr + ":" + min + " AM";
                    } else {
                        retVal = String.valueOf(hour - 12) + ":" + min + " PM";
                    }
                } else {
                    retVal = hr + ":" + min;
                }
            } catch (NumberFormatException nfe) {
                logger.info("not able to convert iOS5 input to " + hours + "hour clock");
            }
            return retVal;
        } else {
            return inputVal;
        }
    }

    private boolean shouldUseNative(TimeSpinner component) {
       return component.isUseNative() && Utils.getClientDescriptor().isHasNativeDatePicker();
    }

    private void writePlusIcon(ResponseWriter writer) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", "icon-plus", null);
        writer.endElement("span");
    }

    private void writeMinusIcon(ResponseWriter writer) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", "icon-minus", null);
        writer.endElement("span");
    }

}

