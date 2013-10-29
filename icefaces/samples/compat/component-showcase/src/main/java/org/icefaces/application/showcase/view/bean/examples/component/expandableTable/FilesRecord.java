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

package org.icefaces.application.showcase.view.bean.examples.component.expandableTable;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * <p>The <code>FilesRecord</code> class contains the base information for an
 * inventory entry in a data table.  This class is meant to represent a model
 * and should only contain base inventory data</p>
 * <p/>
 * <p>The class instance variables are a direct map from the original ascii
 * expandable table specification. </p>
 */
public class FilesRecord implements Serializable{

    // simple list of files records.
    protected String description = "";
    protected String modified = "";
    protected String created = "";
    protected String size = "";
    protected String kind = "";
    protected String version = "";
    protected GregorianCalendar date;
    protected int quantity;
    protected double price;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}