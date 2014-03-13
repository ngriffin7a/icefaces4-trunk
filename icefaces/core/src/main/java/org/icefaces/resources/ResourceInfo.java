/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.resources;

/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 1/25/13
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceInfo {
    public String library = "";
    public String target = "";
    public String name = "";

    public ResourceInfo(ICEResourceDependency inputDep, ICEResourceLibrary lib) {
        if (lib != null && blank(inputDep.library()))
            library = lib.value();
        else
            library = inputDep.library();

        target = inputDep.target();
        name = inputDep.name();
    }

    public ResourceInfo(ICEBrowserDependency override, ICEResourceLibrary lib) {
        if (lib != null && blank(override.library()))
            library = lib.value();
        else
            library = override.library();

        target = override.target();
        name = override.name();
    }

    public ResourceInfo() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceInfo that = (ResourceInfo) o;

        if (library != null ? !library.equals(that.library) : that.library != null) return false;
        if (!name.equals(that.name)) return false;
        if (target != null ? !target.equals(that.target) : that.target != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = library != null ? library.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + name.hashCode();
        return result;
    }

    private boolean blank(String s) {
        if (s == null || s.equals("")) return true;
        else return false;
    }


}
