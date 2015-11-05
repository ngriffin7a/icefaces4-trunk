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

public class Demo implements Serializable{
	private String name;
	private String description;
	private List<DemoSource> sources = new ArrayList<DemoSource>(2);
	
	public Demo(String name, String description, String sourcePath, String xhtmlPath, String javaPath) {
		this(name, description, null, sourcePath, xhtmlPath, javaPath);
	}
	
	public Demo(String name, String description, String keywords, String sourcePath, String xhtmlPath, String javaPath) {
		this(name, description, keywords,
		     	new DemoSource[] { new DemoSource(DemoSource.Type.XHTML, xhtmlPath, sourcePath),
								   new DemoSource(DemoSource.Type.JAVA, javaPath, sourcePath) }
			);
	}
	
	public Demo(String name, String description, DemoSource[] sources) {
		this(name, description, null, sources);
	}
	
	public Demo(String name, String description, String keywords, DemoSource[] sources) {
		this.name = name;
		this.description = description;
		
		if ((sources != null) && (sources.length > 0)) {
			for (DemoSource loopSource : sources) {
				this.sources.add(loopSource);
			}
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DemoSource> getSources() {
		return sources;
	}

	public void setSources(List<DemoSource> sources) {
		this.sources = sources;
	}
	
	public boolean getHasSources() {
		return sources != null && !sources.isEmpty();
	}
	
	public boolean getHasManySources() {
		return sources != null && sources.size() > 1;
	}
	
	public String getIncludedPath() {
		if (sources != null) {
			for (DemoSource loopSource : sources) {
				if (DemoSource.Type.XHTML == loopSource.getType()) {
					return loopSource.getPath();
				}
			}
		}
		return null;
	}
}
