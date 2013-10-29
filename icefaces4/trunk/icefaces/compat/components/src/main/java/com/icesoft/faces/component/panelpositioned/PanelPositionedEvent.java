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

package com.icesoft.faces.component.panelpositioned;

import javax.faces.component.UIComponent;
import javax.faces.el.MethodBinding;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

import com.icesoft.faces.component.panelseries.UISeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Event fired by positioned panel events
 */
public class PanelPositionedEvent extends FacesEvent{

    private MethodBinding listener;
    private MethodBinding beforeChangedListener;    
    private int index;
    private int oldIndex = -1;
    private int type;
    private List oldList;
    private List newList;
    private boolean cancel;

    public static int TYPE_ADD = 1;
    public static int TYPE_REMOVE = 2;
    public static int TYPE_MOVE = 3;
    private PhaseId phaseId = PhaseId.UPDATE_MODEL_VALUES;

    public PanelPositionedEvent(UIComponent uiComponent, MethodBinding listener,
                                int eventType, int index, int oldIndex, List oldList, List newList) {
        super(uiComponent);
        this.listener = listener;
        this.type = eventType;
        this.index = index;
        this.oldIndex = oldIndex;
        this.oldList = oldList;
        this.newList = newList;

        
    }

    public PanelPositionedEvent(UIComponent uiComponent, MethodBinding listener,
            int eventType, int index, int oldIndex, List oldList, List newList, MethodBinding beforeChangedListener) {
		this(uiComponent, listener, eventType, index, oldIndex, oldList, newList);
		this.beforeChangedListener = beforeChangedListener;
    }
    
    public PhaseId getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(PhaseId phaseId) {
        this.phaseId = phaseId;
    }



    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }

    public void processListener(FacesListener facesListener) {
       
    }

    public void process(){
        try {
            if (((UISeries)this.source).getValue() instanceof List) {
               oldList.clear();
               oldList.addAll(newList);               
           } else if (((UISeries)this.source).getValue() instanceof Object[]) {
               Object[] newVal = (Object[])((UISeries)this.source).getValue();
               for (int i = 0; i < newVal.length; i++) {
                   newVal[i] = newList.get(i);
               }
               ((PanelPositioned)this.source).setArrayValue(newVal);
           }

        } catch (Exception e) {}
    }

    public MethodBinding getListener() {
        return listener;
    }

    public void setListener(MethodBinding listener) {
        this.listener = listener;
    }
    
    public MethodBinding getBeforeChangedListener() {
        return beforeChangedListener;
    }

    public void setBeforeChangedListener(MethodBinding beforeChangedListener) {
        this.beforeChangedListener = beforeChangedListener;
    }    

    /**
     * Index added, removed or changed
     *
     * @return int index
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * The other index when the event type is MOVE. Otherwise its -1.
     *
     * @return int oldIndex
     */
    public int getOldIndex() {
        return oldIndex;
    }

    public void setOldIndex(int oldIndex) {
        this.oldIndex = oldIndex;
    }

    /**
     * Type of event cna be Added, Removed or changed
     *
     * @return int type
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
   
    public void cancel() {
    	this.cancel = true;
    }
    
    public boolean isCanceled() {
    	return cancel;
    }
}
