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

package org.icefaces.ace.generator.utils;

import java.lang.reflect.Field;

import org.icefaces.ace.generator.context.GeneratorContext;
import org.icefaces.ace.meta.annotation.*;

public class Utility {
    public static String getComponentType(Component component) {
        String componentType = component.componentType();
        if (Component.EMPTY.equals(componentType)) {
            try {
                Class extended = Class.forName(component.extendsClass());
                Field comp_type = extended.getField("COMPONENT_TYPE");
                componentType = String.valueOf(comp_type.get(comp_type));            
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return componentType;
    }
    
    public static String getFamily(Component component) {
        String componentFamily = component.componentFamily();
        if (Component.EMPTY.equals(componentFamily)) {
            try {
                Class extended = Class.forName(component.extendsClass());
                Field comp_family = extended.getField("COMPONENT_FAMILY");
                componentFamily = String.valueOf(comp_family.get(comp_family));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return componentFamily;
    }    
    
    public static String getGeneratedClassName(Component component) {
        String generatedClass = component.generatedClass();
        if (generatedClass.equals(Component.EMPTY)) {
            generatedClass = component.componentClass();
        } 
        return generatedClass;
    }
	
    public static String getGeneratedClassName(TagHandler tagHandler) {
        String generatedClass = tagHandler.generatedClass();
        if (generatedClass.equals(TagHandler.EMPTY)) {
            generatedClass = tagHandler.tagHandlerClass();
        } 
        return generatedClass;
    }

    public static String getRendererClassName(Component component) {
        String rendererClass = component.rendererClass();
        if (rendererClass.equals(Component.EMPTY)) {
            rendererClass = component.componentClass()+ "Renderer";
        }
        return rendererClass;
    }

    public static String getTagClassName(Component component) {
        String tagClass = component.tagClass();
        if (tagClass.equals(Component.EMPTY)) {
            tagClass = component.componentClass()+ "Tag";
        }
        return tagClass;
    }

    public static String getHandlerClassName(Component component) {
        String handlerClass = component.handlerClass();
        if (handlerClass.equals(Component.EMPTY)) {
            handlerClass = component.componentClass()+ "Handler";
        }
        return handlerClass;
    }

    public static boolean isManualTagClass(Component component) {
        return !Component.EMPTY.equals(component.tagClass());
    }

    public static boolean isManualHandlerClass(Component component) {
        return !Component.EMPTY.equals(component.handlerClass());
    }

	public static String getTagHandlerExtendsClassName(TagHandler tagHandler) {
		String extendsClass = tagHandler.extendsClass();
		if (extendsClass.equals(TagHandler.EMPTY)) {
			extendsClass = getDefaultTagHandlerExtendsClassName(tagHandler.tagHandlerType());
		}
		return extendsClass;
	}
	
	public static String getDefaultTagHandlerExtendsClassName(TagHandlerType type) {
	
		String classPackage = "javax.faces.view.facelets.";
		switch(type) {
			case ATTRIBUTE_HANDLER:
				return classPackage+"AttributeHandler";
			case BEHAVIOR_HANDLER:
				return classPackage+"BehaviorHandler";
			case COMPONENT_HANDLER:
				return classPackage+"ComponentHandler";
			case COMPOSITE_FACELET_HANDLER:
				return classPackage+"CompositeFaceletHandler";
			case CONVERTER_HANDLER:
				return classPackage+"ConverterHandler";
			case DELEGATING_META_TAG_HANDLER:
				return classPackage+"DelegatingMetaTagHandler";
			case FACELET_HANDLER:
				return classPackage+"FaceletHandler";
			case FACELETS_ATTACHED_OBJECT_HANDLER:
				return classPackage+"FaceletsAttachedObjectHandler";
			case FACET_HANDLER:
				return classPackage+"FacetHandler";
			case META_TAG_HANDLER:
				return classPackage+"MetaTagHandler";
			case TAG_HANDLER:
				return classPackage+"TagHandler";
			case TEXT_HANDLER:
				return classPackage+"TextHandler";
			case VALIDATOR_HANDLER:
				return classPackage+"ValidatorHandler";
			default:
				return classPackage+"TagHandler";
		}
	}
	
	public static String getDefaultTagHandlerConfigClass(TagHandlerType type) {
	
		String classPackage = "javax.faces.view.facelets.";
		switch(type) {
			case BEHAVIOR_HANDLER:
				return classPackage+"BehaviorConfig";
			case COMPONENT_HANDLER:
				return classPackage+"ComponentConfig";
			case CONVERTER_HANDLER:
				return classPackage+"ConverterConfig";
			case VALIDATOR_HANDLER:
				return classPackage+"ValidatorConfig";
			default:
				return classPackage+"TagConfig";
		}
	}

    /**
     * @see org.icefaces.ace.meta.annotation.JSP#tagName()
     * @return JSP.tagName(), or if not specified, then Meta class' simple
     * name, removing "Meta" suffix, using camel case.
     * Eg: MyCompMeta -> myComp
     */
    public static String getTagName(Class metaClass, JSP jsp) {
        String tagName = jsp.tagName();
        if (tagName.equals(JSP.EMPTY)) {
            StringBuilder sb = removeSuffix(new StringBuilder(metaClass.getSimpleName()), "Meta");
            if (sb.length() >= 1) {
                sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
            }
            tagName = sb.toString();
        }
        return tagName;
    }

    /**
     * @see org.icefaces.ace.meta.annotation.JSP#tagClass()
     * @return JSP.tagClass(), or if not specified, then Meta class' name
     * removing "Meta" suffix, adding "Tag" suffix.
     * Eg: org.mypackage.MyCompMeta -> org.mypackage.MyCompTag
     */
    public static String getTagClassName(Class metaClass, JSP jsp) {
        return getSpecifiedOrAlternate(jsp.tagClass(), JSP.EMPTY,
            metaClass.getName(), "Meta", "", "Tag");
    }

    /**
     * @see org.icefaces.ace.meta.annotation.JSP#generatedTagClass()
     * @return JSP.generatedTagClass(), or if not specified, then Meta class'
     * name removing "Meta" suffix, adding "BaseTag" suffix.
     * Eg: org.mypackage.MyCompMeta -> org.mypackage.MyCompBaseTag
     */
    public static String getGeneratedTagClassName(Class metaClass, JSP jsp) {
        return getSpecifiedOrAlternate(jsp.generatedTagClass(), JSP.EMPTY,
            metaClass.getName(), "Meta", "", "BaseTag");
    }

    /**
     * @see org.icefaces.ace.meta.annotation.JSP#generatedInterfaceClass()
     * @return If the metaClass has a JSP annotation, then check for
     * JSP.generatedInterfaceClass(), or if not specified, then Meta class'
     * name removing "Meta" suffix, adding "I" prefix to simple name part.
     * Eg: org.mypackage.MyCompMeta -> org.mypackage.IMyComp
     */
    public static String getGeneratedInterfaceClassName(Class metaClass) {
        String jspGeneratedInterfaceClass = JSP.EMPTY;
        if (metaClass.isAnnotationPresent(JSP.class)) {
            JSP jsp = (JSP) metaClass.getAnnotation(JSP.class);
            jspGeneratedInterfaceClass = jsp.generatedInterfaceClass();
        }
        return getSpecifiedOrAlternate(jspGeneratedInterfaceClass, JSP.EMPTY,
            metaClass.getName(), "Meta", "I", "");
    }

    /**
     * @see org.icefaces.ace.meta.annotation.JSP#generatedTagExtendsClass()
     * @see org.icefaces.ace.meta.annotation.JSPBaseMeta#tagClass()
     * @return JSP.generatedTagExtendsClass(), or if not specified, then see if
     * the Meta class' superclass has a @JSPBaseMeta annotation, and use its
     * tagClass, otherwise fallback to javax.servlet.jsp.tagext.TagSupport.
     */
    public static String getGeneratedTagExtendsClassName(
            Class metaClass, JSP jsp) {
        String generatedTagExtendsClass = jsp.generatedTagExtendsClass();
        if (generatedTagExtendsClass.equals(JSP.EMPTY)) {
            Class superClass = metaClass.getSuperclass();
            if (superClass.isAnnotationPresent(JSPBaseMeta.class)) {
                JSPBaseMeta jspBaseMeta = (JSPBaseMeta)
                    superClass.getAnnotation(JSPBaseMeta.class);
                generatedTagExtendsClass = jspBaseMeta.tagClass();
            } else {
                generatedTagExtendsClass = "javax.servlet.jsp.tagext.TagSupport";
            }
        }
        return generatedTagExtendsClass;
    }

    /**
     * @see org.icefaces.ace.meta.annotation.JSP#generatedInterfaceExtendsClass()
     * @see org.icefaces.ace.meta.annotation.JSPBaseMeta#interfaceClass()
     * @return If the metaClass has a JSP annotation, then check for
     * JSP.generatedInterfaceExtendsClass(), or if not specified, then see if
     * the Meta class' superclass has a @JSPBaseMeta annotation, and use its
     * interfaceClass, which may be unspecified as well. Can be null or an empty String.
     */
    public static String getGeneratedInterfaceExtendsClassName(Class metaClass) {
        String generatedInterfaceExtendsClass = JSP.EMPTY;
        if (metaClass.isAnnotationPresent(JSP.class)) {
            JSP jsp = (JSP) metaClass.getAnnotation(JSP.class);
            generatedInterfaceExtendsClass = jsp.generatedInterfaceExtendsClass();
        }
        if (generatedInterfaceExtendsClass.equals(JSP.EMPTY)) {
            Class superClass = metaClass.getSuperclass();
            if (superClass.isAnnotationPresent(JSPBaseMeta.class)) {
                JSPBaseMeta jspBaseMeta = (JSPBaseMeta)
                    superClass.getAnnotation(JSPBaseMeta.class);
                if (!JSPBaseMeta.EMPTY.equals(jspBaseMeta.interfaceClass())) {
                    generatedInterfaceExtendsClass = jspBaseMeta.interfaceClass();
                }
            }
        }
        return generatedInterfaceExtendsClass;
    }

    /**
     * @see org.icefaces.ace.meta.annotation.JSP#bodyContent()
     * @see org.icefaces.ace.meta.annotation.JSPBaseMeta#bodyContent()
     * @return JSP.bodyContent(), or if not specified, then see if
     * the Meta class' superclass has a @JSPBaseMeta annotation, and use its
     * bodyContent, otherwise fallback to BodyContent.JSP.
     */
    public static String getBodyContentString(Class metaClass, JSP jsp) {
        BodyContent bodyContent = jsp.bodyContent();
        if (bodyContent.equals(BodyContent.UNSET)) {
            Class superClass = metaClass.getSuperclass();
            if (superClass.isAnnotationPresent(JSPBaseMeta.class)) {
                JSPBaseMeta jspBaseMeta = (JSPBaseMeta)
                    superClass.getAnnotation(JSPBaseMeta.class);
                bodyContent = jspBaseMeta.bodyContent();
            }
        }
        return bodyContent.toString();
    }

    protected static String getSpecifiedOrAlternate(String specified,
            String unspecified, String alternate, String removeSuffix,
            String insertSimpleNamePrefix, String addSuffix) {
        if (specified.equals(unspecified)) {
            StringBuilder sb = removeSuffix(new StringBuilder(alternate),
                removeSuffix).append(addSuffix);
            if (insertSimpleNamePrefix != null &&
                insertSimpleNamePrefix.length() > 0) {
                int index = sb.lastIndexOf(".");
                if (index >= 0) {
                    sb.insert(index+1, insertSimpleNamePrefix);
                }
            }
            specified = sb.toString();
        }
        return specified;
    }

    public static StringBuilder removeSuffix(StringBuilder sb, String suffix) {
        // if (sb.endsWith(suffix)) then remove it
        if (suffix.contentEquals(sb.subSequence(sb.length()-suffix.length(), sb.length()))) {
            sb.delete(sb.length()-suffix.length(), sb.length());
        }
        return sb;
    }

    public static String getSimpleNameOfClass(String className) {
        int classIndicator = className.lastIndexOf(".");
        return className.substring(classIndicator+1);
    }

    public static String getPackageNameOfClass(String className) {
        int classIndicator = className.lastIndexOf(".");
        if (classIndicator >= 0) {
            return className.substring(0, classIndicator);
        }
        return "";
    }

    public static String getPackagePathOfClass(String className) {
        String pack = getPackageNameOfClass(className);
        String path = pack.replace('.', '/') + '/'; //substring(0, pack.lastIndexOf('.'));
        return path;
    }

    public static String getGeneratedType(PropertyValues prop) {
        String propertyName = prop.resolvePropertyName();
        return getGeneratedType(propertyName, prop.field);
    }

    public static String getGeneratedType(String propertyName, Field field) {
        boolean isPrimitive = field.getType().isPrimitive() ||
                              GeneratorContext.SpecialReturnSignatures.containsKey(propertyName);

        String returnAndArgumentType = getArrayAwareType(field);

        // If primitive property, get the primitive return type
        // otherwise leave it as is.
        if (isPrimitive) {
            String fieldTypeName = field.getType().getName();
            if (GeneratorContext.WrapperTypes.containsKey(fieldTypeName)) {
                returnAndArgumentType = GeneratorContext.WrapperTypes.get(fieldTypeName);
            }
        }
        return returnAndArgumentType;
    }

    public static String getArrayAwareType(Field field) {
        boolean isArray = field.getType().isArray();
        return isArray ? field.getType().getComponentType().getName() + "[]"
                       : field.getType().getName();
    }

    public static String getJavaDocComment(String propertyName, boolean isSetter, String doc) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\t/**\n");
        if (isSetter) {
            sb.append("\t * <p>Set the value of the <code>");
        } else {
            sb.append("\t * <p>Return the value of the <code>");
        }
        sb.append(propertyName);
        sb.append("</code> property.</p>");
        if (doc != null && !"".equals(doc)) {
            sb.append("\n\t * <p>Contents: ");
            appendJavaDocLines(sb, doc, true);
        }
        sb.append("\n\t */\n");
        return sb.toString();
    }

    public static String getJavaDocComment(String usePrimarily, String defaultToOtherwise) {
        StringBuilder sb = new StringBuilder();
        String doc = (usePrimarily != null && !usePrimarily.equals("null") &&
            usePrimarily.length() > 0) ? usePrimarily : defaultToOtherwise;
        if (doc != null && !"".equals(doc)) {
            sb.append("\n/**");
            appendJavaDocLines(sb, doc, false);
            sb.append("\n */\n");
        }
        return sb.toString();
    }

    public static void appendJavaDocLines(StringBuilder sb, String doc, boolean isForProperty) {
        String[] lines = doc.split("\n");
        for (int j=0; j < lines.length; j++){
            if (isForProperty && j>0) {
                sb.append("\n\t * ");
            } else if (!isForProperty) {
                sb.append("\n * ");
            }
            sb.append(lines[j]);
            if (isForProperty && j == (lines.length-1)) {
                sb.append("</p>");
            }
        }
    }
}
