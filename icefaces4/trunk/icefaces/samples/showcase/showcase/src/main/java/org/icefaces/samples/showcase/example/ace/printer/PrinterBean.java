/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.dataGenerators.ImageSet;
import org.icefaces.samples.showcase.dataGenerators.VehicleGenerator;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;

@ManagedBean(name= PrinterBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PrinterBean implements Serializable {
    public static final String BEAN_NAME = "printerBean";
	public String getBeanName() { return BEAN_NAME; }
    private ImageSet.ImageInfo image;
    private List<Car> cars;
    private ImageSet.ImageInfo printerIcon;
    
    
    public PrinterBean() 
    {
        image = ImageSet.getImage(ImageSet.ImageSelect.PICTURE);
        printerIcon = ImageSet.getImage(ImageSet.ImageSelect.PRINTER);
        VehicleGenerator generator = new VehicleGenerator();
        cars = generator.getRandomCars(10);
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