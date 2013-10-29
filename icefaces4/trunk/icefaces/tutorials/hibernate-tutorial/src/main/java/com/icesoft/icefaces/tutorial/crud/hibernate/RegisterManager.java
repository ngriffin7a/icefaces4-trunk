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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import org.hibernate.Query;
import org.hibernate.Session;
import javax.faces.model.SelectItem;

/**
 * Represents the Utility class for the StudentRegister application which uses
 * Hibernate to connect and interact with a MySql database.  
 **/

public class RegisterManager {
    
    // List of student id's
    private List studentItems = new ArrayList();
    // List of course names
    private List courseItems = new ArrayList();
    // Currently selected Student Id
    private String selectedStudent;
    // Currently selected Course name
    private String selectedCourse;
    // Student object used in adding a new student
    private Student newStudent = new Student();
    // Student object that is currently selected
    private Student currentStudent = new Student();
    // Student object used to update an existing student in the DB
    private Student updateStudent = new Student();
    // Course object that is currently selected
    private Course currentCourse = new Course();
    // List of course id's that a student is registered 
    private List studentCourses = new ArrayList();
    // The ice:panelTabSet's selectedIndex, for when we constrain the display
    // to the Add Student tab, when all the students have been deleted
    private int selectedTabIndex = 0;
    private long courseIdToRemoveFromStudent = -1;
    
    public RegisterManager() {
       init();
    }
    
