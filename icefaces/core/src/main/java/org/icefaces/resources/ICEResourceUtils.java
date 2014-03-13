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

package org.icefaces.resources;

import org.icefaces.util.UserAgentContext;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 1/25/13
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ICEResourceUtils {
    public static final ResourceInfo BLANK_INFO = new ResourceInfo();

    public static ResourceInfo getResourceInfo(UserAgentContext uaContext,
                                               ICEResourceDependency inputDep,
                                               ICEResourceLibrary library) {
        ResourceInfo returnDep = null;

        if (inputDep != null) {
            if (inputDep.browserOverride().length > 0) {
                returnDep = new ResourceInfo(inputDep, library);

                for (ICEBrowserDependency override : inputDep.browserOverride()) {
                    if (uaContext.isBrowserType(override.browser())) {
                        returnDep = new ResourceInfo(override, library);
                        break;
                    }
                }
            } else if (uaContext.isBrowserType(inputDep.browser())) {
                returnDep = new ResourceInfo(inputDep, library);
            }

            // If indicated by blank info, skip adding a dependency for this annotation
            if (returnDep != null && returnDep.equals(BLANK_INFO)) returnDep = null;
        }

        return returnDep;
    }
}
