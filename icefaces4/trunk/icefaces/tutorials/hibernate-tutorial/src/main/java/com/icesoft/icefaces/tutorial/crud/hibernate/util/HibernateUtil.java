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

package com.icesoft.icefaces.tutorial.crud.hibernate.util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.icesoft.icefaces.tutorial.crud.hibernate.Course;
import com.icesoft.icefaces.tutorial.crud.hibernate.Student;

public class HibernateUtil {
	
    private static final SessionFactory sessionFactory = buildSessionFactory();

    static {
    	//DB Initalization.
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        session.save(new Student("Alfred","Barkley","123 Fake St."));
        session.save(new Student("Carl","Dodds","124 Fake St."));
        session.save(new Student("Eugene","Faulks","125 Fake St."));
        
        session.save(new Course(null,"ICE101","Introduction to ICEfaces"));
        session.save(new Course(null,"ICE201","Intermediate ICEfaces Techniques"));
        session.save(new Course(null,"ICE301","Advanced ICEfaces Techniques"));
        session.save(new Course(null,"AJAX101","AJAX Basics"));
        session.save(new Course(null,"JAVA101","Beginner Java"));
        session.save(new Course(null,"HIB101","Introduction to Hibernate"));
        session.save(new Course(null,"JSF201","Advanced JSF Techniques"));
        session.save(new Course(null,"SQL101","Beginner SQL Programming"));

        session.getTransaction().commit();
    }
    
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	SessionFactory f = new Configuration().configure().buildSessionFactory();
            return f;
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}