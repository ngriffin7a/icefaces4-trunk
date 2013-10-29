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

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Date;

import org.icefaces.ace.event.DateSelectEvent;  
import org.icefaces.ace.event.DateTextChangeEvent;

@ComponentExample(
        parent = DateEntryBean.BEAN_NAME,
        title = "example.ace.dateentry.ajax.title",
        description = "example.ace.dateentry.ajax.description",
        example = "/resources/examples/ace/date/dateajax.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dateajax.xhtml",
                    resource = "/resources/examples/ace/date/dateajax.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DateAjaxBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/date/DateAjaxBean.java")
        }
)
@ManagedBean(name= DateAjaxBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateAjaxBean extends ComponentExampleImpl<DateAjaxBean> implements Serializable {
    public static final String BEAN_NAME = "dateAjax";
    
    private Date selectedDate = new Date(System.currentTimeMillis());
    private boolean popup;
    
    public DateAjaxBean() {
        super(DateAjaxBean.class);
        this.selectedDate = new Date(System.currentTimeMillis());
        this.popup = true;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void dateSelectListener(DateSelectEvent event) {
        this.selectedDate = event.getDate();
    }
    
    public void dateTextChangeListener(DateTextChangeEvent event){
    	this.selectedDate = event.getDate();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public boolean isPopup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
    }
}