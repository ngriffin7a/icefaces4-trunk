/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.thumbnail;

import org.icefaces.mobi.util.MobiJSFUtils;
import org.icefaces.util.ClientDescriptor;

import javax.faces.component.StateHelper;
import java.util.HashMap;
import java.util.Map;

public class Thumbnail extends ThumbnailBase {

    public static final String CSS_CLASS = "mobi-thumb";
    public static final String CSS_DONE_CLASS = "mobi-thumb-done";
    public static final String FOR_MSG = "for component was not detected. ";

    private String baseClass;
    private String mFor;
    private boolean disabled = false;

    public ClientDescriptor getClient() {
         return MobiJSFUtils.getClientDescriptor();
    }
    public boolean isDisabled(){
        return disabled;
    }
    public void setDisabled(boolean disabled){
        this.disabled=disabled;
    }
    public void setBaseClass(String cSSBase){
        if (cSSBase !=null){
            this.baseClass=cSSBase;
        }
    }

    public String getBaseClass() {
     /*   if ((null != mFor) && MobiJSFUtils.uploadInProgress(mFor))  {
            return IThumbnail.CSS_DONE_CLASS;
        }
        return IThumbnail.CSS_CLASS; */
        return this.baseClass;
    }

    public String getMFor() {
        String retVal = null;
        StateHelper sh = getStateHelper();
		String valuesKey = "mFor"+"_rowValues";
	    Map clientValues = (Map) sh.get(valuesKey);
	    boolean mapNoValue = false;
		if (clientValues != null) {
			String clientId = getClientId();
			if (clientValues.containsKey( clientId ) ) {
				retVal = (String) clientValues.get(clientId);
			} else {
				mapNoValue=true;
			}
		}
		if (mapNoValue || clientValues == null ) {
			String defaultKey = "mFor" + "_defaultValues";
			Map defaultValues = (Map) sh.get(defaultKey);
			if (defaultValues != null) {
				if (defaultValues.containsKey("defValue" )) {
					retVal = (String) defaultValues.get("defValue");
				}
			}
		}
		return retVal;
    }

    public void setMFor(String mFor) {
		StateHelper sh = getStateHelper();
		String clientId = getClientId();
		String valuesKey = "mFor" + "_rowValues";
		Map clientValues = (Map) sh.get(valuesKey);
		if (clientValues == null) {
			clientValues = new HashMap();
		}
		if (mFor== null) {
			clientValues.remove(clientId);
		} else {
			clientValues.put(clientId, mFor);
		}
	    //Always re-add the delta values to the map. JSF merges the values into the main map
	    //and values are not state saved unless they're in the delta map.
		sh.put(valuesKey, clientValues);

    }
}
