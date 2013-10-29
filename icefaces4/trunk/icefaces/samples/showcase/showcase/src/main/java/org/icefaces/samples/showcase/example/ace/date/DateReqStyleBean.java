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
import java.io.Serializable;
import java.util.Date;

@ComponentExample(
        parent = DateEntryBean.BEAN_NAME,
        title = "example.ace.dateentry.reqStyle.title",
        description = "example.ace.dateentry.reqStyle.description",
        example = "/resources/examples/ace/date/datereqstyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="datereqstyle.xhtml",
                    resource = "/resources/examples/ace/date/datereqstyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DateReqStyleBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/date/DateReqStyleBean.java")
        }
)
@ManagedBean(name= DateReqStyleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateReqStyleBean extends ComponentExampleImpl<DateReqStyleBean> implements Serializable {
    public static final String BEAN_NAME = "dateReqStyle";
    
    private Date selectedDate1 = new Date(System.currentTimeMillis());
    private Date selectedDate2 = new Date(System.currentTimeMillis());
    private String reqColor = "redRS";
    private String optColor = "greenRS";

    public DateReqStyleBean() {
        super(DateReqStyleBean.class);
    }
    
    public Date getSelectedDate1() {
        return selectedDate1;
    }
    
    public Date getSelectedDate2() {
        return selectedDate2;
    }
    
    public String getReqColor() {
        return reqColor;
    }
    
    public String getOptColor() {
        return optColor;
    }
    
    public void setSelectedDate1(Date selectedDate1) {
        this.selectedDate1 = selectedDate1;
    }
    
    public void setSelectedDate2(Date selectedDate2) {
        this.selectedDate2 = selectedDate2;
    }
    
    public void setReqColor(String reqColor) {
        this.reqColor = reqColor;
    }
    
    public void setOptColor(String optColor) {
        this.optColor = optColor;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
	
	private boolean useTheme = false;

    public boolean getUseTheme() {
        return useTheme;
    }

    public void setUseTheme(boolean useTheme) {
        this.useTheme = useTheme;
    }
}
