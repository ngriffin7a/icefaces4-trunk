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

package org.icefaces.application.showcase.view.bean.examples.component.menuBar;

import com.icesoft.faces.component.menubar.MenuItem;
import org.icefaces.application.showcase.util.FacesUtils;
import org.icefaces.application.showcase.view.bean.BaseBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>The MenuBarBean class determines which menu item fired the ActionEvent and
 * stores the modified id information in a String. MenuBarBean also controls the
 * orientation of the Menu Bar.</p>
 */
@ManagedBean(name = "menuBar")
@ViewScoped
public class MenuBarBean extends BaseBean {

    // records which menu item fired the event
    private String actionFired;

    // records the param value for the menu item which fired the event
    private String param;

    // orientation of the menubar ("Horizontal" or "Vertical")
    private String orientation = "Horizontal";

    /**
     * Always use explicit ids with the MenuItem components you create with for ice:menuItems, and create
     * them in your bean constructor, so that each call to the bean getter method will return the same components.
     */
    private transient List menuItems;

    public MenuBarBean() {
    }

    /**
     * Get the param value for the menu item which fired the event.
     *
     * @return the param value
     */
    public String getParam() {
        return param;
    }

    /**
     * Set the param value.
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * Get the modified ID of the fired action.
     *
     * @return the modified ID
     */
    public String getActionFired() {
        return actionFired;
    }

    /**
     * Identify the ID of the element that fired the event and return it in a
     * form suitable for display.
     *
     * @param e the event that fired the listener
     */
    public void primaryListener(ActionEvent e) {

        actionFired = ((UIComponent) e.getSource())
                .getClientId(FacesContext.getCurrentInstance());
        param = FacesUtils.getRequestParameter("myParam");

        // highlight server side backing bean values. 
        valueChangeEffect.setFired(false);

    }

    /**
     * Get the orientation of the menu ("horizontal" or "vertical")
     *
     * @return the orientation of the menu
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Set the orientation of the menu ("horizontal" or "vertical").
     *
     * @param orientation the new orientation of the menu
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public List getMenuItems() {

        if( menuItems == null ){
            menuItems = new ArrayList();

            MenuItem topLevel1 = new MenuItem();
            topLevel1.setId("topLevel1");
            topLevel1.setValue("File");

            MenuItem topLevel2 = new MenuItem();
            topLevel2.setId("topLevel2");
            topLevel2.setValue("Edit");

            MenuItem topLevel3 = new MenuItem();
            topLevel3.setId("topLevel3");
            topLevel3.setValue("View");

            menuItems.add(topLevel1);
            menuItems.add(topLevel2);
            menuItems.add(topLevel3);

            MenuItem sub1_1 = new MenuItem();
            sub1_1.setId("sub1_1");
            sub1_1.setIcon("/images/menu/open.gif");
            sub1_1.setValue("Open");
            MenuItem sub1_2 = new MenuItem();
            sub1_2.setId("sub1_2");
            sub1_2.setValue("Close");
            MenuItem sub1_3 = new MenuItem();
            sub1_3.setId("sub1_3");
            sub1_3.setIcon("/images/menu/recent.gif");
            sub1_3.setValue("Recent");

            topLevel1.getChildren().add(sub1_1);
            topLevel1.getChildren().add(sub1_2);
            topLevel1.getChildren().add(sub1_3);

            MenuItem sub1_3_1 = new MenuItem();
            sub1_3_1.setId("sub1_3_1");
            sub1_3_1.setValue("File 1");
            MenuItem sub1_3_2 = new MenuItem();
            sub1_3_2.setId("sub1_3_2");
            sub1_3_2.setValue("File 2");
            MenuItem sub1_3_3 = new MenuItem();
            sub1_3_3.setId("sub1_3_3");
            sub1_3_3.setValue("File 3");

            sub1_3.getChildren().add(sub1_3_1);
            sub1_3.getChildren().add(sub1_3_2);
            sub1_3.getChildren().add(sub1_3_3);
        }

        return menuItems;
    }

}