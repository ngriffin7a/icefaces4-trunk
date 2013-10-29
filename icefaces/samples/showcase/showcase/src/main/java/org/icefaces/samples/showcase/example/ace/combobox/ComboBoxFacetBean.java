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

package org.icefaces.samples.showcase.example.ace.combobox;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.*;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;

@ComponentExample(
		parent = ComboBoxBean.BEAN_NAME,
        title = "example.ace.combobox.facet.title",
        description = "example.ace.combobox.facet.description",
        example = "/resources/examples/ace/combobox/comboBoxFacet.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="comboBoxFacet.xhtml",
                    resource = "/resources/examples/ace/combobox/comboBoxFacet.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ComboBoxFacetBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/combobox/ComboBoxFacetBean.java")
        }
)
@ManagedBean(name= ComboBoxFacetBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ComboBoxFacetBean extends ComponentExampleImpl< ComboBoxFacetBean > implements Serializable {

    public static final String BEAN_NAME = "comboBoxFacetBean";
	
	private List<Color> textColors;
	private List<Color> backgroundColors;
    
    public ComboBoxFacetBean() {
        super(ComboBoxFacetBean.class);
		
		backgroundColors = new ArrayList<Color>();
		backgroundColors.add(new Color("Alice Blue", "#F0F8FF"));
		backgroundColors.add(new Color("Beige", "#F5F5DC"));
		backgroundColors.add(new Color("Cornsilk", "#FFF8DC"));
		backgroundColors.add(new Color("Gainsboro", "#DCDCDC"));
		backgroundColors.add(new Color("Khaki", "#F0E68C"));
		backgroundColors.add(new Color("Lavender", "#E6E6FA"));
		backgroundColors.add(new Color("Lavender Blush", "#FFF0F5"));
		backgroundColors.add(new Color("Lemon Chiffon", "#FFFACD"));
		backgroundColors.add(new Color("Misty Rose", "#FFE4E1"));
		backgroundColors.add(new Color("Mint Cream", "#F5FFFA"));
		backgroundColors.add(new Color("Pale Green", "#98FB98"));
		backgroundColors.add(new Color("Papaya Whip", "#FFEFD5"));
		backgroundColors.add(new Color("Sea Shell", "#FFF5EE"));
		backgroundColors.add(new Color("White Smoke", "#F5F5F5"));
		
		textColors = new ArrayList<Color>();
		textColors.add(new Color("Aquamarine", "#7FFFD4"));
		textColors.add(new Color("Burly Wood", "#DEB887"));
		textColors.add(new Color("Cadet Blue", "#5F9EA0"));
		textColors.add(new Color("Coral", "#FF7F50"));
		textColors.add(new Color("Crimson", "#DC143C"));
		textColors.add(new Color("Fire Brick", "#B22222"));
		textColors.add(new Color("Golden Rod", "#DAA520"));
		textColors.add(new Color("Green Yellow", "#ADFF2F"));
		textColors.add(new Color("Indigo", "#4B0082"));
		textColors.add(new Color("Medium Orchid", "#BA55D3"));
		textColors.add(new Color("Midnight Blue", "#191970"));
		textColors.add(new Color("Olive Drab", "#6B8E23"));
		textColors.add(new Color("Orange", "#FFA500"));
		textColors.add(new Color("Pale Violet Red", "#DB7093"));
		textColors.add(new Color("Sandy Brown", "#F4A460"));
		textColors.add(new Color("Slate Blue", "#6A5ACD"));
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	private String textColor = "";
    public String getTextColor() { return textColor; }
    public void setTextColor(String textColor) { this.textColor = textColor; }

	private String backgroundColor = "";
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
	
    public List<Color> getTextColors() { return textColors; }

    public List<Color> getBackgroundColors() { return backgroundColors; }
	
	private String errorSummary = "Color code must be in the format '#xxx' or '#xxxxxx', where 'x' is a hexadecimal digit.";	
	public void colorValidator(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			String color = value.toString();
			if ("".equals(color.trim())) return;
			
			if (!color.trim().matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorSummary, errorSummary);
				throw new ValidatorException(message);
			}
		}
	}
}