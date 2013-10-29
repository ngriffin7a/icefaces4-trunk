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

package org.icefaces.samples.showcase.example.compat.selector;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = SelectorBean.BEAN_NAME,
        title = "example.compat.selector.type.title",
        description = "example.compat.selector.type.description",
        example = "/resources/examples/compat/selector/selectorType.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="selectorType.xhtml",
                    resource = "/resources/examples/compat/"+
                               "selector/selectorType.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SelectorType.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/selector/SelectorType.java")
        }
)
@ManagedBean(name= SelectorType.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SelectorType extends ComponentExampleImpl<SelectorType> implements Serializable {
	
    public static final String BEAN_NAME = "selectorType";

    private static final String TYPE_SINGLE;
    private static final String TYPE_MULTIPLE;
    private static final String TYPE_ENHMULTIPLE;
    private List<SelectableCar> data;
    private SelectItem[] availableTypes;
    private String type;
    
    static
    {
        TYPE_SINGLE = "single";
        TYPE_MULTIPLE = "multiple";
        TYPE_ENHMULTIPLE = "enhmultiple";
    }

    public SelectorType() {
            super(SelectorType.class);
            initializeInstanceVariables();
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void typeChanged(ValueChangeEvent event) {
        SelectorBean dataBean =
            (SelectorBean)FacesUtils.getManagedBean(SelectorBean.BEAN_NAME);

    // De-select all the current rows if the type changes
    // This makes it easier to start clicking and see the new type in action
        for (SelectableCar currentItem : dataBean.getData()) {
            currentItem.setSelected(false);
        }
    }
    
    private void initializeInstanceVariables()
    {
        data = new ArrayList<SelectableCar>(DataTableData.getDefaultSelectableData());
        
        this.availableTypes = new SelectItem[] 
        {
            new SelectItem(TYPE_SINGLE, "Single Only"),
            new SelectItem(TYPE_MULTIPLE, "Multiple Allowed"),
            new SelectItem(TYPE_ENHMULTIPLE, "Enhanced Multiple")
        };
        
        this.type = TYPE_MULTIPLE;
    }
    
    public SelectItem[] getAvailableTypes() { return availableTypes; }
    public String getType() { return type; }
    public boolean isSingle() { return TYPE_SINGLE.equals(type); }
    public boolean isMultiple() { return TYPE_MULTIPLE.equals(type); }
    public boolean isEnhMultiple() { return TYPE_ENHMULTIPLE.equals(type); }
    public void setType(String type) { this.type = type; }
    public List<SelectableCar> getData() { return data; }
    public void setData(List<SelectableCar> data) { this.data = data; }
}
