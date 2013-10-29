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

package org.icefaces.samples.showcase.example.ace.chart;

import org.icefaces.ace.model.chart.SectorSeries;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

@ComponentExample(
        parent = ChartBean.BEAN_NAME,
        title = "example.ace.chart.donut.title",
        description = "example.ace.chart.donut.description",
        example = "/resources/examples/ace/chart/chartDonut.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="ChartDonut.xhtml",
                    resource = "/resources/examples/ace/chart/chartDonut.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ChartDonutBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/chart/ChartDonutBean.java")
        }
)
@ManagedBean(name= ChartDonutBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ChartDonutBean extends ComponentExampleImpl<ChartDonutBean> implements Serializable
{
    public static final String BEAN_NAME = "chartDonutBean";
    
    private List<SectorSeries> donutData = new ArrayList<SectorSeries>() {{
        add(new SectorSeries() {{
            add("a", 6);
            add("b", 8);
            add("c", 14);
            add("d", 20);
            setType(SectorType.DONUT);
        }});
        add(new SectorSeries() {{
            add("a", 8);
            add("b", 12);
            add("c", 6);
            add("d", 9);
        }});
    }};
    
    public ChartDonutBean() {
        super(ChartDonutBean.class);
    }
    
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
    
    public List<SectorSeries> getDonutData() {
        return donutData;
    }

    public void setDonutData(List<SectorSeries> donutData) {
        this.donutData = donutData;
    }
}
