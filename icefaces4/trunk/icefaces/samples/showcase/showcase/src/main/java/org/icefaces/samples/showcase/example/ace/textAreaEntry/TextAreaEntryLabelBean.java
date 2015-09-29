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

package org.icefaces.samples.showcase.example.ace.textAreaEntry;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name= TextAreaEntryLabelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TextAreaEntryLabelBean implements Serializable
{
    public static final String BEAN_NAME = "textAreaEntryLabelBean";
	public String getBeanName() { return BEAN_NAME; }
    
    private String comment1LabelText = "Comments";
    private String comment2LabelText = "Additional Comments";
    private String labelPosition = "top";
    
    public String getComment1LabelText() {
        return comment1LabelText;
    }
    
    public String getComment2LabelText() {
        return comment2LabelText;
    }
    
    public String getLabelPosition() {
        return labelPosition;
    }
    
    public void setComment1LabelText(String labelText) {
        this.comment1LabelText = labelText;
    }
    
    public void setcomment2LabelText(String labelText) {
        this.comment2LabelText = labelText;
    }
    
    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }
}
