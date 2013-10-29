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

package com.icesoft.faces.webapp.parser;

import java.util.ArrayList;

/**
 * A simple class needed to process tag libraries when creating a
 * TagToComponentMap object.  This object is created by the digester to hold
 * relevant values.
 *
 * @author Steve Maryka
 */
public class TagToTagClassElement {
    /* An obect that we can use to digest <tag> entries in a tld */
    private String tagName;
    private String tagClass;
    private String description;
    private ArrayList<AttributeElement> attributes;

    public ArrayList<AttributeElement> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<AttributeElement> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute (AttributeElement a) {
		attributes.add(a);
	}

	/**
     * Constructor.
     */
    public TagToTagClassElement() {
        tagName = null;
        tagClass = null;
        attributes = new ArrayList<AttributeElement>();
    }

    /**
     * TagName getter.
     *
     * @return tag name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * TagClass getter
     *
     * @return tag class
     */
    public String getTagClass() {
        return tagClass;
    }
    
    public String getDescription() {
    	return description;
    }

    /**
     * TagName setter.
     *
     * @param name tag name
     */
    public void setTagName(String name) {
        tagName = name;
    }

    /**
     * TagClass setter.
     *
     * @param className tag class.
     */
    public void setTagClass(String className) {
        tagClass = className;
    }
    
    public void setDescription (String description) {
    	this.description = description;
    }
}    
