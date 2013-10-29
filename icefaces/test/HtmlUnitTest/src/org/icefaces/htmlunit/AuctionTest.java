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

import java.util.Date;
import java.io.IOException;


/**
 * This Test class is experimenting with the icefaces auction application
 */
public class AuctionTest extends TestCase {

    //    @Test
    /**
     * Test loading the page and let the update polling run
     */
//    public void testHomePage() throws Exception {
//        final WebClient webClient = new WebClient( BrowserVersion.FIREFOX_3 );
//        HtmlPage page = webClient.getPage("http://localhost:8080/auction");
//        System.out.println("Page title = " + page.getTitleText() );
//
//        System.out.println("Page contents: " + page.getPage().asText() );
//        webClient.setAjaxController(new MyAjaxController());
//        page.addDomChangeListener( new MyDomChangeListener() );
//
//        // Three minutes of updates please
//        webClient.waitForBackgroundJavaScript(180000);
//    }

    @Test
    /**
     * Fetch the main page and enter into a bidding loop
     */
    public void testBidding() throws Exception {
        final WebClient webClient = new WebClient( BrowserVersion.FIREFOX_3 );
        HtmlPage page = webClient.getPage("http://localhost:8080/auction");
        System.out.println("Page title = " + page.getTitleText() );

        System.out.println("Page contents: " + page.getPage().asText() );
        webClient.setAjaxController(new MyAjaxController());
        page.addDomChangeListener( new MyDomChangeListener() );

        // Enable the bidding field
        clickElement( page, "iceForm:iceTable:0:image_button_bid", webClient);

        for (int idx = 0; idx < 10; idx ++ ) {
            String  bidAmount = getHtmlInputValue(page, "iceForm:iceTable:0:item_localBid");
            float newBid = Float.parseFloat( bidAmount  ) + 100.0f;

            System.out.println("Current Bid price: " + bidAmount + " raising to: " + newBid );
            setInputTextValue(page,  "iceForm:iceTable:0:item_localBid", Float.toString( newBid ));

            page = clickElement( page,  "iceForm:iceTable:0:image_button_bid_accept", webClient);
            bidAmount = getHtmlInputValue(page, "iceForm:iceTable:0:item_localBid");
        }
    }


    @Test
    /**
     * Test enabling the chat section
     */
    public void testChatEnable() throws Exception {

        final WebClient webClient = new WebClient( BrowserVersion.INTERNET_EXPLORER_7);
        HtmlPage page = webClient.getPage("http://localhost:8080/auction");
        System.out.println("Page title = " + page.getTitleText() );

        webClient.setAjaxController(new MyAjaxController());
        page.addDomChangeListener( new MyDomChangeListener() );

        // Check to see if the chat log is enabled
        HtmlElement pageLog = page.getElementById( "chat:pageLog");
        Assert.assertNull("Log of chat transcripts should not be visible",  pageLog );

        page = clickElement( page,  "chat:join_chat_button", webClient );

        pageLog = page.getElementById( "chat:pageLog" );
        assertNotNull( "Log of chat transcripts should now be visible", pageLog );
    }

    @Test
    /**
     * Test enabling the chat section and making a comment
     */
    public void testChatEnableSpeak() throws Exception {

        final WebClient webClient = new WebClient( BrowserVersion.FIREFOX_3 );
        HtmlPage page = webClient.getPage("http://localhost:8080/auction");
        System.out.println("Page title = " + page.getTitleText() );

        webClient.setAjaxController(new MyAjaxController());
        page.addDomChangeListener( new MyDomChangeListener() );

        // Check to see if the chat log is enabled

        HtmlElement pageLog = page.getElementById( "chat:pageLog");
        Assert.assertNull("Log of chat transcripts should not be visible",  pageLog );

        setInputTextValue(page, "chat:text_nickname_chat", "HtmlUnit user");
        page = clickElement(page, "chat:join_chat_button", webClient  );

        pageLog = page.getElementById( "chat:pageLog" );
        assertNotNull( "Log of chat transcripts should now be visible", pageLog );


        for (int idx = 0; idx < 5;  idx ++ ) {
            setInputTextValue(page, "chat:messageIn","Test Message: " + idx + "  at: " + new Date(System.currentTimeMillis() ));
            page = clickElement(page, "chat:button_send_message", webClient);
        }
    }



    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(AuctionTest.class);
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.icefaces.htmlunit.AuctionTest");
    }

    /**
     * Utility method for finding and clicking an element on a page.
     *
     * @param page Existing HtmlPage
     * @param elementId Id of clickable element, fully qualified containing form id's
     * @param client WebClient instance
     * @return new copy of HtmlPage
     * @throws IOException from underlying test framework
     */
    protected HtmlPage clickElement(HtmlPage page, String elementId, WebClient client) throws IOException {

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
    protected void setInputTextValue(HtmlPage page, String id, String value ) {

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
    protected String getHtmlInputValue(HtmlPage page, String id) {
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

            if (settings.getUrl().toString().indexOf("auction.jsf") > -1) {
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
