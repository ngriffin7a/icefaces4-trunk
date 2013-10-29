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


import org.icefaces.ace.util.Attribute;
import org.icefaces.ace.util.Utils;

import javax.faces.context.FacesContext;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

public class DateSpinner extends DateSpinnerBase {

    private static Logger logger = Logger.getLogger(DateSpinner.class.getName());

    public static final String BLACKOUT_PNL_CLASS = "mobi-date-bg";
    public static final String BLACKOUT_PNL_INVISIBLE_CLASS = "mobi-date-bg-inv";
    public static final String CONTAINER_CLASS = "mobi-date-container ui-widget ui-widget-content";
    public static final String CONTAINER_INVISIBLE_CLASS = "mobi-date-container-inv ui-widget ui-widget-content";
    public static final String INPUT_CLASS = "mobi-input-text";
    public static final String POP_UP_CLASS = "mobi-date-popup-btn";
    public static final String TITLE_CLASS = "mobi-date-title-container ui-widget-content";
    public static final String SELECT_CONT_CLASS = "mobi-date-select-container ui-widget-content";
    public static final String VALUE_CONT_CLASS = "mobi-date-select-value-cont";
    public static final String BUTTON_INC_CONT_CLASS = "mobi-date-btn-cont-incr";
    public static final String BUTTON_INC_CLASS = "mobi-date-btn mobi-date-btn-incr";
    public static final String SEL_VALUE_CLASS = "mobi-date-select-value";
    public static final String BUTTON_DEC_CONT_CLASS = "mobi-date-btn-cont-decr";
    public static final String BUTTON_DEC_CLASS = "mobi-date-btn mobi-date-btn-decr";

    private Attribute[] commonAttributeNames = {
            new Attribute("size", null)
    };

    private Locale appropriateLocale;
    private TimeZone appropriateTimeZone;

    private int monthInt;
    private int yearInt;
    private int dayInt;

    private boolean touchEnabled;

    public DateSpinner() {
        super();
    }

    public Locale calculateLocale(FacesContext facesContext) {
        if (appropriateLocale == null) {
            Object userLocale = getLocale();
            if (userLocale != null) {
                if (userLocale instanceof String) {
                    String[] tokens = ((String) userLocale).split("_");
                    if (tokens.length == 1) {
                        appropriateLocale = new Locale(tokens[0], "");
                    } else {
                        appropriateLocale = new Locale(tokens[0], tokens[1]);
                    }
                } else if (userLocale instanceof Locale) {
                    appropriateLocale = (Locale) userLocale;
                } else {
                    throw new IllegalArgumentException("Type:" +
                            userLocale.getClass() + " is not a valid locale type for calendar:" +
                            this.getClientId(facesContext));
                }
            } else {
                appropriateLocale = facesContext.getViewRoot().getLocale();
            }
        }

        return appropriateLocale;
    }

    public TimeZone calculateTimeZone() {
        if (appropriateTimeZone == null) {
            //default to GMT
            Object usertimeZone = TimeZone.getDefault(); //TimeZone.getTimeZone("GMT");
            if (usertimeZone != null) {
                if (usertimeZone instanceof String)
                    appropriateTimeZone = TimeZone.getTimeZone((String) usertimeZone);
                else if (usertimeZone instanceof TimeZone)
                    appropriateTimeZone = (TimeZone) usertimeZone;
                else
                    throw new IllegalArgumentException(
                            "TimeZone could be either String or java.util.TimeZone");
            } else {
                appropriateTimeZone = TimeZone.getDefault();
            }
        }

        return appropriateTimeZone;
    }

    public int getMonthInt() {
        return monthInt;
    }

    public void setMonthInt(int monthInt) {
        this.monthInt = monthInt;
    }

    public int getYearInt() {
        return yearInt;
    }

    public void setYearInt(int yearInt) {
        this.yearInt = yearInt;
    }

    public int getDayInt() {
        return dayInt;
    }

    public void setDayInt(int dateInt) {
        this.dayInt = dateInt;
    }

    public Attribute[] getCommonAttributeNames() {
        return commonAttributeNames;
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public String getDefaultEventName(FacesContext facesContext) {
        if (Utils.isTouchEventEnabled(facesContext)) {
            return "onblur";
        } else return "onchange";
    }

    public boolean isTouchEnabled() {
        return touchEnabled;
    }

    public void setTouchEnabled(boolean touchEnabled) {
        this.touchEnabled = touchEnabled;
    }
}