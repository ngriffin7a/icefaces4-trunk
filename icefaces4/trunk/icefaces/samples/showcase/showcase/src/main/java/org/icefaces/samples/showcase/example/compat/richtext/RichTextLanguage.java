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

package org.icefaces.samples.showcase.example.compat.richtext;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = RichTextBean.BEAN_NAME,
        title = "example.compat.richtext.language.title",
        description = "example.compat.richtext.language.description",
        example = "/resources/examples/compat/richtext/richtextLanguage.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="richtextLanguage.xhtml",
                    resource = "/resources/examples/compat/"+
                               "richtext/richtextLanguage.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="RichTextLanguage.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/richtext/RichTextLanguage.java")
        }
)
@ManagedBean(name= RichTextLanguage.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class RichTextLanguage extends ComponentExampleImpl<RichTextLanguage> implements Serializable {
	
    public static final String BEAN_NAME = "richtextLanguage";

    private String text;
    private SelectItem[] availableLanguages = new SelectItem[] {
        new SelectItem("en", "English"),
        new SelectItem("en-au", "English (Australia)"),
        new SelectItem("en-ca", "English (Canadian)"),
        new SelectItem("en-gb", "English (United Kingdom)"),
        new SelectItem("af", "Afrikaans"),
        new SelectItem("ar", "Arabic"),
        new SelectItem("eu", "Basque"),
        new SelectItem("bn", "Bengali/Bangla"),
        new SelectItem("bs", "Bosnian"),
        new SelectItem("ca", "Catalan"),
        new SelectItem("zh-cn", "Chinese Simplified"),
        new SelectItem("zh", "Chinese Traditional"),
        new SelectItem("hr", "Croatian"),
        new SelectItem("cs", "Czech"),
        new SelectItem("da", "Danish"),
        new SelectItem("nl", "Dutch"),
        new SelectItem("eo", "Esperanto"),
        new SelectItem("et", "Estonian"),
        new SelectItem("fo", "Faroese"),
        new SelectItem("fi", "Finnish"),
        new SelectItem("fr", "French"),
        new SelectItem("gl", "Galician"),
        new SelectItem("de", "German"),
        new SelectItem("el", "Greek"),
        new SelectItem("he", "Hebrew"),
        new SelectItem("hi", "Hindi"),
        new SelectItem("hu", "Hungarian"),
        new SelectItem("it", "Italian"),
        new SelectItem("ja", "Japanese"),
        new SelectItem("km", "Khmer"),
        new SelectItem("ko", "Korean"),
        new SelectItem("lv", "Latvian"),
        new SelectItem("lt", "Lithuanian"),
        new SelectItem("ms", "Malay"),
        new SelectItem("mn", "Mongolian"),
        new SelectItem("no", "Norwegian"),
        new SelectItem("nb", "Norwegian Bokmal"),
        new SelectItem("fa", "Persian"),
        new SelectItem("pl", "Polish"),
        new SelectItem("pt-br", "Portuguese (Brazil)"),
        new SelectItem("pt", "Portuguese (Portugal)"),
        new SelectItem("ro", "Romanian"),
        new SelectItem("ru", "Russian"),
        new SelectItem("sr", "Serbian (Cyrillic)"),
        new SelectItem("sr-latn", "Serbian (Latin)"),
        new SelectItem("sk", "Slovak"),
        new SelectItem("sl", "Slovenian"),
        new SelectItem("es", "Spanish"),
        new SelectItem("sv", "Swedish"),
        new SelectItem("th", "Thai"),
        new SelectItem("tr", "Turkish"),
        new SelectItem("uk", "Ukrainian"),
        new SelectItem("vi", "Vietnamese")
    };
    private String language = availableLanguages[0].getValue().toString();	

    public RichTextLanguage() {
            super(RichTextLanguage.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getText() { return text; }
    public String getLanguage() { return language; }
    public SelectItem[] getAvailableLanguages() { return availableLanguages; }

    public void setText(String text) { this.text = text; }
    public void setLanguage(String language) { this.language = language; }
}
