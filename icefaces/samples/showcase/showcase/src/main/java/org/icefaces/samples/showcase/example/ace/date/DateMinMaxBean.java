/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.ace.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name= DateMinMaxBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateMinMaxBean implements Serializable {
    public static final String BEAN_NAME = "dateMinMax";
	public String getBeanName() { return BEAN_NAME; }
    
    private Date selectedDate = new Date(System.currentTimeMillis());
    private Date minDate = new Date(System.currentTimeMillis());
    private Date maxDate = new Date(System.currentTimeMillis());

    public DateMinMaxBean() {
        // Set the default minimum date to 1 Year ago
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(minDate);
        calendar.add(Calendar.YEAR, -1);
        minDate = calendar.getTime();
        
        // Set the default maximum date to 5 Years from now
        calendar = Calendar.getInstance();
        calendar.setTime(maxDate);
        calendar.add(Calendar.YEAR, 5);
        maxDate = calendar.getTime();
        
        // Set our selected date to almost 1 Year ago
        // This is done to help show how invalid dates are non-selectable
        calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, -1);
        selectedDate = calendar.getTime();
    }
    
    public Date getSelectedDate() {
        return selectedDate;
    }
    
    public Date getMinDate() {
        return minDate;
    }
    
    public Date getMaxDate() {
        return maxDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
    
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }
    
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }
    
    public void submitMinMax(ActionEvent event) {
        if ((minDate != null) && (selectedDate != null)) {
            if (selectedDate.before(minDate)) {
                selectedDate = new Date(minDate.getTime());
            }
        }
        if ((maxDate != null) && (selectedDate != null)) {
            if (selectedDate.after(maxDate)) {
                selectedDate = new Date(maxDate.getTime());
            }
        }
    }
}
