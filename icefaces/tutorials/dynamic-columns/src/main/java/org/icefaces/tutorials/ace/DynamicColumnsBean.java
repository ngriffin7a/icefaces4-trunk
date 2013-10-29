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

package org.icefaces.tutorials.ace;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DynamicColumnsBean implements Serializable {
    List<Task> data = new ArrayList<Task>() {{
        int i = 0;
        while (i != 10)
            add(new Task(i, "Topic " + i, "Action " + i, "Notes " + i++));
    }};

    List<ColumnModel> columns = new ArrayList<ColumnModel>() {{
        add(new ColumnModel("id", "ID"));
        add(new ColumnModel("topic", "Topic"));
        add(new ColumnModel("action", "Action"));
        add(new ColumnModel("notes", "Notes"));
    }};

    List<SelectItem> checkboxes = new ArrayList<SelectItem>() {{
        add(new SelectItem("Id"));
        add(new SelectItem("Topic"));
        add(new SelectItem("Action"));
        add(new SelectItem("Notes"));
    }};
    
    List<String> selectedCheckboxes = new ArrayList<String>() {{
        add("Id");
        add("Topic");
        add("Action");
        add("Notes");
    }};

    public List<Task> getData() {
        return data;
    }

    public void setData(ArrayList<Task> data) {
        this.data = data;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<ColumnModel> columns) {
        this.columns = columns;
    }

    public List<SelectItem> getCheckboxes() {
        return checkboxes;
    }

    public void setCheckboxes(List<SelectItem> checkboxes) {
        this.checkboxes = checkboxes;
    }

    public List<String> getSelectedCheckboxes() {
        return selectedCheckboxes;
    }

    public void setSelectedCheckboxes(List<String> selectedCheckboxes) {
        this.selectedCheckboxes = selectedCheckboxes;
    }

    public void removeColumn(String name) {
        for (int i = 0; i < columns.size(); i++)
            if (columns.get(i).getValue().equals(name))
                columns.remove(i);
    }
    
    public void addColumn(String name) {
        columns.add(
            new ColumnModel(
                name,
                name.substring(0,1).toUpperCase() + name.substring(1)
            )
        );
    }
    
    public void checkboxChange(ValueChangeEvent event) {        
        List<String> oldVal = (List<String>) event.getNewValue();
        List<String> newVal = (List<String>) event.getOldValue();
        
        List<String> added = new ArrayList<String>(oldVal);
        added.removeAll(newVal);
        for (String s : added) {
            addColumn(s.toLowerCase());
        }

        List<String> removed = new ArrayList<String>(newVal);
        removed.removeAll(oldVal);
        for (String s : removed) {
            removeColumn(s.toLowerCase());
        }
    }
}
