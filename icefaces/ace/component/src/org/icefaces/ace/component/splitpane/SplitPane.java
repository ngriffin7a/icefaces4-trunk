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

package org.icefaces.ace.component.splitpane;

import org.icefaces.ace.util.Utils;
import org.icefaces.ace.util.ClientDescriptor;

public class SplitPane extends SplitPaneBase implements ISplitPane {

    public static final String SPLITPANE_BASE = "mobi-splitpane" ;
    public static final String SPLITPANE_NONSCROLL = "mobi-splitpane-nonScrollable";
    public static final String SPLITPANE_SCROLLABLE = "mobi-splitpane-scrollable";
    public static final String SPLITPANE_DIVIDER = "mobi-splitpane-divider";


   // facet names that define the optionally defined layouts
    public static final String LEFT_FACET = "left";
    public static final String RIGHT_FACET = "right";

    public ClientDescriptor getClient() {
         return Utils.getClientDescriptor();
    }
	
	public String getClientIdX() {
		return super.getClientId();
	}
}
