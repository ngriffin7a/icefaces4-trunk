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

import org.icefaces.impl.component.JSEventListener;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@ComponentExample(
        title = "Javascript Event Listener Component",
        description = "The <b>&lt;icecore:jsEventListener&gt;</b> component can be used to capture Javascript events and then notify a server side defined action listener. In this example only 'click' and 'keypress' events are captured.",
        example = "/resources/examples/core/js-event-listener.xhtml"
)

@ExampleResources(
        resources = {
                @ExampleResource(type = ResourceType.xhtml,
                        title="js-event-listener.xhtml",
                        resource = "/resources/examples/core/js-event-listener.xhtml"),
                @ExampleResource(type = ResourceType.java,
                        title="JsEventListenerBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/JsEventListenerBean.java"),
        }
)


@ManagedBean
@CustomScoped(value = "#{window}")
public class JsEventListenerBean extends ComponentExampleImpl<JsEventListenerBean> implements Serializable {
    private JSEventListener.JSEvent actionEvent;

    public JsEventListenerBean() {
        super(JsEventListenerBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public JSEventListener.JSEvent getActionEvent() {
        return actionEvent;
    }

    public void captureEvent(ActionEvent e) {
        actionEvent = (JSEventListener.JSEvent) e;
    }

    public boolean getShowActionDescription() {
        return actionEvent != null;
    }

}

