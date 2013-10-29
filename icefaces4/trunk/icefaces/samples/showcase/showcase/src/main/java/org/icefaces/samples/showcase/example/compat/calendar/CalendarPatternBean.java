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
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = CalendarBean.BEAN_NAME,
        title = "example.compat.calendar.pattern.title",
        description = "example.compat.calendar.pattern.description",
        example = "/resources/examples/compat/calendar/pattern.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="pattern.xhtml",
                    resource = "/resources/examples/compat/"+
                               "calendar/pattern.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CalendarPatternBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/calendar/CalendarPatternBean.java")
        }
)
@ManagedBean(name= CalendarPatternBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CalendarPatternBean extends ComponentExampleImpl<CalendarPatternBean> implements Serializable {
	
	public static final String BEAN_NAME = "calendarPattern";
	
	private static final String TYPE_PATTERN = "Pattern";
	private static final String TYPE_DATESTYLE = "Date Style";
	
	private Date date;
	private boolean reloadRequired;
	private String patternType;
	
	// Selected backings for the radio buttons
	private String optionPattern;
	private String optionDateStyle;
	
	// Available radio button options 
	private String[] patternTypes;
	private String[] patternOptions;
                private String[] styleOptions;
                
	public CalendarPatternBean() 
                {
                    super(CalendarPatternBean.class);
                    setDefaultValues();
                }
        
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

                private void setDefaultValues() {
                    
                    this.date = CalendarBean.getDefaultDate();
                    this.reloadRequired = false;
                    this.patternType = TYPE_PATTERN;
                    this.optionPattern = CalendarBean.DEFAULT_PATTERN;
                    this.optionDateStyle = "default";
                    // Available radio button options 
                    this.patternTypes = new String[] { TYPE_PATTERN, TYPE_DATESTYLE};
                    this.patternOptions = new String[] { "MM/dd/yyyy", "dd/MM/yyyy",
	                                                 "MMMM/dd/yy", "E, MMMM yyyy",
	                                                 "MM/dd/yy hh:mma", "kk:mm:ss:SS" };
                    this.styleOptions = new String[] { "default", "short", "medium", "long", "full" };
                }
        
                
	
	public Date getDate() { return date; }
	
	public String getOptionPattern() { return optionPattern; }
	public String getOptionDateStyle() { return optionDateStyle; }
	public String getPatternType() { return patternType; }
	
	public String[] getPatternTypes() { return patternTypes; }
	public String[] getPatternOptions() { return patternOptions; }
	public String[] getStyleOptions() { return styleOptions; }
	
	public boolean getTypePattern() { return TYPE_PATTERN.equals(patternType); }
	public boolean getTypeDateStyle() { return TYPE_DATESTYLE.equals(patternType); }
	
	public void setDate(Date date) { this.date = date; }
	public void setOptionPattern(String optionPattern) { this.optionPattern = optionPattern; }
	public void setOptionDateStyle(String optionDateStyle) { this.optionDateStyle = optionDateStyle; }
	public void setPatternType(String patternType) { this.patternType = patternType; }    
}
