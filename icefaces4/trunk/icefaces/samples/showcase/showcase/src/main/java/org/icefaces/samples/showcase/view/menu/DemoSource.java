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

package org.icefaces.samples.showcase.view.menu;

import java.io.File;
import java.io.Serializable;

public class DemoSource implements Serializable {
	public static final String ACE_JAVA_PATH_BASE = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/";
	public static final String ACE_XHTML_PATH_BASE = "/resources/examples/ace/";
	public static final String CORE_JAVA_PATH_BASE = "/WEB-INF/classes/org/icefaces/samples/showcase/example/core/";
	public static final String CORE_XHTML_PATH_BASE = "/resources/examples/core/";
	
	public enum Type {
		XHTML, JAVA
	}
	
	private Type type;
	private String name;
	private String path;
	
	public DemoSource(Type type, String name, String path) {
		this(type, name, path, false);
	}
	
	public DemoSource(Type type, String name, String path, boolean fullPath) {
		this.type = type;
		
		// If we don't have a full path specified we'll create one
		if (!fullPath) {
			if (type == Type.XHTML) {
				path = ACE_XHTML_PATH_BASE + path + "/";
			}
			else if (type == Type.JAVA) {
				path = ACE_JAVA_PATH_BASE + path + "/";
			}
		}
		
		this.path = path + name;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
