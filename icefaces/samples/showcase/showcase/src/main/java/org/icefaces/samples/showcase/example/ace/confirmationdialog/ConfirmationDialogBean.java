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

package org.icefaces.samples.showcase.example.ace.confirmationdialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.confirmationdialog.title",
        description = "example.ace.confirmationdialog.description",
        example = "/resources/examples/ace/confirmationdialog/confirmationDialog.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationDialog.xhtml",
                    resource = "/resources/examples/ace/confirmationdialog/confirmationDialog.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationDialog.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/confirmationdialog/ConfirmationDialogBean.java")
        }
)
@Menu(
	title = "menu.ace.confirmationdialog.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.confirmationdialog.subMenu.main",
	                isDefault = true,
                    exampleBeanName = ConfirmationDialogBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.confirmationdialog.subMenu.modal",
                    exampleBeanName = ConfirmationDialogModalBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.confirmationdialog.subMenu.effect",
                    exampleBeanName = ConfirmationDialogEffectBean.BEAN_NAME)
    }
)
@ManagedBean(name= ConfirmationDialogBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationDialogBean extends ComponentExampleImpl<ConfirmationDialogBean> implements Serializable {
    public static final String BEAN_NAME = "confirmationDialogBean";
    private String outcome = null;

    public ConfirmationDialogBean() {
        super(ConfirmationDialogBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public void yes(ActionEvent actionEvent) {
            outcome = "You are sure";
    }

    public void no(ActionEvent actionEvent) { 
            outcome = "You are not sure";
    }

    public String getOutcome() {
            return outcome;
    }
}