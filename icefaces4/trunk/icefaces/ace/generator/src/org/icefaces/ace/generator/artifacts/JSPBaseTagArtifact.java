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

import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.generator.context.JSPContext;
import org.icefaces.ace.generator.utils.FileWriter;
import org.icefaces.ace.generator.utils.Utility;

import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.JSP;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JSPBaseTagArtifact extends Artifact {
	private StringBuilder generatedTagClass;

	public JSPBaseTagArtifact(JSPContext metaContext) {
		super(metaContext);
	}

	private void startBaseTagClass(JSP jsp) {
		generatedTagClass = new StringBuilder();

        Class metaClass = getMetaContext().getActiveClass();
        String tagClassName = Utility.getTagClassName(metaClass, jsp);
        String generatedTagClassName = Utility.getGeneratedTagClassName(
            metaClass, jsp);
        String generatedTagExtendsClassName =
            Utility.getGeneratedTagExtendsClassName(metaClass, jsp);
        String interfaceClassName = Utility.getGeneratedInterfaceClassName(
            metaClass);

		generatedTagClass.append("package ");
		generatedTagClass.append(Utility.getPackageNameOfClass(generatedTagClassName));
		generatedTagClass.append(";\n\n");

////		generatedTagClass.append("import javax.servlet.jsp.JspException;\n\n");
        generatedTagClass.append("import ").append(generatedTagExtendsClassName).append(";\n");
        generatedTagClass.append("import ").append(interfaceClassName).append(";\n");
		generatedTagClass.append("/*\n * ******* GENERATED CODE - DO NOT EDIT *******\n */\n");
        generatedTagClass.append(Utility.getJavaDocComment(jsp.javadoc(), jsp.tlddoc()));
		generatedTagClass.append("public ");
        if (!tagClassName.equals(generatedTagClassName)) {
            generatedTagClass.append("abstract ");
        }
        generatedTagClass.append("class ");
		generatedTagClass.append(Utility.getSimpleNameOfClass(generatedTagClassName));
		generatedTagClass.append(" extends ");
        generatedTagClass.append(Utility.getSimpleNameOfClass(generatedTagExtendsClassName));
        generatedTagClass.append(" implements ");
        generatedTagClass.append(Utility.getSimpleNameOfClass(interfaceClassName));
        generatedTagClass.append(" {\n");
	}

	private void endBaseTagClass(JSP jsp) {
		addRelease(jsp);
		generatedTagClass.append("}\n");
	}

	private void createJavaFile(JSP jsp) {
		String generatedTagClassName = Utility.getGeneratedTagClassName(getMetaContext().getActiveClass(), jsp);
		String fileName = Utility.getSimpleNameOfClass(generatedTagClassName) + ".java";
        String path = Utility.getPackagePathOfClass(generatedTagClassName);
		FileWriter.write("/generated-jsp/base/", path, fileName, generatedTagClass);        
	}

	private void addProperties(Class clazz, JSP jsp) {
        addPropertyDeclarations();
        addFieldDeclarations();
		addPropertyGetterSetters();
        addFieldGetterSetters();
	}

    private void addPropertyDeclarations() {
        List<PropertyValues> generatingProps = getMetaContext().getGeneratingPropertyValuesSorted();
        if (generatingProps.size() > 0) {
            generatedTagClass.append("\t// Properties\n");
        }
        for(PropertyValues prop : generatingProps) {
            String returnAndArgumentType = Utility.getGeneratedType(prop);
            generatedTagClass.append("\tprotected ");
            generatedTagClass.append(returnAndArgumentType);
            generatedTagClass.append(' ');
            generatedTagClass.append(prop.getJavaVariableName());
            generatedTagClass.append(";\n");
        }
    }

    private void addFieldDeclarations() {
        List<Field> internalFields = getMetaContext().getInternalFieldsSorted();
        if (internalFields.size() > 0) {
            generatedTagClass.append("\n\t// Fields\n");
        }
        for(Field field : internalFields) {
            // private String propName;
            String returnAndArgumentType = Utility.getGeneratedType(field.getName(), field);
            generatedTagClass.append("\tprotected ");
            generatedTagClass.append(returnAndArgumentType);
            generatedTagClass.append(' ');
            generatedTagClass.append(field.getName());
            generatedTagClass.append(";\n");
        }
    }

	private void addPropertyGetterSetters() {
		for(PropertyValues prop : getMetaContext().getGeneratingPropertyValuesSorted()) {
            // propertyName can be a reserved Java keyword like "for", so use
            // is to build the getter/setter method name, but not alone
            String propertyName = prop.resolvePropertyName();
            addSetterGetter(propertyName, prop.getJavaVariableName(),
                prop.field, prop.javadocSet, prop.javadocGet);
		}
	}

    private void addFieldGetterSetters() {
        for (Field field : getMetaContext().getInternalFieldsSorted()) {
            org.icefaces.ace.meta.annotation.Field fieldAnnot =
                (org.icefaces.ace.meta.annotation.Field) field.getAnnotation(
                    org.icefaces.ace.meta.annotation.Field.class);
            addSetterGetter(field.getName(), field.getName(), field,
                fieldAnnot.javadoc(), fieldAnnot.javadoc());
        }
    }

    private void addSetterGetter(String propertyName, String varName, Field field, String setDoc, String getDoc) {
        String returnAndArgumentType = Utility.getGeneratedType(propertyName, field);

        boolean isBoolean = field.getType().equals(Boolean.class) ||
                            field.getType().equals(Boolean.TYPE);

        // The publicly exposed property name. Will differ from the field name
        // if the field name is a java keyword
        String camelCaseMethodName = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
        String setMethodName = "set" + camelCaseMethodName;
        String getMethodName = (isBoolean ? "is" : "get") + camelCaseMethodName;

        generatedTagClass.append(Utility.getJavaDocComment(propertyName, true, setDoc));
        generatedTagClass.append("\tpublic void ");
        generatedTagClass.append(setMethodName);
        generatedTagClass.append("(");
        generatedTagClass.append(returnAndArgumentType);
        generatedTagClass.append(" ");
        generatedTagClass.append(varName);
        generatedTagClass.append(") {\n");
        generatedTagClass.append("\t\tthis.");
        generatedTagClass.append(varName);
        generatedTagClass.append(" = ");
        generatedTagClass.append(varName);
        generatedTagClass.append(";\n\t}\n");

        generatedTagClass.append(Utility.getJavaDocComment(propertyName, false, getDoc));
        generatedTagClass.append("\tpublic ");
        generatedTagClass.append(returnAndArgumentType);
        generatedTagClass.append(" ");
        generatedTagClass.append(getMethodName);
        generatedTagClass.append("() {\n");
        generatedTagClass.append("\t\treturn this.");
        generatedTagClass.append(varName);
        generatedTagClass.append(";\n\t}\n");
    }

	private void addRelease(JSP jsp) {
        // Don't add a release method unless it exists in a super-class
        // BodyTagSupport has it but SimpleTagSupport does not, and who knows
        // about generatedTagExtendsClass, which could inherit from anything.
        try {
            String generatedTagExtendsClassName =
                Utility.getGeneratedTagExtendsClassName(
                    getMetaContext().getActiveClass(), jsp);
            Class extendsClass = Class.forName(generatedTagExtendsClassName);
            Method releaseMethod = extendsClass.getMethod("release", new Class[0]);
            if (releaseMethod.getReturnType() != Void.TYPE) {
                return;
            }
        } catch(Exception e) {
            return;
        }

		generatedTagClass.append("\n\tpublic void release() {\n");
		generatedTagClass.append("\t\tsuper.release();\n");

		for(PropertyValues prop : getMetaContext().getGeneratingPropertyValuesSorted()) {
            boolean isPrimitive = prop.field.getType().isPrimitive() ||
                GeneratorContext.SpecialReturnSignatures.containsKey(
                    prop.resolvePropertyName());
            if (!isPrimitive) {
                generatedTagClass.append("\t\t");
                generatedTagClass.append(prop.getJavaVariableName());
                generatedTagClass.append(" = null;\n");
            }
		}
        for(Field field : getMetaContext().getInternalFieldsSorted()) {
            boolean isPrimitive = field.getType().isPrimitive() ||
                GeneratorContext.SpecialReturnSignatures.containsKey(
                    field.getName());
            if (!isPrimitive) {
                generatedTagClass.append("\t\t");
                generatedTagClass.append(field.getName());
                generatedTagClass.append(" = null;\n");
            }
        }
		generatedTagClass.append("\t}\n");
	}

	public void build() {
		JSP jsp = (JSP) getMetaContext().getActiveClass().getAnnotation(JSP.class);
		startBaseTagClass(jsp);
		addProperties(getMetaContext().getActiveClass(), jsp);
		endBaseTagClass(jsp);
        createJavaFile(jsp);

        GeneratorContext.getInstance().getJspTldBuilder().addTagInfo(
            getMetaContext().getActiveClass(), jsp);
        for(PropertyValues prop : getMetaContext().getPropertyValuesSorted()) {
            GeneratorContext.getInstance().getJspTldBuilder().addAttributeInfo(prop);
        }
	}
}
