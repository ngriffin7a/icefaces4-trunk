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

package org.icefaces.tutorials.ace;

import org.icefaces.ace.model.table.RowStateMap;
import org.icefaces.ace.util.collections.Predicate;

import javax.faces.application.FacesMessage;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.util.ArrayList;

public class ConditionalRowMessagesBean {
    ArrayList<Task> data = new ArrayList<Task>() {{
        int i = 0;
        while (i != 10)
            add(new Task(i++));
    }};

    RowIdHasMessagesPredicate predicate = new RowIdHasMessagesPredicate();

    UIOutput idOutput;

    RowStateMap stateMap;
    

    public ArrayList<Task> getData() {
        return data;
    }

    public void setData(ArrayList<Task> data) {
        this.data = data;
    }

    public UIOutput getIdOutput() {
        return idOutput;
    }

    public void setIdOutput(UIOutput idOutput) {
        this.idOutput = idOutput;
    }

    public RowIdHasMessagesPredicate getPredicate() {
        return predicate;
    }

    public void setPredicate(RowIdHasMessagesPredicate predicate) {
        this.predicate = predicate;
    }

    public RowStateMap getStateMap() {
        return stateMap;
    }

    public void setStateMap(RowStateMap stateMap) {
        this.stateMap = stateMap;
    }

    public class RowIdHasMessagesPredicate implements Predicate {
        public boolean evaluate(Object o) {
            return FacesContext.getCurrentInstance().
                    getMessageList(idOutput.getClientId()).size() > 0;
        }
    }



    public void validateRows(boolean even) {
        for (Object o : stateMap.getSelected()) {
            Task t = (Task)o;
            int r = ((Task) o).getId() % 2;
            if (!((even && r == 0) || (!even && r == 1))) {
                String cid = idOutput.getClientId();
                String rowId = cid.substring(0, cid.lastIndexOf(NamingContainer.SEPARATOR_CHAR)+1);
                rowId += (t.getId()) + cid.substring(cid.lastIndexOf(NamingContainer.SEPARATOR_CHAR));
                
                String message = even ? "The id of this row is not even."
                                      : "The id of this row is not odd.";

                FacesContext.getCurrentInstance()
                        .addMessage(rowId, new FacesMessage(message));
            }                
        }
    }
}
