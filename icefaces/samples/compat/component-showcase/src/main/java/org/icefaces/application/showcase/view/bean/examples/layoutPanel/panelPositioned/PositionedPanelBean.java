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

package org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelPositioned;

import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Simple bean which holds a list of objects which is manipulated by the
 * position panel
 */
@ManagedBean(name = "panelPositioned")
@ViewScoped
public class PositionedPanelBean implements Serializable {

    private List people;

    public PositionedPanelBean() {

        // add some objects to the list
        people = new ArrayList(7);
        people.add(new PostionedPanelPerson("Mary Smith"));
        people.add(new PostionedPanelPerson("James Johnson"));
        people.add(new PostionedPanelPerson("Patricia Williams"));
        people.add(new PostionedPanelPerson("John Jones"));
        people.add(new PostionedPanelPerson("Linda Brown"));
        people.add(new PostionedPanelPerson("Robert Davis"));
        people.add(new PostionedPanelPerson("Barbara Miller"));
        resetRank();
    }


    private void resetRank() {
        for (int i = 0; i < people.size(); i++) {
            ((PostionedPanelPerson) people.get(i)).setRank(i + 1);
        }
    }


    public void changed(PanelPositionedEvent evt) {
        resetRank();
        if (evt.getOldIndex() >= 0) {
            ((PostionedPanelPerson) people.get(
                    evt.getIndex())).getEffect().setFired(false);
        }
    }

    public List getPeople() {
        return people;
    }

    public void setPeople(List people) {
        this.people = people;
    }

}
