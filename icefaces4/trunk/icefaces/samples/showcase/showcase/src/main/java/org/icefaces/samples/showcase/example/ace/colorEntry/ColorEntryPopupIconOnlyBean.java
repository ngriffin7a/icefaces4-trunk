/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.samples.showcase.example.ace.colorEntry;

import org.icefaces.ace.model.colorEntry.ColorEntryLayout;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ManagedBean(name= ColorEntryPopupIconOnlyBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ColorEntryPopupIconOnlyBean implements Serializable
{
    public static final String BEAN_NAME = "colorEntryPopupIconOnlyBean";
	public String getBeanName() { return BEAN_NAME; }
    public List<TableData> table = new ArrayList<TableData>();
    // use custom layout for swatches in dataTable
    private List<String> parts = new ArrayList<String>(Arrays.asList("header", "map", "hex", "swatches", "footer"));
    private List<ColorEntryLayout> layout = new ArrayList<ColorEntryLayout>();

    public ColorEntryPopupIconOnlyBean() {
        this.table.add(new TableData(1, "Dept A"));
        this.table.add(new TableData(2,"Dept B"));
        this.table.add(new TableData(3, "Dept C"));
        this.table.add(new TableData(4, "Dept D"));
        this.table.add(new TableData(5,"Dept E"));
        this.table.add(new TableData(6,"Dept F"));
        this.table.add(new TableData(7, "Dept G"));
        this.table.add(new TableData(8, "Dept H"));
        layout.add(new ColorEntryLayout("hex", 0, 0, 2, 1));
        layout.add(new ColorEntryLayout("preview", 1, 0, 1, 1));
        layout.add(new ColorEntryLayout("swatches", 0, 2, 1, 4));
    }
    private String selectedColor;

    public String getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public List<TableData> getTable() {
        return table;
    }

    public void setTable(List<TableData> table) {
        this.table = table;
    }

    public List<String> getParts() {
        return parts;
    }

    public void setParts(List<String> parts) {
        this.parts = parts;
    }

    public List<ColorEntryLayout> getLayout() {
        return layout;
    }

    public void setLayout(List<ColorEntryLayout> layout) {
        this.layout = layout;
    }

    public class TableData implements Serializable{
        private int id;
        private String name, color;

        public TableData (int id, String name){
            this.id = id;
            this.name=name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
