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

package org.icefaces.context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.w3c.dom.Document;
import org.icefaces.impl.util.DOMUtils;
import org.icefaces.impl.context.DOMResponseWriter;

import javax.faces.context.ResponseWriter;
import java.io.StringWriter;


public class DOMResponseWriterTest {
    
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    private static final String ENCODING_UTF_8 = "UTF-8";

    private Document doc;
    private StringWriter sw;
    private ResponseWriter rw;

    @Before
    public void setUp() {
        doc = DOMUtils.getNewDocument();
        sw = new StringWriter();
        rw = new DOMResponseWriter(doc,sw);
    }

    @After
    public void tearDown() {
        // Add your code here
    }

    @Test
    public void testGetContentType() {
        Assert.assertEquals(CONTENT_TYPE_TEXT_HTML, rw.getContentType());
    }

    @Test
    public void testGetCharacterEncoding() {
        Assert.assertEquals(ENCODING_UTF_8, rw.getCharacterEncoding());
    }




}
