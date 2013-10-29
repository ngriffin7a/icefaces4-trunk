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

package org.icefaces.ace.component.datatable;

import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 12/5/12
 * Time: 11:26 AM
 */
public class PageState {
    Integer rows;
    Integer page;

    public PageState() {}

    public PageState(DataTable table) {
        rows = table.getRows();
        page = table.getPage();
    }

    public PageState(FacesContext context, DataTable table) {
        String clientId = table.getClientId(context);
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();

        String rowsParam = params.get(clientId + "_rows");
        String pageParam = params.get(clientId + "_page");

        rows = Integer.valueOf(rowsParam);
        page = Integer.valueOf(pageParam);
    }

    public void apply(DataTable table) {
        table.setRows(rows);
        table.setPage(page);
        table.setFirst((table.getPage() - 1) * table.getRows());
    }
}
