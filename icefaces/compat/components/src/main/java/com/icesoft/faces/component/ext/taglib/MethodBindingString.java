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

/* This code was reimplemented to eliminate dependency on Sun RI code */

package com.icesoft.faces.component.ext.taglib;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

public class MethodBindingString extends javax.faces.el.MethodBinding
        implements StateHolder {
    private String stringValue;
    private boolean transient_;

    public MethodBindingString() {
        super();
    }

    /**
     * create a method binding for the string
     *
     * @param stringValue
     */
    public MethodBindingString(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * no method invocation required for the string
     *
     * @return stringValue
     */
    public Object invoke(FacesContext facesContext, Object[] params) {
        return stringValue;
    }

    public Class getType(FacesContext facesContext) {
        return String.class;
    }

    public Object saveState(FacesContext facesContext) {
        return stringValue;
    }

    public void restoreState(FacesContext facesContext, Object state) {
        stringValue = (String) state;
    }

    public boolean isTransient() {
        return transient_;
    }

    public void setTransient(boolean transient_) {
        this.transient_ = transient_;
    }

}
