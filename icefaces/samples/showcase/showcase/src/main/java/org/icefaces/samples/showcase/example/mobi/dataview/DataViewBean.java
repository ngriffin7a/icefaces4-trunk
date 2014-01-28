package org.icefaces.samples.showcase.example.mobi.dataview;

//import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
//import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-04-01
 * Time: 11:08 AM
 */
/*
@Destination(
        title = "example.layout.dataview.destination.title.short",
        titleExt = "example.layout.dataview.destination.title.long",
        titleBack = "example.layout.dataview.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/dataview-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/dataview-example.xhtml",
        resourcesPath  = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                                 title = "dataview-example.xhtml",
                                 resource = "/WEB-INF/includes/examples/layout/dataview-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                                 title = "DataViewBean.java",
                                 resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                         "/view/examples/layout/dataview/DataViewBean.java")
        }
)*/
@ManagedBean(name = DataViewBean.BEAN_NAME)
@SessionScoped
public class DataViewBean /*extends ExampleImpl<DataViewBean>*/
        implements Serializable {

    public static final String BEAN_NAME = "dataViewBean";

    public boolean mpgColVis = false;
    public boolean accelColVis = false;
    public boolean costColVis = false;
    public boolean weightColVis = false;
    public boolean textColVis = false;
    public boolean nope = false;
    public String blank = "";

    private List<Car> cars = new ArrayList<Car>() {{
        VehicleGenerator vg = new VehicleGenerator();
        addAll(vg.getRandomCars(100));
    }};

    public DataViewBean() {
        //super(DataViewBean.class);
    }

    public String getBlank() {
        return blank;
    }

    public void setBlank(String blank) {}

    public List<Car> getCars() {
        return cars;
    }


    public boolean isMpgColVis() {
        return mpgColVis;
    }

    public void setMpgColVis(boolean mpgColVis) {
        this.mpgColVis = mpgColVis;
    }

    public boolean isAccelColVis() {
        return accelColVis;
    }

    public void setAccelColVis(boolean accelColVis) {
        this.accelColVis = accelColVis;
    }

    public boolean isCostColVis() {
        return costColVis;
    }

    public void setCostColVis(boolean costColVis) {
        this.costColVis = costColVis;
    }

    public boolean isWeightColVis() {
        return weightColVis;
    }

    public void setWeightColVis(boolean weightColVis) {
        this.weightColVis = weightColVis;
    }

    public boolean isTextColVis() {
        return textColVis;
    }

    public void setTextColVis(boolean textColVis) {
        this.textColVis = textColVis;
    }

    public boolean isNope() {
        return nope;
    }

    public void setNope(boolean nope) {
        this.nope = false;
    }

    public void toggleCostColVis() {
        setCostColVis(!isCostColVis());
    }

    public void toggleWeightColVis() {
        setWeightColVis(!isWeightColVis());
    }

    public void toggleAccelColVis() {
        setAccelColVis(!isAccelColVis());
    }

    public void toggleMpgColVis() {
        setMpgColVis(!isMpgColVis());
    }

    public void toggleTextColVis() {
        setTextColVis(!isTextColVis());
    }
}
