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

package org.icefaces.samples.showcase.example.ace.list;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ComponentExample(
    parent = ListBean.BEAN_NAME,
    title = "example.ace.list.selectionAjax.title",
    description = "example.ace.list.selectionAjax.description",
    example = "/resources/examples/ace/list/listSelectionAjax.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="ListSelectionAjax.xhtml",
                resource = "/resources/examples/ace/"+
                        "list/listSelectionAjax.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ListSelectionAjaxBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/"+
                        "showcase/example/ace/list/ListSelectionAjaxBean.java")
    }
)
@ManagedBean(name= ListSelectionAjaxBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListSelectionAjaxBean extends ComponentExampleImpl<ListSelectionAjaxBean> {
    public static final String BEAN_NAME = "listSelectionAjaxBean";
    
    private List<SelectItem> ajaxStringList = new ArrayList<SelectItem>() {{
        for (String s : DataTableData.CHASSIS_ALL) {
            add(new SelectItem(s));
        }
    }};
    private Set<Object> ajaxSelections;
    private boolean multiSelect = true;
    
    public ListSelectionAjaxBean() {
        super(ListSelectionAjaxBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<SelectItem> getAjaxStringList() {
        return ajaxStringList;
    }

    public void setAjaxStringList(List<SelectItem> ajaxStringList) {
        this.ajaxStringList = ajaxStringList;
    }

    public Set<Object> getAjaxSelections() {
        return ajaxSelections;
    }
    
    public List<Object> getAjaxSelectionList() {
        if (ajaxSelections != null) {
            return new ArrayList<Object>(ajaxSelections);
        }
        return null;
    }

    public void setAjaxSelections(Set<Object> ajaxSelections) {
        this.ajaxSelections = ajaxSelections;
    }
    
    public boolean getMultiSelect() {
        return multiSelect;
    }
    
    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }
}
