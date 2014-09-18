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

package org.icefaces.samples.showcase.example.ace.date;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.ace.event.DateSelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

@ComponentExample(
        parent = DateEntryBean.BEAN_NAME,
        title = "example.ace.dateentry.minmax.title",
        description = "example.ace.dateentry.minmax.description",
        example = "/resources/examples/ace/date/dateminmax.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dateminmax.xhtml",
                    resource = "/resources/examples/ace/date/dateminmax.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DateMinMaxBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/date/DateMinMaxBean.java")
        }
)
@ManagedBean(name= DateMinMaxBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateMinMaxBean extends ComponentExampleImpl<DateMinMaxBean> implements Serializable {
    public static final String BEAN_NAME = "dateMinMax";
    
    private Date selectedDate = new Date(System.currentTimeMillis());
    private Date minDate = new Date(System.currentTimeMillis());
    private Date maxDate = new Date(System.currentTimeMillis());

    public DateMinMaxBean() {
        super(DateMinMaxBean.class);
        
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
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
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
