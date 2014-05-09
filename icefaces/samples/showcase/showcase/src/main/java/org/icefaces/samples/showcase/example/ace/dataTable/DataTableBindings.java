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

package org.icefaces.samples.showcase.example.ace.dataTable;

import org.icefaces.ace.component.datatable.DataTable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@RequestScoped
// Created because MyFaces has id generation issues with non-request scoped
// component bindings.
public class DataTableBindings {
    // Need multiple tables to bind to, as binding to the same table
    // even if the bean is request scoped, causes id errors.
    // MyFaces component binding is very deficient.
    Map<Class, DataTable> tables = new HashMap<Class, DataTable>();

    public Map<Class, DataTable> getTables() {
        return tables;
    }

    public void setTables(Map<Class, DataTable> tables) {
        this.tables = tables;
    }

    public DataTable getTable(Class c) {
        return tables.get(c);
    }
}
