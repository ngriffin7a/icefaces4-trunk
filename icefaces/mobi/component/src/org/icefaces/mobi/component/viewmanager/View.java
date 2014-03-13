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
package org.icefaces.mobi.component.viewmanager;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;


public class View extends ViewBase {

    public List<View> getNavBarItemsToRender(){
        String navBarGroup = getNavBarGroup();
        List<View> views = new ArrayList<View>();
        if( navBarGroup != null ){
            List<UIComponent> children = getParent().getChildren();
            for( int i = 0 ; i < children.size() ; i++ ){
                View view = (View)children.get(i);
                if( navBarGroup.equals(view.getNavBarGroup())){
                    views.add(view);
                }
            }
        }
        return views;
    }
    
    public String getNavBarItemChildWidth(){
        float childWidth = 100f / getNavBarItemsToRender().size();
        return Float.toString(childWidth) + "%";
    }

    public void setDisabled(boolean disabled) {
        //disabled not supported        
    }

    public boolean isDisabled() {
        return false;
    }



}
