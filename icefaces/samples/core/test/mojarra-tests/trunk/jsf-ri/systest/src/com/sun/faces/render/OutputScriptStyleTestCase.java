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
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.render;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;


public class OutputScriptStyleTestCase extends AbstractTestCase {

    public OutputScriptStyleTestCase(String name) {
        super(name);
    }

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(OutputScriptStyleTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testOutputScriptStyle() throws Exception {
        HtmlPage page = getPage("/faces/render/outputScriptStyleNested.xhtml");

        String text = page.asXml();

        // case 1
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/case1.js\">.*" + 
                "</script>.*" + 
                "</head>.*"
                ) || 
                text.matches(
                "(?s).*<head>.*"+
                "<script src=\"/jsf-systest/faces/javax.faces.resource/case1.js\".*type=\"text/javascript\">.*" + 
                "</script>.*" + 
                "</head>.*"
                ));
        
        assertTrue(!text.matches("(?s).*alert\\(\"case1\"\\);.*"));
        
        // case 2
        assertTrue(text.matches(
                "(?s).*<body>.*"+
                "<script type=\"text/javascript\">.*" + 
                "alert\\(\"case2\"\\);.*" +
                "</script>.*" + 
                "</body>.*"
                ));
        
        // case 3
        assertTrue(text.matches(
                "(?s).*<body>.*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/case3.js\">.*" + 
                "</script>.*" + 
                "</body>.*"
                ) || 
                text.matches(
                "(?s).*<body>.*"+
                "<script src=\"/jsf-systest/faces/javax.faces.resource/case3.js\".*type=\"text/javascript\">.*" + 
                "</script>.*" + 
                "</body>.*"
                ));
        
        assertTrue(!text.matches("(?s).*alert\\(\"case3\"\\);.*"));
        
        // case 4
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/case4.js\">.*" + 
                "</script>.*" + 
                "</head>.*"
                ) || text.matches(
                "(?s).*<head>.*"+
                "<script src=\"/jsf-systest/faces/javax.faces.resource/case4.js\".*type=\"text/javascript\">.*" + 
                "</script>.*" + 
                "</head>.*"
                ));

        // case 5, if not satisfied, would cause the page to fail.

        // case 6
        assertTrue(text.matches(
                "(?s).*<body>.*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/case6.js\">.*" + 
                "</script>.*" + 
                "</body>.*"
                ) || text.matches(
                "(?s).*<body>.*"+
                "<script src=\"/jsf-systest/faces/javax.faces.resource/case6.js\".*type=\"text/javascript\">.*" + 
                "</script>.*" + 
                "</body>.*"
                ));
        
        // case 7, if not satisfied, would cause the page to fail
        
        // case 8
        assertTrue(text.matches(
                "(?s).*<body>.*"+
                "<script type=\"text/javascript\">.*" + 
                "alert\\(\"case8\"\\);.*" +
                "</script>.*" + 
                "</body>.*"
                ));
        
        // case 9
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".*rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case9.css\"\\s*/>.*" + 
                "</head>.*"
                ) || text.matches(
                "(?s).*<head>.*"+
                "<link.* href=\"/jsf-systest/faces/javax.faces.resource/case9.css\"\\s*.* rel=\"stylesheet\".*type=\"text/css\"/>.*" + 
                "</head>.*"
                ));
        
        assertTrue(!text.matches("(?s).*\\.case9.*"));
        
        // case 10
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<style\\s*type=\"text/css\">.*" +
                "\\.case10\\s*\\{.*" +
                "color: blue;.*" +
                "\\}.*" +
                "</style>.*" +
                "</head>.*"
                ));
        

        // case 11
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".*rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case11.css\"\\s*/>.*" + 
                "</head>.*"
                ) || text.matches(
                "(?s).*<head>.*"+
                "<link.* href=\"/jsf-systest/faces/javax.faces.resource/case11.css\"\\s*.* rel=\"stylesheet\".*type=\"text/css\"/>.*" + 
                "</head>.*"
                ));
        
        assertTrue(!text.matches("(?s).*\\.case11.*"));
        
        // case 12
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".*rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case12.css\"\\s*/>.*" + 
                "</head>.*"
                ) || text.matches(
                "(?s).*<head>.*"+
                "<link.* href=\"/jsf-systest/faces/javax.faces.resource/case12.css\"\\s*.* rel=\"stylesheet\".*type=\"text/css\"/>.*" + 
                "</head>.*"
                ));

        // case 13, if not satisfied, would cause the page to fail.

        // case 14
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<link.* type=\"text/css\".*rel=\"stylesheet\".* href=\"/jsf-systest/faces/javax.faces.resource/case14.css\"\\s*/>.*" + 
                "</head>.*"
                ) || text.matches(
                "(?s).*<head>.*"+
                "<link.* href=\"/jsf-systest/faces/javax.faces.resource/case14.css\"\\s*.* rel=\"stylesheet\".*type=\"text/css\"/>.*" + 
                "</head>.*"
                ));

        // case 15, if not satisfied, would cause the page to fail.
        
        // case 16
        assertTrue(text.matches(
                "(?s).*<head>.*"+
                "<style\\s*type=\"text/css\">.*" +
                "\\.case16\\s*\\{.*" +
                "color: orange;.*" +
                "\\}.*" +
                "</style>.*" +
                "</head>.*"
                ));

    }

    public void testScriptQuery() throws Exception {
        lastpage = getPage("/faces/render/outputScriptQuery.xhtml");
        String text = lastpage.asXml();

        assertTrue(text.matches(
                "(?s).*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/simple.js\\?mod=test\">.*" +
                "</script>.*"
                ) || text.matches(
                "(?s).*"+
                "<script src=\"/jsf-systest/faces/javax.faces.resource/simple.js\\?mod=test\".*type=\"text/javascript\">.*" +
                "</script>.*"
                ));

        assertTrue(text.matches(
                "(?s).*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/simple2.js\">.*" +
                "</script>.*"
                ) || text.matches(
                "(?s).*"+
                "<script src=\"/jsf-systest/faces/javax.faces.resource/simple2.js\".*type=\"text/javascript\">.*" +
                "</script>.*"
                ));

        assertTrue(text.matches(
                "(?s).*"+
                "<script type=\"text/javascript\".*src=\"/jsf-systest/faces/javax.faces.resource/jsf.js\\?ln=javax.faces&amp;stage=Development\">.*" +
                "</script>.*"
                ) || text.matches(
                "(?s).*"+
                "<script src=\"/jsf-systest/faces/javax.faces.resource/jsf.js\\?ln=javax.faces&amp;stage=Development\".*type=\"text/javascript\">.*" +
                "</script>.*"
                ));

    }
}