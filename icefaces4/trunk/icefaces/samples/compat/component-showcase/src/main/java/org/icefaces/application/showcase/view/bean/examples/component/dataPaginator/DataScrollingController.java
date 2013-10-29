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

package org.icefaces.application.showcase.view.bean.examples.component.dataPaginator;

import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.util.CoreComponentUtils;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class DataScrollingController {
    // Used in this example to reset the paginator when moving between
    // scrolling views, not needed in normal application development. 
    private DataPaginator dataPaginatorBinding;


    public DataPaginator getDataPaginatorBinding() {
        return dataPaginatorBinding;
    }

    public void setDataPaginatorBinding(DataPaginator dataPaginatorBinding) {
        this.dataPaginatorBinding = dataPaginatorBinding;
    }

    public void dataModelChangeListener(ValueChangeEvent event){
        String oldPagingValue = (String)event.getOldValue();
        if (oldPagingValue.equals(DataScrollingModel.PAGINATOR_SCROLLING) &&
                dataPaginatorBinding != null){
            dataPaginatorBinding.gotoFirstPage();
        }
    }
}
