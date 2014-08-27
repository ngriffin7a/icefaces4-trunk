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

package org.icefaces.samples.showcase.example.core;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.model.chart.CartesianSeries;
import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@ComponentExample(
        title = "Push Component",
        description = "The <b>icecore:push</b> component allows the configuration of Ajax Push behavior on a per-view basis.",
        example = "/resources/examples/core/push.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="push.xhtml",
                        resource = "/resources/examples/core/push.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="PushBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/PushBean.java")
        }
)

@ManagedBean
@ViewScoped
public class PushBean extends ComponentExampleImpl<PushBean> implements Serializable {
    private final static String DEMO = "demo";
    private final static Random randomizer = new Random(System.currentTimeMillis());
    private final Timer timer = new Timer();
    private int strategyIndex = 0;

    private TimerTask task = new NoopTask();
    private PortableRenderer portableRenderer;

    public PushBean() {
        super(PushBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
        portableRenderer = PushRenderer.getPortableRenderer();
    }

    public int getStrategyIndex() {
        return strategyIndex;
    }

    public void setStrategyIndex(int strategyIndex) {
        this.strategyIndex = strategyIndex;

        task.cancel();

        switch (strategyIndex) {
            case 0: {
                task = new NoopTask();
                break;
            }
            case 1: {
                task = new UpdateEverySecondTask(timer);
                break;
            }
            case 2: {
                task = new UpdateEveryTwoSecondFiveTimesTask(timer);
                break;
            }
            case 3: {
                task = new UpdateWithThreeSecondDelayTask(timer);
                break;
            }
        }
    }

    private class NoopTask extends TimerTask {
        private NoopTask() {
        }

        public void run() {
        }
    }

    private class UpdateEverySecondTask extends TimerTask {
        private UpdateEverySecondTask(Timer timer) {
            timer.schedule(this, 1000, 1000);
        }

        public void run() {
            portableRenderer.render(DEMO);
        }
    }

    private class UpdateEveryTwoSecondFiveTimesTask extends TimerTask {
        private int counter = 0;

        private UpdateEveryTwoSecondFiveTimesTask(Timer timer) {
            timer.schedule(this, 1000, 2000);
        }

        public void run() {
            if (++counter < 5) {
                portableRenderer.render(DEMO);
            } else {
                cancel();
            }
        }
    }

    private class UpdateWithThreeSecondDelayTask extends TimerTask {
        private int counter = 0;

        private UpdateWithThreeSecondDelayTask(Timer timer) {
            timer.schedule(this, 3000);
        }

        public void run() {
            if (++counter < 3) {
                portableRenderer.render(DEMO);
            } else {
                cancel();
            }
        }
    }

    private Axis defaultAxis = new Axis() {{
        setTickAngle(-30);
    }};

    private Axis xAxis = new Axis() {{
        setType(AxisType.CATEGORY);
    }};

    private Axis yAxis = new Axis() {{
        setAutoscale(true);
        setTickInterval("5");
        setLabel("USD Millions");
    }};

    public Axis getDefaultAxis() {
        return defaultAxis;
    }

    public Axis getAxisX() {
        return xAxis;
    }

    public Axis getAxisY() {
        return yAxis;
    }

    public CartesianSeries getBarData() {
        CartesianSeries barData = new CartesianSeries() {{
            setType(CartesianType.BAR);
            add("HDTV Receiver", randomizer.nextInt(20));
            add("Cup Holder Pinion Bob", randomizer.nextInt(20));
            add("Generic Fog Lamp", randomizer.nextInt(20));
            add("8 Track Control Module", randomizer.nextInt(20));
            add("Sludge Pump Fourier Modulator", randomizer.nextInt(20));
            add("Transceiver Spice Rack", randomizer.nextInt(20));
            add("Hair Spray Danger Indicator", randomizer.nextInt(20));
            setLabel("Product / Sales");
        }};

        return barData;
    }

    public static class Item {
        public int getA() {
            return randomizer.nextInt(1000);
        }

        public int getB() {
            return randomizer.nextInt(100000);
        }

        public int getC() {
            return randomizer.nextInt(10000);
        }

        public int getD() {
            return randomizer.nextInt(100);
        }
    }

    public ArrayList getItems() {
        ArrayList items = new ArrayList();
        for (int i = 0; i < 10; i++) {
            items.add(new Item());
        }

        return items;
    }

}
