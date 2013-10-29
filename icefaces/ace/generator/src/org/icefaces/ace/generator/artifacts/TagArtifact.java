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

package org.icefaces.ace.generator.artifacts;

import org.icefaces.ace.generator.context.ComponentContext;
import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.generator.utils.FileWriter;
import org.icefaces.ace.generator.utils.Utility;

import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;

public class TagArtifact extends Artifact{
	private StringBuilder generatedTagClass;

	public TagArtifact(ComponentContext componentContext) {
		super(componentContext);
	}

	private void startComponentClass(Component component) {
		generatedTagClass = new StringBuilder();

		generatedTagClass.append("package ");
		generatedTagClass.append(Utility.getPackageNameOfClass(Utility.getTagClassName(component)));
		generatedTagClass.append(";\n\n");

		generatedTagClass.append("import java.io.IOException;\n");
		generatedTagClass.append("import javax.el.*;\n");

		generatedTagClass.append("import javax.faces.*;\n");
		generatedTagClass.append("import javax.faces.component.*;\n");
		generatedTagClass.append("import javax.faces.context.*;\n");
		generatedTagClass.append("import javax.faces.convert.*;\n");

		generatedTagClass.append("import javax.faces.el.*;\n");
		generatedTagClass.append("import javax.faces.event.*;\n");
		generatedTagClass.append("import javax.faces.validator.*;\n");
		generatedTagClass.append("import javax.faces.webapp.*;\n");
		generatedTagClass.append("import javax.servlet.jsp.JspException;\n\n");  
		generatedTagClass.append("/*\n * ******* GENERATED CODE - DO NOT EDIT *******\n */\n");
		generatedTagClass.append("public class ");
		generatedTagClass.append(Utility.getSimpleNameOfClass(Utility.getTagClassName(component)));
		generatedTagClass.append(" extends ");
        generatedTagClass.append(component.baseTagClass());
        generatedTagClass.append(" {\n");
		generatedTagClass.append("\tpublic String getRendererType() {\n\t\treturn ");
		String rendererType = null;
		if (!"".equals(component.rendererType())) {
			rendererType = "\""+ component.rendererType() + "\"";
		}

		generatedTagClass.append(rendererType);   
		generatedTagClass.append(";\n\t}\n");
		generatedTagClass.append("\tpublic String getComponentType() {\n\t\treturn \"");
		generatedTagClass.append(Utility.getComponentType(component)); 
		generatedTagClass.append("\";\n\t}\n");
	}

	private void endComponentClass() {
		addDoTags("Start");
		addDoTags("End");
		addRelease();
		generatedTagClass.append("\n}");
		createJavaFile();
	}

	private void createJavaFile() {
		Component component = (Component) getMetaContext().getActiveClass().getAnnotation(Component.class);
		String tagClass = Utility.getTagClassName(component);
		String fileName = Utility.getSimpleNameOfClass(tagClass) + ".java";
        String path = Utility.getPackagePathOfClass(tagClass);
		FileWriter.write("/generated/support/", path, fileName, generatedTagClass);
	}

	private void addProperties(Class clazz, Component component) {
		addSetters();
		addSetProperties(Utility.getGeneratedClassName(component));
	}

	private void addSetters() {
		//set
		for(PropertyValues prop : getMetaContext().getPropertyValuesSorted()) {
			String type = (prop.expression == Expression.METHOD_EXPRESSION) ?"javax.el.MethodExpression " :"javax.el.ValueExpression ";

            // propertyName can be a reserved Java keyword like "for", so use
            // is to build the getter/setter method name, but not alone
            String propertyName = prop.resolvePropertyName();
            String varName = prop.getJavaVariableName();

			generatedTagClass.append("\tprivate ");
			generatedTagClass.append(type);
			generatedTagClass.append(varName);
			generatedTagClass.append(";\n\tpublic void set");
			generatedTagClass.append(propertyName.substring(0,1).toUpperCase());
			generatedTagClass.append(propertyName.substring(1));
			generatedTagClass.append("(");
			generatedTagClass.append(type);
			generatedTagClass.append(varName);
			generatedTagClass.append(") {\n");
			generatedTagClass.append("\t\tthis."); 
			generatedTagClass.append(varName);
			generatedTagClass.append(" = "); 
			generatedTagClass.append(varName);
			generatedTagClass.append(";\n\t}\n");
		}
	}

