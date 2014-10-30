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

package org.icefaces.samples.showcase.example.ace.textEntry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

@ComponentExample(
        parent = TextEntryBean.BEAN_NAME,
        title = "example.ace.textEntry.secret.title",
        description = "example.ace.textEntry.secret.description",
        example = "/resources/examples/ace/textEntry/textEntrySecret.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="textEntrySecret.xhtml",
                    resource = "/resources/examples/ace/textEntry/textEntrySecret.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TextEntrySecretBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/textEntry/TextEntrySecretBean.java")
        }
)
@ManagedBean(name= TextEntrySecretBean.BEAN_NAME)
@ViewScoped
public class TextEntrySecretBean extends ComponentExampleImpl<TextEntrySecretBean> implements Serializable
{
    public static final String BEAN_NAME = "textEntrySecretBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private String username, password;
	private boolean showPassword;
    
    public TextEntrySecretBean() {
        super(TextEntrySecretBean.class);
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShowPassword() {
        return showPassword;
    }

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
