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

import java.util.ArrayList;

import org.icefaces.ace.generator.context.TagHandlerContext;
import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.generator.utils.FileWriter;
import org.icefaces.ace.generator.utils.Utility;

import org.icefaces.ace.generator.utils.PropertyValues;
import org.icefaces.ace.meta.annotation.TagHandler;
import org.icefaces.ace.meta.annotation.TagHandlerType;

public class TagHandlerArtifact extends Artifact{
    private StringBuilder generatedTagHandlerClass;
	
	public TagHandlerArtifact(TagHandlerContext tagHandlerContext) {
		super(tagHandlerContext);
	}

	@Override
	public void build() {
        TagHandler tagHandler = (TagHandler) getMetaContext().getActiveClass().getAnnotation(TagHandler.class);
        startTagHandlerClass(getMetaContext().getActiveClass(), tagHandler);
		addApplyMethod(tagHandler);
        endTagHandlerClass();

        GeneratorContext.getInstance().getFacesConfigBuilder().addTagHandlerEntry(tagHandler);

        GeneratorContext.getInstance().getJsfTldBuilder().addTagInfo(getMetaContext().getActiveClass(), tagHandler);

        if (tagHandler.tagHandlerType() == TagHandlerType.BEHAVIOR_HANDLER) {
            GeneratorContext.getInstance().getFaceletTagLibBuilder().addBehaviorInfo(tagHandler);
        } else {
            GeneratorContext.getInstance().getFaceletTagLibBuilder().addTagInfo(tagHandler);
        }
        
		for(PropertyValues prop : getMetaContext().getPropertyValuesSorted()) {
			GeneratorContext.getInstance().getJsfTldBuilder().addAttributeInfo(prop);
			GeneratorContext.getInstance().getFaceletTagLibBuilder().addAttributeInfo(prop);
        }
	}
    
    private void startTagHandlerClass(Class clazz, TagHandler tagHandler) {
        //initialize
        generatedTagHandlerClass = new StringBuilder();
        
		String className = Utility.getGeneratedClassName(tagHandler);
        int classIndicator = className.lastIndexOf(".");
        generatedTagHandlerClass.append("package ");
        generatedTagHandlerClass.append(className.substring(0, classIndicator));
        generatedTagHandlerClass.append(";\n\n");
		generatedTagHandlerClass.append("import javax.faces.view.facelets.TagAttribute;\n");

        generatedTagHandlerClass.append("/*\n * ******* GENERATED CODE - DO NOT EDIT *******\n */\n");
        generatedTagHandlerClass.append(Utility.getJavaDocComment(tagHandler.javadoc(), tagHandler.tlddoc()));

        generatedTagHandlerClass.append("public class ");
        generatedTagHandlerClass.append(className.substring(classIndicator+1));
        generatedTagHandlerClass.append(" extends ");
		generatedTagHandlerClass.append(Utility.getTagHandlerExtendsClassName(tagHandler));
        generatedTagHandlerClass.append(" {\n");

        if (!tagHandler.behaviorId().equals(TagHandler.EMPTY)) {
            generatedTagHandlerClass.append("\tpublic static final String BEHAVIOR_ID = \"");
            generatedTagHandlerClass.append(tagHandler.behaviorId());
            generatedTagHandlerClass.append("\";\n");
        }

        ArrayList<PropertyValues> allProperties = getMetaContext().getPropertyValuesSorted();
		for(PropertyValues prop : allProperties) {
			generatedTagHandlerClass.append("\n\tprotected final TagAttribute ");
			generatedTagHandlerClass.append(prop.getJavaVariableName());
			generatedTagHandlerClass.append(";");
        }
        generatedTagHandlerClass.append("\n");
		
        generatedTagHandlerClass.append("\n\tpublic ");
        generatedTagHandlerClass.append(className.substring(classIndicator+1));   
        generatedTagHandlerClass.append("(");
		generatedTagHandlerClass.append(Utility.getDefaultTagHandlerConfigClass(tagHandler.tagHandlerType()));
		generatedTagHandlerClass.append(" config) {\n");
        generatedTagHandlerClass.append("\t\tsuper(config);\n");
		
        for(PropertyValues prop : allProperties) {
			generatedTagHandlerClass.append("\t\t");
			generatedTagHandlerClass.append(prop.getJavaVariableName());
			generatedTagHandlerClass.append(" = this.getAttribute(\"");
			generatedTagHandlerClass.append(prop.resolvePropertyName());
			generatedTagHandlerClass.append("\");\n");
        }
		
        generatedTagHandlerClass.append("\t}\n");
    }

    
    private void endTagHandlerClass() {
        generatedTagHandlerClass.append("\n}");
        createJavaFile();

    }

    private void createJavaFile() {
        TagHandler tagHandler = (TagHandler) getMetaContext().getActiveClass().getAnnotation(TagHandler.class);
		String className = Utility.getGeneratedClassName(tagHandler);
		int classNameIndex = className.lastIndexOf(".");
        String fileName = className.substring(classNameIndex+1) + ".java";
		String pack = className.substring(0, classNameIndex);;
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
        System.out.println("_________________________________________________________________________");
        System.out.println("File name "+ fileName);
        System.out.println("path  "+ path);        
        FileWriter.write("/generated/base/", path, fileName, generatedTagHandlerClass);        
    }
	
	/**
	 * Add an empty apply() method so that the generated Base class complies with the requirements 
	 * for extending the TagHandler abstract class.
	 */
	private void addApplyMethod(TagHandler tagHandler) {
	
		if (tagHandler.tagHandlerType() == TagHandlerType.TAG_HANDLER) {
			generatedTagHandlerClass.append("\n");
			generatedTagHandlerClass.append("\tpublic void apply(javax.faces.view.facelets.FaceletContext ctx,");
			generatedTagHandlerClass.append(" javax.faces.component.UIComponent parent) throws java.io.IOException { }");
			generatedTagHandlerClass.append("\n");
		}
	}
}
