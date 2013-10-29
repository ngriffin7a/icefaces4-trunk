package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.ace.component.celleditor.CellEditor;
import org.icefaces.ace.event.DataTableCellClickEvent;
import org.icefaces.ace.model.table.RowState;
import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-06-18
 * Time: 3:18 PM
 */

@ComponentExample(
        parent = DataTableBean.BEAN_NAME,
        title = "example.ace.dataTable.click.title",
        description = "example.ace.dataTable.click.description",
        example = "/resources/examples/ace/dataTable/dataTableClick.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                                 title="dataTableClick.xhtml",
                                 resource = "/resources/examples/ace/dataTable/dataTableClick.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                                 title="DataTableClick.java",
                                 resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                         "/example/ace/dataTable/DataTableClick.java"),
                @ExampleResource(type = ResourceType.java,
                                title="DataTableBean.java",
                                resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                         "/example/ace/dataTable/DataTableBean.java")
        }
)
@ManagedBean(name= DataTableClick.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableClick extends ComponentExampleImpl<DataTableClick> implements Serializable {
    public static final String BEAN_NAME = "dataTableClick";
    public String stateVar = "state";

    public DataTableClick() {
        super(DataTableClick.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void toggleSelect(DataTableCellClickEvent event) {
        RowState state = (RowState) FacesContext.getCurrentInstance()
                                                .getExternalContext()
                                                .getRequestMap().get(stateVar);

        state.setSelected(!state.isSelected());
    }

    public void toggleEditor(DataTableCellClickEvent event) {
        RowState state = (RowState) FacesContext.getCurrentInstance()
                                                .getExternalContext()
                                                .getRequestMap().get(stateVar);

        CellEditor editor = event.getColumn().getCellEditor();

        if (editor != null) {
            if (state.getActiveCellEditorIds().contains(editor.getId())) {
                state.removeActiveCellEditor(editor);
            } else {
                state.addActiveCellEditor(editor);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(event.getColumn().getClientId(),
                new FacesMessage("This column doesn't contain an ace:cellEditor to toggle.")
            );
        }
    }

    public String getStateVar() {
        return stateVar;
    }

    public void setStateVar(String stateVar) {
        this.stateVar = stateVar;
    }
}
