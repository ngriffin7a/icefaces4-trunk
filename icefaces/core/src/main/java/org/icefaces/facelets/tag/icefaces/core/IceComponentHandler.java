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

package org.icefaces.facelets.tag.icefaces.core;

import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.ComponentConfig;

/**
 * @author Mark Collette
 * @since 1.6
 */
public class IceComponentHandler extends ComponentHandler {
    public IceComponentHandler(ComponentConfig componentConfig) {
        super(componentConfig);
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset m = super.createMetaRuleset(type);
        if( tag.getNamespace() != null &&
            tag.getNamespace().equals("http://www.icesoft.com/icefaces/component") )
        {
            if( tag.getLocalName().equals("inputFile") ) {
                try {
                m.addRule( new MethodRule("progressListener", null, new Class[] {
                        Class.forName("java.util.EventObject" ) }) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("outputChart") ) {
                try {
                    m.addRule( new MethodRule("renderOnSubmit", Boolean.TYPE,
                                             new Class[] {Class.forName( "com.icesoft.faces.component.outputchart.OutputChart")
                                              }) );
                 } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("panelGroup") ) {
                try { 
                m.addRule( new MethodRule("dragListener", null, new Class[] {
                        Class.forName("com.icesoft.faces.component.dragdrop.DragEvent") }) );
                m.addRule( new MethodRule("dropListener", null, new Class[] {
                        Class.forName("com.icesoft.faces.component.dragdrop.DropEvent") }) );
             } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("panelPositioned") ) {
                try {
                m.addRule( new MethodRule("beforeChangedListener", null, new Class[] {
                        Class.forName("com.icesoft.faces.component.panelpositioned.PanelPositionedEvent")}) );
                m.addRule( new MethodRule("listener", null, new Class[] {
                        Class.forName("com.icesoft.faces.component.panelpositioned.PanelPositionedEvent")}) );
         } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("panelTabSet") ) {
                try {
                    m.addRule( new MethodRule("tabChangeListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.paneltabset.TabChangeEvent") }) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("rowSelector") ) {
                try {
                    m.addRule( new MethodRule("selectionListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.ext.RowSelectorEvent") }) );
                    m.addRule( new MethodRule("selectionAction", null, new Class[0]) );
                    m.addRule( new MethodRule("clickListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.ext.ClickActionEvent") }) );
                    m.addRule( new MethodRule("clickAction", null, new Class[0]) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("panelTooltip") ) {
                try {
                    m.addRule( new MethodRule("displayListener", null, new Class[] {
                            Class.forName( "com.icesoft.faces.component.DisplayEvent" )}) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("menuPopup") ) {
                try {
                    m.addRule( new MethodRule("displayListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.DisplayEvent") }) );
                } catch (ClassNotFoundException nfe) {}
            }
            else if( tag.getLocalName().equals("selectInputText") ) {
                try {
                    m.addRule( new MethodRule("textChangeListener", null, new Class[] {
                            Class.forName("com.icesoft.faces.component.selectinputtext.TextChangeEvent") }) );
                } catch (ClassNotFoundException nfe) {}
            }
        }
        return m;
    }
}