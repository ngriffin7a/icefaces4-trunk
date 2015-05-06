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

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.ace.event.CharCountEvent;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TextEntryBean.BEAN_NAME,
        title = "example.ace.textEntry.charCount.title",
        description = "example.ace.textEntry.charCount.description",
        example = "/resources/examples/ace/textEntry/textEntryCharCount.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="textEntryCharCount.xhtml",
                    resource = "/resources/examples/ace/textEntry/textEntryCharCount.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TextEntryCharCountBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/textEntry/TextEntryCharCountBean.java")
        }
)
@ManagedBean(name= TextEntryCharCountBean.BEAN_NAME)
@ViewScoped
public class TextEntryCharCountBean extends ComponentExampleImpl<TextEntryCharCountBean> implements Serializable
{
    public static final String BEAN_NAME = "textEntryCharCountBean";
	public String getBeanName() { return BEAN_NAME; }
	
	private int maxlength = 20;
	private String comment;
	private Long currLength = 0l;
	private Long charsRemaining = (long)maxlength;
    
    public TextEntryCharCountBean() {
        super(TextEntryCharCountBean.class);
    }
    
    public int getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Long getCurrLength() {
		return currLength;
	}

	public void setCurrLength(Long currLength) {
		this.currLength = currLength;
	}

	public Long getCharsRemaining() {
		return charsRemaining;
	}

	public void setCharsRemaining(Long charsRemaining) {
		this.charsRemaining = charsRemaining;
	}

	public void updateCount(CharCountEvent event) {
		currLength = event.getCurrentLength();
		charsRemaining = event.getCharsRemaining();
	}

	@PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
