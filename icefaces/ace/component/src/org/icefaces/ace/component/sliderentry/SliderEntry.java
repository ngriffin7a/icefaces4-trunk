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

/*
 * Generated, Do Not Modify
 */

package org.icefaces.ace.component.sliderentry;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.component.UINamingContainer;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import javax.faces.event.AbortProcessingException;

import org.icefaces.component.Focusable;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;

import org.icefaces.ace.util.Utils;
import org.icefaces.impl.util.Util;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

public class SliderEntry extends SliderEntryBase implements Focusable {

	private static final String OPTIMIZED_PACKAGE = "org.icefaces.ace.component.";

    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
    }
	
    public void broadcast(FacesEvent event)
    throws AbortProcessingException {
        super.broadcast(event);

        //event was fired by me
        if (event != null && event instanceof ValueChangeEvent) {
            
            //To keep it simple slider uses the broadcast to update value, so it doesn't
            //have to keep submitted value
            
            //1. update the value
            ValueExpression ve = getValueExpression("value");
            if (ve != null) {
                try {
                    setValue((Integer)((ValueChangeEvent)event).getNewValue());
                } catch (ELException ee) {
                    ee.printStackTrace();
                }
            } else {
                setValue((Integer)((ValueChangeEvent)event).getNewValue());
            }
            //invoke a valuechange listener if any
            MethodExpression method = getValueChangeListener();
            if (method != null) {
                method.invoke(getFacesContext().getELContext(), new Object[]{event});
            }
        }
    }
	
    public void queueEvent(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        }
        else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
        super.queueEvent(event);
    }

	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

    public String getFocusedElementId() {
        return getClientId() + "_handle";
    }
}