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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;

import org.icefaces.ace.util.Constants;

public class AutoUpdateCallbackPartialViewContext extends PartialViewContextWrapper {

    private PartialViewContext wrapped;
    private PartialResponseWriter writer = null;

    public AutoUpdateCallbackPartialViewContext(PartialViewContext wrapped) {
        this.wrapped = wrapped;
        if(isAjaxRequest()) {
            new DefaultRequestContext();    //initialize RequestContext instance
        }
    }
    
    @Override
    public PartialViewContext getWrapped() {
        return this.wrapped;
    }

    @Override
    public void setPartialRequest(boolean value) {
        getWrapped().setPartialRequest(value);
    }

    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        if (writer == null) {
            PartialResponseWriter parentWriter = getWrapped().getPartialResponseWriter();
            writer = new CallbackPartialResponseWriter(parentWriter);
        }

        return writer;
    }

    @Override
    public Collection<String> getRenderIds() {
        List<String> ids = new ArrayList<String>(getWrapped().getRenderIds());
        RequestContext requestContext = RequestContext.getCurrentInstance();
        Collection<String> autoUpdateIds = (Collection<String>) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get(Constants.AUTO_UPDATE);

        if(requestContext != null) {
            ids.addAll(requestContext.getPartialUpdateTargets());
        }

        if(autoUpdateIds != null) {
            ids.addAll(autoUpdateIds);
        }

        return ids;
    }

    @Override
    public boolean isAjaxRequest() {
        return getWrapped().isAjaxRequest();
    }
}
