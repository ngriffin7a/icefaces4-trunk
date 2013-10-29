/*
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 */
package org.icefaces.ace.util;

public class Constants {

    public static final String PARTIAL_REQUEST_PARAM = "javax.faces.partial.ajax";
    public static final String PARTIAL_UPDATE_PARAM = "javax.faces.partial.render";
    public static final String PARTIAL_PROCESS_PARAM = "javax.faces.partial.execute";
    public static final String PARTIAL_SOURCE_PARAM = "javax.faces.source";
    public static final String PARTIAL_BEHAVIOR_EVENT_PARAM = "javax.faces.behavior.event";

    public static final String THEME_PARAM = "org.icefaces.ace.theme";
    public static final String THEME_FORMS_PARAM = "org.icefaces.ace.themeForms";
    public static final String AUTO_UPDATE = "org.icefaces.ace.autoUpdate";

    public final static String VERSION = "2.1.0-b01";
    public final static String LIBRARY = "icefaces.ace";

    public final static String CUSTOM_EVENT = "CUSTOM_EVENT";
	
	/* ------------------------------- */
	/* --- imported from icemobile --- */
	/* ------------------------------- */
	
    public final static String IOS_SMART_APP_BANNER_KEY = "org.icemobile.iosSmartAppBanner";
    public final static String TEMP_DIR = "javax.servlet.context.tmpdir";
    public final static String ICEMOBILE_COOKIE_FORMAT = "org.icemobile.cookieformat";
    public final static String USER_AGENT_COOKIE = "com.icesoft.user-agent";
    public final static String CLOUD_PUSH_KEY = "iceCloudPushId";
    public final static String HEADER_ACCEPT = "Accept";
    public final static String HYPERBROWSER = "HyperBrowser";
    public final static String PROJECT_STAGE_PARAM = "org.icemobile.projectstage";
    
    //rendering standard items
    public final static String SUFFIX_WRAPPER = "_wrp";
    public final static String SUFFIX_HIDDEN = "_hidden";
    public final static String SPACE = " ";

    // rendering mouse vs touch events
    public static final String TOUCH_START_EVENT = "ontouchstart";
    public static final String CLICK_EVENT = "onclick";
}
