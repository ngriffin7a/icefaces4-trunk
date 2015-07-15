/*
 * Copyright 2004-2015 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.emporium.push;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.application.PushRenderer;
import org.icefaces.demo.emporium.watcher.AuctionWatcher;

@ManagedBean(name=PushBean.BEAN_NAME)
@ViewScoped
public class PushBean implements Serializable {
	private static final long serialVersionUID = 431936667425122754L;
	
	public static final String BEAN_NAME = "pushBean";
	
	@PostConstruct
	public void initPushBean() {
		joinIntervalGroup();
	}
	
	public void joinIntervalGroup() {
		try{
			PushRenderer.removeCurrentView(AuctionWatcher.MANUAL_PUSH_GROUP);
			PushRenderer.addCurrentView(AuctionWatcher.INTERVAL_PUSH_GROUP);
		}catch (NullPointerException failed) { }
	}
	
	public void leaveIntervalGroup() {
		try{
			PushRenderer.removeCurrentView(AuctionWatcher.INTERVAL_PUSH_GROUP);
			PushRenderer.addCurrentView(AuctionWatcher.MANUAL_PUSH_GROUP);
		}catch (NullPointerException failed) { }
	}
	
	public String getInit() {
		return null;
	}
}
