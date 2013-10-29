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

package com.icesoft.icefaces.tutorial.component.converter.basic;

import java.util.Date;

/*
 * Basic Backing bean for the ICEfaces converter example
 */
public class User {
    
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private int age;
    private Date birthDate;
    private float salary;
    
    /** Creates a new instance of User */
    public User() {
    }
    
    public User(String firstName, String lastName, String address, 
            String city, int age, Date birthDate, float salary){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.age = age;
        this.birthDate = birthDate;
        this.salary = salary;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public String getAddress(){
        return address;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public String getCity(){
        return city;
    }
    
    public void setCity(String city){
        this.city = city;
    }
    
    public int getAge(){
        return age;
    }
    
    public void setAge(int age){
        this.age = age;
    }
    
    public Date getBirthDate(){
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }
    
    public float getSalary(){
        return salary;
    }
    
    public void setSalary(float salary){
        this.salary = salary;
    }
    
    public String toString(){
        return "First Name: " + firstName + "/n" + "Last Name: " + lastName +
                "/n" + "Address: " + address + "/n" + "City: " + city + "/n"
                + "Age: " + age + "/n" + "Birth Date: " + birthDate.toString();
                
    }
    
}
