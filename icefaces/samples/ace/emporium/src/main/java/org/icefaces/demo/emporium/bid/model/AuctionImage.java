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

package org.icefaces.demo.emporium.bid.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.icefaces.demo.emporium.util.FacesUtils;
import org.icefaces.demo.emporium.util.StringUtil;

@ManagedBean(name=AuctionImage.BEAN_NAME)
@ApplicationScoped
public class AuctionImage implements Serializable {
	private static final long serialVersionUID = 2422253645219479912L;
	
	public static final String BEAN_NAME = "auctionImage";
	private static final Logger log = Logger.getLogger(AuctionImage.class.getName());
	
	public static final String IMAGE_LIBRARY = "items"; // Folder that should be under web/resources/ that contains our item images
	public static final String DEFAULT_NAME = "unknown";
	public static final String EXTENSION = ".jpg";
	private static final String IMAGE_DIR = "/resources/" + IMAGE_LIBRARY;

	private String parentDir;
	private String[] cachedImagesList; // List of images (including extension)

    public boolean isValidParentDir() {
		if (parentDir == null) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			String imageDirectory = IMAGE_DIR;
			URL toReturn;
			try {
				toReturn = externalContext.getResource(imageDirectory);
			} catch (MalformedURLException e) {
				toReturn = null;
			}

			if (toReturn == null) {
				log.log(Level.SEVERE, "Desired item image directory " + IMAGE_DIR + " does not exist or isn't readable. This means all images will be " + DEFAULT_NAME + EXTENSION);
			} else {
				Set<String> imagePaths = externalContext.getResourcePaths(imageDirectory);
				log.info("Item image directory " + toReturn.getPath() + " valid with " + imagePaths.size() + " files.");
			}
			parentDir = imageDirectory;
		}

        return parentDir != null;
    }

	public static String staticConvertNameToImageName(String name) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		// First of all we won't assign any images until we know we have a valid parentDir (ideally resources/items/) to pull from
		// The image name format is all lowercase, spaces replaced with underscores
		if (StringUtil.validString(name)) {
			name = name.toLowerCase();
			if (name.contains(" ")) {
				name = name.replaceAll(" ", "_");
			}

			String imageName = new String(name);

			// Next we have to check that the image actually exists
			URL toCheck = null;
			try {
				toCheck = externalContext.getResource(IMAGE_DIR + "/" + imageName + EXTENSION);
			} catch (MalformedURLException e) {
				//ignore
			}
			if (toCheck != null) {
				return imageName;
			}
		}

		return DEFAULT_NAME;
	}

	public String convertNameToImageName(String name) {
		return staticConvertNameToImageName(name);
	}

	public String[] getImagesList() {
		if (cachedImagesList == null) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Set<String> fileNames = externalContext.getResourcePaths(IMAGE_DIR);
			if (isValidParentDir() && fileNames != null) {
				TreeSet<String> imageFileNames = new TreeSet<String>();
				for (String name : fileNames) {
					if (name.toLowerCase().endsWith(EXTENSION) && (!name.toLowerCase().startsWith(DEFAULT_NAME))) {
						imageFileNames.add(name);
					}
				}
				cachedImagesList = imageFileNames.toArray(new String[0]);
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
