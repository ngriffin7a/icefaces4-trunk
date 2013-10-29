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

package com.icesoft.icefaces.tutorial.component.panelSeries.custom;

import java.io.Serializable;
import java.util.Date;
import javax.faces.event.ActionEvent;

/**
 * <p>A custom object represents the personal information</p>
 */
public class CustomObject implements Serializable {

	private String name;
	private String emailAddress;
	private int age;
	private String phoneNumber;
	private boolean anonymous;
	private String inputMessage;
	private Date selectedDate;

	private boolean done = false;

	public CustomObject(){
	}

	public CustomObject(String newName, String newEmailAddress,
						int newAge, String newPhoneNumber,
						boolean newAnonymous, String newInputMessage,
						Date newSelectedDate){
		name = newName;
		emailAddress = newEmailAddress;
		age = newAge;
		phoneNumber = newPhoneNumber;
		anonymous = newAnonymous;
		inputMessage = newInputMessage;
		selectedDate = newSelectedDate;
	}

	public void showMessage(ActionEvent event){
		done = true;
	}

	public boolean isDone(){
		return done;
	}

	public void setDone(boolean done){
		this.done = done;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getEmailAddress(){
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress){
		this.emailAddress = emailAddress;
	}

	public int getAge(){
		return this.age;
	}

	public void setAge(int age){
		this.age = age;
	}

	public String getPhoneNumber(){
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public boolean isAnonymous(){
		return this.anonymous;
	}

	public void setAnonymous(boolean anonymous){
		this.anonymous = anonymous;
	}

	public String getInputMessage(){
		return this.inputMessage;
	}

	public void setInputMessage(String inputMessage){
		this.inputMessage = inputMessage;
	}

	public Date getSelectedDate(){
		return this.selectedDate;
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}





}