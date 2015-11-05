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

import java.io.Serializable;

public class DemoResource implements Serializable {
    public static final String WIKI_PATH_BASE = "http://wiki.icesoft.org/display/ICE/";
    public static final String ACE_TLD_PATH_BASE = "http://res.icesoft.org/docs/v4_latest/ace/tld/ace/";
    public static final String CORE_TLD_PATH_BASE = "http://res.icesoft.org/docs/v4_latest/core/comps/tld/icecore/";
	
	public enum Type {
		WIKI, TLD;
	}
	
	private String name;
	private String path;
	private Type type;
	
	public DemoResource(Type type, String path) {
		this(type, null, path);
	}
	
	public DemoResource(Type type, String name, String path) {
		this(type, name, path, false);
	}
	
	public DemoResource(Type type, String name, String path, boolean fullPath) {
		this.type = type;
		
		// If we don't have a full path specified we'll create one
		if (!fullPath) {
			if (type == Type.WIKI) {
				path = WIKI_PATH_BASE + path;
			}
			else if (type == Type.TLD) {
				path = ACE_TLD_PATH_BASE + path;
			}
		}
		
		this.name = name;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public boolean getIsWiki() {
		return type != null && type == Type.WIKI;
	}
	
	public boolean getIsTld() {
		return type != null && type == Type.TLD;
	}
}
