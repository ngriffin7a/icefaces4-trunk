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

package org.icefaces.application.showcase.view.bean.examples.component.selectInputDate;

import org.icefaces.application.showcase.view.bean.BaseBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.GregorianCalendar;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * <p>The SelectInputDateBean Class is used to store the selected dates from the
 * selectinputdate components.</p>
 *
 * @since 0.3
 */
@ManagedBean(name = "dateSelect")
@ViewScoped
public class SelectInputDateBean extends BaseBean {
    /**
     * Variables to store the selected dates.
     */
    private Date date1=new Date();
    private Date date2=new Date();
    private Date date3=new Date();
    private Date date4=new Date();    
    private Date date5=new Date();
    private String pattern ="date";
    private List patterns = new ArrayList();
    // effect is fired when dat2 value is changed.  
    protected Effect valueChangeEffect2;

    public SelectInputDateBean() {
        super();
        valueChangeEffect2 = new Highlight("#fda505");
        valueChangeEffect2.setFired(true);
        date2 = new GregorianCalendar().getTime();
        patterns.add(new SelectItem("date", "MM/dd/yyyy"));
        patterns.add(new SelectItem("dateTime", "MMM/dd/yyyy HH:mm"));
        patterns.add(new SelectItem("dateTimeSs", "MMM/dd/yyyy HH:mm:ss"));
        patterns.add(new SelectItem("dateTimeAmPm", "MMM/dd/yyyy hh:mm a"));

        
    }

    /**
     * Gets the first selected date.
     *
     * @return the first selected date
     */
    public Date getDate1() {
        return date1;
    }

    /**
     * Sets the first selected date.
     *
     * @param date the first selected date
     */
    public void setDate1(Date date) {
        date1 = date;
    }

    /**
     * Gets the 2nd selected date.
     *
     * @return the 2nd selected date
     */
    public Date getDate2() {
        return date2;
    }

    /**
     * Sets the 2nd selected date.
     *
     * @param date the 2nd selected date
     */
    public void setDate2(Date date) {
        date2 = date;
    }

    /**
     * Gets the default timezone of the host server.  The timezone is needed
     * by the convertDateTime for formatting the time dat values.
     *
     * @return timezone for the current JVM
     */
    public TimeZone getTimeZone() {
        return java.util.TimeZone.getDefault();
    }

    public Effect getValueChangeEffect2() {
        return valueChangeEffect2;
    }

    public void setValueChangeEffect2(Effect valueChangeEffect2) {
        this.valueChangeEffect2 = valueChangeEffect2;
    }

    /**
     * When values change event occures on date2 then we reset the effect
     * so the user can see the changed value more easily.
     *
     * @param event JSF value change event. 
     */
    public void effect2ChangeListener(ValueChangeEvent event){
        valueChangeEffect2.setFired(false);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List getPatterns() {
        return patterns;
    }

    public void setPatterns(List patterns) {
        this.patterns = patterns;
    }

    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    public Date getDate4() {
        return date4;
    }

    public void setDate4(Date date4) {
        this.date4 = date4;
    }

    public Date getDate5() {
        return date5;
    }

    public void setDate5(Date date5) {
        this.date5 = date5;
    }
    
    
}