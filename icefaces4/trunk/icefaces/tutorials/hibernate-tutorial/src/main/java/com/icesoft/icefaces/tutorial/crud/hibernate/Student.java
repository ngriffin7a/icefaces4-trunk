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

package com.icesoft.icefaces.tutorial.crud.hibernate;


import com.icesoft.icefaces.tutorial.crud.hibernate.util.HibernateUtil;
import java.util.HashSet;
import java.util.Set;
import javax.faces.event.ActionEvent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Session;



/**
 * Represents a student object in the hibernate tutorial
 * Student Register example.
 */
@Entity
@Table(name = "student")
public class Student {
    // unique student id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="student_id")
	private int studentId;
    // first name of the student
	@Column(name="first_name")
    private String firstName;
    // last name of the student
	@Column(name="last_name")
	private String lastName;
    // address of the student
	@Column(name="address")
    private String address;
    // set of courses that the student is related/registered for
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="students", targetEntity=Course.class)
    private Set<Course> courses;
    
    @Transient
    private boolean uiToggle;
    
    
    /**
     * Default constructor
     */
    public Student() {
    }
    
    /**
     * Creates a new instance of Student.
     * @param firstName first name.
     * @param lastName last name.
     * @param address address.
     */
    public Student(String firstName, String lastName, String address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
    
    /**
     * Gets the student id for this student.
     * @return student id.
     */
    public int getStudentId(){
        return studentId;
    }
    
    /**
     * Sets the student id for this student.
     * @return student id.
     */
    public void setStudentId(int studentId){
        this.studentId = studentId;
    }
    
    /**
     * Gets the first name for this student.
     * @return first name.
     */
    public String getFirstName(){
        return firstName;
    }
    
    /**
     * Sets the first name for this student.
     * @param first name.
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    /**
     * Gets the last name for this student.
     * @return last name.
     */
    public String getLastName(){
        return lastName;
    }
    
    /**
     * Sets the last name for this student.
     * @param last name.
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    /**
     * Gets the address for this student.
     * @return address.
     */
    public String getAddress(){
        return address;
    }
    
    /**
     * Sets the address for this student.
     * @param address.
     */
    public void setAddress(String address){
        this.address = address;
    }
    
    /**
     * Gets the Set of courses for this student.
     * @return Set of courses.
     */
    public Set<Course> getCourses(){
        return courses;
    }
    
    /**
     * Sets the Set of courses for this student.
     * @return Set of courses.
     */
    public void setCourses(Set<Course> courses){
        this.courses = courses;
    }
    
    /**
     * Method used by the UI to clear information on the screen.
     * @return String used in the navigation rules.
     */
    @Transient
    public void clear(){
        firstName="";
        lastName="";
        address="";
    }
}
