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

package org.icefaces.samples.showcase.example.ace.printer;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.dataGenerators.VehicleGenerator;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;

@ComponentExample(
        title = "example.ace.printer.title",
        description = "example.ace.printer.description",
        example = "/resources/examples/ace/printer/printerOverview.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="printerOverview.xhtml",
                    resource = "/resources/examples/ace/printer/printerOverview.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PrinterBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/printer/PrinterBean.java")
        }
)
@Menu(
            title = "menu.ace.printer.subMenu.title",
            menuLinks = {
                @MenuLink(title = "menu.ace.printer.subMenu.main", isDefault = true, exampleBeanName = PrinterBean.BEAN_NAME)
            }
)
@ManagedBean(name= PrinterBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PrinterBean extends ComponentExampleImpl< PrinterBean > implements Serializable {
    public static final String BEAN_NAME = "printerBean";
    private ImageSet.ImageInfo image;
    private List<Car> cars;
    private ImageSet.ImageInfo printerIcon;
    
    
    public PrinterBean() 
    {
        super(PrinterBean.class);
        image = ImageSet.getImage(ImageSet.ImageSelect.PICTURE);
        printerIcon = ImageSet.getImage(ImageSet.ImageSelect.PRINTER);
        VehicleGenerator generator = new VehicleGenerator();
        cars = generator.getRandomCars(10);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public ImageSet.ImageInfo getImage() {
        return image;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public ImageSet.ImageInfo getPrinterIcon() {
        return printerIcon;
    }
}