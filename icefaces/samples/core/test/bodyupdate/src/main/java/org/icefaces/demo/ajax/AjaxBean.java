
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

package org.icefaces.demo.ajax;

import java.io.Serializable;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;

@ManagedBean(name = "ajaxBean")
@ViewScoped
public class AjaxBean implements Serializable {

    private final static String SESSION_COUNTER = "org.icefaces.test.sessionCounter";

    int viewCount = 0;

    public void toggle(ActionEvent ae) {
        viewCount++;
    }

    public int getViewCount(){
        return viewCount;
    }

    public int getSessionCount(){
        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Object obj = sessionMap.get(SESSION_COUNTER);
        if( obj == null ){
            sessionMap.put(SESSION_COUNTER, new Integer(0));
        }
        return ((Integer)sessionMap.get(SESSION_COUNTER)).intValue();
    }

    private void incrementSessionCounter() {
        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Object obj = sessionMap.get(SESSION_COUNTER);
        if( obj == null ){
            sessionMap.put(SESSION_COUNTER, new Integer(1));
        } else {
            sessionMap.put(SESSION_COUNTER,new Integer(((Integer)obj).intValue() + 1));
        }
    }

    public String countAction(){
        System.out.println("AjaxBean.countAction");
        viewCount++;
        incrementSessionCounter();
        return "index";
    }

    public void countActionListener(ActionEvent event){
        System.out.println("AjaxBean.countActionListener");
        viewCount++;
        incrementSessionCounter();
    }

}