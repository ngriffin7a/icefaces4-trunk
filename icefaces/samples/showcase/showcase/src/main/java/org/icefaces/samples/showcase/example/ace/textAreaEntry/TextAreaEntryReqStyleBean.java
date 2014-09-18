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

package org.icefaces.samples.showcase.example.ace.textAreaEntry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        parent = TextAreaEntryBean.BEAN_NAME,
        title = "example.ace.textAreaEntry.reqStyle.title",
        description = "example.ace.textAreaEntry.reqStyle.description",
        example = "/resources/examples/ace/textAreaEntry/textAreaEntryReqStyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="textAreaEntryReqStyle.xhtml",
                    resource = "/resources/examples/ace/textAreaEntry/textAreaEntryReqStyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TextAreaEntryReqStyleBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/textAreaEntry/TextAreaEntryReqStyleBean.java")
        }
)
@ManagedBean(name= TextAreaEntryReqStyleBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TextAreaEntryReqStyleBean extends ComponentExampleImpl<TextAreaEntryReqStyleBean> implements Serializable
{
    public static final String BEAN_NAME = "textAreaEntryReqStyleBean";
    
    private String reqColor = "redRS";
    private String optColor = "greenRS";
    
    public TextAreaEntryReqStyleBean() {
        super(TextAreaEntryReqStyleBean.class);
    }
    
    public String getReqColor() {
        return reqColor;
    }
    
    public String getOptColor() {
        return optColor;
    }
    
    public void setReqColor(String reqColor) {
        this.reqColor = reqColor;
    }
    
    public void setOptColor(String optColor) {
        this.optColor = optColor;
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
	
	private boolean useTheme = false;

    public boolean getUseTheme() {
        return useTheme;
    }

    public void setUseTheme(boolean useTheme) {
        this.useTheme = useTheme;
    }
}
