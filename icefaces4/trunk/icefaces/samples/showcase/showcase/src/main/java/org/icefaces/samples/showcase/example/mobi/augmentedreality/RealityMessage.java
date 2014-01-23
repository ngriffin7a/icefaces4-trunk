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

package org.icefaces.samples.showcase.example.mobi.augmentedreality;

import java.io.Serializable;

import java.util.logging.Level;
import java.util.logging.Logger;


public class RealityMessage implements Serializable  {

    private String title;
    private double latitude;
    private double longitude;
    private String selection;
    private String fileName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("properties ");
        sb.append("title=");
        sb.append("'").append(title).append("', ");
        sb.append("location=");
        sb.append("'").append(latitude).append(", ");
        sb.append(longitude).append("' ");
        return sb.toString();
    }
}
