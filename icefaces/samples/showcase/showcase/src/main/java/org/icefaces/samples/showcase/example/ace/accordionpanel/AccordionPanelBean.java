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

package org.icefaces.samples.showcase.example.ace.accordionpanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;

@ComponentExample(
        title = "example.ace.accordionpanel.title",
        description = "example.ace.accordionpanel.description",
        example = "/resources/examples/ace/accordionpanel/accordionPanel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="accordionPanel.xhtml",
                    resource = "/resources/examples/ace/accordionpanel/accordionPanel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AccordionPanel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/accordionpanel/AccordionPanelBean.java")
        }
)
@Menu(
	title = "menu.ace.accordionpanel.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.accordionpanel.subMenu.main",
	                isDefault = true,
                    exampleBeanName = AccordionPanelBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.accordionpanel.subMenu.dynamic",
                    exampleBeanName = AccordionPanelDynamicBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.accordionpanel.subMenu.effect",
                    exampleBeanName = AccordionPanelEffectBean.BEAN_NAME)
    }
)
@ManagedBean(name= AccordionPanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AccordionPanelBean extends ComponentExampleImpl<AccordionPanelBean> implements Serializable {
    public static final String BEAN_NAME = "accordionPanelBean";
    
    private List<Item> items;
    private ImageSet.ImageInfo image;
    private LinkedHashMap <String, Integer> toDoList;
    
    public AccordionPanelBean() 
    {
        super(AccordionPanelBean.class);
        items = populateListWithItems();
        toDoList = populateToDoList();
        image = ImageSet.getImage(ImageSet.ImageSelect.PICTURE);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private ArrayList<Item> populateListWithItems()
   {
        ArrayList<Item> list = new ArrayList<Item>();
        ArrayList<ImageInfo> foodImages = ImageSet.getImages(ImageSet.ImagesSelect.FOOD);
        double basePrice = 1.99;
        double price;
        DecimalFormat doubleFormater = new DecimalFormat("#.##");
        for(int i = 0; i<foodImages.size(); i++)
        {
            Item item = new Item(foodImages.get(i));
            item.setId(i+1);
            price = (0.63+basePrice+(i+1)*basePrice/2);
            item.setPrice( Double.valueOf(doubleFormater.format(price+price*0.05)) );
            item.setQuantity(10);
            list.add(item);
        }
        return list;
    }
    
    private LinkedHashMap <String, Integer> populateToDoList()
    {
        LinkedHashMap <String, Integer> list = new LinkedHashMap <String, Integer>();
        list.put("Buy groceries",1);
        list.put("Review picture of the day",2);
        list.put("Send invitations",3);
        list.put("Call John",4);
        list.put("Check calendar",5);
        return list;
    }
    

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public ImageInfo getImage() {
        return image;
    }

    public void setImage(ImageInfo image) {
        this.image = image;
    }

    public LinkedHashMap <String, Integer> getToDoList() {
        return toDoList;
    }

    public void setToDoList(LinkedHashMap <String, Integer> toDoList) {
        this.toDoList = toDoList;
    }
}