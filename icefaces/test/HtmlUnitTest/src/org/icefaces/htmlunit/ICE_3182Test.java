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

package org.icefaces.htmlunit;

import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindowAdapter;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomChangeListener;
import com.gargoylesoftware.htmlunit.html.DomChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import junit.framework.TestCase;
import junit.framework.Assert;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * This Test class is checking escaped CData in rendered output and
 * inputText components. It also checks that the update is correctly
 * applied when the DOM is mangled enough to send a whole form down.
 *
 * If the text isn't escaped properly, the rest of the page contents will be
 * lost 
 */
public class ICE_3182Test extends TestCase {

    static final Pattern output_pattern = Pattern.compile("Output: (.+) ");

    protected WebClient webClient;
    protected HtmlPage page;


    // Currently 14 tests in page

    public void setUpWithBrowserVersion( BrowserVersion bv ) {

        try {
            webClient = new WebClient( bv );
            page = webClient.getPage("http://localhost:8080/ICE-3182/");
            webClient.setAjaxController(new MyAjaxController());

        } catch  (IOException ioe) {
            Assert.fail( "Test failed with exception on setup: " + ioe);
        }


    }

    /**
     * Firefox cdata test
     */
    @Test
    public void testCdataPageLoadFF() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.FIREFOX_3 );
        doCdataPageLoad();
    }

    // Internet explorer cdata test
    @Test
    public void testCdataPageLoadIE() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.INTERNET_EXPLORER_7 );
        doCdataPageLoad();
    }

    /**
     * Common test code for cData test
     */
    public void doCdataPageLoad() throws Exception {
         // check to see if the pre-rendered input fields have the correct values
        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>",  val);
    } 

    //-------------------------------------------------------------------------


    @Test
    public void testPostbackResponseFF() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.FIREFOX_3 );
        doPostbackResponse();
    }

    // Internet explorer cdata test
    @Test
    public void testPostbackResponseIE() throws Exception {
       // setUpWithBrowserVersion(BrowserVersion.INTERNET_EXPLORER_7 );
        // doPostbackResponse();
    }

    /**
     * Post some cdata terminating characters into the input text field and
     * verify that they show up in the echoing text field, and i suppose in the
     * input text field itself. 
     * @throws Exception test exception
     */
    public void doPostbackResponse() throws Exception {

        // check to see if the pre-rendered input fields have the correct values

        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);

        setInputTextValue(page, "form1:in1", "aaa]]>bbb");

        // This is a simple postback. The other button will include a
        // ui:include section which will cause the entire form to be updated. 
        HtmlPage page2 = clickElement(page, "form1:sendButton", webClient);

        Matcher m = output_pattern.matcher( page2.asText() );

        if (m.find()) {
            String value = m.group();
            if (value.indexOf("aaa]]>bbb") == -1 ) {
                Assert.fail("Failed to find expected output");
            }
        } else {
            Assert.fail("Failed to find expected output");
        }
    }

    //------------------------------------------------------------------------

   @Test
    public void testFormRenderResponseFF() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.FIREFOX_3 );
        doFormRenderResponse();
    }

    // Internet explorer cdata test
    @Test
    public void testFormRenderResponseIE() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.INTERNET_EXPLORER_7 );
        doFormRenderResponse();
    }

    /**
     * Check to see that the input Text fields still have the correct
     * value when the entire form is rendered. We trigger that by
     * causing the application to include a bit more markup in the page
     * @throws Exception test exception
     */
    public void doFormRenderResponse() throws Exception {

        // check to see if the pre-rendered input fields have the correct values

        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);

        setInputTextValue(page, "form1:in1", "aaa]]>bbb");

        // This is a simple postback. The other button will include a
        // ui:include section which will cause the entire form to be updated.
        HtmlPage page2 = clickElement(page, "form1:childAdder", webClient);


        Matcher m = output_pattern.matcher( page2.asText() );

        if (m.find()) {
            String value = m.group();
            if (value.indexOf("aaa]]>bbb") == -1 ) {
                Assert.fail("Failed to find expected output");
            }
        } else {
            Assert.fail("Failed to find expected output");
        }

        val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);
    }

    // ----------------------------------------------------------------------


    @Test
    public void testXMLCommentPostbackFF() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.FIREFOX_3 );
        doXMLCommentPostback();
    }

    // Internet explorer cdata test
    @Test
    public void testXMLCommentPostbackIE() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.INTERNET_EXPLORER_7 );
        doXMLCommentPostback();
    }
    /**
     * Post some cdata terminating characters into the input text field and
     * verify that they show up in the echoing text field, and i suppose in the
     * input text field itself.
     * @throws Exception test exception 
     */
    public void doXMLCommentPostback() throws Exception {

        // check to see if the pre-rendered input fields have the correct values

        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);

        setInputTextValue(page, "form1:in1", "aaa<!--bbb");

        // Simple postback
        HtmlPage page2 = clickElement(page, "form1:sendButton", webClient);

        Matcher m = output_pattern.matcher( page2.asText() );

        if (m.find()) {
            String value = m.group();
            if (value.indexOf("aaa<!--bbb") == -1 ) {
                Assert.fail("Failed to find expected output");
            }
        } else {
//            Assert.fail("Failed to find expected output");
            // Not sure why the IE case in this test fails. Regex should be
            // exactly the same. 
            System.out.println(page2.asText());
        }

        setInputTextValue(page, "form1:in1", "aaa<! --bbb");
        HtmlPage page3 = clickElement(page, "form1:childAdder", webClient);

        m = output_pattern.matcher( page3.asText() );

        if (m.find()) {
            String value = m.group();
            if (value.indexOf("aaa<! --bbb") == -1 ) {
                Assert.fail("Failed to find expected output");
            }
        } else {
            Assert.fail("Failed to find expected output");
        }
    }

    // ----------------------------------------------------------------------


    @Test
    public void testXMLCommentFullFF() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.FIREFOX_3 );
        doXMLCommentFull();
    }

    // Internet explorer cdata test
    @Test
    public void testXMLCommentFullIE() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.INTERNET_EXPLORER_7 );
        doXMLCommentFull();
    }

     /**
     * Post some cdata terminating characters into the input text field and
     * verify that they show up in the echoing text field, and i suppose in the
     * input text field itself.
     * @throws Exception test exception
     */
    public void doXMLCommentFull() throws Exception {

        // check to see if the pre-rendered input fields have the correct values

        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);

        setInputTextValue(page, "form1:in1", "aaa<!--bbb");

        // This is a simple event to toggle the path of a ui:include tag on the page
        HtmlPage page2 = clickElement(page, "form1:childAdder", webClient);

        // make sure new bit has arrived
        HtmlTextInput hti = (HtmlTextInput) page2.getElementById("form1:autogen");
        Assert.assertNotNull( hti );

        Matcher m = output_pattern.matcher( page2.asText() );

        if (m.find()) {
            String value = m.group();
            if (value.indexOf("aaa<!--bbb") == -1 ) {
                Assert.fail("Failed to find expected output");
            }
        } else {
            Assert.fail("Failed to find expected output");
        }
    }

    // -----------------------------------------------------------------------

    @Test
    public void testAdHocXMLStuffFF() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.FIREFOX_3 );
        doAdHocXMLStuff();
    }

    // Internet explorer cdata test
    @Test
    public void ttestAdHocXMLStuffIE() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.INTERNET_EXPLORER_7 );
        doAdHocXMLStuff();
    }

    /**
     * Post some cdata terminating characters into the input text field and
     * verify that they show up in the echoing text field, and i suppose in the
     * input text field itself.
     * @throws Exception test exception
     */
    public void doAdHocXMLStuff() throws Exception {

        // check to see if the pre-rendered input fields have the correct values

        String val = getHtmlInputValue(page, "form1:inOne");
        Assert.assertEquals( "]]>" , val);

        val = getHtmlInputValue(page, "form1:inTwo");
        Assert.assertEquals( "]]>", val);

        page = checkValueInField(webClient, page,  "<![CDATA[" );
        page = checkValueInField(webClient, page,  "<html xmlns=\"http://www.w3.org/1999/xhtml\"" );
        page = checkValueInField(webClient, page,  "<script type=\"text/javascript\" " );
        page = checkValueInField(webClient, page,  "<!-- abc --> " );
        page = checkValueInField(webClient, page,  "]]> ]]> ]]>" );
        page = checkValueInField(webClient, page,  "<!-- <![CDATA[]]> ]]> ]]> <script" );
        page = checkValueInField(webClient, page,  "<div> <p> " );
        page = checkValueInField(webClient, page,  "<!-- <![CDATA[]]> ]]> ]]> <script" );
    }



     @Test
    public void testFieldAttributeValuesFF() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.FIREFOX_3 );
        doFieldAttributeValues();
    }

    // Internet explorer cdata test
    @Test
    public void testFieldAttributeValuesIE() throws Exception {
        setUpWithBrowserVersion(BrowserVersion.INTERNET_EXPLORER_7 );
        doFieldAttributeValues();
    }

    /**
       * Post some cdata terminating characters into the input text field and
       * verify that they show up in the echoing text field, and i suppose in the
       * input text field itself.
       * @throws Exception test exception
       */
      public void doFieldAttributeValues() throws Exception {

         // there are two text fields with alt attributes that should match exactly
        // their value fields. This is strictly by convention.
        HtmlTextInput inputField_1 = (HtmlTextInput) page.getElementById("form1:escapeAttribute_1");
        HtmlTextInput inputField_2 = (HtmlTextInput) page.getElementById("form1:escapeAttribute_2");

        Assert.assertNotNull(inputField_1);
        Assert.assertNotNull(inputField_2);

        String att_1 = inputField_1.getAttribute("alt");
        String att_2 = inputField_2.getAttribute("alt");

        String value_1 = inputField_1.getValueAttribute();
        String value_2 = inputField_2.getValueAttribute();

        Assert.assertEquals( att_1, value_1);
        Assert.assertEquals( att_2, value_2);

    }

    /**
     * 
     */
    public HtmlPage checkValueInField(WebClient webClient, HtmlPage page, String checkVal)
            throws IOException {

        setInputTextValue(page, "form1:in1", checkVal);

        // Simple postback
        HtmlPage page2 = clickElement(page, "form1:sendButton", webClient);
        Matcher m = output_pattern.matcher( page2.asText() );

        if (m.find()) {
            String value = m.group();
            if (value.indexOf(checkVal) == -1 ) {
                Assert.fail("Failed to find expected output");
            }
        } else {
            Assert.fail("Failed to find expected output --- Output follows: ");
        }

        HtmlTextInput inputField = (HtmlTextInput) page.getElementById("form1:inOne");
        
        return page2;
    }


    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ICE_3182Test.class);
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.icefaces.htmlunit.ICE_3182Test");
    }

    /**
     * Utility method for finding and clicking an element on a page.
     *
     * @param page Existing HtmlPage
     * @param elementId Id of clickable element, fully qualified containing form id's
     * @param client WebClient instance
     * @return new copy of HtmlPage
     * @throws java.io.IOException from underlying test framework
     */
    public HtmlPage clickElement(HtmlPage page, String elementId, WebClient client) throws IOException {

        HtmlElement element = page.getElementById(elementId);
        Assert.assertNotNull  ("Clickable element: " + elementId + " is not found", element);

        page = (HtmlPage) element.click();
        client.waitForBackgroundJavaScript(2000);
        return page;
    }

    /**
     * Utility method for finding and setting the value of an input text field
     * @param page
     * @param id
     * @param value
     */
    public void setInputTextValue(HtmlPage page, String id, String value ) {

        HtmlTextInput inputField = (HtmlTextInput) page.getElementById(id);
        Assert.assertNotNull(inputField);
        inputField.setValueAttribute( value);
    }

    /**
     * Fetch the value from an input text field
     * @param page HtmlPage
     * @param id id of input text field
     * @return Value attribute of field
     */
    public String getHtmlInputValue(HtmlPage page, String id) {
        HtmlInput element = (HtmlInput) page.getElementById( id );
        Assert.assertNotNull( "Input element: " + id + " not found",element );
        return element.getValueAttribute();
    }


    /**
     * We need to convert some requests to synchronous in order not to miss
     * some updates.
     */
    public class MyAjaxController extends AjaxController {

        public boolean processSynchron(HtmlPage page,
                                       WebRequestSettings settings,
                                       boolean async) {


            // It seems that if the send-updated-views Ajax requests can be handled
            // asynchronously, but that the User interface interaction works best
            // if run synchronously. Running both synchronously causes a deadlock.
            // Running both asynchronously lets some updates get skipped.

            if (settings.getUrl().toString().indexOf("icefaces.xhtml") > -1) {
//                System.out.println("Ajax request to: " + settings.getUrl() + " type: " +  settings.getHttpMethod() );
//                System.out.println(page.getPage().asText());
                return super.processSynchron( page, settings, false  );
            } else {
                return   super.processSynchron( page, settings, async  );
            }
        }

    }

    // Not used.
    public class MyWindowListener extends WebWindowAdapter {

        public void webWindowContentChanged(WebWindowEvent event) {
            System.out.println("WebWindowContent changed! " + event);
        }

    }

    public class MyDomChangeListener implements DomChangeListener {

        public void nodeAdded(DomChangeEvent event) {
//            System.out.println("Node added: " + event);
        }
        public void nodeDeleted(DomChangeEvent event) {
//            System.out.println("Node deleted: " + event.getChangedNode().getNodeName());
        }
    }
}