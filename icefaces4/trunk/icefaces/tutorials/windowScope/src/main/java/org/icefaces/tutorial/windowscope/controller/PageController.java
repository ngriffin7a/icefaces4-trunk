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

package org.icefaces.tutorial.windowscope.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="pageController")
@SessionScoped
public class PageController implements Serializable {
	public String navigatePage1() {
		System.out.println("Redirect to Page 1");
		
		return "page1";
	}
	
	public String navigatePage2() {
		System.out.println("Redirect to Page 2");
		
		return "page2";
	}
	
	public String action() {
		System.out.println("Action Fired");
		
		return null;
	}
	
	public void actionListener(ActionEvent event) {
		System.out.println("ActionListener Fired");
	}
}
