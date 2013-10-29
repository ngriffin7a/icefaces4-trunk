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

/*
 * PhoneNumberValidator.java
 *
 * Created on March 28, 2007, 2:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.icefaces.tutorial.components.validators.custom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author dnorthcott
 */
public class PhoneNumberValidator implements Validator {

    /** Creates a new instance of PhoneNumberValidator */
    public PhoneNumberValidator() {
    }

    /** phone number*/
    private static final String PHONE_NUM= "[0-9]{3}[-]{1}[0-9]{4}";

    public void validate(FacesContext context, UIComponent component, Object value){
        /*create a mask
         */
        Pattern mask = Pattern.compile(PHONE_NUM);

        /* retrieve the string value of the field*/
        String phoneNumber = (String)value;

        /* ensure value is a phone number*/
        Matcher matcher = mask.matcher(phoneNumber);

        if(!matcher.matches()){
            FacesMessage message = new FacesMessage();
            message.setDetail("Phone number not in valid format");
            message.setSummary("Phone number not in valid format");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }

    }
}
