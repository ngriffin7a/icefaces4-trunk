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

package org.icefaces.demo.messages;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import javax.annotation.PostConstruct;

@ManagedBean
@RequestScoped
public class MessageBean {

    @PostConstruct
    public void constructionMessage(){
        System.out.println("MessageBean.constructionMessage: CALLED");
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Message added via PostConstruct: " + ctx,
                                        "Message added via PostConstruct: " + ctx));
    }

    public void addMessages(ActionEvent ae) {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent c = UIComponent.getCurrentComponent(ctx);
        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Message added via actionListener: " + c.getClientId(ctx),
                                        "Message added via actionListener: " + c.getClientId(ctx)));
    }

}
