/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.demo.auction.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.push.AuctionWatcher;

@ManagedBean(name=PushBean.BEAN_NAME)
@SessionScoped
public class PushBean implements Serializable {
	public static final String BEAN_NAME = "pushBean";
	
	@PostConstruct
	public void startup() {
		PushRenderer.addCurrentSession(AuctionWatcher.INTERVAL_PUSH_GROUP);
	}
	
	@PreDestroy
	public void cleanup() {
		PushRenderer.removeCurrentSession(AuctionWatcher.INTERVAL_PUSH_GROUP);
	}
}
