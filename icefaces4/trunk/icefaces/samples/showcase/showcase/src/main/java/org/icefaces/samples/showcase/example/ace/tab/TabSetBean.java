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

package org.icefaces.samples.showcase.example.ace.tab;

import org.icefaces.samples.showcase.dataGenerators.ImageSet.ImageInfo;
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
import org.icefaces.samples.showcase.example.ace.accordionpanel.Item;

@ComponentExample(
        title = "example.ace.tabSet.title",
        description = "example.ace.tabSet.description",
        example = "/resources/examples/ace/tab/tabset.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tabset.xhtml",
                    resource = "/resources/examples/ace/tab/tabset.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TabSetBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/tab/TabSetBean.java"),
            @ExampleResource(type = ResourceType.java,
                        title = "ImageSet.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/dataGenerators/ImageSet.java")
        }
)
@Menu(
        title = "menu.ace.tabSet.subMenu.title",
        menuLinks = {
                @MenuLink(title = "menu.ace.tabSet.subMenu.main",
                        isDefault = true,
                        exampleBeanName = TabSetBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.subMenu.clientSide",
                        exampleBeanName = TabClientSideBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.subMenu.serverSide",
                        exampleBeanName = TabServerSideBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.subMenu.proxy",
                        exampleBeanName = TabProxyBean.BEAN_NAME)
        })
@ManagedBean(name= TabSetBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TabSetBean extends ComponentExampleImpl<TabSetBean> implements Serializable 
{
    public static final String BEAN_NAME = "tabSet";
    
    private List<Item> items;
    private ImageSet.ImageInfo image;
    private LinkedHashMap <String, Integer> toDoList;

    public TabSetBean() {
        super(TabSetBean.class);
        initializeInstanceVariables();
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    private void initializeInstanceVariables()
    {
        items = populateListWithItems();
        toDoList = populateToDoList();
        image = ImageSet.getImage(ImageSet.ImageSelect.PICTURE);
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
        list.put("Check calendar",5);
        return list;
    }

    public ImageInfo getImage() {
        return image;
    }

    public void setImage(ImageInfo image) {
        this.image = image;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public LinkedHashMap<String, Integer> getToDoList() {
        return toDoList;
    }

    public void setToDoList(LinkedHashMap<String, Integer> toDoList) {
        this.toDoList = toDoList;
    }
}
