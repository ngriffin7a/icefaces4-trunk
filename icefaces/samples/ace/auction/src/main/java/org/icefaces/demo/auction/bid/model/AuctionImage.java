package org.icefaces.demo.auction.bid.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.demo.auction.util.FacesUtils;
import org.icefaces.demo.auction.util.StringUtil;

@ManagedBean(name=AuctionImage.BEAN_NAME)
@ApplicationScoped
public class AuctionImage implements Serializable {
	private static final long serialVersionUID = 2422253645219479912L;
	
	public static final String BEAN_NAME = "auctionImage";
	private static final Logger log = Logger.getLogger(AuctionImage.class.getName());
	
	public static final String IMAGE_LIBRARY = "items"; // Folder that should be under web/resources/ that contains our item images
	public static final String DEFAULT_NAME = "unknown";
	public static final String EXTENSION = ".jpg";
	
	private File parentDir;
	private String[] cachedImagesList; // List of images (including extension)
	
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
	
	public boolean isValidParentDir() {
		return parentDir != null;
	}
	
	public String convertNameToImageName(String name) {
		// First of all we won't assign any images until we know we have a valid parentDir (ideally resources/items/) to pull from
		if (isValidParentDir()) {
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
	
	public String[] getImagesList() {
		if (cachedImagesList == null) {
			if (isValidParentDir()) {
				cachedImagesList = parentDir.list(new FilenameFilter() {
					@Override
	                public boolean accept(File dir, String name) {
	                    return name.toLowerCase().endsWith(EXTENSION) && (!name.toLowerCase().startsWith(DEFAULT_NAME));
	                }
				});
			}
		}
		return cachedImagesList;
	}
	
	public int getNumberOfImages() {
		if (getImagesList() != null) {
			return cachedImagesList.length;
		}
		return 0;
	}
	
	public String getImageLibrary() {
		return IMAGE_LIBRARY;
	}
	
	public String getDefaultName() {
		return DEFAULT_NAME;
	}
	
	public String getExtension() {
		return EXTENSION;
	}
}
