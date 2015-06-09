package org.icefaces.demo.emporium.user;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name=UserBean.BEAN_NAME)
@SessionScoped
public class UserBean implements Serializable {
	public static final String BEAN_NAME = "userBean";
	
	@ManagedProperty(value="#{" + UserCounter.BEAN_NAME + "}")
	private UserCounter counter;
	
	@PostConstruct
	private void initUserBean() {
		if (counter != null) {
			counter.countUser();
		}
	}
	
	@PreDestroy
	private void cleanupUserBean() {
		if (counter != null) {
			counter.cleanupUser();
		}
	}
	
	public UserCounter getCounter() {
		return counter;
	}

	public void setCounter(UserCounter counter) {
		this.counter = counter;
	}

	public String getInit() {
		return null;
	}
}
