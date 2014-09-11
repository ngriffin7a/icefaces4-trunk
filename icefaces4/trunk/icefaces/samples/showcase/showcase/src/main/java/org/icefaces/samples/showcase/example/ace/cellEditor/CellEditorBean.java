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

package org.icefaces.samples.showcase.example.ace.cellEditor;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.event.ActionEvent;

@ComponentExample(
        title = "example.ace.cellEditor.title",
        description = "example.ace.cellEditor.description",
        example = "/resources/examples/ace/cellEditor/cellEditor.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="cellEditor.xhtml",
                    resource = "/resources/examples/ace/cellEditor/cellEditor.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CellEditorBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/cellEditor/CellEditorBean.java")
        }
)
@Menu(
    title = "menu.ace.cellEditor.subMenu.title", 
    menuLinks = {
        @MenuLink(title = "menu.ace.cellEditor.subMenu.main", isDefault = true, exampleBeanName = CellEditorBean.BEAN_NAME)
    }
)

@ManagedBean(name = CellEditorBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CellEditorBean extends ComponentExampleImpl<CellEditorBean> implements Serializable {
    public static final String BEAN_NAME = "sellEditorBean";
    
    private Format formatter;
    private String message;
    private List<String> list;
    public final String DEFAULT_MESSAGE = "please click on a button and select any menu item without icon";
    public final int MAX_LIST_SIZE = 5;
    
    /////////////---- CONSTRUCTORS BEGIN
    public CellEditorBean() {
        super(CellEditorBean.class);
        formatter = new SimpleDateFormat("HH:mm:ss");
        list = new ArrayList<String>(MAX_LIST_SIZE);
        list.add(DEFAULT_MESSAGE);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
        setGroup(4);
    }

    /////////////---- ACTION LISTENERS BEGIN
    public void fireAction(ActionEvent event) 
    {
        String [] results = event.getComponent().getParent().getClientId().split(":");
        message= results[results.length-1].toUpperCase() + " > ";
        results = event.getComponent().getClientId().split(":");
        message += results[results.length-1].toUpperCase();
        message += " - selected @ "+formatter.format(new Date()) + " (server time)";
        
        if(list.get(0).equals(DEFAULT_MESSAGE)) {
            list.clear(); 
        }
        if (list.size()<MAX_LIST_SIZE) {
            list.add(message);
        }
        else {
            list.clear();
            list.add(message);
        }
    }
    /////////////---- GETTERS & SETTERS BEGIN
    public List<String> getList() { return list; }
    public void setList(List<String> list) { this.list = list; }
}
