/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.model.colorEntry;
import java.io.Serializable;

public class ColorEntryLayout implements Serializable{

    private String part;
    private int column;
    private int row;
    private int colspan;
    private int rowspan;

    public ColorEntryLayout(String part, int col, int row, int colspan, int rowspan) {
        this.part = part;
        this.column = col;
        this.row = row;
        this.colspan = colspan;
        this.rowspan = rowspan;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }
    public String getEntry(){
        return(this.column+" ," +this.row+" ,"+this.colspan+" ,"+this.rowspan);
    }
}
