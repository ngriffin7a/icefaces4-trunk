package org.icefaces.demo.auction.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.icefaces.application.PushRenderer;
import org.icefaces.demo.auction.push.AuctionPushRenderer;

@ManagedBean(name=PushBean.BEAN_NAME)
@SessionScoped
public class PushBean implements Serializable {
	public static final String BEAN_NAME = "pushBean";
	
	@PostConstruct
	public void startup() {
		PushRenderer.addCurrentSession(AuctionPushRenderer.INTERVAL_PUSH_GROUP);
	}
	
	@PreDestroy
	public void cleanup() {
		PushRenderer.removeCurrentSession(AuctionPushRenderer.INTERVAL_PUSH_GROUP);
	}
}
