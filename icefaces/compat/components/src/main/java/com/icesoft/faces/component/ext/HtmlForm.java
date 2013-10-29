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

package com.icesoft.faces.component.ext;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.IceExtended;
import com.icesoft.faces.component.ext.taglib.Util;
import org.icefaces.impl.component.UISeriesBase.RowEvent;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.component.ActionSource;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;

@ICEResourceDependencies({
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
	@ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class HtmlForm
        extends javax.faces.component.html.HtmlForm
        implements IceExtended, ActionSource {

    public static final String COMPONENT_TYPE = "com.icesoft.faces.HtmlForm";
    public static final String RENDERER_TYPE = "com.icesoft.faces.Form";
    private static final boolean DEFAULT_PARITALSUBMIT = false;
    private Boolean partialSubmit = null;
    private String enabledOnUserRole = null;
    private String renderedOnUserRole = null;
    private String styleClass = null;
    private MethodBinding action = null;
    private MethodBinding actionListener = null;
    private boolean immediate = false;
    private boolean immediateSet = false;

    public HtmlForm() {
        super();
        setRendererType(RENDERER_TYPE);
    }

    /**
     * <p>Set the value of the <code>partialSubmit</code> property.</p>
     */
    public void setPartialSubmit(boolean partialSubmit) {
        this.partialSubmit = Boolean.valueOf(partialSubmit);
    }


    /**
     * <p>Return the value of the <code>partialSubmit</code> property.</p>
     */
    public boolean getPartialSubmit() {
        if (partialSubmit != null) {
            return partialSubmit.booleanValue();
        }
        ValueBinding vb = getValueBinding("partialSubmit");
        Boolean boolVal =
                vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return boolVal != null ? boolVal.booleanValue() : DEFAULT_PARITALSUBMIT;
    }

    /**
     * <p>Set the value of the <code>enabledOnUserRole</code> property.</p>
     */
    public void setEnabledOnUserRole(String enabledOnUserRole) {
        this.enabledOnUserRole = enabledOnUserRole;
    }


    /**
     * <p>Return the value of the <code>enabledOnUserRole</code> property.</p>
     */
    public String getEnabledOnUserRole() {
        if (enabledOnUserRole != null) {
            return enabledOnUserRole;
        }
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }


    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }


    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }

    private String autocomplete;

    /**
     * <p>Set the value of the <code>autocomplete</code> property.</p>
     */
    public void setAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;
    }

    /**
     * <p>Return the value of the <code>autocomplete</code> property.</p>
     */
    public String getAutocomplete() {
        if (autocomplete != null) {
            return autocomplete;
        }
        ValueBinding vb = getValueBinding("autocomplete");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                styleClass,
                CSS_DEFAULT.FORM_STYLE_CLASS,
                "styleClass");
    }

    /**
     * <p>Gets the state of the instance as a <code>Serializable</code>
     * Object.</p>
     */
    public Object saveState(FacesContext context) {
        Object values[] = new Object[10];
        values[0] = super.saveState(context);
        values[1] = partialSubmit;
        values[2] = enabledOnUserRole;
        values[3] = renderedOnUserRole;
        values[4] = autocomplete;
        values[5] = styleClass;
        values[6] = saveAttachedState(context, action);
        values[7] = saveAttachedState(context, actionListener);
        values[8] = immediate ? Boolean.TRUE : Boolean.FALSE;
        values[9] = immediateSet ? Boolean.TRUE : Boolean.FALSE;        
        return ((Object) (values));
    }

    /**
     * <p>Perform any processing required to restore the state from the entries
     * in the state Object.</p>
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        partialSubmit = (Boolean) values[1];
        enabledOnUserRole = (String) values[2];
        renderedOnUserRole = (String) values[3];
        autocomplete = (String) values[4];
        styleClass = (String) values[5];
        action = (MethodBinding) restoreAttachedState(context, values[6]);
        actionListener =
                (MethodBinding) restoreAttachedState(context, values[7]);
        immediate = ((Boolean) values[8]).booleanValue();
        immediateSet = ((Boolean) values[9]).booleanValue();        
    }
    
    public void queueEvent(FacesEvent event) {
        FacesEvent tempEvent = event;
        RowEvent rowEvent = null;
        
        if (event instanceof RowEvent) {
            rowEvent = (RowEvent)event;
            //support for nested UISeries components
            while(true) {
                //if its a rowEvent, then get the actual faces event
                if (rowEvent instanceof RowEvent) {
                    tempEvent = rowEvent.getFacesEvent();
                    if (tempEvent instanceof RowEvent){
                        rowEvent = (RowEvent)tempEvent;
                    } else {
                        break;
                    }
                }
            }            
        }
        


        //now check if its an action event
        if (tempEvent instanceof ActionEvent) {

            //see if the event source has action or actionListener defined on it
            Object action = tempEvent.getComponent().getAttributes().get("action");
            Object listener = tempEvent.getComponent().getAttributes().get("actionListener");

            //if neither action nor actionListener is defined on the source component AND the form
            //component defines one then it means that form should take care of it
            if ((action == null && listener == null) && (getActionListener() != null || getAction() != null)) {
                 
                 //now create a new ActionEvent, so the form component can be set 
                 //as an event source
                 FacesEvent newEvent = new ActionEvent(this);
                 
                 //if its a rowEvent, then swap the actual FacesEvent, this will 
                 //give us a benefit of dealing with UISeries event, and form.broadcast 
                 //will be called in a proper iteration
                 if (rowEvent != null) {
                     rowEvent.setFacesEvent(newEvent);
                 } else {
                     //this component is not inside any UIData, so just queue 
                     //actionEvent belongs to form
                     super.queueEvent(newEvent);
                     return;
                 }
            }
        }
        //it means that either the event was not an actionEvent or if it was an 
        //actionEvent but the component has defined an action or actionListener as well.
        super.queueEvent(event);
    } 
    
    //this should be called only for those actionEvents which are not handled by their components. 
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        try {
            super.broadcast(event);
        } catch (IllegalArgumentException e) {
        }
        if ((event instanceof ActionEvent)) {
            ActionEvent actionEvent = (ActionEvent) event;
            try {
                MethodBinding actionListenerBinding = getActionListener();
                if (actionListenerBinding != null) {
                    actionListenerBinding.invoke(
                        getFacesContext(), new Object[]{actionEvent});
                }
            } catch (EvaluationException e) {
                Throwable cause = e.getCause();
                if (cause != null &&
                    cause instanceof AbortProcessingException) {
                    throw(AbortProcessingException) cause;
                } else {
                    throw e;
                }
            }

            ActionListener listener =
                    getFacesContext().getApplication().getActionListener();
            if (listener != null) {
                listener.processAction((ActionEvent) event);
            }
        }

    }    
    
    
    /**
     * <p>Set the value of the <code>action</code> property.</p>
     */
    public void setAction(MethodBinding action) {
        this.action = action;
    }

    /**
     * <p>Return the value of the <code>action</code> property.</p>
     */
    public MethodBinding getAction() {
        return action;
    }

    /* (non-Javadoc)
     * @see javax.faces.component.ActionSource#setActionListener(javax.faces.el.MethodBinding)
     */
    public void setActionListener(MethodBinding actionListener) {
        this.actionListener = actionListener;
    }

    /* (non-Javadoc)
     * @see javax.faces.component.ActionSource#getActionListener()
     */
    public MethodBinding getActionListener() {
        return actionListener;
    }

    /* (non-Javadoc)
     * @see javax.faces.component.ActionSource#addActionListener(javax.faces.event.ActionListener)
     */
    public void addActionListener(ActionListener listener) {
        addFacesListener(listener);
    }

    /* (non-Javadoc)
     * @see javax.faces.component.ActionSource#getActionListeners()
     */
    public ActionListener[] getActionListeners() {
        return (ActionListener[]) getFacesListeners(ActionListener.class);
    }

    /* (non-Javadoc)
     * @see javax.faces.component.ActionSource#removeActionListener(javax.faces.event.ActionListener)
     */
    public void removeActionListener(ActionListener listener) {
        removeFacesListener(listener);
    }
    
    /**
     * <p>Return the value of the <code>immediate</code> property.</p>
     */
    public boolean isImmediate() {
        if (this.immediateSet) {
            return (this.immediate);
        }
        ValueBinding vb = getValueBinding("immediate");
        if (vb != null) {
            return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
        } else {
            return (this.immediate);
        }
    }

    /**
     * <p>Set the value of the <code>immediate</code> property.</p>
     */
    public void setImmediate(boolean immediate) {
        if (immediate != this.immediate) {
            this.immediate = immediate;
        }
        this.immediateSet = true;

    }    

}


