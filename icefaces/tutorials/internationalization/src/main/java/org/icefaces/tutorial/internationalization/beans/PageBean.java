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

package org.icefaces.tutorial.internationalization.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class PageBean implements Serializable {
    private static final long serialVersionUID = 8109006400604945995L;
    private List<SelectItem> availableLocales;
	private Locale currentLocale;
	private String dropdownItem;
	
	public PageBean() {
		setCurrentLocale(FacesContext.getCurrentInstance().getViewRoot().getLocale());
		
		dropdownItem = currentLocale.getLanguage();
	}
	
	private void generateAvailableLocales() {
		availableLocales = new ArrayList<SelectItem>(0);
		
		// Add the default locale
		availableLocales.add(makeLocaleItem(
							     FacesContext.getCurrentInstance().getApplication().getDefaultLocale()));
		
		// Add any other supported locales
		for (Iterator<Locale> iter =
			     FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
		     iter.hasNext();) {
			availableLocales.add(makeLocaleItem(iter.next()));
		}
	}
	
	private SelectItem makeLocaleItem(Locale toWrap) {
		if (toWrap != null) {
			return new SelectItem(toWrap.getLanguage(),
					       	      toWrap.getDisplayName());
		}
		
		return null;
	}
	
	public List<SelectItem> getAvailableLocales() {
		if (availableLocales == null) {
			generateAvailableLocales();
		}
		
		return availableLocales;
	}

	public void setAvailableLocales(List<SelectItem> availableLocales) {
		this.availableLocales = availableLocales;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}
	
	public String getDropdownItem() {
		return dropdownItem;
	}

	public void setDropdownItem(String dropdownItem) {
		this.dropdownItem = dropdownItem;
	}

	public void applyLocale(Locale toApply) {
		System.out.println("Apply Locale: " + toApply.getDisplayName() + " (" + toApply.getLanguage() + ")");
		
		setCurrentLocale(toApply);
		
		FacesContext.getCurrentInstance().getViewRoot().setLocale(toApply);
	}
	
	public void localeChanged(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			applyLocale(new Locale(event.getNewValue().toString()));
		}
	}
}
