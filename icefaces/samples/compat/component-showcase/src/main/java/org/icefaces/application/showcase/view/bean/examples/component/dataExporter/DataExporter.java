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

package org.icefaces.application.showcase.view.bean.examples.component.dataExporter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
@ManagedBean(name = "dataExporter")
@ViewScoped
public class DataExporter extends DataTableBase{
    private Effect changeEffect;
    private String type;

    public DataExporter() {
        changeEffect = new Highlight("#fda505");
        changeEffect.setFired(true);
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Effect getChangeEffect() {
        return changeEffect;
    }

    public void setChangeEffect(Effect changeEffect) {
        this.changeEffect = changeEffect;
    }
    
    public void typeChangeListener(ValueChangeEvent event){
        this.changeEffect.setFired(false);
    }
}
