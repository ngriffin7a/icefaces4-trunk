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

package org.icefaces.samples.showcase.example.mobi.datespinner;


/*import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;*/

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;
/*
@Destination(
        title = "example.input.dateSpinner.destination.title.short",
        titleExt = "example.input.dateSpinner.destination.title.long",
        titleBack = "example.input.dateSpinner.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/date-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/date-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "date-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/date-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "DateBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/dateSpinner/DateBean.java")
        }
)*/

@ManagedBean(name = DateBean.BEAN_NAME)
@SessionScoped
public class DateBean /*extends ExampleImpl<DateBean>*/ implements
        Serializable {

    public static final String BEAN_NAME = "dateBean";
    private String timeZone = "America/Edmonton";
    // date input/echo
    private Date dateInput;
    // time input/echo
    private Date timeInput;
    
    private boolean useNative = true;
    private boolean readonly = false;

    public DateBean() {
       //super(DateBean.class);
       this.timeZone = java.util.TimeZone.getDefault().getID();
    }

    public Date getDateInput() {
        return dateInput;
    }

    public void setDateInput(Date dateInput) {
        this.dateInput = dateInput;
    }

    public Date getTimeInput() {
        return timeInput;
    }

    public void setTimeInput(Date timeInput) {
        this.timeInput = timeInput;
    }
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isUseNative() {
        return useNative;
    }

    public void setUseNative(boolean useNative) {
        this.useNative = useNative;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
}