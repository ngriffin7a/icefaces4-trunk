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

package org.icefaces.ace.generator.behavior;

import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.meta.annotation.ClientEvent;
 
public class ClientBehaviorHolder extends Behavior {

	public ClientBehaviorHolder() {
		super(ClientBehaviorHolder.class);
	}

	@Override
	public boolean hasBehavior(Class clazz) {
		return clazz.isAnnotationPresent(org.icefaces.ace.meta.annotation.ClientBehaviorHolder.class);
	}


	public boolean hasInterface() {
		return true;
	}

	public String getInterfaceName() {
		return "IceClientBehaviorHolder";
	}
	
	public void addImportsToComponent(StringBuilder stringBuilder) {
		stringBuilder.append("import org.icefaces.ace.api.IceClientBehaviorHolder;\n");
        stringBuilder.append("import java.util.Collection;\n");
		stringBuilder.append("import java.util.Collections;\n");
	}	
	
	public void addCodeToComponent(StringBuilder output) {
		org.icefaces.ace.meta.annotation.ClientBehaviorHolder anno = (org.icefaces.ace.meta.annotation.ClientBehaviorHolder)
			GeneratorContext.getInstance().getActiveMetaContext().getActiveClass().getAnnotation(org.icefaces.ace.meta.annotation.ClientBehaviorHolder.class);
		ClientEvent[] events = anno.events();

        output.append("\n\tprivate static final Collection<String> eventNames =");
        output.append("\n\t\tCollections.unmodifiableCollection(Arrays.asList(");
        for (int i = 0; i < events.length; i++) {
            output.append("\n\t\t\t\""+ events[i].name() +"\""+((i < events.length-1)?",":"));"));
        }

        output.append("\n\n\tprivate static final Map<String, String> defaultRenderMap;");
        output.append("\n\tprivate static final Map<String, String> defaultExecuteMap;");
        output.append("\n\tprivate static final Map<String, String> listenerArgumentMap;");
        output.append("\n\tstatic {");
        output.append("\n\t\tMap<String, String> drm = new HashMap<String, String>("+(events.length+1)+");");
        output.append("\n\t\tMap<String, String> dem = new HashMap<String, String>("+(events.length+1)+");");
        output.append("\n\t\tMap<String, String> lam = new HashMap<String, String>("+(events.length+1)+");");
        for (int i = 0; i < events.length; i++) {
            output.append("\n\t\tdrm.put(\""+ events[i].name() +"\",\"" + events[i].defaultRender() + "\");");
            output.append("\n\t\tdem.put(\""+ events[i].name() +"\",\"" + events[i].defaultExecute() + "\");");
            output.append("\n\t\tlam.put(\""+ events[i].name() +"\",\"" + events[i].argumentClass() + "\");");
        }
        output.append("\n\t\tdefaultRenderMap = Collections.unmodifiableMap(drm);");
        output.append("\n\t\tdefaultExecuteMap = Collections.unmodifiableMap(dem);");
        output.append("\n\t\tlistenerArgumentMap = Collections.unmodifiableMap(lam);");
        output.append("\n\t}\n");

		output.append("\n\tpublic Collection<String> getEventNames() {");
		output.append("\n\t\treturn eventNames;");
		output.append("\n\t}\n");
		
		output.append("\n\tpublic String getDefaultEventName() {");
		output.append("\n\t\treturn \"" + anno.defaultEvent() + "\";");
		output.append("\n\t}\n");

		output.append("\n\tpublic String getDefaultRender(String event) {");
		output.append("\n\t\treturn defaultRenderMap.get(event);");
		output.append("\n\t}\n");

		output.append("\n\tpublic String getDefaultExecute(String event) {");
		output.append("\n\t\treturn defaultExecuteMap.get(event);");
		output.append("\n\t}\n");

        // The annotation has "" if it's not a custom class and is AjaxBehaviorEvent
        output.append("\n\tpublic String getListenerArgument(String event) {");
        output.append("\n\t\tif (!listenerArgumentMap.containsKey(event)) {");
        output.append("\n\t\t\treturn null;");
        output.append("\n\t\t}");
        output.append("\n\t\tString listenerArg = listenerArgumentMap.get(event);");
        output.append("\n\t\treturn (listenerArg == null || listenerArg.length() == 0) ? \"javax.faces.event.AjaxBehaviorEvent\" : listenerArg;");
        output.append("\n\t}\n");

        if (!anno.allowFAjax()) {
            output.append("\n\tpublic void addClientBehavior(String eventName, javax.faces.component.behavior.ClientBehavior behavior) {");
            output.append("\n\t\tif (behavior.getClass().equals(javax.faces.component.behavior.AjaxBehavior.class)) {");
            output.append("\n\t\t\treturn;");
            output.append("\n\t\t}");
            output.append("\n\t\tsuper.addClientBehavior(eventName, behavior);");
            output.append("\n\t}\n");
        }
	}
}
