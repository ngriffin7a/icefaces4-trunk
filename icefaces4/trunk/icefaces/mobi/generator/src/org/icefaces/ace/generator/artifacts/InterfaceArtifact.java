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

import org.icefaces.ace.generator.context.InterfaceContext;
import org.icefaces.ace.generator.utils.FileWriter;
import org.icefaces.ace.generator.utils.Utility;

import org.icefaces.ace.generator.utils.PropertyValues;

import java.lang.reflect.Field;

public class InterfaceArtifact extends Artifact {
	private StringBuilder generatedInterface;

	public InterfaceArtifact(InterfaceContext metaContext) {
		super(metaContext);
	}

	private void startInterface(String generatedInterfaceName, String generatedInterfaceExtends) {
		generatedInterface = new StringBuilder();

		generatedInterface.append("package ");
		generatedInterface.append(Utility.getPackageNameOfClass(generatedInterfaceName));
		generatedInterface.append(";\n\n");

		generatedInterface.append("/*\n * ******* GENERATED CODE - DO NOT EDIT *******\n */\n");
		generatedInterface.append("public interface ");
		generatedInterface.append(Utility.getSimpleNameOfClass(generatedInterfaceName));
        if (generatedInterfaceExtends != null && generatedInterfaceExtends.length() > 0) {
		    generatedInterface.append(" extends ");
            generatedInterface.append(generatedInterfaceExtends);
        }
        generatedInterface.append(" {\n");
	}
    
	private void addPropertyGetterSetters() {
		for(PropertyValues prop : getMetaContext().
            getIntersectionOfOnlyTypesGeneratingPropertyValuesSorted()) {
            // propertyName can be a reserved Java keyword like "for", so use
            // is to build the getter/setter method name, but not alone
            String propertyName = prop.resolvePropertyName();
            addSetterGetter(propertyName, prop.getJavaVariableName(), prop.field);
		}
	}

    private void addFieldGetterSetters() {
        for (Field field : getMetaContext().getInternalFieldsSorted()) {
            addSetterGetter(field.getName(), field.getName(), field);
        }
    }

    private void addSetterGetter(String propertyName, String varName, Field field) {
        String returnAndArgumentType = Utility.getGeneratedType(propertyName, field);

        boolean isBoolean = field.getType().equals(Boolean.class) ||
                            field.getType().equals(Boolean.TYPE);

        // The publicly exposed property name. Will differ from the field name
        // if the field name is a java keyword
        String camlCaseMethodName = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
        String setMethodName = "set" + camlCaseMethodName;
        String getMethodName = (isBoolean ? "is" : "get") + camlCaseMethodName;

        generatedInterface.append("\n\tpublic void ");
        generatedInterface.append(setMethodName);
        generatedInterface.append("(");
        generatedInterface.append(returnAndArgumentType);
        generatedInterface.append(" ");
        generatedInterface.append(varName);
        generatedInterface.append(");\n");

        generatedInterface.append("\n\tpublic ");
        generatedInterface.append(returnAndArgumentType);
        generatedInterface.append(" ");
        generatedInterface.append(getMethodName);
        generatedInterface.append("();\n");
    }

    private void endInterface() {
        generatedInterface.append("}\n");
    }

    private void createJavaFile(String generatedInterfaceName) {
        String fileName = Utility.getSimpleNameOfClass(generatedInterfaceName) + ".java";
        String path = Utility.getPackagePathOfClass(generatedInterfaceName);
        FileWriter.write("/generated-interfaces/", path, fileName, generatedInterface);
    }

	public void build() {
        Class metaClass = getMetaContext().getActiveClass();
        String generatedInterfaceName = Utility.getGeneratedInterfaceClassName(
            metaClass);
        String generatedInterfaceExtends =
            Utility.getGeneratedInterfaceExtendsClassName(metaClass);

		startInterface(generatedInterfaceName, generatedInterfaceExtends);
        addPropertyGetterSetters();
        addFieldGetterSetters();
		endInterface();
        createJavaFile(generatedInterfaceName);
	}
}
