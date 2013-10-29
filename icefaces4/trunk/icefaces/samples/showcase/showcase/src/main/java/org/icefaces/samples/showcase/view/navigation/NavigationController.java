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

import org.icefaces.ace.event.AccordionPaneChangeEvent;
import org.icefaces.samples.showcase.example.ace.overview.AceSuiteOverviewBean;
import org.icefaces.samples.showcase.example.compat.overview.IceSuiteOverviewBean;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.metadata.context.Menu;
import org.icefaces.samples.showcase.metadata.context.MenuLink;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.faces.application.ViewHandler;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;


@ManagedBean
@ApplicationScoped
public class NavigationController implements Serializable {

    /**
     * Approach to navigation that will grab our params via the request map
     * This is meant to be used with a full page refresh as metadata (called on each page refresh as postValidate listener)
     */
    public void navigate() {
        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        navigate(map.get(NavigationModel.GROUP_PARAM), map.get(NavigationModel.EXAMPLE_PARAM));
    }

    /**
     * Approach to navigation that will try to grab f:attributes that were specified on a command component
     * This is meant to be used with a commandLink
     */
    public void navigate(ActionEvent event) {
        Object newGroup = FacesUtils.getFAttribute(event, NavigationModel.GROUP_PARAM);
        Object newExample = FacesUtils.getFAttribute(event, NavigationModel.EXAMPLE_PARAM);

        navigate(newGroup == null ? null : newGroup.toString(),
                newExample == null ? null : newExample.toString());
    }

    public void onMenuPaneChange(AccordionPaneChangeEvent event) {
        //get parameters
        String groupParamValue = event.getTab().getAttributes().get(NavigationModel.GROUP_PARAM).toString();
        String exampleParamValue = (String) event.getTab().getAttributes().get(NavigationModel.EXAMPLE_PARAM).toString();
        //set NavigationModel variables via main controller logic method navigate(String String)
        navigate(groupParamValue == null ? null : groupParamValue, exampleParamValue == null ? null : exampleParamValue);
        //prepare redirect URL
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        String redirectURL = prepareRedirectURL(groupParamValue, exampleParamValue, extContext);
        //issue redirect request
        try {
            extContext.redirect(redirectURL);
        } catch (IOException e) {
            extContext.log(getClass().getName() + ".invokeRedirect", e);
        }
    }

    public void onMenuPaneChangeWithoutRedirect(AccordionPaneChangeEvent event) {
        //get parameters
        String groupParamValue = event.getTab().getAttributes().get(NavigationModel.GROUP_PARAM).toString();
        String exampleParamValue = (String) event.getTab().getAttributes().get(NavigationModel.EXAMPLE_PARAM).toString();
        //set NavigationModel variables via main controller logic method navigate(String String)
        navigate(groupParamValue == null ? null : groupParamValue, exampleParamValue == null ? null : exampleParamValue);
    }

    /**
     * Main controller logic.
     */
    public void navigate(String newGroup, String newExample) {
        //NavigationModelBean constructor is called here
        NavigationModel navigationModel = (NavigationModel)
                FacesUtils.getManagedBean(NavigationModel.BEAN_NAME);

        // If our new component group is valid then set it into the navigationModel
        if (newGroup != null) {
            navigationModel.setComponentGroup(newGroup);
        }

        // If our new component example is valid then set it into the navigationModel
        if (newExample != null) {
            navigationModel.setComponentExample(newExample);
        } else {
            // Reset the example if the group changed and the example was null
            // This happens if the overall group menu was selected
            // Doing this will ensure that the default component example for the current group is set
            if (newGroup != null) {
                navigationModel.setComponentExample(newExample);
            }
        }

        // Update our group and example to the latest
        newGroup = navigationModel.getComponentGroup();
        newExample = navigationModel.getComponentExample();

        // Check whether we need to load the default navigation (this portion is executed during application startup)
        if (newGroup == null && newExample == null) {
            loadDefaultNavigation(navigationModel);
        }
        // Otherwise we may just need to load the example (this portion is executed during switch between suites (e.g. from ACE to ICE))
        else if (newGroup != null && newExample == null) {
            Menu menu = (Menu) FacesUtils.getManagedBean(newGroup);
            if (menu != null) {
                navigationModel.setCurrentComponentGroup(menu);
                ComponentExampleImpl compExample = (ComponentExampleImpl)
                        FacesUtils.getManagedBean(menu.getDefaultExample().getExampleBeanName());
                if (compExample != null) {
                    navigationModel.setCurrentComponentExample(compExample);
                }
                renderUIbasedOnExample(navigationModel.getCurrentComponentGroup().getDefaultExample().getExampleBeanName(), navigationModel);
            } else {
                loadDefaultNavigation(navigationModel);
            }
            // Otherwise check if the group/example is valid at all (this portion is executed during switch between showcases within suite (e.g. from ACE:accordion to ACE:menu))
        } else {
            Object tmpGroup = FacesUtils.getManagedBean(newGroup);
            Object tmpExample = FacesUtils.getManagedBean(newExample);
            if ((tmpGroup != null && tmpGroup instanceof Menu) && (tmpExample != null && tmpExample instanceof ComponentExampleImpl)) {
                ComponentExampleImpl currentExample = navigationModel.getCurrentComponentExample();
                ComponentExampleImpl setExample = (ComponentExampleImpl) tmpExample;

                //ENABLE/DISABLE CODE BELOW IF SWITCHING TO/FROM ACCORDION MENU TO THE REGULAR MENU(based on links) IN showcase.xhtml
                // Determine if we need to fire the effect
                // This is necessary when the overall demo was changed, so basically when the package is different
                // Although checking against packages is not the desired approach, there isn't another option for matching beans
                //  because they have no built-in idea of a parent-child heirarchy
                if (currentExample != null) {
                    if (!setExample.getClass().getPackage().equals(currentExample.getClass().getPackage())) {
                        setExample.prepareEffect();
                    }
                } else {
                    setExample.prepareEffect();
                }

                // Apply the new group and example
                navigationModel.setCurrentComponentGroup((Menu) tmpGroup);
                navigationModel.setCurrentComponentExample(setExample);
                //determine how shuite menu and example content should appear in UI
                renderUIbasedOnExample(newExample, navigationModel);
            } else {
                loadDefaultNavigation(navigationModel);
            }
        }
    }

