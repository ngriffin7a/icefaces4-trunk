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

package com.icesoft.faces.component.gmap;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.effects.JavascriptContext;
import org.icefaces.resources.BrowserType;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.Map;

@ICEResourceDependencies({
    @ICEResourceDependency(name = "gmap/gmap.js"),
	@ICEResourceDependency(name="icefaces-compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={}),
    @ICEResourceDependency(name="compat.js", library="ice.compat",target="head", browser=BrowserType.ALL, browserOverride={})
})
public class GMap extends UIPanel {
    public static final String COMPONENT_TYPE = "com.icesoft.faces.GMap";
    public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.GMapRenderer";
    private static final String DEFAULT_LONGITUDE = "-114.08538937568665";
    private static final String DEFAULT_LATITUDE = "51.06757388616548";
    private String longitude;
    private String latitude;
    private Integer zoomLevel;


    private Boolean locateAddress;
    private boolean initilized = false;
    private String address;
    private String type;
    private String style = null;
    private String styleClass = null;
    private String renderedOnUserRole = null;

    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public String getLongitude() {
        if (longitude != null) {
            return longitude;
        }
        ValueBinding vb = getValueBinding("longitude");
        return vb != null ? (String) vb.getValue(getFacesContext()) : DEFAULT_LONGITUDE;
    }

    public String getLatitude() {
        if (latitude != null) {
            return latitude;
        }
        ValueBinding vb = getValueBinding("latitude");
        return vb != null ? (String) vb.getValue(getFacesContext()) : DEFAULT_LATITUDE;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        //TODO need to be changed in the processUpdate. However this property is not 
        //involved in any validator or convertor so it will not harm 
        ValueBinding vb = getValueBinding("longitude");
        if (vb != null) {
            vb.setValue(getFacesContext(), longitude);
            this.longitude = null;
        }
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        //TODO need to be changed in the processUpdate. However this property is not 
        //involved in any validator or convertor so it will not harm 
        ValueBinding vb = getValueBinding("latitude");
        if (vb != null) {
            vb.setValue(getFacesContext(), latitude);
            this.latitude = null;
        }
    }

    public void decode(FacesContext facesContext) {
        Map map = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = getClientId(facesContext);
        if (map.get(clientId + "event") != null && map.get(clientId + "event")
                .toString().length() > 0) {
            if (map.containsKey(clientId + "lat")) {
                setLatitude(String.valueOf(map.get(clientId + "lat")));
            }
            if (map.containsKey(clientId + "lng")) {
                setLongitude(String.valueOf(map.get(clientId + "lng")));
            }
            if (map.containsKey(clientId + "zoom")) {
                setZoomLevel(Integer.valueOf(String.
                        valueOf(map.get(clientId + "zoom"))).intValue());
            }
            if (map.containsKey(clientId + "type")) {
                setType(String.valueOf(map.get(clientId + "type")));
            }
        }

    }

    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
        //assume it page refresh/redirect
        if (context.getExternalContext().getRequestParameterMap() != null &&
                context.getExternalContext().getRequestParameterMap().size() <= 1) {
            initilized = false;
        }
        if ((isLocateAddress() || !initilized) && (getAddress() != null
                && getAddress().length() > 2)) {
				JavascriptContext.addJavascriptCall(context,
                    "Ice.GoogleMap.locateAddress('" + getClientId(context) + "', '" +
                            getAddress() + "');");
				JavascriptContext.addJavascriptCall(context,
                        "Ice.GoogleMap.getGMapWrapper('" + getClientId(context) +
                                "').getRealGMap().setZoom(" + getZoomLevel() + ");");
							
            initilized = true;
        } else {
            if (isLocatedByGeocoder(context)) {
                JavascriptContext.addJavascriptCall(context,
                        "Ice.GoogleMap.getGMapWrapper('" + getClientId(context) +
                                "').getRealGMap().setZoom(" + getZoomLevel() + ");");
            } else {
                String latitude = getLatitude();
                String longitude = getLongitude();
                try {
                    Float.parseFloat(latitude);
                    Float.parseFloat(longitude);
                } catch (NumberFormatException e) {
                    latitude = DEFAULT_LATITUDE;
                    longitude = DEFAULT_LONGITUDE;
                }
                JavascriptContext.addJavascriptCall(context,
                        "Ice.GoogleMap.getGMapWrapper('" + getClientId(context) +
                                "').getRealGMap().setCenter(new google.maps.LatLng("+ latitude + "," + longitude + "));");
				JavascriptContext.addJavascriptCall(context,
                        "Ice.GoogleMap.getGMapWrapper('" + getClientId(context) +
                                "').getRealGMap().setZoom(" + getZoomLevel() + ");");
            }
        }
		
		JavascriptContext.addJavascriptCall(context,
                "Ice.GoogleMap.setMapType('" + getClientId(context) + "','" +
                        getType() + "');");
    }

    public int getZoomLevel() {
        if (zoomLevel != null) {
            return zoomLevel.intValue();
        }
        ValueBinding vb = getValueBinding("zoomLevel");
        return vb != null ? ((Integer) vb.getValue(getFacesContext())).intValue() : 5;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = new Integer(zoomLevel);
    }

    private boolean isLocatedByGeocoder(FacesContext context) {
        Object event = context.getExternalContext().getRequestParameterMap()
                .get(getClientId(context) + "event");
        if (event != null && "geocoder".equals(event)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLocateAddress() {
        if (locateAddress != null) {
            return locateAddress.booleanValue();
        }
        ValueBinding vb = getValueBinding("locateAddress");
        return vb != null ?
                ((Boolean) vb.getValue(getFacesContext())).booleanValue() :
                false;
    }

    public void setLocateAddress(boolean locateAddress) {
        this.locateAddress = new Boolean(locateAddress);
    }

    public String getAddress() {
        if (address != null) {
            return address;
        }
        ValueBinding vb = getValueBinding("address");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        if (type != null) {
            return type;
        }
        ValueBinding vb = getValueBinding("type");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "Map";
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }

    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this,
                styleClass,
                CSS_DEFAULT.GMAP,
                "styleClass");
    }

    public String getMapTdStyleClass() {
        return Util.getQualifiedStyleClass(this,
                CSS_DEFAULT.GMAP_MAP_TD);
    }

    public String getTxtTdStyleClass() {
        return Util.getQualifiedStyleClass(this,
                CSS_DEFAULT.GMAP_TXT_TD);
    }

    private transient Object values[];

    public void restoreState(FacesContext context, Object state) {
        values = (Object[]) state;
        super.restoreState(context, values[0]);
        renderedOnUserRole = (String) values[1];
        longitude = (String) values[2];
        styleClass = (String) values[3];
        zoomLevel = (Integer) values[4];
        type = (String) values[5];
        style = (String) values[6];
        latitude = (String) values[7];
        address = (String) values[8];
        locateAddress = (Boolean) values[9];
        initilized = ((Boolean) values[10]).booleanValue();
    }

    public Object saveState(FacesContext context) {
        if (values == null) {
            values = new Object[11];
        }
        values[0] = super.saveState(context);
        values[1] = renderedOnUserRole;
        values[2] = longitude;
        values[3] = styleClass;
        values[4] = zoomLevel;
        values[5] = type;
        values[6] = style;
        values[7] = latitude;
        values[8] = address;
        values[9] = locateAddress;
        values[10] = initilized ? Boolean.TRUE : Boolean.FALSE;
        return values;
    }
}

