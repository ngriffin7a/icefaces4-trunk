package org.icefaces.demo.emporium.auth;

import java.io.File;
import java.io.Serializable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.demo.emporium.bid.AuctionService;
import org.icefaces.demo.emporium.chat.ChatService;
import org.icefaces.demo.emporium.message.GlobalMessageBean;
import org.icefaces.demo.emporium.settings.SettingsBean;
import org.icefaces.demo.emporium.test.TestFlags;
import org.icefaces.demo.emporium.user.UserBean;
import org.icefaces.demo.emporium.util.FacesUtils;
import org.icefaces.demo.emporium.util.StringUtil;

@ManagedBean(name=AuthController.BEAN_NAME,eager=true)
@ApplicationScoped
public class AuthController implements Serializable {
	public static final String BEAN_NAME = "authController";
	private static final Logger log = Logger.getLogger(AuthController.class.getName());
	
	private static final String PASSWORD_FILE_PARAM = "org.icefaces.demo.emporium.auth.passwordFile";
	
	private boolean allowAuth = determineStatus();
	private String correctPassword;
	
	public boolean isAllowAuth() {
		return allowAuth;
	}

	public void setAllowAuth(boolean allowAuth) {
		this.allowAuth = allowAuth;
	}

	private boolean determineStatus() {
		// Figure out if we can allow authentication, which means having a login button on the About tab and an authentication dialog displayable
		// Obviously if our TestFlags disallows it, we comply
		if (TestFlags.TEST_NO_AUTH) {
			return false;
		}
		
		// Otherwise check for a password file that exists
		// We need to separate the password from the source code so we can safely distribute the code without exposing any security risks
		// So if this file isn't present (such as on a test or local deploy) we don't allow auth at all
		String passwordFilePath = FacesUtils.getFacesParameter(PASSWORD_FILE_PARAM);
		
		if (StringUtil.validString(passwordFilePath)) {
			try{
				File testFile = new File(passwordFilePath);
				
				if ((testFile.exists()) && (testFile.canRead()) && (testFile.isFile())) {
					correctPassword = new Scanner(testFile).useDelimiter("\\Z").next();
					
					if (StringUtil.validString(correctPassword)) {
						log.info("Successfully learned Auth password from file " + testFile.getAbsolutePath() + ", allowing Auth.");
						
						return true;
					}
					else {
						log.log(Level.WARNING, "Failed to learn Auth password from valid file " + testFile.getAbsolutePath() + ", disallowing Auth.");
					}
				}
				else {
					log.log(Level.WARNING, "Failed to find or validate Auth password file from path: " + passwordFilePath + ", disallowing Auth.");
				}
			}catch (Exception failedFile) {
				log.log(Level.SEVERE, "Failed to find or validate Auth password file from path: " + passwordFilePath + ", disallowing Auth.", failedFile);
			}
		}
		
		return false;
	}
	
	public void submitAuth(ActionEvent event) {
		AuthBean authBean = (AuthBean)FacesUtils.getManagedBean(AuthBean.BEAN_NAME);
		
		// Start by clearing any old authentication
		cancelAuth(event);
		
		if (StringUtil.validString(authBean.getPassword())) {
			if (authBean.getPassword().equals(correctPassword)) {
				UserBean user = (UserBean)FacesUtils.getManagedBean(UserBean.BEAN_NAME);
				user.setAuthenticated(true);
				
				// Clear our entered password
				authBean.clearPassword();
			}
			else {
				// Have pretty harsh lockout for failed attempts, so users gets one chance to Auth
				UserBean user = (UserBean)FacesUtils.getManagedBean(UserBean.BEAN_NAME);
				user.setLocked(true);
				
				SettingsBean settings = (SettingsBean)FacesUtils.getManagedBean(SettingsBean.BEAN_NAME);
				log.info("User " + settings.getName() + " unsuccessfully attempted Authentication, locking.");
			}
		}
	}
	
	public void cancelAuth(ActionEvent event) {
		UserBean user = (UserBean)FacesUtils.getManagedBean(UserBean.BEAN_NAME);
		user.setAuthenticated(false);
	}
	
	public void resetAuctions(ActionEvent event) {
		AuctionService service = (AuctionService)FacesUtils.getManagedBean(AuctionService.BEAN_NAME);
		service.generateDefaultData();
		
		FacesUtils.addGlobalInfoMessage("Successfully cleared and regenerated auction list items.");
	}
	
	public void resetLogs(ActionEvent event) {
		GlobalMessageBean bean = (GlobalMessageBean)FacesUtils.getManagedBean(GlobalMessageBean.BEAN_NAME);
		bean.resetLog();
		
		FacesUtils.addGlobalInfoMessage("Successfully cleared entire auction log.");
	}
	
	public void resetChat(ActionEvent event) {
		ChatService service = (ChatService)FacesUtils.getManagedBean(ChatService.BEAN_NAME);
		service.resetChatMessages();
		
		FacesUtils.addGlobalInfoMessage("Successfully removed all chat messages in each room.");
	}
}
