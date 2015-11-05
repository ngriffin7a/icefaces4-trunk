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
import java.util.ArrayList;
import java.util.List;

public class ComponentGroup implements Serializable {
	private String name;
	private String keywords;
	private boolean fullPageRefresh = false;
	private List<DemoResource> resources = new ArrayList<DemoResource>(2);
	private List<Demo> demos = new ArrayList<Demo>();
	
	public ComponentGroup(String name) {
		this(name, (DemoResource[])null, (String)null);
	}
	
	public ComponentGroup(String name, String wikiPath, String tldPath, Demo... incomingDemos) {
		this(name, new DemoResource[] { new DemoResource(DemoResource.Type.WIKI, wikiPath),
				 				         new DemoResource(DemoResource.Type.TLD, tldPath) }, null, incomingDemos);
	}
	
	public ComponentGroup(String name, String wikiPath, String tldPath, String keywords, Demo... incomingDemos) {
		this(name, new DemoResource[] { new DemoResource(DemoResource.Type.WIKI, wikiPath),
				 				         new DemoResource(DemoResource.Type.TLD, tldPath) }, keywords, incomingDemos);
	}
	
	public ComponentGroup(String name, String wikiPath, String tldPath, boolean fullPageRefresh, Demo... incomingDemos) {
		this(name, null, fullPageRefresh, new DemoResource[] { new DemoResource(DemoResource.Type.WIKI, wikiPath),
				 				                                    new DemoResource(DemoResource.Type.TLD, tldPath) }, incomingDemos);
	}
	
	public ComponentGroup(String name, String wikiPath, String tldPath, String keywords, boolean fullPageRefresh, Demo... incomingDemos) {
		this(name, keywords, fullPageRefresh, new DemoResource[] { new DemoResource(DemoResource.Type.WIKI, wikiPath),
				 				                                    new DemoResource(DemoResource.Type.TLD, tldPath) }, incomingDemos);
	}
	
	public ComponentGroup(String name, DemoResource[] resources, Demo... incomingDemos) {
		this(name, resources, null, incomingDemos);
	}
	
	public ComponentGroup(String name, DemoResource[] resources, String keywords, Demo... incomingDemos) {
		this(name, keywords, false, resources, incomingDemos);
	}
	
	public ComponentGroup(String name, String keywords, boolean fullPageRefresh, DemoResource[] resources, Demo... incomingDemos) {
		this.name = name;
		if (keywords != null) {
			this.keywords = keywords;
		}
		this.fullPageRefresh = fullPageRefresh;
		
		if ((resources != null) && (resources.length > 0)) {
			for (DemoResource loopResource : resources) {
				this.resources.add(loopResource);
			}
		}
		
		if ((incomingDemos != null) && (incomingDemos.length > 0)) {
			for (Demo loopDemo : incomingDemos) {
				demos.add(loopDemo);
			}
		}		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public boolean isFullPageRefresh() {
		return fullPageRefresh;
	}
	public void setFullPageRefresh(boolean fullPageRefresh) {
		this.fullPageRefresh = fullPageRefresh;
	}
	public List<DemoResource> getResources() {
		return resources;
	}
	public void setResources(List<DemoResource> resources) {
		this.resources = resources;
	}
	public boolean getHasResources() {
		return resources != null && !resources.isEmpty();
	}
	public DemoResource getWikiResource() {
		if (getHasResources()) {
			for (DemoResource loopResource : resources) {
				if (loopResource.getType() == DemoResource.Type.WIKI) {
					return loopResource;
				}
			}
		}
		return null;
	}
	public DemoResource getTldResource() {
		if (getHasResources()) {
			for (DemoResource loopResource : resources) {
				if (loopResource.getType() == DemoResource.Type.TLD) {
					return loopResource;
				}
			}
		}
		return null;		
	}
	public List<Demo> getDemos() {
		return demos;
	}
	public void setDemos(List<Demo> demos) {
		this.demos = demos;
	}
	
	public void addDemo(Demo toAdd) {
		demos.add(toAdd);
	}
	
	public Demo getFirstDemo() {
		if (getHasDemos()) {
			return demos.get(0);
		}
		return null;
	}
	
	public boolean getHasDemos() {
		return demos != null && !demos.isEmpty();
	}
}
