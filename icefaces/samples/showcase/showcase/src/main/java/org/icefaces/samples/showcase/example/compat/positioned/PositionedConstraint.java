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

package org.icefaces.samples.showcase.example.compat.positioned;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = PositionedBean.BEAN_NAME,
        title = "example.compat.positioned.constraint.title",
        description = "example.compat.positioned.constraint.description",
        example = "/resources/examples/compat/positioned/positionedConstraint.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="positionedConstraint.xhtml",
                    resource = "/resources/examples/compat/"+
                               "positioned/positionedConstraint.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PositionedConstraint.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/positioned/PositionedConstraint.java")
        }
)
@ManagedBean(name= PositionedConstraint.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PositionedConstraint extends ComponentExampleImpl<PositionedConstraint> implements Serializable {
	
	public static final String BEAN_NAME = "positionedConstraint";
	public static final String VERTICAL_CONSTRAINT = "vertical";
	public static final String HORIZONTAL_CONSTRAINT = "horizontal";
	public static final String NO_CONSTRAINT = "false";
	
	private SelectItem[] availableConstraints = new SelectItem[] {
	    new SelectItem(VERTICAL_CONSTRAINT, "Vertical"),
	    new SelectItem(HORIZONTAL_CONSTRAINT, "Horizontal"),
	    new SelectItem(NO_CONSTRAINT, "None")
	};
	private String constraint = VERTICAL_CONSTRAINT;
	
	public PositionedConstraint() {
		super(PositionedConstraint.class);
	}

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public SelectItem[] getAvailableConstraints() { return availableConstraints; }
	public String getConstraint() { return constraint; }
	public boolean isHorizontal() { return HORIZONTAL_CONSTRAINT.equals(constraint); }
	
	public void setConstraint(String constraint) { this.constraint = constraint; }
	
	public void applyConstraint(ActionEvent event) {
	    NavigationController.reloadPage();
	}
}
