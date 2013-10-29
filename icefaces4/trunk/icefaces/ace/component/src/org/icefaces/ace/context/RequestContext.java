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
package org.icefaces.ace.context;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class RequestContext {

    private static ThreadLocal<RequestContext> instance = new ThreadLocal<RequestContext>() {

        @Override
        protected RequestContext initialValue() {
            return null;
        }
    };

    public static RequestContext getCurrentInstance() {
        return instance.get();
    }

    protected static void setCurrentInstance(RequestContext requestContext) {
        if (requestContext == null) {
            instance.remove();
        } else {
            instance.set(requestContext);
        }
    }

    @Deprecated
    public abstract boolean isAjaxRequest();

    public abstract void release();

    public abstract void addCallbackParam(String name, Object value);

    public abstract Map<String, Object> getCallbackParams();

    public abstract void addPartialUpdateTarget(String name);

    public abstract void addPartialUpdateTargets(Collection<String> collection);

    public abstract List<String> getPartialUpdateTargets();
}
