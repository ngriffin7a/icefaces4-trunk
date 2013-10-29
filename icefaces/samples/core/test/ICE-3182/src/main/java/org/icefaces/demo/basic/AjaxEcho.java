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

package org.icefaces.demo.basic;

import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 */
@ManagedBean(name = "ajaxecho")
@ViewScoped
public class AjaxEcho {

    private String str;

    private static int count;

    private String filepath;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void reset() {
        this.str = "";
    }

    /**
     * Just a faux method to get post working. 
     */
    public void sendIt() {
    }

    public void toggleInclude() {

         ResponseWriter wr = FacesContext.getCurrentInstance().getResponseWriter();
        if (filepath == null) {
            filepath = "snippet.xhtml";
        } else {
            filepath = null;
        } 
    } 

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
