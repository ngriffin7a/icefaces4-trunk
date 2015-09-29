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

package org.icefaces.samples.showcase.example.ace.menu;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name= MenuType.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuType implements Serializable {
    public static final String BEAN_NAME = "menuType";
	public String getBeanName() { return BEAN_NAME; }
    public final String DEFAULT_MESSAGE = "please select any menu item on the left";
    public final int MAX_LIST_SIZE = 10;
    
    private Format formatter;
    private String message;
    private ArrayList<String> list;
    
    public MenuType() {
        formatter = new SimpleDateFormat("HH:mm:ss");
        list = new ArrayList<String>(MAX_LIST_SIZE);
        list.add(DEFAULT_MESSAGE);
    }
    
    public void fireAction(ActionEvent event)
    {
        String [] results = event.getComponent().getParent().getClientId().split(":");
        message= results[results.length-1].toUpperCase() + " > ";
        
        results = event.getComponent().getClientId().split(":");
        message += results[results.length-1].toUpperCase();
        message += " - selected @ "+formatter.format(new Date()) + " (server time)";
        
        results = event.getComponent().getParent().getParent().getClientId().split(":");
        String prefix = results[results.length-1];
        message = prefix+" > "+message.replaceAll(prefix.toUpperCase(), "");
        
        if(list.get(0).equals(DEFAULT_MESSAGE)) {
            list.clear();
        }
        if (list.size()<MAX_LIST_SIZE){
            list.add(message);
        }
        else {
            list.clear();
            list.add(message);
        }
    }
    
    public ArrayList<String> getList() {return list;}
    public void setList(ArrayList<String> list) {this.list = list;}
}
