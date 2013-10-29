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

/*
 * Template Generator Run time faces-config.xml jsf11
 */
package com.icesoft.jsfmeta.templates.jsf11;

import com.icesoft.jsfmeta.templates.AbstractTempGen;
import com.icesoft.jsfmeta.util.GeneratorUtil;
import com.sun.rave.jsfmeta.beans.ComponentBean;
import com.sun.rave.jsfmeta.beans.RendererBean;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.CollectionModel;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fye
 */
public class TempGenRTFacesConfig11 extends AbstractTempGen {

    private void buildModel() {
        Writer out = null;
        try {
            Map root = defaultMap();
            ComponentBean cbs[] = getFacesConfigBean().getComponents();
            getSortedComponentBeans(cbs);
            List rendererBeanList = new ArrayList();
            List compList = new ArrayList();

            for (int i = 0; i < cbs.length; i++) {
                if (!cbs[i].getComponentClass().startsWith("javax.faces.component")) {
                    RendererBean rendererBean = renderer(cbs[i]);
                    if(rendererBean != null && rendererBean.getRendererType() != null && rendererBean.getRendererClass() != null){
                        rendererBeanList.add(rendererBean);
                    }
                    compList.add(cbs[i]);
                }
            }

            CollectionModel collectionModel = new CollectionModel(rendererBeanList, new BeansWrapper());
            root.put("rendererBeans", collectionModel);

            CollectionModel rCM = new CollectionModel(compList, new BeansWrapper());
            root.put("componentBeans", rCM);
            //String dest = getInternalConfig().getProperty("project.template.jsf11.rt.conf.dir");
            File destDir = GeneratorUtil.getDestFolder(GeneratorUtil.getWorkingFolder() + "conf");
            File outputFile = new File(destDir, "faces-config.xml");
            outputFile.mkdirs();
            outputFile.delete();
            out = new BufferedWriter(new FileWriter(outputFile));
            String tmplFileName = "faces-config-rt.ftl";
            Template template = getConfiguration("resources").getTemplate(tmplFileName);
            template.process(root, out);

        } catch (NullPointerException ex) {
            Logger.getLogger(TempGenRTFacesConfig11.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(TempGenRTFacesConfig11.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TempGenRTFacesConfig11.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TempGenRTFacesConfig11.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(TempGenRTFacesConfig11.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void genConf() {
        buildModel();
    }
}
