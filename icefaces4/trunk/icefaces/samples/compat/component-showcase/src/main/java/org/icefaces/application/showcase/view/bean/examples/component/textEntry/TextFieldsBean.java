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

package org.icefaces.application.showcase.view.bean.examples.component.textEntry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.application.showcase.view.bean.BaseBean;

/**
 * <p>The TextFieldsBean class is the backing bean for the Text Entry
 * demonstration. It is used to store the values of the input fields.</p>
 */
@ManagedBean(name = "textFields")
@ViewScoped
public class TextFieldsBean extends BaseBean {
    /**
     * The different kinds of text input fields.
     */
    private String name;
    private String password;
    private String comments;

    /**
     * Gets the name property.
     *
     * @return value of name property
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name property
     *
     * @param newValue new value of the name property
     */
    public void setName(String newValue) {
        name = newValue;
    }

    /**
     * Gets the password property.
     *
     * @return value of the password property
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password property.
     *
     * @param newValue new value of the password property
     */
    public void setPassword(String newValue) {
        password = newValue;
    }

    /**
     * Gets the comments property.
     *
     * @return value of the comments property
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments property.
     *
     * @param newValue new value of the comments property
     */
    public void setComments(String newValue) {
        comments = newValue;
    }
    
}