    /**
     * Initialising class that populates the studentItems and courseItems 
     * objects to be used by the application.
	 * 
	 * The purpose of loading these objects into this managed bean is to 
	 * reduce the amount of information we need to transfer from the DB about an object.
	 * 
	 * After this inital caching, we only need to access the datasbase when changing values, removing values,
	 * or adding new ones; we already have the information about the current set of objects loaded.
   	 *
	 * Obviously creating an application that works like this requires that you're aware others could be editing
	 * the objects that you currently have cached. Unless a mechanism (like ICEpush) is used to broadcast changes
	 * to other clients to update their objects, a JTA implementation should be used to manage cocurrent transactions.
     */
    private synchronized void init(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List studentResult = session.createQuery("select s.studentId from com.icesoft.icefaces.tutorial.crud.hibernate.Student as s").list();
        List courseResult = session.createQuery("select c.courseName from com.icesoft.icefaces.tutorial.crud.hibernate.Course as c").list();
        if (studentResult.size() > 0) {
            currentStudent = (Student) session.get(Student.class, (java.io.Serializable) Integer.parseInt(studentResult.get(0).toString()));
        } else {
            selectedTabIndex = 0;
        }
        setStudentCourses();
        if (courseResult.size() > 0) {
            Query q = session.createQuery("from com.icesoft.icefaces.tutorial.crud.hibernate.Course as c where " +
                    "c.courseName=:name");
            q.setString("name", courseResult.get(0).toString());
            currentCourse = (Course) q.uniqueResult();
        } else {
            currentCourse = new Course();
        }
        session.getTransaction().commit();

        studentItems.clear();
        for(Object studentId : studentResult){
            studentItems.add(new SelectItem(studentId.toString()));
        }

        courseItems.clear();
        for(Object courseName : courseResult){
            courseItems.add(new SelectItem(courseName));
        }
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    public void setCourseIdToRemoveFromStudent(long courseIdToRemoveFromStudent) {
        this.courseIdToRemoveFromStudent = courseIdToRemoveFromStudent;
    }

    /**
     * Gets the studentItems list of student id's.
     * @return list of student id's.
     */
    public List getStudentItems(){
        return studentItems;
    }
    
    /**
     * Gets the courseItems list of course names.
     * @return list of course names.
     */
    public List getCourseItems(){
        return courseItems;
    }    
    
    /**
     * Gets the selectedStudent student id.
     * @return selected student id.
     */
    public String getSelectedStudent(){
        return selectedStudent;
    }
    
    /**
     * Gets the selectedCourse course name.
     * @return selected course name.
     */
    public String getSelectedCourse(){
        return selectedCourse;
    }
    
    /**
     * Sets the selectedStudent student id.
     * @param selected student id.
     */
    public void setSelectedStudent(String selectedStudent){
        this.selectedStudent = selectedStudent;
    }
    
    /**
     * Sets the selectedCourse course name.
     * @param selected course name.
     */
    public void setSelectedCourse(String selectedCourse){
        this.selectedCourse = selectedCourse;
    }
    
    /**
     * Gets the currentStudent Student object.
     * @return Student object currently selected.
     */
    public Student getCurrentStudent(){
        return currentStudent;
    }
    
    /**
     * Gets the newStudent Student object.
     * @return Student object to be added.
     */
    public Student getNewStudent(){
        return newStudent;
    }
    
    /**
     * Gets the updateStudent Student object.
     * @return Student object to be updated.
     */
    public Student getUpdateStudent(){
        return updateStudent;
    }
    
    /**
     * Gets the currentCourse Course object.
     * @return Course object currently selected.
     */
    public Course getCurrentCourse(){
        return currentCourse;
    }
    
    /**
     * Gets the studentCourses list of course id's.
     * @return List of course id's.
     */
    public List getStudentCourses(){
        return studentCourses;
    }
    
    /**
     * Sets the studentCourses with the courses related to the currentStudent 
     * object.
     */
    protected void setStudentCourses(){
        studentCourses.clear();
        Set<Course> c = (currentStudent != null) ? currentStudent.getCourses() : null;
        if (c != null) {
            studentCourses.addAll(c);
            Collections.sort(studentCourses, new Comparator<Course>() {
                public int compare(Course o1, Course o2) {
                    return Long.signum(o1.getCourseId() - o2.getCourseId());
                }
            });
        }
    }
    
    /**
     * Listener for the student id selectOneMenu component value change action.
     * @param ValueChangeEvent representing the new value.
     */   
    public void studentValueChanged(ValueChangeEvent event){
        if(event.getNewValue() != null){
            int id = Integer.parseInt(event.getNewValue().toString());
            Session session = HibernateUtil.getSessionFactory()
                .getCurrentSession();
            session.beginTransaction();
            currentStudent = (Student) session.load(Student.class, id);
            setStudentCourses();
            session.getTransaction().commit();
        } else {
            currentStudent = new Student();
            setStudentCourses();
        }
    }
    /**
     * Listener for the course name selectOneMenu component value change action.
     * @param ValueChangeEvent representing the new value.
     */
    public void courseValueChanged(ValueChangeEvent event){
        if(event.getNewValue() != null){
            String name = event.getNewValue().toString();
            Session session = HibernateUtil.getSessionFactory()
                .getCurrentSession();
            session.beginTransaction();
            Query q = session.createQuery(
                "from com.icesoft.icefaces.tutorial.crud.hibernate.Course " +
                    "as c where c.courseName=:name");
            q.setString("name", name);
            currentCourse = (Course) q.uniqueResult();
            session.getTransaction().commit();
        }
    }
    
    /**
     * Listener for the add student button click action.
     * @param ActionEvent click action event.
     */
    public void addStudent(ActionEvent event){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        session.save(newStudent);
        
        session.getTransaction().commit();
        
        newStudent.clear();
        init();
    }
    
    /**
     * Listener for the delete student button click action.
     * @param ActionEvent click action event.
     */
    public void deleteStudent(ActionEvent event){
        if(currentStudent != null){
            int id = currentStudent.getStudentId();
            Session session = HibernateUtil.getSessionFactory()
                .getCurrentSession();
            session.beginTransaction();

            currentStudent = (Student) session.get(Student.class,
                currentStudent.getStudentId());
            for (Course c : currentStudent.getCourses()) {
                c.getStudents().remove(currentStudent);
            }
            currentStudent.getCourses().clear();
            session.delete(currentStudent);

            session.getTransaction().commit();

            currentStudent.clear();
            init();
        }
    }
    
    /**
     * Listener for the save changes button click action.
     * @param ActionEvent click action event.
     */
    public void updateStudent(ActionEvent event){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        currentStudent = (Student) session.get(Student.class,
            currentStudent.getStudentId());
        if (updateStudent.getFirstName().length() > 0) {
            currentStudent.setFirstName(updateStudent.getFirstName());
        }
        if (updateStudent.getLastName().length() > 0) {
            currentStudent.setLastName(updateStudent.getLastName());
        }
        if (updateStudent.getAddress().length() > 0) {
            currentStudent.setAddress(updateStudent.getAddress());
        }
        session.persist(currentStudent);

        session.getTransaction().commit();
        
        updateStudent.clear();
    }
    
    /**
     * Listener for the add course button click action.
     * @param ActionEvent click action event.
     */
    public void addCourseToStudent(ActionEvent event){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        currentStudent = (Student) session.get(Student.class,
            currentStudent.getStudentId());
        currentCourse = (Course) session.load(Course.class,
            currentCourse.getCourseId());

        currentStudent.getCourses().add(currentCourse);
        currentCourse.getStudents().add(currentStudent);
        // Or persist currentCourse. Either way cascades.
        session.persist(currentStudent);
        setStudentCourses();

        session.getTransaction().commit();
    }
    
    /**
     * Listener for the remove course button click action.
     * @param ActionEvent click action event.
     */ 
    public void removeCourseFromStudent(){
        if (courseIdToRemoveFromStudent < 0) {
            return;
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        currentStudent = (Student) session.get(Student.class,
            currentStudent.getStudentId());
        Course removeCourse = (Course) session.load(Course.class,
            courseIdToRemoveFromStudent);

        currentStudent.getCourses().remove(removeCourse);
        removeCourse.getStudents().remove(currentStudent);
        // Or persist removeCourse. Either way cascades.
        session.persist(currentStudent);
        setStudentCourses();

        session.getTransaction().commit();

        courseIdToRemoveFromStudent = -1;
    }
}
