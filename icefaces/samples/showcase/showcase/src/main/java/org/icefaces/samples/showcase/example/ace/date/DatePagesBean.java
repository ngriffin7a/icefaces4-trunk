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

@ComponentExample(
        parent = DateEntryBean.BEAN_NAME,
        title = "example.ace.dateentry.pages.title",
        description = "example.ace.dateentry.pages.description",
        example = "/resources/examples/ace/date/datepages.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="datepages.xhtml",
                    resource = "/resources/examples/ace/date/datepages.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DatePagesBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/date/DatePagesBean.java")
        }
)
@ManagedBean(name= DatePagesBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DatePagesBean extends ComponentExampleImpl<DatePagesBean> implements Serializable {
    public static final String BEAN_NAME = "datePages";

    private Date selectedDate;
    private int pages;
    
    public DatePagesBean() {
        super(DatePagesBean.class);
        this.selectedDate = new Date(System.currentTimeMillis());
        this.pages = 3;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void dateSelectListener(DateSelectEvent event) {
        this.selectedDate = event.getDate();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
    
    public int getPages() {
        return pages;
    }
    
    public void setPages(int pages) {
        this.pages = pages;
    }
}
