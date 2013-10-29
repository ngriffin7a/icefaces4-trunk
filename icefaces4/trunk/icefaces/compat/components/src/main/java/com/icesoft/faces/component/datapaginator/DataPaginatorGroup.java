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

package com.icesoft.faces.component.datapaginator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import com.icesoft.util.CoreComponentUtils;

public class DataPaginatorGroup {
	public static void add(FacesContext facesContext, DataPaginator dataPaginator) {
        UIData uiData = dataPaginator.findUIData();
        if (uiData == null) return;
        dataPaginator.setUIData(uiData);
        if (!uiData.getAttributes().containsKey(DataPaginatorGroup.class.getName())) {
        	uiData.getAttributes().put(DataPaginatorGroup.class.getName(), new ArrayList());
        }
        List paginatorList = (List)uiData.getAttributes().get(DataPaginatorGroup.class.getName());
        String clientId = dataPaginator.getClientId(facesContext);
        if (!paginatorList.contains(clientId)) {
        	paginatorList.add(clientId);
        }
	}
	
	public static void execute(UIData uiData, Invoker invoker) {
        if (uiData== null || !uiData.getAttributes().containsKey(DataPaginatorGroup.class.getName())) return;
        List dataPaginatorClientIdList = (List) uiData.getAttributes().get(DataPaginatorGroup.class.getName()); 
        Iterator it = dataPaginatorClientIdList.iterator();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        while (it.hasNext()) {
	        UIComponent component = CoreComponentUtils.findComponent(UINamingContainer.getSeparatorChar(facesContext) + String.valueOf(it.next()), facesContext.getViewRoot());
	        if (component != null && component.isRendered() && 
	        		component instanceof DataPaginator) {
	            invoker.invoke((DataPaginator)component);
	        }		
        }
	}
 
	public interface Invoker {
		void invoke(DataPaginator dataPaginator);
	}
}
