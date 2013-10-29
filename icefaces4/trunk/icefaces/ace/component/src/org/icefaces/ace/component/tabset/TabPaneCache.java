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

package org.icefaces.ace.component.tabset;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.EnumSet;
import java.util.Set;

/**
 * Server side TabPane content is either not cached, or it is dynamically
 * cached, or statically cached. If it is dynamically cached, then once the
 * content has bee sent to the browser, it will continue to be rendered each
 * lifecycle, and the rendered output will be dom differenced, and only
 * updated as necessary. If it is statically cached, then it is not rendered,
 * and will not be updated.
 * The special mode static auto means that it's usually static, but under
 * certain circumstances it will automatically turn dynamic for that lifecycle
 * and then return to being static on subsequent lifecycles.
 * The special mode dynamic revert static auto is when the static auto mode
 * can't detect the need for the temporary switch, and it needs to be done
 * manually for the lifecycle.
 */
public enum TabPaneCache {
    NONE("none", false, false, null),
    DYNAMIC("dynamic", true, false, null),
    STATIC("static", true, true, null),
    STATIC_AUTO("staticAuto", true, true, null) {
        public TabPaneCache resolve(FacesContext facesContext, UIComponent tab) {
            if (TabPaneUtil.isAutoDynamic(facesContext, tab)) {
                return DYNAMIC_REVERT_STATIC_AUTO;
            }
            return this;
        }
    },
    DYNAMIC_REVERT_STATIC_AUTO("dynamicRevertStaticAuto", true, false, STATIC_AUTO);


    public static final String DEFAULT = "none";

    public static TabPaneCache get(String find) {
        if (find == null || find.trim().length() == 0) {
            find = DEFAULT;
        }
        Set<TabPaneCache> s = EnumSet.allOf(TabPaneCache.class);
        for (TabPaneCache tpc : s) {
            if (tpc.named.equals(find)) {
                return tpc;
            }
        }
        throw new IllegalArgumentException("'" + find + "' is not a valid TabPaneCache");
    }


    private String named;
    private boolean cached;
    private boolean cachedStatically;
    private TabPaneCache revertTo;

    private TabPaneCache(String named, boolean cached,
            boolean cachedStatically, TabPaneCache revertTo) {
        this.named = named;
        this.cached = cached;
        this.cachedStatically = cachedStatically;
        this.revertTo = revertTo;
    }

    public TabPaneCache resolve(FacesContext facesContext, UIComponent tab) {
        return this;
    }

    public String getNamed() {
        return named;
    }
    
    public boolean isCached() {
        return cached;
    }

    public boolean isCachedStatically() {
        return cachedStatically;
    }

    public TabPaneCache getRevertTo() {
        return revertTo;
    }
}
