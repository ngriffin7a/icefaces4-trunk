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

package org.icefaces.samples.showcase.example.mobi.flipswitch;


//import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
//import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
/*
@Destination(
        title = "example.input.flipswitch.destination.title.short",
        titleExt = "example.input.flipswitch.destination.title.long",
        titleBack = "example.input.flipswitch.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/flip-switch-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/flip-switch-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "flipSwitch-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/flip-switch-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "FlipSwitchBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/flipSwitch/FlipSwitchBean.java")
        }
)
*/
@ManagedBean(name = FlipSwitchBean.BEAN_NAME)
@SessionScoped
public class FlipSwitchBean /*extends ExampleImpl<FlipSwitchBean>*/ implements
        Serializable {

    public static final String BEAN_NAME = "flipSwitchBean";

    private boolean autoCapitalization = true;
    private boolean autoCorrection = true;
    private boolean checkSpelling;
    private boolean enableCapsLock = true;

    public FlipSwitchBean() {
        //super(FlipSwitchBean.class);
    }

    /**
     * Auto capitalization value change event.  This is a simple example of
     * how to setup a valueChangeListener for the flipSwitch component.
     *
     * @param event jsf valueChangeEvent.
     */
    public void autoCapitalizationChange(ValueChangeEvent event){

    }

    public boolean isAutoCapitalization() {
        return autoCapitalization;
    }

    public void setAutoCapitalization(boolean autoCapitalization) {
        this.autoCapitalization = autoCapitalization;
    }

    public boolean isAutoCorrection() {
        return autoCorrection;
    }

    public void setAutoCorrection(boolean autoCorrection) {
        this.autoCorrection = autoCorrection;
    }

    public boolean isCheckSpelling() {
        return checkSpelling;
    }

    public void setCheckSpelling(boolean checkSpelling) {
        this.checkSpelling = checkSpelling;
    }

    public boolean isEnableCapsLock() {
        return enableCapsLock;
    }

    public void setEnableCapsLock(boolean enableCapsLock) {
        this.enableCapsLock = enableCapsLock;
    }
}