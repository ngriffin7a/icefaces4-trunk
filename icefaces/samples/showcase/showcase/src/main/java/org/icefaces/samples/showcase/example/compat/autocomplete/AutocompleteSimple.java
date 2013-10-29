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

package org.icefaces.samples.showcase.example.compat.autocomplete;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import com.icesoft.faces.component.selectinputtext.TextChangeEvent;
import javax.faces.bean.CustomScoped;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = AutocompleteBean.BEAN_NAME,
        title = "example.compat.autocomplete.simple.title",
        description = "example.compat.autocomplete.simple.description",
        example = "/resources/examples/compat/autocomplete/autocompleteSimple.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="autocompleteSimple.xhtml",
                    resource = "/resources/examples/compat/"+
                               "autocomplete/autocompleteSimple.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AutocompleteSimple.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/autocomplete/AutocompleteSimple.java")
        }
)
@ManagedBean(name= AutocompleteSimple.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutocompleteSimple extends ComponentExampleImpl<AutocompleteSimple> implements Serializable {
	
	public static final String BEAN_NAME = "autocompleteSimple";
	
	private List<SelectItem> availableCities = AutocompleteData.wrapList();
	private String selectedText = null; // Text the user is typing in
	private String selectedCity;        // Entry the user selected
	
	public AutocompleteSimple() {
		super(AutocompleteSimple.class);
	}
	
    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

	public List<SelectItem> getAvailableCities() { return availableCities; }
	public String getSelectedText() { return selectedText; }
	public String getSelectedCity() { return selectedCity; }
	
	public void setAvailableCities(List<SelectItem> availalbeCities) { this.availableCities = availableCities; }
	public void setSelectedText(String selectedText) { this.selectedText = selectedText; }
	public void setSelectedCity(String selectedCity) { this.selectedCity = selectedCity; }
	
	public void textChanged(TextChangeEvent event) {
	    // Filter the list of cities based on what the user has typed so far
	    availableCities = AutocompleteData.wrapList(event.getNewValue().toString());
	}
	
	public void submitText(ActionEvent event) {
	    setSelectedCity(getSelectedText());
	    
	    // Clear out filtered list now that we have made a choice
	    availableCities = null;
	}
}
