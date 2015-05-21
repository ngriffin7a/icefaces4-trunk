package org.icefaces.demo.auction.bid.model;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.auction.util.FacesUtils;
import org.icefaces.demo.auction.util.StringUtil;

@ManagedBean(name=AuctionImage.BEAN_NAME)
@ApplicationScoped
public class AuctionImage {
	public static final String BEAN_NAME = "auctionImage";
	private static final Logger log = Logger.getLogger(AuctionImage.class.getName());
	
	public static final String IMAGE_LIBRARY = "items"; // Folder that should be under web/resources/ that contains our item images
	public static final String DEFAULT_NAME = "unknown";
	public static final String EXTENSION = ".jpg";
	
	private File parentDir;
	
	public AuctionImage() {
		initParentDir();
	}
	
	private void initParentDir() {
		parentDir = new File(FacesUtils.getResourcesDirectory(), IMAGE_LIBRARY);
		
		if ((parentDir == null) || (!parentDir.exists()) || (!parentDir.isDirectory()) || (!parentDir.canRead())) {
			log.log(Level.SEVERE, "Desired item image directory " + parentDir + " does not exist or isn't readable. This means all images will be " + DEFAULT_NAME + EXTENSION);
			
			parentDir = null;
		}
		else {
			log.info("Item image directory " + parentDir.getAbsolutePath() + " valid with " + parentDir.list().length + " files.");
		}
	}
	
	public String convertNameToImageName(String name) {
		// First of all we won't assign any images until we know we have a valid parentDir (ideally resources/items/) to pull from
		if (parentDir != null) {
			// The image name format is all lowercase, spaces replaced with underscores
			if (StringUtil.validString(name)) {
				name = name.toLowerCase();
				if (name.contains(" ")) {
					name = name.replaceAll(" ", "_");
				}
				
				String imageName = new String(name);
				
				// Next we have to check that the image actually exists
				File toCheck = new File(parentDir, imageName + EXTENSION);
				if ((toCheck != null) && (toCheck.exists()) && (toCheck.isFile()) && (toCheck.canRead())) {
					return imageName;
				}
			}
		}
		return DEFAULT_NAME;
	}
	
	public String getImageLibrary() {
		return IMAGE_LIBRARY;
	}
}
