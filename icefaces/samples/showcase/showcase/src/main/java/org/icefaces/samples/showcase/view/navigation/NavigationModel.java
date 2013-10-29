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

package org.icefaces.samples.showcase.view.navigation;

import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.metadata.context.Menu;
import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.util.EnvUtils;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = NavigationModel.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class NavigationModel implements Serializable {

    public static final String BEAN_NAME = "navigationModel";
    
    // TODO NAVIGATION controller should define this from meta data.
    public static final String DEFAULT_MENU = AceMenu.BEAN_NAME;
    public static final String GROUP_PARAM = "grp";
    public static final String EXAMPLE_PARAM = "exp";

    public static final String GROUP_KEY = "org.icefaces.samples.showcase.group";
    public static final String EXAMPLE_KEY = "org.icefaces.samples.showcase.example";
    public static final String DEFAULT_VIEW_ID = "/showcase.jsf";
    public static final int DEFAULT_MENU_TAB_INDEX = 0;
    // references to resolved menu and examples.
    private Menu currentComponentGroup;
    private ComponentExampleImpl currentComponentExample;
    // param for url
    private String componentGroup;
    private String componentExample;
    
    //if set to true Source code panel in UI will stay collapsed
    private boolean sourceCodeToggleStatus = true;
    //if set to false page will be displayed as Suite Overview Page (only description will be shown in the example area)
    private boolean renderAsExample = true;
    //saves selected tab number between redirects (if selected tab number is not saved, accordion menu will reset itself to the default index of 0)
    private int activePaneIndex = DEFAULT_MENU_TAB_INDEX;

    private String theme;
    
    public NavigationModel() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        //If we are running in a portlet, we need to set the default group and example as declared
        //in the portlet.xml init parameters.
        if(EnvUtils.instanceofPortletRequest(ec.getRequest())){

            //Get the params for the group and the example we should start with.  The portlet bridge
            //makes these accessible via standard JSF API calls.
            String group = ec.getInitParameter(GROUP_KEY);
            String example = ec.getInitParameter(EXAMPLE_KEY);

            //Set the group(menu) we should be displaying
            setCurrentComponentGroup((Menu) FacesUtils.getManagedBean(NavigationModel.DEFAULT_MENU));
            setComponentGroup(NavigationModel.DEFAULT_MENU);

            //Determine the default example in the group and set that as well
//            String beanName = getCurrentComponentGroup().getDefaultExample().getExampleBeanName();
            ComponentExampleImpl cei = (ComponentExampleImpl) FacesUtils.getManagedBean(example);
            setCurrentComponentExample(cei);
            setComponentExample(example);
        }
    }
    
    public Menu getCurrentComponentGroup() {
        return currentComponentGroup;
    }

    public void setCurrentComponentGroup(Menu currentComponentGroup) {
        this.currentComponentGroup = currentComponentGroup;
    }

    public ComponentExampleImpl getCurrentComponentExample() {
        return currentComponentExample;
    }

    public void setCurrentComponentExample(ComponentExampleImpl currentComponentExample) {
        this.currentComponentExample = currentComponentExample;
    }

    public String getComponentGroup() {
        return componentGroup;
    }

    public void setComponentGroup(String componentGroup) {
        this.componentGroup = componentGroup;
    }

    public String getComponentExample() {
        return componentExample;
    }

    public void setComponentExample(String componentExample) {
        this.componentExample = componentExample;
    }

    public boolean isSourceCodeToggleStatus() {
        return sourceCodeToggleStatus;
    }

    public void setSourceCodeToggleStatus(boolean sourceCodeToggleStatus) {
        this.sourceCodeToggleStatus = sourceCodeToggleStatus;
    }

    public boolean isRenderAsExample() {
        return renderAsExample;
    }

    public void setRenderAsExample(boolean renderAsExample) {
        this.renderAsExample = renderAsExample;
    }

    public int getActivePaneIndex() {
        return activePaneIndex;
    }

    public void setActivePaneIndex(int activePaneIndex) {
        this.activePaneIndex = activePaneIndex;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
