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

package org.icefaces.application.showcase.view.bean.examples.component.rowSelector;

import org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase;
import org.icefaces.application.showcase.model.entity.Employee;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.icesoft.faces.component.ext.RowSelector;
import com.icesoft.faces.component.ext.RowSelectorEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 * <p>The RowSelectController is responsible for handling the RowSelectorEvent
 * that is fire from the rowSelector component.  This simple class keeps
 * a list of selected.  A user can also change the selection more of the
 * row Selector component. </p>
 *
 * @since 1.7
 */
@ManagedBean
@ViewScoped
public class RowSelectController extends DataTableBase {

    // list of selected employees
    protected ArrayList selectedEmployees;

    // flat to indicate multiselect row enabled.
    protected String multiRowSelect = "Single";
    protected boolean multiple;
    protected boolean enhancedMultiple;
    public RowSelectController() {
        selectedEmployees = new ArrayList();
    }

    /**
     * SelectionListener bound to the ice:rowSelector component.  Called
     * when a row is selected in the UI.
     *
     * @param event from the ice:rowSelector component
     */
    public void rowSelectionListener(RowSelectorEvent event) {
        // clear our list, so that we can build a new one
        selectedEmployees.clear();
        
        /* If application developers rely on validation to control submission of the form or use the result of
           the selection in cascading control set up the may want to defer procession of the event to
           INVOKE_APPLICATION stage by using this code fragment
		    if (event.getPhaseId() != PhaseId.INVOKE_APPLICATION) {
		       event.setPhaseId( PhaseId.INVOKE_APPLICATION );
		       event.queue();
		       return;
		    }

         */

        // build the new selected list
        Employee employee;
        for(int i = 0, max = employees.size(); i < max; i++){
        employee = (Employee)employees.get(i);
            if (employee.isSelected()) {
                selectedEmployees.add(employee);
            }
        }
        /* If application developers do not rely on validation and want to bypass UPDATE_MODEL and
           INVOKE_APPLICATION stages, they may be able to use the following statement:
           FacesContext.getCurrentInstance().renderResponse();
           to send application to RENDER_RESPONSE phase shortening the app. cycle
         */
    }

    /**
     * Clear the selection list if going from multi select to single select.
     *
     * @param event jsf action event.
     */
    public void changeSelectionMode(ValueChangeEvent event) {
        
    	String newValue = event.getNewValue() != null ? event.getNewValue().toString() : null; 
        multiple = false;
        enhancedMultiple = false;
        if ("Single".equals(newValue)){
            selectedEmployees.clear();

            // build the new selected list
            Employee employee;
            for(int i = 0, max = employees.size(); i < max; i++){
                employee = (Employee)employees.get(i);
                employee.setSelected(false);
            }
        } else if ("Multiple".equals(newValue)){
            multiple = true;
        } else if ("Enhanced Multiple".equals(newValue)){
            enhancedMultiple = true;
        }
    }

    public ArrayList getSelectedEmployees() {
        return selectedEmployees;
    }

    public void setSelectedEmployees(ArrayList selectedEmployees) {
        this.selectedEmployees = selectedEmployees;
    }

    public String getMultiRowSelect() {
        return multiRowSelect;
    }

    /**
     * Sets the selection more of the rowSelector.
     *
     * @param multiRowSelect true indicates multi-row select and false indicates
     *                       single row selection mode.
     */
    public void setMultiRowSelect(String multiRowSelect) {
        this.multiRowSelect = multiRowSelect;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isEnhancedMultiple() {
        return enhancedMultiple;
    }

    public void setEnhancedMultiple(boolean enhancedMultiple) {
        this.enhancedMultiple = enhancedMultiple;
    }
    
    public void jsListener(ActionEvent event) {
        Map parameter = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parameter.containsKey("ice.event.keycode")) {
            boolean shiftKey = "true".equals(parameter.get("ice.event.shift"));
            boolean E = "69".equals(parameter.get("ice.event.keycode"));
            boolean DEL = "46".equals(parameter.get("ice.event.keycode"));
            boolean ESC = "27".equals(parameter.get("ice.event.keycode"));  
            
            if (shiftKey && E) {
                editRecords(true);
            } else if (ESC) {
                editRecords(false);
            }else if (DEL) {
                deleteRecords();
            }            
        }
    }

    private void editRecords(boolean edit) {
        Employee employee;
        for(int i = 0, max = employees.size(); i < max; i++){
            employee = (Employee)employees.get(i);
            if (!edit) {
                employee.setEdit(edit);
            } else {
                if (employee.isSelected()) 
                    employee.setEdit(edit);
            }
        }   
    }

    private void deleteRecords() {
        Employee employee;
        List removeList = new ArrayList();
        for(int i = 0, max = employees.size(); i < max; i++){
            employee = (Employee)employees.get(i);
            if (employee.isSelected()) {
                removeList.add(employee);

            }
        }
        Iterator iterator = removeList.iterator();
        while (iterator.hasNext()) {
            employees.remove((Employee)iterator.next());
        }
    }
}
