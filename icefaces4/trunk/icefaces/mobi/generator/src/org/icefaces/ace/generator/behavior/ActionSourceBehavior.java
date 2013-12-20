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

import javax.el.MethodExpression;

import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.ActionSource;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;


public class ActionSourceBehavior extends Behavior{
//	private final String ACTION_EXPRESSION = "actionExpression";
	
	@Property(expression= Expression.METHOD_EXPRESSION)
	private MethodExpression actionExpression;
	
	@Property(expression=Expression.METHOD_EXPRESSION)
	private MethodExpression actionListener;
	
	public ActionSourceBehavior() {
		super(ActionSourceBehavior.class);
	}
	public void addCodeToComponent(StringBuilder output) {
		output.append("\n\t/** ");
		output.append("\n\t* @throws NullPointerException {@inheritDoc}");
		output.append("\n\t*/ ");
		output.append("\n\tpublic void addActionListener(ActionListener listener) {");

		output.append("\n\t\taddFacesListener(listener);");

		output.append("\n\t}");
		output.append("\n");        		    
		output.append("\n\tpublic ActionListener[] getActionListeners() {");

		output.append("\n\t\tActionListener al[] = (ActionListener [])");
		output.append("\n\t\tgetFacesListeners(ActionListener.class);");
		output.append("\n\t\treturn (al);");

		output.append("\n\t}");                    
        
		output.append("\n");  
		output.append("\n\t/**");
    	output.append("\n\t * @throws NullPointerException {@inheritDoc}");
        output.append("\n\t */");
        output.append("\n\tpublic void removeActionListener(ActionListener listener) {");
        output.append("\n\t\tremoveFacesListener(listener);");
		output.append("\n\t}");
		
		output.append("\n\tpublic void broadcast(FacesEvent event) throws AbortProcessingException {");
		output.append("\n\t\t// Perform standard superclass processing (including calling our");
		output.append("\n\t\t// ActionListeners)");
        output.append("\n\t\tsuper.broadcast(event);");

		output.append("\n\t\tif (event instanceof ActionEvent) {");
		output.append("\n\t\t\tFacesContext context = getFacesContext();");
            
		output.append("\n\t\t\t// Notify the specified action listener method (if any)");
		output.append("\n\t\t\tMethodExpression mb = getActionListener();");
		output.append("\n\t\tif (mb != null) {");
		output.append("\n\t\t\t\tmb.invoke(context, new Object[] { event });");
		output.append("\n\t\t\t}");

		output.append("\n\t\t// Invoke the default ActionListener");
		output.append("\n\t\tActionListener listener =");
		output.append("\n\t\t\t   context.getApplication().getActionListener();");
		output.append("\n\t\t\t  if (listener != null) {");
		output.append("\n\t\t\tlistener.processAction((ActionEvent) event);");
        output.append("\n\t\t\t}");
        output.append("\n\t\t}");
	    output.append("\n\t}");		

		
	}

	public void addImportsToComponent(StringBuilder stringBuilder) {
		stringBuilder.append("import javax.faces.application.Application;\n");
		stringBuilder.append("import javax.faces.event.*;\n");
		stringBuilder.append("import javax.faces.component.ActionSource2;\n");		
		
		
	}

	public void addPropertiesEnumToComponent(StringBuilder output) {
		for(PropertyValues prop : getProperties()) {
			output.append("\t\t");
			output.append(prop.getJavaVariableName()); 
			output.append(",\n"); 
		}
	}
	
	public boolean hasBehavior(Class clazz) {
		return clazz.isAnnotationPresent(ActionSource.class);
	}

	public boolean hasInterface() {
		return true;
	}

	public String getInterfaceName() {
		return "ActionSource2";
	}

	
	
}
