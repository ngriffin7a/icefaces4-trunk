/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */

/*
 * Generated, Do Not Modify
 */

package org.icefaces.ace.component.datetimeentry;

import org.icefaces.ace.event.DateSelectEvent;
import org.icefaces.ace.event.DateTextChangeEvent;
import org.icefaces.ace.util.Constants;
import org.icefaces.ace.util.Utils;
import org.icefaces.component.Focusable;
import org.icefaces.impl.util.Util;

import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.*;

public class DateTimeEntry extends DateTimeEntryBase implements Focusable {
    public final static String INPUT_STYLE_CLASS = "ui-inputfield ui-widget ui-state-default ui-corner-all";

    public static String POPUP_ICON = "datetimeentry/calendar_icon.png";

    private Map<String, AjaxBehaviorEvent> customEvents = new HashMap<String, AjaxBehaviorEvent>();

    private java.util.Locale appropriateLocale;
    private java.util.TimeZone appropriateTimeZone;

    public java.util.Locale calculateLocale(FacesContext facesContext) {
        if (appropriateLocale == null) {
            Object userLocale = getLocale();
            if (userLocale != null) {
                if (userLocale instanceof String) {
                    String[] tokens = ((String) userLocale).split("_");
                    if (tokens.length == 1)
                        appropriateLocale = new java.util.Locale(tokens[0], "");
                    else
                        appropriateLocale = new java.util.Locale(tokens[0], tokens[1]);
                } else if (userLocale instanceof java.util.Locale)
                    appropriateLocale = (java.util.Locale) userLocale;
                else
                    throw new IllegalArgumentException("Type:" + userLocale.getClass() + " is not a valid locale type for calendar:" + this.getClientId(facesContext));
            } else {
                appropriateLocale = facesContext.getViewRoot().getLocale();
            }
        }

        return appropriateLocale;
    }

    public java.util.TimeZone calculateTimeZone() {
        if (appropriateTimeZone == null) {
            Object usertimeZone = getTimeZone();
            if (usertimeZone != null) {
                if (usertimeZone instanceof String)
                    appropriateTimeZone = java.util.TimeZone.getTimeZone((String) usertimeZone);
                else if (usertimeZone instanceof java.util.TimeZone)
                    appropriateTimeZone = (java.util.TimeZone) usertimeZone;
                else
                    throw new IllegalArgumentException("TimeZone could be either String or java.util.TimeZone");
            } else {
                appropriateTimeZone = java.util.TimeZone.getDefault();
            }
        }

        return appropriateTimeZone;
    }

    public boolean isPopup() {
        return isRenderAsPopup();
    }

    public boolean hasTime() {
        String pattern = getPattern();

        return (pattern != null && pattern.indexOf(":") != -1);
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        String eventName = context.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            customEvents.put(eventName, (AjaxBehaviorEvent) event);
        } else {
            super.queueEvent(event);
        }
    }

    @Override
    public void validate(FacesContext context) {
        super.validate(context);

        if (isValid()) {
            String eventName, submittedValue;
            AjaxBehaviorEvent behaviorEvent, newEvent = null;
            for (Iterator<String> customEventIter = customEvents.keySet().iterator(); customEventIter.hasNext(); ) {
                eventName = customEventIter.next();
                behaviorEvent = customEvents.get(eventName);
                if (eventName.equals("dateSelect")) {
                    newEvent = new DateSelectEvent(this, behaviorEvent.getBehavior(), (Date) getValue());
                } else if (eventName.equals("dateTextChange")) {
                    submittedValue = context.getExternalContext().getRequestParameterMap().get(getClientId(context) + "_input");
                    newEvent = new DateTextChangeEvent(this, behaviorEvent.getBehavior(), submittedValue, (Date) getValue());
                }
                if (newEvent != null && behaviorEvent.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES)) {
                    newEvent.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
                }

                super.queueEvent(newEvent);
            }
        }
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public boolean isSingleSubmit() {
        return Utils.superValueIfSet(this, getStateHelper(), PropertyKeys.singleSubmit.name(), super.isSingleSubmit(), Util.withinSingleSubmit(this));
    }

    public String getFocusedElementId() {
        return getClientId() + "_input";
    }
}
