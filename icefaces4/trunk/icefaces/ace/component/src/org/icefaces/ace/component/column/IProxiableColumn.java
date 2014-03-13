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

package org.icefaces.ace.component.column;

import org.icefaces.ace.component.datatable.DataTable;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * To fully support ColumnGroup headers, there needs to be a connection between
 * each Column component in the body and its corresponding Column component
 * in the header ColumnGroup. Via a proxy, properties may be accessed and set,
 * so that they don't have to be redundantly specified on each. In the simplest
 * case, there is no header ColumnGroup, and the body column itself implements
 * this interface.
 */
public interface IProxiableColumn extends IColumn {
    public String getId();
    public boolean isRendered();
    public ValueExpression getValueExpression(String name);
    public void encodeAll(FacesContext context) throws IOException;

    public DataTable findParentTable();
    public boolean isOddGroup();
    public void setOddGroup(boolean oddGroup);
    public int getCurrGroupLength();
    public void setCurrGroupLength(int currGroupLength);
    public boolean hasCellEditor();
}
