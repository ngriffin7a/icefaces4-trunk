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

package org.icefaces.samples.showcase.example.ace.dialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.dialog.title",
        description = "example.ace.dialog.description",
        example = "/resources/examples/ace/dialog/dialog.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dialog.xhtml",
                    resource = "/resources/examples/ace/dialog/dialog.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="Dialog.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dialog/DialogBean.java")
        }
)
@Menu(
	title = "menu.ace.dialog.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.dialog.subMenu.main",
	                isDefault = true,
                    exampleBeanName = DialogBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dialog.subMenu.effectsAndSize",
                    exampleBeanName = DialogEffectsAndSizeBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.dialog.subMenu.modalDialog",
                    exampleBeanName = ModalDialogBean.BEAN_NAME)
    }
)
@ManagedBean(name= DialogBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DialogBean extends ComponentExampleImpl<DialogBean> implements Serializable 
{
    public static final String BEAN_NAME = "dialogBean";
    private String firstName;
    private String lastName;
    
    
    public DialogBean() 
    {
        super(DialogBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
