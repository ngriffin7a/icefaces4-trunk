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

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = CalendarBean.BEAN_NAME,
        title = "example.compat.calendar.popup.title",
        description = "example.compat.calendar.popup.description",
        example = "/resources/examples/compat/calendar/popup.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="popup.xhtml",
                    resource = "/resources/examples/compat/"+
                               "calendar/popup.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CalendarPopupBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/calendar/CalendarPopupBean.java")
        }
)
@ManagedBean(name= CalendarPopupBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CalendarPopupBean extends ComponentExampleImpl<CalendarPopupBean> implements Serializable {
	
	public static final String BEAN_NAME = "calendarPopup";
	
	private Date date = CalendarBean.getDefaultDate();
	private boolean renderAsPopup = true;
	private boolean renderMonthAsDropdown = false;
	private boolean renderYearAsDropdown = false;

	public CalendarPopupBean() {
		super(CalendarPopupBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public Date getDate() { return date; }
	public String getPattern() { return CalendarBean.DEFAULT_PATTERN; }
	public boolean getRenderAsPopup() { return renderAsPopup; }
	public boolean getRenderMonthAsDropdown() { return renderMonthAsDropdown; }
	public boolean getRenderYearAsDropdown() { return renderYearAsDropdown; }
	
	public void setDate(Date date) { this.date = date; }
	public void setRenderAsPopup(boolean renderAsPopup) { this.renderAsPopup = renderAsPopup; }
	public void setRenderMonthAsDropdown(boolean renderMonthAsDropdown) { this.renderMonthAsDropdown = renderMonthAsDropdown; }
	public void setRenderYearAsDropdown(boolean renderYearAsDropdown) { this.renderYearAsDropdown = renderYearAsDropdown; }
}
