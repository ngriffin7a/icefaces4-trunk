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
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.menubar.MenuItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MenuPopupBean.BEAN_NAME,
        title = "example.compat.menuPopup.dynamic.title",
        description = "example.compat.menuPopup.dynamic.description",
        example = "/resources/examples/compat/menuPopup/menuPopupDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuPopupDynamic.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuPopup/menuPopupDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuPopupDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuPopup/MenuPopupDynamic.java")
        }
)
@ManagedBean(name= MenuPopupDynamic.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuPopupDynamic extends ComponentExampleImpl<MenuPopupDynamic> implements Serializable {
	
	public static final String BEAN_NAME = "menuPopupDynamic";
	
	private String LABEL_TEXT = "New Menu Item ";
	private String ICON_PATH = "resources/css/images/menu/";
	
	private Random randomizer = new Random(System.nanoTime());
	private String[] icons = new String[] {
	    "open.gif",
	    "print.gif",
	    "recent.gif",
	    "save.gif",
	    "zoomin.gif",
	    "fitinpage.gif"
	};
	private List<MenuItem> items = generateItems(5);
	private int index = 25;
	private String currentText;
	private String currentIcon;
	private int generateCount = 5;
	
	public MenuPopupDynamic() {
		super(MenuPopupDynamic.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public String[] getIcons() { return icons; }
	public List<MenuItem> getItems() { return items; }
	public String getCurrentText() { return currentText; }
	public String getCurrentIcon() { return currentIcon; }
	public int getGenerateCount() { return generateCount; }
	
	public void setItems(List<MenuItem> items) { this.items = items; }
	public void setCurrentText(String currentText) { this.currentText = currentText; }
	public void setCurrentIcon(String currentIcon) { this.currentIcon = currentIcon; }
	public void setGenerateCount(int generateCount) { this.generateCount = generateCount; }
	
	private List<MenuItem> generateItems(int count) {
	    List<MenuItem> toReturn = new ArrayList<MenuItem>(count);
	    
	    for (int i = index; i < (index+count); i++) {
	        toReturn.add(makeItem((index+i)));
	    }
	    
	    index += count;
	    
	    return toReturn;
	}
	
	private MenuItem makeItem(int count) {
	    return makeItem(LABEL_TEXT + count, makeIcon());
	}
	
	private MenuItem makeItem(String text, String icon) {
	    MenuItem toReturn = new MenuItem();
	    
	    toReturn.setValue(text);
	    toReturn.setTitle(text);
	    toReturn.setIcon(ICON_PATH + icon);
	    
	    return toReturn;
	}
	
	private String makeIcon() {
	    return icons[randomizer.nextInt(icons.length-1)];
	}
	
	public void addItem(ActionEvent event) {
	    items.add(makeItem(currentText, currentIcon));
	    
	    currentText = null;
	    currentIcon = null;
	}
	
	public void generateNewItems(ActionEvent event) {
	    items = generateItems(generateCount);
	    
	    currentText = null;
	    currentIcon = null;
	}
}
