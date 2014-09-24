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

package org.icefaces.ace.generator.artifacts;

import org.icefaces.ace.generator.context.MetaContext;
import org.icefaces.ace.generator.context.TagHandlerContext;
import org.icefaces.ace.generator.context.ComponentContext;
import org.icefaces.resources.ICEBrowserDependency;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

public abstract class Artifact {
    private MetaContext metaContext;

    public Artifact(MetaContext metaContext) {
        this.metaContext = metaContext;
    }

    public MetaContext getMetaContext() {
        return metaContext;
    }

	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	public abstract void build();

    /**
     * Used by both component artifact and tag handler artifact to
     * place resource dependencies in base files
     * @param clazz
     * @return
     */
    protected StringBuilder  writeICEResourceLibrary(Class clazz) {
        ICEResourceLibrary lib = (ICEResourceLibrary)clazz.getAnnotation(ICEResourceLibrary.class);
        StringBuilder sb = new StringBuilder();
        sb.append("@ICEResourceLibrary(\"");
        sb.append(lib.value());
        sb.append("\")\n");
        return sb;
    }

    /**
     * Used by both component artifact and tag handler artifact to
     * place resource dependencies in base files
     * @param clazz
     * @return
     */
    protected StringBuilder writeResourceDependencies(Class clazz) {
        StringBuilder writer = new StringBuilder();
        writer.append("@ICEResourceDependencies({\n");

        ICEResourceDependencies rd = (ICEResourceDependencies) clazz.getAnnotation(ICEResourceDependencies.class);
        ICEResourceDependency[] rds = rd.value();
        int rdsLength = rds.length;
        for (int i = 0; i < rdsLength; i++) {
            String overrideString = getOverrideString(rds[i]);
            writer.append(
                    "\t@ICEResourceDependency(name=\"" + rds[i].name() + "\"," +
                    "library=\"" + rds[i].library() +  "\"," +
                    "target=\"" + rds[i].target() + "\"," +
                    "browser=BrowserType." + rds[i].browser().toString() + "," +
                    "browserOverride=" + overrideString + ")");
            if (i < (rdsLength-1)) {
                writer.append(",");
            }
            writer.append("\n");
        }

        writer.append("})\n");
        return writer;
    }

    /**
     * Used by both component artifact and tag handler artifact to
     * place resource dependencies in base files
     * @param clazz
     * @return
     */
    protected StringBuilder writeEachResource(Class clazz){
        StringBuilder writer = new StringBuilder();
        writer.append("\n");
        writer.append("@ResourceDependencies({\n");

        ResourceDependencies rd = (ResourceDependencies) clazz.getAnnotation(ResourceDependencies.class);
        ResourceDependency[] rds = rd.value();
        int rdsLength = rds.length;
        for (int i = 0; i < rdsLength; i++) {
            writer.append("\t@ResourceDependency(name=\"" + rds[i].name() + "\",library=\"" + rds[i].library() + "\",target=\"" + rds[i].target() + "\")");
            if (i < (rdsLength-1)) {
                writer.append(",");
            }
            writer.append("\n");
        }
        writer.append("})");
        writer.append("\n\n");
        return writer;
    }
    private String getOverrideString(ICEResourceDependency rd) {
        String ret = "{";
        ICEBrowserDependency[] overrides = rd.browserOverride();

        if (overrides.length > 0) {
            ICEBrowserDependency bd;
            for (int i = 0; i < overrides.length; i++) {
                bd = overrides[i];
                ret += "\t@ICEBrowserDependency(name=\"" + bd.name() + "\"," +
                            "library=\"" + bd.library() +  "\"," +
                            "target=\"" + bd.target() + "\"," +
                            "browser=BrowserType." + bd.browser().toString() + ")";
                if (i == overrides.length - 1)
                    ret += "\n";
                else
                    ret += ",\n";
            }
            ret += "}";
        } else ret = "{}";

        return ret;
    }

}
