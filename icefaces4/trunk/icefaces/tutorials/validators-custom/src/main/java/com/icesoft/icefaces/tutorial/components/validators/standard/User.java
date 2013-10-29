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
 * User.java
 *
 * Created on March 27, 2007, 9:31 AM
 *
 * Backing Bean for Validatioin tutorial
 */

package com.icesoft.icefaces.tutorial.components.validators.standard;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.Serializable;

/**
 *
 * @author dnorthcott
 */
public class User implements Serializable {

    private static final long serialVersionUID = -2217818719254232652L;
    private long age =0;
    private long displayAge = 0;
    private String name;
    private String displayName;
    private String phoneNumber;
    private String email;
    private String displayEmail;

    private String displayPhoneNumber;


    /** Creates a new instance of User */
    public User() {
    }

    public void setAge(long age){
        this.age = age;
    }

    public long getAge(){
        return age;
    }

    public long getDisplayAge(){
        return displayAge;
    }
    public void setDisplayAge(long ds){
        this.displayAge = ds;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setDisplayName(String ds){
        this.displayName = ds;
    }
    public String getDisplayName(){
        return displayName;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setDisplayPhoneNumber(String dpn){
        this.displayPhoneNumber = dpn;
    }
    public String getDisplayPhoneNumber(){
        return displayPhoneNumber;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setDisplayEmail(String de){
        this.displayEmail = de;
    }
    public String getDisplayEmail(){
        return displayEmail;
    }

    public void submitAction(ActionEvent event){
        setDisplayAge(age);
    }

    /** register method to validate the registered name
     *@param ActionEvent event
     */
    public void register(ActionEvent event){

        setDisplayName(name);
        FacesContext context = FacesContext.getCurrentInstance();
        String str = getName();
        if(str == null || str.length() == 0){
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Name Field is Blank");
            message.setDetail("Name Field is Blank..");
            context.addMessage("tutorialForm:name",message);

        }

    }
    /** method to verify the phone number
     *@param ActionEvent event
     */
    public void submitActionPhone(ActionEvent event){
        setDisplayPhoneNumber(phoneNumber);
    }

    /** method to validate the email address
     *@param ActionEvent event
     */
    public void submitActionEmail(ActionEvent event){
        setDisplayEmail(email);
    }

    /** method to validate the email field
     *@param FacesContext context, UIComponent validate, Object value
     */
    public void validateEmail(FacesContext context, UIComponent validate, Object value){
        String email = (String)value;

        if(email.indexOf('@')==-1){
            ((UIInput)validate).setValid(false);
            FacesMessage msg = new FacesMessage("Invalid Email");
            context.addMessage(validate.getClientId(context), msg);
        }
    }

}
