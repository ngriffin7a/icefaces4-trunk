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

import java.util.HashSet;
import java.util.Set;

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

/**
 * Represents a Course in the hibernate tutorial Student Register example.
 */
@Entity
@Table(name = "course")
public class Course {
	// unique course id
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "course_id")
	private long courseId;
	// name of the course
	@Column(name = "course_name")
	private String courseName;
	// description of the course
	@Column(name = "description")
	private String description;
	// set of students that are related/registered for the course
    @ManyToMany
    @JoinTable(name = "student_course",
        joinColumns =
        @JoinColumn(name = "course_id", referencedColumnName = "course_id"),
        inverseJoinColumns =
        @JoinColumn(name = "student_id", referencedColumnName = "student_id"))
    private Set<Student> students;

	/**
	 * Default contructor.
	 */
	public Course() {
	}

	/**
	 * Creates a new instance of Course.
	 * 
	 * @param courseId
	 *            course id.
	 * @param courseName
	 *            course name.
	 * @param description
	 *            description of the course.
	 */
	public Course(Long courseId, String courseName, String description) {
		if (courseId != null)this.courseId = courseId;
		this.courseName = courseName;
		this.description = description;
	}

	/**
	 * Gets the course Id of this course.
	 * 
	 * @return course id.
	 */
	public long getCourseId() {
		return courseId;
	}

	/**
	 * Sets the course id of this course.
	 * 
	 * @param course
	 *            id.
	 */
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	/**
	 * Gets the course name for this course.
	 * 
	 * @return course name.
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * Sets the course name for this course.
	 * 
	 * @param course
	 *            name.
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * Gets the description for this course.
	 * 
	 * @return description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description for this course.
	 * 
	 * @param description
	 *            .
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the set of students for this course.
	 * 
	 * @return Set of students.
	 */
	public Set<Student> getStudents() {
		return students;
	}

	/**
	 * Sets the set of students for this course.
	 * 
	 * @param Set
	 *            of students.
	 */
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
}
