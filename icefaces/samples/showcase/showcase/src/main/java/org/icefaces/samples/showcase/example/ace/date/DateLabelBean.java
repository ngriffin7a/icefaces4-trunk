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
import org.icefaces.samples.showcase.util.PositionBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.Date;

@ComponentExample(
        parent = DateEntryBean.BEAN_NAME,
        title = "example.ace.dateentry.label.title",
        description = "example.ace.dateentry.label.description",
        example = "/resources/examples/ace/date/datelabel.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "datelabel.xhtml",
                        resource = "/resources/examples/ace/date/datelabel.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "DateLabelBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/date/DateLabelBean.java")
        }
)
@ManagedBean(name = DateLabelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateLabelBean extends ComponentExampleImpl<DateLabelBean> implements Serializable {
    public static final String BEAN_NAME = "dateLabel";

    private Date selectedDate = new Date(System.currentTimeMillis());
    private String labelText = "Selected Date:";
    private String labelPosition = "left";

    public DateLabelBean() {
        super(DateLabelBean.class);
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public String getLabelText() {
        return labelText;
    }

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void positionChanged(ValueChangeEvent event) {
        // Reset our date if the user selected inField, so that we can see the label properly
        Object newValue = event.getNewValue();
        if (newValue != null && PositionBean.POS_INFIELD.equals(newValue.toString())) {
            setSelectedDate(null);
        }
    }
}
