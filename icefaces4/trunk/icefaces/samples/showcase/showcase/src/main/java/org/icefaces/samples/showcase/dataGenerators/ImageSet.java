
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

package org.icefaces.samples.showcase.dataGenerators;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageSet implements Serializable
{
    private static final String BASE_PATH = "/resources/css/images/";
    
    public static ArrayList<ImageInfo> getImages(ImagesSelect imageSetType)
    {
        ArrayList<ImageInfo> imageLocations = new ArrayList<ImageInfo>();
        
        if(imageSetType.equals(ImagesSelect.CARS))
        {
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/bmw.png", "BMW"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/camaro.png", "Camaro"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/chevroletImpala.png", "Chevrolet Impala"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/pickupTruck.png", "Pickup Truck"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/vwBeatle.png", "VW Beatle"));
        }
        else if(imageSetType.equals(ImagesSelect.GADGETS))
        {
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/laptop.png", "Laptop"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/pda.png", "PDA"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/monitor.png", "Monitor"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/desktop.png", "Desktop"));
        }
        else if(imageSetType.equals(ImagesSelect.FOOD))
        {
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/aubergine.png", "Aubergine"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/capsicum.png", "Capsicum"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/chilli.png", "Chili"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/egg.png", "Egg"));
            imageLocations.add(new ImageInfo(BASE_PATH+"dragdrop/orange.png", "Orange"));
        }
        return imageLocations;
    }
    
    public static ImageInfo getImage(ImageSelect imageType)
    {
        ImageInfo image = null;
        if(imageType.equals(ImageSelect.PRINTER))
        {
            image = new ImageInfo(BASE_PATH+"printerIcon.png", "Printer");
        }
        else if(imageType.equals(ImageSelect.PICTURE))
        {
            image = new ImageInfo(BASE_PATH+"rainbowCalgary.png", "Calgary");
        }
        else if(imageType.equals(ImageSelect.FORWARD_ARROW))
        {
            image = new ImageInfo(BASE_PATH+"navigateForward.png", "Navigate Forward");
        }        
        else if(imageType.equals(ImageSelect.BACKWARD_ARROW))
        {
            image = new ImageInfo(BASE_PATH+"navigateBack.png", "Navigate Back");
        }
        else if(imageType.equals(ImageSelect.PLAY))
        {
            image = new ImageInfo(BASE_PATH+"player/play.png", "Play");
        }        
        else if(imageType.equals(ImageSelect.STOP))
        {
            image = new ImageInfo(BASE_PATH+"player/stop.png", "Stop");
        }        
        else if(imageType.equals(ImageSelect.PAUSE))
        {
            image = new ImageInfo(BASE_PATH+"player/pause.png", "Pause");
        }
        else if(imageType.equals(ImageSelect.LIGHTBULB_ON))
        {
            image = new ImageInfo(BASE_PATH+"lightBulb/LightBulbOn.png", "Light bulb on");
        }
        else if(imageType.equals(ImageSelect.LIGHTBULB_OFF))
        {
            image = new ImageInfo(BASE_PATH+"lightBulb/LightBulbOff.png", "Light bulb off");
        }        
        return image;
    }
    
    public static ImageInfo getRandomImage()
    {
        //get all available image sets
        ImagesSelect[] availableSets = ImagesSelect.values();
        //pick one of available sets randomly
        ImagesSelect randomValue = availableSets[(int)(Math.random()*availableSets.length)];
        //get Images from that set
        ArrayList<ImageInfo> randomSet =  getImages(randomValue);
        //Randomly pick one of the images from that set 
        return randomSet.get((int)(Math.random()*randomSet.size()));
    }
    
    public static ImageInfo getNextImage(ImageSet.ImageInfo referenceImage)
    {
        ArrayList<ImageInfo> images = getImagesFromAllSets();
        if(referenceImage == null)
        {
            return images.get(0);
        }
        else
        {
            int imageLocation = findImageLocation(referenceImage, images);
            imageLocation++;
            if(imageLocation < images.size())
                return images.get(imageLocation);
            else
                return images.get(0);
        }
    }
    
    private static ArrayList<ImageInfo> getImagesFromAllSets() 
    {
        ImagesSelect[] availableSets = ImagesSelect.values();
        ArrayList<ImageInfo> images = new ArrayList<ImageInfo>();
        for(int i =0; i<availableSets.length;i++)
        {
            ImagesSelect setType = availableSets[i];
            ArrayList<ImageInfo> setImages = getImages(setType);
            for (ImageInfo image : setImages) {
                images.add(image);
            }
        }
        return images;
    }
    private static int findImageLocation(ImageInfo referenceImage, ArrayList<ImageInfo> searchSet) 
    {
        String selectedImagePath;
        for (int i =0; i<searchSet.size(); i++) 
        {
            selectedImagePath = searchSet.get(i).getPath();
            if(selectedImagePath.equals(referenceImage.getPath()))
                return i;
        }
        //return -1 is search set is empty, otherwise return index of the first element in the list
        if(searchSet.isEmpty())
            return -1;
        else
            return 0;
    }

    


    public enum ImagesSelect {
        CARS,
        GADGETS,
        FOOD,
    }

    public enum ImageSelect {
        PRINTER,
        PICTURE,
        FORWARD_ARROW,
        BACKWARD_ARROW,
        PLAY,
        STOP,
        PAUSE,
        LIGHTBULB_ON,
        LIGHTBULB_OFF,
    }

    public static class ImageInfo implements Serializable {
        private String path;
        private String description;

        ImageInfo(String path, String description) {
            this.path = path;
            this.description = description;
        }

        public String getPath() {
            return path;
        }

        public String getDescription() {
            return description;
        }
    }
}
