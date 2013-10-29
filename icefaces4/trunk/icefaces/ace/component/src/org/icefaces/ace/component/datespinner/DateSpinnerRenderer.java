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

package org.icefaces.ace.component.datespinner;

import org.icefaces.ace.renderkit.InputRenderer;
import org.icefaces.ace.util.PassThruAttributeWriter;
import org.icefaces.ace.util.Utils;
import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateSpinnerRenderer extends InputRenderer {

    private static final Logger logger = Logger.getLogger(DateSpinnerRenderer.class.getName());

    private static final String JS_NAME = "datespinner.js";
    private static final String JS_MIN_NAME = "datespinner-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.datespinner";

    public static final String TOUCH_START_EVENT = "ontouchstart";
    public static final String CLICK_EVENT = "onclick";


    @Override
    public void decode(FacesContext context, UIComponent component) {

        DateSpinner dateSpinner = (DateSpinner) component;
        String clientId = dateSpinner.getClientId(context);
        if (dateSpinner.isDisabled()) {
            return;
        }
        String inputField = clientId + "_input";
        if (shouldUseNative(dateSpinner)) {
            inputField = clientId;
        }
        String inputValue = context.getExternalContext().getRequestParameterMap().get(inputField);
        String hiddenValue = context.getExternalContext().getRequestParameterMap().get(clientId + "_hidden");
        boolean inputNull = isValueBlank(inputValue);
        boolean hiddenNull = isValueBlank(hiddenValue);
        if (!inputNull || (inputNull && hiddenNull)) {
            dateSpinner.setSubmittedValue(inputValue);
        } else if (hiddenNull) {
            dateSpinner.setSubmittedValue(hiddenValue);
        }
        decodeBehaviors(context, dateSpinner);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws
            IOException {
        DateSpinner spinner = (DateSpinner) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = spinner.getClientId(context);
        ClientBehaviorHolder cbh = (ClientBehaviorHolder) component;
        boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();
        boolean singleSubmit = spinner.isSingleSubmit();
        spinner.setTouchEnabled(Utils.isTouchEventEnabled(context));
        String initialValue = ComponentUtils.getStringValueToRender(context, component);
        // detect if an iOS device
        if (shouldUseNative(spinner)) {
            writer.startElement("input", component);
            writer.writeAttribute("type", "date", "type");
            writer.writeAttribute("id", clientId, null);
            writer.writeAttribute("name", clientId, null);
            if (spinner.getStyle()!=null){
                writer.writeAttribute(HTML.STYLE_ATTR, spinner.getStyle(), HTML.STYLE_ATTR);
            }
            boolean disabled = spinner.isDisabled();
            boolean readonly = spinner.isReadonly();

            if (isValueBlank(initialValue)) {
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                Date aDate = new Date();
                writer.writeAttribute("value", df2.format(aDate), "value");

            } else {
                writer.writeAttribute("value", initialValue, "value");
            }
            if (disabled) {
                writer.writeAttribute("disabled", component, "disabled");
            }
            if (readonly) {
                writer.writeAttribute("readonly", component, "readonly");
            }
            boolean noJs = false;
            if (readonly || disabled){
                noJs = true;
            }
            if (!noJs && hasBehaviors) {
                String event = spinner.getDefaultEventName(context);
                String cbhCall = this.buildAjaxRequest(context, cbh, event);
                writer.writeAttribute("onblur", cbhCall, null);
            } else if (!noJs && singleSubmit) {
                writer.writeAttribute("onblur", "ice.se(event, this);", null);
            }
            writer.endElement("input");
        } else {
            writeJavascriptFile(context, component, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
            String value = encodeValue(spinner, initialValue);
            encodeMarkup(context, component, value, hasBehaviors);
            encodeScript(context, component);
        }
    }

    protected void encodeMarkup(FacesContext context, UIComponent uiComponent,
                                String value, boolean hasBehaviors)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        DateSpinner dateSpinner = (DateSpinner) uiComponent;
        String clientId = dateSpinner.getClientId(context);
        ClientBehaviorHolder cbh = (ClientBehaviorHolder) uiComponent;

        // check for a touch enable device and setup events accordingly
        String eventStr = dateSpinner.isTouchEnabled() ?
                TOUCH_START_EVENT : CLICK_EVENT;
        //prep for ajax submit
        boolean singleSubmit = dateSpinner.isSingleSubmit();
        StringBuilder builder = new StringBuilder(255);
        StringBuilder builder2 = new StringBuilder(255);
        String inputCallStart = "mobi.datespinner.inputSubmit('";
        String jsCallStart = "mobi.datespinner.select('";
        builder2.append(clientId).append("',{ event: event,");
        builder2.append("singleSubmit: ").append(singleSubmit);
        if (hasBehaviors) {
            String behaviors = this.encodeClientBehaviors(context, cbh, "change").toString();
            behaviors = behaviors.replace("\"", "\'");
            builder2.append(behaviors);
        }
        builder2.append("});");
        boolean disabledOrReadonly = false;
        boolean disabled = dateSpinner.isDisabled();
        boolean readonly = dateSpinner.isReadonly();
        if (readonly || disabled){
            disabledOrReadonly = true;
        }
        builder.append(jsCallStart).append(builder2);
        StringBuilder inputCall = new StringBuilder(inputCallStart).append(builder2);
        String jsCall = builder.toString();
        //first do the input field and the button
        // build out first input field
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        if (dateSpinner.getStyle() !=null){
            writer.writeAttribute(HTML.STYLE_ATTR, dateSpinner.getStyle(), HTML.STYLE_ATTR);
        }
        writer.startElement("span", uiComponent);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("name", clientId, "name");
        writer.writeAttribute("class", "mobi-date-wrapper", "class");
        writer.startElement("input", uiComponent);
        writer.writeAttribute("id", clientId + "_input", "id");
        writer.writeAttribute("name", clientId + "_input", "name");
        if (!disabledOrReadonly){
            writer.writeAttribute("onblur", inputCall.toString(), null);
        }
        // apply class attribute and pass though attributes for style.
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent,
                dateSpinner.getCommonAttributeNames());
        StringBuilder classNames = new StringBuilder(DateSpinner.INPUT_CLASS);
        Object checker = dateSpinner.getStyleClass();
        if (null != checker) {
            classNames.append(" ").append(dateSpinner.getStyleClass());
        }
        writer.writeAttribute(HTML.CLASS_ATTR, classNames.toString(), HTML.CLASS_ATTR);
        if (value != null) {
            writer.writeAttribute("value", value, null);
        }
        writer.writeAttribute("type", "text", "type");
        if (readonly) {
            writer.writeAttribute("readonly", "readonly", null);
        }
        if (disabled) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        writer.endElement("input");
        //hidden field to store state of current instance
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", clientId + "_hidden", "id");
        writer.writeAttribute("name", clientId + "_hidden", "name");
        writer.endElement("input");
        writer.endElement("span");
        // build out command button for show/hide of date select popup.
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "button", "type");
        writer.writeAttribute("value", "", null);
        writer.writeAttribute("class", DateSpinner.POP_UP_CLASS, null);
        if (dateSpinner.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        if (!disabledOrReadonly){
            // touch event can be problematic sometime not actualy getting called
            // for ui widgets that don't require rapid response then stick with onClick
            writer.writeAttribute(CLICK_EVENT, "mobi.datespinner.toggle('" + clientId + "');", null);
        }
        writer.endElement("input");
        writer.endElement("span"); //end of first span
        // dive that is use to hide/show the popup screen black out, invisible by default.
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", clientId + "_bg", "id");
        writer.writeAttribute("class", "mobi-date-bg-inv", "class");
        writer.endElement("div");

        // actual popup code.
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", clientId + "_popup", "id");
        writer.writeAttribute("class", DateSpinner.CONTAINER_INVISIBLE_CLASS, null);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", clientId + "_title", "id");
        writer.writeAttribute("class", DateSpinner.TITLE_CLASS, null);

        writer.startElement("span", uiComponent);
        writer.writeAttribute("class", "icon-time", null);
        writer.endElement("span");

        writer.startElement("span", uiComponent);
        writer.writeAttribute("class", "mobi-date-title", null);
        writer.endElement("span");

        writer.endElement("div");
        writer.startElement("div", uiComponent);                            //entire selection container
        writer.writeAttribute("class", DateSpinner.SELECT_CONT_CLASS, null);

        // look at pattern or converter pattern to decide as two order of input values.
        String pattern = findPattern(dateSpinner);
        // if we have patter to work off then find out the best way to proceed. 
        if (pattern != null) {
            // use the index to decide the ordering type. 
            int yStart = pattern.toLowerCase().indexOf("y");
            int mStart = pattern.toLowerCase().indexOf("m");
            int dStart = pattern.toLowerCase().indexOf("d");

            // yyy MM dd
            if (yStart < mStart && mStart < dStart) {
                renderYearInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderMonthInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderDayInput(writer, uiComponent, dateSpinner, clientId, eventStr);
            } // yyyy/dd/MM
            else if (yStart < dStart && dStart < mStart) {
                renderYearInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderDayInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderMonthInput(writer, uiComponent, dateSpinner, clientId, eventStr);
            } // dd/MM/yyyy
            else if (dStart < mStart && mStart < yStart) {
                renderDayInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderMonthInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderYearInput(writer, uiComponent, dateSpinner, clientId, eventStr);
            } // MM/dd/yyyy
            else if (mStart < dStart && dStart < yStart) {
                renderMonthInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderDayInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderYearInput(writer, uiComponent, dateSpinner, clientId, eventStr);
            }  // default yyyy MM dd
            else {
                renderYearInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderMonthInput(writer, uiComponent, dateSpinner, clientId, eventStr);
                renderDayInput(writer, uiComponent, dateSpinner, clientId, eventStr);
            }
        }
        // default yyyy MM dd
        else {
            renderYearInput(writer, uiComponent, dateSpinner, clientId, eventStr);
            renderMonthInput(writer, uiComponent, dateSpinner, clientId, eventStr);
            renderDayInput(writer, uiComponent, dateSpinner, clientId, eventStr);
        }

        writer.endElement("div");                                         //end of selection container

        writer.startElement("div", uiComponent);                          //button container for set or cancel
        writer.writeAttribute("class", "mobi-date-submit-container", null);
        writer.startElement("input", uiComponent);
        writer.writeAttribute("class", "mobi-button ui-btn-up-c", null);
        writer.writeAttribute("type", "button", "type");
        writer.writeAttribute("value", "Set", null);
        if (!dateSpinner.isDisabled() && !dateSpinner.isReadonly()) {
            writer.writeAttribute(CLICK_EVENT, jsCall, null);
        }
        writer.endElement("input");

        writer.startElement("input", uiComponent);
        writer.writeAttribute("class", "mobi-button ui-btn-up-c", null);
        writer.writeAttribute("type", "button", "type");
        writer.writeAttribute("value", "Cancel", null);
        writer.writeAttribute(CLICK_EVENT, "mobi.datespinner.close('" + clientId + "');", null);
        writer.endElement("input");

        writer.endElement("div");                                        //end of button container

        writer.endElement("div");                                         //end of entire container
    }

    public void encodeScript(FacesContext context,
                             UIComponent uiComponent) throws IOException {
        //need to initialize the component on the page and can also
        ResponseWriter writer = context.getResponseWriter();
        DateSpinner spinner = (DateSpinner) uiComponent;
        String clientId = spinner.getClientId(context);
        //separate the value into yrInt, mthInt, dateInt for now just use contstants
        int yrInt = spinner.getYearInt();
        int mnthInt = spinner.getMonthInt();
        int dateInt = spinner.getDayInt();
        writer.startElement("span", uiComponent);
        writer.writeAttribute(HTML.CLASS_ATTR, "mobi-hidden", null);
        writer.writeAttribute("id", clientId + "_script", "id");
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("mobi.datespinner.init('" + clientId + "'," + yrInt + ","
                + mnthInt + "," + dateInt + ",'" + findPattern(spinner) + "');");
        writer.endElement("script");
        writer.endElement("span");
    }


    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object value) throws ConverterException {
        DateSpinner spinner = (DateSpinner) component;
        String submittedValue = String.valueOf(value);
        Object objVal;
        Converter converter = spinner.getConverter();

        //Delegate to user supplied converter if defined
        if (converter != null) {
            objVal = converter.getAsObject(context, spinner, submittedValue);
            return objVal;
        }

        try {
            Date convertedValue;
            Locale locale = spinner.calculateLocale(context);
            String pattern = findPattern(spinner);
            SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
            format.setTimeZone(spinner.calculateTimeZone());
            convertedValue = format.parse(submittedValue);
            return convertedValue;

        } catch (ParseException e) {
            throw new ConverterException(e);
        }
    }

    private void renderDayInput(ResponseWriter writer,
                                UIComponent uiComponent,
                                DateSpinner dateEntry,
                                String clientId,
                                String eventStr) throws IOException {
        writer.startElement("div", uiComponent);                             //date select container
        writer.writeAttribute("class", DateSpinner.VALUE_CONT_CLASS, null);
        writer.startElement("div", uiComponent);                            //button increment
        writer.writeAttribute("class", DateSpinner.BUTTON_INC_CONT_CLASS, null);

        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", DateSpinner.BUTTON_INC_CLASS, null);
        writer.writeAttribute("id", clientId + "_dUpBtn", null);
        writer.writeAttribute(eventStr, "mobi.datespinner.dUp('" + clientId + "');", null);
        writePlusIcon(writer);
        writer.endElement("button");

        writer.endElement("div");                                         //end button incr
        writer.startElement("div", uiComponent);                          //day value
        writer.writeAttribute("class", DateSpinner.SEL_VALUE_CLASS, null);
        writer.writeAttribute("id", clientId + "_dInt", null);
        writer.write(String.valueOf(dateEntry.getDayInt()));
        writer.endElement("div");                                         //end of day value
        writer.startElement("div", uiComponent);                          //button decrement
        writer.writeAttribute("class", DateSpinner.BUTTON_DEC_CONT_CLASS, null);

        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", DateSpinner.BUTTON_DEC_CLASS, null);
        writer.writeAttribute("id", clientId + "_dDnBtn", null);
        writer.writeAttribute(eventStr, "mobi.datespinner.dDn('" + clientId + "');", null);
        writeMinusIcon(writer);
        writer.endElement("button");

        writer.endElement("div");                                         //end button decrement
        writer.endElement("div");                                         //end of dateEntry select container
    }

    private void renderMonthInput(ResponseWriter writer,
                                  UIComponent uiComponent,
                                  DateSpinner dateEntry,
                                  String clientId,
                                  String eventStr) throws IOException {
        writer.startElement("div", uiComponent);                             //month select container
        writer.writeAttribute("class", DateSpinner.VALUE_CONT_CLASS, null);
        writer.startElement("div", uiComponent);                            //button increment
        writer.writeAttribute("class", DateSpinner.BUTTON_INC_CONT_CLASS, null);

        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", DateSpinner.BUTTON_INC_CLASS, null);
        writer.writeAttribute("id", clientId + "_mUpBtn", null);
        writer.writeAttribute(eventStr, "mobi.datespinner.mUp('" + clientId + "');", null);
        writePlusIcon(writer);
        writer.endElement("button");

        writer.endElement("div");                                         //end button incr
        writer.startElement("div", uiComponent);                          //month value
        writer.writeAttribute("class", DateSpinner.SEL_VALUE_CLASS, null);
        writer.writeAttribute("id", clientId + "_mInt", null);
        writer.write(String.valueOf(dateEntry.getMonthInt()));
        writer.endElement("div");                                         //end of month value
        writer.startElement("div", uiComponent);                          //button decrement
        writer.writeAttribute("class", DateSpinner.BUTTON_DEC_CONT_CLASS, null);

        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", DateSpinner.BUTTON_DEC_CLASS, null);
        writer.writeAttribute("id", clientId + "_mDnBtn", null);
        writer.writeAttribute(eventStr, "mobi.datespinner.mDn('" + clientId + "');", null);
        writeMinusIcon(writer);
        writer.endElement("button");

        writer.endElement("div");                                         //end button decrement
        writer.endElement("div");                                         //end of month select container
    }

    private void renderYearInput(ResponseWriter writer,
                                 UIComponent uiComponent,
                                 DateSpinner dateEntry,
                                 String clientId,
                                 String eventStr) throws IOException {

        int yMin = dateEntry.getYearStart();
        int yMax = dateEntry.getYearEnd();

        writer.startElement("div", uiComponent);                             //year select container
        writer.writeAttribute("class", DateSpinner.VALUE_CONT_CLASS, null);
        writer.startElement("div", uiComponent);                            //button increment
        writer.writeAttribute("class", DateSpinner.BUTTON_INC_CONT_CLASS, null);

        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", DateSpinner.BUTTON_INC_CLASS, null);
        writer.writeAttribute("id", clientId + "_yUpBtn", null);
        writer.writeAttribute(eventStr, "mobi.datespinner.yUp('" + clientId + "'," + yMin + "," + yMax + ");", null);
        writePlusIcon(writer);
        writer.endElement("button");

        writer.endElement("div");                                         //end button incr
        writer.startElement("div", uiComponent);                          //year value
        writer.writeAttribute("class", DateSpinner.SEL_VALUE_CLASS, null);
        writer.writeAttribute("id", clientId + "_yInt", null);
        writer.write(String.valueOf(dateEntry.getYearInt()));
        writer.endElement("div");                                         //end of year value
        writer.startElement("div", uiComponent);                          //button decrement
        writer.writeAttribute("class", DateSpinner.BUTTON_DEC_CONT_CLASS, null);

        writer.startElement("button", uiComponent);
        writer.writeAttribute("class", DateSpinner.BUTTON_DEC_CLASS, null);
        writer.writeAttribute("id", clientId + "_yDnBtn", null);
        writer.writeAttribute(eventStr, "mobi.datespinner.yDn('" + clientId + "'," + yMin + "," + yMax + ");", null);
        writeMinusIcon(writer);
        writer.endElement("button");

        writer.endElement("div");                                         //end button decrement
        writer.endElement("div");                                         //end of year select container
    }

    private void setIntValues(DateSpinner spinner, Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        spinner.setYearInt(cal.get(Calendar.YEAR));
        spinner.setMonthInt(cal.get(Calendar.MONTH) + 1);  //month is 0-indexed 0 = jan, 1=feb, etc
        spinner.setDayInt(cal.get(Calendar.DAY_OF_MONTH));
    }

    private String convertStringInput(String patternIn, String patternOut,
                                      String inString) {
        SimpleDateFormat df1 = new SimpleDateFormat(patternIn);   //default date pattern
        SimpleDateFormat df2 = new SimpleDateFormat(patternOut);
        String returnString = inString;
        try {
            Date aDate = df1.parse(inString);
            returnString = df2.format(aDate);
        } catch (Exception e) {
            //means that it was already in the pattern format so just return the inString
            logger.log(Level.WARNING, "Error converting string input.", e);
        }
        return returnString;
    }

    private String encodeValue(DateSpinner spinner, String initialValue) {
        String value = "";
        Date aDate = new Date();
        String pattern = findPattern(spinner);
        SimpleDateFormat df2 = new SimpleDateFormat(pattern);
        if (isValueBlank(initialValue)) {
            //nothing values already set as default
        } else {
            try {
                if (isFormattedDate(initialValue, pattern)) {
                    value = initialValue;
                    aDate = df2.parse(value);
                } else if (isFormattedDate(initialValue, "EEE MMM dd hh:mm:ss zzz yyyy")) {
                    value = convertStringInput("EEE MMM dd hh:mm:ss zzz yyyy", pattern, initialValue); //converts to the patter the spinner is set for
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

    /**
     * Utility to see if the date spinner will use the native input method for a
     * data input.  Current can be set by the attribute useNative and iOS
     * or blackberry
     *
     * @param component dateSpinner to test isUseNative.
     * @return ture if the native dialog should be used
     */
    private boolean shouldUseNative(DateSpinner component) {
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


    /**
     * Decides the default pattern for the component.  There are two ways to
     * set the pattern; first is on the pattern attribute and the second is
     * using a dateTimeConverter.  Patter works well enough but when combined with
     * a dateTimeConverter confusion can arise. If the pattern is different then
     * the converter then the converter wins trumping the pattern attribute.
     *
     * @param dateSpinner dateSpinner component to check pattern  value as well
     *                    as a child converter.
     * @return pattern string for dateSpinner, can be null in some cases.
     */
    private String findPattern(DateSpinner dateSpinner) {
        String pattern = dateSpinner.getPattern();
        Converter converter = dateSpinner.getConverter();
        // converter always wins over the pattern attribute
        if (converter != null && converter instanceof DateTimeConverter) {
            DateTimeConverter tmp = (DateTimeConverter) converter;
            pattern = tmp.getPattern();
        }
        return pattern;
    }

}

