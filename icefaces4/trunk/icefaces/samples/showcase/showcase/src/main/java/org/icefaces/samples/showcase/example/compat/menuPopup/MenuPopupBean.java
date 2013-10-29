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

package org.icefaces.samples.showcase.example.compat.menuPopup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.menuPopup.title",
        description = "example.compat.menuPopup.description",
        example = "/resources/examples/compat/menuPopup/menuPopup.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuPopup.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuPopup/menuPopup.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuPopupBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuPopup/MenuPopupBean.java")
        }
)
@Menu(
	title = "menu.compat.menuPopup.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.menuPopup.subMenu.main",
                    isDefault = true,
                    exampleBeanName = MenuPopupBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuPopup.subMenu.events",
                    exampleBeanName = MenuPopupEvents.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuPopup.subMenu.hide",
                    exampleBeanName = MenuPopupHide.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuPopup.subMenu.icons",
                    exampleBeanName = MenuPopupIcons.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuPopup.subMenu.separator",
                    exampleBeanName = MenuPopupSeparator.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuPopup.subMenu.dynamic",
                    exampleBeanName = MenuPopupDynamic.BEAN_NAME)
})
@ManagedBean(name= MenuPopupBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuPopupBean extends ComponentExampleImpl<MenuPopupBean> implements Serializable {
	
	public static final String BEAN_NAME = "menuPopup";
	
	private static final String PARAM_TEXT = "text";
	private static final String PARAM_STYLE = "style";
	
	private List<FormattedWord> wordList = generateWordList();
	
	public MenuPopupBean() {
		super(MenuPopupBean.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String getParamText() { return PARAM_TEXT; }
	public String getParamStyle() { return PARAM_STYLE; }
	public List<FormattedWord> getWordList() { return wordList; }
	
	private List<FormattedWord> generateWordList() {
	    List<FormattedWord> toReturn = new ArrayList<FormattedWord>(7);
	    
	    toReturn.add(new FormattedWord("Context"));
	    toReturn.add(new FormattedWord("sensitive"));
	    toReturn.add(new FormattedWord("menu"));
	    toReturn.add(new FormattedWord("popup"));
	    toReturn.add(new FormattedWord("displayed"));
	    toReturn.add(new FormattedWord("on"));
	    toReturn.add(new FormattedWord("right-click"));
	    
	    return toReturn;
	}
	
	private FormattedWord lookupWord(String text) {
	    if (!FacesUtils.isBlank(text)) {
	        for (FormattedWord currentWord : wordList) {
	            if (text.equals(currentWord.getText())) {
	                return currentWord;
	            }
	        }
	    }
	    
	    return null;
	}
	
	public void applyStyle(ActionEvent event) {
	    FormattedWord toStyle = lookupWord(
	                                FacesUtils.getRequestParameter(PARAM_TEXT));
	    
	    if (toStyle != null) {
	        toStyle.setStyle(FacesUtils.getRequestParameter(PARAM_STYLE));
	    }
	}
}

