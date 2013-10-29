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

package org.icefaces.samples.showcase.example.compat.calendar;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = CalendarBean.BEAN_NAME,
        title = "example.compat.calendar.highlight.title",
        description = "example.compat.calendar.highlight.description",
        example = "/resources/examples/compat/calendar/highlight.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="highlight.xhtml",
                    resource = "/resources/examples/compat/"+
                               "calendar/highlight.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CalendarHighlightBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/calendar/CalendarHighlightBean.java")
        }
)
@ManagedBean(name= CalendarHighlightBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CalendarHighlightBean extends ComponentExampleImpl<CalendarHighlightBean> implements Serializable {
	
	public static final String BEAN_NAME = "calendarHighlight";
	
	private Date date = CalendarBean.getDefaultDate();
	private SelectItem[] classItems = new SelectItem[] { new SelectItem("dateHighlightGray", "Gray and Bold"),
	                                                     new SelectItem("dateHighlightBig", "Big Font"),
	                                                     new SelectItem("dateHighlightRed", "Red Outline"),
	                                                     new SelectItem("dateHighlightItalic", "Left and Italic") };
	private SelectItem[] unitItems = new SelectItem[] { new SelectItem("DAY_OF_WEEK", "Set Day"),
	                                                    new SelectItem("WEEK_OF_MONTH", "By Week") };
	private SelectItem[] valueItems = new SelectItem[] { new SelectItem("1", "Just 1"),
	                                                     new SelectItem("2", "Just 2"),
                                                         new SelectItem("3", "Just 3"),
                                                         new SelectItem("1,4", "1 and 4") };
	private String highlightClass = classItems[0].getValue().toString();
	private String highlightUnit = unitItems[0].getValue().toString();
	private String highlightValue = valueItems[0].getValue().toString();

	public CalendarHighlightBean() {
		super(CalendarHighlightBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public Date getDate() { return date; }
	public String getPattern() { return CalendarBean.DEFAULT_PATTERN; }
	public String getHighlightClass() { return highlightClass; }
	public String getHighlightUnit() { return highlightUnit; }
	public String getHighlightValue() { return highlightValue; }
	public SelectItem[] getClassItems() { return classItems; }
	public SelectItem[] getUnitItems() { return unitItems; }
	public SelectItem[] getValueItems() { return valueItems; }
	
	public void setDate(Date date) { this.date = date; }
	public void setHighlightClass(String highlightClass) { this.highlightClass = highlightClass; }
	public void setHighlightUnit(String highlightUnit) { this.highlightUnit = highlightUnit; }
	public void setHighlightValue(String highlightValue) { this.highlightValue = highlightValue; }
}