	private void addRelease() {
		generatedTagClass.append("\t/**\n\t * <p>Release any allocated tag handler attributes.</p>\n \t */\n");
		generatedTagClass.append("\tpublic void release() {\n");
		generatedTagClass.append("\t\tsuper.release();\n");        

		for(PropertyValues prop : getMetaContext().getPropertyValuesSorted()) {
			generatedTagClass.append("\t\t");
			generatedTagClass.append(prop.getJavaVariableName());
			generatedTagClass.append(" = null;\n"); 
		}
		generatedTagClass.append("\t}");        
	}

	private void addDoTags(String tagName) {
		generatedTagClass.append("\n\tpublic int do");
		generatedTagClass.append(tagName);
		generatedTagClass.append("Tag() throws JspException {\n");
		generatedTagClass.append("\t\ttry {\n\t\t\treturn super.do");
		generatedTagClass.append(tagName);
		generatedTagClass.append("Tag();\n");
		generatedTagClass.append("\t\t} catch (Exception e) {\n\t\t\tThrowable root = e;\t\t\t\n\t\t\twhile (root.getCause() != null) {\n");
		generatedTagClass.append("\t\t\t\troot = root.getCause();\n\t\t\t}\n\t\t\tthrow new JspException(root);\n\t\t}\n\t}\n") ; 
	}

	private void addSetProperties(String componentClass) {
		generatedTagClass.append("\n\tprotected void setProperties(UIComponent component) {\n\t\tsuper.setProperties(component);\n\t\t");
		generatedTagClass.append(componentClass);
		generatedTagClass.append(" _component = null;\n\t\ttry {\n\t\t\t_component = (");
		generatedTagClass.append(componentClass);
		generatedTagClass.append(") component;\n\t\t} catch (ClassCastException cce) {");
		generatedTagClass.append("\n\t\t\tthrow new IllegalStateException(\"Component \" + component.toString() + \" not expected type.  Expected:"); 
		generatedTagClass.append(componentClass);
		generatedTagClass.append("\");\n");
		generatedTagClass.append("\t\t}\n");        
		for(PropertyValues property : getMetaContext().getPropertyValuesSorted()) {
            String propertyName = property.resolvePropertyName();
            String varName = property.getJavaVariableName();
			generatedTagClass.append("\t\tif (");
			generatedTagClass.append(varName);
			generatedTagClass.append(" != null) {\n\t\t\t");
			if (property.expression == Expression.METHOD_EXPRESSION &&
                "actionListener".equals(propertyName)) {
				generatedTagClass.append("_component.addActionListener(new MethodExpressionActionListener(actionListener)");
			} else if (property.expression == Expression.METHOD_EXPRESSION &&
                "action".equals(propertyName)) {
				generatedTagClass.append("_component.setActionExpression(action");
            } else if (property.expression == Expression.METHOD_EXPRESSION &&
                "valueChangeListener".equals(propertyName) &&
                // Any UIInput inherits valueChangeListener, so should use
                // addValueChangeListener, but any component not inheriting it,
                // and just implementing it's own MethodExpression property
                // named valueChangeListener should just use setValueChangeListener
                !getMetaContext().isGeneratingPropertyByName("valueChangeListener")) {
				generatedTagClass.append("_component.addValueChangeListener(new MethodExpressionValueChangeListener(valueChangeListener)");
            } else if (property.expression == Expression.METHOD_EXPRESSION &&
                "validator".equals(propertyName)) {
				generatedTagClass.append("_component.addValidator(new MethodExpressionValidator(valueChangeListener)");
			} else {
				generatedTagClass.append("_component.set");

				if (property.expression == Expression.METHOD_EXPRESSION) {
					generatedTagClass.append(propertyName.substring(0,1).toUpperCase());
					generatedTagClass.append(propertyName.substring(1));
				} else {
					generatedTagClass.append("ValueExpression");            
				}
				generatedTagClass.append("(");
				if (property.expression == Expression.VALUE_EXPRESSION) {
					generatedTagClass.append("\"");
					generatedTagClass.append(propertyName);
					generatedTagClass.append("\", ");
				}
				generatedTagClass.append(varName);  
			}
			generatedTagClass.append(");\n");    
			generatedTagClass.append("\t\t}\n");              
		}
		generatedTagClass.append("\t}\n");        
	}

	public void build() {
		Component component = (Component) getMetaContext().getActiveClass().getAnnotation(Component.class);
        if(Utility.isManualTagClass(component)) return;
		startComponentClass(component);
		addProperties(getMetaContext().getActiveClass(), component);
		endComponentClass();
	}
}
