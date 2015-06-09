package org.icefaces.demo.emporium.auth;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name=AuthBean.BEAN_NAME)
@ViewScoped
public class AuthBean implements Serializable {
	public static final String BEAN_NAME = "authBean";
	
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void clearPassword() {
		setPassword(null);
	}
}
