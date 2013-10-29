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
