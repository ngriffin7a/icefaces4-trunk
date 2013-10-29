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

package org.icefaces.test.dyninc;

import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.logging.Logger;
import java.lang.annotation.Annotation;

@ManagedBean(name = "nav")
//@RequestScoped
//@ViewScoped
//@SessionScoped
@CustomScoped(value="#{window}")
public class NavBean implements Serializable {

    private static final long serialVersionUID = -5395176310517774113L;
    private static Logger log = Logger.getLogger(NavBean.class.getName());

    private static String PAGE01 = "page01.xhtml";
    private static String PAGE02 = "page02.xhtml";
    private static String PAGE03 = "page03.xhtml";

    private boolean page01Disabled = true;
    private boolean page02Disabled = false;
    private boolean page03Disabled = false;

    private String data = null;
    private String path = PAGE01;

    private static String scope = "[unknown]";
    static{
        Annotation[] anns = NavBean.class.getAnnotations();
        for (int index = 0; index < anns.length; index++) {
            Annotation ann = anns[index];
            if(ann instanceof RequestScoped ){
                scope = "Request";
            } else if(ann instanceof ViewScoped ){
                scope = "View";
            } else if(ann instanceof SessionScoped ){
                scope = "Session";
            } else if(ann instanceof CustomScoped ){
                scope = "Window";
            }
        }
    }

    public NavBean() {
        handlePage(PAGE01,"NavBean created");
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPath() {
        log.info("NavBean.getPath: " + path);
        return path;
    }

    public void setPath(String path) {
        log.info("NavBean.setPath: " + path);
        this.path = path;
    }

    public String getResponseWriter() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getResponseWriter().toString();
    }

    public String getScope() {
        return scope;
    }

    public boolean isPage01Disabled() {
        return page01Disabled;
    }

    public void setPage01Disabled(boolean page01Disabled) {
        this.page01Disabled = page01Disabled;
    }

    public boolean isPage02Disabled() {
        return page02Disabled;
    }

    public void setPage02Disabled(boolean page02Disabled) {
        this.page02Disabled = page02Disabled;
    }

    public boolean isPage03Disabled() {
        return page03Disabled;
    }

    public void setPage03Disabled(boolean page03Disabled) {
        this.page03Disabled = page03Disabled;
    }

    public String page01() {
        handlePage(PAGE01, "NavBean.page01 (action): ");
        return null;
    }

    public String page02() {
        handlePage(PAGE02, "NavBean.page02 (action): ");
        return null;
    }

    public String page03() {
        handlePage(PAGE03, "NavBean.page03 (action): ");
        return null;
    }

    public void page01(ActionEvent event) {
        handlePage(PAGE01, "NavBean.page01 (actionListener): ");
    }

    public void page02(ActionEvent event) {
        handlePage(PAGE02, "NavBean.page02 (actionListener): ");
    }

    public void page03(ActionEvent event) {
        handlePage(PAGE03, "NavBean.page03 (actionListener): ");
    }

    private void handlePage(String page, String message){
        path = page;
        log.info(message + path);
        page01Disabled = path.equalsIgnoreCase(PAGE01);
        page02Disabled = path.equalsIgnoreCase(PAGE02);
        page03Disabled = path.equalsIgnoreCase(PAGE03);
    }

}
