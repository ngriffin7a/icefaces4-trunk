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

package org.icefaces.demo.scopes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

import org.icefaces.bean.ViewRetained;

@ManagedBean(name = "ViewScopedCounterViewRetained")
@ViewScoped
@ViewRetained
public class ViewScopedCounterViewRetained extends Counter implements Serializable {
    public ViewScopedCounterViewRetained() {
        System.out.println(this);
    }

    @PostConstruct
    public void created() {
        System.out.println("created >> " + this);
    }

    @PreDestroy
    public void destroyed() {
        System.out.println("destroyed >> " + this);
    }
}