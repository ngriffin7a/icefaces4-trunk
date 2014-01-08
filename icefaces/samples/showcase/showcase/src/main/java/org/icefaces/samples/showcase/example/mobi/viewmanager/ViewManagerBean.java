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

package org.icefaces.samples.showcase.example.mobi.viewmanager;

/**
 *
 */

import java.io.Serializable;
import java.util.Stack;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = ViewManagerBean.BEAN_NAME)
@SessionScoped
public class ViewManagerBean implements Serializable {

    public static final String BEAN_NAME = "viewManagerBean";

    private static final Logger logger =
            Logger.getLogger(ViewManagerBean.class.toString());
            
    private String transitionType = "horizontal";
    private String headerStyle;
    private String view = null;
    private Stack<String> history;

    public ViewManagerBean() {
        history = new Stack<String>();
    }

    public String getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(String transitionType) {
        this.transitionType = transitionType;
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getView() {
        System.out.println("ViewManagerBean.getView()="+view);
        return view;
    }

    public void setView(String view) {
        System.out.println("ViewManagerBean.setView()="+view);
        this.view = view;
    }

    public Stack<String> getHistory() {
        return history;
    }

    public void setHistory(Stack<String> history) {
        this.history = history;
    }

 }