    //The method below is used with accordion main menu - DO NOT REMOVE
    //If example = overview showcase then set active accordion tab tab to 0 and only render description portion of the showcase
    //If example = regular showcase, set active acordion tab based on redirect request parameters and display full showcase (with description, example content and source code)
    private void renderUIbasedOnExample(String exampleDescription, NavigationModel navigationModel) {
        //determine if example source code should appear in UI:
        navigationModel.setRenderAsExample(isContentAnExample(exampleDescription));
//ENABLE/DISABLE CODE BELOW IF SWITCHING TO/FROM ACCORDION MENU TO THE REGULAR MENU(based on links) IN showcase.xhtml
//        if(navigationModel.isRenderAsExample()) {
//            //set accordion menu tab
//            navigationModel.setActivePaneIndex(findActivePaneIndex(exampleDescription, navigationModel));
//        }
//        else {
//            //set accordion menu tab to the first one:
//            navigationModel.setActivePaneIndex(NavigationModel.DEFAULT_MENU_TAB_INDEX);
//        }
    }

    private void loadDefaultNavigation(NavigationModel navigationModel) {
        // load default pages.
        navigationModel.setCurrentComponentGroup((Menu)
                FacesUtils.getManagedBean(NavigationModel.DEFAULT_MENU));
        navigationModel.setComponentGroup(NavigationModel.DEFAULT_MENU);

        String beanName = navigationModel.getCurrentComponentGroup().getDefaultExample().getExampleBeanName();
        navigationModel.setCurrentComponentExample((ComponentExampleImpl) FacesUtils.getManagedBean(beanName));
        navigationModel.setComponentExample(beanName);
        navigationModel.getCurrentComponentGroup().getBeanName();
        renderUIbasedOnExample(beanName, navigationModel);
    }

    private String prepareRedirectURL(String groupParameterValue, String exampleParameterValue, ExternalContext extContext) {
        String viewId = NavigationModel.DEFAULT_VIEW_ID;
        String charEncoding = extContext.getRequestCharacterEncoding();
        try {
            String groupParamName = URLEncoder.encode(NavigationModel.GROUP_PARAM, charEncoding);
            String groupParamValue = URLEncoder.encode(groupParameterValue, charEncoding);
            String exampleParamName = URLEncoder.encode(NavigationModel.EXAMPLE_PARAM, charEncoding);
            String exampleParamValue = URLEncoder.encode(exampleParameterValue, charEncoding);

            viewId = extContext.getRequestContextPath() + viewId + '?' + groupParamName
                    + "=" + groupParamValue + "&" + exampleParamName + "=" + exampleParamValue;
            String urlLink = extContext.encodeActionURL(viewId);
            return urlLink;
        } catch (IOException e) {
            extContext.log(getClass().getName() + ".invokeRedirect", e);
            return null;
        }
    }

    private int findActivePaneIndex(String exampleDescription, NavigationModel navigationModel) {
        ArrayList<MenuLink> currentMenuStructure = navigationModel.getCurrentComponentGroup().getMenuLinks();
        int tabIndex = 0;
        for (MenuLink menuItem : currentMenuStructure) {
            if (menuItem.getExampleBeanName().equals(exampleDescription)) {
                return tabIndex;
            }
            tabIndex++;
        }
        //if tabIndex was not returned by now, return current navigationModel.activePaneIndex (this happens when we switch from one example to another within same tab
        if (tabIndex == currentMenuStructure.size()) {
            tabIndex = navigationModel.getActivePaneIndex();
        }
        return tabIndex;
    }

    public static void refreshPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        ViewHandler handler = context.getApplication().getViewHandler();
        String viewId = context.getViewRoot().getViewId();
        UIViewRoot root = handler.createView(context, viewId);

        root.setViewId(viewId);
        context.setViewRoot(root);
    }

    public static void reloadPage() {
        NavigationModel navigationModel = (NavigationModel)
                FacesUtils.getManagedBean(NavigationModel.BEAN_NAME);

        // Redirect to the current page with the current params
        loadPage("?" +
                NavigationModel.GROUP_PARAM + "=" + navigationModel.getComponentGroup() +
                "&" +
                NavigationModel.EXAMPLE_PARAM + "=" + navigationModel.getComponentExample());
    }

    public static void loadPage(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (Throwable e) {
        }
    }

    private boolean isContentAnExample(String exampleDescription) {
        if (exampleDescription.equals(AceSuiteOverviewBean.BEAN_NAME) || exampleDescription.equals(IceSuiteOverviewBean.BEAN_NAME)) {
            return false;
        } else {
            return true;
        }
    }
}